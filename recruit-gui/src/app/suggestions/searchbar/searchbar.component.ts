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
import { SavedCandidateSearch } 									from '../saved-search';
	
@Component({
  selector: 'app-searchbar',
  standalone: false,
  templateUrl: './searchbar.component.html',
  styleUrl: './searchbar.component.css'
})
export class SearchbarComponent {

	@ViewChild('searchTypeFilterSelectionModal', {static:true}) searchTypeFilterSelectionModal!: ElementRef<HTMLDialogElement>;
	@ViewChild('paidSubscriptionModal', {static:true})			paidSubscriptionBox!: ElementRef<HTMLDialogElement>;
	@ViewChild('savedSearchQueriesModal', {static:true})		savedSearchQueriesBox!: ElementRef<HTMLDialogElement>;
	
	@Output() newSuggestionResults 								= new EventEmitter<Array<Candidate>>();
	
	private FIRST_NAME_DEFAULT:string 							= 'First Name';
	private SURNAME_DEFAULT:string 								= 'Surname';
	
	public showGeoZoneFilters:string							= "";
	public includeUnavailableCandidatesSelected:string 			= 'true';
	public filterView:string									= 'collapsed';
	public includeRequiresSponsorshipCandidatesSelected:string  = 'false';
	public paidFeature:string								 	= '';
	public showIncludeFilters:string							= "";
	public skillFilters:Array<string>							= new Array<string>();
	public minMaxOptions:Array<string> 							= new Array<string>('','1','2','3','4','5','8','10','15','20','25','30');
	public showLanguageFilters:string							= "";
	public supportedLanguages:Array<SupportedLanguage> 			= new Array<SupportedLanguage>();
	public showCountryFilters:string							= "";
	public showSecurityFilters:string							= "";
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
	
	public newSavedSearchForm:UntypedFormGroup 						= new UntypedFormGroup({
			title: 													new UntypedFormControl(''),
			enableEmails: 											new UntypedFormControl(false)
		});
	
	public getCurrentSearchFilterType():string{
		return this.filterTypeFormGroup.get('searchType')?.value;
	}
	
	public filterTypeFormGroup:UntypedFormGroup					= new UntypedFormGroup({
		searchType:												new UntypedFormControl('FUNCTION'),
	});
	
	public securityFilters:Array<string> = new Array<string>();
	
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
		})
		this.fetchSavedSearches();
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
		
		if (this.subscription) {
			this.subscription.unsubscribe();
		}
				
	}
	
	public toggleSecurityFilterOptions(sc:string):void {
		let included:boolean = this.suggestionFilterForm.get((sc))?.value;
		this.suggestionFilterForm.get((sc))?.setValue(!included);
	}
	
	public includeResultsForSC(sc:string):string{
		return this.suggestionFilterForm.get((sc))?.value;
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
			
			if (checked) {
				this.suggestionFilterForm.get("includeRequiresSponsorshipCandidates")?.setValue('');	
				this.includeRequiresSponsorshipCandidatesSelected = 'false';
			} else {
				this.suggestionFilterForm.get("includeRequiresSponsorshipCandidates")?.setValue('true');
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
	
	public showSavedSearchQueriesBox():void{
		this.savedSearchQueriesBox.nativeElement.showModal();
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
	* Swithches between open and closed filter view for Countries 
	*/
	public switchCountriesFilterView(view:string):void{
		this.showCountryFilters = view;
	}
	
	/**
	* Swithches between open and closed filter view for Security options 
	*/
	public showSecurityFiltersView(view:string):void{
		this.showSecurityFilters = view;
	}

	/**
	* Swithches between open and closed filter view for Location 
	*/
	public switchLocationFilterView(view:string):void{
		this.showLocationFilters = view;
	}
	
	/**
 	* Swithches between open and closed filter view for Security Options 
	*/
	public switchSecurityFilterView(view:string):void{
		this.showSecurityFilters = view;
	}
	
	/**
	* Swithches between open and closed filter view for GeoZones 
	*/
	public switchGeoZoneFilterView(view:string):void{
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
		
		let included:boolean = this.suggestionFilterForm.get((country))?.value;
		
		this.suggestionFilterForm.get((country))?.setValue(!included);
		
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
		return this.suggestionFilterForm.get((country))?.value;
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
				this.suggestionFilterForm.get(country.name)?.setValue(true);
			});
		} else {
			this.candidateService.getSupportedCountries().forEach(country => {
				this.suggestionFilterForm.get(country.name)?.setValue(false);
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
			this.suggestionFilterForm.addControl(c.name, new UntypedFormControl(false));
		});
						
		this.supportedLanguages = new Array<SupportedLanguage>();
				
		this.candidateService.getLanguages().forEach(lang => {
			this.suggestionFilterForm.addControl(lang.languageCode.toLowerCase()+'Language', new UntypedFormControl(false));
			this.supportedLanguages.push(lang);
		});
		
		
		this.initSecurityLevels();	
		this.initGeoZones();
		this.showCountryFilters 	= '';
		this.showSecurityFilters	= '';
		this.showLanguageFilters 	= '';
		this.showIncludeFilters 	= '';
			
		this.skilFilterForm = new UntypedFormGroup({
			skill: new UntypedFormControl(''),
		});
		
		this.newSavedSearchForm = new UntypedFormGroup({
			title: 				new UntypedFormControl(''),
			enableEmails: 		new UntypedFormControl(false)
		});
		
		this.skillFilters = new Array<string>();
			
		this.backendRequestCounter 		= 0;
			
		this.includeUnavailableCandidatesSelected = 'false';
		this.includeRequiresSponsorshipCandidatesSelected = 'false';
		this.acitveSavedCandidateSearch = new SavedCandidateSearch();
				
	}
	
	private initSecurityLevels():void{
		
		this.securityFilters = this.candidateService.getSecurityLevels();
	
		this.securityFilters.forEach(sc => {
				this.suggestionFilterForm.addControl(sc, new UntypedFormControl(false));
		});
			
		this.showSecurityFilters 	= "";
			
	}
	
	private generateSuggestionsSearchRequest(isUnfiltered:boolean):SuggestionsSearchRequest{
		
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
			
			this.securityFilters.filter(sc => this.suggestionFilterForm.get(sc)?.value == true).forEach(sc => {
				ssReq.securityFilters.securityLevels?.push(sc);
			});
				
			return ssReq;
	}
	
	/**
	* Sends request for Suggestions to the backend API
	*/
	public getSuggestions(isUnfiltered:boolean):void{
	
		let ssReq:SuggestionsSearchRequest = this.generateSuggestionsSearchRequest(isUnfiltered);
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
	
	
	/** Start Saved Searches */
	public savedSearches:Array<SavedCandidateSearch> = new Array<SavedCandidateSearch>();
	public acitveSavedCandidateSearch:SavedCandidateSearch = new SavedCandidateSearch();
	
	public doSaveSearch():void{
		
		let title:string 			= this.newSavedSearchForm.get('title')?.value;
		let enableEmails:boolean 	= this.newSavedSearchForm.get('enableEmails')?.value;
		
		this.candidateService.saveSearchQuery(title, enableEmails, this.generateSuggestionsSearchRequest(false)).subscribe(res =>{
			
			this.newSavedSearchForm = new UntypedFormGroup({
				title: 				new UntypedFormControl(''),
				enableEmails: 		new UntypedFormControl(false)
			});
			
			this.savedSearchQueriesBox.nativeElement.close();
			
			this.fetchSavedSearches();
			
		});
	}
	
	public selectSavedSearch(savedSearch:SavedCandidateSearch):void{
		this.acitveSavedCandidateSearch = savedSearch;
	}
	
	public fetchSavedSearches():void{
		this.savedSearches 				= new Array<SavedCandidateSearch>();
		this.acitveSavedCandidateSearch = new SavedCandidateSearch();
		this.candidateService.getSavedSearchQueries().subscribe(res => {
			res.forEach(a =>  {
				this.savedSearches.push(a);
			})
		});
	}
	
	public enableSavedSearchEmails():void{
		this.acitveSavedCandidateSearch.emailAlert = !this.acitveSavedCandidateSearch.emailAlert;
	}
	
	public updateSavedSearch():void{
		
		//TODO: Also need to update email option
		
		this.acitveSavedCandidateSearch.searchRequest = this.generateSuggestionsSearchRequest(false);
		
		this.candidateService.updateSearchQuery(this.acitveSavedCandidateSearch.id, this.acitveSavedCandidateSearch).subscribe(res =>{
					
					this.newSavedSearchForm = new UntypedFormGroup({
						title: 				new UntypedFormControl(''),
						enableEmails: 		new UntypedFormControl(false)
					});
					
					this.savedSearchQueriesBox.nativeElement.close();
					
					this.fetchSavedSearches();
					
				});
	}
	
	public deleteSavedSearch(savedCandidateSearch:SavedCandidateSearch):void{
		this.candidateService.deleteSearchQuery(savedCandidateSearch.id).subscribe(res => {
			this.fetchSavedSearches();
		});
			
	}
	
	public runSavedSearch(savedCandidateSearch:SavedCandidateSearch):void{
		this.extractSavedCandidateSearchToFilters(savedCandidateSearch);
		this.acitveSavedCandidateSearch = savedCandidateSearch;
	}
	
	private extractSavedCandidateSearchToFilters(savedCandidateSearch:SavedCandidateSearch):void{
		this.resetSearchFilters();
		
		let searchRequest:SuggestionsSearchRequest = savedCandidateSearch.searchRequest;
		
		if (searchRequest.termFilters.title) {
			this.filterTypeFormGroup.get('searchType')?.setValue("FUNCTION");
			this.suggestionFilterForm.get('searchPhrase')?.setValue(searchRequest.termFilters.title);
		}
		
		if (searchRequest.termFilters.candidateId) {
			this.filterTypeFormGroup.get('searchType')?.setValue("CANDIDATE_ID");
			this.suggestionFilterForm.get('searchPhrase')?.setValue(searchRequest.termFilters.candidateId);
		}
				
		if (searchRequest.termFilters.firstName || searchRequest.termFilters.surname) {
			this.filterTypeFormGroup.get('searchType')?.setValue("NAME");
			this.suggestionFilterForm.get('searchPhraseFirstName')?.setValue(searchRequest.termFilters.firstName);
			this.suggestionFilterForm.get('searchPhraseSurname')?.setValue(searchRequest.termFilters.surname);			
		}
		
		if (searchRequest.termFilters.email) {
			this.filterTypeFormGroup.get('searchType')?.setValue("EMAIL");
			this.suggestionFilterForm.get('searchPhrase')?.setValue(searchRequest.termFilters.email);				
		}
		
		searchRequest.skillFilters?.skills?.forEach(skill => {
			this.skillFilters.push(skill);
		});
		
		searchRequest.locationFilters.geoZones.forEach(geoZone => {
			this.toggleGeoZoneSelection(new GeoZone(geoZone.toUpperCase()));
		});
		
		searchRequest.locationFilters.countries?.forEach(country => {
			this.toggleCountrySelection(country);
		});
		
		searchRequest.securityFilters.securityLevels?.forEach(sc => {
			this.toggleSecurityFilterOptions(sc);
		});
		
		this.supportedLanguages = new Array<SupportedLanguage>();
		this.candidateService.getLanguages().forEach(lang => {
			
			let languages = searchRequest?.languageFilters?.languages;
			
			if (languages && languages.indexOf(lang.languageCode) > -1) {
				this.suggestionFilterForm.addControl(lang.languageCode.toLowerCase()+'Language', new UntypedFormControl(true));	
				this.suggestionFilterForm?.get(lang.languageCode.toLowerCase()+'Language')?.setValue("true");
			} else {
				this.suggestionFilterForm.addControl(lang.languageCode.toLowerCase()+'Language', new UntypedFormControl(false));
			}
			this.supportedLanguages.push(lang);
		});
		
		if (searchRequest.contractFilters.contract == "TRUE") {
			this.suggestionFilterForm.get("contractType")?.setValue("CONTRACT");
		} else if (searchRequest.contractFilters.perm == "TRUE") {
			this.suggestionFilterForm.get("contractType")?.setValue("PERM");
		} else {
			this.suggestionFilterForm.get("contractType")?.setValue("BOTH");
		}
		
		if (searchRequest.locationFilters.range.country) {
			this.suggestionFilterForm.get('locationCountry')?.setValue(searchRequest.locationFilters.range.country);
			this.suggestionFilterForm.get('locationCity')?.setValue(searchRequest.locationFilters.range.city);
			this.suggestionFilterForm.get('locationDistance')?.setValue(searchRequest.locationFilters.range.distanceInKm);
		}
		
		if (searchRequest.includeFilters.includeUnavailableCandidates == "true") {
			this.suggestionFilterForm.get('includeUnavailableCandidates')?.setValue("true");
			this.includeUnavailableCandidatesSelected = "true";
		}
		if (searchRequest.includeFilters.includeRequiresSponsorshipCandidates == "true") {
			this.suggestionFilterForm.get('includeRequiresSponsorshipCandidates')?.setValue("true");
			this.includeRequiresSponsorshipCandidatesSelected = "true";
		}
		
		
		if (searchRequest.experienceFilters.experienceMin) {
			this.suggestionFilterForm.get('minYearsExperience')?.setValue(searchRequest.experienceFilters.experienceMin);
			
		}
		
		if (searchRequest.experienceFilters.experienceMax) {
			this.suggestionFilterForm.get('maxYearsExperience')?.setValue(searchRequest.experienceFilters.experienceMax);
		}
		
		this.addChangeListener(false);
		
		this.savedSearchQueriesBox.nativeElement.close();
		
	}
	
	/** End Saved Searches */
		
	
}