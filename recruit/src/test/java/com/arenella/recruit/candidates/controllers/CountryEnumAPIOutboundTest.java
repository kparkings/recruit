package com.arenella.recruit.candidates.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.enums.COUNTRY;

/**
* Unit tests for the CountryEnumAPIOutbound class
* @author K Parkings
*/
public class CountryEnumAPIOutboundTest {

	/**
	* Tests construction
	* @throws Exception
	*/
	@Test
	public void testConstructor() throws Exception{
		
		final COUNTRY 	country 	= COUNTRY.ITALY;
		final String 	iso2Code 	= COUNTRY.ITALY.getIsoCode();
		
		CountryEnumAPIOutbound apiOutbound = new CountryEnumAPIOutbound(country);
		
		assertEquals(country, apiOutbound.getName());
		assertEquals(iso2Code, apiOutbound.getIso2Code());
		
	}
	
}