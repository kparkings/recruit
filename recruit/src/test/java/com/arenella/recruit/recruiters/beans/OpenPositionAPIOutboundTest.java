package com.arenella.recruit.recruiters.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.recruiters.beans.OpenPosition.ContractType;
import com.arenella.recruit.recruiters.beans.OpenPosition.Country;

/**
* Unit tests for the OpenPositionAPIOutbound class
* @author K Parkings
*/
public class OpenPositionAPIOutboundTest {

	private UUID 			id						= UUID.randomUUID();
	private String 			recruiterName			= "Kevin Parkings";
	private String 			recruiterCompanyName	= "Arenella BV";
	private String 			positionTitle			= "Java Developer";
	private Country	 		country					= Country.EUROPE;
	private String			location				= "Remote in Europe";
	private ContractType 	contractType			= ContractType.CONTRACT;
	private String	 		renumeration			= "500 euros per day";
	private LocalDate 		startDate				= LocalDate.of(2022, 5, 31);
	private LocalDate 		positionClosingDate		= LocalDate.of(2022, 6, 14);;
	private String 			description			 	= "Some long descriptive text";
	private String 			comments				= "Some comments fromt he Recruiter";
	
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
					.recruiterName(recruiterName)
					.recruiterCompanyName(recruiterCompanyName)
					.renumeration(renumeration)
					.startDate(startDate)
				.build();
		
		assertEquals(position.getComments(), 				comments);
		assertEquals(position.getContractType(), 			contractType);
		assertEquals(position.getCountry(), 				country);
		assertEquals(position.getDescription(), 			description);
		assertEquals(position.getId(), 						id);
		assertEquals(position.getLocation(), 				location);
		assertEquals(position.getPositionClosingDate(), 	positionClosingDate);
		assertEquals(position.getPositionTitle(), 			positionTitle);
		assertEquals(position.getRecruiterName(), 			recruiterName);
		assertEquals(position.getRecruiterCompanyName(), 	recruiterCompanyName);
		assertEquals(position.getRenumeration(), 			renumeration);
		assertEquals(position.getStartDate(), 				startDate);
		
	}
}