import { Injectable } 								from '@angular/core';
import { HttpClient, HttpHeaders }  				from '@angular/common/http';
import { Observable }                 				from 'rxjs';
import { environment }								from './../environments/environment';
import { NewListingRequest } 						from './recruiter-listings/new-listing-request';
import { ListingAlertAddRequest } from './listing/listing-alert-add-request';
import { FunctionType } from './listing/function-type';
import { Country } from './listing/country';
import { ContractType } from './listing/contract-type';
import { LanguageType } from './listing/language-type';

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
	* Returns the listings owned by any recruiter
	*/	
	public fetchAllListings(sortColumn:string, order:string, pageNum:number, pageSize:number, filterString:string):Observable<any>{
		
		const backendUrl:string = environment.backendUrl +'listing/public/?orderAttribute='+  sortColumn +  '&order=' + order + '&page=' + pageNum + '&size=' + pageSize + filterString;
		
		return this.httpClient.get<any>(backendUrl, this.httpOptions);
	}
		
	/**
	* Returns the listings ownerd by the Recruiter
	*/	
	public deleteRecruiterListing(listingId:string):Observable<any>{
		
		const backendUrl:string = environment.backendUrl +'listing/' + listingId;
		
		return this.httpClient.delete<any>(backendUrl, this.httpOptions);
	}
	
	/**
	* Registers an event showing that a Listing was viewed
	*/
	public registerListingViewedEvent(listingId:string):Observable<any>{
		
		const backendUrl:string = environment.backendUrl +'listing/public/viewedEvent/'+listingId;
		
		return this.httpClient.post<any>(backendUrl, null, this.httpOptions);
				
	}
	
	/**
	* Performs a check to see if the User has access to the Curriculum either
	* because they do not use credt based acces or beause they have remaining 
	* credits 
	*/
	public doCreditCheck():Observable<boolean>{
		
		const backendUrl:string = environment.backendUrl + 'listing/creditCheck';
  	
		return this.httpClient.get<boolean>(backendUrl, this.httpOptions);
		
	}

	/**
	* Registers a new Listing alert for a User 
	*/
	public createAlert(addRequest:ListingAlertAddRequest):Observable<void>{
		
		const backendUrl:string = environment.backendUrl + 'public/listing-alert';
  	
		return this.httpClient.post<void>(backendUrl, addRequest, this.httpOptions);
		
	}
	
	/**
	* Deletes an existing Listing alert for a User 
	*/
	public deleteAlert(alertId:string):Observable<void>{
	
		const backendUrl:string = environment.backendUrl + 'listing/creditCheck';
  	
		return this.httpClient.delete<void>(backendUrl, this.httpOptions);
	
	}
	
	/**
	* Returns the types of roles recognised by the system
	*/
	public fetchFunctionTypes():Array<FunctionType>{
		
		let functionTypes:Array<FunctionType>  = new Array<FunctionType>();
		
		functionTypes.push(new FunctionType("JAVA","Java"));
		functionTypes.push(new FunctionType("DOT_NET","Dot Net"));
		functionTypes.push(new FunctionType("DEV_OPS","Devops"));
		functionTypes.push(new FunctionType("NETWORKS","Networks"));
		functionTypes.push(new FunctionType("CLOUD","Cloud"));
		functionTypes.push(new FunctionType("WEB","Web"));
		functionTypes.push(new FunctionType("UI_UX","UI/UX"));
		functionTypes.push(new FunctionType("PROJECT_NANAGMENT","Project Management"));
		functionTypes.push(new FunctionType("TESTING","Testing"));
		functionTypes.push(new FunctionType("BUSINESS_ANALYSTS","BA"));
		functionTypes.push(new FunctionType("SECURITY","IT Security"));
		functionTypes.push(new FunctionType("IT_SUPPORT","IT Support"));
		functionTypes.push(new FunctionType("ARCHITECT","Architecture"));
		functionTypes.push(new FunctionType("BI","BI"));
		functionTypes.push(new FunctionType("REC2REC","Recruitment"));
		
		return functionTypes;
	}
	
	/**
	* Returns Countries a listing can be for
	*/
	public fetchCountries():Array<Country>{
		
		let countries:Array<Country> = new Array<Country>();
		
		countries.push(new Country("NETHERLANDS", "The Netherlnads"));
		countries.push(new Country("BELGIUM", "Belgium"));
		countries.push(new Country("UK", "UK"));
		countries.push(new Country("IRELAND", "Ireland"));
		countries.push(new Country("EU_REMOTE", "Remote (EU)"));
		countries.push(new Country("WORLD_REMOTE", "Remote (Worldwide)"));
		
		return countries;
		
	}
	
	/**
	* Returns ContractTypes a listing can be for
	*/
	public fetchContractTypes():Array<ContractType>{
		
		let contractTypes:Array<ContractType> = new Array<ContractType>();
		
		contractTypes.push(new ContractType("CONTRACT_ROLE", "Contract"));
		contractTypes.push(new ContractType("PERM_ROLE", "Perm"));
		contractTypes.push(new ContractType("BOTH", "Contract/Perm"));
		
		return contractTypes;
		
	}
	
		/**
	* Returns ContractTypes a listing can be for
	*/
	public fetchLanguageTypes():Array<LanguageType>{
		
		let languageTypes:Array<LanguageType> = new Array<LanguageType>();
		
		languageTypes.push(new LanguageType("DUTCH", "Dutch"));
		languageTypes.push(new LanguageType("FRENCH", "French"));
		languageTypes.push(new LanguageType("ENGLISH", "English"));
		
		return languageTypes;
		
	}

}