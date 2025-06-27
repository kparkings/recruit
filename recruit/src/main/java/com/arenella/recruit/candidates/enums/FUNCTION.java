package com.arenella.recruit.candidates.enums;

/**
* Function performed by the Candidate
* @author K Parkings
*/
public enum FUNCTION {
	JAVA_DEV("Java Developer"), 
	CSHARP_DEV("C# Developer"), 
	SUPPORT("Support analyst"), 
	BA("Business Analyst"), 
	UI_UX("UI UX"), 
	PROJECT_MANAGER("Project Manager"), 
	ARCHITECT("Software Architect"), 
	TESTER("Test Analyst"), 
	WEB_DEV("Web Developer"),
	SCRUM_MASTER("Scrum Master"),
	DATA_SCIENTIST("Data Scientist"),
	NETWORK_ADMINISTRATOR("Network Administrator"),
	SOFTWARE_DEVELOPER("Software Developer"),
	IT_SECURITY("IT Security"),
	IT_RECRUITER("IT Recruiter"),
	SOFTWARE_DEV_IN_TEST("Software Dev In Test"),
	RUBY("Ruby"),
	RUBY_ON_RAILS("Ruby On Rails"),
	GO("Go"),
	REACT("React"),
	VUE("Vue"),
	NEXT("Next"),
	EXPRES("Expres"),
	RUST("Rust"),
	TEST_MANAGER("Test Manager"),
	PRODUCT_OWNER("Product Owner"),
	NODE("Node"),
	PYTHON("Python"),
	ANGULAR("Angular"),
	PHP("Php"),
	ANDROID("Android"),
	IOS("IOS"),
	KOTLIN("Kotlin");
	
	private String desc;
	
	FUNCTION(String desc){
		this.desc = desc;
	}
	
	public String getDesc() {
		return desc;
	}
}
