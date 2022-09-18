package com.arenella.recruit.recruiters.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.recruiters.beans.OfferedCandidate.DAYS_ON_SITE;
import com.arenella.recruit.recruiters.beans.OfferedCandidate.LANGUAGES;
import com.arenella.recruit.recruiters.beans.OpenPosition.ContractType;
import com.arenella.recruit.recruiters.beans.OpenPosition.Country;

/**
* Unit tests for the OfferedCandidate Class
* @author K Parkings
*/
public class OfferedCandidateAPIInboundTest {
	
	private static final String 		RECRUITER_ID 			= "aRecruiterId";
	private static final String 		CANDIDATE_ROLE_TITLE 	= "Java Developer";
	private static final Country	 	COUNTRY 				= Country.BELGIUM;
	private static final String			LOCATION 				= "Brussels";
	private static final ContractType 	CONTRACT_TYPE 			= ContractType.CONTRACT;
	private static final DAYS_ON_SITE	DAYS_ON_SITE_VAL 		= DAYS_ON_SITE.ZERO;
	private static final String	 		RENUMERATION 			= "500 Euros per Day";
	private static final LocalDate 		AVAILABLE_FROM 			= LocalDate.of(2023, 01, 01);
	private static final Set<String>	CORE_SKILLS				= Set.of("Java","Spring","Hibernate","Angular");
	private static final int			YEARS_EXPERIENCE 		= 20;
	private static final String 		DESCRIPTION 			= "Java Dev with Angular Experience";
	private static final Set<LANGUAGES>	SPOKEN_LANGUAGES		= Set.of(LANGUAGES.DUTCH, LANGUAGES.FRENCH);
	private static final String 		COMMENTS 				= "Candiadte preferes 32 hours per week over 4 days";
	
	/**
	* Tests construction via the Builder
	* @throws Exception
	*/
	@Test
	public void testBuilder() throws Exception{
		
		OfferedCandidateAPIInbound candidate = OfferedCandidateAPIInbound.builder()
				.availableFromDate(AVAILABLE_FROM)
				.candidateRoleTitle(CANDIDATE_ROLE_TITLE)
				.comments(COMMENTS)
				.contractType(CONTRACT_TYPE)
				.coreSkills(CORE_SKILLS)
				.country(COUNTRY)
				.daysOnSite(DAYS_ON_SITE_VAL)
				.description(DESCRIPTION)
				.location(LOCATION)
				.recruiterId(RECRUITER_ID)
				.renumeration(RENUMERATION)
				.spokenLanguages(SPOKEN_LANGUAGES)
				.yearsExperience(YEARS_EXPERIENCE)
				.build();
		
		assertEquals(RECRUITER_ID, 			candidate.getrecruiterId());
		assertEquals(CANDIDATE_ROLE_TITLE, 	candidate.getcandidateRoleTitle());
		assertEquals(COUNTRY, 				candidate.getcountry());
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
		
	}
	
	/**
	* Tests conversion from API representation of the OfferedCandidate 
	* to the Domain version
	* @throws Exception
	*/
	@Test
	public void testConvertToDomain() throws Exception{
		
		OfferedCandidateAPIInbound candidate = OfferedCandidateAPIInbound.builder()
				.availableFromDate(AVAILABLE_FROM)
				.candidateRoleTitle(CANDIDATE_ROLE_TITLE)
				.comments(COMMENTS)
				.contractType(CONTRACT_TYPE)
				.coreSkills(CORE_SKILLS)
				.country(COUNTRY)
				.daysOnSite(DAYS_ON_SITE_VAL)
				.description(DESCRIPTION)
				.location(LOCATION)
				.recruiterId(RECRUITER_ID)
				.renumeration(RENUMERATION)
				.spokenLanguages(SPOKEN_LANGUAGES)
				.yearsExperience(YEARS_EXPERIENCE)
				.build();
		
		OfferedCandidate domain = OfferedCandidateAPIInbound.convertToDomain(candidate);
		
		assertEquals(RECRUITER_ID, 			domain.getRecruiterId());
		assertEquals(CANDIDATE_ROLE_TITLE, 	domain.getcandidateRoleTitle());
		assertEquals(COUNTRY, 				domain.getcountry());
		assertEquals(LOCATION, 				domain.getlocation());
		assertEquals(CONTRACT_TYPE,			domain.getcontractType());
		assertEquals(DAYS_ON_SITE_VAL, 		domain.getDaysOnSite());
		assertEquals(RENUMERATION,			domain.getrenumeration());
		assertEquals(AVAILABLE_FROM,		domain.getavailableFromDate());
		assertEquals(CORE_SKILLS,			domain.getcoreSkills());
		assertEquals(YEARS_EXPERIENCE,		domain.getyearsExperience());
		assertEquals(DESCRIPTION,			domain.getdescription());
		assertEquals(SPOKEN_LANGUAGES,		domain.getspokenLanguages());
		assertEquals(COMMENTS,				domain.getcomments());
		
		assertNull(domain.getid());
		assertNull(domain.getCreated());
		
	}
}