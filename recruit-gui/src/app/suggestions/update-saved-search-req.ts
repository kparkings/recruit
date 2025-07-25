import { SuggestionsSearchRequest } 					from './suggestion-search-request';

export class UpdateSavedSearchRequest{
	public id:string = "";
	public emailAlert:boolean = false;
	public searchName:string = "";
	public searchRequest:SuggestionsSearchRequest = new SuggestionsSearchRequest();
}