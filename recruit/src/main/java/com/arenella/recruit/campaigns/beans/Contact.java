package com.arenella.recruit.campaigns.beans;

/**
* Class represents a User that can participate  in a Campaign
* @param id 					- UserId of the Contact
* @param firstname 				- Contacts firstName
* @param surname 				- Contacts surname
* @param email 					- Email address of the Contact
* @param creditSubscription 	- If the Contact is a Credit subscription User
*/
public record Contact (String id, String firstName, String surname, String email, SubscriptionType creditSubscription){

	public enum SubscriptionType {CREDIT, PAID}
}
