import { Component, ViewChild, OnInit } 		from '@angular/core';
import { UntypedFormGroup, UntypedFormControl }	from '@angular/forms';
import { CandidateServiceService }				from '../candidate-service.service';
import { Candidate }							from './candidate';
import { Recruiter }							from './recruiter';
import { SubscriptionAction }					from './subscription-action';
import { environment }							from '../../environments/environment';
import { Router}								from '@angular/router';
import { NgbModal, NgbModalOptions}				from '@ng-bootstrap/ng-bootstrap';
import { CandidateSkill } 						from './candidate-skill';
import { AppComponent} 							from '../app.component';
import { SupportedCountry } 					from '../supported-candidate';

@Component({
  selector: 'app-accounts',
  templateUrl: './accounts.component.html',
  styleUrls: ['./accounts.component.css']
})
export class AccountsComponent implements OnInit {
	
	@ViewChild('recruiterLoginDetails', { static: false }) private content:any;
	
	recruiters:Array<Recruiter>										= new Array<Recruiter>();
	currentTab:string 												= "recruiters";
	showRecruitersTab:boolean										= true;
	showFlaggedAsUnavailableTab:boolean								= false;
	showSubscriptionActionsTab:boolean								= false;
	showAvailabilityCheckTab:boolean								= false;
	showSkillsValidationTab:boolean									= false;
	showCitiesTab:boolean											= false;
	createAccountDetailsAvailable:boolean 							= false;
	recruiterUsername:string										= '';
	recruiterPassword:string										= '';
	candidates:Array<Candidate>										= new Array<Candidate>();
	skillsToValidate:Array<CandidateSkill>							= new Array<CandidateSkill>();
	activateRecruiterUserId:string									= '';
	activateRecruiterUserPassword:string							= '';
	candidatesToCheckForAvailability:Array<Candidate>				= Array<Candidate>();
	recruitersWithSubscriptionActions:Array<SubscriptionAction>		= new Array<SubscriptionAction>();
	
	public candidateFormGroup:UntypedFormGroup = new UntypedFormGroup({
		firstname:	new UntypedFormControl(''),
		surname: 	new UntypedFormControl(''),
		email:		new UntypedFormControl('')
	});
	
	constructor(
				public candidateService:CandidateServiceService, 
				private modalService: NgbModal, 
				private router: Router,
				private appComponent:AppComponent) {
	 }

  	ngOnInit(): void {
		  this.initSupportedCountries();
	}
	
	private initSupportedCountries():void{
		
		this.supportedCountries = this.candidateService.getSupportedCountries();
		
		
	}

	public switchTab(tab:string){
		
		this.currentTab = tab;
		console.log(">>> " + tab);
		switch(tab){
			
			case "recruiters":{
				this.showCitiesTab					= false;
				this.showRecruitersTab				= true;
				this.showFlaggedAsUnavailableTab	= false;
				this.showSubscriptionActionsTab		= false;
				this.showAvailabilityCheckTab		= false;
				this.showSkillsValidationTab		= false;
				break;
			}
			case "flaggedAsUnavailable":{
				this.showCitiesTab					= false;
				this.showRecruitersTab				= false;
				this.showFlaggedAsUnavailableTab	= true;
				this.showSubscriptionActionsTab		= false;
				this.showAvailabilityCheckTab		= false;
				this.showSkillsValidationTab		= false;
				this.fetchFlaggedAsUnavailableCandidates();
				break;
			}
			case "subscriptionActions":{
				this.showCitiesTab					= false;
				this.showRecruitersTab				= false;
				this.showFlaggedAsUnavailableTab	= false;
				this.showSubscriptionActionsTab		= true;
				this.showAvailabilityCheckTab		= false;
				this.showSkillsValidationTab		= false;
				break;
			}
			case "availabilityChecks":{
				this.showCitiesTab					= false;
				this.showRecruitersTab				= false;
				this.showFlaggedAsUnavailableTab	= false;
				this.showSubscriptionActionsTab		= false;
				this.showAvailabilityCheckTab		= true;
				this.showSkillsValidationTab		= false;
				this.fetchCandidatesDueForAvailabilityCheck();
				break;
			}			
			case "skillValidation":{
				this.showCitiesTab					= false;
				this.showRecruitersTab				= false;
				this.showFlaggedAsUnavailableTab	= false;
				this.showSubscriptionActionsTab		= false;
				this.showAvailabilityCheckTab		= false;
				this.showSkillsValidationTab		= true;
				this.fetchPendingSkills();
				break;
			}
			case "cities":{
				this.showCitiesTab					= true;
				this.showRecruitersTab				= false;
				this.showFlaggedAsUnavailableTab	= false;
				this.showSubscriptionActionsTab		= false;
				this.showAvailabilityCheckTab		= false;
				this.showSkillsValidationTab		= false;
				break;
			}
		}
	}

	/**
	* Builds a query parameter string for flaggedAsUnavailable Candidates
	*/
	private getCandidateFlaggedAsUnavailableFilterParamString():string{
    	
		const filterParams:string = 
								'orderAttribute=candidateId'
                                 + "&order=desc" 
                                 + '&page=0'
                                 + '&size=5000'
								 + '&available=true'
                                 + '&flaggedAsUnavailable=true';

		return filterParams;
	
	}
	
	/**
	*  Returns the url to perform the download of the candidates CV
	*/
	public getCurriculumDownloadUrl(curriculumId:string){
		return  environment.backendUrl + 'curriculum/'+ curriculumId;
	}
	
	public supportedCountries:Array<SupportedCountry>			= new Array<SupportedCountry>();
	
	
	/**
	* Returns the flag css class for the Flag matching
	* the Country 
	*/
	public getFlagClassFromCountry(country:string):string{
		
		let sc:SupportedCountry = this.supportedCountries.filter(c => c.name == country)[0];
		
		return sc ?  "flag-icon-"+sc.iso2Code : '';
		
	}
	
	public fetchCandidatesByFilters(filterParams:string, includeUnavailable:boolean):void{
		
		this.candidates = new Array<Candidate>();

    	this.candidateService.getCandidates(filterParams+ "&available=true").subscribe( data => {
  
      		data.body.content.forEach((c:Candidate) => {
        
        		const candidate:Candidate = new Candidate();

      			candidate.candidateId		= c.candidateId;
  				candidate.firstname			= c.firstname;
				candidate.surname			= c.surname;
				candidate.email				= c.email;
				candidate.available			= c.available;
				
      			this.candidates.push(candidate);
	        
			});

			if (includeUnavailable) {
				/**
				* Quick and dirty as this is only a temp feature until the anonymized accounts
				* are removed
				*/
				this.candidateService.getCandidates(filterParams + "&available=false").subscribe( data => {
			
		      		data.body.content.forEach((c:Candidate) => {
		        
		        		const candidate:Candidate = new Candidate();
		
		      			candidate.candidateId		= c.candidateId;
		  				candidate.firstname			= c.firstname;
						candidate.surname			= c.surname;
						candidate.email				= c.email;
						candidate.available			= c.available;
						
		      			this.candidates.push(candidate);
		
					});
				
				});
			}        
		}, err => {
			
			if (err.status === 401 || err.status === 0) {
				sessionStorage.removeItem('isAdmin');
				sessionStorage.removeItem('isRecruter');
				sessionStorage.removeItem('isCandidate');
				sessionStorage.removeItem('loggedIn');
				sessionStorage.setItem('beforeAuthPage', 'view-candidates');
				this.router.navigate(['login-user']);
			}
    	})

	} 
	
	/**
	* Retrieves candidates from the backend
	*/
	public fetchFlaggedAsUnavailableCandidates(): void{
    	this.fetchCandidatesByFilters(this.getCandidateFlaggedAsUnavailableFilterParamString(), false);
	}
	
	public updateCandidateAvailability(candidateId:string, action:string):void{
		this.candidateService.setCandidateAvailability(candidateId, action).subscribe(() => {
			this.fetchCandidatesDueForAvailabilityCheck();	
			this.appComponent.refreschUnreadAlerts();
		});
	}
	
	/**
	* Retrieves all skills that still need to be validated by an Admin
	*/
	fetchPendingSkills():void{
		
		this.candidateService.fetchSkillsToValidate().subscribe(skills => {
			this.skillsToValidate = skills;	
		});
		
	}
	
	/**
	* Updates the skills status
	*/
	public updateSkillValidationStatus(skill:string, validationStatus:string):void{
		this.skillsToValidate.filter(s => s.skill == skill)[0].validationStatus = validationStatus;
	}

	/**
	* Sends any Skills that have been accepted/rejected to be updated in the backend 
	*/	
	public persistValidatedSkills():void{
		
		let validatedSkills:Array<CandidateSkill> = this.skillsToValidate.filter(s => s.validationStatus != 'PENDING');
		
		this.candidateService.updateValidatedSkills(validatedSkills).subscribe(() => {
			this.fetchPendingSkills();
		});
		
	}
	
	private showAvailable:boolean = true;
	private showUnavailable:boolean = true;
	public availableCount:number = 0;
	public unavailableCount:number = 0;
	
	public toggleShowAvailable():void{
		this.showAvailable = !this.showAvailable;
		this.fetchCandidatesDueForAvailabilityCheck();
	}
	
	public toggleShowUnavailable():void{
		this.showUnavailable = !this.showUnavailable;
		this.fetchCandidatesDueForAvailabilityCheck();
	}
	
	/**
	* Retrieves all the candidates that are due to have their availability checked
	*/
	fetchCandidatesDueForAvailabilityCheck():void{
		
		this.candidatesToCheckForAvailability = new Array<Candidate>();
			
		if (!this.showAvailable && !this.showUnavailable)	{
			this.availableCount = 0;
			this.unavailableCount = 0;
			return;
		}
		
		if (this.showAvailable && !this.showUnavailable) {
			this.candidateService.fetchCandidatesDueForAvailabilityCheck().subscribe(data => {
				this.candidatesToCheckForAvailability = data.content;
				this.availableCount = data.content.length;
				this.unavailableCount = 0;
			}, 
			err => {
				console.log(JSON.stringify(err));		
			});
		}
		
		if (!this.showAvailable && this.showUnavailable) {
			this.candidateService.fetchUnavailableCandidatesDueForAvailabilityCheck().subscribe(data => {
				this.candidatesToCheckForAvailability = data.content;
				this.availableCount = 0;
				this.unavailableCount = data.content.length;
			}, 
			err => {
				console.log(JSON.stringify(err));		
			});
		}
			
		if (this.showAvailable && this.showUnavailable) {
		
		 this.candidateService.fetchCandidatesDueForAvailabilityCheck().subscribe(data => {
			
			this.candidatesToCheckForAvailability = data.content;
			
			this.availableCount = data.content.length;
			
			
			this.candidateService.fetchUnavailableCandidatesDueForAvailabilityCheck().subscribe(candidateData => {
				let candidates  = <Array<Candidate>> candidateData.content;
				candidates.forEach(c => this.candidatesToCheckForAvailability.push(c));
				this.unavailableCount = candidateData.content.length;
			});
			
			}, 
			err => {
				console.log(JSON.stringify(err));		
			});
		
		}
	}
	
	/**
  	*  Closes the filter popup
  	*/
  	public closeModal(): void {
		this.activateRecruiterUserId			= '';
		this.activateRecruiterUserPassword		= '';
    	this.modalService.dismissAll();
  	}

	/**
	* Opens the feedback popup
	*/
	public openModal(): void {
		
    	let options: NgbModalOptions = {
     		centered: true
   		};

  		this.modalService.open(this.content, options);
  	};


	public candidateToDelete:string = '';

	/**
	* Prepares candidate to be deleted and asks for confirmation
	*/
	public deleteCandidate(candidateId:string):void{
		this.candidateToDelete = candidateId;
	}
	
}