package com.arenella.recruit.listings.controllers;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import com.arenella.recruit.listings.utils.ListingGeoZoneSearchUtil.GEO_ZONE;
import com.arenella.recruit.listings.beans.Listing.Country;
import com.arenella.recruit.listings.beans.Listing.LISTING_AGE;
import com.arenella.recruit.listings.beans.Listing.listing_type;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
* Details the filers to apply to search results of Listing's 
*/
@JsonDeserialize(builder=ListingSearchRequestAPIInbound.ListingSearchRequestAPIInboundBuilder.class)
public class ListingSearchRequestAPIInbound {

	private String 				searchTerm;
	private listing_type 		contractType;
	private Set<GEO_ZONE> 		geoZones 		= new HashSet<>();
	private Set<Country> 		countries		= new HashSet<>();
	private UUID				listingId;
	private LISTING_AGE			maxAgeOfPost;
	
	/**
	* Constructor based upon a builder 
	* @param builder - Contains initialization values
	*/
	public ListingSearchRequestAPIInbound(ListingSearchRequestAPIInboundBuilder builder) {

		this.searchTerm 	= builder.searchTerm;
		this.contractType 	= builder.contractType;
		this.geoZones	 	= builder.geoZones;
		this.countries 		= builder.countries;
		this.maxAgeOfPost 	= builder.maxAgeOfPost;
		this.listingId		= builder.listingId;
		
	}

	/**
	* Returns the Listing Id to filter on
	* @return
	*/
	public Optional<UUID> getListingId() {
		return Optional.ofNullable(this.listingId);
	}	
	
	/**
	* Returns the search term to search on
	* @return Search term
	*/
	public Optional<String> getSearchTerm(){
		return Optional.ofNullable(this.searchTerm);
	}
	
	/**
	* Returns the contract type to search on
	* @return contract type
	*/
	public Optional<listing_type> getContractType(){
		return Optional.ofNullable(this.contractType);
	}
	
	/**
	* Returns the country to search on
	* @return Country to search on
	*/
	public Set<Country> getCountries(){
		return this.countries;
	}
	
	/**
	* Returns the geoZones to search on
	* @return GeoZones to search on
	*/
	public Set<GEO_ZONE> getGeoZones(){
		return this.geoZones;
	}
	
	/**
	* Returns the max age of the post to search on
	* @return max age
	*/
	public Optional<LISTING_AGE> getMaxAgeOfPost(){
		return Optional.ofNullable(this.maxAgeOfPost);
	}
	
	/**
	* Returns a builder for the class
	* @return Builder
	*/
	public static ListingSearchRequestAPIInboundBuilder builder() {
		return new ListingSearchRequestAPIInboundBuilder();
	}

	/**
	* Builder for the class 
	*/
	@JsonPOJOBuilder(buildMethodName="build", withPrefix="")
	public static class ListingSearchRequestAPIInboundBuilder {

		private String 				searchTerm;
		private listing_type 		contractType;
		private Set<GEO_ZONE> 		geoZones 		= new HashSet<>();
		private Set<Country> 		countries		= new HashSet<>();
		private LISTING_AGE			maxAgeOfPost;
		private UUID				listingId;

		/**
		* Sets the listingId to filter on
		* @param listingId - Id of listing to filter on
		* @return BUilder
		*/
		public ListingSearchRequestAPIInboundBuilder listingId(UUID listingId) {
			this.listingId = listingId;
			return this;
		}
		
		/**
		* Sets, if provided the search term to filter on
		* @param searchTerm - term to filter on
		* @return
		*/
		public ListingSearchRequestAPIInboundBuilder searchTerm(String searchTerm) {
			this.searchTerm = searchTerm;
			return this;
		}
		
		/**
		* Sets, if provided the type of contract to filter on
		* @param contractType - type of contract
		* @return Builder
		*/
		public ListingSearchRequestAPIInboundBuilder contractType(listing_type contractType) {
			this.contractType = contractType;
			return this;
		}
		
		/**
		* Sets, if provided the GeoZones to filter on
		* @param geoZones - geoZones to filter on
		* @return Builder
		*/
		public ListingSearchRequestAPIInboundBuilder geoZones(Set<GEO_ZONE> countries) {
			this.geoZones.clear();
			this.geoZones.addAll(countries);
			return this;
		}
		
		/**
		* Sets, if provided the countries to filter on
		* @param country - country to filter on
		* @return Builder
		*/
		public ListingSearchRequestAPIInboundBuilder countries(Set<Country> countries) {
			this.countries.clear();
			this.countries.addAll(countries);
			return this;
		}
		
		/**
		* Sets, if provided the max age of the posts to filter on
		* @param ageOfPost = max age of post
		* @return Builder
		*/
		public ListingSearchRequestAPIInboundBuilder maxAgeOfPost(LISTING_AGE maxAgeOfPost) {
			this.maxAgeOfPost = maxAgeOfPost;
			return this;
		}

		/**
		* Returns an initialized instance
		* @return initialized instance
		*/
		public ListingSearchRequestAPIInbound build() {
			return new ListingSearchRequestAPIInbound(this);
		}
		
	}
	
}