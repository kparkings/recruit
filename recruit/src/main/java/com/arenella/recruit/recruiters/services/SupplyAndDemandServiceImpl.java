package com.arenella.recruit.recruiters.services;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.arenella.recruit.recruiters.beans.OfferedCandidate;
import com.arenella.recruit.recruiters.beans.OfferedCandidateAPIOutbound.RecruiterDetails;
import com.arenella.recruit.recruiters.beans.OpenPosition;
import com.arenella.recruit.recruiters.beans.SupplyAndDemandEvent;
import com.arenella.recruit.recruiters.beans.SupplyAndDemandEvent.EventType;
import com.arenella.recruit.recruiters.dao.OfferedCandidateDao;
import com.arenella.recruit.recruiters.dao.OpenPositionDao;
import com.arenella.recruit.recruiters.dao.RecruiterDao;
import com.arenella.recruit.recruiters.dao.SupplyAndDemandEventDao;

/**
* Services for Supply and Demand
* @author K Parkings
*/
@Service
public class SupplyAndDemandServiceImpl implements SupplyAndDemandService{

	@Autowired
	private OpenPositionDao 			openPositionDao;
	
	@Autowired
	private OfferedCandidateDao 		offeredCandidateDao;
	
	@Autowired
	private RecruiterDao				recruiterDao;
	
	@Autowired
	private SupplyAndDemandEventDao		supplyAndDemandEventDao;
	
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
	public void addOfferedCandidate(OfferedCandidate offeredCandidate) {
		
		offeredCandidate.initializeAsNewObject(getAuthenticatedRecruiterId());
		
		offeredCandidateDao.persistOfferedCandidate(offeredCandidate);
		
	}
	
	/**
	* Refer to the SupplyAndDemandService interface for details 
	*/
	@Override
	public void updateOfferedCandidate(UUID offeredCandidateId, OfferedCandidate offeredCandidate) throws IllegalAccessException {
		
		validateAuthenticationForUserForOfferedCandidate(offeredCandidateId);
	
		offeredCandidateDao.updateExistingOfferedCandidate(offeredCandidateId, offeredCandidate);
		
	}
	
	/**
	* Refer to the SupplyAndDemandService interface for details 
	*/
	@Override
	public void deleteOfferedCandidate(UUID offeredCandidateId) throws IllegalAccessException{
		
		validateAuthenticationForUserForOfferedCandidate(offeredCandidateId);
		
		offeredCandidateDao.deleteById(offeredCandidateId);
		
	}

	/**
	* Refer to the SupplyAndDemandService interface for details 
	*/
	@Override
	public Set<OfferedCandidate> fetchOfferedCandidates() {
		return offeredCandidateDao.findAllOfferedCandidates();
	}
	
	/**
	* Refer to the SupplyAndDemandService interface for details 
	*/
	@Override
	public Set<OfferedCandidate> fetchOfferedCandidates(String recruiterId) {
		return offeredCandidateDao.findAllOfferedCandidatesByRecruiterId(recruiterId);
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
		
		if (this.isLoggedInUserAdmin()) {
			return;
		}
	
		if (this.isOwnerOfOpenPosition(id)) {
			return;
		}
		
		this.supplyAndDemandEventDao
			.persistEvent(SupplyAndDemandEvent
					.builder()
						.created(LocalDateTime.now())
						.eventId(UUID.randomUUID())
						.recruiterId(getAuthenticatedRecruiterId())
						.type(EventType.OPEN_POSITION)
					.build());
		
	}
	
	/**
	* Logs an event stating that an offered candidate was viewed
	* @param id - Unique id of the Offered Candidate viewed
	*/
	@Override
	public void registerOfferedCandidateViewedEvent(UUID id) {
	
		if (this.isLoggedInUserAdmin()) {
			return;
		}
	
		if (this.isOwnerOfOfferedCandidate(id)) {
			return;
		}
		
		this.supplyAndDemandEventDao
			.persistEvent(SupplyAndDemandEvent
				.builder()
					.created(LocalDateTime.now())
					.eventId(UUID.randomUUID())
					.recruiterId(getAuthenticatedRecruiterId())
					.type(EventType.OFFERED_CANDIDATE)
				.build());
	
	}
	
	/**
	* Refer to the SupplyAndDemandService interface for details 
	*/
	@Override
	public Set<SupplyAndDemandEvent> fetchOpenPositionViewStats() {
		return this.supplyAndDemandEventDao.fetchThisWeeksEvents(LocalDateTime.now().minusWeeks(1), EventType.OPEN_POSITION);		
	}

	/**
	* Refer to the SupplyAndDemandService interface for details 
	*/
	@Override
	public Set<SupplyAndDemandEvent> fetchOfferedCandidateViewStats() {
		return this.supplyAndDemandEventDao.fetchThisWeeksEvents(LocalDateTime.now().minusWeeks(1), EventType.OFFERED_CANDIDATE);
	}
	
	/**
	* Returns whether the OfferedCandidate is owned by the current logged in User
	* @param offeredCandidateId - Unique id of the Offered Candidate
	* @return Whether the current user is the owner
	*/
	private boolean isOwnerOfOfferedCandidate(UUID offeredCandidateId) {
		
		OfferedCandidate offeredCandidate = offeredCandidateDao.findByOfferedCandidateId(offeredCandidateId);
		
		if (!getAuthenticatedRecruiterId().equals(offeredCandidate.getRecruiterId())) {
			return false;
		}
		
		return true;
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
	private OfferedCandidate validateAuthenticationForUserForOfferedCandidate(UUID offeredCandidateId) throws IllegalAccessException{
		
		OfferedCandidate offeredCandidate = offeredCandidateDao.findByOfferedCandidateId(offeredCandidateId);
		
		if (!getAuthenticatedRecruiterId().equals(offeredCandidate.getRecruiterId())) {
			throw new IllegalAccessException();
		}
		
		return offeredCandidate;
		
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
	* Returns whether or not the currently authenticated User is an Admin User
	* @return Whether or not the currently authenticated User is an Admin User
	*/
	private boolean isLoggedInUserAdmin() {
		return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().filter(role -> role.getAuthority().equals("ROLE_ADMIN")).findAny().isPresent();
	}
	
}