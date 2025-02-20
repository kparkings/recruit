import { Component } 												from '@angular/core';
import { UntypedFormGroup,UntypedFormControl } 						from '@angular/forms';
import { TranslateService } 										from '@ngx-translate/core';
import { CandidateServiceService }									from './../../candidate-service.service';
import { SuggestionsService }										from './../../suggestions.service';
import { GeoZone } 													from './../../geo-zone';
import { SupportedCountry } 										from './../../supported-candidate';


@Component({
  selector: 'app-searchbarlisting',
  standalone: false,
  templateUrl: './searchbar.component.html',
  styleUrl: './searchbar.component.css'
})
export class SearchbarComponentListing {

	public geoZones:Array<GeoZone>								= new Array<GeoZone>();
	public supportedCountries:Array<SupportedCountry>			= new Array<SupportedCountry>();
		
	public listingsFilterForm:UntypedFormGroup 					= new UntypedFormGroup({});
	public filterView:string									= 'collapsed';
	public showCountryFilters:string							= "";
	public showGeoZoneFilters:string							= "";
	public backendRequestCounter:number							= 0;
	
	/**
	* Constructor
	*/
	public constructor(
		private translate:				TranslateService,
		public candidateService:		CandidateServiceService,
		private suggestionsService:		SuggestionsService){

		this.init();	
		
	}
	
	/**
	* Angular lifecycyle: Initializes Component
	*/
	ngOnInit(): void {
		//this.subscription = this.suggestionFilterForm.valueChanges.pipe(debounceTime(500)).subscribe(() => {
		//		this.getSuggestions(false);
		//});
	}
	
	/**
	* Initializes the component
	*/
	public init():void{
		
		this.resetSearchFilters();	
				
		this.initGeoZones();
		this.initSupportedCountries();
		
		//if (this.subscription) {
		//	this.subscription.unsubscribe();
		//}
				
	}
	
	/**
	* Resets the filters
	*/
	public resetSearchFilters():void{
			
		//this.suggestionFilterForm 	= SearchBarFilterFormHelper.resetSuggestionFilterForm(this.suggestionFilterForm);
		
		this.listingsFilterForm.addControl('searchPhrase', new UntypedFormControl(''));
		this.listingsFilterForm.addControl('contractType', new UntypedFormControl(''));
		
		this.candidateService.getSupportedCountries().forEach(c => {
			this.listingsFilterForm.addControl(c.name, new UntypedFormControl(false));
		});
						
		
		this.initGeoZones();
		this.showCountryFilters 	= '';
			
		this.backendRequestCounter 		= 0;
				
	}
		
	/**
	* Toggles between displaying and hiding of the filters
	*/
	public toggleFilters():void{
		
		if (this.filterView == 'collapsed'){
			this.filterView = 'expanded';
		} else {
			this.filterView = 'collapsed';
		}
		
	}
	
	/**
	* Swithches between open and closed filter view for GeoZones 
	*/
	public switchCountriesFilterView(view:string):void{
		this.showCountryFilters = view;
	}
	
	/**
	* Swithches between open and closed filter view for GeoZones 
	*/
	public switchGeoZoneFilterView(view:string):void{
		//this.initGeoZones();
		this.showGeoZoneFilters = view;
	}
	
	/**
	* Returns whether Candidates from the selected country are currently
	* included in the Suggestion results
	*/
	public includeResultsForCountry(country:string):boolean{
		return this.listingsFilterForm.get((country))?.value;
	}
	
	/**
	* Returns whether Candidates from the selected GeoZone are currently
	* included in the Suggestion results
	*/
	public includeResultsForGeoZone(geoZone:GeoZone):boolean{
		return this.listingsFilterForm.get((geoZone.geoZoneId.toLowerCase()+'Results'))?.value;
	}
	
	/**
	* Toggles GeoCpde
	*/
	public toggleGeoZoneSelection(geoZone:GeoZone):void{
		
		let included:boolean = this.listingsFilterForm.get((geoZone.geoZoneId.toLowerCase()+'Results'))?.value;
		this.listingsFilterForm.get((geoZone.geoZoneId.toLowerCase()+'Results'))?.setValue(!included);

		let geoZoneActive = this.candidateService.getGeoZones().filter(gz => this.listingsFilterForm.get((gz.geoZoneId.toLowerCase()+'Results'))?.value == true).length > 0;

		if (!geoZoneActive) {
			this.candidateService.getSupportedCountries().forEach(country => {
				//let key = country.iso2Code + 'Results';
				this.listingsFilterForm.get(country.name)?.setValue(true);
			});
		} else {
			this.candidateService.getSupportedCountries().forEach(country => {
				//let key = country.iso2Code.toLowerCase() + 'Results';
				this.listingsFilterForm.get(country.name)?.setValue(false);
			});
		
		}
		
	}
	
	/**
	* Toggles whether Candidates from selected country 
	* should be included in the Suggestions 
	* @param country - Country to toggle
	*/
	public toggleCountrySelection(country:string):void{
		
		let included:boolean = this.listingsFilterForm.get((country))?.value;
		
		this.listingsFilterForm.get((country))?.setValue(!included);
		
		this.geoZones.forEach(geoZone => {
			let key = geoZone.geoZoneId.toLowerCase() + 'Results';
			this.listingsFilterForm.get(key)?.setValue(false);
		});
				
	}
	
	/**
	*  
	*/
	private initGeoZones():void{
		
		this.geoZones = this.candidateService.getGeoZones();
		
		this.geoZones.forEach(gz => {
			this.listingsFilterForm.addControl(gz.geoZoneId.toLowerCase()+'Results', new UntypedFormControl(false));
		});
		
		this.showGeoZoneFilters 	= "";
		
	}	
	
	private initSupportedCountries():void{
		this.supportedCountries = this.candidateService.getSupportedCountries();
	}
	
}
