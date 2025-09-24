package com.arenella.recruit.candidates.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.PendingCandidate;
import com.arenella.recruit.candidates.beans.Candidate.Photo;
import com.arenella.recruit.candidates.beans.Candidate.Rate;
import com.arenella.recruit.candidates.beans.Candidate.Photo.PHOTO_FORMAT;
import com.arenella.recruit.candidates.beans.Candidate.Rate.CURRENCY;
import com.arenella.recruit.candidates.beans.Candidate.Rate.PERIOD;

/**
* Unit tests for the PendingCandidateEntity class
* @author K Parkings
*/class PendingCandidateEntityTest {

	private static final UUID	 		PENDING_CANDIDATE_ID 		= UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
	private static final String 		FIRSTNAME				= "Kevin";
	private static final String 		SURNAME					= "Parkings";
	private static final String 		EMAIL					= "admin@arenella-ict.com";
	private static final boolean 		FREELANCE 				= true;
	private static final boolean		PERM 					= true;
	private static final String			INTRODUCTION			= "intro";
	private static final Rate			RATE_CONTRACT			= new Rate(CURRENCY.EUR, PERIOD.DAY, 1f, 2f);
	private static final Rate			RATE_PERM				= new Rate(CURRENCY.GBP, PERIOD.YEAR, 1100f, 2200f);
	private static final byte[]			photoBytes			 	= new byte[] {1,2,3};
	private static final PHOTO_FORMAT	photoFormat				= PHOTO_FORMAT.jpeg;
	
	/**
	* Tests that conversion returns an Entity representation of the 
	* Domain representation of a PendingCandidate and the state is copied 
	* successfully 
	*/
	@Test
	void testConversionToEntity() {
		
		PendingCandidate candidate = PendingCandidate
												.builder()
													.pendingCandidateId(PENDING_CANDIDATE_ID)
													.firstname(FIRSTNAME)
													.surname(SURNAME)
													.email(EMAIL)
													.freelance(FREELANCE)
													.perm(PERM)
													.introduction(INTRODUCTION)
													.rateContract(RATE_CONTRACT)
													.ratePerm(RATE_PERM)
													.photo(new Photo(photoBytes, photoFormat))
													
												.build();
		
		PendingCandidateEntity candidateEntity = PendingCandidateEntity.convertToEntity(candidate);

		assertEquals(PENDING_CANDIDATE_ID, 							candidateEntity.getPendingCandidateId());
		assertEquals(FIRSTNAME, 								 	candidateEntity.getFirstname());
		assertEquals(SURNAME, 										candidateEntity.getSurname());
		assertEquals(EMAIL, 										candidateEntity.getEmail());
		assertEquals(FREELANCE, 									candidateEntity.isFreelance());
		assertEquals(PERM, 											candidateEntity.isPerm());
		assertEquals(INTRODUCTION, 									candidateEntity.getIntroduction());
		assertEquals(RATE_CONTRACT.getCurrency(), 					candidateEntity.getRateContractCurrency());
		assertEquals(RATE_CONTRACT.getPeriod(), 					candidateEntity.getRateContractPeriod());
		assertEquals(RATE_CONTRACT.getValueMin(), 					candidateEntity.getRateContractValueMin());
		assertEquals(RATE_CONTRACT.getValueMax(), 					candidateEntity.getRateContractValueMax());
		assertEquals(RATE_PERM.getCurrency(), 						candidateEntity.getRatePermCurrency());
		assertEquals(RATE_PERM.getPeriod(), 						candidateEntity.getRatePermPeriod());
		assertEquals(RATE_PERM.getValueMin(), 						candidateEntity.getRatePermValueMin());
		assertEquals(RATE_PERM.getValueMax(), 						candidateEntity.getRatePermValueMax());
		assertEquals(photoBytes, 									candidateEntity.getPhotoBytes());
		assertEquals(photoFormat, 									candidateEntity.getPhotoFormat());
		
	}
	
	/**
	* Tests that conversion returns a Domain representation of the 
	* Entity representation of a PendingCandidate and the state is copied 
	* successfully 
	*/
	@Test
	void testConversionFromEntity() {
		
		PendingCandidateEntity candidateEntity = PendingCandidateEntity
																.builder()
																	.pendingCandidateId(PENDING_CANDIDATE_ID)
																	.firstname(FIRSTNAME)
																	.surname(SURNAME)
																	.email(EMAIL)
																	.freelance(FREELANCE)
																	.perm(PERM)
																	.introduction(INTRODUCTION)
																	.rateContractCurrency(RATE_CONTRACT.getCurrency())
																	.rateContractPeriod(RATE_CONTRACT.getPeriod())
																	.rateContractValueMin(RATE_CONTRACT.getValueMin())
																	.rateContractValueMax(RATE_CONTRACT.getValueMax())
																	.ratePermCurrency(RATE_PERM.getCurrency())
																	.ratePermPeriod(RATE_PERM.getPeriod())
																	.ratePermValueMin(RATE_PERM.getValueMin())
																	.ratePermValueMax(RATE_PERM.getValueMax())
																	.photoBytes(photoBytes)
																	.photoFormat(photoFormat)
																.build();
														
		PendingCandidate candidate = PendingCandidateEntity.convertFromEntity(candidateEntity);

		assertEquals(PENDING_CANDIDATE_ID,			candidate.getPendingCandidateId());
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
		assertEquals(photoBytes, 					candidate.getPhoto().get().getImageBytes());
		assertEquals(photoFormat,	 				candidate.getPhoto().get().getFormat());
		
	}
	
}