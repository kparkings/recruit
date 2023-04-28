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
public class PendingCandidateAPIInboundTest {

	@Mock
	private MultipartFile mockMultipartFile;
	
	private static final UUID	 		candidateId 			= UUID.randomUUID();
	private static final String 		firstname				= "Kevin";
	private static final String 		surname					= "Parkings";
	private static final String 		email					= "kparkings@gmail.com";
	private static final boolean 		freelance 				= true;
	private static final boolean 		perm 					= true;
	private static final Rate			rate					= new Rate(CURRENCY.EUR, PERIOD.HOUR, 300);
	private static final String			introduction			= "an intro";
	private static final byte[]			PHOTO_BYTES				= new byte[] {1,33,4};
	
	/**
	* Test Builder values used to initialize instance of the Candidate Class 
	*/
	@Test
	public void testInitializationFromBuilder() {

		PendingCandidateAPIInbound candidate = PendingCandidateAPIInbound
						.builder()
							.pendingCandidateId(candidateId)
							.firstname(firstname)
							.surname(surname)
							.email(email)
							.freelance(freelance)
							.perm(perm)
							.rate(rate)
							.introduction(introduction)
							.build();
		
		assertEquals(candidate.getPendingCandidateId(), 		candidateId);
		assertEquals(candidate.getFirstname(), 					firstname);
		assertEquals(candidate.getSurname(), 					surname);
		assertEquals(candidate.getEmail(), 						email);
		assertEquals(candidate.isFreelance(), 					freelance);
		assertEquals(candidate.isPerm(), 						perm);
		assertEquals(introduction, 								candidate.getIntroduction());
		assertEquals(CURRENCY.EUR, 								candidate.getRate().get().getCurrency());
		assertEquals(PERIOD.HOUR, 								candidate.getRate().get().getPeriod());
		assertEquals(300,										candidate.getRate().get().getValue());
		
	}
	
	/**
	* Tests that conversion returns a Domain representation of the 
	* Entity representation of a Candidate and the state is copied 
	* successfully 
	*/
	@Test
	public void testConversionToCandidate() throws Exception {
		
		PendingCandidateAPIInbound pendingCandidateEntity = PendingCandidateAPIInbound
				.builder()
					.pendingCandidateId(candidateId)
					.firstname(firstname)
					.surname(surname)
					.email(email)
					.freelance(freelance)
					.perm(perm)
					.rate(rate)
					.introduction(introduction)
					.build();
		
		PendingCandidate pendingCandidate = PendingCandidateAPIInbound.convertToPendingCandidate(pendingCandidateEntity, Optional.empty());

		assertEquals(pendingCandidate.getPendingCandidateId(), 	candidateId);
		assertEquals(pendingCandidate.getFirstname(), 			firstname);
		assertEquals(pendingCandidate.getSurname(), 			surname);
		assertEquals(pendingCandidate.getEmail(), 				email);
		assertEquals(pendingCandidate.isFreelance(), 			freelance);
		assertEquals(pendingCandidate.isPerm(), 				perm);
		assertEquals(introduction, 								pendingCandidate.getIntroduction());
		assertEquals(CURRENCY.EUR, 								pendingCandidate.getRate().get().getCurrency());
		assertEquals(PERIOD.HOUR, 								pendingCandidate.getRate().get().getPeriod());
		assertEquals(300,										pendingCandidate.getRate().get().getValue());
		
	}
	
	/**
	* Tests that conversion returns a Domain representation of the 
	* Entity representation of a Candidate and the state is copied 
	* successfully 
	*/
	@Test
	public void testConversionToCandidate_profilePhoto() throws Exception {
		
		PendingCandidateAPIInbound pendingCandidateEntity = PendingCandidateAPIInbound
				.builder()
					.pendingCandidateId(candidateId)
					.firstname(firstname)
					.surname(surname)
					.email(email)
					.freelance(freelance)
					.perm(perm)
					.rate(rate)
					.introduction(introduction)
					.build();
		
		Mockito.when(this.mockMultipartFile.getBytes()).thenReturn(PHOTO_BYTES);
		
		
		PendingCandidate pendingCandidate = PendingCandidateAPIInbound.convertToPendingCandidate(pendingCandidateEntity, Optional.of(mockMultipartFile));

		assertEquals(pendingCandidate.getPendingCandidateId(), 	candidateId);
		assertEquals(pendingCandidate.getFirstname(), 			firstname);
		assertEquals(pendingCandidate.getSurname(), 			surname);
		assertEquals(pendingCandidate.getEmail(), 				email);
		assertEquals(pendingCandidate.isFreelance(), 			freelance);
		assertEquals(pendingCandidate.isPerm(), 				perm);
		assertEquals(introduction, 								pendingCandidate.getIntroduction());
		assertEquals(CURRENCY.EUR, 								pendingCandidate.getRate().get().getCurrency());
		assertEquals(PERIOD.HOUR, 								pendingCandidate.getRate().get().getPeriod());
		assertEquals(300,										pendingCandidate.getRate().get().getValue());
		assertEquals(PHOTO_BYTES,								pendingCandidate.getPhoto().get().getImageBytes());
		assertEquals(PHOTO_FORMAT.jpeg,							pendingCandidate.getPhoto().get().getFormat());
		
		
	}
	
	
}
