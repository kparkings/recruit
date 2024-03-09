/**
* Represnets a Subscription belonging to the candidate
* @author: K Parkings 
*/
export class Subscription{
	
	/**
	* Unique Id of the subscription
	*/
	subscriptionId:string 				= "";
	
	/**
	* Unique Id of the recruiter
	*/
	recruiterId:string 					= "";
	
	/**
	* When the subscription was created
	*/
	created:Date					 	= new Date();
	
	/**
	* Date the Subscription is or was activated
	*/
	activatedDate:Date					 = new Date();
	
	/**
	* Status of the Subscription
	*/
	status:string						= "";
	
	/**
	* Whether this subscription is the current active subscription
	*/
	currentSubscription:boolean 		= false;
	
	/**
	* Type of the Subscription
	*/
	type:string 						= "";
	
}