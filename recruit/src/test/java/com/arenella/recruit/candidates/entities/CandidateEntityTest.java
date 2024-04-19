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
import com.arenella.recruit.candidates.beans.Candidate.CANDIDATE_TYPE;
import com.arenella.recruit.candidates.beans.Candidate.Photo;
import com.arenella.recruit.candidates.beans.Candidate.Photo.PHOTO_FORMAT;
import com.arenella.recruit.candidates.beans.Candidate.Rate;
import com.arenella.recruit.candidates.beans.Candidate.Rate.CURRENCY;
import com.arenella.recruit.candidates.beans.Candidate.Rate.PERIOD;
import com.arenella.recruit.candidates.beans.Candidate.SECURITY_CLEARANCE_TYPE;
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

	private static final String	 					candidateId 			= "100";
	private static final String 					firstname				= "Kevin";
	private static final String 					surname					= "Parkings";
	private static final String 					email					= "kparkings@gmail.com";
	private static final String						roleSought				= "Python Developer";
	private static final FUNCTION					function				= FUNCTION.JAVA_DEV;
	private static final COUNTRY 					country 				= COUNTRY.NETHERLANDS;
	private static final String 					city 					= "Den Haag";
	private static final boolean 					available 				= true;
	private static final boolean					flaggedAsUnavailable	= true;
	private static final FREELANCE 					freelance 				= FREELANCE.TRUE;
	private static final PERM 						perm 					= PERM.TRUE;
	private static final LocalDate 					lastAvailabilityCheck 	= LocalDate.of(1980, 12, 3);
	private static final LocalDate 					registerd 				= LocalDate.of(2021, 02, 20);
	private static final int 						yearsExperience 		= 21;
	private static final Set<String>				skills					= new LinkedHashSet<>();
	private static final Set<Language>				languages				= new LinkedHashSet<>();
	private static final String						skill					= "Java";
	private static final Language					language				= Language.builder().language(LANGUAGE.DUTCH).level(LEVEL.BASIC).build();
	private static final String						introduction			= "intro";
	private static final byte[]						photoBytes			 	= new byte[] {1,2,3};
	private static final PHOTO_FORMAT				photoFormat				= PHOTO_FORMAT.jpeg;
	private static final Rate						RATE_CONTRACT			= new Rate(CURRENCY.EUR, PERIOD.DAY, 1f, 2f);
	private static final Rate						RATE_PERM				= new Rate(CURRENCY.GBP, PERIOD.YEAR, 11f, 22f);
	private static final LocalDate					AVAILABLE_FROM			= LocalDate.of(2023, 02, 20);
	private static final String						OWNER_ID				= "rec33";
	private static final CANDIDATE_TYPE 			CANDIDATE_TYPE_VAL		= CANDIDATE_TYPE.MARKETPLACE_CANDIDATE;
	private static final boolean					REQUIRES_SPONSORSHIP	= true;
	private static final SECURITY_CLEARANCE_TYPE 	SECURITY_LEVEL 			= SECURITY_CLEARANCE_TYPE.DV; 
	
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
					.rateContract(RATE_CONTRACT)
					.ratePerm(RATE_PERM)
					.photo(new Photo(photoBytes, photoFormat))
					.availableFromDate(AVAILABLE_FROM)
					.ownerId(OWNER_ID)
					.candidateType(CANDIDATE_TYPE_VAL)
					.requiresSponsorship(REQUIRES_SPONSORSHIP)
					.securityClearance(SECURITY_LEVEL)
					.build();
		
		CandidateEntity candidateEntity = CandidateEntity.convertToEntity(candidate);

		assertEquals(Long.valueOf(candidateId), 	candidateEntity.getCandidateId());
		assertEquals(firstname, 					candidateEntity.getFirstname());
		assertEquals(surname, 						candidateEntity.getSurname());
		assertEquals(email, 						candidateEntity.getEmail());
		assertEquals(roleSought, 					candidateEntity.getRoleSought());
		assertEquals(function, 						candidateEntity.getFunction());
		assertEquals(country, 						candidateEntity.getCountry());
		assertEquals(city, 							candidateEntity.getCity());
		assertEquals(available, 					candidateEntity.isAvailable());
		assertEquals(flaggedAsUnavailable, 			candidateEntity.isFlaggedAsUnavailable());
		assertEquals(freelance, 					candidateEntity.isFreelance());
		assertEquals(perm, 							candidateEntity.isPerm());
		assertEquals(lastAvailabilityCheck, 		candidateEntity.getLastAvailabilityCheckOn());
		assertEquals(registerd, 					candidateEntity.getRegisteredOn());
		assertEquals(yearsExperience, 				candidateEntity.getYearsExperience());
		assertEquals(introduction, 					candidateEntity.getIntroduction());
		assertEquals(RATE_CONTRACT.getCurrency(), 	candidateEntity.getRateContractCurrency());
		assertEquals(RATE_CONTRACT.getPeriod(), 	candidateEntity.getRateContractPeriod());
		assertEquals(RATE_CONTRACT.getValueMin(), 	candidateEntity.getRateContractValueMin());
		assertEquals(RATE_CONTRACT.getValueMax(), 	candidateEntity.getRateContractValueMax());
		assertEquals(RATE_PERM.getCurrency(), 		candidateEntity.getRatePermCurrency());
		assertEquals(RATE_PERM.getPeriod(), 		candidateEntity.getRatePermPeriod());
		assertEquals(RATE_PERM.getValueMin(), 		candidateEntity.getRatePermValueMin());
		assertEquals(RATE_PERM.getValueMax(), 		candidateEntity.getRatePermValueMax());
		assertEquals(photoBytes, 					candidateEntity.getPhotoBytes());
		assertEquals(photoFormat, 					candidateEntity.getPhotoFormat());
		assertEquals(AVAILABLE_FROM, 				candidateEntity.getAvailableFromDate());
		assertEquals(OWNER_ID, 						candidateEntity.getOwnerId().get());
		assertEquals(CANDIDATE_TYPE_VAL, 			candidateEntity.getCandidateType());
		assertEquals(REQUIRES_SPONSORSHIP,			candidateEntity.getRequiresSponsorship());
		assertEquals(SECURITY_LEVEL,				candidateEntity.getSecurityClearance());
		
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
					.rateContractCurrency(RATE_CONTRACT.getCurrency())
					.rateContractPeriod(RATE_CONTRACT.getPeriod())
					.rateContractValueMin(RATE_CONTRACT.getValueMin())
					.rateContractValueMax(RATE_CONTRACT.getValueMax())
					.ratePermCurrency(RATE_PERM.getCurrency())
					.ratePermPeriod(RATE_PERM.getPeriod())
					.ratePermValueMin(RATE_PERM.getValueMin())
					.ratePermValueMax(RATE_PERM.getValueMax())
					.photoBytes(photoBytes)
					.photoFormat(photoFormat)
					.availableFromDate(AVAILABLE_FROM)
					.ownerId(OWNER_ID)
					.candidateType(CANDIDATE_TYPE_VAL)
					.requiresSponsorship(REQUIRES_SPONSORSHIP)
					.securityClearance(SECURITY_LEVEL)
				.build();
		
		Candidate candidate = CandidateEntity.convertFromEntity(candidateEntity);

		assertEquals(candidateId, 					candidate.getCandidateId());
		assertEquals(firstname, 					candidate.getFirstname());
		assertEquals(surname, 						candidate.getSurname());
		assertEquals(email, 						candidate.getEmail());
		assertEquals(roleSought, 					candidate.getRoleSought());
		assertEquals(function, 						candidate.getFunction());
		assertEquals(country, 						candidate.getCountry());
		assertEquals(city, 							candidate.getCity());
		assertEquals(available, 					candidate.isAvailable());
		assertEquals(flaggedAsUnavailable, 			candidate.isFlaggedAsUnavailable());
		assertEquals(freelance, 					candidate.isFreelance());
		assertEquals(perm, 							candidate.isPerm());
		assertEquals(lastAvailabilityCheck, 		candidate.getLastAvailabilityCheckOn());
		assertEquals(registerd, 					candidate.getRegisteredOn());
		assertEquals(yearsExperience,				candidate.getYearsExperience());
		assertEquals(introduction, 					candidate.getIntroduction());
		assertEquals(RATE_CONTRACT.getCurrency(), 	candidate.getRateContract().get().getCurrency());
		assertEquals(RATE_CONTRACT.getPeriod(), 	candidate.getRateContract().get().getPeriod());
		assertEquals(RATE_CONTRACT.getValueMin(), 	candidate.getRateContract().get().getValueMin());
		assertEquals(RATE_CONTRACT.getValueMax(), 	candidate.getRateContract().get().getValueMax());
		assertEquals(RATE_PERM.getCurrency(), 		candidate.getRatePerm().get().getCurrency());
		assertEquals(RATE_PERM.getPeriod(), 		candidate.getRatePerm().get().getPeriod());
		assertEquals(RATE_PERM.getValueMin(), 		candidate.getRatePerm().get().getValueMin());
		assertEquals(RATE_PERM.getValueMax(), 		candidate.getRatePerm().get().getValueMax());
		assertEquals(photoBytes,	 				candidate.getPhoto().get().getImageBytes());
		assertEquals(photoFormat, 					candidate.getPhoto().get().getFormat());
		assertEquals(AVAILABLE_FROM, 				candidate.getAvailableFromDate());
		assertEquals(OWNER_ID, 						candidate.getOwnerId().get());
		assertEquals(CANDIDATE_TYPE_VAL, 			candidate.getCandidateType());
		assertEquals(REQUIRES_SPONSORSHIP, 			candidate.getRequiresSponsorship());
		assertEquals(SECURITY_LEVEL,				candidate.getSecurityClearance());		
		
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
	* Test conversion where no ownerId is present
	* @throws Exception
	*/
	@Test
	public void testConvertFromEntity_noOwnerId() throws Exception{
		
		CandidateEntity candidateEntity = CandidateEntity.builder().candidateId("123").build();
		
		Candidate candidate = CandidateEntity.convertFromEntity(candidateEntity);
		
		assertTrue(candidate.getOwnerId().isEmpty());
	
	}
	
	/**
	* Test conversion where no ownerId is present
	* @throws Exception
	*/
	@Test
	public void testConvertToEntity_noOwnerId() throws Exception{
		
		Candidate candidate = Candidate.builder().candidateId("123").build();
		
		CandidateEntity candidateEntity = CandidateEntity.convertToEntity(candidate);
		
		assertTrue(candidateEntity.getOwnerId().isEmpty());
		
	}
}
