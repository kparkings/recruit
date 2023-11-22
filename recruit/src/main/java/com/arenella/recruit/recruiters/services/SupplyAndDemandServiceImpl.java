package com.arenella.recruit.recruiters.services;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.arenella.recruit.adapters.actions.GrantCreditCommand;
import com.arenella.recruit.adapters.events.OpenPositionContactRequestEvent;
import com.arenella.recruit.emailservice.adapters.RequestSendEmailCommand;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient;
import com.arenella.recruit.emailservice.beans.Email.EmailTopic;
import com.arenella.recruit.emailservice.beans.Email.EmailType;
import com.arenella.recruit.emailservice.beans.Email.Sender;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient.ContactType;
import com.arenella.recruit.emailservice.beans.Email.Sender.SenderType;
import com.arenella.recruit.recruiters.beans.RecruiterCredit;
import com.arenella.recruit.recruiters.adapters.RecruitersExternalEventPublisher;
import com.arenella.recruit.recruiters.beans.OfferedCandidateAPIOutbound.RecruiterDetails;
import com.arenella.recruit.recruiters.beans.OpenPosition;
import com.arenella.recruit.recruiters.beans.SupplyAndDemandEvent;
import com.arenella.recruit.recruiters.beans.SupplyAndDemandEvent.EventType;
import com.arenella.recruit.recruiters.dao.OpenPositionDao;
import com.arenella.recruit.recruiters.dao.RecruiterCreditDao;
import com.arenella.recruit.recruiters.dao.RecruiterDao;
import com.arenella.recruit.recruiters.dao.SupplyAndDemandEventDao;

/**
* Services for Supply and Demand
* @author K Parkings
*/
@Service
public class SupplyAndDemandServiceImpl implements SupplyAndDemandService{

	@Autowired
	private OpenPositionDao 					openPositionDao;
	
	@Autowired
	private RecruiterDao						recruiterDao;
	
	@Autowired
	private SupplyAndDemandEventDao				supplyAndDemandEventDao;
	
	@Autowired
	private RecruitersExternalEventPublisher	eventPublisher;
	
	@Autowired
	private RecruiterCreditDao					creditDao;
	
	/**
	* Refer to the SupplyAndDemandService interface for details 
	*/
	@Override
	public void addOpenPosition(OpenPosition openPosition) {
		
		openPosition.initializeAsNewObject(getAuthenticatedRecruiterId());
		
		openPositionDao.persistOpenPositiion(openPosition);
		
	}

	/**
	* Refer to the SupplyAndDemandService interface for details 
	*/
	@Override
	public void deleteOpenPosition(UUID openPositionId) throws IllegalAccessException{
		
		validateAuthenticationForUserForOpenPosition(openPositionId);
		
		openPositionDao.deleteById(openPositionId);
		
	}
	
	/**
	* Refer to the SupplyAndDemandService interface for details 
	*/
	@Override
	public void updateOpenPosition(UUID openPositionId, OpenPosition openPosition) throws IllegalAccessException {
		
		validateAuthenticationForUserForOpenPosition(openPositionId);
	
		openPositionDao.updateExistingOpenPosition(openPositionId, openPosition);
		
	}
	
	/**
	* Refer to the SupplyAndDemandService interface for details 
	*/
	@Override
	public Set<UUID> fetchViewedEventsByRecruiter(EventType type, String recruiterId){
		return this.supplyAndDemandEventDao.fetchEventsForRecruiter(type, recruiterId);
	}
	
	/**
	* Refer to the SupplyAndDemandService interface for details 
	* @return
	*/
	@Override
	public RecruiterDetails fetchRecruiterDetails(String recruiterId) {
		return recruiterDao.findRecruiterDetailsById(recruiterId);
	}

	/**
	* Refer to the SupplyAndDemandService interface for details 
	* @return
	*/
	@Override
	public Set<OpenPosition> fetchOpenPositions() {
		return openPositionDao.findAllOpenPositions();
	}

	/**
	* Refer to the SupplyAndDemandService interface for details 
	* @return
	*/
	@Override
	public Set<OpenPosition> fetchOpenPositions(String recruiterId) {
		return openPositionDao.findAllOpenPositionsByRecruiterId(recruiterId);
	}
	
	/**
	* Logs an event stating that an open position was viewed
	* @param id - Unique id of the Open Position viewed
	*/
	@Override
	public void registerOpenPositionViewedEvent(UUID id) {
	
		if (this.isOwnerOfOpenPosition(id)) {
			return;
		}
		
		if (isAdmin()) {
			return;
		}
		
		this.supplyAndDemandEventDao
			.persistEvent(SupplyAndDemandEvent
					.builder()
						.created(LocalDateTime.now())
						.eventId(id)
						.recruiterId(getAuthenticatedRecruiterId())
						.type(EventType.OPEN_POSITION)
					.build());
		
	}
	
	/**
	* Returns whether or not the authenticated user is an Admin user
	* @return is Admin or not Admin
	*/
	private boolean isAdmin() {

		UsernamePasswordAuthenticationToken user = (UsernamePasswordAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
		
		return user.getAuthorities().stream().filter(a -> a.getAuthority().equals("ROLE_ADMIN")).findAny().isPresent();		
	
	}
	
	/**
	* Logs an event stating that an offered candidate was viewed
	* @param id - Unique id of the Offered Candidate viewed
	*/
	@Override
	public void registerOfferedCandidateViewedEvent(UUID id) {
	
		//TODO: [KP] Now just standard candidate. If we show stats need to re-implement this
		//if (this.isOwnerOfOfferedCandidate(id)) {
		//	return;
		//}
		
		if (isAdmin()) {
			return;
		}
		
		this.supplyAndDemandEventDao
			.persistEvent(SupplyAndDemandEvent
				.builder()
					.created(LocalDateTime.now())
					.eventId(id)
					.recruiterId(getAuthenticatedRecruiterId())
					.type(EventType.OFFERED_CANDIDATE)
				.build());
	
	}
	
	/**
	* Refer to the SupplyAndDemandService interface for details 
	*/
	@Override
	public Set<SupplyAndDemandEvent> fetchOpenPositionViewStats() {
		return this.supplyAndDemandEventDao.fetchThisWeeksEvents(LocalDateTime.now().with(DayOfWeek.MONDAY).withHour(0).withMinute(0).withSecond(0).withNano(0), EventType.OPEN_POSITION);		
	}

	/**
	* Refer to the SupplyAndDemandService interface for details 
	*/
	@Override
	public Set<SupplyAndDemandEvent> fetchOfferedCandidateViewStats() {
		return this.supplyAndDemandEventDao.fetchThisWeeksEvents(LocalDateTime.now().with(DayOfWeek.MONDAY).withHour(0).withMinute(0).withSecond(0).withNano(0), EventType.OFFERED_CANDIDATE);
	}
	
	/**
	* Refer to the SupplyAndDemandService interface for details 
	*/
	@Override
	public void sendOpenPositionContactEmail(UUID openPositionId, String message, String authenticatedUserId) {
		
		if (!this.openPositionDao.existsById(openPositionId)) {
			throw new RuntimeException("Unknown Open Position");
		}
		
		OpenPosition openPosition = this.openPositionDao.findByOpenPositionId(openPositionId);
		
		this.eventPublisher
			.publishOpenPositionContactRequestEvent(OpenPositionContactRequestEvent
					.builder()
						.message(message)
						.recipientId(openPosition.getRecruiterId())
						.senderId(authenticatedUserId)
						.openPositionId(openPositionId)
						.openPositionTitle(openPosition.getPositionTitle())
					.build());
		
	}
	
	/**
	* Refer to the SupplyAndDemandService interface for details 
	*/
	@Override
	public void disableSupplyAndDemandPostsForRecruiter(String recruiterId) {
		
		Set<OpenPosition> openPositions = this.openPositionDao.findAllOpenPositionsByRecruiterId(recruiterId).stream().map(op -> {
			op.setActive(false);
			return op;
		}).collect(Collectors.toSet());
		
		this.openPositionDao.persistOpenPositions(openPositions);
		
	}

	/**
	* Refer to the SupplyAndDemandService interface for details 
	*/
	@Override
	public void enableSupplyAndDemandPostsForRecruiter(String recruiterId) {

		Set<OpenPosition> openPositions = this.openPositionDao.findAllOpenPositionsByRecruiterId(recruiterId).stream().map(op -> {
			op.setActive(true);
			return op;
		}).collect(Collectors.toSet());
		
		this.openPositionDao.persistOpenPositions(openPositions);
		
	}
	
	/**
	* Returns whether the OpenPosition is owned by the current logged in User
	* @param openPositionId - Unique id of the Open Position
	* @return Whether the current user is the owner
	*/
	private boolean isOwnerOfOpenPosition(UUID openPositionId) {
		
		OpenPosition openPosition = openPositionDao.findByOpenPositionId(openPositionId);
		
		if (!getAuthenticatedRecruiterId().equals(openPosition.getRecruiterId())) {
			return false;
		}
		
		return true;
	}
	
	/**
	* Performs authentication validation to ensure a User can only update their 
	* own OpenPositions
	* @param openPositionId - OpenPoistion the user wants to update
	* @return If user has rights to the OpenPosition the existing OpenPosition
	* @throws IllegalAccessException
	*/
	private OpenPosition validateAuthenticationForUserForOpenPosition(UUID openPositionId) throws IllegalAccessException{
		
		OpenPosition openPosition = openPositionDao.findByOpenPositionId(openPositionId);
		
		if (!getAuthenticatedRecruiterId().equals(openPosition.getRecruiterId())) {
			throw new IllegalAccessException();
		}
		
		return openPosition;
		
	}
	
	/**
	* Retrieves the Id of the current Recruiter
	* @return id from security context
	*/
	private String getAuthenticatedRecruiterId() {
		return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
	}

	/**
	* Refer to the SupplyAndDemandService for details 
	*/
	@Override
	public void updateCredits(GrantCreditCommand command) {
		
		Set<RecruiterCredit> credits = this.creditDao.fetchRecruiterCredits();
		
		credits.stream().forEach(credit -> {
				
			credit.setCredits(RecruiterCredit.DEFAULT_CREDITS);
			
			Map<String,Object> modelExt = Map.of("CREDITS_CURRICULUM",8,"CREDITS_LISTINGS",2,"CREDITS_MARKETPLACE",2);
			
			RequestSendEmailCommand emailCommand = 
					RequestSendEmailCommand
					.builder()
						.emailType(EmailType.EXTERN)
						.model(modelExt)
						.persistable(false)
						.recipients(Set.of(new EmailRecipient<UUID>(UUID.randomUUID(), credit.getRecruiterId(), ContactType.RECRUITER)))
						.sender(new Sender<>(UUID.randomUUID(), "", SenderType.SYSTEM, "kparkings@gmail.com"))
						.title("Arenella-ICT - New Credits Available")
						.topic(EmailTopic.NEW_CREDITS_ASSIGNED)
					.build();
			
				this.eventPublisher.publishSendEmailCommand(emailCommand);
				
		});
		
		creditDao.saveAll(credits);
		
	}

	/**
	* Refer to the SupplyAndDemandService for details 
	*/
	@Override
	public Boolean doCreditsCheck(String recruiterId) {
		
		Optional<RecruiterCredit> recruiterCreditOpt = this.creditDao.getByRecruiterId(recruiterId);
		
		if (recruiterCreditOpt.isEmpty()) {
			return false;
		}
		
		return recruiterCreditOpt.get().getCredits() > 0;
		
	}

	/**
	* Refer to the SupplyAndDemandService for details 
	*/
	@Override
	public void useCredit(String userId) {
		
		RecruiterCredit credit = this.creditDao.getByRecruiterId(userId).orElseThrow(() -> new IllegalArgumentException("Unknown User - Cant use Credit"));
		
		if (credit.getCredits() == 0) {
			throw new IllegalStateException("No credits available for User");
		}
		
		credit.decrementCredits();
		
		this.creditDao.persist(credit);
		
	}
	
	/**
	* Refer to the SupplyAndDemandService for details 
	*/
	@Override
	public void updateCreditsForUser(String userId, int availableCredits) {
		
		Optional<RecruiterCredit> creditOpt = this.creditDao.getByRecruiterId(userId);
		
		if (!creditOpt.isPresent()) {
			return;
		}
		
		RecruiterCredit credits = creditOpt.get();
		
		credits.setCredits(availableCredits);
		
		creditDao.persist(credits);
		
	}
	
	/**
	* Refer to the CandidateService for details 
	*/
	@Override
	public int getCreditCountForUser(String userId) {
		
		Optional<RecruiterCredit> credits =  this.creditDao.getByRecruiterId(userId);
			
		if(credits.isEmpty()) {
			throw new IllegalArgumentException("Unknown User: " + userId);	
		}
		
		return credits.get().getCredits();
		
	}
	
	/**
	* Refer to the CandidateService for details 
	*/
	@Override
	public void addCreditsRecordForUser(String userId) {
		
		Optional<RecruiterCredit> creditOpt = this.creditDao.getByRecruiterId(userId);
		
		if (creditOpt.isPresent()) {
			throw new IllegalStateException("Candidate Credits already exist for user " + userId);
		}
		
		creditDao.persist(RecruiterCredit.builder().recruiterId(userId).credits(RecruiterCredit.DISABLED_CREDITS).build());
		
	}
	
}