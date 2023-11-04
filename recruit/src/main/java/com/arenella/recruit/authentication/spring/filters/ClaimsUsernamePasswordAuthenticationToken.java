package com.arenella.recruit.authentication.spring.filters;

import java.util.Collection;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

/**
* Authentication token allowing for JWT claims
* @author K Parkings
*/
public class ClaimsUsernamePasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {

	private static final long serialVersionUID = 1688912560063527585L;

	private Claims claims = Jwts.claims();
	
	/**
	* Constructor
	* @param principal		- Inherited
	* @param credentials	- Inherited
	* @param claims			- Claim passed via JWT
	*/
	public ClaimsUsernamePasswordAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, Claims claims) {
		super(principal, credentials, authorities);
		this.claims.putAll(claims);
	}

	/**
	* If present returns the value of the requested claim
	* @param claim - claim wanted
	* @return if avalable the claim
	*/
	public Optional<Object> getClaim(String claim){
		return this.claims.containsKey(claim) ? Optional.ofNullable(this.claims.get(claim)) : Optional.empty();
	}
	
}