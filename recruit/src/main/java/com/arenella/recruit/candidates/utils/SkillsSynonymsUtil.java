package com.arenella.recruit.candidates.utils;

import java.util.Set;

/**
* Util for taking a Skill and determining other names for the same Skill.
* 
* An example: js could also be javascript
* 
* @author K Parkings
*/
public interface SkillsSynonymsUtil {

	/**
	* Adds all known synonyms for each skill to the incommming
	* skills Set 
	* @param skills - Skills to add synonyms for
	* @return synonyms
	*/
	public Set<String> addtSynonymsForSkills(Set<String> skills);
	
}
