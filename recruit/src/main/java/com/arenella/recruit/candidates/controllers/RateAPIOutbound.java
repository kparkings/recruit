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
	
	private final CURRENCY currency;
	private final PERIOD period;
	private final float value;
	
	/**
	* Constructor
	* @param currency 	- Currency Candidate charges in
	* @param period	  	- Period charged in
	* @param value		- amount charged for work
	*/
	public RateAPIOutbound(CURRENCY currency, PERIOD period, float value) {
		this.currency 	= currency;
		this.period 	= period;
		this.value 	= value;
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
	* Returns amount charged per unit
	* @return amount charged
	*/
	public float getValue() {
		return this.value;
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
		
		return new RateAPIOutbound(rate.get().getCurrency(), rate.get().getPeriod(), rate.get().getValue());
	}
	
}