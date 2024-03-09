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
	* Recruiters current Company Country
	*/
	companyCountry:string 	= "";
	
	/**
	* Recruiters current Company Address
	*/
	companyAddress:string 	= "";
	
	/**
	* Recruiters current Company VAT Number
	*/
	companyVatNumber:string 	= "";
	
	/**
	* Recruiters current Company Registation Number
	*/
	companyRegistrationNumber:string 	= "";
	
	/**
	* Primary language spoken by the Recruiter
	*/
	language:string 	= "";
	
	/**
	* Subscriptions associated with the Recruiter
	*/
	subscriptions:Array<Subscription> = new Array<Subscription>();

}