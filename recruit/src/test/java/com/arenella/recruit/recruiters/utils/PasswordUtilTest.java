package com.arenella.recruit.recruiters.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the PasswordUtil class
* @author K Parkings
*/
class PasswordUtilTest {

	/**
	* Tests creation of Password
	* @throws Exception
	*/
	@Test
	void testGeneratePassword() {
		assertEquals(PasswordUtil.PASSWORD_LENGTH, PasswordUtil.generatePassword().length());
	}
	
	/**
	* Tests password encryption
	* @throws Exception
	*/
	@Test
	void testEncryptPassword() {
		assertNotNull(PasswordUtil.encryptPassword(""));
	}
	
}
