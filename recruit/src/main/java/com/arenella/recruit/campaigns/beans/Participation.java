package com.arenella.recruit.campaigns.beans;

import java.util.Optional;
import java.util.UUID;

/**
* Represents a Participation in a Campaign by a Contact 
*/
public class Participation {

	public enum ParticipantType {ADMIN, EDIT, VIEW}
	
	private UUID			participationId;
	private String 			contactId;
	private UUID 			campaignId; 
	private UUID 			roleId;
	private ParticipantType type;
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	public Participation(ParticipationBuilder builder) {
		this.participationId 	= builder.participationId;
		this.contactId 			= builder.contactId;
		this.campaignId 		= builder.campaignId;
		this.roleId 			= builder.roleId;
		this.type 				= builder.type;
	}
	
	/**
	* Returns the Unique Id of the Participation
	* @return
	*/
	public UUID getParticipanttionId() {
		return this.participationId;
	}
	
	/**
	* Returns the Unique Id of the Contact who is the Participant 
	* in the Participation
	* @return id of the Contact
	*/
	public String getContactId() {
		return this.contactId;
	}
	
	/**
	* Returns the unique Id of the Campaign the Contact is participating in
	* @return Id of the Campaign
	*/
	public UUID getCampaignId() {
		return this.campaignId;
	} 
	
	/**
	* If this is a Role level Participation returns the Unique if of the Role the 
	* Contact is participating in
	* @return Unique Id of the Role
	*/
	public Optional<UUID> getRoleId() {
		return Optional.ofNullable(this.roleId);
	}
	
	/**
	* Returns the type of the Participation
	* @return participation type
	*/
	public ParticipantType getType() {
		return this.type;
	}
	
	/**
	* Returns a Builder for the Class 
	* @return Builder
	*/
	public static ParticipationBuilder builder() {
		return new ParticipationBuilder();
	}
	
	/**
	* Builder for the Class 
	*/
	public static class ParticipationBuilder {
		
		private UUID			participationId;
		private String 			contactId;
		private UUID 			campaignId; 
		private UUID 			roleId;
		private ParticipantType type;
		
		/**
		* Sets the unique Id of the Participation 
		* @param participationId - Id
		* @return Builder
		*/
		public ParticipationBuilder participationId(UUID participationId) {
			this.participationId = participationId;
			return this;
		}
		
		/**
		* Sets the unique Id of the Contact that is participation
		* @param contactId - Jd of the Contact
		* @return Builder
		*/
		public ParticipationBuilder contactId(String contactId) {
			this.contactId = contactId;
			return this;
		}
		
		/**
		* Sets the Unique id of the Campaign the Contact is participating in
		* @param campaignId - Campaign Id
		* @return Builder
		*/
		public ParticipationBuilder campaignId(UUID campaignId) {
			this.campaignId = campaignId;
			return this;
		} 
		
		/**
		* If the Participation is at Role level. Sets the Unique Id of the Role 
		* being participated in
		* @param roleId - Id of the Role
		* @return Builder
		*/
		public ParticipationBuilder roleId(UUID roleId) {
			this.roleId = roleId;
			return this;
		}
		
		/**
		* Sets the type of Participation
		* @param type - Type of the Participation
		* @return Builder
		*/
		public ParticipationBuilder type(ParticipantType type) {
			this.type = type;
			return this;
		}
		
		/**
		* Returns an Initialized Participation 
		* @return Initialized Participation
		*/
		public Participation build() {
			return new Participation(this);
		}
		
	}
	
}
