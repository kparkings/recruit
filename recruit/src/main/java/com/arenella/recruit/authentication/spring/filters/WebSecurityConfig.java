package com.arenella.recruit.authentication.spring.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
/**
* Configuration for WebSecurity
* @author K Parkings
*/
public class WebSecurityConfig {


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
		
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) 
	         throws Exception {
	    return authenticationConfiguration.getAuthenticationManager();
	}
	
	@SuppressWarnings("removal")
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable);
		http.headers((headers) -> headers.frameOptions().disable());
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        
		http
            .authorizeHttpRequests((authz) -> authz
            	.requestMatchers(new AntPathRequestMatcher("/api/candidate/stats/total-active")).permitAll()
            	.requestMatchers(new AntPathRequestMatcher("/candidate/stats/total-active")).permitAll()
            	.requestMatchers(new AntPathRequestMatcher("/api/authenticate")).permitAll()
            	.requestMatchers(new AntPathRequestMatcher("/authenticate")).permitAll()
            	.requestMatchers(new AntPathRequestMatcher("/pending-curriculum")).permitAll()
            	.requestMatchers(new AntPathRequestMatcher("/pending-candidate")).permitAll()
            	.requestMatchers(new AntPathRequestMatcher("/listing/public/**")).permitAll()
            	.requestMatchers(new AntPathRequestMatcher("/public/recruiter")).permitAll()
            	.requestMatchers(new AntPathRequestMatcher("authenticate")).permitAll()
            	.requestMatchers(new AntPathRequestMatcher("/public/listing-alert")).permitAll()
            	.requestMatchers(new AntPathRequestMatcher("/public/listing-alert/**")).permitAll()
            	.requestMatchers(new AntPathRequestMatcher("/public/candidate/**")).permitAll()
            	.requestMatchers(new AntPathRequestMatcher("/candidate/countries")).permitAll()
            	.requestMatchers(new AntPathRequestMatcher("/candidate/geo-zone")).permitAll()
            	.requestMatchers(new AntPathRequestMatcher("/candidate/languages")).permitAll()
            	.requestMatchers(new AntPathRequestMatcher("/public/listing-alert/**")).permitAll()
            	.anyRequest().authenticated()
        	);
		
		http.addFilterBefore(secRequestFilter, UsernamePasswordAuthenticationFilter.class);
		
		http.cors(cors -> cors.configurationSource(request -> {
			
			String origin = request.getHeader("Origin");
			
	        CorsConfiguration configuration = new CorsConfiguration();
	        configuration.setAllowedOrigins(Arrays.asList(origin));
		    
	        configuration.setAllowedMethods(Arrays.asList("POST", "GET","PUT", "OPTIONS", "DELETE"));
	        configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Accept", "X-Requested-With", "remember-me", "responseType"));
	        configuration.setExposedHeaders(Arrays.asList("X-Arenella-Request-Id"));
	        configuration.setMaxAge((long) 3600);
	        configuration.setAllowCredentials(true);
	        return configuration;
	    }));
		
		
        return http.build();
    }
	
	
	
}
