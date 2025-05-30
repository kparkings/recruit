package com.arenella.recruit.candidates.beans;

import java.util.Optional;

import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.utils.GeoZoneSearchUtil.GEO_ZONE;

/**
* Filters to apply when extracting
* Role total available/unavailable breakdown
*/
public class RoleTotalsFilters {

	private GEO_ZONE 		zone;
	private COUNTRY	 		country;
	
	/**
	* Constructor based upon a builder
	* @param builder - Contains initialization values
	*/
	public RoleTotalsFilters(RoleTotalsFiltersBuilder builder) {
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
	public static RoleTotalsFiltersBuilder builder(){
		return new RoleTotalsFiltersBuilder();
	}
	
	/**
	* Builder for the class 
	*/
	public static class RoleTotalsFiltersBuilder {
		
		private GEO_ZONE 		zone;
		private COUNTRY 		country;
		
		/**
		* Sets optional Zone to filter on
		* @param zone - Id of Zone
		* @return Builder
		*/
		public RoleTotalsFiltersBuilder zone(GEO_ZONE zone) {
			this.zone = zone;
			return this;
		}
		
		/**
		* Sets optional country to filter on
		* @param country - Country id
		* @return Builder
		*/
		public RoleTotalsFiltersBuilder country(COUNTRY country) {
			this.country = country;
			return this;
		}
		
		/**
		* Returns an initialized instance of the class
		* @return initialized instance
		*/
		public RoleTotalsFilters build() {
			return new RoleTotalsFilters(this);
		}
		
	}
	
}