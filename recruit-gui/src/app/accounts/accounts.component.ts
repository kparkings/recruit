import { Component, OnInit } 					from '@angular/core';
import { FormGroup, FormControl }				from '@angular/forms';
import { AccountsService }						from '../accounts.service';
import { RecruiterService }						from '../recruiter.service';
import { CandidateServiceService }				from '../candidate-service.service';
import { Candidate }							from './candidate';
import { Recruiter }							from './recruiter';
import { Subscription }							from './subscription';
import { SubscriptionAction }					from './subscription-action';
import { environment }							from '../../environments/environment';
import { Router}								from '@angular/router';
import { NgbModal, NgbModalOptions}				from '@ng-bootstrap/ng-bootstrap';
import { ViewChild }							from '@angular/core';

@Component({
  selector: 'app-accounts',
  templateUrl: './accounts.component.html',
  styleUrls: ['./accounts.component.css']
})
export class AccountsComponent implements OnInit {
	
	@ViewChild('recruiterLoginDetails', { static: false }) private content:any;
	
	currentTab:string 												= "recruiters";
	showRecruitersCreateTab:boolean									= false;
	showCandidatesTab:boolean										= false;
	showRecruitersTab:boolean										= true;
	showFlaggedAsUnavailableTab:boolean								= false;
	createAccountDetailsAvailable:boolean 							= false;
	showSubscriptionActionsTab:boolean								= false;
	recruiterUsername:string										= '';
	recruiterPassword:string										= '';
	candidates:Array<Candidate>										= new Array<Candidate>();
	recruiters:Array<Recruiter>										= new Array<Recruiter>();
	recruitersWithSubscriptionActions:Array<SubscriptionAction>		= new Array<SubscriptionAction>();
	recruiterCount:number											= 0;
	activateRecruiterUserId:string									= '';
	activateRecruiterUserPassword:string							= '';
	
	public candidateFormGroup:FormGroup = new FormGroup({
		firstname:	new FormControl(''),
		surname: 	new FormControl(''),
		email:		new FormControl('')
	});
	
	constructor(private recruiterService:RecruiterService, private accountsService: AccountsService, public candidateService:CandidateServiceService, private modalService: NgbModal, private router: Router) {
		this.fetchRecruiters();
	 }

  	ngOnInit(): void {}

	public createRecruiterAccountForm:FormGroup = new FormGroup({
    	proposedUserName:new FormControl(''),
		firstName:new FormControl(''),
		surname:new FormControl(''), 
		email:new FormControl(''), 
		companyName:new FormControl(''), 
		language:new FormControl(''),
    });

	public switchTab(tab:string){
		
		this.currentTab = tab;
		
		switch(tab){
			case "recruitersCreate":{
				this.showRecruitersCreateTab=true;
				this.showCandidatesTab=false;
				this.showRecruitersTab=false;
				this.showFlaggedAsUnavailableTab=false;
				this.showSubscriptionActionsTab=false;
				break;
			}
			case "candidates":{
				this.showRecruitersCreateTab=false;
				this.showCandidatesTab=true;
				this.showRecruitersTab=false;
				this.showFlaggedAsUnavailableTab=false;
				this.showSubscriptionActionsTab=false;
				break;
			}
			case "recruiters":{
				this.showRecruitersCreateTab=false;
				this.showCandidatesTab=false;
				this.showRecruitersTab=true;
				this.showFlaggedAsUnavailableTab=false;
				this.showSubscriptionActionsTab=false;
				this.fetchRecruiters();
				break;
			}
			case "flaggedAsUnavailable":{
				this.showRecruitersCreateTab=false;
				this.showCandidatesTab=false;
				this.showRecruitersTab=false;
				this.showFlaggedAsUnavailableTab=true;
				this.showSubscriptionActionsTab=false;
				this.fetchFlaggedAsUnavailableCandidates();
				break;
			}
			case "subscriptionActions":{
				this.showRecruitersCreateTab=false;
				this.showCandidatesTab=false;
				this.showRecruitersTab=false;
				this.showFlaggedAsUnavailableTab=false;
				this.showSubscriptionActionsTab=true;
				this.fetchRecruiters();
				break;
			}
			
		}
		
	}
	
	/**
	* Creates a new Recruiter account. The preferedUser name will be used 
	* if available. If not a similar usename will be returned
	*/
	public createRecruiterAccount():void{
		
		this.recruiterUsername					= '';
		this.recruiterPassword					= '';      		
		this.createAccountDetailsAvailable 		= false;
			
		let proposedUsername:string = this.createRecruiterAccountForm.get('proposedUserName')!.value;
		 
		if (proposedUsername.length <= 4){
			return;
		}
		
		this.accountsService.addCandidate(proposedUsername).forEach(data => {

			this.recruiterUsername					= data.username;
			this.recruiterPassword					= data.password;      		
			this.createAccountDetailsAvailable 		= true;
			
			let firstName:string 	= this.createRecruiterAccountForm.get('firstName')!.value;
			let surname:string 		= this.createRecruiterAccountForm.get('surname')!.value;
			let email:string 		= this.createRecruiterAccountForm.get('email')!.value;
			let companyName:string 	= this.createRecruiterAccountForm.get('companyName')!.value;
			let language:string 	= this.createRecruiterAccountForm.get('language')!.value;
			
			//TODO: Add validation so only valid Recruiters can be sent to backend
			
			this.recruiterService.registerRecruiter(this.recruiterUsername, firstName, surname, email, companyName, language).subscribe( data => {
  				//TODO: update recruiters list      
		
				this.createRecruiterAccountForm.reset();
		
			}, err => {
				console.log("Failed to add Recruiter " + err);
	    	})
			
		});
	}
	
	/**
	* Builds a query parameter string with the selected filter options
	*/
	private getCandidateFilterParamString():string{
    	
		const filterParams:string = 
								'orderAttribute=candidateId'
                                 + "&order=desc" 
                                 + '&page=0'
                                 + '&size=50'
                                 + this.getFirstnameParamString() 
                                 + this.getSurnameParamString()
                                 + this.getEmailParamString();
		return filterParams;
	
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
	
	public fetchCandidatesByFilters(filterParams:string):void{
		
		this.candidates = new Array<Candidate>();

    	this.candidateService.getCandidates(filterParams).subscribe( data => {
  
      		data.content.forEach((c:Candidate) => {
        
        		const candidate:Candidate = new Candidate();

      			candidate.candidateId		= c.candidateId;
  				candidate.firstname			= c.firstname;
				candidate.surname			= c.surname;
				candidate.email				= c.email;
				candidate.available			= c.available;
				
      			this.candidates.push(candidate);

			});
        
		}, err => {
			
			if (err.status === 401 || err.status === 0) {
				sessionStorage.removeItem('isAdmin');
				sessionStorage.removeItem('isRecruter');
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
				if (s.currentSubscription && s.status === "AWAITING_ACTIVATION" && s.type === "TRIAL_PERIOD") {
					
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
				
				if (s.currentSubscription && s.status === "ACTIVE_PENDING_PAYMENT" && s.type === "YEAR_SUBSCRIPTION") {
					
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
				
				if (s.currentSubscription && s.status === "DISABLED_PENDING_PAYMENT" && s.type === "YEAR_SUBSCRIPTION") {
					
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
		
		this.recruiterCount 	= 0;
		this.recruiters 		= new Array<Recruiter>();
		
    	this.recruiterService.getRecruiters().subscribe( data => {
  
				data.forEach((r:Recruiter) => {
      				this.recruiters.push(r);
					this.recruiterCount = this.recruiterCount +1;
										
			});
			
			this.extractRecruitersWithSubscriptionActions();
        
		}, err => {
			
			if (err.status === 401 || err.status === 0) {
				sessionStorage.removeItem('isAdmin');
				sessionStorage.removeItem('isRecruter');
				sessionStorage.removeItem('loggedIn');
				sessionStorage.setItem('beforeAuthPage', 'view-candidates');
				this.router.navigate(['login-user']);
			}
    	})
	}	
	
	/**
	* Retrieves candidates from the backend
	*/
	public fetchCandidates(): void{
    	this.fetchCandidatesByFilters(this.getCandidateFilterParamString());
	}
	
	/**
	* Retrieves candidates from the backend
	*/
	public fetchFlaggedAsUnavailableCandidates(): void{
    	this.fetchCandidatesByFilters(this.getCandidateFlaggedAsUnavailableFilterParamString());
	}
	
	/**
	* Disables the Candidate
	*/
	disableCandidate(candidateId:string):void{
		this.candidateService.disableCandidate(candidateId).subscribe(data => {
			this.fetchCandidates();	
		});
		
	}

	/**
	* Disables flagged As Unavailable Candidate
	*/
	disableFlaggedAsUnavailableCandidate(candidateId:string):void{
		this.candidateService.disableCandidate(candidateId).subscribe(data => {
			this.fetchFlaggedAsUnavailableCandidates();	
		});
		
	}	
	
	/**
	* Removed the flag from flagged As Unavailable Candidate
	*/
	removeFlaggedAsUnavailableFlag(candidateId:string):void{
		this.candidateService.removeMarkCandidateAsUnavailableFlag(candidateId).subscribe(data => {
			this.fetchFlaggedAsUnavailableCandidates();	
		});
		
	}	
	
	/**
	* Enables the Candidate
	*/
	enableCandidate(candidateId:string):void{
		this.candidateService.enableCandidate(candidateId);
		this.fetchCandidates();
	}
	
	/**
	* Update Recruiters subscription
	*/
	performAction(subscriptionAction:SubscriptionAction, action:string) {
			
			this.activateRecruiterUserId		= ''; 
			this.activateRecruiterUserPassword	= '';
	
			this.recruiterService.performSubscriptionAction(subscriptionAction.userId, subscriptionAction.subscriptionId, action).subscribe(data => {
			
			this.fetchRecruiters();
			
			/**
			* IF action == 
			*/
			if (subscriptionAction.status === "AWAITING_ACTIVATION" && subscriptionAction.type === "TRIAL_PERIOD") {
				
				this.activateRecruiterUserId		= data.userId; 
				this.activateRecruiterUserPassword	= data.password;
				this.openModal();
			}
			
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

}