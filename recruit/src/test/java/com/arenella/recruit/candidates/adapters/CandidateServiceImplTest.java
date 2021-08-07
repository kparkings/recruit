package com.arenella.recruit.candidates.adapters;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.CandidateFilterOptions;
import com.arenella.recruit.candidates.controllers.CandidateController.CANDIDATE_UPDATE_ACTIONS;
import com.arenella.recruit.candidates.dao.CandidateDao;
import com.arenella.recruit.candidates.entities.CandidateEntity;
import com.arenella.recruit.candidates.services.CandidateServiceImpl;
import com.arenella.recruit.candidates.services.CandidateStatisticsService;

/**
* Unit tests for the CandidateServiceImpl class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class CandidateServiceImplTest {

	@Mock
	private CandidateDao 				mockCandidateDao;
	
	@Mock
	private ExternalEventPublisher		mockExternalEventPublisher;
	
	@Mock
	private CandidateStatisticsService 	mockStatisticsService;
	
	@InjectMocks
	private CandidateServiceImpl 		service 					= new CandidateServiceImpl();
	
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
	
	/**
	* Tests retrieval of Candidates
	* @throws Exception
	*/
	@Test
	public void testGetCandidates_pagination() throws Exception {
		
		final String candidateId 	= "101";
		final String skill1			= "skillOne";
		final String skill2			= "skillTwo";
		
		@SuppressWarnings("unchecked")
		ArgumentCaptor<Set<String>> 	skillsArgumentCaptor 	= ArgumentCaptor.forClass(Set.class);
		CandidateEntity 				candidateEntity 		= CandidateEntity.builder().candidateId(candidateId).build();
		
		Mockito.when(this.mockCandidateDao.findAll(Mockito.any(CandidateFilterOptions.class), Mockito.any(Pageable.class))).thenReturn(new PageImpl<>(List.of(candidateEntity)));
		Mockito.doNothing().when(this.mockExternalEventPublisher).publishSearchedSkillsEvent(skillsArgumentCaptor.capture());
		Mockito.doNothing().when(this.mockStatisticsService).logCandidateSearchEvent(Mockito.any(CandidateFilterOptions.class));
		
		Page<Candidate> candidates = this.service.getCandidates(CandidateFilterOptions.builder().skills(Set.of(skill1, skill2)).build(), Pageable.unpaged());
		
		Mockito.verify(this.mockStatisticsService).logCandidateSearchEvent(Mockito.any(CandidateFilterOptions.class));
		
		assertEquals(candidateId, candidates.get().findFirst().get().getCandidateId());
		assertTrue(skillsArgumentCaptor.getValue().contains(skill1));
		assertTrue(skillsArgumentCaptor.getValue().contains(skill2));
		
	}

	/**
	* Tests retrieval of Candidates
	* @throws Exception
	*/
	@Test
	public void testGetCandidates() throws Exception {
		
		final String candidateId 	= "101";
		final String skill1			= "skillOne";
		final String skill2			= "skillTwo";
		
		@SuppressWarnings("unchecked")
		ArgumentCaptor<Set<String>> 	skillsArgumentCaptor 	= ArgumentCaptor.forClass(Set.class);
		CandidateEntity 				candidateEntity 		= CandidateEntity.builder().candidateId(candidateId).build();
		
		Mockito.when(this.mockCandidateDao.findAll(Mockito.any(CandidateFilterOptions.class))).thenReturn(List.of(candidateEntity));
		Mockito.doNothing().when(this.mockExternalEventPublisher).publishSearchedSkillsEvent(skillsArgumentCaptor.capture());
		Mockito.doNothing().when(this.mockStatisticsService).logCandidateSearchEvent(Mockito.any(CandidateFilterOptions.class));
		
		Set<Candidate> candidates = this.service.getCandidates(CandidateFilterOptions.builder().skills(Set.of(skill1, skill2)).build());
		
		Mockito.verify(this.mockStatisticsService).logCandidateSearchEvent(Mockito.any(CandidateFilterOptions.class));
		
		assertEquals(candidateId, candidates.stream().findFirst().get().getCandidateId());
		assertTrue(skillsArgumentCaptor.getValue().contains(skill1));
		assertTrue(skillsArgumentCaptor.getValue().contains(skill2));
		
	}
	
	/**
	* Tests Exception thrown if Candidate does not exist
	* @throws Exception
	*/
	@Test
	public void testFlagCandidateAvailability_unknownCandidate() throws Exception{
		
		Mockito.when(this.mockCandidateDao.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.service.flagCandidateAvailability(4L, false);
		});
		
	}
	
	/**
	* Test setting of flaggedAsUnavailable attribute
	* @throws Exception
	*/
	@Test
	public void testFlagCandidateAvailability() throws Exception{
		
		ArgumentCaptor<CandidateEntity> captor = ArgumentCaptor.forClass(CandidateEntity.class);
		CandidateEntity candidateEntity = CandidateEntity.builder().build();
		
		Mockito.when(this.mockCandidateDao.findById(Mockito.anyLong())).thenReturn(Optional.of(candidateEntity));
		Mockito.when(this.mockCandidateDao.save(captor.capture())).thenReturn(null);
		
		this.service.flagCandidateAvailability(4L, true);
		
		Mockito.verify(this.mockCandidateDao).save(candidateEntity);
		
		assertTrue(captor.getValue().isFlaggedAsUnavailable());
		
	}
	
}