import { Component, ViewChild, ElementRef, Output, EventEmitter } 	from '@angular/core';
import { UntypedFormGroup, UntypedFormControl }						from '@angular/forms';
import { TranslateService } 										from '@ngx-translate/core';
import { Candidate}													from './../candidate';
import { CreditsService } 											from './../../credits.service';
import { Router}													from '@angular/router';
import { SupportedLanguage } 										from './../../supported-language';
import { City } 													from './../../city';
import { SupportedCountry } 										from './../../supported-candidate';
import { GeoZone } 													from './../../geo-zone';
import { CandidateServiceService }									from './../../candidate-service.service';
import { CandidateTotals } 											from './../../candidate-totals';
import { ExtractedFilters } 														from './../extracted-filters';

import { Subscription } 											from 'rxjs';
import { SuggestionParams}											from './.././suggestion-param-generator';
import { SuggestionsService }										from './../../suggestions.service';
import { debounceTime, map } 										from "rxjs/operators";
import { CurrentUserAuth }															from './../../current-user-auth';

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
	public includeUnavailableCandidatesSelected:string = 'true';
	public includeRequiresSponsorshipCandidatesSelected:string = 'true';
	public paidFeature:string								 	= '';
	public showIncludeFilters:string							= "";
	public skillFilters:Array<string>							= new Array<string>();
	public minMaxOptions:Array<string> 							= new Array<string>('','1','2','3','4','5','8','10','15','20','25','30');
	public showLanguageFilters:string							= "";
	public supportedLanguages:Array<SupportedLanguage> = new Array<SupportedLanguage>();
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
	* 
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
		* Initializes Component`
		*/
		ngOnInit(): void {
			
			this.subscription = this.suggestionFilterForm.valueChanges.pipe(debounceTime(500)).subscribe(() => {
				this.getSuggestions(false);
			});
			
		}
		
		ngAfterViewChecked(){
			if (sessionStorage.getItem("news-item-div")){
				this.doScrollTop();	
			}
		}
	
	/**
	* 
	*/
	public init():void{
		
		this.resetSearchFilters(true);	
				
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
	* Resets the filters
	*/
	public resetSearchFilters(attachValueChangeListener:boolean):void{
		
		this.resetSuggestionFilterForm();
		
		this.skilFilterForm = new UntypedFormGroup({
			skill: 					new UntypedFormControl(''),
		});
	
		this.skillFilters = new Array<string>();
		
		this.backendRequestCounter 		= 0;
		
		this.includeUnavailableCandidatesSelected = 'false';
		this.includeRequiresSponsorshipCandidatesSelected = 'false';
	
		
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
	* 
	*/
	public cssSearchNameDefault():string{
		
		if (this.getCurrentSearchFilterType() != 'NAME') {
			return '';
		}
		
		let firstName:string = this.suggestionFilterForm.get('searchPhraseFirstName')?.value;
		let surname:string = this.suggestionFilterForm.get('searchPhraseSurname')?.value;
		
		if (firstName == this.FIRST_NAME_DEFAULT || surname == this.SURNAME_DEFAULT){
			return 'search-name-default';
		}
		
		return '';
		
	}
	
	/**
	*
	*/	
	public activateSearhFields():void{
				
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
		this.resetSearhFields();
		this.filterTypeFormGroup.get('searchType')?.setValue(type);
		this.searchTypeFilterSelectionModal.nativeElement.close();
	}
			
	public resetSearhFields():void{
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
	* Whether user has a paid subscription 
	*/
	public hasPaidSubscription():boolean{
		return sessionStorage.getItem('hasPaidSubscription') === 'true';
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
		let hasPaidSubscription:boolean = (sessionStorage.getItem("hasPaidSubscription") === 'true');
		
		if (!this.currentUserAuth.isAdmin() && !this.currentUserAuth.isCandidate() && !hasPaidSubscription) {
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
		//this.initGeoZones();
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
	* 
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
											
	/**
	* Whether or not the Use is a Candidate
	*/
	public getLoggedInUserId():string{
		return ""+sessionStorage.getItem("userId");
	}
											
	private initSupportedCountries():void{
		this.supportedCountries = this.candidateService.getSupportedCountries();
	}
										
	/**
	* Whether or not the User is a Recruiter
	*/
	public isRecruiter():boolean{
		return sessionStorage.getItem('isRecruiter') === 'true';
	}
			
	/**
	* 
	*/								
	public resetSuggestionFilterForm():void{
		this.suggestionFilterForm = 				new UntypedFormGroup({
			searchPhrase:							new UntypedFormControl(''),
			searchPhraseFirstName:					new UntypedFormControl(this.FIRST_NAME_DEFAULT),
			searchPhraseSurname:					new UntypedFormControl(this.SURNAME_DEFAULT),
			contractType: 							new UntypedFormControl('Both'),
			minYearsExperience: 					new UntypedFormControl(''),
			maxYearsExperience: 					new UntypedFormControl(''),
			skill: 									new UntypedFormControl(''),
			includeUnavailableCandidates: 			new UntypedFormControl(''),
			includeRequiresSponsorshipCandidates:	new UntypedFormControl(''),
			locationCountry:						new UntypedFormControl(''),
			locationCity:							new UntypedFormControl(''),
			locationDistance:						new UntypedFormControl(''),
			
		});
		
		this.filterTypeFormGroup					= new UntypedFormGroup({
			searchType:												new UntypedFormControl('FUNCTION'),
		});
				
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
	
	}	
	
	/**
	* Sends request for Suggestions to the backend API
	*/
	public getSuggestions(isUnfiltered:boolean):void{
		
		const maxSuggestions:number 		= 112;
		
		let params:SuggestionParams = new SuggestionParams(this.filterTypeFormGroup, this.suggestionFilterForm, this.skillFilters, new Array<string>(), this.candidateService.getGeoZones(), this.candidateService.supportedCountries, this.supportedLanguages);
		
		let backendRequestId = this.backendRequestCounter + 1;
		this.backendRequestCounter = backendRequestId;
		
		this.suggestionsService.getSuggestons(	
									backendRequestId,
									maxSuggestions,
									params.getTitle(),
									params.getGeoZones(),
									params.getCountries(),
									params.getContract(),
									params.getPerm(),
									params.getMinExperience(),
									params.getMaxExperience(),
									params.getLanguages(),
									params.getSkills(),
									params.getIncludUnavailableCandidates(),
									params.getIncludRequiresSponsorshipCandidates(),
									isUnfiltered,
									params.getFirstName(),
									params.getSurname(),
									params.getEmail(),
									params.getCandidateId(),
									params.getLocCountry(),
									params.getLocCity(),
									params.getLocDistance(),
									).pipe(
										  map((response) => {
										  
											const responseRequestId = response.headers.get('X-Arenella-Request-Id');
											
											if (this.backendRequestCounter == responseRequestId) {
												this.suggestions =  new Array<Candidate>();
												response.body.content.forEach((s:Candidate) => {
													this.suggestions.push(s);	
												});	
											}

											//START
											this.newSuggestionResults.emit(this.suggestions);
											//END
										    return response ;
										
										  })).subscribe(() => {}, 
										  err => {
											if (err.status === 401 || err.status === 0) {
												sessionStorage.removeItem('isAdmin');
												sessionStorage.removeItem('isRecruter');
												sessionStorage.removeItem('isCandidate');
												sessionStorage.removeItem('loggedIn');
												sessionStorage.setItem('beforeAuthPage', 'suggestions');
												this.router.navigate(['login-user']);
											}
    									});
											
	}	
	
	public  processJobSpecExtratedFilters(extractedFilters:ExtractedFilters):void{
			
		this.resetSearchFilters(false);
				
		this.skillFilters 				= extractedFilters.skills.sort();
				
		if (extractedFilters.jobTitle != ''){
			this.suggestionFilterForm.get('searchPhrase')?.setValue(extractedFilters.jobTitle);	
		}
			
		if (extractedFilters.netherlands || extractedFilters.belgium || extractedFilters.uk || extractedFilters.ireland){
			this.suggestionFilterForm.get('nlResults')?.setValue(false);
			this.suggestionFilterForm.get('beResults')?.setValue(false);
			this.suggestionFilterForm.get('ukResults')?.setValue(false);
			this.suggestionFilterForm.get('ieResults')?.setValue(false);
				
			if (extractedFilters.netherlands)  {
				this.suggestionFilterForm.get('nlResults')?.setValue(extractedFilters.netherlands);
			}
					
			if (extractedFilters.belgium) {
				this.suggestionFilterForm.get('beResults')?.setValue(extractedFilters.belgium);
			}
					
			if (extractedFilters.uk) {
				this.suggestionFilterForm.get('ukResults')?.setValue(extractedFilters.uk);
			}
					
			if (extractedFilters.ireland) {
				this.suggestionFilterForm.get('ieResults')?.setValue(extractedFilters.ireland);
			}
					
		}	
				
		if (extractedFilters.perm != 'TRUE' && extractedFilters.freelance != 'TRUE') {
			this.suggestionFilterForm.get('contractType')?.setValue("BOTH");
		} else if (extractedFilters.perm == 'TRUE' && extractedFilters.freelance == 'TRUE') {
			this.suggestionFilterForm.get('contractType')?.setValue("BOTH");
		} else if (extractedFilters.perm != 'TRUE'){
			this.suggestionFilterForm.get('contractType')?.setValue("CONTRACT");
		} else if (extractedFilters.freelance != 'TRUE'){
			this.suggestionFilterForm.get('contractType')?.setValue("PERM");
		}
			
		if (extractedFilters.dutch) {
			this.suggestionFilterForm.get('dutchLanguage')?.setValue(extractedFilters.dutch);
		}
			
		if (extractedFilters.english) {
			this.suggestionFilterForm.get('englishLanguage')?.setValue(extractedFilters.english);
		}
			
		if (extractedFilters.french) {
			this.suggestionFilterForm.get('frenchLanguage')?.setValue(extractedFilters.french);
		}
				
		if (extractedFilters.experienceGTE != '') {
			if (this.minMaxOptions.indexOf(extractedFilters.experienceGTE) != -1){
				this.suggestionFilterForm.get('minYearsExperience')?.setValue(extractedFilters.experienceGTE);	
			}	
		}
				
		if (extractedFilters.experienceLTE != '') {
			if (this.minMaxOptions.indexOf(extractedFilters.experienceLTE) != -1){
				this.suggestionFilterForm.get('maxYearsExperience')?.setValue(extractedFilters.experienceLTE);	
			}
		}
				
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
	
	public addChageListener(isUnfiltered:boolean):void{
		if (this.subscription) {
			this.subscription.unsubscribe();
		}
			
		this.subscription = this.suggestionFilterForm.valueChanges.pipe(debounceTime(0)).subscribe(() => {
			this.getSuggestions(false);	
		}); 

		this.getSuggestions(isUnfiltered);	
	}			
}