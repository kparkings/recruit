package com.arenella.recruit.messaging.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.arenella.recruit.adapters.events.CandidateDeletedEvent;
import com.arenella.recruit.adapters.events.CandidateUpdateEvent;
import com.arenella.recruit.adapters.events.RecruiterCreatedEvent;
import com.arenella.recruit.adapters.events.RecruiterDeletedEvent;
import com.arenella.recruit.adapters.events.RecruiterProfileCreatedEvent;
import com.arenella.recruit.adapters.events.RecruiterProfileUpdatedEvent;
import com.arenella.recruit.adapters.events.RecruiterUpdatedEvent;
import com.arenella.recruit.candidates.adapters.CandidateCreatedEvent;
import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.Candidate.Photo;
import com.arenella.recruit.candidates.beans.Candidate.Photo.PHOTO_FORMAT;
import com.arenella.recruit.messaging.beans.ChatParticipant;
import com.arenella.recruit.messaging.beans.ChatParticipant.CHAT_PARTICIPANT_TYPE;
import com.arenella.recruit.messaging.services.ParticipantService;
import com.arenella.recruit.messaging.services.PrivateChatService;
import com.arenella.recruit.messaging.services.PublicChatNotificationService;
import com.arenella.recruit.messaging.services.PublicChatService;

/**
* Unit tests for the MessagingMessagingServiceExternalEventListenerImpl class 
*/
@ExtendWith(MockitoExtension.class)
class MessagingMessagingServiceExternalEventListenerImplTest {
	
	private static final String CANDIDATE_ID = "1200";
	private static final String RECRUITER_ID = "rec1";
	
	@Mock
	private PublicChatNotificationService 	notificationService;
	
	@Mock
	private ParticipantService 				mockParticipantService;
	
	@Mock
	private PrivateChatService				mockPrivateChatService;
	
	@Mock
	private PublicChatService				mockPublicChatService;
	
	@InjectMocks
	private MessagingMessagingServiceExternalEventListenerImpl listener;;

	//TODO: Version with Photo
	/**
	* Test lister for when Candidate is Created ( photo provided )
	*/
	@Test
	void testListenForCandidateCreatedEventWithPhoto() {
		
		final String firstName 	= "Kevin";
		final String surname 	= "Parkings";
		
		ArgumentCaptor<ChatParticipant> participantCaptr = ArgumentCaptor.forClass(ChatParticipant.class);
		
		CandidateCreatedEvent event = CandidateCreatedEvent
				.builder()
					.candidate(Candidate
							.builder()
								.firstname(firstName)
								.surname(surname)
								.candidateId(CANDIDATE_ID)
								.photo(new Photo(new byte[] {}, PHOTO_FORMAT.png))
							.build())
				.build();
		
		doNothing().when(mockParticipantService).persistParticpant(participantCaptr.capture());
		
		this.listener.listenForCandidateCreatedEvent(event);
		
		verify(this.mockParticipantService).persistParticpant(any(ChatParticipant.class));
		
		ChatParticipant chatParticipant = participantCaptr.getValue();
		
		assertEquals(CANDIDATE_ID, 						chatParticipant.getParticipantId());
		assertEquals(firstName, 						chatParticipant.getFirstName());
		assertEquals(surname, 							chatParticipant.getSurame());
		assertEquals(CHAT_PARTICIPANT_TYPE.CANDIDATE, 	chatParticipant.getType());	
		assertTrue(chatParticipant.getPhoto().isPresent());
	}
	
	/**
	* Test lister for when Candidate is Created ( No photo provided )
	*/
	@Test
	void testListenForCandidateCreatedEvent() {
		
		final String firstName 	= "Kevin";
		final String surname 	= "Parkings";
		
		ArgumentCaptor<ChatParticipant> participantCaptr = ArgumentCaptor.forClass(ChatParticipant.class);
		
		CandidateCreatedEvent event = CandidateCreatedEvent
				.builder()
					.candidate(Candidate
							.builder()
								.firstname(firstName)
								.surname(surname)
								.candidateId(CANDIDATE_ID)
							.build())
				.build();
		
		doNothing().when(mockParticipantService).persistParticpant(participantCaptr.capture());
		
		this.listener.listenForCandidateCreatedEvent(event);
		
		verify(this.mockParticipantService).persistParticpant(any(ChatParticipant.class));
		
		ChatParticipant chatParticipant = participantCaptr.getValue();
		
		assertEquals(CANDIDATE_ID, 						chatParticipant.getParticipantId());
		assertEquals(firstName, 						chatParticipant.getFirstName());
		assertEquals(surname, 							chatParticipant.getSurame());
		assertEquals(CHAT_PARTICIPANT_TYPE.CANDIDATE, 	chatParticipant.getType());	
		assertFalse(chatParticipant.getPhoto().isPresent());
	}
	
	/**
	* Test lister for when Candidate is Updated ( photo provided )
	*/
	@Test
	void testListenForCandidateUpdateEventWithPhoto() {
	
		final int	 	candidateId 	= 1101;
		final String 	firstName 		= "Kevin";
		final String 	surname 		= "Parkings";
		
		ArgumentCaptor<ChatParticipant> participantCaptr = ArgumentCaptor.forClass(ChatParticipant.class);
		
		CandidateUpdateEvent event = CandidateUpdateEvent
				.builder()
					.candidateId(candidateId)
					.firstName(firstName)
					.surname(surname)
					.photo(new Photo(new byte[] {}, PHOTO_FORMAT.png))
				.build();
		
		when(this.mockParticipantService.fetchById(String.valueOf(candidateId)))
			.thenReturn(Optional.of(ChatParticipant
				.builder()
					.firstName(firstName)
					.surname(surname)
				.build()));
		
		doNothing().when(mockParticipantService).persistParticpant(participantCaptr.capture());
		
		this.listener.listenForCandidateUpdateEvent(event);
		
		verify(this.mockParticipantService).persistParticpant(any(ChatParticipant.class));
		
		ChatParticipant chatParticipant = participantCaptr.getValue();
		
		assertEquals(String.valueOf(candidateId), 		chatParticipant.getParticipantId());
		assertEquals(firstName, 						chatParticipant.getFirstName());
		assertEquals(surname, 							chatParticipant.getSurame());
		assertEquals(CHAT_PARTICIPANT_TYPE.CANDIDATE, 	chatParticipant.getType());	
		assertTrue(chatParticipant.getPhoto().isPresent());
	}
	
	/**
	* Test lister for when Candidate is Updated ( No photo provided )
	*/
	@Test
	void testListenForCandidateUpdateEvent() {
	
		final int	 	candidateId 	= 1101;
		final String 	firstName 		= "Kevin";
		final String 	surname 		= "Parkings";
		
		ArgumentCaptor<ChatParticipant> participantCaptr = ArgumentCaptor.forClass(ChatParticipant.class);
		
		CandidateUpdateEvent event = CandidateUpdateEvent
				.builder()
					.candidateId(candidateId)
					.firstName(firstName)
					.surname(surname)
				.build();
		
		when(this.mockParticipantService.fetchById(String.valueOf(candidateId)))
			.thenReturn(Optional.of(ChatParticipant
				.builder()
					.firstName(firstName)
					.surname(surname)
				.build()));
		
		doNothing().when(mockParticipantService).persistParticpant(participantCaptr.capture());
		
		this.listener.listenForCandidateUpdateEvent(event);
		
		verify(this.mockParticipantService).persistParticpant(any(ChatParticipant.class));
		
		ChatParticipant chatParticipant = participantCaptr.getValue();
		
		assertEquals(String.valueOf(candidateId), 		chatParticipant.getParticipantId());
		assertEquals(firstName, 						chatParticipant.getFirstName());
		assertEquals(surname, 							chatParticipant.getSurame());
		assertEquals(CHAT_PARTICIPANT_TYPE.CANDIDATE, 	chatParticipant.getType());	
		assertFalse(chatParticipant.getPhoto().isPresent());
		
	}
	
	/**
	* Tests event results in Participant being created
	*/
	@Test
	void testListenForRecruiterCreatedEvent() {
		
		final String firstName 	= "Kevin";
		final String surname 	= "Parkings";
		
		ArgumentCaptor<ChatParticipant> participantCaptr = ArgumentCaptor.forClass(ChatParticipant.class);
		
		RecruiterCreatedEvent event = RecruiterCreatedEvent
				.builder()
					.recruiterId(RECRUITER_ID)
					.firstName(firstName)
					.surname(surname)
				.build();
		
		doNothing().when(mockParticipantService).persistParticpant(participantCaptr.capture());
		
		this.listener.listenForRecruiterCreatedEvent(event);
		
		verify(this.mockParticipantService).persistParticpant(any(ChatParticipant.class));
		
		ChatParticipant chatParticipant = participantCaptr.getValue();
		
		assertEquals(RECRUITER_ID, 						chatParticipant.getParticipantId());
		assertEquals(firstName, 						chatParticipant.getFirstName());
		assertEquals(surname, 							chatParticipant.getSurame());
		assertEquals(CHAT_PARTICIPANT_TYPE.RECRUITER, 	chatParticipant.getType());	
		
	}
	
	/**
	* Tests event results in Participant being updated
	*/
	@Test
	void testListenForRecruiterUpdatedEvent() {
		
		final String firstName 	= "Kevin";
		final String surname 	= "Parkings";
		
		ArgumentCaptor<ChatParticipant> participantCaptr = ArgumentCaptor.forClass(ChatParticipant.class);
		
		RecruiterUpdatedEvent event = RecruiterUpdatedEvent
				.builder()
					.recruiterId(RECRUITER_ID)
					.firstName(firstName)
					.surname(surname)
				.build();
		
		when(this.mockParticipantService.fetchById(RECRUITER_ID))
			.thenReturn(Optional.of(ChatParticipant
				.builder()
					.firstName(firstName)
					.surname(surname)
				.build()));
		
		doNothing().when(mockParticipantService).persistParticpant(participantCaptr.capture());
		
		this.listener.listenForRecruiterUpdatedEvent(event);
		
		verify(this.mockParticipantService).persistParticpant(any(ChatParticipant.class));
		
		ChatParticipant chatParticipant = participantCaptr.getValue();
		
		assertEquals(RECRUITER_ID, 						chatParticipant.getParticipantId());
		assertEquals(firstName, 						chatParticipant.getFirstName());
		assertEquals(surname, 							chatParticipant.getSurame());
		assertEquals(CHAT_PARTICIPANT_TYPE.RECRUITER, 	chatParticipant.getType());	
		
	}
	
	/**
	* Tests event results in Participant being deleted
	*/
	@Test
	void testListenForCandidateDeletedEvent() {
		
		listener.listenForCandidateDeletedEvent(new CandidateDeletedEvent(CANDIDATE_ID));
	
		verify(this.mockParticipantService).deletePartipant(CANDIDATE_ID);
		verify(this.notificationService).deleteNotificationsForUser(CANDIDATE_ID);
	}
	
	/**
	* Tests event results in Participant being deleted
	*/
	@Test
	void testListenForRecruiterDeletedEvent() {
		
		listener.listenForRecruiterDeletedEvent(new RecruiterDeletedEvent(RECRUITER_ID));
	
		verify(this.mockParticipantService).deletePartipant(RECRUITER_ID);
		verify(this.notificationService).deleteNotificationsForUser(RECRUITER_ID);
		
	}
	
	/**
	* Tests event results in Participant photo being added
	*/
	@Test
	void testListenForRecruiterProfileCreatedEventWithPhoto() {
		
		final String firstName 	= "Kevin";
		final String surname 	= "Parkings";
		
		ArgumentCaptor<ChatParticipant> participantCaptr = ArgumentCaptor.forClass(ChatParticipant.class);
		
		RecruiterProfileCreatedEvent event = RecruiterProfileCreatedEvent
				.builder()
					.recruiterId(RECRUITER_ID)
					.profileImage(new com.arenella.recruit.adapters.events.Photo(com.arenella.recruit.adapters.events.PHOTO_FORMAT.png, new byte[] {}))
				.build();
		
		when(this.mockParticipantService.fetchById(RECRUITER_ID))
			.thenReturn(Optional.of(ChatParticipant
				.builder()
					.type(CHAT_PARTICIPANT_TYPE.RECRUITER)
					.firstName(firstName)
					.surname(surname)
				.build()));
		
		doNothing().when(mockParticipantService).persistParticpant(participantCaptr.capture());
		
		this.listener.listenForRecruiterProfileCreatedEvent(event);
		
		verify(this.mockParticipantService).persistParticpant(any(ChatParticipant.class));
		
		ChatParticipant chatParticipant = participantCaptr.getValue();
		
		assertEquals(RECRUITER_ID, 						chatParticipant.getParticipantId());
		assertEquals(firstName, 						chatParticipant.getFirstName());
		assertEquals(surname, 							chatParticipant.getSurame());
		assertEquals(CHAT_PARTICIPANT_TYPE.RECRUITER, 	chatParticipant.getType());	
		assertTrue(chatParticipant.getPhoto().isPresent());
		
	}
	
	/**
	* Tests event where profile created but no photo was provided
	*/
	@Test
	void testListenForRecruiterProfileCreatedEvent() {
		
		final String firstName 	= "Kevin";
		final String surname 	= "Parkings";
		
		ArgumentCaptor<ChatParticipant> participantCaptr = ArgumentCaptor.forClass(ChatParticipant.class);
		
		RecruiterProfileCreatedEvent event = RecruiterProfileCreatedEvent
				.builder()
					.recruiterId(RECRUITER_ID)
					.profileImage(null)
				.build();
		
		when(this.mockParticipantService.fetchById(RECRUITER_ID))
			.thenReturn(Optional.of(ChatParticipant
				.builder()
					.firstName(firstName)
					.surname(surname)
				.build()));
			
		doNothing().when(mockParticipantService).persistParticpant(participantCaptr.capture());
		
		this.listener.listenForRecruiterProfileCreatedEvent(event);;
		
		verify(this.mockParticipantService).persistParticpant(any(ChatParticipant.class));
		
		ChatParticipant chatParticipant = participantCaptr.getValue();
		
		assertEquals(RECRUITER_ID, 						chatParticipant.getParticipantId());
		assertEquals(firstName, 						chatParticipant.getFirstName());
		assertEquals(surname, 							chatParticipant.getSurame());
		assertEquals(CHAT_PARTICIPANT_TYPE.RECRUITER, 	chatParticipant.getType());	
		assertFalse(chatParticipant.getPhoto().isPresent());
	}
	
	/**
	* Tests event where profile updated and photo was provided
	*/
	@Test
	void testListenForRecruiterProfileUpdatedEventWithPhoto() {
		
		final String firstName 	= "Kevin";
		final String surname 	= "Parkings";
		
		ArgumentCaptor<ChatParticipant> participantCaptr = ArgumentCaptor.forClass(ChatParticipant.class);
		
		RecruiterProfileUpdatedEvent event = RecruiterProfileUpdatedEvent
				.builder()
					.recruiterId(RECRUITER_ID)
					.profileImage(new com.arenella.recruit.adapters.events.Photo(com.arenella.recruit.adapters.events.PHOTO_FORMAT.png, new byte[] {}))
				.build();
		
		when(this.mockParticipantService.fetchById(RECRUITER_ID))
			.thenReturn(Optional.of(ChatParticipant
				.builder()
					.firstName(firstName)
					.surname(surname)
				.build()));
		
		doNothing().when(mockParticipantService).persistParticpant(participantCaptr.capture());
		
		this.listener.listenForRecruiterProfileUpdatedEvent(event);;
		
		verify(this.mockParticipantService).persistParticpant(any(ChatParticipant.class));
		
		ChatParticipant chatParticipant = participantCaptr.getValue();
		
		assertEquals(RECRUITER_ID, 						chatParticipant.getParticipantId());
		assertEquals(firstName, 						chatParticipant.getFirstName());
		assertEquals(surname, 							chatParticipant.getSurame());
		assertEquals(CHAT_PARTICIPANT_TYPE.RECRUITER, 	chatParticipant.getType());	
		assertTrue(chatParticipant.getPhoto().isPresent());
		
	}
	
	/**
	* Tests event where profile updated but no photo was provided
	*/
	@Test
	void testListenForRecruiterProfileUpdatedEvent() {
		
		final String firstName 	= "Kevin";
		final String surname 	= "Parkings";
		
		ArgumentCaptor<ChatParticipant> participantCaptr = ArgumentCaptor.forClass(ChatParticipant.class);
		
		RecruiterProfileUpdatedEvent event = RecruiterProfileUpdatedEvent
				.builder()
					.recruiterId(RECRUITER_ID)
					.profileImage(null)
				.build();
		
		when(this.mockParticipantService.fetchById(RECRUITER_ID))
			.thenReturn(Optional.of(ChatParticipant
					.builder()
						.firstName(firstName)
						.surname(surname)
					.build()));
		
		doNothing().when(mockParticipantService).persistParticpant(participantCaptr.capture());
		
		this.listener.listenForRecruiterProfileUpdatedEvent(event);;
		
		verify(this.mockParticipantService).persistParticpant(any(ChatParticipant.class));
		
		ChatParticipant chatParticipant = participantCaptr.getValue();
		
		assertEquals(RECRUITER_ID, 						chatParticipant.getParticipantId());
		assertEquals(firstName, 						chatParticipant.getFirstName());
		assertEquals(surname, 							chatParticipant.getSurame());
		assertEquals(CHAT_PARTICIPANT_TYPE.RECRUITER, 	chatParticipant.getType());	
		assertFalse(chatParticipant.getPhoto().isPresent());
		
	}
	
}
