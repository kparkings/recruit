package com.arenella.recruit.candidates.controllers;

import java.util.Optional;

import com.arenella.recruit.candidates.beans.Candidate.Rate;
import com.arenella.recruit.candidates.beans.Candidate.Rate.CURRENCY;
import com.arenella.recruit.candidates.beans.Candidate.Rate.PERIOD;

/**
* Represents a Rate charged by a Candidate for the work they do
* @author K Parkings
*/
public class RateAPIOutbound {
	
	private final CURRENCY 	currency;
	private final PERIOD 	period;
	private final float 	valueMin;
	private final float 	valueMax;
	
	/**
	* Constructor
	* @param currency 	- Currency Candidate charges in
	* @param period	  	- Period charged in
	* @param valueMin	- Min value acceptable for the Candidate
	* @param valueMax	- Max value acceptable for the Candidate
	*/
	public RateAPIOutbound(CURRENCY currency, PERIOD period, float valueMin, float valueMax) {
		this.currency 	= currency;
		this.period 	= period;
		this.valueMin 	= valueMin;
		this.valueMax 	= valueMax;
	}
	
	/**
	* Returns currency Candidate charges in
	* @return currency
	*/
	public CURRENCY getCurrency() {
		return this.currency;
	}
	
	/**
	* Returns unit of payment
	* @return payment period unit
	*/
	public PERIOD getPeriod() {
		return this.period;
	}

	/**
	* Returns minimum amount charged per unit 
	* of charging
	* @return value
	*/
	public float getValueMin() {
		return this.valueMin;
	}
	
	/**
	* Returns max amount charged per unit 
	* of charging
	* @return value
	*/
	public float getValueMax() {
		return this.valueMax;
	}
	
	/**
	* Converts from Domain representation
	* @param rate - Domain representation
	* @return API Outbound representation
	*/
	public static RateAPIOutbound convertFromDomain(Optional<Rate> rate) {
		
		if (rate.isEmpty()) {
			return null;
		}
		
		return new RateAPIOutbound(rate.get().getCurrency(), rate.get().getPeriod(), rate.get().getValueMin(), rate.get().getValueMax());
	}
	
}