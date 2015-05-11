package com.github.jrgen.typebuilder;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.github.jrgen.context.JrgenContext;
import com.github.jrgen.generator.DefaultGenerator;

/**
 * <p>
 * The PrimitiveTypeBuilder is an implementation of the {@link TypeBuilder} 
 * interface. This implementation specifically handles {@link JavaType}s that 
 * are type Wrappers or primitives.
 * </p>
 * 
 * <p>
 * Regardless of whether the type is a primitive or Wrapper a Wrapper is 
 * always returned. The Wrapper will have to be unboxed.
 * </p>
 * 
 * <p>
 * Unlike many of the conventional {@link TypeBuilder} implementations, this 
 * implementation does not depend on other {@link TypeBuilder}s when building 
 * and returning a new instance.
 * </p>
 * 
 * @author Allan J. Shoulders
 * @version 1.0
 * @since 1.0.0
 * @see TypeBuilder
 *
 */
public class PrimitiveTypeBuilder implements TypeBuilder<Object> {
	
	private JrgenContext jrgenContext;
	
	/**
	 * Constructs a new instance of the PrimitiveTypeBuilder class using the 
	 * supplied {@link JrgenContext}.
	 * 
	 * @param jrgenContext an instance of a {@link JrgenContext}.
	 */
	public PrimitiveTypeBuilder(JrgenContext jrgenContext) {
		this.jrgenContext = jrgenContext;
	}

	@Override
	public Object build(JavaType javaType) {
		
		Class<?> valueClass;
		
		if (javaType.isPrimitive()) {
			valueClass = 
					ClassUtil.wrapperType(javaType.getRawClass());
		} else {
			valueClass = javaType.getRawClass();
		}
		
		try {
			DefaultGenerator generator = 
					new DefaultGenerator(jrgenContext.getSettings());
			return generator.createValue(valueClass);
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

}
