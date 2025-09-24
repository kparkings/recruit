package com.arenella.recruit.candidates.adapters;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.Language;
import com.arenella.recruit.candidates.beans.Language.LANGUAGE;
import com.arenella.recruit.candidates.beans.Language.LEVEL;
import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.enums.FREELANCE;
import com.arenella.recruit.candidates.enums.FUNCTION;
import com.arenella.recruit.candidates.enums.PERM;

/**
* Unit tests for the CandidateCreatedEvent class
* @author K Parkings
*/
class CandidateCreatedEventTest {

	private static final String 		CANDIDATE_ID 			= "Candidate1";
	private static final String 		FIRST_NAME 				= "kevin";
	private static final String 		SURNAME 				= "parkings";
	private static final FUNCTION		FUNCTION_VAL			= FUNCTION.JAVA_DEV;
	private static final COUNTRY 		COUNTRY_VAL				= COUNTRY.NETHERLANDS;
	private static final String 		CITY 					= "Den Haag";
	private static final String 		EMAIL					= "admin@arenella-ict.com";
	private static final String 		ROLE_SOUGHT				= "Senior java Dev";
	private static final boolean 		AVAILABLE 				= true;
	private static final boolean 		FLAGGED_AS_UNAVAILABLE	= true;
	private static final FREELANCE 		FREELANCE_VAL 			= FREELANCE.TRUE;
	private static final PERM 			PERM_VAL 				= PERM.TRUE;
	private static final LocalDate 		LAST_AVAILABILITY_CHECK	= LocalDate.of(1980, 12, 3);
	private static final LocalDate 		REGISTERED 				= LocalDate.of(2021, 02, 20);
	private static final int 			YEARS_EXPERIENCE 		= 21;
	private static final String			SKILL					= "Java";
	private static final Language		LANGUAGE_VAL			= Language.builder().language(LANGUAGE.DUTCH).level(LEVEL.PROFICIENT).build();
	private static final Set<String>	SKILLS					= Set.of(SKILL);
	private static final Set<Language>	LANGUAGES				= Set.of(LANGUAGE_VAL);
	private static final Candidate 		CANDIDATE 				= Candidate
																		.builder()
																			.candidateId(CANDIDATE_ID)
																			.functions(Set.of(FUNCTION_VAL))
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
																			.firstname(FIRST_NAME)
																			.surname(SURNAME)
																			.build();
																
	/**
	* Tests construction via the builder
	* @throws Exception
	*/
	@Test
	void testBuilder() {
		
		CandidateCreatedEvent event = CandidateCreatedEvent.builder().candidate(CANDIDATE).build();
		
		assertEquals(CANDIDATE_ID, 		event.getCandidateId());
		assertEquals(ROLE_SOUGHT, 		event.getRoleSought());
		assertEquals(FUNCTION_VAL, 		event.getFunctions().toArray()[0]);
		assertEquals(COUNTRY_VAL,		event.getCountry());
		assertEquals(CITY, 				event.getCity());
		assertEquals(PERM_VAL,			event.isPerm());
		assertEquals(FREELANCE_VAL,		event.isFreelance());
		assertEquals(YEARS_EXPERIENCE, 	event.getYearsExperience());
		assertEquals(FIRST_NAME, 		event.getFirstname());
		assertEquals(SURNAME, 			event.getSurname());
		assertEquals(EMAIL, 			event.getEmail());
		assertTrue(event.getSkills().contains(SKILL));
		event.getLanguages().stream().filter(l -> l.getLanguage() == LANGUAGE_VAL.getLanguage()).findAny().orElseThrow();
		
	}
	
	/**
	* Test Exception is thrown if attempt is made to update the candidateId once it has previously been set
	* @throws Excption
	*/
	@Test
	void testAttemptToSetPreviouslySetCandidateIdViaCandiate() throws Exception{
		Assertions.assertThrows(IllegalArgumentException.class, () -> CandidateCreatedEvent.builder().candidateId("aCandidateID").candidate(CANDIDATE).build());
	}
	
}
