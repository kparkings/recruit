/**
* Breakdown of Candidate availability for a Function in a given country 
*/
export class CountryAvailabilityStat{
	
	/**
	* Constructor 
	*/
	constructor(public id:string, public available:number, public unavailable:number, public total:number){}
}