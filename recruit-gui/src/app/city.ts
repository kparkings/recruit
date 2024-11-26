/**
* A City supported by the system 
*/
export class City{

	/**
	* Constructor
	* @param country - Country City belongs tot
	* @param name  	 - Name of the City 
	* @param lat     - Latitude position of City
	* @param lon     - Longitture position of the City
	*/
	constructor(public country:string, public name:string, public lat:number, public lon:number){
		
	}
		
}