package com.arenella.recruit.recruiters.services;

import org.springframework.stereotype.Service;

/**
* Services for working with Credits
* @author K Parkings
*/
@Service
public class CreditServiceImpl implements CreditService{

	/**
	* Refer to the CreditService interface for details 
	*/
	@Override
	public void grantMonthlyCreditsToRecruiters() {
		//1. Generate action grant credits to be recrived by other MS's
		//2. Update Credits in this service too
		//3, Send email to Recruiters to let them know they have new credits ( if not unlimited credits via firstgen or subscriptin)
		
	}

}
