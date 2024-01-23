import { Component, OnInit, ViewChild }					from '@angular/core';
import { UntypedFormGroup }								from '@angular/forms';
import { ListingService }								from '../listing.service';
import { RecruiterService }								from '../recruiter.service';
import { Listing}										from './listing';
import { Candidate}										from './candidate';
import { CandidateServiceService }						from '../candidate-service.service';
import { SuggestionsService }							from '../suggestions.service';
import { Router}										from '@angular/router';
import { DeviceDetectorService } 						from 'ngx-device-detector';
import { RecruiterProfileService} 						from '../recruiter-profile.service';
import { RecruiterProfile }								from '../recruiter-profile/recruiter-profile';
import { RecruiterMarketplaceService }					from '../recruiter-marketplace.service';
import { CreditsService } 								from '../credits.service';
import { ExtractedFilters } 							from '../suggestions/extracted-filters';
import { FormBeanNewListing } 							from '../listing/form-bean-new-listing';
import { FormBeanMarketPlace } 							from '../listing/form-bean-market-place';
import { FormBeanFilterByJobSpec } 						from '../listing/form-bean-filter-by-jobspec';
import { HtmlOption } 									from '../html-option';

@Component({
  selector: 'app-recruiter-listings',
  templateUrl: './recruiter-listings.component.html',
  styleUrls: ['./recruiter-listings.component.css']
})
export class RecruiterListingsComponent implements OnInit {

	@ViewChild('feedbackBox', { static: false }) 			private feedbackBox:any;
	@ViewChild('specUploadBox', { static: false }) 			private specUploadBox:any;
	@ViewChild('publicityBox', { static: false }) 			private publicityBox:any;
	@ViewChild('marketplaceBox', { static: false }) 		private marketplaceBox:any;
	
	public isMobile:boolean 							= false;
	public recruiterProfile:RecruiterProfile 			= new RecruiterProfile();
	public showFilterByJonSpecFailure:boolean  			= false;
	public showFilterByJobSpec:boolean 					= false;
	public jobSpecUploadView:string 					= "chooseType"; //chooseType | doc | text
	public skills:Array<string> 						= new Array<string>();
	public listings:Array<Listing>						= new Array<Listing>();
	public activeView:string							= 'list';
	public activeSubView:string							= 'none';
	public selectedListing:Listing						= new Listing();
	public feedbackBoxClass:string          			= '';
  	public feedbackBoxTitle                 			= '';
  	public feedbackBoxText:string           			= '';
	public validationErrors:Array<string>				= new Array<string>();
	public enabldeDeleteOption:boolean					= false;
	public passedCreditCheck:boolean = false;
	
	public totalPages:number							= 0;
  	public currentPage:number							= 0;
	public yearsExperienceValues:Array<number>			= new Array<number>();		
	public showSuggestedCandidate:boolean 				= false;
	public suggestedCandidate:Candidate			 		= new Candidate();
	public listingForSuggestedCandidate:Listing 		= new Listing();	
	
	private	pageSize:number								= 8;
  	private jobSpecFile!:File;
	private  recruiterId:string							= '';
	private  recruiterFirstName:string 					= '';
	private  recruiterSurname:string					= '';
	private  recruiterEmail:string 						= '';
	private  recruiterCompany:string					= '';
	
	/**
	* Constructor 
	*/
  	constructor(private listingService:				ListingService, 
				private recruiterService:			RecruiterService, 
				public 	candidateService:			CandidateServiceService,
				public 	suggestionsService:			SuggestionsService,
				private deviceDetector:				DeviceDetectorService,
				private recruiterProfileService: 	RecruiterProfileService,
				public 	router:						Router,
				private marketplaceService: 		RecruiterMarketplaceService,
				private creditsService:				CreditsService) {
					
				this.isMobile = deviceDetector.isMobile();
				
				this.recruiterProfileService.fetchOwnRecruiterProfile().subscribe(rec => {
					this.recruiterProfile = rec;
				});
				
				this.doCreditCheck();
					
	}
	
	/**
	* On Init 
	*/
	ngOnInit(): void {
	
		this.recruiterService.getOwnRecruiterAccount().subscribe(data => {
			this.recruiterId 					= data.userId;
			this.recruiterFirstName				= data.firstName;
			this.recruiterSurname				= data.surname;
			this.recruiterEmail					= data.email;
			this.recruiterCompany				= data.companyName;
			this.fetchListings();			
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
	
		this.loadYearsExperienceValues();
	
  	}
	
	/**
	* Forms 
	*/
	public filterByJobSpecForm = FormBeanFilterByJobSpec.getInstance();
	public newListingFormBean:UntypedFormGroup 	= FormBeanNewListing.getInstance();
	public mpFormBean:UntypedFormGroup 			= FormBeanMarketPlace.getInstance();
	
	/**
	* Resets the page 
	*/
	public reset():void{
		this.newListingFormBean 	= FormBeanNewListing.getInstance();
		this.mpFormBean 			= FormBeanMarketPlace.getInstance();
		this.filterByJobSpecForm 	= FormBeanFilterByJobSpec.getInstance();
		
		this.skills 				= new Array<string>();
		this.validationErrors 		= new Array<string>();
		this.enabldeDeleteOption	= false;
		this.doCreditCheck();
		
	}
	
  	/**
	* Switches between options on how to upload job spec 
	* to use to perform filtering
	*/
	public switchJobSpecUpldOpt(type:string):void{
		this.jobSpecUploadView = type;
	}
	
  	public setJobSepecFile(event:any):void{
  
  		if (event.target.files.length <= 0) {
  			return;
  		}
  	
  		this.jobSpecFile = event.target.files[0];
  		
  	}
  	
  	public extractFiltersFromJobSpecText():void{
		let jobSpecText = this.filterByJobSpecForm.get('specAsText')?.value; 
		this.candidateService.extractFiltersFromText(jobSpecText).subscribe(extractedFilters=>{
			this.processJobSpecExtratedFilters(extractedFilters);
		},(failure =>{
			this.showFilterByJonSpecFailure 	= true;
			this.showFilterByJobSpec 			= false;
			
		}));
	}
	
	private processJobSpecExtratedFilters(extractedFilters:ExtractedFilters):void{
		
		this.skills = extractedFilters.skills;
			
			let freelance 	= extractedFilters.freelance 	== 'TRUE' ? true : false;
			let perm 		= extractedFilters.perm 		== 'TRUE' ? true : false;
			
			let netherlands = extractedFilters.netherlands;
			let uk 			= extractedFilters.uk;
			let belgium 	= extractedFilters.belgium;
			let ireland 	= extractedFilters.ireland;
			
			let country = netherlands ? "NETHERLANDS" : uk ? "UK" : belgium ? "BELGIUM" : ireland ? "IRELAND" : "";
			
			let type = (freelance && perm) ? "BOTH" : freelance ? "CONTRACT_ROLE" : "PERM_ROLE";
			
			this.newListingFormBean.get("title")?.setValue(extractedFilters.jobTitle);
			this.newListingFormBean.get("type")?.setValue(type);
			this.newListingFormBean.get("experienceYears")?.setValue(extractedFilters.experienceGTE);
			this.newListingFormBean.get("country")?.setValue(country);
			
			this.newListingFormBean.get("langDutch")?.setValue(extractedFilters.dutch);
			this.newListingFormBean.get("langEnglish")?.setValue(extractedFilters.english);
			this.newListingFormBean.get("langFrench")?.setValue(extractedFilters.french);
			
			this.newListingFormBean.get("description")?.setValue(extractedFilters.extractedText);
			
			this.closeModal();
			
	}

	/**
 	* Extracts filters from job specification file
	*/	
  	public extractFiltersFromJobSpec():void{
  		
  		this.candidateService.extractFiltersFromDocument(this.jobSpecFile).subscribe(extractedFilters=>{
  			
			this.processJobSpecExtratedFilters(extractedFilters);
		
		},(() =>{
			this.showFilterByJonSpecFailure 	= true;
			this.showFilterByJobSpec 			= false;		
		}));
  		
  	}

	/**
	* Displays dialog to create an alert for the current search critera
	*/
	public showFilterByJobSpecDialog():void{
		
		this.showFilterByJonSpecFailure  	= false;
		this.showFilterByJobSpec 			= true;
		this.jobSpecUploadView 				= 'chooseType';
		
		this.filterByJobSpecForm = FormBeanFilterByJobSpec.getInstance();
		
		this.specUploadBox.nativeElement.showModal();
	}
	
	/**
	* Opens the specified Dialog box
	*/
	public open(content:any, msg:string, success:boolean):void {
    
	    if (success) {
	      //Currently not used
	    } else {
	      this.feedbackBoxTitle = 'Validation errors';
	      this.feedbackBoxClass = 'feedback-failure';
	    }
	
	   	this.feedbackBox.nativeElement.showModal();
  	}
	
	/**
	*  Closes the confirm popup
	*/
	public closeModal(): void {
		this.feedbackBox.nativeElement.close();
		this.publicityBox.nativeElement.close();
		this.specUploadBox.nativeElement.close();
		this.marketplaceBox.nativeElement.close();
		this.validationErrors = new Array<string>();
	}

	
	/**
	* Adds the current skill in the newListingFormBean 
	* to the list of skills required for the listing
	*/
	public addSkill():void {
		
		let skill:string = this.newListingFormBean.get('skill')?.value;
		
		skill = skill.trim();
		skill = skill.toLocaleLowerCase();
		
		if (!this.skills.includes(skill) && skill != '') {
			this.skills.push(skill.trim());	
			this.newListingFormBean.get('skill')?.setValue('');
		}
					
	}
	
	/**
	* Removes the selected skill from the 
	* list of skills required for the listing
	*/
	public removeSkill(skill:any):void{
		
		skill = skill.trim();
		skill = skill.toLocaleLowerCase();
		
		this.skills = this.skills.filter(s => s  !== skill);
	} 
	
	/**
	* Navigates to the specified step
	*/
	public updateSubviewStep(step:string):void{
		this.activeSubView = step;
	}
	
	/**
	* Switches to Add Listing view
	*/
	public showAdd():void{
		
		this.reset();
		
		this.activeView 			= 'add';
		this.activeSubView 			= 'step1';
		this.selectedListing		= new Listing();
		this.enabldeDeleteOption 	= false;
		
		/**	
		* Default values
		*/
		this.newListingFormBean.get("contactName")?.setValue(this.recruiterFirstName + ' ' + this.recruiterSurname);
		this.newListingFormBean.get("contactEmail")?.setValue(this.recruiterEmail);
		this.newListingFormBean.get("contactCompany")?.setValue(this.recruiterCompany);
			
	}
	
	/**
	* Switches to Add Listing view
	*/
	public showList():void{
		this.activeView 			= 'list';
		this.activeSubView 			= 'none';
		this.selectedListing		= new Listing();
		this.enabldeDeleteOption 	= false;
	}
	
	/**
	* Switches to Show Listing view
	*/
	public showListingDetails(selectedListing?:Listing):void{
		
		this.activeView 			= 'show';
		this.activeSubView 			= 'none';
		this.enabldeDeleteOption 	= false;
		
		if (selectedListing) {
			this.selectedListing	= selectedListing;	
		}
		
	}
		
	/**
	* Switches to Edit Listing view
	*/
	public showEditListing():void{
		
		this.activeView 			= 'edit';
		this.activeSubView 			= 'step1';
		this.enabldeDeleteOption 	= false;
		
		/**	
		* Default values
		*/
		this.newListingFormBean.get("contactName")?.setValue(this.selectedListing.ownerName);
		this.newListingFormBean.get("contactEmail")?.setValue(this.selectedListing.ownerEmail);
		this.newListingFormBean.get("contactCompany")?.setValue(this.selectedListing.ownerCompany);
		
		this.newListingFormBean.get("title")?.setValue(this.selectedListing.title);
		this.newListingFormBean.get("type")?.setValue(this.selectedListing.type);
       	this.newListingFormBean.get("country")?.setValue(this.selectedListing.country);
		this.newListingFormBean.get("location")?.setValue(this.selectedListing.location);	
		this.newListingFormBean.get("experienceYears")?.setValue(this.selectedListing.yearsExperience);
		this.newListingFormBean.get("rate")?.setValue(this.selectedListing.rate);
		this.newListingFormBean.get("rateCurrency")?.setValue(this.selectedListing.currency);
		this.newListingFormBean.get("description")?.setValue(this.selectedListing.description);
		
		this.newListingFormBean.get("langDutch")?.setValue(this.selectedListing.languages.includes('DUTCH'));
		this.newListingFormBean.get("langEnglish")?.setValue(this.selectedListing.languages.includes('ENGLISH'));
		this.newListingFormBean.get("langFrench")?.setValue(this.selectedListing.languages.includes('FRENCH'));
		
		this.skills = this.selectedListing.skills;

		this.newListingFormBean.get("skill")?.setValue('');
	
	}
	
	/**
	* Switches to Previous Show option
	*/
	public showPreviousShowOption():void{
		
		if (this.activeView === 'add') {
			this.showList();
		}
		
		if (this.activeView === 'edit') {
			this.showListingDetails();
		}
		
	}
		
	/**
	* Creates a new Listing. 
	*/
	public publishListing():void{
		
		let ownerName:string 			= this.newListingFormBean.get('contactName')?.value; 
		let ownerCompany:string 		= this.newListingFormBean.get('contactCompany')?.value;
		let ownerEmail:string 			= this.newListingFormBean.get('contactEmail')?.value;
		let title:string 				= this.newListingFormBean.get('title')?.value;
		let description:string 			= this.newListingFormBean.get('description')?.value;
		let type:string 				= this.newListingFormBean.get('type')?.value;
		let country:string 				= this.newListingFormBean.get('country')?.value;	
		let location:string 			= this.newListingFormBean.get('location')?.value;	
		let yearsExperience:number 		= this.newListingFormBean.get('experienceYears')?.value;
		let rate:number 				= this.newListingFormBean.get('rate')?.value; 			
		let currency:string 			= this.newListingFormBean.get('rateCurrency')?.value; 		
		
		let langDutch:boolean 			= this.newListingFormBean.get('langDutch')?.value;
		let langEnglish:boolean 		= this.newListingFormBean.get('langEnglish')?.value;
		let langFrench:boolean 			= this.newListingFormBean.get('langFrench')?.value;
		
		let languages:Array<string> = new Array<string>();
		
		if (langDutch === true) {
			languages.push('DUTCH');
		}
		
		if (langEnglish === true) {
			languages.push('ENGLISH');
		}
		
		if (langFrench === true) {
			languages.push('FRENCH');
		}
		
		this.validationErrors	= new Array<string>();
		
		if (this.selectedListing.listingId === '') {
			
			this.listingService
				.registerListing(ownerName, 
								ownerCompany,
								ownerEmail,
								title,
								description,
								type,	
								country,	
								location,	
								yearsExperience,
								languages,
								this.skills,	
								rate, 			
								currency, 		
								false).subscribe( data => {
									this.showMPBox();
								}, err => {
									
									if(err.status === 400) {
										
										let failedFields:Array<any> = err.error;
										
										if (err.error.error == 'Bad Request') {
											this.validationErrors.push("Country must be provided");
										}
										
										if (typeof err.error[Symbol.iterator] === 'function') {
											failedFields.forEach(failedField => {
												this.validationErrors.push(failedField.fieldMessageOrKey);
											});
											this.open('feedbackBox', "Failure",  false);
										} else {
											
											if (err.error.error == 'Bad Request'){
												this.open('feedbackBox', "Failure",  false);	
											}else {
												console.log("Failed to Update Listing " + JSON.stringify(err.error));
											}
										}
										
									}
										
								});
								
		} else {
			
			this.listingService
				.updateListing( this.selectedListing.listingId,
								ownerName, 
								ownerCompany,
								ownerEmail,
								title,
								description,
								type,	
								country,	
								location,	
								yearsExperience,
								languages,
								this.skills,	
								rate, 			
								currency, 		
								false).subscribe( data => {
									this.reset();
									this.fetchListings();
									this.showList();
								}, err => {
									
									if(err.status === 400) {
										
										let failedFields:Array<any> = err.error;
										
										if (typeof err.error[Symbol.iterator] === 'function') {
											failedFields.forEach(failedField => {
												this.validationErrors.push(failedField.fieldMessageOrKey);
											});
											this.open('feedbackBox', "Failure",  false);
										} else {
											console.log("Failed to Update Listing " + JSON.stringify(err.error));
										}
										
									}
										
								});
		}
	
	}
	
	/**
	* Retrieves listings belonging to the Recruiter
	*/
	public fetchListings():void{
	
		this.listingService
			.fetchRecruiterListings(this.recruiterId, 'created',"desc", this.currentPage, this.pageSize)
				.subscribe(data => {
					this.totalPages = data.totalPages;
					this.listings 	= data.content;
				}, 
				err => {
					console.log("Error retrieving listings" + JSON.stringify(err));			
				});
		
	}
	
	/**
	* Whether or not the previous page button 
	* should be active
	* */
	public showNavPrev(): boolean{
    
		if (this.totalPages === -1) {
      		return false;
    	}

    	return this.currentPage > 0;

  	}

	/**
	* Whether or not the next page 
	* button should be active
	*/
	public showNavNext():boolean{
    
    	if (this.totalPages === 0) {
      		return false;
    	}

    	return this.currentPage < (this.totalPages -1);

	}
	
	/**
	* Fetches and displays the next page 
	* of candidtes
	*/
	public nextPage(): void{

    	if ((this.currentPage + 1) < this.totalPages) {
      		this.currentPage = this.currentPage + 1;
      		this.fetchListings();
    	}
    
	}

	/**
	* Fetches and displays the previous page of Candidates
	*/
	public previousPage(): void{
    
		if ((this.currentPage) > 0) {
      		this.currentPage = this.currentPage - 1;
      		this.fetchListings();
    	}
  	}

	/**
	* Shows the confirm button to confirm deletion of the 
	* currently selected Listing
	*/
	public showConfirmDelete():void {
		this.enabldeDeleteOption = true;
	}
	
	/**
	* Calls the service to delete the selected Listing
	*/
	public performmDelete():void {
		this.enabldeDeleteOption = false;
		//call service to perform delete
		this.listingService.deleteRecruiterListing(this.selectedListing.listingId).subscribe(data => {
				this.reset();
				this.fetchListings();
				this.showList();
		},err => {
			console.log('Unable to delete Lisring ' + JSON.stringify(err));
		});
	}

	/**
	* Returns the code identifying the country
	* @param country - Country to get the country code for
	*/
	public getCountryCode(country:string):string{

		const matchingCountry = this.listingService.fetchCountries().filter(countryObj => countryObj.key == country)[0];
		
		return matchingCountry == null ? "NA" : matchingCountry.humanReadable;
	
  	}
  	
  	/**
	* Provides HTML Options for country 
	* Selection 
	*/
  	public getCountryOptions():Array<HtmlOption>{
		  
		  let countries:Array<HtmlOption> = new Array<HtmlOption>();
		  
		  countries.push(new HtmlOption("",""));
		  this.listingService.fetchCountries().forEach( country => {
			countries.push(new HtmlOption(country.key,country.humanReadable));  
		  });
		  		  
		  return countries;
	}
	
	/**
	* Provides HTML Options for country 
	* Selection 
	*/
  	public getContractTypeOptions():Array<HtmlOption>{
		  
		let types:Array<HtmlOption> = new Array<HtmlOption>();
		  
		types.push(new HtmlOption("",""));
		
		this.listingService.fetchContractTypes().forEach(contractType => {
			types.push(new HtmlOption(contractType.contractType,contractType.humanReadable));
		});
		  		  
		return types;
	}

	/**
	* Returns the code identifying the country
	* @param country - Country to get the country code for
	*/
	public getContractType(type:string):string{

		const matchingType = this.listingService.fetchContractTypes().filter(contractTypeObj => contractTypeObj.contractType == type)[0];
		
		return matchingType == null ? "NA" : matchingType.humanReadable;
		
  	}	

	/**
	* Returns the Humand readable version of the Language
	* @param country - Language to get the readable version for
	*/
	public getLanguage(lang:string):string{

		const matchingType = this.listingService.fetchLanguageTypes().filter(langTypeObj => langTypeObj.value == lang)[0];
		
		return matchingType == null ? "NA" : matchingType.humanReadable;
		
  	}	

	/**
	* Provides options for number of years of experience
	*/
	private loadYearsExperienceValues(){
		for (let count=0; count <= 100; count++) {
			this.yearsExperienceValues.push(count);
		}
	}

	/**
	* Shows list of candidates similar to job listing
	*/
	public showSuggestedCandidateListView():void{
		this.showSuggestedCandidate = false;
		this.suggestedCandidate = new Candidate();
	}
	
	/**
	* Shows Candidate details
	*/
	public showSuggestedCandidateView(candidate:Candidate):void{
		
		this.showSuggestedCandidate = true;
		this.suggestedCandidate 	= candidate;
	}
	
	/**
	* Flags a Candidate as being potentially unavailable
	*/
	public markCandidateAsUnavailable():void {
		this.candidateService.markCandidateAsUnavailable(this.suggestedCandidate.candidateId).subscribe(data => {});
		this.suggestedCandidate.flaggedAsUnavailable = true;
	}
	
	/**
	* Shows a popup with the details of the listing to 
	* post on other websites
	*/
	public showPublicity():void{
		this.publicityBox.nativeElement.showModal();
	}
	
	/**
	* Persists and Published the OfferedCandidate	
	*/
	public publishOpenPosition():void{

		let title:string 				= this.newListingFormBean.get('title')?.value;
		let description:string 			= this.newListingFormBean.get('description')?.value;
		let type:string 				= this.newListingFormBean.get('type')?.value;
		let country:string 				= this.newListingFormBean.get('country')?.value;	
		let location:string 			= this.newListingFormBean.get('location')?.value;	
		let rate:number 				= this.newListingFormBean.get('rate')?.value; 			
		let currency:string 			= this.newListingFormBean.get('rateCurrency')?.value; 		
		
		let langDutch:boolean 			= this.newListingFormBean.get('langDutch')?.value;
		let langEnglish:boolean 		= this.newListingFormBean.get('langEnglish')?.value;
		let langFrench:boolean 			= this.newListingFormBean.get('langFrench')?.value;
		
		let languages:Array<string> = new Array<string>();
		
		
		let startDate:Date 				= this.mpFormBean.get('startDate')?.value;
		let lastSubmissionDate:Date 	= this.mpFormBean.get('lastSubmissionDate')?.value;
		let comments:string 			= this.mpFormBean.get('comments')?.value;
		
		if (langDutch === true) {
			languages.push('DUTCH');
		}
		
		if (langEnglish === true) {
			languages.push('ENGLISH');
		}
		
		if (langFrench === true) {
			languages.push('FRENCH');
		}
		
		type = type == "CONTRACT_ROLE" ? "CONTRACT" : type == "PERM_ROLE" ? "PERM" : "BOTH";
		
		this.validationErrors			= new Array<string>();
		
			this.marketplaceService.registerOpenPosition(
				title,
				country,
				location,
				type,
				(currency == null ? "" : currency) + " " + (rate == null ? "" : rate) ,
				startDate,
				lastSubmissionDate,
				description,
				comments,
				languages,
				this.skills
			).subscribe( data => {
				this.reset();
				this.fetchListings();
				this.showList();
				this.closeModal();
			}, err => {
		
				if (err.status === 400) {
											
					let failedFields:Array<any> = err.error;
											
					if (typeof err.error[Symbol.iterator] === 'function') {
						failedFields.forEach(failedField => {
							this.validationErrors.push(failedField.issue);
						});
						this.open('feedbackBox', "Failure",  false);
					} else {
						console.log("Failed to persist new Open Position " + JSON.stringify(err.error));
					}
				}
											
			});
	
	}
	
	public showMPBox():void{
		this.marketplaceBox.nativeElement.showModal();
	}
	
	public declineMarketplace():void{
		this.reset();
		this.fetchListings();
		this.showList();
		this.closeModal();
	}
	
	public doCreditCheck():void{
		this.listingService.doCreditCheck().subscribe(passed => {
			this.passedCreditCheck = passed;
		});
	}
	
	public showNoCredits():void{
		this.creditsService.tokensExhaused();
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
	
}