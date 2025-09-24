package com.arenella.recruit.authentication.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
* Statistics relating to User login activity
* over a period of 90 days 
*/
public class UserLoginStats {

	private long 					activityHigh;
	private long 					activityMedium;
	private long 					activityLow;
	private long 					activityNone;
	private List<UserLoginSummary> 	loginSummaries 	= new ArrayList<>();
	
	/**
	* Constructor based upon a builder
	* @param builder - Contains initialization values
	*/
	public UserLoginStats(UserLoginStatsBuilder builder) {
		this.loginSummaries.addAll(builder.loginSummaries);
		this.calculateActivity(loginSummaries);
		this.loginSummaries = this.loginSummaries.stream().sorted((a,b) -> String.valueOf(b.getActivityLevel().levelCode).compareTo(String.valueOf(a.getActivityLevel().levelCode))).collect(Collectors.toList());
	}
	
	/**
	* Returns number of Users who have been 
	* highly active on the site in the past
	* 90 days
	* @return high activity total
	*/
	public long getActivityHigh() {
		return this.activityHigh;
	}
	
	/**
	* Returns number of Users who have been 
	* medium active on the site in the past
	* 90 days
	* @return high activity total
	*/
	public long getActivityMedium() {
		return this.activityMedium;
	}
	
	/**
	* Returns number of Users who have been 
	* low active on the site in the past
	* 90 days
	* @return high activity total
	*/
	public long getActivityLow() {
		return this.activityLow;
	}
	
	/**
	* Returns number of Users who have not 
	* been active on the site in the past
	* 90 days
	* @return high activity total
	*/
	public long getActivityNone() {
		return this.activityNone;
	}
	
	/**
	* Returns individual User login activity data
	*/
	public List<UserLoginSummary> getLoginSummaries(){
		return this.loginSummaries;
	} 
	
	/**
	* Calculates totals
	* @param loginSummaries
	*/
	private void calculateActivity(List<UserLoginSummary> loginSummaries) {
		
		loginSummaries.stream().forEach(recStat -> {
			
			switch(recStat.getActivityLevel()) {
				case UserLoginSummary.ActivityLevel.HIGH 	-> this.activityHigh 	= this.activityHigh+1;
				case UserLoginSummary.ActivityLevel.MEDIUM 	-> this.activityMedium 	= this.activityMedium+1;
				case UserLoginSummary.ActivityLevel.LOW 	-> this.activityLow 	= this.activityLow+1;
				default 									-> this.activityNone 	= this.activityNone+1;
			}
			
		});
		
	}
	
	/**
	* Returns an instance of the Builder
	* @return Builder for the class
	*/
	public static UserLoginStatsBuilder builder() {
		return new UserLoginStatsBuilder();
	}
	
	/**
	* Builder for the class 
	*/
	public static class UserLoginStatsBuilder{
		
		private Set<UserLoginSummary> 	loginSummaries = new HashSet<>();
		
		/**
		* Sets the Summaries of the individual Users logins
		* @param loginSummaries - Login activity data
		* @return Builder
		*/
		public UserLoginStatsBuilder loginSummaries(Set<UserLoginSummary> loginSummaries) {
			this.loginSummaries.addAll(loginSummaries);
			return this;
		}
		
		/**
		* Returns an initialized instance of UserLoginStats
		* @return Initialized instance
		*/
		public UserLoginStats build() {
			return new UserLoginStats(this);
		}
	}
	
}