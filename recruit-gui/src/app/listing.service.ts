import { Injectable } 								from '@angular/core';
import { HttpClient, HttpHeaders }  				from '@angular/common/http';
import { Observable }                 				from 'rxjs';
import { environment }								from './../environments/environment';
import { NewListingRequest } 						from './recruiter-listings/new-listing-request';
/**
* Service relating to the management of listings
*/
@Injectable({
  providedIn: 'root'
})
export class ListingService {

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
	public registerListing(	ownerName:string, 
							ownerCompany:string,
							ownerEmail:string,
							title:string,
							description:string,
							type:string,	
							country:string,	
							location:string,	
							yearsExperience:number,
							languages:Array<string>,
							skills:Array<string>,	
							rate:number, 			
							currency:string, 		
							postToSocialMedia:boolean):Observable<any>{
		
		const backendUrl:string = environment.backendUrl +'listing/';
		
		let listing:NewListingRequest = new NewListingRequest();
		
		listing.ownerName 			= ownerName;
		listing.ownerCompany		= ownerCompany;
		listing.ownerEmail			= ownerEmail;
		listing.title				= title;
		listing.description			= description;
		listing.type				= type;	
		listing.country				= country;	
		listing.location			= location;	
		listing.yearsExperience		= yearsExperience
		listing.languages		 	= languages;
		listing.skills				= skills;
		listing.rate 				= rate;		
		listing.currency 			= currency;	
		listing.postToSocialMedia	= false;
		
		
		
		return this.httpClient.post<any>(backendUrl, JSON.stringify(listing), this.httpOptions);
	
	}

	/**
	* Updates an existing Listing with the backend
	*/
	public updateListing(	listingId:string,
							ownerName:string, 
							ownerCompany:string,
							ownerEmail:string,
							title:string,
							description:string,
							type:string,	
							country:string,	
							location:string,	
							yearsExperience:number,
							languages:Array<string>,
							skills:Array<string>,	
							rate:number, 			
							currency:string, 		
							postToSocialMedia:boolean):Observable<any>{
		
		const backendUrl:string = environment.backendUrl +'listing/' + listingId;
		
		let listing:NewListingRequest = new NewListingRequest();
		
		listing.ownerName 			= ownerName;
		listing.ownerCompany		= ownerCompany;
		listing.ownerEmail			= ownerEmail;
		listing.title				= title;
		listing.description			= description;
		listing.type				= type;	
		listing.country				= country;	
		listing.location			= location;	
		listing.yearsExperience		= yearsExperience
		listing.languages		 	= languages;
		listing.skills				= skills;
		listing.rate 				= rate;		
		listing.currency 			= currency;	
		listing.postToSocialMedia	= false;
		
		return this.httpClient.put<any>(backendUrl, JSON.stringify(listing), this.httpOptions);
	
	}
	
	/**
	* Returns the listings ownerd by the Recruiter
	*/	
	public fetchRecruiterListings(recruiterId:string, sortColumn:string, order:string, pageNum:number, pageSize:number):Observable<any>{
		
		const backendUrl:string = environment.backendUrl +'listing/?recruiterId='+recruiterId +'&orderAttribute='+  sortColumn +  '&order=' + order + '&page=' + pageNum + '&size=' + pageSize;
		
		return this.httpClient.get<any>(backendUrl, this.httpOptions);
	}
	
		/**
	* Returns the listings ownerd by the Recruiter
	*/	
	public deleteRecruiterListing(listingId:string):Observable<any>{
		
		const backendUrl:string = environment.backendUrl +'listing/' + listingId;
		
		return this.httpClient.delete<any>(backendUrl, this.httpOptions);
	}

}
