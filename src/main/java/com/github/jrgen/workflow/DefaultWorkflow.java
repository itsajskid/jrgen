package com.github.jrgen.workflow;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.databind.JavaType;
import com.github.jrgen.context.JrgenContext;
import com.github.jrgen.typebuilder.TypeBuilder;

/**
 * <p>
 * This class is an implementation of the {@link Workflow} interface. It's 
 * workflow method traverses all registered {@link TypeBuilder}s, in search 
 * for the {@link TypeBuilder} that can build and prepopulate an instance of 
 * the encapsulated class. This {@link Workflow} implementation is used by 
 * default (hence the name) in the {@link JrgenContext}. For this 
 * implementation, it's workflow method returns a null value if none of its 
 * registered {@link TypeBuilder}s can build the encapsulated class from the 
 * passed in {@link JavaType}.
 * </p>
 * 
 * @author Allan J. Shoulders
 * @since 1.0.0
 * @version 1.0
 * @see Workflow
 *
 */
public class DefaultWorkflow implements Workflow {
	
	private final List<TypeBuilder<?>> typeBuilders;
	
	/**
	 * Constructs a new instance of the DefaultWorklow.
	 */
	public DefaultWorkflow() {
		typeBuilders = new LinkedList<TypeBuilder<?>>();
	}

	@Override
	public Object workflow(JavaType javaType) {
		
		for (TypeBuilder<?> typeBuilder : typeBuilders) {
			Object o = typeBuilder.build(javaType);
			
			if (o != null) {
				return o;
			}
		}
		
		return null;
	}

	@Override
	public TypeBuilder<?> findTypeBuilder (
			Class<? extends TypeBuilder<?>> typeBuilderClass) {
		
		for (TypeBuilder<?> typeBuilder : typeBuilders) {
			if (typeBuilder.getClass() == typeBuilderClass) {
				return typeBuilder;
			}
		}
		
		return null;
	}
	
	@Override
	public void addTypeBuilder (TypeBuilder<?> typeBuilder) {
		typeBuilders.add(typeBuilder);
	}
	
	@Override
	public boolean removeTypeBuilder (Class<? extends TypeBuilder<?>> typeBuilderClass) {
		TypeBuilder<?> remTypeBuilder = findTypeBuilder(typeBuilderClass);
		return typeBuilders.remove(remTypeBuilder);
	}

	@Override
	public String toString() {
		return "DefaultWorkflow [typeBuilders=" + typeBuilders + "]";
	}

	@Override
	public void addTypeBuilders(Collection<TypeBuilder<?>> typeBuilders) {
		this.typeBuilders.addAll(typeBuilders);
	}
	
}
