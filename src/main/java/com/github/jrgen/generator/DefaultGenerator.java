package com.github.jrgen.generator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.github.jrgen.settings.Settings;
import com.github.jrgen.typebuilder.PrimitiveTypeBuilder;
import com.github.jrgen.util.JrgenUtil;

/***
 * <p>
 * This class is used by the {@link PrimitiveTypeBuilder} to generate basic 
 * Java types. The following basic supported types are:
 * </p>
 * <ul>
 * <li>BigDecimal</li>
 * <li>BigInteger</li>
 * <li>Boolean</li>
 * <li>Byte</li>
 * <li>Character</li>
 * <li>Date</li>
 * <li>Double</li>
 * <li>Float</li>
 * <li>Integer</li>
 * <li>Long</li>
 * <li>Object (defaults to String)</li>
 * <li>Short</li>
 * <li>String</li>
 * </ul>
 * 
 * <p>
 * The {@link Settings} object is an essential component of this class and 
 * all of it's instances. It is used to obtain a range of values to choose
 * from for a given type. If the developer wants to change the range of 
 * types selected at random, they are advised to do so from the 
 * {@link Settings} object.
 * </p>
 * 
 * <p>
 * While a basic type can be obtained by calling the create method for that 
 * type, the method create() is most useful. Future versions of this class will
 * be interface driven and customizable by the developer, in essence allowing
 * the developer to define their own generator instead of this class.
 * </p>
 * 
 * @author Allan J. Shoulders
 * @version 1.0
 * @since 1.0.0
 * @see JrgenUtil
 *
 */
public class DefaultGenerator {
	
	private static final String ALPHA = "abcdefghijklmnopqrstuvwxyz";
	private static final String DIGITS = "0123456789";
	private static final String SPECIALS = "!@#$%^&*()-_+=}{[].,?~`|";
	private static final Calendar CAL = Calendar.getInstance();
	private static final Random RANDOM = new Random();
	
	private enum StringTypes {
		ALPHA_STR, DIGIT_STR, SPECIAL_STR
	}
	
	private enum Months {
		JAN(31), FEB(28), MAR(31), APR(30), MAY(31), JUN(30), JUL(31), AUG(31),
		SEP(30), OCT(31), NOV(30), DEC(31);
		
		private int days;
		
		private Months(int days) {
			this.days = days;
		}
		
		public int getDays() {
			return days;
		}
	}
	
	private Settings settings;

	/**
	 * Constructs a new instance of this class. {@link Settings} object is
	 * required.
	 * 
	 * @param settings the {@link Settings} object this instance will use.
	 */
	public DefaultGenerator (Settings settings) {
		this.setSettings(settings);
	}
	
	/**
	 * This method creates a random {@link Character}.
	 * 
	 * @return a random {@link Character} that is a digit or alpha.
	 */
	public Character createCharacter() {
		StringBuilder sb = new StringBuilder(ALPHA).append(DIGITS);
		
		return sb.charAt(RANDOM.nextInt(sb.length()));
	}
	
	/**
	 * This method creates a random {@link String}. By default the
	 * String will be a combination of alpha and digits. This can be changed
	 * via the {@link Settings}.
	 * 
	 * @return a random {@link String} that is combination of digits and alpha
	 * by default.
	 */
	public String createString () {
		StringBuilder sb = new StringBuilder();
		List<StringTypes> strTypeChoices = new ArrayList<StringTypes>();
		
		if (settings.isAlphaUsed()) {
			strTypeChoices.add(StringTypes.ALPHA_STR);
		}
		
		if (settings.isDigitsUsed()) {
			strTypeChoices.add(StringTypes.DIGIT_STR);
		}
		
		if (settings.isSpecialsUsed()) {
			strTypeChoices.add(StringTypes.SPECIAL_STR);
		}
		
		if (strTypeChoices.isEmpty()) {
			return null;
		}
		
		for (int i=0; i < settings.getDefaultStringLength(); i++) {
			StringTypes type = 
					strTypeChoices
						.get(RANDOM.nextInt(strTypeChoices.size()));
			
			if (type == StringTypes.ALPHA_STR) {
				char c = ALPHA.charAt(RANDOM.nextInt(ALPHA.length()));
				
				if (RANDOM.nextBoolean()) {
					sb.append(c);
				} else {
					sb.append(Character.toUpperCase(c));
				}
			} else if (type == StringTypes.DIGIT_STR) {
				sb.append(DIGITS.charAt(RANDOM.nextInt(DIGITS.length())));
			} else if (type == StringTypes.SPECIAL_STR) {
				sb.append(SPECIALS.charAt(RANDOM.nextInt(SPECIALS.length())));
			}
		}
		
		return sb.toString();
	}
	
	/**
	 * This method returns a {@link Byte}. By default, the value of the 
	 * {@link Byte} returned will be between 0 and it's largest value.
	 * 
	 * @return {@link Byte} chosen at random.
	 * @throws IllegalArgumentException when the maximum value is less 
	 * than the minimum value set in the {@link Settings} object.
	 */
	public Byte createByte() {
		return (byte)JrgenUtil.getNumberFromRange(settings.getMinByteValue(), 
				settings.getMaxByteValue());
	}
	
	/***
	 * This method returns a {@link Short}. By default, the value of the 
	 * {@link Short} returned will be between 0 and 1,000.
	 * 
	 * @return {@link Short} chosen at random.
	 * @throws IllegalArgumentException when the maximum value is less 
	 * than the minimum value set in the {@link Settings} object.
	 */
	public Short createShort() {
		return (short) JrgenUtil.getNumberFromRange(settings.getMinShortValue(), 
				settings.getMaxShortValue());
	}
	
	/***
	 * This method returns a {@link Integer}. By default, the value of the 
	 * {@link Integer} returned will be between 0 and 10,000;
	 * 
	 * @return {@link Integer} chosen at random.
	 * @throws IllegalArgumentException when the maximum value is less 
	 * than the minimum value set in the {@link Settings} object.
	 */	
	public Integer createInteger() {		
		return (int) JrgenUtil.getNumberFromRange(settings.getMinIntValue(), 
				settings.getMaxIntValue());
	}
	
	/***
	 * This method returns a {@link Long}. By default, the value of the 
	 * {@link Long} returned will be between 0 and 100,000.
	 * 
	 * @return {@link Long} chosen at random.
	 * @throws IllegalArgumentException when the maximum value is less 
	 * than the minimum value set in the {@link Settings} object.
	 */	
	public Long createLong() {
		return JrgenUtil.getNumberFromRange(settings.getMinLongValue(),
				settings.getMaxLongValue());		
	}
	
	/***
	 * This method returns a {@link Float}. By default, the value of the 
	 * {@link Float} returned will be between 0 and 10000.0
	 * 
	 * @return {@link Float} chosen at random.
	 * @throws IllegalArgumentException when the maximum value is less 
	 * than the minimum value set in the {@link Settings} object.
	 */	
	public Float createFloat() {
		return (float) JrgenUtil.getNumberFromRange(settings.getMinFloatValue(), 
				settings.getMaxFloatValue());
	}
	
	/***
	 * This method returns a {@link Double}. By default, the value of the 
	 * {@link Double} returned will be between 0 and 100000.0
	 * 
	 * @return {@link Double} chosen at random.
	 * @throws IllegalArgumentException when the maximum value is less 
	 * than the minimum value set in the {@link Settings} object.
	 */	
	public Double createDouble() {
		return JrgenUtil.getNumberFromRange(settings.getMinDoubleValue(), 
				settings.getMaxDoubleValue());
	}
	
	/***
	 * This method returns a {@link Date}. By default, the value of the 
	 * {@link Date} returned will be any random month, a random day within
	 * the selected month, and a year between 1900 and the current year.
	 * 
	 * @return {@link Date} chosen at random.
	 * @throws IllegalArgumentException when the maximum or minimum year is a 
	 * negative number.
	 * @throws IllegalArgumentException when the maximum year is less 
	 * than the minimum year set in the {@link Settings} object.
	 */	
	public Date createDate() {
		int minYear = settings.getMinYear();
		int maxYear = settings.getMaxYear();
		
		JrgenUtil.validatePositiveNumber(minYear);
		JrgenUtil.validatePositiveNumber(maxYear);
		
		int year = (int) JrgenUtil.getNumberFromRange(minYear, maxYear);
		Months month = Months.values()[RANDOM.nextInt(Months.values().length)];
		
		CAL.clear();
		CAL.set(year, 
				month.ordinal(), 
				RANDOM.nextInt(month.getDays()),
				RANDOM.nextInt(24), 
				RANDOM.nextInt(60));
		
		return CAL.getTime();
	}
	
	/***
	 * This method accepts a given {@link Class} type and returns an instance
	 * of that class type, if it is supported. This is in most cases done by
	 * simply calling the create method matching the type that was passed in.
	 * 
	 * @param <T> the parameterized type.
	 * @param objType the class type of the object to be created and returned.
	 * @return an instance of the objType parameter passed in if supported.
	 * @throws IllegalArgumentException if the class passed in as a parameter
	 * is not a supported type.
	 */
	public <T> Object createValue (Class<T> objType) throws IllegalArgumentException {
		if (objType == Integer.class || objType == Number.class) {
			return createInteger();
		}
		
		if (objType == BigDecimal.class) {
			return new BigDecimal(RANDOM.nextDouble());
		}
		
		if (objType == BigInteger.class) {
			return new BigInteger(
					Integer.toString(
							RANDOM.nextInt(Integer.MAX_VALUE)));
		}
		
		if (objType == Byte.class) {
			return createByte();
		}
		
		if (objType == Character.class) {
			return createCharacter();
		}
		
		if (objType == Short.class) {
			return createShort();
		}
		
		if (objType == Long.class) {
			return createLong();
		}
		
		if (objType == Double.class) {
			return createDouble();
		}
		
		if (objType == Float.class) {
			return createFloat();
		}
		
		if (objType == Date.class) {
			return createDate();
		}
		
		if (objType == Boolean.class) {
			return RANDOM.nextBoolean();
		}
		
		if (objType == String.class || objType == Object.class) {
			return createString();
		}
		
		throw new IllegalArgumentException(JrgenUtil.getMessages()
				.getString("defaultgenerator.unsupportedType.exception"));
	}

	/**
	 * Getter method that returns the current instance of {@link Settings}
	 * associate with this instance of DefaultGenerator.
	 * 
	 * @return {@link Settings} instance.
	 */
	public Settings getSettings() {
		return settings;
	}
	
	/**
	 * Setter method that set the {@link Settings} object class to the 
	 * {@link Settings} instance passed in as a parameter.
	 * 
	 * @param settings {@link Settings} instance this instance of 
	 * DefaultGenerator will use.
	 */
	public void setSettings(Settings settings) {
		this.settings = settings;
	}
	
}
