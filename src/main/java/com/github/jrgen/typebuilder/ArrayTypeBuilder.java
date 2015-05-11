package com.github.jrgen.typebuilder;

import com.fasterxml.jackson.databind.JavaType;
import com.github.jrgen.context.JrgenContext;

/**
 * <p>
 * The ArrayTypeBuilder class is an implementation of the {@link TypeBuilder} 
 * interface. This implementation specifically handles {@link JavaType}s that 
 * are Arrays of any type.
 * </p>
 * 
 * <p>
 * This class depends on the entire default {@link TypeBuilder} stack. The 
 * Array's content type is resolved using other {@link TypeBuilder}s that 
 * are expected to be part of the stack.
 * </p>
 * 
 * @author Allan J. Shoulders
 * @version 1.0
 * @since 1.0.0
 * @see ListLikeTypeBuilder
 * @see TypeBuilder
 * @see CollectionTypeBuilder
 *
 */
public class ArrayTypeBuilder implements TypeBuilder<Object> {
	
	private JrgenContext jrgenContext;
	
	/**
	 * Constructs a new instance of the ArrayTypeBuilder class using the 
	 * supplied {@link JrgenContext}.
	 * 
	 * @param jrgenContext an instance of a {@link JrgenContext}.
	 */
	public ArrayTypeBuilder(JrgenContext jrgenContext) {
		this.jrgenContext = jrgenContext;
	}

	@Override
	public Object build(JavaType javaType) {
		
		if (!javaType.isArrayType()) {
			return null;
		}
		
		return new ListLikeTypeBuilder(jrgenContext).build(javaType);
	}

}
