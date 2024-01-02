package com.arenella.recruit.listings.utils;

import java.util.Set;

import com.arenella.recruit.listings.beans.Listing;

/**
* Class represents a type of job and keeps track of synonyms for that job type
* @author K Parkings
*/
public class ListingCategory{
	
	private Listing.TECH 	type;
	private Set<String> 	keywords;
	
	/**
	* Constructor
	* @param type		- Type of job
	* @param titles		- titles / synonyms for the job type
	*/
	public ListingCategory(Listing.TECH type, Set<String> keywords) {
		this.type 		= type;
		this.keywords 	= keywords;
	}
	
	/**
	* Returns the type of job
	* @return Job type
	*/
	public Listing.TECH getCategory() {
		return type;
	}
	
	/**
	* Titles / synonyms for the job type
	* @return titles
	*/
	public Set<String> getKeywords(){
		return keywords;
	}
	
}
