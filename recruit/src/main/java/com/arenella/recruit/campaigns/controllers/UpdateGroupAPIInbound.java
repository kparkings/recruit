package com.arenella.recruit.campaigns.controllers;

import java.util.Set;

import com.arenella.recruit.campaigns.beans.CandidateGroup.Group;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
* Command to Update Group selected candidates belong to. The Groups are a way 
* for a Recruiter to sort the Candidates in their Campaign/Role into buckets
*/
@JsonDeserialize(builder=UpdateGroupAPIInbound.UpdateGroupAPIInboundBuilder.class)
public class UpdateGroupAPIInbound {

	private Set<String> 	candidateIds;
	private Group 		group;
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	public UpdateGroupAPIInbound(UpdateGroupAPIInboundBuilder builder) {
		this.candidateIds 	= builder.candidateIds;
		this.group 			= builder.group;
	}
	
	/**
	* Returns the Id's of the Candidates whose Group is to be updated
	* @return Id's of the Candidates
	*/
	public Set<String> getCandidateIds(){
		return this.candidateIds;
	}
	
	/**
	* Returns the new Group for the Candidates
	* @return new Group
	*/
	public Group getGroup(){
		return this.group;
	}
	
	/**
	* Returns a Builder for the class
	* @return Builder
	*/
	public static UpdateGroupAPIInboundBuilder builder() {
		return new UpdateGroupAPIInboundBuilder();
	}
	
	/**
	* Builder for the class 
	*/
	@JsonPOJOBuilder(buildMethodName="build", withPrefix="")
	public static class UpdateGroupAPIInboundBuilder{
		
		private Set<String> 	candidateIds;
		private Group 		group;
		
		/**
		* Sets the Id's of the Candidates to be added to the new Group
		* @param candidateIds - Id's of the Candidates
		* @return Builder
		*/
		public UpdateGroupAPIInboundBuilder candidateIds(Set<String> candidateIds) {
			this.candidateIds = candidateIds;
			return this;
		}
		
		/**
		* Sets the new Group for the Candidates
		* @param group - Group to add the Candidates to
		* @return Builder
		*/
		public UpdateGroupAPIInboundBuilder group(Group group) {
			this.group = group;
			return this;
		}
		
		/**
		* Returns an initialized instance 
		* @return Initialized instance
		*/
		public UpdateGroupAPIInbound build() {
			return new UpdateGroupAPIInbound(this);
		}
		
	}
	
}