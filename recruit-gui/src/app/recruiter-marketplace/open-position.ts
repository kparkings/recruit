import { RecruiterDetails }								from './recruiter-details';

/**
* Class represents an Open Position being offered by a Recruiter to 
* other recruiters
*/
export class OpenPosition{
	
	public id:string 						= '';
	public recruiter:RecruiterDetails 		= new RecruiterDetails();
	public positionTitle:string 			= '';
	public country:string 					= '';
	public location:string 					= '';
	public contractType:string 				= '';
	public renumeration:string 				= '';
	public startDate:Date 					= new Date();
	public positionClosingDate:Date 		= new Date();
	public description:string 				= '';
	public comments:string 					= '';
	public created:Date						= new Date();
	public viewed:boolean					= false;
}