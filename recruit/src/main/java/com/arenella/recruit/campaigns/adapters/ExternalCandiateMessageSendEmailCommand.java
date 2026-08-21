package com.arenella.recruit.campaigns.adapters;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.arenella.recruit.emailservice.beans.Email.EmailRecipient;

/**
* Command to send Email to External Candidates for a Campaign  or Role 
*/
public class ExternalCandiateMessageSendEmailCommand {

	private Set<EmailRecipient<UUID>> 	recipients = new HashSet<>();
	private String 						message;
	private String 						recruiterName;
	private String 						recruiterCompany;
	private String 						recruiterEmail;
	private String 						campaignOrRole;
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	public ExternalCandiateMessageSendEmailCommand(ExternalCandiateMessageSendEmailCommandBuilder builder) {
		this.recipients.addAll(builder.recipients);
		this.message 			= builder.message;
		this.recruiterName 		= builder.recruiterName;
		this.recruiterCompany 	= builder.recruiterCompany;
		this.recruiterEmail 	= builder.recruiterEmail;
		this.campaignOrRole 	= builder.campaignOrRole;
	}
	
	/**
	* Returns the message body of the Email
	* @return contents of email
	*/
	public String getMessage() {
		return this.message;
	}
	
	/**
	* Returns the Recipients ( External Candidates ) the message is to 
	* be sent to
	* @return Recipients
	*/
	public Set<EmailRecipient<UUID>> getRecipients() {
		return this.recipients;
	}
	
	/**
	* Returns the name of the Recruiter sending the message
	* @return Name of the Recruiter
	*/
	public String getRecruiterName() {
		return this.recruiterName;
	}
	
	/**
	* Returns the name of the Recruiters company
	* @return company name
	*/
	public String getRecruiterCompany() {
		return this.recruiterCompany;
	}

	/**
	* Returns the email address of the Recruiter
	* sending the message
	* @return email address
	*/
	public String getRecruiterEmail() {
		return this.recruiterEmail;
	}

	/**
	* Returns the name of the Campaign or role the message is associated with
	* @return Name of the campaign or role
	*/
	public String getCampaignOrRole() {
		return this.campaignOrRole;
	}

	/**
	* Returns a Builder for the class
	* @return Builder
	*/
	public static ExternalCandiateMessageSendEmailCommandBuilder builder() {
		return new ExternalCandiateMessageSendEmailCommandBuilder();
	}
	
	/**
	* Builder for the Class 
	*/
	public static class ExternalCandiateMessageSendEmailCommandBuilder{
		
		private Set<EmailRecipient<UUID>> 	recipients = new HashSet<>();
		private String 						message;
		private String 						recruiterName;
		private String 						recruiterCompany;
		private String 						recruiterEmail;
		private String 						campaignOrRole;
		
		/**
		* Sets the Recipeints of the Message
		* @param recipients - External candidates to send the message to
		* @return Builder
		*/
		public ExternalCandiateMessageSendEmailCommandBuilder recipients(Set<EmailRecipient<UUID>> recipients) {
			this.recipients.clear();
			this.recipients.addAll(recipients);
			return this;
		}
		
		/**
		* Sets the message to send to the External Candidates
		* @param message - Message to send
		* @return Builder
		*/
		public ExternalCandiateMessageSendEmailCommandBuilder message(String message) {
			this.message = message;
			return this;
		}
		
		/**
		* Sets the name of the recruiter sending the message
		* @param recruiterName - Name of the Recruiter
		* @return Builder
		*/
		public ExternalCandiateMessageSendEmailCommandBuilder recruiterName(String recruiterName) {
			this.recruiterName = recruiterName;
			return this;
		}
		
		/**
		* Sets the name of the Company the Recruiter works for
		* @param recruiterCompany - Company name
		* @return Builder
		*/
		public ExternalCandiateMessageSendEmailCommandBuilder recruiterCompany(String recruiterCompany) {
			this.recruiterCompany = recruiterCompany;
			return this;
		}
		
		/**
		* Sets the email address of the Recruiter sending the Message
		* @param recruiterEmail - Email address of the Recruiter 
		* @return Builder
		*/
		public ExternalCandiateMessageSendEmailCommandBuilder recruiterEmail(String recruiterEmail) {
			this.recruiterEmail = recruiterEmail;
			return this;
		}
		
		/**
		* 
		* @param campaignOrRole
		* @return
		*/
		public ExternalCandiateMessageSendEmailCommandBuilder campaignOrRole(String campaignOrRole) {
			this.campaignOrRole = campaignOrRole;
			return this;
		}
		
		/**
		* Returns an initialized instance of the Class
		* @return Initialized instance
		*/
		public ExternalCandiateMessageSendEmailCommand build() {
			return new ExternalCandiateMessageSendEmailCommand(this);
		}
		
	}
	
}