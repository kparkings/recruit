package com.arenella.recruit.campaigns.adapters;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import com.arenella.recruit.adapters.events.CandidateDeletedEvent;
import com.arenella.recruit.adapters.events.CandidateUpdateEvent;
import com.arenella.recruit.adapters.events.CandidateUpdatedEvent;
import com.arenella.recruit.adapters.events.IMMessageCreatedEvent;
import com.arenella.recruit.adapters.events.IMMessageReadEvent;
import com.arenella.recruit.adapters.events.RecruiterCreatedEvent;
import com.arenella.recruit.adapters.events.RecruiterDeletedEvent;
import com.arenella.recruit.adapters.events.RecruiterNoOpenSubscriptionEvent;
import com.arenella.recruit.adapters.events.RecruiterUpdatedEvent;
import com.arenella.recruit.adapters.events.SubscriptionAddedEvent;
import com.arenella.recruit.campaign.dao.CampaignDao;
import com.arenella.recruit.campaign.dao.CampaignIMNotificationDao;
import com.arenella.recruit.campaign.dao.CandidateEntityDao;
import com.arenella.recruit.campaign.dao.ContactEntityDao;
import com.arenella.recruit.campaign.dao.RoleDao;
import com.arenella.recruit.campaigns.beans.Campaign;
import com.arenella.recruit.campaigns.beans.Candidate;
import com.arenella.recruit.campaigns.beans.Contact;
import com.arenella.recruit.campaigns.beans.Contact.SubscriptionType;
import com.arenella.recruit.campaigns.beans.IMNotification;
import com.arenella.recruit.campaigns.beans.Participation;
import com.arenella.recruit.campaigns.beans.Role;
import com.arenella.recruit.candidates.adapters.CandidateCreatedEvent;
import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_type;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

/**
* Unit tests for the CampaignsMonolithExternalEventPublisher class 
*/
@ExtendWith(MockitoExtension.class)
class CampaignsMonolithExternalEventListenerTest {

	@Mock
	private ContactEntityDao 						mockContactDao;
	
	@Mock
	private CandidateEntityDao 						mockCandidateDao;
	
	@Mock
	private  CampaignDao							mockCampaignDao;
	
	@Mock
	private  RoleDao								mockRoleDao;
	
	@Mock
	private  CampaignIMNotificationDao 				mockImNotificationDao;
	
	@InjectMocks
	private CampaignsMonolithExternalEventListener 	listener;
	
	/**
	* Tests handling of adding a new contact where contact already exists
	*/
	@Test
	void testListenForRecruiterCreatedEventContactExists() {
		
		final String contactId = "rec11";
		
		when(this.mockContactDao.fetchContact(contactId)).thenReturn(Optional.of(new Contact(contactId, "", "", "", SubscriptionType.PAID)));
		
		this.listener.listenForRecruiterCreatedEvent(RecruiterCreatedEvent.builder().recruiterId(contactId).build());
		
		verify(this.mockContactDao, never()).saveContact(any(Contact.class));
		
	}
	
	/**
	* Tests handling of adding a new contact where contact 
	* does not already exists
	*/
	@Test
	void testListenForRecruiterCreatedEvent() {
	
		String contactId = "rec11";
		
		when(this.mockContactDao.fetchContact(contactId)).thenReturn(Optional.empty());
		
		this.listener.listenForRecruiterCreatedEvent(RecruiterCreatedEvent.builder().recruiterId(contactId).build());
		
		verify(this.mockContactDao).saveContact(any(Contact.class));
		
	}
	
	/**
	* Test update where the contact exists 
	*/
	@Test
	void testListenForRecruiterUpdatedEventExistingContact() {
		
		String contactId = "rec11";
		
		when(this.mockContactDao.fetchContact(contactId)).thenReturn(Optional.of(new Contact(contactId, "", "", "", SubscriptionType.PAID)));
		
		this.listener.listenForRecruiterUpdatedEvent(RecruiterUpdatedEvent.builder().recruiterId(contactId).build());
		
		verify(this.mockContactDao).saveContact(any(Contact.class));
		
	}
	
	/**
	* Tests if contact doesn't exist no update is attempted 
	*/
	@Test
	void testListenForRecruiterUpdatedEventNonExistingContact() {
		
		String contactId = "rec11";
		
		when(this.mockContactDao.fetchContact(contactId)).thenReturn(Optional.empty());
		
		this.listener.listenForRecruiterUpdatedEvent(RecruiterUpdatedEvent.builder().recruiterId(contactId).build());
		
		verify(this.mockContactDao, never()).saveContact(any(Contact.class));
		
	}
	
	/**
	* Test update where the contact exists and subscription is a paid type
	*/
	@Test
	void testListenForSubscriptionAddedEventExistingContactPaidSubscription() {
		
		ArgumentCaptor<Contact> contactArgCapt = ArgumentCaptor.forClass(Contact.class);
		
		String contactId = "rec11";
		
		doNothing().when(this.mockContactDao).saveContact(contactArgCapt.capture());
		when(this.mockContactDao.fetchContact(contactId)).thenReturn(Optional.of(new Contact(contactId, "", "", "", SubscriptionType.PAID)));
		
		this.listener.listenForSubscriptionAddedEvent(new SubscriptionAddedEvent(contactId, subscription_type.CREDIT_BASED_SUBSCRIPTION));
		
		verify(this.mockContactDao).saveContact(any(Contact.class));
		
		assertEquals(SubscriptionType.CREDIT, contactArgCapt.getValue().subscriptionType());
		
	}
	
	/**
	* Test update where the contact exists and subscription is not a paid type
	*/
	@Test
	void testListenForSubscriptionAddedEventExistingContactCreditSubscription() {
		
		ArgumentCaptor<Contact> contactArgCapt = ArgumentCaptor.forClass(Contact.class);
		
		String contactId = "rec11";
		
		doNothing().when(this.mockContactDao).saveContact(contactArgCapt.capture());
		when(this.mockContactDao.fetchContact(contactId)).thenReturn(Optional.of(new Contact(contactId, "", "", "", SubscriptionType.PAID)));
		
		this.listener.listenForSubscriptionAddedEvent(new SubscriptionAddedEvent(contactId, subscription_type.SIX_MONTHS_SUBSCRIPTION));
		
		verify(this.mockContactDao).saveContact(any(Contact.class));
		
		assertEquals(SubscriptionType.PAID, contactArgCapt.getValue().subscriptionType());
		
	}
	
	/**
	* Tests if contact doesn't exist no update is attempted 
	*/
	@Test
	void testListenForSubscriptionAddedEventNonExistingContact() {
		
		String contactId = "rec11";
		
		when(this.mockContactDao.fetchContact(contactId)).thenReturn(Optional.empty());
		
		this.listener.listenForSubscriptionAddedEvent(new SubscriptionAddedEvent(contactId, subscription_type.CREDIT_BASED_SUBSCRIPTION));
		
		verify(this.mockContactDao, never()).saveContact(any(Contact.class));
		
	}

	/**
	* Test update where the contact exists and subscription is not a paid type
	*/
	@Test
	void testListenForRecruiterNoOpenSubscriptionsEventExistingContactCreditSubscription() {
		
		ArgumentCaptor<Contact> contactArgCapt = ArgumentCaptor.forClass(Contact.class);
		
		String contactId = "rec11";
		
		doNothing().when(this.mockContactDao).saveContact(contactArgCapt.capture());
		when(this.mockContactDao.fetchContact(contactId)).thenReturn(Optional.of(new Contact(contactId, "", "", "", SubscriptionType.PAID)));
		
		this.listener.listenForRecruiterNoOpenSubscriptionsEvent(new RecruiterNoOpenSubscriptionEvent(contactId));
		
		verify(this.mockContactDao).saveContact(any(Contact.class));
		
		assertEquals(SubscriptionType.CREDIT, contactArgCapt.getValue().subscriptionType());
		
	}
	
	/**
	* Tests if contact doesn't exist no update is attempted 
	*/
	@Test
	void testListenForRecruiterNoOpenSubscriptionsEventNonExistingContact() {
		
		String contactId = "rec11";
		
		when(this.mockContactDao.fetchContact(contactId)).thenReturn(Optional.empty());
		
		this.listener.listenForRecruiterNoOpenSubscriptionsEvent(new RecruiterNoOpenSubscriptionEvent(contactId));
		
		verify(this.mockContactDao, never()).saveContact(any(Contact.class));
		
	}
	
	/**
	* Test delete where the contact exists and subscription is not a paid type
	*/
	@Test
	void testListenForRecruiterAccountDeletedEventExistingContactCreditSubscription() {
		
		String contactId = "rec11";
		
		when(this.mockContactDao.fetchContact(contactId)).thenReturn(Optional.of(new Contact(contactId, "", "", "", SubscriptionType.PAID)));
		
		this.listener.listenForRecruiterAccountDeletedEvent(new RecruiterDeletedEvent(contactId));
		
		verify(this.mockContactDao).deleteById(contactId);
		
	}
	
	/**
	* Tests if contact doesn't exist no delete is attempted 
	*/
	@Test
	void testListenForRecruiterAccountDeletedEventNonExistingContact() {
		
		String contactId = "rec11";
		
		when(this.mockContactDao.fetchContact(contactId)).thenReturn(Optional.empty());
		
		this.listener.listenForRecruiterAccountDeletedEvent(new RecruiterDeletedEvent(contactId));
		
		verify(this.mockContactDao, never()).deleteById(contactId);
		
	}

	/**
	* Test delete where the contact exists and subscription is not a paid type
	*/
	@Test
	void testListenForRecruiterDeletedEventExistingContactCreditSubscription() {
		
		String contactId = "rec11";
		
		when(this.mockContactDao.fetchContact(contactId)).thenReturn(Optional.of(new Contact(contactId, "", "", "", SubscriptionType.PAID)));
		
		this.listener.listenForRecruiterDeletedEvent(new RecruiterDeletedEvent(contactId));
		
		verify(this.mockContactDao).deleteById(contactId);
		
	}
	
	/**
	* Tests if contact doesn't exist no delete is attempted 
	*/
	@Test
	void testListenForRecruiterDeletedEventNonExistingContact() {
		
		String contactId = "rec11";
		
		when(this.mockContactDao.fetchContact(contactId)).thenReturn(Optional.empty());
		
		this.listener.listenForRecruiterDeletedEvent(new RecruiterDeletedEvent(contactId));
		
		verify(this.mockContactDao, never()).deleteById(contactId);
		
	}
	
	/**
	* Tests case event is received informing of newly created
	* Candidate but the Candidate already exists 
	*/
	@Test
	void testListenForCandidateCreatedEventExists() {
		
		final String candidateId = "59";
		
		when(mockCandidateDao.existsById(candidateId)).thenReturn(true);
		
		this.listener.listenFor(CandidateCreatedEvent
				.builder()
					.candidateId(candidateId)
				.build());
		
		verify(this.mockCandidateDao, never()).saveCandidate(any(Candidate.class));
		
	}
	
	/**
	* Tests case event is received informing of newly created
	* Candidate
	*/
	@Test
	void testListenForCandidateCreatedEventDoesntExists() {
		
		final String candidateId = "59";
		
		this.listener.listenFor(CandidateCreatedEvent
				.builder()
					.candidate(com.arenella.recruit.candidates.beans.Candidate
							.builder()
								.candidateId(candidateId)
								.country(COUNTRY.AUSTRIA)
							.build())
				.build());
		
		verify(this.mockCandidateDao).saveCandidate(any(Candidate.class));
		
	}
	
	/**
	* Tests case in which an update event is received but the corresponding
	* Candidate doesn't exists 
	*/
	@Test
	void testListenForCandidateUpdatedEventDoesntExists() {
		
		final String candidateId 	= "59";
		final String firstName 		= "Kevin";
		final String surname 		= "Parkings";
		final String email 			= "kparkings@gmail.com";
		
		when(this.mockCandidateDao.findCandidateById(candidateId)).thenReturn(Optional.empty());
		
		this.listener.listenFor(new CandidateUpdatedEvent(candidateId, firstName, surname, email));
		
		verify(this.mockCandidateDao, never()).saveCandidate(any(Candidate.class));
		
	}
	
	/**
	* Tests case in which an update event is received but the corresponding
	* Candidate exists 
	*/
	@Test
	void testListenForCandidateUpdatedEventExists() {
		
		final String candidateId 	= "59";
		final String firstName 		= "Kevin";
		final String surname 		= "Parkings";
		final String email 			= "kparkings@gmail.com";
		
		when(this.mockCandidateDao.findCandidateById(candidateId)).thenReturn(Optional.of(Candidate.builder().build()));
		
		this.listener.listenFor(new CandidateUpdatedEvent(candidateId, firstName, surname, email));
		
		verify(this.mockCandidateDao).saveCandidate(any(Candidate.class));
		
		
	}
	
	/**
	* Tests case in which an update event is received but the corresponding
	* Candidate doesn't exists 
	*/
	@Test
	void testListenForCandidateUpdateEventDoesntExists() {
		
		final int	 candidateId 	= 59;
		
		when(this.mockCandidateDao.findCandidateById(""+candidateId)).thenReturn(Optional.empty());
		
		this.listener.listenFor(CandidateUpdateEvent.builder().candidateId(candidateId).build());
		
		verify(this.mockCandidateDao, never()).saveCandidate(any(Candidate.class));
		
	}
	
	/**
	* Tests case in which an update event is received but the corresponding
	* Candidate exists 
	*/
	@Test
	void testListenForCandidateUpdateEventEventExists() {
		
		final int	 candidateId 	= 59;
		
		when(this.mockCandidateDao.findCandidateById(""+candidateId)).thenReturn(Optional.of(Candidate.builder().build()));
		
		this.listener.listenFor(CandidateUpdateEvent.builder().candidateId(candidateId).build());
		
		verify(this.mockCandidateDao).saveCandidate(any(Candidate.class));
		
		
	}
	
	/**
	* Tests case in which an Delete event is received but the corresponding
	* Candidate doesn't exists 
	*/
	@Test
	void testListenForCandidateDeletedEventDoesntExists() {
		
		final String candidateId 	= "59";
		
		when(this.mockCandidateDao.findCandidateById(candidateId)).thenReturn(Optional.empty());
		
		this.listener.listenFor(new CandidateDeletedEvent(candidateId));
		
		verify(this.mockCandidateDao, never()).saveCandidate(any(Candidate.class));
		
	}
	
	/**
	* Tests case in which an Delete event is received but the corresponding
	* Candidate exists 
	*/
	@Test
	void testListenForCandidateDeletedEventEventExists() {
		
		final String candidateId 	= "59";
		
		ArgumentCaptor<Candidate> argCapt = ArgumentCaptor.forClass(Candidate.class);
		
		when(this.mockCandidateDao.findCandidateById(candidateId)).thenReturn(Optional.of(Candidate.builder().build()));
		doNothing().when(this.mockCandidateDao).saveCandidate(argCapt.capture());
		
		this.listener.listenFor(new CandidateDeletedEvent(candidateId));
		
		verify(this.mockCandidateDao).saveCandidate(any(Candidate.class));
		
		Candidate candidate = argCapt.getValue();
		
		assertEquals("NA", candidate.getEmail());
		assertEquals("NA", candidate.getSurname());
		
	}
	
	/**
	* Tests that if a Message has been sent by a Candidate associated with a Campaign to a 
	* Recruiter that is a participant in the campaign that the Notification is created
	*/
	@Test
	void listenForIMMessageCreatedEventCampaignLevel() {
	
		final UUID 	campaignId 		= UUID.randomUUID();
		final String recipientId 	= "recruiter1";
		final String senderId 		= "candidate78";
		
		ArgumentCaptor<IMNotification> argCapt = ArgumentCaptor.forClass(IMNotification.class);
		
		IMMessageCreatedEvent event = IMMessageCreatedEvent
				.builder()
					.senderId(senderId)
					.recipientId(recipientId)
				.build();
		
		when(this.mockCampaignDao.fetchCampaignsForCandidate(senderId)).thenReturn(Set.of(Campaign
				.builder()
					.id(campaignId)
					.participants(Set.of(Participation.builder().contactId(recipientId).build()))
				.build()));
		
		this.listener.listenFor(event);
		
		verify(this.mockImNotificationDao).addNotification(argCapt.capture());
		
		IMNotification notification = argCapt.getValue();
		
		assertEquals(recipientId, 	notification.getRecruiterId());
		assertEquals(senderId, 		notification.getCandidateId());
		assertEquals(campaignId, 	notification.getCampaignId());
		
		assertNotNull(notification.getNotificationId());
		
	}
	
	/**
	* Tests that if there is no Campaign with the Recruiter/Candidate combination 
	* that the Notification is not saved 
	*/
	@Test
	void listenForIMMessageCreatedEventCampaignLevelNoMatch() {
	
		final String recipientId 	= "recruiter1";
		final String senderId 		= "candidate78";
		
		IMMessageCreatedEvent event = IMMessageCreatedEvent
				.builder()
					.senderId(senderId)
					.recipientId(recipientId)
				.build();
		
		when(this.mockCampaignDao.fetchCampaignsForCandidate(senderId)).thenReturn(Set.of());
		
		this.listener.listenFor(event);
		
		verify(this.mockImNotificationDao, never()).addNotification(any());
		
	}
	
	/**
	* Tests that if a Message has been sent by a Candidate associated with a Role to a 
	* Recruiter that is a participant in the Role that the Notification is created
	*/
	@Test
	void listenForIMMessageCreatedEventRoleLevel() {
		
		final UUID 	campaignId 		= UUID.randomUUID();
		final UUID 	roleId 			= UUID.randomUUID();
		final String recipientId 	= "recruiter1";
		final String senderId 		= "candidate78";
		
		ArgumentCaptor<IMNotification> argCapt = ArgumentCaptor.forClass(IMNotification.class);
		
		IMMessageCreatedEvent event = IMMessageCreatedEvent
				.builder()
					.senderId(senderId)
					.recipientId(recipientId)
				.build();
		
		when(this.mockRoleDao.fetchRolesForCandidate(senderId)).thenReturn(Set.of(Role
				.builder()
					.id(roleId)
					.campaignId(campaignId)
					.participants(Set.of(Participation.builder().contactId(recipientId).build()))
				.build()));
		
		this.listener.listenFor(event);
		
		verify(this.mockImNotificationDao).addNotification(argCapt.capture());
		
		IMNotification notification = argCapt.getValue();
		
		assertEquals(recipientId, 	notification.getRecruiterId());
		assertEquals(senderId, 		notification.getCandidateId());
		assertEquals(campaignId, 	notification.getCampaignId());
		assertEquals(roleId, 		notification.getRoleId().orElse(null));
		
		assertNotNull(notification.getNotificationId());
		
	}
	
	/**
	* Tests that if there is no Role with the Recruiter/Candidate combination 
	* that the Notification is not saved 
	*/
	@Test
	void listenForIMMessageCreatedEventRoleLevelNoMatch() {
	
		final String recipientId 	= "recruiter1";
		final String senderId 		= "candidate78";
		
		IMMessageCreatedEvent event = IMMessageCreatedEvent
				.builder()
					.senderId(senderId)
					.recipientId(recipientId)
				.build();
		
		when(this.mockRoleDao.fetchRolesForCandidate(senderId)).thenReturn(Set.of());
		
		this.listener.listenFor(event);
		
		verify(this.mockImNotificationDao, never()).addNotification(any());
		
	}
	
	/**
	* Tests case that a recruiter has read an IM sent by a recruiter. All Notifications between 
	* recruiter and candidate must be deleted.
	*/
	@Test
	void listenForIMMessageReadEvent() {
		
		final UUID		notificationId 	= UUID.randomUUID();
		final String 	recipientId 	= "recruiter1";
		final String 	senderId 		= "candidate78";
		
		when(this.mockImNotificationDao.fetchNotificationsBetweenRecruiterAndCandodate(recipientId, senderId))
			.thenReturn(Set.of(IMNotification
					.builder()
						.notificationId(notificationId)
						.recruiterId(recipientId)
						.candidateId(senderId)
					.build()));
		
		this.listener.listenFor(IMMessageReadEvent
				.builder()
					.recipientId(recipientId)
					.senderId(senderId)
				.build());
		
		verify(this.mockImNotificationDao).deleteById(notificationId);
		
	}
	
	/**
	* Tests case that a recruiter has read an IM sent by a recruiter but there is no corresponding 
	* match between a Recruiter/Candidate
	*/
	@Test
	void listenForIMMessageReadEventNoMatch() {
		
		final String 	recipientId 	= "recruiter1";
		final String 	senderId 		= "candidate78";
		
		when(this.mockImNotificationDao.fetchNotificationsBetweenRecruiterAndCandodate(recipientId, senderId))
			.thenReturn(Set.of());
		
		this.listener.listenFor(IMMessageReadEvent
				.builder()
					.recipientId(recipientId)
					.senderId(senderId)
				.build());
		
		verify(this.mockImNotificationDao, never()).deleteById(any());
		
	}
	
}