/**
* Class represents a FunctionType/Role that a candidate can 
* undertake 
*/
export class FunctionType{
	
	/**
	* Constructor
	* key 			-  Maps to enum in backend
	* humanReadable - Human readabl representation of the role 
	*/
	constructor(public key:string, public humanReadable:string){
		
	}
	
}

/**
* Wrapper for FuntcionType to specify if it is selected 
*/
export class SelectableFunctionType {
	
	constructor(public functionType:FunctionType, public selected:boolean){
		
	}
	
}