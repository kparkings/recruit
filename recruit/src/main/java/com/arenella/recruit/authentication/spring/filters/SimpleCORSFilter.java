package com.arenella.recruit.authentication.spring.filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
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
	    response.setHeader("Access-Control-Allow-Methods", 				"POST, GET, OPTIONS, DELETE");
	    response.setHeader("Access-Control-Max-Age", 					"3600");
	    response.setHeader("Access-Control-Allow-Headers", 				"Content-Type, Accept, X-Requested-With, remember-me, responseType");
	
	    if ("OPTIONS".equals(request.getMethod())) {
	        response.setStatus(HttpServletResponse.SC_OK);
	    }
	    
	    chain.doFilter(req, res);
	}
}