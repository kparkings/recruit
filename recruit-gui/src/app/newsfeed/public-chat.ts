/**
* Class represents a Public Chat. That is a Chat
* on the feed and can eiether be a top level Chat or 
* a reply to another Chat. 
*/
export class PublicChat{
	
	/**
	* Constructor 
	* @param id 			- Unique id of the Chat
	* @param audienceType 	- Which type of User can view the Chat
	* @param parentChat 	- If empty top level Chat. If present Chat is a reply and this is the parent Id
	* @param owner 			- User who created the Chat
	* @param created 		- When the Chat was created
	* @param message 		- Textual body of the Chat
	* @param likes 			- Ids of User's who liked the Chat
	*/
	constructor(public id:string,
			public audienceType:AUDIENCE_TYPE,
			public parentChat:string,
			public owner:ChatParticipant,
			public created:Date,
			public message:string,
			public likes:Array<string>,
			public replies:Array<PublicChat>,
			public showReplies:boolean){
				
	}
	
}

/**
* Class used to create a new PublicChat 
*/
export class PublicChatAPIInbound{
	
	/**
	* Constructor
	* @param parentChat - Id of Parent if a reply to existing CHt
	* @param message 	- Textual body of the Chat 
	*/
	constructor(public parentChat:string | undefined, public message:string){
		
	}
	
}

/**
* Class used to update existing PublicChat
*/
export class PublicChatUpdateAPIInbound{
	
	/**
	* Constructor
	* @param message - Textual body of Chat
	*/
	constructor(public message:string){
		
	}
}

/**
* A User participating in the Chat 
*/
export class ChatParticipant {
	
	/**
	* Constructor
	* @paran id 		- Unique Id of the Participant
	* @param type		- Type of Participant ( RECRUIER, CANDIDATE, SYSTEM)
	* @param firstName 	- Participants first name
	* @param surname 	- Participants surname
	* @param photo 		- Participants profile image
	*/
	constructor(public id:string, public type:string, public firstName:string, public surname:string, public photo:Photo){}
	
}

/**
* A Notification for a User relating to Public Chats 
*/
export class PublicChatNotification {

	/**
	* Constructor
	* @paran notificationId 	- Unique Id of the Notification
	* @param type				- Type of Notification. What action its notifying
	* @param created 			- When the Notification was created
	* @param chatId 			- Chat the Notification relates to
	* @param destinationUserId	- User notification is intended for
	* @param initiatingUser		- User whose action resulted in the Notification
	* @param viewed				- Whethere the destination user has viewed the Notification
	*/
	constructor(
		public notificationId:string, 
		public type:string,
		public created:Date,
		public chatId:string,
		public destinationUserId:string,
		public initiatingUser:ChatParticipant,
		public viewed:boolean)
	{}
	
}

/**
* Photo of Chat participant
*/
export class Photo{
	constructor(public imageBytes:any, format:string){}
}

enum AUDIENCE_TYPE {ALL, RECRUITER, CANDIDATE, SYSTEM}