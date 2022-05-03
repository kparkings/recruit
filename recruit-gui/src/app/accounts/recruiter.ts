import { Subscription } from "./subscription";

/**
* Represnets a candidate
* @author: K Parkings 
*/
export class Recruiter {

	/**
	* Recruiters UserId (Unique Id for Recruiter)
	*/
	userId:string		= "";
	
	/**
	* Recruiters Firstname
	*/
	firstName:string 	= "";
	
	/**
	* Recruiters Surname
	*/
	surname:string 		= "";
	
	/**
	* Recruiters current Email address
	*/
	email:string 		= "";
	
	/**
	* Recruiters current Company
	*/
	companyName:string 	= "";
	
	/**
	* Primary language spoken by the Recruiter
	*/
	language:string 	= "";
	
	/**
	* Subscriptions associated with the Recruiter
	*/
	subscriptions:Array<Subscription> = new Array<Subscription>();
	
	/**
	* Whether or not the Recruiter currently has an active subscription allowing them
	* access to the sites features
	*/
	hasActiveSubscription:boolean = false;


}