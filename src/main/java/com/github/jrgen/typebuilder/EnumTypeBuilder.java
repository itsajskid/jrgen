package com.github.jrgen.typebuilder;

import java.util.EnumSet;
import java.util.Random;

import com.fasterxml.jackson.databind.JavaType;

/**
 * <p>
 * The EnumTypeBuilder is an implementation of the {@link TypeBuilder} 
 * interface. This implementation returns an {@link Enum} of some yet to 
 * be determined type.
 * </p>
 * 
 * <p>
 * The {@link Enum} returned is one of the values defined in the {@link Enum} 
 * that is to be handled.
 * </p>
 * 
 * @author Allan J. Shoulders
 * @version 1.0
 * @since 1.0.0
 * @see TypeBuilder
 *
 */
public class EnumTypeBuilder implements TypeBuilder<Enum<?>> {

	private Random random;
	
	/**
	 * Constructs a new instance of EnumTypeBuilder.
	 * 
	 */
	public EnumTypeBuilder() {
		random = new Random();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Enum<?> build(JavaType javaType) {
		
		if (!javaType.isEnumType()) {
			return null;
		}
		
		Class<? extends Enum> enumClass = 
				(Class<? extends Enum>) javaType.getRawClass();
		
		Object[] enumObjs = 
				EnumSet.allOf(enumClass).toArray();
		
		return (Enum<?>)enumObjs[random.nextInt(enumObjs.length)];
	}

}
