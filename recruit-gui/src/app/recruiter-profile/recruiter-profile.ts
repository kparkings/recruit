/**
* Class represents a Recruiter Profile
*/
export class RecruiterProfile{
	
	public recruiterId:string 					= '';
	public recruiterFirstName:string 			='';
	public recruiterSurname:string 				='';
	public recruitsIn:Array<string> 			= new Array<string>();
	public languagesSpoken:Array<string>	 	= new Array<string>();
	public profilePhoto?:PhotoAPIOutbound;
	public visibleToRecruiters?:boolean;
	public visibleToCandidates?:boolean;
	public visibleToPublic?:boolean;
	public companyName:string 					= '';
	public jobTitle:string 						= '';
	public yearsExperience:number 				= 0;
	public introduction:string 					= '';
	public sectors:Array<string> 				= new Array<string>();
	public coreTech:Array<string> 				= new Array<string>();
	public recruitsContractTypes:Array<string> 	= new Array<string>();
	public recruiterType:string 				= '';

}

/**
* Class represents a Recruiter Profile Image
*/
export class PhotoAPIOutbound {
	public imageBytes?:any;
	public format:string = '';
}