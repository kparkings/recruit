package com.arenella.recruit.candidates.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
* Standard implementation of the SkillsSynonymsUtil interface 
* @author K Parkings
*/
@Component
public class SkillsSynonymsUtilImpl implements SkillsSynonymsUtil{
	
	private Set<SkillSynonym> synonyms = new HashSet<>();
	
	public SkillsSynonymsUtilImpl() {
		
		SkillSynonym	TESTER 			= new SkillSynonym("TESTER", Set.of("qa", "tester", "test analyst"));
		SkillSynonym	DOTNET_CORE 	= new SkillSynonym("DOTNET_CORE", Set.of("dotnet", ".net", "dot net")); 
		SkillSynonym	SPRING_CORE 	= new SkillSynonym("SPRING_CORE", Set.of("spring", "spring core", "springcore", "spring-core"));
		
		synonyms.add(TESTER); 
		synonyms.add(DOTNET_CORE);
		synonyms.add(SPRING_CORE);
		
		synonyms.add(new SkillSynonym("BUSINESS_ANALYST", Set.of("ba", "business analyst"))); 
		synonyms.add(new SkillSynonym("JAVASCRIPT",Set.of("js", "javascript", "java script", "java-script"))); 
		synonyms.add(new SkillSynonym("CSS",Set.of("css", "style sheets", "cascading style sheets","css3","css 3"))); 
		synonyms.add(new SkillSynonym("NODE", Set.of("node", "node.js", "nodejs"))); 
		synonyms.add(new SkillSynonym("NEXT", Set.of("next", "next.js", "nextjs"))); 
		synonyms.add(new SkillSynonym("VUE", Set.of("vue", "vue.js", "vuejs"))); 
		synonyms.add(new SkillSynonym("REACT", Set.of("react", "react.js", "reactjs", "react js", "react native"))); 
		synonyms.add(new SkillSynonym("CSHARP", Set.of("c#", "csharp"))); 
		
		synonyms.add(new SkillSynonym("ADO_DOTNET", Set.of("ado", "ado.net", "ado .net"), DOTNET_CORE));
		synonyms.add(new SkillSynonym("VB_DOTNET", Set.of("vb", "vb.net", "vb .net"), DOTNET_CORE)); 
		synonyms.add(new SkillSynonym("ASP_DOTNET", Set.of("asp", "asp.net", "asp .net","asp.net mvc","asp.net mvc core"), DOTNET_CORE)); 
		synonyms.add(new SkillSynonym("FRONT_END", Set.of("frontend", "front end", "fe", "front-end", "front end development", "frontend development"))); 
		synonyms.add(new SkillSynonym("BACK_END", Set.of("backend", "back end", "be", "back-end", "backend development", "bac kend development"))); 
		 
		synonyms.add(new SkillSynonym("SPRING_BOOT", Set.of("springboot", "spring boot", "spring-boot"), SPRING_CORE)); 
		synonyms.add(new SkillSynonym("SPRING_DATA", Set.of("springdata", "spring data", "spring-data"), SPRING_CORE)); 
		synonyms.add(new SkillSynonym("SPRING_TEST", Set.of("springtest", "spring test", "spring-test"), SPRING_CORE)); 
		synonyms.add(new SkillSynonym("SPRING_MVC", Set.of("springmvc", "spring mvc", "spring-mvc"), SPRING_CORE)); 
		synonyms.add(new SkillSynonym("IT_RECRUITER", Set.of("recruiter", "it recruiter", "it recruitment"))); 
		synonyms.add(new SkillSynonym("MICROSERVICES", Set.of("microservices", "micro services", "microservice", "micro service", "microservice architecture"))); 
		synonyms.add(new SkillSynonym("SELENIUM", Set.of("selenium", "selinium web driver","selinium webdriver"))); 
		synonyms.add(new SkillSynonym("UAT", Set.of("uat", "user acceptance testing", "acceptance testing"))); 
		synonyms.add(new SkillSynonym("TECH_SUPPORT", Set.of("tech support", "technical support"))); 
		synonyms.add(new SkillSynonym("REST", Set.of("rest", "rest api","rest apis","rest api's", "rest webservices","restful web services", "webservices"))); 
		synonyms.add(new SkillSynonym("SOAP", Set.of("soap", "soap web services", "webservices"))); 
		synonyms.add(new SkillSynonym("WEB_SERVICES", Set.of("webservice", "web services", "webservices", "web service"))); 
		synonyms.add(new SkillSynonym("PROJECT_MANAGEMENT", Set.of("project manager", "project management", "projectmanager"))); 
		synonyms.add(new SkillSynonym("PL_SQL", Set.of("plsql", "pl-sql", "pl/sql", "oracle sql"))); 
		synonyms.add(new SkillSynonym("HTML", Set.of("html", "html4", "html5"))); 
		synonyms.add(new SkillSynonym("CICD", Set.of("ci", "cd", "ci/cd", "ci-cd", "ci cd", "cicd", "ci / cd"))); 
		synonyms.add(new SkillSynonym("SCRUMMASTER", Set.of("scrummaster", "scrum master"))); 
		synonyms.add(new SkillSynonym("DEVOPS", Set.of("devops", "dev ops", "dev-ops"))); 
		synonyms.add(new SkillSynonym("ROBOT_FRAMEWORK", Set.of("robotframework", "robot framework"))); 
		synonyms.add(new SkillSynonym("ACTIVE_DIRECTORY", Set.of("active directory", "activedirectory"))); 
		synonyms.add(new SkillSynonym("ACTIVE_MQ", Set.of("active mq", "activemq"))); 
		synonyms.add(new SkillSynonym("ADMINISTRATION", Set.of("administration", "administrator"))); 
		synonyms.add(new SkillSynonym("ALGORITHMS", Set.of("algorithm", "algorithms"))); 
		synonyms.add(new SkillSynonym("ANGULAR", Set.of("angular", "angularjs", "angular.js","angular js"))); 
		synonyms.add(new SkillSynonym("KAFKA", Set.of("kafka", "apache kafka"))); 
		synonyms.add(new SkillSynonym("API", Set.of("api", "apis"))); 
		synonyms.add(new SkillSynonym("AUTOMATION_TESTING", Set.of("automated testing", "automation testing", "test automation", "test automation engineer", "test automation specialist"))); 
		synonyms.add(new SkillSynonym("BANKING", Set.of("bank", "banking"))); 
		synonyms.add(new SkillSynonym("BIT_BUCKET", Set.of("bit bucket", "bitbucket"))); 
		synonyms.add(new SkillSynonym("JAVA", Set.of("java", "core java"))); 
		synonyms.add(new SkillSynonym("DATA_ANALYSIS", Set.of("data scientist", "data analysis", "data analyst", "data analytics"))); 
		synonyms.add(new SkillSynonym("DATA_BRICKS", Set.of("databricks", "data bricks"))); 
		synonyms.add(new SkillSynonym("DATA_CENTER", Set.of("datacenter", "data center"))); 
		synonyms.add(new SkillSynonym("DATA_MANAGEMENT", Set.of("data management", "data governance"))); 
		synonyms.add(new SkillSynonym("DATA_MINING", Set.of("data mining", "data scraping"))); 
		synonyms.add(new SkillSynonym("DEFECT_LOGGING", Set.of("defect loging", "defect tracking"))); 
		synonyms.add(new SkillSynonym("DYNAMO", Set.of("dynamo", "dynamo db"))); 
		synonyms.add(new SkillSynonym("ECMA", Set.of("ecma", "ecma script"))); 
		synonyms.add(new SkillSynonym("ELASTIC", Set.of("elastic", "elasticsearch"))); 
		synonyms.add(new SkillSynonym("EMBEDDED", Set.of("embedded", "embedded software"))); 
		synonyms.add(new SkillSynonym("ENTITY_FRAMEWORK", Set.of("entity framework", "entity-framework"))); 
		synonyms.add(new SkillSynonym("EXCHANGE", Set.of("exchange", "excahnge server"))); 
		synonyms.add(new SkillSynonym("EXPRESS", Set.of("express", "expressjs"))); 
		synonyms.add(new SkillSynonym("FIREWALL", Set.of("firewalls", "firewall"))); 
		synonyms.add(new SkillSynonym("FULLSTACK", Set.of("full stack", "fullstack"))); 
		synonyms.add(new SkillSynonym("FUNCTIONAL_TESTING", Set.of("functional test", "functional testing"))); 
		synonyms.add(new SkillSynonym("GATSBY", Set.of("gatsby", "gatsbyjs"))); 
		synonyms.add(new SkillSynonym("GHERKIN", Set.of("gherkin/cucumber", "gherkin"))); 
		synonyms.add(new SkillSynonym("GIT", Set.of("git", "git and github","github"))); 
		synonyms.add(new SkillSynonym("HYPER_V", Set.of("hyper-v", "hyper v", "hypervisors"))); 
		synonyms.add(new SkillSynonym("IOS_DEV", Set.of("ios", "ios dev", "ios developer"))); 
		synonyms.add(new SkillSynonym("ISTQB", Set.of("istqb", "istqb certified tester"))); 
		synonyms.add(new SkillSynonym("ITIL", Set.of("itil", "itilv3"))); 
		synonyms.add(new SkillSynonym("JASPER", Set.of("jasper reports", "jasperreport"))); 
		synonyms.add(new SkillSynonym("JAVA_EIGHT", Set.of("java 8", "java8", "java"))); 
		synonyms.add(new SkillSynonym("JAVA_EE", Set.of("java ee", "j2ee","jee", "java"))); 
		synonyms.add(new SkillSynonym("JPA", Set.of("jpa", "jpa/hibernate"))); 
		synonyms.add(new SkillSynonym("JUNIPER", Set.of("juniper", "juniper network", "juniper switches"))); 
		synonyms.add(new SkillSynonym("JUPYTER", Set.of("jupyter", "jupyter notebook", "jupyter notebooks"))); 
		synonyms.add(new SkillSynonym("LOAD_TESTING", Set.of("load runner", "load testing")));
		synonyms.add(new SkillSynonym("MANUAL_TESTER", Set.of("manual test", "manual tester", "manual testing"), TESTER));
		synonyms.add(new SkillSynonym("MATERIAL_UI", Set.of("material-ui", "material ui")));
		synonyms.add(new SkillSynonym("POWER_BI", Set.of("microsoft powerbi", "powerbi")));
		synonyms.add(new SkillSynonym("MONGO", Set.of("mongo", "mongodb")));
		synonyms.add(new SkillSynonym("NETWORK_ADMIN", Set.of("network admin", "network administration","network configuration")));
		synonyms.add(new SkillSynonym("OBJECTIVE_C", Set.of("objective", "objective-c","objectivec")));
		synonyms.add(new SkillSynonym("OPERATIONS", Set.of("ops", "operations","operations management")));
		synonyms.add(new SkillSynonym("PALOALTO", Set.of("paloalto", "palo alto","palo alto networks")));
		synonyms.add(new SkillSynonym("PEGA", Set.of("pega", "pegasystems")));
		synonyms.add(new SkillSynonym("PEN_TESTER", Set.of("penetration", "pen tester", "pen testing"), TESTER));
		synonyms.add(new SkillSynonym("POSTGRES", Set.of("postgres", "postgressql")));
		synonyms.add(new SkillSynonym("POWERSHELL", Set.of("powershell", "powershell scripting")));
		synonyms.add(new SkillSynonym("PRINCE2", Set.of("prince 2", "prince2")));
		synonyms.add(new SkillSynonym("PROMETHEUS", Set.of("prometheus", "prometheus.io")));
		synonyms.add(new SkillSynonym("QLIKSENSE", Set.of("qliksense", "qlik sense")));
		synonyms.add(new SkillSynonym("QUALITY_CENTER", Set.of("quality center", "qualitycenter")));
		synonyms.add(new SkillSynonym("RABBIT_MQ", Set.of("rabbit mq", "rabbitmq")));
		synonyms.add(new SkillSynonym("REDUX", Set.of("redux", "redux.js")));
		synonyms.add(new SkillSynonym("REGRESSION_TESTER", Set.of("regression", "regression testing"), TESTER));
		synonyms.add(new SkillSynonym("REQUIREMENTS_ANALYSIS", Set.of("requirements", "requirements analysis")));
		synonyms.add(new SkillSynonym("RESPONSIVE_DESIGN", Set.of("responsive design", "responsive web design")));
		synonyms.add(new SkillSynonym("RESTASSURED", Set.of("restassured", "rest assured")));
		synonyms.add(new SkillSynonym("ROUTERS", Set.of("routers", "routing and switching")));
		synonyms.add(new SkillSynonym("SANITY_TESTING", Set.of("sanity", "sanity testing"), TESTER));
		synonyms.add(new SkillSynonym("SERVICE_NOW", Set.of("servicenow", "service now")));
		synonyms.add(new SkillSynonym("SERVLETS", Set.of("servlet", "servlets")));
		synonyms.add(new SkillSynonym("SHELL_PROGRAMING", Set.of("shell programing", "shell scripting")));
		synonyms.add(new SkillSynonym("SIXSIGMA", Set.of("sixsigma", "six sigma")));
		synonyms.add(new SkillSynonym("SDET", Set.of("sdet", "software developer in test", "software engineer in test"), TESTER));
		synonyms.add(new SkillSynonym("SONAR", Set.of("sonar", "sonarqube")));
		synonyms.add(new SkillSynonym("SPLUNK", Set.of("splunk", "splunk enterprise")));
		synonyms.add(new SkillSynonym("SQLSERVER", Set.of("sqlserver", "sql server", "ms sql server", "ms sqlserver", "microsoft sql server", "microsoft sqlserver")));
		synonyms.add(new SkillSynonym("SSL", Set.of("ssl", "ssl certificates")));
		synonyms.add(new SkillSynonym("STAKEHOLDER_MGMT", Set.of("stakeholder engagement", "stakeholder management", "stakeholder meetings")));
		synonyms.add(new SkillSynonym("SWITCHES", Set.of("switches", "switching")));
		synonyms.add(new SkillSynonym("SYSTEM_DESIGN", Set.of("system design", "systemdesign")));
		synonyms.add(new SkillSynonym("TAILWIND", Set.of("tailwind", "tailwind css")));
		synonyms.add(new SkillSynonym("TEAMLEAD", Set.of("team lead", "team management", "teamlead", "teammanager")));
		synonyms.add(new SkillSynonym("TEST_MANAGER", Set.of("test manager", "test management")));
		synonyms.add(new SkillSynonym("TEST_RAIL", Set.of("testrail", "test rail")));
		synonyms.add(new SkillSynonym("VIRTUAL_BOX", Set.of("virtual box", "virtualbox")));
		synonyms.add(new SkillSynonym("WCF", Set.of("wcf", "wcf service")));
		synonyms.add(new SkillSynonym("WEB_API", Set.of("web api", "webapi")));
		synonyms.add(new SkillSynonym("WEB_DEVELOPMENT", Set.of("webdevelopment", "web developer", "webdeveloper", "web development")));
		synonyms.add(new SkillSynonym("WINDOWS_SERVER", Set.of("windows server", "windows servers")));
		synonyms.add(new SkillSynonym("WINFORMS", Set.of("winforms", "winform application")));
		
	}
	
	/**
	* Refer to the SkillsSynonymsUtil interface for details 
	*/
	@Override
	public void addSynonymsForSkills(Set<String> extractedSkills ,Set<String> skills) {
		
		skills = skills.stream().filter(s -> StringUtils.hasText(s)).collect(Collectors.toSet());
		
		extractedSkills.addAll(skills.stream().map(skill -> skill.toLowerCase()).collect(Collectors.toSet()));
		
		skills.stream().forEach(skill -> {
			this.synonyms.stream().forEach(synonym -> synonym.getSynonymsForSkill(extractedSkills, skill));
		});
	
	}

}