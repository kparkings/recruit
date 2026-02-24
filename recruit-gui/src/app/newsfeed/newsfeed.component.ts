import { Component, ElementRef, ViewChild, HostListener } 				from '@angular/core';
import { PublicMessagingService}										from '../public-messaging.service';
import { UntypedFormGroup, UntypedFormControl }							from '@angular/forms';
import { PublicChat, ChatParticipant, PublicChatNotification}			from './public-chat';
import { AppComponent } 												from 'src/app/app.component';
import { ViewportScroller } 											from '@angular/common';
import { Photo } from '../private-messaging.service';

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
	private currentNotification:string = "";
	private showNotificationsForMobile:string = "";
	
	private scheduleOpenChatRefresh = window.setInterval(()=> {},60000);
	
	public usersOwnChatParticipant:ChatParticipant = new ChatParticipant("", "", "", "", new Photo("",""), false);
	
	/**
	* Constructor
	* @oaramparam service  - Services for PublicMessages 
	*/
	public constructor(	public service:PublicMessagingService, 
						private readonly appComponent:AppComponent,
						private readonly scroller: ViewportScroller,){
		
		this.service.fetchOwnParticipant().subscribe(participant => {
			this.usersOwnChatParticipant = participant;
		});
				
	}
	
	public toggleReceiveNotificationEmails():void{
		this.service.toggleOwnParticipantReceiveNotificationEmails().subscribe(participant => {
				this.usersOwnChatParticipant = participant;
		});
	}
	
	ngAfterViewInit(){
		
		this.refreshPosts();
		this.startNotificationPolling();
	}
	
	ngAfterViewChecked(){
		if (localStorage.getItem("display-news-item-notifications") == "true") {
			this.showNotificationsForMobile = "SHOW";
			localStorage.removeItem("display-news-item-notifications");
		}
		
		if (localStorage.getItem("display-news-item-notifications") == "false") {
			this.showNotificationsForMobile = "";
			localStorage.removeItem("display-news-item-notifications");
		}		
	}
	
	ngOnDestroy(){
		clearInterval(this.scheduleOpenChatRefresh);
	}
	
	private startNotificationPolling():void{
		this.refreshNotifications();	
		this.scheduleOpenChatRefresh = window.setInterval(()=> {
			this.refreshNotifications();			
		},60000);
	}
	
	/**
	* In web we always show notification. In mobile by default we use css
	* to hide the div. To make it visible we add the values 
	* of showNotificationsForMobile to the end of the css class name with a 
	* non empty string value. This causes the css class name to be chnaged to 
	* a non existent class name and therefore removing the display_none value.
	* 
	* By setting showNotificationsForMobile to "" the original css class 
	* with the display:none attribue is again called.
	* 
	* The name of the css class is: newsfeed-notification-panel
	* 
	* The code to make it visible is initiated in the menu bar which sets
	* a localStorage item which is set in this component when the component 
	* is loaded.
	*  
	*/
	public showNotificationForMobile():string{
		return this.showNotificationsForMobile;
	}
	
	/**
	* Hides the notification panel when viewing the site on
	* a mobile device
	*/
	public hideNotificationsForMobile():void{
		this.showNotificationsForMobile = "";
	}
	
	private refreshNotifications():void{
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
	}
	
	public notificationPath:Array<string> = new Array<string>(); 
	public loadNotification(notification:PublicChatNotification):void{
		this.hideNotificationsForMobile();
		this.newsFeedPane = "SINGLEPOST"
		this.service.fetchPathToChat(notification.chatId).subscribe(chatPath => {
			this.notificationPath = chatPath;
			this.service.fetchChatById(chatPath[0]).subscribe(chat =>{
				this.service.setViewedStatusForChat(notification.notificationId, true).subscribe(res => {
						this.refreshNotifications();
				});
				this.topLevelPosts = new Array<PublicChat>();
				this.topLevelPosts.push(chat);
				this.topLevelPosts.forEach(tlp => {
					this.service.fetchChatChildren(tlp.id).subscribe(replies => {
						tlp.replies = replies;
						tlp.replies.sort((one:PublicChat, two:PublicChat) => this.isGreater(one,two));
						if (this.notificationPath.length > 0) {
							tlp.replies.forEach(reply => {
								if(this.notificationPath.indexOf(tlp.id) >= 0 ){	
									tlp.showReplies = true;	
									this.currentNotification = 'chat-id-'+ notification.chatId;
									setTimeout(() => {
									  this.scroller.scrollToAnchor('chat-id-'+notification.chatId);
									}, 125);
								}
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
	
	/**
	* Toggles whether the Notification has been viewed by the User. Initially this will be 
	* false and set to true once they have viewed the Notification but the User can 
	* also toggle the value manually 
	* @param notification - Notification to toggle
	*/
	public toggleNotificationViewed(notification:PublicChatNotification):void{
		this.service.setViewedStatusForChat(notification.notificationId, !notification.viewed).subscribe(res => {
			this.refreshNotifications();
		});
	}
	
	/**
	* Deletes the notification for the User
	* @param notification - Notification to toggle
	*/
	public deleteNotification(notification:PublicChatNotification):void{
		this.service.deleteNotification(notification.notificationId).subscribe(res => {
			this.refreshNotifications();
		});
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