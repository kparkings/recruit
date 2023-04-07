/**
* Class represents a Candidate Profile
*/
export class CandidateProfile{
	
	public profilePhoto:PhotoAPIOutbound	= new PhotoAPIOutbound();
	public introduction:string				= '';
	public candidateId:string 				= '';
	public function:string 					= '';
	public roleSought:string 				= '';
	public country:string 					= '';
	public city:string 						= '';
	public perm:string 						= '';
	public freelance:string 				= '';
	public yearsExperience:number	 		= 0;
	public available:boolean 				= false;
	public lastAvailabilityCheck:Date 		= new Date();
	public skills:Array<string> 			= new Array<string>();
	public languages:Array<string> 			= new Array<string>();
	public firstname:string 				= '';
	public surname:string 					= '';

}

/**
* Class represents a Candidate Profile Image
*/
export class PhotoAPIOutbound {
	public imageBytes?:any;
	public format:string = '';
}