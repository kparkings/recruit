import { Component, ElementRef, ViewChild, HostListener } 		from '@angular/core';
import { PublicMessagingService}								from '../public-messaging.service';
import { UntypedFormGroup, UntypedFormControl }					from '@angular/forms';
import { PublicChat, ChatParticipant}							from './public-chat';
import { AppComponent } 										from 'src/app/app.component';
	
@Component({
  selector: 'app-newsfeed',
  standalone: false,
  templateUrl: './newsfeed.component.html',
  styleUrl: './newsfeed.component.css'
})
export class NewsfeedComponent {
	
	@ViewChild('publicChatDeleteConfirmBox', {static:true})	confirmDeleteModal!: 	ElementRef<HTMLDialogElement>;
	@ViewChild('publicChatLikes', {static:true})			publicChatLikesModal!: 	ElementRef<HTMLDialogElement>;
		
	
	public loadedPages:number = 0;
	public pageSize:number = 5;
		
	public currentChat:PublicChat | undefined;
	public currentChatForDelete:PublicChat | undefined;
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
	*/
	private refreshPosts():void{
		console.log("loadedPages " + 0 + " pageSize " + ((this.loadedPages+1) * this.pageSize));
			
		this.service.fetchPageOfTopLevelChats(0,((this.loadedPages+1) * this.pageSize)).subscribe(chats => {
			this.topLevelPosts = chats;
			this.topLevelPosts.sort((one:PublicChat, two:PublicChat) => this.isGreater(one,two));
			this.newMessageForm = new UntypedFormGroup({
				message: new UntypedFormControl(),
			});
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
			this.refreshPosts();
		});
	}
	
	/**
	* Opens confirm Delete Dialog box 
	*/
	public openDeleteOptionBox(chat:PublicChat):void{
		this.confirmDeleteModal.nativeElement.showModal();
		this.currentChatForDelete = chat;
	}
	
	/**
	* Opens the likes Dialog box 
	*/
	public openLikesOptionBox(chat:PublicChat):void{
		this.loadLikeParticipantsForChat(chat);
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
			this.refreshPosts();
			this.showFeedView();	
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
	
	public likeChatParticipants:Array<ChatParticipant> = new Array<ChatParticipant>();
	
	public loadLikeParticipantsForChat(chat:PublicChat):void{
		this.service.fetchLikeParticipantsForChat(chat.id).subscribe(participants => {
			this.likeChatParticipants = participants;		
			this.publicChatLikesModal.nativeElement.showModal();
		});
	}
	
	
	/**
	* Shows the FEED 
	*/
	public showFeedView():void{
		this.currentChat = undefined;
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
	
	private pageYPos = 0;
			
	@HostListener('window:scroll', ['$event']) onWindowScroll(e:any) {
	   	 
		let yPos = window.pageYOffset;
	
		if (yPos > this.pageYPos) {
			this.pageYPos = yPos +  500; 
			this.loadedPages=this.loadedPages + 1;
			console.log("Adding page " + this.loadedPages);
			this.service.fetchPageOfTopLevelChats(this.loadedPages,this.pageSize).subscribe(chats => {
				chats.forEach(chat => {
					this.topLevelPosts.push(chat);
				});
				this.topLevelPosts.sort((one:PublicChat, two:PublicChat) => this.isGreater(one,two));
			});	
		}

	}

	
}