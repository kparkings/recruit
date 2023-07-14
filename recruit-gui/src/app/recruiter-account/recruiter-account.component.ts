import { Component, OnInit } 					from '@angular/core';
import { Router}								from '@angular/router';
import { RecruiterService }						from '../recruiter.service';
import { Recruiter }							from './recruiter';
import { Subscription }							from './subscription';
import { NgbModal}								from '@ng-bootstrap/ng-bootstrap'
import { UntypedFormGroup, UntypedFormControl }	from '@angular/forms';
import {RecruiterUpdateRequest }				from './recruiter-update-request';

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
		
		if (this.isRecruiterNoSubscription() || this.hasUnpaidSubscription()) {
			this.switchTab("showSubscriptions");
		}
		
  	}

	currentTab:string 										= "downloads";
	showAccountTab:boolean									= true;
	recruiter:Recruiter										= new Recruiter();
		
	showAccountDetails:boolean								= true;
	showSubscriptions:boolean								= false;
	
	showSwitchToYearlySubscriptionConfirmButtons:boolean	= false;
	showCancelSubscriptionConfirmButtons:boolean 			= false;
	
	public isInEditMode:boolean 							= false;
	
	public editAccountDetailsFor:UntypedFormGroup = new UntypedFormGroup({
		
	});
	
	public accoundDetailsForm:UntypedFormGroup = new UntypedFormGroup({
		firstName:					new UntypedFormControl(''),
		surname:					new UntypedFormControl(''),
		companyName:				new UntypedFormControl(''),
		companyCountry:				new UntypedFormControl(''),
		companyAddress:				new UntypedFormControl(''),
		companyVatNumber:			new UntypedFormControl(''),
		companyRegistrationNumber:	new UntypedFormControl(''),
		email:						new UntypedFormControl(''),
		language:					new UntypedFormControl(''),
	});
		
	/**
	* Retrieves own recruiter account from the backend
	*/
	public fetchRecruiterDetails(): void{
		
    	this.recruiterService.getOwnRecruiterAccount().subscribe( data => {
  
      		this.recruiter = data;

			if (!this.hasActiveSubscription()){
				sessionStorage.setItem('isRecruiterNoSubscription',		'true');
			} else {
				sessionStorage.setItem('isRecruiterNoSubscription',		'false');
			}
			
			if (this.hasUnpaidSubscription()){
				sessionStorage.setItem('hasUnpaidSubscription',		'true');
			} else {
				sessionStorage.setItem('hasUnpaidSubscription',		'false');
			}
			
			this.accoundDetailsForm = new UntypedFormGroup({
				firstName:					new UntypedFormControl(this.recruiter.firstName),
				surname:					new UntypedFormControl(this.recruiter.surname),
				companyName:				new UntypedFormControl(this.recruiter.companyName),
				companyCountry:				new UntypedFormControl(this.recruiter.companyCountry),
				companyAddress:				new UntypedFormControl(this.recruiter.companyAddress),
				companyVatNumber:			new UntypedFormControl(this.recruiter.companyVatNumber),
				companyRegistrationNumber:	new UntypedFormControl(this.recruiter.companyRegistrationNumber),
				email:						new UntypedFormControl(this.recruiter.email),
				language:					new UntypedFormControl(this.recruiter.language),
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
		
		let hasUnpaidSubscription:boolean = false;
		
		this.recruiter.subscriptions.forEach( s => {
		
			if (s.status === 'DISABLED_PENDING_PAYMENT') {
				hasUnpaidSubscription =  true;
			}
			
		});
		
		return hasUnpaidSubscription;
		
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
 	* Activates the switch to Yearly subscription confirmation buttons
	*/
	public switchToYearlySubscription():void{
		this.cancelAlterSubscriptionOptions();
		this.showSwitchToYearlySubscriptionConfirmButtons	= true;
	}
	
	/**
 	* Confirms the swithch to Yearly subscription button
	*/
	public confirmSwitchToYearlySubscription():void{
		this.cancelAlterSubscriptionOptions();
		this.addAlternateSubscription("YEAR_SUBSCRIPTION");
	}
	
	/**
 	* Cancels the swithch to subscription buttons
	*/
	public cancelAlterSubscriptionOptions():void{
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
		
		this.showSwitchToYearlySubscriptionConfirmButtons	= false;
		
		this.recruiterService.performSubscriptionAction(this.recruiter.userId, subscription.subscriptionId, 'END_SUBSCRIPTION').subscribe(data => {
			this.fetchRecruiterDetails();
		}, 
		err => {
			console.log(JSON.stringify(err));		
		});
	}
	
	/**
 	* Ends the subscription
	*/
	public addAlternateSubscription(subscriptionType:string):void{
		
		this.showSwitchToYearlySubscriptionConfirmButtons	= false;
		
		try{
			this.persistAccountDetails();
		} catch (ex){
			console.log("Failed to update Account details ");
		}
		this.recruiterService.requestNewSubscription(this.recruiter.userId, subscriptionType).subscribe(data => {
			sessionStorage.clear();
			sessionStorage.setItem("new-subscription", "true");
			this.router.navigate(['login-user']);
		}, 
		err => {
			console.log(JSON.stringify(err));		
		});
	}
	
	/**
	* Returns the Recruiters subscriptions in time desc order
	*/
	public getOrderedRecruiterSubscriptions():Array<Subscription>{
		return  (this.recruiter.subscriptions.sort((a:Subscription,b:Subscription) => new Date(a.created).getTime() - new Date(b.created).getTime())).reverse();
	}
	
	/**
	* Whether or not the recruiter that has no open Subscriptiion
	*/
	public isRecruiterNoSubscription():boolean{
		return sessionStorage.getItem('isRecruiterNoSubscription') === 'true';
	}
	
	/**
	* Switches to edit mode to update details of the Recruiter
	*/
	public switchToEditMode():void{
		this.isInEditMode = true;
	}
	
	/**
	* Switches out of edit mode replaces any unsaved changes
	*/
	public cancelEditMode():void{
		this.fetchRecruiterDetails();
		this.isInEditMode = false;
	}
	
	/**
	* Persists any unsaved changes
	*/
	public persistAccountDetails():void{
		
		let recruiter:RecruiterUpdateRequest = new RecruiterUpdateRequest();
		
		const firstName:string = String(this.accoundDetailsForm.get('firstName')); 
		
		recruiter.userId 					= this.recruiter.userId;
		recruiter.firstName 				= String(this.accoundDetailsForm.get('firstName')?.value);
		recruiter.surname 					= String(this.accoundDetailsForm.get('surname')?.value);
		recruiter.email 					= String(this.accoundDetailsForm.get('email')?.value);
		recruiter.companyName 				= String(this.accoundDetailsForm.get('companyName')?.value);
		recruiter.companyCountry			= String(this.accoundDetailsForm.get('companyCountry')?.value);
		recruiter.companyAddress			= String(this.accoundDetailsForm.get('companyAddress')?.value);
		recruiter.companyVatNumber			= String(this.accoundDetailsForm.get('companyVatNumber')?.value);
		recruiter.companyRegistrationNumber	= String(this.accoundDetailsForm.get('companyRegistrationNumber')?.value);
		
		recruiter.language 		= String(this.accoundDetailsForm.get('language')?.value);
		
		this.recruiterService.updateRecruiter(recruiter).subscribe(data => {
			this.cancelEditMode();
		}, 
		err => {
			console.log(JSON.stringify(err));		
		});
	
	}
	
}