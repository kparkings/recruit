/**
* Request for search results for Job board Listings 
* matchig the filters 
*/
export class ListingSearchRequest{
	
	public searchTerm:string|null 		= null;
	public contractType:string|null 	= null;
	public geoZones:Array<string>		= new Array<string>();
	public countries:Array<string> 		= new Array<string>();
	public maxAgeOfPost:string|null 	= null;
	
}