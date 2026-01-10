package com.arenella.recruit.adapters.events;

import java.util.Optional;

import com.arenella.recruit.candidates.beans.Candidate;

public class CandidateUpdatedEvent {

	private final String 	candidateId;
	private final String 	firstName;
	private final String 	surname;
	private final String 	email;
	private  	  Photo		photo;
	
	/**
	* Constructor
	* @param candidateId - id of deleted Candidate
	*/
	public CandidateUpdatedEvent(String candidateId, String firstName, String surname, String email) {
		this.candidateId 	= candidateId;
		this.firstName 		= firstName;
		this.surname 		= surname;
		this.email 			= email;
	}

	public CandidateUpdatedEvent(String candidateId, String firstName, String surname, String email, Candidate.Photo photo) {
		this.candidateId 	= candidateId;
		this.firstName 		= firstName;
		this.surname 		= surname;
		this.email 			= email;
		
		Optional.ofNullable(photo).ifPresent(profileImage -> {
			try {
				PHOTO_FORMAT format = PHOTO_FORMAT.valueOf(profileImage.getFormat().toString());
				this.photo = new Photo(format, profileImage.getImageBytes());
			} catch(Exception e) {
				e.printStackTrace();
				//If image fails we send without image
			}
		});
		
	}
	
	/**
	* Returns the Id of the deleted Candidate
	* @return id of the deleted Candidate
	*/
	public String getCandidateId() {
		return this.candidateId;
	}
	
	/**
	* Returns the Candidates first name
	* @return first name
	*/
	public String getFirstName() {
		return this.firstName;
	}
	
	/**
	* Returns the candidates Surname
	* @return candidates surname
	*/
	public String getSurname() {
		return this.surname;
	}
	
	/**
	* Returns the Candidates email
	* @return email address
	*/
	public String getEmail() {
		return this.email;
	}
	
	public Optional<Photo> getPhoto(){
		return Optional.ofNullable(this.photo);
	}
	
}