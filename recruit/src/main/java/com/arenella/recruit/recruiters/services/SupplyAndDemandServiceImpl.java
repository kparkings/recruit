package com.arenella.recruit.recruiters.services;

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
		
		String recruiterId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		
		openPosition.initializeAsNewObject(recruiterId);
		
		dao.persistOpenPositiion(openPosition);
		
		
	}

	
	
}
