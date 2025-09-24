package com.arenella.recruit.adapters.actions;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the GrantCreditAction class
* @author K Parkings
*/
class GrantCreditActionTest {

	/**
	* Tests construction
	* @throws Exception
	*/
	@Test
	void testConstruction() {
		assertDoesNotThrow(GrantCreditCommand::new);
	}
	
}
