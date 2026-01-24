package com.arenella.recruit.messaging.utils;

//import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

//import org.springframework.stereotype.Component;

import com.arenella.recruit.candidates.repos.CandidateRepository;

//@Component
public class TemporaryDataMigrationScheduler {
	
	//private CandidateRepository canidateRepository;
	
	
	//private RecruiterDao recruiterDao;
	//private ParticipantService service;
	//private RecruiterProfileDao rpDao;
	
	public TemporaryDataMigrationScheduler(CandidateRepository canidateRepository) {
		//this.canidateRepository 	= canidateRepository;
		this.doRun();
	}
	
	public void doRun() {
	
		AtomicLong count = new AtomicLong(10000);
		System.out.println("Starting Canidate migration..");
		while (count.intValue() > 0) {
			System.out.println("processing: " + count.longValue());
			
			try {
				
				
				
					
					
			}catch(Exception e) {
				e.printStackTrace();
			}
			count.set(count.get()-1);
		}
	}
	
}