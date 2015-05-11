package com.github.jrgen.test.core;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.github.jrgen.descriptor.Descriptor;
import com.github.jrgen.test.domain.Name;
import com.github.jrgen.test.domain.Person;

public class DescriptorTest {
	
	private static final Log log = LogFactory.getLog(DescriptorTest.class);
	
	@Test
	public void descriptorTest() {
		Descriptor d = new Descriptor();
		Map<String, JavaType> javaTypeMap = 
				d.describe(Person.class);
		
		for (Entry<String, JavaType> entry : javaTypeMap.entrySet()) {
			log.info(String.format("Property:%s \nType:%s\n\n", 
					entry.getKey(), entry.getValue()));
		}
	}
	
	@Test
	public void conversionTest() {
		TypeFactory tf = TypeFactory.defaultInstance();
		
		Set<List<Person>> personListSet = new LinkedHashSet<List<Person>>();
		
		JavaType javaType = tf.constructType(personListSet.getClass());
		
		log.info(String.format("Type: %s\n", javaType));
		log.info(String.format("Is collectionLike: %s\n", 
				javaType.isCollectionLikeType()));
		
		Person p = new Person();
		Map<String, String> feauxName = new HashMap<String, String>();
		feauxName.put("firstName", "Jon");
		feauxName.put("middleName", "J");
		feauxName.put("lastName", "Jones");
		
		p.setName(new Name("Allan", "Shoulders", "J"));
		
		Object[] o = new Object[]{p, feauxName};
		
		ObjectMapper mapper = new ObjectMapper();
		Object convObj = mapper.convertValue(o, javaType);
		log.info(String.format("Converted Object: %s\n", convObj));
		log.info(String.format("Java Class: %s\n", convObj.getClass()));
	}	
	
	@Test
	public void javaTypeMapTest() {
		JavaType javaType = TypeFactory
				.defaultInstance()
				.constructMapLikeType(HashMap.class, 
						Name.class, 
						Person.class);
		
		log.info(String.format("JavaType: %s\n", javaType));
		log.info(String.format("Key Type: %s\n", javaType.getKeyType()));
		log.info(String.format("Value Type: %s\n", javaType.getContentType()));
	}
	
	@Test
	public void constructorTest() {
		for (Constructor<?> c : File.class.getConstructors()) {
			log.info(c);
		}
	}
}
