import { Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild } 	from '@angular/core';
import { UntypedFormGroup, UntypedFormControl }										from '@angular/forms';
import { CandidateServiceService }													from '../candidate-service.service';
import { SuggestionsService }														from '../suggestions.service';
import { CurriculumService }														from '../curriculum.service';
import { Candidate}																	from './candidate';
import { SavedCandidate}															from './saved-candidate';
import { SuggestionParams}															from './suggestion-param-generator';
import { NgbModal }																	from '@ng-bootstrap/ng-bootstrap';
import { CandidateSearchAlert }														from './candidate-search-alert';
import { DomSanitizer, SafeResourceUrl } 											from '@angular/platform-browser';
import { Router}																	from '@angular/router';
import { debounceTime, map } 														from "rxjs/operators";
import { CandidateProfile } 														from '../candidate-profile';
import { CandidateNavService } 														from '../candidate-nav.service';
import { CreditsService } 															from '../credits.service';
import { ExtractedFilters } 														from './extracted-filters';
import { InfoItemBlock, InfoItemConfig, InfoItemRowKeyValue, InfoItemRowKeyValueFlag, InfoItemRowKeyValueMaterialIcon, InfoItemRowSingleValue } from '../candidate-info-box/info-item';
import {AppComponent} 																from '../app.component';
import { TranslateService } 														from '@ngx-translate/core';
import { HttpResponse } 															from '@angular/common/http';
import { CandidateTotals } 															from '../candidate-totals';
import { GeoZone } 																	from '../geo-zone';
import { SupportedCountry } 														from '../supported-candidate';
import { Subscription } from 'rxjs';
import { SupportedLanguage } from '../supported-language';

/**
* Component to suggest suitable Candidates based upon a 
* Recruiters search
*/
@Component({
  selector: 'app-suggestions',
  standalone: false,
  templateUrl: './suggestions.component.html',
  styleUrls: ['./suggestions.component.css','./suggestions.component.two.css']
})
export class SuggestionsComponent implements OnInit {

	@Input()  externalProvileViewCandidateId:string = "";
	@Input()  parentComponent:string = "";
	@Output() switchViewEvent 						= new EventEmitter<string>();
	
 	@ViewChild('specUploadBox', { static: true }) 		specUploadDialogBox!: ElementRef<HTMLDialogElement>;
 	@ViewChild('feedbackBox', { static: true }) 		feedbackDialogBox!: ElementRef<HTMLDialogElement>;
 	@ViewChild('publicityModal', {static:true})			publicityDialogBox!: ElementRef<HTMLDialogElement>;
 	@ViewChild('confirmDeleteModal', {static:true})		confirmDeleteDialogBox!: ElementRef<HTMLDialogElement>;
 	@ViewChild('contactBox', {static:true})				contactDialogBox!: ElementRef<HTMLDialogElement>;
 	@ViewChild('paidSubscriptionModal', {static:true})	paidSubscriptionBox!: ElementRef<HTMLDialogElement>;
 	
 	public back():void{
		this.switchViewEvent.emit();	 
	}
	
	public candidateTotals:CandidateTotals					 	= new CandidateTotals(0,0,0);
	public backendRequestCounter:number							= 0;
	public candidateProfile:CandidateProfile 					= new CandidateProfile();
	public savedCandidates:Array<SavedCandidate> 				= new Array<SavedCandidate>();
	private jobSpecFile!:File;
	public jobSpecUploadView:string 							= "chooseType"; //chooseType | doc | text
	public infoItemConfig:InfoItemConfig 						= new InfoItemConfig();
	public suggestions:Array<Candidate>  						= new Array<Candidate>();
	public currentView:string 									= 'suggestion-results';
	public skillFilters:Array<string>							= new Array<string>();
	public minMaxOptions:Array<string> 							= new Array<string>('','1','2','3','4','5','8','10','15','20','25','30');
	public suggestedCandidate:Candidate							= new Candidate();
	public showSaveAlertBoxFailure:boolean  					= false;
	public showSaveAlertBoxSuccess:boolean  					= false;
	public showSaveAlertBox:boolean 							= false;
	public showFilterByJonSpecFailure:boolean  					= false;
	public showFilterByJobSpec:boolean 							= false;
	public dangerousUrl 										= 'http://127.0.0.1:8080/curriculum-test/1623.pdf';
	public trustedResourceUrl : SafeResourceUrl;
	public suggestionMobileClass:string							= '';
	public suggestionMobileBtnClass:string						= '';
	public passedCreditCheck:boolean 							= false;
	public lastView:string 										= '';
	public contactCandidateView:string 							= 'message';
	public showPaidSubscriptinOptions:boolean					= false;
	public paidFeature:string								 	= '';
	public showGeoZoneFilters:string							= "";
	public showCountryFilters:string							= "";
	public showLanguageFilters:string							= "";
	public showIncludeFilters:string							= "";
	public geoZones:Array<GeoZone>								= new Array<GeoZone>();
	public supportedCountries:Array<SupportedCountry>			= new Array<SupportedCountry>();
	public publicitySuggestions:Array<Candidate>  				= new Array<Candidate>();
	public createAlertForm:UntypedFormGroup 					= new UntypedFormGroup({
		alertName:			new UntypedFormControl(''),
	});
	
	public suggestionFilterForm:UntypedFormGroup 				= new UntypedFormGroup({});
	public skilFilterForm:UntypedFormGroup 						= new UntypedFormGroup({
		skill: 					new UntypedFormControl(''),
	});
	public filterByJobSpecForm:UntypedFormGroup 				= new UntypedFormGroup({
		specAsText:				new UntypedFormControl('Enter Job specification Text here...'),
	});
	
	/**
	* Switches between options on how to upload job spec 
	* to use to perform filtering
	*/
	public switchJobSpecUpldOpt(type:string):void{
		this.jobSpecUploadView = type;
	}
	
	/**
	* Constructor
	* @param candidateService - Services relating to Candidates
	*/
	constructor(public candidateService:		CandidateServiceService, 
				public suggestionsService:		SuggestionsService, 
				private modalService: 			NgbModal, 
				private sanitizer: 				DomSanitizer,
				private curriculumService: 		CurriculumService,
				private router:					Router,
				private candidateNavService: 	CandidateNavService,
				private creditsService:			CreditsService,
				private appComponent:			AppComponent,
				private translate:				TranslateService) { 
					
		this.trustedResourceUrl = this.sanitizer.bypassSecurityTrustResourceUrl('');
		
		this.init();
	}
	
	private subscription?:Subscription;
	
	private init():void{
		
		this.resetSearchFilters(true);
		
		if (!this.isCandidate()) {
			this.getSuggestions(true);	
		}
		
		//Candidate
		if (this.isCandidate()) {
			this.candidateNavService.startCandidateProfileRouteForCandidate();
			this.candidateService.getCandidateById(this.getLoggedInUserId()).subscribe(candidate => {
				this.showSuggestedCandidateOverview(candidate.content[0]);	
			});
		}
		
		//Recruiter
		if (this.isRecruiter() && this.candidateNavService.isRouteActive()) {
			this.candidateService.getCandidateByIdWithRecruiterAsOwner(this.candidateNavService.getCandidateId(), this.getLoggedInUserId()).subscribe(candidate => {
				this.showSuggestedCandidateOverview(candidate.content[0]);	
			});
		}

		//Admin
		if (this.isAdmin() && this.candidateNavService.isRouteActive()) {
			this.candidateService.getCandidateById(this.candidateNavService.getCandidateId()).subscribe(candidate => {
				this.showSuggestedCandidateOverview(candidate.content[0]);	
			});
		}		
		
		if (this.isAdmin() || sessionStorage.getItem("hasPaidSubscription") === 'true') {
			this.showPaidSubscriptinOptions = true;
		} 
		
		this.initGeoZones();
		this.initSupportedCountries();
		
		this.candidateService.fetchCandidateTotals().subscribe(totals => this.candidateTotals = totals);
		
		this.appComponent.refreschUnreadAlerts();
		
		if(this.subscription) {
			this.subscription.unsubscribe();
		}
		
	}
	
	/**
	* Swithches between open and closed filter view for GeoZones 
	*/
	public switchGeoZoneFilterView(view:string):void{
		this.initGeoZones();
		this.showGeoZoneFilters = view;
	}
	
	/**
	* Swithches between open and closed filter view for GeoZones 
	*/
	public switchCountriesFilterView(view:string):void{
		//this.initGeoZones();
		this.showCountryFilters = view;
	}
	
	/**
	* Swithches between open and closed filter view for Languages 
	*/
	public switchLanguageFilterView(view:string):void{
		this.showLanguageFilters = view;
	}
	
	/**
	* Swithches between open and closed filter view for Languages 
	*/
	public switchIncludeFilterView(view:string):void{
		this.showIncludeFilters = view;
	}
	
  	public setJobSepecFile(event:any):void{
  
  		if (event.target.files.length <= 0) {
  			return;
  		}
  	
  		this.jobSpecFile = event.target.files[0];
  		
  	}
	
	private processJobSpecExtratedFilters(extractedFilters:ExtractedFilters):void{
		
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
			
			this.closeModal();
			
	}
	
	public extractFiltersFromJobSpecText():void{
		
		let jobSpecText = this.filterByJobSpecForm.get('specAsText')?.value; 
		
		this.candidateService.extractFiltersFromText(jobSpecText).subscribe(extractedFilters=>{
			this.processJobSpecExtratedFilters(extractedFilters);
			this.specUploadDialogBox.nativeElement.close();
			this.addChageListener(false);
		},(failure =>{
			this.showFilterByJonSpecFailure 	= true;
			this.showFilterByJobSpec 			= false;
		}));
	}
	
	/**
 	* Extracts filters from job specification file
	*/	
  	public extractFiltersFromJobSpec():void{
  		this.candidateService.extractFiltersFromDocument(this.jobSpecFile).subscribe(extractedFilters=>{
  			this.processJobSpecExtratedFilters(extractedFilters);
  			this.specUploadDialogBox.nativeElement.close();
			this.addChageListener(false);
		},(failure =>{
			this.showFilterByJonSpecFailure 	= true;
			this.showFilterByJobSpec 			= false;
		}));
		
  		
  	}
	
	/**
	* Resets the filters
	*/
	public doReset():void{
		this.resetSearchFilters(true);
		this.addChageListener(true);
	}
	
	private addChageListener(isUnfiltered:boolean):void{
		if(this.subscription) {
			this.subscription.unsubscribe();
		}
		
		this.subscription = this.suggestionFilterForm.valueChanges.pipe(debounceTime(0)).subscribe(res => {
		 		this.getSuggestions(false);	
		}); 

		this.getSuggestions(isUnfiltered);	
	}	
	
	public resetSuggestionFilterForm():void{
		this.suggestionFilterForm = 				new UntypedFormGroup({
			searchPhrase:							new UntypedFormControl(''),
			contractType: 							new UntypedFormControl('Both'),
			minYearsExperience: 					new UntypedFormControl(''),
			maxYearsExperience: 					new UntypedFormControl(''),
			skill: 									new UntypedFormControl(''),
			includeUnavailableCandidates: 			new UntypedFormControl(''),
			includeRequiresSponsorshipCandidates:	new UntypedFormControl(''),
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
	
	public supportedLanguages:Array<SupportedLanguage> = new Array<SupportedLanguage>();
	
	/**
	* Resets the filters
	*/
	private resetSearchFilters(attachValueChangeListener:boolean):void{
		
		this.resetSuggestionFilterForm();
		
		this.skilFilterForm = new UntypedFormGroup({
			skill: 					new UntypedFormControl(''),
		});
	
		this.skillFilters = new Array<string>();
		
		this.backendRequestCounter 		= 0;
		
		this.includeUnavailableCandidatesSelected = 'false';
		this.includeRequiresSponsorshipCandidatesSelected = 'false';
	
		
	}
		
	public showCVInline(candidateId:string):void{
		
		let url = this.curriculumService.getCurriculumUrlForInlinePdf(candidateId); 'http://127.0.0.1:8080/curriculum-test/';
		
		
		this.trustedResourceUrl = this.sanitizer.bypassSecurityTrustResourceUrl(url);
		
	}
	
	/**
	* Initializes Component`
	*/
	ngOnInit(): void {
		
		this.subscription = this.suggestionFilterForm.valueChanges.pipe(debounceTime(500)).subscribe(res => {
			this.getSuggestions(false);
		});
		
		this.candidateService.fetchSavedCandidates().subscribe(response => {
			this.savedCandidates = response;
		})
		
		if (this.externalProvileViewCandidateId.length > 0) {
			this.candidateService.getCandidateById(this.externalProvileViewCandidateId).subscribe(result => {
				this.showSuggestedCandidateOverview(result.content[0]);
			});
		}
		
	}
	
	ngAfterViewChecked(){
		if (sessionStorage.getItem("news-item-div")){
			this.doScrollTop();	
		}
	}
	
	private initGeoZones():void{
		
		this.geoZones = this.candidateService.getGeoZones();
		
		this.geoZones.forEach(gz => {
			this.suggestionFilterForm.addControl(gz.geoZoneId.toLowerCase()+'Results', new UntypedFormControl(false));
		});
		
		this.showGeoZoneFilters = "";
		
	}
	
	private initSupportedCountries():void{
		
		this.supportedCountries = this.candidateService.getSupportedCountries();
		
		
	}
	
	/**
	* If we are only viewing the profile or have access to edit and 
	* update features 
 	*/
	public isViewOnly():boolean{
		return this.parentComponent == 'newsfeed';
	}
	
	/**
	* Whether the candidate is a Saved Candidate
	*/
	public isSavedCandidate(candidate:Candidate):boolean{
		
		let chk:boolean = false;
		
		this.savedCandidates.forEach(sc => {
			if(""+sc.candidateId === ""+candidate.candidateId) {
				chk = true;
			}
		});
		
		return chk;
	}

	/**
	* Sends request for Suggestions to the backend API
	*/
	private getSuggestions(isUnfiltered:boolean):void{
		
		const maxSuggestions:number 		= 112;
		
		let params:SuggestionParams = new SuggestionParams(this.suggestionFilterForm, this.skillFilters, new Array<string>(), this.candidateService.getGeoZones(), this.candidateService.supportedCountries, this.supportedLanguages);
		
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
									isUnfiltered
									).pipe(
										  map((response) => {
										  
											const responseRequestId = response.headers.get('X-Arenella-Request-Id');
											
											if (this.backendRequestCounter == responseRequestId) {
												this.suggestions =  new Array<Candidate>();
												response.body.content.forEach((s:Candidate) => {
													this.suggestions.push(s);	
												});	
											}
										
										    return response ;
										
										  })).subscribe((data: HttpResponse<any>) => {}, 
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
	* Returns whether Candidates from the selected GeoZone are currently
	* included in the Suggestion results
	*/
	public includeResultsForGeoZone(geoZone:GeoZone):boolean{
		return this.suggestionFilterForm.get((geoZone.geoZoneId.toLowerCase()+'Results'))?.value;
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
	* Shows the Saved Candidates view
	*/
	public showSavedCandidates():void{
		this.currentView 	= 'saved-candidates';
		this.lastView 		= 'saved-candidates';
		this.candidateService.fetchSavedCandidates().subscribe(response => {
			this.savedCandidates = response;
		});
	}
	
	/**
	* Shows the Suggesion result view
	*/
	public showSuggestionsResults():void{
		
		 this.candidateService.fetchSavedCandidates().subscribe(response => {
			this.savedCandidates = response;
		});
		
		if(this.parentComponent == 'newsfeed') {
			this.back();		
		} else {
			this.getSuggestions(false);	
		
			if (this.isRecruiter() && this.candidateNavService.isRouteActive()) {
				this.candidateNavService.doNextMove("back",this.candidateNavService.getCandidateId());	
			} else {
				this.currentView 		= 'suggestion-results';
				this.suggestedCandidate = new Candidate();	
				this.lastView 			= '';
			}	
		}
		
	
	}
	
	public doCreditCheck():void{
		
		if(this.isAdmin()){
			this.passedCreditCheck = true;
		} else {
			this.curriculumService.getCreditCount().subscribe(count => {
				this.passedCreditCheck = (count > 0 || count == -1);
			});	
		}
	}
	
	public setLeftInfoPane(candidateId:string, candidate:Candidate):void{
		
		this.candidateService.getCandidateProfileById(candidateId).subscribe( candidate => {
			this.candidateProfile 	= candidate;
			this.infoItemConfig 	= new InfoItemConfig();
			this.infoItemConfig.setProfilePhoto(this.candidateProfile?.photo?.imageBytes);
		
			if (!this.isCandidate()){
				this.infoItemConfig.setShowContactButton(true);
			}
		
			//Location
			let recruiterBlock:InfoItemBlock = new InfoItemBlock();
			recruiterBlock.setTitle(this.translate.instant('info-item-title-location'));
			recruiterBlock.addRow(new InfoItemRowKeyValueFlag(this.translate.instant('info-item-title-country'),this.getFlagClassFromCountry(candidate.country)));
			recruiterBlock.addRow(new InfoItemRowKeyValue(this.translate.instant('info-item-title-city'),candidate.city));
			this.infoItemConfig.addItem(recruiterBlock);
			
			//Languages Block
			let languageBlock:InfoItemBlock = new InfoItemBlock();
			languageBlock.setTitle(this.translate.instant('info-item-title-languages'));
			candidate.languages.forEach(lang => {
					languageBlock.addRow(new InfoItemRowKeyValueMaterialIcon(this.getLanguage(lang.language),this.getMaterialIconClassFromLangLevel(lang.level)));
			});
			
			languageBlock.sort();
			
			this.infoItemConfig.addItem(languageBlock);
			
			//Contract Type Block
			if (candidate.freelance == 'TRUE' || candidate.perm == 'TRUE') {
				let contractTypeBlock:InfoItemBlock = new InfoItemBlock();
				contractTypeBlock.setTitle(this.translate.instant('info-item-title-contract-type'));
				if (candidate.freelance == 'TRUE'){		
					contractTypeBlock.addRow(new InfoItemRowKeyValueMaterialIcon(this.translate.instant('info-item-title-contract'),"available-check-icon"));
				}
				if (candidate.perm == 'TRUE'){		
					contractTypeBlock.addRow(new InfoItemRowKeyValueMaterialIcon(this.translate.instant('info-item-title-permanent'),"available-check-icon"));
				}
				this.infoItemConfig.addItem(contractTypeBlock);
			}
			
			//Contract Rate 
			if(this.hasContractRate()) {
				let contractRateBlock:InfoItemBlock = new InfoItemBlock();
				contractRateBlock.setTitle(this.translate.instant('info-item-contract-rate'));
				contractRateBlock.addRow(new InfoItemRowSingleValue(this.getContractRate()));
				this.infoItemConfig.addItem(contractRateBlock);
			}
		
			//Perm Rate 
			if(this.hasPermRate()) {
				let permRateBlock:InfoItemBlock = new InfoItemBlock();
				permRateBlock.setTitle(this.translate.instant('info-item-title-perm-rate'));
				permRateBlock.addRow(new InfoItemRowSingleValue(this.getPermRate()));
				this.infoItemConfig.addItem(permRateBlock);
			}
		
			//Years Experience 
			let yearsExperienceBlock:InfoItemBlock = new InfoItemBlock();
			yearsExperienceBlock.setTitle(this.translate.instant('info-item-title-years-experience'));
			yearsExperienceBlock.addRow(new InfoItemRowSingleValue(""+candidate.yearsExperience));
			this.infoItemConfig.addItem(yearsExperienceBlock);
			
			//Secuirty Level
			let securityClearanceBlock:InfoItemBlock = new InfoItemBlock();
			securityClearanceBlock.setTitle(this.translate.instant('info-item-title-security-clearance'));
			securityClearanceBlock.addRow(new InfoItemRowKeyValue(this.translate.instant('info-item-security-clearance'),candidate.securityClearance));
			this.infoItemConfig.addItem(securityClearanceBlock);
			
			
			//Requires Sponsorship
			let requiresSponsorhipBlock:InfoItemBlock = new InfoItemBlock();
			requiresSponsorhipBlock.setTitle(this.translate.instant('info-item-title-requires-sponsorship'));
			requiresSponsorhipBlock.addRow(new InfoItemRowKeyValue(this.translate.instant('info-item-requires-sponsorship'),candidate.requiresSponsorship ? this.translate.instant('yes') : this.translate.instant('no')));
			this.infoItemConfig.addItem(requiresSponsorhipBlock);
			
			
			//Availability
			let availabilityBlock:InfoItemBlock = new InfoItemBlock();
			availabilityBlock.setTitle(this.translate.instant('info-item-title-availability'));
			if (this.candidateProfile.daysOnSite) {
				availabilityBlock.addRow(new InfoItemRowKeyValue(this.translate.instant('info-item-title-max-days-on-site'),this.formatHumanReadableDaysOnsite(this.candidateProfile.daysOnSite)));
			}
			if (this.candidateProfile.availableFromDate) {
				availabilityBlock.addRow(new InfoItemRowKeyValue(this.translate.instant('info-item-title-available-from'),""+this.candidateProfile.availableFromDate));
			}
			this.infoItemConfig.addItem(availabilityBlock);
			
		});
		
	}
	
	/**
	* Shows the Suggesion result view
	*/
	public showSuggestedCandidateOverview(candidateSuggestion:Candidate):void{
		
		if(!candidateSuggestion.available && !this.doPaidSubscriptionCheck()){
			this.paidFeature = "paidFeatureUnavailableCandidates";
			return;
		}
		
		
		this.doCreditCheck();
		
		//Admin
		if (this.isAdmin()) {
			this.candidateNavService.startCandidateProfileRouteForAdmin();
		}
		
		this.setLeftInfoPane(candidateSuggestion.candidateId, this.suggestedCandidate);
		
		this.currentView 			= 'suggested-canidate-overview';
		this.suggestedCandidate 	= candidateSuggestion;
		
		this.doScrollTop();
		
	}
	
	/**
	* Returns the flag css class for the Flag matching
	* the Country 
	*/
	public getFlagClassFromCountry(country:string):string{
		
		let sc:SupportedCountry = this.supportedCountries.filter(c => c.name == country)[0];
		
		return sc ?  "flag-icon-"+sc.iso2Code : '';
		
	}
	
	/**
	* Returns the materialIcon css class for the Flag matching
	* the Country 
	*/
	private getMaterialIconClassFromLangLevel(langLevel:string):string{
		
		switch(langLevel){
			case "PROFICIENT":{return "lang-proficient-check-icon"}
			case "BASIC":{return "lang-basic-check-icon"}
			default: return "";
		}
			
	}
	
	safeUrl:any;
	public filename:string = '';
	
	/**
  	* Whether or not the user has authenticated as an Admin user 
  	*/
  	public isAuthenticatedAsAdmin():boolean {
    	return sessionStorage.getItem('isAdmin') === 'true';
  	}
	
	/**
	* Returns the Humand readable version of the Language
	* @param country - Language to get the readable version for
	*/
	public getLanguage(lang:string):string{
		return this.translate.instant(lang);
  	}	

	public hasSkill(skill:string):boolean {
		
		let formattedSkill:string = skill.trim().toLowerCase();
		
		let match:boolean = false;
		this.suggestedCandidate.skills.forEach(skill => {
			if (skill === formattedSkill) {
				 match = true;
			}
		})
		
		if (match) {
			return true;
		}
		
		return false;
	}
		
	/**
 	* Saves the alert
	*/
	public saveAlert():void{
		
		this.showSaveAlertBox 			= true;
		this.showSaveAlertBoxSuccess 	= false;
		this.showSaveAlertBoxFailure 	= false;
		
		let params:SuggestionParams 	= new SuggestionParams(this.suggestionFilterForm, this.skillFilters, new Array<string>(), this.candidateService.getGeoZones(), this.supportedCountries, this.supportedLanguages);
		let alert:CandidateSearchAlert 	= new CandidateSearchAlert();
		
		alert.alertName 			= this.createAlertForm.get(('alertName'))?.value;
		alert.countries 			= params.getCountries();
		alert.dutch 				= params.getLanguageLevel("DUTCH");
		alert.english 				= params.getLanguageLevel("ENGLISH");
		alert.freelance 			= params.getContract();
		alert.french 				= params.getLanguageLevel("FRENCH");
		alert.perm 					= params.getPerm();
		alert.skills 				= params.getSkills();
		alert.searchText			= params.getTitle();
		
		alert.yearsExperienceLtEq 	= params.getMinExperience();
		alert.yearsExperienceGtEq 	= params.getMaxExperience();
		
		this.candidateService.createCandidateSearchAlert(alert).subscribe(data => {
			
			this.showSaveAlertBox 			= false;
			this.showSaveAlertBoxSuccess 	= true;
			this.showSaveAlertBoxFailure 	= false;
			
			this.createAlertForm = new UntypedFormGroup({
				alertName:			new UntypedFormControl(''),
			});
			
		}, err => {
			this.showSaveAlertBox 		= false;
			this.showSaveAlertBoxSuccess 	= false;
			this.showSaveAlertBoxFailure 	= true;
		});
		
	}
	
	/**
	* Displays dialog to create an alert for the current search critera
	*/
	public showCreateAlertDialog():void{
		
		this.showSaveAlertBox 			= true;
		this.showSaveAlertBoxSuccess 	= false;
		this.showSaveAlertBoxFailure 	= false;
		
		this.feedbackDialogBox.nativeElement.showModal();
		
	}
	
	/**
	* Displays dialog to create an alert for the current search critera
	*/
	public showFilterByJobSpecDialog(content:any):void{
		
		this.switchJobSpecUpldOpt('chooseType');
		
		this.filterByJobSpecForm = new UntypedFormGroup({
			specAsText: new UntypedFormControl('Enter Job specification Text here...'),
		});
		
		this.showFilterByJonSpecFailure  	= false;
		this.showFilterByJobSpec 			= true;
	
		 this.specUploadDialogBox.nativeElement.showModal();
			
	}
	
	/**
	*  Closes the confirm popup
	*/
	public closeModal(): void {
		
		this.showSaveAlertBox 				= false;
		this.showSaveAlertBoxSuccess 		= false;
		this.showSaveAlertBoxFailure 		= false;
		this.showFilterByJonSpecFailure  	= false;
		this.showFilterByJobSpec 			= true;
		
		this.modalService.dismissAll();
	}
	
	public openPublicityDialog() {
		
		this.publicitySuggestions = this.suggestions.slice();
		
		
		this.publicitySuggestions = this.publicitySuggestions.splice(0,Math.min(this.publicitySuggestions.length, 10));
		
		this.publicityDialogBox.nativeElement.showModal();
  	}

	/**
	* Returns the code identifying the country
	* @param country - Country to get the country code for
	*/
	public getCountryCode(country:string):string{
		return this.candidateService.getCountryCode(country);
  	}
	
	/**
	* Whether Contract Rate info is available 
	*/
	public hasContractRate():boolean{
		return this.candidateProfile.rateContract && (this.candidateProfile.rateContract.valueMin != 0 || this.candidateProfile.rateContract.valueMax != 0);
	}
	
	/**
	* Contract Rate info 
	*/
	public getContractRate():string{
		
		if(this.candidateProfile.rateContract.valueMin != 0 && this.candidateProfile.rateContract.valueMax != 0){
			return this.translate.instant('info-item-title-rate') + this.candidateProfile.rateContract.currency + " "
			+ this.candidateProfile.rateContract.valueMin 
			+ this.translate.instant('info-item-title-to') + this.candidateProfile.rateContract.valueMax 
			+ this.translate.instant('info-item-title-per') + this.candidateProfile.rateContract.period.toLowerCase() ;
		}
		
		if(this.candidateProfile.rateContract.valueMin == 0 && this.candidateProfile.rateContract.valueMax != 0){
			return this.translate.instant('info-item-title-rate') + this.candidateProfile.rateContract.currency 
			+ " " + this.candidateProfile.rateContract.valueMax 
			+ this.translate.instant('info-item-title-per') + this.candidateProfile.rateContract.period.toLowerCase() ;
		}
		
		if(this.candidateProfile.rateContract.valueMin != 0 && this.candidateProfile.rateContract.valueMax == 0){
			return this.translate.instant('info-item-title-rate') + this.candidateProfile.rateContract.currency 
			+ " " + this.candidateProfile.rateContract.valueMin 
			+ this.translate.instant('info-item-title-per') + this.candidateProfile.rateContract.period.toLowerCase() ;
		}
		
		return "";
		
	}

	/**
	* Whether Perm Rate info is available 
	*/
	public hasPermRate():boolean{
		return this.candidateProfile.ratePerm && (this.candidateProfile.ratePerm.valueMin != 0 || this.candidateProfile.ratePerm.valueMax != 0);
	}
	
	/**
	* Contract Rate info 
	*/
	public getPermRate():string{
		
		if(this.candidateProfile.ratePerm.valueMin != 0 && this.candidateProfile.ratePerm.valueMax != 0){
			return this.translate.instant('info-item-title-rate') + this.candidateProfile.ratePerm.currency + " "
			+ this.candidateProfile.ratePerm.valueMin 
			+ this.translate.instant('info-item-title-to') + this.candidateProfile.ratePerm.valueMax 
			+ this.translate.instant('info-item-title-per') + this.candidateProfile.ratePerm.period.toLowerCase() ;
		}
		
		if(this.candidateProfile.ratePerm.valueMin == 0 && this.candidateProfile.ratePerm.valueMax != 0){
			return this.translate.instant('info-item-title-rate') + this.candidateProfile.ratePerm.currency 
			+ " " + this.candidateProfile.ratePerm.valueMax 
			+ this.translate.instant('info-item-title-location-per') + this.candidateProfile.ratePerm.period.toLowerCase() ;
		}
		
		if(this.candidateProfile.ratePerm.valueMin != 0 && this.candidateProfile.ratePerm.valueMax == 0){
			return this.translate.instant('info-item-title-rate') + this.candidateProfile.ratePerm.currency 
			+ " " + this.candidateProfile.ratePerm.valueMin 
			+ this.translate.instant('info-item-title-per') + this.candidateProfile.ratePerm.period.toLowerCase() ;
		}
		
		return "";
		
	}
	
	/**
	* Whether or not the User is a Candidate
	*/
	public isCandidate():boolean{
		return sessionStorage.getItem('isCandidate') === 'true';
	}
	
	/**
	* Whether or not the User is a Admin
	*/
	public isAdmin():boolean{
		return sessionStorage.getItem('isAdmin') === 'true';
	}
	
	/**
	* Whether user has a paid subscription 
	*/
	public hasPaidSubscription():boolean{
		return sessionStorage.getItem('hasPaidSubscription') === 'true';
	}
	
	
	/**
	* If the Recruiter is the owner of the selected Candidate
	*/
	public isOwner():boolean{
		
		if (!this.candidateNavService.isRouteActive()) {
			return false;
		}
		
		return this.candidateProfile.ownerId == this.getLoggedInUserId();
	}
	
	
	/**
	* Whether or not the User is a Recruiter
	*/
	public isRecruiter():boolean{
		return sessionStorage.getItem('isRecruiter') === 'true';
	}
	
	/**
	* Whether or not the Use is a Candidate
	*/
	public getLoggedInUserId():string{
		return ""+sessionStorage.getItem("userId");
	}
	
	/**
	* Removes defailt text from text area so user can paste 
	* actial content
	*/
	public clearSpecAsText():void{
		
		let jobSpecText = this.filterByJobSpecForm.get('specAsText')?.value;
		
		if (jobSpecText == 'Enter Job specification Text here...'){
			this.filterByJobSpecForm.get('specAsText')?.setValue(''); 
		}
		 
	}
	
	/**
	* Returns human readable version of days onsite
	*/
	public formatHumanReadableDaysOnsite(value:string):string{
		switch(value){
			case 'ZERO': 	return this.translate.instant('info-item-days-onsite-fully-remote');//"Fully Remote";
			case 'ONE': 	return "1";
			case 'TWO': 	return "2";
			case 'THREE': 	return "3";
			case 'FOUR': 	return "4";
			case 'FIVE':	return "5";
			default: 		return "";
		}
	}
	
	public doPaidSubscriptionCheckUnavailableCandidates():void{
		this.paidFeature = 'paidFeatureUnavailableCandidates';
		if(this.doPaidSubscriptionCheck()){
			
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
	
	public includeUnavailableCandidatesSelected:string = 'true';
	public includeRequiresSponsorshipCandidatesSelected:string = 'true';
	
	
	
	public doPaidSubscriptionCheck():boolean{
		let hasPaidSubscription:boolean = (sessionStorage.getItem("hasPaidSubscription") === 'true');
		
		if (!this.isAdmin() && !hasPaidSubscription) {
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
	* Scrolls to top of page
	*/
	public doScrollTop():void{
		(<HTMLInputElement>document.getElementById('page-top')).scrollIntoView({
			block: "start",
			inline: "nearest"
			});
		
	}
	
	public getUnvailableCssClass(candidate:Candidate):string{
		
		if(this.isAdmin() || this.hasPaidSubscription()) {
			return "";
		}
		
		return "candidate-available-" + candidate.available;
		
	}
	
}