package com.github.jrgen.descriptor;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * <p>
 * The Descriptor class "describes" a Java class. A description is simply a
 * {@link Map} with {@link String} as its keys and {@link JavaType}s as its 
 * value. 
 * </p>
 * 
 * <p>
 * The {@link String} keys are actually the display name of each property in
 * the class. For example setId/getId would just be "id". The corresponding 
 * values, are the property's type wrapped in a {@link JavaType}. 
 * {@link JavaType}s preserve parameterized types suchs a Collections. See 
 * the Jackson documentation for further details.
 * </p>
 * 
 * <p>
 * A valid "description" of a property is only returned if and only if the 
 * property contains both a setter and getter method. Any properties that
 * do not meet this criteria, will be omitted.
 * </p>
 * 
 * <p>
 * If a property do not have at least one conforming property, a null value
 * will be returned by the describe() method.
 * </p>
 * 
 * @author Allan J. Shoulders
 * @version 1.0
 * @since 1.0.0
 *
 */
public class Descriptor {
	
	private static final TypeFactory typeFactory = 
			TypeFactory.defaultInstance();
	
	
	/**
	 * Default no argument constructor.
	 */
	public Descriptor() {
		super();
	}
	
	/***
	 * Same as the describe method that accepts a {@link Class}, only this 
	 * overloaded method accepts a {@link JavaType}.
	 * 
	 * @param javaType {@link JavaType} containing the class information needed
	 * to create a "description".
	 * @return a "description" Map with property names and associated type
	 * information.
	 */
	public Map<String, JavaType> describe (JavaType javaType) {
		return describe(javaType.getRawClass());
	}
	
	/***
	 * Same as the describe method that accepts a {@link Class}, only this 
	 * overloaded method accepts an instance of some class type.
	 * 
	 * @param <T> the parameterized type.
	 * @param classObj the class object that will be described.
	 * @return a "description" Map with property names and associated type
	 * information.
	 */	
	public <T> Map<String, JavaType> describe (T classObj) {
		return this.describe(classObj.getClass());
	}
	
	/***
	 * The describe method accepts a Class object of a specific type and
	 * attempts to get Map to which is keys are the property names and 
	 * the values are JavaTypes containing type information about that
	 * property.
	 * 
	 * @param <T> the parameterized type.
	 * @param classObj the class type that will be described.
	 * @return a "description" Map with property names and associated type
	 * information.
	 */
	public <T> Map<String, JavaType> describe (Class<T> classObj) {
		PropertyDescriptor[] propertyDescriptors = 
				PropertyUtils.getPropertyDescriptors(classObj);
		
		Map<String, JavaType> description = 
				new LinkedHashMap<String, JavaType>();
		
		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			Method readMethod = propertyDescriptor.getReadMethod();
			
			if (propertyDescriptor.getWriteMethod() != null && 
					readMethod != null) {
				String property = propertyDescriptor.getDisplayName();
				
				Type t = readMethod.getGenericReturnType();
				JavaType finalJavaType;
				
				if (t instanceof ParameterizedType) {
					JavaType[] javaTypes = toJavaTypes((ParameterizedType)t);
					finalJavaType = typeFactory.constructParametrizedType(
							readMethod.getReturnType(), 
							readMethod.getReturnType(), 
							javaTypes);
				} else {
					finalJavaType = typeFactory.constructType(t);
				}
				
				description.put(property, finalJavaType);
			}
		}
		
		return description.isEmpty() ? null : description;
	}
	
	/**
	 * Helper method that takes a standard ParameterizedType and converts it to
	 * an array of JavaTypes. This method's purpose is for code clarity.
	 * 
	 * @param parameterizedType
	 * @return
	 */
	private JavaType[] toJavaTypes (ParameterizedType parameterizedType) {
		Type[] types = parameterizedType.getActualTypeArguments();
		JavaType[] javaTypes = new JavaType[types.length];
		
		for (int i=0; i < types.length; i++) {
			javaTypes[i] = typeFactory.constructType(types[i]);
		}
		
		return javaTypes;
	}
}
