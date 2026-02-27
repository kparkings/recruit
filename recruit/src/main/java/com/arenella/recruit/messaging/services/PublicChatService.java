package com.arenella.recruit.messaging.services;

import java.security.Principal;
import java.time.LocalDate;
import java.util.SequencedSet;
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
	public static final String ERR_MSG_UKNOWN_CHAT 							= "Reference to unknown Chat";
	
	/**
	* Creates a mew Chat  
	* @param parentChat - If exists parent chat ID
	* @param message	- Test body of the Chat
	* @param user		- Authenticated user
	* @return ID of new PublicChat
	*/
	UUID createChat(UUID parentChat, String message, Principal user); 
	
	/**
	* Updates an existing Chat  
	* @param message	- Test body of the Chat
	* @param user		- Authenticated user
	* @return ID of new PublicChat
	*/
	void updateChat(UUID chatId, String message, Principal user); 
	
	
	/**
	* Deletes an existing Chat and any Child chats
	* @param chatId - Id of the Chat to delete
	* @param user - Currently authenticated User 
	*/
	void deleteChat(UUID chatId, Principal user);
	
	/**
	* Like the deleteUserChats except there is no validation. THis is 
	* for use by the system to delete chats and should never be 
	* exposed to the end users via the API
	* @param userId - Id of User
	 */
	void systemDeleteChatsForUser(String userId);
	
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

	/**
	* Toggles the authenticated users like for a given Chat
	* @param chatId - Unique Id of Chat to like/unlike
	* @param name	- Id if authenticated user
	* @return Update PublicChat
	*/
	PublicChat toggleLikeForChat(UUID chatId, String name);
	
	public PublicChat removeLikeForChat(UUID chatId, String name);
	
	public void removeLikesForUser(String userId);
	
	public SequencedSet<UUID> fetchPathToTopLevelChat(UUID chatId);

	/**
	* Returns whether at least one unread Public chat has been added since
	* the User last viewed the public chats
	* @param authenticatedUserId - Id of authenticated User
	* @return If unread public chats exist for USer
	*/
	public boolean isUnreadNewsFeedItems(String authenticatedUserId);
	
}
