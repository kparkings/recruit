import { ExtractedFilters } 										from "../extracted-filters";
import { UntypedFormControl, UntypedFormGroup }						from '@angular/forms';

/**
* Helper class for populating the Search bar's for filters witht 
* pre-defined values 
*/
export class SearchBarFilterFormHelper{
	
	public static 	minMaxOptions:Array<string> 	= new Array<string>('','1','2','3','4','5','8','10','15','20','25','30');
	public static  	FIRST_NAME_DEFAULT:string 		= 'First Name';
	public static 	SURNAME_DEFAULT:string 			= 'Surname';
		
	/**
	* Applies values extracted values from text/files to the filter form 
	*/
	public static applyExtractedFiltersToForm(extractedFilters:ExtractedFilters, skillFilters:Array<string>, filterForm:UntypedFormGroup) {
		
		extractedFilters.skills.sort();
		skillFilters.splice(0);
		
		for(let i = 0; i < extractedFilters.skills.length; i++){
			skillFilters.push(extractedFilters.skills[i]);
		}
				
		if (extractedFilters.jobTitle != ''){
			filterForm.get('searchPhrase')?.setValue(extractedFilters.jobTitle);	
		}
					
		if (extractedFilters.netherlands || extractedFilters.belgium || extractedFilters.uk || extractedFilters.ireland){
			
			filterForm.get('nlResults')?.setValue(false);
			filterForm.get('beResults')?.setValue(false);
			filterForm.get('ukResults')?.setValue(false);
			filterForm.get('ieResults')?.setValue(false);
						
			if (extractedFilters.netherlands)  {
				filterForm.get('nlResults')?.setValue(extractedFilters.netherlands);
			}
							
			if (extractedFilters.belgium) {
				filterForm.get('beResults')?.setValue(extractedFilters.belgium);
			}
							
			if (extractedFilters.uk) {
				filterForm.get('ukResults')?.setValue(extractedFilters.uk);
			}
							
			if (extractedFilters.ireland) {
				filterForm.get('ieResults')?.setValue(extractedFilters.ireland);
			}
							
		}	
						
		if (extractedFilters.perm != 'TRUE' && extractedFilters.freelance != 'TRUE') {
			filterForm.get('contractType')?.setValue("BOTH");
		} else if (extractedFilters.perm == 'TRUE' && extractedFilters.freelance == 'TRUE') {
			filterForm.get('contractType')?.setValue("BOTH");
		} else if (extractedFilters.perm != 'TRUE'){
			filterForm.get('contractType')?.setValue("CONTRACT");
		} else if (extractedFilters.freelance != 'TRUE'){
			filterForm.get('contractType')?.setValue("PERM");
		}
					
		if (extractedFilters.dutch) {
			filterForm.get('dutchLanguage')?.setValue(extractedFilters.dutch);
		}
					
		if (extractedFilters.english) {
			filterForm.get('englishLanguage')?.setValue(extractedFilters.english);
		}
					
		if (extractedFilters.french) {
			filterForm.get('frenchLanguage')?.setValue(extractedFilters.french);
		}
						
		if (extractedFilters.experienceGTE != '') {
			if (SearchBarFilterFormHelper.minMaxOptions.indexOf(extractedFilters.experienceGTE) != -1){
				filterForm.get('minYearsExperience')?.setValue(extractedFilters.experienceGTE);	
			}	
		}
						
		if (extractedFilters.experienceLTE != '') {
			if (SearchBarFilterFormHelper.minMaxOptions.indexOf(extractedFilters.experienceLTE) != -1){
				filterForm.get('maxYearsExperience')?.setValue(extractedFilters.experienceLTE);	
			}
		}
		
	}
	
	/**
	* Resets the search form in the SearchBar
	*/
	public static resetSearchTytpeFilterForm(filterTypeFormGroup:UntypedFormGroup):UntypedFormGroup{
		return new UntypedFormGroup({
			searchType:new UntypedFormControl('FUNCTION'),
		});
	}
			
	/**
	* Resets the search form in the SearchBar
	*/
	public static resetSuggestionFilterForm(suggestionFilterForm:UntypedFormGroup):UntypedFormGroup{
		return new UntypedFormGroup({
			searchPhrase:							new UntypedFormControl(''),
			searchPhraseFirstName:					new UntypedFormControl(this.FIRST_NAME_DEFAULT),
			searchPhraseSurname:					new UntypedFormControl(this.SURNAME_DEFAULT),
			contractType: 							new UntypedFormControl('Both'),
			minYearsExperience: 					new UntypedFormControl(''),
			maxYearsExperience: 					new UntypedFormControl(''),
			skill: 									new UntypedFormControl(''),
			includeUnavailableCandidates: 			new UntypedFormControl(''),
			includeRequiresSponsorshipCandidates:	new UntypedFormControl(''),
			locationCountry:						new UntypedFormControl(),
			locationCity:							new UntypedFormControl(''),
			locationDistance:						new UntypedFormControl(''),
		});
	}
	
}