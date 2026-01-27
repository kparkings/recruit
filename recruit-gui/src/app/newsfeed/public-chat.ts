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
	* @param ownerId 		- Id of User who created the Chat
	* @param created 		- When the Chat was created
	* @param message 		- Textual body of the Chat
	* @param likes 			- Ids of User's who liked the Chat
	*/
	constructor(public id:string,
			public audienceType:AUDIENCE_TYPE,
			public parentChat:string,
			public ownerId:string,
			public created:Date,
			public message:string,
			public likes:Array<string>){
				
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
	* @param firstName 	- Participants first name
	* @param surname 	- Participants surname
	* @param photo 		- Participants profile image
	*/
	constructor(public id:string, public firstName:string, public surname:string, public photo:Photo){}
	
}

/**
* Photo of Chat participant
*/
export class Photo{
	constructor(public imageBytes:any, format:string){}
}

enum AUDIENCE_TYPE {ALL, RECRUITER, CANDIDATE, SYSTEM}