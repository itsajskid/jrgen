package com.github.jrgen.context;

import static java.util.Collections.unmodifiableMap;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.github.jrgen.configuration.Configuration;
import com.github.jrgen.configuration.ConfigurationImpl;
import com.github.jrgen.descriptor.Descriptor;
import com.github.jrgen.exception.JrgenInitializationException;
import com.github.jrgen.settings.Settings;
import com.github.jrgen.typebuilder.ArrayTypeBuilder;
import com.github.jrgen.typebuilder.CollectionTypeBuilder;
import com.github.jrgen.typebuilder.ConfigurationBasedTypeBuilder;
import com.github.jrgen.typebuilder.EnumTypeBuilder;
import com.github.jrgen.typebuilder.ImmutableTypeBuilder;
import com.github.jrgen.typebuilder.MapTypeBuilder;
import com.github.jrgen.typebuilder.PrimitiveTypeBuilder;
import com.github.jrgen.typebuilder.TypeBuilder;
import com.github.jrgen.typehandler.AbstractTypeHandler;
import com.github.jrgen.util.JrgenUtil;
import com.github.jrgen.workflow.DefaultWorkflow;
import com.github.jrgen.workflow.Workflow;

/**
 * <p>
 * Instances of this class are responsible for generating instances of a given
 * class by the developer. The class is also responsible for allowing the 
 * developer to alter what is generated using the {@link Settings} property 
 * that is provided.
 * </p>
 * 
 * <p>
 * In general, the JrgenContext will attempt to instantiate the given class,
 * and provide data for all of its Java-bean declared properties. The 
 * properties can be user defined Java-beans, 
 * supported Basic Types (see below), Immutable Types (see below), Enums,
 * Arrays, Collections, and Maps. Classes or properties of classes that are
 * interfaces can be supported but steps must be taken to do so 
 * (see {@link AbstractTypeHandler}).
 * </p>
 * 
 * <p>
 * For given situations which the developer may want to define the set of data
 * to be chosen from, the developer must provide a JSON resource file. The JSON
 * resource file must specify the data set, and the classes for which the data
 * set applies. See {@link Configuration} for more details. Instances of 
 * JrgenContext will load such configurations (if they exist) when the 
 * initialize() method is called. The initialize() method must be called prior
 * to any generate() methods, otherwise a 
 * {@link JrgenInitializationException} will be thrown. The default location
 * that JrgenContext will look for resource files will be in the classpath
 * within a folder called "jrgen-resources".
 * </p>
 * 
 * <p>
 * {@link Configuration} files are not needed in situations where there is no
 * constraints on data, and or the data is a supported typed. The types 
 * supported are:
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
 * Immutable objects from the perspective of the JrgenContext, is any object
 * in which the {@link Descriptor}'s describe() method returns null. 
 * The {@link File} object for instance, is immutable from both a JrgenContext 
 * and definitive sense. In cases such as {@link File}, a 
 * {@link Configuration} is required.
 * </p>
 * 
 * <p>
 * By default, if a property or class cannot be resolved, a null reference will be
 * returned.
 * </p>
 * 
 * @author Allan J. Shoulders
 * @version 1.0
 * @since 1.0.0
 * @see Settings 
 * @see Workflow
 * @see TypeBuilder
 * @see AbstractTypeHandler
 *
 */
public final class JrgenContext {
	
	private final Set<Configuration> configSet;
	private final Map<JavaType, Configuration> typeMap;
	
	private boolean initialized;
	private Settings settings;
	private Workflow workflow;
	private List<TypeBuilder<?>> typeBuilders;
	private AbstractTypeHandler abstractTypeHandler;

	/**
	 * Constructs an instance using the default {@link Settings}.
	 */
	public JrgenContext() {
		this(Settings.getInstance());
	}

	/***
	 * Constructs an instance using the provided {@link Settings} object.
	 * 
	 * @param settings the provided {@link Settings} instance.
	 */
	public JrgenContext(Settings settings) {
		typeMap = new HashMap<JavaType, Configuration>();
		configSet = new LinkedHashSet<Configuration>();
		
		this.settings = settings;
		abstractTypeHandler = new AbstractTypeHandler();
		workflow = new DefaultWorkflow();
		typeBuilders = new ArrayList<TypeBuilder<?>>();
		initialized = false;
	}
	
	/***
	 * Executes the initialization process by first loading all resource (JSON)
	 * configuration files, creating a {@link TypeBuilder} stack, assigning the
	 * {@link TypeBuilder} stack to the {@link Workflow} object within the 
	 * JrgenContext instance, and finally changing the initialization flag to
	 * the correct state.
	 * 
	 * @return a reference to the JrgenContext from which this method was 
	 * called.
	 * 
	 * @throws RuntimeException for JsonParseException, 
	 * JsonMappingException, MalformedURLException, IOException, 
	 * URISyntaxException
	 */	
	public JrgenContext initalizeContext() {
		try {
			return initializeContextCE();
		} catch (JsonParseException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}	
	}
	
	/***
	 * Functions the same as initializeContext() with the difference being
	 * that the calling method must handle the Checked Exceptions thrown.
	 * 
	 * @return a reference to the JrgenContext from which this method was 
	 * called.
	 * 
	 * @throws JsonParseException failure to parse a configuration file
	 * because of invalid JSON or other reason.
	 * @throws JsonMappingException a property in the configuration file
	 * that cannot be mapped to the {@link Configuration} object.
	 * @throws MalformedURLException the specified URL of the configuration
	 * file has an invalid or unknown protocol, or invalid URL format.
	 * @throws IOException an error occurred while trying to read the 
	 * configuration file.
	 * @throws URISyntaxException when a configuration file's location is 
	 * in the classpath, the URL fails to convert to a URI which is in turn
	 * converted to a file location. 
	 */	
	public JrgenContext initializeContextCE() 
			throws JsonParseException, 
			JsonMappingException, 
			MalformedURLException, 
			IOException, 
			URISyntaxException {
		
		for (URI uri : settings.getResources()) {
			Set<Configuration> confSet = getConfigurationSetFromJson(uri);
			
			if (confSet != null) {
				configSet.addAll(getConfigurationSetFromJson(uri));
			}
		}
		
		initializeWorkflow();
		buildConfigurationMapping(configSet);
		initialized = true;
		
		return this;
	}
	
	/***
	 * Returns an immutable {@link Map} containing the type as 
	 * ({@link JavaType}) and the corresponding {@link Configuration} object.
	 * This mapping is built during initialization.
	 * 
	 * @return an immutable (unmodifiable) {@link Map} containing the 
	 * type as {@link JavaType} and the configuration file represented
	 * as a {@link Configuration} object.
	 */	
	public Map<JavaType, Configuration> getTypeMap() {
		return unmodifiableMap(typeMap);
	}
	
	/***
	 * Generates an instance of the {@link Class} passed in.
	 * 
	 * @param <T> the parameterized type.
	 * @param generateClass the provided {@link Class} type.
	 * 
	 * @return an instance of the {@link Class} whose fields
	 * contains random values provided by Jrgen or a configuration file
	 * provided by the developer. 
	 */	
	@SuppressWarnings("unchecked")
	public <T> T generate (Class<T> generateClass) {		
		JavaType generateJavaType = abstractTypeHandler.getObjectMapper()
				.getTypeFactory().constructType(generateClass);
		
		Object generatedObj = generate(generateJavaType);
		
		return generatedObj == null ? null : (T)generatedObj;
	}

	/***
	 * Generates an instance of the {@link JavaType} passed in. This method
	 * is very useful when it parameterized or more complex types need to be
	 * represented through the use of a {@link JavaType} rather than a Class.
	 * 
	 * @param javaType the provided {@link JavaType}.
	 * @return an instance of the type the {@link JavaType} represents. 
	 */	
	public Object generate (JavaType javaType) {
		if (!isInitialized()) {
			throw new JrgenInitializationException(JrgenUtil.getMessages()
						.getString("jrgencontext.notinitialized.exception"));
		}
		
		Object generatedObj = workflow.workflow(javaType);
		
		if (generatedObj == null && settings.isNotNull()) {
			return abstractTypeHandler.getObjectMapper()
					.convertValue(Collections.EMPTY_MAP, javaType);
		}
		
		return generatedObj;
	}

	/***
	 * Generates a {@link Collection} of instances of the provided {@link Class}.
	 * The number of instances or size of the collection is determined by the 
	 * numObjs parameter.
	 * 
	 * @param <T> the parameterized type.
	 * @param generateClass the provided {@link Class} type.
	 * @param numObjs the size of the returned collection which is equal to
	 * the number of objects created.
	 * 
	 * @return a {@link Collection} of instances of the provided {@link Class}. 
	 */	
	@SuppressWarnings("unchecked")
	public <T> Collection<T> generate (Class<T> generateClass, int numObjs) {		
		JavaType generateJavaType = abstractTypeHandler.getObjectMapper()
				.constructType(generateClass);
		
		Collection<Object> generatedCollection = 
				generate(generateJavaType, numObjs);
		
		return (Collection<T>) generatedCollection;
	}
	
	/***
	 * Generates a {@link Map} of the given key and value parameters.
	 * 
	 * @param <K> the parameterized key type.
	 * @param <V> the parameterized value type.
	 * @param generateMap the {@link Map} instance to be generated.
	 * @param keyClass the type belonging to the keys of the generated 
	 * {@link Map}. 
	 * @param valueClass the type belonging the values of the generated
	 * {@link Map}. 
	 * 
	 * @return A {@link Map} containing generated entries matching the 
	 * type {@link Class} types of the key/value pairs.
	 */	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <K, V> Map<K, V> generate (Class<? extends Map> generateMap, 
			Class<K> keyClass, Class<V> valueClass) {
		JavaType javaType = abstractTypeHandler.getObjectMapper()
				.getTypeFactory()
				.constructMapType(generateMap, keyClass, valueClass);
		
		return (Map<K, V>) generate(javaType);
	}
		
	/***
	 * Creates a {@link Collection} of the objects matching the specified
	 * {@link JavaType}.
	 * 
	 * @param javaType An object in possession of the underlying type that
	 * will be created.
	 * @param numObjs the size of the returned collection which is equal to
	 * the number of objects created.
	 * 
	 * @return {@link Collection} of specified objects defined by the 
	 * {@link JavaType}.
	 */
	public Collection<Object> generate (JavaType javaType, int numObjs) {
		List<Object> generatedObjs = new LinkedList<Object>();
		
		for (int i=0; i < numObjs; i++) {
			generatedObjs.add(generate(javaType));
		}
		
		return generatedObjs;
	}
	
	/***
	 * Sets the {@link Settings} object for this instance of JrgenContext.
	 * 
	 * @param settings the {@link Settings} object.
	 */
	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	/***
	 * Sets the {@link Workflow} object for this instance of JrgenContext.
	 * 
	 * @param workflow the {@link Workflow} object.
	 */
	public void setWorkflow(Workflow workflow) {
		this.workflow = workflow;
	}
	
	/***
	 * Sets the {@link List} of {@link TypeBuilder} to be used by the 
	 * {@link Workflow} object associated with this instance of 
	 * JrgenContext.
	 * 
	 * @param typeBuilders The {@link List} of {@link TypeBuilder}s to use.
	 */
	public void setTypeBuilders(List<TypeBuilder<?>> typeBuilders) {
		this.typeBuilders = typeBuilders;
	}

	/***
	 * Returns the {@link Workflow} object associated with this JrgenContext
	 * instance.
	 * 
	 * @return the current {@link Workflow} object.
	 */
	public Workflow getWorkflow() {
		return workflow;
	}
	
	/***
	 * Returns {@link List} of {@link TypeBuilder}s associated with this 
	 * JrgenContext instance.
	 * 
	 * @return the current {@link List} of {@link TypeBuilder}s.
	 */
	public List<TypeBuilder<?>> getTypeBuilders() {
		return typeBuilders;
	}
	
	/***
	 * Returns a boolean value indicating if this JrgenContext instance had
	 * already run its initialize() method.
	 * 
	 * @return boolean indicating if the initialize() method had already run.
	 * 
	 */
	public boolean isInitialized() {
		return initialized;
	}

	/***
	 * Returns the current {@link Settings} object associated with this
	 * instance of JrgenContext.
	 * 
	 * @return the {@link Settings} object.
	 */
	public Settings getSettings() {
		return settings;
	}		

	/***
	 * Returns the {@link AbstractTypeHandler} object associated with this
	 * instance of JrgenContext.
	 * 
	 * @return the {@link AbstractTypeHandler} object.
	 */
	public AbstractTypeHandler getAbstractTypeHandler() {
		return abstractTypeHandler;
	}
	
	/***
	 * Sets the given {@link AbstractTypeHandler} object to this instance
	 * of JrgenContext.
	 * 
	 * @param abstractTypeHandler the {@link AbstractTypeHandler} object.
	 */
	public void setAbstractTypeHandler(AbstractTypeHandler abstractTypeHandler) {
		this.abstractTypeHandler = abstractTypeHandler;
	}

	/**
	 * A helper method that creates new instances of the default
	 * TypeBuilder, and Workflow objects provided by Jrgen.
	 */
	private void initializeWorkflow() {
		if (CollectionUtils.isEmpty(typeBuilders)) {
			
			//Default typeBuilder stack...
			TypeBuilder<?>[] _typeBuilders = {
					new PrimitiveTypeBuilder(this),
					new EnumTypeBuilder(),
					new ArrayTypeBuilder(this),
					new CollectionTypeBuilder(this), 
					new MapTypeBuilder(this),
					new ConfigurationBasedTypeBuilder(this),
					new ImmutableTypeBuilder(this)
			};
			
			typeBuilders.addAll(Arrays.asList(_typeBuilders));
			workflow.addTypeBuilders(typeBuilders);
		}
	}

	/**
	 * A helper method that builds a "typeMap". That is, a Map object that
	 * contains Class objects as keys with Configuration objects as values.
	 * The Class objects are determined by the "namespace" of the Configuration
	 * object.   
	 * 
	 * @param configSet Set of Configuration objects. Usually this is provided
	 * by the getConfigurationSetFromJson() method.
	 */
	private void buildConfigurationMapping(Set<Configuration> configSet) {
		ObjectMapper mapper = new ObjectMapper();
		
		for (Configuration config : configSet) {
			for (String actualNamespace : config.getNamespaces()) {
				Class<?> _class = findClass(actualNamespace);
				
				if (_class != null) {
					typeMap.put(mapper.constructType(_class), config);
				}
			}
		}
	}
	
	/**
	 * Helper method that builds a Set of Configuration objects that are 
	 * representations of the JSON resource files. It pulls the URIs from the
	 * Settings object. Each URI is converted to a URL or File object depending
	 * on the scheme of the URI. 
	 * 
	 * If the URI indicates it is anything other than a file, that resource is
	 * fetched using the appropriate protocol of the URI if that protocol is 
	 * supported.
	 * 
	 * If the URI is a file (the scheme being "file"), the URI is converted to
	 * File object. This done in situations where it is the developer's intent
	 * to load any number of JSON configuration files from a specified folder
	 * on the classpath. All of the files associated with that classpath
	 * directory, are then loaded individually by conversion to URIs, once
	 * again having a "file" scheme.
	 * 
	 * ObjectMapper provided by the Jackson library is used to convert the 
	 * JSON resource file to an actual Configuration object.
	 * 
	 * @param resourceURI
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	@SuppressWarnings("unchecked")
	private Set<Configuration> getConfigurationSetFromJson (URI resourceURI) 
			throws JsonParseException, 
			JsonMappingException, 
			MalformedURLException, 
			IOException, 
			URISyntaxException {
		
		ObjectMapper mapper = new ObjectMapper();
		Set<Configuration> fileConfigSet = new LinkedHashSet<Configuration>();
		
		JavaType setConfigType = 
				mapper.getTypeFactory().constructCollectionType(
						Set.class, ConfigurationImpl.class);
		
		if (resourceURI.isAbsolute()) {
			if (hasSupportedExtension(settings, resourceURI)) {
				Set<Configuration> configs = 
						(Set<Configuration>) mapper
							.readValue(resourceURI.toURL(), setConfigType);
				
				setResource(resourceURI, fileConfigSet);
				
				fileConfigSet.addAll(configs);
			}
			
			return fileConfigSet;
		}
		
		//Classpath resources...
		File[] resourceFiles = createResourceFile(resourceURI);
		
		if (ArrayUtils.isEmpty(resourceFiles)) {
			return fileConfigSet;
		}
		
		for (File resourceFile : resourceFiles) {
			if (hasSupportedExtension(settings, resourceFile)) {
				Set<Configuration> configs = (Set<Configuration>) mapper
						.readValue(resourceFile, setConfigType);
				setResource(resourceFile.toURI(), configs);
				fileConfigSet.addAll(configs);
			}
		}
		
		return fileConfigSet;
	}
	
	/**
	 * Helper method that simply sets the original URI onto all the 
	 * configuration objects in the given Set of Configurations. THis method
	 * was created for code clarity.
	 * 
	 * @param uri
	 * @param configSet
	 */
	private void setResource (URI uri, Set<Configuration> configSet) {
		for (Configuration config : configSet) {
			config.setResource(uri);
		}
	}
	
	/***
	 * This helper method converts a given URI to an Array of Files. Even
	 * if the File that is produced by conversion of the URI is an actual
	 * file and not a directory it is still an Array of Files (albeit an array
	 * with just one element.)
	 * 
	 * @param uri
	 * @return
	 * @throws URISyntaxException 
	 */
	private File[] createResourceFile(URI uri) throws URISyntaxException {		
		URL resourceURL = 
				JrgenContext.class.getResource(uri.toString());
		
		if (resourceURL == null) {
			return null;
		}
		
		File classpathFile = new File(resourceURL.toURI());
		
		if (classpathFile.isDirectory()) {
			return classpathFile.listFiles();
		} else {
			return new File[] {classpathFile};
		}		
	}
	
	/***
	 * A helper method that returns the actual class represented by the 
	 * className String passed in. If the class does not exist, or cannot
	 * be found, a null value is returned.
	 * 
	 * @param className
	 * @return
	 */
	private Class<?> findClass (String className) {
		try {
			return ClassUtil.findClass(className);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
	
	/**
	 * A helper method that checks if the URI resource has a .json
	 * extension. From the perspective of Jrgen, a file containing
	 * a .json extension is assumed to be a json file.
	 * 
	 * @param settings
	 * @param uri
	 * @return
	 */
	private boolean hasSupportedExtension (Settings settings, URI uri) {
		return uri.getPath().endsWith(settings.getExtension().toString());
	}	
	
	/**
	 * A helper method that checks if the File object has a .json
	 * extension. From the perspective of Jrgen, a file containing
	 * a .json extension is assumed to be a json file.
	 * 
	 * @param settings
	 * @param file
	 * @return
	 */
	private boolean hasSupportedExtension (Settings settings, File file) {
		return file.getName().endsWith(settings.getExtension().toString());
	}
	
	@Override
	public String toString() {
		return "JrgenContext [configSet=" + configSet + ", typeMap=" + typeMap
				+ ", initialized=" + initialized + ", settings=" + settings
				+ ", workflow=" + workflow + ", typeBuilders=" + typeBuilders
				+ ", abstractTypeHandler=" + abstractTypeHandler + "]";
	}	
}
