package com.arenella.recruit.candidates.controllers;

import java.util.UUID;

import com.arenella.recruit.candidates.beans.SavedCandidateSearch;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
* Representation of SavedCandidteSearchRequest specific
* to the API to request creation of a new search 
*/
@JsonDeserialize(builder=SavedCandidateSearchRequestCreateAPIInbound.SavedCandidateSearchRequestCreateAPIInboundBuilder.class)
public class SavedCandidateSearchRequestCreateAPIInbound {

	private boolean 	emailAlert;
	private String 		searchName;
	private JsonNode 	searchRequest;
	
	/**
	* Constructor based on a builder
	* @param builder - Contains initialization values
	*/
	public SavedCandidateSearchRequestCreateAPIInbound(SavedCandidateSearchRequestCreateAPIInboundBuilder builder) {
		this.emailAlert 		= builder.emailAlert;
		this.searchName 		= builder.searchName;
		this.searchRequest 		= builder.searchRequest;
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
	public static SavedCandidateSearchRequestCreateAPIInboundBuilder builder() {
		return new SavedCandidateSearchRequestCreateAPIInboundBuilder();
	}
	
	/**
	* Builder for the class 
	*/
	@JsonPOJOBuilder(buildMethodName="build", withPrefix="")
	public static class SavedCandidateSearchRequestCreateAPIInboundBuilder {
		
		private boolean 	emailAlert;
		private String 		searchName;
		private JsonNode 	searchRequest;
		
		/**
		* Sets whether User should receive an email alert when a Candidates is 
		* added what matches the Saved Search
		* @param emailAlert - Whether to email alert
		* @return Builder
		*/
		public SavedCandidateSearchRequestCreateAPIInboundBuilder emailAlert(boolean emailAlert) {
			this.emailAlert = emailAlert;
			return this;
		}
		
		/**
		* Sets a user readable name for the search
		* @param searchName - name of the search
		* @return Builder
		*/
		public SavedCandidateSearchRequestCreateAPIInboundBuilder searchName(String searchName) {
			this.searchName = searchName;
			return this;
		}
		
		/**
		* Sets a Json representation of the Saved Search 
		* @param searchRequest - The actial search request with par
		* @return Builder
		*/
		public SavedCandidateSearchRequestCreateAPIInboundBuilder searchRequest(JsonNode searchRequest) {
			this.searchRequest = searchRequest;
			return this;
		}
		
		/**
		* Returns an initialized instance of the Class
		* @return Initialized instance
		*/
		public SavedCandidateSearchRequestCreateAPIInbound build() {
			return new SavedCandidateSearchRequestCreateAPIInbound(this);
		}
		
	}
	
	/**
	* Converts from API representation to Domain representation
	* @param inbound - Inbound representation
	* @param id		 - Unique Id for new Search
	* @param userId	 - owner of Search
	* @return Domain representation
	*/
	public static SavedCandidateSearch toDomain(SavedCandidateSearchRequestCreateAPIInbound inbound, UUID id, String userId) {
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