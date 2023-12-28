/**
* Represents a country a listing can be for 
*/
export class Country{
	public constructor(public key:string, public humanReadable:string){
		
	}
}

/**
* Wrapper for selecting a Country 
*/
export class SelectableCountry{
	public constructor(public country:Country, public selected:boolean){
		
	}
}