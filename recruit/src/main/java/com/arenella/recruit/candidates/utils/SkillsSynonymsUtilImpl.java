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
				case "qa":{
					synonyms.add("tester");
					synonyms.add("test analyst");
					break;
				}
				case "test analyst":{
					synonyms.add("qa");
					synonyms.add("tester");
					break;
				}
				case "tester":{
					synonyms.add("qa");
					synonyms.add("test analyst");
					break;
				}
				case "js":{
					synonyms.add("javascript");
					break;
				}
				case "javascript":{
					synonyms.add("js");
					break;
				} 
				case "css":{
					synonyms.add("cascading style sheets");
					break;
				}
				case "cascading style sheets":{
					synonyms.add("css");
					break;
				}
				case "node":{
					synonyms.add("nodejs");
					synonyms.add("node.js");
					break;
				}
				case "nodejs":{
					synonyms.add("node");
					synonyms.add("node.js");
					break;
				}
				case "node.js":{
					synonyms.add("node");
					synonyms.add("nodejs");
					break;
				}
				case "vue":{
					synonyms.add("vuejs");
					synonyms.add("vue.js");
					break;
				}
				case "vuejs":{
					synonyms.add("vue");
					synonyms.add("vue.js");
					break;
				}
				case "vue.js":{
					synonyms.add("vue");
					synonyms.add("vuejs");
					break;
				}
				case "react":{
					synonyms.add("reactjs");
					synonyms.add("react.js");
					break;
				}
				case "reactjs":{
					synonyms.add("react");
					synonyms.add("react.js");
					break;
				}
				case "react.js":{
					synonyms.add("react");
					synonyms.add("reactjs");
					break;
				}
				case "c#":{
					synonyms.add("csharp");
					break;
				}
				case "csharp":{
					synonyms.add("c#");
					break;
				}
				case "front-end":{
					synonyms.add("fe");
					synonyms.add("frontend");
					break;
				}
				case "frontend":{
					synonyms.add("fe");
					synonyms.add("front-end");
					break;
				}
				case "fe":{
					synonyms.add("front-end");
					synonyms.add("frontend");
					break;
				}
				case "back-end":{
					synonyms.add("be");
					synonyms.add("backend");
					break;
				}
				case "backend":{
					synonyms.add("be");
					synonyms.add("back-end");
					break;
				}
				case "be":{
					synonyms.add("back-end");
					synonyms.add("backend");
					break;
				}
				case "spring core":{
					synonyms.add("spring");
					synonyms.add("springcore");
					break;
				}
				case "springcore":{
					synonyms.add("spring");
					synonyms.add("spring core");
					break;
				}
				case "spring":{
					synonyms.add("spring core");
					synonyms.add("spring");
					synonyms.add("springcore");
					break;
				}
				case "springdata":
				case "springtest":
				case "springmvc":
				case "springboot":{
					synonyms.add("spring");
					synonyms.add("spring core");
					synonyms.add("springcore");
					break;
				}
				case "spring data":
				case "spring test":
				case "spring mvc":
				case "spring boot":{
					synonyms.add("spring");
					synonyms.add("spring core");
					synonyms.add("springcore");
					break;
				}
			}
			
		});
		
		return synonyms;
	
	}

}
