package com.arenella.recruit.candidates.controllers;

import java.util.Optional;

import com.arenella.recruit.candidates.beans.RoleTotalsFilters;
import com.arenella.recruit.candidates.beans.RoleTotalsFilters.RoleTotalsFiltersBuilder;
import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.utils.GeoZoneSearchUtil.GEO_ZONE;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
* Filter options to apply to retrieval of 
* Role total statistics 
*/
@JsonDeserialize(builder=RoleTotalsFiltersAPIInbound.RoleTotalsFiltersAPIInboundBuilder.class)
public class RoleTotalsFiltersAPIInbound {
	
	private GEO_ZONE 		zone;
	private COUNTRY 		country;
	
	/**
	* Constructor based upon a builder
	* @param builder - Contains initialization values
	*/
	public RoleTotalsFiltersAPIInbound(RoleTotalsFiltersAPIInboundBuilder builder) {
		this.zone	 		= builder.zone;
		this.country 		= builder.country;
	}
	
	/**
	* Sets the Zone, if any to filter on
	* @return
	*/
	public Optional<GEO_ZONE> getZone() {
		return Optional.ofNullable(this.zone);
	}
	
	/**
	* Sets the Country, if any to filter on
	* @return
	*/
	public Optional<COUNTRY> getCountry() {
		return Optional.ofNullable(this.country);
	}
	
	/**
	* Returns a builder for the class
	* @return Builder for the class
	*/
	public static RoleTotalsFiltersAPIInboundBuilder builder(){
		return new RoleTotalsFiltersAPIInboundBuilder();
	}
	
	/**
	* Builder for the class 
	*/
	@JsonPOJOBuilder(buildMethodName="build", withPrefix="")
	public static class RoleTotalsFiltersAPIInboundBuilder {
		
		private GEO_ZONE 		zone;
		private COUNTRY 		country;
		
		/**
		* Sets optional Zone to filter on
		* @param zone - Id of Zone
		* @return Builder
		*/
		public RoleTotalsFiltersAPIInboundBuilder zone(GEO_ZONE zone) {
			this.zone = zone;
			return this;
		}
		
		/**
		* Sets optional country to filter on
		* @param country - Country id
		* @return Builder
		*/
		public RoleTotalsFiltersAPIInboundBuilder country(COUNTRY country) {
			this.country = country;
			return this;
		}
		
		/**
		* Returns an initialized instance of the class
		* @return initialized instance
		*/
		public RoleTotalsFiltersAPIInbound build() {
			return new RoleTotalsFiltersAPIInbound(this);
		}
		
	}
	
	/**
	* Converts from API Inbound representation to 
	* Domain representation
	* @param filters - To convert
	* @return converted
	*/
	public static RoleTotalsFilters toDomain(RoleTotalsFiltersAPIInbound filters) {
		
		RoleTotalsFiltersBuilder builder = RoleTotalsFilters.builder();
		
		filters.getCountry().ifPresent(builder::country);
		filters.getZone().ifPresent(builder::zone);
		
		return builder.build();
		
	}
		
}