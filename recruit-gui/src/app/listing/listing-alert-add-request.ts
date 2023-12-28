/**
* Request for creating a new Listing Alert. A configuration to specify
* which types of Listing a user wants to receive alerts for when they 
* are added to the jobboard 
*/
export class ListingAlertAddRequest{

	public email:string 				= '';
	public contractType:string 			= '';
	public countries:Array<string> 		= new Array<string>();
	public categories:Array<string> 	= new Array<string>();

}