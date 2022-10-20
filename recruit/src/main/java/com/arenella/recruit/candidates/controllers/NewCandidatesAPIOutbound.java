package com.arenella.recruit.candidates.controllers;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.enums.COUNTRY;

/**
* API Outbound view of new Candidates added to the System
* @author K Parkings
*/
public class NewCandidatesAPIOutbound {

	private Set<SummaryItem> candidates = new LinkedHashSet<>();
	
	/**
	* Constructor based upon Builder
	* @param builder - Contains initialization information
	*/
	public NewCandidatesAPIOutbound(NewCandidatesAPIOutboundBuilder builder) {
		this.candidates = builder.candidates;
	}
	
	/**
	* Returns a summary of the new Candidates
	* @return
	*/
	public Set<SummaryItem> getCandidateSummary() {
		return this.candidates.stream().sorted(Comparator.comparingInt(SummaryItem::getYearsExperience).reversed()).collect(Collectors.toCollection(LinkedHashSet::new));
	}
	
	/**
	* Returns a builder for the NewCandidatesAPIOutbound class
	* @return Builder
	*/
	public static NewCandidatesAPIOutboundBuilder builder() {
		return new NewCandidatesAPIOutboundBuilder();
	}
	
	/**
	* Builder for the NewCandidatesAPIOutbound class
	* @author K Parkings
	*/
	public static class NewCandidatesAPIOutboundBuilder{
		
		private Set<SummaryItem> candidates = new LinkedHashSet<>();
				
		/**
		* Adds summary information for a Candidate
		* @param candidate
		* @return
		*/
		public NewCandidatesAPIOutboundBuilder addCandidate(Candidate candidate) {
			candidates.add(new SummaryItem(candidate.getCountry(), candidate.getYearsExperience(), candidate.getRoleSought()));
			return this;
		}
		
		/**
		* Returns a new NewCandidatesAPIOutbound initialized with 
		* the detial in the builder
		* @return NewCandidatesAPIOutboundBuilder
		*/
		public NewCandidatesAPIOutbound build() {
			return new NewCandidatesAPIOutbound(this);
		}
		
	}
	
	/**
	* Summary Details of a New Candidate
	* @author K Parkings
	*/
	public static class SummaryItem{
		
		private final COUNTRY 	country;
		private final int 		yearsExperience;
		private final String 	functionDesc;
		
		/**
		* Constructor
		* @param country			- Country candidate is looking for work in
		* @param yearsExperience	- Number of years experience of the Candidate
		* @param functionDesc		- Description of the function Candidate performs
		*/
		public SummaryItem(COUNTRY country, int yearsExperience, String functionDesc) {
			this.country 			= country;
			this.yearsExperience 	= yearsExperience;
			this.functionDesc 		= functionDesc;
		}

		/**
		* Return the Country where the Candidate is looking for work
		* @return Country
		*/
		public COUNTRY getCountry() {
			return this.country;
		}
		
		/**
		* Return the candidates experience in years
		* @return Experience
		 */
		public int getYearsExperience() {
			return this.yearsExperience;
		}
		
		/**
		* Returns a description of the Function the Candidate performs
		* @return
		*/
		public String getFunctionDesc() {
			return this.functionDesc;
		}
		
	}
	
}