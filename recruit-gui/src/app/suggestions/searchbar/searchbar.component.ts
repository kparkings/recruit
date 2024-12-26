import { Component, ViewChild, ElementRef, Output, EventEmitter } 	from '@angular/core';
import { UntypedFormGroup, UntypedFormControl }						from '@angular/forms';
import { Router}													from '@angular/router';
import { TranslateService } 										from '@ngx-translate/core';
import { Subscription } 											from 'rxjs';
import { debounceTime, map } 										from "rxjs/operators";
import { Candidate}													from './../candidate';
import { CreditsService } 											from './../../credits.service';
import { SupportedLanguage } 										from './../../supported-language';
import { City } 													from './../../city';
import { SupportedCountry } 										from './../../supported-candidate';
import { GeoZone } 													from './../../geo-zone';
import { CandidateServiceService }									from './../../candidate-service.service';
import { CandidateTotals } 											from './../../candidate-totals';
import { ExtractedFilters } 										from './../extracted-filters';
import { SuggestionParams}											from './.././suggestion-param-generator';
import { SuggestionsService }										from './../../suggestions.service';
import { CurrentUserAuth }											from './../../current-user-auth';
import { SearchBarFilterFormHelper } 								from './search-fiter-form-helper';
import { SuggestionsSearchRequest } 								from '../suggestion-search-request';

@Component({
  selector: 'app-searchbar',
  standalone: false,
  templateUrl: './searchbar.component.html',
  styleUrl: './searchbar.component.css'
})
export class SearchbarComponent {

	@ViewChild('searchTypeFilterSelectionModal', {static:true}) searchTypeFilterSelectionModal!: ElementRef<HTMLDialogElement>;
	@ViewChild('paidSubscriptionModal', {static:true})			paidSubscriptionBox!: ElementRef<HTMLDialogElement>;
	@Output() newSuggestionResults 								= new EventEmitter<Array<Candidate>>();
	
	private FIRST_NAME_DEFAULT:string 							= 'First Name';
	private SURNAME_DEFAULT:string 								= 'Surname';
	
	public showGeoZoneFilters:string							= "";
	public includeUnavailableCandidatesSelected:string 			= 'true';
	public filterView:string									= 'collapsed';
	public includeRequiresSponsorshipCandidatesSelected:string  = 'true';
	public paidFeature:string								 	= '';
	public showIncludeFilters:string							= "";
	public skillFilters:Array<string>							= new Array<string>();
	public minMaxOptions:Array<string> 							= new Array<string>('','1','2','3','4','5','8','10','15','20','25','30');
	public showLanguageFilters:string							= "";
	public supportedLanguages:Array<SupportedLanguage> 			= new Array<SupportedLanguage>();
	public showCountryFilters:string							= "";
	public supportedCountries:Array<SupportedCountry>			= new Array<SupportedCountry>();
	public suggestionFilterForm:UntypedFormGroup 				= new UntypedFormGroup({});
	public citiesForSelectedCountry:Array<City>					= new Array<City>();
	public showLocationFilters:string							= "";
	public geoZones:Array<GeoZone>								= new Array<GeoZone>();
	public candidateTotals:CandidateTotals					 	= new CandidateTotals(0,0,0);
	public backendRequestCounter:number							= 0;
	private subscription?:Subscription;
	public suggestions:Array<Candidate>  						= new Array<Candidate>();
	public currentUserAuth:CurrentUserAuth 						= new CurrentUserAuth();
	
	public skilFilterForm:UntypedFormGroup 						= new UntypedFormGroup({
		skill: 													new UntypedFormControl(''),
	});
	
	public getCurrentSearchFilterType():string{
		return this.filterTypeFormGroup.get('searchType')?.value;
	}
	
	public filterTypeFormGroup:UntypedFormGroup					= new UntypedFormGroup({
		searchType:												new UntypedFormControl('FUNCTION'),
	});
	
	/**
	* Constructor
	*/
	public constructor(
		private translate:				TranslateService,
		public candidateService:		CandidateServiceService,
		private creditsService:			CreditsService,
		private suggestionsService:		SuggestionsService,
		private router:					Router){
	
		this.init();	
			
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
	* Angular lifecycyle: Initializes Component
	*/
	ngOnInit(): void {
		this.subscription = this.suggestionFilterForm.valueChanges.pipe(debounceTime(500)).subscribe(() => {
			this.getSuggestions(false);
		});
	}
	
	/**
	* Angular lifecycyle: After view exists
	*/	
	ngAfterViewChecked(){
		if (sessionStorage.getItem("news-item-div")){
			this.doScrollTop();	
		}
	}
	
	/**
	* Initializes the component
	*/
	public init():void{
		
		this.resetSearchFilters();	
				
		if (!this.currentUserAuth.isCandidate()) {
			this.getSuggestions(true);	
		}
				
		this.initGeoZones();
		this.initSupportedCountries();
				
		this.candidateService.fetchCandidateTotals().subscribe(totals => this.candidateTotals = totals);
				
		if (this.subscription) {
			this.subscription.unsubscribe();
		}
				
	}
	
	/**
	* Shows search type selection modal 
	*/
	public doShowSearchTypeFilterSelectionModal():void{
			
		this.FIRST_NAME_DEFAULT 	= this.translate.instant('arenella-suggestions-search-type-name-firstname-default');
		this.SURNAME_DEFAULT 		= this.translate.instant('arenella-suggestions-search-type-name-surname-default');
			
		this.searchTypeFilterSelectionModal.nativeElement.showModal();;
	}
	
	/**
	* Sets the default values in the firstname/surname fields of the search form
	* when a User selects to search on names
	*/
	public cssSearchNameDefault():string{
		
		if (this.getCurrentSearchFilterType() != 'NAME') {
			return '';
		}
		
		let firstName:string 	= this.suggestionFilterForm.get('searchPhraseFirstName')?.value;
		let surname:string 		= this.suggestionFilterForm.get('searchPhraseSurname')?.value;
		
		if (firstName == this.FIRST_NAME_DEFAULT || surname == this.SURNAME_DEFAULT){
			return 'search-name-default';
		}
		
		return '';
		
	}
	
	/**
	* When a User clicks in the firstname/surname search fields, removes the default 
	* value ready for the User to enter the values they want to seearch on
	*/	
	public activateFirstnameSurnameFields():void{
				
		let firstName:string 	= this.suggestionFilterForm.get('searchPhraseFirstName')?.value;
		let surname:string 		= this.suggestionFilterForm.get('searchPhraseSurname')?.value;
				
		if (firstName && firstName == this.FIRST_NAME_DEFAULT){
			this.suggestionFilterForm.get('searchPhraseFirstName')?.setValue('');	
		}
				
		if(surname && surname == this.SURNAME_DEFAULT){
			this.suggestionFilterForm.get('searchPhraseSurname')?.setValue('');
		}
				
		this.suggestionFilterForm.get('searchPhrase')?.setValue('');
	}
			
	/**
	* Closes the searchTypeFilterSelectionModal modal box 
	*/
	public closeSearchTypeBox(type:string):void{
		this.resetSearhFirstnameSurnameFields();
		this.filterTypeFormGroup.get('searchType')?.setValue(type);
		this.searchTypeFilterSelectionModal.nativeElement.close();
	}
	
	/**
	* Resets the default values of thte firstname/surname search fields
	*/		
	public resetSearhFirstnameSurnameFields():void{
		this.suggestionFilterForm.get('searchPhraseFirstName')?.setValue(this.FIRST_NAME_DEFAULT);
		this.suggestionFilterForm.get('searchPhraseSurname')?.setValue(this.SURNAME_DEFAULT);
		this.suggestionFilterForm.get('searchPhrase')?.setValue('');
	} 
			
	/**
	* Adds a skill to the list of Skills to filter on
	*/
	public addSkill():void{
					
		let skillFormatted:string 	= this.skilFilterForm.get('skill')?.value.trim();
		skillFormatted 				= skillFormatted.toLocaleLowerCase();
		
		if (skillFormatted.length > 0 && this.skillFilters.indexOf(skillFormatted) == -1) {
			this.skillFilters.push(skillFormatted);	
			this.skillFilters.sort();
		}
		
		this.getSuggestions(false);
							
		this.skilFilterForm.get('skill')?.setValue('');
		
	}
				
	/**
	* Removes a skill and calls for new suggestions
	*/
	public removeSkill(skill:string):void{
					
		skill = skill.trim();
		skill = skill.toLocaleLowerCase();
		
		this.skillFilters = this.skillFilters.filter(s => s  !== skill);
		
		this.getSuggestions(false);	
						
	}
	
	/**
	* Whether or not the User is a Admin
	*/
	public doPaidSubscriptionCheckRequiresSponsorshipCandidates():void{
		this.paidFeature = 'paidFeatureRequiresSponsorshipeCandidates';
		if(this.doPaidSubscriptionCheck()){
			let checked =  this.suggestionFilterForm.get("includeRequiresSponsorshipCandidates")?.value;
			
			if(checked){
				this.suggestionFilterForm.get("includeRequiresSponsorshipCandidates")?.setValue('');	
				this.includeRequiresSponsorshipCandidatesSelected = 'false';
			} else {
				this.suggestionFilterForm.get("includeRequiresSponsorshipCandidates")?.setValue('false');
				this.includeRequiresSponsorshipCandidatesSelected = 'true';
			}
		}
	}
	
	/**
	* Swithches between open and closed filter view for Languages 
	*/
	public switchIncludeFilterView(view:string):void{
		this.showIncludeFilters = view;
	}

	/**
	*  
	*/					
	public doPaidSubscriptionCheckUnavailableCandidates():void{
	
		this.paidFeature = 'paidFeatureUnavailableCandidates';
		
		if (this.doPaidSubscriptionCheck()){
				
			let checked =  this.suggestionFilterForm.get("includeUnavailableCandidates")?.value;
			
			if(checked){
				this.suggestionFilterForm.get("includeUnavailableCandidates")?.setValue('');	
				this.includeUnavailableCandidatesSelected = 'false';
			} else {
				this.suggestionFilterForm.get("includeUnavailableCandidates")?.setValue('true');
				this.includeUnavailableCandidatesSelected = 'true';
			}
			
		}
	}
						
	public doPaidSubscriptionCheck():boolean{
		if (!this.currentUserAuth.isAdmin() && !this.currentUserAuth.isCandidate() && !this.currentUserAuth.hasPaidSubscription()) {
			this.suggestionFilterForm.get("includeUnavailableCandidates")?.setValue('');
			this.paidSubscriptionBox.nativeElement.showModal();
			return false;
		}
		return true;
	}
						
	public choseSubscription():void{
		this.creditsService.buySubscription();
		this.router.navigate(['recruiter-account']);
	}

	/**
	* Swithches between open and closed filter view for Languages 
	*/
	public switchLanguageFilterView(view:string):void{
		this.showLanguageFilters = view;
	}
							
	/**
	* Swithches between open and closed filter view for GeoZones 
	*/
	public switchCountriesFilterView(view:string):void{
		this.showCountryFilters = view;
	}

	/**
	* Swithches between open and closed filter view for Location 
	*/
	public switchLocationFilterView(view:string):void{
		this.showLocationFilters = view;
	}
	
	/**
	* Swithches between open and closed filter view for GeoZones 
	*/
	public switchGeoZoneFilterView(view:string):void{
		this.initGeoZones();
		this.showGeoZoneFilters = view;
	}

	/**
	*  
	*/
	private initGeoZones():void{
		
		this.geoZones = this.candidateService.getGeoZones();
		
		this.geoZones.forEach(gz => {
			this.suggestionFilterForm.addControl(gz.geoZoneId.toLowerCase()+'Results', new UntypedFormControl(false));
		});
		
		this.showGeoZoneFilters 	= "";
		this.showLocationFilters 	= "";
		
	}					
							
	/**
	* Toggles whether Candidates from selected country 
	* should be included in the Suggestions 
	* @param country - Country to toggle
	*/
	public toggleCountrySelection(country:string):void{
		
		let included:boolean = this.suggestionFilterForm.get((country+'Results'))?.value;
		
		this.suggestionFilterForm.get((country+'Results'))?.setValue(!included);
		
		this.geoZones.forEach(geoZone => {
			let key = geoZone.geoZoneId.toLowerCase() + 'Results';
			this.suggestionFilterForm.get(key)?.setValue(false);
		});
				
	}
									
	/**
	* Returns whether Candidates from the selected country are currently
	* included in the Suggestion results
	*/
	public includeResultsForCountry(country:string):boolean{
		return this.suggestionFilterForm.get((country+'Results'))?.value;
	}
	
	/**
	* Used for Range query. When country is selected loads Cities
	* for that country. 
	* 
	* TODO: [0]KP] Needs to change to pick lost of major cities
	* not all cities for country as part of refactor
	*/
	public switchLocationCountry():void{
		
		//TODO: [KP] need to setup form and use the selectd value
		let country = this.suggestionFilterForm.get('locationCountry')?.value;
		this.citiesForSelectedCountry = new Array<City>();
		this.candidateService.getCitiesForCountry(country).subscribe(b => {
			this.citiesForSelectedCountry = b;	
		});
	}
									
	/**
	* Toggles GeoCpde
	*/
	public toggleGeoZoneSelection(geoZone:GeoZone):void{
		
		let included:boolean = this.suggestionFilterForm.get((geoZone.geoZoneId.toLowerCase()+'Results'))?.value;
		this.suggestionFilterForm.get((geoZone.geoZoneId.toLowerCase()+'Results'))?.setValue(!included);
	
		let geoZoneActive = this.candidateService.getGeoZones().filter(gz => this.suggestionFilterForm.get((gz.geoZoneId.toLowerCase()+'Results'))?.value == true).length > 0;
	
		if (!geoZoneActive) {
			this.candidateService.getSupportedCountries().forEach(country => {
				let key = country.iso2Code + 'Results';
				this.suggestionFilterForm.get(key)?.setValue(true);
			});
		} else {
			this.candidateService.getSupportedCountries().forEach(country => {
				let key = country.iso2Code.toLowerCase() + 'Results';
				this.suggestionFilterForm.get(key)?.setValue(false);
			});
		
		}
		
	}
										
	/**
	* Returns whether Candidates from the selected GeoZone are currently
	* included in the Suggestion results
	*/
	public includeResultsForGeoZone(geoZone:GeoZone):boolean{
		return this.suggestionFilterForm.get((geoZone.geoZoneId.toLowerCase()+'Results'))?.value;
	}
											
	private initSupportedCountries():void{
		this.supportedCountries = this.candidateService.getSupportedCountries();
	}
	
	/**
	* Resets the filters
	*/
	public resetSearchFilters():void{
			
		this.suggestionFilterForm 	= SearchBarFilterFormHelper.resetSuggestionFilterForm(this.suggestionFilterForm);
		this.filterTypeFormGroup 	= SearchBarFilterFormHelper.resetSearchTytpeFilterForm(this.filterTypeFormGroup);
						
		this.candidateService.getSupportedCountries().forEach(c => {
			this.suggestionFilterForm.addControl(c.iso2Code+'Results', new UntypedFormControl(false));
		});
						
		this.supportedLanguages = new Array<SupportedLanguage>();
				
		this.candidateService.getLanguages().forEach(lang => {
			this.suggestionFilterForm.addControl(lang.languageCode.toLowerCase()+'Language', new UntypedFormControl(false));
			this.supportedLanguages.push(lang);
		});
						
		this.initGeoZones();
		this.showCountryFilters 	= '';
		this.showLanguageFilters 	= '';
		this.showIncludeFilters 	= '';
			
		this.skilFilterForm = new UntypedFormGroup({
			skill: new UntypedFormControl(''),
		});
		
		this.skillFilters = new Array<string>();
			
		this.backendRequestCounter 		= 0;
			
		this.includeUnavailableCandidatesSelected = 'false';
		this.includeRequiresSponsorshipCandidatesSelected = 'false';
				
	}
	
	/**
	* Sends request for Suggestions to the backend API
	*/
	public getSuggestions(isUnfiltered:boolean):void{
		
		const maxSuggestions:number 		= 112;
		
		let params:SuggestionParams = new SuggestionParams(this.filterTypeFormGroup, this.suggestionFilterForm, this.skillFilters, new Array<string>(), this.candidateService.getGeoZones(), this.candidateService.supportedCountries, this.supportedLanguages);
		
		let backendRequestId = this.backendRequestCounter + 1;
		this.backendRequestCounter = backendRequestId;
		
		//START
		
		//TODO: [KP] 1. Implement backend call to perform sSearchbarComponent
		//TODO: [KP] 2. Change logic to only set available params
		//TODO: [KP] 3. Mce logic into separate class like SuggestionParams
		
		let ssReq:SuggestionsSearchRequest = new SuggestionsSearchRequest();
		
		ssReq.requestFilters.backendRequestId 						= backendRequestId;
		ssReq.requestFilters.unfiltered 							= isUnfiltered;
		ssReq.requestFilters.maxNumberOfSuggestions 				= maxSuggestions;
		
		ssReq.contractFilters.contract 								= params.getContract()?.toUpperCase();
		ssReq.contractFilters.perm 									= params.getPerm()?.toUpperCase();
		
		ssReq.experienceFilters.experienceMin 						= params.getMinExperience();
		ssReq.experienceFilters.experienceMax 						= params.getMaxExperience();
		
		ssReq.includeFilters.includeRequiresSponsorshipCandidates 	= params.getIncludRequiresSponsorshipCandidates(),
		ssReq.includeFilters.includeUnavailableCandidates			= params.getIncludUnavailableCandidates();
		
		ssReq.languageFilters.languages 							= params.getLanguages();
		
		ssReq.locationFilters.countries 							= params.getCountries();
		ssReq.locationFilters.geoZones 								= params.getGeoZones();
		ssReq.locationFilters.range.country 						= params.getLocCountry();
		ssReq.locationFilters.range.city 							= params.getLocCity();
		ssReq.locationFilters.range.distanceInKm 					= params.getLocDistance();
		
		ssReq.skillFilters.skills 									= params.getSkills();
		
		ssReq.termFilters.candidateId 								= params.getCandidateId();
		ssReq.termFilters.email 									= params.getEmail();
		ssReq.termFilters.title 									= params.getTitle();
		ssReq.termFilters.firstName 								= params.getFirstName();
		ssReq.termFilters.surname 									= params.getSurname();
		
		this.suggestionsService
			.getCandidateSuggestions(ssReq).pipe(
				map((response) => {
					
					const responseRequestId = response.headers.get('X-Arenella-Request-Id');

					
					if (this.backendRequestCounter == responseRequestId) {
														
						this.suggestions =  new Array<Candidate>();
								
						response.body.content.forEach((s:Candidate) => {
							this.suggestions.push(s);	
						});	
														
					}

					this.newSuggestionResults.emit(this.suggestions);
					return response ;
												
				})).subscribe(() => {}, 
				err => {
					if (err.status === 401 || err.status === 0) {
						this.currentUserAuth.doLogout(this.router);
						sessionStorage.setItem('beforeAuthPage', 'suggestions');
					}
		    	});
	}	
	
	/**
	* Applies a set of previously extracted filter values to the 
	* filters form
	*/
	public  processJobSpecExtratedFilters(extractedFilters:ExtractedFilters):void{
		this.resetSearchFilters();
		SearchBarFilterFormHelper.applyExtractedFiltersToForm(extractedFilters, this.skillFilters, this.suggestionFilterForm);				
	}
	
	/**
	* Scrolls to top of page
	*/
	public doScrollTop():void{
		(<HTMLInputElement>document.getElementById('page-top')).scrollIntoView({
			block: "start",
			inline: "nearest"
			});
		
	}
	
	/**
	* Listens for changes to the values in the Search bar filters, 
	* waits a period and then sends a request to the backend to 
	* update the search results 
	*/
	public addChangeListener(isUnfiltered:boolean):void{
		if (this.subscription) {
			this.subscription.unsubscribe();
		}
			
		this.subscription = this.suggestionFilterForm.valueChanges.pipe(debounceTime(0)).subscribe(() => {
			this.getSuggestions(false);	
		}); 

		this.getSuggestions(isUnfiltered);	
	}			
}