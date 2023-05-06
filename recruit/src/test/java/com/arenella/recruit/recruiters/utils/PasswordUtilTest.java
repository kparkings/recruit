package com.arenella.recruit.recruiters.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

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
	
}
