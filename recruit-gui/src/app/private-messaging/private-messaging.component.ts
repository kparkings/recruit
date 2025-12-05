import { Component } 					from '@angular/core';
import { AppComponent } 				from 'src/app/app.component';
import { PrivateChatAPIOutbound } 		from '../private-messaging.service';

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

	public chatViewItem:string 						= "message";
	public viewItemContactsSeleted:string 			= "";
	public viewItemMessageSeleted:string 			= "view-item-selected";
	
	public chats:Array<PrivateChatAPIOutbound>		= new Array<PrivateChatAPIOutbound>();
	
	/**
	* Constructor
	* @param appComponent - Main component, Chat window component 
	* 						is embedded here and we need to share 
	* 						information between the two components 
	*/
	constructor(private readonly appComponent:AppComponent){
		
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