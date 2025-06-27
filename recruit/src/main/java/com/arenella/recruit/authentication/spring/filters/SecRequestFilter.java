package com.arenella.recruit.authentication.spring.filters;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.arenella.recruit.authentication.utils.JwtTokenUtil;

import io.jsonwebtoken.Claims;

/**
* Performs authentication of each incomming request
* @author K Parkings
*/
@Component
public class SecRequestFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtTokenUtil 		jwtTokenUtil;
	
	/**
	* Refer to the OncePerRequestFilter for details 
	*/
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		
		/**
		* We want to bypass the filter and let the request arrive at the endpoint for authentication
		*/
		if (request.getRequestURI().equals("/authenticate")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		/**
		* We want to allow the stats url to be accessed before authentication
		*/
		if (request.getRequestURI().equals("/candidate/stats/total-active")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		/**
		* We want to allow the listings url to be accessed before authentication
		*/
		if (request.getRequestURI().startsWith("/listing/public/")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		/**
		* We want to allow the stats url to be accessed before authentication
		*/
		if (request.getRequestURI().equals("/candidate/public/search-history")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		
		
		/**
		* We want to allow the listings url to be accessed before authentication
		*/
		if (request.getRequestURI().startsWith("/public/listing-alert")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		/**
		* We want to allow the public candidate urls to be accessed before authentication
		*/
		if (request.getRequestURI().startsWith("/public/candidate")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		/**
		* We want to allow the listings url to be accessed before authentication
		*/
		if (request.getRequestURI().startsWith("/public/recruiter")) {
			filterChain.doFilter(request, response);
			return;
		}
		

		/**
		* We want to allow the chart for candidates function url to be accessed before authentication
		*/
		if (request.getRequestURI().startsWith("/candidate/public/function-count")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		/**
		* TODO: [KP] Static data. Though not a big concern ideally this should 
		* be called only after authentication but we are initializing the service 
		* in angular before authentication so this needs to be public until a 
		* better solution can be found
		*/
		if (request.getRequestURI().startsWith("/candidate/countries")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		/**
		* TODO: [KP] Static data. Though not a big concern ideally this should 
		* be called only after authentication but we are initializing the service 
		* in angular before authentication so this needs to be public until a 
		* better solution can be found
		*/
		if (request.getRequestURI().startsWith("/candidate/geo-zone")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		/**
		* TODO: [KP] Static data. Though not a big concern ideally this should 
		* be called only after authentication but we are initializing the service 
		* in angular before authentication so this needs to be public until a 
		* better solution can be found
		*/
		if (request.getRequestURI().startsWith("/candidate/languages")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		
		
		/**
		* We want to allow Candidates to upload their CVs before authentication
		*/
		if (request.getRequestURI().equals("/pending-curriculum") || (request.getRequestURI().equals("/pending-candidate") && request.getMethod().equalsIgnoreCase("POST"))) {
			filterChain.doFilter(request, response);
			return;
		}
		
		if (!request.getMethod().equals("OPTIONS")) {
			
			/**
			* Expecting Cookie Containing Token. Of not Cookie the request fails
			*/
			if (request.getCookies() == null ) {
				throw new BadCredentialsException("No token");
			}
					
			Arrays.asList(request.getCookies()).forEach(cookie -> {
				
				if (cookie.getName().equals("Authorization")) {
					
					String token = cookie.getValue();
				
					if (this.jwtTokenUtil.isTokenExpired(token)) {
						throw new BadCredentialsException("Token expired");
					}
					
					String username = this.jwtTokenUtil.getUsernameFromToken(token);
					
					Set<String> roleNames = this.jwtTokenUtil.getRoles(token);
					
					Claims claims = this.jwtTokenUtil.getAllClaimsFromToken(token);
					
					List<GrantedAuthority> roles = roleNames.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
					
					ClaimsUsernamePasswordAuthenticationToken  authToken = new ClaimsUsernamePasswordAuthenticationToken(username, "", roles, claims);
					
					SecurityContextHolder.getContext().setAuthentication(authToken);
					
				}
			}); 
			
		}
		
		filterChain.doFilter(request, response);
		
	}

}