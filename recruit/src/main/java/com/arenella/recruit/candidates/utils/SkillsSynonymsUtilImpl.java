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
	private static final Set<String> JAVASCRIPT				= Set.of("js", "javascript");
	private static final Set<String> CSS					= Set.of("css", "style sheets", "cascading style sheets");
	private static final Set<String> NODE					= Set.of("node", "node.js", "nodejs");
	private static final Set<String> NEXT					= Set.of("next", "next.js", "nextjs");
	private static final Set<String> VUE					= Set.of("vue", "vue.js", "vuejs");
	private static final Set<String> REACT					= Set.of("react", "react.js", "reactjs");
	private static final Set<String> CSHARP					= Set.of("c#", "csharp");
	private static final Set<String> DOTNET_CORE			= Set.of("dotnet", ".net", "dot net");
	private static final Set<String> ADO_DOTNET				= Set.of("ado", "ado.net", "ado .net");
	private static final Set<String> VB_DOTNET				= Set.of("vb", "vb.net", "vb .net");
	private static final Set<String> ASP_DOTNET				= Set.of("asp", "asp.net", "asp .net");
	private static final Set<String> FRONT_END				= Set.of("frontend", "front end", "fe", "front-end");
	private static final Set<String> BACK_END				= Set.of("backend", "back end", "be", "back-end");
	private static final Set<String> SPRING_CORE			= Set.of("spring", "spring core", "springcore", "spring-core");
	private static final Set<String> SPRING_BOOT			= Set.of("springboot", "spring boot", "spring-boot");
	private static final Set<String> SPRING_DATA			= Set.of("springdata", "spring data", "spring-data");
	private static final Set<String> SPRING_TEST			= Set.of("springtest", "spring test", "spring-test");
	private static final Set<String> SPRING_MVC				= Set.of("springmvc", "spring mvc", "spring-mvc");
	
	//sql server
	//Project management : project manager
	//Rest | soap : web services
	//technical suppoer : tech support
	//acceptance testing : uat
	//pl/sql : pl-sql oracle sql
	//selenium : selinium web driver
	//ms sql server: ms sqlserver
	//rest apis: rest apis
	//microservices: micro services

	
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
				case "qa":
				case "automation tester":
				case "manual tester":
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
				case "javascript":{
					synonyms.addAll(JAVASCRIPT);
					break;
				} 
				case "css":
				case "cascading style sheets":{
					synonyms.addAll(CSS);
					break;
				}
				case ".net":
				case "dot net":
				case "dotnemt":{
					synonyms.addAll(DOTNET_CORE);
					break;
				}
				case "ado .net":
				case "ado.net":{
					synonyms.addAll(DOTNET_CORE);
					synonyms.addAll(ADO_DOTNET);
					break;
				}
				case "vb .net":
				case "vb.net":{
					synonyms.addAll(DOTNET_CORE);
					synonyms.addAll(VB_DOTNET);
					break;
				}
				case "asp.net mvc":
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
				case "frontend":
				case "fe":{
					synonyms.addAll(FRONT_END);
					break;
				}
				case "back-end":
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