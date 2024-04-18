package com.arenella.recruit.candidates.entities;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.arenella.recruit.candidates.beans.Candidate.Rate;
import com.arenella.recruit.candidates.beans.Candidate.Rate.CURRENCY;
import com.arenella.recruit.candidates.beans.Candidate.Rate.PERIOD;

/**
* Elasticsearch Document representation of a Rate
* @author K Parkings
*/
public class RateDocument {
		
	@Field(type = FieldType.Keyword)
	private CURRENCY 	currency;
	
	@Field(type = FieldType.Keyword)
	private PERIOD 	period;
	
	@Field(type = FieldType.Float)
	private float 	valueMin;
	
	@Field(type = FieldType.Float)
	private float 	valueMax;

	public RateDocument() {
		
	}
	
	/**
	* Constructor
	* @param currency	- Currenty
	* @param period		- Type of billing period
	* @param valueMin	- Min possible rate
	* @param valueMax	- Max possible rate
	*/
	public RateDocument(CURRENCY currency, PERIOD period, float valueMin, float valueMax) {
		this.currency 	= currency;
		this.period 	= period;
		this.valueMin 	= valueMin;
		this.valueMax 	= valueMax;
	}
	
	/**
	* Returns the Currency the Rate is in
	* @return Currency
	*/
	public final CURRENCY getCurrency() {
		return this.currency;
	}
	
	/**
	* Returns the period the Rate applies to
	* @return Period
	*/
	public final PERIOD getPeriod() {
		return this.period;
	}
	
	/**
	* Returns the minimum value of the currency 
	* the candidate is looking for in the given 
	* period
	* @return min compensation
	*/
	public final float getValueMin() {
		return this.valueMin;
	}
	
	/**
	* Returns the maximum value of the currency 
	* the candidate is looking for in the given 
	* period
	* @return max compendation
	*/
	public final float getValueMax() {
		return this.valueMax;
	}
	
	/**
	* Converts from Domain to Persistence layer representation
	* @param rate - Rate to convert
	* @return converted Rate
	*/
	public static RateDocument convertFromDomain(Rate rate) {
		return new RateDocument(rate.getCurrency(), rate.getPeriod(), rate.getValueMin(), rate.getValueMax());
	}
	
	/**
	* Converts from Persistence layer to Domain representation
	* @param rate - Rate to convert
	* @return converted Rate
	*/
	public static Rate convertToDomain(RateDocument rate) {
		return new Rate(rate.getCurrency(), rate.getPeriod(), rate.getValueMin(), rate.getValueMax());
	}
	
}