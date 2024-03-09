/** * 
* Class used for UI to track the human readable label and
* the Value understood by the backend
*/
export class LanguageOption{
	
	/**
	* constructor
	* @param value - Value backend understands
	* @param label - Label human User unserstands 
	*/
	public constructor(public value:string = '', public label:string = ''){
		
	}
	
}