package com.github.jrgen.test.core;

import java.lang.reflect.InvocationTargetException;

import junit.framework.TestCase;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import com.github.jrgen.generator.DefaultGenerator;
import com.github.jrgen.settings.Settings;

public class DefaultGeneratorTest {
	
	private static Log log = LogFactory.getLog(DefaultGeneratorTest.class);
	
	public static Byte[][] byteData = new Byte[][] {
		{0, 127}, {-128, 0}, {Byte.MIN_VALUE, Byte.MAX_VALUE},
		{50, 100}, {-100, -50}, {0, 0}, {0, 1}, {-10, 10}, {-1, 0}
	};
	
	public static Short[][] shortData = new Short[][] {
		{0, 1000}, {-1000, 1000}, {-1000, 0}, {0, 1000},
		{Short.MIN_VALUE, Short.MAX_VALUE}, {10, 10}, {-10, 10},
		{-300, -200}
	};
	
	public static Integer[][] intData = new Integer[][] {
		{100, 500}, {100, 1000}, {0, 10}, {-1, 0}, {-40, -1},
		{Integer.MIN_VALUE, Integer.MAX_VALUE}, {9, 9}, {0, 0}, {-1, -1},
		{-5, 5}
	};
	
	public static Long[][] longData = new Long[][] {
		{0l, 100000l}, {-100000l, 0l}, {-100000l, 100000l},
		{Long.MIN_VALUE, Long.MAX_VALUE}, {0l, 1l}, {2l, 2l},
		{5l, 10l}, {-20l, -10l}
	};
	
	public static Double[][] doubleData = new Double[][] {
		{800.897, 801.00}, {-109.89, 0d}, {0d, 456.89393},
		{3.14, 3.14}, {0.0001, 0.0002}, {10d, 15d},
		{Double.MIN_VALUE, Double.MAX_VALUE}, {0.0, 1.0}
	};
	
	public static Float[][] floatData = new Float[][] {
		{800.897f, 801.00f}, {-109.89f, 0f}, {0f, 456.89393f},
		{3.14f, 3.14f}, {0.0001f, 0.0002f}, {10f, 15f},
		{Float.MIN_VALUE, Float.MAX_VALUE}, {0.0f, 1.0f}		
	};
	
	private Settings settings;
	private DefaultGenerator dg;
	
	public DefaultGeneratorTest() {
		settings = Settings.getInstance();
		dg = new DefaultGenerator(settings);
	}
	
	@Test
	public void createCharacterTest() {
		for (int i = 0; i < 20; i++) {
			Character c = dg.createCharacter();
			log.info(c);
			TestCase.assertTrue(c != null);
		}
	}
	
	@Test
	public void createStringTest() {		
		for (int i = 0; i < 10; i++ ) {
			String s = dg.createString();
			log.info(s);
			TestCase.assertTrue(StringUtils.containsNone(s, 
					"!@#$%^&*()-_+=}{[].,?~`|"));
		}
	}	
	
	@Test
	public void createByteTest() {
		for (Byte[] b : byteData) {
			settings.setMinByteValue(b[0]);
			settings.setMaxByteValue(b[1]);
			
			createTest("createByte", 
					settings.getMinByteValue(), 
					settings.getMaxByteValue());
		}
	}
	
	@Test
	public void createShortTest() {
		for (Short[] s : shortData) {
			settings.setMinShortValue(s[0]);
			settings.setMaxShortValue(s[1]);
			
			createTest("createShort",
					settings.getMinShortValue(),
					settings.getMaxShortValue());
		}
	}
	
	@Test
	public void createIntegerTest() {
		for (Integer[] i : intData) {
			settings.setMinIntValue(i[0]);
			settings.setMaxIntValue(i[1]);
			
			createTest("createInteger", 
					settings.getMinIntValue(), 
					settings.getMaxIntValue());	
		}
	}
	
	@Test
	public void createLongTest() {
		for (Long[] l : longData) {
			settings.setMinLongValue(l[0]);
			settings.setMaxLongValue(l[1]);
			
			createTest("createLong",
					settings.getMinLongValue(),
					settings.getMaxLongValue()
					);
		}
	}	
	
	@Test
	public void createFloatTest() {
		for (Float[] f: floatData) {
			settings.setMinFloatValue(f[0]);
			settings.setMaxFloatValue(f[1]);
			
			createTest("createFloat",
					settings.getMinFloatValue(),
					settings.getMaxFloatValue());
		}
	}
	
	@Test
	public void createDoubleTest() {		
		for (Double[] d : doubleData) {
			settings.setMinDoubleValue(d[0]);
			settings.setMaxDoubleValue(d[1]);
			
			createTest("createDouble", 
					settings.getMinDoubleValue(), 
					settings.getMaxDoubleValue());	
		}
	}
	
	@Test
	public void createDateTest() {
		DefaultGenerator dg = 
				new DefaultGenerator(Settings.getInstance());
		
		for (int i = 0; i < 50; i++) {
			log.info(dg.createDate());
		}
	}	
	
	@SuppressWarnings("unchecked")
	public <T extends Comparable<T>>void createTest(String createMethod, T min, T max) {
		try {	
			for (int i = 0; i < 10000; i++ ) {			
				T genNum = (T) dg.getClass()
						.getMethod(createMethod)
						.invoke(dg);
				
				boolean ltEqMax = genNum.compareTo(max) <= 0;
				boolean gtEqMin = genNum.compareTo(min) >= 0;
				
				if (i < 5)  {
					log.info(String.format("min: %s, max: %s, generated: %s\n",
							min, 
							max, 
							genNum));
				}  
				
				if ((!ltEqMax || !gtEqMin)) {
					log.error(String.format("min: %s, max: %s, generated: %s\n",
							min, 
							max, 
							genNum));					
				}
				
				TestCase.assertTrue(gtEqMin);
				TestCase.assertTrue(ltEqMax);
			}	
		} catch (IllegalArgumentException e) {
			log.error("See exception below", e);
			throw new RuntimeException(e);
		} catch (SecurityException e) {
			log.error("See exception below", e);
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			log.error("See exception below", e);
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			log.error("See exception below", e);
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			log.error("See exception below", e);
			throw new RuntimeException(e);
		}		
	}

}
