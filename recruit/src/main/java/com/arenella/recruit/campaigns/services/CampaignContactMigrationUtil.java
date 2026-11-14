package com.arenella.recruit.campaigns.services;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.arenella.recruit.campaign.dao.CampaignDao;
import com.arenella.recruit.campaign.dao.CandidateEntityDao;
import com.arenella.recruit.campaign.dao.CandidateGroupEntityDao;
import com.arenella.recruit.campaign.dao.ContactEntityDao;
import com.arenella.recruit.campaigns.beans.CandidateGroup;
import com.arenella.recruit.campaigns.beans.Campaign;
import com.arenella.recruit.campaigns.beans.Candidate.Type;
import com.arenella.recruit.campaigns.beans.Contact.SubscriptionType;
import com.arenella.recruit.campaigns.entities.CampaignContactEntity;
import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.CandidateFilterOptions;
import com.arenella.recruit.candidates.repos.CandidateRepository;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_status;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_type;
import com.arenella.recruit.recruiters.dao.RecruiterDao;

import co.elastic.clients.elasticsearch.ElasticsearchClient;

/**
* Temporary tool for migration Contact data to the Campaign schema 
*/
@Service
@Transactional
public class CampaignContactMigrationUtil {

	private final CandidateRepository 		candidateRepo;
	private final ElasticsearchClient 		esClient;
	private final CandidateEntityDao		candidateEntityRepo;
	private final RecruiterDao				recruiterDao;
	private final ContactEntityDao			contactEntityDao;
	private final CampaignService			campaignService;
	private final CampaignDao				campaignDao;
	private final CandidateGroupEntityDao 	candidateGroupEntityDao;
	
	public CampaignContactMigrationUtil(CandidateRepository candidateRepo, RecruiterDao recruiterRepo, ElasticsearchClient 	esClient, CandidateEntityDao candidateEntityRepo, RecruiterDao recruiterDao, ContactEntityDao contactEntityDao, CampaignService campaignService, CampaignDao campaignDao, CandidateGroupEntityDao candidateGroupEntityDao) {
		this.candidateRepo 				= candidateRepo;
		this.esClient 					= esClient;
		this.candidateEntityRepo 		= candidateEntityRepo;
		this.recruiterDao 				= recruiterDao;
		this.contactEntityDao 			= contactEntityDao;
		this.campaignService 			= campaignService;
		this.campaignDao 				= campaignDao;
		this.candidateGroupEntityDao	= candidateGroupEntityDao;
		
	}
	
	@PostConstruct
	public void setupGroupForExistingCampaignCandidates() {
		
		this.campaignDao.wtf().stream().forEach(group -> {
			this.candidateGroupEntityDao.saveGrouping(group);
		});
		
	}
	
	
	
	//public void runMigration() throws Exception {
		
		
	//	//Recruiters
	//	this.recruiterDao.findAll().forEach(recruiter -> {
	//		System.out.println("Processing Recruiter " + recruiter.getUserId());
	//		try {
	//			AtomicReference<SubscriptionType> subType = new AtomicReference<>(SubscriptionType.CREDIT);
	//				
	//			
	//			recruiter.getSubscriptions().stream().filter(s -> s.isCurrentSubscription()).findAny().ifPresent(s -> {
	//				if (s.getType() != subscription_type.CREDIT_BASED_SUBSCRIPTION 
	//						&& (
	//								s.getStatus() == subscription_status.ACTIVE
	//								|| s.getStatus() == subscription_status.ACTIVE_PENDING_PAYMENT
	//								|| s.getStatus() == subscription_status.ACTIVE_INVOICE_SENT)) {
	//					subType.set(SubscriptionType.PAID);
	//				}
	//			});
	//			
	//			this.contactEntityDao.save(CampaignContactEntity
	//					.builder()
	//						.id(recruiter.getUserId())
	//						.firstName(recruiter.getFirstName())
	//						.surname(recruiter.getSurname())
	//						.email(recruiter.getEmail())
	//						.subscriptionType(subType.get())
	//					.build());
	//		}catch(Exception e) {
	//			e.printStackTrace();
	//		}
	//	});
		
		
		
		
		
		
		
		
		
		
		
		
		//int firstRecordInPage = 0;
		//int maxRecordsInPage = 500;
		//AtomicBoolean guard = new AtomicBoolean(false);
		//while(!guard.get()) {
			
		//	Page<Candidate> batch = candidateRepo.findAll(CandidateFilterOptions.builder().build(), esClient, firstRecordInPage, maxRecordsInPage);
			
		//	firstRecordInPage = (firstRecordInPage + 500)-1;
			
		//	if(batch.getContent().isEmpty()) {
		//		guard.set(true);
		//		return;
		//	} 
			
		//	batch.getContent().stream().forEach(candidate -> {
		//		System.out.println("Processing candidate:" + candidate.getCandidateId());
			
		//		try {
		//		this.candidateEntityRepo.saveCandidate(com.arenella.recruit.campaigns.beans.Candidate
		//				.builder()
		//					.id(candidate.getCandidateId())
		//					.firstName(candidate.getFirstname())
		//					.surname(candidate.getSurname())
		//					.email(candidate.getEmail())
		//					.jobTitle(candidate.getRoleSought())
		//					.countryCode(candidate.getCountry().getIsoCode())
		//					.deletedFromSystem(false)
		//					.type(Type.INTERNAL)		
		//					.created(LocalDateTime.now())
							
		//				.build());
				
		//		}catch(Exception e) {
		//			e.printStackTrace();
		//		}
		//	});
			
		//}
		
		
		
		
	//}
	
	//1. Migrate all recruiters to contacts table
	//1. Migrate all candidats to candidates table
	
}
