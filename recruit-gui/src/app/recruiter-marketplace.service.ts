import { Injectable }                             	from '@angular/core';
import { HttpClient, HttpResponse, HttpHeaders }  	from '@angular/common/http';
import { Observable, throwError }                 	from 'rxjs';
import { environment }								from './../environments/environment';
import { NewOfferedCandidate } 						from './recruiter-marketplace/new-offered-candidate';

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
		offeredCandidate.spokenLanguages 				= languages;
		offeredCandidate.coreSkills 				= skills;
		
		console.log("Sending Offered Candiadte");
		console.log("Y1" + offeredCandidate.candidateRoleTitle);
		console.log("Y2" + offeredCandidate.country);
		console.log("Y3" + offeredCandidate.location);
		console.log("Y4" + offeredCandidate.spokenLanguages);
		console.log("Y5" + offeredCandidate.coreSkills);
		
		return this.httpClient.post<any>(backendUrl, JSON.stringify(offeredCandidate), this.httpOptions);
	
	}
}
