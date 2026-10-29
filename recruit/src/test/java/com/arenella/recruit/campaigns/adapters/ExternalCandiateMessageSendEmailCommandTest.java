package com.arenella.recruit.campaigns.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.emailservice.beans.Email.EmailRecipient;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient.ContactType;

/**
* Unit tests for the ExternalCandiateMessageSendEmailCommand class 
*/
class ExternalCandiateMessageSendEmailCommandTest {

	private static final EmailRecipient<UUID> 	RECIPIENT = new EmailRecipient<>(UUID.randomUUID(), "candidateExt1", ContactType.UNREGISTERED_USER);
	private static final String 				MESSAGE 			= "Message body";
	private static final String 				RECRUITER_NAME 		= "John doe";
	private static final String 				RECRUITER_COMPANY 	= "Boop Recruitment";
	private static final String 				RECRUITER_EMAIL 	= "booprec@recboop.bp";
	private static final String 				CAMPAIGN_OR_ROLE 	= "JD0010A Senior Java Developer";
	
	/**
	* Test construction via a Builder 
	*/
	@Test
	void testConstruction() {
		 
		ExternalCandiateMessageSendEmailCommand command = ExternalCandiateMessageSendEmailCommand
				.builder()
				.recipients(Set.of(RECIPIENT))
				.message(MESSAGE)
				.recruiterName(RECRUITER_NAME)
				.recruiterCompany(RECRUITER_COMPANY)
				.recruiterEmail(RECRUITER_EMAIL)
				.campaignOrRole(CAMPAIGN_OR_ROLE)
				.build();
		 
		assertEquals(1, command.getRecipients().size());
		
		assertEquals(MESSAGE, 			command.getMessage());
		assertEquals(RECRUITER_NAME, 	command.getRecruiterName());
		assertEquals(RECRUITER_COMPANY, command.getRecruiterCompany());
		assertEquals(RECRUITER_EMAIL, 	command.getRecruiterEmail());
		assertEquals(CAMPAIGN_OR_ROLE, 	command.getCampaignOrRole());
		 
	}
	
}
