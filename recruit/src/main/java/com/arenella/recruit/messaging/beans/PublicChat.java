package com.arenella.recruit.messaging.beans;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

/**
* Represents a conversation via text based messages between
* two users of the system
*/
public class PublicChat {
	
	private UUID 				id;
	private UUID				parentChat;
	private String				ownerId;
	private LocalDateTime 		created;
	private String 				message;
	private Set<ChatMessage>	replies 				= new LinkedHashSet<>();
	private Set<PublicChat> 	children 				= new LinkedHashSet<>();
	private Set<String>			likes					= new LinkedHashSet<>();

}
