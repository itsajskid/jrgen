package com.github.jrgen.test.core;

import java.util.Map;
import java.util.Map.Entry;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.github.jrgen.descriptor.Descriptor;
import com.github.jrgen.test.domain.Person;

public class DescriptorTest {
	
	private static final Log log = LogFactory.getLog(DescriptorTest.class);
	private StringBuilder strBlder;
	
	public DescriptorTest() {
		strBlder = new StringBuilder();
	}
	
	@Test
	public void descriptorTestClass() {
		Descriptor d = new Descriptor();
		Map<String, JavaType> javaTypeMap = 
				d.describe(Person.class);
		
		for (Entry<String, JavaType> entry : javaTypeMap.entrySet()) {
			strBlder.append(String.format("Property:%s \nType:%s\n", 
					entry.getKey(), entry.getValue()));
		}
		
		log.info(strBlder);
		TestCase.assertFalse(javaTypeMap.isEmpty());
	}
	
	@Test
	public void descriptorTestInstance() {
		Descriptor d = new Descriptor();
		Map<String, JavaType> javaTypeMap = 
				d.describe(new Person());
		
		for (Entry<String, JavaType> entry : javaTypeMap.entrySet()) {
			strBlder.append(String.format("Property:%s \nType:%s\n", 
					entry.getKey(), entry.getValue()));
		}		
		
		log.info(strBlder);
		TestCase.assertFalse(javaTypeMap.isEmpty());
	}
	
	@Test
	public void descriptorTestJavaType() {
		Descriptor d = new Descriptor();
		JavaType javaType = 
				TypeFactory.defaultInstance().constructType(Person.class);
		Map<String, JavaType> javaTypeMap = 
				d.describe(javaType);
		
		for (Entry<String, JavaType> entry : javaTypeMap.entrySet()) {
			strBlder.append(String.format("Property:%s \nType:%s\n", 
					entry.getKey(), entry.getValue()));
		}		
		
		log.info(strBlder);
		TestCase.assertFalse(javaTypeMap.isEmpty());
	}
}
