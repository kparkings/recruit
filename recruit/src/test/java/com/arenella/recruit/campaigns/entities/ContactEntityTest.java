package com.arenella.recruit.campaigns.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.campaigns.beans.Contact;
import com.arenella.recruit.campaigns.beans.Contact.SubscriptionType;

/**
* Unit tests for the ContactEntity class 
*/
class ContactEntityTest {

	private String 				ID 					= "rec23";
	private String 				FIRSTNAME 			= "kevin";
	private String 				SURNAME 			= "parkings";
	private String 				EMAIL 				= "kparkings@gmail.com";
	private SubscriptionType 	SUBSCRIPTION_TYPE 	= SubscriptionType.PAID;
	
	/**
	* Tests construction via a Builder 
	*/
	@Test
	void testConstruction() {
		
		CampaignContactEntity contact = CampaignContactEntity
				.builder()
					.id(ID)
					.firstName(FIRSTNAME)
					.surname(SURNAME)
					.email(EMAIL)
					.subscriptionType(SUBSCRIPTION_TYPE)
				.build();
		
		assertEquals(ID, 				contact.getId());
		assertEquals(FIRSTNAME, 		contact.getFirstName());
		assertEquals(SURNAME, 			contact.getSurname());
		assertEquals(EMAIL, 			contact.getEmail());
		assertEquals(SUBSCRIPTION_TYPE, contact.getSubscriptionType());
		
	}
	
	/**
	* Tests conversion from Entity to Domain representation 
	*/
	@Test
	void testFromEntity() {
		
		CampaignContactEntity entity = CampaignContactEntity
				.builder()
					.id(ID)
					.firstName(FIRSTNAME)
					.surname(SURNAME)
					.email(EMAIL)
					.subscriptionType(SUBSCRIPTION_TYPE)
				.build();
		
		assertEquals(ID, 				entity.getId());
		assertEquals(FIRSTNAME, 		entity.getFirstName());
		assertEquals(SURNAME, 			entity.getSurname());
		assertEquals(EMAIL, 			entity.getEmail());
		assertEquals(SUBSCRIPTION_TYPE, entity.getSubscriptionType());
		
		Contact contact = CampaignContactEntity.fromEntity(entity);
		
		assertEquals(ID, 				contact.id());
		assertEquals(FIRSTNAME, 		contact.firstName());
		assertEquals(SURNAME, 			contact.surname());
		assertEquals(EMAIL, 			contact.email());
		assertEquals(SUBSCRIPTION_TYPE, contact.subscriptionType());
		
	}
	
	/**
	* Tests conversion from Domain to Entity representation 
	*/
	@Test
	void testToEntity() {
		
		Contact contact = new Contact(ID, FIRSTNAME, SURNAME, EMAIL, SUBSCRIPTION_TYPE);
		
		CampaignContactEntity entity = CampaignContactEntity.toEntity(contact);
		
		assertEquals(ID, 				entity.getId());
		assertEquals(FIRSTNAME, 		entity.getFirstName());
		assertEquals(SURNAME, 			entity.getSurname());
		assertEquals(EMAIL, 			entity.getEmail());
		assertEquals(SUBSCRIPTION_TYPE, entity.getSubscriptionType());
		
	}
	
}