package com.arenella.recruit.messaging.utils;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

import com.arenella.recruit.candidates.repos.CandidateRepository;
import com.arenella.recruit.messaging.beans.ChatParticipant;
import com.arenella.recruit.messaging.beans.ChatParticipant.CHAT_PARTICIPANT_TYPE;
import com.arenella.recruit.messaging.beans.ChatParticipant.ChatParticipantBuilder;
import com.arenella.recruit.messaging.beans.Photo;
import com.arenella.recruit.messaging.services.ParticipantService;
import com.arenella.recruit.recruiters.dao.RecruiterDao;
import com.arenella.recruit.recruiters.dao.RecruiterProfileDao;
import com.arenella.recruit.recruiters.services.RecruiterProfileService;

@Component
public class TemporaryDataMigrationScheduler {
	
	//@Autowired
	//private RecruiterService recruiterService;
	
	private CandidateRepository canidateRepository;
	
	private RecruiterDao recruiterDao;
	private ParticipantService service;
	private RecruiterProfileDao rpDao;
	
	public TemporaryDataMigrationScheduler(CandidateRepository canidateRepository, RecruiterDao recruiterDao, ParticipantService service, RecruiterProfileService rpService, RecruiterProfileDao rpDao) {
		this.canidateRepository 	= canidateRepository;
		this.recruiterDao 			= recruiterDao;
		this.service 				= service;
		this.rpDao 					= rpDao;
		this.doRun();
	}
	
	public void doRun() {
		System.out.println("Running..");
		
		recruiterDao.findAll().forEach(rec -> {
			
			try {
			
				ChatParticipantBuilder builder = ChatParticipant.builder();
				
				builder
					.participantId(rec.getUserId())
					.firstName(rec.getFirstName())
					.surname(rec.getSurname())
					.type(CHAT_PARTICIPANT_TYPE.RECRUITER);
				
				rpDao.findById(rec.getUserId()).ifPresent(rp -> {
					builder.photo(new Photo(rp.getPhotoBytes(), Photo.PHOTO_FORMAT.valueOf(rp.getPhotoFormat().toString().toUpperCase())));
				});
				
				service.persistParticpant(builder.build());
			}catch(Exception e) {
				e.printStackTrace();
			}
		
		});	
			
		System.out.println("Completed");
		
		AtomicInteger count = new AtomicInteger(10000);
		System.out.println("Starting Canidate migration..");
		while (count.intValue() > 0) {
			System.out.println("processing: " + count.intValue());
			
			try {
				this.canidateRepository.findCandidateById(count.intValue()).ifPresent(candidate -> {
					System.out.println("Found " + count.intValue());
					
					ChatParticipantBuilder builder = ChatParticipant.builder();
					
					builder
						.participantId(candidate.getCandidateId())
						.firstName(candidate.getFirstname())
						.surname(candidate.getSurname())
						.type(CHAT_PARTICIPANT_TYPE.CANDIDATE);
					
				
					candidate.getPhoto().ifPresent(imagePhoto -> {
						builder.photo(new Photo(imagePhoto.getImageBytes(), Photo.PHOTO_FORMAT.valueOf(imagePhoto.getFormat().toString().toUpperCase())));
					});
					
					service.persistParticpant(builder.build());
				});
			}catch(Exception e) {
				e.printStackTrace();
			}
			count.set(count.get()-1);
		}
	}
	
}