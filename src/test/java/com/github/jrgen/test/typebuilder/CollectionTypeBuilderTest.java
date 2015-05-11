package com.github.jrgen.test.typebuilder;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;
import java.util.Vector;
import java.util.WeakHashMap;

import javax.swing.SortOrder;

import junit.framework.TestCase;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.github.jrgen.context.JrgenContext;
import com.github.jrgen.settings.Settings;
import com.github.jrgen.test.domain.Address;
import com.github.jrgen.test.domain.BasicTypesTestBean;
import com.github.jrgen.test.domain.CollectionsTestBean;
import com.github.jrgen.test.domain.ImmutableName;
import com.github.jrgen.test.domain.ImmutableTestBean;
import com.github.jrgen.test.domain.Name;
import com.github.jrgen.test.domain.NestedTypesTestBean;

public class CollectionTypeBuilderTest {

	private JrgenContext jrgenContext;
	private Settings settings;
	
	private final Log log = LogFactory.getLog(getClass());
	private final static TypeFactory typeFactory = TypeFactory.defaultInstance();
	
	private static final Class<?>[] arrayTypes = {
		Name[].class, ImmutableName[].class, Address[].class, BasicTypesTestBean[].class,
		CollectionsTestBean[].class, ImmutableTestBean[].class, NestedTypesTestBean[].class,
		Integer[].class, Long[].class, Short[].class, Character[].class,
		BigDecimal[].class, BigInteger[].class, Boolean[].class, Byte[].class, 
		Double[].class, Float[].class, Date[].class
	};
	
	private static final Class<?>[] userTypes = {
		Name.class, ImmutableName.class, Address.class, BasicTypesTestBean.class,
		CollectionsTestBean.class, ImmutableTestBean.class, NestedTypesTestBean.class
	};
	
	private static final Class<?>[] enumTypes = {
		Thread.State.class, Name.Salutation.class, RoundingMode.class,
		SortOrder.class
	};
	
	//Should generate Map of Strings. For more extensive Map tests see
	//MapTypeBuilderTest.java.
	private static final Class<?>[] mapTypes = {
		Map.class, TreeMap.class, WeakHashMap.class, HashMap.class, LinkedHashMap.class
	};
	
	private static final Class<?>[] basicTypes = {
		Integer.class, Long.class, Short.class, Character.class,
		BigDecimal.class, BigInteger.class, Boolean.class, Byte.class, 
		Double.class, Float.class, Date.class		
	};
	
	private static final Class<?>[] colllectionTypes = {
		Collection.class, ArrayList.class, Vector.class,
		LinkedList.class, PriorityQueue.class, List.class
	};
	
	public CollectionTypeBuilderTest() {
		settings = Settings.getInstance();
		settings.setMinContainerSize(1);
		jrgenContext = new JrgenContext(settings).initalizeContext();
	}
	
	@Test
	public void testTypes() {
		testByClassTypeToJavaType(userTypes);
		testByClassTypeToJavaType(enumTypes);
		testByClassTypeToJavaType(basicTypes);
		testByClassTypeForCollectionsMaps(mapTypes);
		testByClassTypeForCollectionsMaps(colllectionTypes);
		testByClassTypeForArrays(arrayTypes);
	}
	
	private void testByClassTypeToJavaType(Class<?>[] types) {
		StringBuilder sb = new StringBuilder("\n");
		
		try {
			for (Class<?> type : types) {
				//Create a collection the given type...
				JavaType javaType = 
						typeFactory.constructCollectionType(Collection.class, 
								type);
				Object obj = jrgenContext.generate(javaType);
				
				//make sure we get back a collection...
				TestCase.assertTrue(obj instanceof Collection);
				sb.append(obj).append("\n");
				
				for (Object o : (Collection<?>)obj) {
					//make sure the content types match...
					//if null - meaning the object could not be resolved.
					Class<?> contentClass = 
							javaType.getContentType().getRawClass();
					if (o != null) {
						TestCase.assertTrue(o.getClass() == contentClass);
					}
				}
			}
		} finally {
			log.info(sb);			
		}
	}
	
	private void testByClassTypeForCollectionsMaps (Class<?>[] types) {
		StringBuilder sb = new StringBuilder("\n");
		
		try {
			for (Class<?> type : types) {
				//Create a collection the given type...
				JavaType javaType = 
						typeFactory.constructCollectionType(Collection.class, 
								type);
				Object obj = jrgenContext.generate(javaType);
				
				//make sure we get back a collection...
				TestCase.assertTrue(obj instanceof Collection);
				sb.append(obj).append("\n");
				
				for (Object o : (Collection<?>)obj) {
					//make sure each object in the collection is a collection
					//if null - meaning it could not be resolved.
					if (o != null) {
						TestCase.assertTrue(o instanceof Map || o instanceof Collection);
					}
				}
			}
		} finally {
			log.info(sb);			
		}		
	}
	
	private void testByClassTypeForArrays (Class<?>[] arrayClassTypes) {
		StringBuilder sb = new StringBuilder("\n");
		
		try {
			for (Class<?> arrayClassType : arrayClassTypes) {
				//Create a collection the given type...
				JavaType javaType = 
						typeFactory.constructCollectionType(Collection.class, 
								arrayClassType);
				Object obj = jrgenContext.generate(javaType);
				
				//make sure we get back a collection...
				TestCase.assertTrue(obj instanceof Collection);
				
				for (Object o : (Collection<?>)obj) {
					//make sure the content types match...
					Class<?> contentClass = 
							javaType.getContentType().getRawClass();
					sb.append(ArrayUtils.toString(o)).append("\n");
					TestCase.assertTrue(o.getClass() == contentClass);
				}
			}
		} finally {
			log.info(sb);			
		}		
	}
}
