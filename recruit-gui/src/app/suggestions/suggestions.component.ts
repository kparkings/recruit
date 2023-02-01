import { Component, OnInit, SecurityContext } 		from '@angular/core';
import { UntypedFormGroup, UntypedFormControl,FormGroup, FormControl }		from '@angular/forms';
import { CandidateServiceService }					from '../candidate-service.service';
import { SuggestionsService }						from '../suggestions.service';
import { CurriculumService }						from '../curriculum.service';
import { Candidate}									from './candidate';
import { SuggestionParams}							from './suggestion-param-generator';
import { environment }								from '../../environments/environment';
import { Clipboard } 								from '@angular/cdk/clipboard';
import { NgbModal, NgbModalOptions }				from '@ng-bootstrap/ng-bootstrap';
import { ViewChild }								from '@angular/core';
import { CandidateSearchAlert }						from './candidate-search-alert';
import { CandidateFunction }						from '../candidate-function';
import { DomSanitizer, SafeResourceUrl, SafeUrl } 	from '@angular/platform-browser';
import { DeviceDetectorService } 					from 'ngx-device-detector';
import { Router}									from '@angular/router';

/**
* Component to suggest suitable Candidates based upon a 
* Recruiters search
*/
@Component({
  selector: 'app-suggestions',
  templateUrl: './suggestions.component.html',
  styleUrls: ['./suggestions.component.css']
})
export class SuggestionsComponent implements OnInit {

	public createAlertForm:UntypedFormGroup = new UntypedFormGroup({
		alertName:			new UntypedFormControl(''),
	});

	private jobSpecFile!:File;
  
  	public setJobSepecFile(event:any):void{
  
  		if (event.target.files.length <= 0) {
  			return;
  		}
  	
  		this.jobSpecFile = event.target.files[0];
  		
  	}

	/**
 	* Extracts filters from job specification file
	*/	

  	public extractFiltersFromJobSpec():void{
  		
  		this.candidateService.extractFiltersFromDocument(this.jobSpecFile).subscribe(extractedFilters=>{
  				
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
		skill: 					new UntypedFormControl(''),
	});
	
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
	
	/**
	* Constructor
	* @param candidateService - Services relating to Candidates
	*/
	constructor(public candidateService:CandidateServiceService, 
				public suggestionsService:	SuggestionsService, 
				private clipboard: 			Clipboard, 
				private modalService: 		NgbModal, 
				private sanitizer: 			DomSanitizer,
				private curriculumService: 	CurriculumService,
				private deviceDetector: 	DeviceDetectorService,
				private router:				Router) { 
					
		this.getSuggestions();	
	 	this.trustedResourceUrl = this.sanitizer.bypassSecurityTrustResourceUrl('');
		this.isMobile = deviceDetector.isMobile();
	}
	
	public showCVInline(candidateId:string):void{
		
		let url = this.curriculumService.getCurriculumUrlForInlinePdf(candidateId); 'http://127.0.0.1:8080/curriculum-test/';
		
		
		this.trustedResourceUrl = this.sanitizer.bypassSecurityTrustResourceUrl(url);
		
	}
	
	/**
	* Initializes Component`
	*/
	ngOnInit(): void {
		this.suggestionFilterForm.valueChanges.subscribe(value => {
			this.getSuggestions();	
		});
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
		
		const maxSuggestions:number 		= 56;
		
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
											sessionStorage.removeItem('loggedIn');
											sessionStorage.setItem('beforeAuthPage', 'view-candidates');
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
		
		let skillFormatted:string 	= this.suggestionFilterForm.get('skill')?.value.trim();
		skillFormatted 				= skillFormatted.toLocaleLowerCase();
		
		if (skillFormatted.length > 0 && this.skillFilters.indexOf(skillFormatted) == -1) {
			this.skillFilters.push(skillFormatted);	
		}
		
		this.getSuggestions();
		
		this.suggestionFilterForm.get('skill')?.setValue('');
		
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
	* Shows the Suggesion result view
	*/
	public showSuggestionsResults():void{
		this.currentView = 'suggestion-results';
		this.suggestedCandidate = new Candidate();
	}

	/**
	* Shows the inline CV view
	*/
	public showInlineCVView():void{
		this.currentView = 'inline-cv';
		this.showCVInline(this.suggestedCandidate.candidateId);
	}

	/**
	* Shows the Suggesion result view
	*/
	public showSuggestedCandidateOverview(candidateSuggestion:Candidate):void{
		this.currentView = 'suggested-canidate-overview';
		this.suggestedCandidate = candidateSuggestion;
	}
	
	/**
	* Flags a Candidate as being potentially unavailable
	*/
	public markCandidateAsUnavailable():void {
		this.candidateService.markCandidateAsUnavailable(this.suggestedCandidate.candidateId).subscribe(data => {});
		this.suggestedCandidate.flaggedAsUnavailable = true;
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
	
	public contractType():string{
		
		if (this.suggestedCandidate.freelance === 'TRUE' && this.suggestedCandidate.perm === 'TRUE') {
			return'Contract / Pern ';
		}
		
		if (this.suggestedCandidate.perm === 'TRUE') {
			return'Pern ';
		}
		
		if (this.suggestedCandidate.freelance === 'TRUE') {
			return'Contract ';
		}
		
		return '';
		
		
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
		
		let formattedSkill:string = skill.trim().toLowerCase();
		
		let match:boolean = false;
		this.suggestedCandidate.skills.forEach(skill => {
			if (skill === formattedSkill) {
				 match = true;
			}
		})
		
		if (match) {
			return 'skill-match';
		}
		
		return 'skill-no-match';
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
		
		alert.functions			 	= this.suggestionsService.getFunctionTypeFromTitleText(params.getTitle());
		alert.alertName 			= this.createAlertForm.get(('alertName'))?.value;
		alert.countries 			= params.getCountries();
		alert.dutch 				= params.getDutchLevel();
		alert.english 				= params.getEnglishLevel();
		alert.freelance 			= params.getContract();
		alert.french 				= params.getFrenchLevel();
		alert.perm 					= params.getPerm();
		alert.skills 				= params.getSkills();
		
		alert.yearsExperienceLtEq 	= params.getMinExperience();
		alert.yearsExperienceGtEq 	= params.getMaxExperience();
		
		this.suggestionsService.extractSkillsFromSearchPhrase(params.getTitle()).forEach(s => {
			alert.skills.push(s);
		})
		
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
	public showCreateAlertDialog(content:any):void{
		
		this.showSaveAlertBox 			= true;
		this.showSaveAlertBoxSuccess 	= false;
		this.showSaveAlertBoxFailure 	= false;
		
		this.open(content, '', true);
	}
	
	/**
	* Displays dialog to create an alert for the current search critera
	*/
	public showFilterByJobSpecDialog(content:any):void{
		
		this.showFilterByJonSpecFailure  	= false;
		this.showFilterByJobSpec 			= true;
	
		this.open(content, '', true);
			
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

}