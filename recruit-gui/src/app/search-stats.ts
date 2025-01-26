/** 
* Container for statistics relating the the Search behavior of the Recruiters
*/
export class SearchStats{
	public countryStats:Array<SearchStatCountry> 	= new Array<SearchStatCountry>();
	public functionStats:Array<SearchStatFunction> 	= new Array<SearchStatFunction>();
}

/**
* Stats of a Specific function
*/
export class SearchStatFunction{
	public function:string 			= '';
	public searches:number 			= 0;
	public percentageOfTotal:number = 0;
}

/**
* Stats of a specific country
*/
export class SearchStatCountry{
	public country:string 			= '';
	public searches:number 			= 0;
	public percentageOfTotal:number = 0;
}
