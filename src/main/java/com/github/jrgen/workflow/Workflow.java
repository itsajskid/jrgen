package com.github.jrgen.workflow;

import java.util.Collection;

import com.fasterxml.jackson.databind.JavaType;
import com.github.jrgen.typebuilder.TypeBuilder;

/**
 * <p>
 * The Workflow interface (or it's implementations) provide a means to kick 
 * off traversal of all of it's {@link TypeBuilder}s. 
 * </p>
 * 
 * <p>
 * This interface defines what a Workflow implementation must provide. The 
 * Workflow implementation must allow {@link TypeBuilder}(s) to be added, 
 * removed, and found at a very minimum. The Workflow implementation should 
 * provide a workflow method that kicks off the actual workflow, which 
 * consists of {@link TypeBuilder} traversal based on the given 
 * {@link JavaType} parameter.
 * </p>
 * 
 * @author Allan J. Shoulders
 * @version 1.0
 * @since 1.0.0
 * @see DefaultWorkflow
 *
 */
public interface Workflow {

	/**
	 * This method begins traversal of all its registered {@link TypeBuilder}s 
	 * for the given {@link JavaType} parameter. The returned 
	 * object should match the type, or be an assignable type of the 
	 * encapsulated class type. In most cases, if no {@link TypeBuilder} 
	 * registered can build an instance of the given encapsulated class type, 
	 * then a null reference is returned. This can, however, vary based on the 
	 * implementation of the Workflow.
	 * 
	 * @param javaType the {@link JavaType} containing the class type that 
	 * will be passed to each registered {@link TypeBuilder} for resolution.
	 * @return An instance of the encapsulated class prepopulated with data. A 
	 * null value may be returned based on the Workflow implementation.
	 */
	public Object workflow(JavaType javaType);
	
	/**
	 * Registers a {@link TypeBuilder} implementation with this instance of 
	 * Workflow.
	 * 
	 * @param typeBuilder the {@link TypeBuilder} reference that will be 
	 * registered.
	 */
	public void addTypeBuilder (TypeBuilder<?> typeBuilder);
	
	/**
	 * Registers a number of {@link TypeBuilder} implementations within a 
	 * {@link Collection}.
	 * 
	 * @param typeBuilders the {@link Collection} of {@link TypeBuilder}s. 
	 * Each {@link TypeBuilder} will be registered.
	 */
	public void addTypeBuilders (Collection<TypeBuilder<?>> typeBuilders);
	
	/**
	 * Finds and returns the registered {@link TypeBuilder} if any.
	 * 
	 * @param typeBuilderClass - the class of the {@link TypeBuilder} 
	 * implementation.
	 * @return the registered {@link TypeBuilder} if any.
	 */
	public TypeBuilder<?> findTypeBuilder(
			Class<? extends TypeBuilder<?>> typeBuilderClass);

	/**
	 * Removes or "De-registers" the {@link TypeBuilder} implementation from 
	 * this Workflow implementation.
	 * 
	 * @param typeBuilderClass - the {@link TypeBuilder} implementation's 
	 * class type. This will be used to search for the {@link TypeBuilder} 
	 * and if found, removed.
	 * @return true if the registered {@link TypeBuilder} was found and 
	 * removed, false otherwise.
	 */
	public boolean removeTypeBuilder (
			Class<? extends TypeBuilder<?>> typeBuilderClass);
}
