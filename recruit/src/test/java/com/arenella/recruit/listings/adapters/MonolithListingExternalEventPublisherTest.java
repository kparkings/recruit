package com.arenella.recruit.listings.adapters;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.arenella.recruit.curriculum.adapters.CurriculumExternalEventListener;
import com.arenella.recruit.emailservice.adapters.EmailServiceExternalEventListener;
import com.arenella.recruit.emailservice.adapters.RequestSendEmailCommand;
import com.arenella.recruit.emailservice.beans.Email.EmailType;
import com.arenella.recruit.listings.services.FileSecurityParser.FileType;

/**
* Unit tests for the MonolithListingExternalEventPublisher class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
class MonolithListingExternalEventPublisherTest{

	@Mock
	private CurriculumExternalEventListener 			mockCurriculumEventListener;

	@Mock
	private EmailServiceExternalEventListener 			mockEmailServiceListener;
	
	@InjectMocks
	private MonolithListingExternalEventPublisher 		publisher;
	
	/**
	* Tests both and External and Internal command are sent 
	*/
	@Test
	void testPublicRequestSendListingContactEmailCommand() {
		
		final String 			title 			= "Java Developer";
		final String 			ownerId			= "kparkings001";
		final String			message			= "dear recruiter blah blah";
		final String 			senderName		= "Kevin Parkings";
		final String			senderEmail		= "admin@arenella-ict.com";
		final byte[]			attachmentBytes	= new byte[] {1,22,3};
		final FileType			fileType		= FileType.doc;
		
		ArgumentCaptor<RequestSendEmailCommand> capt = ArgumentCaptor.forClass(RequestSendEmailCommand.class);
		
		RequestListingContactEmailCommand command = 
				RequestListingContactEmailCommand
					.builder()
						.file(attachmentBytes)
						.fileType(fileType.toString())
						.listingName(title)
						.message(message)
						.recruiterId(ownerId)
						.senderEmail(senderEmail)
						.senderName(senderName)
					.build();
		
		Mockito.doNothing().when(this.mockEmailServiceListener).listenForSendEmailCommand(capt.capture());
		
		this.publisher.publicRequestSendListingContactEmailCommand(command);
		
		Mockito.verify(this.mockEmailServiceListener, Mockito.times(2)).listenForSendEmailCommand(Mockito.any(RequestSendEmailCommand.class));
		
		capt.getAllValues().stream().filter(e -> e.getEmailType() == EmailType.INTERN).findAny().orElseThrow();
		capt.getAllValues().stream().filter(e -> e.getEmailType() == EmailType.EXTERN).findAny().orElseThrow();
		
	}
}
