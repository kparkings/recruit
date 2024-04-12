package com.arenella.recruit.authentication.spring.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;

import jakarta.servlet.http.HttpServletResponse;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
/**
* Configuration for WebSecurity
* @author K Parkings
*/
//public class WebSecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {
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
	
	/**
	* AuthenticationManager bean 
	*/
	//@Bean
	//@Override
	//public AuthenticationManager authenticationManagerBean() throws Exception {
	//return super.authenticationManagerBean();
	//}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) 
	         throws Exception {
	    return authenticationConfiguration.getAuthenticationManager();
	}
	
	/**
	* Refer to WebConfigurer service for details
	*/
	//@Override
	//protected void configure(HttpSecurity http) throws Exception {
		
	//	http.csrf().disable();
		
	//	http.headers().frameOptions().disable();
		
	//	http
	//		.authorizeRequests()
	//		.antMatchers("/api/candidate/stats/total-active").permitAll()
	//		.antMatchers("/candidate/stats/total-active").permitAll()
	//		.antMatchers("/api/authenticate").permitAll() 
	//		.antMatchers("/authenticate").permitAll() 
	//		.antMatchers("/pending-curriculum").permitAll()
	//		.antMatchers("/pending-candidate").permitAll()
	//		.antMatchers("/listing/public/**").permitAll()
	//		.antMatchers("/public/recruiter").permitAll()
	//		.antMatchers("authenticate").permitAll()
	//		.antMatchers("/public/listing-alert").permitAll()
	//		.antMatchers("/public/listing-alert/**").permitAll()
	//		.antMatchers("/public/candidate/**").permitAll()
	//		.antMatchers("/candidate/countries").permitAll()
	//		.antMatchers("/candidate/geo-zone").permitAll()
	//		.antMatchers("/candidate/languages").permitAll()
	//		.antMatchers(HttpMethod.OPTIONS,"/**").permitAll()
	//		.anyRequest().authenticated().and()
	//		.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
	//			.and()
	//		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	//	
	//	http.addFilterBefore(secRequestFilter, UsernamePasswordAuthenticationFilter.class);
	//	
	//}
	
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		
		
		
		//http.cors(Customizer.withDefaults());
		//http.cors().disable();
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
            	
            	
            	.requestMatchers(new AntPathRequestMatcher("/v1/open-position/**")).permitAll()
            	.requestMatchers(new AntPathRequestMatcher("/candidate/**")).permitAll()
            	
            	
            	
            	.anyRequest().authenticated()
        	);
		
		http.addFilterBefore(secRequestFilter, UsernamePasswordAuthenticationFilter.class);
		
		http.cors(cors -> cors.configurationSource(request -> {
			
			String origin = request.getHeader("Origin");
			
	        CorsConfiguration configuration = new CorsConfiguration();
	        configuration.setAllowedOrigins(Arrays.asList(origin));
		  //  if ("OPTIONS".equals(request.getMethod())) {
		  //      response.setStatus(HttpServletResponse.SC_OK);
		  //  }
	        
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
