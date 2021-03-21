package com.arenella.recruit.authentication.utils;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.servlet.http.Cookie;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
* Util for workig with JWT tokens
* @author K Parkings
*/
@Component
public class JwtTokenUtil implements Serializable{

	private static final long serialVersionUID = -9219101626775347703L;
	
	private static final int JWT_TOKEN_VALIDITY = 5 * 60 * 60;
	
	@Value("${jwt.secret}")
	private String secret;
	
	/**
	* Extracts the Username from the token
	* @param token - JWT Token
	* @return Username in the Token
	*/
	public String getUsernameFromToken(String token) {
		return getClaimsFromToken(token, Claims::getSubject);
	}
	
	/**
	* Extracts the ExpirationDate from the Token
	* @param token - JWT Token
	* @return returns the expirationDate in the Token
	*/
	public Date getExpirationDateFromToken(String token) {
		return getClaimsFromToken(token, Claims::getExpiration);
	}
	
	/**
	* Extracts the Claims from the Token
	* @param <T>			- Claims
	* @param token			- Token containing Claims
	* @param claimsResolver - Function to extract claims
	* @return Claims
	*/
	public <T> T getClaimsFromToken(String token, Function<Claims, T> claimsResolver){
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}
	
	/**
	* Returns all the Claims in the Token 
	* @param token - Token containing claims
	* @return claims
	*/
	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
	}
		
	/**
	* Returns whether or not the Token is expired
	* @param token - Token whose expiry is to be checked
	* @return whether or not the Token has expired
	*/
	public boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}
	
	/**
	* Generates a new Token for a User
	* @param userDetails - Details of User
	* @return Token for the User
	*/
	public String generateToken(UserDetails userDetails) {
		Map<String,Object> claims = new HashMap<>();
		return Jwts.builder().setClaims(claims)
							.setSubject(userDetails.getUsername())
							.setIssuedAt(new Date(System.currentTimeMillis()))
							.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
							.signWith(SignatureAlgorithm.HS512, secret).compact();
	}
	
	/**
	* Returns whether or not the Token is valid
	* @param token			- Token to Check
	* @param userDetails	- Details of the User
	* @return Whether or not the Token is valid
	*/
	public boolean validateToken(String token, UserDetails userDetails) {
		final String userName = this.getUsernameFromToken(token);
		return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
	
	/**
	* Wraps the Token in a Cookie
	* @param token - Token to be wrapped
	* @return wrapped Token
	*/
	public Cookie wrapInCookie(String token) {
		
		Cookie cookie = new Cookie("Authorization", token);
		cookie.setDomain("127.0.0.1");
		cookie.setMaxAge(500000);
		cookie.setSecure(false); //TODO: Change when we are live and using https (setup with profiles or application.properties)
		
		return cookie;
		
	}
	
}