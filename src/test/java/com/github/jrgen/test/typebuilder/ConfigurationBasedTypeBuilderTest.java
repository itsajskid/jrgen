package com.github.jrgen.test.typebuilder;

import java.util.Collection;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import com.github.jrgen.context.JrgenContext;
import com.github.jrgen.test.domain.Address;
import com.github.jrgen.test.domain.CollectionsTestBean;
import com.github.jrgen.test.domain.Name;
import com.github.jrgen.test.domain.Person;
import com.github.jrgen.typehandler.TransientPropertyHandler;

public class ConfigurationBasedTypeBuilderTest {

	private final static Log log = 
			LogFactory.getLog(ConfigurationBasedTypeBuilderTest.class);
	private JrgenContext jrgenContext;
	private StringBuilder strBlder;
	
	public ConfigurationBasedTypeBuilderTest() {
		jrgenContext = new JrgenContext().initalizeContext();
		strBlder = new StringBuilder();
	}
	
	@Test
	public void ignorePropertiesTest() {
		jrgenContext.getSettings().setGenerateUndefined(false);
		Name name = jrgenContext.generate(Name.class);
		strBlder.append("Before Ignoring: ").append(name).append("\n");
		
		jrgenContext.getTransientPropertyHandler().addProperties(Name.class, 
				"salutation", 
				"middleName");
		name = jrgenContext.generate(Name.class);
		strBlder.append("After Ignoring: ").append(name).append("\n");
		log.info(strBlder);
		
		TestCase.assertNull(name.getSalutation());
		TestCase.assertNull(name.getMiddleName());
	}
	
	@Test
	public void ignoreLowConfigurationTest() {
		jrgenContext
			.getTransientPropertyHandler()
			.addProperties(CollectionsTestBean.class, 
					"stringSet", "primitiveInt", "intSet");
		
		CollectionsTestBean ctb =
				jrgenContext.generate(CollectionsTestBean.class);
		strBlder.append(ctb).append("\n");
		log.info(strBlder);
		
		//primitive defaults to 0.
		TestCase.assertTrue(ctb.getPrimitiveInt() == 0);
		TestCase.assertNull(ctb.getStringSet());
		TestCase.assertNull(ctb.getIntSet());
	}

	@Test
	public void ignorePropertiesFromFieldTest() {
		TransientPropertyHandler tph = jrgenContext.getTransientPropertyHandler();
		tph.addProperties(Name.class, 
				"salutation", 
				"middleName");
		tph.addProperties(Address.class, "otherStreet");
		
		Collection<Person> people = jrgenContext.generate(Person.class, 10);
		
		try {
			for (Person person : people) {
				strBlder.append(person).append("\n");
				TestCase.assertNull(person.getAddress().getOtherStreet());
				TestCase.assertNull(person.getName().getSalutation());
				TestCase.assertNull(person.getName().getMiddleName());
				
				if (person.getContacts() != null) {
					for (Name name : person.getContacts()) {
						TestCase.assertNull(name.getMiddleName());
						TestCase.assertNull(name.getSalutation());
					}
				}
			}
		} finally {
			log.info(strBlder);
		}
		
	}
	
	@Test
	public void ignoreNoConfigurationTest() {
		jrgenContext.getSettings().getResources().clear();
		
		jrgenContext
		.getTransientPropertyHandler()
		.addProperties(CollectionsTestBean.class, 
				"stringSet", "primitiveInt", "intSet");
	
		CollectionsTestBean ctb =
				jrgenContext.generate(CollectionsTestBean.class);
		strBlder.append(ctb).append("\n");
		log.info(strBlder);
		
		//primitive defaults to 0.
		TestCase.assertTrue(ctb.getPrimitiveInt() == 0);
		TestCase.assertNull(ctb.getStringSet());
		TestCase.assertNull(ctb.getIntSet());		
	}
	
	@Test
	public void ignoreThenRemoveTets() {
		jrgenContext.getSettings().getResources().clear();
		
		jrgenContext
			.getTransientPropertyHandler()
			.addProperties(CollectionsTestBean.class, 
				"stringSet", "intSet");
		
		//Remove the properties we just added...
		jrgenContext.getTransientPropertyHandler().removeProperties(
				CollectionsTestBean.class, 
				"stringSet", "intSet");
	
		CollectionsTestBean ctb =
				jrgenContext.generate(CollectionsTestBean.class);
		strBlder.append(ctb).append("\n");		
		log.info(strBlder);
		
		TestCase.assertNotNull(ctb.getStringSet());
		TestCase.assertNotNull(ctb.getIntSet());			
	}
}
