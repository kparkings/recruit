package com.arenella.recruit.candidates.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.adapters.RequestSendAlertDailySummaryEmailCommand.AlertSummary;
import com.arenella.recruit.candidates.beans.CandidateSearchAlertMatch;
import com.arenella.recruit.candidates.utils.CandidateSuggestionUtil.suggestion_accuracy;

/**
* Unit tests for the RequestSendAlertDailySummaryEmailCommand class
* @author K Parkings
*/
public class RequestSendAlertDailySummaryEmailCommandTest {

	/**
	* Tests the command is setup correctly with a summary of matches 
	* for each alert.
	* @throws Exception
	*/
	@Test
	public void testConstructor() throws Exception{
		
		final UUID 		alertId1 		= UUID.randomUUID();
		final UUID 		alertId2 		= UUID.randomUUID();
		final String 	recruiterEmail 	= "kparkings@gmail.com";
		
		final suggestion_accuracy m1Accuracy = suggestion_accuracy.perfect;
		final suggestion_accuracy m2Accuracy = suggestion_accuracy.excellent;
		final suggestion_accuracy m3Accuracy = suggestion_accuracy.excellent;
		final suggestion_accuracy m4Accuracy = suggestion_accuracy.good;
		
		final String alert1Name = "Alert1";
		final String alert2Name = "Alert2";
		
		final Long m1CanidateId = 1L;
		final Long m2CanidateId = 2L;
		final Long m3CanidateId = 2L;
		final Long m4CanidateId = 4L;
		
		final String m1Role = "C# Dev";
		final String m2Role = "Senior C# Dev";
		final String m3Role = "Java Developer";
		final String m4Role = "Java / Spring Dev";
		
		Set<CandidateSearchAlertMatch> matches = Set.of(
				CandidateSearchAlertMatch.builder().id(UUID.randomUUID()).alertId(alertId1).accuracy(m1Accuracy).alertName(alert1Name).candidateId(m1CanidateId).roleSought(m1Role).build(),
				CandidateSearchAlertMatch.builder().id(UUID.randomUUID()).alertId(alertId2).accuracy(m3Accuracy).alertName(alert2Name).candidateId(m3CanidateId).roleSought(m3Role).build(),
				CandidateSearchAlertMatch.builder().id(UUID.randomUUID()).alertId(alertId2).accuracy(m4Accuracy).alertName(alert2Name).candidateId(m4CanidateId).roleSought(m4Role).build(),
				CandidateSearchAlertMatch.builder().id(UUID.randomUUID()).alertId(alertId1).accuracy(m2Accuracy).alertName(alert1Name).candidateId(m2CanidateId).roleSought(m2Role).build()
				);
		
		RequestSendAlertDailySummaryEmailCommand command = new RequestSendAlertDailySummaryEmailCommand(recruiterEmail, matches);
	
		assertEquals(recruiterEmail, command.getRecruiterEmailAddress());
				
		Map<String, Set<AlertSummary>> summaries = command.getMatchesByAlert();
		
		AlertSummary match1 = summaries.get(alertId1.toString()).stream().filter(m -> m.getRoleSought().equals(m1Role)).findAny().orElseThrow();
		AlertSummary match2 = summaries.get(alertId1.toString()).stream().filter(m -> m.getRoleSought().equals(m2Role)).findAny().orElseThrow();
		
		AlertSummary match3 = summaries.get(alertId2.toString()).stream().filter(m -> m.getRoleSought().equals(m3Role)).findAny().orElseThrow();
		AlertSummary match4 = summaries.get(alertId2.toString()).stream().filter(m -> m.getRoleSought().equals(m4Role)).findAny().orElseThrow();
		
		assertEquals(m1CanidateId, match1.getCandidateId());
		assertEquals(m2CanidateId, match2.getCandidateId());
		assertEquals(m3CanidateId, match3.getCandidateId());
		assertEquals(m4CanidateId, match4.getCandidateId());
		
		assertEquals(m1Role, match1.getRoleSought());
		assertEquals(m2Role, match2.getRoleSought());
		assertEquals(m3Role, match3.getRoleSought());
		assertEquals(m4Role, match4.getRoleSought());
		
		
	}
	
}
