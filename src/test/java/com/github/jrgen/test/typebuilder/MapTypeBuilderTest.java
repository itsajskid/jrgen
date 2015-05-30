package com.github.jrgen.test.typebuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import junit.framework.TestCase;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.github.jrgen.context.JrgenContext;
import com.github.jrgen.settings.Settings;
import com.github.jrgen.test.domain.ImmutableName;
import com.github.jrgen.test.domain.Name;
import com.github.jrgen.test.domain.Name.Salutation;

public class MapTypeBuilderTest {
	
	private JrgenContext jrgenContext;
	private Settings settings;
	
	private final Log log = LogFactory.getLog(getClass());
	private final TypeFactory typeFactory = TypeFactory.defaultInstance();
	
	public MapTypeBuilderTest() throws URISyntaxException {
		settings = Settings.getInstance();
		settings.getResources().clear();
		settings.getResources().add(new URI("/map-resources"));
		jrgenContext = new JrgenContext(settings).initalizeContext();
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void generateMapOfObjectsTest() {
		Map objMap = jrgenContext.generate(Map.class);
		TestCase.assertNotNull(objMap);
		log.info(objMap);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void generateMapOfStringIntegerTest() {
		JavaType javaType = typeFactory
				.constructMapType(Map.class, String.class, Integer.class);
		
		Map<String, Integer> strIntMap = (Map<String, Integer>) jrgenContext
				.initalizeContext().generate(javaType);
		
		log.info(strIntMap);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void generateMapOfDoubleMapOfStringIntegerTest() {
		//We must use JavaType provided by Jackson so we can get
		//parameterized types.
		JavaType keyJavaType = typeFactory.constructType(Double.class);
		
		JavaType javaType = typeFactory
				.constructMapType(Map.class, String.class, Integer.class);
		
		JavaType mapOfMapJavaType = typeFactory
				.constructMapType(Map.class, keyJavaType, javaType);
		
		//Override settings so we guarantee 2 entries in each map.
		jrgenContext.getSettings().setRandomContainerSize(false);
		jrgenContext.getSettings().setDefaultContainerSize(2);
		
		Map<Double, Map<String, Integer>> doubleStrIntMap = 
				(Map<Double, Map<String, Integer>>) jrgenContext
					.generate(mapOfMapJavaType);
		
		log.info(doubleStrIntMap);
		TestCase.assertNotNull(doubleStrIntMap);
		TestCase.assertEquals(2, doubleStrIntMap.size());
		
		for (Entry<Double, Map<String, Integer>> entry : 
			doubleStrIntMap.entrySet()) {
			TestCase.assertEquals(2, entry.getValue().size());
		}
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void generateMapOfImmutableNamesNamesTest() {		
		JavaType javaType = typeFactory.constructMapType(Map.class, 
				ImmutableName.class, 
				Name.class);
		
		//Guarantee we get back a non-empty map...
		jrgenContext.getSettings().setMinContainerSize(1);
		
		Map<ImmutableName, Name> immutableNameOfNameMap = 
				(Map<ImmutableName, Name>)jrgenContext.generate(javaType);
		
		TestCase.assertNotNull(immutableNameOfNameMap);
		TestCase.assertTrue(!MapUtils.isEmpty(immutableNameOfNameMap));
		log.info(immutableNameOfNameMap);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void changeDefaultMapTest() {
		settings.setMinContainerSize(1);
		
		//Should return map of integers...by default returns strings...
		JavaType intOfIntMap = 
				typeFactory.constructMapType(Map.class, 
						Integer.class, 
						Integer.class);
		
		Map<Integer, Integer> mapAsObj = (Map<Integer, Integer>)
				jrgenContext.generate(intOfIntMap);
		
		log.info(mapAsObj);
		TestCase.assertTrue(mapAsObj.size() > 0);
		
		for (Entry<Integer, Integer> entry : mapAsObj.entrySet()) {
			TestCase.assertTrue(entry.getKey().getClass() == Integer.class);
			TestCase.assertTrue(entry.getValue().getClass() == Integer.class);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void changeDefaultMapImplTest() {
		JavaType mapJavaType = typeFactory.constructMapType(Map.class, 
				Integer.class, 
				Integer.class);
		
		//Should return a TreeMap of key Integer and value Integer
		jrgenContext.getAbstractTypeHandler()
			.registerAbstractType(Map.class, TreeMap.class);
		settings.setMinContainerSize(1);
		
		Map<Integer, Integer> genMap = (Map<Integer, Integer>)
				jrgenContext.generate(mapJavaType);
		
		log.info(genMap);
		log.info(genMap.getClass());
		
		TestCase.assertTrue(genMap.getClass() == TreeMap.class);
		for (Entry<Integer, Integer> entry : genMap.entrySet()) {
			TestCase.assertTrue(entry.getKey().getClass() == Integer.class);
			TestCase.assertTrue(entry.getValue().getClass() == Integer.class);
		}		
	}

	@SuppressWarnings("unchecked")
	@Test
	public void mapOfEnumNameArray() {
		JavaType javaType = typeFactory.constructMapType(LinkedHashMap.class, 
				Name.Salutation.class, 
				long[].class);
		
		//Guarantee we have one element at least...
		settings.setMinContainerSize(1);
		
		Map<Name.Salutation, long[]> m = (Map<Salutation, long[]>) 
				jrgenContext.generate(javaType);
		
		for (Entry<Name.Salutation, long[]> entry : m.entrySet()) {
			log.info(String.format("%s=%s", entry.getKey(), 
					Arrays.toString(entry.getValue())));
		}
		
		TestCase.assertFalse(MapUtils.isEmpty(m));
	}

}
