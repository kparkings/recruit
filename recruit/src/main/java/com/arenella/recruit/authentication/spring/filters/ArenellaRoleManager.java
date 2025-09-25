package com.arenella.recruit.authentication.spring.filters;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
* Component to centralise User role information. Creates a wrapper for the Spring 
* internal object that can be reused throughout the application without spring 
* security implementation specifics being directly called in the service layers
* @author K Parkings
*/
@Component
public class ArenellaRoleManager {

	/**
	* Roles are provided by SpringSecurity. We keep this internal to the Manager so we can swap out 
	* Spring if we need to in the future without affecting the rest of the cose
	* @param roleToCheck - Role we want the User to have
	* @return Whether the authenticated user has the desired role
	*/
	private boolean checkHasRole(String roleToCheck) {
		return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(role -> role.getAuthority().equals(roleToCheck));
	}
	
	/**
	* Whether the authenticated user has the role Administrator
	* @return Whether the authenticated user has the role Administrator
	*/
	public boolean isAdmin() {
		return this.checkHasRole("ROLE_ADMIN");
	}
	
	/**
	* Whether the authenticated user has the role Recruiter
	* @return Whether the authenticated user has the role Administrator
	*/
	public boolean isRecruiter() {
		return this.checkHasRole("ROLE_RECRUITER");
	}
	
	/**
	* Whether the authenticated user has the role Candidate
	* @return Whether the authenticated user has the role Administrator
	*/
	public boolean isCandidate() {
		return this.checkHasRole("ROLE_CANDIDATE");
	}
	
	//An idea for case where could be system. in case where it can be both use this overloaded version?
	public boolean isAdmin(boolean isSystem) {
		return isSystem ? false : this.checkHasRole("ROLE_ADMIN");
	}
	
	public boolean isRecruiter(boolean isSystem) {
		return isSystem ? false : this.checkHasRole("ROLE_RECRUITER");
	}
	
	public boolean isCandidate(boolean isSystem) {
		return isSystem ? false : this.checkHasRole("ROLE_CANDIDATE");
	}
	
}