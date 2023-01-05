package com.arenella.recruit.candidates.adapters;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.arenella.recruit.candidates.beans.CandidateSearchAlertMatch;
import com.arenella.recruit.candidates.utils.CandidateSuggestionUtil.suggestion_accuracy;

/**
* Command to instruct service to send an email to a recruiter with the daily summary
* of new Candidates matching one or more of their CandidateSearchAlert specifications
* @author K Parkings
*/
public class RequestSendAlertDailySummaryEmailCommand {

	private String 							recruiterId;
	private Map<String, Set<AlertSummary>> 	matchesByAlert 			= new LinkedHashMap<>();
	
	/**
	* Constructor - Separates matches into summary per Alert
	* @param recruiterId 	- Unique id of the recruiter owning the Alerts that produced the matches
	* @param matches 		- Details of Matches between Alert and Candidate 
	*/
	public RequestSendAlertDailySummaryEmailCommand(String recruiterId, Set<CandidateSearchAlertMatch> matches) {
		
		this.recruiterId = recruiterId;
		matches.stream().forEach(match -> {
			
			if (!matchesByAlert.containsKey(match.getAlertId().toString())){
				matchesByAlert.put(match.getAlertId().toString(), Set.of(new AlertSummary(match)));
			} else {
				
				Set<AlertSummary> summary = new LinkedHashSet<>();
				
				summary.addAll(matchesByAlert.get(match.getAlertId().toString()));
				
				summary.add(new AlertSummary(match));
			
				matchesByAlert.put(match.getAlertId().toString(), summary);
				
			}
			
		});
		
	}
	
	/**
	* Returns the id of the recruiter that owns the Alerts
	* @return
	*/
	public String getRecruiterId() {
		return this.recruiterId;
	}
	
	/**
	* Returns a summary of Alerts and their matching candidates
	* @return
	*/
	public Map<String, Set<AlertSummary>> getMatchesByAlert(){
		return this.matchesByAlert;
	}
	
	/**
	* Details of Matches for a single Alert
	* @author K Parkings
	*/
	public static class AlertSummary{
		
		private String 					alertName;
		private Long 					candidateId;
		private String					roleSought;
		private suggestion_accuracy 	accuracy;
		
		/**
		* Constructor
		* @param match - Details of a match for the alert
		*/
		public AlertSummary(CandidateSearchAlertMatch match) {
			this.alertName 		= match.getAlertName();
			this.candidateId 	= match.getCandidateId();
			this.roleSought 	= match.getRoleSought();
			this.accuracy 		= match.getAccuracy();
		}
		
		/**
		* Returns the name of the Alert
		* @return Alert name
		*/
		public String getAlertName(){
			return this.alertName;
		}
		
		/**
		* Returns the id of the Candidate the Alert matched on
		* @return candiate's unique id
		*/
		public Long getCandidateId(){
			return this.candidateId;
		}
		
		/**
		* Returns the role the Candidate is looking for
		* @return role performed by Candidate
		*/
		public String getRoleSought(){
			return this.roleSought;
		}
		
		/**
		* Returns the accuracy of the match against the Alert specification
		* @return how accurate the candidate matches the recuiter's wishes
		*/
		public suggestion_accuracy getAccuracy(){
			return this.accuracy;
		}
		
	}
	
}