import { Component, OnInit } 							from '@angular/core';
import { FormGroup, FormControl }						from '@angular/forms';
import { NgbModal, NgbModalOptions}						from '@ng-bootstrap/ng-bootstrap';
import { ViewChild }									from '@angular/core';
import { RecruiterMarketplaceService }					from '../recruiter-marketplace.service';

@Component({
  selector: 'app-recruiter-marketplace',
  templateUrl: './recruiter-marketplace.component.html',
  styleUrls: ['./recruiter-marketplace.component.css']
})
export class RecruiterMarketplaceComponent implements OnInit {

	@ViewChild('feedbackBox', { static: false }) private content:any;

  	constructor(private modalService: NgbModal, private marketplaceService: RecruiterMarketplaceService) { }

	ngOnInit(): void {
 	}

	currentTab:string 						= "downloads";
	showSupply:boolean						= true;
	showDemand:boolean						= false;
	addSupply:boolean						= false;
	addDemand:Boolean						= false;
	
	/**
	* Validation 
	*/
	public feedbackBoxClass:string          			= '';
  	public feedbackBoxTitle                 			= '';
  	public feedbackBoxText:string           			= '';
	public validationErrors:Array<string>				= new Array<string>();
	
	/**
	* OfferedCandidate - Add details 
	*/
	coreSkills:Array<string>				= new Array<string>();
	spokenLanguages:Array<string>			= new Array<string>();
	
	public offeredCandidateFormBean:FormGroup = new FormGroup({
     	candidateRoleTitle:		new FormControl(''),
		country:				new FormControl(),
       	location:				new FormControl(),
		contractType:			new FormControl(),	
		daysOnSite:				new FormControl(),
		renumeration:			new FormControl(),
		availableFromDate:		new FormControl(),
		yearsExperience:		new FormControl(),
		description:			new FormControl(),
		comments:				new FormControl(),
		skill:					new FormControl(),
		language:				new FormControl(),
		
	});
	
	/**
	* Resets the OfferedCandudates FormBean
	*/
	private resetOfferedCandidatesForm(){
			this.offeredCandidateFormBean = new FormGroup({
	     	candidateRoleTitle:		new FormControl(''),
			country:				new FormControl(),
	       	location:				new FormControl(),
			contractType:			new FormControl(),	
			daysOnSite:				new FormControl(),
			renumeration:			new FormControl(),
			availableFromDate:		new FormControl(),
			yearsExperience:		new FormControl(),
			description:			new FormControl(),
			comments:				new FormControl(),
			skill:					new FormControl(),
			language:				new FormControl(),
			
		});
	}
	
	/**
	* Switches to the selected tab
	*/
	public switchTab(tab:string){
		
		this.currentTab = tab;
		
		switch(tab){
			case "showSupply":{
				this.showSupply	=true;
				this.showDemand	=false;
				this.addSupply	= false;
				this.addDemand	= false;
				break;
			}
			case "showDemand":{
				this.showSupply=false;
				this.showDemand=true;
				this.addSupply	= false;
				this.addDemand	= false;
				break;
			}
			case "addSupply":{
				this.showSupply	=	false;
				this.showDemand	=	false; 
				this.addSupply	= 	true;
				this.addDemand	= 	false;
				break;
			}
			case "addDemand":{
				this.showSupply		= false;
				this.showDemand		= false;
				this.addSupply		= false;
				this.addDemand		= true;
				break;
			}
		}
		
	}
	
	/**
	* Adds a skill posessed by the offered candidate 
	*/
	public addOfferedCandidateSkill():void{
		
		let skillFormatted:string 	= this.offeredCandidateFormBean.get('skill')?.value.trim();
		skillFormatted 				= skillFormatted.toLocaleLowerCase();
		
		if (skillFormatted.length > 0 && this.coreSkills.indexOf(skillFormatted) == -1) {
			this.coreSkills.push(skillFormatted);	
		}
		
		this.offeredCandidateFormBean.get('skill')?.setValue('');
		
	}
	
	/**
	* Removes a skill posessed by the offered Candidate
	*/
	public removeOfferedCandidateSkill(skill:string):void{
		
		skill = skill.trim();
		skill = skill.toLocaleLowerCase();
		
		this.coreSkills = this.coreSkills.filter(s => s  !== skill);
		
	}
	
		/**
	* Adds a language posessed by the offered candidate 
	*/
	public addOfferedCandidateLanguage():void{
		
		let languageFormatted:string 	= this.offeredCandidateFormBean.get('language')?.value.trim();
		languageFormatted 				= languageFormatted.toUpperCase();
		
		if (languageFormatted.length > 0 && this.spokenLanguages.indexOf(languageFormatted) == -1) {
			this.spokenLanguages.push(languageFormatted);	
		}
		
		this.offeredCandidateFormBean.get('language')?.setValue('');
		
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
	* Removes a Language posessed by the offered Candidate
	*/
	public removeOfferedCandidateLanguage(language:string):void{
		
		language = language.trim();
		language = language.toUpperCase();
		
		this.spokenLanguages	 = this.spokenLanguages.filter(l => l  !== language);
		
	}
	
	/**
	* Persists and Published the OfferedCandidate	
	*/
	public publishOfferedCandidate():void{

		let candidateRoleTitle:string 	= this.offeredCandidateFormBean.get('candidateRoleTitle')?.value; 
		let country:string 				= this.offeredCandidateFormBean.get('country')?.value;
		let location:string 			= this.offeredCandidateFormBean.get('location')?.value;
		let contractType:string 		= this.offeredCandidateFormBean.get('contractType')?.value;
		let daysOnSite:string 			= this.offeredCandidateFormBean.get('daysOnSite')?.value;
		let renumeration:string 		= this.offeredCandidateFormBean.get('renumeration')?.value;
		let availableFromDate:string 	= this.offeredCandidateFormBean.get('availableFromDate')?.value;	
		let yearsExperience:string 		= this.offeredCandidateFormBean.get('yearsExperience')?.value;	
		let description:string 			= this.offeredCandidateFormBean.get('description')?.value;
		let comments:string 			= this.offeredCandidateFormBean.get('comments')?.value; 			
		
		let languages:Array<string> 	= this.spokenLanguages;
		let skills:Array<string> 		= this.coreSkills;
		
		this.validationErrors			= new Array<string>();
		
		this.marketplaceService.registerOfferedCandidate(
			candidateRoleTitle,
			country,
			location,
			contractType,
			daysOnSite,
			renumeration,
			availableFromDate,
			yearsExperience,
			description,
			comments,
			languages,
			skills
		).subscribe( data => {
			//						this.fetchListings();
									this.resetOfferedCandidatesForm();
									this.switchTab('showSupply');
								}, err => {
									console.log(err);
									if(err.status === 400) {
										
		//								let failedFields:Array<any> = err.error;
										
		//								if (typeof err.error[Symbol.iterator] === 'function') {
		//									failedFields.forEach(failedField => {
		//										this.validationErrors.push(failedField.fieldMessageOrKey);
		//									});
		//									this.open('feedbackBox', "Failure",  false);
		//								} else {
		//									console.log("Failed to Update Listing " + JSON.stringify(err.error));
		//								}
										
									}
										
								});
								
		//} else {
			
			//this.listingService
			//		.updateListing( this.selectedListing.listingId,
			//					ownerName, 
			//					ownerCompany,
			//					ownerEmail,
			//					title,
			//					description,
			//					type,	
			//					country,	
			//					location,	
			//					yearsExperience,
			//					languages,
			//					this.skills,	
			//					rate, 			
			//					currency, 		
			//					false).subscribe( data => {
			//						this.reset();
			//						this.fetchListings();
			//						this.showList();
			//					}, err => {
			//						
			//						if(err.status === 400) {
			//							
			//							let failedFields:Array<any> = err.error;
			//							
			//							if (typeof err.error[Symbol.iterator] === 'function') {
			//								failedFields.forEach(failedField => {
			//									this.validationErrors.push(failedField.fieldMessageOrKey);
			//								});
			//								this.open('feedbackBox', "Failure",  false);
			//							} else {
			//								console.log("Failed to Update Listing " + JSON.stringify(err.error));
			//							}
			//							
			//						}
			//							
			//					});
		//}
	
	//}
		
	}

}