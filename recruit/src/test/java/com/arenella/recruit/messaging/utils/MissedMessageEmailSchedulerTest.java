package com.arenella.recruit.messaging.utils;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

import com.arenella.recruit.messaging.adapters.MessagingMonolithExternalEventPublisher;
import com.arenella.recruit.messaging.beans.PrivateChat;
import com.arenella.recruit.messaging.dao.PrivateChatDao;
import com.arenella.recruit.messaging.services.PrivateChatService;

/**
* Unit tests for the MissedMessageEmailScheduler class
**/
@ExtendWith(MockitoExtension.class)
class MissedMessageEmailSchedulerTest {

	private final PrivateChatService 						mockService 		= mock(PrivateChatService.class);
	private final PrivateChatDao 	 						mockDao				= mock(PrivateChatDao.class);
	private final MessagingMonolithExternalEventPublisher 	mockEventPublisher 	= mock(MessagingMonolithExternalEventPublisher.class);
	private final MissedMessageEmailScheduler 				scheduler 			= new MissedMessageEmailScheduler(mockService, mockDao, mockEventPublisher);
	
	/**
	* Chats last updtae must be more than 5 minutes for a reminder email to 
	* be sent 
	*/
	@Test
	void testmissedEmalMessageSenderLessThan5MinutesAgo() {
		
		LocalDateTime twoMinutesAgo = LocalDateTime.now().minusMinutes(2);
		
		PrivateChat chat = PrivateChat.builder().blockedBySender(false).blockedByRecipient(false).lastUpdate(twoMinutesAgo).build();
		
		when(this.mockService.getUnblockedChatsBeforeCuttoff(any())).thenReturn(Set.of(chat));
		
		this.scheduler.missedEmalMessageSender();
		
		verify(this.mockDao, never()).saveChat(any());
		
	}
	
	/**
	* Blocked Chats should be ignored. No updates should be 
	* make if the Sender has blocked the chat
	*/
	@Test
	void testmissedEmalMessageSenderIsBlockedBySender() {
		
		LocalDateTime tenMinutesAgo = LocalDateTime.now().minusMinutes(10);
		
		PrivateChat chat = PrivateChat.builder().blockedBySender(true).blockedByRecipient(false).lastUpdate(tenMinutesAgo).build();
		
		when(this.mockService.getUnblockedChatsBeforeCuttoff(any())).thenReturn(Set.of(chat));
		
		this.scheduler.missedEmalMessageSender();
		
		verify(this.mockDao, never()).saveChat(any());
		
	}
	
	/**
	* Blocked Chats should be ignored. No updates should be 
	* make if the Recipient has blocked the chat
	*/
	@Test
	void testmissedEmalMessageSenderIsBlockedByRecipient() {
		
		LocalDateTime tenMinutesAgo = LocalDateTime.now().minusMinutes(10);
		
		PrivateChat chat = PrivateChat.builder().blockedBySender(false).blockedByRecipient(true).lastUpdate(tenMinutesAgo).build();
		
		when(this.mockService.getUnblockedChatsBeforeCuttoff(any())).thenReturn(Set.of(chat));
		
		this.scheduler.missedEmalMessageSender();
		
		verify(this.mockDao, never()).saveChat(any());
	}
	
	/**
	* Last viewed by Sender value is null. This should 
	* result in an email being sent
	*/
	@Test
	void testmissedEmalMessageSenderPastCuttoffLastSenderViewIsNull() {
		
		ArgumentCaptor<PrivateChat> chatArgCapt 		= ArgumentCaptor.forClass(PrivateChat.class);
		LocalDateTime 				tenMinutesAgo 		= LocalDateTime.now().minusMinutes(10);
		PrivateChat 				chat 				= PrivateChat.builder().id(UUID.randomUUID()).recipientId("").senderId("").blockedBySender(false).blockedByRecipient(false).lastUpdate(tenMinutesAgo).lastViewedBySender(null).lastViewedByRecipient(LocalDateTime.now()).build();
		
		when(this.mockService.getUnblockedChatsBeforeCuttoff(any())).thenReturn(Set.of(chat));	
		doNothing().when(this.mockDao).saveChat(chatArgCapt.capture());
		
		this.scheduler.missedEmalMessageSender();
		
		verify(this.mockDao).saveChat(any());
		
		assertNotNull(chatArgCapt.getValue().getLastMissedMessageAlertSender().get());
		
	}
	
	/**
	* Last viewed by Sender value is null. This should 
	* result in an email being sent
	*/
	@Test
	void testmissedEmalMessageSenderPastCuttoffLastRecipientViewIsNull() {
		
		ArgumentCaptor<PrivateChat> chatArgCapt 		= ArgumentCaptor.forClass(PrivateChat.class);
		LocalDateTime 				tenMinutesAgo 		= LocalDateTime.now().minusMinutes(10);
		PrivateChat 				chat 				= PrivateChat.builder().id(UUID.randomUUID()).recipientId("").senderId("").blockedBySender(false).blockedByRecipient(false).lastUpdate(tenMinutesAgo).lastViewedBySender(LocalDateTime.now()).lastViewedByRecipient(null).build();
		
		when(this.mockService.getUnblockedChatsBeforeCuttoff(any())).thenReturn(Set.of(chat));	
		doNothing().when(this.mockDao).saveChat(chatArgCapt.capture());
		
		this.scheduler.missedEmalMessageSender();
		
		verify(this.mockDao).saveChat(any());
		
		assertNotNull(chatArgCapt.getValue().getLastMissedMessageAlertRecipient().get());
		
	}
	
	/**
	* Last viewed by Sender value is before last update. This should 
	* result in an email being sent
	*/
	@Test
	void testmissedEmalMessageSenderPastCuttoffLastSenderViewIsBeforeLastUpdate() {
		
		ArgumentCaptor<PrivateChat> chatArgCapt 		= ArgumentCaptor.forClass(PrivateChat.class);
		LocalDateTime 				tenMinutesAgo 		= LocalDateTime.now().minusMinutes(10);
		LocalDateTime 				twentyMinutesAgo 	= LocalDateTime.now().minusMinutes(20);
		PrivateChat 				chat 				= PrivateChat.builder().id(UUID.randomUUID()).recipientId("").senderId("").blockedBySender(false).blockedByRecipient(false).lastUpdate(tenMinutesAgo).lastViewedBySender(twentyMinutesAgo).lastViewedByRecipient(LocalDateTime.now()).build();
		
		when(this.mockService.getUnblockedChatsBeforeCuttoff(any())).thenReturn(Set.of(chat));	
		doNothing().when(this.mockDao).saveChat(chatArgCapt.capture());
		
		this.scheduler.missedEmalMessageSender();
		
		verify(this.mockDao).saveChat(any());
		
		assertNotNull(chatArgCapt.getValue().getLastMissedMessageAlertSender().get());
		
	}
	
	/**
	* Last viewed by Recipient value is before last update. This should 
	* result in an email being sent
	*/
	@Test
	void testmissedEmalMessageSenderPastCuttoffLastRecipientViewIsBeforeLastUpdate() {
		
		ArgumentCaptor<PrivateChat> chatArgCapt 		= ArgumentCaptor.forClass(PrivateChat.class);
		LocalDateTime 				tenMinutesAgo 		= LocalDateTime.now().minusMinutes(10);
		LocalDateTime 				twentyMinutesAgo 	= LocalDateTime.now().minusMinutes(20);
		PrivateChat 				chat 				= PrivateChat.builder().id(UUID.randomUUID()).recipientId("").senderId("").blockedBySender(false).blockedByRecipient(false).lastUpdate(tenMinutesAgo).lastViewedBySender(LocalDateTime.now()).lastViewedByRecipient(twentyMinutesAgo).build();
		
		when(this.mockService.getUnblockedChatsBeforeCuttoff(any())).thenReturn(Set.of(chat));	
		doNothing().when(this.mockDao).saveChat(chatArgCapt.capture());
		
		this.scheduler.missedEmalMessageSender();
		
		verify(this.mockDao).saveChat(any());
		
		assertNotNull(chatArgCapt.getValue().getLastMissedMessageAlertRecipient().get());
		
	}
	
	/**
	* Last viewed by Sender value is before last update and the Sender has received an 
	* update but that update was before the Sender last viewed the chat. This should 
	* result in an email being sent
	*/
	@Test
	void testmissedEmalMessageSenderPastCuttoffLastSenderViewIsBeforeLastUpdatePriorEmailSent() {
		
		ArgumentCaptor<PrivateChat> chatArgCapt 		= ArgumentCaptor.forClass(PrivateChat.class);
		LocalDateTime 				tenMinutesAgo 		= LocalDateTime.now().minusMinutes(10);
		LocalDateTime 				fifteenMinutesAgo 	= LocalDateTime.now().minusMinutes(15);
		LocalDateTime 				twentyMinutesAgo 	= LocalDateTime.now().minusMinutes(20);
		PrivateChat 				chat 				= PrivateChat.builder().id(UUID.randomUUID()).recipientId("").senderId("").blockedBySender(false).blockedByRecipient(false).lastUpdate(tenMinutesAgo).lastViewedBySender(fifteenMinutesAgo).lastMissedMessageAlertSender(twentyMinutesAgo).lastViewedByRecipient(LocalDateTime.now()).build();
		
		when(this.mockService.getUnblockedChatsBeforeCuttoff(any())).thenReturn(Set.of(chat));	
		doNothing().when(this.mockDao).saveChat(chatArgCapt.capture());
		
		this.scheduler.missedEmalMessageSender();
		
		verify(this.mockDao).saveChat(any());
		
		assertNotNull(chatArgCapt.getValue().getLastMissedMessageAlertSender().get());
		
	}
	
	/**
	* Last viewed by Sender value is before last update and the Sender has received an 
	* update but that update was before the Sender last viewed the chat. This should 
	* result in an email being sent
	*/
	@Test
	void testmissedEmalMessageSenderPastCuttoffLastRecipientViewIsBeforeLastUpdatePriorEmailSent() {
		
		ArgumentCaptor<PrivateChat> chatArgCapt 		= ArgumentCaptor.forClass(PrivateChat.class);
		LocalDateTime 				tenMinutesAgo 		= LocalDateTime.now().minusMinutes(10);
		LocalDateTime 				fifteenMinutesAgo 	= LocalDateTime.now().minusMinutes(15);
		LocalDateTime 				twentyMinutesAgo 	= LocalDateTime.now().minusMinutes(20);
		PrivateChat 				chat 				= PrivateChat.builder().id(UUID.randomUUID()).recipientId("").senderId("").blockedBySender(false).blockedByRecipient(false).lastUpdate(tenMinutesAgo).lastViewedByRecipient(fifteenMinutesAgo).lastMissedMessageAlertRecipient(twentyMinutesAgo).lastViewedBySender(LocalDateTime.now()).build();
		
		when(this.mockService.getUnblockedChatsBeforeCuttoff(any())).thenReturn(Set.of(chat));	
		doNothing().when(this.mockDao).saveChat(chatArgCapt.capture());
		
		this.scheduler.missedEmalMessageSender();
		
		verify(this.mockDao).saveChat(any());
		
		assertNotNull(chatArgCapt.getValue().getLastMissedMessageAlertRecipient().get());
		
	}
	
}