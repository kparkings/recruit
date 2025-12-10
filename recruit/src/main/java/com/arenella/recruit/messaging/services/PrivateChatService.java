package com.arenella.recruit.messaging.services;

import java.security.Principal;
import java.util.Set;
import java.util.UUID;

import com.arenella.recruit.messaging.beans.PrivateChat;

/**
* Defines services relating to PrivateChat messaging 
*/
public interface PrivateChatService {

	public static String ERR_MSG_UNKNOWN_CHAT 							= "You cannot fetch an unknown Chat.";
	public static String ERR_MSG_UNAUTHORISED_CHAT 						= "You are not authorised to view this Chat.";
	public static String ERR_MSG_CANNOT_CREATE_CHAT 					= "Cannot create Chat.";
	public static String ERR_MSG_CANNOT_CREATE_CHAT_FOR_ANOTHER_USER 	= "Invalid attempt to create chat for another User.";
	public static String ERR_MSG_CANNOT_CREATE_CHAT_WITH_YOURSELF		= "You cannot chat with yourself.";
	public static String ERR_MSG_CANNOT_CREATE_CHAT_COMBINATION_EXISTS	= "Invalid attempt to create chat for User combination. Combination already exists.";

	/**
	* Returns Chats where the user is either the 
	* Sender or Recipient 
	* @param user - Authenticated User
	* @return Users Chat's
	*/
	Set<PrivateChat> getUsersChats(Principal user);
	
	/**
	* Retrieves a Chat including its messages
	* @param chatId - UniqueId of the Chat
	* @param user - Authenticated User
	* @return Chat matching the chatId
	*/
	PrivateChat getChat(UUID chatId, Principal user);

	/**
	* Persists a new or existing Chat
	* @param chat - To be persisted
	* @param user - Authenticated User
	*/
	UUID saveChat(PrivateChat chat, Principal user);

	/**
	* Sets when the user last viewed the Chat
	* @param chatId - Unique Id of the Chat viewed
	* @param user - Authenticated User
	*/
	void setLastViewed(UUID chatId, Principal user);

	/**
	* Sets when the user last typed the Chat
	* @param chatId - Unique Id of the Chat typed in
	* @param user - Authenticated User
	*/
	void setLastKeyPress(UUID chatId, Principal user);

	/**
	* Updates the blocked status of the Chat by either the 
	* Sender or the Recipient
	* @param chatId		- Id of Chat to update
	* @param user - Authenticated User
	* @param blocked	- new blocked status of Chat for the User
	*/
	void setBlockedStatus(UUID chatId, Principal user, boolean blocked);
	
	/**
	* Deletes an existing Chat
	* Needed if either the Candidate or Recruiters account is deleted
	* @param user - Authenticated User
	*/
	void deleteUserChats(Principal user);
	
	/**
	* Adds a Message to an existing Chat. Only a User who is a 
	* Sender/Receiver of the Chat can add a Message
	* @param charId		- Id of Chat to add message to
	* @param message	- new Message
	* @param user - Authenticated User
	*/
	void addMessage(UUID chatId, String message, Principal user);
	
	/**
	* Marks a Message in a Chat as being Deleted. Only the User who
	* created the message can mark it as Deleted 
	* @param chatId		- Id of Chat message belongs to
	* @param messageId	- Id of Message to Delete
	* @param user - Authenticated User
	*/
	void deleteMessage(UUID chatId, UUID messageId, Principal user);
	
	/**
	* Returns whether a authenticated User has permission to use the
	* Private Chat. The method can optionally throw an exception if
	* the User does not have permission
	* @param principal			- Authenticated User
	* @param throwIfCannotChat	- Throws Exception if User cannot chat and value equals true
	* @return whether the User has permissions to use the Private Chat functionality
	*/
	boolean canChat(Principal principal, boolean throwIfCannotChat);
	
}