package com.arenella.recruit.authentication.spring.filters;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
* Unit tests for the ArenellaRoleManager class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class ArenellaRoleManagerTest {

	@Mock
	private SecurityContext							mockSecurityContext;
	
	@Mock
	private Authentication							mockAuthentication;
	
	@InjectMocks
	private ArenellaRoleManager						roleManager;
	
	/**
	* Sets up test environment 
	*/
	@BeforeEach
	public void init() throws Exception{
		SecurityContextHolder.setContext(mockSecurityContext);
	}
	
	/**
	* Tests whether authenticated user has Admin role
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testIsAdmin() throws Exception{
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		
		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		
		assertTrue(roleManager.isAdmin());
		
		authorities.clear();
		
		assertFalse(roleManager.isAdmin());
		
	}
	
	/**
	* Tests whether authenticated user has User role
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testIsRecruiter() throws Exception{
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_RECRUITER"));
		
		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		
		assertTrue(roleManager.isRecruiter());
		
		authorities.clear();
		
		assertFalse(roleManager.isRecruiter());
		
	}
	
	/**
	* Tests whether authenticated user has Candidate role
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testIsCandidate() throws Exception{
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_CANDIDATE"));
		
		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		
		assertTrue(roleManager.isCandidate());
		
		authorities.clear();
		
		assertFalse(roleManager.isCandidate());
		
	}
}
