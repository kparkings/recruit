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
		
		return switch(sendEmailCommand.getTopic()) {
			case ACCOUNT_CREATED 						-> loadTemplate("new-recruiter-signup.ftlh", model);
			case FORGOTTEN_PWD, WEEKLY_UPDATE	 		-> "";
			case ALERT_MATCHES 							-> loadTemplate("alert-matches.ftlh", model);
			case PASSWORD_RESET							-> loadTemplate("password-reset.ftlh", model);
			case REC_TO_REC_EMAIL_REPLY_NOTICICATION 	-> loadTemplate("rec-to-rec-reply-notification.ftlh", model);
			case OPEN_POSITION_CONTACT_REQUEST 			-> handleOpenPositionContactRequest(sendEmailCommand, model);
			case OFFERED_CANDIDATE_CONTACT_REQUEST 		-> handleOfferedCandidateContactRequest(sendEmailCommand, model);
			case LISTING_RECRUITER_CONTACT_REQUEST 		-> handleRecruiterContactRequest(sendEmailCommand, model);
			case REC_TO_REC_CONTACT_REQUEST 			-> handleRecToRecContactRequest(sendEmailCommand, model);
			case CANDIDATE_ACCOUNT_CREATED 				-> loadTemplate("new-candidate-signup.ftlh", model);
			case NEW_CREDITS_ASSIGNED					-> loadTemplate("new-credits-assigned.ftlh", model);
			case LISTING_MATCHING_ROLE					-> loadTemplate("listing-alert-hit.ftlh",model);
			case CANDIDATE_SUMMARY						-> loadTemplate("candidate-summary.ftlh", model);
			case MISSED_CHAT_MESSAGE					-> loadTemplate("missed-message.ftlh",model);
			default -> "Could not process template";
		};
		
	}
	
	/**
	* Returns correct template filles with model data
	* @param sendEmailCommand - Info about command to send email
	* @param model - Model to construct the email
	* @return returns the template filled with model details
	*/
	private String handleOpenPositionContactRequest(RequestSendEmailCommand sendEmailCommand, Map<String,Object> model) {
		if (sendEmailCommand.getEmailType() == EmailType.INTERN) {
			return loadTemplate("open-position-contact-request-intern.ftlh", model);
		}
		
		if (sendEmailCommand.getEmailType() == EmailType.EXTERN) {
			return loadTemplate("open-position-contact-request-extern.ftlh", model);
		}
		return "";
	}
	
	/**
	* Returns correct template filles with model data
	* @param sendEmailCommand - Info about command to send email
	* @param model - Model to construct the email
	* @return returns the template filled with model details
	*/
	private String handleOfferedCandidateContactRequest(RequestSendEmailCommand sendEmailCommand, Map<String,Object> model) {
		
		if (sendEmailCommand.getEmailType() == EmailType.INTERN) {
			return loadTemplate("offered-candidate-contact-request-intern.ftlh", model);
		}
		
		if (sendEmailCommand.getEmailType() == EmailType.EXTERN) {
			return loadTemplate("offered-candidate-contact-request-extern.ftlh", model);
		}
		
		return "";
	}
	
	/**
	* Returns correct template filles with model data
	* @param sendEmailCommand - Info about command to send email
	* @param model - Model to construct the email
	* @return returns the template filled with model details
	*/
	private String handleRecruiterContactRequest(RequestSendEmailCommand sendEmailCommand, Map<String,Object> model) {
		if (sendEmailCommand.getEmailType() == EmailType.INTERN) {
			return loadTemplate("listing-contact-intern.ftlh", model);
		}
		
		if (sendEmailCommand.getEmailType() == EmailType.EXTERN) {
			return loadTemplate("listing-contact-extern.ftlh", model);
		}
		
		return  "";
	}
	
	/**
	* Returns correct template filles with model data
	* @param sendEmailCommand - Info about command to send email
	* @param model - Model to construct the email
	* @return returns the template filled with model details
	*/
	private String handleRecToRecContactRequest(RequestSendEmailCommand sendEmailCommand, Map<String,Object> model) {
		if (sendEmailCommand.getEmailType() == EmailType.INTERN) {
			return loadTemplate("rec-to-rec-contact-request-intern.ftlh", model);
		}
		
		if (sendEmailCommand.getEmailType() == EmailType.EXTERN) {
			return loadTemplate("rec-to-rec-contact-request-extern.ftlh", model);
		}
		
		return ""; 
	}
	
	/**
	* Returns correct template filles with model data
	* @param sendEmailCommand - Info about command to send email
	* @param model - Model to construct the email
	* @return returns the template filled with model details
	*/
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