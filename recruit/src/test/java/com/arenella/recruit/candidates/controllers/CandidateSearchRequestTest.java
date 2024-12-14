package com.arenella.recruit.candidates.controllers;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.CandidateFilterOptions;
import com.arenella.recruit.candidates.beans.Language;
import com.arenella.recruit.candidates.beans.Language.LANGUAGE;
import com.arenella.recruit.candidates.beans.Language.LEVEL;
import com.arenella.recruit.candidates.controllers.CandidateSearchRequest.IncludeFilters;
import com.arenella.recruit.candidates.controllers.CandidateSearchRequest.LanguageFilters;
import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.enums.FREELANCE;
import com.arenella.recruit.candidates.enums.FUNCTION;
import com.arenella.recruit.candidates.enums.PERM;
import com.arenella.recruit.candidates.enums.RESULT_ORDER;
import com.arenella.recruit.candidates.utils.GeoZoneSearchUtil.GEO_ZONE;

import static com.arenella.recruit.candidates.controllers.CandidateSearchRequest.ContractFilters;
import static com.arenella.recruit.candidates.controllers.CandidateSearchRequest.ExperienceFilters;
import static com.arenella.recruit.candidates.controllers.CandidateSearchRequest.LocationFilters;
import static com.arenella.recruit.candidates.controllers.CandidateSearchRequest.RequestFilters;
import static com.arenella.recruit.candidates.controllers.CandidateSearchRequest.SkillFilters;
import static com.arenella.recruit.candidates.controllers.CandidateSearchRequest.TermFilters;
import static com.arenella.recruit.candidates.controllers.CandidateSearchRequest.LocationFilters.LocationRangeFilters;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
/**
* Unit tests for the CandidateSearchRequest class
*/
class CandidateSearchRequestTest {

	private static final FREELANCE 				CONTRACT 								= FREELANCE.TRUE;
	private static final PERM 					PERM_VAL 								= PERM.FALSE;
	private static final int 					MIN_EXPERIENCE 							= 2;
	private static final int 					MAX_EXPERIENCE 							= 5;
	private static final boolean 				INCLUDE_REQUIRES_SPONSORSHIP 			= true;
	private static final boolean 				INCLUDE_UNAVAILABLE 					= false;
	private static final Language				LANG_NL									= Language.builder().language(LANGUAGE.DUTCH).level(LEVEL.BASIC).build();
	private static final Language				LANG_IT									= Language.builder().language(LANGUAGE.ITALIAN).level(LEVEL.PROFICIENT).build();
	private static final COUNTRY				NETHERLANDS							 	= COUNTRY.NETHERLANDS;
	private static final COUNTRY				ITALY								 	= COUNTRY.ITALY;
	private static final GEO_ZONE				EUROPE									= GEO_ZONE.EUROPE;
	private static final GEO_ZONE				BENELUX									= GEO_ZONE.BENELUX;
	
	private static final COUNTRY				RANGE_COUNTRY_IT						= COUNTRY.ITALY;
	private static final String					RANGE_CITY_COSENZA						= "COSENZA";
	private static final int					RANGE_DISANCE_KM						= 150;
	private static final int					BACKEND_REQUEST_ID						= 1256;
	private static final int					MAX_SUGGESTIONS							= 156;
	private static final boolean				UNFILTERED							 	= false;
	
	private static final String					SKILL_JAVA							 	= "JAVA";
	private static final String					SKILL_CSHARP							= "C#";
	
	private static final String					TERM_TITLE								= "Java";
	private static final String					TERM_CANDIDATE_ID						= "1254";
	private static final String					TERM_EMAIL								= "kparkings@gmail.com";
	private static final String					TERM_FIRSTNAME							= "Kevin";
	private static final String					TERM_SURNAME							= "Parkings";
	
	
	/**
	* Tests construction via the Builder 
	*/
	@Test
	void testCandidateSearchRequest() {
		
		CandidateSearchRequest csr = CandidateSearchRequest
				.builder()
					.contractFilters(ContractFilters
								.builder()
									.contract(CONTRACT)
									.perm(PERM_VAL)
								.build())
					.experienceFilters(new ExperienceFilters(MIN_EXPERIENCE,MAX_EXPERIENCE))
					.includeFilters(IncludeFilters
							.builder()
								.includeRequiresSponsorshipCandidates(INCLUDE_REQUIRES_SPONSORSHIP)
								.includeUnavailableCandidates(INCLUDE_UNAVAILABLE)
							.build())
					.languageFilters(new LanguageFilters(Set.of(LANG_NL, LANG_IT)))
					.locationFilters(LocationFilters
							.builder()
								.countries(Set.of(NETHERLANDS, ITALY))
								.geoZones(Set.of(EUROPE, BENELUX))
								.locationFilters(new LocationRangeFilters(RANGE_COUNTRY_IT, RANGE_CITY_COSENZA, RANGE_DISANCE_KM))
							.build())
					.requestFilters(RequestFilters
							.builder()
								.backendRequestId(BACKEND_REQUEST_ID)
								.maxNumberOfSuggestions(MAX_SUGGESTIONS)
								.unfiltered(UNFILTERED)
							.build())
					.skillFilters(new SkillFilters(Set.of(SKILL_JAVA, SKILL_CSHARP)))
					.termFilters(TermFilters
							.builder()
								.candidateId(TERM_CANDIDATE_ID)
								.email(TERM_EMAIL)
								.title(TERM_TITLE)
								.firstname(TERM_FIRSTNAME)
								.surname(TERM_SURNAME)
							.build())
				.build();
		
		assertTrue(csr.contractFilters().isPresent());
		assertTrue(csr.experienceFilters().isPresent());
		assertTrue(csr.includeFilters().isPresent());
		assertTrue(csr.languageFilters().isPresent());
		assertTrue(csr.locationFilters().isPresent());
		assertTrue(csr.requestFilters().isPresent());
		assertTrue(csr.skillFilters().isPresent());
		assertTrue(csr.termFilters().isPresent());
		
		csr.contractFilters().ifPresent(f -> {
			assertEquals(CONTRACT, f.getContract().get());
			assertEquals(PERM_VAL, f.getPerm().get());
		});
		
		csr.experienceFilters().ifPresent(f -> {
			assertEquals(MIN_EXPERIENCE, f.getExperienceMin().get().intValue());
			assertEquals(MAX_EXPERIENCE, f.getExperienceMax().get().intValue());
		});
		
		csr.includeFilters().ifPresent(f -> {
			f.includeRequiresSponsorshipCandidates().ifPresent( inc -> {
				assertTrue(inc.booleanValue());			
			});
			f.includeUnavailableCandidates().ifPresent( inc -> {
				assertFalse(inc.booleanValue());			
			});
		});
		
		csr.languageFilters().ifPresent(f -> {
			assertEquals(2, f.getLanguages().size());
			assertTrue(f.getLanguages().stream().filter(l -> l.getLanguage() == LANG_NL.getLanguage()).findAny().isPresent());
			assertTrue(f.getLanguages().stream().filter(l -> l.getLanguage() == LANG_IT.getLanguage()).findAny().isPresent());
		});

		csr.locationFilters().ifPresent(f -> {
			f.getCountries();
			f.getGeoZones();
			f.getLocationFilters();
		});
		
		csr.requestFilters().ifPresent(f -> {
			f.getBackendRequestId().ifPresent(id 			-> assertEquals(BACKEND_REQUEST_ID, id.longValue()));
			f.getMaxNumberOfSuggestions().ifPresent(maxNum 	-> assertEquals(MAX_SUGGESTIONS, maxNum.longValue()));
			f.getUnfiltered().ifPresent(unfiltered 			-> assertEquals(UNFILTERED, unfiltered.booleanValue()));
		});
		
		csr.skillFilters().ifPresent(f -> {
			assertEquals(2, f.getSkills().size());
			assertTrue(f.getSkills().contains(SKILL_JAVA));
			assertTrue(f.getSkills().contains(SKILL_CSHARP));
			
		});
		
		csr.termFilters().ifPresent(f -> {
			f.getCandidateId().ifPresent(id 		-> assertEquals(TERM_CANDIDATE_ID, id));
			f.getEmail().ifPresent(email 			-> assertEquals(TERM_EMAIL, email));
			f.getFirstName().ifPresent(firstName 	-> assertEquals(TERM_FIRSTNAME, firstName));
			f.getSurname().ifPresent(surname 		-> assertEquals(TERM_SURNAME, surname));
			f.getTitle().ifPresent(title 			-> assertEquals(TERM_TITLE, title));
			
		});
	}
	
	/**
	* Tests case where filters are not added and to ensure 
	* default structures exist so no null pointer exceptions
	* can arise
	*/
	@Test
	void testCandidateSearchRequestWithDefaultFilters() {
		
		CandidateSearchRequest csr = CandidateSearchRequest
				.builder()
				.build();
	
		assertFalse(csr.contractFilters().isPresent());
		assertFalse(csr.experienceFilters().isPresent());
		assertFalse(csr.includeFilters().isPresent());
		assertFalse(csr.languageFilters().isPresent());
		assertFalse(csr.locationFilters().isPresent());
		assertFalse(csr.requestFilters().isPresent());
		assertFalse(csr.skillFilters().isPresent());
		assertFalse(csr.termFilters().isPresent());
		
		csr = CandidateSearchRequest
				.builder()
					.contractFilters(ContractFilters.builder().build())
					.experienceFilters(new ExperienceFilters(null, null))
					.includeFilters(IncludeFilters.builder().build())
					.languageFilters(new LanguageFilters(null))
					.locationFilters(LocationFilters.builder().build())
					.requestFilters(RequestFilters.builder().build())
					.skillFilters(new SkillFilters(null))
					.termFilters(TermFilters.builder().build())
				.build();
		
		csr.contractFilters().ifPresent(f -> {
			assertTrue(f.getContract().isEmpty());
			assertTrue(f.getPerm().isEmpty());
		});
		
		csr.experienceFilters().ifPresent(f -> {
			assertTrue(f.getExperienceMin().isEmpty());
			assertTrue(f.getExperienceMax().isEmpty());
		});
		
		csr.includeFilters().ifPresent(f -> {
			assertTrue(f.includeRequiresSponsorshipCandidates().isEmpty());
			assertTrue(f.includeUnavailableCandidates().isEmpty());
		});
		
		csr.languageFilters().ifPresent(f -> {
			assertTrue(f.getLanguages().isEmpty());
		});

		csr.locationFilters().ifPresent(f -> {
			assertTrue(f.getCountries().isEmpty());
			assertTrue(f.getGeoZones().isEmpty());
			assertTrue(f.getLocationFilters().isEmpty());
		});
		
		csr.requestFilters().ifPresent(f -> {
			assertTrue(f.getBackendRequestId().isEmpty());
			assertTrue(f.getMaxNumberOfSuggestions().isEmpty());
			assertTrue(f.getUnfiltered().isEmpty());
		});
		
		csr.skillFilters().ifPresent(f -> {
			assertTrue(f.getSkills().isEmpty());
		});
		
		csr.termFilters().ifPresent(f -> {
			assertTrue(f.getCandidateId().isEmpty());
			assertTrue(f.getEmail().isEmpty());
			assertTrue(f.getFirstName().isEmpty());
			assertTrue(f.getSurname().isEmpty());
			assertTrue(f.getTitle().isEmpty());
		});
		
	}
	
	/**
	* Tests conversion from API Inbound version to internal
	* Filters representation of search request 
	*/
	@Test
	void testConvertToCandidateFilterOptions() {
		
		final String ownerId = "123";
		final String orderAttribute = "candidateId";
		
		CandidateSearchRequest csr = CandidateSearchRequest
				.builder()
					.contractFilters(ContractFilters
								.builder()
									.contract(CONTRACT)
									.perm(PERM_VAL)
								.build())
					.experienceFilters(new ExperienceFilters(MIN_EXPERIENCE,MAX_EXPERIENCE))
					.includeFilters(IncludeFilters
							.builder()
								.includeRequiresSponsorshipCandidates(INCLUDE_REQUIRES_SPONSORSHIP)
								.includeUnavailableCandidates(INCLUDE_UNAVAILABLE)
							.build())
					.languageFilters(new LanguageFilters(Set.of(LANG_NL, LANG_IT)))
					.locationFilters(LocationFilters
							.builder()
								.countries(Set.of(NETHERLANDS, ITALY))
								.geoZones(Set.of(EUROPE, BENELUX))
								.locationFilters(new LocationRangeFilters(RANGE_COUNTRY_IT, RANGE_CITY_COSENZA, RANGE_DISANCE_KM))
							.build())
					.requestFilters(RequestFilters
							.builder()
								.backendRequestId(BACKEND_REQUEST_ID)
								.maxNumberOfSuggestions(MAX_SUGGESTIONS)
								.unfiltered(UNFILTERED)
							.build())
					.skillFilters(new SkillFilters(Set.of(SKILL_JAVA, SKILL_CSHARP)))
					.termFilters(TermFilters
							.builder()
								.candidateId(TERM_CANDIDATE_ID)
								.email(TERM_EMAIL)
								.title(TERM_TITLE)
								.firstname(TERM_FIRSTNAME)
								.surname(TERM_SURNAME)
							.build())
				.build();
		
		CandidateFilterOptions filters = CandidateSearchRequest.convertToCandidateFilterOptions(csr, orderAttribute, RESULT_ORDER.desc, Set.of(), Set.of(FUNCTION.ARCHITECT), ownerId, 99);
	
		assertTrue(filters.getFunctions().contains(FUNCTION.ARCHITECT));
		
		assertTrue(filters.getSkills().contains(SKILL_CSHARP));
		assertTrue(filters.getSkills().contains(SKILL_JAVA));
		assertTrue(filters.getLanguages().contains(LANG_IT));
		assertTrue(filters.getLanguages().contains(LANG_NL));
		assertTrue(filters.getGeoZones().contains(EUROPE));
		assertTrue(filters.getGeoZones().contains(BENELUX));		
		assertTrue(filters.getCountries().contains(NETHERLANDS));
		assertTrue(filters.getCountries().contains(ITALY));
		assertEquals(orderAttribute, filters.getOrderAttribute().get());
		assertEquals(INCLUDE_REQUIRES_SPONSORSHIP, 	filters.getIncludeRequiresSponsorship().get());
		assertEquals(RESULT_ORDER.desc, 			filters.getOrder().get());
		assertEquals(RANGE_CITY_COSENZA, 			filters.getLocCity().get());
		assertEquals(RANGE_COUNTRY_IT, 				filters.getLocCountry().get());
		assertEquals(RANGE_DISANCE_KM, 				filters.getLocDistance().get().intValue());
		assertEquals(MIN_EXPERIENCE, 				filters.getYearsExperienceGtEq());
		assertEquals(MAX_EXPERIENCE, 				filters.getYearsExperienceLtEq());
		assertEquals(TERM_TITLE, 					filters.getSearchText());
		assertEquals(TERM_FIRSTNAME, 				filters.getFirstname().get());
		assertEquals(TERM_SURNAME, 					filters.getSurname().get());
		assertEquals(TERM_EMAIL, 					filters.getEmail().get());
		assertEquals(ownerId, 						filters.getOwnerId().get());
		
	}
	
	
}