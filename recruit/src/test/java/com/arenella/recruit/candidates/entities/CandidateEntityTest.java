package com.arenella.recruit.candidates.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.Candidate.Photo;
import com.arenella.recruit.candidates.beans.Candidate.Photo.PHOTO_FORMAT;
import com.arenella.recruit.candidates.beans.Candidate.Rate;
import com.arenella.recruit.candidates.beans.Candidate.Rate.CURRENCY;
import com.arenella.recruit.candidates.beans.Candidate.Rate.PERIOD;
import com.arenella.recruit.candidates.beans.Language;
import com.arenella.recruit.candidates.beans.Language.LANGUAGE;
import com.arenella.recruit.candidates.beans.Language.LEVEL;
import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.enums.FREELANCE;
import com.arenella.recruit.candidates.enums.FUNCTION;
import com.arenella.recruit.candidates.enums.PERM;

/**
* Unit tests for the CandidateEntity class
* @author K Parkings
*/
public class CandidateEntityTest {

	private static final String	 		candidateId 			= "100";
	private static final String 		firstname				= "Kevin";
	private static final String 		surname					= "Parkings";
	private static final String 		email					= "kparkings@gmail.com";
	private static final String			roleSought				= "Python Developer";
	private static final FUNCTION		function				= FUNCTION.JAVA_DEV;
	private static final COUNTRY 		country 				= COUNTRY.NETHERLANDS;
	private static final String 		city 					= "Den Haag";
	private static final boolean 		available 				= true;
	private static final boolean		flaggedAsUnavailable	= true;
	private static final FREELANCE 		freelance 				= FREELANCE.TRUE;
	private static final PERM 			perm 					= PERM.TRUE;
	private static final LocalDate 		lastAvailabilityCheck 	= LocalDate.of(1980, 12, 3);
	private static final LocalDate 		registerd 				= LocalDate.of(2021, 02, 20);
	private static final int 			yearsExperience 		= 21;
	private static final Set<String>	skills					= new LinkedHashSet<>();
	private static final Set<Language>	languages				= new LinkedHashSet<>();
	private static final String			skill					= "Java";
	private static final Language		language				= Language.builder().language(LANGUAGE.DUTCH).level(LEVEL.BASIC).build();
	private static final String			introduction			= "intro";
	private static final CURRENCY		rateCurrency			= CURRENCY.EUR;
	private static final PERIOD			ratePeriod				= PERIOD.DAY;
	private static final float			rateValue				= 1f;
	private static final byte[]			photoBytes			 	= new byte[] {1,2,3};
	private static final PHOTO_FORMAT	photoFormat				= PHOTO_FORMAT.jpeg;
	
	
	/**
	* Sets up test environment 
	*/
	public CandidateEntityTest(){
		
		languages.add(language);
		skills.add(skill);
		
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
					.firstname(firstname)
					.surname(surname)
					.email(email)
					.roleSought(roleSought)
					.function(function)
					.country(country)
					.city(city)
					.available(available)
					.flaggedAsUnavailable(flaggedAsUnavailable)
					.freelance(freelance)
					.perm(perm)
					.lastAvailabilityCheck(lastAvailabilityCheck)
					.registerd(registerd)
					.yearsExperience(yearsExperience)
					.introduction(introduction)
					.rate(new Rate(rateCurrency, ratePeriod, rateValue))
					.photo(new Photo(photoBytes, photoFormat))
					.build();
		
		CandidateEntity candidateEntity = CandidateEntity.convertToEntity(candidate);

		assertEquals(Long.valueOf(candidateId), candidateEntity.getCandidateId());
		assertEquals(firstname, 				candidateEntity.getFirstname());
		assertEquals(surname, 					candidateEntity.getSurname());
		assertEquals(email, 					candidateEntity.getEmail());
		assertEquals(roleSought, 				candidateEntity.getRoleSought());
		assertEquals(function, 					candidateEntity.getFunction());
		assertEquals(country, 					candidateEntity.getCountry());
		assertEquals(city, 						candidateEntity.getCity());
		assertEquals(available, 				candidateEntity.isAvailable());
		assertEquals(flaggedAsUnavailable, 		candidateEntity.isFlaggedAsUnavailable());
		assertEquals(freelance, 				candidateEntity.isFreelance());
		assertEquals(perm, 						candidateEntity.isPerm());
		assertEquals(lastAvailabilityCheck, 	candidateEntity.getLastAvailabilityCheckOn());
		assertEquals(registerd, 				candidateEntity.getRegisteredOn());
		assertEquals(yearsExperience, 			candidateEntity.getYearsExperience());
		assertEquals(introduction, 				candidateEntity.getIntroduction());
		assertEquals(rateCurrency, 				candidateEntity.getRateCurrency());
		assertEquals(ratePeriod, 				candidateEntity.getRatePeriod());
		assertEquals(rateValue, 				candidateEntity.getRateValue());
		assertEquals(photoBytes, 				candidateEntity.getPhotoBytes());
		assertEquals(photoFormat, 				candidateEntity.getPhotoFormat());
		
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
					.firstname(firstname)
					.surname(surname)
					.email(email)
					.roleSought(roleSought)
					.function(function)
					.country(country)
					.city(city)
					.available(available)
					.flaggedAsUnavailable(flaggedAsUnavailable)
					.freelance(freelance)
					.perm(perm)
					.lastAvailabilityCheck(lastAvailabilityCheck)
					.registerd(registerd)
					.yearsExperience(yearsExperience)
					.skills(skills)
					.languages(languages)
					.introduction(introduction)
					.rateCurrency(rateCurrency)
					.ratePeriod(ratePeriod)
					.rateValue(rateValue)
					.photoBytes(photoBytes)
					.photoFormat(photoFormat)
				.build();
		
		Candidate candidate = CandidateEntity.convertFromEntity(candidateEntity);

		assertEquals(candidateId, 			candidate.getCandidateId());
		assertEquals(firstname, 			candidate.getFirstname());
		assertEquals(surname, 				candidate.getSurname());
		assertEquals(email, 				candidate.getEmail());
		assertEquals(roleSought, 			candidate.getRoleSought());
		assertEquals(function, 				candidate.getFunction());
		assertEquals(country, 				candidate.getCountry());
		assertEquals(city, 					candidate.getCity());
		assertEquals(available, 			candidate.isAvailable());
		assertEquals(flaggedAsUnavailable, 	candidate.isFlaggedAsUnavailable());
		assertEquals(freelance, 		candidate.isFreelance());
		assertEquals(perm, 					candidate.isPerm());
		assertEquals(lastAvailabilityCheck, candidate.getLastAvailabilityCheckOn());
		assertEquals(registerd, 			candidate.getRegisteredOn());
		assertEquals(yearsExperience,		candidate.getYearsExperience());
		assertEquals(introduction, 			candidate.getIntroduction());
		assertEquals(rateCurrency, 			candidate.getRate().get().getCurrency());
		assertEquals(ratePeriod, 			candidate.getRate().get().getPeriod());
		assertEquals(rateValue, 			candidate.getRate().get().getValue());
		assertEquals(photoBytes,	 		candidate.getPhoto().get().getImageBytes());
		assertEquals(photoFormat, 			candidate.getPhoto().get().getFormat());
		
		
		assertTrue(candidate.getSkills().contains(skill));
		assertEquals(candidate.getLanguages().stream().findFirst().get().getLanguage(), language.getLanguage());
	
	}
	
	/**
	* Tests updating of attributes on existing entity
	* @throws Exception
	*/
	@Test
	public void testUpdateExistingCandidate() throws Exception {
		
		CandidateEntity candidate = CandidateEntity.builder().build();
		
		assertFalse(candidate.isAvailable());
		
		candidate.setAvailable(true);
		
		assertTrue(candidate.isAvailable());
		assertNotEquals(CandidateEntity.ANONYMOUS_DATA, candidate.getFirstname());
		assertNotEquals(CandidateEntity.ANONYMOUS_DATA, candidate.getSurname());
		assertNotEquals(CandidateEntity.ANONYMOUS_DATA, candidate.getEmail());
		
		candidate.setAvailable(false);
		
		assertFalse(candidate.isAvailable());
		assertEquals(CandidateEntity.ANONYMOUS_DATA, candidate.getFirstname());
		assertEquals(CandidateEntity.ANONYMOUS_DATA, candidate.getSurname());
		assertEquals(CandidateEntity.ANONYMOUS_DATA, candidate.getEmail());
		
		
		assertFalse(candidate.isFlaggedAsUnavailable());
		
		candidate.setFlaggedAsUnavailable(true);
		
		assertTrue(candidate.isFlaggedAsUnavailable());
		
		candidate.setFlaggedAsUnavailable(false);
		
		assertFalse(candidate.isFlaggedAsUnavailable());
		
	}
	
	/**
	* Tests updating of the last time the Candidates availability was checked
	* @throws Exception
	*/
	@Test
	public void testSetCandidateAvailabilityChecked() throws Exception {
		
		final LocalDate pastDate = LocalDate.of(2000, 10, 5);
		
		CandidateEntity candidate = CandidateEntity.builder().available(false).lastAvailabilityCheck(pastDate).build();
		
		assertEquals(candidate.getLastAvailabilityCheckOn(), pastDate);
		
		candidate.setCandidateAvailabilityChecked();
		
		assertNotEquals(candidate.getLastAvailabilityCheckOn(), pastDate);
		
		assertTrue(candidate.getLastAvailabilityCheckOn() instanceof LocalDate);
		assertTrue(candidate.isAvailable());
		
	}
	
}
