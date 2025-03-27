package com.arenella.recruit.curriculum.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.arenella.recruit.curriculum.entity.CurriculumDownloadedEventEntity;

/**
* Unit tests for the CurriculumDownloadedEvent class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
class CurriculumDownloadedEventTest {

	@Mock
	private					Authentication					mockAuthentication;
	
	private static final String 			CURRICULUM_ID 		= "anId";
	private static final String 			USER_ID 			= "kparkings";
	private static final boolean 			IS_ADMIN_USER 		= true;
	private static final LocalDateTime 		timestamp 			= LocalDateTime.of(2021, 5, 28, 12, 05);
	
	/**
	* Tests manual builder. This is a builder to be used where the 
	* event already exists and needs to be reconstructed (think conversion
	* from domain to Entity representation)
	* @throws Exception
	*/
	@Test
	void testManualBuilder() {
		
		CurriculumDownloadedEvent event = CurriculumDownloadedEvent
														.manualBuilder()
															.curriculumId(CURRICULUM_ID)
															.userId(USER_ID)
															.isAdminUser(IS_ADMIN_USER)
															.timestamp(timestamp)
														.build();

		CurriculumDownloadedEventEntity entity = CurriculumDownloadedEventEntity.toEntity(event);
		
		assertEquals(CURRICULUM_ID, 	entity.getCurriculumId());
		assertEquals(USER_ID, 			entity.getUserId());
		assertEquals(IS_ADMIN_USER, 	entity.isAdminUser());
		assertEquals(timestamp, 		entity.getTimestamp());

	}
	
	/**
	* Tests builder. This is a builder to be used where the 
	* event is new (i.e not an existing event being converted) 
	* @throws Exception
	*/
	@Test
	void testBuilder() {
		
		SecurityContextHolder.getContext().setAuthentication(mockAuthentication);
		
		GrantedAuthority mockGrantedAuthority = Mockito.mock(GrantedAuthority.class);
		
		Mockito.when(mockAuthentication.getPrincipal()).thenReturn(USER_ID);
		
		Mockito.doReturn(List.of(mockGrantedAuthority)).when(mockAuthentication).getAuthorities();
		Mockito.when(mockGrantedAuthority.getAuthority()).thenReturn("ROLE_ADMIN");
		
		CurriculumDownloadedEvent event = CurriculumDownloadedEvent
														.builder()
															.curriculumId(CURRICULUM_ID)
														.build();

		CurriculumDownloadedEventEntity entity = CurriculumDownloadedEventEntity.toEntity(event);
		
		assertEquals(CURRICULUM_ID, 	entity.getCurriculumId());
		assertEquals(USER_ID, 			entity.getUserId());
		assertTrue(entity.isAdminUser());
		assertNotNull(entity.getTimestamp());

	}
	
	/**
	* Tests builder. This is a builder to be used where the 
	* event is new (i.e not an existing event being converted).
	* Non Admin user version of test
	* @throws Exception
	*/
	@Test
	void testBuilder_nonAdminUser() {
		
		SecurityContextHolder.getContext().setAuthentication(mockAuthentication);
		
		GrantedAuthority mockGrantedAuthority = Mockito.mock(GrantedAuthority.class);
		
		Mockito.when(mockAuthentication.getPrincipal()).thenReturn(USER_ID);
		
		Mockito.doReturn(List.of(mockGrantedAuthority)).when(mockAuthentication).getAuthorities();
		Mockito.when(mockGrantedAuthority.getAuthority()).thenReturn("ROLE_RECRUITER");
		
		CurriculumDownloadedEvent event = CurriculumDownloadedEvent
														.builder()
															.curriculumId(CURRICULUM_ID)
														.build();

		CurriculumDownloadedEventEntity entity = CurriculumDownloadedEventEntity.toEntity(event);
		
		assertEquals(CURRICULUM_ID, 	entity.getCurriculumId());
		assertEquals(USER_ID, 			entity.getUserId());
		assertFalse(entity.isAdminUser());
		assertNotNull(entity.getTimestamp());

	}
}