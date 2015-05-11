package com.github.jrgen.configuration;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;

/***
 * <p>
 * A Configuration (or classes that implement it) is a representation of
 * JSON resource files. The JSON resource files must have at a minimum a 
 * namespace defined. The data property should be defined in most cases,
 * but it is not required.
 * </p>
 *  
 * <p>
 * The namespace field must be an Array of String types according to the JSON 
 * specification. It should coincide with the {@link String} type in the Java 
 * language. The namespace should contain fully qualified names of each Java
 * type to which the data as described below should be applied to.
 * </p>
 * 
 * <p>
 * The data property is an Object type as defined by the JSON specification.
 * The fields of that object should consist of property names common to the one
 * or many namespaces as defined above, followed by an Array of various values,
 * which will be selected at random. It is important that these values be able
 * to be converted to the same type as that of the corresponding property.
 * </p>
 * 
 * @author Allan J. Shoulders
 * @version 1.0
 * @since 1.0.0
 * @see ConfigurationImpl
 *
 */
public interface Configuration {
	
	/***
	 * This method sets the {@link URI} of the actual JSON resource file(s) to
	 * this instance of Configuration.
	 * 
	 * @param uri the {@link URI} of the JSON resource file(s).
	 */
	public void setResource(URI uri);

	/***
	 * Returns the {@link URI} of the JSON resource file used by this instance 
	 * of Configuration.
	 * 
	 * @return {@link URI} of the JSON resource file.
	 */
	public URI getResource();
	
	/***
	 * Returns a {@link Set} of {@link String}s representing each class that
	 * the data properties should apply to.
	 * 
	 * @return {@link Set} of {@link String}s representing each class. 
	 */
	public Set<String> getNamespaces();
	
	/***
	 * Returns a {@link Map} which the keys represent the property names, and
	 * the values represent the Array of values specified in the JSON resource
	 * file.
	 * 
	 * @return {@link Map} of key/value pairs representing the data field
	 * of a JSON resource file.
	 */
	public Map<String, List<Object>> getData();
}
