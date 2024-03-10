import { UntypedFormGroup }				from '@angular/forms';
import { CandidateFunction }		from '../candidate-function';

/**
* Class containing the logic to prepare the filter params for the 
* Suggestions search
*/
export class SuggestionParams{
	
	private countries:Array<string> 					= new Array<string>();
	private skills:Array<string> 						= new Array<string>();
	private functionTypes:Array<string>					= new Array<string>();
	private languages:Array<string> 					= new Array<string>();
	private title:string 								= "";
	private contract:string 							= "";
	private perm:string 								= "";
	private minExperience:string 						= "";
	private maxExperience:string 						= "";
	private dutch:string								= "UNKNOWN";
	private french:string								= "UNKNOWN";
	private english:string								= "UNKNOWN";
	private unavailableCadidates:string					= "";
		
	/**
	* Constructor
	* @param suggestionFilterForm 	- contains raw filter info
	* @param skillFilters			- contains raw skill filter info 
	* @param functionTypes			- contains raw function types info
	*/
	public constructor(suggestionFilterForm:UntypedFormGroup, skillFilters:Array<string>, functionTypes:Array<string>){
		
		this.skills 		= skillFilters.concat();
		this.functionTypes	= functionTypes;
		this.title  		= suggestionFilterForm.get('searchPhrase')?.value;
		this.minExperience 	= suggestionFilterForm.get('minYearsExperience')?.value;
		this.maxExperience 	= suggestionFilterForm.get('maxYearsExperience')?.value;
		
		this.unavailableCadidates = suggestionFilterForm.get('includeUnavailableCandidates')?.value;
		
		/**
		* Add any country filters 	
		*/
		
		if (suggestionFilterForm.get('nlResults')?.value) {
			this.countries.push("NETHERLANDS");
		}
		
		if (suggestionFilterForm.get('beResults')?.value) {
			this.countries.push("BELGIUM");
		}
		
		if (suggestionFilterForm.get('ukResults')?.value) {
			this.countries.push("UK");
		}

		if (suggestionFilterForm.get('ieResults')?.value) {
			this.countries.push("REPUBLIC_OF_IRELAND");
		}
		
		/**
		* Add any language filters 	
		*/
		if (suggestionFilterForm.get('dutchLanguage')?.value) {
			this.languages.push("DUTCH");
			this.dutch = "PROFICIENT";
		}
		
		if (suggestionFilterForm.get('frenchLanguage')?.value) {
			this.languages.push("FRENCH");
			this.french = "PROFICIENT";
		}
		
		if (suggestionFilterForm.get('englishLanguage')?.value) {
			this.languages.push("ENGLISH");
			this.english = "PROFICIENT";
		}
				
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
	
	/**
	* Proficiency level for Dutch
	*/
	public getDutchLevel():string{
		return this.dutch;	
	}
	
	/**
	* Proficiency level for French
	*/
	public getFrenchLevel():string{
		return this.french;	
	}
	
	/**
	* Proficiency level for English
	*/
	public getEnglishLevel():string{
		return this.english;	
	}
	
	/**
	* Whether to include unavailable cadidates
	*/
	public getIncludUnavailableCandidates():string {
		return this.unavailableCadidates;
	}
	
}