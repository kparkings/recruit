import { Component, Input, Output, ViewChild, ElementRef, EventEmitter } from '@angular/core';
import { UntypedFormGroup, UntypedFormControl }							from '@angular/forms';
import { PublicChat, ChatParticipant}									from '../public-chat';

import { PublicMessagingService}										from '../../public-messaging.service';

import { AppComponent } 												from 'src/app/app.component';

@Component({
  selector: 'app-public-posts',
  standalone: false,
  templateUrl: './public-posts.component.html',
  styleUrl: './public-posts.component.css'
})
export class PublicPostsComponent {

	@ViewChild('publicChatDeleteConfirmBox', {static:true})	confirmDeleteModal!: 	ElementRef<HTMLDialogElement>;
	@ViewChild('publicChatLikes', {static:true})			publicChatLikesModal!: 	ElementRef<HTMLDialogElement>;
		
	@Input()  public topLevelPosts:Array<PublicChat> = new Array();
	@Input()  public topLevel:string = "false";
	@Input()  public currentTopLevelChatId:string = "";
	@Input()  public parentCss:string = "Even";
	
	@Output() replyAddedEvent 		= new EventEmitter<string>();

	public loadedPages:number = 0;
	public pageSize:number = 5;
	public currentChat:PublicChat | undefined;
	public currentChatForDelete:PublicChat | undefined;
	public currentChatForReply:PublicChat | undefined;
	public replyCssClass:string = "Even";
		
	public replyChatForm:UntypedFormGroup = new UntypedFormGroup({
		message: 			new UntypedFormControl(),
	});
			
	/**
	* Constructor
	* @oaram service  		- Services for PublicMessages 
	* @param appComponent 	- Main system component
	*/
	public constructor(	public service:PublicMessagingService, private readonly appComponent:AppComponent){
			
	}
	
	/**
	* Angular lifecycle method
	*/
	ngOnInit(){
	
		this.refreshPosts();
		
		if(this.parentCss == 'Odd') {
			this.replyCssClass = 'Even';
		} else {
			this.replyCssClass = 'Odd';
		}
					
	}
	
	/**
	* Re-fetches posts including new and updated versions
	*/
	private refreshPosts():void{
		
		if (this.topLevel == 'true') {
			this.service.fetchPageOfTopLevelChats(0,((this.loadedPages+1) * this.pageSize)).subscribe(chats => {
				this.topLevelPosts = chats;
				this.topLevelPosts.sort((one:PublicChat, two:PublicChat) => this.isGreater(one,two));
				this.topLevelPosts.forEach(tlp => {
					this.service.fetchChatChildren(tlp.id).subscribe(replies => {
						tlp.replies = replies;
						tlp.replies.sort((one:PublicChat, two:PublicChat) => this.isGreater(one,two));
					});
				})
			});	
		} else {
			this.service.fetchChatChildren(""+this.currentTopLevelChatId).subscribe(chats => {
				this.topLevelPosts = chats;
				this.topLevelPosts.sort((one:PublicChat, two:PublicChat) => this.isGreater(one,two));
				this.topLevelPosts.forEach(tlp => {
					this.service.fetchChatChildren(tlp.id).subscribe(replies => {
						tlp.replies = replies;
						tlp.replies.sort((one:PublicChat, two:PublicChat) => this.isGreater(one,two));
					});
				})
			});	
		}
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
	* Opens confirm Delete Dialog box 
	*/
	public openDeleteOptionBox(chat:PublicChat):void{
		this.confirmDeleteModal.nativeElement.showModal();
		this.currentChatForDelete = chat;
	}
	
	public editChatForm:UntypedFormGroup = new UntypedFormGroup({
		message: 			new UntypedFormControl(),
	});
	
	/**
	* Shows the Edit view for the Chat
	* @param chat - Chat to be edited 
	*/
	public showEditView(chat:PublicChat):void{
		this.showFeedView();
		this.currentChat = chat;
		this.editChatForm = new UntypedFormGroup({
			message: new UntypedFormControl(),
		});
		this.editChatForm.get('message')?.setValue(chat.message);
	}
	
	/**
	* Shows the FEED 
	*/
	public showFeedView():void{
		this.currentChat = undefined;
		this.currentChatForReply = undefined;
	}
	
	/**
	* Returns whether or not the chat is currently being edited 
	* or just viewd 
	*/
	public isInEditMode(chat:PublicChat):boolean{
		
		if (!this.currentChat) {
			return false;
		}
		
		return this.currentChat.id == chat.id;
	}
	
	/**
	* Returns whether or not the chat is currently being edited 
	* or just viewd 
	*/
	public isInReplyMode(chat:PublicChat):boolean{
		
		if (!this.currentChatForReply) {
			return false;
		}
		
		return this.currentChatForReply.id == chat.id;
	}
	
	/**
	* Shows/Hides replies to Chat
	*/
	public toggleReplies(chat:PublicChat):void{
		chat.showReplies = !chat.showReplies;
	}
	
	/**
	* Returns number of direct replies to a message
	*/
	public getReplyCount(chat:PublicChat):number{
		if (!chat.replies){
			return 0;
		}
		
		return chat.replies.length;
	}
	
	/**
	* Opens the likes Dialog box 
	*/
	public openLikesOptionBox(chat:PublicChat):void{
		this.loadLikeParticipantsForChat(chat);
	}
	
	public likeChatParticipants:Array<ChatParticipant> = new Array<ChatParticipant>();
		
	public loadLikeParticipantsForChat(chat:PublicChat):void{
		this.service.fetchLikeParticipantsForChat(chat.id).subscribe(participants => {
			this.likeChatParticipants = participants;		
			this.publicChatLikesModal.nativeElement.showModal();
		});
	}
	
	/**
	* Returns whether the User has liked the Chat
	* @param chat - Chat to be updated 
	*/
	public isLikedByUser(chat:PublicChat):boolean{
		let userId:string = ""+sessionStorage.getItem("userId");
		return chat.likes.indexOf(userId) > -1;
	}
	
	/**
	* Toggles whether the current User has liked the Chat
	* @param chat - Chat to be updated 
	*/	
	public toggleLikeForChat(chat:PublicChat):void{
		this.service.toggleLikeForChat(chat.id).subscribe(res =>{
			this.topLevelPosts.filter(p => p.id == chat.id).forEach(match => match.likes = res.likes);
		});
	}
	
	public showReplyView(chat:PublicChat):void{
		this.showFeedView();
		this.currentChatForReply = chat;
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
			this.refreshPosts();
			this.showFeedView();	
		});
		
	}
		
	/**
	* Updates the selected Chat
	* @param chat - Chat to be updated 
	*/
	public saveChatReply(chat:PublicChat):void{
		this.service.createChat(chat.id, this.replyChatForm.get('message')?.value).subscribe(res => {
			this.replyChatForm = new UntypedFormGroup({
				message: new UntypedFormControl(),
			});
			this.refreshPosts();
			this.showFeedView();	
			this.replyAddedEvent.emit('replyAddedEvent');
		});
		
	}
	
	/**
	* Closed Delete Chat confirmation box 
	*/
	public closeDeleteOptionBox():void{
		this.confirmDeleteModal.nativeElement.close();
		this.currentChatForDelete = undefined;
	}
	
	/**
	* Closes Likes box 
	*/
	public closeLikesOptionBox():void{
		this.publicChatLikesModal.nativeElement.close();
	}
	
	/**
	* Opens Chat session with ChatParticipant
	*/
	public openChatChatParticipant(chatParticipant:ChatParticipant):void{
		this.closeLikesOptionBox();
		this.appComponent.privateChat.openChat(chatParticipant.id);	
	}
	
	/**
	* Deletes Chat 
	*/
	public deleteChat():void{
		this.service.deleteChat(""+this.currentChatForDelete?.id).subscribe(response => {
			this.topLevelPosts = this.topLevelPosts.filter(p => p.id != ""+this.currentChatForDelete?.id);
			this.currentChatForDelete = undefined;
			this.confirmDeleteModal.nativeElement.close();
		});
	}
	
	/**
	* Returns whether a given message was created by the authenticated
	* user or another user
	* @param msg: Message to be checked
	*/
	public isOwnMessageChatParticipant(chatParticipant:ChatParticipant):boolean{
		return chatParticipant.id == sessionStorage.getItem("userId") ? true : false;
	}
	
}