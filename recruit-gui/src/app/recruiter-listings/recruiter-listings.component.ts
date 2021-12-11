import { Component, OnInit } 							from '@angular/core';
import { FormGroup, FormControl }						from '@angular/forms';
import { ListingService }								from '../listing.service';
import { RecruiterService }								from '../recruiter.service';
import { NgbModal, NgbModalOptions}						from '@ng-bootstrap/ng-bootstrap';
import { ViewChild }									from '@angular/core';
import { Listing}										from './listing';

@Component({
  selector: 'app-recruiter-listings',
  templateUrl: './recruiter-listings.component.html',
  styleUrls: ['./recruiter-listings.component.css']
})
export class RecruiterListingsComponent implements OnInit {

	@ViewChild('feedbackBox', { static: false }) private content:any;

  	constructor(private listingService:ListingService, private modalService: NgbModal, private recruiterService:RecruiterService) { }

	private recruiterId:string				= '';
	private recruiterFirstName:string 		= '';
	private recruiterSurname:string			= '';
	private recruiterEmail:string 			= '';
	private recruiterCompany:string			= '';
	
	ngOnInit(): void {
	
		this.recruiterService.getOwnRecruiterAccount().subscribe(data => {
			this.recruiterId 					= data.userId;
			this.recruiterFirstName				= data.firstName;
			this.recruiterSurname				= data.surname;
			this.recruiterEmail					= data.email;
			this.recruiterCompany				= data.companyName;
			this.fetchListings();			
		});
	
  	}

	public skills:Array<string> 			= new Array<string>();
	public listings:Array<Listing>			= new Array<Listing>();
	public activeView:string				= 'list';
	public activeSubView:string				= 'none';
	public selectedListing:Listing			= new Listing();
	
	public feedbackBoxClass:string          = '';
  	public feedbackBoxTitle                 = '';
  	public feedbackBoxText:string           = '';
	public validationErrors:Array<string>	= new Array<string>();
	
	private	pageSize:						number						= 8;
  	public	totalPages:						number						= 0;
  	public	currentPage:					number						= 0;
  	

	public newListingFormBean:FormGroup 	= new FormGroup({
     
		title:				new FormControl(''),
		type:				new FormControl(),
       	country:			new FormControl(),
		location:			new FormControl(),	
		experienceYears:	new FormControl(),
		rate:				new FormControl(),
		rateCurrency:		new FormControl(),
		description:		new FormControl(),
		contactName:		new FormControl(),
		contactCompany:		new FormControl(),
		contactEmail:		new FormControl(),
		langDutch:			new FormControl(),
		langEnglish:		new FormControl(),
		langFrench:			new FormControl(),
		skill:				new FormControl()
		
	});
	
	public reset():void{
		
		this.newListingFormBean  = new FormGroup({
     
			title:				new FormControl(''),
			type:				new FormControl(),
	       	country:			new FormControl(),
			location:			new FormControl(),	
			experienceYears:	new FormControl(),
			rate:				new FormControl(),
			rateCurrency:		new FormControl(),
			description:		new FormControl(),
			contactName:		new FormControl(),
			contactCompany:		new FormControl(),
			contactEmail:		new FormControl(),
			langDutch:			new FormControl(),
			langEnglish:		new FormControl(),
			langFrench:			new FormControl(),
			skill:				new FormControl()
			
		});
		
		this.skills 			= new Array<string>();
		this.validationErrors 	= new Array<string>();
		
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
	
	      let options: NgbModalOptions = {
	    	 centered: true
	   };
	
	  this.modalService.open(this.content, options);

  }
	
	/**
	*  Closes the confirm popup
	*/
	public closeModal(): void {
		this.modalService.dismissAll();
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
		this.activeView 		= 'add';
		this.activeSubView 		= 'step1';
		this.selectedListing	= new Listing();
		
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
		this.activeView 		= 'list';
		this.activeSubView 		= 'none';
		this.selectedListing	= new Listing();
	}
	
	/**
	* Switches to Show Listing view
	*/
	public showListingDetails(selectedListing?:Listing):void{
		
		this.activeView 		= 'show';
		this.activeSubView 		= 'none';
		
		if (selectedListing) {
			this.selectedListing	= selectedListing;	
		}
		
	}
		
	/**
	* Switches to Edit Listing view
	*/
	public showEditListing():void{
		
		this.activeView 		= 'edit';
		this.activeSubView 		= 'step1';
		
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
			languages.push('FRENCH');
		}
		
		if (langFrench === true) {
			languages.push('ENGLISH');
		}
		
		this.validationErrors	= new Array<string>();
		
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
									this.reset();
									this.fetchListings();
									this.showList();
								}, err => {
									
									if(err.status === 400) {
										
										let failedFields:Array<any> = err.error;
										
										failedFields.forEach(failedField => {
											this.validationErrors.push(failedField.fieldMessageOrKey);
										});
										
										this.open('feedbackBox', "Failure",  false);
									}
										
								});
	
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
	
}