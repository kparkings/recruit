import { HttpClient, HttpHeaders } 					from '@angular/common/http';
import { Injectable } 								from '@angular/core';
import { Observable }                 				from 'rxjs';
import { environment }								from './../environments/environment';
import { NewsFeedItem } from './newsfeed/news-feed-item';

/**
* Services for interacting with NewsFeedItems 
*/
@Injectable({
  providedIn: 'root'
})
export class NewsfeedService {

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
  	public getNewsFeedItems(): Observable<Array<NewsFeedItem>>{
      
		const backendUrl:string = environment.backendUrl +'newsfeeditem';
  
    	return this.httpClient.get<any>(backendUrl, this.httpOptions);
  	}
	
	/**
	* Returns the record for when the User last viewed the Newsfeed 
	*/
	public getLastViewRecord():Observable<any>{
		
		const backendUrl:string = environment.backendUrl +'newsfeeduserview';
  
    	return this.httpClient.get<any>(backendUrl, this.httpOptions);
		
	}
	
	/**
	* Sends update to show when User last viewed the newsfeed 
	*/
	public updateLastViewedRecord():Observable<any>{
	
		const backendUrl:string = environment.backendUrl +'newsfeeduserview';
  
		return this.httpClient.put<any>(backendUrl, {}, {headers: new HttpHeaders({ }), withCredentials: true});
				
	}
	
}