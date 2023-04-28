package com.arenella.recruit.candidates.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.Candidate.Photo;
import com.arenella.recruit.candidates.beans.Candidate.Rate;
import com.arenella.recruit.candidates.beans.Candidate.Photo.PHOTO_FORMAT;
import com.arenella.recruit.candidates.beans.Candidate.Rate.CURRENCY;
import com.arenella.recruit.candidates.beans.Candidate.Rate.PERIOD;

/**
* Unit test for the PendingCandidate Class
* @author K Parkings
*/
public class PendingCandidateTest {

	private static final UUID 			pendingCandidateId 		= UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
	private static final String 		email					= "kparkings@gmail.com";
	private static final boolean 		freelance 				= true;
	private static final boolean		perm 					= true;
	private static final Rate			rate					= new Rate(CURRENCY.EUR, PERIOD.HOUR, 300);
	private static final String			introduction			= "An intro";
	private static final Photo			profilePhoto			= new Photo(new byte[] {}, PHOTO_FORMAT.jpeg);
	
	/**
	* Test Builder values used to initialize instance of the Candidate Class 
	*/
	@Test
	public void testInitializationFromBuilder() {
		
		PendingCandidate candidate = PendingCandidate
												.builder()
													.pendingCandidateId(pendingCandidateId)
													.email(email)
													.freelance(freelance)
													.perm(perm)
													.rate(rate)
													.introduction(introduction)
													.photo(profilePhoto)
													.build();
		
		assertEquals(pendingCandidateId, 		candidate.getPendingCandidateId());
		assertEquals(email, 					candidate.getEmail());
		assertEquals(freelance, 				candidate.isFreelance());
		assertEquals(perm, 						candidate.isPerm());
		assertEquals(rate.getCurrency(), 		candidate.getRate().get().getCurrency());
		assertEquals(rate.getPeriod(), 			candidate.getRate().get().getPeriod());
		assertEquals(rate.getValue(), 			candidate.getRate().get().getValue());
		assertEquals(introduction, 				candidate.getIntroduction());
		assertEquals(profilePhoto, 				candidate.getPhoto().get());
		
	}
	
}