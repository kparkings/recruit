package com.arenella.recruit.recruiters.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

import com.arenella.recruit.curriculum.entity.CurriculumEntity;
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
	
}
