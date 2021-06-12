package com.arenella.recruit.authentication.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.arenella.recruit.authentication.beans.AccountCreatedAPIOutbound;
import com.arenella.recruit.authentication.beans.CreateAccountAPIInbound;
import com.arenella.recruit.authentication.beans.User;
import com.arenella.recruit.authentication.enums.AccountType;
import com.arenella.recruit.authentication.services.AccountService;

/**
* Unit tests for the AccountController class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {
	
	@Mock
	private AccountService 		mockAccountService;
	
	@InjectMocks
	private AccountController 	accountController 		= new AccountController();
	
	/**
	* Happy path test for creating new Recruiter Account
	* @throws Exception
	*/
	@Test
	public void testCreateRecruiterAccount() throws Exception{
		
		final String 		proposedName 	= "kparkings";
		final AccountType 	accountType 	= AccountType.RECRUITER;
		final String		password		= "aPassword";
		final User			user			= User.builder().username(proposedName).password(password).build();
		
		Mockito.when(mockAccountService.createAccount(proposedName, accountType)).thenReturn(user);
		
		CreateAccountAPIInbound accountRequest = CreateAccountAPIInbound
															.builder()
																.proposedUsername(proposedName)
																.accountType(accountType)
															.build();
		
		AccountCreatedAPIOutbound response = accountController.createAccount(accountRequest);
		
		assertEquals(proposedName, 	response.getUsername());
		assertEquals(password, 		response.getPassword());
		
	}
	
}