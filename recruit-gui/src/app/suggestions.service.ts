import { Injectable } 									from '@angular/core';
import { CandidateServiceService }						from './candidate-service.service';
import { Observable, throwError }                 		from 'rxjs';
import { SuggestionsSearchRequest } 					from './suggestions/suggestion-search-request';

/**
* Service relating to filtering on and retrieving 
* Candidate Suggestions
*/
@Injectable({
  providedIn: 'root'
})
export class SuggestionsService {

	/**
	* Constructor 
	*/
	constructor(private candidateService:CandidateServiceService) {
	}
	
	/**
	* Makes a SearchRequest for retrieving Candidates matching the Filters selected by the User
	*/
	public getCandidateSuggestions(searchRequest:SuggestionsSearchRequest): Observable<any>{
		return this.candidateService.getCandidateSuggestions(searchRequest);
	}
	
}