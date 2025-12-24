import { Component, ViewChild } 																from '@angular/core';
import { UntypedFormControl, UntypedFormGroup } 												from '@angular/forms';
import { AppComponent } 																		from 'src/app/app.component';
import { PrivateChatAPIOutbound, ChatMessageAPIOutbound, PrivateMessagingService } 				from '../private-messaging.service';
import { CandidateServiceService, CandidateSuggestionAPIOutbound} 								from '../candidate-service.service';
import { RecruiterService, RecruiterBasicInfoAPIOutbound} 										from '../recruiter.service';
import { CurrentUserAuth }																		from '../current-user-auth';
import { SuggestionsSearchRequest }																from '../suggestions/suggestion-search-request';
	
/**
* Backing Component for the Chat window 
*/
@Component({
  selector: 'app-private-messaging',
  styleUrls: ['./private-messaging.component.css','./private-messaging.component-mob.css'],
  standalone: false,
  templateUrl: './private-messaging.component.html',
  
})
export class PrivateMessagingComponent {
	
	public readonly UNBLOCKED:string 		= "unblocked";
	public readonly BLOCKED:string 			= "blocked";
	public readonly BLOCKED_BY_OTHER_USER 	= "blocked-by-other-user";

	public chatViewItem:string 										= "contacts";
	public viewItemContactsSeleted:string 							= "view-item-selected";
	public viewItemMessageSeleted:string 							= "";
	public currentUserAuth:CurrentUserAuth 							= new CurrentUserAuth();
	public chats:Array<PrivateChatAPIOutbound>						= new Array<PrivateChatAPIOutbound>();
	public candidates:Array<CandidateSuggestionAPIOutbound>			= new Array<CandidateSuggestionAPIOutbound>();
	public recruiters:Array<RecruiterBasicInfoAPIOutbound>			= new Array<RecruiterBasicInfoAPIOutbound>();
	public currentChat:PrivateChatAPIOutbound | undefined;
	public userHasChatAccess:boolean								= false;	
	public lastKeyPress:Date										= new Date();
	
	/**
	* Constructor
	* @param appComponent 		- 	Main component, Chat window component 
	* 								is embedded here and we need to share 
	* 								information between the two components I
	* @param chatService  		- 	Services for Chats
	* @param candidateService	-	Services for Candidates
	*/
	constructor(
		private readonly appComponent:AppComponent, 
		private readonly chatService:PrivateMessagingService, 
		private readonly candidateService:CandidateServiceService, 
		private readonly recruiterService:RecruiterService){
	}
	
	public messageForm:UntypedFormGroup 	= new UntypedFormGroup({
		newMessage:			new UntypedFormControl(''),
	});
	
	/**
	* Opens the chat window.
	* If the Chat is new, sends request to create the Chat between the Recruiter and the Candidate.
	* Once Chat's are retrieved fetches the Candidate details for each of the Chats
	*/
	public openChat(candidateId:string):void{
		this.currentChat = undefined;
		this.currentReplies = new Array<ChatMessageAPIOutbound>();
		this.chatService.hasChatAccess().subscribe(res => {
					
			if (res == true) {

				this.chatService.fetchChatsByUserId().subscribe(chats => {
					
					this.chats = chats;
					
					let existingChat:PrivateChatAPIOutbound[] = this.chats.filter(c => 
												c.senderId == this.currentUserAuth.getLoggedInUserId() 	&& c.recipientId == candidateId
											|| 	c.senderId == candidateId 								&& c.recipientId == this.currentUserAuth.getLoggedInUserId());
					
					if (existingChat.length == 0 ) {
						this.chatService.createChat(this.currentUserAuth.getLoggedInUserId(), candidateId).subscribe(response => {
							this.fetchChats();
							this.chatService.fetchPrivateChatById(response).subscribe(res => {
								this.currentChat = res;
								this.showChatMessages(this.currentChat!);
								this.startReplyScheduler();
							});
						})
					} else {
						this.currentChat = existingChat[0];
						this.fetchChats();
						this.showChatMessages(this.currentChat!);
						this.startReplyScheduler();
					}
				});

				this.chatViewItem = "message";
				this.appComponent.currentChatWindowState = "maximized";
				
			} else {
				this.appComponent.openNoChatAccessBox();
			}
			
			
		});
	}
	
	/**
	* Fetches the Chat's for the User. It will subsequently 
	* retrieve all the Candidates involved in the Chat's 
	*/
	private fetchChats():void{
		
		if (this.appComponent.isAuthenticated()) {
		
			let searchRequest:SuggestionsSearchRequest = new SuggestionsSearchRequest();
			searchRequest.requestFilters.maxNumberOfSuggestions = 100000;
			let recruiterIds:Array<string> = new Array<string>();			
			this.chatService.fetchChatsByUserId().subscribe(chats => {
				
				this.chats = chats;
										
				chats.forEach(c => {
					searchRequest.candidateFilters.candidateIds.push(c.recipientId);
					recruiterIds.push(c.senderId);	
				})
				
				/**
				* Fetch recruiters 
				*/
				if (recruiterIds.length > 0 && !this.isRecruiter()) {
					this.recruiterService.getRecruitersByIds(recruiterIds).subscribe(result => {
						this.recruiters = result;
					})
				}
				
				/**
				* Fetch candidates 
				*/
				if (searchRequest.candidateFilters.candidateIds.length > 0) {
					this.candidateService.getCandidateSuggestions(searchRequest).subscribe(response => {
						
						let candidates:Array<CandidateSuggestionAPIOutbound> = response.body.content;
						
						candidates.forEach(candidate => {
							
							let candidateDetails:CandidateSuggestionAPIOutbound = new CandidateSuggestionAPIOutbound();
							
							candidateDetails.candidateId = candidate.candidateId;
							candidateDetails.firstname = candidate.firstname;
							candidateDetails.surname = candidate.surname;
							
							this.candidates.push(candidateDetails);
						});
						
					});
				}
			});			
		
		}
	}
	
	private scheduleOpenChatRefresh = window.setInterval(()=> {},1000);
	public otherUserTypeing:boolean = false;
	
	/**
	* Sets the current chat and then retrieves the represenation containing the 
	* replies
	* @param chat selected from Chat list.
	*/
	public showChatMessages(currentChat:PrivateChatAPIOutbound):void{
		this.setScrollOn();
		this.lastKeyPress = new Date();
		this.showMessageItemView();
		this.chatService.fetchPrivateChatById(currentChat.id).subscribe(chat => {
			this.currentChat = chat;
			
			//this.doScrollTop('boop');
			
			//Last viewed functionality
			this.chatService.doSetLastViewed(this.currentChat.id).subscribe(res => {
				
			})
			
			//User typing functionality
			this.otherUserTypeing = false;
			this.startReplyScheduler();
			
		});
	}
	
	/**@
	* Polls backend for new messages and updates
	* for the current chat
	*/
	private startReplyScheduler():void{
		
		this.scheduleOpenChatRefresh = window.setInterval(()=> {
			this.chatService.fetchPrivateChatById(this.currentChat!.id).subscribe(chat => {
				this.currentChat 		= chat;
				this.otherUserTypeing 	= false;
				this.getCurrentChatReplies();
				if (this.isUserChatRecipient()) {
					let lastKeyPress:Date =  this.currentChat.lastKeyPressSender;
					let lastKeyPressSecondsAgo = (new Date().getTime() - new Date(lastKeyPress).getTime())/1000;
					if (lastKeyPressSecondsAgo <= 2){
						this.otherUserTypeing = true 
					}	
				} else {
					let lastKeyPress:Date =  this.currentChat.lastKeyPressRecipient;
					let lastKeyPressSecondsAgo = (new Date().getTime() - new Date(lastKeyPress).getTime())/1000;
					if (lastKeyPressSecondsAgo <= 2){
						this.otherUserTypeing = true 
					}
				}
				
				
			});
		},1000);
	}
	
	private scheduleContactListRefresh = window.setInterval(()=> {},1000);
	public hasUnreadMessagesTracker:boolean = false;

	public getHasUnreadMessagesTracker():boolean{
		return this.hasUnreadMessagesTracker;
	}
	
	public startChatContactListPolling():void{
		this.scheduleContactListRefresh = window.setInterval(()=> {
			this.fetchChats();		
			this.hasUnreadMessagesTracker = this.hasUnreadMessages();
		},20000);
	}
	
	
	public hasUnreadMessages():boolean{
		
		let hasUnreadMessages:boolean = false;
		
		this.chats.forEach(chat => {
			if (this.isChatRecipient(chat)) {
				if (new Date(chat.lastViewedByRecipient).getTime() <  new Date(chat.lastUpdated).getTime()){
					hasUnreadMessages = true;
				}
			} else {
				if (new Date(chat.lastViewedBySender).getTime() <  new Date(chat.lastUpdated).getTime()) {
					hasUnreadMessages = true;
				}
			}
		});
		
		return hasUnreadMessages;
	}
	
	public hasUnviewedMessages(chat:PrivateChatAPIOutbound):boolean{
		
		if (this.isChatRecipient(chat)) {
			return new Date(chat.lastViewedByRecipient).getTime() <  new Date(chat.lastUpdated).getTime();
		} else {
			return new Date(chat.lastViewedBySender).getTime() <  new Date(chat.lastUpdated).getTime();
		}
		
	}
	
	
	/**
	* Returns if current User is the Chat Recipient 
	*/
	private isChatRecipient(chat:PrivateChatAPIOutbound):boolean{
		
		let user = sessionStorage.getItem("userId");
		
		return chat.recipientId == user;
	}
	
	/**
	* Returns if current User is the Chat Recipient 
	*/
	private isUserChatRecipient():boolean{
		
		let user = sessionStorage.getItem("userId");
		
		return this.currentChat?.recipientId == user;
	}
	
	/**
	* Returns if current User is the Chat Sender 
	*/
	private isUserChatSender():boolean{
		
		let user = sessionStorage.getItem("userId");
				
		
		return this.currentChat?.senderId == user;
	}
	
	/**
	* Retuens the name of the User who the user is chatting with in the 
	* currently selected chat.
	*/
	public getSelectedChatOtherUserName():string {
		return this.getUserName(this.currentChat?.senderId || '-', this.currentChat?.recipientId || '-');
	}
	
	/**
	* Display name of the User in the Chat
	*/
	public getUserName(senderId:string,recipientId:string):string{
		
		let fn:string | undefined;
		let sn:string | undefined;
		
		if (this.isCandidate()) {
		
			if (senderId == "kparkings") {
				return "Kevin Parkings";
			}
			
			let recruiter = this.recruiters.filter(c => c.recruiterId == senderId)[0];
			
			if (!recruiter) {
				return "-";
			}
			
			fn = recruiter.firstname.charAt(0).toUpperCase() + recruiter.firstname.slice(1);
			sn = recruiter.surname.charAt(0).toUpperCase() + recruiter.surname.slice(1);
						
		} else {
			
			if (recipientId == "kparkings") {
				return "Kevin Parkings";
			}
						
			let candidate = this.candidates.filter(c => c.candidateId == recipientId)[0];
					
			if (!candidate) {
				return "-";
			}
			
			fn = candidate.firstname.charAt(0).toUpperCase() + candidate.firstname.slice(1);
			sn = candidate.surname.charAt(0).toUpperCase() + candidate.surname.slice(1);

						
		}
		
		let fullName = fn + " " + sn;
		
		if (fullName.length >= 50) {
			return fullName.substring(0,48) + "..";
		} else {
			return fullName;	
		}
		 
	}

	/**
	* Shows the list of the User's contacts in the 
	* Chat window 
	*/
	public showContactsItemView():void{
		this.chatViewItem = "contacts";
		this.viewItemContactsSeleted = "view-item-selected";
		this.viewItemMessageSeleted = "";
		this.fetchChats();
		clearInterval(this.scheduleOpenChatRefresh);
	}
	
	/**
	* Switches the displayed pane in the Chat window 
	* to the message view where the User can read messages
	* from and send messages to the selected User
	*/
	public showMessageItemView():void{
		this.chatViewItem = "message";	
		this.viewItemContactsSeleted = "";
		this.viewItemMessageSeleted = "view-item-selected";	
	}
	 
	/**
	* Returns the current state of the Chat windon
	* - Closed
	* - Minimized
	* - Maximized 
	*/
	public getChatWindowState():string{
		return this.appComponent.currentChatWindowState;
	}
	
	/**
	* Minimizes the chat window
	*/
	public maximizeChat():void{
		this.appComponent.currentChatWindowState = "maximized";
		this.fetchChats();
	}
	
	/**
	* Maximizes the chat window as it is opened
	* and ensure contact list view is displayed
	*/
	public maximizeOnOpen():void{
		this.chatService.hasChatAccess().subscribe(res => {
							
			if (res == true) {
				this.chatViewItem = "contacts";
				this.maximizeChat();
			} else {
				this.appComponent.openNoChatAccessBox();
			}	
		});
	}
		
	/**
	* Minimizes the chat window
	*/
	public minimizeChat():void{
		this.appComponent.currentChatWindowState 	= "minimized";
	}
	
	/**
	* Closes the chat window 
	*/
	public closeChat():void{
		this.appComponent.currentChatWindowState = "closed";
		clearInterval(this.scheduleOpenChatRefresh);
	}
	
	//TODO: Used in other places. Add to environemnt and refactor everywhere
	/**
	* Whether or not User is a Recruiter
	*/
	public isRecruiter():boolean{
		return sessionStorage.getItem('isRecruiter') === 'true';
	}
	
	/**
	* Whether or not the Use is a Candidate
	*/
	public isCandidate():boolean{
		return sessionStorage.getItem('isCandidate') === 'true';
	}
	
	/**
	* Whether or not the recruiter that has no open Subscriptiion
	*/
	public isRecruiterNoSubscription():boolean{
		return sessionStorage.getItem('isRecruiterNoSubscription') === 'true';
	}
	
	/**
	* Scrolls to top of page
	*/
	public doScrollTop(elementId:string):void{
		(<HTMLInputElement>document.getElementById(elementId)).scrollIntoView({
			block: "start",
			inline: "nearest"
			});
	}
	
	/**
	* Sens a message to the other user in the current Chat 
	*/
	public sendMessage():void{
		
		let msg:string = this.messageForm.get('newMessage')?.value;
		
		this.messageForm = new UntypedFormGroup({
			newMessage:			new UntypedFormControl(''),	
		});
		
		this.chatService.addMessageToExistingChat(this.currentChat!.id || '', msg).subscribe(res => {
			this.chatService.fetchPrivateChatById(this.currentChat!.id || '').subscribe(chat => {
				this.currentChat = chat;	
				this.doScrollTop('boop');
			});
			
		});
		
	}
	
	/**
	* Compares messages so the last added message appears at the bottom
	* of the list
	*/
	private compareMessages(a:ChatMessageAPIOutbound, b:ChatMessageAPIOutbound) {
		
		if (a.created > b.created) {
			return 1;
		}
		
		if(b.created > a.created) {
			return -1;
		}
		
		return 0;
	}
	
	/**
	* Compares Chats so the last modified chat appears at the top
	* of the list
	*/
	private compareChats(a:PrivateChatAPIOutbound, b:PrivateChatAPIOutbound) {
			
		if (a.lastUpdated > b.lastUpdated) {
			return -1;
		}
		
		if(b.lastUpdated > a.lastUpdated) {
			return 1;
		}
		
		return 0;
	}
	
	/**
	* Returns sorted chats
	*/
	public getChats():Array<PrivateChatAPIOutbound>{
		return this.chats.sort(this.compareChats);
	}
	
	/**
	* Takes the replies to the current chat and orders them to the 
	* latest message is shown at the bottom and scrolls to that 
	* message
	*/
	public currentReplies:Array<ChatMessageAPIOutbound> = new Array<ChatMessageAPIOutbound>();
	public scrollOff:boolean = false;
	
	public getCurrentChatReplies():void{
		
		let replies:Array<ChatMessageAPIOutbound> = new Array<ChatMessageAPIOutbound>();
		
		const map:Map<string,ChatMessageAPIOutbound> = new Map(Object.entries(this.currentChat!.replies));

		map.forEach((value: ChatMessageAPIOutbound, key: string) => {
			replies.push(value);
		});
		
		this.currentReplies = replies.sort(this.compareMessages);
			
		if (this.scrollOff == false) {
			this.doScrollTop('boop');
			this.doScrollTop('pageTop');
		}
		
	}
	
	public setScrollOff():void{
		this.scrollOff = true;
	}
	
	public setScrollOn():void{
			this.scrollOff = false;
		}
	
	/**
	* Deletes the Chat message 
	*/
	public deleteReply(msg:ChatMessageAPIOutbound):void{
		this.chatService.deleteMessageFromExistingChat(this.currentChat!.id, msg.id).subscribe(res => {
			this.chatService.fetchPrivateChatById(this.currentChat!.id || '').subscribe(chat => {
				this.currentChat = chat;	
				this.doScrollTop('boop');
			});	
		});
		
	}	
	
	/**
	* Returns the blocked state of the Chat
	* - unblocked
	* - blocked ( by current user)
	* - blocked-by-other-user 
	*/
	public getBlockedStatus():string{
		
		if (this.currentChat) {
			if (this.currentChat!.blockedBySender == false && this.currentChat!.blockedByRecipient == false) {
				return this.UNBLOCKED;
			}
			
			if (this.currentChat!.blockedByRecipient && sessionStorage.getItem("userId") == this.currentChat!.recipientId) {
				return this.BLOCKED;	
			}
			
			if (this.currentChat!.blockedBySender && sessionStorage.getItem("userId") == this.currentChat!.senderId) {
				return this.BLOCKED;	
			}
		}
		
		return this.BLOCKED_BY_OTHER_USER;
	
	}
	
	/**
	* Blocks the current Chat so that the other User can no longer message
	* the current USer 
	*/
	public toggleBlockUserChat():void{
		
		let doBlock:boolean = false;
		
		if (this.getBlockedStatus() == this.UNBLOCKED){
			doBlock = true;
		} 
		
		if (this.getBlockedStatus() != this.BLOCKED_BY_OTHER_USER) {
			this.chatService.setBlockedStatus(this.currentChat!.id, doBlock).subscribe(res => {
				this.chatService.fetchPrivateChatById(this.currentChat!.id || '').subscribe(chat => {
					this.currentChat = chat;	
					this.doScrollTop('boop');
				});	
			});
		}
		
	}
	
	/**
	* Returns whether a given message was created by the authenticated
	* user or another user
	* @param msg: Message to be checked
	*/
	public isOwnMessage(msg:ChatMessageAPIOutbound):string{
		return msg.senderId == sessionStorage.getItem("userId") ? "true" : "false";
	}
	
	/**
	* Sends a request to the backend to indicate the User is typeing in the Chat. A check is done
	* so that a request is sent max, once every second. 
	*/
	public handleIsTyping():void{
		
		let currentTime:Date = new Date();
		let numSecondsSinceBackedLastInformed = (currentTime.getTime() - this.lastKeyPress.getTime())/1000;
		
		if (numSecondsSinceBackedLastInformed >= 1) {
			this.lastKeyPress = currentTime;
			this.chatService.doSetKeyPressed(this.currentChat!.id).subscribe(res => {
				
			});
		}
		
	}
	
}	