import { SuggestionsSearchRequest } 					from './suggestion-search-request';

/**
* Saved Candiadte Search 
*/
export class SavedCandidateSearch{
	public id:string 								= "";
	public emailAlert:boolean 						= false;
	public searchName:string						="";
	public searchRequest:SuggestionsSearchRequest 	= new SuggestionsSearchRequest();
}