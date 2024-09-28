import { Injectable }								from '@angular/core';
import { HttpClient, HttpHeaders }  				from '@angular/common/http';
import { Observable }                 				from 'rxjs';
import { environment }								from './../environments/environment';
import { RecruiterSignup }							from './recruiter-signup/signup-recruiter';
import { SubscriptionAPIInbound }					from './recruiter-account/subscription-api-inbound';
import { RecruiterUpdateRequest }					from './recruiter-account/recruiter-update-request';


@Injectable({
  providedIn: 'root'
})
export class RecruiterService {
  
	/**
	* Constructor
	* @param httpClient - for sending httpRequests to backend
	*/
	constructor(private httpClient: HttpClient) { }
	
	httpOptions = {
		headers: new HttpHeaders({ 'Content-Type': 'application/json' }), withCredentials: true
	};

	headers = { 'content-type': 'application/json'};
	
  	/**
  	* Returns a list of available Recruiters 
  	*/
  	public getRecruiters(): Observable<any>{
      
		const backendUrl:string = environment.backendUrl +'recruiter';
  
    	return this.httpClient.get<any>(backendUrl, this.httpOptions);
  	}

	/**
  	* Sends request to delete Recruiter and all related data 
  	*/
  	public deleteRecruiter(recruiterToDelete: string) {
        
        const backendUrl:string = environment.backendUrl +'recruiter/' + recruiterToDelete;
  
    	return this.httpClient.delete<any>(backendUrl, this.httpOptions);
    }
    
  	/**
  	* Returns a list of available Recruiters 
  	*/
  	public getOwnRecruiterAccount(): Observable<any>{
      
		const backendUrl:string = environment.backendUrl +'recruiter/me';
  
    	return this.httpClient.get<any>(backendUrl, this.httpOptions);
  	}

	/**
	* Sends request to register the Recruiter for a free trial
	*/
	public registerForFreeTrial(firstName:string, surname:string, email:string, company:string, preferredLanguage:string):Observable<any>{
		
		const backendUrl:string = environment.backendUrl +'public/recruiter';
		
		let recruiter:RecruiterSignup = new RecruiterSignup();
		
		recruiter.firstName 	= firstName;
		recruiter.surname 		= surname;
		recruiter.email 		= email;
		recruiter.companyName 	= company;
		recruiter.language 		= preferredLanguage;
		
		return this.httpClient.post<any>(backendUrl, JSON.stringify(recruiter), this.httpOptions);
		
	}
	
	/**
	* Sends a request to the backend to perform an action on a Recruiters subscription
	* @param recruiterId 	- Unique Id of the recruiter whose Subscription is being updated
	* @param subscriptionId - Unique Id of the subscription being updated
	* @param action			- Action to be performed
	*/
	public performSubscriptionAction(recruiterId:string, subscriptionId:string, action:string):Observable<any>{
		
		const backendUrl:string = environment.backendUrl + 'recruiter/' + recruiterId + '/subscription/' + subscriptionId + '/?action=' + action;
		
		return this.httpClient.put<any>(backendUrl, null, this.httpOptions);
		
	}
	
	/**
	* Requests a new Subscription of the given type for the Recruiter
	*/
	public requestNewSubscription(recruiterId:string, type:string):Observable<any>{
		
		const backendUrl:string = environment.backendUrl +'recruiter/' + recruiterId + '/subscription/';
		
		let subscription:SubscriptionAPIInbound = new SubscriptionAPIInbound();
		
		subscription.type = type;
		
		return this.httpClient.post<any>(backendUrl, JSON.stringify(subscription), this.httpOptions);
		
	}
	
	/**
    * Sends request to reset users password. Email will be sent if user exists 
    * @param email - Email address of the User
    */
	public resetPassword(email:string): Observable<void>{

        const backendUrl:string     = environment.backendUrl + 'recruiter/reset-password/'+email;
  
        return this.httpClient.put<any>(backendUrl, {}, this.httpOptions);

    }

	public updateRecruiter(recruiter:RecruiterUpdateRequest): Observable<void>{

        const backendUrl:string     = environment.backendUrl + 'recruiter';
  
        return this.httpClient.put<any>(backendUrl, recruiter, this.httpOptions);

    }

}