package com.arenella.recruit.authentication.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.arenella.recruit.authentication.beans.User;
import com.arenella.recruit.authentication.entity.UserEntity;

/**
* Entity version of a Use in the System
* @author K Parkings
*/
public interface UserDao extends CrudRepository<UserEntity, String>{

	/**
	* Fetches the User matching the username
	* @param username - id of the User 
	* @return User matching the username
	*/
	public default Optional<User> fetchUser(String username) {
		
		Optional<UserEntity> userEntity =  this.findById(username);
		
		if (userEntity.isEmpty()) {
			return Optional.empty();
		}
		
		User user = UserEntity.convertFromEntity(userEntity.get());
		
		return Optional.of(user);
		
	}
	
	/**
	* Updates a User
	* @param user - User to update
	*/
	//TODO: [KP] Currently only updates password. Need to add Roles and enabled later
	public default void updateUser(User user) {
		
		UserEntity entity = this.findById(user.getUsername()).orElseThrow(()-> new IllegalArgumentException("No Such User"));
		
		entity.setPassword(user.getPassword());
		
		this.save(entity);
		
	}
		
}