/**
* Class is a request that can be sent to the server
* in order to receive a batch of Suggestions matching
* the SearchFilters 
*/
export class SuggestionsSearchRequest{
	
	public requestFilters:RequestFilters 		= new RequestFilters();
	public locationFilters:LocationFilters 		= new LocationFilters();
	public contractFilters:ContractFilters 		= new ContractFilters();	
	public languageFilters:LanguageFilters 		= new LanguageFilters();
	public experienceFilters:ExperienceFilters 	= new ExperienceFilters();
	public skillFilters:SkillFilters 			= new SkillFilters();
	public includeFilters:IncludeFilters 		= new IncludeFilters();
	public termFilters:TermFilters 				= new TermFilters();
	
}

export class RequestFilters{
	public backendRequestId?:number;
	public isUnfiltered?:boolean;
	public maxNumberOfSuggestions?:number;
}

export class LocationFilters{
	public geoZones:Array<string> 		= new Array<string>(); 
	public countries?:Array<string> 	= new Array<string>();
	public range:LocationRangeFilters 	= new LocationRangeFilters();		
}

export class LocationRangeFilters{
	
	constructor(public locCountry?:string, public locCity?:string, public locDistance?:number){
		
	}
			
}

export class ExperienceFilters{
	constructor(public experienceMin?:string, public experienceMax?:string){
		
	}
}

export class ContractFilters{
	contract?:string;
		perm?:string; 
}

export class LanguageFilters{
	languages?:Array<string> = new Array<string>();
		
}

export class SkillFilters{
	skills?:Array<string> = new Array<string>();
		
}

export class IncludeFilters{
	includeUnavailableCandidates?:string;
	includeRequiresSponsorshipCandidates?:string; 	
}

export class TermFilters{
	title?:string; 
	candidateId?:string;
	firstName?:string;
	surname?:string;
	email?:string;	
}


