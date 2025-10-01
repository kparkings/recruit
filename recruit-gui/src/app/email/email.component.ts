import { Component, ElementRef, ViewChild } 	from '@angular/core';
import { EmailService} 							from '../email.service';
import { Email} 								from './email';
import { EmailAttachment} 						from './email-attachment';
import { environment }							from '../../environments/environment';
import { UntypedFormGroup, UntypedFormControl }	from '@angular/forms';
import { NgbModal, NgbModalOptions }			from '@ng-bootstrap/ng-bootstrap';
import { Observable, Observer }                 from 'rxjs';

/**
* Internal Email client
*/
@Component({
    selector: 'app-email',
    templateUrl: './email.component.html',
    styleUrls: ['./email.component.css'],
    standalone: false
})
export class EmailComponent {

	@ViewChild('replyBox', {static:true})replyDialogBox!: ElementRef<HTMLDialogElement>;
 	
//replyBox

	public emails:Array<Email> 		= new Array<Email>();
	public currentView:string 		= "view-inbox";
	public currentEmail:Email 		= new Email();
	
	/**
	* FormGroup for Reply to Email
	*/
	public replyFormGroup:UntypedFormGroup = new UntypedFormGroup({
		message:	new UntypedFormControl('')
	});
	
	/**
	* Constrcutor
	*/
	constructor(private emailService:EmailService, private modalService:NgbModal){
		if (sessionStorage.getItem("userId")) {		
			this.fetchEmails();
		}
	}
	
	/**
	* Shows the selected email
	*/
	public viewEmail(email:Email):void{
		this.currentView	= "view-read";
		this.currentEmail 	= email;
		this.markAsRead();
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
		this.emailService.setReadStatus(this.currentEmail.id, false).subscribe(response => {
			this.fetchEmails();
			this.emailService.updateUnseenEmails();
		});
	}
	
	/**
	* Sends request to mark Email as Read
	*/
	public markAsRead():void{
		this.emailService.setReadStatus(this.currentEmail.id, true).subscribe(response => {
			this.fetchEmails();
			this.emailService.updateUnseenEmails();
		});
	}

	/**
	* Retrieves Emails from backend
	*/
	private fetchEmails():void{
		this.emailService.fetchEmails().subscribe(emails => {
			
			this.emails = emails;	
			
			if(this.currentEmail.id != ''){
				this.currentEmail = this.emails.filter(e => e.id == this.currentEmail.id)[0];
			}
			
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

	/**
	*  Returns whether or not the User has at least one email
	*/
	public hasEmails():boolean{
		return this.emails.length >= 1;
	}
	
	public replyState:string = 'compose';
	
	/**
	* Opens editor to reply to the Email
	*/
	public composeReply():void{
		this.replyState = 'compose';
		//let options: NgbModalOptions = {
	    //	 centered: true
	   //};

		this.replyDialogBox.nativeElement.showModal();
		//this.modalService.open(replyBox, options);
	}
	
	/**
	*  Closes the popups
	*/
	//public closeModal(): void {
	//	this.modalService.dismissAll();
	//}
	

	/**
	* Sends a reply to the Email
	*/
	public sendReply():void{
		
		this.emailService.sendReply(this.currentEmail.id, this.replyFormGroup.get("message")?.value).subscribe(
			data => {
			
				this.replyState 	= 'success';
				
				new Observable((observer: Observer<any>) => {
  					observer.next(this.fetchEmails());
  					observer.complete();
				}).subscribe(res => {
					this.currentView 	= "view-inbox";
					this.currentEmail 	= new Email();
					this.replyFormGroup = new UntypedFormGroup({
						message:	new UntypedFormControl('')
					});	
				});
				
			}, 
			err => {
				this.replyState = 'failure';
			});
		
	}

}
