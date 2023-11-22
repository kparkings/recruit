package com.arenella.recruit.curriculum.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.arenella.recruit.adapters.events.CandidateDeletedEvent;
import com.arenella.recruit.adapters.events.CandidateNoLongerAvailableEvent;
import com.arenella.recruit.adapters.events.RecruiterCreatedEvent;
import com.arenella.recruit.curriculum.dao.CurriculumSkillsDao;
import com.arenella.recruit.curriculum.services.CurriculumService;

/**
* Unit tests for the MonolithExternalEventListener class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class MonolithExternalEventListenerTest {

	@Mock
	private CurriculumSkillsDao 			mockSkillsDao;
	
	@Mock
	private CurriculumService 				mockCurriculumService;
	
	@InjectMocks
	private MonolithExternalEventListener 	listener = new MonolithExternalEventListener();
	
	/**
	* Tests SearchSkillsEvent listener
	* @throws Exception
	*/
	@Test
	public void testListenForSearchedSkillsEvent() throws Exception {
		
		final String skill1 = " aSkill1";
		final String skill2 = "aSkill2 ";
		
		final String skill1Processed = "askill1";
		final String skill2Processed = "askill2";
		
		@SuppressWarnings("unchecked")
		ArgumentCaptor<Set<String>> skillsCaptor = ArgumentCaptor.forClass(Set.class);
		
		Mockito.doNothing().when(this.mockSkillsDao).persistSkills(skillsCaptor.capture());
		
		listener.listenForSearchedSkillsEvent(Set.of(skill1, skill2));
		
		skillsCaptor.getValue().stream().filter(s -> s.equals(skill1Processed)).findAny().get();
		skillsCaptor.getValue().stream().filter(s -> s.equals(skill2Processed)).findAny().get();
		
	}
	
	/**
	* Test handling of listenForCandidateNoLongerAvailableEvent 
	* @throws Exception
	*/
	@Test
	public void testListenForCandidateNoLongerAvailableEvent() throws Exception {
		
		final long candidateId = 123L;
		
		CandidateNoLongerAvailableEvent event = new CandidateNoLongerAvailableEvent(candidateId);
		
		this.listener.listenForCandidateNoLongerAvailableEvent(event);
		
		Mockito.verify(mockCurriculumService).deleteCurriculum(candidateId);
		
	}
	
	/**
	* Tests only new skills added to DB
	* @throws Exception
	*/
	@Test
	public void testOnlyNewSkillsAdded() throws Exception{
		
		final String skill1 = "JAVA";
		final String skill2 = "c#";
		final String skill3 = "hibernate";
		
		@SuppressWarnings("unchecked")
		ArgumentCaptor<Set<String>> skillArgCaptor = ArgumentCaptor.forClass(Set.class);
		
		Set<String> newSkills 		= Set.of(skill1,skill2,skill3);
		
		Mockito.when(mockSkillsDao.existsById(skill1.toLowerCase())).thenReturn(false);
		Mockito.when(mockSkillsDao.existsById(skill2.toLowerCase())).thenReturn(true);
		Mockito.when(mockSkillsDao.existsById(skill3.toLowerCase())).thenReturn(false);
		Mockito.doNothing().when(mockSkillsDao).persistSkills(skillArgCaptor.capture());
		
		this.listener.listenForSearchedSkillsEvent(newSkills);
		
		Set<String> persistedSkills = skillArgCaptor.getValue();
		
		assertEquals(2, persistedSkills.size());
		assertTrue(persistedSkills.contains(skill1.toLowerCase()));
		assertTrue(persistedSkills.contains(skill3.toLowerCase()));
		
	}
	
	/**
	* Tests only new skills added to DB
	* @throws Exception
	*/
	@Test
	public void testOnlyNewSkillsAdded_noNewSkills() throws Exception{
		
		final String skill1 = "JAVA";
		final String skill2 = "c#";
		final String skill3 = "hibernate";
		
		Set<String> newSkills 		= Set.of(skill1,skill2,skill3);
		
		Mockito.when(mockSkillsDao.existsById(skill1.toLowerCase())).thenReturn(true);
		Mockito.when(mockSkillsDao.existsById(skill2.toLowerCase())).thenReturn(true);
		Mockito.when(mockSkillsDao.existsById(skill3.toLowerCase())).thenReturn(true);
		
		this.listener.listenForSearchedSkillsEvent(newSkills);
		
		Mockito.verify(this.mockSkillsDao, Mockito.never()).persistSkills(Mockito.anySet());
		
	}
	
	/**
	* Test Curriculum is removed if Candidate is Deletes
	* @throws Exception
	*/
	@Test
	public void testListenForCandidteDeletedEvent() throws Exception {
		
		final String candidateId = "1222";
		
		CandidateDeletedEvent event = new CandidateDeletedEvent(candidateId);
		
		this.listener.listenForCandidteDeletedEvent(event);
		
		Mockito.verify(this.mockCurriculumService).deleteCurriculum(Long.valueOf(candidateId));
		
	}
	
	/**
	* Tests handling of Recruiter created event
	* @throws Exception
	*/
	@Test
	public void testListenForRecruiterCreatedEvent() throws Exception{
		
		this.listener.listenForRecruiterCreatedEvent(RecruiterCreatedEvent.builder().recruiterId("rec22").build());
		
		Mockito.verify(this.mockCurriculumService).addCreditsRecordForUser(Mockito.anyString());
		
	}
	
}