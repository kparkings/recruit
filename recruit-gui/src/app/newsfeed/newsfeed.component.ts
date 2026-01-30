import { Component } 								from '@angular/core';
import { NewsFeedItem, NewsFeedItemLine } 			from './news-feed-item';
import { PublicMessagingService}					from '../public-messaging.service';
import { UntypedFormGroup, UntypedFormControl }		from '@angular/forms';
import { PublicChat}								from './public-chat';
import { AppComponent } 							from 'src/app/app.component';

@Component({
  selector: 'app-newsfeed',
  standalone: false,
  templateUrl: './newsfeed.component.html',
  styleUrl: './newsfeed.component.css'
})
export class NewsfeedComponent {
	
	public currentChat:PublicChat | undefined;
	public topLevelPosts:Array<PublicChat> = new Array<PublicChat>();
	
	/**
	* Constructor
	* @oaramparam service  - Services for PublicMessages 
	*/
	public constructor(public service:PublicMessagingService, private appComponent:AppComponent){
		this.refreshPosts();
	}
	
	/**
	* Re-fetches posts including new and updated versions
	* TODO: [KP] Need to tak into account individual page loads already made 
	*/
	private refreshPosts():void{
		this.service.fetchPageOfTopLevelChats(0,10000).subscribe(chats => {
			this.topLevelPosts = chats;
			this.topLevelPosts.sort((one:PublicChat, two:PublicChat) => this.isGreater(one,two));
		});	
	}
	
	public newMessageForm:UntypedFormGroup = new UntypedFormGroup({
		message: 			new UntypedFormControl(),
	});
	
	public editChatForm:UntypedFormGroup = new UntypedFormGroup({
		message: 			new UntypedFormControl(),
	});
	
	/**
	* Creates a new message
	*/
	public createNewMessage():void{
		this.service.createChat(undefined, this.newMessageForm.get("message")?.value).subscribe(res => {
			this.service.fetchPageOfTopLevelChats(0,10000).subscribe(chats => {
				this.topLevelPosts = chats;
				this.topLevelPosts.sort((one:PublicChat, two:PublicChat) => this.isGreater(one,two));
				this.newMessageForm = new UntypedFormGroup({
					message: new UntypedFormControl(),
				});
			});
		});
	}
	
	public deleteChat(chat:PublicChat):void{
		this.service.deleteChat(chat.id).subscribe(response => {
			this.refreshPosts();
		});
	}
	
	/**
	* Comparator to sort Chats in descending creation time order 
	*/
	private isGreater(one:PublicChat, two:PublicChat):number{
		console.log(one.created < two.created);
		
		if(one.created > two.created){
			return -1;
		}
		
		if(one.created < two.created){
			return 1;
		}
		
		return 0;
	}
	
	/**
	* Returns whether the Chat contains a profile
	* image for the owner 
	*/
	public hasImage(chat:PublicChat):boolean{
		if (chat.owner.photo ) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	* Returns the bytes of the profile image of the owner 
	*/
	public getChatParticipantPhoto(chat:PublicChat):any{
		return chat.owner.photo.imageBytes;
	}
	
	/**
	* Returns whether a given message was created by the authenticated
	* user or another user
	* @param msg: Message to be checked
	*/
	public isOwnMessage(chat:PublicChat):boolean{
		return chat.owner.id == sessionStorage.getItem("userId") ? true : false;
	}
	
	/**
	* Opens Chat session with the owner of the Chat 
	*/
	public openChat(chat:PublicChat):void{
		this.appComponent.privateChat.openChat(chat.owner.id);	
	}
	
	/**
	* Shows the Edit view for the Chat
	* @param chat - Chat to be edited 
	*/
	public showEditView(chat:PublicChat):void{
		this.currentChat = chat;
		this.editChatForm = new UntypedFormGroup({
			message: new UntypedFormControl(),
		});
		this.editChatForm.get('message')?.setValue(chat.message);
	}
	
	/**
	* Updates the selected Chat
	* @param chat - Chat to be updated 
	*/
	public saveChatUpdate(chat:PublicChat):void{
		this.service.updateChat(chat.id, this.editChatForm.get('message')?.value).subscribe(res => {
			this.editChatForm = new UntypedFormGroup({
				message: new UntypedFormControl(),
			});
			this.service.fetchPageOfTopLevelChats(0,10000).subscribe(chats => {
				this.topLevelPosts = chats;
				this.topLevelPosts.sort((one:PublicChat, two:PublicChat) => this.isGreater(one,two));
				this.newMessageForm = new UntypedFormGroup({
					message: new UntypedFormControl(),
				});
			});
			this.showFeedView();	
		});
		
	}
	
	/**
	* Shows the FEED 
	*/
	public showFeedView():void{
		this.currentChat = undefined;
	}
	
	public isInEditMode(chat:PublicChat):boolean{
		
		if (!this.currentChat) {
			return false;
		}
		
		return this.currentChat.id == chat.id;
	}
	
}