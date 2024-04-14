package com.arenella.recruit.candidates.entities;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

//import org.springframework.data.elasticsearch.annotations.Field;
//import org.springframework.data.elasticsearch.annotations.FieldType;

import com.arenella.recruit.candidates.beans.Language;

/**
* Elasticsearch Document representation of a Candidates 
* ability with a Language
* @author K Parkings
*/
public class LanguageDocument {

	//@Field(type = FieldType.Keyword, name="language")
	@Enumerated(EnumType.STRING)
	private final Language.LANGUAGE		language;

	//@Field(type = FieldType.Keyword, name="level")
	@Enumerated(EnumType.STRING)
	private final Language.LEVEL		level;
	
	/**
	* Constructor
	* @param language - Language spoken by the Candidate
	* @param level	  - Level of fluency
	*/
	public LanguageDocument(Language.LANGUAGE language, Language.LEVEL level) {
		this.language 	= language;
		this.level 		= level;
	}
	
	/**
	* Returns the name of the Language
	* @return language name
	*/
	public Language.LANGUAGE getLanguage(){
		return this.language;
	}

	/**
	* Returns users level of fluency with the Language
	* @return Fluency level
	*/
	public Language.LEVEL getLevel(){
		return this.level;
	}
	
	/**
	* Converts from Persistence to Domain representation
	* @param doc - Persistence representation to convert
	* @return Domain representation
	*/
	public static Language convertToDomain(LanguageDocument doc) {
		return Language
				.builder()
					.language(doc.getLanguage())
					.level(doc.getLevel())
				.build();
	}
	
	/**
	* Converts from Domain to Persistence representation
	* @param language - Domain representation to convert
	* @return Persistence representation
	*/
	public static LanguageDocument convertFromDomain(Language language) {
		return new LanguageDocument(language.getLanguage(), language.getLevel());
	}
	
}