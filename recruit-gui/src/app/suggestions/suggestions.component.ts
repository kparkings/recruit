import { Component, ElementRef, EventEmitter, Input, OnInit, Output, TemplateRef, ViewChild } 														from '@angular/core';
import { UntypedFormGroup, UntypedFormControl, FormsModule, ReactiveFormsModule }	from '@angular/forms';
import { CandidateServiceService }													from '../candidate-service.service';
import { SuggestionsService }														from '../suggestions.service';
import { CurriculumService }														from '../curriculum.service';
import { Candidate}																	from './candidate';
import { SavedCandidate}															from './saved-candidate';
import { SuggestionParams}															from './suggestion-param-generator';
import { environment }																from '../../environments/environment';
import { Clipboard } 																from '@angular/cdk/clipboard';
import { NgbModal, NgbModalOptions, NgbAlert }												from '@ng-bootstrap/ng-bootstrap';
import { CandidateSearchAlert }														from './candidate-search-alert';
import { DomSanitizer, SafeResourceUrl } 											from '@angular/platform-browser';
import { DeviceDetectorService } 													from 'ngx-device-detector';
import { Router}																	from '@angular/router';
import { debounceTime } 															from "rxjs/operators";
import { PhotoAPIOutbound, CandidateProfile, Language, Rate } 						from '../candidate-profile';
import { EmailService, EmailRequest }												from '../email.service';
import { CandidateNavService } 														from '../candidate-nav.service';
import { CreditsService } 															from '../credits.service';
import { ExtractedFilters } 														from './extracted-filters';
import { InfoItemBlock, InfoItemConfig, InfoItemRowKeyValue, InfoItemRowKeyValueFlag, InfoItemRowKeyValueMaterialIcon, InfoItemRowSingleValue } from '../candidate-info-box/info-item';
import {AppComponent} 																from '../app.component';
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
	@Output() switchViewEvent 						= new EventEmitter<string>();
	
 	@ViewChild('specUploadBox', { static: true }) 		specUploadDialogBox!: ElementRef<HTMLDialogElement>;
 	@ViewChild('feedbackBox', { static: true }) 		feedbackDialogBox!: ElementRef<HTMLDialogElement>;
 	@ViewChild('notesBox', { static: true }) 			notesDialogBox!: ElementRef<HTMLDialogElement>;
 	@ViewChild('publicityModal', {static:true})			publicityDialogBox!: ElementRef<HTMLDialogElement>;
 	@ViewChild('confirmDeleteModal', {static:true})		confirmDeleteDialogBox!: ElementRef<HTMLDialogElement>;
 	@ViewChild('contactBox', {static:true})				contactDialogBox!: ElementRef<HTMLDialogElement>;
 	
 	public backToNewsFeed():void{
		this.switchViewEvent.emit();	 
	}
 	
	public candidateProfile:CandidateProfile = new CandidateProfile();
	
	public savedCandidates:Array<SavedCandidate> = new Array<SavedCandidate>();

	public createAlertForm:UntypedFormGroup = new UntypedFormGroup({
		alertName:			new UntypedFormControl(''),
	});
	
	public savedCandidateNotesForm:UntypedFormGroup = new UntypedFormGroup({
		notes:			new UntypedFormControl(''),
	});
	
	private jobSpecFile!:File;
	
	public jobSpecUploadView:string = "chooseType"; //chooseType | doc | text
	
	/*Mobile */
	public mobileListingLeftPaneContainer:string = '';
	public mobileDescBody:string = '';
	public mobileJobTitleBar:string = '';
	public mobileSearchBox:string = '';
	
	public infoItemConfig:InfoItemConfig 							= new InfoItemConfig();
	
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
				//private clipboard: 				Clipboard, 
				private modalService: 			NgbModal, 
				private sanitizer: 				DomSanitizer,
				private curriculumService: 		CurriculumService,
				private deviceDetector: 		DeviceDetectorService,
				private router:					Router,
				private emailService:			EmailService,
				private candidateNavService: 	CandidateNavService,
				private creditsService:			CreditsService,
				private appComponent:			AppComponent) { 
					
		this.trustedResourceUrl = this.sanitizer.bypassSecurityTrustResourceUrl('');
		
		this.isMobile = deviceDetector.isMobile();
		
		if (this.isMobile) {
			this.mobileListingLeftPaneContainer = "mobile-left-pane-container";
			this.mobileDescBody 				= 'mobile-desc-body';
			this.mobileJobTitleBar				= 'sugg-srch-job-title-mobile';
			this.mobileSearchBox			 	= 'mobile-search-box';
		} 
		
		
		this.init();
	}
	
	private init():void{
		
		if(!this.isCandidate()) {
			this.getSuggestions();	
		}
		
	 	
		if (this.isMobile) {
			this.suggestionMobileClass 		= 'suggestion-icon-mobile';
			this.suggestionMobileBtnClass	= 'buttons-icon-mobile';
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
			this.candidateService.getCandidateById(this.candidateNavService.getCandidateId()).subscribe(candidate => {
				this.showSuggestedCandidateOverview(candidate.content[0]);	
			});
		}

		//Admin
		if (this.isAdmin() && this.candidateNavService.isRouteActive()) {
			this.candidateService.getCandidateById(this.candidateNavService.getCandidateId()).subscribe(candidate => {
				this.showSuggestedCandidateOverview(candidate.content[0]);	
			});
		}		
		
		this.appComponent.refreschUnreadAlerts();
		
	}
	
	/*
	* Navigates to the edit page
	*/
	public editAccount():void{
		this.candidateNavService.doNextMove("edit", this.candidateProfile.candidateId);
	}

	/*
	* Opens confirm delete popup
	*/	
	public deleteAccount(){
		this.confirmDeleteDialogBox.nativeElement.showModal();
		//this.open(content, '', true);
	}
	
	public confirmDeleteProfile():void{
		this.candidateService.deleteCandidate(this.candidateProfile.candidateId).subscribe(data => {
			if (this.isCandidate()) {
				sessionStorage.removeItem('isAdmin');
				sessionStorage.removeItem('isRecruter');
				sessionStorage.removeItem('isCandidate');
				sessionStorage.removeItem('loggedIn');
				sessionStorage.setItem('beforeAuthPage', 'view-candidates');
				this.candidateNavService.reset();
				this.router.navigate(['login-user']);
				this.appComponent.refreschUnreadAlerts();
			}else {
				this.doReset();
				this.currentView = 'suggestion-results';
			}
			//this.closeModal();
			this.confirmDeleteDialogBox.nativeElement.close();
		
		});
	}
	
  	public setJobSepecFile(event:any):void{
  
  		if (event.target.files.length <= 0) {
  			return;
  		}
  	
  		this.jobSpecFile = event.target.files[0];
  		
  	}

	public isNoLongerAvailable():boolean{
		
		if (this.currentSavedCandidate.userId === '') {
			return false;
		}
		
		return this.currentSavedCandidate.candidate.candidateId === ' Removed';;
	}
	
	public isNoLongerAvailableSC(savedCandidate:SavedCandidate):boolean{
		return savedCandidate.candidate.candidateId === ' Removed';
	}
	
	private processJobSpecExtratedFilters(extractedFilters:ExtractedFilters):void{
		
		this.resetSearchFilters(false);
			
			this.skillFilters = extractedFilters.skills;
			
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
			
			this.suggestionFilterForm.valueChanges.subscribe(value => {
				this.getSuggestions();	
			});
			
			this.getSuggestions();
			
	}
	
	public extractFiltersFromJobSpecText():void{
		
		let jobSpecText = this.filterByJobSpecForm.get('specAsText')?.value; 
		
		this.candidateService.extractFiltersFromText(jobSpecText).subscribe(extractedFilters=>{
			this.processJobSpecExtratedFilters(extractedFilters);
			this.specUploadDialogBox.nativeElement.close();
		},(failure =>{
			this.showFilterByJonSpecFailure 	= true;
			this.showFilterByJobSpec 			= false;
			this.suggestionFilterForm.valueChanges.subscribe(value => {
				this.getSuggestions();	
			});
		}));
	}
	
	/**
 	* Extracts filters from job specification file
	*/	
  	public extractFiltersFromJobSpec():void{
  		
  		this.candidateService.extractFiltersFromDocument(this.jobSpecFile).subscribe(extractedFilters=>{
  			this.processJobSpecExtratedFilters(extractedFilters);
  			this.specUploadDialogBox.nativeElement.close();
		},(failure =>{
			this.showFilterByJonSpecFailure 	= true;
			this.showFilterByJobSpec 			= false;
			this.suggestionFilterForm.valueChanges.subscribe(value => {
				this.getSuggestions();	
			});
		}));
  		
  	}

	public suggestions:Array<Candidate>  = new Array<Candidate>();

	public suggestionFilterForm:UntypedFormGroup 	= new UntypedFormGroup({
		searchPhrase:			new UntypedFormControl(''),
		nlResults: 				new UntypedFormControl(true),
		beResults: 				new UntypedFormControl(true),
		ukResults: 				new UntypedFormControl(true),
		ieResults: 				new UntypedFormControl(true),
		contractType: 			new UntypedFormControl('Both'),
		dutchLanguage: 			new UntypedFormControl(false),
		englishLanguage: 		new UntypedFormControl(false),
		frenchLanguage:			new UntypedFormControl(false),
		minYearsExperience: 	new UntypedFormControl(''),
		maxYearsExperience: 	new UntypedFormControl(''),
		//skill: 					new UntypedFormControl(''),
	});
	
	public skilFilterForm:UntypedFormGroup 	= new UntypedFormGroup({
		skill: 					new UntypedFormControl(''),
	});
	
	public filterByJobSpecForm:UntypedFormGroup = new UntypedFormGroup({
		specAsText:				new UntypedFormControl('Enter Job specification Text here...'),
	});
	
	/**
	* Resets the filters
	*/
	public doReset():void{
		this.resetSearchFilters(true);
		this.getSuggestions();
	}	
	
	/**
	* Resets the filters
	*/
	private resetSearchFilters(attachValueChangeListener:boolean):void{
		this.suggestionFilterForm = new UntypedFormGroup({
			searchPhrase:			new UntypedFormControl(''),
			nlResults: 				new UntypedFormControl(true),
			beResults: 				new UntypedFormControl(true),
			ukResults: 				new UntypedFormControl(true),
			ieResults: 				new UntypedFormControl(true),
			contractType: 			new UntypedFormControl('Both'),
			dutchLanguage: 			new UntypedFormControl(false),
			englishLanguage: 		new UntypedFormControl(false),
			frenchLanguage:			new UntypedFormControl(false),
			minYearsExperience: 	new UntypedFormControl(''),
			maxYearsExperience: 	new UntypedFormControl(''),
			skill: 					new UntypedFormControl(''),
		});
		
		this.skilFilterForm = new UntypedFormGroup({
			skill: 					new UntypedFormControl(''),
		});
	
		this.skillFilters = new Array<string>();
		
		if (attachValueChangeListener) {
			this.suggestionFilterForm.valueChanges.subscribe(value => {
				this.getSuggestions();	
			});
		}
		
	}
	
	public currentView:string 				= 'suggestion-results';
	public skillFilters:Array<string>		= new Array<string>();
	public minMaxOptions:Array<string> 		= new Array<string>('','1','2','3','4','5','8','10','15','20','25','30');
	public suggestedCandidate:Candidate		= new Candidate();
	
	public showSaveAlertBoxFailure:boolean  = false;
	public showSaveAlertBoxSuccess:boolean  = false;
	public showSaveAlertBox:boolean 		= false;
	
	public showFilterByJonSpecFailure:boolean  	= false;
	public showFilterByJobSpec:boolean 				= false;
	
	
	public dangerousUrl = 'http://127.0.0.1:8080/curriculum-test/1623.pdf';
	public trustedResourceUrl : SafeResourceUrl;
	
	public isMobile:boolean = false;
	public suggestionMobileClass:string			= '';
	public suggestionMobileBtnClass:string		= '';

	
	public showCVInline(candidateId:string):void{
		
		let url = this.curriculumService.getCurriculumUrlForInlinePdf(candidateId); 'http://127.0.0.1:8080/curriculum-test/';
		
		
		this.trustedResourceUrl = this.sanitizer.bypassSecurityTrustResourceUrl(url);
		
	}
	
	/**
	* Initializes Component`
	*/
	ngOnInit(): void {
		
		this.suggestionFilterForm.valueChanges.pipe(debounceTime(500)).subscribe(res => {
			
			this.getSuggestions();
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
			//sessionStorage.setItem("clear-news-item-div", "true");
		}
		
	}
	
	/**
	* If we are only viewing the profile or have access to edit and 
	* update features 
 	*/
	public isViewOnly():boolean{
		return this.externalProvileViewCandidateId.length > 0;
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
	
	
	public performFilterExtraction(event:any):void{
  
  		if (event.target.files.length <= 0) {
  			return;
  		}
  	
		this.candidateService.extractFiltersFromDocument(event.target.files[0]).subscribe(data => {
		
			this.getSuggestions();
			this.closeModal();	
			
		});
		
  	}

	/**
	* Sends request for Suggestions to the backend API
	*/
	private getSuggestions():void{
		
		const maxSuggestions:number 		= 112;
		
		let params:SuggestionParams = new SuggestionParams(this.suggestionFilterForm, this.skillFilters, new Array<string>());
		
		this.suggestionsService.getSuggestons(	
									maxSuggestions,
									params.getTitle(),
									params.getCountries(),
									params.getContract(),
									params.getPerm(),
									params.getMinExperience(),
									params.getMaxExperience(),
									params.getLanguages(),
									params.getSkills()
									).subscribe(data => {
												
										this.suggestions =  new Array<Candidate>();
												
										data.content.forEach((s:Candidate) => {
											this.suggestions.push(s);	
										});
												 
									}, err => {
			
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
	* Toggles whether Candidates from selected country 
	* should be included in the Suggestions 
	* @param country - Country to toggle
	*/
	public toggleCountrySelection(country:string):void{
		
		let included:boolean = this.suggestionFilterForm.get((country+'Results'))?.value;
		
		this.suggestionFilterForm.get((country+'Results'))?.setValue(!included);
				
	}
	
	/**
	* Returns whether Candidates from the selected country are currently
	* included in the Suggestion results
	*/
	public includeResultsForCountry(country:string):boolean{
		return this.suggestionFilterForm.get((country+'Results'))?.value;
	}
	
	/**
	* Adds a skill to the list of Skills to filter on
	*/
	public addSkill():void{
		
		let skillFormatted:string 	= this.skilFilterForm.get('skill')?.value.trim();
		skillFormatted 				= skillFormatted.toLocaleLowerCase();
		
		if (skillFormatted.length > 0 && this.skillFilters.indexOf(skillFormatted) == -1) {
			this.skillFilters.push(skillFormatted);	
		}
		
		this.getSuggestions();
		
		this.skilFilterForm.get('skill')?.setValue('');
		
	}
	
	/**
	* Removes a skill and calls for new suggestions
	*/
	public removeSkill(skill:string):void{
		
		skill = skill.trim();
		skill = skill.toLocaleLowerCase();
		
		this.skillFilters = this.skillFilters.filter(s => s  !== skill);
		
		this.getSuggestions();	
	
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
		
		this.getSuggestions();	
		
		if (this.isRecruiter() && this.candidateNavService.isRouteActive()) {
			this.candidateNavService.doNextMove("back",this.candidateNavService.getCandidateId());	
		} else {
			this.currentView 		= 'suggestion-results';
			this.suggestedCandidate = new Candidate();	
			this.lastView 			= '';
		}
	
	}
	
	public passedCreditCheck:boolean = false;
	
	public doCreditCheckByCount():void{
		
		if(this.isAdmin()){
			this.passedCreditCheck = true;
		} else {
			this.curriculumService.getCreditCount().subscribe(count => {
				this.passedCreditCheck = (count > 1 || count == -1);
			});	
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
	
	disableDownload():void{
		this.passedCreditCheck = false;
		this.doCreditCheck();
	}

	/**
	* Shows the inline CV view
	*/
	public showInlineCVView():void{
		
		if (this.passedCreditCheck) {
			this.currentView = 'inline-cv';
			this.showCVInline(this.suggestedCandidate.candidateId);
		} else {
			this.handleNoCredit();		
		}
		
		this.doCreditCheck();
	}

	public handleNoCredit():void{
		this.creditsService.tokensExhaused();
	}
	
	/**
	* Shows the Suggesion result view
	*/
	public showSuggestedCandidateOverview(candidateSuggestion:Candidate):void{
		
		this.doCreditCheck();
		
		//Admin
		if (this.isAdmin()) {
			this.candidateNavService.startCandidateProfileRouteForAdmin();
		}
		
		this.candidateService.getCandidateProfileById(candidateSuggestion.candidateId).subscribe( candidate => {
		this.candidateProfile = candidate;
		//START
		this.infoItemConfig = new InfoItemConfig();
		this.infoItemConfig.setProfilePhoto(this.candidateProfile?.photo?.imageBytes);
		if (!this.isCandidate()){
			this.infoItemConfig.setShowContactButton(true);
		}
		
		
		//Location
		let recruiterBlock:InfoItemBlock = new InfoItemBlock();
		recruiterBlock.setTitle("Location");
		recruiterBlock.addRow(new InfoItemRowKeyValueFlag("Country",this.getFlagClassFromCountry(this.suggestedCandidate.country)));
		recruiterBlock.addRow(new InfoItemRowKeyValue("City",this.suggestedCandidate.city));
		this.infoItemConfig.addItem(recruiterBlock);
		
		//Languages Block
		let languageBlock:InfoItemBlock = new InfoItemBlock();
		languageBlock.setTitle("Languages");
		this.suggestedCandidate.languages.forEach(lang => {
				languageBlock.addRow(new InfoItemRowKeyValueMaterialIcon(this.getLanguage(lang.language),this.getMaterialIconClassFromLangLevel(lang.level)));
		});
		this.infoItemConfig.addItem(languageBlock);
		
		//Contract Type Block
		if (this.suggestedCandidate.freelance == 'TRUE' || this.suggestedCandidate.perm == 'TRUE') {
			let contractTypeBlock:InfoItemBlock = new InfoItemBlock();
			contractTypeBlock.setTitle("Contract Type");
			if (this.suggestedCandidate.freelance == 'TRUE'){		
				contractTypeBlock.addRow(new InfoItemRowKeyValueMaterialIcon("Contract","available-check-icon"));
			}
			if (this.suggestedCandidate.perm == 'TRUE'){		
				contractTypeBlock.addRow(new InfoItemRowKeyValueMaterialIcon("Permanent","available-check-icon"));
			}
			this.infoItemConfig.addItem(contractTypeBlock);
		}
		//Contract Rate 
		if(this.hasContractRate()) {
			let contractRateBlock:InfoItemBlock = new InfoItemBlock();
			contractRateBlock.setTitle("Contract Rate");
			contractRateBlock.addRow(new InfoItemRowSingleValue(this.getContractRate()));
			this.infoItemConfig.addItem(contractRateBlock);
		}
		
		//Perm Rate 
		if(this.hasPermRate()) {
			let permRateBlock:InfoItemBlock = new InfoItemBlock();
			permRateBlock.setTitle("Perm Rate");
			permRateBlock.addRow(new InfoItemRowSingleValue(this.getPermRate()));
			this.infoItemConfig.addItem(permRateBlock);
		}
		
		//Years Experience 
		let yearsExperienceBlock:InfoItemBlock = new InfoItemBlock();
		yearsExperienceBlock.setTitle("Years Experience");
		yearsExperienceBlock.addRow(new InfoItemRowSingleValue(""+this.suggestedCandidate.yearsExperience));
		this.infoItemConfig.addItem(yearsExperienceBlock);
		
		//Availability
		let availabilityBlock:InfoItemBlock = new InfoItemBlock();
		availabilityBlock.setTitle("Availability");
		if (this.candidateProfile.daysOnSite) {
			availabilityBlock.addRow(new InfoItemRowKeyValue("Max Days on Site",this.formatHumanReadableDaysOnsite(this.candidateProfile.daysOnSite)));
		}
		if (this.candidateProfile.availableFromDate) {
			availabilityBlock.addRow(new InfoItemRowKeyValue("Available From",""+this.candidateProfile.availableFromDate));
		}
		this.infoItemConfig.addItem(availabilityBlock);
		
		
		//END
				
			});
		
		this.currentView 			= 'suggested-canidate-overview';
		this.suggestedCandidate 	= candidateSuggestion;
		
		//this.fetchCandidateProfile(candidateSuggestion.candidateId);
		
		this.doScrollTop();
		
		
	}
	
	public handleOpenContactBoxEvent():void{
		
	}
	
	/**
	* Returns the flag css class for the Flag matching
	* the Country 
	*/
	private getFlagClassFromCountry(country:string):string{
		
		switch(country){
			case "NETHERLANDS":{return "flag-icon-nl"}
			case "BELGIUM":{return "flag-icon-be"}
			case "UK":{return "flag-icon-gb"}
			case "REPUBLIC_OF_IRELAND":{return "flag-icon-ie"}
			default: return "";
		}
		
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
	
	public showSuggestedCandidateOverviewSavedCandidate(savedCandidate:SavedCandidate){
		this.currentView 			= 'suggested-canidate-overview';
		this.suggestedCandidate 	= savedCandidate.candidate;
		this.currentSavedCandidate	= savedCandidate;
		this.fetchCandidateProfile(savedCandidate.candidate.candidateId);
	}
	
	/**
	* Flags a Candidate as being potentially unavailable
	*/
	public markCandidateAsUnavailable():void {
		this.candidateService.markCandidateAsUnavailable(this.suggestedCandidate.candidateId).subscribe(data => {});
		this.suggestedCandidate.flaggedAsUnavailable = true;
	}
	
	public lastView:string = '';
	
	/**
	* Marks Candidate as being remembered
	*/
	public rememberCandidate(suggestedCandidate:Candidate):void{
		
		const savedCandidate:SavedCandidate = new SavedCandidate();
		
		savedCandidate.candidateId = Number(suggestedCandidate.candidateId);
		
		this.candidateService.addSavedCandidate(savedCandidate).subscribe(response => {
			this.candidateService.fetchSavedCandidates().subscribe(response => {
				this.savedCandidates = response;
			})
		});
	}
	
	/**
	* Marks Candidate as no longer being being remembered
	*/
	public forgetCandidate():void{
		this.candidateService.deleteSavedCandidate(Number(this.currentSavedCandidate.candidateId)).subscribe(response => {
			this.showSavedCandidates();
		});
	}
	
	
	
	/**
	*  Returns the url to perform the download of the candidates CV
	*/
	public getCurriculumDownloadUrl(curriculumId:string){
		return  environment.backendUrl + 'curriculum/'+ curriculumId;
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

		switch(lang){
			case "DUTCH":{
				return "Dutch";
			}
			case "FRENCH":{
				return "French";
			}
			case "ENGLISH":{
				return "English";
			}
			default:{
				return 'NA';
			}
		}

  	}	
	
	public hasRequiredSkill(skill:string):string {
		
		if (this.hasSkill(skill)) {
			return 'skill-match';
		}
		
		return 'skill-no-match';
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
		
		let params:SuggestionParams 	= new SuggestionParams(this.suggestionFilterForm, this.skillFilters, new Array<string>());
		let alert:CandidateSearchAlert 	= new CandidateSearchAlert();
		
		alert.alertName 			= this.createAlertForm.get(('alertName'))?.value;
		alert.countries 			= params.getCountries();
		alert.dutch 				= params.getDutchLevel();
		alert.english 				= params.getEnglishLevel();
		alert.freelance 			= params.getContract();
		alert.french 				= params.getFrenchLevel();
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
		//this.open(content, '', true);
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
	
	
	public currentSavedCandidate:SavedCandidate = new SavedCandidate();
	
	/**
	* Displays dialog to edit notes for a SavedCandidate
	*/
	public showNotesDialog(content:any, candidate:Candidate):void{
		
		this.updatedSavedCandidate = false;
		
		if (!this.isNoLongerAvailable()){
			this.currentSavedCandidate = new SavedCandidate()
		}
		
		this.savedCandidates.forEach(sc => {
			if(""+sc.candidateId === ""+candidate.candidateId) {
				this.currentSavedCandidate = sc;
			}
		});
		
		this.savedCandidateNotesForm = new UntypedFormGroup({
			notes:			new UntypedFormControl(this.currentSavedCandidate.notes)
		});
	
		this.notesDialogBox.nativeElement.showModal();
		//this.open(content, '', true);
	}
	
	public updatedSavedCandidate:boolean = false; 
	
	/**
	* Updates the current Saved Candidate
	*/
	public updateSavedCandidate():void{
		this.updatedSavedCandidate = false;
		this.currentSavedCandidate.notes = this.savedCandidateNotesForm.get('notes')?.value;
		this.candidateService.updateSavedCandidate(this.currentSavedCandidate).subscribe(response =>{
			this.candidateService.fetchSavedCandidates().subscribe(response => {
				this.savedCandidates = response;
				this.updatedSavedCandidate = true;
			});
		});
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
	
	public open(content:any, msg:string, success:boolean):void {
		
	
	   let options: NgbModalOptions = {
	    	 centered: true
	   };

		this.modalService.open(content, options);

  	}

	public publicitySuggestions:Array<Candidate>  = new Array<Candidate>();
	
	public openPublicityDialog() {
		
		this.publicitySuggestions = this.suggestions.slice();
		
		
		this.publicitySuggestions = this.publicitySuggestions.splice(0,Math.min(this.publicitySuggestions.length, 10));
		
		this.publicityDialogBox.nativeElement.showModal();
    	//this.modalService.open(content, { centered: true });
  	}

	/**
	* Returns the code identifying the country
	* @param country - Country to get the country code for
	*/
	public getCountryCode(country:string):string{

		switch(country){
			case "NETHERLANDS":{
				return "NL";
			}
			case "BELGIUM":{
				return "BE";
			}
			case "UK":{
				return "UK";
			}
			case "REPUBLIC_OF_IRELAND":{
				return "IE";
			}
		}

     	return 'NA';

  	}

	/**
	* Fetches Candidate
	*/
	public fetchCandidateProfile(candidateId:string):void{
		this.candidateProfile = new CandidateProfile();
		this.candidateService.getCandidateProfileById(candidateId).subscribe( candidate => {
				this.candidateProfile = candidate;
				this.currentView = 'suggested-canidate-overview';
			}, err => {
				if (err.status === 401 || err.status === 0) {
					sessionStorage.removeItem('isAdmin');
					sessionStorage.removeItem('isRecruter');
					sessionStorage.removeItem('isCandidate');
					sessionStorage.removeItem('loggedIn');
					sessionStorage.setItem('beforeAuthPage', 'view-candidates');
					this.router.navigate(['login-user']);
			}
			});
	}
	
	public contactCandidateView:string = 'message';
	
	/**
	*Extract the level of proficiency in a language of the Candidate
	*/
	public getLanguageLevel(language:string):string{
		if(!this.candidateProfile?.languages.filter(l => l.language == language)[0]) {
			return '';
		} else {
			return this.candidateProfile?.languages.filter(l => l.language == language)[0].level;
		}
	}
	
	/**
	*  Closes the confirm popup
	*/
	public closeModalContact(): void {
		this.modalService.dismissAll();
	}
	
	/**
	* Opend dialog to contact recuiter posting	
	*/
	public contactCandidate(contactBox:any):void{
		
		this.contactCandidateView = 'message';
		let options: NgbModalOptions = {
	    	 centered: true
	   };

		this.contactDialogBox.nativeElement.showModal();
		//this.modalService.open(contactBox, options);
	
	}
	
	/**
	* Forms group for sending a message to a Recruiter relating to a
	* specific post on the jobboard 
	*/
	public sendMessageGroup:UntypedFormGroup = new UntypedFormGroup({
		message:				new UntypedFormControl(''),
		title:				new UntypedFormControl('')
	});
	
	/**
	* Opend dialog to contact recuiter posting	
	*/
	public sendMessageToCandidate():void{
		
		let emailRequest:EmailRequest = new EmailRequest();
		
		emailRequest.title = this.sendMessageGroup.get('title')?.value;;
		emailRequest.message = this.sendMessageGroup.get('message')?.value;;
		
		this.emailService.sendCandidateContactEmail(emailRequest,this.candidateProfile.candidateId).subscribe(body => {
			this.contactCandidateView = 'success';
			this.sendMessageGroup = new UntypedFormGroup({
				message: new UntypedFormControl(''),
				title: new UntypedFormControl(''),
			});
		}, err => {
			this.contactCandidateView = 'failure';
		});
		
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
			return "Rate: " + this.candidateProfile.rateContract.currency + " "
			+ this.candidateProfile.rateContract.valueMin 
			+ " to " + this.candidateProfile.rateContract.valueMax 
			+ " per " + this.candidateProfile.rateContract.period.toLowerCase() ;
		}
		
		if(this.candidateProfile.rateContract.valueMin == 0 && this.candidateProfile.rateContract.valueMax != 0){
			return "Rate: " + this.candidateProfile.rateContract.currency 
			+ " " + this.candidateProfile.rateContract.valueMax 
			+ " per " + this.candidateProfile.rateContract.period.toLowerCase() ;
		}
		
		if(this.candidateProfile.rateContract.valueMin != 0 && this.candidateProfile.rateContract.valueMax == 0){
			return "Rate: " + this.candidateProfile.rateContract.currency 
			+ " " + this.candidateProfile.rateContract.valueMin 
			+ " per " + this.candidateProfile.rateContract.period.toLowerCase() ;
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
			return "Rate: " + this.candidateProfile.ratePerm.currency + " "
			+ this.candidateProfile.ratePerm.valueMin 
			+ " to " + this.candidateProfile.ratePerm.valueMax 
			+ " per " + this.candidateProfile.ratePerm.period.toLowerCase() ;
		}
		
		if(this.candidateProfile.ratePerm.valueMin == 0 && this.candidateProfile.ratePerm.valueMax != 0){
			return "Rate: " + this.candidateProfile.ratePerm.currency 
			+ " " + this.candidateProfile.ratePerm.valueMax 
			+ " per " + this.candidateProfile.ratePerm.period.toLowerCase() ;
		}
		
		if(this.candidateProfile.ratePerm.valueMin != 0 && this.candidateProfile.ratePerm.valueMax == 0){
			return "Rate: " + this.candidateProfile.ratePerm.currency 
			+ " " + this.candidateProfile.ratePerm.valueMin 
			+ " per " + this.candidateProfile.ratePerm.period.toLowerCase() ;
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
	* Returns skills that the Canidate has that match the 
	* required skills
	*/
	public skillsFiltersMatch():Array<string>{
		return this.skillFilters.filter(s => this.hasSkill(s) && s != '').sort();
	}
	
	/**
	* Returns skills that the Canidate does not has that are 
	* required skills
	*/
	public skillsFiltersNoMatch():Array<string>{
		return this.skillFilters.filter(s => !this.hasSkill(s) && s != '').sort();
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
			case 'ZERO': 	return "Fully Remote";
			case 'ONE': 	return "1";
			case 'TWO': 	return "2";
			case 'THREE': 	return "3";
			case 'FOUR': 	return "4";
			case 'FIVE':	return "5";
			default: 		return "";
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
}