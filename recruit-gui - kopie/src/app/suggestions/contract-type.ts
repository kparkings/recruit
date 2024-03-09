/**
* Represents a ContractType for the Listing 
*/
export class ContractType{
	
	/**
	* Constructor
	* contractType: corresponds to backend Enum value
	* humanReadable: Human readable label
	*/
	public constructor(public contractType:string, public humanReadable:string){
		
	}
	
}