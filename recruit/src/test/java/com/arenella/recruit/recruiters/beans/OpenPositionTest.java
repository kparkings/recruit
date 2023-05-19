package com.arenella.recruit.recruiters.beans;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.recruiters.beans.OpenPosition.ContractType;
import com.arenella.recruit.recruiters.beans.OpenPosition.Country;

/**
* Unit tests for the OpenPosition class
* @author K Parkings
*/
public class OpenPositionTest {

	private UUID 			id						= UUID.randomUUID();
	private String 			recruiterId				= "recruier1Id";
	private String 			positionTitle			= "Java Developer";
	private Country	 		country					= Country.EUROPE;
	private String			location				= "Remote in Europe";
	private ContractType 	contractType			= ContractType.CONTRACT;
	private String	 		renumeration			= "500 euros per day";
	private LocalDate 		startDate				= LocalDate.of(2022, 5, 31);
	private LocalDate 		positionClosingDate		= LocalDate.of(2022, 6, 14);;
	private String 			description			 	= "Some long descriptive text";
	private String 			comments				= "Some comments fromt he Recruiter";
	private LocalDate		created					= LocalDate.of(2022, 9, 26);
	private boolean			active					= false;
	
	/**
	* Tests creation via Builder
	* @throws Exception
	*/
	@Test
	public void testBuilder() throws Exception {
		
		OpenPosition position = OpenPosition
				.builder()
					.comments(comments)
					.contractType(contractType)
					.country(country)
					.description(description)
					.id(id)
					.location(location)
					.positionClosingDate(positionClosingDate)
					.positionTitle(positionTitle)
					.recruiterId(recruiterId)
					.renumeration(renumeration)
					.startDate(startDate)
					.created(created)
					.active(active)
				.build();
		
		assertEquals(position.getComments(), 				comments);
		assertEquals(position.getContractType(), 			contractType);
		assertEquals(position.getCountry(), 				country);
		assertEquals(position.getDescription(), 			description);
		assertEquals(position.getId(), 						id);
		assertEquals(position.getLocation(), 				location);
		assertEquals(position.getPositionClosingDate(), 	positionClosingDate);
		assertEquals(position.getPositionTitle(), 			positionTitle);
		assertEquals(position.getRecruiterId(), 			recruiterId);
		assertEquals(position.getRenumeration(), 			renumeration);
		assertEquals(position.getStartDate(), 				startDate);
		assertEquals(position.getCreated(), 				created);
		assertFalse(position.isActive());
		
	}
	
	/**
	* Tests setters set values correctly
	* @throws Exception
	*/
	@Test
	public void testSetters() throws Exception{
		
		OpenPosition position = OpenPosition
				.builder()
				.build();
		
		assertTrue(position.isActive());
		
		position.setActive(false);
		
		assertFalse(position.isActive());
		
	}
	
	/**
	* Test that OpenPosition can be initialized as a new Object. 
	* @throws Exception
	*/
	@Test
	public void testInitializeAsNewObject() throws Exception{
		
	OpenPosition position = OpenPosition
				.builder()
				.build();
		
		assertNull(position.getId());
		assertNull(position.getRecruiterId());
		
		position.initializeAsNewObject(recruiterId);
		
		assertTrue(position.getId() instanceof UUID);
		assertEquals(position.getRecruiterId(), recruiterId);
		
		assertTrue(position.isActive());
		
	}
	
}