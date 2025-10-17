package com.arenella.recruit.candidates.extractors;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
* Class represents a type of job and keeps track of synonyms for that job type
* @author K Parkings
*/
public class JobType{
	
	enum Type {
		java("Java Developer"), 
		csharp("C# Developer"), 
		ba("Business Analyst"),
		qa("Test Analyst"),
		itSupport("IT Support Analyst"),
		uiux("UI/UX Designer"),
		projectManager("Project manager"),
		architect("Architect"),
		webDeveloper("Web Developer"),
		scrumMaster("Scrum Master"),
		dataScientist("Data Scientist"),
		networkAdmin("DevOps Engineer"),
		softwareDeveloper("Software Developer"),
		itSecurity("IT Security"),
		itRecruiter("IT Recruiter"),
		sdet("SDET"),
		
		
		ruby("Ruby Developer"),
		rubyOnRails("Ruby On Rails Developer"),
		go("Golang Developer"),
		react("React Developer"),
		vue("Vue Developer"),
		next("Next Developer"),
		expres("Expres Developer"),
		rust("Rust Developer"),
		testManager("Test Manager"),
		productOwner("Product Owner"),
		node("Node Developer"),
		python("Python Developer"),
		angular("Angular Developer"),
		php("PHP Developer"),
		android("Android Developer"),
		ios("IOS Developer"),
		ccplusplus("C/C++ Developer"),
		cobol("Cobol Developer"),
		sap("SAP Developer"),
		kotlin("Kotlin Developer"),
		
		salesforce("Salesforce"),
		
		softwareArchitect("Software architect"), 
		dataArchitect("Data architect"), 
		infrastructureArchitect("Infrastructure architect"), 
		solutionsArchitect("Solutions architect"), 
		enterpriseArchitect("Enterprise architect"), 
		
		
		softwareManager("Software development manager"), 
		infrastructureManager("Infrastructure manager"), 
		cto("CTO"),
		diretor("Director");
			
		public final String role;
		
		Type(String role) {
			this.role = role;
		}
		
	}
	
	private static Set<Type> softwareDeveloperTypes = Set.of(
			JobType.Type.android,
			JobType.Type.angular,
			JobType.Type.ccplusplus,
			JobType.Type.expres,
			JobType.Type.go,
			JobType.Type.ios,
			JobType.Type.java,
			JobType.Type.kotlin,
			JobType.Type.next,
			JobType.Type.node,
			JobType.Type.php,
			JobType.Type.python,
			JobType.Type.react,
			JobType.Type.ruby,
			JobType.Type.rubyOnRails,
			JobType.Type.rust,
			JobType.Type.sap,
			JobType.Type.vue);
	
	private static Set<Type> webDeveloperTypes = Set.of(
			JobType.Type.angular,
			JobType.Type.expres,
			JobType.Type.go,
			JobType.Type.next,
			JobType.Type.node,
			JobType.Type.react,
			JobType.Type.ruby,
			JobType.Type.rubyOnRails,
			JobType.Type.rust,
			JobType.Type.vue);
	
	private static Set<Type> architectTypes = Set.of(
			JobType.Type.softwareArchitect,
			JobType.Type.dataArchitect,
			JobType.Type.solutionsArchitect,
			JobType.Type.enterpriseArchitect,
			JobType.Type.infrastructureArchitect);
	 
	private static Set<Type> managerTypes = Set.of(
			JobType.Type.softwareManager,
			JobType.Type.infrastructureManager,
			JobType.Type.cto,
			JobType.Type.diretor);
	
	private Type 		type;
	private Set<String> titles;
	
	/**
	* Constructor
	* @param type		- Type of job
	* @param titles		- titles / synonyms for the job type
	*/
	public JobType(Type type, Set<String> titles) {
		this.type 	= type;
		this.titles = titles;
	}
	
	/**
	* Returns the type of job
	* @return Job type
	*/
	public Type getType() {
		return type;
	}
	
	/**
	* Titles / synonyms for the job type
	* @return titles
	*/
	public Set<String> getTitles(){
		return titles;
	}
	
	/**
	* Returns Type based upon its role
	* @param role - role to search on
	* @return Type with specified role defined
	*/
	public static Type getByRole(String role) {
		
		Set<Type> matches = Arrays.asList(Type.values()).stream().filter(t -> t.role.equals(role)).collect(Collectors.toSet());
		
		if (matches.isEmpty()) {
			throw new IllegalArgumentException("Unknown Enum Type for :" + role);
		}
		
		if (matches.size() > 1) {
			throw new IllegalStateException("Duplicate roles defined in same enum");
		}
		
		return matches.stream().findFirst().get();
		
	}
	
	/**
	* Returns all types that can be considered a type of 
	* software developer
	*/
	public static Set<Type>getSoftwareDeveloperTypes() {
		return softwareDeveloperTypes;
	}
	
	/**
	* Returns all types that can be considered a type of 
	* web developer
	* @return
	*/
	public static Set<Type>getWebDeveloperTypes() {
		return webDeveloperTypes;
	}
	
	/**
	* Returns all types that can be considered a type of 
	* architect
	* @return
	*/
	public static Set<Type>getArchitectTypes() {
		return architectTypes;
	}
	
	/**
	* Returns all types that can be considered a type of 
	* manager
	* @return
	*/
	public static Set<Type>getManagerTypes() {
		return managerTypes;
	}
	
}