package com.arenella.recruit.candidates.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.Candidate.CANDIDATE_TYPE;
import com.arenella.recruit.candidates.beans.Candidate.DAYS_ON_SITE;
import com.arenella.recruit.candidates.beans.Candidate.Photo.PHOTO_FORMAT;
import com.arenella.recruit.candidates.beans.Candidate.Rate.CURRENCY;
import com.arenella.recruit.candidates.beans.Candidate.Rate.PERIOD;
import com.arenella.recruit.candidates.beans.Language.LANGUAGE;
import com.arenella.recruit.candidates.beans.Language.LEVEL;
import com.arenella.recruit.candidates.beans.Candidate.SECURITY_CLEARANCE_TYPE;
import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.enums.FREELANCE;
import com.arenella.recruit.candidates.enums.FUNCTION;
import com.arenella.recruit.candidates.enums.PERM;

/**
* Unit tests for the CandidateDocument class
* @author K Parkings
*/
public class CandidateDocumentTest {

	private static final long						CANDIDATE_ID			= 123;
	private static final String 					FIRSTNAME				= "Kevin";
	private static final String 					SURNAME					= "Parkings";
	private static final String 					EMAIL					= "kparkings@gmail.com";
	private static final String 					ROLE_SOUGHT				= "Senior Java Developer";
	private static final FUNCTION 					FUNCTION_VAL			= FUNCTION.JAVA_DEV;
	private static final COUNTRY 					COUNTRY_VAL				= COUNTRY.ITALY;
	private static final String 					CITY					= "Scalea";
	private static final PERM 						PERM_VAL				= PERM.FALSE;
	private static final FREELANCE 					FREELANCE_VAL			= FREELANCE.TRUE;
	private static final int						YEARS_EXPERIENCE		= 25;
	private static final boolean 					AVAILABLE				= true;
	private static final LocalDate 					REGISTERED				= LocalDate.of(2024,4,11);
	private static final LocalDate 					LAST_AVAILABILITY_CHECK = LocalDate.of(2024,4,10);
	private static final String 					INTRODUCTION			= "Im a Java Dev";
	private static final RateDocument 				RATE_CONTRACT			= new RateDocument(CURRENCY.EUR, PERIOD.HOUR, 110, 120);
	private static final RateDocument 				RATE_PERM				= new RateDocument(CURRENCY.EUR, PERIOD.YEAR, 200000, 250000);
	private static final LocalDate 					AVAILABLE_FROM_DATE		= LocalDate.of(2024,5,11);
	private static final String 					OWNER_ID				= "456";
	private static final CANDIDATE_TYPE				CANDIDATE_TYPE_VAL		= CANDIDATE_TYPE.MARKETPLACE_CANDIDATE;
	private static final String 					COMMENTS				= "boop";
	private static final DAYS_ON_SITE 				DAYS_ON_SITE_VAL		= DAYS_ON_SITE.ZERO;
    private static final PhotoDocument	 			PHOTO					= new PhotoDocument(new byte[] {}, PHOTO_FORMAT.jpeg);      
    private static final boolean 					REQUIRES_SPONSORSHIP	= true;
    private static final SECURITY_CLEARANCE_TYPE 	SECURITY_CLEARANCE		= SECURITY_CLEARANCE_TYPE.NONE;
    private static final Set<String> 				SKILLS					= Set.of("java","spring");
	private static final Set<LanguageDocument> 		LANGUAGES				= Set.of(new LanguageDocument(LANGUAGE.ITALIAN, LEVEL.PROFICIENT));
	
	/**
	* Tests construction
	* @throws Exception
	*/
	@Test
	public void testConstruction() throws Exception {
		
		CandidateDocument doc = 
				CandidateDocument
				.builder()
					.available(AVAILABLE)
					.availableFromDate(AVAILABLE_FROM_DATE)
					.candidateId(CANDIDATE_ID)
					.candidateType(CANDIDATE_TYPE_VAL)
					.city(CITY)
					.comments(COMMENTS)
					.country(COUNTRY_VAL)
					.daysOnSite(DAYS_ON_SITE_VAL)
					.email(EMAIL)
					.firstname(FIRSTNAME)
					.freelance(FREELANCE_VAL)
					.function(FUNCTION_VAL)
					.introduction(INTRODUCTION)
					.languages(LANGUAGES)
					.lastAvailabilityCheck(LAST_AVAILABILITY_CHECK)
					.ownerId(OWNER_ID)
					.perm(PERM_VAL)
					.photo(PHOTO)
					.rateContract(RATE_CONTRACT)
					.ratePerm(RATE_PERM)
					.registerd(REGISTERED)
					.requiresSponsorship(AVAILABLE)
					.roleSought(ROLE_SOUGHT)
					.securityClearance(SECURITY_CLEARANCE)
					.skills(SKILLS)
					.surname(SURNAME)
					.yearsExperience(YEARS_EXPERIENCE)
				.build();
		
		assertEquals(CANDIDATE_ID,					doc.getCandidateId());
		assertEquals(FIRSTNAME,						doc.getFirstname());
		assertEquals(SURNAME,						doc.getSurname());
		assertEquals(EMAIL,							doc.getEmail());
		assertEquals(ROLE_SOUGHT,					doc.getRoleSought());
		assertEquals(FUNCTION_VAL,					doc.getFunction());
		assertEquals(COUNTRY_VAL,					doc.getCountry());
		assertEquals(CITY,							doc.getCity());
		assertEquals(PERM_VAL,						doc.isPerm());
		assertEquals(FREELANCE_VAL,					doc.isFreelance());
		assertEquals(YEARS_EXPERIENCE,				doc.getYearsExperience());
		assertEquals(AVAILABLE,						doc.isAvailable());
		assertEquals(REGISTERED,					doc.getRegisteredOn());
		assertEquals(LAST_AVAILABILITY_CHECK,		doc.getLastAvailabilityCheckOn());
		assertEquals(INTRODUCTION,					doc.getIntroduction());
		assertEquals(RATE_CONTRACT,					doc.getRateContract().get());
		assertEquals(RATE_PERM,						doc.getRatePerm().get());
		assertEquals(AVAILABLE_FROM_DATE,			doc.getAvailableFromDate());
		assertEquals(OWNER_ID,						doc.getOwnerId().get());
		assertEquals(CANDIDATE_TYPE_VAL,			doc.candidateType());
		assertEquals(COMMENTS,						doc.getComments());
		assertEquals(DAYS_ON_SITE_VAL,				doc.getDaysOnSite());
		assertEquals(PHOTO,							doc.getPhoto().get());      
		assertEquals(REQUIRES_SPONSORSHIP,			doc.getRequiresSponsorship());
		assertEquals(SECURITY_CLEARANCE,			doc.getSecurityClearance());
		assertEquals(SKILLS,						doc.getSkills());
		assertEquals(LANGUAGES,						doc.getLanguages());
		
	}
	
	/**
	* Tests conversion from Persistence to domain representation
	* @throws Exception
	*/
	@Test
	public void testConvertToDomain() throws Exception{
		
		CandidateDocument doc = 
				CandidateDocument
				.builder()
					.available(AVAILABLE)
					.availableFromDate(AVAILABLE_FROM_DATE)
					.candidateId(CANDIDATE_ID)
					.candidateType(CANDIDATE_TYPE_VAL)
					.city(CITY)
					.comments(COMMENTS)
					.country(COUNTRY_VAL)
					.daysOnSite(DAYS_ON_SITE_VAL)
					.email(EMAIL)
					.firstname(FIRSTNAME)
					.freelance(FREELANCE_VAL)
					.function(FUNCTION_VAL)
					.introduction(INTRODUCTION)
					.languages(LANGUAGES)
					.lastAvailabilityCheck(LAST_AVAILABILITY_CHECK)
					.ownerId(OWNER_ID)
					.perm(PERM_VAL)
					.photo(PHOTO)
					.rateContract(RATE_CONTRACT)
					.ratePerm(RATE_PERM)
					.registerd(REGISTERED)
					.requiresSponsorship(AVAILABLE)
					.roleSought(ROLE_SOUGHT)
					.securityClearance(SECURITY_CLEARANCE)
					.skills(SKILLS)
					.surname(SURNAME)
					.yearsExperience(YEARS_EXPERIENCE)
				.build();
		
		Candidate candidate = CandidateDocument.convertFromDocument(doc);
		
		assertEquals(CANDIDATE_ID,					candidate.getCandidateId());
		assertEquals(FIRSTNAME,						candidate.getFirstname());
		assertEquals(SURNAME,						candidate.getSurname());
		assertEquals(EMAIL,							candidate.getEmail());
		assertEquals(ROLE_SOUGHT,					candidate.getRoleSought());
		assertEquals(FUNCTION_VAL,					candidate.getFunction());
		assertEquals(COUNTRY_VAL,					candidate.getCountry());
		assertEquals(CITY,							candidate.getCity());
		assertEquals(PERM_VAL,						candidate.isPerm());
		assertEquals(FREELANCE_VAL,					candidate.isFreelance());
		assertEquals(YEARS_EXPERIENCE,				candidate.getYearsExperience());
		assertEquals(AVAILABLE,						candidate.isAvailable());
		assertEquals(REGISTERED,					candidate.getRegisteredOn());
		assertEquals(LAST_AVAILABILITY_CHECK,		candidate.getLastAvailabilityCheckOn());
		assertEquals(INTRODUCTION,					candidate.getIntroduction());
		assertEquals(AVAILABLE_FROM_DATE,			candidate.getAvailableFromDate());
		assertEquals(OWNER_ID,						candidate.getOwnerId().get());
		assertEquals(CANDIDATE_TYPE_VAL,			candidate.candidateType());
		assertEquals(COMMENTS,						candidate.getComments());
		assertEquals(DAYS_ON_SITE_VAL,				candidate.getDaysOnSite());
		assertEquals(REQUIRES_SPONSORSHIP,			candidate.getRequiresSponsorship());
		assertEquals(SECURITY_CLEARANCE,			candidate.getSecurityClearance());
		assertEquals(SKILLS,						candidate.getSkills());
		
		assertEquals(PHOTO.getImageBytes(),			candidate.getPhoto().get().getImageBytes());
		assertEquals(PHOTO.getFormat(),				candidate.getPhoto().get().getFormat());
		
		assertEquals(RATE_CONTRACT.getCurrency(),	candidate.getRateContract().get().getCurrency());
		assertEquals(RATE_CONTRACT.getPeriod(),		candidate.getRateContract().get().getPeriod());
		assertEquals(RATE_CONTRACT.getValueMin(),	candidate.getRateContract().get().getValueMin());
		assertEquals(RATE_CONTRACT.getValueMax(),	candidate.getRateContract().get().getValueMax());
		
		assertEquals(RATE_PERM.getCurrency(),		candidate.getRatePerm().get().getCurrency());
		assertEquals(RATE_PERM.getPeriod(),			candidate.getRatePerm().get().getPeriod());
		assertEquals(RATE_PERM.getValueMin(),		candidate.getRatePerm().get().getValueMin());
		assertEquals(RATE_PERM.getValueMax(),		candidate.getRatePerm().get().getValueMax());
		
		assertEquals(LANGUAGE.ITALIAN,				candidate.getLanguages().stream().findFirst().get().getLanguage());
		assertEquals(LEVEL.PROFICIENT,				candidate.getLanguages().stream().findFirst().get().getLevel());
		
	}
	
	/**
	* Test conversion from domain to persistence representation
	* @throws Exception
	*/
	@Test
	public void testConvertFromDomain() throws Exception{
		
		Candidate candidate = 
				Candidate
				.builder()
					.available(AVAILABLE)
					.availableFromDate(AVAILABLE_FROM_DATE)
					.candidateId(String.valueOf(CANDIDATE_ID))
					.candidateType(CANDIDATE_TYPE_VAL)
					.city(CITY)
					.comments(COMMENTS)
					.country(COUNTRY_VAL)
					.daysOnSite(DAYS_ON_SITE_VAL)
					.email(EMAIL)
					.firstname(FIRSTNAME)
					.freelance(FREELANCE_VAL)
					.function(FUNCTION_VAL)
					.introduction(INTRODUCTION)
					.languages(LANGUAGES.stream().map(l -> LanguageDocument.convertToDomain(l)).collect(Collectors.toSet()))
					.lastAvailabilityCheck(LAST_AVAILABILITY_CHECK)
					.ownerId(OWNER_ID)
					.perm(PERM_VAL)
					.photo(PhotoDocument.convertToDomain(PHOTO))
					.rateContract(RateDocument.convertToDomain(RATE_CONTRACT))
					.ratePerm(RateDocument.convertToDomain(RATE_PERM))
					.registerd(REGISTERED)
					.requiresSponsorship(AVAILABLE)
					.roleSought(ROLE_SOUGHT)
					.securityClearance(SECURITY_CLEARANCE)
					.skills(SKILLS)
					.surname(SURNAME)
					.yearsExperience(YEARS_EXPERIENCE)
				.build();
		
		CandidateDocument doc = CandidateDocument.convertToDocument(candidate);
		
		assertEquals(CANDIDATE_ID,					doc.getCandidateId());
		assertEquals(FIRSTNAME,						doc.getFirstname());
		assertEquals(SURNAME,						doc.getSurname());
		assertEquals(EMAIL,							doc.getEmail());
		assertEquals(ROLE_SOUGHT,					doc.getRoleSought());
		assertEquals(FUNCTION_VAL,					doc.getFunction());
		assertEquals(COUNTRY_VAL,					doc.getCountry());
		assertEquals(CITY,							doc.getCity());
		assertEquals(PERM_VAL,						doc.isPerm());
		assertEquals(FREELANCE_VAL,					doc.isFreelance());
		assertEquals(YEARS_EXPERIENCE,				doc.getYearsExperience());
		assertEquals(AVAILABLE,						doc.isAvailable());
		assertEquals(REGISTERED,					doc.getRegisteredOn());
		assertEquals(LAST_AVAILABILITY_CHECK,		doc.getLastAvailabilityCheckOn());
		assertEquals(INTRODUCTION,					doc.getIntroduction());
		assertEquals(AVAILABLE_FROM_DATE,			doc.getAvailableFromDate());
		assertEquals(OWNER_ID,						doc.getOwnerId().get());
		assertEquals(CANDIDATE_TYPE_VAL,			doc.candidateType());
		assertEquals(COMMENTS,						doc.getComments());
		assertEquals(DAYS_ON_SITE_VAL,				doc.getDaysOnSite());
		assertEquals(REQUIRES_SPONSORSHIP,			doc.getRequiresSponsorship());
		assertEquals(SECURITY_CLEARANCE,			doc.getSecurityClearance());
		assertEquals(SKILLS,						doc.getSkills());
		
		assertEquals(PHOTO.getImageBytes(),			doc.getPhoto().get().getImageBytes());
		assertEquals(PHOTO.getFormat(),				doc.getPhoto().get().getFormat());
		
		assertEquals(RATE_CONTRACT.getCurrency(),	doc.getRateContract().get().getCurrency());
		assertEquals(RATE_CONTRACT.getPeriod(),		doc.getRateContract().get().getPeriod());
		assertEquals(RATE_CONTRACT.getValueMin(),	doc.getRateContract().get().getValueMin());
		assertEquals(RATE_CONTRACT.getValueMax(),	doc.getRateContract().get().getValueMax());
		
		assertEquals(RATE_PERM.getCurrency(),		doc.getRatePerm().get().getCurrency());
		assertEquals(RATE_PERM.getPeriod(),			doc.getRatePerm().get().getPeriod());
		assertEquals(RATE_PERM.getValueMin(),		doc.getRatePerm().get().getValueMin());
		assertEquals(RATE_PERM.getValueMax(),		doc.getRatePerm().get().getValueMax());
		
		assertEquals(LANGUAGE.ITALIAN,				doc.getLanguages().stream().findFirst().get().getLanguage());
		assertEquals(LEVEL.PROFICIENT,				doc.getLanguages().stream().findFirst().get().getLevel());
		
	}
	
}