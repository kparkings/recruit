package com.arenella.recruit.candidates.adapters;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.Set;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.CandidateFilterOptions;
import com.arenella.recruit.candidates.beans.CandidateSearchAlert;
import com.arenella.recruit.candidates.beans.PendingCandidate;
import com.arenella.recruit.candidates.controllers.CandidateController.CANDIDATE_UPDATE_ACTIONS;
import com.arenella.recruit.candidates.dao.CandidateDao;
import com.arenella.recruit.candidates.dao.CandidateSearchAlertDao;
import com.arenella.recruit.candidates.dao.PendingCandidateDao;
import com.arenella.recruit.candidates.entities.CandidateEntity;
import com.arenella.recruit.candidates.entities.PendingCandidateEntity;
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
	private PendingCandidateDao 		mockPendingCandidateDao;
	
	@Mock
	private ExternalEventPublisher		mockExternalEventPublisher;
	
	@Mock
	private CandidateStatisticsService 	mockStatisticsService;
	
	@Mock
	private CandidateSearchAlertDao		mockSkillAlertDao;
	
	@Mock
	private SecurityContext				mockSecurityContext;
	
	@Mock
	private Authentication				mockAuthentication;
	
	@InjectMocks
	private CandidateServiceImpl 		service 					= new CandidateServiceImpl();
	
	@BeforeEach
	public void init() throws Exception{
		SecurityContextHolder.setContext(mockSecurityContext);
		
	}
	
	/**
	* Tests Exception is thrown if attempt is made to 
	* update a non existent Candidate
	* @throws Exception
	*/
	@Test
	public void testUpdateCandidate_unknownCandidate() throws Exception {
		
		Mockito.when(mockCandidateDao.findCandidateById(0)).thenReturn(Optional.empty());
		
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
		
		ArgumentCaptor<Candidate> 	captor 		= ArgumentCaptor.forClass(Candidate.class);
		Candidate 					candidate 	= Candidate.builder().available(false).build();
		
		Mockito.when(mockCandidateDao.findCandidateById(1L)).thenReturn(Optional.of(candidate));
		
		this.service.updateCandidate("1", CANDIDATE_UPDATE_ACTIONS.enable);
		
		Mockito.verify(mockCandidateDao).saveCandidate(captor.capture());
		Mockito.verify(this.mockExternalEventPublisher, Mockito.never()).publishCandidateNoLongerAvailableEvent(Mockito.any());
		
		assertTrue(captor.getValue().isAvailable());
		
	}
	
	/**
	* Tests setting the Candidate as not active
	* @throws Exception
	*/
	@Test
	public void testUpdateCandidate_disable() throws Exception {
		
		ArgumentCaptor<Candidate> 	captor 		= ArgumentCaptor.forClass(Candidate.class);
		Candidate 					candidate 	= Candidate.builder().candidateId("123").available(true).build();
		
		Mockito.when(mockCandidateDao.findCandidateById(1L)).thenReturn(Optional.of(candidate));
		
		this.service.updateCandidate("1", CANDIDATE_UPDATE_ACTIONS.disable);
		
		Mockito.verify(mockCandidateDao).saveCandidate(captor.capture());
		Mockito.verify(this.mockExternalEventPublisher).publishCandidateNoLongerAvailableEvent(Mockito.any());
		
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
	
	/**
	* Tests Exception thrown if Candidate does not exist
	* @throws Exception
	*/
	@Test
	public void testUpdateCandidatesLastAvailabilityCheck_unknownCandidate() throws Exception{
		
		Mockito.when(this.mockCandidateDao.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.service.updateCandidatesLastAvailabilityCheck(4L);
		});
		
	}
	
	/**
	* Test setting of flaggedAsUnavailable attribute
	* @throws Exception
	*/
	@Test
	public void testUpdateCandidatesLastAvailabilityCheck() throws Exception{
		
		ArgumentCaptor<CandidateEntity> captor = ArgumentCaptor.forClass(CandidateEntity.class);
		CandidateEntity candidateEntity = CandidateEntity.builder().build();
		
		Mockito.when(this.mockCandidateDao.findById(Mockito.anyLong())).thenReturn(Optional.of(candidateEntity));
		Mockito.when(this.mockCandidateDao.save(captor.capture())).thenReturn(null);
		
		this.service.updateCandidatesLastAvailabilityCheck(4L);
		
		Mockito.verify(this.mockCandidateDao).save(candidateEntity);
		
	}

	
	/**
	* Tests Exception is thrown if an attempt is made to persist 
	* a PendingCandidate without specifying an Id
	* @throws Exception
	*/
	@Test
	public void testPersistPendingCandidate_noId() throws Exception {
		
		PendingCandidate pendingCandidate = PendingCandidate.builder().build();
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.service.persistPendingCandidate(pendingCandidate);
		});
		
	}
	
	/**
	* Tests Exception is thrown if an attempt is made to persist 
	* a PendingCandidate with an existing Id
	* @throws Exception
	*/
	@Test
	public void testPersistPendingCandidate_existingId() throws Exception {
		
		PendingCandidate pendingCandidate = PendingCandidate.builder().build();
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.service.persistPendingCandidate(pendingCandidate);
		});
		
	}
	
	/**
	* Tests successful persisting of PendingCandidate
	* @throws Exception
	*/
	@Test
	public void testPersistPendingCandidate() throws Exception {
		
		PendingCandidate pendingCandidate = PendingCandidate
													.builder()
														.pendingCandidateId(UUID.randomUUID())
													.build();
		
		Mockito.when(mockPendingCandidateDao.existsById(Mockito.any())).thenReturn(false);
		
		this.service.persistPendingCandidate(pendingCandidate);
		
		Mockito.verify(mockPendingCandidateDao).save(Mockito.any(PendingCandidateEntity.class));
		
	}
	
	/**
	* Tests persisting of Alert
	* @throws Exception
	*/
	@Test
	public void testAddSearchAlert() throws Exception{
		
		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		
		Mockito.when(mockAuthentication.getPrincipal()).thenReturn("reruiter1");

		CandidateSearchAlert alert = CandidateSearchAlert.builder().build();
		
		this.service.addSearchAlert(alert);
		
		assertNotNull(alert.getAlertId());
		
		Mockito.verify(this.mockSkillAlertDao).saveAlert(alert);
		
	}
	
	/**
	* Tests retrieval of Alerts for current Recruiter
	* @throws Exception
	*/
	@Test
	public void testGetAlertsForCurrentUser() throws Exception{
		
		final String recruiterId = "reruiter1";
		
		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		
		Mockito.when(mockAuthentication.getPrincipal()).thenReturn(recruiterId);
		
		Mockito.when(this.mockSkillAlertDao
				.fetchAlertsByRecruiterId(Mockito.anyString()))
				.thenReturn(Set.of(CandidateSearchAlert.builder().build(),
						CandidateSearchAlert.builder().build()));
		
		Set<CandidateSearchAlert> alerts = this.service.getAlertsForCurrentUser();
		
		assertEquals(2, alerts.size());
		
		Mockito.verify(this.mockSkillAlertDao).fetchAlertsByRecruiterId(recruiterId);
		
	}
	
	/**
	* Tests attempt made to delete unknown Alert
	* @throws Exception
	*/
	@Test
	public void testDeleteCandidateSearchAlert_unknownId() throws Exception{
		
		UUID id = UUID.randomUUID();
		
		Mockito.when(this.mockSkillAlertDao.getchAlertById(id)).thenReturn(Optional.empty());
		
		assertThrows(IllegalArgumentException.class, () -> {
			this.service.deleteSearchAlert(id);
		});
		
	}
	
	/**
	* Tests if User is owner of Alert the Alert is Deleted
	* @throws Exception
	*/
	@Test
	public void testDeleteCandidateSearchAlert_owner() throws Exception{
		
		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		
		Mockito.when(mockAuthentication.getPrincipal()).thenReturn("reruiter1");
		
		UUID id = UUID.randomUUID();
		
		Mockito.when(this.mockSkillAlertDao.getchAlertById(id)).thenReturn(Optional.of(CandidateSearchAlert.builder().recruiterId("reruiter1").build()));
		
		this.service.deleteSearchAlert(id);
		
		Mockito.verify(this.mockSkillAlertDao).deleteById(id);
		
	}
	
	/**
	* Tests if User is not the owner of Alert the Alert is not Deleted
	* @throws Exception
	*/
	@Test
	public void testDeleteCandidateSearchAlert_isNotOwner() throws Exception{
		
		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		
		Mockito.when(mockAuthentication.getPrincipal()).thenReturn("reruiter1");
		
		UUID id = UUID.randomUUID();
		
		Mockito.when(this.mockSkillAlertDao.getchAlertById(id)).thenReturn(Optional.of(CandidateSearchAlert.builder().recruiterId("r1").build()));
		
		assertThrows(IllegalArgumentException.class, () -> {
			this.service.deleteSearchAlert(id);
			
		});
		
	}
	
}