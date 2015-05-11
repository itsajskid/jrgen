package com.github.jrgen.typebuilder;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jrgen.configuration.Configuration;
import com.github.jrgen.context.JrgenContext;
import com.github.jrgen.descriptor.Descriptor;

/**
 * <p>
 * The ImmutableTypeBuilder class is an implementation of the 
 * {@link TypeBuilder} interface. This implementation specifically handles 
 * {@link JavaType}s that encapsulate types that are immutable. From the 
 * point of view of Jrgen, an immutable class is one that cannot be described 
 * through the {@link Descriptor} class.
 * </p>
 * 
 * <p>
 * Unlike other {@link TypeBuilder} implementations, the ImmutableTypeBuilder 
 * requires a {@link Configuration}. If one is not present for the immutable 
 * class attempting to be built, the build method will return a null value.
 * </p>
 * 
 * @author Allan J. Shoulders
 * @version 1.0
 * @since 1.0.0
 * @see TypeBuilder
 *
 */
public class ImmutableTypeBuilder implements TypeBuilder<Object> {
	
	private static final Log log = LogFactory.getLog(ImmutableTypeBuilder.class);
	private static final String LOG_EXCEPTION_MSG = 
			"Exception thrown during conversion. See below:";
	
	private JrgenContext jrgenContext;
	private Random random;
	
	/**
	 * Constructs a new instance of the ImmutableTypeBuilder class using the 
	 * supplied {@link JrgenContext}.
	 * 
	 * @param jrgenContext an instance of a {@link JrgenContext}.
	 */
	public ImmutableTypeBuilder(JrgenContext jrgenContext) {
		this.jrgenContext = jrgenContext;
		random = new Random();
	}
	
	@Override
	public Object build(JavaType javaType) {
		Configuration config = jrgenContext.getTypeMap().get(javaType);
		
		if (config == null) {
			return null;
		}
		
		Object[] objects = configDataToArray(config.getData());
		
		return ArrayUtils.isEmpty(objects) ? 
				null : convertArrayToObject(objects, javaType);
	}
	
	private Object[] configDataToArray(Map<String, List<Object>> data) {
		
		if (MapUtils.isEmpty(data)) {
			return null;
		}
		
		Object[] objects = new Object[data.size()];
		
		for (Entry<String, List<Object>> entry : data.entrySet()) {
			List<Object> objList = entry.getValue();
			
			if (!CollectionUtils.isEmpty(objList)) {
				Object value = objList.get(random.nextInt(objList.size()));
				Integer idx = Integer.valueOf(entry.getKey());
				objects[idx] = value;
			}
		}		
		
		return ArrayUtils.isEmpty(objects) ?  null : objects;
	}
	
	private Object convertArrayToObject (Object[] objects, JavaType javaType) {
		ObjectMapper mapper = jrgenContext.getAbstractTypeHandler()
				.getObjectMapper();
		
		Constructor<?>[] constructors = 
				javaType.getRawClass().getConstructors();
		
		for (Constructor<?> c : constructors) {
			Class<?>[] paramTypes = c.getParameterTypes();
			
			if (paramTypes.length == objects.length) {
				Object[] params = new Object[paramTypes.length];
				
				try {
					for (int i=0; i < objects.length; i++) {
						params[i] = 
								mapper.convertValue(objects[i], paramTypes[i]);
					}
					
					return c.newInstance(params);
				} catch (IllegalArgumentException e) {
					log.debug(LOG_EXCEPTION_MSG, e);
					continue;
				} catch (InstantiationException e) {
					log.debug(LOG_EXCEPTION_MSG, e);
					continue;
				} catch (IllegalAccessException e) {
					log.debug(LOG_EXCEPTION_MSG, e);
					continue;
				} catch (InvocationTargetException e) {
					log.debug(LOG_EXCEPTION_MSG, e);
					continue;
				}
			}
			
		}
		
		return null;
	}

	@Override
	public String toString() {
		return "ImmutableTypeBuilder [jrgenContext=" + jrgenContext + "]";
	}

}
