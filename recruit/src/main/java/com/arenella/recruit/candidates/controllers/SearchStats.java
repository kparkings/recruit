package com.arenella.recruit.candidates.controllers;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import com.arenella.recruit.candidates.beans.CandidateSearchEvent;
import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.enums.FUNCTION;

/**
* Contains statistics relating to search behavior
* over a given period 
*/
public class SearchStats {
	
	private Set<SearchStatFunction>	functionStats 	= new LinkedHashSet<>();
	private Set<SearchStatCountry> 	countryStats 	= new LinkedHashSet<>();
	private AtomicLong 				totalFunctions 	= new AtomicLong();
	private AtomicLong 				totalCountries 	= new AtomicLong();
	
	/**
	* Constructor
	* @param events - Events to process
	*/
	public SearchStats(Set<CandidateSearchEvent> events) {
		
		Map<FUNCTION, Long> functions 	= new HashMap<>();
		Map<COUNTRY, Long> countries 	= new HashMap<>();
		
		events.stream().forEach(event -> {
			event.getCountry().ifPresent(country -> {
				countries.keySet().stream().filter(c -> c == country).findAny().ifPresentOrElse(
						a->{
							countries.put(country, countries.get(a) + 1L);
						}, 
						()->{
							countries.put(country, 1L);
						});
				totalCountries.addAndGet(1);
			});
			
			event.getFunction().ifPresent(function -> {
				functions.keySet().stream().filter(f -> f == function).findAny().ifPresentOrElse(
						a->{
							functions.put(function, functions.get(a) + 1L);
						}, 
						()->{
							functions.put(function, 1L);
						});
				totalFunctions.addAndGet(1);
			});
			
		});
		
		functions.keySet().forEach(function -> this.functionStats.add(new SearchStatFunction(function, functions.get(function), getPercentAsInt(functions.get(function), totalFunctions.get()))));
		countries.keySet().forEach(country -> this.countryStats.add(new SearchStatCountry(country, countries.get(country), getPercentAsInt(countries.get(country), totalCountries.get()))));
		
	}
	
	/**
	* Calculates percentage as int
	* @param total - part to calculate percentage f
	* @param overalTotal - total overal
	* @return total as percentage of overalToal, as int
	*/
	private int getPercentAsInt(float total ,float overalTotal) {
		return Float.valueOf((total / overalTotal) * 100).intValue();
	}
	
	/**
	* Returns statistics relating to Searches on Functions
	* @return statistics relating to Searches on Functions
	*/
	public Set<SearchStatFunction> getFunctionStats(){
		return this.functionStats;
	}
	
	/**
	* Returns statistics relating to Searches on Countries
	* @return statistics relating to Searches on Countries
	*/
	public Set<SearchStatCountry> getCountryStats(){
		return this.countryStats;
	}
	
	/**
	* Container for Searched Function statistics
	* @param function - Function searched on
	* @param searches - Number of searches on Function
	* @param percentage - Percentage of overall Searches that were for the function
	*/
	public record SearchStatFunction(FUNCTION function, long searches, int percentageOfTotal) {}
	
	/**
	* Container for Searched Country statistics
	* @param country 	- Country searched on
	* @param searches 	- Number of searches on Country
	* @param percentage - Percentage of overall Searches that were for the Country
	*/
	public record SearchStatCountry(COUNTRY country, long searches, int percentageOfTotal) {};

}