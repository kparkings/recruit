import { SuggestionsSearchRequest } 					from './suggestion-search-request';

/**
* Request for creating a new Saved Candidate search
*/
export class NewSavedSearchRequest {
	public emailAlert:boolean=false;
	public searchName:string='';
	public searchRequest:SuggestionsSearchRequest = new SuggestionsSearchRequest();
}
	