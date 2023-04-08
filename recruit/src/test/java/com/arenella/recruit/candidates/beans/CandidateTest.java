package com.arenella.recruit.candidates.beans;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.Candidate.Photo;
import com.arenella.recruit.candidates.beans.Candidate.Photo.PHOTO_FORMAT;
import com.arenella.recruit.candidates.beans.Candidate.Rate;
import com.arenella.recruit.candidates.beans.Candidate.Rate.CURRENCY;
import com.arenella.recruit.candidates.beans.Candidate.Rate.PERIOD;
import com.arenella.recruit.candidates.beans.Language.LANGUAGE;
import com.arenella.recruit.candidates.beans.Language.LEVEL;
import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.enums.FREELANCE;
import com.arenella.recruit.candidates.enums.FUNCTION;
import com.arenella.recruit.candidates.enums.PERM;

/**
* Unit test for the Candidate Class
* @author K Parkings
*/
public class CandidateTest {

	private static final String 		candidateId 			= "Candidate1";
	private static final FUNCTION		function				= FUNCTION.JAVA_DEV;
	private static final COUNTRY 		country 				= COUNTRY.NETHERLANDS;
	private static final String 		city 					= "Den Haag";
	private static final String 		email					= "kparkings@gmail.com";
	private static final String 		roleSought				= "Senior java Dev";
	private static final boolean 		available 				= true;
	private static final boolean 		flaggedAsUnavailable	= true;
	private static final FREELANCE 		freelance 				= FREELANCE.TRUE;
	private static final PERM 			perm 					= PERM.TRUE;
	private static final LocalDate 		lastAvailabilityCheck 	= LocalDate.of(1980, 12, 3);
	private static final LocalDate 		registerd 				= LocalDate.of(2021, 02, 20);
	private static final int 			yearsExperience 		= 21;
	private static final Set<String>	skills					= new LinkedHashSet<>();
	private static final Set<Language>	languages				= new LinkedHashSet<>();
	private static final String			skill					= "Java";
	private static final Language		language				= Language.builder().language(LANGUAGE.DUTCH).level(LEVEL.PROFICIENT).build();
	private static final Rate			rate					= new Rate(CURRENCY.EUR, PERIOD.HOUR, 11.34f);
	private static final String			introduction			= "Candiates own intro";
	private static final Photo			profilePhoto			= new Photo(new byte[] {}, PHOTO_FORMAT.jpeg);
	
	/**
	* Sets up test environment 
	*/
	public CandidateTest(){
		
		languages.add(language);
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
							.email(email)
							.roleSought(roleSought)
							.available(available)
							.flaggedAsUnavailable(flaggedAsUnavailable)
							.freelance(freelance)
							.perm(perm)
							.lastAvailabilityCheck(lastAvailabilityCheck)
							.registerd(registerd)
							.yearsExperience(yearsExperience)
							.skills(skills)
							.languages(languages)
							.rate(rate)
							.introduction(introduction)
							.photo(profilePhoto)
							.build();
		
		assertEquals(candidateId, 			candidate.getCandidateId());
		assertEquals(function, 				candidate.getFunction());
		assertEquals(country, 				candidate.getCountry());
		assertEquals(city, 					candidate.getCity());
		assertEquals(email, 				candidate.getEmail());
		assertEquals(roleSought, 			candidate.getRoleSought());
		assertEquals(available, 			candidate.isAvailable());
		assertEquals(flaggedAsUnavailable, 	candidate.isFlaggedAsUnavailable());
		assertEquals(freelance, 			candidate.isFreelance());
		assertEquals(perm, 					candidate.isPerm());
		assertEquals(lastAvailabilityCheck, candidate.getLastAvailabilityCheckOn());
		assertEquals(registerd, 			candidate.getRegisteredOn());
		assertEquals(yearsExperience, 		candidate.getYearsExperience());
		assertEquals(profilePhoto, 			candidate.getPhoto().get());
		
		assertEquals(introduction, 			candidate.getIntroduction());
		assertEquals(rate.getCurrency(), 	candidate.getRate().get().getCurrency());
		assertEquals(rate.getPeriod(), 		candidate.getRate().get().getPeriod());
		assertEquals(rate.getValue(), 		candidate.getRate().get().getValue());
		
		assertTrue(candidate.getSkills().contains(skill));
		candidate.getLanguages().stream().filter(l -> l.getLanguage() == language.getLanguage()).findAny().orElseThrow();
		
	}
	
	/**
	* Tests correct values are made anonymous if the Candidate is 
	* no longer available. We want to see their info in the Status but 
	* we no longer need to know who they are
	* @throws Exception
	*/
	@Test
	public void testNoLongerAvailable() throws Exception {
		
		final String firstName 	= "Kevin";
		final String surname 	= "Parkings";
		final String email 		= "kparkings@gmail.com";
		
		Candidate candidate = Candidate
				.builder()
					.available(true)
					.firstname(firstName)
					.surname(surname)
					.email(email)
				.build();
		
		assertTrue(candidate.isAvailable());
		assertEquals(candidate.getFirstname(), firstName);
		assertEquals(candidate.getSurname(), surname);
		assertEquals(candidate.getEmail(), email);
		
		candidate.noLongerAvailable();
		
		assertFalse(candidate.isAvailable());
		
	}
	
	/**
	* Tests case where Candidate is made available to search on
	* @throws Exception
	*/
	@Test
	public void testMakeAvailable() throws Exception{
		
		Candidate candidate = Candidate.builder().build();
				
		assertFalse(candidate.isAvailable());
	
		candidate.makeAvailable();
	
		assertTrue(candidate.isAvailable());
		
	}
	
}