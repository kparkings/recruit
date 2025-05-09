package com.arenella.recruit.candidates.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
* Util to determin the authenticated Users roles and userId 
*/
@Component
public class ArenellaSecurityUtil {
			
		/**
		* Returns whether or no the User has the ADMIN role
		* @return Whether or no the User has the ADMIN role
		*/
		public boolean isAdmin() {
			return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().filter(role -> role.getAuthority().equals("ROLE_ADMIN")).findAny().isPresent();
		}
		
		/**
		* Returns whether or no the User has the CANDIDATE role
		* @return Whether or no the User has the CANDIDATE role
		*/
		public boolean isCandidate() {
			return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().filter(role -> role.getAuthority().equals("ROLE_ADMIN")).findAny().isPresent();
		}
		
		/**
		* Returns whether or no the User has the RECRUITER role
		* @return Whether or no the User has the RECRUITER role
		*/
		public boolean isRecruiter() {
			return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().filter(role -> role.getAuthority().equals("ROLE_ADMIN")).findAny().isPresent();
		}
		
		/**
		* Returns the unique identifier of the User
		* @return id of the User
		*/
		public String getUserId() {
			return SecurityContextHolder.getContext().getAuthentication().getName();
		}
	}