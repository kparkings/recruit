package com.arenella.recruit.emailservice.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.arenella.recruit.emailservice.adapters.RequestSendEmailCommand;
import com.arenella.recruit.emailservice.beans.Email.EmailTopic;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
* Unit tests for the EmailTemplateFactory class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class EmailTemplateFactoryTest {

	@Mock
	private Configuration 			freemarkerTemplateConfigMock;
	
	@Mock
	private Template				templateMock;
	
	@InjectMocks
	private EmailTemplateFactory 	factory 						= new EmailTemplateFactory();
	
	@Test
	public void testEmailTemplateFactory() throws Exception{
		
		RequestSendEmailCommand command = RequestSendEmailCommand.builder().topic(EmailTopic.ACCOUNT_CREATED).build();
		
		Mockito.when(this.freemarkerTemplateConfigMock.getTemplate(Mockito.eq("new-recruiter-signup.ftlh"))).thenReturn(templateMock);
		
		factory.fetchTemplate(command);
		
		Mockito.verify(this.freemarkerTemplateConfigMock).getTemplate(Mockito.eq("new-recruiter-signup.ftlh"));
		
		
	}
	
}
