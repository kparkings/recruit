package com.arenella.recruit.candidates.entities;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import com.arenella.recruit.candidates.beans.Language.LANGUAGE;

/**
* Composite key for the LanguageEntity class
* @author K Parkings
*/
@Embeddable
public class LanguageEntityId implements Serializable{

	private static final long serialVersionUID = -8013905255308582940L;
	
	public LanguageEntityId() {}
	
	@Column(name="candidate_id")
	private long 		candidateId;
	
	@Enumerated(EnumType.STRING)
	@Column(name="language")
	private LANGUAGE 	language;
	
	/**
	* Sets the unique identifier of the Candidayte
	* @param candidateId - Candidate Id
	*/
	public void setCandidateId(long candidateId) {
		this.candidateId = candidateId;
	}
	
	/**
	* Sets the Language spoken by the Candidate
	* @param language - Language name
	*/
	public void setLanguage(LANGUAGE language) {
		this.language = language;
	}
	
	/**
	* Returns the Candidates unique Id
	* @return Candidate ID
	*/
	public long getCandidateId() {
		return this.candidateId;
	}
	
	/**
	* Returns the name of the Language spoken by the Candidate
	* @return Langauge spoken by the Candidate
	*/
	public LANGUAGE getLanguage() {
		return this.language;
	}
	
}