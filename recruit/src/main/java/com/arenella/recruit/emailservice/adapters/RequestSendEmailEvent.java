package com.arenella.recruit.emailservice.adapters;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.arenella.recruit.emailservice.beans.Email.EmailTopic;
import com.arenella.recruit.emailservice.beans.Email.EmailType;
import com.arenella.recruit.emailservice.beans.Email.Recipient;
import com.arenella.recruit.emailservice.beans.Email.Sender;

/**
* Event requesting that an Email is Sent
* @author K Parkings
*/
//TODO: [KP] For now Classes are in shared folder. In MS implementation these will be Json String and 
//			 the class definitions can be declared in each service
public class RequestSendEmailEvent {

	private String 				title;
	private EmailType 			emailType;
	private Sender<?> 			sender; 
	private Set<Recipient<?>> 	recipients					= new LinkedHashSet<>(); 
	
	private EmailTopic 			topic;
	private Map<String,Object>	model						= new HashMap<>();
	
	/**
	* Returns the title for the Email
	* @return Email title
	*/
	public String getTitle() {
		return this.title;
	}
	
	/**
	* Returns the type of the Email
	* @return Email type
	*/
	public EmailType getEmailType() {
		return this.emailType;
	}
	
	/**
	* Returns details of the Sender of the Email
	* @return Sender
	*/
	public Sender<?> getSender() {
		return this.sender;
	}
	
	/**
	* Returns details of the Recipient(s) of the Email
	* @return Recipients
	*/
	public Set<Recipient<?>> getRecipients(){
		return this.recipients;
	}
	
	/**
	* Returns the topic of the email
	* @return - determines template to use
	*/
	public EmailTopic getTopic() {
		return this.topic;
	}
	
	/**
	* Returns the Model containing the data to be 
	* used in the email
	* @return Model
	*/
	public Map<String,Object> getModel(){
		return this.model;
	}
	
	/**
	* Constructor based upon a Builder
	* @param builder
	*/
	public RequestSendEmailEvent(RequestSendEmailEventBuilder builder) {
		this.title 		= builder.title;
		this.emailType 	= builder.emailType;
		this.sender 	= builder.sender;
		this.recipients = builder.recipients;
		this.topic		= builder.topic;
		this.model		= builder.model;
	}
	
	/**
	* Returns a builder for the RequestSendEmailEvent class
	* @return Builder
	*/
	public static RequestSendEmailEventBuilder builder() {
		return new RequestSendEmailEventBuilder();
	}
	
	/**
	* Builder for the RequestSendEmailEvent class
	* @author K Parkings
	*/
	public static class RequestSendEmailEventBuilder{
		
		private String 				title;
		private EmailType 			emailType;
		private Sender<?> 			sender; 
		private Set<Recipient<?>> 	recipients					= new LinkedHashSet<>(); 
		private EmailTopic 			topic;
		private Map<String,Object>	model						= new HashMap<>();
		
		/**
		* Sets the Title of the Email
		* @param title - Title of the Email
		* @return Builder
		*/
		public RequestSendEmailEventBuilder title(String title) {
			this.title = title;
			return this;
		}
		
		/**
		* Sets the email type
		* @param emailType - Type of the Email
		* @return Builder
		*/
		public RequestSendEmailEventBuilder emailType(EmailType emailType) {
			this.emailType = emailType;
			return this;
		}
		
		/**
		* Sets the Sender of the Email
		* @param sender - Sender
		* @return Builder
		*/
		public RequestSendEmailEventBuilder sender(Sender<?> sender) {
			this.sender = sender;
			return this;
		}
		
		/**
		* Sets the Recipient(s) of the Email
		* @param recipient - Recipients
		* @return Builder
		*/
		public RequestSendEmailEventBuilder recipients(Set<Recipient<?>> recipient) {
			this.recipients = recipient;
			return this;
		}
		
		/**
		* Sets the topic of the Email (Determines templace to use)
		* @param topic - Email topic
		* @return Builder
		*/
		public RequestSendEmailEventBuilder topic(EmailTopic topic) {
			this.topic = topic;
			return this;
		}
		
		/**
		* Contains data to be used in the email
		* @param model - Data for constructing the email
		* @return Builder
		*/
		public RequestSendEmailEventBuilder model(Map<String,Object> model) {
			this.model = model;
			return this;
		}
		
		/**
		* Returns a new initialized RequestSendEmailEvent 
		* @return Event
		*/
		public RequestSendEmailEvent build() {
			return new RequestSendEmailEvent(this);
		}
		
	}
	
}