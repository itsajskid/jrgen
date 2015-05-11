package com.github.jrgen.typebuilder;

import com.fasterxml.jackson.databind.JavaType;
import com.github.jrgen.workflow.Workflow;

/**
 * <p>
 * The TypeBuilder interface is an interface whose purpose is to build an
 * instance of its parameterized type. A class that implements this interface 
 * should provide a specific type parameter that the build method is 
 * responsible for returning. The instance can be null 
 * unless by design such values are not acceptable, which is determined by 
 * the developer. 
 * </p>
 * 
 * <p>
 * As with most components through Jrgen, {@link JavaType}s are used since 
 * it provides better support parameterized types amongst other features.
 * </p>
 * 
 * <p>
 * Typically a TypeBuilder is part of a {@link Workflow}. The {@link Workflow} 
 * consists of a stack of TypeBuilders which are passed a 
 * {@link JavaType}. The {@link JavaType} parameter is used by the build 
 * method to determine whether or not the current TypeBuilder should handle 
 * specific types. If the type should not be handled by the current 
 * {@link TypeBuilder}, (i.e. {@link CollectionTypeBuilder} should not handle 
 * a {@link JavaType} of {@link Enum}) then the {@link Workflow} object should 
 * be notified to find another TypeBuilder that can handle creation of that 
 * type on the stack. This is typically done by returning a null instance, but 
 * the developer may choose to do otherwise, since a custom {@link Workflow} 
 * may be defined.
 * </p>
 * 
 * @author Allan J. Shoulders
 * @param <T> the parameterized type the TypeBuilder should return.
 * @version 1.0
 * @since 1.0.0
 * 
 * @see ArrayTypeBuilder
 * @see CollectionTypeBuilder
 * @see ConfigurationBasedTypeBuilder
 * @see EnumTypeBuilder
 * @see ImmutableTypeBuilder
 * @see ListLikeTypeBuilder
 * @see MapTypeBuilder
 * @see PrimitiveTypeBuilder
 * @see Workflow
 * 
 */
public interface TypeBuilder<T> {

	/**
	 * This method is responsible for instantiating and returning an instance 
	 * of its parameterized type. 
	 * 
	 * @param javaType type requested by the developer and passed in by the 
	 * {@link Workflow} object. The type held by the {@link JavaType} should 
	 * match the type the build method is responsible for returning.
	 * @return An instance of its parameterized type.
	 */
	public T build(JavaType javaType);
	
}
