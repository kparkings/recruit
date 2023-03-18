package com.arenella.recruit.recruiters.beans;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import com.arenella.recruit.recruiters.beans.RecruiterProfile.CONTRACT_TYPE;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.COUNTRY;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.TECH;

/**
* Filters for searching on RecruiterProfiles
* @author K Parkings
*/
public class RecruiterProfileFilter {

	private Optional<Boolean> 	visibleToRecruiters;
	private Optional<Boolean> 	visibleToCandidates;
	private Optional<Boolean> 	visibleToPublic			= null;
	private Set<COUNTRY> 		recruitsIn				= new LinkedHashSet<>();
	private Set<TECH>			coreTech				= new LinkedHashSet<>();
	private Set<CONTRACT_TYPE> 	recruitsContractTypes	= new LinkedHashSet<>();
	
	/**
	* Constructor based on Builder
	* @param builder - contains initialization values
	*/
	public RecruiterProfileFilter(RecruiterProfileFilterBuilder builder) {
		this.visibleToRecruiters 	= Optional.ofNullable(builder.visibleToRecruiters);
		this.visibleToCandidates 	= Optional.ofNullable(builder.visibleToCandidates);
		this.visibleToPublic 		= Optional.ofNullable(builder.visibleToPublic);
		this.recruitsIn 			= builder.recruitsIn;
		this.coreTech 				= builder.coreTech;
		this.recruitsContractTypes 	= builder.recruitsContractTypes;
	}
	
	/**
	* Whether to filter on profiles visible to the recruiters 
	* @return visible to the recruiters
	*/
	public Optional<Boolean> isVisibleToRecruiters(){
		return this.visibleToRecruiters;
	} 
	
	/**
	* Whether to filter on profiles visible to candidates
	* @return visible to candidates
	*/
	public Optional<Boolean> isVisibleToCandidates(){
		return this.visibleToCandidates;
	}		
	
	/**
	* Whether to filter on profiles visible to the public
	* @return visible to candidates
	*/
	public Optional<Boolean> isVisibleToPublic(){
		return this.visibleToPublic;
	}
	
	/**
	* Countries to filter on or empty if all countries
	* @return countries
	*/
	public Set<COUNTRY> getRecruitsIn(){
		return this.recruitsIn;
	}
	
	/**
	* Core technologies to filter on or empty if all technology types
	* @return technologies
	*/
	public Set<TECH> getCoreTech(){
		return this.coreTech;
	}
	
	/**
	* Contract types to filter on or empty if all contract types
	* @return contract types
	*/
	public Set<CONTRACT_TYPE> getRecruitsContractTypes(){
		return this.recruitsContractTypes;
	}
	
	/**
	* Returns a Builder for the class
	* @return Builder
	*/
	public static RecruiterProfileFilterBuilder builder(){
		return new RecruiterProfileFilterBuilder();
	}
	
	/**
	* Builder for the Class
	* @author K Parkings
	*/
	public static class RecruiterProfileFilterBuilder{
		
		private Boolean 			visibleToRecruiters		= null;
		private Boolean 			visibleToCandidates		= null;
		private Boolean 			visibleToPublic			= null;
		private Set<COUNTRY> 		recruitsIn				= new LinkedHashSet<>();
		private Set<TECH>			coreTech				= new LinkedHashSet<>();
		private Set<CONTRACT_TYPE> 	recruitsContractTypes	= new LinkedHashSet<>();
		
		/**
		* Sets whether to include profiles visible to the Recruiters
		* @param visibleToRecruiters - visible to Recruiter's
		* @return Builder
		*/
		public RecruiterProfileFilterBuilder visibleToRecruiters(Boolean visibleToRecruiters) {
			this.visibleToRecruiters = visibleToRecruiters;
			return this;
		}
		
		/**
		* Sets whether to include profiles visible to the candidates
		* @param visibleToCandidates - visible to Candidate's
		* @return Builder
		*/
		public RecruiterProfileFilterBuilder visibleToCandidates(Boolean visibleToCandidates) {
			this.visibleToCandidates = visibleToCandidates;
			return this;
		}
		
		/**
		* Sets whether to include profiles visible to the public
		* @param visibleToPublic - visible to public
		* @return Builder
		*/
		public RecruiterProfileFilterBuilder visibleToPublic(Boolean visibleToPublic) {
			this.visibleToPublic = visibleToPublic;
			return this;
		}
		
		/**
		* Sets the countries the recruiter recruits for
		* @param recruitsIn - which countries to filter on
		* @return Builder
		*/
		public RecruiterProfileFilterBuilder recruitsIn(Set<COUNTRY> recruitsIn) {
			this.recruitsIn.clear();
			this.recruitsIn.addAll(recruitsIn);
			return this;
		}
		
		/**
		* Sets the core technologies the recruiter recruits for
		* @param coreTech - technologies the recruiter recruits for
		* @return Builder
		*/
		public RecruiterProfileFilterBuilder coreTech(Set<TECH> coreTech) {
			this.coreTech.clear();
			this.coreTech.addAll(coreTech);
			return this;
		}
		
		/**
		* Sets the contract type the recruiter recruits for
		* @param recruitsContractTypes - types to filter on
		* @return
		*/
		public RecruiterProfileFilterBuilder recruitsContractTypes(Set<CONTRACT_TYPE> recruitsContractTypes) {
			this.recruitsContractTypes.clear();
			this.recruitsContractTypes.addAll(recruitsContractTypes);
			return this;
		}
		
		/**
		* Returns an intialized instance of RecruiterProfileFilter
		* @return initialzied instance
		*/
		public RecruiterProfileFilter build() {
			return new RecruiterProfileFilter(this);
		}
		
	}
	
}