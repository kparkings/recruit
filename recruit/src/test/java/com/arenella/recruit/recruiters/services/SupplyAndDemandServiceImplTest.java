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

import com.arenella.recruit.recruiters.beans.OfferedCandidate;
import com.arenella.recruit.recruiters.beans.OpenPosition;
import com.arenella.recruit.recruiters.dao.OfferedCandidateDao;
import com.arenella.recruit.recruiters.dao.OpenPositionDao;

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
	private OpenPositionDao 			mockOpenPositionDao;
	
	@Mock
	private OfferedCandidateDao			mockOfferedCandidateDao;
	
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
		
		Mockito.verify(mockOpenPositionDao).persistOpenPositiion(captor.capture());
		
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
		
		Mockito.when(this.mockOpenPositionDao.findByOpenPositionId(openPositionId)).thenReturn(openPosition);
		
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
		
		Mockito.when(this.mockOpenPositionDao.findByOpenPositionId(openPositionId)).thenReturn(openPosition);
		
		Assertions.assertThrows(IllegalAccessException.class, () -> {
			service.deleteOpenPosition(openPositionId);
		});
		
	}
	
	@Test
	public void testUpdateOpenPosition() throws Exception{
		
		UUID 			openPositionId 	= UUID.randomUUID();
		OpenPosition 	openPosition 	= OpenPosition.builder().recruiterId(RECRUITER_ID).build();
		
		Mockito.when(this.mockOpenPositionDao.findByOpenPositionId(openPositionId)).thenReturn(openPosition);
		
		service.updateOpenPosition(openPositionId, openPosition);
		
		Mockito.verify(this.mockOpenPositionDao).updateExistingOpenPosition(openPositionId, openPosition);
	}
	
	@Test
	public void testUpdateOpenPosition_wrongUser() throws Exception{
		
		UUID 			openPositionId 	= UUID.randomUUID();
		OpenPosition 	openPosition 	= OpenPosition.builder().recruiterId("AnotherRecruitersId").build();
		
		Mockito.when(this.mockOpenPositionDao.findByOpenPositionId(openPositionId)).thenReturn(openPosition);
		
		Assertions.assertThrows(IllegalAccessException.class, () -> {
			service.updateOpenPosition(openPositionId, openPosition);
		});
		
	}
	
	/**
	* Tests persisting of an OfferedCandidate
	* @throws Exception
	*/
	@Test
	public void testAddOfferedCandidate() throws Exception{
		
		ArgumentCaptor<OfferedCandidate> captor = ArgumentCaptor.forClass(OfferedCandidate.class);
		
		OfferedCandidate offeredCandidate = OfferedCandidate.builder().build();
		
		service.addOfferedCandidate(offeredCandidate);
		
		Mockito.verify(mockOfferedCandidateDao).persistOfferedCandidate(captor.capture());
		
		assertEquals(RECRUITER_ID, captor.getValue().getRecruiterId());
		
	}
	
	/**
	* Happy path for deleting an OfferedCandidate
	* @throws Exception
	*/
	@Test
	public void testDeleteOfferedCandidate() throws Exception{
		
		UUID 				offeredCandidateId 		= UUID.randomUUID();
		OfferedCandidate 	offeredCandidate 		= OfferedCandidate.builder().recruiterId(RECRUITER_ID).build();
		
		Mockito.when(this.mockOfferedCandidateDao.findByOfferedCandidateId(offeredCandidateId)).thenReturn(offeredCandidate);
		
		service.deleteOfferedCandidate(offeredCandidateId);
		
	}
	
	/**
	* Failure path where OfferedCandidate does not exist
	* @throws Exception
	*/
	@Test
	public void testDeleteOfferedCandidate_unknownOpenPosition() throws Exception{
		
		UUID offeredCandidateId 	= UUID.randomUUID();
		
		Assertions.assertThrows(RuntimeException.class, () -> {
			service.deleteOfferedCandidate(offeredCandidateId);
		});
		
	}
	
	/**
	* Failure position where an attempt is made to delete another users 
	* OfferedCandidate
	* @throws Exception
	*/
	@Test
	public void testDeleteOfferedCandidate_wrongUser() throws Exception{
		
		UUID 				offeredCandidateId 	= UUID.randomUUID();
		OfferedCandidate 	offeredCandidate 	= OfferedCandidate.builder().recruiterId("AnotherRecruitersId").build();
		
		Mockito.when(this.mockOfferedCandidateDao.findByOfferedCandidateId(offeredCandidateId)).thenReturn(offeredCandidate);
		
		Assertions.assertThrows(IllegalAccessException.class, () -> {
			service.deleteOfferedCandidate(offeredCandidateId);
		});
		
	}
	
}
