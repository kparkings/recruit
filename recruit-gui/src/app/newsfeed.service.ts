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
	
}
