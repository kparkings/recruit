package com.arenella.recruit.messaging.adapters;

import org.springframework.stereotype.Component;

import com.arenella.recruit.adapters.events.CandidateDeletedEvent;
import com.arenella.recruit.adapters.events.CandidateUpdateEvent;
import com.arenella.recruit.adapters.events.RecruiterCreatedEvent;
import com.arenella.recruit.adapters.events.RecruiterDeletedEvent;
import com.arenella.recruit.adapters.events.RecruiterProfileCreatedEvent;
import com.arenella.recruit.adapters.events.RecruiterProfileUpdatedEvent;
import com.arenella.recruit.adapters.events.RecruiterUpdatedEvent;
import com.arenella.recruit.candidates.adapters.CandidateCreatedEvent;
import com.arenella.recruit.messaging.beans.ChatParticipant;
import com.arenella.recruit.messaging.services.ParticipantService;
import com.arenella.recruit.messaging.beans.Photo;
import com.arenella.recruit.messaging.beans.Photo.PHOTO_FORMAT;

import static com.arenella.recruit.messaging.beans.ChatParticipant.CHAT_PARTICIPANT_TYPE;

import java.util.Optional;

/**
* Event Listener 
*/
@Component
public class MessagingMessagingServiceExternalEventListenerImpl implements MessagingMessagingServiceExternalEventListener{

	private ParticipantService participantService;
	
	/**
	* Constructor
	* @param participantService
	*/
	public MessagingMessagingServiceExternalEventListenerImpl(ParticipantService participantService) {
		this.participantService = participantService;
	}
	
	/**
	* Refer to the MessagingMessagingServiceExternalEventListener interface for details 
	*/
	@Override
	public void listenForCandidateCreatedEvent(CandidateCreatedEvent event) {
		ChatParticipant participant = 
				ChatParticipant
				.builder()
					.participantId(event.getCandidateId())
					.type(CHAT_PARTICIPANT_TYPE.CANDIDATE)
					.firstName(event.getFirstname())
					.surname(event.getSurname())
					.photo(extractAndConvertPhoto(event.getPhoto()))
				.build();
		
		this.participantService.persistParticpant(participant);
	}

	/**
	* Refer to the MessagingMessagingServiceExternalEventListener interface for details 
	*/
	@Override
	public void listenForCandidateUpdateEvent(CandidateUpdateEvent event) {
		ChatParticipant participant = 
				ChatParticipant
				.builder()
					.participantId(String.valueOf(event.getCandidateId()))
					.type(CHAT_PARTICIPANT_TYPE.CANDIDATE)
					.firstName(event.getFirstName())
					.surname(event.getSurname())
					.photo(extractAndConvertPhoto(event.getPhoto()))
				.build();
		
		this.participantService.persistParticpant(participant);
	}

	/**
	* Refer to the MessagingMessagingServiceExternalEventListener interface for details 
	*/
	@Override
	public void listenForRecruiterCreatedEvent(RecruiterCreatedEvent event) {
		
		ChatParticipant participant = 
				ChatParticipant
				.builder()
					.participantId(String.valueOf(event.getRecruiterId()))
					.type(CHAT_PARTICIPANT_TYPE.RECRUITER)
					.firstName(event.getFirstName())
					.surname(event.getSurname())
				.build();
		
		this.participantService.persistParticpant(participant);
	}

	/**
	* Refer to the MessagingMessagingServiceExternalEventListener interface for details 
	*/
	@Override
	public void listenForRecruiterUpdatedEvent(RecruiterUpdatedEvent event) {
		
		ChatParticipant participant = 
				ChatParticipant
				.builder()
					.participantId(String.valueOf(event.getRecruiterId()))
					.type(CHAT_PARTICIPANT_TYPE.RECRUITER)
					.firstName(event.getFirstName())
					.surname(event.getSurname())
				.build();
		
		this.participantService.persistParticpant(participant);
	}

	/**
	* Refer to the MessagingMessagingServiceExternalEventListener interface for details 
	*/
	@Override
	public void listenForCandidateDeletedEvent(CandidateDeletedEvent event) {
		this.participantService.deletePartipant(event.getCandidateId());
		
	}

	/**
	* Refer to the MessagingMessagingServiceExternalEventListener interface for details 
	*/
	@Override
	public void listenForRecruiterDeletedEvent(RecruiterDeletedEvent event) {
		this.participantService.deletePartipant(event.getRecruiterId());
	}

	/**
	* Refer to the MessagingMessagingServiceExternalEventListener interface for details 
	*/
	@Override
	public void listenForRecruiterProfileCreatedEvent(RecruiterProfileCreatedEvent event) {
		ChatParticipant participant = 
				ChatParticipant
				.builder()
					.participantId(String.valueOf(event.getRecruiterId()))
					.type(CHAT_PARTICIPANT_TYPE.RECRUITER)
					.photo(extractAndConvertPhoto(event.getProfileImage()))
				.build();
		
		this.participantService.persistParticpant(participant);
	}

	/**
	* Refer to the MessagingMessagingServiceExternalEventListener interface for details 
	*/
	@Override
	public void listenForRecruiterProfileUpdatedEvent(RecruiterProfileUpdatedEvent event) {
		ChatParticipant participant = 
				ChatParticipant
				.builder()
					.participantId(String.valueOf(event.getRecruiterId()))
					.type(CHAT_PARTICIPANT_TYPE.RECRUITER)
					.photo(extractAndConvertPhoto(event.getProfileImage()))
				.build();
		
		this.participantService.persistParticpant(participant);
	}
	
	/**
	* Utility method to convert the Event format of Photo to the local format
	* @param eventPhoto
	* @return
	*/
	private static Photo extractAndConvertPhoto(Optional<com.arenella.recruit.adapters.events.Photo> eventPhoto) {
		
		Photo photo = null;
		
		if (eventPhoto.isPresent()) {
			try {
				PHOTO_FORMAT format = PHOTO_FORMAT.valueOf(eventPhoto.get().format().toString());
				photo = new Photo(eventPhoto.get().bytes(), format);
			} catch(Exception e) {
				e.printStackTrace();
				//If image fails we send without image
			}
		}
		return photo;
	}

}