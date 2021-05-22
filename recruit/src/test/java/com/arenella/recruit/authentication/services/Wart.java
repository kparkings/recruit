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
		
		Random x = new Random();
		
		final String 	email 		=	"kparkings@gmail.com";
		final String 	username	=	email.substring(0, email.indexOf("@")); 
		final String 	code 		=	"";
		
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
		
		final String 	email 		=	"kparkings@gmail.com";
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
		
			Set<String> recruiters = Set.of(
			
			  "Nicole.Kroon@braincap.nl"
			, "joshuasmith.s@gmail.com"
			, "johannsmit31@outlook.com"
			, "glen.smale@staffcluster.com"
			, "james@acceler-it.com"
			, "james.seals@lorienglobal.com"
			, "joshua@innovex-recruitment.com"
			, "will.roeder@select-tech.co.uk"
			, "xgomez@spilberg.be"
			, "sean@jl-recruitment.com"
			, "marcoloen@buddie.nl"
			, "Owen@shaerp.nl"
			, "Matt@next-ventures.com"
			, "lewis.gage@parallelconsulting.com"
			, "bart@louterrecruitment.nl"
			, "Info@u-team.nl"
			, "sales@ecosystemglobal.com"
			, "amit_shah@bluerose-tech.com"
			, "ivory@thinknorth.nl"
			, "arsene.uwimana@git-consulting.com"
			, "saskia.braam@aca-it.be"
			, "philip@wypoon.com"
			, "jeroen@trpoc.com"
			, "vandenhendemanon@gmail.com"
			, "christopher.leung@quadsolutions.nl"
			, "sabine@likewize.nl"
			, "Aamani@onesource.one"
			, "Baspels@sempersoneelsdiensten.nl"
			, "dave@teamict.nl"
			, "jade.dejong@stcorp.nl"
			, "a.yazar@just-it.io"
			, "iouahhoud@teksystems.com"
			, "djali.poppen@stcorp.nl"
			, "akel.jounes@recruitmytalent.co.uk"
			, "hassan.ali@neyoit.com"
			, "jvb.ext@brainbridge.be"
			, "joey@move-it-up.nl"
			, "george@intelligentsteps.co.uk"
			, "florine.vissers@xploregroup.be"
			, "fverdonk@vtwr.nl"
			, "Corina.Chicote@g2recruitment.com"
			, "joost@diqq.nl"
			, "rvangeel@quinnoah.nl"
			, "anthony@rgsconsulting.nl"
			, "divya.14t@gmail.com"
			, "marnix@mxld.nlv"
			, "Calebe.FERRARI@systemsolutions.com"
			, "hjanssen@powerprofit.nl"
			, "roebi.boer@peoplesrepublic.nl"
			, "Rick@kodastaff.com"
			, "Funda@intrameo.nl"
			, "jeroen@it4people.nl");

			
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
