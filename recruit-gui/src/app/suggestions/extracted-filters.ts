/**
* Class represents candidate filter options extracted
* from a job specification document
*/
export class ExtractedFilters{
	
	public jobTitle:string 				= '';
	public skills:Array<string> 		= new Array<string>();
	public experienceGTE:string 		= "";
	public experienceLTE:string 		= "";
	public freelance:string 			= '';
	public perm:string 					= '';
	public languages:Array<string>		= new Array<string>();
	public countries:Array<string>		= new Array<string>();
	public city:string					= "";
	public eu:boolean					= false;
	public world:boolean				= false;
	public extractedText:string | any;
	
}