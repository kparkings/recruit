package com.arenella.recruit.candidates.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.arenella.recruit.candudates.beans.Language.LANGUAGE;
import com.arenella.recruit.candudates.beans.Language.LEVEL;

/**
* Entity representation of a Language spoken by a Candidate
* @author K Parkings
*/
@Entity
@Table(schema="candidate", name="candidate_language")
public class LanguageEntity implements Serializable{

	private static final long serialVersionUID = -7723031022369804453L;

	@EmbeddedId
	private LanguageEntityId id = new LanguageEntityId();
	
	@Column(name="level")
	@Enumerated(EnumType.STRING)
	private LEVEL 				level;
	
	@SuppressWarnings("unused")
	private LanguageEntity() {}
	
	/**
	* Constructor based upon values in a Builder
	* @param builder - Contains initialization values
	*/
	public LanguageEntity(LanguageEntityBuilder builder) {
		this.id.setCandidateId(builder.candidate.getCandidateId());
		this.id.setLanguage(builder.language);
		this.level = builder.level;
	}
	
	/**
	* Returns the unique identifier of the Candidaye
	* @return candidate Id
	*/
	public long getCandidateId() {
		return id.getCandidateId();
	}
	
	/**
	* Returns the details of a Language spoken 
	* by a Candidate
	* @return Language spoken by the Candidate
	*/
	public LANGUAGE getLanguage() {
		return id.getLanguage();
	}
	
	/**
	* Returns Candidates proficiency in the Language
	* @return Proficiency
	*/
	public LEVEL getLevel() {
		return level;
	}
	
	/**
	* Returns an instance of a Builder for the LanguageEntity class
	* @return Builder for the Class
	*/
	public static LanguageEntityBuilder builder() {
		return new LanguageEntityBuilder();
	}
	
	/**
	* Builder for the LanguageEntity clas
	* @author K Parkings
	*/
	public static class LanguageEntityBuilder {
	
		private CandidateEntity 	candidate;
		private LANGUAGE 			language;
		private LEVEL 				level;
		
		/**
		* Sets the Candidate 
		* @param candidate - Candidate that speaks the Language
		* @return Builder
		*/
		public LanguageEntityBuilder candidate(CandidateEntity candidate) {
			this.candidate = candidate;
			return this;
		}
		
		/**
		* Sets the Language spoken by the Candidate
		* @param language - Language spoken
		* @return Builder
		*/
		public LanguageEntityBuilder language(LANGUAGE language) {
			this.language = language;
			return this;
		}
		
		/**
		* Sets the Candidates proficiency Level with the Language
		* @param level - Candidates proficiency with the Langauge
		* @return Builder
		*/
		public LanguageEntityBuilder level(LEVEL level) {
			this.level = level;
			return this;
		}
		
		/**
		* Returns an instance of LanguageEntity initialized with the 
		* values in the Builder
		* @return initialized LanguageEntity
		*/
		public LanguageEntity build() {
			return new LanguageEntity(this);
		}
		
	}
	
}