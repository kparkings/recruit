/**
* Request to Update City in the Backend 
*/
export class UpdateCityRequest{
	
	/**
	* Consructor
	* @oaram country - part of Unique id
	* @param name    - part of unique id
	* @param lat     - latitude of the City
	* @param lon	 - longitude of the City
	*/
	constructor(public country:string, public name:string, public lat:number, public lon: number, public active:boolean){}
	
}
