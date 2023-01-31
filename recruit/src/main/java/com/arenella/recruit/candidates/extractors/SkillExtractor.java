package com.arenella.recruit.candidates.extractors;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arenella.recruit.candidates.beans.CandidateExtractedFilters.CandidateExtractedFiltersBuilder;
import com.arenella.recruit.candidates.dao.CandidateSkillsDao;

/**
* Extractor to determine languages to filter on
* @author K Parkings
*/
@Component
public class SkillExtractor implements JobSpecifcationFilterExtractor{
	
	@Autowired
	private CandidateSkillsDao skillsDao;
	
	private String sanitizeDocumentText(String documentText) {
		
		String sanitizedDocumentText = documentText.replaceAll("\\t", " ");
		sanitizedDocumentText = sanitizedDocumentText.replaceAll("\\.", " ");
		sanitizedDocumentText = sanitizedDocumentText.replaceAll(",", " ");
		sanitizedDocumentText = sanitizedDocumentText.replaceAll(";", " ");
		sanitizedDocumentText = sanitizedDocumentText.replaceAll("/", " ");
		sanitizedDocumentText = sanitizedDocumentText.replaceAll("-", " ");
		sanitizedDocumentText = sanitizedDocumentText.replaceAll("\\n", " ");
		
		return sanitizedDocumentText;
	}
	
	/**
	* Refer to JobSpecifcationFilterExtractor interface for details
	*/
	public void extractFilters(String documentText, CandidateExtractedFiltersBuilder filterBuilder) {
		
		Set<String> extractedSkills = new HashSet<>();
		
		Set<String> blacklist = Set.of("it", "back", "informatica","capital","people","idea","auto","ideal", "development");
		
		//TODO: Need to add synonyms reducer. This will find the most popular synonym and use that instead of other synonyms
		//		example core java / java = java
		//		
		
		//TODO: Blacklist per type. If we determine jobTitle we can strip out keywords that are not relevant to the individual roles
		//switch(filterBuilder.build().getJobTitle()) {}
		
		String sanitizedDocumentText = sanitizeDocumentText(documentText);
		
		//TODO: [KP] Cannot use this skills. Need event to make copy in candidate schema when search is made ( happens already ?? to populate curriculum skills so just add to table in correct schema)
		skillsDao.getSkills().stream().filter(s -> !s.equals("")).forEach(skill -> {
					
			String skillPatternSpace 		= " " + skill.trim() + " ";
			
			if (sanitizedDocumentText.contains(skillPatternSpace)){
				
				if (!blacklist.contains(skill)) {
					extractedSkills.add(skill);
				}
				
			}
					
		});
		
		filterBuilder.skills(extractedSkills);
		
	}
}