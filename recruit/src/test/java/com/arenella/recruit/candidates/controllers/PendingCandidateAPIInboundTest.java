package com.arenella.recruit.candidates.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import com.arenella.recruit.candidates.beans.Candidate.Photo.PHOTO_FORMAT;
import com.arenella.recruit.candidates.beans.Candidate.Rate.CURRENCY;
import com.arenella.recruit.candidates.beans.Candidate.Rate.PERIOD;
import com.arenella.recruit.candidates.beans.PendingCandidate;
import com.arenella.recruit.candidates.controllers.CandidateUpdateRequestAPIInbound.Rate;

/**
* Unit tests for the PendingCandidateAPIInbound class 
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
class PendingCandidateAPIInboundTest {

	@Mock
	private MultipartFile mockMultipartFile;
	
	private static final UUID	 		CANDIDATE_ID 			= UUID.randomUUID();
	private static final String 		FIRSTNAME				= "Kevin";
	private static final String 		SURNAME					= "Parkings";
	private static final String 		EMAIL					= "admin@arenella-ict.com";
	private static final boolean 		FREELANCE 				= true;
	private static final boolean 		PERM 					= true;
	private static final Rate			RATE_CONTRACT			= new Rate(CURRENCY.EUR, PERIOD.HOUR, 300, 400);
	private static final Rate			RATE_PERM				= new Rate(CURRENCY.GBP, PERIOD.DAY, 300, 500);
	private static final String			INTRODUCTION			= "an intro";
	private static final byte[]			PHOTO_BYTES				= new byte[] {1,33,4};
	
	/**
	* Test Builder values used to initialize instance of the Candidate Class 
	*/
	@Test
	void testInitializationFromBuilder() {

		PendingCandidateAPIInbound candidate = PendingCandidateAPIInbound
						.builder()
							.pendingCandidateId(CANDIDATE_ID)
							.firstname(FIRSTNAME)
							.surname(SURNAME)
							.email(EMAIL)
							.freelance(FREELANCE)
							.perm(PERM)
							.rateContract(RATE_CONTRACT)
							.ratePerm(RATE_PERM)
							.introduction(INTRODUCTION)
							.build();
		
		assertEquals(CANDIDATE_ID, 					candidate.getPendingCandidateId());
		assertEquals(FIRSTNAME, 					candidate.getFirstname());
		assertEquals(SURNAME, 						candidate.getSurname());
		assertEquals(EMAIL, 						candidate.getEmail());
		assertEquals(FREELANCE, 					candidate.isFreelance());
		assertEquals(PERM, 							candidate.isPerm());
		assertEquals(INTRODUCTION, 					candidate.getIntroduction());
		
		assertEquals(RATE_CONTRACT.getCurrency(), 	candidate.getRateContract().get().getCurrency());
		assertEquals(RATE_CONTRACT.getPeriod(), 	candidate.getRateContract().get().getPeriod());
		assertEquals(RATE_CONTRACT.getValueMin(), 	candidate.getRateContract().get().getValueMin());
		assertEquals(RATE_CONTRACT.getValueMax(), 	candidate.getRateContract().get().getValueMax());
		
		assertEquals(RATE_PERM.getCurrency(), 		candidate.getRatePerm().get().getCurrency());
		assertEquals(RATE_PERM.getPeriod(), 		candidate.getRatePerm().get().getPeriod());
		assertEquals(RATE_PERM.getValueMin(), 		candidate.getRatePerm().get().getValueMin());
		assertEquals(RATE_PERM.getValueMax(), 		candidate.getRatePerm().get().getValueMax());
		
	}
	
	/**
	* Tests that conversion returns a Domain representation of the 
	* Entity representation of a Candidate and the state is copied 
	* successfully 
	*/
	@Test
	void testConversionToCandidate() throws Exception {
		
		PendingCandidateAPIInbound pendingCandidateEntity = PendingCandidateAPIInbound
				.builder()
					.pendingCandidateId(CANDIDATE_ID)
					.firstname(FIRSTNAME)
					.surname(SURNAME)
					.email(EMAIL)
					.freelance(FREELANCE)
					.perm(PERM)
					.rateContract(RATE_CONTRACT)
					.ratePerm(RATE_PERM)
					.introduction(INTRODUCTION)
					.build();
		
		PendingCandidate pendingCandidate = PendingCandidateAPIInbound.convertToPendingCandidate(pendingCandidateEntity, Optional.empty());

		assertEquals(CANDIDATE_ID, 					pendingCandidate.getPendingCandidateId());
		assertEquals(FIRSTNAME, 					pendingCandidate.getFirstname());
		assertEquals(SURNAME, 						pendingCandidate.getSurname());
		assertEquals(EMAIL, 						pendingCandidate.getEmail());
		assertEquals(FREELANCE, 					pendingCandidate.isFreelance());
		assertEquals(PERM, 						 pendingCandidate.isPerm());
		assertEquals(INTRODUCTION, 					pendingCandidate.getIntroduction());
		
		assertEquals(RATE_CONTRACT.getCurrency(), 	pendingCandidate.getRateContract().get().getCurrency());
		assertEquals(RATE_CONTRACT.getPeriod(), 	pendingCandidate.getRateContract().get().getPeriod());
		assertEquals(RATE_CONTRACT.getValueMin(), 	pendingCandidate.getRateContract().get().getValueMin());
		assertEquals(RATE_CONTRACT.getValueMax(), 	pendingCandidate.getRateContract().get().getValueMax());
		
		assertEquals(RATE_PERM.getCurrency(), 		pendingCandidate.getRatePerm().get().getCurrency());
		assertEquals(RATE_PERM.getPeriod(), 		pendingCandidate.getRatePerm().get().getPeriod());
		assertEquals(RATE_PERM.getValueMin(), 		pendingCandidate.getRatePerm().get().getValueMin());
		assertEquals(RATE_PERM.getValueMax(), 		pendingCandidate.getRatePerm().get().getValueMax());
	}
	
	/**
	* Tests that conversion returns a Domain representation of the 
	* Entity representation of a Candidate and the state is copied 
	* successfully 
	*/
	@Test
	void testConversionToCandidate_profilePhoto() throws Exception {
		
		PendingCandidateAPIInbound pendingCandidateEntity = PendingCandidateAPIInbound
				.builder()
					.pendingCandidateId(CANDIDATE_ID)
					.firstname(FIRSTNAME)
					.surname(SURNAME)
					.email(EMAIL)
					.freelance(FREELANCE)
					.perm(PERM)
					.rateContract(RATE_CONTRACT)
					.ratePerm(RATE_PERM)
					.introduction(INTRODUCTION)
					.build();
		
		Mockito.when(this.mockMultipartFile.getBytes()).thenReturn(PHOTO_BYTES);
		
		PendingCandidate pendingCandidate = PendingCandidateAPIInbound.convertToPendingCandidate(pendingCandidateEntity, Optional.of(mockMultipartFile));

		assertEquals(CANDIDATE_ID, 					pendingCandidate.getPendingCandidateId());
		assertEquals(FIRSTNAME, 					pendingCandidate.getFirstname());
		assertEquals(SURNAME, 						pendingCandidate.getSurname());
		assertEquals(EMAIL, 						pendingCandidate.getEmail());
		assertEquals(FREELANCE, 					pendingCandidate.isFreelance());
		assertEquals(PERM, 							pendingCandidate.isPerm());
		assertEquals(INTRODUCTION, 					pendingCandidate.getIntroduction());
		assertEquals(PHOTO_BYTES,					pendingCandidate.getPhoto().get().getImageBytes());
		assertEquals(PHOTO_FORMAT.jpeg,				pendingCandidate.getPhoto().get().getFormat());
	
		assertEquals(RATE_CONTRACT.getCurrency(), 	pendingCandidate.getRateContract().get().getCurrency());
		assertEquals(RATE_CONTRACT.getPeriod(), 	pendingCandidate.getRateContract().get().getPeriod());
		assertEquals(RATE_CONTRACT.getValueMin(), 	pendingCandidate.getRateContract().get().getValueMin());
		assertEquals(RATE_CONTRACT.getValueMax(), 	pendingCandidate.getRateContract().get().getValueMax());
		
		assertEquals(RATE_PERM.getCurrency(), 		pendingCandidate.getRatePerm().get().getCurrency());
		assertEquals(RATE_PERM.getPeriod(), 		pendingCandidate.getRatePerm().get().getPeriod());
		assertEquals(RATE_PERM.getValueMin(), 		pendingCandidate.getRatePerm().get().getValueMin());
		assertEquals(RATE_PERM.getValueMax(), 		pendingCandidate.getRatePerm().get().getValueMax());
		
	}
	
}