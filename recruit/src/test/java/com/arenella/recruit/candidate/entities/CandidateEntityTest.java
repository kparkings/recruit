package com.arenella.recruit.candidate.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidate.beans.Candidate.COUNTRY;

/**
* Unit tests for the CandidateEntity class
* @author K Parkings
*/
public class CandidateEntityTest {

	/**
	* Test Builder values used to initialize instance of the CandidateEntity Class 
	*/
	@Test
	public void testInitializationFromBuilder() {
		
		final String 		candidateId 			= "Candidate1";
		final COUNTRY 		country 				= COUNTRY.NETHERLANDS;
		final String 		city 					= "Den Haag";
		final boolean 		available 				= true;
		final boolean 		freelance 				= true;
		final boolean 		perm 					= true;
		final LocalDate 	lastAvailabilityCheck 	= LocalDate.of(1980, 12, 3);
		final LocalDate 	registerd 				= LocalDate.of(2021, 02, 20);
		final int 			yearsExperience 		= 21;
		
		CandidateEntity candidateEntity = CandidateEntity
						.builder()
							.candidateId(candidateId)
							.country(country)
							.city(city)
							.available(available)
							.freelance(freelance)
							.perm(perm)
							.lastAvailabilityCheck(lastAvailabilityCheck)
							.registerd(registerd)
							.yearsExperience(yearsExperience)
							.build();
		
		assertEquals(candidateEntity.getCandidateId(), candidateId);
		assertEquals(candidateEntity.getCountry(), country);
		assertEquals(candidateEntity.getCity(), city);
		assertEquals(candidateEntity.isAvailable(), available);
		assertEquals(candidateEntity.isFreelance(), freelance);
		assertEquals(candidateEntity.isPerm(), perm);
		assertEquals(candidateEntity.getLastAvailabilityCheckOn(), lastAvailabilityCheck);
		assertEquals(candidateEntity.getRegisteredOn(), registerd);
		assertEquals(candidateEntity.getYearsExperience(), yearsExperience);
		
	}
	
}