package com.github.jrgen.typehandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * <p>
 * The AbstractTypeHandler class allows the developer to specify which 
 * concrete types or implementations to use when it encounters a non-concrete 
 * type or implementation.
 * </p>
 * 
 * <p>
 * In some ways this class can be thought of as a factory for creating an 
 * {@link ObjectMapper} instance. 
 * </p>
 * 
 * @author Allan J. Shoulders
 * @version 1.0
 * @since 1.0.0
 * @see ObjectMapper
 *
 */
public class AbstractTypeHandler {

	private static final TypeFactory TYPE_FACTORY;
	
	@SuppressWarnings("rawtypes")
	private Map<Class, Class> defaultTypeMap;
	private ObjectMapper objectMapper;
	
	static {
		TYPE_FACTORY = TypeFactory.defaultInstance();
	}

	/**
	 * Constructs a new instance of the AbstractTypeHandler class.
	 * 
	 */
	@SuppressWarnings("rawtypes")
	public AbstractTypeHandler() {
		defaultTypeMap = new HashMap<Class, Class>();
	}
	
	/**
	 * Method locates and returns the concrete class associated with the 
	 * parameter which is assumed to be an abstract class type.
	 * 
	 * @param classType the abstract class type that will be used to find 
	 * the associated concrete type.
	 * @return the concrete type associated with the concrete type.
	 */
	public Class<?> findAbstractType (Class<?> classType) {
		return defaultTypeMap.get(classType);
	}
	
	/**
	 * Method locates and returns the {@link JavaType} associated with the 
	 * parameter which is assumed to be an abstract class type encapsulated 
	 * in a {@link JavaType}. 
	 * 
	 * @param javaType the abstract class type encapsulated within a 
	 * {@link JavaType}.
	 * @return the associated concrete class (if any) as a {@link JavaType}.
	 */
	public JavaType findAbstractTypeAsJavaType (JavaType javaType) {
		Class<?> absType = findAbstractType(javaType.getRawClass());
		
		return (absType == null) ? null : 
			TYPE_FACTORY.constructType(absType);
	}
	
	/**
	 * This method is almost identical to findAbstractTypeAsJavaType with the 
	 * only difference being that it returns the {@link JavaType} parameter if 
	 * a concrete class associated with the abstract class cannot be found.
	 * Hence, this method never returns a null value.
	 * 
	 * 
	 * @param javaType the abstract class type encapsulated within a 
	 * {@link JavaType}.
	 * @return the associated concrete class (if any) as a {@link JavaType} or 
	 * the parameter that was passed in if no concrete class exists.
	 */
	public JavaType findAbstractTypeDefaultJavaType (JavaType javaType) {
		JavaType defJavaType = findAbstractTypeAsJavaType(javaType);
		
		return (defJavaType == null) ? javaType : defJavaType;
	}
	
	/**
	 * Method associates an abstract type with a concrete type. When an 
	 * abstract type is encountered for conversion, the concrete type will 
	 * be instantiated and assigned to the abstract type variable.
	 * 
	 * @param <T> the parameterized type.
	 * @param whenThisType the abstract type that should be assigned an 
	 * instance of the concrete type when conversion takes place.
	 * @param useThisType the concrete type that will be instantiated,
	 * populated, and assigned to the abstract type variable.
	 */
	public <T> void registerAbstractType (Class<T> whenThisType, 
			Class<? extends T> useThisType) {
		defaultTypeMap.put(whenThisType, useThisType);
	}
	
	/**
	 * Removes any registered abstract type and its associated concrete 
	 * type.
	 * 
	 * @param classType the abstract type that will be removed along with 
	 * any associated concrete type.
	 * 
	 * @return the concrete type (if any).
	 */
	public Class<?> removeAbstractType (Class<?> classType) {
		return defaultTypeMap.remove(classType);
	}
	
	/**
	 * Removes every abstract type and associated concrete types.
	 * 
	 */
	public void removeAllAbstractTypes () {
		defaultTypeMap.clear();
	}
	
	/**
	 * Returns a <b>new</b> instance of an {@link ObjectMapper} when called. 
	 * If an {@link ObjectMapper} instance has already been assigned using 
	 * the setter method, than that instance will always be returned. If the 
	 * setter method is passed a null instance, then a new 
	 * {@link ObjectMapper} will be returned.
	 * 
	 * @return a new {@link ObjectMapper} instance or the {@link ObjectMapper} 
	 * instance that was previously set.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ObjectMapper getObjectMapper() {
		if (objectMapper != null) {
			return objectMapper;
		}
		
		SimpleModule simpleModule = new SimpleModule();
		
		for (Entry<Class, Class> absTypeEntry : defaultTypeMap.entrySet()) {
			simpleModule.addAbstractTypeMapping(absTypeEntry.getKey(), 
					absTypeEntry.getValue());
		}
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(simpleModule);
		
		return mapper;
	}

	/**
	 * Setter method that sets the {@link ObjectMapper} instance to this 
	 * instance of AbstractTypeHandler. This allows use of a customized 
	 * {@link ObjectMapper} instance if needed or desired.
	 * 
	 * @param objectMapper the customized instance of {@link ObjectMapper} 
	 * this class instance will use. If null, the class will return a new 
	 * instance of {@link ObjectMapper}.
	 */
	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public String toString() {
		return "AbstractTypeHandler [defaultTypeMap=" + defaultTypeMap
				+ ", objectMapper=" + objectMapper + "]";
	}	
	
} 
