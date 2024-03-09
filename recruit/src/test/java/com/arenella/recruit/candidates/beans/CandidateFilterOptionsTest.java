package com.arenella.recruit.candidates.beans;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.enums.FUNCTION;
import com.arenella.recruit.candidates.enums.RESULT_ORDER;

/**
* Unit tests for the CandidateFilterOptions class
* @author K Parkings
*/
public class CandidateFilterOptionsTest {
	
	/**
	* Tests the instance built by the Builder has been initialized
	* with the expected values
	* @throws Exception
	*/
	@Test
	public void testBuilder() throws Exception{
		
		Set<String> 			candidateIds 					= new HashSet<>();
		Set<COUNTRY> 			countries 						= new HashSet<>();
		Set<FUNCTION> 			functions 						= new HashSet<>();
		Set<String> 			skills 							= new HashSet<>();
		Language.LEVEL 			dutch 							= Language.LEVEL.PROFICIENT;
		Language.LEVEL 			english 						= Language.LEVEL.BASIC;
		Language.LEVEL 			french 							= Language.LEVEL.BASIC;
		boolean 				freelance	 					= false;
		boolean 				perm 							= true;
		String 					orderAttribute 					= "candidateId";
		int 					yearsExperienceGtEq 			= 2;
		String 					candidateId 					= "aCandidateId";
		String 					skill 							= "aSkill";
		String					firstname						= "kevin";
		String					surname							= "Parkings";
		String					email							= "email";
		Boolean					flaggedAsUnavailable			= true;	
		Integer					daysSinceLastAvailabilityCheck 	= 14;
		String					searchText						= "Java Developer";
		String					ownerId							= "rec22";
		
		candidateIds.add(candidateId);
		skills.add(skill);
		
		countries.add(COUNTRY.NETHERLANDS);
		functions.add(FUNCTION.PROJECT_MANAGER);
		
		CandidateFilterOptions filters = CandidateFilterOptions
												.builder()
													.candidateIds(candidateIds)
													.countries(countries)
													.dutch(dutch)
													.english(english)
													.french(french)
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
													.build();
		
		assertEquals(filters.getCandidateIds().stream().findAny().get(), 	candidateId);
		assertEquals(filters.getCountries().stream().findAny().get(), 		COUNTRY.NETHERLANDS);
		assertEquals(filters.getFunctions().stream().findAny().get(), 		FUNCTION.PROJECT_MANAGER);
		assertEquals(filters.getSkills().stream().findAny().get(), 			skill);
		
		assertEquals(filters.getDutch().get(), 								dutch);
		assertEquals(filters.getFrench().get(), 							french);
		assertEquals(filters.getEnglish().get(), 							english);
		assertEquals(filters.getOrder().get(), 								RESULT_ORDER.asc);
		assertEquals(filters.getOrderAttribute().get(), 					orderAttribute);
		assertEquals(filters.getYearsExperienceGtEq(), 						yearsExperienceGtEq);
		assertEquals(firstname, 											filters.getFirstname().get());
		assertEquals(surname, 												filters.getSurname().get());
		assertEquals(email, 												filters.getEmail().get());
		assertEquals(searchText,											filters.getSearchText());
		assertEquals(flaggedAsUnavailable, 									filters.isFlaggedAsUnavailable().get());
		assertEquals(daysSinceLastAvailabilityCheck, 						filters.getDaysSinceLastAvailabilityCheck().get());
		assertEquals(ownerId,						 						filters.getOwnerId().get());
	}
	
	/**
	* Tests setter replaces existing FUNCTION's
	* @throws Exception
	*/
	@Test
	public void testSetters() throws Exception{
		
		Set<FUNCTION> 	originalFunctions 	= Set.of(FUNCTION.ARCHITECT);
		Set<FUNCTION> 	newFunctions 		= Set.of(FUNCTION.JAVA_DEV);
		Boolean 		active 				= false;
		
		CandidateFilterOptions filters = CandidateFilterOptions.builder().functions(originalFunctions).build();
		
		assertTrue(filters.getFunctions().contains(FUNCTION.ARCHITECT));
		assertFalse(filters.getFunctions().contains(FUNCTION.JAVA_DEV));
		assertTrue(filters.isAvailable().isEmpty());
		
		filters.setFunctions(newFunctions);
		filters.setAvailable(active);
		
		assertFalse(filters.getFunctions().contains(FUNCTION.ARCHITECT));
		assertTrue(filters.getFunctions().contains(FUNCTION.JAVA_DEV));
		assertFalse(filters.isAvailable().get());
		
	}
	
}