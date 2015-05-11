package com.github.jrgen.test.core;

import java.util.Arrays;
import java.util.Collection;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import com.github.jrgen.util.JrgenUtil;

@RunWith(Parameterized.class)
public class UtilitiesTest {

	@Parameters
	public static Collection<Object[]> rangeParams() {
		return Arrays.asList(new Object[][]{
				{7000, 10000}, {0, 5}, {10, 20}, {9, 10},
				{-100, -10}, {-10000, 5}, {Integer.MIN_VALUE, Integer.MAX_VALUE},
				{10, 10}, {-10, -10}
		});
	}
	
	@Parameter
	public int min;
	
	@Parameter(value=1)
	public int max;
	
	@Test
	public void testWholeNumberRange() {
		for (int i = 0; i < 10000; i++) {
			long num = JrgenUtil.getNumberFromRange(min, max);
			TestCase.assertTrue(num <= max && num >= min);
		}
	}
}
