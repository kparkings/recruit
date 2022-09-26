package com.arenella.recruit.recruiters.services;

import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.arenella.recruit.recruiters.beans.OfferedCandidate;
import com.arenella.recruit.recruiters.beans.OfferedCandidateAPIOutbound.RecruiterDetails;
import com.arenella.recruit.recruiters.beans.OpenPosition;
import com.arenella.recruit.recruiters.dao.OfferedCandidateDao;
import com.arenella.recruit.recruiters.dao.OpenPositionDao;
import com.arenella.recruit.recruiters.dao.RecruiterDao;

/**
* Services for Supply and Demand
* @author K Parkings
*/
@Service
public class SupplyAndDemandServiceImpl implements SupplyAndDemandService{

	@Autowired
	private OpenPositionDao 	openPositionDao;
	
	@Autowired
	private OfferedCandidateDao offeredCandidateDao;
	
	@Autowired
	private RecruiterDao		recruiterDao;
	
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

}