package com.arenella.recruit.emailservice.services;

import java.io.StringWriter;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arenella.recruit.emailservice.adapters.RequestSendEmailCommand;

import freemarker.template.Configuration;

/**
* Factory loads the correct template based opon an Email's topic ID
* @author K Parkings
*/
@Component
public class EmailTemplateFactory {

	@Autowired
	private Configuration freemarkerConfig;
	
	/**
	* 
	* @param sendEmailCommand
	* @return Compiled Template as String
	*/
	public String fetchTemplate(RequestSendEmailCommand sendEmailCommand) {
		
		switch(sendEmailCommand.getTopic()) {
			case ACCOUNT_CREATED:{
				return loadTemplate("new-recruiter-signup.ftlh", sendEmailCommand.getModel());
			}
			case FORGOTTEN_PWD:{
				return "";
			}
			case WEEKLY_UPDATE:{
				return "";
			}
			case ALERT_MATCHES:{
				return loadTemplate("alert-matches.ftlh", sendEmailCommand.getModel());
			}
			case PASSWORD_RESET:{
				return loadTemplate("password-reset.ftlh", sendEmailCommand.getModel());
			}
			default:{
				return "Could not process template";
			}
		}
		
	}
	
	private String loadTemplate(String templateName, Map<String,Object> model) {
		StringWriter stringWriter = new StringWriter();
		
		try {
			this.freemarkerConfig.getTemplate(templateName).process(model, stringWriter);
		}catch(Exception e) {
			return "Could not process template";
		}
		return stringWriter.getBuffer().toString();
	}
	
}
