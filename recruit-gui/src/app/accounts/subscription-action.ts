
/**
* Represnets a candidate
* @author: K Parkings 
*/
export class SubscriptionAction {

	/**
	* Recruiters UserId (Unique Id for Recruiter)
	*/
	userId:string								= "";
	
	/**
	* Recruiters Firstname
	*/
	firstName:string 							= "";
	
	/**
	* Recruiters Surname
	*/
	surname:string 								= "";
	
	/**
	* Recruiters current Email address
	*/
	email:string 								= "";
	
	/**
	* Primary language spoken by the Recruiter
	*/
	language:string 							= "";
	
	/**
	* Unique Id of the subscription
	*/
	subscriptionId:string 						= "";
	
	/**
	* Status of the Subscription
	*/
	status:string								= "";
	
	/**
	* Type of the Subscription
	*/
	type:string 								= "";
	
	/**
	* Type of Invoice to send
	*/
	invoiceType:string							= "";
	
	/**
	* Date the Subscription was created 
	*/
	created:Date 								= new Date();
	
	/**
	* Ids of actions that can be carried out for the subscription
	*/
	actions:Array<string>			 			= new Array<string>();

}