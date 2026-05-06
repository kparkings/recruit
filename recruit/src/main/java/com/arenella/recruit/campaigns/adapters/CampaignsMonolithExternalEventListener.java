package com.arenella.recruit.campaigns.adapters;

import org.springframework.stereotype.Service;

import com.arenella.recruit.adapters.events.CandidateDeletedEvent;
import com.arenella.recruit.adapters.events.CandidateUpdateEvent;
import com.arenella.recruit.adapters.events.CandidateUpdatedEvent;
import com.arenella.recruit.adapters.events.RecruiterCreatedEvent;
import com.arenella.recruit.adapters.events.RecruiterDeletedEvent;
import com.arenella.recruit.adapters.events.RecruiterNoOpenSubscriptionEvent;
import com.arenella.recruit.adapters.events.RecruiterUpdatedEvent;
import com.arenella.recruit.adapters.events.SubscriptionAddedEvent;
import com.arenella.recruit.campaign.dao.CandidateEntityDao;
import com.arenella.recruit.campaign.dao.ContactEntityDao;
import com.arenella.recruit.campaigns.beans.Contact.SubscriptionType;
import com.arenella.recruit.candidates.adapters.CandidateCreatedEvent;
import com.arenella.recruit.campaigns.beans.Candidate;
import com.arenella.recruit.campaigns.beans.Candidate.Type;
import com.arenella.recruit.campaigns.beans.Contact;

/**
* Listener handles incoming events form external services
*/
@Service
public class CampaignsMonolithExternalEventListener implements CampaignsExternalEventListener{

	private final ContactEntityDao contactDao;
	private final CandidateEntityDao candidateDao;
	
	/**
	* Constructor
	* @param contactDao 	- Repository for interacting with Contacts
	* @param candidateDao 	- Repository for interacting with Candidats
	*/
	public CampaignsMonolithExternalEventListener(ContactEntityDao contactDao, CandidateEntityDao candidateDao) {
		this.contactDao 	= contactDao;
		this.candidateDao 	= candidateDao;
	}
	
	/**
	* Refer to the interface CampaignsExternalEventListener for details 
	*/
	@Override
	public void listenForRecruiterCreatedEvent(RecruiterCreatedEvent event) {
		
		if (this.contactDao.fetchContact(event.getRecruiterId()).isPresent()) {
			return ;
		}
		
		Contact contact = new Contact (event.getRecruiterId(), event.getFirstName(), event.getSurname(), event.getEmail(), SubscriptionType.CREDIT);
		
		this.contactDao.saveContact(contact);
	}

	/**
	* Refer to the interface CampaignsExternalEventListener for details 
	*/
	@Override
	public void listenForRecruiterUpdatedEvent(RecruiterUpdatedEvent event) {
		this.contactDao.fetchContact(event.getRecruiterId()).ifPresent(_ ->{
			Contact contact = new Contact (event.getRecruiterId(), event.getFirstName(), event.getSurname(), event.getEmail(), SubscriptionType.CREDIT);
			this.contactDao.saveContact(contact);
		});
	}

	/**
	* Refer to the interface CampaignsExternalEventListener for details 
	*/
	@Override
	public void listenForSubscriptionAddedEvent(SubscriptionAddedEvent event) {
		
		this.contactDao.fetchContact(event.getRecruiterId()).ifPresent(c ->{
			Contact contact = new Contact (c.id(), c.firstName(), c.surname(), c.email(), event.getSubscriptionType().toString().equals("CREDIT_BASED_SUBSCRIPTION") ? SubscriptionType.CREDIT : SubscriptionType.PAID);
			this.contactDao.saveContact(contact);
		});
		
	}

	/**
	* Refer to the interface CampaignsExternalEventListener for details 
	*/
	@Override
	public void listenForRecruiterNoOpenSubscriptionsEvent(RecruiterNoOpenSubscriptionEvent event) {
		this.contactDao.fetchContact(event.geRecruiterId()).ifPresent(c ->{
			Contact contact = new Contact (c.id(), c.firstName(), c.surname(), c.email(), SubscriptionType.CREDIT);
			this.contactDao.saveContact(contact);
		});
	}

	/**
	* Refer to the interface CampaignsExternalEventListener for details 
	*/
	@Override
	public void listenForRecruiterAccountDeletedEvent(RecruiterDeletedEvent event) {
		this.contactDao.fetchContact(event.getRecruiterId()).ifPresent(_ ->
			this.contactDao.deleteById(event.getRecruiterId())
		);
	}

	/**
	* Refer to the interface CampaignsExternalEventListener for details 
	*/
	@Override
	public void listenForRecruiterDeletedEvent(RecruiterDeletedEvent event) {
		this.contactDao.fetchContact(event.getRecruiterId()).ifPresent(_ -> this.contactDao.deleteById(event.getRecruiterId()));
	}

	/**
	* Refer to the interface CampaignsExternalEventListener for details 
	*/
	@Override
	public void listenFor(CandidateUpdateEvent candidateUpdateEvent) {
		
		this.candidateDao.findCandidateById(""+candidateUpdateEvent.getCandidateId()).ifPresent(c -> 
			
			this.candidateDao.saveCandidate(Candidate
					.builder()
					.from(c)
					.firstName(candidateUpdateEvent.getFirstName())
					.surname(candidateUpdateEvent.getSurname())
					.jobTitle(candidateUpdateEvent.getRoleSought())
					.build())
		);
		
	}

	/**
	* Refer to the interface CampaignsExternalEventListener for details 
	*/
	@Override
	public void listenFor(CandidateDeletedEvent candidateDeletedEvent) {
		
		this.candidateDao.findCandidateById(""+candidateDeletedEvent.getCandidateId()).ifPresent(c -> 
		
			this.candidateDao.saveCandidate(Candidate
				.builder()
				.from(c)
				.email("NA")
				.surname("NA")
				.build())
		);
		
	}

	/**
	* Refer to the interface CampaignsExternalEventListener for details 
	*/
	@Override
	public void listenFor(CandidateUpdatedEvent candidateUpdatedEvent) {
		this.candidateDao.findCandidateById(candidateUpdatedEvent.getCandidateId()).ifPresent(c -> 
			
			this.candidateDao.saveCandidate(Candidate
					.builder()
					.from(c)
					.firstName(candidateUpdatedEvent.getFirstName())
					.surname(candidateUpdatedEvent.getSurname())
					.email(candidateUpdatedEvent.getEmail())
					.build())
		);
		
	}

	/**
	* Refer to the interface CampaignsExternalEventListener for details 
	*/
	@Override
	public void listenFor(CandidateCreatedEvent candidateCreatedEvent) {
	
		if (this.candidateDao.existsById(candidateCreatedEvent.getCandidateId())) {
			return;
		}
		
		this.candidateDao.saveCandidate(Candidate
				.builder()
					.countryCode(candidateCreatedEvent.getCountry().getIsoCode())
					.deletedFromSystem(false)
					.email(candidateCreatedEvent.getEmail())
					.firstName(candidateCreatedEvent.getFirstname())
					.id(candidateCreatedEvent.getCandidateId())
					.jobTitle(candidateCreatedEvent.getRoleSought())
					.surname(candidateCreatedEvent.getSurname())
					.type(Type.INTERNAL)
				.build());
		
	}

}