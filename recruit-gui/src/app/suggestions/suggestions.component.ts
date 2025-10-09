import { Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild } 	from '@angular/core';
import { UntypedFormGroup, UntypedFormControl }										from '@angular/forms';
import { TranslateService } 														from '@ngx-translate/core';
import { NgbModal }																	from '@ng-bootstrap/ng-bootstrap';
import { DomSanitizer, SafeResourceUrl } 											from '@angular/platform-browser';
import { Router}																	from '@angular/router';
import { Subscription } 															from 'rxjs';
import { CandidateServiceService }													from '../candidate-service.service';
import { SuggestionsService }														from '../suggestions.service';
import { CurriculumService }														from '../curriculum.service';
import { Candidate}																	from './candidate';
import { SavedCandidate}															from './saved-candidate';
import { SuggestionParams}															from './suggestion-param-generator';
import { CandidateProfile } 														from '../candidate-profile';
import { CandidateNavService } 														from '../candidate-nav.service';
import { CreditsService } 															from '../credits.service';
import { InfoItemConfig } 															from '../candidate-info-box/info-item';
import { AppComponent} 																from '../app.component';
import { CandidateTotals } 															from '../candidate-totals';
import { GeoZone } 																	from '../geo-zone';
import { SupportedCountry } 														from '../supported-candidate';
import { SupportedLanguage } 														from '../supported-language';
import { CurrentUserAuth }															from '../current-user-auth';
import { City } 																	from '../city';
import { InfoPaneUtil } 															from './info-pane-util';
import { SearchbarComponent } 														from '../suggestions/searchbar/searchbar.component';
import { SuggestionsSearchRequest } 												from './suggestion-search-request';

/**
* Component to suggest suitable Candidates based upon a 
* Recruiters search
*/
@Component({
  selector: 'app-suggestions',
  standalone: false,
  templateUrl: './suggestions.component.html',
  styleUrls: ['./suggestions.component.css','./suggestions.component.two.css','./suggestions.component-mob.css']
})
export class SuggestionsComponent implements OnInit {

	@Input()  externalProvileViewCandidateId:string = "";
	@Input()  parentComponent:string = "";
	
	@Output() switchViewEvent 						= new EventEmitter<string>();
	
 	@ViewChild('specUploadBox', { static: true }) 				specUploadDialogBox!: ElementRef<HTMLDialogElement>;
 	@ViewChild('feedbackBox', { static: true }) 				feedbackDialogBox!: ElementRef<HTMLDialogElement>;
 	@ViewChild('publicityModal', {static:true})					publicityDialogBox!: ElementRef<HTMLDialogElement>;
 	@ViewChild('confirmDeleteModal', {static:true})				confirmDeleteDialogBox!: ElementRef<HTMLDialogElement>;
 	@ViewChild('contactBox', {static:true})						contactDialogBox!: ElementRef<HTMLDialogElement>;
 	@ViewChild('paidSubscriptionModal', {static:true})			paidSubscriptionBox!: ElementRef<HTMLDialogElement>;
	@ViewChild('searchTypeFilterSelectionModal', {static:true}) searchTypeFilterSelectionModal!: ElementRef<HTMLDialogElement>;
	@ViewChild(SearchbarComponent) 								searchBar!:SearchbarComponent;
	
	public currentUserAuth:CurrentUserAuth 						= new CurrentUserAuth();
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
	public showLocationFilters:string							= "";
	public showCountryFilters:string							= "";
	public showLanguageFilters:string							= "";
	public showIncludeFilters:string							= "";
	public geoZones:Array<GeoZone>								= new Array<GeoZone>();
	public supportedCountries:Array<SupportedCountry>			= new Array<SupportedCountry>();
	public citiesForSelectedCountry:Array<City>					= new Array<City>();
	public publicitySuggestions:Array<Candidate>  				= new Array<Candidate>();
	public searchBarCss 										= this.currentView === 'suggestion-results' ? 'showChild' : 'hideChild';
	private subscription?:Subscription;
	public supportedLanguages:Array<SupportedLanguage> 			= new Array<SupportedLanguage>();
	public infoPaneUtil:InfoPaneUtil; 				
	public filename:string 										= '';
	public includeUnavailableCandidatesSelected:string 			= 'true';
	public includeRequiresSponsorshipCandidatesSelected:string 	= 'true';
	safeUrl:any
	public publicityView:string									= 'basic';
			
	public createAlertForm:UntypedFormGroup 					= new UntypedFormGroup({
		alertName:												new UntypedFormControl(''),
	});
	
	public suggestionFilterForm:UntypedFormGroup 				= new UntypedFormGroup({});
	public skilFilterForm:UntypedFormGroup 						= new UntypedFormGroup({
		skill: 													new UntypedFormControl(''),
	});
	public filterByJobSpecForm:UntypedFormGroup 				= new UntypedFormGroup({
		specAsText:												new UntypedFormControl('Enter Job specification Text here...'),
	});
	
	public filterTypeFormGroup:UntypedFormGroup					= new UntypedFormGroup({
		searchType:												new UntypedFormControl('FUNCTION'),
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
		
		this.infoPaneUtil = new InfoPaneUtil(this.candidateProfile, this.translate, this.supportedCountries);
		
	}
		
	public handleNewSearchRequest(newSuggestions:Array<Candidate>){
		this.suggestions = newSuggestions;
	}
	
	private init():void{
		
		//Candidate
		if (this.currentUserAuth.isCandidate()) {
			this.candidateNavService.startCandidateProfileRouteForCandidate();
			
			let searchRequest:SuggestionsSearchRequest = new SuggestionsSearchRequest();
			searchRequest.candidateFilters.candidateIds.push(this.currentUserAuth.getLoggedInUserId());
						
			this.candidateService.getCandidateSuggestions(searchRequest).subscribe(candidate => {			
				this.showSuggestedCandidateOverview(candidate.body.content[0]);	
			});
		}
		
		//Recruiter
		if (this.currentUserAuth.isRecruiter() && this.candidateNavService.isRouteActive()) {
			
			let searchRequest:SuggestionsSearchRequest = new SuggestionsSearchRequest();
			searchRequest.candidateFilters.ownerId = this.currentUserAuth.getLoggedInUserId();
			searchRequest.candidateFilters.candidateIds.push(this.candidateNavService.getCandidateId());
												
			this.candidateService.getCandidateSuggestions(searchRequest).subscribe(candidate => {
			//this.candidateService.getCandidateByIdWithRecruiterAsOwner(this.candidateNavService.getCandidateId(), this.currentUserAuth.getLoggedInUserId()).subscribe(candidate => {
				this.showSuggestedCandidateOverview(candidate.body.content[0]);	
			});
		}

		//Admin
		if (this.currentUserAuth.isAdmin() && this.candidateNavService.isRouteActive()) {
			let searchRequest:SuggestionsSearchRequest = new SuggestionsSearchRequest();
			searchRequest.candidateFilters.candidateIds.push(this.candidateNavService.getCandidateId());
									
			this.candidateService.getCandidateSuggestions(searchRequest).subscribe(candidate => {
				this.showSuggestedCandidateOverview(candidate.body.content[0]);	
			});
		}		
		
		if (this.currentUserAuth.isAdmin() || this.currentUserAuth.hasPaidSubscription()) {
			this.showPaidSubscriptinOptions = true;
		} 
		
		this.initSupportedCountries();
		
		if (this.subscription) {
			this.subscription.unsubscribe();
		}
		
	}
	
	public back():void{
		this.switchViewEvent.emit();
	}
	
  	public setJobSepecFile(event:any):void{
  
  		if (event.target.files.length <= 0) {
  			return;
  		}
  	
  		this.jobSpecFile = event.target.files[0];
  		
  	}
	
	/**
	* Resets the filters
	*/
	public doReset():void{
		this.searchBar.resetSearchFilters();
		this.searchBar.addChangeListener(true);
		this.filterByJobSpecForm = new UntypedFormGroup({
			specAsText: new UntypedFormControl('Enter Job specification Text here...'),
		});
	}
		
	public showCVInline(candidateId:string):void{
		
		let url = this.curriculumService.getCurriculumUrlForInlinePdf(candidateId); 'http://127.0.0.1:8080/curriculum-test/';
		
		
		this.trustedResourceUrl = this.sanitizer.bypassSecurityTrustResourceUrl(url);
		
	}
	
	/**
	* Initializes Component`
	*/
	ngOnInit(): void {
		
		if (this.externalProvileViewCandidateId.length > 0) {
			
			let searchRequest:SuggestionsSearchRequest = new SuggestionsSearchRequest();
			searchRequest.candidateFilters.candidateIds.push(this.externalProvileViewCandidateId);
			
			this.candidateService.getCandidateSuggestions(searchRequest).subscribe(result => {
					this.showSuggestedCandidateOverview(result.body.content[0]);
			});
		}
		
	}
	
	public savedCandidatesLoaded:boolean = false;
	
	ngAfterViewChecked(){
		
		this.searchBarCss = this.currentView === 'suggestion-results' ? 'showChild' : 'hideChild';
			
		if (sessionStorage.getItem("news-item-div")){
			this.doScrollTop();	
		}
		
		if (!this.savedCandidatesLoaded && !this.isAuthenticatedAsCandidate()) {
			this.savedCandidatesLoaded = true;
			this.candidateService.fetchSavedCandidates().subscribe(response => {
				this.savedCandidates = response;
			});
		}
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
	* Shows the Saved Candidates view
	*/
	public showSavedCandidates():void{
		this.currentView 	= 'saved-candidates';
		this.lastView 		= 'saved-candidates';
		//this.candidateService.fetchSavedCandidates().subscribe(response => {
		//	this.savedCandidates = response;
		//});
	}
	
	/**
	* Shows the Suggesion result view
	*/
	public showSuggestionsResults():void{
	
		if (this.isAuthenticatedAsCandidate()) {
			return;
		}
		
		this.candidateService.fetchSavedCandidates().subscribe(response => {
			this.savedCandidates = response;
		});
		
		if(this.parentComponent == 'newsfeed') {
			this.back();		
		} else {
			if (this.currentUserAuth.isRecruiter() && this.candidateNavService.isRouteActive()) {
				this.candidateNavService.doNextMove("back",this.candidateNavService.getCandidateId());	
			} else {
				this.currentView 		= 'suggestion-results';
				this.suggestedCandidate = new Candidate();	
				this.lastView 			= '';
			}	
		}
		
	}
	
	public doCreditCheck():void{
		
		if(this.currentUserAuth.isAdmin()){
			this.passedCreditCheck = true;
		} else {
			this.curriculumService.getCreditCount().subscribe(count => {
				this.passedCreditCheck = (count > 0 || count == -1);
			});	
		}
	}
	
	public setLeftInfoPane(candidateId:string):void{
		
		this.candidateService.getCandidateProfileById(candidateId).subscribe( candidate => {
			this.candidateProfile 	= candidate;
			this.infoPaneUtil 		= new InfoPaneUtil(candidate, this.translate, this.supportedCountries); 
			this.infoItemConfig 	= this.infoPaneUtil.generateInfoPane();
		});
		
	}
	
	/**
	* Shows the Suggesion result view
	*/
	public showSuggestedCandidateOverview(candidateSuggestion:Candidate):void{
		
		this.skillFilters = this.searchBar.skillFilters;
		
		if (this.currentUserAuth.isCandidate()) {
			this.setLeftInfoPane(candidateSuggestion.candidateId);
		
			this.currentView 			= 'suggested-canidate-overview';
			this.suggestedCandidate 	= candidateSuggestion;
			
			this.doScrollTop();
		}
		
		if(!candidateSuggestion.available && !this.doPaidSubscriptionCheck() || this.currentUserAuth.isCandidate()){
			this.paidFeature = "paidFeatureUnavailableCandidates";
			return;
		}
		
		
		this.doCreditCheck();
		
		//Admin
		if (this.currentUserAuth.isAdmin()) {
			this.candidateNavService.startCandidateProfileRouteForAdmin();
		}
		
		this.setLeftInfoPane(candidateSuggestion.candidateId);
		
		this.currentView 			= 'suggested-canidate-overview';
		this.suggestedCandidate 	= candidateSuggestion;
		
		if(this.currentUserAuth.isRecruiter()) {
			this.candidateService.registerCandidateProfileViewed(candidateSuggestion.candidateId).subscribe();
		}
		
		this.doScrollTop();
		
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
	* Displays dialog to create an alert for the current search critera
	*/
	public showFilterByJobSpecDialog(content:any):void{
		
		this.switchJobSpecUpldOpt('chooseType');
		
		//this.filterByJobSpecForm = new UntypedFormGroup({
		//	specAsText: new UntypedFormControl('Enter Job specification Text here...'),
		//});
		
		this.showFilterByJonSpecFailure  	= false;
		this.showFilterByJobSpec 			= true;
	
		 this.specUploadDialogBox.nativeElement.showModal();
			
	}
	
	/**
	*  Closes the confirm popup
	*/
	public closeModal(): void {
		
		this.showFilterByJonSpecFailure  	= false;
		this.showFilterByJobSpec 			= true;
		
		this.modalService.dismissAll();
	}
	
	public openPublicityDialog() {
		
		this.publicityView = 'basic';
		
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
	* If the Recruiter is the owner of the selected Candidate
	*/
	public isOwner():boolean{
		
		if (!this.candidateNavService.isRouteActive()) {
			return false;
		}
		
		return this.candidateProfile.ownerId == this.currentUserAuth.getLoggedInUserId();
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
	* Scrolls to top of page
	*/
	public doScrollTop():void{
		(<HTMLInputElement>document.getElementById('page-top')).scrollIntoView({
			block: "start",
			inline: "nearest"
			});
		
	}
	
	/**
	* Depending upon a Users role and subscriptiton options a User will not be able to view
	* certain suggested Candidates. This method return the name of a CSS class which will 
	* either make the Candidate appar available or unavailable are required
	*/
	public getUnvailableCssClass(candidate:Candidate):string{
		
		if (this.currentUserAuth.isAdmin() || this.currentUserAuth.isCandidate() || this.currentUserAuth.hasPaidSubscription()) {
			return "";
		}
		
		return "candidate-available-" + candidate.available;
		
	}
	
	//1. Extract filters code [ Logic ]
	/**
	* Extracts filters from job specification text
	*/
	public extractFiltersFromJobSpecText():void{
		
		let jobSpecText = this.filterByJobSpecForm.get('specAsText')?.value; 
		
		this.candidateService.extractFiltersFromText(jobSpecText).subscribe(extractedFilters=>{
			this.extractFiltersSuccess(extractedFilters);
		},(() =>{
			this.extractFiltersFailure();
		}));
	}

	/**
	* Extracts filters from job specification file
	*/	
	public extractFiltersFromJobSpec():void{
		
		this.filterByJobSpecForm = new UntypedFormGroup({
			specAsText: new UntypedFormControl('Enter Job specification Text here...'),
		});
				
		this.candidateService.extractFiltersFromDocument(this.jobSpecFile).subscribe(extractedFilters=>{
			this.extractFiltersSuccess(extractedFilters);
		},(() =>{
			this.extractFiltersFailure();
		}));
	}

	/**
	* Handles failure case where filters where 
	* not able to be extracted 
	*/
	private extractFiltersFailure():void{
		this.showFilterByJonSpecFailure 	= true;
		this.showFilterByJobSpec 			= false;
	}

	/**
	* Handles success case where filters where 
	* able to be extracted 
	*/
	private extractFiltersSuccess(extractedFilters:any):void{
		this.searchBar.processJobSpecExtratedFilters(extractedFilters);
		this.specUploadDialogBox.nativeElement.close();
		this.searchBar.addChangeListener(false);
		this.closeModal();
	}
	
	//2. Subscriptions
	/**
	* If the User attempts an action that is only available to Admin or Recruiters with a subscription 
	* and the User is a Recruiter without a subscription, shows a modal informing them they must
	* select a subscriptiton if they want to perform the action 
	*/
	public doPaidSubscriptionCheck():boolean{
		if (!this.currentUserAuth.isAdmin() && !this.currentUserAuth.isCandidate() && !this.currentUserAuth.hasPaidSubscription()) {
			this.suggestionFilterForm.get("includeUnavailableCandidates")?.setValue('');
			this.paidSubscriptionBox.nativeElement.showModal();
			return false;
		}
		return true;
	}

	/**
	* Navigates to the Subscription selection page
	*/
	public choseSubscription():void{
		this.creditsService.buySubscription();
		this.router.navigate(['recruiter-account']);
	}
	
	/**
	* Whether or not the user has authenticated as an Candidate user 
	*/
	public isAuthenticatedAsCandidate():boolean {
		return sessionStorage.getItem('isCandidate') === 'true';
	}

	/**
	* Shows basic view in publicity popup 
	*/
	public showPublicityBasic():void {
		this.publicityView = 'basic';
	}
	
	/**
	* Shows advanced view in publicity popup 
	*/
	public showPublicityAdvanced():void {
		this.publicityView = 'advanced';
	}
	
}