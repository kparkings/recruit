package com.arenella.recruit.candidate.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.arenella.recruit.candidate.beans.Language.LANGUAGE;

/**
* Composite key for the LanguageEntity class
* @author K Parkings
*/
@Embeddable
public class LanguageEntityId implements Serializable{

	private static final long serialVersionUID = -8013905255308582940L;
	
	public LanguageEntityId() {}
	
	@Column(name="candidate_id")
	private String 		candidateId;
	
	@Enumerated(EnumType.STRING)
	@Column(name="language")
	private LANGUAGE 	language;
	
	/**
	* Sets the unique identifier of the Candidayte
	* @param candidateId - Candidate Id
	*/
	public void setCandidateId(String candidateId) {
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
	public String getCandidateId() {
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