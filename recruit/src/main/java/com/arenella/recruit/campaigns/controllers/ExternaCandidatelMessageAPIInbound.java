package com.arenella.recruit.campaigns.controllers;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
* A command to send a message to selected 
* External candidates. These are candidates with 
* no profile in the system but have been added to 
* a Campaign or Role by a recruiter. 
*/
@JsonDeserialize(builder=ExternaCandidatelMessageAPIInbound.ExternaCandidatelMessageAPIInboundBuilder.class)
public class ExternaCandidatelMessageAPIInbound {

	private Set<String> 	candidateIds 	= new HashSet<>();
	private String 			message;
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	public ExternaCandidatelMessageAPIInbound(ExternaCandidatelMessageAPIInboundBuilder builder) {
		this.candidateIds.clear();
		this.candidateIds.addAll(builder.candidateIds);
		this.message = builder.message;
	}
	
	/**
	* Returns the Ids of the Candidates to send the message to
	* @return Candidate Ids
	*/
	public Set<String> getCandidateIds(){
		return this.candidateIds;
	}
	
	/**
	* Returns the message to send to the Canddates
	* @return message to send
	*/
	public String getMessage() {
		return this.message;
	}
	
	/**
	* Returns a new Builder for the class
	* @return
	*/
	public static ExternaCandidatelMessageAPIInboundBuilder builder() {
		return new ExternaCandidatelMessageAPIInboundBuilder();
	}
	
	/**
	* Builder 
	*/
	@JsonPOJOBuilder(buildMethodName="build", withPrefix="")
	public static class ExternaCandidatelMessageAPIInboundBuilder{
		
		private Set<String> 	candidateIds 	= new HashSet<>();
		private String 			message;
		
		/**
		* Sets the Ids of the Candidates to Message
		* @param candidateIds - Candidate Ids
		* @return Builder
		*/
		public ExternaCandidatelMessageAPIInboundBuilder candidateIds(Set<String> candidateIds) {
			this.candidateIds.clear();
			this.candidateIds.addAll(candidateIds);
			return this;
		}
		
		/**
		* Sets the message to send to the Candidates
		* @param message - Message to be sent
		* @return Builder
		*/
		public ExternaCandidatelMessageAPIInboundBuilder message(String message) {
			this.message = message;
			return this;
		}
		
		/**
		* Returns initialized instance
		* @return
		*/
		public ExternaCandidatelMessageAPIInbound build() {
			return new ExternaCandidatelMessageAPIInbound(this);
		}
		
	}
	
}