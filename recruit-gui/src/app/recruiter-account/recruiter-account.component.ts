import { Component, OnInit } 					from '@angular/core';
import { Router}								from '@angular/router';
import { RecruiterService }						from '../recruiter.service';
import { Recruiter }							from './recruiter';
import { Subscription }							from './subscription';
import { NgbModal}								from '@ng-bootstrap/ng-bootstrap';
import { SubscriptionAction }					from './subscription-action';

/**
* Component to allow the Recruiter to aministrate their 
* account
*/
@Component({
  selector: 'app-recruiter-account',
  templateUrl: './recruiter-account.component.html',
  styleUrls: ['./recruiter-account.component.css']
})
export class RecruiterAccountComponent implements OnInit {

	/**
	* Constructor
	*/
	constructor(private recruiterService:RecruiterService, private router: Router, private modalService: NgbModal) { }

	/**
	* Initializes the Component
	*/
	ngOnInit(): void {
		this.fetchRecruiterDetails();
  	}

	currentTab:string 					= "downloads";
	showAccountTab:boolean				= true;
	recruiter:Recruiter					= new Recruiter();
		
	showAccountDetails:boolean			= true;
	showSubscriptions:boolean			= false;
	
	showSwitchToFirstGenSubscriptionConfirmButtons:boolean	= false;
	showSwitchToYearlySubscriptionConfirmButtons:boolean	= false;
	showCancelSubscriptionConfirmButtons:boolean 			= false;
		
	/**
	* Retrieves own recruiter account from the backend
	*/
	public fetchRecruiterDetails(): void{
		
    	this.recruiterService.getOwnRecruiterAccount().subscribe( data => {
  
      		this.recruiter = data;

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
	* Switches to the selected tab
	*/
	public switchTab(tab:string){
		
		this.currentTab = tab;
		
		switch(tab){
			case "showAccountDetails":{
				this.showAccountDetails=true;
				this.showSubscriptions=false;
				break;
			}
			case "showSubscriptions":{
				this.showAccountDetails=false;
				this.showSubscriptions=true;
				break;
			}
		}
		
	}
	
	/**
	* Returns whether or not the user has an active subscription.
	*/
	public hasActiveSubscription():boolean{
		
		let currentSubscription:boolean = false;
		
		this.recruiter.subscriptions.forEach( s => {
		
			if (s.currentSubscription) {
				currentSubscription = true;
			}
			
		});
		
		return currentSubscription;
	}
	
	/**
	* Returns active subscription type.
	*/
	public getCurrentSubscriptionType():string{
		
		let currentSubscriptionType:string = '';
		
		this.recruiter.subscriptions.forEach( s => {
		
			if (s.currentSubscription) {
				currentSubscriptionType = s.type;
			}
			
		});
		
		return currentSubscriptionType;
	}
	
	/**
	* Returns whether or not the recruiter has an unpaid invoice
	*/
	public hasUnpaidSubscription():boolean{
		
		let status:string = '';
		
		this.recruiter.subscriptions.forEach( s => {
		
			if (s.currentSubscription) {
				status = s.status;
			}
			
		});
		
		return status === 'DISABLED_PENDING_PAYMENT';
		
	}
	
	/**
	* Whether or not the Recruiter has a FirstGen account. If so the Recruiter must be able 
	* to reselect this option. We however dont want to present this option to second generation
	* recruiters 
	*/
	public hasFirstGenSubscription():boolean{
		
		let isFirstGenRecruiter:boolean = false;
		
		this.recruiter.subscriptions.forEach( s => {
		
			if (s.type == 'FIRST_GEN') {
				isFirstGenRecruiter = true;
			}
			
		});
		
		return isFirstGenRecruiter;
		
	}
	
	/**
 	* Activates the switch to FirstGen subscription confirmation buttons
	*/
	public switchToFirstGenSubscription():void{
		this.cancelAlterSubscriptionOptions();
		this.showSwitchToFirstGenSubscriptionConfirmButtons = true;
		this.showSwitchToYearlySubscriptionConfirmButtons	= false;
	}
	
	/**
 	* Confirms the swithch to FirstGen subscription button
	*/
	public confirmSwitchToFirstGenSubscription():void{
		this.cancelAlterSubscriptionOptions();
	}
	
	/**
 	* Activates the switch to Yearly subscription confirmation buttons
	*/
	public switchToYearlySubscription():void{
		this.cancelAlterSubscriptionOptions();
		this.showSwitchToFirstGenSubscriptionConfirmButtons = false;
		this.showSwitchToYearlySubscriptionConfirmButtons	= true;
	}
	
	/**
 	* Confirms the swithch to Yearly subscription button
	*/
	public confirmSwitchToYearlySubscription():void{
		this.cancelAlterSubscriptionOptions();
	}
	
	/**
 	* Cancels the swithch to subscription buttons
	*/
	public cancelAlterSubscriptionOptions():void{
		this.showSwitchToFirstGenSubscriptionConfirmButtons = false;
		this.showSwitchToYearlySubscriptionConfirmButtons	= false;
		this.showCancelSubscriptionConfirmButtons = false;
	}
	
	/**
	* Show confirm cancel current subscription options
	*/
	public cancelSubscription():void{
		this.cancelAlterSubscriptionOptions();
		this.showCancelSubscriptionConfirmButtons = true;
	}


	/**
 	* Cancels the swithch to subscription buttons
	*/
	public cancelCancelSubscription():void{
		this.showCancelSubscriptionConfirmButtons = false;
	}	
	
	/**
 	* Ends the subscription
	*/
	public confirmCancelSubscription(subscription:Subscription):void{
		
		this.showSwitchToFirstGenSubscriptionConfirmButtons = false;
		this.showSwitchToYearlySubscriptionConfirmButtons	= false;
		
		this.recruiterService.performSubscriptionAction(this.recruiter.userId, subscription.subscriptionId, 'END_SUBSCRIPTION').subscribe(data => {
			this.fetchRecruiterDetails();
		}, 
		err => {
			console.log(JSON.stringify(err));		
		});
	}
	
}
