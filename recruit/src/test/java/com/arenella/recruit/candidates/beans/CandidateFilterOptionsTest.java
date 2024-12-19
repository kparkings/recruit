package com.arenella.recruit.candidates.beans;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.CandidateFilterOptions.GeoPosFilter;
import com.arenella.recruit.candidates.beans.Language.LANGUAGE;
import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.enums.FUNCTION;
import com.arenella.recruit.candidates.enums.RESULT_ORDER;
import com.arenella.recruit.candidates.utils.GeoZoneSearchUtil.GEO_ZONE;

/**
* Unit tests for the CandidateFilterOptions class
* @author K Parkings
*/
class CandidateFilterOptionsTest {
	
	/**
	* Tests the instance built by the Builder has been initialized
	* with the expected values
	* @throws Exception
	*/
	@Test
	void testBuilder() {
		
		Set<String> 			candidateIds 							= new HashSet<>();
		Set<GEO_ZONE> 			geoZones 								= new HashSet<>();
		Set<COUNTRY> 			countries 								= new HashSet<>();
		Set<FUNCTION> 			functions 								= new HashSet<>();
		Set<String> 			skills 									= new HashSet<>();
		boolean 				freelance	 							= false;
		boolean 				perm 									= true;
		String 					orderAttribute 							= "candidateId";
		int 					yearsExperienceGtEq 					= 2;
		String 					candidateId 							= "aCandidateId";
		String 					skill 									= "aSkill";
		String					firstname								= "kevin";
		String					surname									= "Parkings";
		String					email									= "email";
		Boolean					flaggedAsUnavailable					= true;	
		Integer					daysSinceLastAvailabilityCheck 			= 14;
		String					searchText								= "Java Developer";
		String					ownerId									= "rec22";
		boolean 				includeRequiresSponsorship				= true;
		LocalDate				registeredAfter							= LocalDate.of(2024,5,8);
		LocalDate				lastAccountRefreshLtEq					= LocalDate.of(2024,8,8);
		Integer 				daysSinceLastavailabilityCheckEmail 	= 2;
		int						maxResults								= 500;
		
		candidateIds.add(candidateId);
		skills.add(skill);
		
		geoZones.add(GEO_ZONE.BENELUX);
		countries.add(COUNTRY.NETHERLANDS);
		functions.add(FUNCTION.PROJECT_MANAGER);
		
		CandidateFilterOptions filters = CandidateFilterOptions
												.builder()
													.candidateIds(candidateIds)
													.geoZones(geoZones)
													.countries(countries)
													.languages(Set.of(
															LANGUAGE.ENGLISH,
															LANGUAGE.DUTCH,
															LANGUAGE.FRENCH)
													)
													.freelance(freelance)
													.perm(perm)
													.functions(functions)
													.order(RESULT_ORDER.asc)
													.orderAttribute(orderAttribute)
													.skills(skills)
													.yearsExperienceGtEq(yearsExperienceGtEq)
													.firstname(firstname)
													.surname(surname)
													.email(email)
													.flaggedAsUnavailable(flaggedAsUnavailable)
													.daysSinceLastAvailabilityCheck(daysSinceLastAvailabilityCheck)
													.searchText(searchText)
													.ownerId(ownerId)
													.includeRequiresSponsorship(includeRequiresSponsorship)
													.registeredAfter(registeredAfter)
													.lastAccountRefreshLtEq(lastAccountRefreshLtEq)
													.lastAccountRefreshMissing()
													.daysSincelastAvailabilityCheckEmailSent(daysSinceLastavailabilityCheckEmail)
													.maxResults(maxResults)
													.build();
		
		assertEquals(filters.getCandidateIds().stream().findAny().get(), 	candidateId);
		assertEquals(filters.getGeoZones().stream().findAny().get(), 		GEO_ZONE.BENELUX);
		assertEquals(filters.getCountries().stream().findAny().get(), 		COUNTRY.NETHERLANDS);
		assertEquals(filters.getFunctions().stream().findAny().get(), 		FUNCTION.PROJECT_MANAGER);
		assertEquals(filters.getSkills().stream().findAny().get(), 			skill);
		
		assertTrue(filters.getLanguages().stream().filter(l -> l == LANGUAGE.DUTCH).findFirst().isPresent());
		assertTrue(filters.getLanguages().stream().filter(l -> l == LANGUAGE.FRENCH).findFirst().isPresent());
		assertTrue(filters.getLanguages().stream().filter(l -> l == LANGUAGE.ENGLISH).findFirst().isPresent());
		assertEquals(filters.getOrder().get(), 								RESULT_ORDER.asc);
		assertEquals(filters.getOrderAttribute().get(), 					orderAttribute);
		assertEquals(filters.getYearsExperienceGtEq(), 						yearsExperienceGtEq);
		assertEquals(maxResults, 											filters.getMaxResults());
		assertEquals(firstname, 											filters.getFirstname().get());
		assertEquals(surname, 												filters.getSurname().get());
		assertEquals(email, 												filters.getEmail().get());
		assertEquals(searchText,											filters.getSearchText());
		assertEquals(flaggedAsUnavailable, 									filters.isFlaggedAsUnavailable().get());
		assertEquals(daysSinceLastAvailabilityCheck, 						filters.getDaysSinceLastAvailabilityCheck().get());
		assertEquals(ownerId,						 						filters.getOwnerId().get());
		assertEquals(registeredAfter,				 						filters.getRegisteredAfter().get());
		assertEquals(lastAccountRefreshLtEq, 								filters.getLastAccountRefreshLtEq().get());				
		assertTrue(filters.getIncludeRequiresSponsorship().get());
		assertTrue(filters.getLastAccountRefreshMissing().get());
		assertEquals(daysSinceLastavailabilityCheckEmail, filters.getDaysSincelastAvailabilityCheckEmailSent().get());
		
	}
	
	/**
	* Tests reset sets values to defaults
	* @throws Exception
	*/
	@Test
	void testReset() {
		
		Set<String> 			candidateIds 							= new HashSet<>();
		Set<GEO_ZONE> 			geoZones 								= new HashSet<>();
		Set<COUNTRY> 			countries 								= new HashSet<>();
		Set<FUNCTION> 			functions 								= new HashSet<>();
		Set<String> 			skills 									= new HashSet<>();
		boolean 				freelance	 							= false;
		boolean 				perm 									= true;
		String 					orderAttribute 							= "candidateId";
		int 					yearsExperienceGtEq 					= 2;
		String 					candidateId 							= "aCandidateId";
		String 					skill 									= "aSkill";
		String					firstname								= "kevin";
		String					surname									= "Parkings";
		String					email									= "email";
		Boolean					flaggedAsUnavailable					= true;	
		Integer					daysSinceLastAvailabilityCheck 			= 14;
		String					searchText								= "Java Developer";
		String					ownerId									= "rec22";
		boolean 				includeRequiresSponsorship				= true;
		LocalDate				registeredAfter							= LocalDate.of(2024,5,8);
		LocalDate				lastAccountRefreshLtEq					= LocalDate.of(2024,8,8);
		Integer 				daysSinceLastavailabilityCheckEmail 	= 2;
		
		candidateIds.add(candidateId);
		skills.add(skill);
		
		geoZones.add(GEO_ZONE.BENELUX);
		countries.add(COUNTRY.NETHERLANDS);
		functions.add(FUNCTION.PROJECT_MANAGER);
		
		CandidateFilterOptions filters = CandidateFilterOptions
												.builder()
													.candidateIds(candidateIds)
													.geoZones(geoZones)
													.countries(countries)
													.languages(Set.of(
															LANGUAGE.ENGLISH,
															LANGUAGE.DUTCH,
															LANGUAGE.FRENCH)
													)
													.freelance(freelance)
													.perm(perm)
													.functions(functions)
													.order(RESULT_ORDER.asc)
													.orderAttribute(orderAttribute)
													.skills(skills)
													.yearsExperienceGtEq(yearsExperienceGtEq)
													.firstname(firstname)
													.surname(surname)
													.email(email)
													.flaggedAsUnavailable(flaggedAsUnavailable)
													.daysSinceLastAvailabilityCheck(daysSinceLastAvailabilityCheck)
													.searchText(searchText)
													.ownerId(ownerId)
													.includeRequiresSponsorship(includeRequiresSponsorship)
													.registeredAfter(registeredAfter)
													.lastAccountRefreshLtEq(lastAccountRefreshLtEq)
													.lastAccountRefreshMissing()
													.daysSincelastAvailabilityCheckEmailSent(daysSinceLastavailabilityCheckEmail)
													.build();
		
		CandidateFilterOptions defaults = CandidateFilterOptions.builder().build();
		
		filters.reset();
		
		assertEquals(filters.getCandidateIds(), 							defaults.getCandidateIds());
		assertEquals(filters.getGeoZones(), 								defaults.getGeoZones());
		assertEquals(filters.getCountries(), 								defaults.getCountries());
		assertTrue(filters.getLanguages().isEmpty());
		assertEquals(filters.isFreelance(), 								defaults.isFreelance());
		assertEquals(filters.isPerm(), 										defaults.isPerm());
		assertEquals(filters.getFunctions(), 								defaults.getFunctions());
		assertEquals(filters.getOrder(), 									defaults.getOrder());
		assertEquals(filters.getOrderAttribute(), 							defaults.getOrderAttribute());
		assertEquals(filters.getSkills(), 									defaults.getSkills());
		assertEquals(filters.getYearsExperienceGtEq(), 						defaults.getYearsExperienceGtEq());
		assertEquals(filters.getFirstname(), 								defaults.getFirstname());
		assertEquals(filters.getSurname(), 									defaults.getSurname());
		assertEquals(filters.getEmail(), 									defaults.getEmail());
		assertEquals(filters.isFlaggedAsUnavailable(), 						defaults.isFlaggedAsUnavailable());
		assertEquals(filters.getDaysSinceLastAvailabilityCheck(), 			defaults.getDaysSinceLastAvailabilityCheck());
		assertEquals(filters.getSearchText(), 								defaults.getSearchText());
		assertEquals(filters.getOwnerId(), 									defaults.getOwnerId());
		assertEquals(filters.getIncludeRequiresSponsorship(), 				defaults.getIncludeRequiresSponsorship());
		assertEquals(filters.getRegisteredAfter(),							defaults.getRegisteredAfter());
		assertEquals(filters.getLastAccountRefreshLtEq(), 					defaults.getLastAccountRefreshLtEq());	
		assertEquals(filters.getLastAccountRefreshMissing(), 				defaults.getLastAccountRefreshMissing());	
		assertEquals(filters.getDaysSincelastAvailabilityCheckEmailSent(), 	defaults.getDaysSincelastAvailabilityCheckEmailSent());
	}
	
	/**
	* Tests setter replaces existing FUNCTION's
	* @throws Exception
	*/
	@Test
	void testSetters() {
		
		Set<FUNCTION> 	originalFunctions 				= Set.of(FUNCTION.ARCHITECT);
		Set<FUNCTION> 	newFunctions 					= Set.of(FUNCTION.JAVA_DEV);
		Boolean 		active 							= false;
		Boolean 		includeRequiresSponsorship 		= true;
		String			candidateId3000					= "3000";
		String			candidateId3001					= "3001";
		
		CandidateFilterOptions filters = CandidateFilterOptions.builder().candidateIds(Set.of(candidateId3000)).functions(originalFunctions).includeRequiresSponsorship(includeRequiresSponsorship).build();
		
		assertTrue(filters.getFunctions().contains(FUNCTION.ARCHITECT));
		assertFalse(filters.getFunctions().contains(FUNCTION.JAVA_DEV));
		assertTrue(filters.isAvailable().isEmpty());
		assertTrue(filters.getIncludeRequiresSponsorship().get());
		
		filters.setFunctions(newFunctions);
		filters.setAvailable(active);
		filters.setIncludeRequiresSponsorship(false);
		filters.setCandidateIds(Set.of(candidateId3001));
		
		assertFalse(filters.getFunctions().contains(FUNCTION.ARCHITECT));
		assertTrue(filters.getFunctions().contains(FUNCTION.JAVA_DEV));
		assertFalse(filters.isAvailable().get());
		assertFalse(filters.getIncludeRequiresSponsorship().get());
		assertEquals(candidateId3001, filters.getCandidateIds().toArray()[0]);
		assertEquals(1, filters.getCandidateIds().size());
	}
	
	/**
	* Removes any GeoZone filters
	* @throws Exception
	*/
	@Test
	void testRemoveGeoZones() {
		
		CandidateFilterOptions filters = CandidateFilterOptions
				.builder()
					.geoZones(Set.of(GEO_ZONE.BRITISH_ISLES))
				.build();
		
		assertEquals(filters.getGeoZones().stream().findAny().get(), GEO_ZONE.BRITISH_ISLES);
				
		filters.removeGeoZones();
		
		assertTrue(filters.getGeoZones().stream().findAny().isEmpty());
		
	}
	
	/**
	* Tests adding countries to filter on
	* @throws Exception
	*/
	@Test
	void testAddCountry() {
		
		CandidateFilterOptions filters = CandidateFilterOptions
				.builder()
					.countries(Set.of(COUNTRY.BELGIUM))
				.build();
		
		assertEquals(filters.getCountries().stream().filter(c -> c == COUNTRY.BELGIUM).findAny().get(), COUNTRY.BELGIUM);
		assertEquals(1, filters.getCountries().size());
		
		
		filters.addCountry(COUNTRY.BULGARIA);
		
		assertEquals(filters.getCountries().stream().filter(c -> c == COUNTRY.BELGIUM).findAny().get(), COUNTRY.BELGIUM);
		assertEquals(filters.getCountries().stream().filter(c -> c == COUNTRY.BULGARIA).findAny().get(), COUNTRY.BULGARIA);
		assertEquals(2, filters.getCountries().size());
	
	}
	
	/**
	* Tests adding GeoPos filter
	*/
	@Test
	void testAddGeoPosFIlter() {
		
		GeoPosFilter geoPosFilter = new GeoPosFilter("cityPosGeoCode", 100, 55.0f, 1.5f); 
		
		assertTrue(CandidateFilterOptions.builder().build().getGeoPosFilter().isEmpty());
		
		CandidateFilterOptions filters = CandidateFilterOptions
				.builder()
					.geoPosFilter(geoPosFilter)
				.build();
		
		assertEquals(geoPosFilter.field(), 		filters.getGeoPosFilter().get().field());
		assertEquals(geoPosFilter.distance(), 	filters.getGeoPosFilter().get().distance());
		assertEquals(geoPosFilter.lat(), 		filters.getGeoPosFilter().get().lat(), 0);
		assertEquals(geoPosFilter.lon(), 		filters.getGeoPosFilter().get().lon(), 0);
		
	}
}