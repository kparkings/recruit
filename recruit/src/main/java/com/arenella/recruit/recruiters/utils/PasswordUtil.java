package com.arenella.recruit.recruiters.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.Random;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.arenella.recruit.recruiters.beans.Recruiter;

/**
* Utils for working with Passwords
* @author K Parkings
*/
public class PasswordUtil{

	/**
	* Generates a password for the user
	* @param username - username of the user
	* @return password for the user
	*/
	public static String generatePassword(String username) {
		
		Random x = new Random();
		
		final String 	code 		=	String.valueOf(x.nextInt()).substring(1,5);
		
		String passwordUnencoded = username + "!" + code;
		
		return passwordUnencoded;
		
		
	}
	
	/**
	* Performs encryption of a password
	* @param passwordUnencoded - raw password to encode
	* @return encoded version of the password
	*/
	public static String encryptPassword(String passwordUnencoded) {
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		String password = encoder.encode(passwordUnencoded);
		
		return password;
		
	}
	
	/**
	* Generates a username for the recruiter
	* @param recruiter - Contains information about the recruiter
	* @return username for the recruiter
	*/
	public static String generateUsername(Recruiter recruiter) {
		
		LocalDateTime 	startOfyear 	= LocalDateTime.of(LocalDateTime.now().getYear(), 1,1,0,0);
		long 			hours 			= ChronoUnit.HOURS.between(startOfyear, LocalDateTime.now());
		WeekFields 		weekFields 		= WeekFields.of(Locale.getDefault());
		int 			weekNumber 		= LocalDate.now().get(weekFields.weekOfYear());
		
		String userName = "";
		
		if (recruiter.getFirstName().length() > 5) {
			userName = recruiter.getFirstName().substring(0,5);
		} else {
			userName = recruiter.getFirstName();
		}
		
		userName = userName + weekNumber + recruiter.getSurname().substring(0,1).toUpperCase() + hours;
		
		return userName.toLowerCase();
		
	}
	
}