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
	* Country the Company is registered in
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
	
	/**
	* Subscriptions associated with the Recruiter
	*/
	companyAddress:string = "";
	
	/**
	* Subscriptions associated with the Recruiter
	*/
	companyCountry:string = "";
	
	/**
	* Name of the company
	*/
	companyVatNumber:string = "";
	
	/**
	* Registrtion number of company with Chamber or commerce
	*/
	companyRegistrationNumber:string = "";
	
}