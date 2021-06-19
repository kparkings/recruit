package com.arenella.recruit.candidate.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import com.arenella.recruit.candidates.controllers.CandidateController.CANDIDATE_UPDATE_ACTIONS;
import com.arenella.recruit.candidates.dao.CandidateDao;
import com.arenella.recruit.candidates.entities.CandidateEntity;
import com.arenella.recruit.candidates.services.CandidateServiceImpl;

/**
* Unit tests for the CandidateServiceImpl class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class CandidateServiceImplTest {

	@Mock
	private CandidateDao mockCandidateDao;
	
	@InjectMocks
	private CandidateServiceImpl service = new CandidateServiceImpl();
	
	/**
	* Tests Exception is thrown if attempt is made to 
	* update a non existent Candidate
	* @throws Exception
	*/
	@Test
	public void testUpdateCandidate_unknownCandidate() throws Exception {
		
		Mockito.when(mockCandidateDao.findById(1L)).thenReturn(Optional.empty());
		
		Assertions.assertThrows(RuntimeException.class , () -> {
			service.updateCandidate("1", CANDIDATE_UPDATE_ACTIONS.disable);
		});
		
	}
	
	/**
	* Tests setting a Candidate as active
	* @throws Exception
	*/
	@Test
	public void testUpdateCandidate_enable() throws Exception {
		
		ArgumentCaptor<CandidateEntity> 	captor 		= ArgumentCaptor.forClass(CandidateEntity.class);
		CandidateEntity 					candidate 	= CandidateEntity.builder().available(false).build();
		
		Mockito.when(mockCandidateDao.findById(1L)).thenReturn(Optional.of(candidate));
		Mockito.when(mockCandidateDao.save(captor.capture())).thenReturn(candidate);
		
		this.service.updateCandidate("1", CANDIDATE_UPDATE_ACTIONS.enable);
		
		Mockito.verify(mockCandidateDao).save(candidate);
		
		assertTrue(captor.getValue().isAvailable());
		
	}
	
	/**
	* Tests setting the Candidate as not active
	* @throws Exception
	*/
	@Test
	public void testUpdateCandidate_disable() throws Exception {
		
		ArgumentCaptor<CandidateEntity> 	captor 		= ArgumentCaptor.forClass(CandidateEntity.class);
		CandidateEntity 					candidate 	= CandidateEntity.builder().available(true).build();
		
		Mockito.when(mockCandidateDao.findById(1L)).thenReturn(Optional.of(candidate));
		Mockito.when(mockCandidateDao.save(captor.capture())).thenReturn(candidate);
		
		this.service.updateCandidate("1", CANDIDATE_UPDATE_ACTIONS.disable);
		
		Mockito.verify(mockCandidateDao).save(candidate);
		
		assertFalse(captor.getValue().isAvailable());
		
	}
	
}