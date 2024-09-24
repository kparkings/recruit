package com.arenella.recruit.candidates.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.CandidateSearchAccuracyWrapper;
import com.arenella.recruit.candidates.beans.Language;
import com.arenella.recruit.candidates.beans.Candidate.SECURITY_CLEARANCE_TYPE;
import com.arenella.recruit.candidates.beans.Language.LANGUAGE;
import com.arenella.recruit.candidates.beans.Language.LEVEL;
import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.enums.FREELANCE;
import com.arenella.recruit.candidates.enums.FUNCTION;
import com.arenella.recruit.candidates.enums.PERM;
import com.arenella.recruit.candidates.utils.CandidateSuggestionUtil.suggestion_accuracy;

/**
* Unit tests for the CandidateAPIInboundTest class
* @author K Parkings
*/
public class CandidateSuggestionAPIOutboundTest {

	private static final String 					candidateId				= "999";
	private static final String 					firstname				= "Kevin";
	private static final String 					surname					= "Parkings";
	private static final String 					email					= "admin@arenella-ict.com";
	private static final String						roleSought				= "Senior Java Developer";
	private static final FUNCTION					function				= FUNCTION.JAVA_DEV;
	private static final COUNTRY 					country					= COUNTRY.NETHERLANDS;
	private static final String 					city					= "Noordwijk";
	private static final PERM 						perm					= PERM.FALSE;
	private static final FREELANCE 					freelance				= FREELANCE.TRUE;
	private static final int						yearsExperience			= 22;
	private static final boolean					flaggedAsUnavailable	= true;
	private static final boolean 					available				= true;
	private static final Set<String> 				skills					= Set.of("Java","Angular");
	private static final Set<Language> 				languages				= Set.of(Language.builder().language(LANGUAGE.DUTCH).level(LEVEL.PROFICIENT).build());
	private static final LocalDate					lastAvailabilityCheck	= LocalDate.of(2022, 05, 20);
	private static final LocalDate					registered				= LocalDate.of(2021, 05, 20);
	private static final suggestion_accuracy 		accuracySkills			= suggestion_accuracy.perfect;
	private static final suggestion_accuracy 		accuracyLanguages		= suggestion_accuracy.poor; 
	private static final String						ownerId 				= "rec44";
	private static final SECURITY_CLEARANCE_TYPE 	SECURITY_CLEARANCE 		= SECURITY_CLEARANCE_TYPE.DV;
	private static final boolean					REQUIRES_SPONSORSHIP 	= true;
	
	/**
	* Tests construction via a Builder
	* @throws Exception
	*/
	@Test
	public void testBuilder() throws Exception{
		
		CandidateSuggestionAPIOutbound candidate =  CandidateSuggestionAPIOutbound
														.builder()
															.available(available)
															.candidateId(candidateId)
															.city(city)
															.country(country)
															.freelance(freelance)
															.function(function)
															.languages(languages)
															.flaggedAsUnavailable(flaggedAsUnavailable)
															.lastAvailabilityCheck(lastAvailabilityCheck)
															.perm(perm)
															.roleSought(roleSought)
															.skills(skills)
															.yearsExperience(yearsExperience)
															.firstname(firstname)
															.surname(surname)
															.email(email)
															.accuracyLanguages(accuracyLanguages)
															.accuracySkills(accuracySkills)
															.securityClearance(SECURITY_CLEARANCE)
															.requiresSponsorship(REQUIRES_SPONSORSHIP)
														.build();
		
		assertEquals(candidateId, 				candidate.getCandidateId());
		assertEquals(roleSought, 				candidate.getRoleSought());
		assertEquals(function, 					candidate.getFunction());
		assertEquals(country, 					candidate.getCountry());
		assertEquals(city, 						candidate.getCity());
		assertEquals(perm, 						candidate.getPerm());
		assertEquals(freelance, 				candidate.getFreelance());
		assertEquals(yearsExperience, 			candidate.getYearsExperience());
		assertEquals(available, 				candidate.isAvailable());
		assertEquals(flaggedAsUnavailable, 		candidate.isFlaggedAsUnavailable());
		assertEquals(lastAvailabilityCheck, 	candidate.getLastAvailabilityCheckOn());
		assertEquals(firstname, 				candidate.getFirstname());
		assertEquals(surname, 					candidate.getSurname());
		assertEquals(email, 					candidate.getEmail());
		assertEquals(accuracyLanguages, 		candidate.getAccuracyLanguages());
		assertEquals(accuracySkills, 			candidate.getAccuracySkills());
		assertEquals(SECURITY_CLEARANCE, 		candidate.getSecurityClearance());
		assertEquals(REQUIRES_SPONSORSHIP, 		candidate.getRequiresSponsorship());
		
		assertTrue( candidate.getSkills().contains("Java"));
		assertTrue(candidate.getSkills().contains("Angular"));
		
		candidate.getLanguages().stream().filter(l -> l.getLanguage() == LANGUAGE.DUTCH && l.getLevel() == LEVEL.PROFICIENT).findAny().orElseThrow();
		
	}
	
	/**
	* Tests that by default all collections exist but are empty. This is defensive programing to 
	* avoid unexpected nullPointerExceptions
	* @throws Exception
	*/
	@Test
	public void testBuilderDefaultCollections() throws Exception {
		
		CandidateSuggestionAPIOutbound candidate = CandidateSuggestionAPIOutbound.builder().build();
		
		assertTrue(candidate.getSkills().isEmpty());
		assertTrue(candidate.getLanguages().isEmpty());
		
	}
	
	/**
	* Tests convertsion from Domain representatin of Candidate to API Outbound
	* representation
	* @throws Exception
	*/
	@Test
	public void testConvertFromCandidate() throws Exception{
		
		Candidate candidate = Candidate
									.builder()
										.flaggedAsUnavailable(flaggedAsUnavailable)
										.available(available)
										.candidateId(candidateId)
										.city(city)
										.email(email)
										.country(country)
										.freelance(freelance)
										.function(function)
										.languages(languages)
										.lastAvailabilityCheck(lastAvailabilityCheck)
										.perm(perm)
										.roleSought(roleSought)
										.skills(skills)
										.yearsExperience(yearsExperience)
										.firstname(firstname)
										.registerd(registered)
										.surname(surname)
										.available(available)
										.ownerId(ownerId)
										.securityClearance(SECURITY_CLEARANCE)
										.requiresSponsorship(REQUIRES_SPONSORSHIP)
									.build();
		
		CandidateSearchAccuracyWrapper wrapper = new CandidateSearchAccuracyWrapper(candidate);
		
		wrapper.setAccuracyLanguages(accuracyLanguages);
		wrapper.setAccuracySkills(accuracySkills);
		
		CandidateSuggestionAPIOutbound candidateAPIOutbound = CandidateSuggestionAPIOutbound.convertFromCandidate(wrapper);
		
		assertEquals(candidateId, 				candidateAPIOutbound.getCandidateId());
		assertEquals(roleSought, 				candidateAPIOutbound.getRoleSought());
		assertEquals(function, 					candidateAPIOutbound.getFunction());
		assertEquals(country, 					candidateAPIOutbound.getCountry());
		assertEquals(city, 						candidateAPIOutbound.getCity());
		assertEquals(perm, 						candidateAPIOutbound.getPerm());
		assertEquals(freelance, 				candidateAPIOutbound.getFreelance());
		assertEquals(yearsExperience, 			candidateAPIOutbound.getYearsExperience());
		assertEquals(available, 				candidateAPIOutbound.isAvailable());
		assertEquals(flaggedAsUnavailable, 		candidateAPIOutbound.isFlaggedAsUnavailable());
		assertEquals(lastAvailabilityCheck, 	candidateAPIOutbound.getLastAvailabilityCheckOn());
		assertEquals(firstname, 				candidateAPIOutbound.getFirstname());
		assertEquals(surname, 					candidateAPIOutbound.getSurname());
		assertEquals(email, 					candidateAPIOutbound.getEmail());
		assertEquals(accuracyLanguages, 		candidateAPIOutbound.getAccuracyLanguages());
		assertEquals(accuracySkills, 			candidateAPIOutbound.getAccuracySkills());
		assertEquals(SECURITY_CLEARANCE, 		candidateAPIOutbound.getSecurityClearance());
		assertEquals(REQUIRES_SPONSORSHIP, 		candidateAPIOutbound.getRequiresSponsorship());
		
		assertTrue(candidateAPIOutbound.getSkills().isEmpty());
		
		candidateAPIOutbound.getLanguages().stream().filter(l -> l.getLanguage() == LANGUAGE.DUTCH && l.getLevel() == LEVEL.PROFICIENT).findAny().orElseThrow();
		
	}
	
	/**
	* Tests convertsion from Domain representatin of Candidate to API Outbound
	* representation
	* @throws Exception
	*/
	@Test
	public void testConvertFromCandidateAsCensored() throws Exception{
		
		Candidate candidate = Candidate
									.builder()
										.flaggedAsUnavailable(flaggedAsUnavailable)
										.available(available)
										.candidateId(candidateId)
										.city(city)
										.email(email)
										.country(country)
										.freelance(freelance)
										.function(function)
										.languages(languages)
										.lastAvailabilityCheck(lastAvailabilityCheck)
										.perm(perm)
										.roleSought(roleSought)
										.skills(skills)
										.yearsExperience(yearsExperience)
										.firstname(firstname)
										.registerd(registered)
										.surname(surname)
										.available(available)
										.ownerId(ownerId)
										.securityClearance(SECURITY_CLEARANCE)
										.requiresSponsorship(REQUIRES_SPONSORSHIP)
									.build();
		
		CandidateSearchAccuracyWrapper wrapper = new CandidateSearchAccuracyWrapper(candidate);
		
		wrapper.setAccuracyLanguages(accuracyLanguages);
		wrapper.setAccuracySkills(accuracySkills);
		
		CandidateSuggestionAPIOutbound candidateAPIOutbound = CandidateSuggestionAPIOutbound.convertFromCandidateAsCensored(wrapper);
		
		assertEquals(candidateId, 									candidateAPIOutbound.getCandidateId());
		assertEquals(roleSought, 									candidateAPIOutbound.getRoleSought());
		assertEquals(function, 										candidateAPIOutbound.getFunction());
		assertEquals(country, 										candidateAPIOutbound.getCountry());
		assertEquals(city, 											candidateAPIOutbound.getCity());
		assertEquals(perm, 											candidateAPIOutbound.getPerm());
		assertEquals(freelance, 									candidateAPIOutbound.getFreelance());
		assertEquals(yearsExperience, 								candidateAPIOutbound.getYearsExperience());
		assertEquals(available, 									candidateAPIOutbound.isAvailable());
		assertEquals(flaggedAsUnavailable, 							candidateAPIOutbound.isFlaggedAsUnavailable());
		assertEquals(lastAvailabilityCheck, 						candidateAPIOutbound.getLastAvailabilityCheckOn());
		assertEquals(firstname, 									candidateAPIOutbound.getFirstname());
		assertEquals(CandidateSuggestionAPIOutbound.CENSORED_ITEM, 	candidateAPIOutbound.getSurname());
		assertEquals(CandidateSuggestionAPIOutbound.CENSORED_ITEM, 	candidateAPIOutbound.getEmail());
		assertEquals(accuracyLanguages, 							candidateAPIOutbound.getAccuracyLanguages());
		assertEquals(accuracySkills, 								candidateAPIOutbound.getAccuracySkills());
		assertEquals(SECURITY_CLEARANCE, 							candidateAPIOutbound.getSecurityClearance());
		assertEquals(REQUIRES_SPONSORSHIP, 							candidateAPIOutbound.getRequiresSponsorship());
		
		assertTrue(candidateAPIOutbound.getSkills().isEmpty());
		
		candidateAPIOutbound.getLanguages().stream().filter(l -> l.getLanguage() == LANGUAGE.DUTCH && l.getLevel() == LEVEL.PROFICIENT).findAny().orElseThrow();
		
	}
	
	/**
	* Tests convertsion from Domain representatin of Candidate to API Outbound
	* representation
	* @throws Exception
	*/
	@Test
	public void testConvertFromCandidateAsCensoredForActiveCredits() throws Exception{
		
		Candidate candidate = Candidate
									.builder()
										.flaggedAsUnavailable(flaggedAsUnavailable)
										.available(available)
										.candidateId(candidateId)
										.city(city)
										.email(email)
										.country(country)
										.freelance(freelance)
										.function(function)
										.languages(languages)
										.lastAvailabilityCheck(lastAvailabilityCheck)
										.perm(perm)
										.roleSought(roleSought)
										.skills(skills)
										.yearsExperience(yearsExperience)
										.firstname(firstname)
										.registerd(registered)
										.surname(surname)
										.available(available)
										.ownerId(ownerId)
										.securityClearance(SECURITY_CLEARANCE)
										.requiresSponsorship(REQUIRES_SPONSORSHIP)
									.build();
		
		CandidateSearchAccuracyWrapper wrapper = new CandidateSearchAccuracyWrapper(candidate);
		
		wrapper.setAccuracyLanguages(accuracyLanguages);
		wrapper.setAccuracySkills(accuracySkills);
		
		CandidateSuggestionAPIOutbound candidateAPIOutbound = CandidateSuggestionAPIOutbound.convertFromCandidateAsCensoredForActiveCredits(wrapper);
		
		assertEquals(candidateId, 									candidateAPIOutbound.getCandidateId());
		assertEquals(roleSought, 									candidateAPIOutbound.getRoleSought());
		assertEquals(function, 										candidateAPIOutbound.getFunction());
		assertEquals(country, 										candidateAPIOutbound.getCountry());
		assertEquals(city, 											candidateAPIOutbound.getCity());
		assertEquals(perm, 											candidateAPIOutbound.getPerm());
		assertEquals(freelance, 									candidateAPIOutbound.getFreelance());
		assertEquals(yearsExperience, 								candidateAPIOutbound.getYearsExperience());
		assertEquals(available, 									candidateAPIOutbound.isAvailable());
		assertEquals(flaggedAsUnavailable, 							candidateAPIOutbound.isFlaggedAsUnavailable());
		assertEquals(lastAvailabilityCheck, 						candidateAPIOutbound.getLastAvailabilityCheckOn());
		assertEquals(firstname, 	candidateAPIOutbound.getFirstname());
		assertEquals(" ", 	candidateAPIOutbound.getSurname());
		assertEquals(email, 	candidateAPIOutbound.getEmail());
		assertEquals(accuracyLanguages, 							candidateAPIOutbound.getAccuracyLanguages());
		assertEquals(accuracySkills, 								candidateAPIOutbound.getAccuracySkills());
		assertEquals(SECURITY_CLEARANCE, 							candidateAPIOutbound.getSecurityClearance());
		assertEquals(REQUIRES_SPONSORSHIP, 							candidateAPIOutbound.getRequiresSponsorship());
		
		assertTrue(candidateAPIOutbound.getSkills().isEmpty());
		
		candidateAPIOutbound.getLanguages().stream().filter(l -> l.getLanguage() == LANGUAGE.DUTCH && l.getLevel() == LEVEL.PROFICIENT).findAny().orElseThrow();
		
	}
	
}