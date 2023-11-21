package com.arenella.recruit.recruiters.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.recruiters.beans.OpenPosition.ContractType;
import com.arenella.recruit.recruiters.beans.OpenPosition.Country;

/**
* Unit tests for the OpenPositionAPIInbound class
* @author K Parkings
*/
public class OpenPositionAPIInboundTest {

	private String 			recruiterId				= "recruier1Id";
	private String 			positionTitle			= "Java Developer";
	private Country	 		country					= Country.EU_REMOTE;
	private String			location				= "Remote in Europe";
	private ContractType 	contractType			= ContractType.CONTRACT;
	private String	 		renumeration			= "500 euros per day";
	private LocalDate 		startDate				= LocalDate.of(2022, 5, 31);
	private LocalDate 		positionClosingDate		= LocalDate.of(2022, 6, 14);;
	private String 			description			 	= "Some long descriptive text";
	private String 			comments				= "Some comments fromt he Recruiter";
	private Set<String>		skills					= Set.of("java","c#");
	
	/**
	* Tests creation via Builder
	* @throws Exception
	*/
	@Test
	public void testBuilder() throws Exception {
		
		OpenPositionAPIInbound position = OpenPositionAPIInbound
				.builder()
					.comments(comments)
					.contractType(contractType)
					.country(country)
					.description(description)
					.location(location)
					.positionClosingDate(positionClosingDate)
					.positionTitle(positionTitle)
					.recruiterId(recruiterId)
					.renumeration(renumeration)
					.startDate(startDate)
					.skills(skills)
				.build();
		
		assertEquals(position.getComments(), 				comments);
		assertEquals(position.getContractType(), 			contractType);
		assertEquals(position.getCountry(), 				country);
		assertEquals(position.getDescription(), 			description);
		assertEquals(position.getLocation(), 				location);
		assertEquals(position.getPositionClosingDate(), 	positionClosingDate);
		assertEquals(position.getPositionTitle(), 			positionTitle);
		assertEquals(position.getRecruiterId(), 			recruiterId);
		assertEquals(position.getRenumeration(), 			renumeration);
		assertEquals(position.getStartDate(), 				startDate);
		
		assertTrue(position.getSkills().contains("java"));
		assertTrue(position.getSkills().contains("c#"));
		
	}

	/**
	* Tests the conversion of OpenPosition incoming API 
	* representation to the Domain representation
	* @throws Exception
	*/
	@Test
	public void testConvertToDomain() throws Exception{
		
		OpenPositionAPIInbound position = OpenPositionAPIInbound
				.builder()
					.comments(comments)
					.contractType(contractType)
					.country(country)
					.description(description)
					.location(location)
					.positionClosingDate(positionClosingDate)
					.positionTitle(positionTitle)
					.recruiterId(recruiterId)
					.renumeration(renumeration)
					.startDate(startDate)
					.skills(skills)
				.build();
		
		OpenPosition openPosition = OpenPositionAPIInbound.convertToDomain(position);
		
		assertEquals(openPosition.getComments(), 				comments);
		assertEquals(openPosition.getContractType(), 			contractType);
		assertEquals(openPosition.getCountry(), 				country);
		assertEquals(openPosition.getDescription(), 			description);
		assertEquals(openPosition.getLocation(), 				location);
		assertEquals(openPosition.getPositionClosingDate(), 	positionClosingDate);
		assertEquals(openPosition.getPositionTitle(), 			positionTitle);
		assertEquals(openPosition.getRecruiterId(), 			recruiterId);
		assertEquals(openPosition.getRenumeration(), 			renumeration);
		assertEquals(openPosition.getStartDate(), 				startDate);
		
		assertTrue(openPosition.getSkills().contains("java"));
		assertTrue(openPosition.getSkills().contains("c#"));
		
		assertNull(openPosition.getId());
	}
	
}