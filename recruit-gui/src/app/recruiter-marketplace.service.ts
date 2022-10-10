import { Injectable }                             	from '@angular/core';
import { HttpClient, HttpResponse, HttpHeaders }  	from '@angular/common/http';
import { Observable, throwError }                 	from 'rxjs';
import { environment }								from './../environments/environment';
import { NewOfferedCandidate } 						from './recruiter-marketplace/new-offered-candidate';
import { OfferedCandidate } 						from './recruiter-marketplace/offered-candidate';

@Injectable({
  providedIn: 'root'
})
export class RecruiterMarketplaceService {

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
	* Fetches Candidates being offered by Recruiters
	*/
	public fetchOfferedCandidates(): Observable<Array<OfferedCandidate>>{
		
		const backendUrl:string = environment.backendUrl +'v1/offered-candidate/';
		
		return this.httpClient.get<any>(backendUrl, this.httpOptions);
	}
	
	/**
	* Fetches Candidates being offered by Recruiters
	*/
	//TODO: Why not get this from SC and if not make sure check done on BE for correct Recruiter
	public fetchRecruitersOwnOfferedCandidates(recruiterId:string): Observable<Array<OfferedCandidate>>{
		
		const backendUrl:string = environment.backendUrl +'v1/offered-candidate/rectuiter/'+recruiterId;
		
		return this.httpClient.get<any>(backendUrl, this.httpOptions);
	}

	/**
	* Deletes a Candidate being offered by Recruiters
	*/
	public deleteRecruitersOwnOfferedCandidates(candidateId:string): Observable<Array<OfferedCandidate>>{
		
		const backendUrl:string = environment.backendUrl +'v1/offered-candidate/'+candidateId;
		
		return this.httpClient.delete<any>(backendUrl, this.httpOptions);
	}
		
	/**
	* Registers a Listing with the backend
	*/
	public registerOfferedCandidate(candidateRoleTitle:string, 
									country:string,
									location:string,
									contractType:string,
									daysOnSite:string,
									renumeration:string,
									availableFromDate:string,	
									yearsExperience:string,	
									description:string,
									comments:string, 			
									languages:Array<string>,
									skills:Array<string>):Observable<any>{
		
		const backendUrl:string = environment.backendUrl +'v1/offered-candidate/';
		
		let offeredCandidate:NewOfferedCandidate = new NewOfferedCandidate();
		
		offeredCandidate.candidateRoleTitle 	= candidateRoleTitle; 
		offeredCandidate.country 				= country;
		offeredCandidate.location 				= location;
		offeredCandidate.contractType 			= contractType;
		offeredCandidate.daysOnSite 			= daysOnSite;
		offeredCandidate.renumeration 			= renumeration;
		offeredCandidate.availableFromDate 		= availableFromDate;	
		offeredCandidate.yearsExperience 		= yearsExperience;	
		offeredCandidate.description 			= description;
		offeredCandidate.comments 				= comments; 			
		offeredCandidate.spokenLanguages 		= languages;
		offeredCandidate.coreSkills 			= skills;
		
		return this.httpClient.post<any>(backendUrl, JSON.stringify(offeredCandidate), this.httpOptions);
	
	}
	
	/**
	* Registers a Listing with the backend
	*/
	public updateOfferedCandidate(	candidateId:string,
									candidateRoleTitle:string, 
									country:string,
									location:string,
									contractType:string,
									daysOnSite:string,
									renumeration:string,
									availableFromDate:string,	
									yearsExperience:string,	
									description:string,
									comments:string, 			
									languages:Array<string>,
									skills:Array<string>):Observable<any>{
		
		const backendUrl:string = environment.backendUrl +'v1/offered-candidate/'+candidateId;
		
		let offeredCandidate:NewOfferedCandidate = new NewOfferedCandidate();
		
		offeredCandidate.candidateRoleTitle 	= candidateRoleTitle; 
		offeredCandidate.country 				= country;
		offeredCandidate.location 				= location;
		offeredCandidate.contractType 			= contractType;
		offeredCandidate.daysOnSite 			= daysOnSite;
		offeredCandidate.renumeration 			= renumeration;
		offeredCandidate.availableFromDate 		= availableFromDate;	
		offeredCandidate.yearsExperience 		= yearsExperience;	
		offeredCandidate.description 			= description;
		offeredCandidate.comments 				= comments; 			
		offeredCandidate.spokenLanguages 		= languages;
		offeredCandidate.coreSkills 			= skills;
		
		return this.httpClient.put<any>(backendUrl, JSON.stringify(offeredCandidate), this.httpOptions);
	
	}
}
