package com.arenella.recruit.messaging.services;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.arenella.recruit.messaging.beans.ChatParticipant;
import com.arenella.recruit.messaging.dao.ChatParticipantDao;

/**
* Unit tests for the ParticipantServiceImpl class 
*/
@ExtendWith(MockitoExtension.class)
class ParticipantServiceImplTest {

	@Mock
	private ChatParticipantDao mockDao;
	
	@InjectMocks
	private ParticipantServiceImpl service;
	
	/**
	* Tests ChatParticipant is persisted
	*/
	@Test
	void testPersistChatParticipant() {
		
		ChatParticipant chatParticipant = ChatParticipant.builder().build();
		
		this.service.persistParticpant(chatParticipant);
		
		verify(mockDao).persistChatParticipant(chatParticipant);
		
	}
	
	/**
	* Tests case attempt is made to delete an existing 
	* Chat participant
	*/
	@Test
	void testDeleteChatParticipantExistingChatParticipant() {
		
		final String chatParticipantId = "kp11";
		
		when(this.mockDao.fetchChatParticipantById(chatParticipantId)).thenReturn(Optional.of(ChatParticipant.builder().build()));
		
		this.service.deletePartipant(chatParticipantId);
		
		verify(this.mockDao).deleteById(chatParticipantId);
		
	}
	
	/**
	* Tests case attempt is made to delete a non existing 
	* Chat participant
	*/
	@Test
	void testDeleteChatParticipantNonExistingChatParticipant() {
		
		final String chatParticipantId = "kp11";
		
		when(this.mockDao.fetchChatParticipantById(chatParticipantId)).thenReturn(Optional.empty());
		
		this.service.deletePartipant(chatParticipantId);
		
		verify(this.mockDao, never()).deleteById(chatParticipantId);
		
	}
	
}
