package com.arenella.recruit.emailservice.services;

import java.io.StringWriter;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arenella.recruit.emailservice.adapters.RequestSendEmailCommand;
import com.arenella.recruit.emailservice.beans.Email.EmailType;

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
	public String fetchTemplate(RequestSendEmailCommand sendEmailCommand, Map<String,Object> model) {
		
		switch(sendEmailCommand.getTopic()) {
			case ACCOUNT_CREATED:{
				return loadTemplate("new-recruiter-signup.ftlh", model);
			}
			case FORGOTTEN_PWD:{
				return "";
			}
			case WEEKLY_UPDATE:{
				return "";
			}
			case ALERT_MATCHES:{
				return loadTemplate("alert-matches.ftlh", model);
			}
			case PASSWORD_RESET:{
				return loadTemplate("password-reset.ftlh", model);
			}
			case OPEN_POSITION_CONTACT_REQUEST:{
				if (sendEmailCommand.getEmailType() == EmailType.INTERN) {
					return loadTemplate("open-position-contact-request-intern.ftlh", model);
				}
				
				if (sendEmailCommand.getEmailType() == EmailType.EXTERN) {
					return loadTemplate("open-position-contact-request-extern.ftlh", model);
				}
			}
			case OFFERED_CANDIDATE_CONTACT_REQUEST:{
				if (sendEmailCommand.getEmailType() == EmailType.INTERN) {
					return loadTemplate("offered-candidate-contact-request-intern.ftlh", model);
				}
				
				if (sendEmailCommand.getEmailType() == EmailType.EXTERN) {
					return loadTemplate("offered-candidate-contact-request-extern.ftlh", model);
				}
			}
			case LISTING_RECRUITER_CONTACT_REQUEST:{
				
				if (sendEmailCommand.getEmailType() == EmailType.INTERN) {
					return loadTemplate("listing-contact-intern.ftlh", model);
				}
				
				if (sendEmailCommand.getEmailType() == EmailType.EXTERN) {
					return loadTemplate("listing-contact-extern.ftlh", model);
				}
				
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
