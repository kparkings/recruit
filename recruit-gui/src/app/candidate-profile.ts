/**
* Class represents a Candidate Profile
*/
export class CandidateProfile{
	
	public photo:PhotoAPIOutbound			= new PhotoAPIOutbound();
	public cvFile:CVAPIOutbound				= new CVAPIOutbound();
	public introduction:string				= '';
	public rate:Rate						= new Rate(); //TODO [KP] This one should be removed
	public rateContract:Rate				= new Rate();
	public ratePerm:Rate					= new Rate();
	public email:string						= '';
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
	public languages:Array<Language> 		= new Array<Language>();
	public firstname:string 				= '';
	public surname:string 					= '';
	public comments:string					= '';
	public daysOnSite:string				= '';
	public availableFromDate:Date 			= new Date();
	public candidateType:string				= '';

}

/**
* Class represents a Candidates proficiency 
* in a Language
*/
export class Language {
	public language:string 	= '';
	public level:string 	= '';
}

/**
* Class represents a Candidate Profile Image
*/
export class PhotoAPIOutbound {
	public imageBytes?:any;
	public format:string = '';
}

/**
* Class represents a Candidate Profile Image
*/
export class CVAPIOutbound {
	public fileBytes?:any;
	public format:string = '';
}

export class Rate{
	public currency:string 	= '';
	public period:string 	= '';
	public value:string 	= ''; //TODO [KP] remove when done
	public valueMin:number  = 0;
	public valueMax:number 	= 0; 
}