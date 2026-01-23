package com.arenella.recruit.messaging.services;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import com.arenella.recruit.messaging.beans.PublicChat;

/**
* Defines services related to Public Chats 
*/
public interface PublicChatService {

	public static final String ERR_MSG_UNKNOWN_PARENT 						= "Reference to unknown parent";
	public static final String ERR_MSG_CANT_CREATE_CHAT_FOR_ANOTHER_USER 	= "Cant create a Chat for another User";
	public static final String ERR_MSG_CANT_UPDATE_CHAT_FOR_ANOTHER_USER 	= "Cant update other Users Chat";
	
	/**
	* Saves a Chat
	* @param chat - Chat to be Saved
	* @param user - Currently authenticated User
	* @return Id of the chat
	*/
	UUID saveChat(PublicChat chat, Principal user); 
	
	/**
	* Deletes an existing Chat and any Child chats
	* @param chatId - Id of the Chat to delete
	* @param user - Currently authenticated User 
	*/
	void deleteChat(UUID chatId, Principal user);
	
	/**
	* Returns a specified Chat
	* @param chatId - Unique id of the Chat to return
	* @return Chat
	*/
	PublicChat fetchChat(UUID chatId);
	
	/**
	* Returns all direct Children of the PublicChat
	* @param chatId - Unique Id of Chat
	* @return Direct child Chats
	*/
	Set<PublicChat> fetchChatChildren(UUID chatId);
	
	/**
	* Returns a page of top level public Chats. These are 
	* the original messages posted. That is, they have no
	* parent Chat. They are the top level Chats
	* @param page		- Which page of results to return
	* @param maxResults - Max number of results on a page
	* @return Page of PublicChats
	*/
	Set<PublicChat> fetchTopLevelChats(int page, int maxResults);
	
	/**
	* Deletes any top level chats, their children and likes if they 
	* are older than the given date
	* @param date - Date to delete from
	*/
	void deleteChatsOlderThan(LocalDate date);
	
}
