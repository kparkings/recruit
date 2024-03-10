import { Injectable } 									from '@angular/core';
import { CandidateServiceService }						from './candidate-service.service';
import { Candidate }									from './candidate';
import { Observable, throwError }                 		from 'rxjs';

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
	* Returns the Suggestons based upon the filter criteria 
	*/
	public getSuggestons(	maxNumberOfSuggestions:number, 
							title:string, 
							countries:Array<string>, 
							contract:string, 
							perm:string, 
							experienceMin:string, 
							experienceMax:string, 
							languages:Array<string>, 
							skills:Array<string>,
							includeUnavailableCandidates:string): Observable<any>{
		
		return this.candidateService.getCandidates(this.getCandidateFilterParamString(	maxNumberOfSuggestions, 
																						title, 
																						countries, 
																						contract, 
																						perm, 
																						experienceMin, 
																						experienceMax, 
																						languages, 
																						includeUnavailableCandidates,
																						skills,
																						));
		
	}
	
	/**
	* Builds a query parameter string with the selected filter options
	*/
	private getCandidateFilterParamString(	maxNumberOfSuggestions:number, 
											title:string, 
											countries:Array<string>, 
											contract:string, 
											perm:string, 
											experienceMin:string, 
											experienceMax:string,
											languages:Array<string>, 
											includeUnavailableCandidates:string,
											skills:Array<string>):string{

		const filterParams:string = 'orderAttribute=candidateId&order=desc'
                                                         + '&page=0'
                                                         + '&size=' + maxNumberOfSuggestions
														 + '&searchText=' + encodeURIComponent(title)
														 + this.getCountryFilterParamString(countries) 			
                                                         + this.getContractTypeParamString(contract, perm)				
                                                         + this.getYearsExperienceFilterParamAsString(experienceMin, experienceMax)
                                                         + this.includeUnavailableCandidates(includeUnavailableCandidates)
														 + this.getSkillsParamString(skills)
														 + this.getLanguagesParamString(languages);
					                                  
		return filterParams;
	
	}
	
	/**
	* Adds filter if unavailable candidates should also be included
	* By default only availabe. 
	*/
	public includeUnavailableCandidates(includeUnavailableCandidates:string):string{
		
		if (includeUnavailableCandidates.length === 0 || ""+includeUnavailableCandidates === 'false') {
			return '&available=true';
		}
		
		return '';
	}
	
	/**
	* Adds filter string if country specifed in Listing
	*/
	public getCountryFilterParamString(countries:Array<string>):string{
		
		if (countries.length === 0){
			return '';
		}
		
		return '&countries=' + countries;
	}
	
	/**
	* Adds filter for perm positions if specified in the listing
	*/
	public getContractTypeParamString(contract:string, perm:string):string{
		
		let paramString:string = '';
		
		if (contract !== "") {
			paramString = '&freelance=true';
		}
		
		if (perm !== "") {
			paramString = paramString + '&perm=true';
		}
	
      	return paramString;
	}
	
	private getSkillsParamString(skills:Array<string>):string{
      
		if (skills.length > 0) {

			let rawSkills:string 		= skills.toString();
			let encodedSkills:string 	= '&skills='+encodeURIComponent(rawSkills);
			
			return encodedSkills; 
      	}

      return '';
 
	}
	
	/**
  	* Creates a query param string with the filter options to apply to the dutch languge filter
  	*/
	private getLanguagesParamString(languages:Array<string>):string{
  
		let paramString:string = '';
		
		if (languages.indexOf('DUTCH')  >= 0 ) {
			paramString = paramString +  '&dutch=' + 'PROFICIENT';
		}
		
		if (languages.indexOf('FRENCH')  >= 0 ) {
			paramString = paramString +  '&french=' + 'PROFICIENT';
		}
		
		if (languages.indexOf('ENGLISH')  >= 0 ) {
			paramString = paramString +  '&english=' + 'PROFICIENT';
		}  
		
		return paramString;
  
	}
		
	/**
 	* Adds filter for years expeprience  if specified in the listing
	*/
	public getYearsExperienceFilterParamAsString(min:string, max:string):string{
		
		let paramStr:string = '';
		
		if (min !== ''){
			paramStr = paramStr + '&yearsExperienceGtEq=' + min;
		}
		
		if (max !== ''){
			paramStr = paramStr + '&yearsExperienceLtEq=' + max;
		}
		
		return paramStr;
	}
	
}