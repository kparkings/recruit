package com.arenella.recruit.messaging.utils;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
	
	/**
	* Constructor
	* @param service - Services for interaction with PrivateChat's
	*/
	public MissedMessageEmailScheduler(PrivateChatService service, PrivateChatDao dao) {
		this.service 	= service;
		this.dao 		= dao;
	}
	
	/**
	* Checks for missed messages from User. If messages are unread 
	* sends an email to the User to let them know 
	*/
	@Scheduled(fixedRate=60000)
	public void missedEmalMessageSender() {
		System.out.println("Running..");
		final LocalDateTime  fiveMinutesAgo = LocalDateTime.now().minusMinutes(5);
		
		this.service.getUnblockedChatsBeforeCuttoff(fiveMinutesAgo).stream().filter(c -> c.getLastUpdated().isBefore(fiveMinutesAgo) && !c.isBlockedBySender() && !c.isBlockedByRecipient()).forEach(chat -> {
			
			System.out.println("Processing Chat 1");
			
			//Case 1: Last viewed by Sender is null
			//        No alert has ever been sent
			if (chat.getLastViewedBySender().isEmpty() && chat.getLastMissedMessageAlertSender().isEmpty()) {
				System.out.println("Sender -> You have an unread message from " + chat.getRecipientId());
				chat.updateLastUnreadMessageReminderSentSender();
			}
			
			//Case 2: Last viewed by Receiver is null
			//        No alert has ever been sent
			if (chat.getLastViewedByRecipient().isEmpty() && chat.getLastMissedMessageAlertRecipient().isEmpty()) {
				System.out.println("Recipient -> You have an unread message from " + chat.getSenderId());
				chat.updateLastUnreadMessageReminderSentRecipient();
			}
			
			//Case 3: Last viewed by Sender is before last update
			//        No alert has ever been sent
			chat.getLastViewedBySender().ifPresent(c -> {
				if(c.isBefore(chat.getLastUpdated()) && chat.getLastMissedMessageAlertSender().isEmpty()) {
					System.out.println("Sender -> You have an unread message from " + chat.getRecipientId());
					chat.updateLastUnreadMessageReminderSentSender();
				}
			});
	
			//Case 4: Last viewed by Recipient is before last update
			//        No alert has ever been sent
			chat.getLastViewedByRecipient().ifPresent(c -> {
				if(c.isBefore(chat.getLastUpdated()) && chat.getLastMissedMessageAlertRecipient().isEmpty()) {
					System.out.println("Recipient -> You have an unread message from " + chat.getSenderId());
					chat.updateLastUnreadMessageReminderSentRecipient();
				}
			});
			
			//Case 5: Last viewed by Sender is before last update
			//        No reminder has been sent since the after the Sender last viewed the Chat
			chat.getLastViewedBySender().ifPresent(lastViewed -> {
				if(lastViewed.isBefore(chat.getLastUpdated())) {
					chat.getLastMissedMessageAlertSender().ifPresent(calert -> {
						if(calert.isBefore(lastViewed)) {
							System.out.println("Sender -> You have an unread message from " + chat.getRecipientId());
							chat.updateLastUnreadMessageReminderSentSender();
						}
					});
				} 
				
			});
			
			//Case 6: Last viewed by Recipient is before last update
			//        No reminder has been sent since the after the Recipient last viewed the Chat
			chat.getLastViewedByRecipient().ifPresent(lastViewed -> {
				if(lastViewed.isBefore(chat.getLastUpdated())) {
					chat.getLastMissedMessageAlertRecipient().ifPresent(calert -> {
						if(calert.isBefore(lastViewed)) {
							System.out.println("Recipient -> You have an unread message from " + chat.getSenderId());
							chat.updateLastUnreadMessageReminderSentRecipient();
						}
					});
				} 
				
			});
			
			this.dao.saveChat(chat);
			
		});
		
	}
	
}