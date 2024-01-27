import { Component, ElementRef, OnInit, ViewChild } 							from '@angular/core';
import { ListingService }								from '../listing.service';
import { EmailService, EmailRequest }					from '../email.service';
import { Listing}										from './listing';
import { ActivatedRoute } 								from '@angular/router';
import { UntypedFormGroup, UntypedFormControl,  }		from '@angular/forms';
import { NgbModal, NgbModalOptions }					from '@ng-bootstrap/ng-bootstrap';
import { HostListener}									from '@angular/core';
import { DeviceDetectorService } 						from 'ngx-device-detector';
import { RecruiterProfileService} 						from '../recruiter-profile.service';
import { RecruiterProfile }								from '../recruiter-profile/recruiter-profile';
import { ListingAlertAddRequest } 						from './listing-alert-add-request';
import { SelectableFunctionType } 						from '../shared-domain-object/function-type';
import { SelectableCountry } 							from '../shared-domain-object/country';
import { StaticDataService } 							from '../static-data.service';
import { HtmlOption } 									from '../html-option';
import { InfoItemBlock, InfoItemConfig, InfoItemRowKeyValue, InfoItemRowMultiValues, InfoItemRowSingleValue } 				from '../candidate-info-box/info-item';

@Component({
  selector: 'app-listing',
  templateUrl: './listing.component.html',
  styleUrls: ['./listing.component.css']
})
export class ListingComponent implements OnInit {

	@ViewChild('feedbackBox',  		{static:true}) feedbackDialogBox!:  ElementRef<HTMLDialogElement>;
	@ViewChild('publicityBox', 		{static:true}) publicityDialogBox!: ElementRef<HTMLDialogElement>;
	@ViewChild('listingAlertBox', 	{static:true}) listingAlertDialogBox!: ElementRef<HTMLDialogElement>;
	
	public recruiterProfiles:Array<RecruiterProfile> 				= new Array<RecruiterProfile>();
	public recruiterProfile:RecruiterProfile 						= new RecruiterProfile();
	public selectableFunctionTypes:Array<SelectableFunctionType>	= new Array<SelectableFunctionType>();
	public selectableCountries:Array<SelectableCountry>				= new Array<SelectableCountry>();
	public createAlertStep:number 									= 0;
	public listingAlertConfirmDisabled:boolean 						= true;
	public infoItemConfig:InfoItemConfig 							= new InfoItemConfig();
	
  	constructor(private listingService:ListingService, 
				private emailService:EmailService, 
				private _Activatedroute:ActivatedRoute, 
				private modalService:NgbModal, 
				private deviceDetector:DeviceDetectorService,
				private recruiterProfileService:RecruiterProfileService,
				private staticDataService:			StaticDataService) { 
	
		this.isMobile = deviceDetector.isMobile();
		
		if (this.isMobile) {
			this.mobileListingclass 			= 'mobile-listing-layout';
			this.mobileListingclassPane 		= 'mobile-listing-layout-pane';
			this.mobileListingclassFilters 		= 'mobile-listing-layout-filters';
			this.mobileListingLeftPaneContainer = "mobile-left-pane-container";
			this.mobileDescBody 				= 'mobile-desc-body';
			this.mobileListingViewDiv			= 'mobile-listing-view';
			this.mobileButton				 	= 'buttons-icon-mobile';
		} 
		
		this.recruiterProfileService.fetchRecruiterProfiles("RECRUITERS").subscribe(rps => {
			this.recruiterProfiles = rps
		});
	
		this.selectableFunctionTypes 	= this.staticDataService.fetchFunctionTypes().map(ft => new SelectableFunctionType(ft, false));
		this.selectableCountries 		= this.staticDataService.fetchCountries().map(c => new SelectableCountry(c, false));
	
	}
	
  	ngOnInit(): void {
		
		var id = this._Activatedroute.snapshot.paramMap.get("id");
		
		if (id ) {
			this.fetchListings(id);
		} else {
			this.fetchListings("");
		}
		
  	}

	public activeView:string					= 'list';
	
	private	pageSize:number						= 20;
  	public	totalPages:number					= 0;
  	public	currentPage:number					= 0;

	public listings:Array<Listing>				= new Array<Listing>();
	public selectedListing:Listing				= new Listing();
	public contractTypeFilter					= '';
	public ageFilter							= 'ALL';

  	public curriculumFile!:File| any;

	public displayFilters:boolean				= false;
	
	public isMobile:boolean = false;
	public mobileListingclass:string 				= '';
	public mobileListingclassPane:string 			= '';
	public mobileListingclassFilters:string 		= '';
	public mobileListingLeftPaneContainer:string 	= '';
	public mobileDescBody:string					= '';
	public mobileListingViewDiv:string				= '';
	public mobileButton:string						= '';					
	
	/**
	* Toggoles whether or not a FunctionType has been selected by the User
	*/
	public toggleFunctionType(ft:SelectableFunctionType):void{
		
		let sft:SelectableFunctionType = this.selectableFunctionTypes.filter(ftItem => ftItem == ft)[0];
	
		sft.selected = !sft.selected;
	
	}
	
	/**
	* Toggoles whether or not a Country has been selected by the User
	*/
	public toggleCountry(ft:SelectableCountry):void{
		
		let sft:SelectableCountry = this.selectableCountries.filter(ftItem => ftItem == ft)[0];
	
		sft.selected = !sft.selected;
	
	}
	
	/**
	* Uploads the file for the Curriculum and stored 
	* it ready to be sent to the backend
	*/
  	public uploadCurriculumFile(event:any):void{
  
  		if (event.target.files.length <= 0) {
  			return;
  		}
  	
  		this.curriculumFile = event.target.files[0];
		
	}
	
	showSendAlertBoxSuccess:boolean 		= false;
	showSendAlertBoxFailure:boolean 		= false;
	
	/**
	* Form for job alert 
	*/
	public alertFormBean:UntypedFormGroup = new UntypedFormGroup({
		emailAddress: new UntypedFormControl(''),
			contractType: new UntypedFormControl('BOTH')
	});
	
	/**
	* Reset Form for jb alert 
	*/
	private resetAlertFormBean():void{
		
		this.alertFormBean = new UntypedFormGroup({
			emailAddress: new UntypedFormControl(''),
			contractType: new UntypedFormControl('BOTH')
		});
		
		this.createAlertStep = 0;
		this.selectableFunctionTypes 	= this.staticDataService.fetchFunctionTypes().map(ft => new SelectableFunctionType(ft, false));
		this.selectableCountries 		= this.staticDataService.fetchCountries().map(c => new SelectableCountry(c, false));
	}
	
	public emailRequestFormBean:UntypedFormGroup = new UntypedFormGroup({
     	email:			new UntypedFormControl(),
		name:			new UntypedFormControl(),
		message:		new UntypedFormControl(),
	});
	
	/**
	* Resets the form 
	*/
	private resetEmailRequestForm():void{
		this.emailRequestFormBean = new UntypedFormGroup({
     		email:			new UntypedFormControl(),
			name:			new UntypedFormControl(),
			message:		new UntypedFormControl(),
		});
		
		this.showSendAlertBoxSuccess 		= false;
		this.showSendAlertBoxFailure 		= false;
	
	}
	
	/**
	* Toggles Filters
	*/
	public toggleFilters():void{
		this.displayFilters = !this.displayFilters;
	
	}
	
	/**
	* Send an email request 
	*/
	public sendEmailRequest():void{
		
		let emailRequest:EmailRequest = new EmailRequest();
		
		emailRequest.attachment 	= this.curriculumFile;
		emailRequest.message 		= this.emailRequestFormBean.get('message')?.value; 
		emailRequest.senderEmail 	= this.emailRequestFormBean.get('email')?.value; 
		emailRequest.senderName 	= this.emailRequestFormBean.get('name')?.value; 
		
		this.emailService.sendEmail(emailRequest, this.selectedListing.listingId, this.isCandidate()).subscribe(response =>{
			this.resetEmailRequestForm();
			this.curriculumFile = null;
			this.showSendAlertBoxSuccess 		= true;
			this.showSendAlertBoxFailure 		= false;
			this.feedbackDialogBox.nativeElement.showModal();
	    },err => {
			this.showSendAlertBoxSuccess 		= false;
			this.showSendAlertBoxFailure 		= true;
			this.feedbackDialogBox.nativeElement.showModal();
		});

	}
	
	/**
	* Whether or not to enable send email button. If missing 
	* info button must be disbled. 
	*/
	public enableSendEmail():boolean{
		
		if(this.isCandidate() && this.emailRequestFormBean.get('message')?.value) {
			return true;
		}
		
		if (!this.emailRequestFormBean.get('message')?.value) {
			return false;
		}
		
		if (!this.emailRequestFormBean.get('email')?.value) {
			return false;
		}
		
		if (!this.emailRequestFormBean.get('name')?.value) {
			return false;
		}
		
		return true;
	}
		
	/**
	* Switches to Add Listing view
	*/
	public showList():void{
		this.activeView 			= 'list';
		this.selectedListing		= new Listing();
	}
	
	/**
	* Switches to Show Listing view
	*/
	public showListingDetails(selectedListing?:Listing):void{
	
		this.activeView 			= 'show';
		
		if (selectedListing) {
		
			this.recruiterProfile = this.recruiterProfiles.filter(p => p.recruiterId == selectedListing.ownerId)[0];
		
		
			this.infoItemConfig = new InfoItemConfig();
			this.infoItemConfig.setProfilePhoto(this.recruiterProfile?.profilePhoto?.imageBytes);
	
			//Recruiter Block
			let recruiterBlock:InfoItemBlock = new InfoItemBlock();
			recruiterBlock.setTitle("Recruiter Details");
			recruiterBlock.addRow(new InfoItemRowKeyValue("Name",selectedListing!.ownerName));
			recruiterBlock.addRow(new InfoItemRowKeyValue("Company",selectedListing!.ownerCompany));
			this.infoItemConfig.addItem(recruiterBlock);
		
			//Location Block
			if (selectedListing!.country || selectedListing!.location) {
				let locationBlock:InfoItemBlock = new InfoItemBlock();
				locationBlock.setTitle("Location");
				if	(selectedListing!.country) {
					locationBlock.addRow(new InfoItemRowKeyValue("Country",this.getCountryCode(selectedListing!.country)));
				}
				if	(selectedListing!.location) {
					locationBlock.addRow(new InfoItemRowKeyValue("City",selectedListing!.location));
				}
				this.infoItemConfig.addItem(locationBlock);
			}
		
			//Contract Block
			if (selectedListing!.type || selectedListing!.currency) {
				let currencyBlock:InfoItemBlock = new InfoItemBlock();
				currencyBlock.setTitle("Contract Information");
				if	(selectedListing!.type) {
					currencyBlock.addRow(new InfoItemRowKeyValue("Contract Type",this.getContractType(selectedListing!.type)));
				}
				if	(selectedListing!.currency) {
					currencyBlock.addRow(new InfoItemRowKeyValue("Renumeration",selectedListing!.currency + " : " + selectedListing!.rate));
				}
				this.infoItemConfig.addItem(currencyBlock);
			}
		
			//Recruiter Block
			let experienceBlock:InfoItemBlock = new InfoItemBlock();
			experienceBlock.setTitle("Years Experience");
			experienceBlock.addRow(new InfoItemRowSingleValue(selectedListing!.yearsExperience));
			this.infoItemConfig.addItem(experienceBlock);
		
			//Languages Block
			if (selectedListing!.languages.length > 0) {
				let languagesBlock:InfoItemBlock = new InfoItemBlock();
				languagesBlock.setTitle("Languages");
				selectedListing!.languages.forEach(lang => {
					languagesBlock.addRow(new InfoItemRowSingleValue(this.getLanguage(lang)));	
				});
				this.infoItemConfig.addItem(languagesBlock);
			}
		
			//Skills Block
			if ( selectedListing!.skills.length > 0) {
				let skillsBlock:InfoItemBlock = new InfoItemBlock();
				skillsBlock.setTitle("Skills");
				skillsBlock.addRow(new InfoItemRowMultiValues(selectedListing!.skills, "skill"));
				this.infoItemConfig.addItem(skillsBlock);
			}
			
			this.selectedListing	= selectedListing;	
			this.recruiterProfile = new RecruiterProfile();
			this.recruiterProfile = this.recruiterProfiles.filter(p => p.recruiterId == selectedListing.ownerId)[0];
	
			this.registerListingViewedEvent();
			
			window.scroll({ 
      			top: 0, 
      			left: 0, 
      			behavior: 'auto' 
    		});


		}
		
	}
	
	/**
	* Returns human readable version
	*/
	public getFormattedContractType(type:string):string{
		return type.replace('_ROLE','');
	}
	
	/**
	* Returns size limited version
	*/
	public getFormattedJobTitle(title:string):string{
		return title.length < 50 ? title : title.substring(0,49) + "...";
	}
	
	/**
	* Generated the filter sring to filter results by. Can be empty
	* String if no filters required
	*/
	private generateFilterString(): string{
		
		let filterString: string = "";
		
		if (this.contractTypeFilter != "") {
			filterString = filterString + '&listingType=' + this.contractTypeFilter;
		}
		
		if (this.ageFilter != "ALL") {
			filterString = filterString + '&listingAge=' + this.ageFilter;
		}
		
		return filterString;
	}
	
	/**
	* Overloaded version
	*/
	public fetchListings(id:string):void{
		this.fetchListingsFull(id, false);
	}
	/**
	* Retrieves listings belonging to the Recruiter
	*/
	public fetchListingsFull(id:string, resetSelectedListing:boolean):void{
	
		if (resetSelectedListing) {
			this.selectedListing	= new Listing();
		}
		
		this.listingService
			.fetchAllListings('created',"desc", this.currentPage, this.pageSize, this.generateFilterString())
				.subscribe(data => {
					this.totalPages = data.totalPages;
					
					let lis:Array<Listing> = data.content;
					
					lis.forEach(l =>{
						this.listings.push(l);	
					});
					
					if (id !== "") {
		
						var results: Array<Listing> = this.listings.filter(listing => listing.listingId === id);
		
						if (results.length > -1) {
							let listing: Listing = results[0];
				
							this.showListingDetails(listing)
				
						}
		
					}
					
				}, 
				err => {
					console.log("Error retrieving listings for all recruiters" + JSON.stringify(err));			
				});
		
	}
	
	/**
	* Registers that a Listing has been viewed
	*/
	private registerListingViewedEvent():void {
		
		if (!this.isAuthenticatedAsAdmin()) {
			this.listingService.registerListingViewedEvent(this.selectedListing.listingId).subscribe();
		}
	}
	
	public getContractTypeOptions():Array<HtmlOption>{
		  
		let types:Array<HtmlOption> = new Array<HtmlOption>();
		  
		this.staticDataService.fetchContractTypes().forEach(contractType => {
			types.push(new HtmlOption(contractType.contractType,contractType.humanReadable));
		});
		  		  
		return types;
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
	* Sets which contract type filters have been selected
	*/
	public updateContractTypeFilter(contractType:string){
		
		this.listings			= new Array<Listing>();
		this.pageYPos = 0;
		
		
		switch(contractType){
			case "Contract": {
				this.currentPage		= 0;
				this.contractTypeFilter = "CONTRACT_ROLE"
				this.fetchListings("");
				return;
			}
			case "Perm": {
				this.currentPage		= 0;
				this.contractTypeFilter = "PERM_ROLE"
				this.fetchListings("");
				return;
			}
			default: {
				this.currentPage		= 0;
				this.contractTypeFilter = ""
				this.fetchListings("");
				return;
			}
		}	
	}
	
	/**
	* Returns the appropriate css class to indicate if the filter 
	* is selected or not
	*/
	public getFilterTypeClass(contractType: string):string {
		if (this.contractTypeFilter === contractType) {
			return "active-filter";
		}
		
		return "inactive-filter";
	}
	
	/**
	* Sets which post age filter have been selected
	*/
	public updatePostAgeFilter(contractType:string){
		
		this.listings			= new Array<Listing>();
		this.pageYPos = 0;
		
		switch(contractType){
			
			case "TODAY": {
				this.currentPage		= 0;
				this.ageFilter = "TODAY"
				this.fetchListings("");
				return;
			}
			case "THIS_WEEK": {
				this.currentPage		= 0;
				this.ageFilter = "THIS_WEEK"
				this.fetchListings("");
				return;
			}
			case "THIS_MONTH": {
				this.currentPage		= 0;
				this.ageFilter = "THIS_MONTH";
				this.fetchListings("");
				return;
			}
			default: {
				this.currentPage		= 0;
				this.ageFilter = "ALL";
				this.fetchListings("");
				return;
			}
		}	
	}
	
	/**
	* Returns the appropriate css class to indicate if the filter 
	* is selected or not
	*/
	public getPostAgeClass(option: string):string {
		
		if (this.ageFilter === option) {
			return "active-filter";
		}
		
		return "inactive-filter";
	}
	
	/**
  	* Returns whether the user has logged into the system 
  	*/
	public isLoggedIn():boolean{
		return sessionStorage.getItem('loggedIn') == 'true';
	}
	
	public getExternalUrl(id:string):string{
		
		let externalUrl = '';
		
		if (window.location.href.indexOf('listing') < 0){
			externalUrl = window.location.href + 'listing/' +  id;
		} else {
			externalUrl = window.location.href + '/' + id;
		}
		
		return externalUrl;
	}
	
	/**
  	* Whether or not the user has authenticated with the System 
  	*/
  	public isAuthenticated():boolean {
    	return sessionStorage.getItem('loggedIn') === 'true';
  	}

  	/**
  	* Whether or not the user has authenticated as an Admin user 
  	*/
  	public isAuthenticatedAsAdmin():boolean {
    	return sessionStorage.getItem('isAdmin') === 'true';
  	}
	
	private pageYPos = 0;
	
	@HostListener('window:scroll', ['$event']) onWindowScroll(e:any) {
   	 
		let yPos = window.pageYOffset;

		if (yPos > this.pageYPos) {
			this.pageYPos = yPos + (this.isMobile ? 50 : 500); 
			this.currentPage = this.currentPage + 1;
			this.fetchListingsFull("", false);
			
		}

  }

	/**
	* Whether or not the Use is a Candidate
	*/
	public isCandidate():boolean{
		return sessionStorage.getItem('isCandidate') === 'true';
	}
	
	/**
	* Shows a popup with the details of the listing to 
	* post on other websites
	*/
	public showPublicity():void{
		this.publicityDialogBox.nativeElement.showModal();
	}
	
	/**
	* Creates a new ListingAlert
	*/
	public createListingAlert():void{
		
		let alert:ListingAlertAddRequest = new ListingAlertAddRequest();
		
		alert.categories 	= this.selectableFunctionTypes.filter(c => c.selected == true).map(c => c.functionType.key);
		alert.countries 	= this.selectableCountries.filter(c => c.selected == true).map(c => c.country.key);
		alert.email		 	= this.alertFormBean.get('emailAddress')?.value;
		alert.contractType  = this.alertFormBean.get('contractType')?.value;
		
		this.listingService.createAlert(alert).subscribe(res => {
			this.resetAlertFormBean();
			//this.listingAlertDialogBox.nativeElement.close();
			this.createAlertStep = 4;
		});	
		
	}
	
	/**
	* Opens a dialog box to create a Listing Alert 
	*/
	public openCreateAlertDialog():void{
		this.resetAlertFormBean();
		this.listingAlertDialogBox.nativeElement.showModal();
	}
	
	public selectedFunctionTypeCss(ft:SelectableFunctionType):string{
		if(this.selectableFunctionTypes.filter(ftItem => ftItem == ft)[0].selected){
			return "listing-alert-option-selected";
		} else {
			return "";
		}
	}
	
	public selectedCountryCss(ft:SelectableCountry):string{
		if(this.selectableCountries.filter(ftItem => ftItem == ft)[0].selected){
			return "listing-alert-option-selected";
		} else {
			return "";
		}
	}
	
	public createListingAlertNextStep():void{
		this.createAlertStep = this.createAlertStep+1;
	}
	
	public createListingAlertLastStep():void{
		this.createAlertStep = this.createAlertStep-1;
	}
	
	public handleAlertEmailOnChange():void{
		
		let email:string = this.alertFormBean.get('emailAddress')?.value;
		
		if (email && email.length >= 6) {
			this.listingAlertConfirmDisabled = false;
		} else {
			this.listingAlertConfirmDisabled = true;
		}
	}
	
	
}