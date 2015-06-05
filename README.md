## Java Resource Generator (JRGEN)  

The Java Resource Generator (JRGEN – pronounced “jur-gehn”) is a utility that can create instances of Java Bean style objects, and populates them with random data.  JRGEN can assist the developer in the following ways:  

- Test data is needed during unit or automated testing.
- Test data is needed during learning efforts such as learning a new API.
- Test data is needed during prototyping, or the creation of business logic.
- The developer simply wants to test their code using "unbiased" data.
  
To achieve these goals, a developer usually has to write tedious, non-portable, "throw away" code. JRGEN can simplify and in some cases relieve the developer of such mundane tasks.  
##Simple Examples

Generate a simple Integer:

	JrgenContext jrgenContext = new JrgenContext().initialize(); 
	Integer myInt = jrgenContext.generate(Integer.class);
	log.info("myInt: " + myInt);
	
	Output:
	INFO: myInt: 2887

Generate a Java-bean named NutritionFacts (properties omitted)
	
	NutritionFacts nFacts = jrgenContext.generate(NutritionFacts.class);
	log.info(nFacts);
	
	Output:
	INFO: NutritionFacts [calories=4338, 
	caloriesFromFat=1889,
	servingSize=o1n9A9R9H1o3v5v1T3h4U6,
	servingsPerContainer=5629, 
	totalFat=5444, 
	saturatedFat=6864, 
	monoUnsatFat=7643, 
	polyUnsatFat=5891, 
	transFat=9459, 
	cholesterol=3062, 
	sodium=6122, 
	potassium=800, 
	totalCarb=8180, 
	fiber=2499, 
	sugar=320, 
	protein=677]

JRGEN allows you to control the data that gets generated. See the Wiki or the <i>JRGEN By Example Guide</i> for further details.

## Getting JRGEN
###Maven

Add this dependency to your pom.xml:

	<dependency>
		<groupId>com.github.itsajskid</groupId>
    		<artifactId>jrgen</artifactId>
    		<version>1.1.0-RELEASE</version>
	</dependency>

You can download JRGEN artifacts individually [here](https://github.com/itsajskid/jrgen/releases).

##Supported Java Versions
JRGEN was built using Java 6. We plan to release a Java 8 version that takes advantage of all of Java 8's features. We chose Java 6 to reach a developer audience who may be be forced (or even prefer) Java 6.

##JRGEN Examples and Further Reading
Examples can be found in the <i>JRGEN By Example PDF</i> guide. For those who prefer HTML, the same content can be found in the [JRGEN Wiki](https://github.com/itsajskid/jrgen/wiki).


