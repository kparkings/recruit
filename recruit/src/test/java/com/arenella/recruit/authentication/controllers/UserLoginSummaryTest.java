package com.arenella.recruit.authentication.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the UserLoginSummary class 
*/
class UserLoginSummaryTest {

	/**
	* Tests construction  
	*/
	@Test
	void testConstruction() {
		
		UserLoginSummary rec1 = new UserLoginSummary("rec1", 1, 2, 3,4);
		UserLoginSummary rec2 = new UserLoginSummary("rec2", 1, 2, 3,0);
		UserLoginSummary rec3 = new UserLoginSummary("rec3", 1, 2, 0,0);
		UserLoginSummary rec4 = new UserLoginSummary("rec4", 1, 0, 0,0);
		UserLoginSummary rec5 = new UserLoginSummary("rec5", 0, 0, 0,0);
		
		assertEquals("rec1", rec1.getUserId());
		assertEquals(UserLoginSummary.ActivityLevel.HIGH, 	rec1.getActivityLevel());
		assertEquals(UserLoginSummary.ActivityLevel.MEDIUM, rec2.getActivityLevel());
		assertEquals(UserLoginSummary.ActivityLevel.MEDIUM, rec3.getActivityLevel());
		assertEquals(UserLoginSummary.ActivityLevel.LOW, 	rec4.getActivityLevel());
		assertEquals(UserLoginSummary.ActivityLevel.NONE, 	rec5.getActivityLevel());
	
		assertEquals(1, rec1.getLoginsThisWeeek());
		assertEquals(2, rec1.getLoginsLast30Days());
		assertEquals(3, rec1.getLoginsLast60Days());
		assertEquals(4, rec1.getLoginsLast90Days());
	}
	
}
