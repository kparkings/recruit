/**
* Class represents a request to the API to 
* generate and return the Invoice for a given
* Subscription 
*/
export class GenerateInvoiceRequest{
	
	public subscriptionId:string 		= '';
	public invoiceNumber:string 		= '';
	public invoiceDate:Date 			= new Date();
	public unitDescription:string 		= 'SS';
	
}