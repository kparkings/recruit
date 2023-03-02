import { Component } 			from '@angular/core';
import { EmailService} 			from '../email.service';
import { Email} 				from './email';
import { EmailAttachment} 		from './email-attachment';
import { environment }			from '../../environments/environment';

/**
* Internal Email client
*/
@Component({
  selector: 'app-email',
  templateUrl: './email.component.html',
  styleUrls: ['./email.component.css']
})
export class EmailComponent {

	public emails:Array<Email> 		= new Array<Email>();
	public currentView:string 		= "view-inbox";
	public currentEmail:Email 		= new Email();
	/**
	* Constrcutor
	*/
	constructor(private emailService:EmailService){
		this.fetchEmails();
	}
	
	/**
	* Shows the selected email
	*/
	public viewEmail(email:Email):void{
		this.currentView	= "view-read";
		this.currentEmail 	= email;
		this.markAsRead();
		console.log(this.currentEmail.body);
	}
	
	/**
	* Returns to inbox
	*/
	public viewInbox():void{
		this.currentView	= "view-inbox";
		this.currentEmail 	= new Email();
	}

	/**
	* Sends request to mark Email as Unread
	*/
	public markAsUnread():void{
		
	}
	
	/**
	* Sends request to mark Email as Read
	*/
	public markAsRead():void{
		
	}

	/**
	* Retrieves Emails from backend
	*/
	private fetchEmails():void{
		this.emailService.fetchEmails().subscribe(emails => {
			this.emails = emails;	
		}, err => {
			console.log(err);
		});
	}
	
	/**
	*  Returns the url to perform the download of the candidates CV
	*/
	public getAttachmentUrl(attachment:EmailAttachment, email:Email){
		return  environment.backendUrl + "email/" + email.id + "/attachment/" + attachment.id;
	}


}
