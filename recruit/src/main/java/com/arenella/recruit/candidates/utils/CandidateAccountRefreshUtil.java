package com.arenella.recruit.candidates.utils;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.arenella.recruit.adapters.actions.RequestSkillsForCurriculumCommand;
import com.arenella.recruit.candidates.adapters.ExternalEventPublisher;
import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.CandidateFilterOptions;
import com.arenella.recruit.candidates.beans.City;
import com.arenella.recruit.candidates.controllers.CandidateSuggestionAPIOutbound;

import com.arenella.recruit.candidates.enums.RESULT_ORDER;
import com.arenella.recruit.candidates.repos.CandidateRepository;
import com.arenella.recruit.candidates.services.CityService;
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
import java.util.Optional;

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
	
	@Value("${arenella.ict.candidate.email.availability}")
	public String candidateAvailabilityEmailEnabled = "true";
	
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
	
	@Autowired
	private CityService							cityService;
	
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
			if (candidateAvailabilityEmailEnabled.equals("true")) {
				this.runEmailCandidateSummary();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	
	}
	
	/**
	* Task to periodically update a Candidate's skills
	*/
	private void runSkillUpdateRefresh() {
		
		CandidateFilterOptions filters = CandidateFilterOptions.builder().lastAccountRefreshLtEq(LocalDate.now().minusWeeks(2)).build();
		
		try {
			candidateRepository.findCandidates(filters, esClient, 0, 10).forEach(candidate -> {
				
				//Hijacking the skills update to also update the geopos info
				City city = cityService.findCityById(candidate.getCountry(), candidate.getCity()).orElse(City.builder().build());
				candidate.setLatitude(city.getLat());
				candidate.setLongitude(city.getLon());
				
				candidate.setLastAccountRefresh(LocalDate.now().plusDays(1));
				
				if (Optional.ofNullable(city.getCountry()).isEmpty()) {
					cityService.performNewCityCheck(candidate.getCountry(), candidate.getCity());
				}
				
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

		final CandidateFilterOptions filterOptionsMarkedAsAvailable = CandidateFilterOptions.builder()
				.daysSincelastAvailabilityCheckEmailSent(14)
				.available(true)
				.order(RESULT_ORDER.desc)
				.orderAttribute("candidateId")
				.maxResults(20)
				.build();
		
		final CandidateFilterOptions filterOptionsMarkedAsUnAvailable = CandidateFilterOptions.builder()
				.daysSincelastAvailabilityCheckEmailSent(30)
				.available(false)
				.order(RESULT_ORDER.desc)
				.orderAttribute("candidateId")
				.maxResults(20)
				.build();
		
		Set<CandidateSuggestionAPIOutbound> candidates = new HashSet<>();
		
		try {
			
			candidates.addAll(this.candidateSearchUtil.searchAndPackageForAPIOutput(false, false, false, filterOptionsMarkedAsAvailable, false, true)
				.getContent()
				.stream()
				.map(c -> ((CandidateSuggestionAPIOutbound) c)).toList());
			
			candidates.addAll(this.candidateSearchUtil.searchAndPackageForAPIOutput(false, false, false, filterOptionsMarkedAsUnAvailable, false, true)
					.getContent()
					.stream()
					.map(c -> ((CandidateSuggestionAPIOutbound) c)).toList());
		
			candidates.stream().forEach(candidate -> {
				
				Optional<Candidate> candidateOpt = this.candidateRepository.findCandidateById(Long.valueOf(candidate.getCandidateId()));
				
				if (candidateOpt.isPresent()) {
				
					UUID summaryEmailIdToken = UUID.randomUUID();
				
					Candidate can = candidateOpt.get();
					
					can.setLastAvailabilityCheckEmailSent(LocalDate.now().plusDays(1));
					can.setLastAvailabilityCheckIdSent(summaryEmailIdToken);
					
					this.candidateRepository.saveCandidate(can);
					
					RequestSendEmailCommand command = RequestSendEmailCommand
							.builder()
								.emailType(EmailType.EXTERN)
								.recipients(Set.of(new EmailRecipient<UUID>(UUID.randomUUID(),candidate.getCandidateId(), ContactType.CANDIDATE)))
								.sender(new Sender<>(UUID.randomUUID(), "", SenderType.SYSTEM, "no-reply@arenella-ict.com"))
								.title("Arenella-ICT - Your Availability")
								.topic(EmailTopic.CANDIDATE_SUMMARY)
								.model(Map.of("candidateId", candidate.getCandidateId(), "firstname",candidate.getFirstname(),"available",candidate.isAvailable(), "summaryEmailIdToken", summaryEmailIdToken))
								.persistable(false)
							.build();
					
					this.externalEventPublisher.publishSendEmailCommand(command);
				}
				
			});
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
