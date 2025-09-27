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
class CandidateSuggestionAPIOutboundTest {

	private static final String 					CANDIDATE_ID			= "999";
	private static final String 					FIRST_NAME				= "Kevin";
	private static final String 					SURNAME					= "Parkings";
	private static final String 					EMAIL					= "admin@arenella-ict.com";
	private static final String						ROLE_SOUGHT				= "Senior Java Developer";
	private static final FUNCTION					FUNCTION_VAL			= FUNCTION.JAVA_DEV;
	private static final COUNTRY 					COUNTRY_VAL				= COUNTRY.NETHERLANDS;
	private static final String 					CITY					= "Noordwijk";
	private static final PERM 						PERM_VAL				= PERM.FALSE;
	private static final FREELANCE 					FREELANCE_VAL			= FREELANCE.TRUE;
	private static final int						YEARS_EXPERIENCE		= 22;
	private static final boolean					FLAGGED_AS_UNAVAILABLE	= true;
	private static final boolean 					AVAILABLE				= true;
	private static final Set<String> 				SKILLS					= Set.of("Java","Angular");
	private static final Set<Language> 				LANGUAGES				= Set.of(Language.builder().language(LANGUAGE.DUTCH).level(LEVEL.PROFICIENT).build());
	private static final LocalDate					LAST_AVAILABILITY_CHK	= LocalDate.of(2022, 05, 20);
	private static final LocalDate					REGISTERED				= LocalDate.of(2021, 05, 20);
	private static final suggestion_accuracy 		ACCURACY_SKILLS			= suggestion_accuracy.perfect;
	private static final suggestion_accuracy 		ACCURACY_LANGUAGES		= suggestion_accuracy.poor; 
	private static final String						OWNER_ID 				= "rec44";
	private static final SECURITY_CLEARANCE_TYPE 	SECURITY_CLEARANCE 		= SECURITY_CLEARANCE_TYPE.DV;
	private static final boolean					REQUIRES_SPONSORSHIP 	= true;
	
	/**
	* Tests construction via a Builder
	* @throws Exception
	*/
	@Test
	void testBuilder() {
		
		CandidateSuggestionAPIOutbound candidate =  CandidateSuggestionAPIOutbound
														.builder()
															.available(AVAILABLE)
															.candidateId(CANDIDATE_ID)
															.city(CITY)
															.country(COUNTRY_VAL)
															.freelance(FREELANCE_VAL)
															.functions(Set.of(FUNCTION_VAL))
															.languages(LANGUAGES)
															.flaggedAsUnavailable(FLAGGED_AS_UNAVAILABLE)
															.lastAvailabilityCheck(LAST_AVAILABILITY_CHK)
															.perm(PERM_VAL)
															.roleSought(ROLE_SOUGHT)
															.skills(SKILLS)
															.yearsExperience(YEARS_EXPERIENCE)
															.firstname(FIRST_NAME)
															.surname(SURNAME)
															.email(EMAIL)
															.accuracyLanguages(ACCURACY_LANGUAGES)
															.accuracySkills(ACCURACY_SKILLS)
															.securityClearance(SECURITY_CLEARANCE)
															.requiresSponsorship(REQUIRES_SPONSORSHIP)
														.build();
		
		assertEquals(CANDIDATE_ID, 				candidate.getCandidateId());
		assertEquals(ROLE_SOUGHT, 				candidate.getRoleSought());
		assertEquals(FUNCTION_VAL, 				candidate.getFunctions().toArray()[0]);
		assertEquals(COUNTRY_VAL, 				candidate.getCountry());
		assertEquals(CITY, 						candidate.getCity());
		assertEquals(PERM_VAL, 					candidate.getPerm());
		assertEquals(FREELANCE_VAL, 			candidate.getFreelance());
		assertEquals(YEARS_EXPERIENCE, 			candidate.getYearsExperience());
		assertEquals(AVAILABLE, 				candidate.isAvailable());
		assertEquals(FLAGGED_AS_UNAVAILABLE, 	candidate.isFlaggedAsUnavailable());
		assertEquals(LAST_AVAILABILITY_CHK, 	candidate.getLastAvailabilityCheckOn());
		assertEquals(FIRST_NAME, 				candidate.getFirstname());
		assertEquals(SURNAME, 					candidate.getSurname());
		assertEquals(EMAIL, 					candidate.getEmail());
		assertEquals(ACCURACY_LANGUAGES, 		candidate.getAccuracyLanguages());
		assertEquals(ACCURACY_SKILLS, 			candidate.getAccuracySkills());
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
	void testBuilderDefaultCollections() {
		
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
	void testConvertFromCandidate() {
		
		Candidate candidate = Candidate
									.builder()
										.flaggedAsUnavailable(FLAGGED_AS_UNAVAILABLE)
										.available(AVAILABLE)
										.candidateId(CANDIDATE_ID)
										.city(CITY)
										.email(EMAIL)
										.country(COUNTRY_VAL)
										.freelance(FREELANCE_VAL)
										.functions(Set.of(FUNCTION_VAL))
										.languages(LANGUAGES)
										.lastAvailabilityCheck(LAST_AVAILABILITY_CHK)
										.perm(PERM_VAL)
										.roleSought(ROLE_SOUGHT)
										.skills(SKILLS)
										.yearsExperience(YEARS_EXPERIENCE)
										.firstname(FIRST_NAME)
										.registerd(REGISTERED)
										.surname(SURNAME)
										.available(AVAILABLE)
										.ownerId(OWNER_ID)
										.securityClearance(SECURITY_CLEARANCE)
										.requiresSponsorship(REQUIRES_SPONSORSHIP)
									.build();
		
		CandidateSearchAccuracyWrapper wrapper = new CandidateSearchAccuracyWrapper(candidate);
		
		wrapper.setAccuracyLanguages(ACCURACY_LANGUAGES);
		wrapper.setAccuracySkills(ACCURACY_SKILLS);
		
		CandidateSuggestionAPIOutbound candidateAPIOutbound = CandidateSuggestionAPIOutbound.convertFromCandidate(wrapper);
		
		assertEquals(CANDIDATE_ID, 				candidateAPIOutbound.getCandidateId());
		assertEquals(ROLE_SOUGHT, 				candidateAPIOutbound.getRoleSought());
		assertEquals(FUNCTION_VAL,				candidateAPIOutbound.getFunctions().toArray()[0]);
		assertEquals(COUNTRY_VAL,				candidateAPIOutbound.getCountry());
		assertEquals(CITY, 						candidateAPIOutbound.getCity());
		assertEquals(PERM_VAL,					candidateAPIOutbound.getPerm());
		assertEquals(FREELANCE_VAL,				candidateAPIOutbound.getFreelance());
		assertEquals(YEARS_EXPERIENCE, 			candidateAPIOutbound.getYearsExperience());
		assertEquals(AVAILABLE, 				candidateAPIOutbound.isAvailable());
		assertEquals(FLAGGED_AS_UNAVAILABLE,	candidateAPIOutbound.isFlaggedAsUnavailable());
		assertEquals(LAST_AVAILABILITY_CHK, 	candidateAPIOutbound.getLastAvailabilityCheckOn());
		assertEquals(FIRST_NAME, 				candidateAPIOutbound.getFirstname());
		assertEquals(SURNAME, 					candidateAPIOutbound.getSurname());
		assertEquals(EMAIL, 					candidateAPIOutbound.getEmail());
		assertEquals(ACCURACY_LANGUAGES, 		candidateAPIOutbound.getAccuracyLanguages());
		assertEquals(ACCURACY_SKILLS, 			candidateAPIOutbound.getAccuracySkills());
		assertEquals(SECURITY_CLEARANCE, 		candidateAPIOutbound.getSecurityClearance());
		assertEquals(REQUIRES_SPONSORSHIP, 		candidateAPIOutbound.getRequiresSponsorship());
		
		assertTrue(candidateAPIOutbound.getSkills().isEmpty());
		
		candidateAPIOutbound.getLanguages().stream().filter(l -> l.getLanguage() == LANGUAGE.DUTCH && l.getLevel() == LEVEL.PROFICIENT).findAny().orElseThrow();
		
	}
	
	/**
	* Tests conversion from Domain representatin of Candidate to API Outbound
	* representation
	* @throws Exception
	*/
	@Test
	void testConvertFromCandidateAsCensored() {
		
		Candidate candidate = Candidate
									.builder()
										.flaggedAsUnavailable(FLAGGED_AS_UNAVAILABLE)
										.available(AVAILABLE)
										.candidateId(CANDIDATE_ID)
										.city(CITY)
										.email(EMAIL)
										.country(COUNTRY_VAL)
										.freelance(FREELANCE_VAL)
										.functions(Set.of(FUNCTION_VAL))
										.languages(LANGUAGES)
										.lastAvailabilityCheck(LAST_AVAILABILITY_CHK)
										.perm(PERM_VAL)
										.roleSought(ROLE_SOUGHT)
										.skills(SKILLS)
										.yearsExperience(YEARS_EXPERIENCE)
										.firstname(FIRST_NAME)
										.registerd(REGISTERED)
										.surname(SURNAME)
										.available(AVAILABLE)
										.ownerId(OWNER_ID)
										.securityClearance(SECURITY_CLEARANCE)
										.requiresSponsorship(REQUIRES_SPONSORSHIP)
									.build();
		
		CandidateSearchAccuracyWrapper wrapper = new CandidateSearchAccuracyWrapper(candidate);
		
		wrapper.setAccuracyLanguages(ACCURACY_LANGUAGES);
		wrapper.setAccuracySkills(ACCURACY_SKILLS);
		
		CandidateSuggestionAPIOutbound candidateAPIOutbound = CandidateSuggestionAPIOutbound.convertFromCandidateAsCensored(wrapper);
		
		assertEquals(CANDIDATE_ID, 									candidateAPIOutbound.getCandidateId());
		assertEquals(ROLE_SOUGHT, 									candidateAPIOutbound.getRoleSought());
		assertEquals(FUNCTION_VAL,									candidateAPIOutbound.getFunctions().toArray()[0]);
		assertEquals(COUNTRY_VAL,									candidateAPIOutbound.getCountry());
		assertEquals(CITY, 											candidateAPIOutbound.getCity());
		assertEquals(PERM_VAL,										candidateAPIOutbound.getPerm());
		assertEquals(FREELANCE_VAL,									candidateAPIOutbound.getFreelance());
		assertEquals(YEARS_EXPERIENCE, 								candidateAPIOutbound.getYearsExperience());
		assertEquals(AVAILABLE, 									candidateAPIOutbound.isAvailable());
		assertEquals(FLAGGED_AS_UNAVAILABLE,						candidateAPIOutbound.isFlaggedAsUnavailable());
		assertEquals(LAST_AVAILABILITY_CHK, 						candidateAPIOutbound.getLastAvailabilityCheckOn());
		assertEquals(FIRST_NAME, 									candidateAPIOutbound.getFirstname());
		assertEquals(CandidateSuggestionAPIOutbound.CENSORED_ITEM, 	candidateAPIOutbound.getSurname());
		assertEquals(CandidateSuggestionAPIOutbound.CENSORED_ITEM, 	candidateAPIOutbound.getEmail());
		assertEquals(ACCURACY_LANGUAGES, 							candidateAPIOutbound.getAccuracyLanguages());
		assertEquals(ACCURACY_SKILLS, 								candidateAPIOutbound.getAccuracySkills());
		assertEquals(SECURITY_CLEARANCE, 							candidateAPIOutbound.getSecurityClearance());
		assertEquals(REQUIRES_SPONSORSHIP, 							candidateAPIOutbound.getRequiresSponsorship());
		
		assertTrue(candidateAPIOutbound.getSkills().isEmpty());
		
		candidateAPIOutbound.getLanguages().stream().filter(l -> l.getLanguage() == LANGUAGE.DUTCH && l.getLevel() == LEVEL.PROFICIENT).findAny().orElseThrow();
		
	}
	
	/**
	* Tests conversion from Domain representation of Candidate to API Outbound
	* representation
	* @throws Exception
	*/
	@Test
	void testConvertFromCandidateAsCensoredForActiveCredits() {
		
		Candidate candidate = Candidate
									.builder()
										.flaggedAsUnavailable(FLAGGED_AS_UNAVAILABLE)
										.available(AVAILABLE)
										.candidateId(CANDIDATE_ID)
										.city(CITY)
										.email(EMAIL)
										.country(COUNTRY_VAL)
										.freelance(FREELANCE_VAL)
										.functions(Set.of(FUNCTION_VAL))
										.languages(LANGUAGES)
										.lastAvailabilityCheck(LAST_AVAILABILITY_CHK)
										.perm(PERM_VAL)
										.roleSought(ROLE_SOUGHT)
										.skills(SKILLS)
										.yearsExperience(YEARS_EXPERIENCE)
										.firstname(FIRST_NAME)
										.registerd(REGISTERED)
										.surname(SURNAME)
										.available(AVAILABLE)
										.ownerId(OWNER_ID)
										.securityClearance(SECURITY_CLEARANCE)
										.requiresSponsorship(REQUIRES_SPONSORSHIP)
									.build();
		
		CandidateSearchAccuracyWrapper wrapper = new CandidateSearchAccuracyWrapper(candidate);
		
		wrapper.setAccuracyLanguages(ACCURACY_LANGUAGES);
		wrapper.setAccuracySkills(ACCURACY_SKILLS);
		
		CandidateSuggestionAPIOutbound candidateAPIOutbound = CandidateSuggestionAPIOutbound.convertFromCandidateAsCensoredForActiveCredits(wrapper);
		
		assertEquals(CANDIDATE_ID, 									candidateAPIOutbound.getCandidateId());
		assertEquals(ROLE_SOUGHT, 									candidateAPIOutbound.getRoleSought());
		assertEquals(FUNCTION_VAL, 									candidateAPIOutbound.getFunctions().toArray()[0]);
		assertEquals(COUNTRY_VAL,									candidateAPIOutbound.getCountry());
		assertEquals(CITY, 											candidateAPIOutbound.getCity());
		assertEquals(PERM_VAL, 										candidateAPIOutbound.getPerm());
		assertEquals(FREELANCE_VAL,									candidateAPIOutbound.getFreelance());
		assertEquals(YEARS_EXPERIENCE, 								candidateAPIOutbound.getYearsExperience());
		assertEquals(AVAILABLE, 									candidateAPIOutbound.isAvailable());
		assertEquals(FLAGGED_AS_UNAVAILABLE,						candidateAPIOutbound.isFlaggedAsUnavailable());
		assertEquals(LAST_AVAILABILITY_CHK, 						candidateAPIOutbound.getLastAvailabilityCheckOn());
		assertEquals(FIRST_NAME, 									candidateAPIOutbound.getFirstname());
		assertEquals(" ", 											candidateAPIOutbound.getSurname());
		assertEquals(EMAIL, 										candidateAPIOutbound.getEmail());
		assertEquals(ACCURACY_LANGUAGES, 							candidateAPIOutbound.getAccuracyLanguages());
		assertEquals(ACCURACY_SKILLS, 								candidateAPIOutbound.getAccuracySkills());
		assertEquals(SECURITY_CLEARANCE, 							candidateAPIOutbound.getSecurityClearance());
		assertEquals(REQUIRES_SPONSORSHIP, 							candidateAPIOutbound.getRequiresSponsorship());
		
		assertTrue(candidateAPIOutbound.getSkills().isEmpty());
		
		candidateAPIOutbound.getLanguages().stream().filter(l -> l.getLanguage() == LANGUAGE.DUTCH && l.getLevel() == LEVEL.PROFICIENT).findAny().orElseThrow();
		
	}
	
}