package com.arenella.recruit.curriculum.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.arenella.recruit.curriculum.beans.RecruiterCredit;
import com.arenella.recruit.curriculum.services.CurriculumService;

/**
* Scheduler sends a command to reset the Recruiters Credits once per week
* @author K Parkings
*/
@Component
@EnableScheduling
public class CurriculumCreditScheduler {
	
	@Autowired
	private CurriculumService curriculumService;
	
	/**
	* Periodically grants new credits to Recruiters so that can carry
	* out operations in the system 
	*/
	@Scheduled(cron = "0 0 0 * * MON")
	public void grantCredits() {
		this.curriculumService.updateCredits(RecruiterCredit.DEFAULT_CREDITS);
	}
	
}