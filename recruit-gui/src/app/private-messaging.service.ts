import { Injectable } 								from '@angular/core';
import { HttpClient, HttpHeaders }  				from '@angular/common/http';
import { Observable }                 				from 'rxjs';
import { environment }								from './../environments/environment';

/**
* Services for communication with the backend API
* relating to Private Chats between two User's. 
*/
@Injectable({
  providedIn: 'root'
})	
export class PrivateMessagingService {
	
	/**
	* Constructor
	* @param - Utility for sending messages to backend API 
	*/
	constructor(private httpClient: HttpClient) {
		
	}
	
	httpOptions = {
		headers: new HttpHeaders({ 'Content-Type': 'application/json' }), withCredentials: true
	};

	headers = { 'content-type': 'application/json'};
	
	/**
	* Sends a command to the API to create a new Chat between two User's
	*/
	public createChat(senderId:string, recipientId:string): Observable<any>{
		
		const backendUrl:string = environment.backendUrl + 'privatechat';

		let chat:PrivateChatAPIInbound = new PrivateChatAPIInbound(senderId, recipientId);
			
  		return this.httpClient.post<any>(backendUrl, chat, this.httpOptions);

	}
	
	/**
	* Sends command to the API to add a message to a CHat
	*/
	public addMessageToExistingChat(chatId:string, messageText:string): Observable<any>{
		
		const backendUrl:string = environment.backendUrl + 'privatechat/'+chatId+'/message';

		let message:ChatMessageAPIInbound = new ChatMessageAPIInbound(messageText);
			
		return this.httpClient.put<any>(backendUrl, message, this.httpOptions);

	}
	
	/**
	* Sends command to update the blockes status for the User of the chat
	*/
	public setBlockedStatus(chatId:string, blocked:boolean): Observable<any>{
		
		const backendUrl:string = environment.backendUrl + 'privatechat/'+chatId+'/block/'+blocked;
			
		return this.httpClient.put<any>(backendUrl, {}, this.httpOptions);

	}
	
	/**
	* Fetches all the Chats for currently authenticated User
	*/
	public fetchChatsByUserId(): Observable<Array<PrivateChatAPIOutbound>>{
		
		const backendUrl:string = environment.backendUrl + 'privatechat';

  		return this.httpClient.get<any>(backendUrl, this.httpOptions);

	}

	/**
	* Fetches specific Chat
	*/
	public fetchPrivateChatById(chatId:string): Observable<PrivateChatAPIOutbound>{
		
		const backendUrl:string = environment.backendUrl + 'privatechat/'+chatId;

		return this.httpClient.get<any>(backendUrl, this.httpOptions);

	}
		
	/**
	* Sends command to the API to delete a message from a CHat
	*/
	public deleteMessageFromExistingChat(chatId:string, messageId:string): Observable<any>{
		
		const backendUrl:string = environment.backendUrl + 'privatechat/'+chatId+'/message/'+messageId;

		return this.httpClient.delete<any>(backendUrl, this.httpOptions);

	}
	
	/**
	* Sends command to the API to update the last time the authenticated user pressed a key 
	* in the Chat screen
	*/
	public doSetKeyPressed(chatId:string): Observable<any> {
		
		const backendUrl:string = environment.backendUrl + 'privatechat/'+chatId+'/keypressed';

		return this.httpClient.put<any>(backendUrl, {}, this.httpOptions);
	
	}
	
	/**
	* Sends command to the API to update the last time the authenticated user viewed the Chat
	*/
	public doSetLastViewed(chatId:string): Observable<any> {
		
		const backendUrl:string = environment.backendUrl + 'privatechat/'+chatId+'/viewed';

		return this.httpClient.put<any>(backendUrl, {}, this.httpOptions);
	
	}
	
	/**
	* Returns whether or not the Recruiter has access to the Private Chat functinality 
	*/
	public hasChatAccess():Observable<boolean>{
		
		const backendUrl:string = environment.backendUrl +'privatechat/hasAccess';
		  
		return this.httpClient.get<any>(backendUrl, this.httpOptions);
		
	}
  
}


/**
* Details of a given Private Chat between 
* two User's
*/
export class PrivateChatAPIOutbound {
	
	/**
	* Constructor
	* @param public id						- Unique id of the Chat
	* @param public sender					- User who created the Chat
	* @param public recipient				- User who Chat was created to chat with
	* @param public created					- When the Chat was created
	* @param public lastUpdated				- When the Chat was last updated. Including a new reply
	* @param public replies					- Messages between User's
	* @param public lastKeyPressSender		- Last time Sender was typing in the Chat
	* @param public lastKeyPressRecipient	- Last time the Recipient was typing in the Chat
	* @param public blockedBySender			- If the Sender has blocked the Chat
	* @param public blockedByRecipient		- If the Recipient has blocked the Chat
	* @param public lastViewedBySender		- Last time the Sender viewed the Chat
	* @param public recipientIsTyping		- If Recipient is typing in the Chat
	* @param public senderIsTyping			- If Sender is typing in the Chat
	* @param public lastViewedByRecipient	- Last time the Recipient viewed the Chat	
	*/
	constructor(
		public id:string,
		public sender:ChatParticipantAPIOutbound,
		public recipient:ChatParticipantAPIOutbound,
		public created:Date,
		public lastUpdated:Date,
		public replies:Map<string,ChatMessageAPIOutbound> = new Map<string,ChatMessageAPIOutbound>([]),
		public lastKeyPressSender:Date,
		public lastKeyPressRecipient:Date,
		public blockedBySender:boolean,
		public blockedByRecipient:boolean,
		public lastViewedBySender:Date,
		public recipientIsTyping:boolean,
		public senderIsTyping:boolean,
		public lastViewedByRecipient:Date){}
	
}

/**
* An individual Message in a given Chat 
*/
export class ChatMessageAPIOutbound {
	
	/**
	* Constructor 
	* param id				- Unique id of the Message
	* param chatId			- Unique id of Message's parent Chat
	* param senderId		- User who sent this specific message in the Chat
	* param recipientId		- Recipient of this specific message
	* param created			- When the message was created
	* param message			- The text body of the Message
	* param likes			- Ids of User's who liked the message
	*/
	constructor(
		public id:string,
		public chatId:string,
		public senderId:string,
		public recipientId:string,
		public created:Date,
		public message:string,
		public likes:Array<string>		= new Array<string>()
	){}
	
}

/**
* Represents a Participant in the Chat 
*/
export class ChatParticipantAPIOutbound {
	
	constructor(
		public id:string,
		public firstName:string,
		public surname:string,
		public photo:Photo
	){}
		
}

/**
* Photo of Chat participant
*/
export class Photo{
	constructor(public imageBytes:any, format:string){}
}

/**
* Command to create a new Chat between two User's   
*/
export class PrivateChatAPIInbound {
	
	/**
	* Constructor
	* @param senderId 		- id of User initializing the Chat
	* @param recipientId 	- id of User the sender is creating a Chat with
	*/
	constructor(public senderId:string, public recipientId:string) {
		
	}
	
}

/**
* Command to add a new Chat message to a 
* to a Chat
*/
export class ChatMessageAPIInbound {
	
	/**
	* Constructor
	* @oaram message - chat message text
	*/
	constructor(public message:string) {
	}
	
}
