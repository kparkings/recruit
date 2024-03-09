package com.arenella.recruit.candidates.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

public class RecruiterCreditTest {

	final String 	RECRUITER_ID 		= "recruiter44";
	final int 		CREDITS 			= 4;
	final boolean 	PAID_SUBSCRIPTION 	= true;
	
	/**
	* Tests credit default values
	* @throws Exception
	*/
	@Test
	public void testDefaultCredits() throws Exception{
		
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
	public void testBuilder() throws Exception{
		
		RecruiterCredit recruiterCredit = 
				RecruiterCredit
				.builder()
					.recruiterId(RECRUITER_ID)
					.credits(CREDITS)
					.paidSubscription(PAID_SUBSCRIPTION)
				.build();
		
		assertEquals(RECRUITER_ID, 		recruiterCredit.getRecruiterId());
		assertEquals(CREDITS, 			recruiterCredit.getCredits());
		assertEquals(PAID_SUBSCRIPTION, recruiterCredit.hasPaidSubscription());
	}
	
	/**
	* Tests construction via the Builder
	* @throws Exception
	*/
	@Test
	public void testIncrementDecrementCredits() throws Exception{
		
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
	public void testSetters() throws Exception{
		
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