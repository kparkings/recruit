package com.arenella.recruit.authentication.spring.filters;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
* Filter to add CORS headers to each request
* @author K Parkings
*/
@Component
public class SimpleCORSFilter extends OncePerRequestFilter {

	/**
	* Filter to add CORS headers for the current request
	*/
	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
		
	    HttpServletRequest request = (HttpServletRequest) req;
	    HttpServletResponse response = (HttpServletResponse) res;
	
	    String origin = request.getHeader("Origin");
	    
	    response.setHeader("Access-Control-Allow-Origin", 				origin);
	    response.setHeader("Access-Control-Allow-Credentials", 			"true");
	    response.setHeader("Access-Control-Allow-Methods", 				"POST, GET,PUT, OPTIONS, DELETE");
	    response.setHeader("Access-Control-Max-Age", 					"3600");
	    response.setHeader("Access-Control-Allow-Headers", 				"Content-Type, Accept, X-Requested-With, remember-me, responseType");
	    response.setHeader("Access-Control-Expose-Headers", 			"X-Arenella-Request-Id");
		
	    if ("OPTIONS".equals(request.getMethod())) {
	        response.setStatus(HttpServletResponse.SC_OK);
	    }
	    
	    chain.doFilter(req, res);	
	}
}