package com.arenella.recruit.candidates.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

/**
* Standard implementation of the SkillsSynonymsUtil interface 
* @author K Parkings
*/
@Component
public class SkillsSynonymsUtilImpl implements SkillsSynonymsUtil{

	private static final Set<String> TESTER 				= Set.of("qa", "tester", "test analyst"); 
	private static final Set<String> BUSINESS_ANALYST 		= Set.of("ba", "business analyst");
	private static final Set<String> JAVASCRIPT				= Set.of("js", "javascript", "java script", "java-script");
	private static final Set<String> CSS					= Set.of("css", "style sheets", "cascading style sheets","css3","css 3");
	private static final Set<String> NODE					= Set.of("node", "node.js", "nodejs");
	private static final Set<String> NEXT					= Set.of("next", "next.js", "nextjs");
	private static final Set<String> VUE					= Set.of("vue", "vue.js", "vuejs");
	private static final Set<String> REACT					= Set.of("react", "react.js", "reactjs", "react js", "react native");
	private static final Set<String> CSHARP					= Set.of("c#", "csharp");
	private static final Set<String> DOTNET_CORE			= Set.of("dotnet", ".net", "dot net");
	private static final Set<String> ADO_DOTNET				= Set.of("ado", "ado.net", "ado .net");
	private static final Set<String> VB_DOTNET				= Set.of("vb", "vb.net", "vb .net");
	private static final Set<String> ASP_DOTNET				= Set.of("asp", "asp.net", "asp .net","asp.net mvc","asp.net mvc core");
	private static final Set<String> FRONT_END				= Set.of("frontend", "front end", "fe", "front-end", "front end development", "frontend development");
	private static final Set<String> BACK_END				= Set.of("backend", "back end", "be", "back-end", "backend development", "bac kend development");
	private static final Set<String> SPRING_CORE			= Set.of("spring", "spring core", "springcore", "spring-core");
	private static final Set<String> SPRING_BOOT			= Set.of("springboot", "spring boot", "spring-boot");
	private static final Set<String> SPRING_DATA			= Set.of("springdata", "spring data", "spring-data");
	private static final Set<String> SPRING_TEST			= Set.of("springtest", "spring test", "spring-test");
	private static final Set<String> SPRING_MVC				= Set.of("springmvc", "spring mvc", "spring-mvc");
	private static final Set<String> IT_RECRUITER			= Set.of("recruiter", "it recruiter", "it recruitment");
	private static final Set<String> MICROSERVICES			= Set.of("microservices", "micro services", "microservice", "micro service", "microservice architecture");
	private static final Set<String> SELENIUM				= Set.of("selenium", "selinium web driver","selinium webdriver");
	private static final Set<String> UAT					= Set.of("uat", "user acceptance testing", "acceptance testing");
	private static final Set<String> TECH_SUPPORT			= Set.of("tech support", "technical support");
	private static final Set<String> REST					= Set.of("rest", "rest api","rest apis","rest api's", "rest webservices","restful web services", "webservices");
	private static final Set<String> SOAP					= Set.of("soap", "soap web services", "webservices");
	private static final Set<String> WEB_SERVICES			= Set.of("webservice", "web services", "webservices", "web service");
	private static final Set<String> PROJECT_MANAGEMENT		= Set.of("project manager", "project management", "projectmanager");
	private static final Set<String> PL_SQL					= Set.of("plsql", "pl-sql", "pl/sql", "oracle sql");
	private static final Set<String> HTML					= Set.of("html", "html4", "html5");
	private static final Set<String> CICD					= Set.of("ci", "cd", "ci/cd", "ci-cd", "ci cd", "cicd", "ci / cd");
	private static final Set<String> SCRUMMASTER			= Set.of("scrummaster", "scrum master");
	private static final Set<String> DEVOPS					= Set.of("devops", "dev ops", "dev-ops");
	private static final Set<String> ROBOT_FRAMEWORK		= Set.of("robotframework", "robot framework");
	private static final Set<String> ACTIVE_DIRECTORY		= Set.of("active directory", "activedirectory");
	private static final Set<String> ACTIVE_MQ				= Set.of("active mq", "activemq");
	private static final Set<String> ADMINISTRATION			= Set.of("administration", "administrator");
	private static final Set<String> ALGORITHMS				= Set.of("algorithm", "algorithms");
	private static final Set<String> ANGULAR				= Set.of("angular", "angularjs", "angular.js","angular js");
	private static final Set<String> KAFKA					= Set.of("kafka", "apache kafka");
	private static final Set<String> API					= Set.of("api", "apis");
	private static final Set<String> AUTOMATION_TESTING		= Set.of("automated testing", "automation testing", "test automation", "test automation engineer", "test automation specialist");
	private static final Set<String> BANKING				= Set.of("bank", "banking");
	private static final Set<String> BIT_BUCKET				= Set.of("bit bucket", "bitbucket");
	private static final Set<String> JAVA					= Set.of("java", "core java");
	private static final Set<String> DATA_ANALYSIS			= Set.of("data scientist", "data analysis", "data analyst", "data analytics");
	private static final Set<String> DATA_BRICKS			= Set.of("databricks", "data bricks");
	private static final Set<String> DATA_CENTER			= Set.of("datacenter", "data center");
	private static final Set<String> DATA_MANAGEMENT		= Set.of("data management", "data governance");
	private static final Set<String> DATA_MINING			= Set.of("data mining", "data scraping");
	private static final Set<String> DEFECT_LOGGING			= Set.of("defect loging", "defect tracking");
	private static final Set<String> DYNAMO					= Set.of("dynamo", "dynamo db");
	private static final Set<String> ECMA					= Set.of("ecma", "ecma script");
	private static final Set<String> ELASTIC				= Set.of("elastic", "elasticsearch");
	private static final Set<String> EMBEDDED				= Set.of("embedded", "embedded software");
	private static final Set<String> ENTITY_FRAMEWORK		= Set.of("entity framework", "entity-framework");
	private static final Set<String> EXCHANGE				= Set.of("exchange", "excahnge server");
	private static final Set<String> EXPRESS				= Set.of("express", "expressjs");
	private static final Set<String> FIREWALL				= Set.of("firewalls", "firewall");
	private static final Set<String> FULLSTACK				= Set.of("full stack", "fullstack");
	private static final Set<String> FUNCTIONAL_TESTING		= Set.of("functional test", "functional testing");
	private static final Set<String> GATSBY					= Set.of("gatsby", "gatsbyjs");
	private static final Set<String> GHERKIN				= Set.of("gherkin/cucumber", "gherkin");
	private static final Set<String> GIT					= Set.of("git", "git and github","github");
	private static final Set<String> HYPER_V				= Set.of("hyper-v", "hyper v", "hypervisors");
	private static final Set<String> IOS_DEV				= Set.of("ios", "ios dev", "ios developer");
	private static final Set<String> ISTQB					= Set.of("istqb", "istqb certified tester");
	private static final Set<String> ITIL					= Set.of("itil", "itilv3");
	private static final Set<String> JASPER					= Set.of("jasper reports", "jasperreport");
	private static final Set<String> JAVA_EIGHT				= Set.of("java 8", "java8", "java");
	private static final Set<String> JAVA_EE				= Set.of("java ee", "j2ee","jee", "java");
	private static final Set<String> JPA					= Set.of("jpa", "jpa/hibernate");
	private static final Set<String> JUNIPER				= Set.of("juniper", "juniper network", "juniper switches");
	private static final Set<String> JUPYTER				= Set.of("jupyter", "jupyter notebook", "jupyter notebooks");
	private static final Set<String> LOAD_TESTING			= Set.of("load runner", "load testing");
	private static final Set<String> MANUAL_TESTER			= Set.of("manual test", "manual tester", "manual testing");
	private static final Set<String> MATERIAL_UI			= Set.of("material-ui", "material ui");
	private static final Set<String> POWER_BI				= Set.of("microsoft powerbi", "powerbi");
	private static final Set<String> MONGO					= Set.of("mongo", "mongodb");
	private static final Set<String> NETWORK_ADMIN			= Set.of("network admin", "network administration","network configuration");
	private static final Set<String> OBJECTIVE_C			= Set.of("objective", "objective-c","objectivec");
	private static final Set<String> OPERATIONS				= Set.of("ops", "operations","operations management");
	private static final Set<String> PALOALTO				= Set.of("paloalto", "palo alto","palo alto networks");
	private static final Set<String> PEGA					= Set.of("pega", "pegasystems");
	private static final Set<String> PEN_TESTER				= Set.of("penetration", "pen tester", "pen testing");
	private static final Set<String> POSTGRES				= Set.of("postgres", "postgressql");
	private static final Set<String> POWERSHELL				= Set.of("powershell", "powershell scripting");
	private static final Set<String> PRINCE2				= Set.of("prince 2", "prince2");
	private static final Set<String> PROMETHEUS				= Set.of("prometheus", "prometheus.io");
	private static final Set<String> QLIKSENSE				= Set.of("qliksense", "qlik sense");
	private static final Set<String> QUALITY_CENTER			= Set.of("quality center", "qualitycenter");
	private static final Set<String> RABBIT_MQ				= Set.of("rabbit mq", "rabbitmq");
	private static final Set<String> REDUX					= Set.of("redux", "redux.js");
	private static final Set<String> REGRESSION_TESTER		= Set.of("regression", "regression testing");
	private static final Set<String> REQUIREMENTS_ANALYSIS	= Set.of("requirements", "requirements analysis");
	private static final Set<String> RESPONSIVE_DESIGN		= Set.of("responsive design", "responsive web design");
	private static final Set<String> RESTASSURED			= Set.of("restassured", "rest assured");
	private static final Set<String> ROUTERS				= Set.of("routers", "routing and switching");
	private static final Set<String> SANITY_TESTING			= Set.of("sanity", "sanity testing");
	private static final Set<String> SERVICE_NOW			= Set.of("servicenow", "service now");
	private static final Set<String> SERVLETS				= Set.of("servlet", "servlets");
	private static final Set<String> SHELL_PROGRAMING		= Set.of("shell programing", "shell scripting");
	private static final Set<String> SIXSIGMA				= Set.of("sixsigma", "six sigma");
	private static final Set<String> SDET					= Set.of("sdet", "software developer in test", "software engineer in test");
	private static final Set<String> SONAR					= Set.of("sonar", "sonarqube");
	private static final Set<String> SPLUNK					= Set.of("splunk", "splunk enterprise");
	private static final Set<String> SQLSERVER				= Set.of("sqlserver", "sql server", "ms sql server", "ms sqlserver", "microsoft sql server", "microsoft sqlserver");
	private static final Set<String> SSL					= Set.of("ssl", "ssl certificates");
	private static final Set<String> STAKEHOLDER_MGMT		= Set.of("stakeholder engagement", "stakeholder management", "stakeholder meetings");
	private static final Set<String> SWITCHES				= Set.of("switches", "switching");
	private static final Set<String> SYSTEM_DESIGN			= Set.of("system design", "systemdesign");
	private static final Set<String> TAILWIND				= Set.of("tailwind", "tailwind css");
	private static final Set<String> TEAMLEAD				= Set.of("team lead", "team management", "teamlead", "teammanager");
	private static final Set<String> TEST_MANAGER			= Set.of("test manager", "test management");
	private static final Set<String> TEST_RAIL				= Set.of("testrail", "test rail");
	private static final Set<String> VIRTUAL_BOX			= Set.of("virtual box", "virtualbox");
	private static final Set<String> WCF					= Set.of("wcf", "wcf service");
	private static final Set<String> WEB_API				= Set.of("web api", "webapi");
	private static final Set<String> WEB_DEVELOPMENT		= Set.of("webdevelopment", "web developer", "webdeveloper", "web development");
	private static final Set<String> WINDOWS_SERVER			= Set.of("windows server", "windows servers");
	private static final Set<String> WINFORMS				= Set.of("winforms", "winform application");
	
	/**
	* Refer to the SkillsSynonymsUtil interface for details 
	*/
	@Override
	public Set<String> addtSynonymsForSkills(Set<String> skills) {
		
		Set<String> originalSkills = new HashSet<>();
		Set<String> synonyms		= new HashSet<>();
		
		originalSkills.addAll(skills.stream().map(skill -> skill.toLowerCase()).collect(Collectors.toSet()));
		
		originalSkills.stream().forEach(skill -> {
			
			switch(skill) {
				case "robotframework":
				case "robot framework":{
					synonyms.addAll(ROBOT_FRAMEWORK);
					break;
				}
				case "winforms":
				case "winform application":{
					synonyms.addAll(WINFORMS);
					break;
				}
				case "windows server":
				case "windows servers":{
					synonyms.addAll(WINDOWS_SERVER);
					break;
				}
				case "webdevelopment":
				case "web developer":
				case "webdeveloper":
				case "web development":{
					synonyms.addAll(WEB_DEVELOPMENT);
					break;
				}
				case "webapi":
				case "web api":{
					synonyms.addAll(WEB_API);
					break;
				}
				case "wcf":
				case "wcf service":{
					synonyms.addAll(WCF);
					break;
				}
				case "virtual box":
				case "virtualbox":{
					synonyms.addAll(VIRTUAL_BOX);
					break;
				}
				case "testrail":
				case "test rail":{
					synonyms.addAll(TEST_RAIL);
					break;
				}
				case "test manager":
				case "test management":{
					synonyms.addAll(TEST_MANAGER);
					break;
				}
				case "team lead":
				case "team management":
				case "teamlead":
				case "teammanager":{
					synonyms.addAll(TEAMLEAD);
					break;
				}
				case "tailwind":
				case "tailwind css":{
					synonyms.addAll(TAILWIND);
					break;
				}
				case "system design":
				case "systemdesign":{
					synonyms.addAll(SYSTEM_DESIGN);
					break;
				}
				case "switches":
				case "switching":{
					synonyms.addAll(SWITCHES);
					break;
				}
				case "stakeholder engagement":
				case "stakeholder management":
				case "stakeholder meetings":{
					synonyms.addAll(STAKEHOLDER_MGMT);
					break;
				}
				case "ssl":
				case "ssl certificates":{
					synonyms.addAll(SSL);
					break;
				}
				case "sqlserver":
				case "sql server":
				case "ms sql server":
				case "ms sqlserver":
				case "microsoft sql server":
				case "microsoft sqlserver":{
					synonyms.addAll(SQLSERVER);
					break;
				}
				case "splunk":
				case "splunk enterprise":{
					synonyms.addAll(SPLUNK);
					break;
				}
				case "sonar":
				case "sonarqube":{
					synonyms.addAll(SONAR);
					break;
				}
				case "sixsigma":
				case "six sigma":{
					synonyms.addAll(SIXSIGMA);
					break;
				}
				case "sdet":
				case "software developer in test":
				case "software engineer in test":{
					synonyms.addAll(SDET);
					synonyms.addAll(TESTER);
					break;
				}
				case "shell programing":
				case "shell scripting":{
					synonyms.addAll(SHELL_PROGRAMING);
					break;
				}
				case "servlet":
				case "servlets":{
					synonyms.addAll(SERVLETS);
					break;
				}
				case "servicenow":
				case "service now":{
					synonyms.addAll(SERVICE_NOW);
					break;
				}
				case "sanity":
				case "sanity testing":{
					synonyms.addAll(SANITY_TESTING);
					synonyms.addAll(TESTER);
					break;
				}
				case "routers":
				case "routers and switching":{
					synonyms.addAll(ROUTERS);
					break;
				}
				case "restassured":
				case "rest assured":{
					synonyms.addAll(RESTASSURED);
					break;
				}
				case "responsive design":
				case "responsive web design":{
					synonyms.addAll(RESPONSIVE_DESIGN);
					break;
				}
				case "requirements":
				case "requirements analysis":{
					synonyms.addAll(REQUIREMENTS_ANALYSIS);
					break;
				}
				case "redux":
				case "redux.js":{
					synonyms.addAll(REDUX);
					break;
				}
				case "regression":
				case "regression tester":{
					synonyms.addAll(REGRESSION_TESTER);
					synonyms.addAll(TESTER);
					break;
				}
				case "rabbit mq":
				case "rabbitmq":{
					synonyms.addAll(RABBIT_MQ);
					break;
				}
				case "quality center":
				case "qualitycenter":{
					synonyms.addAll(QUALITY_CENTER);
					break;
				}
				case "prometheus":
				case "prometheus.io":{
					synonyms.addAll(PROMETHEUS);
					break;
				}
				case "qliksense":
				case "qlik sense":{
					synonyms.addAll(QLIKSENSE);
					break;
				}
				case "prince2":
				case "prince 2":{
					synonyms.addAll(PRINCE2);
					break;
				}
				case "postgres":
				case "postgressql":{
					synonyms.addAll(POSTGRES);
					break;
				}
				case "powershell":
				case "powershell scripting":{
					synonyms.addAll(POWERSHELL);
					break;
				}
				case "pega":
				case "pega systems":{
					synonyms.addAll(PEGA);
					break;
				}
				case "penetration":
				case "pen tester":
				case "pen testing":{
					synonyms.addAll(TESTER);
					synonyms.addAll(PEN_TESTER);
					break;
				}
				case "palo":
				case "palo alto":
				case "palo alto networks":{
					synonyms.addAll(PALOALTO);
					break;
				}
				case "network admin":
				case "network administration":
				case "network configuration":{
					synonyms.addAll(NETWORK_ADMIN);
					break;
				}
				case "ops":
				case "operations":
				case "operations management":{
					synonyms.addAll(OPERATIONS);
					break;
				}
				case "objective":
				case "objective-c":
				case "objective c":{
					synonyms.addAll(OBJECTIVE_C);
					break;
				}
				case "mongo":
				case "mongodb":{
					synonyms.addAll(MONGO);
					break;
				}
				case "microsoft powerbi":
				case "powerbi":{
					synonyms.addAll(POWER_BI);
					break;
				}
				case "material-ui":
				case "material ui":{
					synonyms.addAll(MATERIAL_UI);
					break;
				}
				case "manual test":
				case "manual tester":
				case "manual testing":{
					synonyms.addAll(TESTER);
					synonyms.addAll(MANUAL_TESTER);
					break;
				}
				case "load runner":
				case "load testing":{
					synonyms.addAll(LOAD_TESTING);
					break;
				}
				case "juniper":
				case "juniper network":
				case "juniper switches":{
					synonyms.addAll(JUNIPER);
					break;
				}
				case "jupyter":
				case "jupyter notebook":
				case "jupyter notebooks":{
					synonyms.addAll(JUPYTER);
					break;
				}
				case "jpa":
				case "jpa/hibernate":{
					synonyms.addAll(JPA);
					break;
				}
				case "data management":
				case "data governance":{
					synonyms.addAll(DATA_MANAGEMENT);
					break;
				}
				case "java ee":
				case "j2ee":
				case "jee":{
					synonyms.addAll(JAVA_EE);
					break;
				}
				case "java8":
				case "java 8":{
					synonyms.addAll(JAVA_EIGHT);
					break;
				}
				case "jasper reports":
				case "jasperreport":{
					synonyms.addAll(JASPER);
					break;
				}
				case "itil":
				case "itilv3":{
					synonyms.addAll(ITIL);
					break;
				}
				case "istqb":
				case "istqb certified tester":{
					synonyms.addAll(ISTQB);
					break;
				}
				case "hyper-v":
				case "hyper visors":
				case "hyper v":{
					synonyms.addAll(HYPER_V);
					break;
				}
				case "ios":
				case "ios developer":
				case "ios dev":{
					synonyms.addAll(IOS_DEV);
					break;
				}
				case "git ":
				case "git and github":
				case "github":{
					synonyms.addAll(GIT);
					break;
				}
				case "gherkin/cucumber":
				case "gherkin":{
					synonyms.addAll(GHERKIN);
					break;
				}
				case "gatsby":
				case "gatsbyjs":{
					synonyms.addAll(GATSBY);
					break;
				}
				case "functional test":
				case "functional testing":{
					synonyms.addAll(FUNCTIONAL_TESTING);
					break;
				}
				case "fullstack":
				case "full stack":{
					synonyms.addAll(FULLSTACK);
					break;
				}
				case "firewall":
				case "firewalls":{
					synonyms.addAll(FIREWALL);
					break;
				}
				case "express":
				case "expressjs":{
					synonyms.addAll(EXPRESS);
					break;
				}
				case "exchange":
				case "exchange server":{
					synonyms.addAll(EXCHANGE);
					break;
				}
				case "entity framework":
				case "entity-framework":{
					synonyms.addAll(ENTITY_FRAMEWORK);
					break;
				}
				case "embedded":
				case "embedded software":{
					synonyms.addAll(EMBEDDED);
					break;
				}
				case "elastic":
				case "elasticsearch":{
					synonyms.addAll(ELASTIC);
					break;
				}
				case "ecma":
				case "ecma script":{
					synonyms.addAll(ECMA);
					break;
				}
				case "dynamo":
				case "dynamo db":{
					synonyms.addAll(DYNAMO);
					break;
				}
				case "defect logging":
				case "defect tracking":{
					synonyms.addAll(DEFECT_LOGGING);
					break;
				}
				case "data mining":
				case "data scraping":{
					synonyms.addAll(DATA_MINING);
					break;
				}
				case "data center":
				case "datacenter":{
					synonyms.addAll(DATA_CENTER);
					break;
				}
				case "databricks":
				case "data bricks":{
					synonyms.addAll(DATA_BRICKS);
					break;
				}
				case "data analysis":
				case "data analyst":
				case "data analytics":{
					synonyms.addAll(DATA_ANALYSIS);
					break;
				}
				case "activedirectory":
				case "active directory":{
					synonyms.addAll(ACTIVE_DIRECTORY);
					break;
				}
				case "java":
				case "core java":{
					synonyms.addAll(JAVA);
					break;
				}
				case "bitbucket":
				case "bit bucket":{
					synonyms.addAll(BIT_BUCKET);
					break;
				}
				case "bank":
				case "banking":{
					synonyms.addAll(BANKING);
					break;
				}
				case "test automation":
				case "test automation engineer":
				case "test automation specialist":
				case "automated testing":
				case "automation testing":{
					synonyms.addAll(AUTOMATION_TESTING);
					break;
				}
				case "api":
				case "apis":{
					synonyms.addAll(API);
					break;
				}
				case "kafka":
				case "apache kafka":{
					synonyms.addAll(KAFKA);
					break;
				}
				case "administration":
				case "administrator":{
					synonyms.addAll(ADMINISTRATION);
					break;
				}
				case "algorithm":
				case "algorithms":{
					synonyms.addAll(ALGORITHMS);
					break;
				}
				case "activemq":
				case "active mq":{
					synonyms.addAll(ACTIVE_MQ);
					break;
				}
				case "devops":
				case "dev-ops":
				case "dev ops":{
					synonyms.addAll(DEVOPS);
					break;
				}
				case "scrum master":
				case "scrummaster":{
					synonyms.addAll(SCRUMMASTER);
					break;
				}
				case "angular":
				case "angularjs":
				case "angular.js":
				case "angular js":{
					synonyms.addAll(ANGULAR);
					break;
				}
				case "ci":
				case "cd":
				case "ci/cd":
				case "ci-cd":
				case "ci cd":
				case "cicd":
				case "ci / cd":{
					synonyms.addAll(CICD);
					break;
				}
				case "html":
				case "html4":
				case "html5":{
					synonyms.addAll(HTML);
					break;
				}
				case "plsql":
				case "pl-sql":
				case "pl/sql":
				case "oracle sql":{
					synonyms.addAll(PL_SQL);
					break;
				}
				case "project manager":
				case "projectmanager":
				case "project management":{
					synonyms.addAll(PROJECT_MANAGEMENT);
					break;
				}
				case "webservice":
				case "web service":
				case "web services":
				case "webservices":{
					synonyms.addAll(WEB_SERVICES);
					break;
				}
				case "rest":
				case "rest api":
				case "rest apis":
				case "rest api's":
				case "rest webservices":
				case "restful web services":{
					synonyms.addAll(REST);
					break;
				}
				case "soap":
				case "soap web services":{
					synonyms.addAll(SOAP);
					break;
				}
				case "tech support":
				case "technical support":{
					synonyms.addAll(TECH_SUPPORT);
					break;
				}
				case "uat":
				case "acceptance testing":
				case "user acceptance testing":{
					synonyms.addAll(UAT);
					break;
				}
				case "selenium":
				case "selenium webdriver":
				case "selenium web driver":{
					synonyms.addAll(SELENIUM);
					break;
				}
				case "microservices":
				case "micro service":
				case "micro services":
				case "microservice architecture":
				case "microservice":{
					synonyms.addAll(MICROSERVICES);
					break;
				}
				case "recruiter":
				case "it recruitment":
				case "it recruiter":{
					synonyms.addAll(IT_RECRUITER);
					break;
				}
				case "qa":
				case "automation tester":
				case "test analyst":
				case "tester":{
					synonyms.addAll(TESTER);
					break;
				}
				case "ba":
				case "business analyst":{
					synonyms.addAll(BUSINESS_ANALYST);
					break;
				}
				case "js":
				case "java script":
				case "java-script":
				case "javascript":{
					synonyms.addAll(JAVASCRIPT);
					break;
				} 
				case "css":
				case "css3":
				case "css 3":
				case "cascading style sheets":{
					synonyms.addAll(CSS);
					break;
				}
				case ".net":
				case "dot net":
				case "dotnet":{
					synonyms.addAll(DOTNET_CORE);
					break;
				}
				case "ado .net":
				case "ado.net":{
					synonyms.addAll(DOTNET_CORE);
					synonyms.addAll(ADO_DOTNET);
					break;
				}
				case "vb":
				case "vb .net":
				case "vb.net":{
					synonyms.addAll(DOTNET_CORE);
					synonyms.addAll(VB_DOTNET);
					break;
				}
				case "asp":
				case "asp.net mvc":
				case "asp.net mvc core":
				case "asp .net":
				case "asp.net":{
					synonyms.addAll(DOTNET_CORE);
					synonyms.addAll(ASP_DOTNET);
					break;
				}
				case "node":
				case "nodejs":
				case "node.js":{
					synonyms.addAll(NODE);
					break;
				}
				case "next":
				case "extjs":
				case "next.js":{
					synonyms.addAll(NEXT);
					break;
				}
				case "vue":
				case "vuejs":
				case "vue.js":{
					synonyms.addAll(VUE);
					break;
				}
				case "react":
				case "react js":
				case "react native":
				case "reactjs":
				case "react.js":{
					synonyms.addAll(REACT);
					break;
				}
				case "c#":
				case "csharp":{
					synonyms.addAll(CSHARP);
					break;
				}
				case "front-end":
				case "front end development":
				case "frontend development":
				case "frontend":
				case "fe":{
					synonyms.addAll(FRONT_END);
					break;
				}
				case "back-end":
				case "back end development":
				case "backend development":
				case "backend":
				case "be":{
					synonyms.addAll(BACK_END);
					break;
				}
				case "spring core":
				case "springcore":
				case "spring":{
					synonyms.addAll(SPRING_CORE);
					break;
				}
				case "spring-data":
				case "spring data":
				case "springdata":{
					synonyms.addAll(SPRING_CORE);
					synonyms.addAll(SPRING_DATA);
					break;
				}
				case "spring-test":
				case "spring test":
				case "springtest":{
					synonyms.addAll(SPRING_CORE);
					synonyms.addAll(SPRING_TEST);
					break;
				}
				case "spring-mvc":
				case "spring mvc":
				case "springmvc":{
					synonyms.addAll(SPRING_CORE);
					synonyms.addAll(SPRING_MVC);
					break;
				}
				case "spring-boot":
				case "spring boot":
				case "springboot":{
					synonyms.addAll(SPRING_CORE);
					synonyms.addAll(SPRING_BOOT);
					break;
				}
			}
			
		});
		
		return synonyms;
	
	}

}