package com.arenella.recruit.recruiters.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.arenella.recruit.recruiters.beans.OpenPosition;
import com.arenella.recruit.recruiters.dao.SupplyAndDemanDao;

/**
* Services for Supply and Demand
* @author K Parkings
*/
@Service
public class SupplyAndDemandServiceImpl implements SupplyAndDemandService{

	@Autowired
	private SupplyAndDemanDao dao;
	
	/**
	* Refer to the SupplyAndDemandService interface for details 
	*/
	@Override
	public void addOpenPosition(OpenPosition openPosition) {
		
		openPosition.initializeAsNewObject(getAuthenticatedRecruiterId());
		
		dao.persistOpenPositiion(openPosition);
		
	}

	/**
	* Refer to the SupplyAndDemandService interface for details 
	*/
	@Override
	public void deleteOpenPosition(UUID openPositionId) throws IllegalAccessException{
		
		validateAuthenticationForUser(openPositionId);
		
		dao.deleteById(openPositionId);
		
	}
	
	/**
	* Refer to the SupplyAndDemandService interface for details 
	*/
	@Override
	public void updateOpenPosition(UUID openPositionId, OpenPosition openPosition) throws IllegalAccessException {
		
		validateAuthenticationForUser(openPositionId);
	
		dao.updateExistingOpenPosition(openPositionId, openPosition);
		
	}
	
	/**
	* Performs authentication validation to ensure a User can only update their 
	* own OpenPositions
	* @param openPositionId - OpenPoistion the user wants to update
	* @return If user has rights to the OpenPosition the existing OpenPosition
	* @throws IllegalAccessException
	*/
	private OpenPosition validateAuthenticationForUser(UUID openPositionId) throws IllegalAccessException{
		
		OpenPosition openPosition = dao.findByOpenPositionId(openPositionId);
		
		if (!getAuthenticatedRecruiterId().equals(openPosition.getRecruiterId())) {
			throw new IllegalAccessException();
		}
		
		return openPosition;
		
	}
	
	private String getAuthenticatedRecruiterId() {
		return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
	}
	
}