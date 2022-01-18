package com.arenella.recruit.candidates.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.arenella.recruit.candidates.beans.CandidateFilterOptions;
import com.arenella.recruit.candidates.beans.CandidateSearchAccuracyWrapper;
import com.arenella.recruit.candidates.beans.Language;
import com.arenella.recruit.candidates.beans.Language.LANGUAGE;
import com.arenella.recruit.candidates.beans.Language.LEVEL;

/**
* Implementation of the CandidateSuggestion interface based upon 
* the percentages of matches between filter values and candidate
* @author K Parkings
*/
@Component
public class CandidateSuggestionUtilImpl implements CandidateSuggestionUtil{

	/**
	* Returns the percentage of matching skills between those asked and 
	* those the Candidate has
	* @param candidate 	- Candidate to check for accuracy
	* @param skills		 - target skills required
	* @return percentage of skills possessed by the Candidate
	*/
	private int getAccuracyOfSkillsMatch(CandidateSearchAccuracyWrapper candidate, Set<String> skills) {
		
		int	totalPossibleSkills		= skills.size();
		
		/**
		* If no required skills then is always a perfect match
		*/
		if (skills.isEmpty()) {
			return 100;
		}
		
		final Set<String> requiredSkills = skills.stream().map(skill -> skill.toLowerCase()).collect(Collectors.toSet());
		final Set<String> candidateSkills = candidate.get().getSkills().stream().map(skill -> skill.toLowerCase()).collect(Collectors.toSet());
		
		AtomicInteger matchingSkillCount = new AtomicInteger(0);
		
		requiredSkills.stream().forEach(skill -> {
			if (candidateSkills.contains(skill)) {
				matchingSkillCount.addAndGet(1);
			}
		});
		
		if (matchingSkillCount.get() == 0) {
			return 0;
		}
		
		Float perc = ((float)matchingSkillCount.get()  /  totalPossibleSkills) * 100; 
		
		return perc.intValue();
	
	}
	
	/**
	* Returns the percentage of matching languages between those asked and 
	* those the Candidate has
	* @param candidate 	- Candidate to check for accuracy
	* @param languages	- Languages required
	* @return
	*/
	private int getAccuracyOfLanguageMatch(CandidateSearchAccuracyWrapper candidate, Set<Language> languages) {
		
		int totalPossibleLanguagePoints = 0 ;
		
		int points = 0;
		
		boolean requiresDutch 	= languages.stream().filter(lang -> lang.getLanguage() == LANGUAGE.DUTCH).findAny().isPresent();
		boolean requiresEnglish = languages.stream().filter(lang -> lang.getLanguage() == LANGUAGE.ENGLISH).findAny().isPresent();
		boolean requiresFrench 	= languages.stream().filter(lang -> lang.getLanguage() == LANGUAGE.FRENCH).findAny().isPresent();
		
		/**
		* If no languages required then it is always a perfect math 
		*/
		if (languages.isEmpty()) {
			return 100;
		}
		
		if (requiresDutch) {
			
			Language.LEVEL requiredLevel 	=  languages.stream().filter(lang -> lang.getLanguage() == LANGUAGE.DUTCH).findAny().get().getLevel();
			
			if (requiredLevel == Language.LEVEL.PROFICIENT) {
				totalPossibleLanguagePoints = totalPossibleLanguagePoints + 2; 
			} 
			
			if (requiredLevel == Language.LEVEL.BASIC) {
				totalPossibleLanguagePoints = totalPossibleLanguagePoints + 1; 
			}
			
			if (!candidate.get().getLanguages().stream().filter(lang -> lang.getLanguage() == LANGUAGE.DUTCH && lang.getLevel() == Language.LEVEL.PROFICIENT).findAny().isEmpty()) {
				
				Language.LEVEL candidateLevel 	=  candidate.get().getLanguages().stream().filter(lang -> lang.getLanguage() == LANGUAGE.DUTCH).findAny().get().getLevel();
				
				if (requiredLevel == Language.LEVEL.PROFICIENT) {
					
					if (candidateLevel == LEVEL.PROFICIENT) {
						points = points + 2;
					}
					
					if (candidateLevel == LEVEL.BASIC) {
						points = points + 1;
					}
					
				}
				
				if (requiredLevel == Language.LEVEL.BASIC) {
					
					if (candidateLevel == LEVEL.PROFICIENT) {
						points = points + 2;
					}
					
					if (candidateLevel == LEVEL.BASIC) {
						points = points + 2;
					}
					
				}
				
			}
			
		}
		
		if (requiresEnglish) {
			
			Language.LEVEL requiredLevel 	=  languages.stream().filter(lang -> lang.getLanguage() == LANGUAGE.ENGLISH).findAny().get().getLevel();
			
			if (requiredLevel == Language.LEVEL.PROFICIENT) {
				totalPossibleLanguagePoints = totalPossibleLanguagePoints + 2; 
			} 
			
			if (requiredLevel == Language.LEVEL.BASIC) {
				totalPossibleLanguagePoints = totalPossibleLanguagePoints + 1; 
			}
			
			if (!candidate.get().getLanguages().stream().filter(lang -> lang.getLanguage() == LANGUAGE.ENGLISH && lang.getLevel() == Language.LEVEL.PROFICIENT).findAny().isEmpty()) {
				
				Language.LEVEL candidateLevel 	=  candidate.get().getLanguages().stream().filter(lang -> lang.getLanguage() == LANGUAGE.ENGLISH).findAny().get().getLevel();
				
				if (requiredLevel == Language.LEVEL.PROFICIENT) {
					
					if (candidateLevel == LEVEL.PROFICIENT) {
						points = points + 2;
					}
					
					if (candidateLevel == LEVEL.BASIC) {
						points = points + 1;
					}
					
				}
				
				if (requiredLevel == Language.LEVEL.BASIC) {
					
					if (candidateLevel == LEVEL.PROFICIENT) {
						points = points + 2;
					}
					
					if (candidateLevel == LEVEL.BASIC) {
						points = points + 2;
					}
					
				}
				
			}
			
		}

		if (requiresFrench) {
			
			Language.LEVEL requiredLevel 	=  languages.stream().filter(lang -> lang.getLanguage() == LANGUAGE.FRENCH).findAny().get().getLevel();
			
			if (requiredLevel == Language.LEVEL.PROFICIENT) {
				totalPossibleLanguagePoints = totalPossibleLanguagePoints + 2; 
			} 
			
			if (requiredLevel == Language.LEVEL.BASIC) {
				totalPossibleLanguagePoints = totalPossibleLanguagePoints + 1; 
			}
			
			if (!candidate.get().getLanguages().stream().filter(lang -> lang.getLanguage() == LANGUAGE.FRENCH && lang.getLevel() == Language.LEVEL.PROFICIENT).findAny().isEmpty()) {
				
				Language.LEVEL candidateLevel 	=  candidate.get().getLanguages().stream().filter(lang -> lang.getLanguage() == LANGUAGE.FRENCH).findAny().get().getLevel();
				
				if (requiredLevel == Language.LEVEL.PROFICIENT) {
					
					if (candidateLevel == LEVEL.PROFICIENT) {
						points = points + 2;
					}
					
					if (candidateLevel == LEVEL.BASIC) {
						points = points + 1;
					}
					
				}
				
				if (requiredLevel == Language.LEVEL.BASIC) {
					
					if (candidateLevel == LEVEL.PROFICIENT) {
						points = points + 2;
					}
					
					if (candidateLevel == LEVEL.BASIC) {
						points = points + 2;
					}
					
				}
				
			}
			
			
		}
		
		if (points == 0) {
			System.out.println("LANGUAGE PERC =====> XXXXX ");
			return 0;
		}
		System.out.println("LANGUAGE PERC =====> " + (points / totalPossibleLanguagePoints * 100));
		return points / totalPossibleLanguagePoints * 100;
		
	}
	
	/**
	* Extracts the Language required
	* @param filterOptions - requirments
	* @return languages required
	*/
	private Set<Language> extractLanguageRequirements(CandidateFilterOptions filterOptions) {
		
		Set<Language> languageRequirements = new HashSet<>();
		
		if (filterOptions.getDutch().isPresent()) {
			languageRequirements.add(Language.builder().language(LANGUAGE.DUTCH).level(filterOptions.getDutch().get()).build());
		}
		
		if (filterOptions.getEnglish().isPresent()) {
			languageRequirements.add(Language.builder().language(LANGUAGE.ENGLISH).level(filterOptions.getEnglish().get()).build());
		}
		
		if (filterOptions.getFrench().isPresent()) {
			languageRequirements.add(Language.builder().language(LANGUAGE.FRENCH).level(filterOptions.getFrench().get()).build());
		}
		
		return languageRequirements;
		
	}

	/**
	* Refer to the CandidateSuggestionUtil interface for details 
	*/
	@Override
	public boolean isPerfectMatch(CandidateSearchAccuracyWrapper candidate, CandidateFilterOptions filterOptions) {
		
		int skillsAccuracy 		= this.getAccuracyOfSkillsMatch(candidate, filterOptions.getSkills());
		int languageAccuracy 	= this.getAccuracyOfLanguageMatch(candidate, extractLanguageRequirements(filterOptions)) ;
		
		if (skillsAccuracy == 100 && languageAccuracy == 100) {
			candidate.setAccuracySkills(convertPercentAccuracy(skillsAccuracy));
			candidate.setAccuracyLanguages(convertPercentAccuracy(languageAccuracy));
			return true;
		} 
		
		return false;
		
		//return this.getAccuracyOfSkillsMatch(candidate, filterOptions.getSkills()) == 100 && this.getAccuracyOfLanguageMatch(candidate, extractLanguageRequirements(filterOptions)) == 100;
	}

	/**
	* Refer to the CandidateSuggestionUtil interface for details 
	*/
	@Override
	public boolean isExcellentMatch(CandidateSearchAccuracyWrapper candidate, CandidateFilterOptions filterOptions) {
		
		int skillsAccuracy 		= this.getAccuracyOfSkillsMatch(candidate, filterOptions.getSkills());
		int languageAccuracy 	= this.getAccuracyOfLanguageMatch(candidate, extractLanguageRequirements(filterOptions)) ;
		
		if (skillsAccuracy >= 85 && languageAccuracy >= 85) {
			candidate.setAccuracySkills(convertPercentAccuracy(skillsAccuracy));
			candidate.setAccuracyLanguages(convertPercentAccuracy(languageAccuracy));
			return true;
		} 
		
		return false;
	}

	/**
	* Refer to the CandidateSuggestionUtil interface for details 
	*/
	@Override
	public boolean isGoodMatch(CandidateSearchAccuracyWrapper candidate, CandidateFilterOptions filterOptions) {

		int skillsAccuracy 		= this.getAccuracyOfSkillsMatch(candidate, filterOptions.getSkills());
		int languageAccuracy 	= this.getAccuracyOfLanguageMatch(candidate, extractLanguageRequirements(filterOptions)) ;
		
		if (skillsAccuracy >= 70 && languageAccuracy >= 70) {
			candidate.setAccuracySkills(convertPercentAccuracy(skillsAccuracy));
			candidate.setAccuracyLanguages(convertPercentAccuracy(languageAccuracy));
			return true;
		} 
		
		return false;
	}

	/**
	* Refer to the CandidateSuggestionUtil interface for details 
	*/
	@Override
	public boolean isAverageMatch(CandidateSearchAccuracyWrapper candidate, CandidateFilterOptions filterOptions) {

		int skillsAccuracy 		= this.getAccuracyOfSkillsMatch(candidate, filterOptions.getSkills());
		int languageAccuracy 	= this.getAccuracyOfLanguageMatch(candidate, extractLanguageRequirements(filterOptions)) ;
		
		if (skillsAccuracy >= 50 && languageAccuracy >= 50) {
			candidate.setAccuracySkills(convertPercentAccuracy(skillsAccuracy));
			candidate.setAccuracyLanguages(convertPercentAccuracy(languageAccuracy));
			return true;
		} 
		
		return false;
	}
	
	/**
	* Refer to the CandidateSuggestionUtil interface for details 
	*/
	@Override
	public boolean isPoorMatch(CandidateSearchAccuracyWrapper candidate, CandidateFilterOptions filterOptions) {

		int skillsAccuracy 		= this.getAccuracyOfSkillsMatch(candidate, filterOptions.getSkills());
		int languageAccuracy 	= this.getAccuracyOfLanguageMatch(candidate, extractLanguageRequirements(filterOptions)) ;
		
		if (skillsAccuracy >= 25 && languageAccuracy >= 25) {
			candidate.setAccuracySkills(convertPercentAccuracy(skillsAccuracy));
			candidate.setAccuracyLanguages(convertPercentAccuracy(languageAccuracy));
			return true;
		} 
		
		return false;
	}
	
	/**
	* Converts a score for a 
	* @param score
	* @return
	*/
	private suggestion_accuracy convertPercentAccuracy(int score) {
		
		if (score == 100) {
			return suggestion_accuracy.perfect;
		}
		
		if (score >= 85) {
			return suggestion_accuracy.excellent;
		}
		
		if (score >= 70) {
			return suggestion_accuracy.good;
		}
		
		if (score >= 50) {
			return suggestion_accuracy.average;
		}
		
		if (score >= 25) {
			return suggestion_accuracy.poor;
		}
		
		return null;
		
	}

}