package com.arenella.recruit.authentication.controllers;

/**
* Summary of a Users login behavior
*/
public class UserLoginSummary{
	
	public enum ActivityLevel {
		HIGH(4), 
		MEDIUM(3), 
		LOW(2), 
		NONE(1);
		
		public int levelCode;
		
		ActivityLevel(int levelCode) {
			this.levelCode = levelCode;
		}
		
		public int getLevelCode() {
			return levelCode;
		}
		
	}
	
	private String 			userId;
	private ActivityLevel 	activityLevel;
	private long 			loginsThisWeeek;
	private long 			loginsLast30Days;
	private long 			loginsLast60Days;
	private long 			loginsLast90Days;
	
	/**
	* Constructor
	* @param userId				- Unique Id of the USer
	* @param loginsThisWeeek	- Number of logins this week
	* @param loginsLast30Days	- Number of logins last 30 days
	* @param loginsLast60Days	- Number of logins last 60 days
	* @param loginsLast90Day	- Number of logins last 90 days
	*/
	public UserLoginSummary(
			String userId,
			long loginsThisWeeek,
			long loginsLast30Days,
			long loginsLast60Days,
			long loginsLast90Days) {
		
		this.userId 			= userId;
		this.loginsThisWeeek 	= loginsThisWeeek;
		this.loginsLast30Days 	= loginsLast30Days;
		this.loginsLast60Days 	= loginsLast60Days;
		this.loginsLast90Days 	= loginsLast90Days;
		this.calculateActivity();
	}
	
	/**
	* Returns the unique Id of the User
	* @return - Id
	*/
	public String getUserId() {
		return this.userId;
	}
	
	/**
	* Returns how active the user has been on the site
	* @return
	*/
	public ActivityLevel getActivityLevel() {
		return this.activityLevel;
	}
	
	/**
	* Returns number of logins in last week
	* @return number of logins
	*/
	public long getLoginsThisWeeek() {
		return this.loginsThisWeeek;
	}
	
	/**
	* Returns number of logins in last 30 days
	* @return number of logins
	*/
	public long getLoginsLast30Days() {
		return this.loginsLast30Days;
	}
	
	/**
	* Returns number of logins in last 60 days
	* @return number of logins
	*/
	public long getLoginsLast60Days() {
		return this.loginsLast60Days;
	}
	
	/**
	* Returns number of logins in last 90 days
	* @return number of logins
	*/
	public long getLoginsLast90Days() {
		return this.loginsLast90Days;
	}
	
	/**
	* Calculates the Users login activity over
	* a period
	*/
	private void calculateActivity() {
			
		int score = 0;
		
		if (this.loginsThisWeeek > 0) {
			score = score+1;
		}
			
		if (this.loginsLast30Days > 0) {
			score = score+1;
		}
		
		if (this.loginsLast60Days > 0) {
			score = score+1;
		}
			
		if (this.loginsLast90Days > 0) {
			score = score+1;
		}
			
		switch(score) {
			case 4 -> {
				this.activityLevel = ActivityLevel.HIGH;
				break;
			}
			case  2, 3 -> {
				this.activityLevel = ActivityLevel.MEDIUM;
				break;
			}
			case 1 -> {
				this.activityLevel = ActivityLevel.LOW;
				break;
			}
			default -> {
				this.activityLevel = ActivityLevel.NONE;
			}
		}
				
	}
		
}