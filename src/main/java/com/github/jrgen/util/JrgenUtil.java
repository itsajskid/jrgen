package com.github.jrgen.util;

import java.util.Properties;
import java.util.Random;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import com.github.jrgen.generator.DefaultGenerator;

/**
 * <p>
 * JrgenUtil is a simple Utility class with utility methods used in various 
 * classes. This class is used extensively by the {@link DefaultGenerator}
 * class.
 * </p>
 * 
 * <p>
 * This class has very little use outside of Jrgen.
 * </p>
 * 
 * @author Allan J. Shoulders
 * @version 1.0
 * @since 1.0.0
 * @see DefaultGenerator
 *
 */
public class JrgenUtil {
	
	private static final Configuration MESSAGES;
	private static final Random RANDOM;
	
	static {
		RANDOM = new Random();
		
		try {
			MESSAGES = new PropertiesConfiguration("messages.properties");
		} catch (ConfigurationException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Private constructor to prevent instantiation of a utility class 
	 * which has mostly static methods.
	 */
	private JrgenUtil() {
		super();
	}
	
	/**
	 * Returns a static {@link Configuration} instance used for retrieving 
	 * messages from a {@link Properties} file.
	 * 
	 * @return the static {@link Configuration} instance.
	 */
    public static Configuration getMessages () {
    	return MESSAGES;
    }
    
    /**
     * Returns a randomly generated double bound by the minimum and 
     * maximum values.
     * 
     * @param min the smallest possible value that can be returned.
     * @param max the largest possible value that can be returned.
     * @return a randomly generated value greater than or equal to the 
     * min and less than or equal to the max.
     * 
     * @throws IllegalArgumentException if the max value is greater than the 
     * min value.
     */
    public static double getNumberFromRange(double min, double max) {
    	validateMaxMin(min, max);
    	return min + (RANDOM.nextDouble() * (max-min));
    }
    
    /**
     * Returns a randomly generated long bound by the minimum and 
     * maximum values
     * 
     * @param min the smallest possible value that can be returned.
     * @param max the largest possible value that can be returned.
     * @return a randomly generated value greater than or equal to the 
     * min and less than or equal to the max.
     * 
     * @throws IllegalArgumentException if the max value is greater than the 
     * min value.
     */
    public static long getNumberFromRange (long min, long max) {
    	validateMaxMin(min, max);
    	
    	if (max - min == 1) {
    		return RANDOM.nextBoolean() ? min : max;
    	}    	
    	
    	return min + (long)(RANDOM.nextDouble() * (max - min));
    }
    
    /**
     * Validates that the max is not less than the min. If this statement is 
     * false, then an {@link IllegalArgumentException} is thrown.
     * 
     * @param min the smallest possible value that can be returned.
     * @param max the largest possible value that can be returned.
     * 
     * @throws IllegalArgumentException if the max value is greater than the 
     * min value.
     */
    public static void validateMaxMin (long min, long max) {
		if (max < min) {
			throw minGreaterThanMax(min, max);
		}
    }
    
    /**
     * Validates that the max is not less than the min. If this statement is 
     * false, then an {@link IllegalArgumentException} is thrown.
     * 
     * @param min the smallest possible value that can be returned.
     * @param max the largest possible value that can be returned.
     * 
     * @throws IllegalArgumentException if the max value is greater than the 
     * min value.
     */
    public static void validateMaxMin (double min, double max) {
    	if (max < min) {
    		throw minGreaterThanMax(min, max);
    	}
    }
    
    /**
     * Validates that the number is a non-negative number. If this is false, 
     * then an {@link IllegalArgumentException} is thrown.
     * 
     * @param num the number to validate as a non-negative number.
     * @throws IllegalArgumentException if the number is a negative number.
     */
    public static void validatePositiveNumber (long num) {
    	if (num < 0) {
    		throw negativeNumber(num);
    	}
    }
    
    /**
     * Validates that the parameter passed is not null. If the parameter is 
     * null an {@link IllegalArgumentException} is thrown.
     * 
     * @param param the object reference that is checked against a 
     * null value.
     * @throws IllegalArgumentException if the param reference is null.
     */
    public static void validateNonNullArgument (Object param, 
    		String paramName) {
		if (param == null) {
			String message = getMessages()
					.getString("nonnull.argument.exception");
			
			throw new IllegalArgumentException(String.format(message, 
					paramName));
		}    	
    }
    
	private static <T extends Number> IllegalArgumentException minGreaterThanMax (
			T min, T max) {
		return new IllegalArgumentException(
				String.format("min(%s) cannot be greater than max(%s)", 
						min, max));
	}
	
	private static <T extends Number> IllegalArgumentException negativeNumber (
			T num) {
		return new IllegalArgumentException(
				String.format("Cannot be less zero (%s).", num));
	}    
	
}
