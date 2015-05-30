package com.github.jrgen.typebuilder;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

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
	}

	@Override
	public Object build(JavaType javaType) {
		AbstractTypeHandler abstractTypeHandler = 
				jrgenContext.getAbstractTypeHandler();
		
		javaType = abstractTypeHandler
				.findAbstractTypeDefaultJavaType(javaType);	
		
		Set<String> ignoreProperties = jrgenContext
				.getTransientPropertyHandler()
				.getProperties(javaType);
		
		Map<String, Object> propertiesMap = new HashMap<String, Object>();
		
		//Find configuration object...Resolve on the object type level first.
		resolveByObjectConfig(javaType, propertiesMap, ignoreProperties);
		
		//Find the description of the javaType. The description contains all
		//the properties for the type we are trying to resolve.
		Map<String, JavaType> description = descriptor.describe(javaType);
		
		if (MapUtils.isEmpty(description)) {
			return null;
		}

		//If top-level configuration has undefined fields, use field level
		//configuration.
		resolveByFieldConfig(description, propertiesMap);
		
		//If there are fields that cannot be resolved through configuration
		//files (or lack thereof) try pass the field type to the workflow.
		resolveByWorkflow(description, propertiesMap, ignoreProperties);
		
		//If fields are undefined in configurations, generate the values
		//if this setting is turned on.
		generateDefaultsForUndefined(description, propertiesMap);		
		
		return MapUtils.isEmpty(propertiesMap) ? null : abstractTypeHandler
				.getObjectMapper().convertValue(propertiesMap, javaType);
	}
	
	private void resolveByObjectConfig (JavaType javaType, 
			Map<String, Object> propertiesMap,
			Set<String> ignoreProperties) {
		
		Configuration config = jrgenContext.getTypeMap().get(javaType);
		
		if (config == null || MapUtils.isEmpty(config.getData())) {
			return;
		} 				
		
		for (Entry<String, List<Object>> configDataEntry : 
			config.getData().entrySet()) {
			
			String fieldName = configDataEntry.getKey();
			
			if (!isPropertyIgnored(fieldName, ignoreProperties)) {
				List<Object> fieldData = configDataEntry.getValue();
				propertiesMap.put(fieldName, 
						fieldData.get(random.nextInt(fieldData.size())));				
			}
		}
	}
	
	private void resolveByFieldConfig (Map<String, JavaType> description,
			Map<String, Object> propertiesMap) {
		
		for (Entry<String, JavaType> d : description.entrySet()) {
			String fieldName = d.getKey();			
			JavaType fieldJavaType = d.getValue();
			
			Set<String> ignoreProperties = jrgenContext
					.getTransientPropertyHandler()
					.getProperties(fieldJavaType);
			
			if (!propertiesMap.containsKey(fieldName)) {
				Map<String, Object> fieldPropertiesMap = 
						new HashMap<String, Object>();
				resolveByObjectConfig(fieldJavaType, fieldPropertiesMap, 
						ignoreProperties);
				
				if (!MapUtils.isEmpty(fieldPropertiesMap)) {	
					propertiesMap.put(fieldName, fieldPropertiesMap);
				}
			}
		}
	}
	
	private void resolveByWorkflow (Map<String, JavaType> description,
			Map<String, Object> propertiesMap, 
			Set<String> ignoreProperties) {
		
		for (Entry<String, JavaType> d : description.entrySet()) {
			JavaType fieldJavaType = d.getValue();
			String fieldName = d.getKey();		
			
			if (!isPropertyIgnored(fieldName, ignoreProperties)) {
				if (!propertiesMap.containsKey(fieldName)) {
					Object tempValue = jrgenContext.getWorkflow()
							.workflow(fieldJavaType);
					
					propertiesMap.put(fieldName, tempValue);
				}
			}
		}
	}
	
	private void resolveUndefined (JavaType fieldJavaType, 
			Map<String, Object> tempField,
			Set<String> ignoreProperties) {
		Map<String, JavaType> fieldDescr = 
				descriptor.describe(fieldJavaType);
		
		if (MapUtils.isEmpty(fieldDescr)) {
			return;
		}		
		
		Collection<?> diffKeys = CollectionUtils.subtract(fieldDescr.keySet(), 
				tempField.keySet());
		
		for (Object keyObj : diffKeys) {
			if (!isPropertyIgnored(keyObj.toString(), ignoreProperties)) {
				JavaType keyJavaType = fieldDescr.get(keyObj);
				
				Object tempValue = jrgenContext
						.getWorkflow().workflow(keyJavaType);
				
				tempField.put(keyObj.toString(), tempValue);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private void generateDefaultsForUndefined (
			Map<String, JavaType> description,
			Map<String, Object> propertiesMap) {
		
		if (!jrgenContext.getSettings().isGenerateUndefined()) {
			return;
		}
		
		for (Entry<String, Object> entry : propertiesMap.entrySet()) {
			JavaType fieldJavaType = description.get(entry.getKey());
			
			Set<String> ignoreProperties = jrgenContext
					.getTransientPropertyHandler()
					.getProperties(fieldJavaType);
			
			if (entry.getValue() instanceof Map) {
				resolveUndefined(fieldJavaType, 
						(Map<String, Object>)entry.getValue(), 
						ignoreProperties);
			}
		}
	}
	
	private boolean isPropertyIgnored (String fieldName, 
			Set<String> ignoreProperties) {
		return ignoreProperties != null && 
				ignoreProperties.contains(fieldName);
	}
}
