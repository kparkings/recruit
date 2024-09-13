package com.arenella.recruit.candidates.utils;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.arenella.recruit.adapters.actions.RequestSkillsForCurriculumCommand;
import com.arenella.recruit.candidates.adapters.ExternalEventPublisher;
import com.arenella.recruit.candidates.beans.CandidateFilterOptions;
import com.arenella.recruit.candidates.controllers.CandidateSuggestionAPIOutbound;
import com.arenella.recruit.candidates.enums.RESULT_ORDER;
import com.arenella.recruit.candidates.repos.CandidateRepository;
import com.arenella.recruit.curriculum.adapters.CurriculumExternalEventListener;
import com.arenella.recruit.emailservice.adapters.RequestSendEmailCommand;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient;
import com.arenella.recruit.emailservice.beans.Email.EmailTopic;
import com.arenella.recruit.emailservice.beans.Email.EmailType;
import com.arenella.recruit.emailservice.beans.Email.Sender;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient.ContactType;
import com.arenella.recruit.emailservice.beans.Email.Sender.SenderType;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import java.util.Set;
import java.util.UUID;
import java.util.HashSet;
import java.util.Map;

/**
* Scheduler to periodically perform tasks to update the a Candidates account. 
* 
* Tasks include
* 
* 1. Update Skill's against current known Skill's in the system
* 2. Send email to ask for availability confirmation <NOT YET IMPLEMENTED>
* @author K Parkings
*/
@Component
@EnableScheduling
public class CandidateAccountRefreshUtil {

	//TODO: [KP] Setting this out for the time being because I want the new candidates to already start getting their lastAccountRefresh values assigned
	
	@Autowired
	private CandidateRepository 				candidateRepository;
	
	@Autowired
	private ElasticsearchClient 				esClient;
	
	@Autowired
	private CurriculumExternalEventListener 	eventPublisher;
	
	@Autowired
	private CandidateSearchUtil					candidateSearchUtil;
	
	@Autowired
	private ExternalEventPublisher				externalEventPublisher;
	
	/**
	* Triggers refresh actions for Candidates that are outdated
	*/
	@Scheduled(cron = "0 */10 * ? * *")
	public void performRefreshOnOutdatedAccounts() {
		try {
			this.runSkillUpdateRefresh();	
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		try {
			this.runEmailCandidateSummary();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	* Task to periodically update a Candidate's skills
	*/
	private void runSkillUpdateRefresh() {
		
		//TODO: 1 - Admin available screen to change from 2 weeks to 2 weeks and 3 days to give candidates chance to react to email
		
		CandidateFilterOptions filters = CandidateFilterOptions.builder().lastAccountRefreshLtEq(LocalDate.now().minusWeeks(2)).build();
		
		try {
			candidateRepository.findCandidates(filters, esClient, 10).forEach(candidate -> {
				candidate.setLastAccountRefresh(LocalDate.now().plusDays(1));
				this.candidateRepository.saveCandidate(candidate);
				eventPublisher.listenForRequestSkillsForCurriculumCommand(new RequestSkillsForCurriculumCommand(Long.valueOf(candidate.getCandidateId())));
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	* Sends a periodic summary email to candidates asking them to confirm their availability 
	* and showing statistics relating to their profile
	*/
	private void runEmailCandidateSummary() {
//need to check both date fields to only send if email wasn't sent and not manually updated by admin
//If updated bt admin also update email sent date?		
		final CandidateFilterOptions filterOptionsMarkedAsAvailable = CandidateFilterOptions.builder()
				.daysSincelastAvailabilityCheckEmailSent(14)
				.available(true)
				.order(RESULT_ORDER.desc)
				.orderAttribute("candidateId")
				.build();
		
		final CandidateFilterOptions filterOptionsMarkedAsUnAvailable = CandidateFilterOptions.builder()
				.daysSincelastAvailabilityCheckEmailSent(30)
				.available(false)
				.order(RESULT_ORDER.desc)
				.orderAttribute("candidateId")
				.build();
		
		final Pageable pageable = PageRequest.of(0, 20);
		
		Set<CandidateSuggestionAPIOutbound> candidates = new HashSet<>();
		
		try {
			
			candidates.addAll(this.candidateSearchUtil.searchAndPackageForAPIOutput(false, false, false, filterOptionsMarkedAsAvailable, pageable, false, true)
				.getContent()
				.stream()
				.map(c -> ((CandidateSuggestionAPIOutbound) c)).toList());
			
			candidates.addAll(this.candidateSearchUtil.searchAndPackageForAPIOutput(false, false, false, filterOptionsMarkedAsUnAvailable, pageable, false, true)
					.getContent()
					.stream()
					.map(c -> ((CandidateSuggestionAPIOutbound) c)).toList());
				
			candidates.stream().forEach(candidate -> {
				
				System.out.println("SEND MAIL FOR C#"+candidate.getCandidateId() + " available: " + candidate.isAvailable());
			
				UUID summaryEmailId = UUID.randomUUID();
				
				RequestSendEmailCommand command = RequestSendEmailCommand
						.builder()
							.emailType(EmailType.EXTERN)
							.recipients(Set.of(new EmailRecipient<UUID>(UUID.randomUUID(),candidate.getCandidateId(), ContactType.CANDIDATE)))
							.sender(new Sender<>(UUID.randomUUID(), "", SenderType.SYSTEM, "kparkings@gmail.com"))
							.title("Arenella-ICT - Your Availability")
							.topic(EmailTopic.CANDIDATE_SUMMARY)
							.model(Map.of("userId", candidate.getCandidateId(), "firstname",candidate.getFirstname(),"available",candidate.isAvailable(), "summaryEmailId", summaryEmailId))
							.persistable(false)
						.build();
				
				this.externalEventPublisher.publishSendEmailCommand(command);
				
				//Config Email
				//Register UUID for email ( check both incoming UUID and candidate Id  + date. Only valid x days). // No security for engpoint
				//Need endpoint which will update candidate status,last availability check and remove UUID from new table
				//Make availbility in admin console 2 weeks/1 Months --> + 3 days to give candidates time to respond to email
			});
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
