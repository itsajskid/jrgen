package com.github.jrgen.test.core;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.github.jrgen.context.JrgenContext;
import com.github.jrgen.test.domain.Address;
import com.github.jrgen.test.domain.Name;
import com.github.jrgen.test.domain.Person;
import com.github.jrgen.test.domain.PrimitiveTypesBean;
import com.github.jrgen.typebuilder.ImmutableTypeBuilder;
import com.github.jrgen.typebuilder.TypeBuilder;
import com.github.jrgen.workflow.DefaultWorkflow;

public class WorkflowTest {
	
	private static final Log log = LogFactory.getLog(WorkflowTest.class);
	
	private DefaultWorkflow workflow;
	private JrgenContext jrgenContext;
	
	public WorkflowTest() {
		workflow = new DefaultWorkflow();
		jrgenContext = new JrgenContext();
	}

	@Test
	public void findWorkFlowTest() {
		ImmutableTypeBuilder imtb1 = new ImmutableTypeBuilder(jrgenContext);
		
		workflow.addTypeBuilder(imtb1);
		log.info(workflow);
		TypeBuilder<?> t = workflow.findTypeBuilder(ImmutableTypeBuilder.class);
		
		log.info(t);
		TestCase.assertNotNull(t);
		
		TestCase.assertTrue(workflow.removeTypeBuilder(ImmutableTypeBuilder.class));
		log.info(workflow);
	}
	
	@Test
	public void getCollectionFromWorkflowTest() {
		JavaType javaType = jrgenContext.getAbstractTypeHandler()
				.getObjectMapper().getTypeFactory()
					.constructCollectionType(Set.class, Integer.class);
		
		log.info(doWorkflow(javaType));
	}
	
	@Test
	public void getMapFromWorkFlowTest() {
		JavaType javaType = jrgenContext.getAbstractTypeHandler().getObjectMapper().getTypeFactory()
				.constructMapType(Map.class, String.class, Integer.class);
		
		log.info(doWorkflow(javaType));
	}
	
	@Test
	public void getEnumFromWorkFlowTest() {
		JavaType javaType = jrgenContext.getAbstractTypeHandler().getObjectMapper().getTypeFactory()
				.constructType(Name.Salutation.class);
		
		log.info(doWorkflow(javaType));
	}
	
	@Test
	public void getPersonFromWorkFlowTest() {
		jrgenContext.getSettings().setMaxContainerSize(1);
		jrgenContext.getSettings().setRandomContainerSize(false);
		JavaType javaType = jrgenContext.getAbstractTypeHandler()
				.getObjectMapper()
				.getTypeFactory()
				.constructType(Person.class);
		
		log.info(doWorkflow(javaType));
	}
	
	@Test
	public void generateUndefinedTest() {
		JavaType javaType = jrgenContext.getAbstractTypeHandler()
				.getObjectMapper()
				.getTypeFactory()
				.constructCollectionType(Set.class, Address.class);
		
		log.info(doWorkflow(javaType));
	}
	
	@Test
	public void generatePrimitiveTypesBeanTest() {
		JavaType javaType = jrgenContext.getAbstractTypeHandler().getObjectMapper()
				.constructType(PrimitiveTypesBean.class);
		
		log.info(doWorkflow(javaType));
	}
	
	@Test
	public void setSubtractionTest() {
		LinkedHashSet<Integer> set1 = new LinkedHashSet<Integer>();
		set1.addAll(Arrays.asList(1, 2, 3, 4, 5));
		
		LinkedHashSet<Integer> set2 = new LinkedHashSet<Integer>();
		set2.addAll(Arrays.asList(1, 3, 6));
		
		Collection<?> diff = CollectionUtils.subtract(set1, set2);
		log.info(diff);
	}
	
	private Object doWorkflow(JavaType javaType) {
		jrgenContext.initalizeContext();
		
		return jrgenContext.getWorkflow().workflow(javaType);
	}
}
