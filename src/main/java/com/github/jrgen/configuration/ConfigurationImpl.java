package com.github.jrgen.configuration;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;

/***
 * The default implementation of a {@link Configuration}.
 * 
 * 
 * @author Allan J. Shoulders
 * @version 1.0
 * @since 1.0.0
 * @see Configuration
 *
 */
public class ConfigurationImpl implements Configuration {
	
	private Set<String> namespaces;
	private URI resource;
	private Map<String, List<Object>> data;
	
	/***
	 * Default constructor for instantiation.
	 */
	public ConfigurationImpl() {
		super();
	}
	
	/***
	 * Designated constructor allows for specifying the namespaces and
	 * resources for this instance at construction.
	 * 
	 * @param namespaces {@link Set} of {@link String} representing the 
	 * namespaces of the JSON resource file.
	 * @param resource the original {@link URI} of the JSON resource.
	 * @param data {@link Map} containing properties and data as key/value
	 * pairs.
	 */
	public ConfigurationImpl(Set<String> namespaces, URI resource,
			Map<String, List<Object>> data) {
		super();
		this.namespaces = namespaces;
		this.resource = resource;
		this.data = data;
	}

	@Override
	public void setResource(URI resource) {
		this.resource = resource;
	}

	@Override
	public Set<String> getNamespaces() {
		return namespaces;
	}

	@Override
	public Map<String, List<Object>> getData() {
		return data;
	}
	
	@Override
	public URI getResource() {
		return resource;
	}	

	/***
	 * Setter method that sets the namespace parameter to the
	 * current instance.
	 * 
	 * @param namespaces a {@link Set} of {@link String}s representing each 
	 * class that the data properties should apply to.
	 */
	public void setNamespaces(Set<String> namespaces) {
		this.namespaces = namespaces;
	}

	/***
	 * Setter method that sets the data parameter to the current instance.
	 * 
	 * @param data {@link Map} which the keys represent the property names, 
	 * and the values represent the Array of values specified in JSON resource
	 * file.
	 */
	public void setData(Map<String, List<Object>> data) {
		this.data = data;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result
				+ ((namespaces == null) ? 0 : namespaces.hashCode());
		result = prime * result
				+ ((resource == null) ? 0 : resource.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConfigurationImpl other = (ConfigurationImpl) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (namespaces == null) {
			if (other.namespaces != null)
				return false;
		} else if (!namespaces.equals(other.namespaces))
			return false;
		if (resource == null) {
			if (other.resource != null)
				return false;
		} else if (!resource.equals(other.resource))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ConfigurationImpl [namespaces=" + namespaces + ", resource="
				+ resource + ", data=" + data + "]";
	}
	
}
