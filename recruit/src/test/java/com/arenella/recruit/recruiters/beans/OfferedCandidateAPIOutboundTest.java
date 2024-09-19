package com.arenella.recruit.recruiters.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.recruiters.beans.OfferedCandidate.DAYS_ON_SITE;
import com.arenella.recruit.recruiters.beans.OfferedCandidate.LANGUAGE;
import com.arenella.recruit.recruiters.beans.OfferedCandidateAPIOutbound.RecruiterDetails;
import com.arenella.recruit.recruiters.beans.OpenPosition.ContractType;
import com.arenella.recruit.recruiters.enums.COUNTRY;

/**
* Unit tests for the OfferedCandidateAPIOutbound class
* @author K Parkings
*/
public class OfferedCandidateAPIOutboundTest {
	
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
	
	/**
	* Tests construction via Builder
	* @throws Exception
	*/
	@Test
	public void testBuilder() throws Exception{

		final String 			recruiterId 			= "kparkings";
		final String 			recruiterName 			= "Kevin Parkings";
		final String 			companyName 			= "Arenella BV";
		final String 			email					= "kparkings@gmail.com";
		final UUID 				id						= UUID.randomUUID();
		final RecruiterDetails 	recruiter				= new RecruiterDetails(recruiterId, recruiterName, companyName, email);
		final String 			candidateRoleTitle		= "Java Dev";
		final COUNTRY	 		country					= COUNTRY.BELGIUM;
		final String			location				= "Brusells";
		final ContractType 		contractType			= ContractType.BOTH;
		final DAYS_ON_SITE		daysOnSite			 	= DAYS_ON_SITE.FIVE;
		final String	 		renumeration			= "100 euros p/d";
		final LocalDate 		availableFromDate		= LocalDate.of(2023, 3, 30);
		final Set<String>		coreSkills				= Set.of("JAVA");
		final int				yearsExperience			= 10;
		final String 			description				= "aDesc";
		final Set<LANGUAGE>		spokenLanguages			= Set.of(LANGUAGE.DUTCH);
		final String 			comments				= "A coment";
		final LocalDate			created					= LocalDate.of(2023, 4, 30);
		final boolean			viewed					= true;			
		
		
		OfferedCandidateAPIOutbound oc = 
				OfferedCandidateAPIOutbound
				.builder()
					.availableFromDate(availableFromDate)
					.candidateRoleTitle(candidateRoleTitle)
					.comments(comments)
					.contractType(contractType)
					.coreSkills(coreSkills)
					.country(country)
					.created(created)
					.daysOnSite(daysOnSite)
					.description(description)
					.id(id)
					.location(location)
					.recruiter(recruiter)
					.renumeration(renumeration)
					.spokenLanguages(spokenLanguages)
					.viewed(viewed)
					.yearsExperience(yearsExperience)
				.build();

		assertEquals(recruiterId, 			oc.getRecruiter().getRecruiterId());
		assertEquals(recruiterName, 		oc.getRecruiter().getRecruiterName());
		assertEquals(companyName, 			oc.getRecruiter().getCompanyName());
		assertEquals(email, 				oc.getRecruiter().getRecruiterEmail());
		assertEquals(id, 					oc.getId());
		assertEquals(recruiter, 			oc.getRecruiter());
		assertEquals(candidateRoleTitle, 	oc.getCandidateRoleTitle());
		assertEquals(country, 				oc.getCountry());
		assertEquals(location, 				oc.getLocation());
		assertEquals(contractType, 			oc.getContractType());
		assertEquals(daysOnSite, 			oc.getDaysOnSite());
		assertEquals(renumeration, 			oc.getRenumeration());
		assertEquals(availableFromDate, 	oc.getAvailableFromDate());
		assertEquals(coreSkills, 			oc.getCoreSkills());
		assertEquals(yearsExperience, 		oc.getYearsExperience());
		assertEquals(description, 			oc.getDescription());
		assertEquals(spokenLanguages, 		oc.getSpokenLanguages());
		assertEquals(comments, 				oc.getComments());
		assertEquals(created, 				oc.getCreated());
		assertEquals(viewed, 				oc.isViewed());			
		
	}
	
	/**
	* Tests conversion from domain to API Outbound
	* @throws Exception
	*/
	@Test
	public void testConvertFromDomain() throws Exception{
		
		
		final String recruiterId		= "kparkings";
		final String recruiterName		= "kevin parkings";	
		final String companyName		= "Arenella BV";
		final String email			 	= "kparkings@gmail.com";
		
		RecruiterDetails rd = new RecruiterDetails(recruiterId, recruiterName, companyName, email);
			
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
				.build();		
	
		Set<UUID> viewedByAuthenticatedUser = Set.of(ID);
		
		OfferedCandidateAPIOutbound out = OfferedCandidateAPIOutbound.convertFromDomain(candidate, rd, viewedByAuthenticatedUser);
		
		assertEquals(recruiterId, 			out.getRecruiter().getRecruiterId());
		assertEquals(recruiterName, 		out.getRecruiter().getRecruiterName());
		assertEquals(companyName, 			out.getRecruiter().getCompanyName());
		assertEquals(email, 				out.getRecruiter().getRecruiterEmail());
		assertEquals(ID, 					out.getId());
		assertEquals(rd,		 			out.getRecruiter());
		assertEquals(CANDIDATE_ROLE_TITLE, 	out.getCandidateRoleTitle());
		assertEquals(COUNTRY_VAL, 			out.getCountry());
		assertEquals(LOCATION, 				out.getLocation());
		assertEquals(CONTRACT_TYPE, 		out.getContractType());
		assertEquals(DAYS_ON_SITE_VAL, 		out.getDaysOnSite());
		assertEquals(RENUMERATION, 			out.getRenumeration());
		assertEquals(AVAILABLE_FROM		, 	out.getAvailableFromDate());
		assertEquals(CORE_SKILLS, 			out.getCoreSkills());
		assertEquals(YEARS_EXPERIENCE, 		out.getYearsExperience());
		assertEquals(DESCRIPTION, 			out.getDescription());
		assertEquals(SPOKEN_LANGUAGES, 		out.getSpokenLanguages());
		assertEquals(COMMENTS, 				out.getComments());
		assertEquals(CREATED, 				out.getCreated());
		assertTrue(out.isViewed());	
		
	}
	
}