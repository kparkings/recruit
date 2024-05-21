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

	public static final int POINTS_FOR_PROFICIENT 	= 2;
	public static final int POINTS_FOR_BASIC 		= 1;
	
	public static final int THRESHOLD_PERFECT 		= 100;
	public static final int THRESHOLD_EXCELLENT 	= 75;
	public static final int THRESHOLD_GOOD 			= 50;
	public static final int THRESHOLD_AVERAGE 		= 40;
	public static final int THRESHOLD_POOR 			= 25;
	
	/**
	* Returns the percentage of keywords from the search term found in the title
	* @param candidate 		- Candidate to check for accuracy
	* @param searchTermKeywords - Keywords extracted from search term
	* @return percentage of keywords in the candidate Title
	*/
	private int getAccuracyOfTitle(CandidateSearchAccuracyWrapper candidate, Set<String> searchTermKeywords) {
		
		if (searchTermKeywords.isEmpty()) {
			return 0;
		}
		
		String title = candidate.get().getRoleSought().toLowerCase();
		
		Set<String> matchingKeywords = searchTermKeywords.stream().map(String::toLowerCase).filter(keyword -> title.contains(keyword)).collect(Collectors.toSet());
		
		if (matchingKeywords.isEmpty()) {
			return 0;
		}
		
		Float perc = ((float)matchingKeywords.size()  /  searchTermKeywords.size()) * 100;
		
		return perc.intValue();
	}
	
	/**
	* Returns the percentage of matching skills between those asked and 
	* those the Candidate has
	* @param candidate 	- Candidate to check for accuracy
	* @param skills		 - target skills required
	* @return percentage of skills possessed by the Candidate
	*/
	private int getAccuracyOfSkillsMatch(CandidateSearchAccuracyWrapper candidate, Set<String> skills, boolean searchTermPresent) {
		
		int	totalPossibleSkills		= skills.size();
		
		/**
		* If no required skills then is always a perfect match
		*/
		if (skills.isEmpty() && !searchTermPresent) {
			return 100;
		}
		
		/**
		* In this case we want to leave the filtering to just the searchTerms
		*/
		if (skills.isEmpty() && searchTermPresent) {
			return 0;
		}
		
		final Set<String> requiredSkills = skills.stream().map(String::toLowerCase).collect(Collectors.toSet());
		final Set<String> candidateSkills = candidate.get().getSkills().stream().map(String::toLowerCase).collect(Collectors.toSet());
		
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
		
		int 			totalPossibleLanguagePoints = getMaxAvailableLanguagePoints(languages);
		AtomicInteger 	points 						= new AtomicInteger(0);
		
		/**
		* If no languages required then it is always a perfect math 
		*/
		if (languages.isEmpty()) {
			return 100;
		}
		
		languages.stream().forEach(language -> {
			points.addAndGet(getCandidateLanguagePoints(language.getLanguage(), language.getLevel(), candidate));
		});
		
		if (points.get() == 0) {
			return 0;
		}
		
		Float perc = ((float)points.get()  /  totalPossibleLanguagePoints) * 100; 
		
		return perc.intValue();
		
	}
	
	/**
	* Examines the languages required by from the Candidate and determines 
	* a score that each Candidate can be measured against
	* @param languages - Required languages
	* @return Max obtainable score that a Candidate can reach in relation to languages
	*/
	private int getMaxAvailableLanguagePoints(Set<Language> languages) {

		AtomicInteger totalPossibleLanguagePoints = new AtomicInteger(0);
		
		languages.stream().forEach(language -> {
			switch(language.getLevel()) {
				case BASIC:{
					totalPossibleLanguagePoints.addAndGet(POINTS_FOR_BASIC);
				}
				case PROFICIENT:{
					totalPossibleLanguagePoints.addAndGet(POINTS_FOR_PROFICIENT);
				}
				default:{
					
				}
			}
		});
			
		return totalPossibleLanguagePoints.get();
		
	}
	
	/**
	* Returns the number of points the Candidate scored for their Languages in relation to 
	* those required 
	* @param language			- The language the Candidate will be checked for
	* @param levelRequired		- The level the Candidate will be checked for
	* @param candidate			- Contains information about the Candidates language knowledge
	* @return Number of points the Candidate scores in relation to requirements 
	*/
	private int getCandidateLanguagePoints(LANGUAGE language, Language.LEVEL levelRequired, CandidateSearchAccuracyWrapper candidate) {
		
		AtomicInteger points = new AtomicInteger(0);
		
		if (!candidate.get().getLanguages().stream().filter(lang -> lang.getLanguage() == language && lang.getLevel() == levelRequired).findAny().isEmpty()) {
			
			Language.LEVEL candidateLevel 	=  candidate.get().getLanguages().stream().filter(lang -> lang.getLanguage() == language).findAny().get().getLevel();
			
			if (levelRequired == Language.LEVEL.PROFICIENT) {
				
				if (candidateLevel == LEVEL.PROFICIENT) {
					points.addAndGet(POINTS_FOR_PROFICIENT);
				}
				
				if (candidateLevel == LEVEL.BASIC) {
					points.addAndGet(POINTS_FOR_BASIC);
				}
				
			}
			
			if (levelRequired == Language.LEVEL.BASIC) {
				
				if (candidateLevel == LEVEL.PROFICIENT) {
					points.addAndGet(POINTS_FOR_PROFICIENT);
				}
				
				if (candidateLevel == LEVEL.BASIC) {
					points.addAndGet(POINTS_FOR_PROFICIENT);
				}
				
			}
			
		}
		
		return points.get();
		
	}
	
	/**
	* Extracts the Language required
	* @param filterOptions - requirements
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
	public boolean isPerfectMatch(CandidateSearchAccuracyWrapper candidate, CandidateFilterOptions filterOptions, Set<String> searchTermKeywords) {
		
		try {
			
			int titleAccuracy		= this.getAccuracyOfTitle(candidate, searchTermKeywords);
			int skillsAccuracy 		= this.getAccuracyOfSkillsMatch(candidate, filterOptions.getSkills(), !searchTermKeywords.isEmpty());
			int languageAccuracy 	= this.getAccuracyOfLanguageMatch(candidate, extractLanguageRequirements(filterOptions)) ;
			
			if ((skillsAccuracy == THRESHOLD_PERFECT || titleAccuracy == THRESHOLD_PERFECT) && languageAccuracy == 100) {
				candidate.setAccuracySkills(convertPercentAccuracy(skillsAccuracy));
				candidate.setAccuracyLanguages(convertPercentAccuracy(languageAccuracy));
				return true;
			} 
			
		}catch(Exception e) {
			return false;
		}
	
		return false;
		
	}

	/**
	* Refer to the CandidateSuggestionUtil interface for details 
	*/
	@Override
	public boolean isExcellentMatch(CandidateSearchAccuracyWrapper candidate, CandidateFilterOptions filterOptions, Set<String> searchTermKeywords) {
		
		try {
			int titleAccuracy		= this.getAccuracyOfTitle(candidate, searchTermKeywords);
			int skillsAccuracy 		= this.getAccuracyOfSkillsMatch(candidate, filterOptions.getSkills(), !searchTermKeywords.isEmpty());
			int languageAccuracy 	= this.getAccuracyOfLanguageMatch(candidate, extractLanguageRequirements(filterOptions)) ;
			
			if ((skillsAccuracy >= THRESHOLD_EXCELLENT || titleAccuracy >= 60) && languageAccuracy >= 60) {
				candidate.setAccuracySkills(convertPercentAccuracy(skillsAccuracy));
				candidate.setAccuracyLanguages(convertPercentAccuracy(languageAccuracy));
				return true;
			} 
		}catch(Exception e) {
			return false;
		}
		
		return false;
	}

	/**
	* Refer to the CandidateSuggestionUtil interface for details 
	*/
	@Override
	public boolean isGoodMatch(CandidateSearchAccuracyWrapper candidate, CandidateFilterOptions filterOptions, Set<String> searchTermKeywords) {

		try {
			int titleAccuracy		= this.getAccuracyOfTitle(candidate, searchTermKeywords);
			int skillsAccuracy 		= this.getAccuracyOfSkillsMatch(candidate, filterOptions.getSkills(), !searchTermKeywords.isEmpty());
			int languageAccuracy 	= this.getAccuracyOfLanguageMatch(candidate, extractLanguageRequirements(filterOptions)) ;
			
			if ((skillsAccuracy >= THRESHOLD_GOOD || titleAccuracy >= 50) && languageAccuracy >= 50) {
				candidate.setAccuracySkills(convertPercentAccuracy(skillsAccuracy));
				candidate.setAccuracyLanguages(convertPercentAccuracy(languageAccuracy));
				return true;
			} 
		}catch(Exception e) {
			return false;
		}
		
		return false;
	}

	/**
	* Refer to the CandidateSuggestionUtil interface for details 
	*/
	@Override
	public boolean isAverageMatch(CandidateSearchAccuracyWrapper candidate, CandidateFilterOptions filterOptions, Set<String> searchTermKeywords) {

		try {
			int titleAccuracy		= this.getAccuracyOfTitle(candidate, searchTermKeywords);
			int skillsAccuracy 		= this.getAccuracyOfSkillsMatch(candidate, filterOptions.getSkills(), !searchTermKeywords.isEmpty());
			int languageAccuracy 	= this.getAccuracyOfLanguageMatch(candidate, extractLanguageRequirements(filterOptions)) ;
			
			if ((skillsAccuracy >= THRESHOLD_AVERAGE || titleAccuracy >= 40) && languageAccuracy >= 40) {
				candidate.setAccuracySkills(convertPercentAccuracy(skillsAccuracy));
				candidate.setAccuracyLanguages(convertPercentAccuracy(languageAccuracy));
				return true;
			} 
		}catch(Exception e) {
			return false;
		}
		
		return false;
	}
	
	/**
	* Refer to the CandidateSuggestionUtil interface for details 
	*/
	@Override
	public boolean isPoorMatch(CandidateSearchAccuracyWrapper candidate, CandidateFilterOptions filterOptions, Set<String> searchTermKeywords) {

		try {
			int titleAccuracy		= this.getAccuracyOfTitle(candidate, searchTermKeywords);
			int skillsAccuracy 		= this.getAccuracyOfSkillsMatch(candidate, filterOptions.getSkills(), !searchTermKeywords.isEmpty());
			int languageAccuracy 	= this.getAccuracyOfLanguageMatch(candidate, extractLanguageRequirements(filterOptions)) ;
			
			if ((skillsAccuracy >= THRESHOLD_POOR || titleAccuracy >= 30) && languageAccuracy >= 30) {
				candidate.setAccuracySkills(convertPercentAccuracy(skillsAccuracy));
				candidate.setAccuracyLanguages(convertPercentAccuracy(languageAccuracy));
				return true;
			} 
		}catch(Exception e) {
			//TODO: [KP] All these methods throw excetion. Need to work out why. Something to do with skills in a certain situation. This ugly 
			// fix resolves issue in prod but now need to investigate where the nullpoiinter is coming from
			return false;
		}
		
		return false;
	}
	
	/**
	* Converts a score for a 
	* @param score
	* @return
	*/
	private suggestion_accuracy convertPercentAccuracy(int score) {
		
		if (score == THRESHOLD_PERFECT) {
			return suggestion_accuracy.perfect;
		}
		
		if (score >= THRESHOLD_EXCELLENT) {
			return suggestion_accuracy.excellent;
		}
		
		if (score >= THRESHOLD_GOOD) {
			return suggestion_accuracy.good;
		}
		
		if (score >= THRESHOLD_AVERAGE) {
			return suggestion_accuracy.average;
		}
		
		if (score >= THRESHOLD_POOR) {
			return suggestion_accuracy.poor;
		}
		
		return null;
		
	}

}