import { Injectable } 								from '@angular/core';
import { HttpClient, HttpHeaders }  				from '@angular/common/http';
import { Observable, BehaviorSubject  }             from 'rxjs';
import { environment } 								from './../environments/environment';
import { Email } 									from './email/email';
import { OpenPosition } 							from './recruiter-marketplace/open-position';
import { OfferedCandidate } 						from './recruiter-marketplace/offered-candidate';

/**
* Service for sending emails
*/
@Injectable({
  providedIn: 'root'
})
export class EmailService {

	public unseenEmails = new BehaviorSubject(0);
	
	/**
	* Updates the unseenEmails count
	*/
	public updateUnseenEmails():void{
		this.fetchEmails().subscribe(emails => {
			this.unseenEmails.next(emails.filter(a => a.viewed == false).length);
		});
	}
	
	/**
	* Returns number of unseen email as observable
	*/
	public fetchUnseenEmailsCount():Observable<number>{
		return this.unseenEmails;
	}

	/**
 	* Constructor
	* @param httpClient - for sending httpRequests to backend
	*/
	constructor(private httpClient: HttpClient) {
		this.updateUnseenEmails();
	}

	httpOptions = {
		headers: new HttpHeaders({ 'Content-Type': 'application/json' }), withCredentials: true
	};

	/**
	* Registers an event showing that a Listing was viewed
	*/
	public sendEmail(emailRequest:EmailRequest, listingId:string):Observable<any>{
	
	
		let attachment:File = emailRequest.attachment;
		
		var fd = new FormData();
  		fd.append('attachment', 	attachment);
		fd.append("senderName",		emailRequest.senderName);
		fd.append("senderEmail",	emailRequest.senderEmail);
		fd.append("message",		emailRequest.message);

		const backendUrl:string = environment.backendUrl +'listing/public/'+listingId+'/contact-recruiter';
		
		return this.httpClient.post<any>(backendUrl, fd, {headers: new HttpHeaders({ }), withCredentials: true});
				
	}
	
	/**
	* Sends Email to owner of Open Position on the Marketplace
	*/
	public sendMarketplaceContactRequestOpenPositionEmail(emailRequest:EmailRequest, openPosition:OpenPosition):Observable<any>{
	
		var fd = new FormData();
  		
		fd.append("message",		emailRequest.message);

		const backendUrl:string = environment.backendUrl +'v1/open-position/'+openPosition.id+'/_message';
		
		return this.httpClient.put<any>(backendUrl, fd, {headers: new HttpHeaders({ }), withCredentials: true});
				
	}
	
	/**
	* Sends Email to owner of Open Position on the Marketplace
	*/
	public sendMarketplaceContactRequestOfferedCandidateEmail(emailRequest:EmailRequest, offeredCandidate:OfferedCandidate):Observable<any>{
	
		var fd = new FormData();
  	
		fd.append("message",		emailRequest.message);

		const backendUrl:string = environment.backendUrl +'v1/offered-candidate/'+offeredCandidate.id+'/_message';
		
		return this.httpClient.put<any>(backendUrl, fd, {headers: new HttpHeaders({ }), withCredentials: true});
				
	}
	
	/**
	* Sends Email to owner of Open Position on the Marketplace
	*/
	public sendRecruiterContactEmail(emailRequest:EmailRequest, recruiterId:string):Observable<any>{
	
		var fd = new FormData();
  	
		fd.append("message",	emailRequest.message);
		fd.append("title",		emailRequest.title);

		const backendUrl:string = environment.backendUrl +'v1/recruiter-profile/'+recruiterId+'/_message';
		
		return this.httpClient.put<any>(backendUrl, fd, {headers: new HttpHeaders({ }), withCredentials: true});
				
	}
	
	/**
	* Retrieves Emails sent to the Authenticated User
	*/
	public fetchEmails():Observable<Array<Email>>{
		
		const backendUrl:string = environment.backendUrl +'email';
  
    	return this.httpClient.get<any>(backendUrl, this.httpOptions);
		
		
	}
	
	/**
	* Retrieves Emails sent to the Authenticated User
	*/
	public fetchAttachmentFile(emailId:string, attachmentId:string):Observable<any>{
		
		const backendUrl:string = environment.backendUrl +'email/' + emailId + "/attachment/" + attachmentId  ;
  
    	return this.httpClient.get<any>(backendUrl, this.httpOptions);
		
		
	}
	
	/**
	* Updates read status for the email for the current user. User determines via JWT on 
	* the backend
	*/
	public setReadStatus(emailId:string, read:boolean):Observable<any>{
		
		const backendUrl:string = environment.backendUrl + 'email/' + emailId + "/" + (read ? "read" : "unread");
  
		return this.httpClient.put<any>(backendUrl, {}, this.httpOptions);
		
	}

	/**
	* Updates read status for the email for the current user. User determines via JWT on 
	* the backend
	*/
	public sendReply(emailId:string, replyMessage:string):Observable<any>{
		
		const backendUrl:string = environment.backendUrl + 'email/' + emailId + "/reply";
  
		var fd = new FormData();
  	
		fd.append("message",		replyMessage);
		
		return this.httpClient.put<any>(backendUrl, fd, {headers: new HttpHeaders({ }), withCredentials: true});
		
	}
	
}

/**
* Request for an Email to Be sent
*/
export class EmailRequest{
	public senderName!:string;
	public senderEmail!:string;
	public title!:string;
	public message!:string;
	public attachment!:File;
}