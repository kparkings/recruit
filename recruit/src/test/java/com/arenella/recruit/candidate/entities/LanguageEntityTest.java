package com.arenella.recruit.candidate.entities;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.arenella.recruit.beans.Language.LANGUAGE;
import com.arenella.recruit.beans.Language.LEVEL;
import com.arenella.recruit.entities.CandidateEntity;
import com.arenella.recruit.entities.LanguageEntity;

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

		final String 			candidateId 			= "C1002";
		final LANGUAGE 			language 				= LANGUAGE.DUTCH;
		final LEVEL 			level 					= LEVEL.PROFICIENT;
		final CandidateEntity 	candidateEntity 		= CandidateEntity.builder().candidateId(candidateId).build();
		
		LanguageEntity entity = LanguageEntity.builder().candidate(candidateEntity).language(language).level(level).build();
		
		assertEquals(entity.getCandidateId(), 		candidateId);
		assertEquals(entity.getLanguage(), 			language);
		assertEquals(entity.getLevel(),	 			level);
		
	}
	
}