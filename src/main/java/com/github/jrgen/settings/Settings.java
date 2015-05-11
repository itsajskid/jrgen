package com.github.jrgen.settings;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import com.github.jrgen.context.JrgenContext;
import com.github.jrgen.generator.DefaultGenerator;
import com.github.jrgen.typebuilder.TypeBuilder;

/***
 * <p>
 * The Settings class allows the developer using Jrgen to customize the default
 * behavior to their liking. It is an essential component of Jrgen and is used
 * ubiquitously throughout Jrgen library.
 * </p>
 * 
 * <p>
 * It is used by the {@link JrgenContext} to discover the location of resource
 * files. It is used by the {@link DefaultGenerator} to determine a ranges of
 * values it must generate, sizes of containers 
 * ({@link java.util.Collection}s, {@link java.util.Map}s, etc. It is also 
 * used by the various supported {@link TypeBuilder}s.
 * </p>
 * 
 * <p>
 * Because of the existence of so many available options, there are no
 * constructors for Settings instances. The static <i>getInstance</i>
 * method should be used to obtain an instance. The instance has default
 * values set to it, that may be changed or overridden.
 * </p>
 * 
 * <p>
 * Default Values:
 * </p>
 * <ul>
 * <li>extension = JSON</li>
 * <li>resources = /jrgen-resources</li>
 * <li>randomContainerSize = true</li>
 * <li>alphaUsed = true</li>
 * <li>digitsUsed = true</li>
 * <li>specialsUsed = false</li>
 * <li>defaultStringLength = 20</li>
 * <li>maxContainerSize = 10</li>
 * <li>maxIntValue = 10000</li>
 * <li>maxLongValue = 100000</li>
 * <li>maxShortValue = 1000</li>
 * <li>maxByteValue = Byte.MAX_VALUE</li>
 * <li>maxFloatValue = 10000.0</li>
 * <li>maxDoubleValue = 100000.0</li>
 * <li>maxYear = &lt;the current year&gt;</li>
 * <li>minLongValue = 0</li>
 * <li>minIntValue = 0</li>
 * <li>minShortValue = 0</li>
 * <li>minByteValue = 0</li>
 * <li>minFloatValue = 0.0</li> 
 * <li>minDoubleValue = 0.0</li>
 * <li>minYear = 1900</li>
 * <li>minContainerSize = 0</li>
 * <li>defaultContainerSize = 10</li>
 * <li>generateUndefined = true</li>      
 * <li>nullOnEmptySet = false</li>      
 * <li>isNotNull = false</li>                                
 * </ul>
 * 
 * @author Allan J. Shoulders
 * @version 1.0
 * @since 1.0.0
 * 
 * @see DefaultGenerator
 * @see JrgenContext
 */
public class Settings {
	
	private final Set<URI> resources;
	private final Calendar cal;
	private FileType extension;
	
	private boolean alphaUsed;
	private boolean digitsUsed;
	private boolean specialsUsed;
	private int defaultStringLength;
	
	private int maxIntValue;
	private int minIntValue;
	
	private boolean generateUndefined;
	
	private byte maxByteValue;
	private byte minByteValue;
	
	private short maxShortValue;
	private short minShortValue;
	
	private float maxFloatValue;
	private float minFloatValue;
	
	private double maxDoubleValue;
	private double minDoubleValue;
	
	private long maxLongValue;
	private long minLongValue;
	
	private int minYear;
	private int maxYear;
	
	private boolean randomContainerSize;
	private boolean nullOnEmptySet;
	private boolean isNotNull;
	private int defaultContainerSize;
	private int maxContainerSize;
	private int minContainerSize;
	
	public enum FileType {
		JSON(".json");
		
		private String fileType;
		
		private FileType(String fileType) {
			this.fileType = fileType;
		}
		
		@Override
		public String toString() {
			return fileType;
		}
	}
	
	/***
	 * This method is used to obtain an instance of the Settings object.
	 * The instance returned contains the default values listed in the class
	 * description above.
	 * 
	 * @return an instance of Settings with the default values set.
	 */
	public static Settings getInstance() {		
		try {
			return getInstanceCE();
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}
	
	/***
	 * Same as the <i>getInstance</i> method only that calling method is 
	 * responsible for handling any checked exceptions.
	 * 
	 * @return an instance of Settings with the default values set.
	 * @throws URISyntaxException when the URI does not conform to syntax of 
	 * a valid URI.
	 */
	public static Settings getInstanceCE () throws URISyntaxException {
		Settings settings = new Settings();
		settings.extension = FileType.JSON;
		settings.resources.add(new URI("/jrgen-resources"));
		settings.randomContainerSize = true;

		settings.alphaUsed = true;
		settings.digitsUsed = true;
		settings.specialsUsed = false;
		settings.defaultStringLength = 20;
		
		settings.maxContainerSize = 10;
		settings.maxIntValue = 10000;
		settings.maxLongValue = 100000;
		settings.maxShortValue = 1000;
		settings.maxByteValue = Byte.MAX_VALUE;
		settings.maxFloatValue = 10000.0f;
		settings.maxDoubleValue = 100000.0;
		settings.maxYear = settings.cal.get(Calendar.YEAR);
		
		settings.minLongValue = 0;
		settings.minIntValue = 0;
		settings.minShortValue = 0;
		settings.minByteValue = 0;
		settings.minFloatValue = 0.0f;
		settings.minDoubleValue = 0.0;
		settings.minYear = 1900;
		settings.minContainerSize = 0;
		
		settings.defaultContainerSize = 10;
		settings.generateUndefined = true;
		settings.nullOnEmptySet = false;
		settings.isNotNull = false;
		
		return settings;
	}
	
	private Settings() {
		this.resources = new LinkedHashSet<URI>();
		this.cal = Calendar.getInstance();
	}

	/***
	 * Getter method to return the {@link FileType} extension.
	 * 
	 * @return the {@link FileType} extension. 
	 */
	public FileType getExtension() {
		return extension;
	}
	
	/***
	 * Getter method to return the JSON resource location.
	 * 
	 * @return the JSON location(s) as a {@link Set} of {@link URI}s. 
	 */	
	public Set<URI> getResources() {
		return resources;
	}

	/***
	 * Getter method to return the current maxContainerSize. The max container
	 * size represents the largest number of elements in any {@link java.util.Collection}
	 * or {@link java.util.Map} that is generated.
	 * 
	 * @return int current maxContainerSize. 
	 */	
	public int getMaxContainerSize() {
		return maxContainerSize;
	}

	/***
	 * Setter method to set the current maxContainerSize. The maxContainerSize
	 * must be an integer greater than 0.
	 * 
	 * @param maxContainerSize the value to set the maxContainerSize.
	 * @throws IllegalArgumentException when then maxContainerSize parameter is
	 * less than 0.
	 */
	public void setMaxContainerSize(int maxContainerSize) {
		if (maxContainerSize < 0) {
			throw new 
				IllegalArgumentException("settings.illegal.containerSize");
		}
		
		this.maxContainerSize = maxContainerSize;
	}

	/***
	 * Getter method to return the minContainerSize. The minContainerSize
	 * represents the lowest number of elements in any {@link Collection}
	 * or {@link java.util.Map} that is generated.
	 * 
	 * @return int current minContainerSize.
	 */
	public int getMinContainerSize() {
		return minContainerSize;
	}

	/**
	 * Setter method to set the current minContainerSize. The maxContainerSize
	 * must be an integer greater than 0.
	 * 
	 * @param minContainerSize the value to set the maxContainerSize.
	 * @throws IllegalArgumentException when then minContainerSize parameter is
	 * less than 0.
	 */
	public void setMinContainerSize(int minContainerSize) {
		if (minContainerSize < 0) {
			throw new 
				IllegalArgumentException("settings.illegal.containerSize");
		}
		
		this.minContainerSize = minContainerSize;
	}

	/***
	 * Getter method to return ({@link Collection}, 
	 * {@link java.util.Map}, or Array) that is generated when the 
	 * randomContainerSize is <b>false</b>.
	 * 
	 * @return int current defaultContainerSize.
	 */
	public int getDefaultContainerSize() {
		return defaultContainerSize;
	}

	/***
	 * Setter method to set ({@link Collection}, {@link java.util.Map}, or Array) 
	 * that is generated when the randomContainerSize is <b>false</b>.
	 * 
	 * @param defaultContainerSize the value to set the defaultContainerSize.
	 */
	public void setDefaultContainerSize(int defaultContainerSize) {
		this.defaultContainerSize = defaultContainerSize;
	}

	/***
	 * Getter method to return a boolean value of true when a generated
	 * container ({@link Collection}, {@link java.util.Map}, or Array) should have
	 * a random number of elements between the minContainerSize and the
	 * maxContainerSize. A return value of false indicates that a 
	 * generated container should have a fixed size to that of the
	 * defaultContainerSize value.
	 * 
	 * @return boolean true or false indicating if generated container sizes
	 * are random or fixed.
	 */
	public boolean isRandomContainerSize() {
		return randomContainerSize;
	}

	/***
	 * Setter method to set the current randomContainerSize. A value of 
	 * true indicates a generated container ({@link Collection}, 
	 * {@link java.util.Map}, or Array) should have a random number of 
	 * elements between the minContainerSize and the maxContainerSize. A 
	 * value of false indicates that a generated container should have a 
	 * fixed size to that of the defaultContainerSize value.
	 * 
	 * @param randomContainerSize boolean true or false indicating if 
	 * generated container sizes are random or fixed.
	 */
	public void setRandomContainerSize(boolean randomContainerSize) {
		this.randomContainerSize = randomContainerSize;
	}

	/**
	 * Getter method to return boolean value to indicate if a null value should
	 * be returned instead of an empty set when a container is generated. A
	 * value of true indicates a null value will be returned, a value of false 
	 * indicates an empty set will be returned if generated.
	 * 
	 * @return boolean value to indicate that a null value should be returned 
	 * rather than an empty set. 
	 */
	public boolean isNullOnEmptySet() {
		return nullOnEmptySet;
	}

	/***
	 * Setter method to set boolean value to indicate if a null value should
	 * be returned instead of an empty set when a container is generated. A
	 * value of true indicates a null value will be returned, a value of false 
	 * indicates an empty set will be returned if generated.
	 * 
	 * @param nullOnEmptySet boolean value to indicate that a null value 
	 * should be returned rather than an empty set. 
	 */
	public void setNullOnEmptySet(boolean nullOnEmptySet) {
		this.nullOnEmptySet = nullOnEmptySet;
	}

	/***
	 * Getter method to return a boolean value of true to indicate that if 
	 * Jrgen cannot resolve an object to be generated, then return an instance 
	 * of that object using the default constructor (if present.) A false 
	 * value would indicate that null value will be returned if Jrgen is
	 * unable to resolve an object.
	 * 
	 * @return boolean true to return an instance if an object cannot be
	 * generated, false to indicate a null value should be returned when an 
	 * object cannot be generated.
	 */
	public boolean isNotNull() {
		return isNotNull;
	}

	/***
	 * Setter method to set a boolean value of true to indicate that if 
	 * Jrgen cannot resolve an object to be generated, then return an instance 
	 * of that object using the default constructor (if present.) A false 
	 * value would indicate that null value will be returned if Jrgen is
	 * unable to resolve an object.
	 * 
	 * @param isNotNull boolean true to return an instance if an object cannot 
	 * be generated, false to indicate a null value should be returned when an 
	 * object cannot be generated.
	 */
	public void setNotNull(boolean isNotNull) {
		this.isNotNull = isNotNull;
	}

	/**
	 * Getter method returns a boolean value true to indicate that a generated 
	 * {@link String} value will have alphabetical characters. A false value
	 * will indicate that the generated {@link String} will not have any 
	 * alphabetical characters.
	 * 
	 * @return boolean true to include alphas in generated {@link String}s,
	 * false to exclude them.
	 */
	public boolean isAlphaUsed() {
		return alphaUsed;
	}

	/***
	 * Setter method sets a boolean value true to indicate that a generated 
	 * {@link String} value will have alphabetical characters. A false value
	 * will indicate that the generated {@link String} will not have any 
	 * alphabetical characters.
	 * 
	 * @param alphaUsed boolean true to include alphas in generated 
	 * {@link String}s, false to exclude them.
	 */
	public void setAlphaUsed(boolean alphaUsed) {
		this.alphaUsed = alphaUsed;
	}

	/***
	 * Getter method returns a boolean value true to indicate that a generated 
	 * {@link String} value will have numeric characters. A false value will
	 * indicate that the generated {@link String} will not have any numeric 
	 * characters
	 * 
	 * @return boolean value of true to include numeric characters in a 
	 * generated {@link String} false otherwise.
	 */
	public boolean isDigitsUsed() {
		return digitsUsed;
	}

	/***
	 * Setter method sets a boolean value true to indicate that a generated 
	 * {@link String} value will have numeric characters. A false value will
	 * indicate that the generated {@link String} will not have any numeric 
	 * characters
	 * 
	 * @param digitsUsed boolean value of true to include numeric characters 
	 * in a generated {@link String} false otherwise.
	 */
	public void setDigitsUsed(boolean digitsUsed) {
		this.digitsUsed = digitsUsed;
	}

	/***
	 * Getter method returns a boolean value true to indicate that a generated
	 * {@link String} value will have special characters. A false value will 
	 * indicate that the generated {@link String} will not have any special
	 * characters.
	 * 
	 * @return boolean value of true when the generated String has special 
	 * characters, false otherwise.
	 */
	public boolean isSpecialsUsed() {
		return specialsUsed;
	}

	/***
	 * Setter method sets a boolean value true to indicate that a generated
	 * {@link String} value will have special characters. A false value will 
	 * indicate that the generated {@link String} will not have any special
	 * characters.
	 * 
	 * @param specialsUsed boolean value of true when the generated String 
	 * has special characters, false otherwise.
	 */
	public void setSpecialsUsed(boolean specialsUsed) {
		this.specialsUsed = specialsUsed;
	}

	/***
	 * Getter method returns an int value indicating how long a generated 
	 * {@link String} will be.
	 * 
	 * @return the length of a generated String.
	 */
	public int getDefaultStringLength() {
		return defaultStringLength;
	}

	/**
	 * Setter method set an int value indicating how long a generated 
	 * {@link String} will be.
	 * 
	 * @param defaultStringLength the length of a generated String.
	 * @throws IllegalArgumentException when the defaultStringLength parameter 
	 * is less than zero.
	 */
	public void setDefaultStringLength(int defaultStringLength) {
		if (defaultStringLength < 0) {
			throw new 
				IllegalArgumentException("settings.illegal.stringLength");
		}
		
		this.defaultStringLength = defaultStringLength;
	}

	/**
	 * Getter method returns an int indicate the maximum value a randomly
	 * generated integer can have.
	 * 
	 * @return the maximum value of a randomly generated integer.
	 */
	public int getMaxIntValue() {
		return maxIntValue;
	}

	/**
	 * Setter method sets an int to indicate the maximum value a randomly
	 * generated integer can have.
	 * 
	 * @param maxIntValue the maximum value of a randomly generated integer.
	 */
	public void setMaxIntValue(int maxIntValue) {
		this.maxIntValue = maxIntValue;
	}

	/**
	 * Getter method returns an int to indicate the minimum value a randomly
	 * generated integer can have.
	 * 
	 * @return the minimum value of a randomly generated integer.
	 */
	public int getMinIntValue() {
		return minIntValue;
	}

	/**
	 * Setter method set an int to indicate the minimum value a randomly
	 * generated integer can have.
	 * 
	 * @param minIntValue the minimum value of a randomly generated integer.
	 */
	public void setMinIntValue(int minIntValue) {
		this.minIntValue = minIntValue;
	}

	/**
	 * Getter method to return boolean value indicating if Jrgen will generate
	 * a value for properties that are not defined in the JSON resource file
	 * for the respective class type.
	 * 
	 * @return boolean true to indicate generation when a property is not 
	 * defined in a JSON resource file or false otherwise.
	 */
	public boolean isGenerateUndefined() {
		return generateUndefined;
	}

	/**
	 * Setter method to set boolean value indicating if Jrgen will generate
	 * a value for properties that are not defined in the JSON resource file
	 * for the respective class type.
	 * 
	 * @param generateUndefined boolean true to indicate generation when a 
	 * property is not defined in a JSON resource file or false otherwise.
	 */
	public void setGenerateUndefined(boolean generateUndefined) {
		this.generateUndefined = generateUndefined;
	}

	/**
	 * Getter method returns a long to indicate the maximum value a randomly
	 * generated long can have.
	 * 
	 * @return the maximum value of a randomly generated long.
	 */
	public long getMaxLongValue() {
		return maxLongValue;
	}

	/**
	 * Setter method sets a long to indicate the maximum value a randomly
	 * generated long can have.
	 * 
	 * @param maxLongValue the maximum value of a randomly generated long.
	 */
	public void setMaxLongValue(long maxLongValue) {
		this.maxLongValue = maxLongValue;
	}

	/**
	 * Getter method returns a long to indicate the minimum value a randomly
	 * generated long can have.
	 * 
	 * @return the minimum value of a randomly generated long.
	 */
	public long getMinLongValue() {
		return minLongValue;
	}

	/**
	 * Setter method sets a long to indicate the minimum value a randomly
	 * generated long can have.
	 * 
	 * @param minLongValue the minimum value of a randomly generated long.
	 */
	public void setMinLongValue(long minLongValue) {
		this.minLongValue = minLongValue;
	}

	/**
	 * Getter method returns the int minimum year of a randomly generated 
	 * {@link java.util.Date} object.
	 * 
	 * @return the minimum int year of a randomly generated
	 * {@link java.util.Date} object.
	 */
	public int getMinYear() {
		return minYear;
	}

	/**
	 * Setter method sets the int minimum year of a randomly generated 
	 * {@link java.util.Date} object.
	 * 
	 * @param minYear the minimum int year of a randomly generated
	 * {@link java.util.Date} object.
	 */
	public void setMinYear(int minYear) {
		this.minYear = minYear;
	}

	/**
	 * Getter method returns the int maximum year of a randomly generated 
	 * {@link java.util.Date} object.
	 * 
	 * @return returns the int maximum year of a randomly generated 
	 * {@link java.util.Date} object.
	 */
	public int getMaxYear() {
		return maxYear;
	}

	/**
	 * Setter method sets the int maximum year of a randomly generated 
	 * {@link java.util.Date} object.
	 * 
	 * @param maxYear the int maximum year of a randomly generated 
	 * {@link java.util.Date} object.
	 */
	public void setMaxYear(int maxYear) {
		this.maxYear = maxYear;
	}

	/**
	 * Getter method returns the minimum value of a randomly generated short 
	 * value.
	 * 
	 * @return the minimum value of randomly a generated short 
	 * value.
	 */
	public short getMinShortValue() {
		return minShortValue;
	}

	/**
	 * Setter method sets the minimum value of a randomly generated short 
	 * value.
	 * 
	 * @param minShortValue the minimum value of a randomly generated short 
	 * value.
	 */
	public void setMinShortValue(short minShortValue) {
		this.minShortValue = minShortValue;
	}

	/**
	 * Getter method returns the maximum value of a randomly generated short 
	 * value.
	 * 
	 * @return the maximum value of randomly a generated short 
	 * value.
	 */
	public short getMaxShortValue() {
		return maxShortValue;
	}

	/**
	 * Setter method sets the maximum value of a randomly generated short 
	 * value.
	 * 
	 * @param maxShortValue the maximum value of a randomly generated short 
	 * value.
	 */
	public void setMaxShortValue(short maxShortValue) {
		this.maxShortValue = maxShortValue;
	}

	/**
	 * Getter method returns the maximum value of a randomly generated byte 
	 * value.
	 * 
	 * @return the maximum value of a randomly generated byte 
	 * value.
	 */
	public byte getMaxByteValue() {
		return maxByteValue;
	}

	/**
	 * Setter method sets the maximum value of a randomly generated byte 
	 * value.
	 * 
	 * @param maxByteValue the maximum value of a randomly generated byte 
	 * value.
	 */
	public void setMaxByteValue(byte maxByteValue) {
		this.maxByteValue = maxByteValue;
	}

	/**
	 * Getter method returns the minimum value of a randomly generated byte 
	 * value.
	 * 
	 * @return the minimum value of a randomly generated byte 
	 * value.
	 */
	public byte getMinByteValue() {
		return minByteValue;
	}

	/**
	 * Setter method sets the minimum value of a randomly generated byte 
	 * value.
	 * 
	 * @param minByteValue the minimum value of a randomly generated byte 
	 * value.
	 */
	public void setMinByteValue(byte minByteValue) {
		this.minByteValue = minByteValue;
	}

	/**
	 * Getter method returns the maximum value of a randomly generated float 
	 * value.
	 * 
	 * @return the maximum value of a randomly generated float 
	 * value.
	 */
	public float getMaxFloatValue() {
		return maxFloatValue;
	}

	/**
	 * Setter method sets the maximum value of a randomly generated float 
	 * value.
	 * 
	 * @param maxFloatValue the maximum value of a randomly generated float 
	 * value.
	 */
	public void setMaxFloatValue(float maxFloatValue) {
		this.maxFloatValue = maxFloatValue;
	}

	/**
	 * Getter method returns the minimum value of a randomly generated float 
	 * value.
	 * 
	 * @return the minimum value of a randomly generated float 
	 * value.
	 */
	public float getMinFloatValue() {
		return minFloatValue;
	}
	
	/**
	 * Setter method sets the minimum value of a randomly generated float 
	 * value.
	 * 
	 * @param minFloatValue the minimum value of a randomly generated float 
	 * value.
	 */
	public void setMinFloatValue(float minFloatValue) {
		this.minFloatValue = minFloatValue;
	}

	/**
	 * Getter method returns the maximum value of a randomly generated double  
	 * value.
	 * 
	 * @return the maximum value of a randomly generated double 
	 * value.
	 */
	public double getMaxDoubleValue() {
		return maxDoubleValue;
	}

	/**
	 * Setter method sets the maximum value of a randomly generated double 
	 * value.
	 * 
	 * @param maxDoubleValue the maximum value of a randomly generated 
	 * double value.
	 */
	public void setMaxDoubleValue(double maxDoubleValue) {
		this.maxDoubleValue = maxDoubleValue;
	}

	/**
	 * Getter method returns the minimum value of a randomly generated double  
	 * value.
	 * 
	 * @return the minimum value of a randomly generated double  
	 * value.
	 */
	public double getMinDoubleValue() {
		return minDoubleValue;
	}

	/**
	 * Setter method sets the minimum value of a randomly generated double  
	 * value.
	 * 
	 * @param minDoubleValue the minimum value of a randomly generated 
	 * double value.
	 */
	public void setMinDoubleValue(double minDoubleValue) {
		this.minDoubleValue = minDoubleValue;
	}

	@Override
	public String toString() {
		return "Settings [resources=" + resources + ", cal=" + cal
				+ ", extension=" + extension + ", alphaUsed=" + alphaUsed
				+ ", digitsUsed=" + digitsUsed + ", specialsUsed="
				+ specialsUsed + ", defaultStringLength=" + defaultStringLength
				+ ", maxIntValue=" + maxIntValue + ", minIntValue="
				+ minIntValue + ", generateUndefined=" + generateUndefined
				+ ", maxByteValue=" + maxByteValue + ", minByteValue="
				+ minByteValue + ", maxShortValue=" + maxShortValue
				+ ", minShortValue=" + minShortValue + ", maxFloatValue="
				+ maxFloatValue + ", minFloatValue=" + minFloatValue
				+ ", maxDoubleValue=" + maxDoubleValue + ", minDoubleValue="
				+ minDoubleValue + ", maxLongValue=" + maxLongValue
				+ ", minLongValue=" + minLongValue + ", minYear=" + minYear
				+ ", maxYear=" + maxYear + ", randomContainerSize="
				+ randomContainerSize + ", nullOnEmptySet=" + nullOnEmptySet
				+ ", isNotNull=" + isNotNull + ", defaultContainerSize="
				+ defaultContainerSize + ", maxContainerSize="
				+ maxContainerSize + ", minContainerSize=" + minContainerSize
				+ "]";
	}

	
}
