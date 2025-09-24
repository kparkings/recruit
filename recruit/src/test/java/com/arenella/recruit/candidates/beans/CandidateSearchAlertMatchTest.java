package com.arenella.recruit.candidates.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.utils.CandidateSuggestionUtil.suggestion_accuracy;

/**
* Unit tests for the CandidateSearchAlertMatch class
* @author K Parkings
*/
public class CandidateSearchAlertMatchTest {

	/**
	* Tests construction via builder
	* @throws Exception
	*/
	@Test
	void testBuilder() {
		
		final UUID					alertId			= UUID.randomUUID();
		final String 				alertName 		= "alertName";
		final Long 					candidateId 	= 1L;
		final UUID 					matchId 		= UUID.randomUUID();
		final String 				recruiterId 	= "recruiter1";
		final String 				roleSought 		= "Java Developer";
		final suggestion_accuracy 	accuracy 		= suggestion_accuracy.average;
		
		CandidateSearchAlertMatch match 
			= CandidateSearchAlertMatch
				.builder()
					.alertId(alertId)
					.alertName(alertName)
					.candidateId(candidateId)
					.id(matchId)
					.recruiterId(recruiterId)
					.roleSought(roleSought)
					.accuracy(accuracy)
				.build();
		
		assertEquals(alertId, 		match.getAlertId());
		assertEquals(alertName, 	match.getAlertName());
		assertEquals(candidateId, 	match.getCandidateId());
		assertEquals(matchId, 		match.getId());
		assertEquals(recruiterId, 	match.getRecruiterId());
		assertEquals(roleSought, 	match.getRoleSought());
		assertEquals(accuracy, 		match.getAccuracy());
		
	}
	
}