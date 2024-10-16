import { Component } 							from '@angular/core';
import { Router } from '@angular/router';
import { RecruiterService } 					from 'src/app/recruiter.service';
import { SubscriptionAction } 					from '../subscription-action';
import { Recruiter }							from './../recruiter';
import { Subscription }							from './../subscription';

@Component({
  selector: 'app-subscriptions',
  standalone: false,
  templateUrl: './subscriptions.component.html',
  styleUrl: './subscriptions.component.css'
})
export class SubscriptionsComponent {

	public SUBSCRIPTION_VIEW = SUBSCRIPTION_VIEW;
	
	public currentView:SUBSCRIPTION_VIEW = SUBSCRIPTION_VIEW.SELECT_SUBSCRIPTION_STATUS; 

	recruiters:Array<Recruiter>										= new Array<Recruiter>();
	recruitersWithSubscriptionActions:Array<SubscriptionAction>		= new Array<SubscriptionAction>();
	
	
	
	/**
	* Constructor
	*/
	constructor(private router: Router, private recruiterService:RecruiterService){
		this.fetchRecruiters();
	}

	/**
	* Update Recruiters subscription
	*/
	performAction(subscriptionAction:SubscriptionAction, action:string) {
	
			this.recruiterService.performSubscriptionAction(subscriptionAction.userId, subscriptionAction.subscriptionId, action).subscribe(data => {
			
			this.fetchRecruiters();
			
			this.backFromSubscriptionDetails();
			
		}, 
		err => {
			console.log(JSON.stringify(err));		
		});
	}

	/**
	* Retrieves recruiters from the backend
	*/
	public fetchRecruiters(): void{
		
		this.recruiters 						= new Array<Recruiter>();
		
    	this.recruiterService.getRecruiters().subscribe( data => {
  			data.forEach((r:Recruiter) => {
				this.recruiters.push(r);
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
					sa.invoiceType	 	= s.invoiceType;
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
					sa.invoiceType	 	= s.invoiceType;
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
					sa.invoiceType	 	= s.invoiceType;
					sa.actions.push("ACTIVATE_SUBSCRIPTION");
					
					this.recruitersWithSubscriptionActions.push(sa);
				}
				
			});
		});
		
		this.recruitersWithSubscriptionActions = this.recruitersWithSubscriptionActions.sort((a,b) => a.type.localeCompare(b.type) || a.userId.localeCompare(b.userId));
	}
	
	public getRecruitersWithSubscriptionActions():Array<SubscriptionAction>{
		return this.recruitersWithSubscriptionActions.filter(s => s.status == this.currentView);	
	}
	
	public getRecruitersWithSubscriptionActionsStatusCount(status:SUBSCRIPTION_VIEW):number{
		return this.recruitersWithSubscriptionActions.filter(s => s.status == status).length;	
	}
	
	public selectSubscriptionStatus(status:SUBSCRIPTION_VIEW):void{
		this.currentView = status;
	}
	
	public showStatusOptions():void{
		this.currentView = SUBSCRIPTION_VIEW.SELECT_SUBSCRIPTION_STATUS;
	}
	
	public selectedSubscriptionAction:SubscriptionAction 	= new SubscriptionAction;
	public selectedRecruiter:Recruiter 						= new Recruiter();
	public lastCurrentView:SUBSCRIPTION_VIEW 				= SUBSCRIPTION_VIEW.SELECT_SUBSCRIPTION_STATUS;
	
	public backFromSubscriptionDetails():void{
		this.currentView 					= this.lastCurrentView;
		this.lastCurrentView 				= SUBSCRIPTION_VIEW.SELECT_SUBSCRIPTION_STATUS;
		this.selectedSubscriptionAction 	= new SubscriptionAction();
		this.selectedRecruiter 				= new Recruiter();
	}
	
	public setSelectSubscriptionAction(subscriptionAction:SubscriptionAction):void{
		this.selectedSubscriptionAction = subscriptionAction;
		this.lastCurrentView 			= this.currentView;
		this.currentView 				= SUBSCRIPTION_VIEW.SUBSCRIPTION_DETAILS;
		this.selectedRecruiter 			= this.getRecruiterForSubscriptionAction(subscriptionAction);
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
	* Returns details of Recruiter that owns the Subscription
	*/
	public getRecruiterForSubscriptionAction(subscriptionAction:SubscriptionAction):Recruiter{
		return this.recruiters.filter(r => r.userId == subscriptionAction.userId)[0];
	}
		
}

enum SUBSCRIPTION_VIEW {
	SELECT_SUBSCRIPTION_STATUS 				= "",
	SUBSCRIPTION_STATUS_AWAITING_ACTIVATION = "AWAITING_ACTIVATION",
	SUBSCRIPTION_STATUS_AWAITING_PAYMENT 	= "ACTIVE_PENDING_PAYMENT",
	SUBSCRIPTION_STATUS_DISABLED 			= "DISABLED_PENDING_PAYMENT",
	SUBSCRIPTION_DETAILS			  		= "SUBSCRIPTION_DETAILS"
};
