import { Component, OnInit } 							from '@angular/core';
import { UntypedFormGroup, UntypedFormControl }						from '@angular/forms';
import { NgbModal, NgbModalOptions}						from '@ng-bootstrap/ng-bootstrap';
import { ViewChild }									from '@angular/core';
import { RecruiterMarketplaceService }					from '../recruiter-marketplace.service';
import { RecruiterService }								from '../recruiter.service';
import { OfferedCandidate }								from './offered-candidate';
import { OpenPosition }									from './open-position';

@Component({
  selector: 'app-recruiter-marketplace',
  templateUrl: './recruiter-marketplace.component.html',
  styleUrls: ['./recruiter-marketplace.component.css']
})
export class RecruiterMarketplaceComponent implements OnInit {

	@ViewChild('feedbackBox', { static: false }) private content:any;

  	constructor(private modalService: NgbModal, private marketplaceService: RecruiterMarketplaceService, private recruiterService:RecruiterService) { }

	/**
	* Sets up initial component
	*/
	ngOnInit(): void {
		
		this.fetchOfferedCandidates();
		this.fetchOpenPositions();
		
 	}

	currentTab:string 						= "downloads";
	showSupply:boolean						= true;
	showDemand:boolean						= false;
	addSupply:boolean						= false;
	editSupply:boolean						= false;
	editDemand:boolean						= false;
	addDemand:Boolean						= false;
	showSupplyDetails:boolean				= false;
	showDemandDetails:boolean				= false;
	
	/**
	* Validation 
	*/
	public feedbackBoxClass:string          			= '';
  	public feedbackBoxTitle                 			= '';
  	public feedbackBoxText:string           			= '';
	public validationErrors:Array<string>				= new Array<string>();

	/**
	* OfferedCandidate - List 
	*/
	public offeredCandidates:Array<OfferedCandidate> 	= new Array<OfferedCandidate>();
	public showJustMyCandidatesActive:boolean			= false;

	/**
	* OpenPositions - List 
	*/
	public openPositions:Array<OpenPosition> 			= new Array<OpenPosition>();
	public showJustMyOpenPositionsActive:boolean		= false;

	/**
	* Fetches only candidates offered by the current Recruiter 
	*/		
	public showJustMyCandidates(){
		this.offeredCandidates = new Array<OfferedCandidate>();
		this.showJustMyCandidatesActive = true;
		this.recruiterService.getOwnRecruiterAccount().subscribe(data => {
			this.marketplaceService.fetchRecruitersOwnOfferedCandidates(data.userId).subscribe(data => {
				this.offeredCandidates = data;
			});	
		});	
	}

	/**
	* Fetches only candidates offered by the current Recruiter 
	*/		
	public showJustMyOpenPositions(){
		this.openPositions = new Array<OpenPosition>();
		this.showJustMyOpenPositionsActive = true;
		this.recruiterService.getOwnRecruiterAccount().subscribe(data => {
			this.marketplaceService.fetchRecruitersOwnOpenPositions(data.userId).subscribe(data => {
				this.openPositions = data;
			});	
		});	
	}
	
	/**
	* Edit Open Position 
	*/	
	public activeOpenPosition:OpenPosition = new OpenPosition();
		
	public confirmDeleteOpenPosition:string = '';
	
	public deleteMyOpenPosition(openPositionId:string):void{
		this.confirmDeleteOpenPosition = openPositionId;
	}
	
	public applyDeleteMyOpenPosition(openPositionId:string):void{		
		this.confirmDeleteOpenPosition = '';
		this.marketplaceService.deleteRecruitersOwnOpenPositions(openPositionId).subscribe(data => {
			this.refreshOpenPositionList();
		});
	}
		
	/**
	* Edit Offered Candidate 
	*/	
	public activeCandidate:OfferedCandidate = new OfferedCandidate();
		
	public confirmDeleteCandidate:string = '';
	
	public deleteMyCandidate(candidateId:string):void{
		this.confirmDeleteCandidate = candidateId;
	}
	
	public applyDeleteMyCandidate(candidateId:string):void{
		this.confirmDeleteCandidate = '';
		this.marketplaceService.deleteRecruitersOwnOfferedCandidates(candidateId).subscribe(data => {
			this.refreshCandidateList();
		});
	}
	
	public refreshCandidateList(){
		if (this.showJustMyCandidatesActive){
			this.showJustMyCandidates();
		} else {
			this.fetchOfferedCandidates();
		}
	}

	public refreshOpenPositionList(){
		if (this.showJustMyOpenPositionsActive){
			this.showJustMyOpenPositions();
		} else {
			this.fetchOpenPositions();
		}
	}
		
	public editMyCandidate(candidateId:string):void{
		this.confirmDeleteCandidate = '';
	}
		
	/**
	* OfferedCandidate - Add details 
	*/
	coreSkills:Array<string>				= new Array<string>();
	spokenLanguages:Array<string>			= new Array<string>();
	
	public offeredCandidateFormBean:UntypedFormGroup = new UntypedFormGroup({
     	candidateRoleTitle:		new UntypedFormControl(''),
		country:				new UntypedFormControl(),
       	location:				new UntypedFormControl(),
		contractType:			new UntypedFormControl(),	
		daysOnSite:				new UntypedFormControl(),
		renumeration:			new UntypedFormControl(),
		availableFromDate:		new UntypedFormControl(),
		yearsExperience:		new UntypedFormControl(),
		description:			new UntypedFormControl(),
		comments:				new UntypedFormControl(),
		skill:					new UntypedFormControl(),
		language:				new UntypedFormControl(),
		
	});
	
	/**
	* RequestedCandidate - Add details 
	*/
	requestedCandidateCoreSkills:Array<string>				= new Array<string>();
	requestedCandidateSpokenLanguages:Array<string>			= new Array<string>();
	
	public requestedCandidateFormBean:UntypedFormGroup = new UntypedFormGroup({
    	positionTitle:			new UntypedFormControl(''),
		country:				new UntypedFormControl(),
       	location:				new UntypedFormControl(),
		contractType:			new UntypedFormControl(),	
		renumeration:			new UntypedFormControl(),
		startDate:				new UntypedFormControl(),
		positionClosingDate:	new UntypedFormControl(),
		description:			new UntypedFormControl(),
		comments:				new UntypedFormControl(),
	});
	
	/**
	* Resets the OfferedCandudates FormBean
	*/
	private resetOpenPositionsForm(){
			this.requestedCandidateFormBean = new UntypedFormGroup({
	     	positionTitle:			new UntypedFormControl(''),
			country:				new UntypedFormControl(),
       		location:				new UntypedFormControl(),
			contractType:			new UntypedFormControl(),	
			renumeration:			new UntypedFormControl(),
			startDate:				new UntypedFormControl(),
			positionClosingDate:	new UntypedFormControl(),
			description:			new UntypedFormControl(),
			comments:				new UntypedFormControl(),
		});
		this.requestedCandidateSpokenLanguages 	= new Array<string>();
		this.requestedCandidateCoreSkills 		= new Array<string>();
	}
	
	/**
	* Resets the OfferedCandudates FormBean
	*/
	private resetOfferedCandidatesForm(){
			this.offeredCandidateFormBean = new UntypedFormGroup({
	     	candidateRoleTitle:		new UntypedFormControl(''),
			country:				new UntypedFormControl(),
	       	location:				new UntypedFormControl(),
			contractType:			new UntypedFormControl(),	
			daysOnSite:				new UntypedFormControl(),
			renumeration:			new UntypedFormControl(),
			availableFromDate:		new UntypedFormControl(),
			yearsExperience:		new UntypedFormControl(),
			description:			new UntypedFormControl(),
			comments:				new UntypedFormControl(),
			skill:					new UntypedFormControl(),
			language:				new UntypedFormControl(),
			
		});
		this.spokenLanguages 	= new Array<string>();
		this.coreSkills 		= new Array<string>();
	}

	/**
	* Populates the OfferedCandidate form with details of the 
	* selected OfferedCandidate in order to edit the OfferedCandidates
	* details
	*/
	public editOfferedCandidate(candidate:OfferedCandidate){
		
		this.switchTab('editSupply');
		this.activeCandidate = candidate;
		
		this.offeredCandidateFormBean = new UntypedFormGroup({
	     	candidateRoleTitle:		new UntypedFormControl(candidate.candidateRoleTitle),
			country:				new UntypedFormControl(candidate.country),
	       	location:				new UntypedFormControl(candidate.location),
			contractType:			new UntypedFormControl(candidate.contractType),	
			daysOnSite:				new UntypedFormControl(candidate.daysOnSite),
			renumeration:			new UntypedFormControl(candidate.renumeration),
			availableFromDate:		new UntypedFormControl(candidate.availableFromDate),
			yearsExperience:		new UntypedFormControl(candidate.yearsExperience),
			description:			new UntypedFormControl(candidate.description),
			comments:				new UntypedFormControl(candidate.comments),
			skill:					new UntypedFormControl(),
			language:				new UntypedFormControl()});
			
			this.coreSkills 		= candidate.coreSkills;
			this.spokenLanguages 	= candidate.spokenLanguages;
		
	}

	/**
	* Populates the OfferedCandidate form with details of the 
	* selected OfferedCandidate in order to edit the OfferedCandidates
	* details
	*/
	public editOpenPosition(openPosition:OpenPosition){
		
		this.switchTab('editDemand');
		this.activeOpenPosition = openPosition;
		
		this.requestedCandidateFormBean = new UntypedFormGroup({
	     	positionTitle:			new UntypedFormControl(openPosition.positionTitle),
			country:				new UntypedFormControl(openPosition.country),
	       	location:				new UntypedFormControl(openPosition.location),
			contractType:			new UntypedFormControl(openPosition.contractType),	
			renumeration:			new UntypedFormControl(openPosition.renumeration),
			startDate:				new UntypedFormControl(openPosition.startDate),
			positionClosingDate:	new UntypedFormControl(openPosition.positionClosingDate),
			description:			new UntypedFormControl(openPosition.description),
			comments:				new UntypedFormControl(openPosition.comments),
			});
	}
		
	/**
	* Shows details of selected Offered Candidate
	*/
	public viewCandidate(candidate:OfferedCandidate){
		this.activeCandidate = candidate;
		this.switchTab('showSupplyDetails');
		this.marketplaceService.registerOfferedCandidateViewedEvent(candidate.id).subscribe( data => {});
	}
	
	/**
	* Shows details of selected Offered Candidate
	*/
	public viewOpenPosition(openPosition:OpenPosition){
		this.activeOpenPosition = openPosition;
		this.switchTab('showDemandDetails');
		this.marketplaceService.registerOpenPositionViewedEvent(openPosition.id).subscribe( data => {});
	}

	/**
	* Returns the previous OfferedCandidate list (all/own candidate)
	*/		
	public showOfferedCandidates(){
		this.switchTab("showSupply")
		this.resetOfferedCandidatesForm();
	}
	
	/**
	* Returns the previous OpenPositions list (all/own candidate)
	*/		
	public showOpenPositions(){
		this.switchTab("showDemand")
		this.resetOpenPositionsForm();
	}
	
	/**
	* Switches to the selected tab
	*/
	public switchTab(tab:string){
		
		this.currentTab 				= tab;
		//this.showJustMyCandidatesActive = false;
		this.confirmDeleteCandidate 	= '';
		
		switch(tab){
			case "showSupply":{
				this.showSupply			= true;
				this.showDemand			= false;
				this.addSupply			= false;
				this.addDemand			= false;
				this.editSupply 		= false;
				this.editDemand			= false;
				this.showSupplyDetails 	= false;
				this.showDemandDetails 	= false;
				break;
			}
			case "showDemand":{
				this.showSupply			= false;
				this.showDemand			= true;
				this.addSupply			= false;
				this.addDemand			= false;
				this.editSupply 		= false;
				this.editDemand			= false;
				this.showSupplyDetails	= false;
				this.showDemandDetails 	= false;
				break;
			}
			case "addSupply":{
				this.showSupply			= false;
				this.showDemand			= false; 
				this.addSupply			= true;
				this.addDemand			= false;
				this.editSupply 		= false;
				this.editDemand			= false;
				this.showSupplyDetails 	= false;
				this.showDemandDetails 	= false;
				break;
			}
			case "editSupply":{
				this.showSupply			= false;
				this.showDemand			= false; 
				this.addSupply			= false;
				this.addDemand			= false;
				this.editSupply 		= true;
				this.editDemand			= false;
				this.showSupplyDetails 	= false;
				this.showDemandDetails 	= false;
				break;
			}
			case "addDemand":{
				this.showSupply			= false;
				this.showDemand			= false;
				this.addSupply			= false;
				this.addDemand			= true;
				this.editSupply 		= false;
				this.editDemand			= false;
				this.showSupplyDetails 	= false;
				this.showDemandDetails 	= false;
				break;
			}
			case "showSupplyDetails":{
				this.showSupply			= false;
				this.showDemand			= false;
				this.addSupply			= false;
				this.addDemand			= false;
				this.editSupply 		= false;
				this.editDemand			= false;
				this.showSupplyDetails 	= true;
				this.showDemandDetails 	= false;
				break;
			}
			case "showDemandDetails":{
				this.showSupply			= false;
				this.showDemand			= false;
				this.addSupply			= false;
				this.addDemand			= false;
				this.editSupply 		= false;
				this.editDemand			= false;
				this.showSupplyDetails 	= false;
				this.showDemandDetails 	= true;
				break;
			}
			case "editDemand":{
				this.showSupply			= false;
				this.showDemand			= false;
				this.addSupply			= false;
				this.addDemand			= false;
				this.editSupply 		= false;
				this.editDemand			= true;
				this.showSupplyDetails 	= false;
				this.showDemandDetails 	= false;
				break;
			}
		}
		
	}
	
	/**
  	* Whether or not the user has authenticated as an Admin user 
  	*/
  	public isAuthenticatedAsAdmin():boolean {
    	return sessionStorage.getItem('isAdmin') === 'true';
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
	* Fetches available Candidates offered by other Recruiters
	*/
	public fetchOfferedCandidates():void{
		this.offeredCandidates 			= new Array<OfferedCandidate>();
		this.showJustMyCandidatesActive = false;
		this.marketplaceService.fetchOfferedCandidates().subscribe(data => {
			this.offeredCandidates = data;
		});
	}

	/**
	* Fetches available Open Positions
	*/	
	public fetchOpenPositions():void{
		this.openPositions 					= new Array<OpenPosition>();
		this.showJustMyOpenPositionsActive 	= false;
		this.marketplaceService.fetchOpenPositions().subscribe(data => {
			this.openPositions = data;
		});
	}
	
	/**
	* Persists and Published the OfferedCandidate	
	*/
	public publishOpenPosition():void{

		let positionTitle:string 		= this.requestedCandidateFormBean.get('positionTitle')?.value; 
		let country:string 				= this.requestedCandidateFormBean.get('country')?.value;
		let location:string 			= this.requestedCandidateFormBean.get('location')?.value;
		let contractType:string 		= this.requestedCandidateFormBean.get('contractType')?.value;
		let renumeration:string 		= this.requestedCandidateFormBean.get('renumeration')?.value;
		let startDate:Date 				= this.requestedCandidateFormBean.get('startDate')?.value;
		let positionClosingDate:Date 	= this.requestedCandidateFormBean.get('positionClosingDate')?.value;	
		let description:string 			= this.requestedCandidateFormBean.get('description')?.value;
		let comments:string 			= this.requestedCandidateFormBean.get('comments')?.value; 			
		
		let languages:Array<string> 	= this.requestedCandidateSpokenLanguages;
		let skills:Array<string> 		= this.requestedCandidateCoreSkills;
		
		this.validationErrors			= new Array<string>();
		
		if (this.addDemand) {
			this.marketplaceService.registerOpenPosition(
				positionTitle,
				country,
				location,
				contractType,
				renumeration,
				startDate,
				positionClosingDate,
				description,
				comments,
				languages,
				skills
			).subscribe( data => {
				this.resetOpenPositionsForm();
				this.switchTab('showDemand');
				this.refreshOpenPositionList();
			}, err => {
				console.log(err);
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
						
		} else {
			
			this.marketplaceService.updateOpenPosition(
				this.activeOpenPosition.id,
				positionTitle,
				country,
				location,
				contractType,
				renumeration,
				startDate,
				positionClosingDate,
				description,
				comments,
				languages,
				skills
			).subscribe( data => {
				this.resetOpenPositionsForm();
				this.switchTab('showDemand');
				this.refreshOpenPositionList();
			}, err => {
				console.log(err);
				if (err.status === 400) {
											
					let failedFields:Array<any> = err.error;
											
					if (typeof err.error[Symbol.iterator] === 'function') {
						failedFields.forEach(failedField => {
							this.validationErrors.push(failedField.issue);
						});
						this.open('feedbackBox', "Failure",  false);
					} else {
						console.log("Failed to update Open Position " + JSON.stringify(err.error));
					}
				}
											
			});
		}
	
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
		
		if (this.addSupply) {
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
				this.resetOfferedCandidatesForm();
				this.switchTab('showSupply');
				this.refreshCandidateList();
			}, err => {
				console.log(err);
				if (err.status === 400) {
											
					let failedFields:Array<any> = err.error;
											
					if (typeof err.error[Symbol.iterator] === 'function') {
						failedFields.forEach(failedField => {
							this.validationErrors.push(failedField.issue);
						});
						this.open('feedbackBox', "Failure",  false);
					} else {
						console.log("Failed to persist new OfferedCandidate " + JSON.stringify(err.error));
					}
				}
											
			});
						
		} else {
			
			this.marketplaceService.updateOfferedCandidate(
				this.activeCandidate.id,
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
				this.resetOfferedCandidatesForm();
				this.switchTab('showSupply');
				this.refreshCandidateList();
			}, err => {
				
				if (err.status === 400) {
											
					let failedFields:Array<any> = err.error;
											
					if (typeof err.error[Symbol.iterator] === 'function') {
						failedFields.forEach(failedField => {
							this.validationErrors.push(failedField.issue);
						});
						this.open('feedbackBox', "Failure",  false);
					} else {
						console.log("Failed to update OfferedCandidate " + JSON.stringify(err.error));
					}
				}
											
			});
		}
	
	}

}