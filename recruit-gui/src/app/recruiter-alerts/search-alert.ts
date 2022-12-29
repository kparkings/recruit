/**
* Represents an Alert a Recruiters has created to receive notifications
* when a matching Candidate has been uploaded 
*/
export class SearchAlert{

	
	/** 
	* Constructor
	*/
	public constructor(	public alertId:string,
						public alertName:string,
						public countries:Array<string>,
						public functions:Array<string>,
						public yearsExperienceGtEq:number,
						public yearsExperienceLtEq:number,
						public dutch:string,
						public english:string,
						public french:string,
						public skills:Array<String>,
						public freelance:any,
						public perm:any){
		
	}
	
}