import { HttpClient, HttpHeaders } 					from '@angular/common/http';
import { Injectable } 								from '@angular/core';
import { Observable, BehaviorSubject  }             from 'rxjs';
import { environment }								from './../environments/environment';
import { of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CreditsService {

	public tokens 					= new BehaviorSubject(true);
	public purchaseSubscription 	= new BehaviorSubject(false);
	
	/**
	* constructor 
	*/
	constructor(private httpClient: HttpClient) { }
	httpOptions = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }), withCredentials: true
    };
    
    /**
  	* Returns remaining credits for Curriculum downloads for USer 
  	*/
  	public getCreditCountCurriculum(): Observable<any>{
      
		const backendUrl:string = environment.backendUrl +'candidate/_credits';
  
    	return this.httpClient.get<any>(backendUrl, this.httpOptions);
  	}
  	
  	/**
  	* Returns whether User ( Recruiter ) has a paid subscription
  	*/
  	public hasPaidSubscription(): Observable<any>{
      
		const backendUrl:string = environment.backendUrl +'recruiter/_paid_subscription';
  
    	return this.httpClient.get<any>(backendUrl, this.httpOptions);
  	}
  	
  	
  	/**
  	* Returns remaining credits for Curriculum downloads for USer 
  	*/
  	public getCreditCountJobboard(): Observable<any>{
      
		const backendUrl:string = environment.backendUrl +'listing/_credits';
  
    	return this.httpClient.get<any>(backendUrl, this.httpOptions);
  	}
  	
  	/**
  	* Returns remaining credits for Curriculum downloads for USer 
  	*/
  	public getCreditCountMarketplace(): Observable<any>{
      
		const backendUrl:string = environment.backendUrl +'v1/open-position/_credits';
  
    	return this.httpClient.get<any>(backendUrl, this.httpOptions);
  	}

	/**
	* Returns whether or not User has remaining tokens 
	*/
	public hasTokens():Observable<boolean>{
		return this.tokens;
	}
	
	/**
	* Sets tokens as being exhausted 
	*/
	public tokensExhaused():void{
		this.tokens.next(false);
	}
	
	/**
	* Returns whether user is currently in the process 
	* of purchasing a subscription
	*/
	public isPurchaseSubscription():Observable<boolean>{
		let x:boolean = this.purchaseSubscription.value; 
		//this.setPurchaseSubscription(false)
		this.purchaseSubscription.next(false);
		return of(x);
	}
	
	/**
	* sets the User is currently in the process of buying subscriptions 
	*/
	public buySubscription():void{
		this.purchaseSubscription.next(true);
	}
	

}
