package com.arenella.recruit.recruiters.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.recruiters.beans.OfferedCandidateAPIOutbound.RecruiterDetails;
import com.arenella.recruit.recruiters.beans.OpenPosition.ContractType;
import com.arenella.recruit.recruiters.enums.COUNTRY;

/**
* Unit tests for the OpenPositionAPIOutbound class
* @author K Parkings
*/
public class OpenPositionAPIOutboundTest {

	private UUID 				id						= UUID.randomUUID();
	private String				recruiterId			 	= "kadawdk";
	private String 				recruiterName			= "Kevin Parkings";
	private String 				recruiterCompanyName	= "Arenella BV";
	private String 				recruiterEmail			= "admin@arenella-ict.com";
	private String 				positionTitle			= "Java Developer";
	private COUNTRY	 			country					= COUNTRY.EU_REMOTE;
	private String				location				= "Remote in Europe";
	private ContractType 		contractType			= ContractType.CONTRACT;
	private String	 			renumeration			= "500 euros per day";
	private LocalDate 			startDate				= LocalDate.of(2022, 5, 31);
	private LocalDate 			positionClosingDate		= LocalDate.of(2022, 6, 14);;
	private String 				description			 	= "Some long descriptive text";
	private String 				comments				= "Some comments fromt he Recruiter";
	private RecruiterDetails 	recruiter				= new RecruiterDetails(recruiterId, recruiterName, recruiterCompanyName, recruiterEmail);
	private LocalDateTime 		created					= LocalDateTime.of(2022, 6, 14, 0, 0, 0);
	private Set<String>			skills					= Set.of("java","c#");
	
	/**
	* Tests creation via Builder
	* @throws Exception
	*/
	@Test
	public void testBuilder() throws Exception {
		
		OpenPositionAPIOutbound position = OpenPositionAPIOutbound
				.builder()
					.comments(comments)
					.contractType(contractType)
					.country(country)
					.description(description)
					.id(id)
					.location(location)
					.positionClosingDate(positionClosingDate)
					.positionTitle(positionTitle)
					.recruiter(recruiter)
					.renumeration(renumeration)
					.startDate(startDate)
					.created(created)
					.viewed(true)
					.skills(skills)
				.build();
		
		assertEquals(position.getComments(), 						comments);
		assertEquals(position.getContractType(), 					contractType);
		assertEquals(position.getCountry(), 						country);
		assertEquals(position.getDescription(), 					description);
		assertEquals(position.getId(), 								id);
		assertEquals(position.getLocation(), 						location);
		assertEquals(position.getPositionClosingDate(), 			positionClosingDate);
		assertEquals(position.getPositionTitle(), 					positionTitle);
		assertEquals(position.getRecruiter().getRecruiterId(), 		recruiterId);
		assertEquals(position.getRecruiter().getRecruiterName(), 	recruiterName);
		assertEquals(position.getRecruiter().getCompanyName(), 		recruiterCompanyName);
		assertEquals(position.getRecruiter().getRecruiterEmail(), 	recruiterEmail);
		assertEquals(position.getRenumeration(), 					renumeration);
		assertEquals(position.getStartDate(), 						startDate);
		assertEquals(position.getCreated(), 						created);
		assertTrue(position.isViewed());
		
		assertTrue(position.getSkills().contains("java"));
		assertTrue(position.getSkills().contains("c#"));
		
	}
	
	/**
	* Tests the conversion of OpenPosition domain representation 
	* to a OpenPositionAPIOutbound representation
	* @throws Exception
	*/
	@Test
	public void testConvertFromDomain() throws Exception{
		
		final String recruiterId 	= "rec1";
		final String recruiterName 	= "rec name";
		final String companyName 	= "rec1 BV";
		final String email			= "rec1@rec1.nl";
		final UUID   viewedPost		= UUID.randomUUID();
		final UUID   id				= UUID.randomUUID();
		
		OpenPosition position = OpenPosition
				.builder()
					.id(id)
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
		
		OpenPositionAPIOutbound openPosition = OpenPositionAPIOutbound.convertFromDomain(position, new RecruiterDetails(recruiterId, recruiterName, companyName, email), Set.of(viewedPost));
		
		assertEquals(comments, 				openPosition.getComments());
		assertEquals(contractType, 			openPosition.getContractType());
		assertEquals(country, 				openPosition.getCountry());
		assertEquals(description, 			openPosition.getDescription());
		assertEquals(location, 				openPosition.getLocation());
		assertEquals(positionClosingDate, 	openPosition.getPositionClosingDate());
		assertEquals(positionTitle, 		openPosition.getPositionTitle());
		assertEquals(recruiterId, 			openPosition.getRecruiter().getRecruiterId());
		assertEquals(recruiterName, 		openPosition.getRecruiter().getRecruiterName());
		assertEquals(companyName, 			openPosition.getRecruiter().getCompanyName());
		assertEquals(email, 				openPosition.getRecruiter().getRecruiterEmail());
		assertEquals(renumeration, 			openPosition.getRenumeration());
		assertEquals(startDate, 			openPosition.getStartDate());
		
		assertTrue(openPosition.getSkills().contains("java"));
		assertTrue(openPosition.getSkills().contains("c#"));
	
	}
	
}