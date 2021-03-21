package com.arenella.recruit.authentication.spring.filters;

import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.arenella.recruit.authentication.utils.JwtTokenUtil;


@Component
public class SecRequestFilter extends OncePerRequestFilter {

	@Autowired
	private UserDetailsService 	userDetailsService;
	
	@Autowired
	private JwtTokenUtil 		jwtTokenUtil;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		
		/**
		* We want to bypass the filter and let the request arrive at the endpoint for authentication
		*/
		if (request.getRequestURI().equals("/authenticate")) {
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
					
					//TODO: Check if Token comes form us. There is a check for this.
					
					UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
					
					usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
					//UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, null);
					
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
					
				}
			}); 
			
		}
		
		filterChain.doFilter(request, response);
		
	}

}
