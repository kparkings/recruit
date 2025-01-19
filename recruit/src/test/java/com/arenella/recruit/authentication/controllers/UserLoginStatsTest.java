package com.arenella.recruit.authentication.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the UserLoginStats class 
*/
class UserLoginStatsTest {
	
	/**
	* Tests construction of Stats
	*/
	@Test
	void testBulder() {
		
		UserLoginStats stats = UserLoginStats
				.builder()
				.loginSummaries(Set.of(
					new UserLoginSummary("rec1", 1, 5, 1, 2),
					new UserLoginSummary("rec2", 1, 5, 1, 0),
					new UserLoginSummary("rec3", 1, 0, 0, 2),
					new UserLoginSummary("rec4", 1, 0, 0, 0),
					new UserLoginSummary("rec5", 0, 0, 0, 0),
					new UserLoginSummary("rec6", 0, 5, 1, 2),
					new UserLoginSummary("rec7", 1, 0, 0, 0),
					new UserLoginSummary("rec8", 1, 0, 0, 2),
					new UserLoginSummary("rec9", 1, 0, 0, 1),
					new UserLoginSummary("rec0", 0, 0, 0, 0)
				))
				.build();
		
		assertEquals(1, stats.getActivityHigh());
		assertEquals(5, stats.getActivityMedium());
		assertEquals(2, stats.getActivityLow());
		assertEquals(2, stats.getActivityNone());
		
	}

}