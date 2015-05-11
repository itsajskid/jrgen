package com.github.jrgen.test.typebuilder;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import java.util.WeakHashMap;

import junit.framework.TestCase;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.impl.WeakHashtable;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.github.jrgen.context.JrgenContext;
import com.github.jrgen.settings.Settings;
import com.github.jrgen.test.domain.Address;
import com.github.jrgen.test.domain.BasicTypesTestBean;
import com.github.jrgen.test.domain.CollectionsTestBean;
import com.github.jrgen.test.domain.ImmutableName;
import com.github.jrgen.test.domain.Name;
import com.github.jrgen.test.domain.Person;
import com.github.jrgen.test.domain.PrimitiveTypesBean;

public class ArrayTypeBuilderTest {
	
	private JrgenContext jrgenContext;
	private Settings settings;
	
	private final Log log = LogFactory.getLog(getClass());
	private final static TypeFactory typeFactory = TypeFactory.defaultInstance();	
	
	private static final Class<?>[] primitiveTypes = {int[].class, int[][].class,
		long[].class, long[][].class, short[].class, short[][].class,
		byte[].class, byte[][].class, boolean[].class, boolean[][].class,
		double[].class, double[][].class, char[].class, char[][].class,
		float[].class, float[][].class};
	
	private static final Class<?>[] wrapperTypes = {
		Integer[].class, Integer[][].class, Long[].class, Long[][].class,
		Short[].class, Short[][].class, Byte[].class, Byte[][].class,
		Boolean[].class, Boolean[][].class, Double[].class, Double[][].class,
		Character[].class, Character[][].class, Float[].class, Float[][].class
	};
	
	private static final Class<?>[] enumTypes = {Thread.State.class,
		Name.Salutation.class
	};
	
	private static final Class<?>[] collectionTypes ={
		Collection[].class, List[].class, LinkedList[].class, ArrayList[].class,
		Vector[].class, PriorityQueue[].class, TreeSet[].class, HashSet[].class
	};
	
	private static final Class<?>[] mapTypes = {
		Map[].class, TreeMap[].class, LinkedHashMap[].class, HashMap[].class,
		Hashtable[].class, WeakHashMap[].class, WeakHashtable[].class
	};
	
	private static final Class<?>[] userTypes = {
		Name[].class, Person[].class, Address[].class, ImmutableName[].class,
		PrimitiveTypesBean[].class, BasicTypesTestBean[].class, File[].class
	};
	
	private static final JavaType[] collectionJavaTypes = {
		typeFactory.constructCollectionType(List.class, Name.class),
		typeFactory.constructCollectionType(Set.class, ImmutableName.class),
		typeFactory.constructCollectionType(LinkedList.class, Person.class),
		typeFactory.constructCollectionType(ArrayList.class, Integer.class),
		typeFactory.constructCollectionType(Collection.class, Address.class),
		typeFactory.constructCollectionType(Collection.class, int[].class)
	};
	
	private static final JavaType[] mapJavaTypes = {
		typeFactory.constructMapType(Map.class, Integer.class, Name.class),
		typeFactory.constructMapType(TreeMap.class, Integer.class, PrimitiveTypesBean.class),
		typeFactory.constructMapType(LinkedHashMap.class, Float.class, ImmutableName.class),
		typeFactory.constructMapType(HashMap.class, Address.class, CollectionsTestBean.class),
	};
	
	public ArrayTypeBuilderTest() {
		jrgenContext = new JrgenContext().initalizeContext();
		settings = jrgenContext.getSettings();
		settings.setMinContainerSize(1);
	}

	@Test
	public void generateClassTest() {
		doTest(primitiveTypes);
		doTest(wrapperTypes);
		doTest(enumTypes);
		doTest(userTypes);
		doCollectionTest(collectionTypes);
		doCollectionTest(mapTypes);
		
		doTestUsingJavaType(primitiveTypes);
		doTestUsingJavaType(wrapperTypes);
		doTestUsingJavaType(enumTypes);	
		doTestUsingCollectionJavaType(collectionJavaTypes);
		doTestUsingCollectionJavaType(mapJavaTypes);
		
		doMultiObjectTest(userTypes);
	}
	
	private void doTest (Class<?>[] arrayClassTypes) {
		StringBuilder sb = new StringBuilder("\n");
		
		for (Class<?> arrayClassType : arrayClassTypes) {
			Object obj = jrgenContext.generate(arrayClassType);
			sb.append(ArrayUtils.toString(obj)).append("\n");
			TestCase.assertTrue(obj.getClass() == arrayClassType);
		}		
		
		log.info(sb);
	}
	
	private void doCollectionTest (Class<?>[] arrayClassTypes) {
		for (Class<?> arrayClassType : arrayClassTypes) {
			Object obj = jrgenContext.generate(arrayClassType);
			log.info(ArrayUtils.toString(obj));
			log.info(obj.getClass());
			TestCase.assertTrue(obj
					.getClass()
					.isAssignableFrom(arrayClassType));
		}				
	}	
	
	private void doMultiObjectTest(Class<?>[] arrayClassTypes) {
		StringBuilder sb = new StringBuilder("\n");
		
		for (Class<?> arrayClassType : arrayClassTypes) {
			Collection<?> c = jrgenContext.generate(arrayClassType, 3);
			
			for (Object o : c) {
				sb.append(ArrayUtils.toString(o)).append("\n");
				TestCase.assertTrue(o.getClass() == arrayClassType);
			}
			
			TestCase.assertTrue(c.size() == 3);
		}
		
		log.info(sb);
	}
	
	private void doTestUsingJavaType (Class<?>[] arrayClassTypes) {
		StringBuilder sb = new StringBuilder("\n");
		
		for (Class<?> arrayClassType : arrayClassTypes) {
			Object obj = jrgenContext.generate(
					typeFactory.constructType(arrayClassType));
			sb.append(ArrayUtils.toString(obj)).append("\n");
			TestCase.assertTrue(obj.getClass() == arrayClassType);
		}		
		
		log.info(sb);
	}
	
	private void doTestUsingCollectionJavaType (JavaType[] collectionJavaTypes) {
		StringBuilder sb = new StringBuilder("\n");
		
		for (JavaType collectionJavaType : collectionJavaTypes ) {
			JavaType arrayJavaType = 
					typeFactory.constructArrayType(collectionJavaType);
			Object obj = jrgenContext.generate(arrayJavaType);
			TestCase.assertTrue(obj .getClass()== arrayJavaType.getRawClass());
			
			for (int i=0; i < Array.getLength(obj); i++) {
				Object o = Array.get(obj, i);
				
				if (o instanceof Collection) {
					for (Object element : (Collection<?>)o) {
						sb.append(ArrayUtils.toString(element)).append("\n");
					}				
				} else if (o instanceof Map) {
					sb.append(o).append("\n");
				} else {
					throw new RuntimeException(
							"Expected a collection or map got: " + 
					o.getClass());
				}
			}
			
			log.info(sb);			
			TestCase.assertTrue(obj.getClass() == arrayJavaType.getRawClass());
		}
	}

}

	