import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders }  				from '@angular/common/http';
import { Observable }                 				from 'rxjs';
import { environment } 								from './../environments/environment';
import { CandidateProfile } from './candidate-profile';
//import { CandidateProfile }							from './candidate-profile/candidate-profile';

@Injectable({
  providedIn: 'root'
})
export class CandidateProfileService {

	constructor(private httpClient: HttpClient) { }

	httpOptions = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }), withCredentials: true
    };

	/**
	* Returns Alerts for authenticated Recruiter
	*/
	public fetchOwnCandidateProfile(): Observable<CandidateProfile>{
		
		const backendUrl:string = environment.backendUrl +'candidate-profile/own';
  
    	return this.httpClient.get<any>(backendUrl, this.httpOptions);

	}
}
