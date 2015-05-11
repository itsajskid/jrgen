package com.github.jrgen.typebuilder;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import com.fasterxml.jackson.databind.JavaType;
import com.github.jrgen.configuration.Configuration;
import com.github.jrgen.context.JrgenContext;
import com.github.jrgen.descriptor.Descriptor;
import com.github.jrgen.typehandler.AbstractTypeHandler;
import com.github.jrgen.workflow.DefaultWorkflow;

/**
 * <p>
 * The ConfigurationBasedTypeBuilder is an implementation of the 
 * {@link TypeBuilder} interface. This implementation returns an 
 * {@link Object} as it is meant to handle several types. For this reason, 
 * it is place near the bottom of the {@link DefaultWorkflow} instance used
 * by the {@link JrgenContext}.
 * </p>
 * 
 * <p>
 * This class in particular builds objects using the following algorithm: 
 * </p>
 * <ol>
 * <li>Find the {@link Configuration} object matching the class encapsulated 
 * by the {@link JavaType}. If this exists, the fields belonging to the 
 * encapsulated type are populated with a random value from the data field of 
 * the {@link Configuration} object.
 * </li>
 * <li>If there are fields that were not defined in the {@link Configuration} 
 * object in step 1, then find {@link Configuration} objects matching the 
 * types for the missing fields. If {@link Configuration} objects are found 
 * for the missing fields, populate those missing fields using the data field 
 * for the matching {@link Configuration} object.
 * </li>
 * <li>If turned on by the developer, generate any missing fields that could
 * not be resolve in steps 1 and 2.
 * </li>
 * </ol>
 * 
 * @author Allan J. Shoulders
 * @version 1.0
 * @since 1.0.0
 * @see TypeBuilder
 *
 */
public class ConfigurationBasedTypeBuilder implements TypeBuilder<Object> {
	
	private JrgenContext jrgenContext;
	private Random random;	
	private Descriptor descriptor;
	private AbstractTypeHandler abstractTypeHandler;
	
	/**
	 * Constructs a new instance of the ConfigurationBasedTypeBuilder class 
	 * using the supplied {@link JrgenContext}.
	 * 
	 * @param jrgenContext an instance of a {@link JrgenContext}.
	 */
	public ConfigurationBasedTypeBuilder(JrgenContext jrgenContext) {
		this.jrgenContext = jrgenContext;
		random = new Random();
		descriptor = new Descriptor();
		abstractTypeHandler = jrgenContext.getAbstractTypeHandler();
	}

	@Override
	public Object build(JavaType javaType) {
		Map<String, Object> tempObj = new HashMap<String, Object>();
		
		javaType = abstractTypeHandler
				.findAbstractTypeDefaultJavaType(javaType);
		
		//Find configuration object...Resolve on the object type level first.
		Configuration objConfig = jrgenContext.getTypeMap().get(javaType);
		
		if (objConfig != null) {
			tempObj = resolveByObjectConfig(objConfig);
		}
		
		Map<String, JavaType> description = descriptor.describe(javaType);
		
		if (MapUtils.isEmpty(description)) {
			return null;
		}

		//If top-level configuration has undefined fields, use field level
		//configuration
		resolveByFieldConfig(description, tempObj);
		
		//If there are fields that cannot be resolved through configuration
		//files (or lack thereof) try pass the field type to the workflow.
		resolveByWorkflow(description, tempObj);
		
		//If fields are undefined in configurations, generate the values
		//if this setting is turned on.
		generateDefaultsForUndefined(description, tempObj);		
		
		return MapUtils.isEmpty(tempObj) ? null : abstractTypeHandler
				.getObjectMapper().convertValue(tempObj, javaType);
	}
	
	private Map<String, Object> resolveByObjectConfig (Configuration config) {
		Map<String, Object> tempObj = new HashMap<String, Object>();
		Map<String, List<Object>> configData = config.getData();
		
		if (!MapUtils.isEmpty(configData)) {
			for (Entry<String, List<Object>> configDataEntry : 
				configData.entrySet()) {
				
				String fieldName = configDataEntry.getKey();
				List<Object> fieldData = configDataEntry.getValue();
				
				tempObj.put(fieldName, 
						fieldData.get(random.nextInt(fieldData.size())));
			}
		} 
		
		return tempObj;
	}
	
	private void resolveByFieldConfig (Map<String, JavaType> description,
			Map<String, Object> tempObj) {
		if (MapUtils.isEmpty(description)) {
			return;
		}
		
		for (Entry<String, JavaType> d : description.entrySet()) {
			JavaType fieldJavaType = d.getValue();
			String fieldName = d.getKey();
			
			Configuration fieldTypeConfig = jrgenContext
					.getTypeMap()
					.get(fieldJavaType);			
			
			if (!tempObj.containsKey(fieldName) && fieldTypeConfig != null) {
				Map<String, Object> fieldResult = 
						resolveByObjectConfig(fieldTypeConfig);
				
				if (!MapUtils.isEmpty(fieldResult)) {	
					tempObj.put(fieldName, fieldResult);
				}
			}
		}
	}
	
	private void resolveByWorkflow (Map<String, JavaType> description,
			Map<String, Object> tempObj) {
		if (MapUtils.isEmpty(description) || 
				!jrgenContext.getSettings().isGenerateUndefined()) {
			return;
		}
		
		for (Entry<String, JavaType> d : description.entrySet()) {
			JavaType fieldJavaType = d.getValue();
			String fieldName = d.getKey();
			
			if (!tempObj.containsKey(fieldName)) {
				Object tempValue = jrgenContext.getWorkflow()
						.workflow(fieldJavaType);
				
				tempObj.put(fieldName, tempValue);
			}
		}
	}
	
	private void resolveUndefined (JavaType fieldJavaType, Map<String, 
			Object> tempField) {
		Map<String, JavaType> fieldDescr = 
				descriptor.describe(fieldJavaType);
		
		if (MapUtils.isEmpty(fieldDescr)) {
			return;
		}		
		
		Collection<?> diffKeys = CollectionUtils.subtract(fieldDescr.keySet(), 
				tempField.keySet());
		
		for (Object keyObj : diffKeys) {
			JavaType keyJavaType = fieldDescr.get(keyObj);
			
			Object tempValue = jrgenContext
					.getWorkflow().workflow(keyJavaType);
			
			tempField.put(keyObj.toString(), tempValue);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void generateDefaultsForUndefined (Map<String, JavaType> description,
			Map<String, Object> tempObj) {
		
		if (MapUtils.isEmpty(description) || 
				!jrgenContext.getSettings().isGenerateUndefined()) {
			return;
		}
		
		for (Entry<String, Object> entry : tempObj.entrySet()) {
			JavaType fieldJavaType = description.get(entry.getKey());
			
			if (entry.getValue() instanceof Map) {
				resolveUndefined(fieldJavaType, 
						(Map<String, Object>)entry.getValue());
			}
		}
	}
	
}
