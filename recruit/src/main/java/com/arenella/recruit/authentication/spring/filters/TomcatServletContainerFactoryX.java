package com.arenella.recruit.authentication.spring.filters;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.ajp.AjpNioProtocol;
import org.springframework.stereotype.Component;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;

@Component
public class TomcatServletContainerFactoryX {

	@Bean
	public TomcatServletWebServerFactory servletContainer() {
	      
		TomcatServletWebServerFactory 	tomcat 			= new TomcatServletWebServerFactory();
		Connector 						ajpConnector 	= new Connector("org.apache.coyote.ajp.AjpNioProtocol");
		AjpNioProtocol 					protocol		= (AjpNioProtocol)ajpConnector.getProtocolHandler();
	      
		protocol.setSecret("arenella2022NW1");
		protocol.setSecretRequired(true);
		
		ajpConnector.setPort(9090);
		ajpConnector.setSecure(false);
		ajpConnector.setScheme("http");
		
		tomcat.addAdditionalTomcatConnectors(ajpConnector);
	      
		return tomcat;
	  
	 }
	
}
