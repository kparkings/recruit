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
	public getSuggestons(	backendRequestId:number,
							maxNumberOfSuggestions:number, 
							title:string, 
							geoZones:Array<string>, 
							countries:Array<string>, 
							contract:string, 
							perm:string, 
							experienceMin:string, 
							experienceMax:string, 
							languages:Array<string>, 
							skills:Array<string>,
							includeUnavailableCandidates:string, 
							includeRequiresSponsorshipCandidates:string, 
							isUnfiltered:boolean,
							firstName:string,
							surname:string,
							email:string,
							candidateId:string,
							locCountry:string,
							locCity:string,
							locDistance:number): Observable<any>
							{
		
		return this.candidateService.getCandidates(this.getCandidateFilterParamString(	backendRequestId,
																						maxNumberOfSuggestions, 
																						title, 
																						geoZones,
																						countries, 
																						contract, 
																						perm, 
																						experienceMin, 
																						experienceMax, 
																						languages, 
																						includeUnavailableCandidates,
																						includeRequiresSponsorshipCandidates,
																						skills,
																						isUnfiltered,
																						firstName,
																						surname,
																						email,
																						candidateId,
																						locCountry,
																						locCity,
																						locDistance
																						));
		
	}
	
	/**
	* Builds a query parameter string with the selected filter options
	*/
	private getCandidateFilterParamString(	backendRequestId:number,
											maxNumberOfSuggestions:number, 
											title:string, 
											geoZones:Array<string>,
											countries:Array<string>, 
											contract:string, 
											perm:string, 
											experienceMin:string, 
											experienceMax:string,
											languages:Array<string>, 
											includeUnavailableCandidates:string,
											includeRequiresSponsorshipCandidates:string, 
											skills:Array<string>,
											isUnfiltered:boolean,
											firstName:string,
											surname:string,
											email:string,
											candidateId:string,
											locCountry:string,
											locCity:string,
											locDistance:number):string
											{

		const filterParams:string = 'orderAttribute=candidateId&order=desc'
                                                         + '&page=0'
                                                         + '&unfiltered=' + isUnfiltered
                                                         + '&size=' + maxNumberOfSuggestions
                                                         + '&backendRequestId='+backendRequestId
														 + '&searchText=' + encodeURIComponent(title)
														 + this.getGeoZoneFilterParamString(geoZones)
														 + this.getCountryFilterParamString(countries) 			
                                                         + this.getContractTypeParamString(contract, perm)				
                                                         + this.getYearsExperienceFilterParamAsString(experienceMin, experienceMax)
                                                         + this.includeUnavailableCandidates(includeUnavailableCandidates)
                                                         + this.includeRequiresSponsorshipCandidates(includeRequiresSponsorshipCandidates)
														 + this.getSkillsParamString(skills)
														 + this.getLanguagesParamString(languages)
														 + this.getFirstName(firstName)
														 + this.getSurname(surname)
														 + this.getEmail(email)
														 + this.getCandidateId(candidateId)
														 + this.getLocation(locCountry, locCity, locDistance);
														 
					                                  
		return filterParams;
	
	}
	
	/**
	* Constructs search param for firstName 
	*/
	private getFirstName(firstName:string):string{
		if (firstName && firstName.length > 0) {
			return '&firstname='+firstName;
		}
		
		return '';
	}
	
	/**
	* Constructs search param for surname 
	*/
	private getSurname(surname:string):string{
		if (surname && surname.length > 0) {
			return '&surname='+surname;
		}
		
		return '';
	}
	
	/**
	* Constructs search param for email 
	*/
	private getEmail(email:string):string{
		if (email && email.length > 0) {
			return '&email='+email;
		}
		
		return '';	
	}
	
	/**
	* Constructs search param for candidateId 
	*/
	private getCandidateId(candidateId:string):string{
		if (candidateId && candidateId.length > 0) {
			return '&candidateId='+candidateId;
		}
		
		return '';	
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
	* Adds filter if unavailable candidates requiring sponsorship should also be included
	* By default only those not requiring sponsorsho. 
	*/
	public includeRequiresSponsorshipCandidates(includeRequiresSponsorshipCandidates:string):string{
		
		if (includeRequiresSponsorshipCandidates.length > 0 || ""+includeRequiresSponsorshipCandidates === 'true') {
			return '&includeRequiresSponsorship=true';
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
	
	public getLocation(country:string, city:string, distance:number):string{
		if (country.length === 0 || city.length === 0 || distance === 0 || !distance ) {
			return "";
		}
		return '&locCountry='+country.toUpperCase()+'&locCity='+city+'&locDistance='+distance;
		
	}
	
	/**
	* Adds filter string if geoZone specifed in Listing
	*/
	public getGeoZoneFilterParamString(geoZones:Array<string>):string{
		
		if (geoZones.length === 0){
			return '';
		}
		
		return '&geoZones=' + geoZones;
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
		
		languages.forEach(lang => {
			paramString = paramString +  '&' + lang.toLowerCase() + '=PROFICIENT';
		});
		
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