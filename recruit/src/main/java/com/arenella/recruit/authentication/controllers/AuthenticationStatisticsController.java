package com.arenella.recruit.authentication.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
* Controller / REST API for Authentication Statistics
* @author K Parkings
*/
@RestController
public class AuthenticationStatisticsController {
	
	/**
	* Returns statisitc's of logins related to users
	* @return
	*/
	public ResponseEntity<Void> fetchLoginTrendStats() {
		
		// # per user
		//1. split by recruiter / candidate
		//2. Id of user
		//3. logins by day pas 3 months
		//3. logins by week past 6 months
		//4. logins per month past year
		
		//5. list of logins today (for drill down)
		//6. list of logins this week ( grouped by day to show on graph) 
		
		return ResponseEntity.ok().build();
	}
	
	/**
	* Returns statisics for an individual user relating to 
	* how often they log into the system
	* @return individual user stats
	*/
	public ResponseEntity<Void> fetchUsersStat(){
		
				//2. Id of user
				//3. logins by day past 30 days
				//3. logins by week past 90 months
				//4. logins per month 12 months
		return ResponseEntity.ok().build();
		
	}
	
}