package com.arenella.recruit.candidate.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.arenella.recruit.candidate.beans.Language.LANGUAGE;
import com.arenella.recruit.candidate.beans.Language.LEVEL;

@Entity
@Table(name="candidate_language")
public class LanguageEntity implements Serializable{

	private static final long serialVersionUID = -7723031022369804453L;

	@Id
	@Column(name="candidate_id")
	private String candidateId;
	
	//@Id
	@Column(name="language")
	@Enumerated(EnumType.STRING)
	private LANGUAGE 			language;
	
	@Column(name="level")
	@Enumerated(EnumType.STRING)
	private LEVEL 				level;
	
	private LanguageEntity() {}
	
	public LanguageEntity(LanguageEntityBuilder builder) {
		this.candidateId 	= builder.candidate.getCandidateId();
		this.language 		= builder.language;
		this.level 			= builder.level;
	}
	
	public String getCandidateId() {
		return candidateId;
	}
	
	public LANGUAGE getLanguage() {
		return language;
	}
	
	public LEVEL getLevel() {
		return level;
	}
	
	public static LanguageEntityBuilder builder() {
		return new LanguageEntityBuilder();
	}
	
	public static class LanguageEntityBuilder {
	
		private CandidateEntity 	candidate;
		private LANGUAGE 			language;
		private LEVEL 				level;
		
		public LanguageEntityBuilder candidate(CandidateEntity candidate) {
			this.candidate = candidate;
			return this;
		}
		
		public LanguageEntityBuilder language(LANGUAGE language) {
			this.language = language;
			return this;
		}
		
		public LanguageEntityBuilder level(LEVEL level) {
			this.level = level;
			return this;
		}
		
		public LanguageEntity build() {
			return new LanguageEntity(this);
		}
		
	}
}