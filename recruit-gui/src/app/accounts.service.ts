import { Injectable } 								from '@angular/core';
import { HttpClient, HttpResponse, HttpHeaders }  	from '@angular/common/http';
import { Observable, throwError }                 	from 'rxjs';
import { environment }								from './../environments/environment';
import { NewAccountRequest } 						from './accounts/new-account-request';

@Injectable({
  providedIn: 'root'
})
export class AccountsService {

	constructor(private httpClient: HttpClient) { }

	httpOptions = {
		headers: new HttpHeaders({ 'Content-Type': 'application/json' }), withCredentials: true
	};

	headers = { 'content-type': 'application/json'};

	/**
  	* Created a new Recruiter account 
  	*/
  	public addCandidate(proposedUsername:string): Observable<any>{

        const backendUrl:string = environment.backendUrl +'account';
    
		let newAccountRequest:NewAccountRequest = new NewAccountRequest();

		newAccountRequest.proposedUsername=  proposedUsername;
		newAccountRequest.accountType = 'RECRUITER';

    	return this.httpClient.post<any>(backendUrl, JSON.stringify(newAccountRequest), this.httpOptions);

  }

}
