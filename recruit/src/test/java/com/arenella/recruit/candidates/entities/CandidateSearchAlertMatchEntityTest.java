package com.arenella.recruit.candidates.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.CandidateSearchAlertMatch;
import com.arenella.recruit.candidates.utils.CandidateSuggestionUtil.suggestion_accuracy;

/**
* Unit tests for the CandidateSearchAlertMatchEntity class
* @author K Parkings
*/
public class CandidateSearchAlertMatchEntityTest {

	private static final String 				ALERT_NAME 		= "alertName";
	private static final Long 					CANDIDATE_ID 	= 101L;
	private static final UUID 					MATCH_ID 		= UUID.randomUUID();
	private static final String 				RECRUITER_ID 	= "recruiter1";
	private static final String 				ROLE_SOUGHT 	= "Java Developer";
	private static final suggestion_accuracy 	ACCURACY 		= suggestion_accuracy.average;

	/**
	* Tests construction via builder
	* @throws Exception
	*/
	@Test
	public void testBuilder() throws Exception{
		
		CandidateSearchAlertMatchEntity match 
			= CandidateSearchAlertMatchEntity
				.builder()
					.alertName(ALERT_NAME)
					.candidateId(CANDIDATE_ID)
					.id(MATCH_ID)
					.recruiterId(RECRUITER_ID)
					.roleSought(ROLE_SOUGHT)
					.accuracy(ACCURACY)
				.build();
		
		assertEquals(ALERT_NAME, 	match.getAlertName());
		assertEquals(CANDIDATE_ID, 	match.getCandidateId());
		assertEquals(MATCH_ID, 		match.getId());
		assertEquals(RECRUITER_ID, 	match.getRecruiterId());
		assertEquals(ROLE_SOUGHT, 	match.getRoleSought());
		assertEquals(ACCURACY, 		match.getAccuracy());
		
	}
	
	/**
	* Tests conversion from Entity to Domain representation of Match
	* @throws Exception
	*/
	@Test
	public void testConvertFromEntity() throws Exception{

		CandidateSearchAlertMatchEntity entity 
			= CandidateSearchAlertMatchEntity
				.builder()
					.alertName(ALERT_NAME)
					.candidateId(CANDIDATE_ID)
					.id(MATCH_ID)
					.recruiterId(RECRUITER_ID)
					.roleSought(ROLE_SOUGHT)
					.accuracy(ACCURACY)
				.build();
		
		CandidateSearchAlertMatch match = CandidateSearchAlertMatchEntity.convertFromEntity(entity);
		
		assertEquals(ALERT_NAME, 	match.getAlertName());
		assertEquals(CANDIDATE_ID, 	match.getCandidateId());
		assertEquals(MATCH_ID, 		match.getId());
		assertEquals(RECRUITER_ID, 	match.getRecruiterId());
		assertEquals(ROLE_SOUGHT, 	match.getRoleSought());
		assertEquals(ACCURACY, 		match.getAccuracy());
		
	}
	
	/**
	* Tests conversion from Entity to Domain representation of Match
	* @throws Exception
	*/
	@Test
	public void testConvertToEntity() throws Exception{

		CandidateSearchAlertMatch match 
			= CandidateSearchAlertMatch
				.builder()
					.alertName(ALERT_NAME)
					.candidateId(CANDIDATE_ID)
					.id(MATCH_ID)
					.recruiterId(RECRUITER_ID)
					.roleSought(ROLE_SOUGHT)
					.accuracy(ACCURACY)
				.build();
		
		CandidateSearchAlertMatchEntity entity = CandidateSearchAlertMatchEntity.convertToEntity(match);
		
		assertEquals(ALERT_NAME, 	entity.getAlertName());
		assertEquals(CANDIDATE_ID, 	entity.getCandidateId());
		assertEquals(MATCH_ID, 		entity.getId());
		assertEquals(RECRUITER_ID, 	entity.getRecruiterId());
		assertEquals(ROLE_SOUGHT, 	entity.getRoleSought());
		assertEquals(ACCURACY, 		entity.getAccuracy());
		
	}
	
}