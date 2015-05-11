package com.github.jrgen.typebuilder;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Collections;

import com.fasterxml.jackson.databind.JavaType;
import com.github.jrgen.context.JrgenContext;

/**
 * <p>
 * The CollectionTypeBuilder is an implementation of the {@link TypeBuilder} 
 * interface. This implementation specifically handles {@link JavaType}s that 
 * are {@link Collection}s parameterized or not of any type.
 * </p>
 * 
 * <p>
 * This class depends on the entire default {@link TypeBuilder} stack. The 
 * Collection's content type is resolved using other {@link TypeBuilder}s that 
 * are expected to be part of the stack.
 * </p>
 * 
 * @author Allan J. Shoulders
 * @version 1.0
 * @since 1.0.0
 * @see ListLikeTypeBuilder
 * @see TypeBuilder
 * @see ArrayTypeBuilder
 *
 */
public class CollectionTypeBuilder implements TypeBuilder<Collection<?>> {

	private JrgenContext jrgenContext;

	/**
	 * Constructs a new instance of the CollectionTypeBuilder class using the 
	 * supplied {@link JrgenContext}.
	 * 
	 * @param jrgenContext an instance of a {@link JrgenContext}.
	 */
	public CollectionTypeBuilder(JrgenContext jrgenContext) {
		this.jrgenContext = jrgenContext;
	}
	
	@Override
	public Collection<?> build(JavaType javaType) {
		
		if (!javaType.isCollectionLikeType()) {
			return null;
		}
		
		Object objs = new ListLikeTypeBuilder(jrgenContext).build(javaType);
		
		Collection<Object> objsColl = jrgenContext.getAbstractTypeHandler()
				.getObjectMapper()
				.convertValue(Collections.EMPTY_LIST, javaType);
		
		for (int i = 0; i < Array.getLength(objs); i++) {
			objsColl.add(Array.get(objs, i));
		}
		
 		return objsColl;
	}

}
