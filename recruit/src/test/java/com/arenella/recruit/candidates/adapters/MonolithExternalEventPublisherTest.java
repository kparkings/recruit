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
import com.arenella.recruit.adapters.events.CandidateUpdateEvent;
import com.arenella.recruit.adapters.events.ContactRequestEvent;
import com.arenella.recruit.candidates.beans.CandidateSearchAlertMatch;
import com.arenella.recruit.curriculum.adapters.CurriculumExternalEventListener;
import com.arenella.recruit.emailservice.adapters.MessagingEmailServiceExternalEventListener;
import com.arenella.recruit.emailservice.adapters.RequestSendEmailCommand;
import com.arenella.recruit.newsfeed.adapters.NewsFeedExternalEventListener;

/**
* Unit tests for the MonolithExternalEventPublisher class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
class MonolithExternalEventPublisherTest {
	
	@Mock
	private CurriculumExternalEventListener 	mockCurriculumEventListener;

	@Mock
	private MessagingEmailServiceExternalEventListener 	mockEmailServiceListener;
	
	@Mock
	private NewsFeedExternalEventListener		mockNewsFeedExternalEventListener;
	
	@InjectMocks
	private MonolithExternalEventPublisher 		publisher;
	
	/**
	* Tests publishing of Event representing searched skills 
	* to external services
	* @throws Exception
	*/
	@Test
	void testPublishSearchedSkillsEvent() {
		
		final String skill1 = "aSkill1";
		final String skill2 = "aSkill2";
		
		List<GrantedAuthority> roles = List.of("ROLE_RECRUITER").stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
		
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
	void testPublishSearchedSkillsEvent_admin() {
		
		final String skill1 = "aSkill1";
		final String skill2 = "aSkill2";
		
		List<GrantedAuthority> roles = List.of("ROLE_ADMIN").stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
		
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
	void testPublishCandidateNoLongerAvailableEvent() {
		
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
	void testPublishCandidateCreatedEvent() {
		
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
	void testPublishRequestSendAlertDailySummaryEmailCommand() {
		
		final String 							recruiterId 	= "recruiter21";
		final Set<CandidateSearchAlertMatch> 	matches 		= Set.of(CandidateSearchAlertMatch.builder().alertId(UUID.randomUUID()).build()
																		,CandidateSearchAlertMatch.builder().alertId(UUID.randomUUID()).build());
		
		RequestSendAlertDailySummaryEmailCommand command = new RequestSendAlertDailySummaryEmailCommand(recruiterId, matches);
		
		publisher.publishRequestSendAlertDailySummaryEmailCommand(command);
		
		Mockito.verify(this.mockEmailServiceListener).listenForSendEmailCommand(Mockito.any(RequestSendEmailCommand.class));
		
	}
	
	/**
	* Tests both internal and external email sent
	* @throws Exception
	*/
	@Test
	void testPublishContactRequestEvent() {
		
		final String senderRecruiterId 	= "rec33"; 
		final String recipientId		= "123"; 
		final String title				= "aTitle"; 
		final String message			= "aMessage";
		
		ContactRequestEvent event = new ContactRequestEvent(senderRecruiterId, recipientId, title, message);
		
		this.publisher.publishContactRequestEvent(event);
		
		Mockito.verify(this.mockEmailServiceListener, Mockito.times(2)).listenForSendEmailCommand(Mockito.any());
		
	}
	
	/**
	* Tests publishing of Event
	* @throws Exception
	*/
	@Test
	void testPublishCandidateUpdateEvent() {
		
		CandidateUpdateEvent candidateUpdateEvent = CandidateUpdateEvent.builder().build();
		
		this.publisher.publishCandidateUpdateEvent(candidateUpdateEvent);
	
		Mockito.verify(this.mockNewsFeedExternalEventListener).listenForEventCandidateUpdate(candidateUpdateEvent);
		
	}
	
}