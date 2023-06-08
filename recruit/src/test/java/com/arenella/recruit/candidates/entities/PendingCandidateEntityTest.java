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

		assertEquals(pendingCandidateId,	candidate.getPendingCandidateId());
		assertEquals(firstname, 			candidate.getFirstname());
		assertEquals(surname, 				candidate.getSurname());
		assertEquals(email, 				candidate.getEmail());
		assertEquals(freelance, 			candidate.isFreelance());
		assertEquals(perm, 					candidate.isPerm());
		
		assertEquals(introduction, 			candidate.getIntroduction());
		assertEquals(rateCurrency, 			candidate.getRate().get().getCurrency());
		assertEquals(ratePeriod, 			candidate.getRate().get().getPeriod());
		assertEquals(rateValue, 			candidate.getRate().get().getValue());
		assertEquals(photoBytes, 			candidate.getPhoto().get().getImageBytes());
		assertEquals(photoFormat,	 		candidate.getPhoto().get().getFormat());
		
	}
	
}