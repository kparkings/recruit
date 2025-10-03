import { SearchbarComponent }										from './searchbar.component';
import { TranslateService } 										from '@ngx-translate/core';
import { GeoZone } 													from './../../geo-zone';

/**
* Defines all the actions that the html template can call relating to the SearchBar
* The object is intended to make the interface between the template and component 
* clear and easier to test 
*/
export class SearchBarAPI {

	/**
	* Constructor
	* @param component - Component whose template we are providing an API to
	* @param translate - Service for translating text into different languages 
	*/
	constructor(public component: SearchbarComponent, public translate: TranslateService) {
		
	}
	
	/**
	* Shows search type selection modal 
	*/
	public doShowSearchTypeFilterSelectionModal():void{
				
		this.component.FIRST_NAME_DEFAULT 	= this.translate.instant('arenella-suggestions-search-type-name-firstname-default');
		this.component.SURNAME_DEFAULT 		= this.translate.instant('arenella-suggestions-search-type-name-surname-default');
				
		this.component.searchTypeFilterSelectionModal.nativeElement.showModal();;
	}
	
	/**
	* Sets the default values in the firstname/surname fields of the search form
	* when a User selects to search on names
	*/
	public cssSearchNameDefault():string{
		
		if (this.getCurrentSearchFilterType() != 'NAME') {
			return '';
		}
		
		let firstName:string 	= this.component.suggestionFilterForm.get('searchPhraseFirstName')?.value;
		let surname:string 		= this.component.suggestionFilterForm.get('searchPhraseSurname')?.value;
		
		if (firstName == this.component.FIRST_NAME_DEFAULT || surname == this.component.SURNAME_DEFAULT){
			return 'search-name-default';
		}
		
		return '';
		
	}
	
	/**
	* Returns the search type. There are various types of searches such as 
	* searching on job title, name, email and candidateId. This function
	* returns the currently selected type
	*/
	public getCurrentSearchFilterType():string{
		return this.component.filterTypeFormGroup.get('searchType')?.value;
	}
	
	/**
	* Displays a modal box for saving Search queries 
	*/
	public showSavedSearchQueriesBox():void{
		this.component.savedSearchQueriesBox.nativeElement.showModal();
	}
	
	/**
	* Toggles between displaying and hiding of the filters
	*/
	public toggleFilters():void{
		
		if (this.component.filterView == 'collapsed'){
			this.component.filterView = 'expanded';
		} else {
			this.component.filterView = 'collapsed';
		}
		
	}
	
	public simpleTest():string {
		return "done testing";
	}
	
	/**
	* Swithches between open and closed filter view for GeoZones 
	*/
	public switchGeoZoneFilterView(view:string):void{
		this.component.showGeoZoneFilters = view;
	}
	
	/**
	* Toggles GeoCpde
	*/
	public toggleGeoZoneSelection(geoZone:GeoZone):void{
		
		let included:boolean = this.component.suggestionFilterForm.get((geoZone.geoZoneId.toLowerCase()+'Results'))?.value;
		this.component.suggestionFilterForm.get((geoZone.geoZoneId.toLowerCase()+'Results'))?.setValue(!included);
	
		let geoZoneActive = this.component.candidateService.getGeoZones().filter(gz => this.component.suggestionFilterForm.get((gz.geoZoneId.toLowerCase()+'Results'))?.value == true).length > 0;
	
		if (!geoZoneActive) {
			this.component.candidateService.getSupportedCountries().forEach(country => {
				this.component.suggestionFilterForm.get(country.name)?.setValue(true);
			});
		} else {
			this.component.candidateService.getSupportedCountries().forEach(country => {
				this.component.suggestionFilterForm.get(country.name)?.setValue(false);
			});
		
		}
		
	}
	
	/**
	* Swithches between open and closed filter view for Location 
	*/
	public switchLocationFilterView(view:string):void{
		this.component.showLocationFilters = view;
		setTimeout(()=>{ 
			if (this.component.extractedFilters.city != "") {
				this.component.suggestionFilterForm.get('locationCity')?.setValue(this.component.extractedFilters.city);
			}	
		 }, 50)
		
	}
	
	/**
	* Returns whether Candidates from the selected GeoZone are currently
	* included in the Suggestion results
	*/
	public includeResultsForGeoZone(geoZone:GeoZone):boolean{
		return this.component.	suggestionFilterForm.get((geoZone.geoZoneId.toLowerCase()+'Results'))?.value;
	}
		
}