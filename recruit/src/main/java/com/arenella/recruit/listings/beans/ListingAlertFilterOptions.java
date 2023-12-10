package com.arenella.recruit.listings.beans;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
* Filters specification for filtering on Listing Alerts.
* @author K Parkings
*/
public class ListingAlertFilterOptions {

	private String			 				searchTerm;
	private Listing.listing_type		 	contractType;
	private Set<Listing.country> 			countries 		= new HashSet<>();
	private Set<Listing.TECH> 				categories	 	= new HashSet<>();
	
	/**
	* Constructor based upon a Builder
	* @param builder - initialization information
	*/
	public ListingAlertFilterOptions(ListingAlertFilterOptionsBuilder builder) {
		this.searchTerm 	= builder.searchTerm;
		this.contractType	= builder.contractType;
		this.countries 		= builder.countries;
		this.categories	 	= builder.categories;
	}
	
	/**
	* Returns any Search term to filter on
	* @return search term to filter on
	*/
	public Optional<String> 				getSearchTerm(){
		return Optional.ofNullable(this.searchTerm);
	}
	
	/**
	* Returns the Contract type to filter n
	* @return Contract type to filter n
	*/
	public Optional<Listing.listing_type> 	getContractType(){
		return Optional.ofNullable(this.contractType);
	}
	
	/**
	* Returns any countries to filter on
	* @return Countries to filter on
	*/
	public Set<Listing.country> 			getCountries(){
		return this.countries;
	}
	
	/**
	* Returns any categories to filter on
	* @return categories to filter on
	*/
	public Set<Listing.TECH> 				getCategories(){
		return this.categories;
	}
	
	/**
	* Returns and instance of a Builder for the class
	* @return Builder
	*/
	public static ListingAlertFilterOptionsBuilder builder() {
		return new ListingAlertFilterOptionsBuilder();
	}
	
	/**
	* 
	* @author K Parkings
	*/
	public static class ListingAlertFilterOptionsBuilder{
	
		private String 					searchTerm;
		private Listing.listing_type 	contractType;
		private Set<Listing.country> 	countries 		= new HashSet<>();
		private Set<Listing.TECH> 		categories	 	= new HashSet<>();
		
		/**
		* Sets a search term to filter on
		* @param searchTerm - search term that must be in the listing title
		* @return Builder
		*/
		public ListingAlertFilterOptionsBuilder searchTerm(String searchTerm) {
			this.searchTerm = searchTerm;
			return this;
		}
		
		/**
		* Sets the Contract Type to filter on
		* @param contractType - Contract type to filter on
		* @return Builder
		*/
		public ListingAlertFilterOptionsBuilder contractType(Listing.listing_type contractType) {
			
			if (contractType == Listing.listing_type.BOTH) {
				this.contractType = null;
			} else {
				this.contractType = contractType;
			}
			
			return this;
		}
		
		/**
		* Sets countries to filter on
		* @param countries - Countries to filter n
		* @return Builder
		*/
		public ListingAlertFilterOptionsBuilder countries(Set<Listing.country> countries) {
			this.countries.clear();
			this.countries.addAll(countries);
			return this;
		}
		
		/**
		* Sets the Categories to filter on
		* @param categories - Categories to filter on
		* @return Builder
		*/
		public ListingAlertFilterOptionsBuilder categories(Set<Listing.TECH> categories) {
			this.categories.clear();
			this.categories.addAll(categories);
			return this;
		}	
		
		/**
		* Returns an initialized instance
		* @return initialized instance
		*/
		public ListingAlertFilterOptions build() {
			return new ListingAlertFilterOptions(this);
		}
	}
}
