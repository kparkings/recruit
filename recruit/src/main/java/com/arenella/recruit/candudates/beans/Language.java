package com.arenella.recruit.candudates.beans;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
* Represents a language spoken by a Candidate
* @author K Parkings
*/
@JsonDeserialize(builder=Language.LanguageBuilder.class)
public class Language {

	public static enum LANGUAGE {DUTCH, ENGLISH, FRENCH}
	public static enum LEVEL {BASIC, PROFICIENT, UNKNOWN}

	private LANGUAGE 	language;
	private LEVEL 		level;
	
	/**
	* Constructor based upon a builder
	* @param builder - Contains the initialization values
	*/
	public Language(LanguageBuilder builder) {
		this.language 	= builder.language;
		this.level 		= builder.level;
	}
	
	/**
	* Sets the Langauge spoken by the Candidate
	* @return the Language
	*/
	public LANGUAGE getLanguage() {
		return language;
	}
	
	/**
	* Returns the Candidates level of competence with the Language
	* @return level of competence
	*/
	public LEVEL getLevel() {
		return level;
	}
	
	/**
	* Returns an instance of a Builder for the Language class
	* @return Builder for the Language class
	*/
	public static LanguageBuilder builder() {
		return new LanguageBuilder();
	}
	
	/**
	* Builder for the Language class
	* @author K Parkings
	*/
	@JsonPOJOBuilder(buildMethodName="build", withPrefix="")
	public static class LanguageBuilder {
	
		private LANGUAGE 	language;
		private LEVEL 		level;

		/**
		* Sets the Language spoken by the Candidate 
		* @param language - Language spoken by the Candidate
		* @return Builder
		*/
		public LanguageBuilder language(LANGUAGE language) {
			this.language = language;
			return this;
		}
		
		/**
		* Sets how competent the Candidate is with the language
		* @param level - How experienced the Candidate is with the Language
		* @return Builder
		*/
		public LanguageBuilder level(LEVEL level) {
			this.level = level;
			return this;
		}
		
		/**
		* Returns an instance of Language initialized with the 
		* values in the Builder
		* @return Initialized instance of Language
		*/
		public Language build() {
			
			/**
			* Language only valid if both the language 
			* and level have been provided
			*/
			if (language == null || level == null) {
				throw new IllegalStateException();
			}
			
			return new Language(this);
		
		}
		
	}
	
}