import { Injectable } 								from '@angular/core';
import { HttpClient, HttpHeaders }  				from '@angular/common/http';
import { Observable }                 				from 'rxjs';
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

	/**
 	* Constructor
	* @param httpClient - for sending httpRequests to backend
	*/
	constructor(private httpClient: HttpClient) {}

	httpOptions = {
		headers: new HttpHeaders({ 'Content-Type': 'application/json' }), withCredentials: true
	};

	/**
	* Registers an event showing that a Listing was viewed
	*/
	public sendEmail(emailRequest:EmailRequest, listingId:string):Observable<any>{
	
		//TODO: 1. On Server side APIInbound object for ApplyForPositionAPIInput
		//TODO: 2. Change this to send bean instead of tst
	
		let attachment:File = emailRequest.attachment;
		let test:string = emailRequest.message;
		
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

}

/**
* Request for an Email to Be sent
*/
export class EmailRequest{
	public senderName!:string;
	public senderEmail!:string;
	public message!:string;
	public attachment!:File;
}