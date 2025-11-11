/**
* Class represents the Statistics relating to the number of 
* contact requests a recruiter has received for a specific 
* Listing on the job board. 
*/
export class ListingStatContactRequests{
	
	/**
	* Constructor
	* @param listingId 							- Unique Id of the listing
	* @oaran registeredUserRequests:number 		- Contact request from registerd/logged in users
	* @oaran unregisteredUserRequests:number 	- Contact request from non registerd/logged in users
	* @param alertsSent 						- Number of email alerts sent to users when Litsing created
	*/
	constructor(public listingId:string, public registeredUserRequests:number, public unregisteredUserRequests:number, public alertsSent:number){
		
	}
	
}