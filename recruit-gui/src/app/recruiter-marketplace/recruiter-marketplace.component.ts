import { Component, OnInit } 							from '@angular/core';
import { UntypedFormGroup, UntypedFormControl }			from '@angular/forms';
import { NgbModal, NgbModalOptions}						from '@ng-bootstrap/ng-bootstrap';
import { ViewChild }									from '@angular/core';
import { RecruiterMarketplaceService }					from '../recruiter-marketplace.service';
import { RecruiterService }								from '../recruiter.service';
import { EmailService, EmailRequest }					from '../email.service';
import { OfferedCandidate }								from './offered-candidate';
import { OpenPosition }									from './open-position';
import { Router}										from '@angular/router';
import { CandidateServiceService }						from '../candidate-service.service';
import { RecruiterProfileService} 						from '../recruiter-profile.service';
import { RecruiterProfile }								from '../recruiter-profile/recruiter-profile';
import { Candidate } 																	from '../suggestions/candidate';
import { CandidateNavService } 															from '../candidate-nav.service';
import { CreditsService } 																from '../credits.service';
import { InfoItemBlock, InfoItemConfig, InfoItemRowKeyValue, InfoItemRowMultiValues } 	from '../candidate-info-box/info-item';
import { TranslateService } from '@ngx-translate/core';
import { SupportedCountry } from '../supported-candidate';

@Component({
  selector: 'app-recruiter-marketplace',
  templateUrl: './recruiter-marketplace.component.html',
  styleUrls: ['./recruiter-marketplace.component.css']
})
export class RecruiterMarketplaceComponent implements OnInit {

	@ViewChild('feedbackBox', { static: false }) private feedbackBox:any;
	@ViewChild('contactBox', { static: false }) private contactBox:any;
	@ViewChild('specUploadBox', { static: false }) private specUploadBox:any;
	@ViewChild('mpPublicityBox', { static: false }) private mpPublicityBox:any;

	public unseenOfferedCandidates:number 				= 0;
	public unseenOpenPositions:number 					= 0;
	public isMobile:boolean 							= false;
	public suggestionMobileBtnClass:string				= '';
	public mobileListingLeftPaneContainer:string 		= '';
	public mobileDescBody:string 						= '';
	public mobileLeftBox:string 						= '';
	public mobileListingViewTitle:string 				= '';
	public recruiterProfiles:Array<RecruiterProfile> 	= new Array<RecruiterProfile>();
	public recruiterProfile:RecruiterProfile 			= new RecruiterProfile();
	public infoItemConfig:InfoItemConfig 				= new InfoItemConfig();
	public supportedCountries:Array<SupportedCountry>	= new Array<SupportedCountry>();

  	constructor(private modalService: 				NgbModal, 
				private marketplaceService: 		RecruiterMarketplaceService, 
				private recruiterService:			RecruiterService,
				private emailService:				EmailService,
				private router:						Router,
				public 	candidateService:			CandidateServiceService,
				private recruiterProfileService: 	RecruiterProfileService,
				private candidateNavService: 		CandidateNavService,
				private creditsService:				CreditsService,
				private translate:					TranslateService) {
					
					this.marketplaceService.fetchUnseenOfferedCandidates().subscribe(val => {
						this.unseenOfferedCandidates = val;
						this.showJustMyCandidates();
					});
					
					this.marketplaceService.fetchUnseenOpenPositions().subscribe(val => {
						this.unseenOpenPositions = val;
						this.fetchOpenPositions();
					});
					
					//TODO: [KP] Fetching own profile not profile of postion/candidate
					this.recruiterProfileService.fetchRecruiterProfiles("RECRUITERS").subscribe(rps => this.recruiterProfiles = rps);
					
					//Refactor into central navigation logic service
					
					if (this.candidateNavService.isRouteActive()) {
						this.switchTab('showSupply');
					} 
					
					this.doCreditCheck();
					
	}

	/**
	* Sets up initial component
	*/
	ngOnInit(): void {
		this.fetchOpenPositions();
		this.showJustMyCandidates();
		this.supportedCountries = this.candidateService.getSupportedCountries();
	}

	currentTab:string 						= "downloads";
	showSupply:boolean						= false;
	showDemand:boolean						= true;
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
	//public offeredCandidates:Array<OfferedCandidate> 	= new Array<OfferedCandidate>();
	public offeredCandidates:Array<Candidate> 	= new Array<Candidate>();
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
		if (this.isRecruiter()) {
			this.offeredCandidates = new Array<Candidate>();
			this.showJustMyCandidatesActive = true;
			this.recruiterService.getOwnRecruiterAccount().subscribe(data => {
				this.candidateService.getCandidates("orderAttribute=candidateId&order=desc&ownerId="+sessionStorage.getItem("userId")).subscribe(candidates =>{
					this.offeredCandidates = candidates.body.content;
				});
			});	
		}
	}
	
	public getIso2Code(candidate:Candidate):string{
		return this.supportedCountries.filter(c => c.name == candidate.country)[0].iso2Code;
	}

	/**
	* Fetches only candidates offered by the current Recruiter 
	*/		
	public showJustMyOpenPositions(){
		if(this.isRecruiter()) {
			this.openPositions = new Array<OpenPosition>();
			this.showJustMyOpenPositionsActive = true;
			this.recruiterService.getOwnRecruiterAccount().subscribe(data => {
				this.marketplaceService.fetchRecruitersOwnOpenPositions(data.userId).subscribe(data => {
					this.openPositions = data;
				});	
			});	
		}
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
			this.switchTab('showDemand');
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
			this.switchTab('showSupply');
		});
	}
	
	public refreshCandidateList(){
		if (this.showJustMyCandidatesActive){
			this.showJustMyCandidates();
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
	* Forms group for sending a message to a Recruiter relating to a
	* specific post on the jobboard 
	*/
	public sendMessageGroup:UntypedFormGroup = new UntypedFormGroup({
		message:				new UntypedFormControl('')
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
		skill:					new UntypedFormControl(),
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
			skill:					new UntypedFormControl(),
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
		sessionStorage.setItem("last-page", 'rec-mp');
		sessionStorage.setItem("mp-edit-candidate", candidate.id);
		this.router.navigate(['new-candidate']);
	}

	/**
	* Populates the OfferedCandidate form with details of the 
	* selected OfferedCandidate in order to edit the OfferedCandidates
	* details
	*/
	public editOpenPosition(openPosition:OpenPosition){
		
		this.switchTab('editDemand');
		this.activeOpenPosition = openPosition;
		
		this.requestedCandidateCoreSkills = new Array<string>();
		this.requestedCandidateCoreSkills = this.activeOpenPosition.skills;
		
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
			skill:					new UntypedFormControl(),
			});
	}
		
	/**
	* Shows details of selected Offered Candidate
	*/
	//public viewCandidate(candidate:OfferedCandidate){
	public viewCandidate(candidate:Candidate){
		this.candidateNavService.startCandidateProfileRouteForRecruiter();
		this.candidateNavService.doNextMove("view", candidate.candidateId);
	}
	
	/**
	* Shows details of selected Offered Candidate
	*/
	public viewOpenPosition(openPosition:OpenPosition){
		this.activeOpenPosition = openPosition;
		this.switchTab('showDemandDetails');
		this.marketplaceService.registerOpenPositionViewedEvent(openPosition.id).subscribe( data => {
			this.marketplaceService.updateUnseenMpPosts();
		});
		
		this.recruiterProfile = new RecruiterProfile();
		
		this.recruiterProfile = this.recruiterProfiles.filter(p => p.recruiterId == openPosition.recruiter.recruiterId)[0];
		
		
		this.infoItemConfig = new InfoItemConfig();
		this.infoItemConfig.setProfilePhoto(this.recruiterProfile?.profilePhoto?.imageBytes);
		
		this.infoItemConfig.setShowContactButton(!this.isMyOpenPosition());
		 
		if (this.activeOpenPosition?.recruiter?.recruiterName) {
			let recruiterBlock:InfoItemBlock = new InfoItemBlock();
			recruiterBlock.setTitle(this.translate.instant('mp-left-mnu-requested-by'));//"Requested By"
			recruiterBlock.addRow(new InfoItemRowKeyValue(this.translate.instant('mp-left-mnu-name'),this.activeOpenPosition?.recruiter?.recruiterName));
			recruiterBlock.addRow(new InfoItemRowKeyValue(this.translate.instant('mp-left-mnu-company'),this.activeOpenPosition?.recruiter?.companyName));
			this.infoItemConfig.addItem(recruiterBlock);
		}
		
		if (this.activeOpenPosition.country || this.activeOpenPosition.location) {
			let recruiterBlock:InfoItemBlock = new InfoItemBlock();
			recruiterBlock.setTitle(this.translate.instant('mp-left-mnu-location'));//Location
			if (this.activeOpenPosition.country) {
				recruiterBlock.addRow(new InfoItemRowKeyValue(this.translate.instant('mp-left-mnu-country'),this.getCountryCode(this.activeOpenPosition.country)));
			}
			if (this.activeOpenPosition.location) {
				recruiterBlock.addRow(new InfoItemRowKeyValue(this.translate.instant('mp-left-mnu-city'),this.activeOpenPosition.location));
			}
			this.infoItemConfig.addItem(recruiterBlock);
		}
		
		if ( this.activeOpenPosition.skills.length > 0) {
			let skillsBlock:InfoItemBlock = new InfoItemBlock();
			skillsBlock.setTitle(this.translate.instant('mp-left-mnu-requested-skills'));
			skillsBlock.addRow(new InfoItemRowMultiValues(this.activeOpenPosition.skills, "skill"));
			this.infoItemConfig.addItem(skillsBlock);
		}
		
		if (this.activeOpenPosition.contractType || this.activeOpenPosition.startDate || this.activeOpenPosition.renumeration) {
			let conditionsBlock:InfoItemBlock = new InfoItemBlock();
			conditionsBlock.setTitle(this.translate.instant('mp-left-mnu-condistons'));
			if (this.activeOpenPosition.contractType) {
				conditionsBlock.addRow(new InfoItemRowKeyValue(this.translate.instant('mp-left-mnu-contract-type'),this.getContractType(this.activeOpenPosition.contractType)));
			}
			if (this.activeOpenPosition.startDate) {
				conditionsBlock.addRow(new InfoItemRowKeyValue(this.translate.instant('mp-left-mnu-start-date'),""+this.activeOpenPosition.startDate));
			}
			if (this.activeOpenPosition.renumeration) {
				conditionsBlock.addRow(new InfoItemRowKeyValue(this.translate.instant('mp-left-mnu-renumerationo'),this.activeOpenPosition.renumeration));
			}
			
			this.infoItemConfig.addItem(conditionsBlock);
		}
		
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
		this.confirmDeleteCandidate 	= '';
		
		switch(tab){
			case "showSupply":{
				this.showSupply			= true;
				this.showDemand			= false;
				this.addDemand			= false;
				this.editSupply 		= false;
				this.editDemand			= false;
				this.showSupplyDetails 	= false;
				this.showDemandDetails 	= false;
				this.showJustMyCandidates();
				break;
			}
			case "showDemand":{
				this.showSupply			= false;
				this.showDemand			= true;
				this.addDemand			= false;
				this.editSupply 		= false;
				this.editDemand			= false;
				this.showSupplyDetails	= false;
				this.showDemandDetails 	= false;
				break;
			}
			case "addSupply":{
				sessionStorage.setItem("last-page", 'rec-mp');
				this.router.navigate(['new-candidate']);
				break;
			}
			case "editSupply":{
				this.showSupply			= false;
				this.showDemand			= false; 
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
	* Adds a skill posessed required 
	*/
	public addOfferedPositionSkill():void{
	
		let skillFormatted:string 	= this.requestedCandidateFormBean.get('skill')?.value.trim();
		skillFormatted 				= skillFormatted.toLocaleLowerCase();
		
		if (skillFormatted.length > 0 && this.coreSkills.indexOf(skillFormatted) == -1) {
			this.requestedCandidateCoreSkills.push(skillFormatted);	
		}
		
		this.requestedCandidateFormBean.get('skill')?.setValue('');
		
	}
	
	/**
	* Removes a skill posessed by the offered Candidate
	*/
	public removeRequestedCandidateSkill(skill:string):void{
		
		skill = skill.trim();
		skill = skill.toLocaleLowerCase();
		
		this.requestedCandidateCoreSkills = this.requestedCandidateCoreSkills.filter(s => s  !== skill);
		
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
	public open(success:boolean):void {
		
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
		this.contactBox.nativeElement.close();
		this.specUploadBox.nativeElement.close();
		this.mpPublicityBox.nativeElement.close();
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
				this.doCreditCheck();
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
						this.open(false);
					} else {
						console.log(this.translate.instant('mp-left-failure-failed-to-persits-new-open-position') + " " + JSON.stringify(err.error));
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
						this.open(false);
					} else {
						console.log(this.translate.instant('mp-failure-failed-to-update-open-position') + " " + JSON.stringify(err.error));
					}
				}
											
			});
		}
	
	}
	
	public contactRecruiterView:string = 'message';
	
	/**
	* Opend dialog to contact recuiter posting	
	*/
	public contactRecruiter():void{
		this.contactRecruiterView = 'message';
		this.contactBox.nativeElement.showModal();
	}
	
	/**
	* Opend dialog to contact recuiter posting	
	*/
	public sendMessageToRecruiter():void{
		
		let emailRequest:EmailRequest = new EmailRequest();
		
		emailRequest.message = this.sendMessageGroup.get('message')?.value;;
		
		if (this.showSupplyDetails) {
			this.emailService.sendMarketplaceContactRequestOfferedCandidateEmail(emailRequest, this.activeCandidate).subscribe(body => {
				this.contactRecruiterView = 'success';
				this.sendMessageGroup = new UntypedFormGroup({
					message: new UntypedFormControl('')
				});
			}, err => {
				this.contactRecruiterView = 'failure';
			});
		}
		
		if (this.showDemandDetails) {
			this.emailService.sendMarketplaceContactRequestOpenPositionEmail(emailRequest, this.activeOpenPosition).subscribe(body => {
				this.contactRecruiterView = 'success';
				this.sendMessageGroup = new UntypedFormGroup({
					message: new UntypedFormControl('')
				});
			}, err => {
				this.contactRecruiterView = 'failure';
			});
		}
		
	}
	
	/**
	* Returns whtehr the candidate being offered is owned by the
	* logged in user	
	*/
	public isMyCandidate():boolean{
		return this.activeCandidate.recruiter.recruiterId === sessionStorage.getItem("userId");
	}
	
	/**
	* Returns whtehr the open Position being offered is owned by the
	* logged in user	
	*/
	public isMyOpenPosition():boolean{
		return this.activeOpenPosition.recruiter.recruiterId === sessionStorage.getItem("userId");
	
	}
	
	/**
	* Returns whehter or not the open position is the users own open position	
	*/
	public isNotMineOP(openPosition:OpenPosition):boolean{
		return !(sessionStorage.getItem("userId")+'' == openPosition.recruiter.recruiterId);
	}
	
	/**
	* Returns whehter or not the offered Candidate is the users own offered candidate	
	*/
	public isNotMineOC(offeredCandidate:OfferedCandidate):boolean{
		return !(sessionStorage.getItem("userId")+'' == offeredCandidate.recruiter.recruiterId);
	}
	
	/**
	* Displays dialog to create an alert for the current search critera
	*/
	public showFilterByJobSpecDialog():void{
		
		this.showFilterByJonSpecFailure  	= false;
		this.showFilterByJobSpec 			= true;
		this.specUploadBox.nativeElement.showModal();
	}
	
	/**
	* Shows the publicity box
	*/
	public showPublicity():void{
		this.mpPublicityBox.nativeElement.showModal();
	}
	
	private jobSpecFile!:File;
	public showFilterByJonSpecFailure:boolean  	= false;
	public showFilterByJobSpec:boolean 				= false;
	
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
  			
		this.requestedCandidateCoreSkills = extractedFilters.skills;
			
			let freelance 	= extractedFilters.freelance 	== 'TRUE' ? true : false;
			let perm 		= extractedFilters.perm 		== 'TRUE' ? true : false;
			
			let netherlands = extractedFilters.netherlands;
			let uk 			= extractedFilters.uk;
			let belgium 	= extractedFilters.belgium;
			let ireland 	= extractedFilters.ireland;
			
			let country = netherlands ? "NETHERLANDS" : uk ? "UK" : belgium ? "BELGIUM" : ireland ? "IRELAND" : "";
			
			let type = (freelance && perm) ? "BOTH" : freelance ? "CONTRACT" : "PERM";
			
			this.requestedCandidateFormBean.get("positionTitle")?.setValue(extractedFilters.jobTitle);
			this.requestedCandidateFormBean.get("contractType")?.setValue(type);
			this.requestedCandidateFormBean.get("experienceYears")?.setValue(extractedFilters.experienceGTE);
			this.requestedCandidateFormBean.get("country")?.setValue(country);
			
			this.requestedCandidateFormBean.get("langDutch")?.setValue(extractedFilters.dutch);
			this.requestedCandidateFormBean.get("langEnglish")?.setValue(extractedFilters.english);
			this.requestedCandidateFormBean.get("langFrench")?.setValue(extractedFilters.french);
			
			this.requestedCandidateFormBean.get("description")?.setValue(extractedFilters.extractedText);
			
			this.closeModal();
		
		},(failure =>{
			this.showFilterByJonSpecFailure 	= true;
			this.showFilterByJobSpec 			= false;		
		}));
  		
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
			case "CONTRACT":{
				return "Contract";
			}
			case "PERM":{
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
  	
  	public passedCreditCheck:boolean = false;
	
	
	public doCreditCheck():void{
		this.marketplaceService.doCreditCheck().subscribe(passed => {
			this.passedCreditCheck = passed;
		});
	}
	
	public showNoCredits():void{
		this.creditsService.tokensExhaused();
	}
	
	/**
	* Whether or not User is a Recruiter
	*/
	public isRecruiter():boolean{
		return sessionStorage.getItem('isRecruiter') === 'true';
	}

}