package com.arenella.recruit.curriculum.adapters;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.arenella.recruit.adapters.events.CandidateNoLongerAvailableEvent;
import com.arenella.recruit.curriculum.dao.SkillsDao;
import com.arenella.recruit.curriculum.entity.SkillEntity;
import com.arenella.recruit.curriculum.services.CurriculumService;

/**
* Unit tests for the MonolithExternalEventListener class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class MonolithExternalEventListenerTest {

	@Mock
	private SkillsDao 						mockSkillsDao;
	
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
		ArgumentCaptor<Set<SkillEntity>> skillsCaptor = ArgumentCaptor.forClass(Set.class);
		
		Mockito.when(this.mockSkillsDao.saveAll(skillsCaptor.capture())).thenReturn(null);
		
		listener.listenForSearchedSkillsEvent(Set.of(skill1, skill2));
		
		skillsCaptor.getValue().stream().filter(s -> s.getSkill().equals(skill1Processed)).findAny().get();
		skillsCaptor.getValue().stream().filter(s -> s.getSkill().equals(skill2Processed)).findAny().get();
		
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
	
}