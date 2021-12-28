import { Injectable }								from '@angular/core';
import { HttpClient, HttpHeaders }  				from '@angular/common/http';
import { Observable }                 				from 'rxjs';
import { environment }								from './../environments/environment';
import { CreateRecruiter } 							from './accounts/create-recruiter';
import { RecruiterSignup }							from './recruiter-signup/signup-recruiter';

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
	* Registers the Recruiter with the backend
	*/
	public registerRecruiter(username:string, firstName:string, surname:string, email:string, companyName:string, language:string):Observable<any>{
		
		const backendUrl:string = environment.backendUrl +'recruiter';
		
		let recruiter:CreateRecruiter = new CreateRecruiter();
		
		recruiter.userId 		= username;
		recruiter.firstName 	= firstName;
		recruiter.surname 		= surname;
		recruiter.email 		= email;
		recruiter.companyName 	= companyName;
		recruiter.language 		= language;
		
		return this.httpClient.post<any>(backendUrl, JSON.stringify(recruiter), this.httpOptions);
	
	}
	
  	/**
  	* Returns a list of available Recruiters 
  	*/
  	public getRecruiters(): Observable<any>{
      
		const backendUrl:string = environment.backendUrl +'recruiter';
  
    	return this.httpClient.get<any>(backendUrl, this.httpOptions);
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


}