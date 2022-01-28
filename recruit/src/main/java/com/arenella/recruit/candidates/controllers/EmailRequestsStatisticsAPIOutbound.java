package com.arenella.recruit.candidates.controllers;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.arenella.recruit.curriculum.beans.CandidateEmailRequestEvent;

/**
* Provides Stats relating to email requests formatted to be
* returned by the API
* @author K Parkings
*/
public class EmailRequestsStatisticsAPIOutbound {

	public Map<String,Integer> recruiterRequestsDaily 	= new LinkedHashMap<>();
	public Map<String,Integer> recruiterRequestsWeekly 	= new LinkedHashMap<>();
	public Map<String,Integer> weeklyRequests 			= new LinkedHashMap<>();
	
	/**
	* Constructor. Processed events into usefule stats
	* @param events
	*/
	public EmailRequestsStatisticsAPIOutbound(Set<CandidateEmailRequestEvent> events) {
		
		events.stream().forEach(event -> {
			this.processRecruiterRequests(event);
			this.processWeeklyRequests(event);
		});
		
	}
	
	/**
	* Returns stats for number of email requests per recruiter for the current day
	* @param event
	*/
	private void processRecruiterRequests(CandidateEmailRequestEvent event) {
		
		if (event.getCreated().isAfter(LocalDate.now().atStartOfDay())) {
	
			if (recruiterRequestsDaily.containsKey(event.getRecruiterId())) {
				recruiterRequestsDaily.put(event.getRecruiterId(), recruiterRequestsDaily.get(event.getRecruiterId()) + 1 );
			} else {
				recruiterRequestsDaily.put(event.getRecruiterId(), 1 );
			}

		}
		
		if (event.getCreated().isAfter(LocalDateTime.now().with(DayOfWeek.MONDAY).minusDays(1))) {
			
			if (recruiterRequestsWeekly.containsKey(event.getRecruiterId())) {
				recruiterRequestsWeekly.put(event.getRecruiterId(), recruiterRequestsWeekly.get(event.getRecruiterId()) + 1 );
			} else {
				recruiterRequestsWeekly.put(event.getRecruiterId(), 1 );
			}

		}
		
	}

	/**
	* Returns stats for Email request by requirers per week
	* @param event
	*/
	private void processWeeklyRequests(CandidateEmailRequestEvent event) {
		
		TemporalField 	weekOfYear = WeekFields.of(Locale.getDefault()).weekOfYear();
		int 			weekNumber = event.getCreated().get(weekOfYear);
		
		String 			key = event.getCreated().getYear() + " - " + weekNumber;
		
		if (weeklyRequests.containsKey(key)) {
			weeklyRequests.put(key, weeklyRequests.get(key) + 1 );
		} else {
			weeklyRequests.put(key, 1);
		}
		
	}
	
	/**
	* Requests per recruiter current day
	* @return stats
	*/
	public Map<String, Integer> getRecruiterRequestsDaily() {
		return this.recruiterRequestsDaily;
	}
	
	/**
	* Requests per recruiter current week
	* @return stats
	*/
	public Map<String, Integer> getRecruiterRequestsWeekly() {
		return this.recruiterRequestsWeekly;
	}
	
	/**
	* Returns the stats for requests per week
	* @return stats
	*/
	public Map<String,Integer> getWeeklyRequests(){
		return weeklyRequests;
	}
	
}
