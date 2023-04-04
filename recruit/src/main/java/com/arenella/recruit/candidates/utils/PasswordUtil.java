package com.arenella.recruit.candidates.utils;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.arenella.recruit.candidates.beans.Candidate;

/**
* Utils for working with Passwords
* @author K Parkings
*/
public class PasswordUtil{

	public static final int PASSWORD_LENGTH = 16;
	
	private static Set<Character> chars = Set.of('!','@','$','%','-','W','D','S','s',':','1','8','9','+');
	
	/**
	* Generates a password for the user
	* @param username - username of the user
	* @return password for the user
	*/
	public static String generatePassword() {
		
		String passwordUnencoded =  "";
		
		while(passwordUnencoded.length() < PASSWORD_LENGTH) {
			passwordUnencoded = passwordUnencoded + getRandom();
		};
		
		return passwordUnencoded;
		
		
	}
	
	/**
	* Generates a random String
	* @return randomString
	*/
	private static String getRandom() {
		
		int randomInt = ThreadLocalRandom.current().nextInt(0, chars.size());
		
		return chars.toArray()[randomInt] +  (randomInt > 3 ? "R" : chars.toArray()[randomInt]+"");
		
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
	public static String generateUsername(Candidate candidate) {
		
		String	userName = candidate.getFirstname()+candidate.getCandidateId();
		
		return userName.toLowerCase();
		
	}
	
}
