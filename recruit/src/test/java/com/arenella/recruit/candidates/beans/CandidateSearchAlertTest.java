package com.arenella.recruit.candidates.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.enums.FUNCTION;

/**
* Unit tests for the CandidateSearchAlert class
* @author K Parkings
*/
public class CandidateSearchAlertTest {

	private static final UUID 				ALERT_ID 		= UUID.randomUUID();
	private static final String 			RECRUITER_ID 	= "kparkings";
	private static final String 			ALERT_NAME 		= "Alter name";
	private static final Set<COUNTRY> 		COUNTRIES 		= Set.of(COUNTRY.BELGIUM);
	private static final Set<FUNCTION> 		FUNCTIONS 		= Set.of(FUNCTION.ARCHITECT);
	private static final Optional<Boolean> 	FREELANCE 		= Optional.of(Boolean.TRUE);
	private static final Optional<Boolean> 	PERM			= Optional.of(Boolean.FALSE);
	private static final int 				YEARS_EXP_GTE 	= 1;
	private static final int				YEARS_EXP_LTE 	= 2;
	private static final Language.LEVEL 	DUTCH 			= Language.LEVEL.BASIC;
	private static final Language.LEVEL 	ENGLISH 		= Language.LEVEL.PROFICIENT;
	private static final Language.LEVEL 	FRENCH 			= Language.LEVEL.UNKNOWN;
	private static final String				SKILL_JAVA	  	= "java";
	private static final Set<String>		SKILLS 			= Set.of(SKILL_JAVA);
	
	/**
	* Tests construction via the Builder
	* @throws Exception
	*/
	@Test
	public void testBuilder() throws Exception{
		
		CandidateSearchAlert alert = 
				CandidateSearchAlert
					.builder()
						.alertId(ALERT_ID)
						.alertName(ALERT_NAME)
						.countries(COUNTRIES)
						.dutch(DUTCH)
						.english(ENGLISH)
						.french(FRENCH)
						.freelance(FREELANCE)
						.functions(FUNCTIONS)
						.perm(PERM)
						.recruiterId(RECRUITER_ID)
						.skills(SKILLS)
						.yearsExperienceGtEq(YEARS_EXP_GTE)
						.yearsExperienceLtEq(YEARS_EXP_LTE)
					.build();
	
		assertEquals(ALERT_ID, 			alert.getAlertId());
		assertEquals(ALERT_NAME, 		alert.getAlertName());
		assertEquals(DUTCH, 			alert.getDutch());
		assertEquals(ENGLISH, 			alert.getEnglish());
		assertEquals(FRENCH, 			alert.getFrench());
		assertEquals(FREELANCE.get(), 	alert.getFreelance().get());
		assertEquals(PERM.get(), 		alert.getPerm().get());
		assertEquals(RECRUITER_ID, 		alert.getRecruiterId());
		assertEquals(YEARS_EXP_GTE, 	alert.getYearsExperienceGtEq());
		assertEquals(YEARS_EXP_LTE, 	alert.getyearsExperienceLtEq());
		
		alert.getSkills().stream().filter(s -> s.equals(SKILL_JAVA)).findAny().orElseThrow();
		alert.getCountries().stream().filter(c -> c == COUNTRY.BELGIUM).findAny().orElseThrow();
		alert.getFunctions().stream().filter(f -> f == FUNCTION.ARCHITECT).findAny().orElseThrow();
	}
	
}