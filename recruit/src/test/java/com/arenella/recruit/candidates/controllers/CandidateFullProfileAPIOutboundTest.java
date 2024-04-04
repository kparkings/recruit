package com.arenella.recruit.candidates.controllers;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.Candidate.CANDIDATE_TYPE;
import com.arenella.recruit.candidates.beans.Candidate.DAYS_ON_SITE;
import com.arenella.recruit.candidates.beans.Candidate.Rate;
import com.arenella.recruit.candidates.beans.Candidate.SECURITY_CLEARANCE_TYPE;
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

	private static final String 					CANDIDATE_ID 			= "Candidate1";
	private static final FUNCTION					FUNCTIONVAL				= FUNCTION.JAVA_DEV;
	private static final COUNTRY 					COUNTRYVAL 				= COUNTRY.NETHERLANDS;
	private static final String 					CITY 					= "Den Haag";
	private static final boolean 					AVAILABLE 				= true;
	private static final FREELANCE 					FREELANCEVAL 			= FREELANCE.TRUE;
	private static final PERM	 					PERMVAL 				= PERM.TRUE;
	private static final LocalDate 					LAST_AVAILABILITY_CHECK = LocalDate.of(1980, 12, 3);
	private static final int 						YEARS_EXPERIENCE 		= 21;
	private static final Set<String>				SKILLS					= new LinkedHashSet<>();
	private static final Set<Language>				LANGUAGES				= new LinkedHashSet<>();
	private static final String						SKILL					= "Java";
	private static final Language					LANGUAGEVAL				= Language.builder().language(LANGUAGE.DUTCH).level(LEVEL.PROFICIENT).build();
	private static final String						ROLE_SOUGHT				= "Java Dev";
	private static final String						FIRST_NAME				= "Kevin";
	private static final String						SURNAME					= "Parkings";
	private static final RateAPIOutbound			RATE_CONTRACT			= new RateAPIOutbound(CURRENCY.EUR, PERIOD.HOUR, 100, 200);
	private static final RateAPIOutbound			RATE_PERM				= new RateAPIOutbound(CURRENCY.GBP, PERIOD.YEAR, 10000, 20000);
	private static final Rate						RATE_CONTRACT_CAN		= new Rate(CURRENCY.EUR, PERIOD.HOUR, 200, 300);
	private static final Rate						RATE_PERM_CAN			= new Rate(CURRENCY.GBP, PERIOD.YEAR, 20000, 30000);
	private static final String						EMAIL				 	= "kparkings@gmail.com";
	private static final String 					COMMENTS				= "A comment";
	private static final DAYS_ON_SITE				DAYS_ON_SITE_VAL		= DAYS_ON_SITE.FOUR;
	private static final LocalDate 					AVAILABLE_FROM_DATE		= LocalDate.of(2023, 12, 3);
	private static final CANDIDATE_TYPE				CANDIDATE_TYPE_VAL		= CANDIDATE_TYPE.CANDIDATE;
	private static final String						OWNER_ID				= "rec33";
	private static final SECURITY_CLEARANCE_TYPE 	SECURITY_CLEARANCE 		= SECURITY_CLEARANCE_TYPE.DV;
	private static final boolean					REQUIRES_SPONSORSHIP 	= true;
	
	
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
							.comments(COMMENTS)
							.daysOnSite(DAYS_ON_SITE_VAL)
							.availableFromDate(AVAILABLE_FROM_DATE)
							.candidateType(CANDIDATE_TYPE_VAL)
							.ownerId(OWNER_ID)
							.securityClearance(SECURITY_CLEARANCE)
							.requiresSponsorship(REQUIRES_SPONSORSHIP)
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
		assertEquals(COMMENTS, 							candidate.getComments());
		assertEquals(DAYS_ON_SITE_VAL, 					candidate.getDaysOnSite());
		assertEquals(AVAILABLE_FROM_DATE, 				candidate.getAvailableFromDate());
		assertEquals(CANDIDATE_TYPE_VAL, 				candidate.getCandidateType());
		assertEquals(OWNER_ID,			 				candidate.getOwnerId());
		assertEquals(SECURITY_CLEARANCE, 				candidate.getSecurityClearance());
		assertEquals(REQUIRES_SPONSORSHIP, 				candidate.getRequiresSponsorship());
		
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
							.comments(COMMENTS)
							.daysOnSite(DAYS_ON_SITE_VAL)
							.availableFromDate(AVAILABLE_FROM_DATE)
							.candidateType(CANDIDATE_TYPE_VAL)
							.ownerId(OWNER_ID)
							.securityClearance(SECURITY_CLEARANCE)
							.requiresSponsorship(REQUIRES_SPONSORSHIP)
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
		assertEquals(COMMENTS, 					candidate.getComments());
		assertEquals(DAYS_ON_SITE_VAL, 			candidate.getDaysOnSite());
		assertEquals(AVAILABLE_FROM_DATE, 		candidate.getAvailableFromDate());
		assertEquals(CANDIDATE_TYPE_VAL, 		candidate.getCandidateType());
		assertEquals(OWNER_ID, 					candidate.getOwnerId().get());
		assertEquals(SECURITY_CLEARANCE, 		candidate.getSecurityClearance());
		assertEquals(REQUIRES_SPONSORSHIP, 		candidate.getRequiresSponsorship());
		
		
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
		assertEquals(COMMENTS, 								candidateProfile.getComments());
		assertEquals(DAYS_ON_SITE_VAL, 						candidateProfile.getDaysOnSite());
		assertEquals(AVAILABLE_FROM_DATE, 					candidateProfile.getAvailableFromDate());
		assertEquals(CANDIDATE_TYPE_VAL, 					candidateProfile.getCandidateType());
		assertEquals(OWNER_ID,			 					candidateProfile.getOwnerId());
		assertEquals(SECURITY_CLEARANCE, 					candidateProfile.getSecurityClearance());
		assertEquals(REQUIRES_SPONSORSHIP, 					candidateProfile.getRequiresSponsorship());
	}
	
	/**
	* Tests no exceptions thrown if null values present
	* @throws Exception
	*/
	@Test
	public void testCensored_null_values() throws Exception{
		
		Candidate candidate = Candidate.builder().build();
		
		CandidateFullProfileAPIOutbound.convertFromCandidateAsCensored(candidate);
	
	}
	
	/**
	* Null in equals null out
	* @throws Exception
	*/
	@Test
	public void testCensored() throws Exception{
		
		final String comments 			= "My name is " + FIRST_NAME + " " + SURNAME + " my email is " + EMAIL + " Number 0031 643 220 866" + " 1 years experience";
		final String commentsCensored 	= "My name is " + CandidateFullProfileAPIOutbound.CENSORED_ITEM + " " + CandidateFullProfileAPIOutbound.CENSORED_ITEM + " my email is " + CandidateFullProfileAPIOutbound.CENSORED_ITEM + " Number " + CandidateFullProfileAPIOutbound.CENSORED_ITEM + " " + CandidateFullProfileAPIOutbound.CENSORED_ITEM + " " + CandidateFullProfileAPIOutbound.CENSORED_ITEM + " " + CandidateFullProfileAPIOutbound.CENSORED_ITEM + " 1 years experience";
		final String intro 				= "My name is " + FIRST_NAME + " " + SURNAME + " my email is " + EMAIL + " Number 0031 643 220 866" + " 1 years experience";
		final String introCensored 		= "My name is " + CandidateFullProfileAPIOutbound.CENSORED_ITEM + " " + CandidateFullProfileAPIOutbound.CENSORED_ITEM + " my email is " + CandidateFullProfileAPIOutbound.CENSORED_ITEM + " Number " + CandidateFullProfileAPIOutbound.CENSORED_ITEM + " " + CandidateFullProfileAPIOutbound.CENSORED_ITEM + " " + CandidateFullProfileAPIOutbound.CENSORED_ITEM + " " + CandidateFullProfileAPIOutbound.CENSORED_ITEM + " 1 years experience";
		
		
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
					.comments(comments)
					.daysOnSite(DAYS_ON_SITE_VAL)
					.availableFromDate(AVAILABLE_FROM_DATE)
					.candidateType(CANDIDATE_TYPE_VAL)
					.ownerId(OWNER_ID)
					.introduction(intro)
					.securityClearance(SECURITY_CLEARANCE)
					.requiresSponsorship(REQUIRES_SPONSORSHIP)
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
		assertEquals(comments, 					candidate.getComments());
		assertEquals(intro, 					candidate.getIntroduction());
		assertEquals(DAYS_ON_SITE_VAL, 			candidate.getDaysOnSite());
		assertEquals(AVAILABLE_FROM_DATE, 		candidate.getAvailableFromDate());
		assertEquals(CANDIDATE_TYPE_VAL, 		candidate.getCandidateType());
		assertEquals(OWNER_ID, 					candidate.getOwnerId().get());
		assertEquals(SECURITY_CLEARANCE, 		candidate.getSecurityClearance());
		assertEquals(REQUIRES_SPONSORSHIP, 		candidate.getRequiresSponsorship());
		
		assertTrue(candidate.getSkills().contains(SKILL));
		candidate.getLanguages().stream().filter(l -> l.getLanguage() == LANGUAGEVAL.getLanguage()).findAny().orElseThrow();
		
		CandidateFullProfileAPIOutbound candidateProfile = CandidateFullProfileAPIOutbound.convertFromCandidateAsCensored(candidate);
		
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
		assertEquals(CandidateFullProfileAPIOutbound.CENSORED_ITEM,	candidateProfile.getFirstname());
		assertEquals(CandidateFullProfileAPIOutbound.CENSORED_ITEM, candidateProfile.getSurname());
		assertEquals(CandidateFullProfileAPIOutbound.CENSORED_ITEM, candidateProfile.getEmail());
		assertEquals(RATE_CONTRACT_CAN.getCurrency(), 		candidateProfile.getRateContract().get().getCurrency());
		assertEquals(RATE_CONTRACT_CAN.getPeriod(), 		candidateProfile.getRateContract().get().getPeriod());
		assertEquals(RATE_CONTRACT_CAN.getValueMin(), 		candidateProfile.getRateContract().get().getValueMin());
		assertEquals(RATE_CONTRACT_CAN.getValueMax(), 		candidateProfile.getRateContract().get().getValueMax());
		assertEquals(RATE_PERM_CAN.getCurrency(), 			candidateProfile.getRatePerm().get().getCurrency());
		assertEquals(RATE_PERM_CAN.getPeriod(), 			candidateProfile.getRatePerm().get().getPeriod());
		assertEquals(RATE_PERM_CAN.getValueMin(), 			candidateProfile.getRatePerm().get().getValueMin());
		assertEquals(RATE_PERM_CAN.getValueMax(), 			candidateProfile.getRatePerm().get().getValueMax());
		assertEquals(commentsCensored, 						candidateProfile.getComments());
		assertEquals(introCensored, 						candidateProfile.getIntroduction());
		assertEquals(DAYS_ON_SITE_VAL, 						candidateProfile.getDaysOnSite());
		assertEquals(AVAILABLE_FROM_DATE, 					candidateProfile.getAvailableFromDate());
		assertEquals(CANDIDATE_TYPE_VAL, 					candidateProfile.getCandidateType());
		assertEquals(OWNER_ID,			 					candidateProfile.getOwnerId());
		assertEquals(SECURITY_CLEARANCE, 					candidateProfile.getSecurityClearance());
		assertEquals(REQUIRES_SPONSORSHIP, 					candidateProfile.getRequiresSponsorship());
		
		
	}
	
}
