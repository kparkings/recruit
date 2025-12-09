import { Component } 													from '@angular/core';
import { AppComponent } 												from 'src/app/app.component';
import { PrivateChatAPIOutbound, PrivateMessagingService } 				from '../private-messaging.service';
import { CandidateServiceService, CandidateSuggestionAPIOutbound} 		from '../candidate-service.service';
import { RecruiterService, RecruiterBasicInfoAPIOutbound} 				from '../recruiter.service';
import { CurrentUserAuth }												from '../current-user-auth';
import { SuggestionsSearchRequest }										from '../suggestions/suggestion-search-request';

/**
* Backing Component for the Chat window 
*/
@Component({
  selector: 'app-private-messaging',
  standalone: false,
  templateUrl: './private-messaging.component.html',
  styleUrl: './private-messaging.component.css'
})
export class PrivateMessagingComponent {

	public chatViewItem:string 										= "contacts";
	public viewItemContactsSeleted:string 							= "";
	public viewItemMessageSeleted:string 							= "view-item-selected";
	public currentUserAuth:CurrentUserAuth 							= new CurrentUserAuth();
	public chats:Array<PrivateChatAPIOutbound>						= new Array<PrivateChatAPIOutbound>();
	public candidates:Array<CandidateSuggestionAPIOutbound>			= new Array<CandidateSuggestionAPIOutbound>();
	public recruiters:Array<RecruiterBasicInfoAPIOutbound>			= new Array<RecruiterBasicInfoAPIOutbound>();
	public currentChat:PrivateChatAPIOutbound | undefined;
		
	
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
	
	/**
	* Opens the chat window.
	* If the Chat is new, sends request to create the Chat between the Recruiter and the Candidate.
	* Once Chat's are retrieved fetches the Candidate details for each of the Chats
	*/
	public openChat(candidateId:string):void{
				
		this.chatService.fetchChatsByUserId().subscribe(chats => {
			
			this.chats = chats;
			
			if (this.chats.filter(c => 
					c.senderId == this.currentUserAuth.getLoggedInUserId() 	&& c.recipientId == candidateId
				|| 	c.senderId == candidateId 								&& c.recipientId == this.currentUserAuth.getLoggedInUserId()).length == 0
			) {
				this.chatService.createChat(this.currentUserAuth.getLoggedInUserId(), candidateId).subscribe(response => {
						this.fetchChats();
				})
			}
		});
		
		this.chatViewItem = "message";
	}
	
	/**
	* Fetches the Chat's for the User. It will subsequently 
	* retrieve all the Candidates involved in the Chat's 
	*/
	private fetchChats():void{
		
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
			this.recruiterService.getRecruitersByIds(recruiterIds).subscribe(result => {
				this.recruiters = result;
			})
			
			/**
			* Fetch candidates 
			*/
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
		});			
	}
	
	/**
	* Sets the current chat and then retrieves the represenation containing the 
	* replies
	* @param chat selected from Chat list.
	*/
	public showChatMessages(currentChat:PrivateChatAPIOutbound):void{
		this.showMessageItemView();
		this.chatService.fetchPrivateChatById(currentChat.id).subscribe(chat => {
			this.currentChat = chat;
		});
		
		//TODO:[KP] If clicking on contacts when in messages need to scroll to previous position in contact list
	}
	
	/**
	* Display name of the User in the Chat
	* TODO: [KP] When candidate logged in need to retrieve recruiters and return Recruiters name
	* TODO: [KP] Need to take account of Admin account. Edge case which can start chat with any user. 
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
	* Minimizes the chat window as it is opened
	* and ensure contact list view is displayed
	*/
	public maximizeOnOpen():void{
		this.chatViewItem = "contacts";
		this.maximizeChat();
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
	
}	