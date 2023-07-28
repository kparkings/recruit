package com.arenella.recruit.candidates.controllers;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.Candidate;
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
* Unit tests for the CandidateFullProfileAPIOutbound class
* @author K Parkings
*/
public class CandidateFullProfileAPIOutboundTest {

	private static final String 			CANDIDATE_ID 			= "Candidate1";
	private static final FUNCTION			FUNCTIONVAL				= FUNCTION.JAVA_DEV;
	private static final COUNTRY 			COUNTRYVAL 				= COUNTRY.NETHERLANDS;
	private static final String 			CITY 					= "Den Haag";
	private static final boolean 			AVAILABLE 				= true;
	private static final FREELANCE 			FREELANCEVAL 			= FREELANCE.TRUE;
	private static final PERM	 			PERMVAL 				= PERM.TRUE;
	private static final LocalDate 			LAST_AVAILABILITY_CHECK = LocalDate.of(1980, 12, 3);
	private static final int 				YEARS_EXPERIENCE 		= 21;
	private static final Set<String>		SKILLS					= new LinkedHashSet<>();
	private static final Set<Language>		LANGUAGES				= new LinkedHashSet<>();
	private static final String				SKILL					= "Java";
	private static final Language			LANGUAGEVAL				= Language.builder().language(LANGUAGE.DUTCH).level(LEVEL.PROFICIENT).build();
	private static final String				ROLE_SOUGHT				= "Java Dev";
	private static final String				FIRST_NAME				= "Kevin";
	private static final String				SURNAME					= "Parkings";
	private static final RateAPIOutbound	RATE_CONTRACT			= new RateAPIOutbound(CURRENCY.EUR, PERIOD.HOUR, 100, 200);
	private static final RateAPIOutbound	RATE_PERM				= new RateAPIOutbound(CURRENCY.GBP, PERIOD.YEAR, 10000, 20000);
	private static final Rate				RATE_CONTRACT_CAN		= new Rate(CURRENCY.EUR, PERIOD.HOUR, 200, 300);
	private static final Rate				RATE_PERM_CAN			= new Rate(CURRENCY.GBP, PERIOD.YEAR, 20000, 30000);
	private static final String				EMAIL				 	= "kparkings@gmail.com";
	
	/**
	* Sets up test environment 
	*/
	public CandidateFullProfileAPIOutboundTest(){
		
		LANGUAGES.add(LANGUAGEVAL);
		SKILLS.add(SKILL);
		
	}
	
	/**
	* Test Builder values used to initialize instance of the Candidate Class 
	*/
	@Test
	public void testInitializationFromBuilder() {
		
		CandidateFullProfileAPIOutbound candidate = CandidateFullProfileAPIOutbound
						.builder()
							.candidateId(CANDIDATE_ID)
							.function(FUNCTIONVAL)
							.country(COUNTRYVAL)
							.city(CITY)
							.available(AVAILABLE)
							.freelance(FREELANCEVAL)
							.perm(PERMVAL)
							.lastAvailabilityCheck(LAST_AVAILABILITY_CHECK)
							.yearsExperience(YEARS_EXPERIENCE)
							.skills(SKILLS)
							.languages(LANGUAGES)
							.roleSought(ROLE_SOUGHT)
							.firstname(FIRST_NAME)
							.surname(SURNAME)
							.rateContract(RATE_CONTRACT)
							.ratePerm(RATE_PERM)
							.email(EMAIL)
							.build();
		
		assertEquals(CANDIDATE_ID, 						candidate.getCandidateId());
		assertEquals(FUNCTIONVAL,						candidate.getFunction());
		assertEquals(COUNTRYVAL, 						candidate.getCountry());
		assertEquals(CITY, 								candidate.getCity());
		assertEquals(AVAILABLE, 						candidate.isAvailable());
		assertEquals(FREELANCEVAL, 						candidate.getFreelance());
		assertEquals(PERMVAL, 							candidate.getPerm());
		assertEquals(LAST_AVAILABILITY_CHECK, 			candidate.getLastAvailabilityCheckOn());
		assertEquals(YEARS_EXPERIENCE, 					candidate.getYearsExperience());
		assertEquals(ROLE_SOUGHT, 						candidate.getRoleSought());
		assertEquals(FIRST_NAME,	 					candidate.getFirstname());
		assertEquals(SURNAME, 							candidate.getSurname());
		assertEquals(RATE_CONTRACT, 					candidate.getRateContract().get());
		assertEquals(RATE_PERM, 						candidate.getRatePerm().get());
		assertEquals(EMAIL, 							candidate.getEmail());
		
		assertTrue(candidate.getSkills().contains(SKILL));
		candidate.getLanguages().stream().filter(l -> l.getLanguage() == LANGUAGEVAL.getLanguage()).findAny().orElseThrow();
		
	}
	
	/**
	* Test conversion from Domain representation
	*/
	@Test
	public void testConvertFromDomain() {
		
		Candidate candidate = Candidate
						.builder()
							.candidateId(CANDIDATE_ID)
							.function(FUNCTIONVAL)
							.country(COUNTRYVAL)
							.city(CITY)
							.available(AVAILABLE)
							.freelance(FREELANCEVAL)
							.perm(PERMVAL)
							.lastAvailabilityCheck(LAST_AVAILABILITY_CHECK)
							.yearsExperience(YEARS_EXPERIENCE)
							.skills(SKILLS)
							.languages(LANGUAGES)
							.roleSought(ROLE_SOUGHT)
							.firstname(FIRST_NAME)
							.surname(SURNAME)
							.rateContract(RATE_CONTRACT_CAN)
							.ratePerm(RATE_PERM_CAN)
							.email(EMAIL)
							.build();
		
		assertEquals(CANDIDATE_ID, 				candidate.getCandidateId());
		assertEquals(FUNCTIONVAL,				candidate.getFunction());
		assertEquals(COUNTRYVAL, 				candidate.getCountry());
		assertEquals(CITY, 						candidate.getCity());
		assertEquals(AVAILABLE, 				candidate.isAvailable());
		assertEquals(FREELANCEVAL, 				candidate.isFreelance());
		assertEquals(PERMVAL, 					candidate.isPerm());
		assertEquals(LAST_AVAILABILITY_CHECK, 	candidate.getLastAvailabilityCheckOn());
		assertEquals(YEARS_EXPERIENCE, 			candidate.getYearsExperience());
		assertEquals(ROLE_SOUGHT, 				candidate.getRoleSought());
		assertEquals(FIRST_NAME,	 			candidate.getFirstname());
		assertEquals(SURNAME, 					candidate.getSurname());
		assertEquals(SURNAME, 					candidate.getSurname());
		
		assertTrue(candidate.getSkills().contains(SKILL));
		candidate.getLanguages().stream().filter(l -> l.getLanguage() == LANGUAGEVAL.getLanguage()).findAny().orElseThrow();
		
		CandidateFullProfileAPIOutbound candidateProfile = CandidateFullProfileAPIOutbound.convertFromDomain(candidate);
		
		assertEquals(CANDIDATE_ID, 							candidateProfile.getCandidateId());
		assertEquals(FUNCTIONVAL,							candidateProfile.getFunction());
		assertEquals(COUNTRYVAL, 							candidateProfile.getCountry());
		assertEquals(CITY, 									candidateProfile.getCity());
		assertEquals(AVAILABLE, 							candidateProfile.isAvailable());
		assertEquals(FREELANCEVAL, 							candidateProfile.getFreelance());
		assertEquals(PERMVAL, 								candidateProfile.getPerm());
		assertEquals(LAST_AVAILABILITY_CHECK, 				candidateProfile.getLastAvailabilityCheckOn());
		assertEquals(YEARS_EXPERIENCE, 						candidateProfile.getYearsExperience());
		assertEquals(ROLE_SOUGHT, 							candidateProfile.getRoleSought());
		assertEquals(FIRST_NAME,	 						candidateProfile.getFirstname());
		assertEquals(SURNAME, 								candidateProfile.getSurname());
		assertEquals(SURNAME, 								candidateProfile.getSurname());
		assertEquals(RATE_CONTRACT_CAN.getCurrency(), 		candidateProfile.getRateContract().get().getCurrency());
		assertEquals(RATE_CONTRACT_CAN.getPeriod(), 		candidateProfile.getRateContract().get().getPeriod());
		assertEquals(RATE_CONTRACT_CAN.getValueMin(), 		candidateProfile.getRateContract().get().getValueMin());
		assertEquals(RATE_CONTRACT_CAN.getValueMax(), 		candidateProfile.getRateContract().get().getValueMax());
		assertEquals(RATE_PERM_CAN.getCurrency(), 			candidateProfile.getRatePerm().get().getCurrency());
		assertEquals(RATE_PERM_CAN.getPeriod(), 			candidateProfile.getRatePerm().get().getPeriod());
		assertEquals(RATE_PERM_CAN.getValueMin(), 			candidateProfile.getRatePerm().get().getValueMin());
		assertEquals(RATE_PERM_CAN.getValueMax(), 			candidateProfile.getRatePerm().get().getValueMax());
		
	}
	
}
