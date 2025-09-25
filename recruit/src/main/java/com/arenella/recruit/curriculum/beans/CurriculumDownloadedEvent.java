package com.arenella.recruit.curriculum.beans;

import java.time.LocalDateTime;

import org.springframework.security.core.context.SecurityContextHolder;

/**
* Event representing the downloading of a Curriculum
* @author K Parkings
*/
public class CurriculumDownloadedEvent {

	private String 			curriculumId;
	private String 			userId;
	private boolean			isAdminUser;
	private LocalDateTime 	timestamp;
	
	/**
	* Constructor based upon a builder
	* @param builder - Contains initialization values
	*/
	public CurriculumDownloadedEvent(CurriculumDownloadEventBuilder builder) {
		
		this.curriculumId 	= builder.curriculumId;
		this.userId			= SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		this.isAdminUser	= SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));
		this.timestamp		= LocalDateTime.now();
		
	}
	
	/**
	* Constructor based upon a builder
	* @param builder - Contains initialization valies
	*/
	public CurriculumDownloadedEvent(CurriculumDownloadEventManualBuilder builder) {
		
		this.curriculumId 	= builder.curriculumId;
		this.userId			= builder.userId;
		this.isAdminUser	= builder.isAdminUser;
		this.timestamp		= builder.timestamp;
	}
	
	/**
	* Returns the uniqueId of the curriculum that was 
	* downloaded
	* @return Unique Id of the curriculum
	*/
	public String getCurriculumId() {
		return this.curriculumId;
	}
	
	/**
	* Returns the unique Id of the user that 
	* downloaded the Curriculum
	* @return unique Id of the User
	*/
	public String getUserId() {
		return this.userId;
	}
	
	/**
	* Returns whether or not the user downloaded the Curriculum
	* was an Admin user
	* @return
	*/
	public boolean isAdminUser() {
		return this.isAdminUser;
	}
	
	/**
	* Returns the time that the Curriculum was downloaded
	* @return When the Curriculum was downloaded
	*/
	public LocalDateTime getTimestamp() {
		return this.timestamp;
	}
	
	/**
	* Returns a Builder for the CurriculumDownloadedEvent
	* @return Builder
	*/
	public static CurriculumDownloadEventBuilder builder() {
		return new CurriculumDownloadEventBuilder();
	}
	
	/**
	* Returns a Builder for the CurriculumDownloadedEvent
	* @return Builder
	*/
	public static CurriculumDownloadEventManualBuilder manualBuilder() {
		return new CurriculumDownloadEventManualBuilder();
	}
	
	/**
	* Builder for the CurriculumDownloadedEvent class
	* @author K Parkings
	*/
	public static class CurriculumDownloadEventBuilder {
		
		private String 			curriculumId;
		
		/**
		* Sets the Id of the curriculum that was downloaded
		* @param curriculumId - Unique Id of the downloaded curriculum
		* @return Builder
		*/
		public CurriculumDownloadEventBuilder curriculumId(String curriculumId) {
			this.curriculumId = curriculumId;
			return this;
		}
		
		/**
		* Returns an initialized instance of the CurriculumDownloadedEvent
		* @return instance of CurriculumDownloadedEvent
		*/
		public CurriculumDownloadedEvent build() {
			return new CurriculumDownloadedEvent(this);
		}
	}
	
	/**
	* Builder for the CurriculumDownloadedEvent class
	* @author K Parkings
	*/
	public static class CurriculumDownloadEventManualBuilder {
		
		private String 			curriculumId;
		private String 			userId;
		private boolean			isAdminUser;
		private LocalDateTime 	timestamp;
		
		/**
		* Sets the Id of the curriculum that was downloaded
		* @param curriculumId - Unique Id of the downloaded curriculum
		* @return Builder
		*/
		public CurriculumDownloadEventManualBuilder curriculumId(String curriculumId) {
			this.curriculumId = curriculumId;
			return this;
		}
		
		/**
		* Sets the Unique Identifier of the User that downloaded the Curriculum
		* @param userId		- Unique id of the User
		* @return Builder
		*/
		public CurriculumDownloadEventManualBuilder userId(String userId) {
			this.userId = userId;
			return this;
		}
		
		/**
		* Sets whether the User who downloaded the Curriculum was an Admin user
		* @param isAdminUser - Whether download was done by an Admin user
		* @return Builder
		*/
		public CurriculumDownloadEventManualBuilder isAdminUser(boolean isAdminUser) {
			this.isAdminUser = isAdminUser;
			return this;
		}
		
		/**
		* Sets the time when the download took place
		* @param timestamp - Time download took place
		* @return Builder
		*/
		public CurriculumDownloadEventManualBuilder timestamp(LocalDateTime timestamp) {
			this.timestamp = timestamp;
			return this;
		}
		
		/**
		* Returns an initialized instance of the CurriculumDownloadedEvent
		* @return instance of CurriculumDownloadedEvent
		*/
		public CurriculumDownloadedEvent build() {
			return new CurriculumDownloadedEvent(this);
		}
	}
	
}