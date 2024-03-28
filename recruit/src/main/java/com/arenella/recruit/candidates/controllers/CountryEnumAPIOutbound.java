package com.arenella.recruit.candidates.controllers;

import com.arenella.recruit.candidates.enums.COUNTRY;

/**
* API Outbound representation of Country supported by the System 
* @author K Parkings
*/
public class CountryEnumAPIOutbound {

	private final COUNTRY 	country;
	private final String 	iso2Code;
	
	/**
	* Constructor
	* @param coountry		- Name of the Country
	* @param iso2Code	- iso2Code of the Country
	*/
	public CountryEnumAPIOutbound(COUNTRY country) {
		this.country 	= country;
		this.iso2Code 	= country.getIsoCode();
	}
	
	/**
	* Returns the name/id of the Country
	* @return name/id of the Country
	*/
	public COUNTRY getName() {
		return this.country;
	}

	/**
	* Returns 2 digit iso code
	* @return iso 2 code
	*/
	public String getIso2Code() {
		return this.iso2Code;
	}
	
}