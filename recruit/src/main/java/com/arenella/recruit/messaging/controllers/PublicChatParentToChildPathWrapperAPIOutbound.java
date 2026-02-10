package com.arenella.recruit.messaging.controllers;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

/**
* Wrapper used to return a top level Chat with all of its Child 
* Chat's loaded and the path defined to navigate from the top 
* level to a given child chat. 
*/
public class PublicChatParentToChildPathWrapperAPIOutbound {

	private PublicChatAPIOutbound 	chat;
	private Set<UUID> 				pathFromTopToBottom = new LinkedHashSet<>();
	
	PublicChatParentToChildPathWrapperAPIOutbound(PublicChatParentToChildPathWrapperAPIOutboundBuilder builder) {
		
	}
	
	public static PublicChatParentToChildPathWrapperAPIOutboundBuilder builder() {
		return new PublicChatParentToChildPathWrapperAPIOutboundBuilder();
	}
	
	public static class PublicChatParentToChildPathWrapperAPIOutboundBuilder{
		
	}
}
