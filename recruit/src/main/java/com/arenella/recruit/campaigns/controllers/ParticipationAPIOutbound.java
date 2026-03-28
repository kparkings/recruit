package com.arenella.recruit.campaigns.controllers;

import java.util.UUID;

import com.arenella.recruit.campaigns.beans.Contact;
import com.arenella.recruit.campaigns.beans.Participation;
import com.arenella.recruit.campaigns.beans.Participation.ParticipantType;

/**
* API Outbound presentation of a Participation in a Campaign or Role 
*/
public class ParticipationAPIOutbound {

	private UUID					participationId;
	private ContactAPIOutbound 		contact;
	private ParticipantType 		type;
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	public ParticipationAPIOutbound(ParticipationAPIOutboundBuilder builder) {
		this.participationId 	= builder.participationId;
		this.contact 			= builder.contact;
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
	* Returns the information about the Contact involved in the 
	* Participation
	* @return Contact details
	*/
	public ContactAPIOutbound getContact() {
		return this.contact;
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
	public static ParticipationAPIOutboundBuilder builder() {
		return new ParticipationAPIOutboundBuilder();
	}
	
	/**
	* Builder for the Class 
	*/
	public static class ParticipationAPIOutboundBuilder {
		
		private UUID					participationId;
		private ContactAPIOutbound 		contact;
		private ParticipantType 		type;
		
		/**
		* Populated the builder with values origination from the Domain 
		* objects
		* @param participation - Domain representation of the Participation
		* @param contact       - Domain representation of the Contact involved in the Participation
		* @return
		*/
		public ParticipationAPIOutboundBuilder from(Participation participation, Contact contact) {
			this.participationId = participation.getParticipanttionId();
			this.contact = new ContactAPIOutbound(contact.firstName(), contact.surname());
			this.type = participation.getType();
			return this;
		}
		
		/**
		* sets the unique Id of the Participation
		* @param participationId - Id
		* @return Builder
		*/
		public ParticipationAPIOutboundBuilder participationId(UUID	participationId) {
			this.participationId = participationId;
			return this;
		}
		
		/**
		* Sets the Human readable contact details
		* @param contact - Details of contact in the Participation
		* @return Builder
		*/
		public ParticipationAPIOutboundBuilder contact(ContactAPIOutbound contact) {
			this.contact = contact;
			return this;
		}
		
		/**
		* Sets the type of Participation
		* @param type - Participation type
		* @return Builder
		*/ 
		public ParticipationAPIOutboundBuilder type(ParticipantType type) {
			this.type = type;
			return this;
		}
		
		/**
		* Returns an Initialized Participation 
		* @return Initialized Participation
		*/
		public ParticipationAPIOutbound build() {
			return new ParticipationAPIOutbound(this);
		}
		
	}
	
}