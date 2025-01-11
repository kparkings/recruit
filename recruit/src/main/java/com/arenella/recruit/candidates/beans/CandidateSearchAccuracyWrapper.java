package com.arenella.recruit.candidates.beans;

import java.util.Optional;

import com.arenella.recruit.candidates.utils.CandidateSuggestionUtil.suggestion_accuracy;

/**
* Wraps a Candidate to provide information about how accurate the 
* Candidate was in relation to filters used in a Search
* @author K Parkings
*/
public class CandidateSearchAccuracyWrapper {

	private final Candidate candidate;
	
	private suggestion_accuracy accuracySkills;
	private suggestion_accuracy accuracyLanguages;
	
	/**
	* Constructor
	* @param candidate - Candidate to be wrapped
	*/
	public CandidateSearchAccuracyWrapper(Candidate candidate) {
		this.candidate = candidate;
	}
	
	/**
	* Returns the wrapped Candidate
	* @return
	*/
	public Candidate get() {
		return this.candidate;
	}
	
	/**
	* Sets how accurate the Candidates skills are in relation to a filtered Search
	* @param accuracySkills
	*/
	public void setAccuracySkills(suggestion_accuracy accuracySkills) {
		this.accuracySkills = accuracySkills;
	}
	
	/**
	* Sets how accurate the Candidates languages are in relation to a filtered Search
	* @param accuracyLanguages
	*/
	public void setAccuracyLanguages(suggestion_accuracy accuracyLanguages) {
		this.accuracyLanguages = accuracyLanguages;
	}
	
	/**
	* Returns how accurate the Candidates skills are in relation to a filtered Search
	* @return skills accuracy
	*/
	public suggestion_accuracy getAccuracySkills() {
		return this.accuracySkills;
	}
	
	/**
	* Returns a numeric representation of the skills accuracy which 
	* can be useful for sorting
	* @return accuracy as number
	*/
	public Integer getAccuracySkillsAsNumber() {
		
		if (Optional.ofNullable(this.accuracySkills).isEmpty()) {
			return 100;
		}
		
		return this.accuracySkills.asNumber();
	}

	/**
	* Returns how accurate the Candidates languages are in relation to a filtered Search
	* @return language accuracy
	*/
	public suggestion_accuracy getAccuracyLanguages() {
		return this.accuracyLanguages;
	}
	
	/**
	* Returns numeric representation of the language accuracy which can be useful
	* for sorting
	* @return accuracy as number
	*/
	public Integer getAccuracyLanguagesAsNumber() {
		return this.accuracyLanguages.asNumber;
	}

}