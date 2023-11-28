import { Injectable }								from '@angular/core';
import { FormGroup }                              	from '@angular/forms';
import { HttpClient, HttpResponse, HttpHeaders }  	from '@angular/common/http';
import { Observable, throwError }                 	from 'rxjs';
import { environment }								from './../environments/environment';
import { MarketplaceRecruiterViewStatResponse }		from './statistics/marketplace-recruiter-view-stat';
import { LoginStats}  								from './login-event-stats';
import { RecruiterListingStatistics } 				from './recruiter-listing-statistics';
import { RecruiterSearchStatistics } from './recruiter-search-stats';

@Injectable({
  providedIn: 'root'
})
export class StatisticsService {

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
	* Returns a list of available Candidates 
	*/
	public getCurriculumDownloadStatistics(): Observable<any>{
      
		const backendUrl:string = environment.backendUrl +'curriculum/stats/downloads';
  
		return this.httpClient.get<any>(backendUrl, this.httpOptions);

	}

	/**
	* Returns a list of available Candidates 
	*/
	public getTotalNumberOfActiceCandidatesStatistics(): Observable<any>{
      
		const backendUrl:string = environment.backendUrl +'candidate/stats/total-active';
  
		return this.httpClient.get<any>(backendUrl, this.httpOptions);

	}
	
	/**
	* Returns a list of functions and available candidates 
	*/
	public getAvailableCandidatesByFunctionStatistics(): Observable<any>{
      
		const backendUrl:string = environment.backendUrl +'candidate/stat/function-count';
  
		return this.httpClient.get<any>(backendUrl, this.httpOptions);

	}
	
	/**
	* Returns status relating to Listings
	*/
	public getListingStats():Observable<any>{
		
		const backendUrl:string = environment.backendUrl +'listings/stats/';
  
		return this.httpClient.get<any>(backendUrl, this.httpOptions);
		
	}
	
	/**
	* Returns stats relating to email requests
	*/
	public getEmailRequestStats():Observable<any>{
		
		const backendUrl:string = environment.backendUrl +'candidate/stat/email-request';
  
		return this.httpClient.get<any>(backendUrl, this.httpOptions);
		
	}
	
	/**
	* Returns a summary of new Candidates
	*/
	public getNewCandidatesSummary():Observable<any>{
		
		const backendUrl:string = environment.backendUrl +'candidate/stat/new-candidate-summary';
  
		return this.httpClient.get<any>(backendUrl, this.httpOptions);
		
	}
	
	/**
	* Returns a summary of new Candidates
	*/
	public getNewCandidatesList():Observable<any>{
		
		const backendUrl:string = environment.backendUrl +'candidate/stat/new-addtions';
  
		return this.httpClient.get<any>(backendUrl, this.httpOptions);
		
	}
	
	/**
	* Returns stats relating to Views of Requested Candidates in the Marketplace
	*/
	public getMarketPlaceRequestedCandidateViewStats():Observable<MarketplaceRecruiterViewStatResponse>{
		
		const backendUrl:string = environment.backendUrl +'v1/open-position/stats/week/';
  
		return this.httpClient.get<any>(backendUrl, this.httpOptions);
		
	}
	
	public fetchLoginStats(): Observable<LoginStats>{
		
		const backendUrl:string = environment.backendUrl +'authentication/stats';
  
    	return this.httpClient.get<any>(backendUrl, this.httpOptions);

	}
	
	/**
	* Returns statistics relating to Listing for an individual recruiter 
	*/
	public fetchRecruiterListingStats(recruiterId:string):Observable<RecruiterListingStatistics> {
		
		const backendUrl:string = environment.backendUrl +'listings/stats/recruiter/'+recruiterId;
  
		return this.httpClient.get<any>(backendUrl, this.httpOptions);
		
	}
	
	/**
	* Returns statistics relating to Recruiter searches
	*/
	public fetchRecruiterSearchStats(recruiterId:string, period:string):Observable<RecruiterSearchStatistics> {
		
		const backendUrl:string = environment.backendUrl +'candidate/stat/search/'+recruiterId + "?period="+period;
  
		return this.httpClient.get<any>(backendUrl, this.httpOptions);
		
	}
	
}