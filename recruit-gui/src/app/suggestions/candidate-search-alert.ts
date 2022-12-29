export class CandidateSearchAlert{
	
	public alertName:string 			= '';
	public countries:Array<string> 		= new Array<string>();
	public functions:Array<string> 		= new Array<string>();
	public freelance:string 			= '';
	public perm:string 					= '';
	public yearsExperienceGtEq:string 	= '';
	public yearsExperienceLtEq:string 	= '';
	public dutch:string 				= "UNKNOWN";
	public english:string 				= "UNKNOWN";
	public french:string 				= "UNKNOWN";
	public skills:Array<string> 		= new Array<string>();

	public constructor(){}
	
}
