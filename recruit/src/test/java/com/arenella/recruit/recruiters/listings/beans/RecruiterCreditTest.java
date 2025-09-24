package com.arenella.recruit.recruiters.listings.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.listings.beans.RecruiterCredit;

class RecruiterCreditTest {

	private static final String 	RECRUITER_ID 	= "recruiter44";
	private static final int 		CREDITS 		= 4;
	
	/**
	* Tests credit default values
	* @throws Exception
	*/
	@Test
	void testDefaultCredits() {
		
		RecruiterCredit recruiterCredit = 
			RecruiterCredit
			.builder()
			.build();

		assertEquals(RecruiterCredit.DEFAULT_CREDITS, recruiterCredit.getCredits());
		
	}
	
	/**
	* Tests construction via the Builder
	* @throws Exception
	*/
	@Test
	void testBuilder() {
		
		RecruiterCredit recruiterCredit = 
				RecruiterCredit
				.builder()
					.recruiterId(RECRUITER_ID)
					.credits(CREDITS)
				.build();
		
		assertEquals(RECRUITER_ID, 	recruiterCredit.getRecruiterId());
		assertEquals(CREDITS, 		recruiterCredit.getCredits());
		
	}
	
	/**
	* Tests construction via the Builder
	* @throws Exception
	*/
	@Test
	void testIncrementDecrementCredits() {
		
		final int newCreditsAmount = 2;
		
		RecruiterCredit recruiterCredit = 
				RecruiterCredit
				.builder()
					.recruiterId(RECRUITER_ID)
					.credits(CREDITS)
				.build();
		
		assertEquals(RECRUITER_ID, 	recruiterCredit.getRecruiterId());
		assertEquals(CREDITS, 		recruiterCredit.getCredits());
		
		recruiterCredit.setCredits(newCreditsAmount);
		
		assertEquals(newCreditsAmount, 	recruiterCredit.getCredits());
		
		recruiterCredit.decrementCredits();
		
		assertEquals(1, 	recruiterCredit.getCredits());
		
		recruiterCredit.decrementCredits();
		
		assertEquals(0, 	recruiterCredit.getCredits());
		
		recruiterCredit.decrementCredits();
		
		assertEquals(0, 	recruiterCredit.getCredits());
		
	}
	
}
