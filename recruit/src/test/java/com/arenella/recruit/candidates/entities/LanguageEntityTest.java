package com.arenella.recruit.candidates.entities;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.arenella.recruit.candidates.beans.Language.LANGUAGE;
import com.arenella.recruit.candidates.beans.Language.LEVEL;
import com.arenella.recruit.candidates.entities.CandidateEntity;
import com.arenella.recruit.candidates.entities.LanguageEntity;

/**
* Unit tests for the LanguageEntity class
* @author K Parkings
*/
public class LanguageEntityTest {

	/**
	* Tests Instance is populated by the Builder
	* @throws Exception
	*/
	@Test
	public void testBuilder() throws Exception{

		final String 			candidateId 			= "1002";
		final LANGUAGE 			language 				= LANGUAGE.DUTCH;
		final LEVEL 			level 					= LEVEL.PROFICIENT;
		final CandidateEntity 	candidateEntity 		= CandidateEntity.builder().candidateId(candidateId).build();
		
		LanguageEntity entity = LanguageEntity.builder().candidate(candidateEntity).language(language).level(level).build();
		
		assertEquals(entity.getCandidateId(), 		Long.valueOf(candidateId).longValue());
		assertEquals(entity.getLanguage(), 			language);
		assertEquals(entity.getLevel(),	 			level);
		
	}
	
}