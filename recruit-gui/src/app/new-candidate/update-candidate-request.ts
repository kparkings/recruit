/**
* Request to create a new Candidate 
*/
export class UpdateCandidateRequest{
	public firstname:string							='';
	public surname:string							='';
	public email:string								='';
	public roleSought:string						='';
	public function:string							='';
	public country:string							='';
	public city:string								='';
	public perm:string								='';
	public freelance:string							='';
	public yearsExperience:number 					= 0;
	public available:boolean						= true;
	public languages:Array<Language> 				= new Array<Language>();
	public skills:Array<string> 					= new Array<string>();
	public comments:string 							= '';
	public introduction:string 						= '';
	public daysOnSite:string 						= '';
	public ratePerm:Rate 							= new Rate();
	public rateContract 							= new Rate();
	public availableFromDate:Date 					= new Date();
}

/**
* Represents Rate/Salary requested by the Candidate 
*/
export class Rate{
	public currency:string 							= 'EUR';
	public period:string 							= 'HOUR';
	public valueMin:number 							= 0.0;
	public valueMax:number 							= 0.0;;
}

export class Language{
	public language:string = '';
	public level:string = '';
	
	public constructor(language:string, level:string){
		this.language = language;
		this.level = level;
	}
}