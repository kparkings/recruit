package com.arenella.recruit.adapters.events;

import java.util.Optional;

import com.arenella.recruit.newsfeed.beans.NewsFeedItem.NEWSFEED_ITEM_TYPE;

/**
* Event reporting some news item relating to a Candidate
* @author K Parkings
*/
public class CandidateUpdateEvent {

	private NEWSFEED_ITEM_TYPE 		itemType;
	private int						candidateId;
	private String 					firstName;
	private String					surname;
	private String					roleSought;
	private Photo					photo;
	
	/**
	* Constructor based upon a builder
	* @param builder - Contains initialization values
	*/
	public CandidateUpdateEvent(CandidateUpdateEventBuilder builder) {
		this.itemType 		= builder.itemType;
		this.candidateId 	= builder.candidateId;
		this.firstName		= builder.firstName;
		this.surname		= builder.surname;
		this.roleSought		= builder.roleSought;
		this.photo			= builder.photo;
	}
	
	/**
	* Returns the type of news item
	* @return type of news item
	*/
	public NEWSFEED_ITEM_TYPE getItemType() {
		return this.itemType;
	}
	
	/**
	* Returns the unique id of the Candidate
	* @return
	*/
	public int getCandidateId(){
		return this.candidateId;
	}
	
	/**
	* Returns the first name of the Candidate
	* @return Name of the Candidate
	*/
	public String getFirstName() {
		return this.firstName;
	}
	
	/**
	* Returns the Surname of the Candidate
	* @return surname of the Candidate
	*/
	public String getSurname() {
		return this.surname;
	}
	
	/**
	* Returns the role sought by the Candidate
	* @return role sought by the Candidate
	*/
	public String getRoleSought() {
		return this.roleSought;
	}
	
	/**
	* If exists returns Candidates profile image
	* @return profile image
	*/
	public Optional<Photo> getPhoto() {
		return Optional.ofNullable(this.photo);
	}
	
	/**
	* Returns a builder for the class
	* @return builder
	*/
	public static CandidateUpdateEventBuilder builder() {
		return new CandidateUpdateEventBuilder();
	}
	
	/**
	* Builder for the class
	* @author K Parkings
	*/
	public static class CandidateUpdateEventBuilder{
		
		private NEWSFEED_ITEM_TYPE 		itemType;
		private int 					candidateId;
		private String 					firstName;
		private String					surname;
		private String					roleSought;
		private Photo					photo;
		
		/**
		* Sets the type of Candidate update that occurred
		* @param itemType - Type of candidate update
		* @return Builder
		*/
		public CandidateUpdateEventBuilder itemType(NEWSFEED_ITEM_TYPE itemType) {
			this.itemType = itemType;
			return this;
		}
		
		/**
		* Sets the id of the Candidate
		* @param candidateId - Id of the Candidate
		* @return Builder
		*/
		public CandidateUpdateEventBuilder candidateId(int candidateId) {
			this.candidateId = candidateId;
			return this;
		}
		
		/**
		* 
		* @param firstName
		* @return
		*/
		public CandidateUpdateEventBuilder firstName(String firstName) {
			this.firstName = firstName;
			return this;
		}
		
		/**
		* 
		* @param surname
		* @return
		*/
		public CandidateUpdateEventBuilder surname(String surname) {
			this.surname = surname;
			return this;
		}
		
		/**
		* 
		* @param roleSought
		* @return
		*/
		public CandidateUpdateEventBuilder roleSought(String roleSought) {
			this.roleSought = roleSought;
			return this;
		}
		
		/**
		* Sets user profile image
		* @param photo - Users profile image
		* @return Builder
		*/
		public CandidateUpdateEventBuilder photo(com.arenella.recruit.candidates.beans.Candidate.Photo photo) {
			
			Optional.ofNullable(photo).ifPresent(_ -> {
				try {
					PHOTO_FORMAT format = PHOTO_FORMAT.valueOf(photo.getFormat().toString());
					this.photo = new Photo(format, photo.getImageBytes());
				} catch(Exception e) {
					e.printStackTrace();
					//If image fails we send without image
				}
			});
			
			
			return this;
		}
		
		/**
		* Returns an initialized instance of the CandidateUpdateEvent
		* @return initialized instance
		*/
		public CandidateUpdateEvent build() {
			return new CandidateUpdateEvent(this);
		}

	}
	
}