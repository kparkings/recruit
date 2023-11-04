package com.arenella.recruit.authentication.services;

import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.arenella.recruit.authentication.beans.User;
import com.arenella.recruit.authentication.beans.User.USER_ROLE;
import com.arenella.recruit.authentication.dao.UserDao;
import com.arenella.recruit.authentication.entity.UserEntity;
import com.arenella.recruit.authentication.enums.AccountType;

/**
* Services for interaction with User Accounts
* @author K Parkings
*/
@Service
public class AccountServiceImpl implements AccountService{

	@Autowired
	private UserDao userDao;
	
	/**
	* Refer to AccountService for details 
	*/
	@Override
	public User createAccount(String proposedUsername, AccountType accountType) {
		
		AtomicInteger 	duplicated 	= new AtomicInteger(1);
		String 			userName 	= proposedUsername;
		
		while (true) {
			
			if (!userDao.existsById(userName)) {
				
				final String password 			= this.generatePassword(userName);
				final String passwordEncoded 	= this.encryptPassword(password);
				
				User user = User.builder().enabled(true).username(userName).password(passwordEncoded).roles(getUserRoles(accountType)).build();
				
				userDao.save(UserEntity.convertToEntity(user));
				
				return User.builder().enabled(user.isEnabled()).username(user.getUsername()).password(password).roles(user.getRoles()).build(); 
			}
			
			userName = proposedUsername + duplicated.getAndIncrement();
			
		}
		
	}
	
	/**
	* Refer to AccountService for details 
	*/
	@Override
	public void replaceRolesForUser(String userId, Set<USER_ROLE> roles) {
		
		Optional<User>  userOpt = this.userDao.fetchUser(userId);
				
		User user = userOpt.orElseThrow(() -> new IllegalArgumentException("Cannot Switch AccountType for User: " + userId));
		
		user.replaceRoles(roles);
		
		userDao.save(UserEntity.convertToEntity(user));
		
	}
	
	/**
	*  Refer to AccountService for details 
	*/
	@Override
	public void createAccount(String userId, String encryptedPassword, AccountType accountType) {
		
		User user = User.builder().enabled(true).username(userId).password(encryptedPassword).roles(getUserRoles(accountType)).build();
				
		userDao.save(UserEntity.convertToEntity(user));
		
	}
	
	/**
	* Refer to AccountService for details 
	*/
	@Override
	public void updateUserPassword(String userId, String password) {
		
		User user = this.userDao.fetchUser(userId).orElseThrow(() -> new IllegalArgumentException("Unknown User Id."));
		
		user.setPassword(password);
		
		this.userDao.updateUser(user);
		
	}
	
	/**
	* Refer to AccountService for details 
	*/
	@Override
	public void deleteAccount(String userId) {
		if (this.userDao.existsById(userId)) {
			this.userDao.deleteById(userId);
		}
	}
	
	/**
	* Refer to AccountService for details 
	*/
	@Override
	public void updateUsersCreditStatus(String userId, boolean useCredits) {

		User user = this.userDao.fetchUser(userId).orElseThrow(() -> new IllegalArgumentException("Unknown User Id."));
		
		user.setUseCredits(useCredits);
		
		this.userDao.updateUser(user);
		
	}
	
	/**
	* Returns the roles associated with the AccountType
	* @param accountType - Type of account being created
	* @return Roles for the account
	*/
	private Set<USER_ROLE> getUserRoles(AccountType accountType){
		
		switch(accountType) {
			case RECRUITER: {
				return Set.of(USER_ROLE.recruiter);
			}
			case CANDIDATE: {
				return Set.of(USER_ROLE.candidate);
			}
			default: {return new HashSet<>();}
		} 
		
		
	}
	
	/**
	* Generates a password for the user
	* @param username - username of the user
	* @return password for the user
	*/
	private String generatePassword(String username) {
		
		Random x = new Random();
		
		final String 	code 		=	String.valueOf(x.nextInt()).substring(1,5);
		
		String passwordUnencoded = username + "!" + code;
		
		return passwordUnencoded;
		
		
	}
	
	private String encryptPassword(String passwordUnencoded) {
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		String password = encoder.encode(passwordUnencoded);
		
		return password;
		
	}

}