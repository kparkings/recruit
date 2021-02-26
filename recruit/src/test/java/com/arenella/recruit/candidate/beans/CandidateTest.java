package com.arenella.recruit.candidate.beans;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.arenella.recruit.candidate.beans.Candidate;
import com.arenella.recruit.candidate.beans.Candidate.COUNTRY;
import com.arenella.recruit.candidate.beans.Candidate.FUNCTION;
import com.arenella.recruit.candidate.entities.CandidateEntity;

/**
* Unit test for the Candidate Class
* @author K Parkings
*/
@RunWith(MockitoJUnitRunner.class)
public class CandidateTest {

	@Mock
	private Language mockLanguage;
	
	private static final String 		candidateId 			= "Candidate1";
	private static final FUNCTION		function				= FUNCTION.JAVA_DEV;
	private static final COUNTRY 		country 				= COUNTRY.NETHERLANDS;
	private static final String 		city 					= "Den Haag";
	private static final boolean 		available 				= true;
	private static final boolean 		freelance 				= true;
	private static final boolean 		perm 					= true;
	private static final LocalDate 		lastAvailabilityCheck 	= LocalDate.of(1980, 12, 3);
	private static final LocalDate 		registerd 				= LocalDate.of(2021, 02, 20);
	private static final int 			yearsExperience 		= 21;
	private static final Set<String>	skills					= new LinkedHashSet<>();
	private static final Set<Language>	languages				= new LinkedHashSet<>();
	private static final String			skill					= "Java";
	
	/**
	* Sets up test environment 
	*/
	public CandidateTest(){
		languages.add(mockLanguage);
		skills.add(skill);
		
	}
	
	/**
	* Test Builder values used to initialize instance of the Candidate Class 
	*/
	@Test
	public void testInitializationFromBuilder() {
		
		Candidate candidate = Candidate
						.builder()
							.candidateId(candidateId)
							.function(function)
							.country(country)
							.city(city)
							.available(available)
							.freelance(freelance)
							.perm(perm)
							.lastAvailabilityCheck(lastAvailabilityCheck)
							.registerd(registerd)
							.yearsExperience(yearsExperience)
							.skills(skills)
							.languages(languages)
							.build();
		
		assertEquals(candidate.getCandidateId(), 				candidateId);
		assertEquals(candidate.getFunction(), 					function);
		assertEquals(candidate.getCountry(), 					country);
		assertEquals(candidate.getCity(), 						city);
		assertEquals(candidate.isAvailable(), 					available);
		assertEquals(candidate.isFreelance(), 					freelance);
		assertEquals(candidate.isPerm(), 						perm);
		assertEquals(candidate.getLastAvailabilityCheckOn(), 	lastAvailabilityCheck);
		assertEquals(candidate.getRegisteredOn(), 				registerd);
		assertEquals(candidate.getYearsExperience(), 			yearsExperience);
		
		assertTrue(candidate.getSkills().contains(skill));
		assertTrue(candidate.getLanguages().contains(mockLanguage));
		
	}
	
	/**
	* Tests that conversion returns an Entity representation of the 
	* Domain representation of a Candidate and the state is copied 
	* successfully 
	*/
	@Test
	public void testConversionToEntity() {
		
		Candidate candidate = Candidate
				.builder()
					.candidateId(candidateId)
					.function(function)
					.country(country)
					.city(city)
					.available(available)
					.freelance(freelance)
					.perm(perm)
					.lastAvailabilityCheck(lastAvailabilityCheck)
					.registerd(registerd)
					.yearsExperience(yearsExperience)
					.build();
		
		CandidateEntity candidateEntity = Candidate.convertToEntity(candidate);

		assertEquals(candidateEntity.getCandidateId(), 				candidateId);
		assertEquals(candidateEntity.getFunction(),					function);
		assertEquals(candidateEntity.getCountry(), 					country);
		assertEquals(candidateEntity.getCity(), 					city);
		assertEquals(candidateEntity.isAvailable(), 				available);
		assertEquals(candidateEntity.isFreelance(), 				freelance);
		assertEquals(candidateEntity.isPerm(), 						perm);
		assertEquals(candidateEntity.getLastAvailabilityCheckOn(), 	lastAvailabilityCheck);
		assertEquals(candidateEntity.getRegisteredOn(), 			registerd);
		assertEquals(candidateEntity.getYearsExperience(), 			yearsExperience);

	}
	
	/**
	* Tests that conversion returns a Domain representation of the 
	* Entity representation of a Candidate and the state is copied 
	* successfully 
	*/
	@Test
	public void testConversionFromEntity() {
		
		CandidateEntity candidateEntity = CandidateEntity
				.builder()
					.candidateId(candidateId)
					.function(function)
					.country(country)
					.city(city)
					.available(available)
					.freelance(freelance)
					.perm(perm)
					.lastAvailabilityCheck(lastAvailabilityCheck)
					.registerd(registerd)
					.yearsExperience(yearsExperience)
					.skills(skills)
					.languages(languages)
					.build();
		
		Candidate candidate = Candidate.convertFromEntity(candidateEntity);

		assertEquals(candidate.getCandidateId(), 				candidateId);
		assertEquals(candidate.getFunction(), 					function);
		assertEquals(candidate.getCountry(), 					country);
		assertEquals(candidate.getCity(), 						city);
		assertEquals(candidate.isAvailable(), 					available);
		assertEquals(candidate.isFreelance(), 					freelance);
		assertEquals(candidate.isPerm(), 						perm);
		assertEquals(candidate.getLastAvailabilityCheckOn(), 	lastAvailabilityCheck);
		assertEquals(candidate.getRegisteredOn(), 				registerd);
		assertEquals(candidate.getYearsExperience(), 			yearsExperience);
		
		assertTrue(candidate.getSkills().contains(skill));
		assertTrue(candidate.getLanguages().contains(mockLanguage));
	
	}
	
}