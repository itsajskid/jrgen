package com.github.jrgen.test.core;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.github.jrgen.configuration.Configuration;
import com.github.jrgen.context.JrgenContext;
import com.github.jrgen.test.domain.ImmutableName;
import com.github.jrgen.test.domain.Name;
import com.github.jrgen.test.domain.Person;

public class JrgenContextTest {
	
	private static final Log log = LogFactory.getLog(JrgenContextTest.class);
	
	private JrgenContext ctx;
	private TypeFactory typeFactory;
	private ObjectMapper mapper;
	
	public JrgenContextTest() {
		ctx = new JrgenContext().initalizeContext();
		typeFactory = TypeFactory.defaultInstance();
		mapper = ctx.getAbstractTypeHandler().getObjectMapper();
	}

	@Test
	public void defaultJrgenTest() {		
		Map<JavaType, Configuration> typeMap = ctx.getTypeMap();
		
		Configuration personConfig = typeMap.get(typeFactory
				.constructType(Person.class));
		Configuration nameConfig = typeMap.get(typeFactory
				.constructType(Name.class));
		
		log.info(personConfig);
		log.info(nameConfig);	
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void unmodifiableMapTest() {
		ctx.getTypeMap().remove(typeFactory.constructType(Name.class));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void generateTest() {
		Name name = ctx.initalizeContext().generate(Name.class);
		log.info(name);
		
		JavaType intSetJavaType = ctx.initalizeContext()
				.getAbstractTypeHandler()
				.getObjectMapper()
				.getTypeFactory()
				.constructCollectionType(Set.class, Integer.class);
		
		Set<Integer> intSet = (Set<Integer>) ctx.generate(intSetJavaType);
		log.info(String.format("Type: %s, data: %s", 
				intSet.getClass(), intSet));
		
		//Override the HashSet default...
		ctx.getAbstractTypeHandler()
			.registerAbstractType(Set.class, TreeSet.class);
		
		//Characters should be in natural order...
		JavaType charSetJavaType = ctx.initalizeContext()
				.getAbstractTypeHandler()
				.getObjectMapper()
				.getTypeFactory()
				.constructCollectionType(Set.class, Character.class);
		Set<Character> charSet = (Set<Character>) ctx.generate(charSetJavaType);
		
		log.info(String.format("Type: %s, data: %s", 
				charSet.getClass(), charSet));
	}
	
	@Test
	public void generateMulitTest() {
		LinkedList<ImmutableName> immNameLinkedlist = 
				new LinkedList<ImmutableName>();
		
		log.info(immNameLinkedlist.getClass());
		
		Collection<ImmutableName> immNameColl =
				ctx.generate(ImmutableName.class, 10);
		
		log.info(immNameColl);
	}
		
	@Test
	public void convertMapperTest() {
		log.info(ctx.getAbstractTypeHandler()
				.getObjectMapper()
				.convertValue(new Object[]{}, Set.class));
	}
	
	@Test
	public void emptyContainerSet() {
		ctx.getSettings().setMaxContainerSize(0);
		log.info(ctx.initalizeContext().generate(List.class));
	}
	
	@Test
	public void getObjectMapperTest() {
		Name name = mapper.convertValue(null, Name.class);
		log.info(name);
		TestCase.assertNull(name);

		name = mapper.convertValue(Collections.EMPTY_MAP, Name.class);
		log.info(name);
		TestCase.assertNotNull(name);
	}
	
	@Test
	public void resourcesTest() throws URISyntaxException {
		URI uri = new URI("/map-resources");
		URI resource = JrgenContext.class.getResource(uri
				.toString())
				.toURI();
		
		File classpathFile = 
				new File(resource);		
		log.info(classpathFile);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void setAbstractTypeHandlerNullTest() {
		ctx.setAbstractTypeHandler(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void setSettingsNullTest() {
		ctx.setSettings(null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void setTransientPropertiesHandlerNullTest() {
		ctx.setTransientPropertyHandler(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void setWorkflowNullTest() {
		ctx.setWorkflow(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void setTypeBuildersNullTest() {
		ctx.setTypeBuilders(null);
	}
}
