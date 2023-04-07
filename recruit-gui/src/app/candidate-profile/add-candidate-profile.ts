/**
* Class represents a Request to add a new RecruiterProfile
*/
export class AddCandidateProfileRequest{
	
	public recruitsIn:Array<string> 			= new Array<string>();
	public languagesSpoken:Array<string> 		= new Array<string>();
	public visibleToRecruiters?:boolean;
	public visibleToCandidates?:boolean;
	public visibleToPublic?:boolean;
	public jobTitle:string 						= '';
	public yearsExperience:number 				= 0;
	public introduction:string 					= '';
	public sectors:Array<string> 				= new Array<string>();
	public coreTech:Array<string> 				= new Array<string>();
	public recruitsContractTypes:Array<string> 	= new Array<string>();
	public recruiterType:string 				= '';
	
}