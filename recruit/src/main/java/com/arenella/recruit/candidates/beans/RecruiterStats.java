package com.arenella.recruit.candidates.beans;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.enums.FUNCTION;

/**
* Stats relating to what Recruiter has been searching on
* @author K Parkings
*/
public class RecruiterStats {

	private final Set<Search> searches = new LinkedHashSet<>();
	
	/**
	* Constructor
	* @param events - Events to be converted into Stat's
	*/
	public RecruiterStats(Set<CandidateSearchEvent> events) {
		
		events.stream().forEach(evt -> {
			
			COUNTRY 	country 	= evt.getCountry().orElse(null);
			FUNCTION 	function 	= evt.getFunction().orElse(null); 
			
			if (!searches.stream().anyMatch(s -> s.function == function && s.country == country)) {
				this.searches.add(new Search(country, function));
			}
			
		});
		
	}
	
	/**
	* Returns the unique types of Search made by the 
	* Recruiter
	* @return - Type of search
	*/
	public Set<Search> getUniqueSearches(){
		return this.searches.stream().sorted((i1, i2) -> {
			
			if (i2.getCountry() == null) {
				return 0;
			}
			
			if (i1.getCountry() == null) {
				return -1;
			}
			
			return i2.getCountry().compareTo(i1.getCountry());
			
		}).collect(Collectors.toCollection(LinkedHashSet::new));
	}
	
	/**
	* Represents an Search by the Recruiter
	* @author K Parkings
	*/
	public static class Search{
		
		private COUNTRY 	country;
		private FUNCTION 	function;
		
		/**
		* Constructor
		* @param country	- Country searched on
		* @param function	- Function searched on
		*/
		public Search(COUNTRY country, FUNCTION function) {
			this.country 	= country;
			this.function 	= function;
		}
		
		/**
		* Returns the Country searched on
		* @return Country
		*/
		public COUNTRY getCountry() {
			return this.country;
		}
		
		/**
		* Returns the Function searched on
		* @return Function searched on
		*/
		public FUNCTION getFunction() {
			return this.function;
		}
		
	}
	
}