package com.arenella.recruit.authentication.services;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Wart {


	/**
	* Create Single User
	* @throws Exception
	*/
	@Test
	public void testAdminUser() throws Exception{
		
		final String 	email 		=	"kparkings@gmail.com";
		final String 	username	=	email.substring(0, email.indexOf("@")); 
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		String passwordUnencoded = "Fender1980";
		
		String password = encoder.encode(passwordUnencoded);
		
		System.out.println("");
		System.out.println("");
		System.out.println("Password: " + passwordUnencoded); 
		System.out.println("");
		System.out.println("");
		System.out.println("insert into users.users values ('"+username+"', '"+password+"', true);");
		System.out.println("insert into users.user_roles values('"+username+"', 'admin');");
		
	}
	
	/**
	* Create Single User
	* @throws Exception
	*/
	@Test
	public void testSingleUser() throws Exception{
		
		Random x = new Random();
		
		final String 	email 		=	"arye.kirzner@gmail.com";
		final String 	username	=	email.substring(0, email.indexOf("@")); 
		final String 	code 		=	String.valueOf(x.nextInt()).substring(1,5);
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		String passwordUnencoded = username + code + "!";
		
		String password = encoder.encode(passwordUnencoded);
		
		System.out.println("");
		System.out.println("");
		System.out.println("Password: " + passwordUnencoded); 
		System.out.println("");
		System.out.println("");
		System.out.println("insert into users.users values ('"+username+"', '"+password+"', true);");
		System.out.println("insert into users.user_roles values('"+username+"', 'recruiter');");
		
	}
	
	/**
	* Create Mutli Users
	* @throws Exception
	*/
	@Test
	public void testMultiUsers() throws Exception{
		
		System.out.println("/* Start script */");
		System.out.println("");
		
			Set<String> 	usedUserNames 		= new HashSet<>();
			AtomicInteger 	duplicateNameCount 	= new AtomicInteger(7);
		
			Set<String> recruiters = Set.of();

			
			Random x = new Random();
			
			recruiters.stream().forEach(recruiter -> {
				
				String 	email 		=	recruiter;
				String 	username	=	email.substring(0, email.indexOf("@")); 
				username = username.replaceAll("\\.", "");
				username = username.replaceAll("_", "");
				username = username.replaceAll("-", "");
				
				if (usedUserNames.contains(username)) {
					username = username + duplicateNameCount.getAndAdd(1);
				}
				
				usedUserNames.add(username);
				
				String 	code 		=	String.valueOf(x.nextInt()).substring(1,5);
				
				BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
				
				String passwordUnencoded 	= username + code + "!";
				String password 			= encoder.encode(passwordUnencoded);
				
				System.out.println("");
				System.out.println("/*");
				System.out.println("Email      : " + recruiter);
				System.out.println("username   : " + username);
				System.out.println("Password   : " + passwordUnencoded); 
				System.out.println("*/");
				System.out.println("");
				System.out.println("insert into users.users values ('"+username+"', '"+password+"', true);");
				System.out.println("insert into users.user_roles values('"+username+"', 'recruiter');");
			
			});
			
			
		System.out.println("");
		System.out.println("/* End script */");
		
	}
	
}
