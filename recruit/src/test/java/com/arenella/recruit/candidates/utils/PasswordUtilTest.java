package com.arenella.recruit.candidates.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.Candidate;

/**
* Unit tests for the PasswordUtil class
* @author K Parkings
*/
public class PasswordUtilTest {

	/**
	* Tests creation of Password
	* @throws Exception
	*/
	@Test
	public void testGeneratePassword() throws Exception{
		assertEquals(PasswordUtil.PASSWORD_LENGTH, PasswordUtil.generatePassword().length());
	}
	
	/**
	* Tests password encryption
	* @throws Exception
	*/
	@Test
	public void testEncryptPassword() throws Exception{
		assertNotNull(PasswordUtil.encryptPassword(""));
	}
	
	/**
	* Tests password encryption
	* @throws Exception
	*/
	@Test
	public void testGenerateUserName() throws Exception{
		
		Candidate c = Candidate.builder().firstname("KevIn").candidateId("123").build();
		
		assertEquals("kevin123", PasswordUtil.generateUsername(c));
		
	}
	
}
