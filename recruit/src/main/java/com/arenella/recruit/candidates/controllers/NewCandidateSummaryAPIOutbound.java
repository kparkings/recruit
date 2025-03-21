package com.arenella.recruit.candidates.controllers;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.enums.FUNCTION;

/**
* Wrapper for Summary information relating to new
* Candidates added to the system
* @author K Parkings
*/
public class NewCandidateSummaryAPIOutbound {

	private Set<SummaryItem> summary = new LinkedHashSet<>();
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization information
	*/
	public NewCandidateSummaryAPIOutbound(NewCandidateSummaryAPIOutboundBuilder builder) {
		this.summary = builder.summary;
	}
	
	/**
	* Returns summary information about the new Candidates
	* @return Candidate summary information
	*/
	public Set<SummaryItem> getCandidatesSummary(){
		return this.summary.stream().sorted(Comparator.comparingInt(SummaryItem::getCount).reversed()).collect(Collectors.toCollection(LinkedHashSet::new));
	}
	
	/**
	* Returns a Builder for the NewCandidateSummaryAPIOutbound class
	* @return Builder
	*/
	public static NewCandidateSummaryAPIOutboundBuilder builder() {
		return new NewCandidateSummaryAPIOutboundBuilder();
	}
	
	/**
	* Builder for the NewCandidateSummaryAPIOutbound class
	* @author K Parkings
	*/
	public static class NewCandidateSummaryAPIOutboundBuilder{
		
		private Set<SummaryItem> summary = new LinkedHashSet<>();
		
		/**
		* Adds a Candidate to the Summary
		* @param candidate - Candidate information
		* @return Builder
		*/
		public NewCandidateSummaryAPIOutboundBuilder addCandidate(Candidate candidate) {
			
			candidate.getFunctions().stream().forEach(func -> {
				
				SummaryItem summaryItem = summary.stream().filter(f -> f.getFunctionType() == func).findFirst().orElse(new SummaryItem(func));
				
				summaryItem.increment();
				
				this.summary.add(summaryItem);
				
			});
			
			
			return this;
		}
		
		/**
		* Returns a new NewCandidateSummaryAPIOutbound initialized with 
		* the values in the Builder 
		* @return NewCandidateSummaryAPIOutbound
		*/
		public NewCandidateSummaryAPIOutbound build() {
			return new NewCandidateSummaryAPIOutbound(this);
		}
		
	}
	
	/**
	* Contains summary information relating to new Candidates
	* @author K Parkings
	*/
	public static class SummaryItem {
		
		private final FUNCTION 	functionType;
		private int 			count;
		
		/**
		* Constructor
		* @param function - Type of function a Candidate performs
		*/
		public SummaryItem(FUNCTION function) {
			this.functionType = function;
		}
		
		/**
		* Increments the count of the number of candidates performing
		* the function type
		*/
		public void increment() {
			this.count = count + 1;
		}
		
		/**
		* Returns the type of Function the Candidate performs
		* @return Function type
		*/
		public FUNCTION getFunctionType() {
			return this.functionType;
		}
		
		/**
		* Returns the number of Candidates with a specific
		* Function type
		* @return number of Candidates
		*/
		public int getCount() {
			return this.count;
		}
		
	}
}