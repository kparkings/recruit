package com.arenella.recruit.recruiters.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.arenella.recruit.adapters.actions.GrantCreditCommand;
import com.arenella.recruit.recruiters.adapters.RecruitersExternalEventPublisher;

/**
* Scheduler sends a command to reset the Recruiters Credits once per week
* @author K Parkings
*/
@Component
@EnableScheduling
public class RecruiterCreditScheduler {
	
	@Autowired
	private RecruitersExternalEventPublisher eventPublisher;
	
	/**
	* Periodically grants new credits to Recruiters so that can carry
	* out operations in the system 
	*/
	@Scheduled(cron = "0 0 0 * * MON")
	public void grantCredits() {
		eventPublisher.publishGrantCreditCommand(new GrantCreditCommand());
	}
	
}