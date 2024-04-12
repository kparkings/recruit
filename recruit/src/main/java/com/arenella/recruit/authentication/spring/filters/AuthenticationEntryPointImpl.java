package com.arenella.recruit.authentication.spring.filters;

import java.io.IOException;
import java.io.Serializable;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
* Ensures correct response is sent to User if Authentication attemt fails
* @author K Parkings
*/
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint, Serializable {

	private static final long serialVersionUID = 85134467927693534L;

	/**
	* Refer to AuthenticationEntryPointInterface for details
	*/
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
	}

}