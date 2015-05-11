package com.github.jrgen.typebuilder;

import java.util.Collections;
import java.util.Map;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jrgen.context.JrgenContext;
import com.github.jrgen.settings.Settings;
import com.github.jrgen.util.JrgenUtil;

/**
 * <p>
 * The MapTypeBuilder is an implementation of the {@link TypeBuilder} 
 * interface. This implementation specifically handles {@link JavaType}s that
 * are {@link Map} implementations. 
 * </p>
 * 
 * <p>
 * This class depends on the entire default {@link TypeBuilder} stack. The 
 * Map's key/value types are resolved using other {@link TypeBuilder}s that 
 * are expected to be part of the stack.
 * </p>
 * 
 * @author Allan J. Shoulders
 * @version 1.0
 * @since 1.0.0
 * @see TypeBuilder
 *
 */
public class MapTypeBuilder implements TypeBuilder<Map<?, ?>> {
	
	private JrgenContext jrgenContext;

	/**
	 * Constructs a new instance of the MapTypeBuilder class using the 
	 * supplied {@link JrgenContext}.
	 * 
	 * @param jrgenContext an instance of a {@link JrgenContext}.
	 */
	public MapTypeBuilder(JrgenContext jrgenContext) {
		this.jrgenContext = jrgenContext;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<?, ?> build(JavaType javaType) {	
		
		if(!javaType.isMapLikeType()) {
			return null;
		}
		
		ObjectMapper mapper = jrgenContext.getAbstractTypeHandler()
				.getObjectMapper();
		Settings settings = jrgenContext.getSettings();
		int containerSize;
		
		JavaType keyJavaType = javaType.getKeyType();
		JavaType valueJavaType = javaType.getContentType();
		
		Map<Object, Object> mapObj = (Map<Object, Object>)mapper
				.convertValue(Collections.EMPTY_MAP, javaType.getRawClass());
		
		if (settings.isRandomContainerSize()) {			
			containerSize = (int)JrgenUtil.getNumberFromRange(
					settings.getMinContainerSize(), 
					settings.getMaxContainerSize());		
		} else {
			containerSize = settings.getDefaultContainerSize();
		}
		
		if (containerSize == 0) {
			return (settings.isNullOnEmptySet()) ? 
					null : mapObj;
		}	
		
		for (int i = 0; i < containerSize; i++) {
			Object key = jrgenContext.getWorkflow().workflow(keyJavaType);
			if (key != null) {
				Object value = jrgenContext.getWorkflow().workflow(valueJavaType);
				mapObj.put(mapper.convertValue(key, keyJavaType), 
						mapper.convertValue(value, valueJavaType));
			}
		}			

		return mapObj;
	}

}
