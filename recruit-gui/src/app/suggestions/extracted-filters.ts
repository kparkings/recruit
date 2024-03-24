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
	public dutch:boolean				= false;
	public english:boolean				= false;
	public french:boolean				= false;
	public netherlands:boolean			= false;
	public uk:boolean					= false;
	public belgium:boolean				= false;
	public ireland:boolean				= false;
	public eu:boolean					= false;
	public world:boolean				= false;
	public extractedText:string | any;
	
}