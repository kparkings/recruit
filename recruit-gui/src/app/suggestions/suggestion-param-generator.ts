import { UntypedFormGroup }				from '@angular/forms';
import { GeoZone } from '../geo-zone';
import { SupportedCountry } from '../supported-candidate';
import { SupportedLanguage } from '../supported-language';

/**
* Class containing the logic to prepare the filter params for the 
* Suggestions search
*/
export class SuggestionParams{
	
	private geoZones:Array<string> 						= new Array<string>();
	private countries:Array<string> 					= new Array<string>();
	private skills:Array<string> 						= new Array<string>();
	private functionTypes:Array<string>					= new Array<string>();
	private languages:Array<string> 					= new Array<string>();
	private filterType:string 								= "";
	private title:string 								= "";
	private firstName:string 							= "";
	private surname:string 								= "";
	private email:string 								= "";
	private candidateId:string							= "";
	private contract:string 							= "";
	private perm:string 								= "";
	private minExperience:string 						= "";
	private maxExperience:string 						= "";
	private unavailableCadidates:string					= "";
	private includeRequiresSponsorship:string			= "";	
	private locCountry:string 							= "";
	private locCity:string 								= "";
	private locDistance:number 							= 0;
		
	/**
	* Constructor
	* @param suggestionFilterForm 	- contains raw filter info
	* @param skillFilters			- contains raw skill filter info 
	* @param functionTypes			- contains raw function types info
	*/
	public constructor(filterTypeFormGroup:UntypedFormGroup, suggestionFilterForm:UntypedFormGroup, skillFilters:Array<string>, functionTypes:Array<string>, public availableGeoZones:Array<GeoZone>, public supportedCountries:Array<SupportedCountry>, supportedLanguages:Array<SupportedLanguage>){
		
		this.filterType 	= filterTypeFormGroup.get('searchType')?.value;
		this.skills 		= skillFilters.concat();
		this.functionTypes	= functionTypes;
		this.title  		= suggestionFilterForm.get('searchPhrase')?.value;
		this.minExperience 	= suggestionFilterForm.get('minYearsExperience')?.value;
		this.maxExperience 	= suggestionFilterForm.get('maxYearsExperience')?.value;
		
		this.unavailableCadidates = suggestionFilterForm.get('includeUnavailableCandidates')?.value;
		this.includeRequiresSponsorship = suggestionFilterForm.get('includeRequiresSponsorshipCandidates')?.value;
		
		
		//START DISTANCE
		this.locCountry 	= suggestionFilterForm.get('locationCountry')?.value;
		this.locCity 		= suggestionFilterForm.get('locationCity')?.value;
		this.locDistance 	= suggestionFilterForm.get('locationDistance')?.value;
		
		
		//END DISTANCE
		
		
		if (this.filterType != 'FUNCTION') {
			this.title = '';
		}
		
		if (this.filterType == 'EMAIL') {
			this.email = suggestionFilterForm.get('searchPhrase')?.value;
		}
		
		if (this.filterType == 'CANDIDATE_ID') {
			
			const candidateId =  suggestionFilterForm.get('searchPhrase')?.value;
			
			const isNumeric = /^[+-]?\d+(\.\d+)?$/.test(candidateId);
			
			if (isNumeric){
				this.candidateId = candidateId;
			}
			
		}
		
		if (this.filterType == 'NAME') {
			this.firstName = suggestionFilterForm.get('searchPhraseFirstName')?.value;
			this.surname = suggestionFilterForm.get('searchPhraseSurname')?.value;
		}
		
		/**
		* Add any geoZone filters 	
		*/
		this.availableGeoZones.forEach(geoZone => {
			let key = geoZone.geoZoneId.toLowerCase() + 'Results';
			
			if (suggestionFilterForm.get(key)?.value) {
				this.geoZones.push(geoZone.geoZoneId);
			}
			
		});
		
		/**
		* Add any country filters 	
		*/
		this.supportedCountries.forEach(country => {
			let key = country.iso2Code + 'Results';
			if (suggestionFilterForm.get(key)?.value) {
				this.countries.push(country.name);
			}
			
		});
		
		/**
		* Add any language filters 	
		*/
		supportedLanguages.forEach(lang => {
			if (suggestionFilterForm.get(lang.languageCode.toLowerCase()+'Language')?.value) {
				this.languages.push(lang.languageCode);
			}
		});
				
		/**
		* Ccontract type filters
		*/
		if (suggestionFilterForm.get('contractType')?.value === 'BOTH'){
			//perm 		= false;
			//contract 	= false;
		}
		
		if (suggestionFilterForm.get('contractType')?.value === 'CONTRACT'){
			this.contract 	= "true";
		}
		
		if (suggestionFilterForm.get('contractType')?.value === 'PERM'){
			this.perm 		= "true";
			
		}
		
	}
	
	/**
	* Returns geoZones to filter on
	*/
	public getGeoZones():Array<string>{
		return this.geoZones;
	} 
	/**
	* Returns countries to filter on
	*/
	public getCountries():Array<string>{
		return this.countries;
	} 
	/**
	* Returns skills to filter on
	*/	
	public getSkills():Array<string>{
		return this.skills;
	}
		/**
	* Returns function types to filter on
	*/	
	public getFunctionTYpes():Array<string>{
		return this.functionTypes;
	}
	/**
	* Returns languages to filter on
	*/
	public getLanguages():Array<string>{
		return this.languages;
	}
	/**
	* Returns title to filter on
	*/	
	public getTitle():string{
		return this.title;
	}
	/**
	* Returns contract option to filter on
	*/
	public getContract():string{
		return this.contract;
	}
	/**
	* Returns perm option to filter on
	*/
	public getPerm():string{
		return this.perm;
	}
	/**
	* Returns minimum number of years to filter on
	*/
	public getMinExperience():string{
		return this.minExperience
	}
	/**
	* Returns max number of years to filter on
	*/
	public getMaxExperience():string {
		return this.maxExperience
	}
	
	public getLanguageLevel(languageCode:string):string{
		return this.languages.includes(languageCode) ? "PROFICIENT" : "UNKNOWN";
	}
	
	/**
	* Whether to include unavailable cadidates
	*/
	public getIncludUnavailableCandidates():string {
		return this.unavailableCadidates;
	}
	
	public getIncludRequiresSponsorshipCandidates():string {
		return this.includeRequiresSponsorship;
	}
	
	/**
	* firstname filter
	*/
	public getFirstName():string {
		return this.firstName;
	}
	
	/**
	* surname filter
	*/
	public getSurname():string {
		return this.surname;
	}
	
	/**
	* email filter
	*/
	public getEmail():string {
		return this.email;
	}
	
	/**
	* Candidate Id filter
	*/
	public getCandidateId():string{
		return this.candidateId;
	}
	
	public getLocCountry():string{
		return this.locCountry;
	}	
	
	public getLocCity():string{
		return this.locCity;	
	}
	public getLocDistance():number{
		return this.locDistance;
	}
	
}