package com.arenella.recruit.candidates.beans;

import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;

/**
* Represents a predefined search setup by a User 
* to search on Candidates
*/
public class SavedCandidateSearch {

	//TODO: [KP] Need to remove these if recruiter account deleted
	
	private UUID 		id;
	private String 		userId;
	private boolean 	emailAlert;
	private String 		searchName;
	private JsonNode 	searchRequest;
	
	/**
	* Constructor based on a builder
	* @param builder - Contains initialization values
	*/
	public SavedCandidateSearch(SavedCandidateSearchBuilder builder) {
		this.id 				= builder.id;
		this.userId 			= builder.userId;
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
	* Returns the UniqueId of the User who owns the saved search
	* @return Id of the User
	*/
	public String getUserId() {
		return this.userId;
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
	public static SavedCandidateSearchBuilder builder() {
		return new SavedCandidateSearchBuilder();
	}
	
	/**
	* Builder for the class 
	*/
	public static class SavedCandidateSearchBuilder {
		
		private UUID 		id;
		private String 		userId;
		private boolean 	emailAlert;
		private String 		searchName;
		private JsonNode 	searchRequest;
		
		/**
		* Sets the unique Id of the Saved Search
		* @param id - Unique Id
		* @return Builder
		*/
		public SavedCandidateSearchBuilder id(UUID id) {
			this.id = id;
			return this;
		}
		
		/**
		* Sets the Unique Id of the User who owns the Saved Search
		* @param userId - Id of User
		* @return Builder
		*/
		public SavedCandidateSearchBuilder userId(String userId) {
			this.userId = userId;
			return this;
		}
		
		/**
		* Sets whether User should receive an email alert when a Candidates is 
		* added what matches the Saved Search
		* @param emailAlert - Whether to email alert
		* @return Builder
		*/
		public SavedCandidateSearchBuilder emailAlert(boolean emailAlert) {
			this.emailAlert = emailAlert;
			return this;
		}
		
		/**
		* Sets a user readable name for the search
		* @param searchName - name of the search
		* @return Builder
		*/
		public SavedCandidateSearchBuilder searchName(String searchName) {
			this.searchName = searchName;
			return this;
		}
		
		/**
		* Sets a Json representation of the Saved Search 
		* @param searchRequest - The actial search request with par
		* @return Builder
		*/
		public SavedCandidateSearchBuilder searchRequest(JsonNode searchRequest) {
			this.searchRequest = searchRequest;
			return this;
		}
		
		/**
		* Returns an initialized instance of the Class
		* @return Initialized instance
		*/
		public SavedCandidateSearch build() {
			return new SavedCandidateSearch(this);
		}
		
	}
	
}