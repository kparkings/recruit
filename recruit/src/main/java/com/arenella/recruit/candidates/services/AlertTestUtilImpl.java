package com.arenella.recruit.candidates.services;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arenella.recruit.candidates.adapters.CandidateCreatedEvent;
import com.arenella.recruit.candidates.adapters.ExternalEventPublisher;
import com.arenella.recruit.candidates.adapters.RequestSendAlertDailySummaryEmailCommand;
import com.arenella.recruit.candidates.beans.CandidateFilterOptions;
import com.arenella.recruit.candidates.beans.CandidateSearchAlert;
import com.arenella.recruit.candidates.beans.CandidateSearchAlertMatch;
import com.arenella.recruit.candidates.beans.Language.LEVEL;
import com.arenella.recruit.candidates.dao.CandidateSearchAlertDao;
import com.arenella.recruit.candidates.dao.CandidateSearchAlertMatchDao;
import com.arenella.recruit.candidates.utils.CandidateSuggestionUtil.suggestion_accuracy;

/**
* Utility for performing tests of CandidateSearchEvents against 
* newly uploaded Candidates
* @author K Parkings
*/
@Service
public class AlertTestUtilImpl implements AlertTestUtil{

	private final ScheduledExecutorService 		scheduler 			= Executors.newScheduledThreadPool(5);
	private final ScheduledExecutorService		endOfDayScheduler 	= Executors.newScheduledThreadPool(1);
	
	@Autowired
	private CandidateSearchAlertDao 			alertDao;
	
	@Autowired
	private CandidateService 					candidateService;
	
	@Autowired
	private CandidateSearchAlertMatchDao		matchDao;
	
	@Autowired
	private ExternalEventPublisher				commandPublisher;
	
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
						
						matches.stream().forEach(match -> { 
							matchDao.deleteById(match.getId());
						});
						
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
		
		this.alertDao.fetchAlerts().forEach(alert -> {
			scheduler.execute(packageTest(event,alert));
		});
		
	}
	
	/**
	* Creates a Test for the CandidateSearchEvent / CandidateSearchAlert combination
	* @return
	*/
	private Runnable packageTest(CandidateCreatedEvent event, CandidateSearchAlert alert) {
		
		return new Runnable(){

			@Override
			public void run() {
				
				try {
					CandidateFilterOptions filterOptions = 
							CandidateFilterOptions
								.builder()
									.candidateIds(Set.of(event.getCandidateId()))
									.countries(alert.getCountries())
									.dutch(alert.getDutch()		== LEVEL.UNKNOWN ? null : alert.getDutch())
									.english(alert.getEnglish()	== LEVEL.UNKNOWN ? null : alert.getEnglish())
									.french(alert.getFrench()	== LEVEL.UNKNOWN ? null : alert.getFrench())
									.freelance(alert.getFreelance().isEmpty() ? null : alert.getFreelance().get())
									.functions(alert.getFunctions())
									.perm(alert.getPerm().isEmpty() ? null : alert.getPerm().get())
									.skills(alert.getSkills())
									.yearsExperienceGtEq(alert.getYearsExperienceGtEq())
									.yearsExperienceLtEq(alert.getyearsExperienceLtEq())
								.build();
					
					suggestion_accuracy accuracy = null;
					
					accuracy = candidateService.doTestCandidateAlert(Long.valueOf(event.getCandidateId()), filterOptions);
				
					if (accuracy !=suggestion_accuracy.poor) {
						matchDao.saveMatch(CandidateSearchAlertMatch
								.builder()
									.id(UUID.randomUUID())
									.alertName(alert.getAlertName())
									.candidateId(Long.valueOf(event.getCandidateId()))
									.recruiterId(alert.getRecruiterId())
									.roleSought(event.getRoleSought())
									.accuracy(accuracy)
									.alertId(alert.getAlertId())
								.build());
					}
					
				}catch(Exception e) {
					e.printStackTrace();
				}
				
			}
			
		};
		
	}

}