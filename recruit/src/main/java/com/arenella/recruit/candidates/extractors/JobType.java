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
		networkAdmin("Network Administrator"),
		softwareDeveloper("Software Developer"),
		itSecurity("IT Security"),
		itRecruiter("IT Recruiter"),
		sdet("SDET"),
		
		
		ruby("Ruby Developer"),
		rubyOnRails("Ruby On Rails Developer"),
		go("Golang Developer"),
		react("React Developer"),
		vue("Vue Developer"),
		next("NextDeveloper"),
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
		kotlin("Kotlin Developer");
	
		public final String role;
		
		Type(String role) {
			this.role = role;
		}
		
	}
	
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
	
}
