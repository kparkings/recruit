import { Component, OnInit } 					from '@angular/core';
import { Router}								from '@angular/router';
import { RecruiterService }						from '../recruiter.service';
import { Recruiter }							from './recruiter';
import { Subscription }							from './subscription';
import { NgbModal}								from '@ng-bootstrap/ng-bootstrap'
import { UntypedFormGroup, UntypedFormControl }	from '@angular/forms';
import {RecruiterUpdateRequest }				from './recruiter-update-request';
import { CreditsService } 						from '../credits.service';
import { TranslateService } 					from '@ngx-translate/core';
import { INVOICE_TYPE } from './subscription-api-inbound';

/**
* Component to allow the Recruiter to aministrate their 
* account
*/
@Component({
    selector: 'app-recruiter-account',
    templateUrl: './recruiter-account.component.html',
    styleUrls: ['./recruiter-account.component.css'],
    standalone: false
})
export class RecruiterAccountComponent implements OnInit {

	INVOICE_TYPE = INVOICE_TYPE;	

	public isMobile:boolean 						= false;
	public mobCss:string 							= "";
	public subscriptionSelectionMobCss 				= "";
	public accountDetailsMobCss 					= "";
	public showBillingInvoiceType:boolean			= false;
	public showBillingDetails:boolean 				= false;
	public showBillingDetailsPrivatePerson:boolean 	= false;
	public selectedSubscriptionOption:string 		= "";
	public isCreditBasedSubscription:boolean 		= false;
	public isTrialOrFirstGen 						= false;
	
	/**
	* Constructor
	*/
	constructor(private recruiterService:		RecruiterService, 
				private router: 				Router, 
				private creditsService: 		CreditsService,
				private translate:				TranslateService) { 
					
		this.creditsService.isPurchaseSubscription().subscribe(value => {
			this.creditsService.setPurchaseSubscription(false);
			this.isBuyingSubscription = value;
		});					
					
	}

	/**
	* Initializes the Component
	*/
	ngOnInit(): void {
		if (this.isRecruiter() || this.isRecruiterNoSubscription()) {
			this.fetchRecruiterDetails();
		}
		
		if (this.isRecruiterNoSubscription() || this.hasUnpaidSubscription() || this.isBuyingSubscription) {
			this.switchTab("showSubscriptions");
		}
		
  	}
	
	ngAfterViewInit():void{
		/**
		* If arriving from quickactions go straight to add Role 
		*/
		if (localStorage.getItem("quick-action-activated")) {
			localStorage.removeItem("quick-action-activated");
			this.switchTab("showSubscriptions");
		}
	}
  	
  	/**
	* Requests a Credit based subscription to the site for the
	* Recruiter
	*/
	public selectCreditBasedSubscription():void{
		this.addAlternateSubscription("CREDIT_BASED_SUBSCRIPTION", INVOICE_TYPE.BUSINESS);
	}
	
	public doShowBillingInvoiceType(subscriptionOption:string):void{
		  this.showBillingInvoiceType 			= true;
		  this.selectedSubscriptionOption 	= subscriptionOption;
	}
  	
  	public doShowBillingDetails():void{
		  this.showBillingInvoiceType		= false;
		  this.showBillingDetails 			= true;
	}
	
	public doShowBillingDetailsPrivatePerson():void{
		this.showBillingInvoiceType				= false;
		this.showBillingDetailsPrivatePerson 	= true;
	}
	
	public confirmSwitchToOtherSubscriptionCancel():void{
		this.showBillingDetails 				= false; 
		this.showBillingInvoiceType 			= false; 
		this.showBillingDetailsPrivatePerson 	= false;
	}
	
	
  	
  	public setSubscriptionOption():void{
		  
		let subscription:string = this.accoundDetailsForm.get('subscriptionOption')?.value;
		this.subscriptionOption = subscription;
	}

	public subscriptionOption:string					 	="CREDIT_BASED_ACCESS";
	isBuyingSubscription:boolean							= false;
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
		subscriptionOption:			new UntypedFormControl('CREDIT_BASED_ACCESS'),
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
			
			let activeSubscription:Subscription = this.recruiter.subscriptions.filter(s => s.status == 'ACTIVE')[0];
			
			if (activeSubscription && activeSubscription.type == 'CREDIT_BASED_SUBSCRIPTION'){
				this.isCreditBasedSubscription = true;
			}
			
			if (activeSubscription && (activeSubscription.type == 'FIRST_GEN' || activeSubscription.type =='TRIAL_PERIOD')){
				this.isTrialOrFirstGen = true;
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
				subscriptionOption:			new UntypedFormControl('CREDIT_BASED_ACCESS'),
			});
				
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
	* Switches to the selected tab
	*/
	public switchTab(tab:string){
		
		this.currentTab 					= tab;
		this.showBillingDetails 			= false;
		this.selectedSubscriptionOption 	= "";
		
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
	public confirmSwitchToOtherSubscription(invoiceType:INVOICE_TYPE):void{
		this.cancelAlterSubscriptionOptions();
		this.addAlternateSubscription(this.selectedSubscriptionOption, invoiceType);	
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
	public addAlternateSubscription(subscriptionType:string, invoiceType?:INVOICE_TYPE):void{
		this.showSwitchToYearlySubscriptionConfirmButtons	= false;
		
		if (invoiceType && invoiceType == INVOICE_TYPE.BUSINESS){
			try{
					this.persistAccountDetails();
				
			} catch (ex){
				console.log("Failed to update Account details ");
			}
		}
		
		//TODO: Add invoiceType to request and implement in backend'
		if (invoiceType == INVOICE_TYPE.BUSINESS || invoiceType == INVOICE_TYPE.PERSON) {
			this.recruiterService.requestNewSubscription(this.recruiter.userId, subscriptionType, invoiceType).subscribe(data => {
				sessionStorage.clear();
				sessionStorage.setItem("new-subscription", "true");
				this.router.navigate(['login-user']);
			}, 
			err => {
				console.log(JSON.stringify(err));		
			});
		}
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
	
	public showSubscriptionOptions():void{
		this.isBuyingSubscription = true;
	}
	/**
	* Persists any unsaved changes
	*/
	public persistAccountDetails():void{
		
		let recruiter:RecruiterUpdateRequest = new RecruiterUpdateRequest();
		
		recruiter.userId 					= this.recruiter.userId;
		recruiter.firstName 				= this.accoundDetailsForm.get('firstName')?.value;
		recruiter.surname 					= this.accoundDetailsForm.get('surname')?.value;
		recruiter.email 					= this.accoundDetailsForm.get('email')?.value;
		recruiter.companyName 				= this.accoundDetailsForm.get('companyName')?.value;
		recruiter.companyCountry			= this.accoundDetailsForm.get('companyCountry')?.value;
		recruiter.companyAddress			= this.accoundDetailsForm.get('companyAddress')?.value;
		recruiter.companyVatNumber			= this.accoundDetailsForm.get('companyVatNumber')?.value;
		recruiter.companyRegistrationNumber	= this.accoundDetailsForm.get('companyRegistrationNumber')?.value;
		
		recruiter.language 		= String(this.accoundDetailsForm.get('language')?.value);
		
		this.recruiterService.updateRecruiter(recruiter).subscribe(data => {
			this.cancelEditMode();
		}, 
		err => {
			console.log(JSON.stringify(err));		
		});
	
	}
	
	/**
	* Gets human readable translation for country code 
	*/
	public getLang(langCode:string):string{
								
		switch(langCode){
			case 'ENGLISH': {return this.translate.instant('arenella-recruiter-account-english');}
			case 'DUTCH': {return this.translate.instant('arenella-recruiter-account-dutch');}
			case 'FRENCH': {return this.translate.instant('arenella-recruiter-account-french');}
			default: {return langCode;}
		}
		
	}
	
	/**
	* Whether or not User is a Recruiter
	*/
	public isRecruiter():boolean{
		return sessionStorage.getItem('isRecruiter') === 'true';
	}
	
	private formInitialized:boolean = false;
	
	/**
	* Front end validation to prompt Recruiter to add all the billing details 
	*/
	public businessDetailsValidated():boolean{
		
		
		
		let companyName:string  				= "" +String(this.accoundDetailsForm.get('companyName')?.value);
		let companyCountry:string				= "" +String(this.accoundDetailsForm.get('companyCountry')?.value);
		let companyAddress:string				= "" +String(this.accoundDetailsForm.get('companyAddress')?.value);
		let companyVatNumber:string				= "" +String(this.accoundDetailsForm.get('companyVatNumber')?.value);
		let companyRegistrationNumber:string	= "" +String(this.accoundDetailsForm.get('companyRegistrationNumber')?.value);
		
		companyName 				= companyName.replace("null","");
		companyCountry 				= companyCountry.replace("null","");
		companyAddress 				= companyAddress.replace("null","");
		companyVatNumber 			= companyVatNumber.replace("null","");
		companyRegistrationNumber 	= companyRegistrationNumber.replace("null","");
		
		
		if (	companyName.length < 2 
			||  companyCountry.length < 2
			|| companyAddress.length < 2
			|| companyVatNumber.length < 2
			|| companyRegistrationNumber.length < 2) {
				return false;
			}
		
		return true;
	}
	
}