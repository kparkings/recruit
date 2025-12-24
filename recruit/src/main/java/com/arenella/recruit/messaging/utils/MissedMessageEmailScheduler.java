package com.arenella.recruit.messaging.utils;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.arenella.recruit.emailservice.adapters.RequestSendEmailCommand;
import com.arenella.recruit.emailservice.adapters.RequestSendEmailCommand.RequestSendEmailCommandBuilder;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient;
import com.arenella.recruit.emailservice.beans.Email.EmailTopic;
import com.arenella.recruit.emailservice.beans.Email.EmailType;
import com.arenella.recruit.emailservice.beans.Email.Sender;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient.ContactType;
import com.arenella.recruit.emailservice.beans.Email.Sender.SenderType;
import com.arenella.recruit.messaging.adapters.MessagingMonolithExternalEventPublisher;
import com.arenella.recruit.messaging.dao.PrivateChatDao;
import com.arenella.recruit.messaging.services.PrivateChatService;

/**
* Utility that periodically email's User's to inform them 
* that they have been sent a message that they have not yet 
* seen.  
*/
@Component
@EnableScheduling
public class MissedMessageEmailScheduler {

	private final PrivateChatService 	service;
	private final PrivateChatDao 	 	dao;
	private final MessagingMonolithExternalEventPublisher eventPublisher;
	
	/**
	* Constructor
	* @param service - Services for interaction with PrivateChat's
	*/
	public MissedMessageEmailScheduler(PrivateChatService service, PrivateChatDao dao, MessagingMonolithExternalEventPublisher eventPublisher) {
		this.service 		= service;
		this.dao 			= dao;
		this.eventPublisher = eventPublisher;
	}
	
	/**
	* Checks for missed messages from User. If messages are unread 
	* sends an email to the User to let them know 
	*/
	@Scheduled(fixedRate=60000)
	public void missedEmalMessageSender() {
		
		try {
			
			final LocalDateTime  fiveMinutesAgo = LocalDateTime.now().minusMinutes(5);
			
			this.service.getUnblockedChatsBeforeCuttoff(fiveMinutesAgo).stream().filter(c -> c.getLastUpdated().isBefore(fiveMinutesAgo) && !c.isBlockedBySender() && !c.isBlockedByRecipient()).forEach(chat -> {
				
				RequestSendEmailCommandBuilder emailCommandBuilder = RequestSendEmailCommand
						.builder()
							.emailType(EmailType.SYSTEM_EXTERN)
							.persistable(true)
							.sender(new Sender<>(UUID.randomUUID(), "", SenderType.SYSTEM, "no-reply@arenella-ict.com"))
							.title("Unread Chat Message")
							.topic(EmailTopic.MISSED_CHAT_MESSAGE)
							.model(Map.of(
									"CHAT_ID",				chat.getId(), 
									"CHAT_MESSAGE_FROM",	chat.getRecipientId()));
				
				
				
				//Case 1: Last viewed by Sender is null
				//        No alert has ever been sent
				if (chat.getLastViewedBySender().isEmpty() && chat.getLastMissedMessageAlertSender().isEmpty()) {
					
					emailCommandBuilder
						.recipients(Set.of(new EmailRecipient<UUID>(UUID.randomUUID(), chat.getSenderId(), ContactType.RECRUITER)))
						.model(Map.of("CHAT_ID",chat.getId(), "CHAT_MESSAGE_FROM",chat.getRecipientId()));
					
					eventPublisher.publishSendEmailCommand(emailCommandBuilder.build());
					
					chat.updateLastUnreadMessageReminderSentSender();
					
				}
				
				//Case 2: Last viewed by Receiver is null
				//        No alert has ever been sent
				if (chat.getLastViewedByRecipient().isEmpty() && chat.getLastMissedMessageAlertRecipient().isEmpty()) {
					
					emailCommandBuilder
						.recipients(Set.of(new EmailRecipient<UUID>(UUID.randomUUID(), chat.getRecipientId(), ContactType.CANDIDATE)))
						.model(Map.of("CHAT_ID",chat.getId(), "CHAT_MESSAGE_FROM",chat.getSenderId()));
					
						
					eventPublisher.publishSendEmailCommand(emailCommandBuilder.build());
						
					chat.updateLastUnreadMessageReminderSentRecipient();
				}
				
				//Case 3: Last viewed by Sender is before last update
				//        No alert has ever been sent
				chat.getLastViewedBySender().ifPresent(c -> {
					if (c.isBefore(chat.getLastUpdated()) && chat.getLastMissedMessageAlertSender().isEmpty()) {
						
						emailCommandBuilder
							.recipients(Set.of(new EmailRecipient<UUID>(UUID.randomUUID(), chat.getSenderId(), ContactType.RECRUITER)))
							.model(Map.of("CHAT_ID",chat.getId(), "CHAT_MESSAGE_FROM",chat.getRecipientId()));
							
						eventPublisher.publishSendEmailCommand(emailCommandBuilder.build());
							
							
						chat.updateLastUnreadMessageReminderSentSender();
					}
				});
		
				//Case 4: Last viewed by Recipient is before last update
				//        No alert has ever been sent
				chat.getLastViewedByRecipient().ifPresent(c -> {
					if (c.isBefore(chat.getLastUpdated()) && chat.getLastMissedMessageAlertRecipient().isEmpty()) {
						
						emailCommandBuilder
							.recipients(Set.of(new EmailRecipient<UUID>(UUID.randomUUID(), chat.getRecipientId(), ContactType.CANDIDATE)))
							.model(Map.of("CHAT_ID",chat.getId(), "CHAT_MESSAGE_FROM",chat.getSenderId()));
							
						eventPublisher.publishSendEmailCommand(emailCommandBuilder.build());
							
							
						chat.updateLastUnreadMessageReminderSentRecipient();
					}
				});
				
				//Case 5: Last viewed by Sender is before last update
				//        No reminder has been sent since the after the Sender last viewed the Chat
				chat.getLastViewedBySender().ifPresent(lastViewed -> {
					if (lastViewed.isBefore(chat.getLastUpdated())) {
						chat.getLastMissedMessageAlertSender().ifPresent(calert -> {
							if (calert.isBefore(lastViewed)) {
								
								emailCommandBuilder
									.recipients(Set.of(new EmailRecipient<UUID>(UUID.randomUUID(), chat.getSenderId(), ContactType.RECRUITER)))
									.model(Map.of("CHAT_ID",chat.getId(), "CHAT_MESSAGE_FROM",chat.getRecipientId()));
									
								eventPublisher.publishSendEmailCommand(emailCommandBuilder.build());
									
									
								chat.updateLastUnreadMessageReminderSentSender();
							}
						});
					} 
					
				});
				
				//Case 6: Last viewed by Recipient is before last update
				//        No reminder has been sent since the after the Recipient last viewed the Chat
				chat.getLastViewedByRecipient().ifPresent(lastViewed -> {
					if (lastViewed.isBefore(chat.getLastUpdated())) {
						chat.getLastMissedMessageAlertRecipient().ifPresent(calert -> {
							if (calert.isBefore(lastViewed)) {
								System.out.println("Recipient -> You have an unread message from " + chat.getSenderId());
								
								emailCommandBuilder
									.recipients(Set.of(new EmailRecipient<UUID>(UUID.randomUUID(), chat.getRecipientId(), ContactType.CANDIDATE)))
									.model(Map.of("CHAT_ID",chat.getId(), "CHAT_MESSAGE_FROM",chat.getSenderId()));
								
								eventPublisher.publishSendEmailCommand(emailCommandBuilder.build());
									
								chat.updateLastUnreadMessageReminderSentRecipient();
							}
						});
					} 
					
				});
				
				this.dao.saveChat(chat);
			
				
			});
			
		}catch(Exception e) {
			
		}
		
	}
	
}