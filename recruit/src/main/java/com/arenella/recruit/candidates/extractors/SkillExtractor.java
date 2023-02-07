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
		sanitizedDocumentText = sanitizedDocumentText.replaceAll(" ", " ");
		
		return sanitizedDocumentText;
	}

	/**
	* Refer to JobSpecifcationFilterExtractor interface for details
	*/
	public void extractFilters(String documentText, CandidateExtractedFiltersBuilder filterBuilder) {
		
		Set<String> extractedSkills = new HashSet<>();
		
		//TODO: Need to add synonyms reducer. This will find the most popular synonym and use that instead of other synonyms
		//		example core java / java = java
		//		
		
		String sanitizedDocumentText = sanitizeDocumentText(documentText);
		
		skillsDao.getSkills().stream().filter(s -> !s.equals("")).forEach(skill -> {
					
			String skillPatternSpace = " " + skill.trim() + " ";
			
			if (sanitizedDocumentText.contains(skillPatternSpace)){
				extractedSkills.add(skill);
			}
					
		});
		
		removeBlacklistedItems(extractedSkills, filterBuilder);
		
		filterBuilder.skills(extractedSkills);
		
	}
	
	/**
	 * Synonyms
	 * 	- Tester
	 * 		- API [API TESTING]
	 * 		- QA	[QUALITY ASSURANCE]
	 * 
	 * 	- Htmll + html5
	 * 
	 * 
	 * 
	 * 
	 */
	
	//TODO: [KP] Software developer not being filtered out for C#
	
	private void removeBlacklistedItems(Set<String> extractedSkills, CandidateExtractedFiltersBuilder filterBuilder) {
		
		Set<String> blacklist 				= Set.of("e","r","c","applicatie","insight","team player","dynamics", "teamplayer", "elk","net","teams", "na","core java", "11","qa","test","testing","delivery","transformation", "it", "back", "informatica","capital","people","idea","auto","ideal", "development", "express", "front", "native","data");
		Set<String> blackListNonRecruiter 	= Set.of("recruiter","tech recruiter", "it recruiter", "recruitment consultant", "technical recruiter");
		Set<String> blackDevelopers 		= Set.of("software","project manager","budget","administration","software development", "research","hosting","requirements","test","sales","backlog");
		Set<String> blackTesters 			= Set.of("software","documentation");
		
		extractedSkills.removeAll(blacklist);
		
		final String jobTitle = filterBuilder.build().getJobTitle();
		
		/**
		* No Job title can't apply role specific blacklists
		*/
		if ("".equals(jobTitle)) {
			return;
		}
		
		Set<String> jobTypeBlacklist = new HashSet<>();
		
		switch(JobType.getByRole(filterBuilder.build().getJobTitle())) {
			case java: {
				jobTypeBlacklist.addAll(Set.of("hardware", "backend", "engineer (java","developer (java","java developer", "java software engineer", "java engineer", "java software ontwikkelaar", "java ontwikkelaar", "fullstack java", "java backend developer"));
				jobTypeBlacklist.addAll(blackDevelopers);
				jobTypeBlacklist.addAll(blackListNonRecruiter);
				break ;
			} 
			case csharp: {
				jobTypeBlacklist.addAll(Set.of());
				jobTypeBlacklist.addAll(blackDevelopers);
				jobTypeBlacklist.addAll(blackListNonRecruiter);
				break;
			} 
			case ba: {
				jobTypeBlacklist.addAll(Set.of("data", "analyst", "scientist", "business analyst", "ba"));
				jobTypeBlacklist.addAll(blackListNonRecruiter);
				break;
			}
			case qa: {
				jobTypeBlacklist.addAll(Set.of());
				jobTypeBlacklist.addAll(blackTesters);
				jobTypeBlacklist.addAll(blackListNonRecruiter);
				break;
			}
			case itSupport: {
				jobTypeBlacklist.addAll(Set.of("budget"));
				jobTypeBlacklist.addAll(blackListNonRecruiter);
				break;
			}
			case uiux: {
				jobTypeBlacklist.addAll(Set.of());
				jobTypeBlacklist.addAll(blackListNonRecruiter);
				break;
			}
			case projectManager: {
				jobTypeBlacklist.addAll(Set.of());
				jobTypeBlacklist.addAll(blackListNonRecruiter);
				break;
			}
			case architect: {
				jobTypeBlacklist.addAll(Set.of());
				jobTypeBlacklist.addAll(blackListNonRecruiter);
				break;
			}
			case webDeveloper: {
				jobTypeBlacklist.addAll(Set.of());
				jobTypeBlacklist.addAll(blackDevelopers);
				jobTypeBlacklist.addAll(blackListNonRecruiter);
				break;
			}
			case scrumMaster: {
				jobTypeBlacklist.addAll(Set.of());
				jobTypeBlacklist.addAll(blackListNonRecruiter);
				break;
			}
			case dataScientist: {
				jobTypeBlacklist.addAll(Set.of());
				jobTypeBlacklist.addAll(blackListNonRecruiter);
				break;
			}
			case networkAdmin: {
				jobTypeBlacklist.addAll(Set.of());
				jobTypeBlacklist.addAll(blackListNonRecruiter);
				break;
			}
			case softwareDeveloper: {
				jobTypeBlacklist.addAll(Set.of());
				jobTypeBlacklist.addAll(blackDevelopers);
				jobTypeBlacklist.addAll(blackListNonRecruiter);
				break;
			}
			case itSecurity: {
				jobTypeBlacklist.addAll(Set.of());
				jobTypeBlacklist.addAll(blackListNonRecruiter);
				break;
			}		
			case itRecruiter: {
				jobTypeBlacklist.addAll(Set.of());
				break;
			}
			case sdet: {
				jobTypeBlacklist.addAll(Set.of());
				jobTypeBlacklist.addAll(blackTesters);
				jobTypeBlacklist.addAll(blackListNonRecruiter);
				
				break;
			}
			default:{
				throw new RuntimeException("SkillExtractor not configred for " + JobType.Type.valueOf(filterBuilder.build().getJobTitle()));
			}
			
		}
		
		extractedSkills.removeAll(jobTypeBlacklist);
		
	}
}