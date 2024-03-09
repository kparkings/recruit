import { RecruiterDetails }								from './recruiter-details';

/**
* Class represents an Open Position being offered by a Recruiter to 
* other recruiters
*/
export class NewOpenPosition{
	
	public positionTitle:string 			= '';
	public country:string 					= '';
	public location:string 					= '';
	public contractType:string 				= '';
	public renumeration:string 				= '';
	public startDate:Date 					= new Date();
	public positionClosingDate:Date 		= new Date();
	public description:string 				= '';
	public comments:string 					= '';
	
	public spokenLanguages:Array<string> 	= new Array<string>();
	public skills:Array<string> 		= new Array<string>();
	
}