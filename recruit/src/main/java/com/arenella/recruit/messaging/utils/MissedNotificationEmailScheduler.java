package com.arenella.recruit.messaging.utils;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.arenella.recruit.emailservice.adapters.RequestSendEmailCommand;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient;
import com.arenella.recruit.emailservice.beans.Email.EmailTopic;
import com.arenella.recruit.emailservice.beans.Email.EmailType;
import com.arenella.recruit.emailservice.beans.Email.Sender;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient.ContactType;
import com.arenella.recruit.emailservice.beans.Email.Sender.SenderType;
import com.arenella.recruit.messaging.adapters.MessagingMonolithExternalEventPublisher;
import com.arenella.recruit.messaging.beans.ChatParticipant;
import com.arenella.recruit.messaging.beans.PublicChatNotification;
import com.arenella.recruit.messaging.beans.PublicChatNotification.NotificationType;
import com.arenella.recruit.messaging.services.ParticipantService;
import com.arenella.recruit.messaging.services.PublicChatNotificationService;

/**
* Utility that periodically email's User's to inform them 
* that they have been sent a message that they have not yet 
* seen.  
*/
@Component
@EnableScheduling
public class MissedNotificationEmailScheduler {

	private final PublicChatNotificationService service;
	private final MessagingMonolithExternalEventPublisher eventPublisher;
	private final ParticipantService participantService;
	
	/**
	* Constructor
	* @param service 			- Services for interaction with Notification
	* @oaram eventPublisher 	- For sending emails
	*/
	public MissedNotificationEmailScheduler(PublicChatNotificationService service, MessagingMonolithExternalEventPublisher eventPublisher, ParticipantService participantService) {
		this.service 				= service;
		this.eventPublisher 		= eventPublisher;
		this.participantService 	= participantService;
	}
	
	/**
	* Checks for missed messages from User. If messages are unread 
	* sends an email to the User to let them know 
	*/
	@Scheduled(fixedRate=60000)
	public void missedNotificationEmalMessageSender() {
		
		try {
			
			final LocalDateTime  oneHourAgo = LocalDateTime.now().minusHours(1);
			final LocalDateTime  oneDayAgo 	= LocalDateTime.now().minusDays(1);
			
			Set<String> users = this.service.fetchUndeliveredBefore(oneHourAgo).stream().filter(n -> !n.isDelivered() && !n.isNotificationEmailSent()).map(n -> n.getDestinationUserId()).collect(Collectors.toCollection(LinkedHashSet::new));
			
			users.stream().forEach(userId -> {
				
				this.participantService.fetchById(userId).ifPresent(participant -> {
					
					if (!participant.isDisableNotificationEmails()) {
						
						boolean sufficientTimeSinceLastEmail = participant.getLastNotificationEmailSent().isEmpty() || !participant.getLastNotificationEmailSent().get().isAfter(oneDayAgo);
						
						if (sufficientTimeSinceLastEmail) {
							
							AtomicInteger likeCount = new AtomicInteger();
							AtomicInteger replyCount = new AtomicInteger();
							
							this.service.fetchNotificationsForUser(userId).stream().filter(n -> !n.isDelivered() && !n.isNotificationEmailSent()).forEach(notification -> {
								
								if (notification.getType() == NotificationType.LIKE) {
									likeCount.incrementAndGet();
								}
								
								if (notification.getType() == NotificationType.REPLY) {
									replyCount.incrementAndGet();
								}
								
								this.service.persistNotification(PublicChatNotification.builder().publicChatNotification(notification).notificationEmailSent(true).build());
								
							});
							
							if (likeCount.intValue() != 0 || replyCount.intValue() != 0 ) {
								this.eventPublisher.publishSendEmailCommand(RequestSendEmailCommand
										.builder()
											.emailType(EmailType.SYSTEM_EXTERN)
											.persistable(false)
											.sender(new Sender<>(UUID.randomUUID(), "", SenderType.SYSTEM, "no-reply@arenella-ict.com"))
											.recipients(Set.of(new EmailRecipient<UUID>(UUID.randomUUID(), userId, ContactType.RECRUITER)))
											.title("Unread Newsfeed Notification")
											.model(Map.of("likeCount",likeCount.get(), "replyCount", replyCount.get()))
											.topic(EmailTopic.MISSED_NEWSFEED_NOTIFICATION)
										.build());
								this.participantService.persistParticpant(ChatParticipant.builder().chatParticipant(participant).lastNotificationEmailSent(LocalDateTime.now()).build());
							}
							
						}
						
					}
					
				});
				
			});
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
}