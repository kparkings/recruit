package com.arenella.recruit.candidates.services;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import jakarta.annotation.PostConstruct;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.arenella.recruit.candidates.adapters.CandidateCreatedEvent;
import com.arenella.recruit.candidates.adapters.ExternalEventPublisher;
import com.arenella.recruit.candidates.adapters.RequestSendAlertDailySummaryEmailCommand;
import com.arenella.recruit.candidates.beans.CandidateFilterOptions;
import com.arenella.recruit.candidates.beans.CandidateSearchAccuracyWrapper;
import com.arenella.recruit.candidates.beans.CandidateSearchAlertMatch;
import com.arenella.recruit.candidates.beans.SavedCandidateSearch;
import com.arenella.recruit.candidates.controllers.CandidateSearchRequest;
import com.arenella.recruit.candidates.dao.CandidateSearchAlertMatchDao;
import com.arenella.recruit.candidates.enums.RESULT_ORDER;
import com.arenella.recruit.candidates.utils.CandidateSuggestionUtil.suggestion_accuracy;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
* Utility for performing tests of CandidateSearchEvents against 
* newly uploaded Candidates
* @author K Parkings
*/
@Service
public class AlertTestUtilImpl implements AlertTestUtil{

	private final ScheduledExecutorService 		scheduler 			= Executors.newScheduledThreadPool(5);
	private final ScheduledExecutorService		endOfDayScheduler 	= Executors.newScheduledThreadPool(1);
	
	private CandidateService 					candidateService;
	private CandidateSearchAlertMatchDao		matchDao;
	private ExternalEventPublisher				commandPublisher;
	private ObjectMapper						objectMapper;
	
	/**
	* Constructor
	* @param candidateService	- Services realating to candidates
	* @param matchDao			- DAO operations for matched
	* @param commandPublisher	- Publishes commands to other services
	* @param objectMapper		- Object Mapper
	*/
	public AlertTestUtilImpl(CandidateService 					candidateService,
							 CandidateSearchAlertMatchDao		matchDao,
							 ExternalEventPublisher				commandPublisher,
							 ObjectMapper						objectMapper) {
		
		this.candidateService	= candidateService;
		this.matchDao 			= matchDao;
		this.commandPublisher 	= commandPublisher;
		this.objectMapper 		= objectMapper;
		
	}
	
	/**
	* Kicks off the Scheduler to package Matches 
	*/
	@PostConstruct
	public void init() {
		
		endOfDayScheduler.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				
				try {
					matchDao.getRecruitersWithMatches().stream().forEach(recruiterId ->{
					
						Set<CandidateSearchAlertMatch> matches = matchDao.getMatchesForRecruiters(recruiterId);
						
						constructAndSendDailyMatchSummaryEmail(recruiterId, matches);
						
						matches.stream().forEach(match ->  
							matchDao.deleteById(match.getId())
						);
						
						});
					
				}catch(Exception e) {
					e.printStackTrace();
				}
				
			}
			
		}, 1, 1, TimeUnit.DAYS);
		
	}
	
	/**
	* Creates and Sends an Event to trigger the sending of the email to the recruiter with 
	* a summary of the days matching candidates
	* @param matches
	*/
	private void constructAndSendDailyMatchSummaryEmail(String recruiterId, Set<CandidateSearchAlertMatch> matches) {
		
		RequestSendAlertDailySummaryEmailCommand command = new RequestSendAlertDailySummaryEmailCommand(recruiterId, matches);
		
		commandPublisher.publishRequestSendAlertDailySummaryEmailCommand(command);
		
	}
	
	/**
	* Refer to the AlertTestUtil interface for details 
	*/
	@Override
	public void testAgainstCandidateSearchAlerts(CandidateCreatedEvent event) {
		
		//1. Need method to get all SavedCandidateSearches that have email enabled
		Set<SavedCandidateSearch>  alerts = this.candidateService.fetchSavedCandidateSearchAlerts();
		
		alerts.forEach(alert -> 
			scheduler.execute(packageTest(event,alert))
		);
		
	}
	
	/**
	* Creates a Test for the CandidateSearchEvent / CandidateSearchAlert combination
	* @return
	*/
	private Runnable packageTest(CandidateCreatedEvent event, SavedCandidateSearch alert) {
		
		return new Runnable(){

			@Override
			public void run() {
				
				try {
					
					CandidateSearchRequest searchRequest = objectMapper.treeToValue(alert.getSearchRequest(), CandidateSearchRequest.class);
					
					CandidateFilterOptions filterOptions = 
							CandidateSearchRequest
								.convertToCandidateFilterOptions(
										searchRequest, 
										"candidateId", 
										RESULT_ORDER.asc);
					
					filterOptions.setCandidateIds(Set.of(event.getCandidateId()));
					
					Page<CandidateSearchAccuracyWrapper> results =  candidateService.getCandidateSuggestions(filterOptions, 1, false, true);
					
					results.stream().filter(r -> r.getAccuracySkills() != suggestion_accuracy.poor).forEach(match -> 
						matchDao.saveMatch(CandidateSearchAlertMatch
												.builder()
													.id(UUID.randomUUID())
													.alertName(alert.getSearchName())
													.candidateId(Long.valueOf(event.getCandidateId()))
													.recruiterId(alert.getUserId())
													.roleSought("")
													.accuracy(match.getAccuracySkills())
													.alertId(alert.getId())
												.build())
					);
					
				}catch(Exception e) {
					e.printStackTrace();
				}
				
			}
			
		};
		
	}

}