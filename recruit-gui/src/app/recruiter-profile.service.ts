import { Injectable } 								from '@angular/core';
import { HttpClient, HttpHeaders }  				from '@angular/common/http';
import { Observable }                 				from 'rxjs';
import { environment } 								from './../environments/environment';
import { AddRecruiterProfileRequest }				from './recruiter-profile/add-recruiter-profile';
import { RecruiterProfile }							from './recruiter-profile/recruiter-profile';

@Injectable({
  providedIn: 'root'
})
export class RecruiterProfileService {

	constructor(private httpClient: HttpClient) { }

	httpOptions = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }), withCredentials: true
    };

	/**
	* Adds a new Recruiter Profile
	*/
	public addProfile(request:AddRecruiterProfileRequest, profileImage:File):Observable<any>{
		
		const backendUrl:string = environment.backendUrl + 'recruiter-profile';

		var fd = new FormData();
		fd.append('file', profileImage);
  		fd.append("profile", new Blob([JSON.stringify(request)], { type: 'application/json' }));
		
		return this.httpClient.post<any>(backendUrl, fd, {headers: new HttpHeaders({ }), withCredentials: true});
		
	}

	/**
	* Adds a new Recruiter Profile
	*/
	public updateProfile(request:AddRecruiterProfileRequest, profileImage:File):Observable<any>{
		
		const backendUrl:string = environment.backendUrl + 'recruiter-profile';
  
		var fd = new FormData();
		fd.append('file', profileImage);
  		fd.append("profile", new Blob([JSON.stringify(request)], { type: 'application/json' }));
		
		return this.httpClient.put<any>(backendUrl, fd, {headers: new HttpHeaders({ }), withCredentials: true});
		
	}
	
	/**
	* Returns Alerts for authenticated Recruiter
	*/
	public fetchOwnRecruiterProfile(): Observable<RecruiterProfile>{
		
		const backendUrl:string = environment.backendUrl +'recruiter-profile/own';
  
    	return this.httpClient.get<any>(backendUrl, this.httpOptions);

	}

	/**
	* Returns Alerts for authenticated Recruiter
	*/
	public fetchRecruiterProfiles(type:string): Observable<Array<RecruiterProfile>>{
		
		const backendUrl:string = environment.backendUrl +'recruiter-profile?visibilityType=' + type;
  
    	return this.httpClient.get<any>(backendUrl, this.httpOptions);

	}
		
}
