package com.arenella.recruit.recruiters.entities;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.recruiters.beans.OpenPosition;
import com.arenella.recruit.recruiters.beans.OpenPosition.ContractType;
import com.arenella.recruit.recruiters.beans.OpenPosition.Country;

/**
* Unit tests for the OpenPosition class
* @author K Parkings
*/
public class OpenPositionEntityTest {

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
	private LocalDate		created					= LocalDate.of(2022, 7, 26);
	private boolean			active					= false;
	
	/**
	* Tests creation via Builder
	* @throws Exception
	*/
	@Test
	public void testBuilder() throws Exception {
		
		OpenPositionEntity position = OpenPositionEntity
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
	* Test that OpenPosition can be initialized as a new Object. 
	* @throws Exception
	*/
	@Test
	public void testInitializeAsNewObject() throws Exception{
		
		OpenPositionEntity position = OpenPositionEntity
										.builder()
										.build();
		
		assertNull(position.getId());
		assertNull(position.getRecruiterId());
		
		position.initializeAsNewObject(recruiterId);
		
		assertTrue(position.getId() instanceof UUID);
		assertEquals(position.getRecruiterId(), recruiterId);
		assertNotNull(position.getCreated());
		assertTrue(position.isActive());
	}
	
	/**
	* Tests conversion from Domain to Entity representation
	* @throws Exception
	*/
	@Test
	public void testConvertToEntity() throws Exception{
		
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
					.active(active)
				.build();
		
		OpenPositionEntity entity = OpenPositionEntity.convertToEntity(position, Optional.empty());
		
		assertEquals(entity.getComments(), 				comments);
		assertEquals(entity.getContractType(), 			contractType);
		assertEquals(entity.getCountry(), 				country);
		assertEquals(entity.getDescription(), 			description);
		assertEquals(entity.getId(), 					id);
		assertEquals(entity.getLocation(), 				location);
		assertEquals(entity.getPositionClosingDate(), 	positionClosingDate);
		assertEquals(entity.getPositionTitle(), 		positionTitle);
		assertEquals(entity.getRecruiterId(), 			recruiterId);
		assertEquals(entity.getRenumeration(), 			renumeration);
		assertEquals(entity.getStartDate(), 			startDate);
		
		assertNotNull(entity.getCreated());
		assertTrue(entity.getCreated() instanceof LocalDate);
		assertFalse(position.isActive());
		
	}
	
	/**
	* Tests conversion from Domain to Entity representation when the conversion 
	* involves the update of an existing Entity
	* @throws Exception
	*/
	@Test
	public void testConvertToEntity_updateExistingEntity() throws Exception{
		
		final UUID 			idOrig						= UUID.randomUUID();
		final String 		recruiterIdOrig				= "recruier1IdUpdt";
		final String 		positionTitleOrig			= "Java DeveloperUpdt";
		final Country	 	countryOrig					= Country.IRL;
		final String		locationOrig				= "Remote in EuropeUpdt";
		final ContractType 	contractTypeOrig			= ContractType.PERM;
		final String	 	renumerationOrig			= "500 euros per dayUpdt";
		final LocalDate 	startDateOrig				= LocalDate.of(2022, 6, 28);
		final LocalDate 	positionClosingDateOrig		= LocalDate.of(2022, 7, 14);;
		final String 		descriptionOrig			 	= "Some long descriptive textUpdt";
		final String 		commentsOrig				= "Some comments fromt he RecruiterUpdt";
		
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
					.active(active)
				.build();
		
		OpenPositionEntity positionEntity = OpenPositionEntity
				.builder()
					.comments(commentsOrig)
					.contractType(contractTypeOrig)
					.country(countryOrig)
					.description(descriptionOrig)
					.id(idOrig)
					.location(locationOrig)
					.positionClosingDate(positionClosingDateOrig)
					.positionTitle(positionTitleOrig)
					.recruiterId(recruiterIdOrig)
					.renumeration(renumerationOrig)
					.startDate(startDateOrig)
					.active(true)
					
				.build();
		
		final LocalDate createdOrig = positionEntity.getCreated();
		
		OpenPositionEntity entity = OpenPositionEntity.convertToEntity(position, Optional.of(positionEntity));
		
		assertEquals(entity.getId(), 					idOrig);
		assertEquals(entity.getRecruiterId(), 			recruiterIdOrig);
		assertEquals(entity.getCreated(), 				createdOrig);

		assertEquals(entity.getComments(), 				comments);
		assertEquals(entity.getContractType(), 			contractType);
		assertEquals(entity.getCountry(), 				country);
		assertEquals(entity.getDescription(), 			description);
		
		assertEquals(entity.getLocation(), 				location);
		assertEquals(entity.getPositionClosingDate(), 	positionClosingDate);
		assertEquals(entity.getPositionTitle(), 		positionTitle);
		assertEquals(entity.getRenumeration(), 			renumeration);
		assertEquals(entity.getStartDate(), 			startDate);
		
		assertNotNull(entity.getCreated());
		assertTrue(entity.getCreated() instanceof LocalDate);
		assertFalse(entity.isActive());
		
	}
	
	/**
	* Tests conversion from Entity to Domain representation
	* @throws Exception
	*/
	@Test
	public void testConvertFromEntity() throws Exception{
		
		OpenPositionEntity entity = OpenPositionEntity
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
		
		OpenPosition position = OpenPositionEntity.convertFromEntity(entity);
		
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
	
}