package com.github.jrgen.typebuilder;

import java.lang.reflect.Array;

import com.fasterxml.jackson.databind.JavaType;
import com.github.jrgen.context.JrgenContext;
import com.github.jrgen.settings.Settings;
import com.github.jrgen.util.JrgenUtil;
import com.github.jrgen.workflow.Workflow;

/**
 * <p>
 * The ListLikeTypeBuilder class is an implementation of the {@link TypeBuilder} 
 * interface. This implementation is used as "helper" class for the 
 * {@link ArrayTypeBuilder} and {@link CollectionTypeBuilder} classes 
 * respectively. This class is not part of the {@link Workflow} implementation 
 * that is created by default.
 * </p>
 * 
 * @author Allan J. Shoulders
 * @version 1.0
 * @since 1.0.0
 * @see ArrayTypeBuilder
 * @see CollectionTypeBuilder
 * @see TypeBuilder
 * 
 */
class ListLikeTypeBuilder implements TypeBuilder<Object> {

	private JrgenContext jrgenContext;
	
	public ListLikeTypeBuilder(JrgenContext jrgenContext) {
		this.jrgenContext = jrgenContext;
	}
	
	@Override
	public Object build(JavaType javaType) {
		Settings settings = jrgenContext.getSettings();
		
		JavaType contentType = 
				javaType.getContentType();
		
		int containerSize;
		
		if (jrgenContext.getSettings().isRandomContainerSize()) {			
			containerSize = (int)JrgenUtil.getNumberFromRange(
					settings.getMinContainerSize(), 
					settings.getMaxContainerSize());		
		} else {
			containerSize = settings.getDefaultContainerSize();
		}
		
		Object objs = Array.newInstance(
				javaType.getContentType().getRawClass(), 
				containerSize);
		
		if (Array.getLength(objs) == 0) {
			return (settings.isNullOnEmptySet()) ? 
					null : objs;
		}
		
		for (int i=0; i < containerSize; i++) {
			Array.set(objs, 
					i, 
					jrgenContext.getWorkflow().workflow(contentType));
		}		
		
		return objs;
	}

}
