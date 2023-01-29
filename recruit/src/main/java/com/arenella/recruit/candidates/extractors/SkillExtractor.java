package com.arenella.recruit.candidates.extractors;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arenella.recruit.candidates.beans.CandidateExtractedFilters.CandidateExtractedFiltersBuilder;
import com.arenella.recruit.curriculum.dao.SkillsDao;

/**
* Extractor to determine languages to filter on
* @author K Parkings
*/
@Component
public class SkillExtractor implements JobSpecifcationFilterExtractor{
	
	@Autowired
	private SkillsDao skillsDao;
	
	/**
	* Refer to JobSpecifcationFilterExtractor interface for details
	*/
	public void extractFilters(String documentText, CandidateExtractedFiltersBuilder filterBuilder) {
		
		Set<String> extractedSkills = new HashSet<>();
		
		skillsDao.getSkills().stream().filter(s -> !s.equals("")).forEach(skill -> {
					
			String skillPatternSpace 		= skill.trim() + " ";
			String skillPatternPeriod 		= skill.trim() + ".";
			String skillPatternNewLine 		= skill.trim() + "\n";
			String skillPatternComma 		= skill.trim() + ",";
			String skillPatternSemiColom 	= skill.trim() + ";";
					
			if (documentText.contains(skillPatternSpace) 
				|| documentText.contains(skillPatternPeriod) 
				|| documentText.contains(skillPatternNewLine)
				|| documentText.contains(skillPatternComma)
				|| documentText.contains(skillPatternSemiColom)){
				
				extractedSkills.add(skill);
				
			}
					
		});
		
		filterBuilder.skills(extractedSkills);
		
	}
}