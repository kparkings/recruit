import { Component, OnInit, ViewChild }					from '@angular/core';
import { UntypedFormGroup }								from '@angular/forms';
import { ListingService }								from '../listing.service';
import { RecruiterService }								from '../recruiter.service';
import { Listing}										from './listing';
import { Candidate}										from './candidate';
import { CandidateServiceService }						from '../candidate-service.service';
import { SuggestionsService }							from '../suggestions.service';
import { Router}										from '@angular/router';
import { RecruiterProfileService} 						from '../recruiter-profile.service';
import { RecruiterProfile }								from '../recruiter-profile/recruiter-profile';
import { RecruiterMarketplaceService }					from '../recruiter-marketplace.service';
import { CreditsService } 								from '../credits.service';
import { ExtractedFilters } 							from '../suggestions/extracted-filters';
import { FormBeanNewListing } 							from '../listing/form-bean-new-listing';
import { FormBeanMarketPlace } 							from '../listing/form-bean-market-place';
import { FormBeanFilterByJobSpec } 						from '../listing/form-bean-filter-by-jobspec';
import { HtmlOption } 									from '../html-option';
import { StaticDataService } 							from '../static-data.service';
import { InfoItemBlock, InfoItemConfig, InfoItemRowKeyValue, InfoItemRowKeyValueFlag, InfoItemRowMultiValues, InfoItemRowSingleValue } from '../candidate-info-box/info-item';
import { TranslateService } 							from '@ngx-translate/core';
import { Country } 										from '../shared-domain-object/country';
import { UntypedFormControl } 							from "@angular/forms";


@Component({
    selector: 'app-recruiter-listings',
    templateUrl: './recruiter-listings.component.html',
    styleUrls: ['./recruiter-listings.component.css'],
    standalone: false
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
	//public activeSubView:string							= 'none';
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
	
	public infoItemConfig:InfoItemConfig 				= new InfoItemConfig();
	public contractTypeOptions:Array<HtmlOption> 		= new Array<HtmlOption>();
	public countryOptions:Array<HtmlOption> 			= new Array<HtmlOption>();
	public languages:Array<string> 						= new Array<string>();
  	
	private	pageSize:number								= 1000;
  	private jobSpecFile!:File;
	private  recruiterId:string							= '';
	private  recruiterFirstName:string 					= '';
	private  recruiterSurname:string					= '';
	private  recruiterEmail:string 						= '';
	private  recruiterCompany:string					= '';
	
	public currencies:Array<string> 		  = new Array<string>();
	
	/**
	* Forms 
	*/
	public filterByJobSpecForm 					= FormBeanFilterByJobSpec.getInstance();
	public newListingFormBean:UntypedFormGroup 	= FormBeanNewListing.getInstance();
	public mpFormBean:UntypedFormGroup 			= FormBeanMarketPlace.getInstance();
	
	/**
	* Constructor 
	*/
  	constructor(private listingService:				ListingService, 
				private recruiterService:			RecruiterService, 
				public 	candidateService:			CandidateServiceService,
				public 	suggestionsService:			SuggestionsService,
				private recruiterProfileService: 	RecruiterProfileService,
				public 	router:						Router,
				private marketplaceService: 		RecruiterMarketplaceService,
				private creditsService:				CreditsService,
				private staticDataService:			StaticDataService,
				private translate:					TranslateService) {
				
				this.recruiterProfileService.fetchOwnRecruiterProfile().subscribe(rec => {
					this.recruiterProfile = rec;
				});
				
				this.contractTypeOptions 	= this.getContractTypeOptions();
				this.countryOptions 		= this.getCountryOptions();
				this.getLanguageOptions();
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
			//this.getLanguageOptions();	
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
		
		this.currencies.push("EUR");
		this.currencies.push("GBP");
		this.currencies.push("BGN");
		this.currencies.push("CAD");
		this.currencies.push("CHF");
		this.currencies.push("CZK");
		this.currencies.push("DKK");
		this.currencies.push("HUF");
		this.currencies.push("INR");
		this.currencies.push("PLN");
		this.currencies.push("PKR");
		this.currencies.push("RON");	
		this.currencies.push("RUB");	
		this.currencies.push("SEK");	
		this.currencies.push("TRY");	
		this.currencies.push("UAH");	
		this.currencies.push("USD");
	
		this.loadYearsExperienceValues();
		
  	}
	
	ngAfterViewInit():void{
		/**
		* If arriving from quickactions go straight to add Role 
		*/
		if (localStorage.getItem("quick-action-activated")) {
			localStorage.removeItem("quick-action-activated");
			this.showFilterByJobSpecDialog();
		}
	}
	
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
		this.getLanguageOptions();
		
		this.languages.forEach(lang => {
			this.newListingFormBean.addControl(lang, new UntypedFormControl(''));	
		})
		
		
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
			let country 	= extractedFilters.countries.length > 0 ? extractedFilters.countries[0] : "";
			let type 		= (freelance && perm) ? "BOTH" : freelance ? "CONTRACT_ROLE" : "PERM_ROLE";
			
			this.newListingFormBean.get("title")?.setValue(extractedFilters.jobTitle);
			this.newListingFormBean.get("type")?.setValue(type);
			this.newListingFormBean.get("experienceYears")?.setValue(extractedFilters.experienceGTE);
			this.newListingFormBean.get("country")?.setValue(country);
			
			extractedFilters.languages.forEach(lang => {
				this.newListingFormBean.get(lang)?.setValue(lang);
			});
			
			this.newListingFormBean.get("description")?.setValue(extractedFilters.extractedText);
			
			this.closeModal();
			
			this.activeView 			= 'add';
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
	* Switches to Add Listing view
	*/
	public showAdd():void{
		
		this.reset();
		
		this.activeView 			= 'add';
		//this.activeSubView 			= 'step1';
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
		this.selectedListing		= new Listing();
		this.enabldeDeleteOption 	= false;
	}
	
	/**
	* Returns the flag css class for the Flag matching
	* the Country 
	*/
	public getFlagClassFromCountry(country:string):string{
		
		let sc:Country =  this.staticDataService.fetchCountries().filter(c => c.key == country)[0];
		
		return sc ?  "flag-icon-"+sc.iso2Code : '';
		
	}
	
	/**
	* Returns the code identifying the country
	* @param country - Country to get the country code for
	*/
	public getCountryCode(country:string):string{

		const matchingCountry = this.staticDataService.fetchCountries().filter(countryObj => countryObj.key == country)[0];
		
		return matchingCountry == null ? "NA" : matchingCountry.humanReadable;
	
  	}
	
	/**
	* Switches to Show Listing view
	*/
	public showListingDetails(selectedListing?:Listing):void{
		
		this.activeView 			= 'show';
		this.enabldeDeleteOption 	= false;
		
		if (selectedListing) {
			
			this.selectedListing	= selectedListing;
		
			this.infoItemConfig = new InfoItemConfig();
			this.infoItemConfig.setProfilePhoto(this.recruiterProfile?.profilePhoto?.imageBytes);
	
			//Recruiter Block
			let recruiterBlock:InfoItemBlock = new InfoItemBlock();
			recruiterBlock.setTitle(this.translate.instant('arenella-recruiter-listing-recruiter-details'));//"Recruiter Details");
			recruiterBlock.addRow(new InfoItemRowKeyValue(this.translate.instant('arenella-recruiter-listing-name'),selectedListing!.ownerName));
			recruiterBlock.addRow(new InfoItemRowKeyValue(this.translate.instant('arenella-recruiter-listing-company'),selectedListing!.ownerCompany));
			this.infoItemConfig.addItem(recruiterBlock);
		
			//Location Block
			if (selectedListing!.country || selectedListing!.location) {
				let locationBlock:InfoItemBlock = new InfoItemBlock();
				locationBlock.setTitle(this.translate.instant('arenella-recruiter-listing-location'));
				if	(selectedListing!.country) {
					recruiterBlock.addRow(new InfoItemRowKeyValueFlag(this.translate.instant('info-item-title-country'),this.getFlagClassFromCountry(this.selectedListing.country)));
				}
				if	(selectedListing!.location) {
					locationBlock.addRow(new InfoItemRowKeyValue(this.translate.instant('arenella-recruiter-listing-city'),selectedListing!.location));
				}
				this.infoItemConfig.addItem(locationBlock);
			}
		
			//Contract Block
			if (selectedListing!.type || selectedListing!.currency) {
				let currencyBlock:InfoItemBlock = new InfoItemBlock();
				currencyBlock.setTitle(this.translate.instant('arenella-recruiter-listing-contract-information'));
				if	(selectedListing!.type) {
					currencyBlock.addRow(new InfoItemRowKeyValue(this.translate.instant('arenella-recruiter-listing-contract-type'),this.getContractType(selectedListing!.type)));
				}
				if	(selectedListing!.currency && selectedListing!.rate && selectedListing!.rate != "0" ) {
					currencyBlock.addRow(new InfoItemRowKeyValue(this.translate.instant('arenella-recruiter-listing-renumeration'),selectedListing!.currency + " : " + selectedListing!.rate));
				}
				this.infoItemConfig.addItem(currencyBlock);
			}
		
			//Recruiter Block
			if (selectedListing!.yearsExperience && selectedListing!.yearsExperience != 0) {
				let experienceBlock:InfoItemBlock = new InfoItemBlock();
				experienceBlock.setTitle(this.translate.instant('arenella-recruiter-listing-years-experience'));
				experienceBlock.addRow(new InfoItemRowSingleValue(selectedListing!.yearsExperience));
				this.infoItemConfig.addItem(experienceBlock);
			}
			//Languages Block
			if (selectedListing!.languages.length > 0) {
				let languagesBlock:InfoItemBlock = new InfoItemBlock();
				languagesBlock.setTitle(this.translate.instant('arenella-recruiter-listing-languages'));
				selectedListing!.languages.forEach(lang => {
					languagesBlock.addRow(new InfoItemRowSingleValue(this.translate.instant(lang)));	
				});
				this.infoItemConfig.addItem(languagesBlock);
			}
		
			//Skills Block
			if ( selectedListing!.skills.length > 0) {
				let skillsBlock:InfoItemBlock = new InfoItemBlock();
				skillsBlock.setTitle(this.translate.instant('arenella-recruiter-listing-skills'));
				skillsBlock.addRow(new InfoItemRowMultiValues(selectedListing!.skills, "skill"));
				this.infoItemConfig.addItem(skillsBlock);
			}
			
		}
		
	}
		
	/**
	* Switches to Edit Listing view
	*/
	public showEditListing():void{
		
		this.activeView 			= 'edit';
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
		
		this.languages.forEach(lang => {
			this.newListingFormBean.get(lang)?.setValue(this.selectedListing.languages.includes(lang));
			
		});
		
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
		
		let languages:Array<string> = new Array<string>();
		this.languages.forEach(lang => {
			if (this.newListingFormBean.get(lang)?.value === true) {
				languages.push(lang);
			}
		});
		
		type = type == "" ? "BOTH" : type;
		
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
	* Provides HTML Options for country 
	* Selection 
	*/
  	private getCountryOptions():Array<HtmlOption>{
		  
		  let countries:Array<HtmlOption> = new Array<HtmlOption>();
		   
		  countries.push(new HtmlOption("",""));
		  
		  this.staticDataService.fetchCountries().forEach( country => {
			countries.push(new HtmlOption(country.key,country.humanReadable));  
		  });
		  		  
		  return countries;
	}
	
	/**
	* Supported for Listings
	*/
	private getLanguageOptions():void{
		this.languages = new Array<string>();
		this.listingService.getSupportedLanguagesAfterLoading().forEach(language => {
			this.languages.push(language);	
			this.newListingFormBean.addControl(language, new UntypedFormControl(''));	
		})	;
		
  	}
	
	/**
	* Provides HTML Options for country 
	* Selection 
	*/
  	private getContractTypeOptions():Array<HtmlOption>{
		  
		let types:Array<HtmlOption> = new Array<HtmlOption>();
		  
		types.push(new HtmlOption("",""));
		
		this.staticDataService.fetchContractTypes().forEach(contractType => {
			types.push(new HtmlOption(contractType.contractType,contractType.humanReadable));
		});
		  		  
		return types;
	}
	
	/**
	* Returns the code identifying the country
	* @param country - Country to get the country code for
	*/
	public getContractType(type:string):string{

		const matchingType = this.staticDataService.fetchContractTypes().filter(contractTypeObj => contractTypeObj.contractType == type)[0];
		
		return matchingType == null ? "NA" : matchingType.humanReadable;
		
  	}	

	/**
	* Returns the Humand readable version of the Language
	* @param country - Language to get the readable version for
	*/
	public getLanguage(lang:string):string{

		const matchingType = this.staticDataService.fetchLanguageTypes().filter(langTypeObj => langTypeObj.value == lang)[0];
		
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
		
		let startDate:Date 				= this.mpFormBean.get('startDate')?.value;
		let lastSubmissionDate:Date 	= this.mpFormBean.get('lastSubmissionDate')?.value;
		let comments:string 			= this.mpFormBean.get('comments')?.value;
		
		let languages:Array<string> = new Array<string>();
				
		this.languages.forEach(lang => {
			if (this.newListingFormBean.get(lang)?.value === true) {
				languages.push(lang);
			}
		});
		
		type = type == "" ? "BOTH" : type == "CONTRACT_ROLE" ? "CONTRACT" : type == "PERM_ROLE" ? "PERM" : "BOTH";
		
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
	
	/**
	* Returns the flag for a given country 
	*/
	public getCountryISO2Code(country:string):string{
		return this.staticDataService.getCountryISO2Code(country);
  	}
	
	/**
	* Returns size limited version
	*/
	public getFormattedJobTitle(title:string):string{
		return title.length < 25 ? title : title.substring(0,25) + "...";
	}
	
}