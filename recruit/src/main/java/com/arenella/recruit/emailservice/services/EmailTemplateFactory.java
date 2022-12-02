package com.arenella.recruit.emailservice.services;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arenella.recruit.emailservice.adapters.RequestSendEmailEvent;

import freemarker.template.Configuration;

@Component
public class EmailTemplateFactory {

	@Autowired
	private Configuration freemarkerConfig;
	
	public String fetchTemplate(RequestSendEmailEvent event) {
		
		switch(event.getTopic()) {
			case ACCOUNT_CREATED:{
				return loadTemplate("new-recruiter-signup.ftlh", event.getModel());
			}
			case FORGOTTEN_PWD:{
				return "";
			}
			case WEEKLY_UPDATE:{
				return "";
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
