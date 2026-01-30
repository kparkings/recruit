import { Injectable } 								from '@angular/core';
import { HttpClient, HttpHeaders }  				from '@angular/common/http';
import { Observable }                 				from 'rxjs';
import { environment }								from './../environments/environment';
import { PublicChat, ChatParticipant }				from './newsfeed/public-chat';

/**
* Services for interaction with public Chats ( message board posts )
*/
@Injectable({
  providedIn: 'root'
})
export class PublicMessagingService {
  
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
	* Sends a command to the API to create a new Pubilc Chat
	*/
	public createChat(parentChatId:string | undefined, message:string): Observable<any>{
		
		const backendUrl:string = environment.backendUrl + 'publicchat';

		return this.httpClient.post<any>(backendUrl, new NewMessage(parentChatId, message), this.httpOptions);

	}
	
	/**
	* Sends a command to the API to update an existing Pubilc Chat
	*/
	public updateChat(chatId:string, message:string): Observable<any>{
		
		const backendUrl:string = environment.backendUrl + 'publicchat/'+chatId;

		return this.httpClient.put<any>(backendUrl, new UpdateMessage(message), this.httpOptions);

	}
	
	/**
	* Sends a command to delete an existing Pubilc Chat
	*/
	public deleteChat(chatId:string): Observable<any>{
		
		const backendUrl:string = environment.backendUrl + 'publicchat/'+chatId;

		return this.httpClient.delete<any>(backendUrl, this.httpOptions);

	}
	
	/**
	* Fetches Chats referenced by Id
	*/
	public fetchChatById(chatId:string): Observable<PublicChat>{
		
		const backendUrl:string = environment.backendUrl + 'publicchat/'+chatId;

		return this.httpClient.get<any>(backendUrl, this.httpOptions);

	}
	
	/**
	* Fetches Child Chats of the given Chat ( I.e the replies to the Chat)
	*/
	public fetchChatChildren(chatId:string): Observable<Array<PublicChat>>{
		
		const backendUrl:string = environment.backendUrl + 'publicchat/'+chatId+"/children";

		return this.httpClient.get<any>(backendUrl, this.httpOptions);

	}
	
	/**
	* Fetches top level Chats
	*/
	public fetchPageOfTopLevelChats(pageNum:number, pageSize:number): Observable<Array<PublicChat>>{
		
		const backendUrl:string = environment.backendUrl + 'publicchat/toplevel?page=' + pageNum + '&size=' + pageSize;;

		return this.httpClient.get<any>(backendUrl, this.httpOptions);

	}
	
	/**
	* Toggles like for given chat for the authenticated User
	*/
	public toggleLikeForChat(chatId:string): Observable<PublicChat>{
		
		const backendUrl:string = environment.backendUrl + 'publicchat/'+chatId+'/like';

		return this.httpClient.put<any>(backendUrl, {}, this.httpOptions);

	}
	
	/**
	* Returns Chat participants that likes the Chat
	*/
	public fetchLikeParticipantsForChat(chatId:string):Observable<Array<ChatParticipant>>{
		const backendUrl:string = environment.backendUrl + 'publicchat/'+chatId+'/likes';
		return this.httpClient.get<any>(backendUrl, this.httpOptions);
	}
	
}

/**
* New Message
*/
export class NewMessage {
	
	/**
	* Constructor
	* @param parentChatId 	- Optional of of Parent if this is a reply and not top level message
	* @param message 		- Text of the message 
	*/
	constructor(public parentChatId:string | undefined, public message:string) {
		
	}
}

/**
* Update Message
*/
export class UpdateMessage {
	
	/**
	* Constructor
	* @param message 		- Text of the message 
	*/
	constructor(public message:string) {
		
	}
}