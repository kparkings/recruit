package com.arenella.recruit.candidates.adapters;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.arenella.recruit.adapters.events.CandidateNoLongerAvailableEvent;
import com.arenella.recruit.candidates.beans.CandidateSearchAlertMatch;
import com.arenella.recruit.curriculum.adapters.ExternalEventListener;
import com.arenella.recruit.emailservice.adapters.EmailServiceExternalEventListener;
import com.arenella.recruit.emailservice.adapters.RequestSendEmailCommand;

/**
* Unit tests for the MonolithExternalEventPublisher class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class MonolithExternalEventPublisherTest {
	
	@Mock
	private ExternalEventListener 				mockCurriculumEventListener;

	@Mock
	private EmailServiceExternalEventListener 	mockEmailServiceListener;
	
	@InjectMocks
	private MonolithExternalEventPublisher 		publisher;
	
	/**
	* Tests publishing of Event representing searched skills 
	* to external services
	* @throws Exception
	*/
	@Test
	public void testPublishSearchedSkillsEvent() throws Exception {
		
		final String skill1 = "aSkill1";
		final String skill2 = "aSkill2";
		
		List<GrantedAuthority> roles = List.of("ROLE_RECRUITER").stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
		
		UsernamePasswordAuthenticationToken  authToken = new UsernamePasswordAuthenticationToken("username", "", roles);
		
		SecurityContextHolder.getContext().setAuthentication(authToken);
		
		@SuppressWarnings("unchecked")
		ArgumentCaptor<Set<String>> skillsCaptor = ArgumentCaptor.forClass(Set.class);
		
		Mockito.doNothing().when(this.mockCurriculumEventListener).listenForSearchedSkillsEvent(skillsCaptor.capture());
		
		publisher.publishSearchedSkillsEvent(Set.of(skill1,skill2));
		
		assertTrue(skillsCaptor.getValue().contains(skill1));
		assertTrue(skillsCaptor.getValue().contains(skill2));
		
	}

	/**
	* Tests publishing of Event representing searched skills 
	* to external services
	* @throws Exception
	*/
	@Test
	public void testPublishSearchedSkillsEvent_admin() throws Exception {
		
		final String skill1 = "aSkill1";
		final String skill2 = "aSkill2";
		
		List<GrantedAuthority> roles = List.of("ROLE_ADMIN").stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
		
		UsernamePasswordAuthenticationToken  authToken = new UsernamePasswordAuthenticationToken("username", "", roles);
		
		SecurityContextHolder.getContext().setAuthentication(authToken);
	
		publisher.publishSearchedSkillsEvent(Set.of(skill1,skill2));
		
		Mockito.verify(this.mockCurriculumEventListener, Mockito.times(1)).listenForSearchedSkillsEvent(Mockito.anySet());
		
	}

	/**
	* Tests listener is called when Event is published
	* @throws Exception
	*/
	@Test
	public void testPublishPendingCurriculumDeletedEvent() throws Exception {
		
		Mockito.doNothing().when(mockCurriculumEventListener).listenForPendingCurriculumDeletedEvent(Mockito.any());
		
		publisher.publishPendingCurriculumDeletedEvent(UUID.randomUUID());
		
		Mockito.verify(mockCurriculumEventListener).listenForPendingCurriculumDeletedEvent(Mockito.any());
	
	}

	/**
	* Tests listener is called when Event is published
	* @throws Exception
	*/
	@Test
	public void testPublishCandidateNoLongerAvailableEvent() throws Exception {
		
		final long candidateId = 123L;
		
		CandidateNoLongerAvailableEvent event = new CandidateNoLongerAvailableEvent(candidateId);
		
		Mockito.doNothing().when(this.mockCurriculumEventListener).listenForCandidateNoLongerAvailableEvent(Mockito.any());
		
		publisher.publishCandidateNoLongerAvailableEvent(event);
		
		Mockito.verify(this.mockCurriculumEventListener).listenForCandidateNoLongerAvailableEvent(event);
		
	}
	
	/**
	* Tests listener is called when Event is published
	* @throws Exception
	*/
	@Test
	public void testPublishCandidateCreatedEvent() throws Exception{
		
		CandidateCreatedEvent event = CandidateCreatedEvent.builder().build();
		
		Mockito.doNothing().when(this.mockCurriculumEventListener).listenForCandidateCreatedEvent(Mockito.any());
		
		publisher.publishCandidateCreatedEvent(event);
		
		Mockito.verify(this.mockCurriculumEventListener).listenForCandidateCreatedEvent(event);
		
	}
	
	/**
	* Tests sending of event to send daily summary alerts to a
	* recruiters whose Candidate Search Alert(s) have generated Matches
	* @throws Exception
	*/
	@Test
	public void testPublishRequestSendAlertDailySummaryEmailCommand() throws Exception{
		
		final String 							recruiterId 	= "recruiter21";
		final Set<CandidateSearchAlertMatch> 	matches 		= Set.of(CandidateSearchAlertMatch.builder().alertId(UUID.randomUUID()).build()
																		,CandidateSearchAlertMatch.builder().alertId(UUID.randomUUID()).build());
		
		RequestSendAlertDailySummaryEmailCommand command = new RequestSendAlertDailySummaryEmailCommand(recruiterId, matches);
		
		publisher.publishRequestSendAlertDailySummaryEmailCommand(command);
		
		Mockito.verify(this.mockEmailServiceListener).listenForSendEmailCommand(Mockito.any(RequestSendEmailCommand.class));
		
	}
	
}