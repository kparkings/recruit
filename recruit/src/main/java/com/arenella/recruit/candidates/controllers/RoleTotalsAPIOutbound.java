package com.arenella.recruit.candidates.controllers;

import com.arenella.recruit.candidates.beans.RoleTotals;
import com.arenella.recruit.candidates.enums.COUNTRY;

/**
* API Outbound representation of Candidate availability 
*/
public record RoleTotalsAPIOutbound(COUNTRY id, Long available, Long unavailable, Long total) {

	/**
	* Convert from domain representation to API Outbound representation
	* @param roleTotals - Domain representation
	* @return API Outbound representation
	*/
	public static RoleTotalsAPIOutbound convertFromDomain(RoleTotals roleTotals) {
		return new RoleTotalsAPIOutbound(roleTotals.id(), roleTotals.available(), roleTotals.unavailable(), roleTotals.available() + roleTotals.unavailable());
	}
	
}
