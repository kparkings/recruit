package com.arenella.recruit.authentication.entity;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import com.arenella.recruit.authentication.beans.User;
import com.arenella.recruit.authentication.beans.User.USER_ROLE;

/**
* Entity version of the User
* @author K Parkings
*/
@Entity
@Table(schema="users", name="users")
public class UserEntity {

	@Id
	@Column(name="username")
	private String 				username;
	
	@Column(name="password")
	private String 				password;
	
	@Column(name="enabled")
	private boolean 			enabled;
	
	@Enumerated(EnumType.STRING)
	@ElementCollection(targetClass=USER_ROLE.class, fetch=FetchType.EAGER)
	@CollectionTable(schema="users", name="user_roles", joinColumns=@JoinColumn(name="username"))
	@Column(name="role")
	private Set<USER_ROLE> 		roles		= new LinkedHashSet<>();
	
	/**
	*  Default constructor
	*/
	public UserEntity() {
		//For Hibernate
	}
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	public UserEntity(UserEntityBuilder builder) {
		
		this.username = builder.username;
		this.password = builder.password;
		this.enabled	= builder.enabled;
		this.roles.addAll(builder.roles);
		
	}
	
	/**
	* Returns the Users username which is also the Unique Id of the User
	* @return User's Username
	*/
	public String getUsername() {
		return username;
	}
	
	/**
	* Returns the User's password
	* @return User's password
	*/
	public String getPassword() {
		return password;
	}
	
	/**
	* Returns whether or not the User's account is 
	* active
	* @return Whether the users account is enabled
	*/
	public boolean isEnabled() {
		return enabled;
	}
	
	/**
	* Returns the Roles assigned to the User
	* @return User's roles in the System
	*/
	public Set<USER_ROLE> getRoles() {
		return roles;
	}
	
	/**
	* Updates the users password
	* @param password - Password of the User
	*/
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	* Returns a builder for the UserEntity class
	* @return Builder
	*/
	public static UserEntityBuilder builder() {
		return new UserEntityBuilder();
	}
	
	/**
	* Builder for the UserEntity class
	* @author K Parkings
	*/
	public static class UserEntityBuilder {
		
		private String 				username;
		private String 				password;
		private boolean 			enabled;
		private Set<USER_ROLE> 		roles		= new LinkedHashSet<>();
		
		/**
		* Sets the Username of the User (also the unique id)
		* @param username - The Users username
		* @return Builder
		*/
		public UserEntityBuilder username(String username) {
			this.username = username;
			return this;
		}
		
		/**
		* Sets the users password
		* @param password - The user's password
		* @return Builder
		*/
		public UserEntityBuilder password(String password) {
			this.password = password;
			return this;
		}
		
		/**
		* Sets whether or not the User account is 
		* active in the system
		* @param enabled - Whether the User's account is active
		* @return Builder
		*/
		public UserEntityBuilder enabled(boolean enabled) {
			this.enabled = enabled;
			return this;
		}
		
		/**
		* Adds the Roles assigned to the User
		* @param roles - Users Roles in the System
		* @return Builder
		*/
		public UserEntityBuilder roles(Set<USER_ROLE> roles) {
			this.roles.clear();
			this.roles.addAll(roles);
			return this;
		}
		
		/**
		* Returns a new Instance of the UserEntity class initialized with the 
		* values in the builder
		* @return Initialized UserEntity instance
		*/
		public UserEntity build() {
			return new UserEntity(this);
		}
		
	}
	
	/**
	* Converts from Domain version of User to Entity version
	* @param user - User to convert from
	* @return Entity version of User
	*/
	public static UserEntity convertToEntity(User user) {
		
		return UserEntity
					.builder()
						.username(user.getUsername())
						.password(user.getPassword())
						.enabled(user.isEnabled())
						.roles(user.getRoles())
						.build();
	}
	
	/**
	* Converts from Entity version of UserEntity to User version
	* @param userEntity - Entity to convert from
	* @return Domain version of User
	*/
	public static User convertFromEntity(UserEntity userEntity) {

		return User
				.builder()
					.username(userEntity.getUsername())
					.password(userEntity.getPassword())
					.enabled(userEntity.isEnabled())
					.roles(userEntity.getRoles())
					.build();
	}
	
}