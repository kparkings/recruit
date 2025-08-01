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
	public candidateFilters:CandidateFilters 	= new CandidateFilters();
	public securityFilters:SecurityFilters		= new SecurityFilters();
}

export class CandidateFilters{
	public available?:boolean | null | undefined;
	public ownerId?:string | null | undefined;
	public daysSinceLastAvailabilityCheck?:number | null | undefined;
	public candidateIds:Array<string> = new Array<string>();
}

export class RequestFilters{
	public backendRequestId?:number;
	public unfiltered?:boolean;
	public maxNumberOfSuggestions?:number;
}

export class LocationFilters{
	public geoZones:Array<string> 		= new Array<string>(); 
	public countries?:Array<string> 	= new Array<string>();
	public range:LocationRangeFilters 	= new LocationRangeFilters();		
}

export class LocationRangeFilters{
	
	constructor(public country?:string | null | undefined, public city?:string | null | undefined, public distanceInKm?:number){
		
	}
			
}

export class ExperienceFilters{
	constructor(public experienceMin?:string | null | undefined, public experienceMax?:string | null | undefined){
		
	}
}

export class ContractFilters{
	contract?:string | null | undefined;
		perm?:string | null | undefined; 
}

export class LanguageFilters{
	languages?:Array<string> = new Array<string>();
		
}

export class SecurityFilters{
	securityLevels?:Array<string> = new Array<string>();
}

export class SkillFilters{
	skills?:Array<string> = new Array<string>();
		
}

export class IncludeFilters{
	includeUnavailableCandidates?:string | null | undefined;
	includeRequiresSponsorshipCandidates?:string | null | undefined; 	
}

export class TermFilters{
	title?:string | null | undefined; 
	candidateId?:string | null | undefined;
	firstName?:string | null | undefined;
	surname?:string | null | undefined;
	email?:string | null | undefined;	
}


