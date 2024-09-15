package com.arenella.recruit.candidates.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.Candidate.CANDIDATE_TYPE;
import com.arenella.recruit.candidates.beans.Candidate.DAYS_ON_SITE;
import com.arenella.recruit.candidates.beans.Candidate.Photo;
import com.arenella.recruit.candidates.beans.Candidate.Photo.PHOTO_FORMAT;
import com.arenella.recruit.candidates.beans.Candidate.Rate;
import com.arenella.recruit.candidates.beans.Candidate.Rate.CURRENCY;
import com.arenella.recruit.candidates.beans.Candidate.Rate.PERIOD;
import com.arenella.recruit.candidates.beans.Candidate.SECURITY_CLEARANCE_TYPE;
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

	private static final String 					CANDIDATE_ID 				= "Candidate1";
	private static final FUNCTION					FUNCTION_VAL				= FUNCTION.JAVA_DEV;
	private static final COUNTRY 					COUNTRY_VAL 				= COUNTRY.NETHERLANDS;
	private static final String 					CITY 						= "Den Haag";
	private static final String 					EMAIL						= "kparkings@gmail.com";
	private static final String 					ROLE_SOUGHT					= "Senior java Dev";
	private static final boolean 					AVAILABLE 					= true;
	private static final boolean 					FLAGGED_AS_UNAVAILABLE		= true;
	private static final FREELANCE 					FREELANCE_VAL 				= FREELANCE.TRUE;
	private static final PERM 						PERM_VAL 					= PERM.TRUE;
	private static final LocalDate 					LAST_AVAILABILITY_CHECK 	= LocalDate.of(1980, 12, 3);
	private static final LocalDate 					REGISTERED 					= LocalDate.of(2021, 02, 20);
	private static final int 						YEARS_EXPERIENCE 			= 21;
	private static final Set<String>				SKILLS						= new LinkedHashSet<>();
	private static final Set<Language>				LANGUAGES					= new LinkedHashSet<>();
	private static final String						SKILL						= "Java";
	private static final Language					LANGUAGE_VAL				= Language.builder().language(LANGUAGE.DUTCH).level(LEVEL.PROFICIENT).build();
	private static final String						INTRODUCTION				= "Candiates own intro";
	private static final Photo						PROFILE_PHOTO				= new Photo(new byte[] {}, PHOTO_FORMAT.jpeg);
	private static final String 					COMMENTS					= "aComment";
	private static final DAYS_ON_SITE				DAYS_ON_SITE_VAL			= DAYS_ON_SITE.ZERO;
	private static final Rate						RATE_CONTRACT 				= new Rate(CURRENCY.EUR, PERIOD.DAY, 80.50f, 99f);
	private static final Rate						RATE_PERM 					= new Rate(CURRENCY.EUR, PERIOD.YEAR, 25000f, 30000);
	private static final LocalDate 					AVAILABLE_FROM_DATE 		= LocalDate.of(2023, 7, 21);
	private static final LocalDate 					AVAILABIILTY_CHK_EMAIL_DATE = LocalDate.of(2024, 9, 21);
	private static final String						OWNER_ID					= "kparkings";
	private static final CANDIDATE_TYPE				CANDIDATE_TYPE_VAL			= CANDIDATE_TYPE.MARKETPLACE_CANDIDATE;
	private static final SECURITY_CLEARANCE_TYPE 	SECURITY_LEVEL 				= SECURITY_CLEARANCE_TYPE.NATO;
	private static final LocalDate 					LAST_REFRESH 				= LocalDate.of(2021, 8, 8);
	private static final LocalDate 					AVAILABIILTY_CHK_RESPONSE 	= LocalDate.of(2024, 9, 13);
	private static final UUID						AVAILABILITY_CHK_TOKEN_ID	= UUID.randomUUID();
	
	
	/**
	* Sets up test environment 
	*/
	public CandidateTest(){
		
		LANGUAGES.add(LANGUAGE_VAL);
		SKILLS.add(SKILL);
		
	}
	
	/**
	* Test Builder values used to initialize instance of the Candidate Class 
	*/
	@Test
	public void testInitializationFromBuilder() {
		
		Candidate candidate = Candidate
						.builder()
							.candidateId(CANDIDATE_ID)
							.function(FUNCTION_VAL)
							.country(COUNTRY_VAL)
							.city(CITY)
							.email(EMAIL)
							.roleSought(ROLE_SOUGHT)
							.available(AVAILABLE)
							.flaggedAsUnavailable(FLAGGED_AS_UNAVAILABLE)
							.freelance(FREELANCE_VAL)
							.perm(PERM_VAL)
							.lastAvailabilityCheck(LAST_AVAILABILITY_CHECK)
							.registerd(REGISTERED)
							.yearsExperience(YEARS_EXPERIENCE)
							.skills(SKILLS)
							.languages(LANGUAGES)
							.introduction(INTRODUCTION)
							.photo(PROFILE_PHOTO)
							.comments(COMMENTS)
							.daysOnSite(DAYS_ON_SITE_VAL)
							.rateContract(RATE_CONTRACT)
							.ratePerm(RATE_PERM)
							.availableFromDate(AVAILABLE_FROM_DATE)
							.ownerId(OWNER_ID)
							.candidateType(CANDIDATE_TYPE_VAL)
							.securityClearance(SECURITY_LEVEL)
							.lastAccountRefresh(LAST_REFRESH)
							.lastAvailabilityCheckEmailSent(AVAILABIILTY_CHK_EMAIL_DATE)
							.lastAvailabilityCheckIdSent(AVAILABILITY_CHK_TOKEN_ID)
							.lastAvailabilityCheckConfirmedOn(AVAILABIILTY_CHK_RESPONSE)
							.build();
		
		assertEquals(CANDIDATE_ID, 					candidate.getCandidateId());
		assertEquals(FUNCTION_VAL, 					candidate.getFunction());
		assertEquals(COUNTRY_VAL, 					candidate.getCountry());
		assertEquals(CITY, 							candidate.getCity());
		assertEquals(EMAIL, 						candidate.getEmail());
		assertEquals(ROLE_SOUGHT, 					candidate.getRoleSought());
		assertEquals(AVAILABLE, 					candidate.isAvailable());
		assertEquals(FLAGGED_AS_UNAVAILABLE, 		candidate.isFlaggedAsUnavailable());
		assertEquals(FREELANCE_VAL, 				candidate.isFreelance());
		assertEquals(PERM_VAL, 						candidate.isPerm());
		assertEquals(LAST_AVAILABILITY_CHECK, 		candidate.getLastAvailabilityCheckOn());
		assertEquals(REGISTERED, 					candidate.getRegisteredOn());
		assertEquals(YEARS_EXPERIENCE, 				candidate.getYearsExperience());
		assertEquals(PROFILE_PHOTO, 				candidate.getPhoto().get());
		
		assertEquals(INTRODUCTION, 					candidate.getIntroduction());
		
		assertEquals(DAYS_ON_SITE_VAL, 				candidate.getDaysOnSite());
		assertEquals(RATE_CONTRACT, 				candidate.getRateContract().get());
		assertEquals(RATE_PERM, 					candidate.getRatePerm().get());
		assertEquals(AVAILABLE_FROM_DATE, 			candidate.getAvailableFromDate());
		assertEquals(OWNER_ID, 						candidate.getOwnerId().get());
		assertEquals(CANDIDATE_TYPE_VAL, 			candidate.getCandidateType());
		assertEquals(SECURITY_LEVEL,				candidate.getSecurityClearance());
		assertEquals(LAST_REFRESH,					candidate.getLastAccountRefresh());
		assertEquals(AVAILABIILTY_CHK_EMAIL_DATE, 	candidate.getLastAvailabilityCheckEmailSent().get());
		assertEquals(AVAILABILITY_CHK_TOKEN_ID,		candidate.getLastAvailabilityCheckIdSent().get());
		assertEquals(AVAILABIILTY_CHK_RESPONSE,		candidate.getLastAvailabilityCheckConfirmedOn().get());
		
		assertTrue(candidate.getSkills().contains(SKILL));
		candidate.getLanguages().stream().filter(l -> l.getLanguage() == LANGUAGE_VAL.getLanguage()).findAny().orElseThrow();
		
	}
	
	/**
	* Tests correct values are made anonymous if the Candidate is 
	* no longer available. We want to see their info in the Status but 
	* we no longer need to know who they are
	* @throws Exception
	*/
	@Test
	public void testNoLongerAvailable() throws Exception {
		
		final String 	firstName 	= "Kevin";
		final String 	surname 	= "Parkings";
		final String 	email 		= "kparkings@gmail.com";
		final LocalDate	lastAvailabilityCheck = LocalDate.of(2000, 01, 01);
		
		Candidate candidate = Candidate
				.builder()
					.available(true)
					.firstname(firstName)
					.surname(surname)
					.email(email)
					.lastAvailabilityCheck(lastAvailabilityCheck)
				.build();
		
		assertTrue(candidate.isAvailable());
		
		assertEquals(firstName, 			candidate.getFirstname(), firstName);
		assertEquals(surname,	 			candidate.getSurname(), surname);
		assertEquals(email, 				candidate.getEmail(), email);
		assertEquals(lastAvailabilityCheck, candidate.getLastAvailabilityCheckOn());
		
		candidate.noLongerAvailable();
		
		assertFalse(candidate.isAvailable());
		assertNotEquals(lastAvailabilityCheck, candidate.getLastAvailabilityCheckOn());
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
	
	/**
	* Tests construction via builder with no values provided
	* @throws Exception
	*/
	@Test
	public void testBuilder_defaults() throws Exception {
		
		Candidate candidate = Candidate.builder().build();
		
		assertTrue(candidate.getSkills().isEmpty());
		assertTrue(candidate.getLanguages().isEmpty());
		assertEquals(0, candidate.getYearsExperience());
		assertNull(candidate.isFreelance());
		assertNull(candidate.isPerm());
		assertNull(candidate.getFunction());
		assertNull(candidate.getCountry());
		
		assertTrue(candidate.getRateContract().isEmpty());
		assertTrue(candidate.getRatePerm().isEmpty());
		assertTrue(candidate.getOwnerId().isEmpty());
		
		assertTrue(candidate.getLastAvailabilityCheckIdSent().isEmpty());
		assertTrue(candidate.getLastAvailabilityCheckConfirmedOn().isEmpty());
		
		assertEquals(candidate.getAvailableFromDate(), LocalDate.now()); //[KP] Small chance of failure if test run in exactly midnight
		
	}
	
	/**
	* If LastRefresh has not yet taken place needs to use registered date
	* @throws Exception
	*/
	@Test
	public void testLastRefrehIsAccountCreatedIfNotExists() throws Exception{
	
		Candidate candidate = Candidate.builder().registerd(REGISTERED).build();
		
		assertEquals(REGISTERED, candidate.getLastAccountRefresh());
		
		candidate = Candidate.builder().build();
		
		assertNotEquals(REGISTERED, candidate.getLastAccountRefresh());
		assertNotNull(candidate.getLastAccountRefresh());
		
	}
	
	/**
	* Tests setters
	* @throws Exception
	*/
	@Test
	public void testSetters() throws Exception{
		
		Candidate candidate = Candidate.builder().build();
		
		assertTrue(candidate.getLastAvailabilityCheckEmailSent().isEmpty());
		
		candidate.setCandidateType(CANDIDATE_TYPE_VAL);
		candidate.setOwnerId(OWNER_ID);
		candidate.setEmail(EMAIL);
		candidate.setLastAvailabilityCheckEmailSent(AVAILABIILTY_CHK_EMAIL_DATE);
		candidate.setLastAvailabilityCheckIdSent(AVAILABILITY_CHK_TOKEN_ID);
		candidate.setLastAvailabilityCheckConfirmedOn(AVAILABIILTY_CHK_RESPONSE);
		
		assertEquals(CANDIDATE_TYPE_VAL, 			candidate.getCandidateType());
		assertEquals(OWNER_ID, 						candidate.getOwnerId().get());
		assertEquals(EMAIL, 						candidate.getEmail());
		assertEquals(AVAILABIILTY_CHK_EMAIL_DATE, 	candidate.getLastAvailabilityCheckEmailSent().get());
		assertEquals(AVAILABILITY_CHK_TOKEN_ID,		candidate.getLastAvailabilityCheckIdSent().get());
		assertEquals(AVAILABIILTY_CHK_RESPONSE,		candidate.getLastAvailabilityCheckConfirmedOn().get());
		
	}
	
	/**
	* Tests updating of the last time the Candidates availability was checked
	* @throws Exception
	*/
	@Test
	public void testSetCandidateAvailabilityChecked() throws Exception {
		
		final LocalDate pastDate = LocalDate.of(2000, 10, 5);
		
		Candidate candidate = Candidate.builder().available(false).lastAvailabilityCheck(pastDate).build();
		
		assertEquals(candidate.getLastAvailabilityCheckOn(), pastDate);
		
		candidate.setCandidateAvailabilityChecked();
		
		assertNotEquals(candidate.getLastAvailabilityCheckOn(), pastDate);
		
		assertTrue(candidate.getLastAvailabilityCheckOn() instanceof LocalDate);
		assertTrue(candidate.isAvailable());
		
	}
	
}