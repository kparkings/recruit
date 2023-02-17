package com.arenella.recruit.candidates.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
* Class manages Synonyms for a Skill
* @author K Parkings
*/
public class SkillSynonym {

	private String 				groupName;
	private Set<String> 		skills 				= new HashSet<>();
	private SkillSynonym[] 		secondarySynonyms 	= new SkillSynonym[]{};
	
	/**
	* Constructor
	* @param groupName			- Name of the skills synonym group
	* @param skills 			- All synonyms for the Skill
	* @param secondaySynonyms	- Not for matching on but to be returned if match exists
	*/
	@SafeVarargs
	public SkillSynonym(String groupName, Set<String> skills, SkillSynonym... secondarySynonyms) {
		
		this.groupName = groupName;
		this.skills.addAll(skills.stream().map(s -> s.toLowerCase()).map(s -> s.trim()).collect(Collectors.toSet()));
		this.secondarySynonyms = secondarySynonyms;
		
		//Arrays.asList(secondarySynonyms).stream().forEach(ss -> {
		//	this.secondarySynonyms.addAll(ss.stream().map(s -> s.toLowerCase()).map(s -> s.trim()).collect(Collectors.toSet()));
		//});
		
	}
	
	/**
	* Returns the name of the skills Synonyms group
	* @return name of group
	*/
	public String getGroupName() {
		return this.groupName;
	}
	
	/**
	* Returns the secondary synonyms
	* @return secondary synonyms
	*/
	public SkillSynonym[] getSecondarySynonyms(){
		return this.secondarySynonyms;
	}
	
	/**
	* If the skill is one of the synonyms for this skill return the 
	* full list of synonyms. Otherwise skill is not a synonym of this 
	* skill so we return an empty Set 
	* @param skill - Skill to check against
	* @return synonyms for the skill is skill is synonym of this Skill type
	*/
	public void getSynonymsForSkill(Set<String> extractedSkills, String skill){
		
		String sanitizedSkill = skill.toLowerCase();
		sanitizedSkill = sanitizedSkill.trim();
		
		if (!skills.contains(sanitizedSkill)) {
			return;
		}
		
		extractedSkills.addAll(this.skills);
		
		final Set<String> extracted = new HashSet<>();
		
		Arrays.asList(this.secondarySynonyms).stream().forEach(ss -> extractSynonyms(extracted, ss));
		
		extractedSkills.addAll(extracted);
		
	}
	
	/**
	* Filters down nested secondary synonyms extracting skills as it goes
	* @param extracted 	- All the extracted skills
	* @param ss 		- current secondary synonym
	*/
	private void extractSynonyms(Set<String> extracted, SkillSynonym ss){
		
		extracted.addAll(ss.skills);
		
		Arrays.asList(ss.secondarySynonyms).stream().forEach(s ->extractSynonyms(extracted, s));
	}
	
}
