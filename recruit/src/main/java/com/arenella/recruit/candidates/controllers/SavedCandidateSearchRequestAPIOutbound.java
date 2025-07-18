package com.arenella.recruit.candidates.controllers;

import java.util.UUID;

import com.arenella.recruit.candidates.beans.SavedCandidateSearch;
import com.fasterxml.jackson.databind.JsonNode;

/**
* Representation of SavedCandidteSearchRequest specific
* to the API outgoing responses 
*/
public class SavedCandidateSearchRequestAPIOutbound {

	private UUID 		id;
	private boolean 	emailAlert;
	private String 		searchName;
	private JsonNode 	searchRequest;
	
	/**
	* Constructor based on a builder
	* @param builder - Contains initialization values
	*/
	public SavedCandidateSearchRequestAPIOutbound(SavedCandidateSearchRequestAPIOutboundBuilder builder) {
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
	public static SavedCandidateSearchRequestAPIOutboundBuilder builder() {
		return new SavedCandidateSearchRequestAPIOutboundBuilder();
	}
	
	/**
	* Builder for the class 
	*/
	public static class SavedCandidateSearchRequestAPIOutboundBuilder {
		
		private UUID 		id;
		private boolean 	emailAlert;
		private String 		searchName;
		private JsonNode 	searchRequest;
		
		/**
		* Sets the unique Id of the Saved Search
		* @param id - Unique Id
		* @return Builder
		*/
		public SavedCandidateSearchRequestAPIOutboundBuilder id(UUID id) {
			this.id = id;
			return this;
		}
		
		/**
		* Sets whether User should receive an email alert when a Candidates is 
		* added what matches the Saved Search
		* @param emailAlert - Whether to email alert
		* @return Builder
		*/
		public SavedCandidateSearchRequestAPIOutboundBuilder emailAlert(boolean emailAlert) {
			this.emailAlert = emailAlert;
			return this;
		}
		
		/**
		* Sets a user readable name for the search
		* @param searchName - name of the search
		* @return Builder
		*/
		public SavedCandidateSearchRequestAPIOutboundBuilder searchName(String searchName) {
			this.searchName = searchName;
			return this;
		}
		
		/**
		* Sets a Json representation of the Saved Search 
		* @param searchRequest - The actial search request with par
		* @return Builder
		*/
		public SavedCandidateSearchRequestAPIOutboundBuilder searchRequest(JsonNode searchRequest) {
			this.searchRequest = searchRequest;
			return this;
		}
		
		/**
		* Returns an initialized instance of the Class
		* @return Initialized instance
		*/
		public SavedCandidateSearchRequestAPIOutbound build() {
			return new SavedCandidateSearchRequestAPIOutbound(this);
		}
		
	}
	
	/**
	* 
	* @param search
	* @return
	*/
	public static SavedCandidateSearchRequestAPIOutbound fromDomain(SavedCandidateSearch search) {
		return SavedCandidateSearchRequestAPIOutbound
				.builder()
					.id(search.getId())
					.searchName(search.getSearchName())
					.searchRequest(search.getSearchRequest())
					.emailAlert(search.hasEmailAlert())
				.build();
	}
	
}