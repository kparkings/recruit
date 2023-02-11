package com.arenella.recruit.candidates.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
* Composite key for the SavedCandidateEntity class
* @author K Parkings
*/
@Embeddable
public class SavedCandidateEntityId implements Serializable{

	private static final long serialVersionUID = 669808053138831457L;

	public SavedCandidateEntityId() {}
	
	@Column(name="candidate_id")
	private long	candidateId;
	
	@Column(name="user_id")
	private String 	userId;
	
	/**
	* Sets the unique identifier of the Candidate
	* @param candidateId - Candidate Id
	*/
	public void setCandidateId(long candidateId) {
		this.candidateId = candidateId;
	}
	
	/**
	* Sets the Unique id of the User who saved the Candidate
	* @param userId - Unique Id of the User
	*/
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	/**
	* Returns the Candidates unique Id
	* @return Candidate ID
	*/
	public long getCandidateId() {
		return this.candidateId;
	}
	
	/**
	* Returns the userId of the User who saved the Candidate
	* @return Unique id of the User
	*/
	public String getUserId() {
		return this.userId;
	}
	
}