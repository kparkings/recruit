/**
* Info about number of candidates registered and their availability 
*/
export class CandidateTotals{
	
	/**
	* Constructor
	*/
	constructor(public available:number, public unavailable:number, public totalRegisteredCandidates:number){}
	
}