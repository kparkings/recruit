package com.arenella.recruit.recruiters.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the RecruiterCredit class
* @author K Parkings
*/
class RecruiterCreditTest {

	private static final String 	RECRUITER_ID 	= "recruiter44";
	private static final int 		CREDITS 		= 4;
	private static final boolean 	PAID_SUBSCRIPTION 	= true;
	
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
					.paidSubscription(PAID_SUBSCRIPTION)
				.build();
		
		assertEquals(RECRUITER_ID, 	recruiterCredit.getRecruiterId());
		assertEquals(CREDITS, 		recruiterCredit.getCredits());
		assertEquals(PAID_SUBSCRIPTION, recruiterCredit.hasPaidSubscription());
		
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
	
	/**
	* Tests setters
	* @throws Exception
	*/
	@Test
	void testSetters() {
		
		RecruiterCredit recruiterCredit = 
				RecruiterCredit
				.builder()
					.recruiterId(RECRUITER_ID)
					.credits(CREDITS)
					.paidSubscription(PAID_SUBSCRIPTION)
				.build();
		
		assertEquals(PAID_SUBSCRIPTION, recruiterCredit.hasPaidSubscription());
		
		recruiterCredit.setPaidSubscription(!PAID_SUBSCRIPTION);
	
		assertNotEquals(PAID_SUBSCRIPTION, recruiterCredit.hasPaidSubscription());
	
	}
	
}