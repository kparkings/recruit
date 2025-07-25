package com.arenella.recruit.candidates.controllers;

import java.util.UUID;

import com.arenella.recruit.candidates.beans.SavedCandidateSearch;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
* Representation of SavedCandidteSearchRequest specific
* to the API to request an update to an existing search 
*/
@JsonDeserialize(builder=SavedCandidateSearchRequestUpdateAPIInbound.SavedCandidateSearchRequestUpdateAPIInboundBuilder.class)
public class SavedCandidateSearchRequestUpdateAPIInbound {

	private UUID 		id;			//Why is this here. We pass it in the path
	private boolean 	emailAlert;
	private String 		searchName;
	private JsonNode 	searchRequest;
	
	/**
	* Constructor based on a builder
	* @param builder - Contains initialization values
	*/
	public SavedCandidateSearchRequestUpdateAPIInbound(SavedCandidateSearchRequestUpdateAPIInboundBuilder builder) {
		this.id 				= builder.id;
		this.emailAlert 		= builder.emailAlert;
		this.searchName 		= builder.searchName;
		this.searchRequest 		= builder.searchRequest;
	}
	
	/**
	* Returns the unique Id of the Saved Search
	* @return Id
	*/
	public UUID getId() {
		return this.id;
	}
	
	/**
	* Returns whether the system should automatically send an email to 
	* the User when a new candidate is added that matches the query 
	* parameters
	* @return Whether to email User
	*/
	public boolean hasEmailAlert() {
		return this.emailAlert;
	}
	
	/**
	* Returns a user readable name for the Search
	* @return name for the search
	*/
	public String getSearchName() {
		return this.searchName;
	}
	
	/**
	* Returns a Json representation of the SearchRequest
	* @return Search Request
	*/
	public JsonNode getSearchRequest() {
		return this.searchRequest;
	}
	
	/**
	* Returns a builder for the class
	* @return Builder
	*/
	public static SavedCandidateSearchRequestUpdateAPIInboundBuilder builder() {
		return new SavedCandidateSearchRequestUpdateAPIInboundBuilder();
	}
	
	/**
	* Builder for the class 
	*/
	@JsonPOJOBuilder(buildMethodName="build", withPrefix="")
	public static class SavedCandidateSearchRequestUpdateAPIInboundBuilder {
		
		private UUID 		id;
		private boolean 	emailAlert;
		private String 		searchName;
		private JsonNode 	searchRequest;
		
		/**
		* Sets the unique Id of the Saved Search
		* @param id - Unique Id
		* @return Builder
		*/
		public SavedCandidateSearchRequestUpdateAPIInboundBuilder id(UUID id) {
			this.id = id;
			return this;
		}
		
		/**
		* Sets whether User should receive an email alert when a Candidates is 
		* added what matches the Saved Search
		* @param emailAlert - Whether to email alert
		* @return Builder
		*/
		public SavedCandidateSearchRequestUpdateAPIInboundBuilder emailAlert(boolean emailAlert) {
			this.emailAlert = emailAlert;
			return this;
		}
		
		/**
		* Sets a user readable name for the search
		* @param searchName - name of the search
		* @return Builder
		*/
		public SavedCandidateSearchRequestUpdateAPIInboundBuilder searchName(String searchName) {
			this.searchName = searchName;
			return this;
		}
		
		/**
		* Sets a Json representation of the Saved Search 
		* @param searchRequest - The actial search request with par
		* @return Builder
		*/
		public SavedCandidateSearchRequestUpdateAPIInboundBuilder searchRequest(JsonNode searchRequest) {
			this.searchRequest = searchRequest;
			return this;
		}
		
		/**
		* Returns an initialized instance of the Class
		* @return Initialized instance
		*/
		public SavedCandidateSearchRequestUpdateAPIInbound build() {
			return new SavedCandidateSearchRequestUpdateAPIInbound(this);
		}
		
	}

	/**
	* Converts from inbound to domain representation
	* @param inbound - to convert
	* @param id		 - Id of existing search
	* @param userId	 - Id of current authenticated User
	* @return Domain representation
	*/
	public static SavedCandidateSearch toDomain(SavedCandidateSearchRequestUpdateAPIInbound inbound, UUID id, String userId) {
		return SavedCandidateSearch
				.builder()
					.id(id)
					.userId(userId)
					.searchName(inbound.getSearchName())
					.searchRequest(inbound.getSearchRequest())
					.emailAlert(inbound.hasEmailAlert())
				.build();
	}
	
}