import { Injectable } from '@angular/core';
import { Observable, BehaviorSubject  }             from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CreditsService {

	public tokens 					= new BehaviorSubject(true);
	public purchaseSubscription 	= new BehaviorSubject(false);
	
	/**
	* constructor 
	*/
	constructor() { 
	 
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
		return this.purchaseSubscription;
	}
	
	/**
	* Set whether User is in the process of buying a 
	* subscription
	*/
	public setPurchaseSubscription(inProcess:boolean):void{
		this.purchaseSubscription.next(inProcess);
	}
	
	/**
	* sets the User is currently in the process of buying subscriptions 
	*/
	public buySubscription():void{
		this.purchaseSubscription.next(true);
	}
	

}
