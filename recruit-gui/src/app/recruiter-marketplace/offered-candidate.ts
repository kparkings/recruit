import { RecruiterDetails }								from './recruiter-details';

/**
* Represents a Candidate offered by a Recruiter
*/
export class OfferedCandidate{
	
	public id:string 						= '';
	public recruiter:RecruiterDetails 		= new RecruiterDetails();
	public candidateRoleTitle:string 		= '';
	public country:string					= '';
	public location:string 					= '';
	public contractType:string 				= '';
	public daysOnSite:string 				= '';
	public renumeration:string 				= '';
	public availableFromDate:Date 			= new Date();
	public coreSkills:Array<string> 		= new Array<string>();
	public yearsExperience:number 			= 0;
	public description:string 				= '';
	public spokenLanguages:Array<string>	= new Array<string>();
	public comments:string 					= '';
	public created:Date 					= new Date();
	
}