package com.github.jrgen.test.typebuilder;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import com.github.jrgen.context.JrgenContext;
import com.github.jrgen.settings.Settings;
import com.github.jrgen.test.domain.ImmutableName;
import com.github.jrgen.test.domain.ImmutableTestBean;

public class ImmutableTypeBuilderTest {

	private static final Log log = LogFactory.getLog(ImmutableTypeBuilderTest.class);
	
	private JrgenContext jrgenContext;
	private Settings settings;
	
	public ImmutableTypeBuilderTest() {
		jrgenContext = new JrgenContext().initalizeContext();
		settings = jrgenContext.getSettings();
	}
	
	@Test
	public void immutableNameTest() {
		ImmutableName immutableName = 
				jrgenContext.generate(ImmutableName.class);
		
		TestCase.assertNotNull(immutableName);
		log.info(immutableName);
	}
	
	@Test
	public void immutableTestBeanNullTest() {
		ImmutableTestBean itb = 
				jrgenContext.generate(ImmutableTestBean.class);
		
		TestCase.assertNull(itb);
		log.info(itb);
	}	
	
	@Test
	public void immutableTestBeanNotNullTest() {
		settings.setNotNull(true);
		ImmutableTestBean itb = 
				jrgenContext.generate(ImmutableTestBean.class);
		
		TestCase.assertNotNull(itb);
		log.info(itb);
	}
	
	@Test
	public void fileStrConstructorTest() throws URISyntaxException {
		changeDefaultConfigDir("/immutable-resources/file-strconst");
		File file = jrgenContext.generate(File.class);
		TestCase.assertNotNull(file);
		log.info(file);
	}
	
	@Test
	public void fileUriConstructorTest() throws URISyntaxException {
		changeDefaultConfigDir("/immutable-resources/file-uriconst");
		File file = jrgenContext.generate(File.class);
		TestCase.assertNotNull(file);
		log.info(file);
	}	
	
	@Test
	public void fileTwoArgConstructorTest() throws URISyntaxException {
		changeDefaultConfigDir("/immutable-resources/file-twoargconst");
		Collection<File> file = jrgenContext.generate(File.class, 5);
		TestCase.assertNotNull(file);
		log.info(file);	
	}
	
	@Test
	public void fileNullArgConstructorTest() throws URISyntaxException {
		changeDefaultConfigDir("/immutable-resources/file-nullargconst");
		File file = jrgenContext.generate(File.class);
		TestCase.assertNotNull(file);
		log.info(file);		
	}
	
	private void changeDefaultConfigDir(String dir) throws URISyntaxException {
		settings.getResources().clear();
		settings.getResources().add(
				new URI(dir));
		jrgenContext.initalizeContext();
	}
}
