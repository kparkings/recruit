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
public class CandidateCreatedEventTest {

	private static final String 		candidateId 			= "Candidate1";
	private static final String 		firstname 				= "kevin";
	private static final String 		surname 				= "parkings";
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
	private static final String			skill					= "Java";
	private static final Language		language				= Language.builder().language(LANGUAGE.DUTCH).level(LEVEL.PROFICIENT).build();
	private static final Set<String>	skills					= Set.of(skill);
	private static final Set<Language>	languages				= Set.of(language);
	private static final Candidate 		candidate 				= Candidate
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
																			.firstname(firstname)
																			.surname(surname)
																			.build();
																
	/**
	* Tests construction via the builder
	* @throws Exception
	*/
	@Test
	public void testBuilder() throws Exception{
		
		CandidateCreatedEvent event = CandidateCreatedEvent.builder().candidate(candidate).build();
		
		assertEquals(candidateId, 		event.getCandidateId());
		assertEquals(roleSought, 		event.getRoleSought());
		assertEquals(function, 			event.getFunction());
		assertEquals(country,			event.getCountry());
		assertEquals(city, 				event.getCity());
		assertEquals(perm, 				event.isPerm());
		assertEquals(freelance, 		event.isFreelance());
		assertEquals(yearsExperience, 	event.getYearsExperience());
		assertEquals(firstname, 		event.getFirstname());
		assertEquals(surname, 			event.getSurname());
		assertEquals(email, 			event.getEmail());
		assertTrue(event.getSkills().contains(skill));
		event.getLanguages().stream().filter(l -> l.getLanguage() == language.getLanguage()).findAny().orElseThrow();
		
	}
	
	/**
	* Test Exception is thrown if attempt is made to update the candidateId once it has previously been set
	* @throws Excption
	*/
	@Test
	public void testAttemptToSetPreviouslySetCandidateIdViaCandiate() throws Exception{
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			CandidateCreatedEvent.builder().candidateId("aCandidateID").candidate(candidate).build();
		});
	}
	
}
