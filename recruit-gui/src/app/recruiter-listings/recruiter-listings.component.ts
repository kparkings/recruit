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

	private recruiterId:string = '';
	
	ngOnInit(): void {
	
		this.recruiterService.getOwnRecruiterAccount().subscribe(data => {
			this.recruiterId = data.userId;
			this.fetchListings();			
		});
	
  	}

	public listings:Array<Listing>			= new Array<Listing>();
	public activeView:string				= 'list';
	public activeSubView:string				= 'none';
	
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
	
	public skills:Array<string> = new Array<string>();
	
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
	*
	*/
	public showAdd():void{
		this.activeView = 'add';
		this.activeSubView = 'step1';
	}
	
	/**
	*
	*/
	public showList():void{
		this.activeView = 'list';
		this.activeSubView = 'none';
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
		
}