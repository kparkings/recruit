import { Component, ElementRef, ViewChild, HostListener } 				from '@angular/core';
import { PublicMessagingService}										from '../public-messaging.service';
import { UntypedFormGroup, UntypedFormControl }							from '@angular/forms';
import { PublicChat, ChatParticipant, PublicChatNotification}			from './public-chat';
import { AppComponent } 												from 'src/app/app.component';

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
	public mainViewPane:string = "FEED"; //FEED or CV
	public newsFeedPane:string = "NEWSFEED"; //NEWSFEED or SINGLEPOST
		
	public currentChat:PublicChat | undefined;
	public currentChatForDelete:PublicChat | undefined;
	public currentChatForReply:PublicChat | undefined;
	public topLevelPosts:Array<PublicChat> = new Array<PublicChat>();
	public notifications:Array<PublicChatNotification> = new Array<PublicChatNotification>();
	
	/**
	* Constructor
	* @oaramparam service  - Services for PublicMessages 
	*/
	public constructor(	public service:PublicMessagingService, 
						private appComponent:AppComponent){
		//this.refreshPosts();
	}
	
	ngAfterViewInit(){
		
		this.refreshPosts();
		this.service.fetchNotificationsForUser().subscribe(notifications => {
			this.notifications = notifications;
			this.notifications.sort((one:PublicChatNotification, two:PublicChatNotification) => this.isGreaterNotification(one,two));
			this.topLevelPosts.sort((one:PublicChat, two:PublicChat) => this.isGreater(one,two));
			this.service.unreadNotifications = notifications.filter(n => n.viewed == false).length;
		});
	}
	
	public resetScroll():void{
		this.pageYPos = window.pageYOffset;
		this.refreshPosts();
		this.newsFeedPane = "NEWSFEED";
	}
	
	public handleReplyForNotification():void{
		this.refreshPosts();
		//this.pageYPos = window.pageYOffset;
		//this.refreshPosts();
	}
	
	public notificationPath:Array<string> = new Array<string>(); 
	public loadNotification(notification:PublicChatNotification):void{
		this.newsFeedPane = "SINGLEPOST"
		this.service.fetchPathToChat(notification.chatId).subscribe(chatPath => {
			this.notificationPath = chatPath;
			this.service.fetchChatById(chatPath[0]).subscribe(chat =>{
				this.topLevelPosts = new Array<PublicChat>();
				this.topLevelPosts.push(chat);
				this.topLevelPosts.forEach(tlp => {
					this.service.fetchChatChildren(tlp.id).subscribe(replies => {
						tlp.replies = replies;
						tlp.replies.sort((one:PublicChat, two:PublicChat) => this.isGreater(one,two));
						
						
						if (this.notificationPath.length > 0) {
							tlp.replies.forEach(reply => {
							//	tlp.showReplies = true;
													
							
							console.log("Path " + JSON.stringify(this.notificationPath));
							console.log("TLP " + tlp.id);
										
								//if(this.notificationPath.indexOf(tlp.id) >= 0 && tlp.id != notification.chatId){
								if(this.notificationPath.indexOf(tlp.id) >= 0 ){	
									console.log("Opening " + tlp.id + "  where ncid = " + notification.chatId);
									tlp.showReplies = true;	
								}
								//if(this.notificationPath.indexOf(reply.id) > 0){
									//reply.showReplies = true;	
								//}
							});
							
						}
						
						
					});
				})
			});
			
		});
		
	
	}
	
	public closeNotificationView():void{
		window.location.reload();
	}
	
	public closeNotification():void{
		this.newsFeedPane = "NEWSFEED";
		this.resetScroll();
	}
	
	/**
	* Re-fetches posts including new and updated versions
	*/
	private refreshPosts():void{
		this.service.fetchPageOfTopLevelChats(0,((this.loadedPages+1) * this.pageSize)).subscribe(chats => {
					this.topLevelPosts = chats;
					this.topLevelPosts.sort((one:PublicChat, two:PublicChat) => this.isGreater(one,two));
					this.newMessageForm = new UntypedFormGroup({
						message: new UntypedFormControl(),
					});
					this.topLevelPosts.forEach(tlp => {
						this.service.fetchChatChildren(tlp.id).subscribe(replies => {
							tlp.replies = replies;
							tlp.replies.sort((one:PublicChat, two:PublicChat) => this.isGreater(one,two));
						});
					})
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
	* Comparator to sort Chats in descending creation time order 
	*/
	private isGreater(one:PublicChat, two:PublicChat):number{
		
		if(one.created > two.created){
			return -1;
		}
		
		if(one.created < two.created){
			return 1;
		}
		
		return 0;
	}
	
	/**
	* Comparator to sort Notifications in descending creation time order 
	*/
	private isGreaterNotification(one:PublicChatNotification, two:PublicChatNotification):number{
		
		if(one.created > two.created){
			return -1;
		}
		
		if(one.created < two.created){
			return 1;
		}
		
		return 0;
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
	
	private pageYPos = 0;
			
	@HostListener('window:scroll', ['$event']) onWindowScroll(e:any) {
	   	 
		if(this.newsFeedPane == "SINGLEPOST") {
			return;
		}
		
		try{
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

		} catch(error) {
			console.log(error);
		}

	}
	
}