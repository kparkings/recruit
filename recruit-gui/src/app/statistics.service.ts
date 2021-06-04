import { Injectable }								from '@angular/core';
import { FormGroup }                              	from '@angular/forms';
import { HttpClient, HttpResponse, HttpHeaders }  	from '@angular/common/http';
import { Observable, throwError }                 	from 'rxjs';
import { environment }								from './../environments/environment';

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
      
		const backendUrl:string = environment.backendUrl +'curriculum/stats/dailydownloads';
  
		return this.httpClient.get<any>(backendUrl, this.httpOptions);

	}

	/**
	* Returns a list of available Candidates 
	*/
	public getTotalNumberOfActiceCandidatesStatistics(): Observable<any>{
      
		const backendUrl:string = environment.backendUrl +'candidate/stats/total-active';
  
		return this.httpClient.get<any>(backendUrl, this.httpOptions);

	}
}