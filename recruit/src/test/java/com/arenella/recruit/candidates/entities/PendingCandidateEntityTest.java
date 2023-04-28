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
*/
public class PendingCandidateEntityTest {

	private static final UUID	 		pendingCandidateId 		= UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
	private static final String 		firstname				= "Kevin";
	private static final String 		surname					= "Parkings";
	private static final String 		email					= "kparkings@gmail.com";
	private static final boolean 		freelance 				= true;
	private static final boolean		perm 					= true;
	private static final String			introduction			= "intro";
	private static final CURRENCY		rateCurrency			= CURRENCY.EUR;
	private static final PERIOD			ratePeriod				= PERIOD.DAY;
	private static final float			rateValue				= 1f;
	private static final byte[]			photoBytes			 	= new byte[] {1,2,3};
	private static final PHOTO_FORMAT	photoFormat				= PHOTO_FORMAT.jpeg;
	
	/**
	* Tests that conversion returns an Entity representation of the 
	* Domain representation of a PendingCandidate and the state is copied 
	* successfully 
	*/
	@Test
	public void testConversionToEntity() {
		
		PendingCandidate candidate = PendingCandidate
												.builder()
													.pendingCandidateId(pendingCandidateId)
													.firstname(firstname)
													.surname(surname)
													.email(email)
													.freelance(freelance)
													.perm(perm)
													.introduction(introduction)
													.rate(new Rate(rateCurrency, ratePeriod, rateValue))
													.photo(new Photo(photoBytes, photoFormat))
													
												.build();
		
		PendingCandidateEntity candidateEntity = PendingCandidateEntity.convertToEntity(candidate);

		assertEquals(candidateEntity.getPendingCandidateId(), 		pendingCandidateId);
		assertEquals(candidateEntity.getFirstname(), 				firstname);
		assertEquals(candidateEntity.getSurname(), 					surname);
		assertEquals(candidateEntity.getEmail(), 					email);
		assertEquals(candidateEntity.isFreelance(), 				freelance);
		assertEquals(candidateEntity.isPerm(), 						perm);
		assertEquals(candidateEntity.getIntroduction(), 			introduction);
		assertEquals(candidateEntity.getRateCurrency(), 			rateCurrency);
		assertEquals(candidateEntity.getRatePeriod(), 				ratePeriod);
		assertEquals(candidateEntity.getRateValue(), 				rateValue);
		assertEquals(candidateEntity.getPhotoBytes(), 				photoBytes);
		assertEquals(candidateEntity.getPhotoFormat(), 				photoFormat);
		
	}
	
	/**
	* Tests that conversion returns a Domain representation of the 
	* Entity representation of a PendingCandidate and the state is copied 
	* successfully 
	*/
	@Test
	public void testConversionFromEntity() {
		
		PendingCandidateEntity candidateEntity = PendingCandidateEntity
																.builder()
																	.pendingCandidateId(pendingCandidateId)
																	.firstname(firstname)
																	.surname(surname)
																	.email(email)
																	.freelance(freelance)
																	.perm(perm)
																	.introduction(introduction)
																	.rateCurrency(rateCurrency)
																	.ratePeriod(ratePeriod)
																	.rateValue(rateValue)
																	.photoBytes(photoBytes)
																	.photoFormat(photoFormat)
																.build();
														
		PendingCandidate candidate = PendingCandidateEntity.convertFromEntity(candidateEntity);

		assertEquals(candidate.getPendingCandidateId(), 		pendingCandidateId);
		assertEquals(candidate.getFirstname(), 					firstname);
		assertEquals(candidate.getSurname(), 					surname);
		assertEquals(candidate.getEmail(), 						email);
		assertEquals(candidate.isFreelance(), 					freelance);
		assertEquals(candidate.isPerm(), 						perm);
		
		assertEquals(candidate.getIntroduction(), 				introduction);
		assertEquals(candidate.getRate().get().getCurrency(), 	rateCurrency);
		assertEquals(candidate.getRate().get().getPeriod(), 	ratePeriod);
		assertEquals(candidate.getRate().get().getValue(), 		rateValue);
		assertEquals(candidate.getPhoto().get().getImageBytes(),photoBytes);
		assertEquals(candidate.getPhoto().get().getFormat(), 	photoFormat);
		
	}
	
}