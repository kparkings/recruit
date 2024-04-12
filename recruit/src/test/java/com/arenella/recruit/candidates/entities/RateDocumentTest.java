package com.arenella.recruit.candidates.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.Candidate.Rate.CURRENCY;
import com.arenella.recruit.candidates.beans.Candidate.Rate.PERIOD;

/**
* Unit tests for the RateDocument class
* @author K Parkings
*/
public class RateDocumentTest {

	private static final CURRENCY 	CURRENCY_VAL	= CURRENCY.EUR;
	private static final PERIOD 	PERIOD_VAL		= PERIOD.YEAR;
	private static final float 		VALUE_MIN		= 18000f;
	private static final float 		VALUE_MAX		= 20000f;
	
	/**
	* Tests construction of the RateDocument
	* @throws Exception
	*/
	@Test
	public void testConstruction() throws Exception{
		
		RateDocument doc = new RateDocument(CURRENCY_VAL, PERIOD_VAL, VALUE_MIN, VALUE_MAX);
		
		assertEquals(CURRENCY_VAL, 	doc.getCurrency());
		assertEquals(PERIOD_VAL, 	doc.getPeriod());
		assertEquals(VALUE_MIN, 	doc.getValueMin());
		assertEquals(VALUE_MAX, 	doc.getValueMax());
		
	}
	
	/**
	* Test conversion from Domain to Persistence representation
	* @throws Exception
	*/
	@Test
	public void testConvertFromDomain() throws Exception{
		
		 Candidate.Rate rate 	= new Candidate.Rate(CURRENCY_VAL, PERIOD_VAL, VALUE_MIN, VALUE_MAX);
		 RateDocument 	doc 	= RateDocument.convertFromDomain(rate);
		
		assertEquals(CURRENCY_VAL, 	doc.getCurrency());
		assertEquals(PERIOD_VAL, 	doc.getPeriod());
		assertEquals(VALUE_MIN, 	doc.getValueMin());
		assertEquals(VALUE_MAX, 	doc.getValueMax());
		
	}
	
	/**
	* Test conversion from Persistence to Domain representation
	* @throws Exception
	*/
	@Test
	public void testConvertToDomain() throws Exception{
		
		RateDocument 	doc 	= new RateDocument(CURRENCY_VAL, PERIOD_VAL, VALUE_MIN, VALUE_MAX);
		Candidate.Rate 	rate 	= RateDocument.convertToDomain(doc);
		
		assertEquals(CURRENCY_VAL, 	rate.getCurrency());
		assertEquals(PERIOD_VAL, 	rate.getPeriod());
		assertEquals(VALUE_MIN, 	rate.getValueMin());
		assertEquals(VALUE_MAX, 	rate.getValueMax());
		
	}
	
}