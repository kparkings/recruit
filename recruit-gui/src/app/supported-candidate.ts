/**
* Represents a Country supported by the system 
*/
export class SupportedCountry{
	
	/**
	* Constructor
	* @param name 			- Name of country
	* @param humanReadable 	- Human readable name
	* @param iso2Code		- Iso2 Code of country
	*/
	constructor(public name:string, public iso2Code:string, public humanReadable:string){}
	
}