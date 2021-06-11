package com.arenella.recruit.authentication.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.arenella.recruit.authentication.services.AccountService;
import com.arenella.recruit.candidates.controllers.AccountCreatedAPIOutbound;
import com.arenella.recruit.candidates.controllers.CreateAccountAPIInbound;

@CrossOrigin(origins = {	"https://api-arenella-ict.wosah.nl/authenticate"	
		,	"https://arenella-ict.wosah.nl"
		, 	"http://arenella-ict.wosah.nl"
		,	"https://arenella-ict.wosah.nl/"
		, 	"http://arenella-ict.wosah.nl/"
		,  	"http://api-arenella-ict.wosah.nl/"
		, 	"https://api-arenella-ict.wosah.nl/"
		, 	"http://api-arenella-ict.wosah.nl"
		, 	"https://api-arenella-ict.wosah.nl"
		,	"http://api.arenella-ict.com/"
		, 	"htts://api.arenella-ict.com/"
		, 	"http://127.0.0.1:4200"
		, 	"http://127.0.0.1:8080"
		, 	"http://127.0.0.1:9090"
		,	"https://www.arenella-ict.com"
		, 	"https://www.arenella-ict.com:4200"
		, 	"https://www.arenella-ict.com:8080"}, allowedHeaders = "*")
@RestController
public class AccountController {

	@Autowired
	private AccountService accountService;
	
	/**
	* Creates a new User account
	* @param account - Details of the account to create
	* @return Details of the new account
	* @throws Exception
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping(path="account", consumes="application/json", produces="application/json")
	public AccountCreatedAPIOutbound createAuthenticationToken(@RequestBody CreateAccountAPIInbound account) throws Exception{
		 
		return AccountCreatedAPIOutbound.convertFromUser(accountService.createAccount(account.getProposedUsername(), account.getAccountType()));
		
	}
	
	
}
