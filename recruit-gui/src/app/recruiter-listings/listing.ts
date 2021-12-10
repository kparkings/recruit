/** 
* Class for creating a new Listing. Intended to be sent 
* to be backends API
*/
export class Listing {

	public listingId:string					= '';
	public ownerId:string				 	= '';
	public ownerName:string 				= '';
	public ownerCompany:string 				= '';
	public ownerEmail:string				= '';
	public created:Date					 	= new Date();
	public title:string						= '';
	public description:string				= '';
	public type:string						= '';
	public country:string					= '';
	public location:string					= '';
	public yearsExperience:number 			= 0;
	public languages:Array<string> 			= new Array<string>();
	public skills:Array<string>				= new Array<string>();
	public rate:number 						= 0;
	public currency:string 					= '';
	public views:number						= 0;
	
}

