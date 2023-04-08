package com.arenella.recruit.candidates.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.Candidate.Rate;
import com.arenella.recruit.candidates.beans.Candidate.Rate.CURRENCY;
import com.arenella.recruit.candidates.beans.Candidate.Rate.PERIOD;

/**
* Unit tests for the RateAPIOutbound class
* @author K Parkings
*/
public class RateAPIOutboundTest {

	/**
	* Tests construction
	* @throws Exception
	*/
	@Test
	public void testConstructor() throws Exception{
		
		final float value = 110.50f;
		
		RateAPIOutbound rate = new RateAPIOutbound(CURRENCY.EUR, PERIOD.HOUR, value);
		
		assertEquals(CURRENCY.EUR, rate.getCurrency());
		assertEquals(PERIOD.HOUR, rate.getPeriod());
		assertEquals(value, rate.getValue());
		
	}
	
	/**
	* Tests conversion from domain to API Outbound representation
	* @throws Exception
	*/
	@Test
	public void testConvertFromDomain_exists() throws Exception{
		
		Rate rate = new Rate(CURRENCY.EUR, PERIOD.DAY, 100);
		
		RateAPIOutbound rateAPIOutbound = RateAPIOutbound.convertFromDomain(Optional.of(rate));
		
		assertEquals(CURRENCY.EUR, 	rateAPIOutbound.getCurrency());
		assertEquals(PERIOD.DAY, 	rateAPIOutbound.getPeriod());
		assertEquals(100, 			rateAPIOutbound.getValue());
		
	}
	
	/**
	* Tests conversion from domain to API Outbound representation
	* @throws Exception
	*/
	@Test
	public void testConvertFromDomain_does_not_exists() throws Exception{
		
		RateAPIOutbound rateAPIOutbound = RateAPIOutbound.convertFromDomain(Optional.empty());
		
		assertNull(rateAPIOutbound);
		
	}
	
}