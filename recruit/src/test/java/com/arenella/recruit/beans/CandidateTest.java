package com.arenella.recruit.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.beans.Candidate.COUNTRY;

/**
* Unit test for the Candidate Class
* @author K Parkings
*/
public class CandidateTest {

	/**
	* Test Builder values used to initialize instance of the Candidate Class 
	*/
	@Test
	public void testInitializationFromBuilder() {
		
		final String 		candidateId 			=	"Candidate1";
		final COUNTRY 		country 				= COUNTRY.NETHERLANDS;
		final String 		city 					= "Den Haag";
		final boolean 		available 				= true;
		final boolean 		freelance 				= true;
		final boolean 		perm 					= true;
		final LocalDate 	lastAvailabilityCheck 	= LocalDate.of(1980, 12, 3);
		final LocalDate 	registerd 				= LocalDate.of(2021, 02, 20);
		final int 			yearsExperience 		= 21;
		
		Candidate candidate = Candidate
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
		
		assertEquals(candidate.getCandidateId(), candidateId);
		assertEquals(candidate.getCountry(), country);
		assertEquals(candidate.getCity(), city);
		assertEquals(candidate.isAvailable(), available);
		assertEquals(candidate.isFreelance(), freelance);
		assertEquals(candidate.isPerm(), perm);
		assertEquals(candidate.getLastAvailabilityCheckOn(), lastAvailabilityCheck);
		assertEquals(candidate.getRegisteredOn(), registerd);
		assertEquals(candidate.getYearsExperience(), yearsExperience);
		
		
	}
	
}
