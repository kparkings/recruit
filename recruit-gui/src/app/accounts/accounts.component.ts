import { Component, ViewChild, OnInit } 		from '@angular/core';
import { UntypedFormGroup, UntypedFormControl }	from '@angular/forms';
import { RecruiterService }						from '../recruiter.service';
import { CandidateServiceService }				from '../candidate-service.service';
import { Candidate }							from './candidate';
import { Recruiter }							from './recruiter';
import { Subscription }							from './subscription';
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
	
	currentTab:string 												= "recruiters";
	showRecruitersTab:boolean										= true;
	showFlaggedAsUnavailableTab:boolean								= false;
	showSubscriptionActionsTab:boolean								= false;
	showAvailabilityCheckTab:boolean								= false;
	showSkillsValidationTab:boolean									= false;
	createAccountDetailsAvailable:boolean 							= false;
	recruiterUsername:string										= '';
	recruiterPassword:string										= '';
	candidates:Array<Candidate>										= new Array<Candidate>();
	recruiters:Array<Recruiter>										= new Array<Recruiter>();
	recruitersWithSubscriptionActions:Array<SubscriptionAction>		= new Array<SubscriptionAction>();
	skillsToValidate:Array<CandidateSkill>							= new Array<CandidateSkill>();
	recruiterCount:number											= 0;
	activateRecruiterUserId:string									= '';
	activateRecruiterUserPassword:string							= '';
	expiredSubscriptionRecruiters:Array<Recruiter>					= Array<Recruiter>();
	activePaidSubscription:Array<Recruiter>							= Array<Recruiter>();
	creditBasedSubscription:Array<Recruiter>						= Array<Recruiter>();
	firstGenActiveSubscription:Array<Recruiter>						= Array<Recruiter>();
	candidatesToCheckForAvailability:Array<Candidate>				= Array<Candidate>();
	
	showExpiredSubscriptionRecruiters:boolean 						= true;
	showActivePaidSubscription:boolean								= true;
	showCreditBasedSubscription:boolean								= true;
	showFirstGenActiveSubscription:boolean							= true;
	
	
	public candidateFormGroup:UntypedFormGroup = new UntypedFormGroup({
		firstname:	new UntypedFormControl(''),
		surname: 	new UntypedFormControl(''),
		email:		new UntypedFormControl('')
	});
	
	constructor(private recruiterService:RecruiterService, 
				public candidateService:CandidateServiceService, 
				private modalService: NgbModal, 
				private router: Router,
				private appComponent:AppComponent) {
		this.fetchRecruiters();
	 }

  	ngOnInit(): void {
		  this.initSupportedCountries();
	}
	
	private initSupportedCountries():void{
		
		this.supportedCountries = this.candidateService.getSupportedCountries();
		
		
	}

	public switchTab(tab:string){
		
		this.currentTab = tab;
		
		switch(tab){
			
			case "recruiters":{
				this.showRecruitersTab				= true;
				this.showFlaggedAsUnavailableTab	= false;
				this.showSubscriptionActionsTab		= false;
				this.showAvailabilityCheckTab		= false;
				this.showSkillsValidationTab		= false;
				this.fetchRecruiters();
				break;
			}
			case "flaggedAsUnavailable":{
				this.showRecruitersTab				= false;
				this.showFlaggedAsUnavailableTab	= true;
				this.showSubscriptionActionsTab		= false;
				this.showAvailabilityCheckTab		= false;
				this.showSkillsValidationTab		= false;
				this.fetchFlaggedAsUnavailableCandidates();
				break;
			}
			case "subscriptionActions":{
				this.showRecruitersTab				= false;
				this.showFlaggedAsUnavailableTab	= false;
				this.showSubscriptionActionsTab		= true;
				this.showAvailabilityCheckTab		= false;
				this.showSkillsValidationTab		= false;
				this.fetchRecruiters();
				break;
			}
			case "availabilityChecks":{
				this.showRecruitersTab				= false;
				this.showFlaggedAsUnavailableTab	= false;
				this.showSubscriptionActionsTab		= false;
				this.showAvailabilityCheckTab		= true;
				this.showSkillsValidationTab		= false;
				this.fetchCandidatesDueForAvailabilityCheck();
				break;
			}			
			case "skillValidation":{
				this.showRecruitersTab				= false;
				this.showFlaggedAsUnavailableTab	= false;
				this.showSubscriptionActionsTab		= false;
				this.showAvailabilityCheckTab		= false;
				this.showSkillsValidationTab		= true;
				this.fetchPendingSkills();
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
	* Filter param for firstname
	*/
	private getFirstnameParamString():string{
		let value:string = this.candidateFormGroup.get("firstname")?.value;
		return value  == '' ? '' : '&firstname='+value;
	}
	
	/**
	* Filter param for surname 
	*/
	private getSurnameParamString():string{
		let value:string = this.candidateFormGroup.get("surname")?.value;
		return value  == '' ? '' : '&surname='+value;
	}
	
	/**
	* Filter param for email
	*/
	private getEmailParamString():string{
		let value:string = this.candidateFormGroup.get("email")?.value;
		return value  == '' ? '' : '&email='+value;
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
	* Determines which recruiters have a subscrition requiring an action
	* and extracts them into the recruitersWithSubscriptionActions array
	*/
	public extractRecruitersWithSubscriptionActions():void{
		
		this.recruitersWithSubscriptionActions = new Array<SubscriptionAction>();
		
		this.recruiters.forEach((r:Recruiter) => {
			r.subscriptions.forEach((s:Subscription) => {
				if (s.currentSubscription && s.status === "AWAITING_ACTIVATION" && s.type === "CREDIT_BASED_SUBSCRIPTION") {
					
					let sa:SubscriptionAction = new SubscriptionAction();
					
					sa.firstName 		= r.firstName;
					sa.surname 			= r.surname;
					sa.email 			= r.email;
					sa.subscriptionId 	= s.subscriptionId;
					sa.status 			= s.status;
					sa.language 		= r.language;
					sa.userId 			= r.userId;
					sa.type 			= s.type;
					sa.actions.push("ACTIVATE_SUBSCRIPTION");
					sa.actions.push("REJECT_SUBSCRIPTION");
					
					this.recruitersWithSubscriptionActions.push(sa);
				}
				
				if (s.currentSubscription && s.status === "ACTIVE_PENDING_PAYMENT" && 
					(	   s.type === "YEAR_SUBSCRIPTION"
						|| s.type === "ONE_MONTH_SUBSCRIPTION"
						|| s.type === "THREE_MONTHS_SUBSCRIPTION" 
						|| s.type === "SIX_MONTHS_SUBSCRIPTION")
					) {
					
					let sa:SubscriptionAction = new SubscriptionAction();
					
					sa.firstName 		= r.firstName;
					sa.surname 			= r.surname;
					sa.email 			= r.email;
					sa.subscriptionId 	= s.subscriptionId;
					sa.status 			= s.status;
					sa.language 		= r.language;
					sa.userId 			= r.userId;
					sa.type 			= s.type;
					sa.actions.push("ACTIVATE_SUBSCRIPTION");
					sa.actions.push("DISABLE_PENDING_PAYMENT");
					
					this.recruitersWithSubscriptionActions.push(sa);
				}
				
				if (s.currentSubscription && s.status === "DISABLED_PENDING_PAYMENT" && 
					(	s.type === "YEAR_SUBSCRIPTION"
						|| s.type === "ONE_MONTH_SUBSCRIPTION"
						|| s.type === "THREE_MONTHS_SUBSCRIPTION" 
						|| s.type === "SIX_MONTHS_SUBSCRIPTION")
					) {
					
					let sa:SubscriptionAction = new SubscriptionAction();
					
					sa.firstName 		= r.firstName;
					sa.surname 			= r.surname;
					sa.email 			= r.email;
					sa.subscriptionId 	= s.subscriptionId;
					sa.status 			= s.status;
					sa.language 		= r.language;
					sa.userId 			= r.userId;
					sa.type 			= s.type;
					sa.actions.push("ACTIVATE_SUBSCRIPTION");
					
					this.recruitersWithSubscriptionActions.push(sa);
				}
				
			});
		});
	}
	
	/**
	* Returns human readable version of Id
	*/
	public frmtSubscriptionAction(actionId:string):string{
		
		switch(actionId) {
			case "ACTIVATE_SUBSCRIPTION": {
				return "Activate";
			}
			case "REJECT_SUBSCRIPTION": {
				return "Reject";
			}
			case "DISABLE_PENDING_PAYMENT": {
				return "Disable";
			}
			default:{
				return actionId;
			}
		}
		
	}

	/**
	* Retrieves recruiters from the backend
	*/
	public fetchRecruiters(): void{
		
		this.recruiterCount 					= 0;
		this.recruiters 						= new Array<Recruiter>();
		this.expiredSubscriptionRecruiters		= new Array<Recruiter>();
		this.activePaidSubscription 			= new Array<Recruiter>();
		this.creditBasedSubscription			= new Array<Recruiter>();
		this.firstGenActiveSubscription			= new Array<Recruiter>();
		
    	this.recruiterService.getRecruiters().subscribe( data => {
  
				data.forEach((r:Recruiter) => {
					this.recruiters.push(r);
					this.addRecruiterToSubscriptionBucker(r);
					this.recruiterCount = this.recruiterCount +1;
										
			});
			
			this.extractRecruitersWithSubscriptionActions();
        
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
	* Examins the Recruiters current subscription and assigns them to 
	* a corresponding bucket
	* recruiter - Recruiter to be added to the appropriate Subscrition bucket
	*/
	private addRecruiterToSubscriptionBucker(recruiter:Recruiter):void{
		
		let activeSubscription = recruiter.subscriptions.filter(s => s.currentSubscription == true)[0];
		
		if (!activeSubscription) {
			this.expiredSubscriptionRecruiters.push(recruiter);
			return;
		}
		
		if (activeSubscription.type == 'FIRST_GEN') {
			this.firstGenActiveSubscription.push(recruiter);
			return;
		}
		
		if (activeSubscription.type == 'CREDIT_BASED_SUBSCRIPTION') {
			this.creditBasedSubscription.push(recruiter);
			return;
		}
		
		if (	activeSubscription.status == 'AWAITING_ACTIVATION'
			|| 	activeSubscription.status == 'ACTIVE_PENDING_PAYMENT'
			|| 	activeSubscription.status == 'ACTIVE'){
			this.activePaidSubscription.push(recruiter);
			return;
		}
		
		if (	activeSubscription.status == 'DISABLED_PENDING_PAYMENT'
			|| 	activeSubscription.status == 'SUBSCRIPTION_ENDED'){
			this.expiredSubscriptionRecruiters.push(recruiter);
			return;
		}
		 
	}
	
	/**
	* Toggles whether recruiters with expired subscriptions are shown
	*/
	toggleShowExpiredSubscriptionRecruiters():void{
		this.showExpiredSubscriptionRecruiters = !this.showExpiredSubscriptionRecruiters;
	}
	
	/**
	* Toggles whether recruiters with active paid subscriptions are shown
	*/
	toggleShowActivePaidSubscription():void{
		this.showActivePaidSubscription = !this.showActivePaidSubscription;
	}
	
	toggleShowCreditBasedSubscription():void{
		this.showCreditBasedSubscription = !this.showCreditBasedSubscription;
	}
	
	/**
	* Toggles whether recruiters with First-gen subscriptions are shown
	*/
	toggleShowFirstGenActiveSubscription():void{
		this.showFirstGenActiveSubscription = !this.showFirstGenActiveSubscription;
	}		
	
	public getTotalActiveRecruiters():number{
		
		if (this.recruiterCount == 0) {
			return 0;
		}
		
		return this.recruiterCount - this.expiredSubscriptionRecruiters.length
	}					
	
	/**
	* Retrieves candidates from the backend
	*/
	public fetchFlaggedAsUnavailableCandidates(): void{
    	this.fetchCandidatesByFilters(this.getCandidateFlaggedAsUnavailableFilterParamString(), false);
	}
	
	public updateCandidateAvailability(candidateId:string, action:string):void{
		this.candidateService.setCandidateAvailability(candidateId, action).subscribe(data => {
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
		
		this.candidateService.updateValidatedSkills(validatedSkills).subscribe(results => {
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
	* Update Recruiters subscription
	*/
	performAction(subscriptionAction:SubscriptionAction, action:string) {
	
			this.recruiterService.performSubscriptionAction(subscriptionAction.userId, subscriptionAction.subscriptionId, action).subscribe(data => {
			
			this.fetchRecruiters();
			
		}, 
		err => {
			console.log(JSON.stringify(err));		
		});
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