package com.arenella.recruit.recruiters.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.arenella.recruit.recruiters.beans.OpenPosition;
import com.arenella.recruit.recruiters.dao.SupplyAndDemanDao;

/**
* Unit tests for the SupplyAndDemandServiceImpl class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class SupplyAndDemandServiceImplTest {

	private static final String RECRUITER_ID = "recruiterId1";
	
	@InjectMocks
	private SupplyAndDemandServiceImpl 	service;
	
	@Mock
	private SupplyAndDemanDao 			mockSupplyAndDemandDao;
	
	@Mock
	private	Authentication				mockAuthentication;
	
	/**
	* Sets up test environment
	* @throws Exception
	*/
	@BeforeEach
	public void init() throws Exception {
	
		SecurityContextHolder.getContext().setAuthentication(mockAuthentication);
		Mockito.when(mockAuthentication.getPrincipal()).thenReturn(RECRUITER_ID);
		
	}
	
	/**
	* Tests happy path for persisting an OpenPosition
	* @throws Exception
	*/
	@Test
	public void testAddOpenPosition() throws Exception{
		
		ArgumentCaptor<OpenPosition> captor = ArgumentCaptor.forClass(OpenPosition.class);
		
		OpenPosition openPosition = OpenPosition.builder().build();
		
		service.addOpenPosition(openPosition);
		
		Mockito.verify(mockSupplyAndDemandDao).persistOpenPositiion(captor.capture());
		
		assertEquals(RECRUITER_ID, captor.getValue().getRecruiterId());
		
	}
	
	/**
	* Happy path for deleting an OpenPosition
	* @throws Exception
	*/
	@Test
	public void testDeleteOpenPosition() throws Exception{
		
		UUID 			openPositionId 	= UUID.randomUUID();
		OpenPosition 	openPosition 	= OpenPosition.builder().recruiterId(RECRUITER_ID).build();
		
		Mockito.when(this.mockSupplyAndDemandDao.findByOpenPositionId(openPositionId)).thenReturn(openPosition);
		
		service.deleteOpenPosition(openPositionId);
		
	}
	
	/**
	* Failure path where OpenPosition does not exist
	* @throws Exception
	*/
	@Test
	public void testDeleteOpenPosition_unknownOpenPosition() throws Exception{
		
		UUID 			openPositionId 	= UUID.randomUUID();
		
		Assertions.assertThrows(RuntimeException.class, () -> {
			service.deleteOpenPosition(openPositionId);
		});
		
	}
	
	/**
	* Failure position where an attempt is made to delete another users 
	* OpenPosition
	* @throws Exception
	*/
	@Test
	public void testDeleteOpenPosition_wrongUser() throws Exception{
		
		UUID 			openPositionId 	= UUID.randomUUID();
		OpenPosition 	openPosition 	= OpenPosition.builder().recruiterId("AnotherRecruitersId").build();
		
		Mockito.when(this.mockSupplyAndDemandDao.findByOpenPositionId(openPositionId)).thenReturn(openPosition);
		
		Assertions.assertThrows(IllegalAccessException.class, () -> {
			service.deleteOpenPosition(openPositionId);
		});
		
	}
	
	@Test
	public void testUpdateOpenPosition() throws Exception{
		
		UUID 			openPositionId 	= UUID.randomUUID();
		OpenPosition 	openPosition 	= OpenPosition.builder().recruiterId(RECRUITER_ID).build();
		
		Mockito.when(this.mockSupplyAndDemandDao.findByOpenPositionId(openPositionId)).thenReturn(openPosition);
		
		service.updateOpenPosition(openPositionId, openPosition);
		
		Mockito.verify(this.mockSupplyAndDemandDao).updateExistingOpenPosition(openPositionId, openPosition);
	}
	
	@Test
	public void testUpdateOpenPosition_wrongUser() throws Exception{
		
		UUID 			openPositionId 	= UUID.randomUUID();
		OpenPosition 	openPosition 	= OpenPosition.builder().recruiterId("AnotherRecruitersId").build();
		
		Mockito.when(this.mockSupplyAndDemandDao.findByOpenPositionId(openPositionId)).thenReturn(openPosition);
		
		Assertions.assertThrows(IllegalAccessException.class, () -> {
			service.updateOpenPosition(openPositionId, openPosition);
		});
		
	}
	
}
