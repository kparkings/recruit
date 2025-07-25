package com.arenella.recruit.candidates.entities;

import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.arenella.recruit.candidates.beans.SavedCandidateSearch;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
* Represents a predefined search setup by a User 
* to search on Candidates
*/
@Entity
@Table(schema="candidate", name="saved_candidate_searches")
public class SavedCandidateSearchEntity {

	@Id
	@Column(name="id")
	private UUID 		id;
	
	@Column(name="user_id")
	private String 		userId;
	
	@Column(name="email_alert")
	private boolean 	emailAlert;
	
	@Column(name="search_name")
	private String 		searchName;
	
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name="search_request")
	private JsonNode 	searchRequest;
	
	/**
	* Default constructor 
	*/
	public SavedCandidateSearchEntity() {
		//Hibernate
	}
	
	/**
	* Constructor based on a builder
	* @param builder - Contains initialization values
	*/
	public SavedCandidateSearchEntity(SavedCandidateSearchEntityBuilder builder) {
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
	public static SavedCandidateSearchEntityBuilder builder() {
		return new SavedCandidateSearchEntityBuilder();
	}
	
	/**
	* Builder for the class 
	*/
	public static class SavedCandidateSearchEntityBuilder {
		
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
		public SavedCandidateSearchEntityBuilder id(UUID id) {
			this.id = id;
			return this;
		}
		
		/**
		* Sets the Unique Id of the User who owns the Saved Search
		* @param userId - Id of User
		* @return Builder
		*/
		public SavedCandidateSearchEntityBuilder userId(String userId) {
			this.userId = userId;
			return this;
		}
		
		/**
		* Sets whether User should receive an email alert when a Candidates is 
		* added what matches the Saved Search
		* @param emailAlert - Whether to email alert
		* @return Builder
		*/
		public SavedCandidateSearchEntityBuilder emailAlert(boolean emailAlert) {
			this.emailAlert = emailAlert;
			return this;
		}
		
		/**
		* Sets a user readable name for the search
		* @param searchName - name of the search
		* @return Builder
		*/
		public SavedCandidateSearchEntityBuilder searchName(String searchName) {
			this.searchName = searchName;
			return this;
		}
		
		/**
		* Sets a Json representation of the Saved Search 
		* @param searchRequest - The actial search request with par
		* @return Builder
		*/
		public SavedCandidateSearchEntityBuilder searchRequest(JsonNode searchRequest) {
			this.searchRequest = searchRequest;
			return this;
		}
		
		/**
		* Returns an initialized instance of the Class
		* @return Initialized instance
		*/
		public SavedCandidateSearchEntity build() {
			return new SavedCandidateSearchEntity(this);
		}
		
	}
	
	/**
	* Converts from Domain representation to Entity representation of SavedCandidateSearch
	* @param savedSearch - To convert
	* @return converted
	*/
	public static SavedCandidateSearchEntity toEntity(SavedCandidateSearch savedSearch) {
		return SavedCandidateSearchEntity
				.builder()
					.id(savedSearch.getId())
					.searchRequest(savedSearch.getSearchRequest())
					.searchName(savedSearch.getSearchName())
					.userId(savedSearch.getUserId())
					.emailAlert(savedSearch.hasEmailAlert())
				.build();
	}
	
	/**
	* Converts from Entity representation to Domain representation of SavedCandidateSearch
	* @param entity - To convert
	* @return converted
	*/
	public static SavedCandidateSearch fromEntity(SavedCandidateSearchEntity entity) {
		return SavedCandidateSearch
				.builder()
					.id(entity.getId())
					.searchRequest(entity.getSearchRequest())
					.searchName(entity.getSearchName())
					.userId(entity.getUserId())
					.emailAlert(entity.hasEmailAlert())
				.build();
	}
	
}