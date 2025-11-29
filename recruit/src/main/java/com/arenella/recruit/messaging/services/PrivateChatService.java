package com.arenella.recruit.messaging.services;

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
	* @param userId - Unique Id of theUser
	* @return Users Chat's
	*/
	Set<PrivateChat> getUsersChats(String userId);
	
	/**
	* Retrieves a Chat including its messages
	* @param chatId - UniqueId of the Chat
	* @param userId - UniqueId of the User requesting the Chat
	* @return Chat matching the chatId
	*/
	PrivateChat getChat(UUID chatId, String userId);

	/**
	* Persists a new or existing Chat
	* @param chat - To be persisted
	*/
	UUID saveChat(PrivateChat chat, String userId);

	/**
	* Sets when the user last viewed the Chat
	* @param chatId - Unique Id of the Chat viewed
	* @param userId - Unique Id of the User viewing the Chat
	*/
	void setLastViewed(UUID chatId, String userId);

	/**
	* Sets when the user last typed the Chat
	* @param chatId - Unique Id of the Chat typed in
	* @param userId - Unique Id of the User typing in the Chat
	*/
	void setLastKeyPress(UUID chatId, String userId);

	/**
	* Updates the blocked status of the Chat by either the 
	* Sender or the Recipient
	* @param chatId		- Id of Chat to update
	* @param userId		- Id of User making update
	* @param blocked	- new blocked status of Chat for the User
	*/
	void setBlockedStatus(UUID chatId, String userId, boolean blocked);
	
	/**
	* Deletes an existing Chat
	* Needed if either the Candidate or Recruiters account is deleted
	* @param chat - Delete Chat
	*/
	void deleteUserChats(String userId);
	
}