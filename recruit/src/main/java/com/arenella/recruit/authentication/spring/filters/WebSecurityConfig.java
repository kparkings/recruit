package com.arenella.recruit.authentication.spring.filters;

import org.apache.catalina.filters.RequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
/**
* Configuration for WebSecurity
* @author K Parkings
*/
public class WebSecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

	@Autowired
	private AuthenticationEntryPoint 	authenticationEntryPoint;
	
	@Autowired
	private UserDetailsService 			userDetailsService;
	
	@Autowired 
	private SecRequestFilter 			secRequestFilter;
	
	/**
	* Sets an Authentication Manager
	* @param auth 		Builder for Authentication
	* @throws Exception
	*/
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
	
	/**
	* Bean for encoding Passwords
	* @return Password Encoder
	*/
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	/**
	* AuthenticationManager bean 
	*/
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	/**
	* Refer to WebConfigurer service for details
	*/
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.csrf().disable()
		
			.authorizeRequests().antMatchers("/authenticate").permitAll() 
			.antMatchers(HttpMethod.OPTIONS,"/**").permitAll()
			.anyRequest().authenticated().and()
			.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
				.and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		http.addFilterBefore(secRequestFilter, UsernamePasswordAuthenticationFilter.class);
		
	}
	
}