/**
* Class represents a request to the backend API for 
* a specific type of Subscription for a Recruiter
*/

export enum INVOICE_TYPE {
	PERSON = 'PERSON',
	BUSINESS = 'BUSINESS'			
};
	
export class SubscriptionAPIInbound{
	
	invoiceType:INVOICE_TYPE = INVOICE_TYPE.BUSINESS;
	
	/**
	* Type of Subscription being requested for 
	* the Recruiter
	*/
	type:string = "";
	
}