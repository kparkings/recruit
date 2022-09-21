package com.arenella.recruit.recruiters.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.recruiters.beans.OfferedCandidate;
import com.arenella.recruit.recruiters.beans.OfferedCandidate.DAYS_ON_SITE;
import com.arenella.recruit.recruiters.beans.OfferedCandidate.LANGUAGE;
import com.arenella.recruit.recruiters.beans.OpenPosition.ContractType;
import com.arenella.recruit.recruiters.beans.OpenPosition.Country;

/**
* Unit tests for the OfferedCandidateEntity class 
* @author K Parkings
*/
public class OfferedCandidateEntityTest {

	private static final UUID 			ID						= UUID.randomUUID();
	private static final String 		RECRUITER_ID			= "aRecruiterId";
	private static final String 		CANDIDATE_ROLE_TITLE	= "aRoleTitle";
	private static final Country	 	COUNTRY					= Country.BELGIUM;
	private static final String			LOCATION				= "Brussels";
	private static final ContractType 	CONTRACT_TYPE		  	= ContractType.BOTH;
	private static final DAYS_ON_SITE	DAYS_ON_SITE_NUM		= DAYS_ON_SITE.ONE;
	private static final String	 		RENUMERATION			= "120 euros p/h";
	private static final LocalDate 		AVAILABLE_FROM_DATE		= LocalDate.of(2022, 9, 21);
	private static final Set<String>	CORE_SKILLS				= Set.of("Java","Spring");
	private static final int			YEARS_EXPERIENCE		= 23;
	private static final String 		DESCRIPTION				= "aDesc";
	private static final Set<LANGUAGE>	SPOKEN_LANGUAGES		= new HashSet<>();
	private static final String 		COMMENTS			 	= "Some additional commment from recruiter";
	private static final LocalDate		CREATED					= LocalDate.of(2022, 8, 15);
	
	/**
	* Tests construction via a Builder
	* @throws Exception
	*/
	@Test
	public void testOfferedCandidateEntity_builder() throws Exception{
		
		OfferedCandidateEntity entity = 
				OfferedCandidateEntity
				.builder()
					.availableFromDate(AVAILABLE_FROM_DATE)
					.candidateRoleTitle(CANDIDATE_ROLE_TITLE)
					.comments(COMMENTS)
					.contractType(CONTRACT_TYPE)
					.coreSkills(CORE_SKILLS)
					.country(COUNTRY)
					.created(CREATED)
					.daysOnSite(DAYS_ON_SITE_NUM)
					.description(DESCRIPTION)
					.id(ID)
					.location(LOCATION)
					.recruiterId(RECRUITER_ID)
					.renumeration(RENUMERATION)
					.spokenLanguages(SPOKEN_LANGUAGES)
					.yearsExperience(YEARS_EXPERIENCE)	
				.build();
		
		assertEquals(ID,					entity.getId());			
		assertEquals(RECRUITER_ID,			entity.getRecruiterId());
		assertEquals(CANDIDATE_ROLE_TITLE,	entity.getCandidateRoleTitle());
		assertEquals(COUNTRY,				entity.getCountry());
		assertEquals(LOCATION,				entity.getLocation());
		assertEquals(CONTRACT_TYPE,			entity.getContractType());
		assertEquals(DAYS_ON_SITE_NUM,		entity.getDaysOnSite());
		assertEquals(RENUMERATION,			entity.getRenumeration());
		assertEquals(AVAILABLE_FROM_DATE,	entity.getAvailableFromDate());
		assertEquals(CORE_SKILLS,			entity.getCoreSkills());
		assertEquals(YEARS_EXPERIENCE,		entity.getYearsExperience());
		assertEquals(DESCRIPTION,			entity.getDescription());
		assertEquals(SPOKEN_LANGUAGES,		entity.getSpokenLanguages());
		assertEquals(COMMENTS,				entity.getComments());
		assertEquals(CREATED,				entity.getCreated());
		
	}
	
	/**
	* Tests conversion of Domain representation to an Entity representation
	* @throws Exception
	*/
	@Test
	public void testConvertToEntity() throws Exception{
		
		OfferedCandidate candidate = OfferedCandidate.builder()
				.availableFromDate(AVAILABLE_FROM_DATE)
				.candidateRoleTitle(CANDIDATE_ROLE_TITLE)
				.comments(COMMENTS)
				.contractType(CONTRACT_TYPE)
				.coreSkills(CORE_SKILLS)
				.country(COUNTRY)
				.created(CREATED)
				.daysOnSite(DAYS_ON_SITE_NUM)
				.description(DESCRIPTION)
				.id(ID)
				.location(LOCATION)
				.recruiterId(RECRUITER_ID)
				.renumeration(RENUMERATION)
				.spokenLanguages(SPOKEN_LANGUAGES)
				.yearsExperience(YEARS_EXPERIENCE)
				.build();
		
		OfferedCandidateEntity.convertToEntity(candidate, Optional.empty());
		
		assertEquals(ID, 					candidate.getid());
		assertEquals(RECRUITER_ID, 			candidate.getRecruiterId());
		assertEquals(CANDIDATE_ROLE_TITLE, 	candidate.getcandidateRoleTitle());
		assertEquals(COUNTRY, 				candidate.getcountry());
		assertEquals(LOCATION, 				candidate.getlocation());
		assertEquals(CONTRACT_TYPE,			candidate.getcontractType());
		assertEquals(DAYS_ON_SITE_NUM, 		candidate.getDaysOnSite());
		assertEquals(RENUMERATION,			candidate.getrenumeration());
		assertEquals(AVAILABLE_FROM_DATE,	candidate.getavailableFromDate());
		assertEquals(CORE_SKILLS,			candidate.getcoreSkills());
		assertEquals(YEARS_EXPERIENCE,		candidate.getyearsExperience());
		assertEquals(DESCRIPTION,			candidate.getdescription());
		assertEquals(SPOKEN_LANGUAGES,		candidate.getspokenLanguages());
		assertEquals(COMMENTS,				candidate.getcomments());
		assertEquals(CREATED,				candidate.getCreated());
		
	}
	
	/**
	* Tests conversion of Domain representation to an Entity representation
	* where an existing version of the Entity exists
	* @throws Exception
	*/
	@Test
	public void testConvertToEntity_existingEntity() throws Exception{
		
		final UUID 	 	originalId				= UUID.randomUUID();
		final String 	originalRecruiterId 	= "orginalId";
		final LocalDate originalCreated 		= LocalDate.of(2022, 1, 1);
		
		OfferedCandidate candidate = OfferedCandidate.builder()
				.availableFromDate(AVAILABLE_FROM_DATE)
				.candidateRoleTitle(CANDIDATE_ROLE_TITLE)
				.comments(COMMENTS)
				.contractType(CONTRACT_TYPE)
				.coreSkills(CORE_SKILLS)
				.country(COUNTRY)
				.created(CREATED)
				.daysOnSite(DAYS_ON_SITE_NUM)
				.description(DESCRIPTION)
				.id(ID)
				.location(LOCATION)
				.recruiterId(RECRUITER_ID)
				.renumeration(RENUMERATION)
				.spokenLanguages(SPOKEN_LANGUAGES)
				.yearsExperience(YEARS_EXPERIENCE)
				.build();
		
		OfferedCandidateEntity originalEntity =
				OfferedCandidateEntity
					.builder()
						.id(originalId)
						.recruiterId(originalRecruiterId)
						.created(originalCreated)
					.build();
		
		OfferedCandidateEntity updated = OfferedCandidateEntity.convertToEntity(candidate, Optional.of(originalEntity));
		
		assertEquals(originalId, 			updated.getId());
		assertEquals(originalRecruiterId, 	updated.getRecruiterId());
		assertEquals(CANDIDATE_ROLE_TITLE, 	updated.getCandidateRoleTitle());
		assertEquals(COUNTRY, 				updated.getCountry());
		assertEquals(LOCATION, 				updated.getLocation());
		assertEquals(CONTRACT_TYPE,			updated.getContractType());
		assertEquals(DAYS_ON_SITE_NUM, 		updated.getDaysOnSite());
		assertEquals(RENUMERATION,			updated.getRenumeration());
		assertEquals(AVAILABLE_FROM_DATE,	updated.getAvailableFromDate());
		assertEquals(CORE_SKILLS,			updated.getCoreSkills());
		assertEquals(YEARS_EXPERIENCE,		updated.getYearsExperience());
		assertEquals(DESCRIPTION,			updated.getDescription());
		assertEquals(SPOKEN_LANGUAGES,		updated.getSpokenLanguages());
		assertEquals(COMMENTS,				updated.getComments());
		assertEquals(originalCreated,		updated.getCreated());
		
	}

	/**
	* Tests conversion of Entity representation to a Domain representation
	* @throws Exception
	*/
	@Test
	public void testConvertFromEntity() throws Exception{
		
		OfferedCandidateEntity entity = 
				OfferedCandidateEntity
				.builder()
					.availableFromDate(AVAILABLE_FROM_DATE)
					.candidateRoleTitle(CANDIDATE_ROLE_TITLE)
					.comments(COMMENTS)
					.contractType(CONTRACT_TYPE)
					.coreSkills(CORE_SKILLS)
					.country(COUNTRY)
					.created(CREATED)
					.daysOnSite(DAYS_ON_SITE_NUM)
					.description(DESCRIPTION)
					.id(ID)
					.location(LOCATION)
					.recruiterId(RECRUITER_ID)
					.renumeration(RENUMERATION)
					.spokenLanguages(SPOKEN_LANGUAGES)
					.yearsExperience(YEARS_EXPERIENCE)	
				.build();
		
		OfferedCandidate domain = OfferedCandidateEntity.convertFromEntity(entity); 
		
		assertEquals(ID, 					domain.getid());
		assertEquals(RECRUITER_ID, 			domain.getRecruiterId());
		assertEquals(CANDIDATE_ROLE_TITLE, 	domain.getcandidateRoleTitle());
		assertEquals(COUNTRY, 				domain.getcountry());
		assertEquals(LOCATION, 				domain.getlocation());
		assertEquals(CONTRACT_TYPE,			domain.getcontractType());
		assertEquals(DAYS_ON_SITE_NUM, 		domain.getDaysOnSite());
		assertEquals(RENUMERATION,			domain.getrenumeration());
		assertEquals(AVAILABLE_FROM_DATE,	domain.getavailableFromDate());
		assertEquals(CORE_SKILLS,			domain.getcoreSkills());
		assertEquals(YEARS_EXPERIENCE,		domain.getyearsExperience());
		assertEquals(DESCRIPTION,			domain.getdescription());
		assertEquals(SPOKEN_LANGUAGES,		domain.getspokenLanguages());
		assertEquals(COMMENTS,				domain.getcomments());
		assertEquals(CREATED,				domain.getCreated());
		
	}
	
}