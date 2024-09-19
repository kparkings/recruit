package com.arenella.recruit.recruiters.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.arenella.recruit.recruiters.beans.OfferedCandidate.DAYS_ON_SITE;
import com.arenella.recruit.recruiters.beans.OfferedCandidate.LANGUAGE;
import com.arenella.recruit.recruiters.beans.OpenPosition.ContractType;
import com.arenella.recruit.recruiters.enums.COUNTRY;

/**
* Unit tests for the OfferedCandidate Class
* @author K Parkings
*/
public class OfferedCandidateTest {
	
	private static final UUID 			ID 						= UUID.randomUUID();
	private static final String 		RECRUITER_ID 			= "aRecruiterId";
	private static final String 		CANDIDATE_ROLE_TITLE 	= "Java Developer";
	private static final COUNTRY	 	COUNTRY_VAL				= COUNTRY.BELGIUM;
	private static final String			LOCATION 				= "Brussels";
	private static final ContractType 	CONTRACT_TYPE 			= ContractType.CONTRACT;
	private static final DAYS_ON_SITE	DAYS_ON_SITE_VAL 		= DAYS_ON_SITE.ZERO;
	private static final String	 		RENUMERATION 			= "500 Euros per Day";
	private static final LocalDate 		AVAILABLE_FROM 			= LocalDate.of(2023, 01, 01);
	private static final Set<String>	CORE_SKILLS				= Set.of("Java","Spring","Hibernate","Angular");
	private static final int			YEARS_EXPERIENCE 		= 20;
	private static final String 		DESCRIPTION 			= "Java Dev with Angular Experience";
	private static final Set<LANGUAGE>	SPOKEN_LANGUAGES		= Set.of(LANGUAGE.DUTCH, LANGUAGE.FRENCH);
	private static final String 		COMMENTS 				= "Candiadte preferes 32 hours per week over 4 days";
	private static final LocalDate		CREATED 				= LocalDate.of(2022, 9, 16);
	private static final boolean		ACTIVE					= false;
	
	/**
	* Tests construction via the Builder
	* @throws Exception
	*/
	@Test
	public void testBuilder() throws Exception{
		
		OfferedCandidate candidate = OfferedCandidate.builder()
				.availableFromDate(AVAILABLE_FROM)
				.candidateRoleTitle(CANDIDATE_ROLE_TITLE)
				.comments(COMMENTS)
				.contractType(CONTRACT_TYPE)
				.coreSkills(CORE_SKILLS)
				.country(COUNTRY_VAL)
				.created(CREATED)
				.daysOnSite(DAYS_ON_SITE_VAL)
				.description(DESCRIPTION)
				.id(ID)
				.location(LOCATION)
				.recruiterId(RECRUITER_ID)
				.renumeration(RENUMERATION)
				.spokenLanguages(SPOKEN_LANGUAGES)
				.yearsExperience(YEARS_EXPERIENCE)
				.active(ACTIVE)
				.build();
		
		assertEquals(ID, 					candidate.getId());
		assertEquals(RECRUITER_ID, 			candidate.getRecruiterId());
		assertEquals(CANDIDATE_ROLE_TITLE, 	candidate.getcandidateRoleTitle());
		assertEquals(COUNTRY_VAL,			candidate.getcountry());
		assertEquals(LOCATION, 				candidate.getlocation());
		assertEquals(CONTRACT_TYPE,			candidate.getcontractType());
		assertEquals(DAYS_ON_SITE_VAL, 		candidate.getDaysOnSite());
		assertEquals(RENUMERATION,			candidate.getrenumeration());
		assertEquals(AVAILABLE_FROM,		candidate.getavailableFromDate());
		assertEquals(CORE_SKILLS,			candidate.getcoreSkills());
		assertEquals(YEARS_EXPERIENCE,		candidate.getyearsExperience());
		assertEquals(DESCRIPTION,			candidate.getdescription());
		assertEquals(SPOKEN_LANGUAGES,		candidate.getspokenLanguages());
		assertEquals(COMMENTS,				candidate.getcomments());
		assertEquals(CREATED,				candidate.getCreated());
		
		assertFalse(candidate.isActive());
		
	}
	
	/**
	* Tests an Exception is thrown if an attempt is made to initialize the same
	* instance twice 
	*/
	@Test
	public void testInitalizeObject_alreadyInitialized() {
		
		OfferedCandidate candidate = OfferedCandidate.builder().build();
		
		candidate.initializeAsNewObject(RECRUITER_ID);
		
		Assertions.assertThrows(IllegalStateException.class, () -> 
			candidate.initializeAsNewObject(RECRUITER_ID)
		);
		
	}
	
	/**
	* Tests the setters
	*/
	@Test
	public void testSetters() throws Exception{
		
		OfferedCandidate candidate = OfferedCandidate.builder().build();
		
		assertTrue(candidate.isActive());
		
		candidate.setActive(false);
		
		assertFalse(candidate.isActive());
		
	}
	
	/**
	* Tests the successful initialization of an OfferedCandidate
	*/
	@Test
	public void testInitalizeObject() {
	
		OfferedCandidate candidate = OfferedCandidate.builder().build();
		
		assertNull(candidate.getId());
		assertNull(candidate.getRecruiterId());
		
		candidate.initializeAsNewObject(RECRUITER_ID);
		
		assertNotNull(candidate.getId());
		assertEquals(RECRUITER_ID, 			candidate.getRecruiterId());
		assertNotNull(candidate.getCreated());
		assertTrue(candidate.isActive());
			
	}
	
}
