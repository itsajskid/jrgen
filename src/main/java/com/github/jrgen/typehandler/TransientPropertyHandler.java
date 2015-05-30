package com.github.jrgen.typehandler;

import static com.github.jrgen.util.JrgenUtil.validateNonNullArgument;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

/***
 * <p>
 * This class manages a set of properties associated with a class type that 
 * should be ignored when the associated class type is called upon to be 
 * generated.
 * </p>
 * 
 * <p>
 * The {@link ObjectMapper} class provides a means for the developer to ignore 
 * properties when a conversion takes place. However, that facility either 
 * requires the use of annotations, or use of utility classes not easily 
 * accessed. This class provides an alternative, simple approach.
 * </p>
 * 
 * @author Allan J. Shoulders
 * @since 1.1.0
 * @version 1.0
 *
 */
public class TransientPropertyHandler {
	
	private Map<Class<?>, Set<String>> transientPropertiesMap;

	/**
	 * Constructs a new instance of the TransientPropertyHandler.
	 */
	public TransientPropertyHandler() {
		transientPropertiesMap = new HashMap<Class<?>, Set<String>>();
	}

	/**
	 * Constructs a new instance of the TransientPropertyHandler that will 
	 * use the provided class-properties mapping.
	 * 
	 * @param transientPropertiesMap the provided class-properties mapping 
	 * to be used.
	 * @throws IllegalArgumentException if the transietnPropertiesMap 
	 * parameter is null.
	 */
	public TransientPropertyHandler(Map<Class<?>, 
			Set<String>> transientPropertiesMap) {
		validateNonNullArgument(transientPropertiesMap, 
				"transientPropertiesMap");
		this.transientPropertiesMap = transientPropertiesMap;
	}
	
	/***
	 * Convenience method to add class-properties mappings. If the class 
	 * being passed in does not exist, then it is added along with all of the 
	 * specified properties. If the class being passed in does exist, the 
	 * provided properties will simply be added to the existing set.
	 * 
	 * @param classObj the class which will be associated with the set of 
	 * properties that will be ignored.
	 * @param properties the set of properties that will be ignored.
	 */
	public void addProperties(Class<?> classObj, String... properties) {
		Set<String> propertySet = transientPropertiesMap.get(classObj);
		
		if (propertySet == null) {
			propertySet = new HashSet<String>();
		} 
		
		for (String property : properties) {
			propertySet.add(property);
		}

		transientPropertiesMap.put(classObj, propertySet);
	}
	
	/***
	 * Similar alternative to the aforementioned addProperties method. The 
	 * only difference is that overloaded method accepts a {@link JavaType} 
	 * instead of a class.
	 * 
	 * @param javaType the {@link JavaType} containing the actual class that 
	 * will be associated with a set of properties that will be ignored.
	 * @param properties the set of properties that will be ignored.
	 */
	public void addProperties(JavaType javaType, String... properties) {
		this.addProperties(javaType.getRawClass(), properties);
	}
	
	/**
	 * A convenience method that accepts a {@link JavaType} when removal 
	 * of an entry needs to take place.
	 * 
	 * @param javaType the {@link JavaType} containing the actual class that 
	 * will looked up and removed.
	 * @return the set of properties associated with the class if an entry for 
	 * that class exists.
	 */
	public Set<String> remove(JavaType javaType) {
		return transientPropertiesMap.remove(javaType.getRawClass());
	}
	
	/***
	 * A convenience method that removes all the properties associated with 
	 * the class passed in as a parameter. If the class exists, the properties 
	 * passed in will be removed from that class. If the removal results in a 
	 * empty set of ignored properties, the entry is removed.
	 * 
	 * @param classObj the class which will be associated with the set of 
	 * properties that will be ignored.
	 * @param properties the set of properties that will be ignored.
	 * @return true if all the properties were removed, false otherwise.
	 */
	public boolean removeProperties(Class<?> classObj, String... properties) {
		Set<String> propertySet = transientPropertiesMap.get(classObj);
		
		if (propertySet != null) {
			boolean isRemoved = propertySet
					.removeAll(Arrays.asList(properties));
			
			if (propertySet.isEmpty()) {
				transientPropertiesMap.remove(classObj);
			}
			
			return isRemoved;
		}
		
		return false;
	}
	
	/**
	 * Similar to the aformention removeProperties method except this 
	 * overloaded version accepts a {@link JavaType}. 
	 * 
	 * @param javaType the {@link JavaType} containing the actual class that 
	 * will looked up and removed.
	 * @param properties the set of properties that will be ignored.
	 * @return true if all the properties were removed, false otherwise.
	 */
	public boolean removeProperties(JavaType javaType, String... properties) {
		return removeProperties(javaType.getRawClass(), properties);
	}
	
	/**
	 * Convenience method to get the properties associated with the 
	 * class within the passed in {@link JavaType}.
	 * 
	 * @param javaType the {@link JavaType} containing the actual class that 
	 * will looked up for its associated properties if the class entry exists.
	 * @return the associated properties for the class contained in the 
	 * {@link JavaType} parameter.
	 */
	public Set<String> getProperties (JavaType javaType) {
		return transientPropertiesMap.get(javaType.getRawClass());
	}

	/**
	 * Getter method that returns a reference to the {@link java.util.Map} 
	 * that this class manages.
	 * 
	 * @return a mapping of all classes and associated properties to be 
	 * ignored.
	 */
	public Map<Class<?>, Set<String>> getTransientPropertiesMap() {
		return transientPropertiesMap;
	}

	/**
	 * Setter method that accepts a reference to a mapping of all associated 
	 * properties and classes to be ignored.
	 * 
	 * @param transientPropertiesMap the mapping of properties to be ignored 
	 * for each class entry.
	 * @throws IllegalArgumentException when the transientPropertiesMap 
	 * parameter is null.
	 * 
	 */
	public void setTransientPropertiesMap(Map<Class<?>, 
			Set<String>> transientPropertiesMap) {
		validateNonNullArgument(transientPropertiesMap, 
				"transientPropertiesMap");
		this.transientPropertiesMap = transientPropertiesMap;
	}

	@Override
	public String toString() {
		return "TransientPropertyHandler [transPropMap=" + transientPropertiesMap + "]";
	}

}
