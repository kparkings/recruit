import { Component, OnInit } 							from '@angular/core';
import { ListingService }								from '../listing.service';
import { EmailService, EmailRequest }					from '../email.service';
import { Listing}										from './listing';
import { ActivatedRoute } 								from '@angular/router';
import { UntypedFormGroup, UntypedFormControl,  }		from '@angular/forms';
import { NgbModal, NgbModalOptions }					from '@ng-bootstrap/ng-bootstrap';
import { HostListener}									from '@angular/core';
import { DeviceDetectorService } 						from 'ngx-device-detector';

@Component({
  selector: 'app-listing',
  templateUrl: './listing.component.html',
  styleUrls: ['./listing.component.css']
})
export class ListingComponent implements OnInit {

  	constructor(private listingService:ListingService, 
				private emailService:EmailService, 
				private _Activatedroute:ActivatedRoute, 
				private modalService:NgbModal, 
				private deviceDetector:DeviceDetectorService) { 
	
		this.isMobile = deviceDetector.isMobile();
		
		if (this.isMobile) {
			this.displayFilters = false;
			this.mobileListingclass = 'mobile-listing-layout';
		} else {
			this.displayFilters = true;
		}
		
		this.emailRequestFormBean?.get('useStoredCV')?.valueChanges.subscribe(val => {
			if (val == true){
				this.fileSelectEvent.target.value = null;
			}
		});
	
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

  	private curriculumFile!:File| any;

	public displayFilters:boolean				= true;
	
	public isMobile:boolean = false;
	public mobileListingclass:string 			= '';
	
	public fileSelectEvent:any;
	
	/**
	* Uploads the file for the Curriculum and stored 
	* it ready to be sent to the backend
	*/
  	public uploadCurriculumFile(event:any):void{
  
  		if (event.target.files.length <= 0) {
  			return;
  		}
  	
  		this.curriculumFile = event.target.files[0];
		this.emailRequestFormBean.controls['useStoredCV'].setValue(false);
  		this.fileSelectEvent = event;
	}
	
	showSendAlertBoxSuccess:boolean 		= false;
	showSendAlertBoxFailure:boolean 		= false;
	
	
	public emailRequestFormBean:UntypedFormGroup = new UntypedFormGroup({
     	email:			new UntypedFormControl(),
		name:			new UntypedFormControl(),
		message:		new UntypedFormControl(),
		useStoredCV:	new UntypedFormControl(true),
	});
	
	/**
	* Resets the form 
	*/
	private resetEmailRequestForm():void{
		this.emailRequestFormBean = new UntypedFormGroup({
     		email:		new UntypedFormControl(),
			name:		new UntypedFormControl(),
			message:	new UntypedFormControl(),
			useStoredCV:	new UntypedFormControl(true),
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
	public sendEmailRequest(content:any):void{
		
		let emailRequest:EmailRequest = new EmailRequest();
		
		emailRequest.attachment 	= this.curriculumFile;
		emailRequest.message 		= this.emailRequestFormBean.get('message')?.value; 
		emailRequest.senderEmail 	= this.emailRequestFormBean.get('email')?.value; 
		emailRequest.senderName 	= this.emailRequestFormBean.get('name')?.value; 
		
		this.emailService.sendEmail(emailRequest, this.selectedListing.listingId).subscribe(response =>{
			this.resetEmailRequestForm();
			this.curriculumFile = null;
			this.showSendAlertBoxSuccess 		= true;
			this.showSendAlertBoxFailure 		= false;
			this.open(content);
	    },err => {
			this.showSendAlertBoxSuccess 		= false;
			this.showSendAlertBoxFailure 		= true;
			this.open(content);
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
			this.selectedListing	= selectedListing;	
			this.registerListingViewedEvent();
			
			window.scroll({ 
      			top: 0, 
      			left: 0, 
      			behavior: 'auto' 
    		});


		}
		
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

	/**
	* Returns the code identifying the country
	* @param country - Country to get the country code for
	*/
	public getCountryCode(country:string):string{

		switch(country){
			case "NETHERLANDS":{
				return "The Netherlands";
			}
			case "BELGIUM":{
				return "Belgium";
			}
			case "UK":{
				return "United Kingdom";
			}
			case "IRELAND":{
				return "Ireland";
			}
			case "EU_REMOTE":{
				return "Remote within EU";
			}
			case "UK":{
				return "Remote within World";
			}
			default:{
				return 'NA';
			}
		}

  	}

	/**
	* Returns the code identifying the country
	* @param country - Country to get the country code for
	*/
	public getContractType(type:string):string{

		switch(type){
			case "CONTRACT_ROLE":{
				return "Contract";
			}
			case "PERM_ROLE":{
				return "Permanent";
			}
			case "BOTH":{
				return "Contract/Permanent";
			}
			default:{
				return 'NA';
			}
		}

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
	
	public getExternalUrl(id:string):string{
		
		let externalUrl = '';
		
		if (window.location.href.indexOf('listing') < 0){
			externalUrl = window.location.href + 'listing/' +  id;
		} else {
			externalUrl = window.location.href + id;
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

	/**
	*  Closes the confirm popup
	*/
	public closeModal(): void {
		
		this.showSendAlertBoxSuccess 		= false;
		this.showSendAlertBoxFailure 		= false;
		
		this.modalService.dismissAll();
	}
	
	public open(content:any):void {
		
	
	   let options: NgbModalOptions = {
	    	 centered: true
	   };

		this.modalService.open(content, options);

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
  

}