import { Component, ViewChild }				from '@angular/core';
import { Router}							from '@angular/router';
import { CookieService } 					from 'ngx-cookie-service';
import { NgbModal, NgbModalOptions}			from '@ng-bootstrap/ng-bootstrap'
import { RecruiterMarketplaceService }		from './recruiter-marketplace.service';
import { EmailService }						from './email.service';
import { PopupsService }					from './popups.service';
import { CandidateNavService } 				from './candidate-nav.service';
import { CreditsService } 					from './credits.service';
import { NewsfeedService } 					from './newsfeed.service';
import { ListingService }					from './listing.service';
import { TranslateService} 					from "@ngx-translate/core";
import { CurrentUserAuth }					from './current-user-auth';
import { PrivateMessagingComponent }		from './private-messaging/private-messaging.component';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css','./app.component-mob.css'],
    standalone: false
}) 
export class AppComponent {

	@ViewChild('validationExBox', { static: false }) 	private validationExBox:any;
	@ViewChild('noCreditsBox', { static: false }) 	 	private noCreditsBox:any;
	@ViewChild('quickActionsBox', { static: false }) 	private quickActionsBox:any;
	@ViewChild('tandcBox', { static: false }) 	 		private tandcBox:any;
	@ViewChild(PrivateMessagingComponent) 				privateChat!:PrivateMessagingComponent;
	
	public currentUserAuth:CurrentUserAuth 						= new CurrentUserAuth();
	
	private readonly termsAndConditionsCookieVersion:string = '3';

  	title = 'Arenella-ICT - IT Candidates for Recruiters';

	public termsAndConditionsAccepted:boolean = false;

	public isMobile:boolean = false;
	
	public unseenMpPosts:number = 0;
	public unseenEmails:number 	= 0;
	
	public validationExceptions:Array<string> 	= new Array<string>();
	public menuItemMobileCss 					= '';
	
	public creditsCurriculum:number 	= 0;
	public creditsMarketplace:number 	= 0;
	public creditsJobboard:number 		= 0;
	
	public lastNewsfeedView:Date		= new Date();
	public unseenNewsfeedItems:boolean  = true;
	
	public toggleMobileMenu:string = "toggleMobileMenuOff";
	public toggleMobileAuthMenu:string = "toggleMobileAuthMenuOff";
	
	/**
	* Constructor
	*/
	constructor(private router: 				Router, 
				private cookieService: 			CookieService, 
				private modalService: 			NgbModal, 
				private mpService:				RecruiterMarketplaceService,
				private emailService:			EmailService,
				public  popupsService:			PopupsService,
				private candidateNavService: 	CandidateNavService,
				public  creditsService:			CreditsService,
				private newsfeedService:		NewsfeedService,
				private translate: 				TranslateService,
				private listingService:			ListingService,
			){
		
		translate.setDefaultLang('en');
    	translate.use(""+translate.getBrowserLang());
    
		if (this.isRecruiterNoSubscription() || this.hasUnpaidSubscription()) {
			this.router.navigate(['recruiter-account']);
		}
		
		if (cookieService.get(this.getTandCsCookieName())) {
			this.termsAndConditionsAccepted = true;
		}
		
		this.creditsService.hasTokens().subscribe(tokens => {
			if (tokens == false) {
				this.updateTokenCounts();
				this.popupsService.openModal(this.noCreditsBox);
				
			}
		});
		
		this.popupsService.fetchValidationErrors().subscribe(val => {
			this.validationExceptions = val;
			this.closeModal();
			if (this.validationExceptions.length > 0) {
				this.popupsService.openModal(this.validationExBox);
			}
		});
		
		if(!this.isAuthenticatedAsCandidate()){
			this.refreschUnreadAlerts();
		}
	}
	
	ngAfterViewInit():void{
		if (this.tandcBox.nativeElement && this.termsAndConditionsAccepted != true) {
			this.tandcBox.nativeElement.showModal();
		}
	}
	
	/**
	* When using mobile. Shows or hides menu options
	*/
	public toggleMobileMenuOpt():void {
		this.toggleMobileAuthMenu = 'toggleMobileAuthMenuOff';
		if(this.toggleMobileMenu == 'toggleMobileMenuOff'){
			this.toggleMobileMenu = 'toggleMobileMenuOn';
		} else {
			this.toggleMobileMenu = 'toggleMobileMenuOff';
		}
	}
	
	public toggleMobileAuthMenuOpt():void {
		this.toggleMobileMenu = 'toggleMobileMenuOff';
		if(this.toggleMobileAuthMenu == 'toggleMobileAuthMenuOff'){
			this.toggleMobileAuthMenu = 'toggleMobileAuthMenuOn';
		} else {
			this.toggleMobileAuthMenu = 'toggleMobileAuthMenuOff';
		}
	}
	
	/**
	* Closes menu when routing to new page
	*/
	public resetMenu():void{
		this.toggleMobileMenu = 'toggleMobileMenuOff';
		this.toggleMobileAuthMenu = 'toggleMobileAuthMenuOff';
	}
	
	private lastAlertRefresh!:Date;
	
	public refreschUnreadAlerts():void{
		
		if (this.isAuthenticatedAsCandidate()){
			return;
		}
		
		let now:Date = new Date();
		
		if(this.lastAlertRefresh && this.lastAlertRefresh > now){
			//So we dont make lots of expensive backend calls
		} else {
			if(!this.lastAlertRefresh){
				this.lastAlertRefresh = now;
			} else {
				this.lastAlertRefresh.setSeconds(this.lastAlertRefresh.getSeconds() + 10);
			}	
			
			if (sessionStorage.getItem("userId")) {
				this.newsfeedService.getLastViewRecord().subscribe(record => {
					this.lastNewsfeedView = record.lastViewed;	
					this.newsfeedService.getNewsFeedItems().subscribe(items => {
						if(items[0]) {
							this.unseenNewsfeedItems = (items[0].created > this.lastNewsfeedView);
						}
					});
				});
			}
		
			this.mpService.fetchUnseenOpenPositionCount().subscribe(val => {
				this.unseenMpPosts = val;
			});
			
			this.emailService.updateUnseenEmails();
			this.emailService.fetchUnseenEmailsCount().subscribe(val => {
				this.unseenEmails = val;
			});
		
		}
		
	}
	
	/**
	* Whether or not the user has authenticated as an Candidate user 
	*/
	public isAuthenticatedAsCandidate():boolean {
		return sessionStorage.getItem('isCandidate') === 'true';
	}
		
	/**
	* Returns the name of the current T&C acceptance cookie
	*/
	public navToSuggestions():void{
		this.candidateNavService.reset();
		this.router.navigate(['suggestions']);
	}
	
	/**
	* Returns the name of the current T&C acceptance cookie
	*/
	public navToNewCandidate():void{
		this.candidateNavService.reset();
		this.router.navigate(['new-candidate']);
	}
	
	private updateTokenCounts():void{
		this.creditsService.getCreditCountCurriculum().subscribe(res => {
			this.creditsCurriculum = res;
		});
		this.creditsService.getCreditCountJobboard().subscribe(res => {
			this.creditsJobboard = res;
		});
		this.creditsService.getCreditCountMarketplace().subscribe(res => {
			this.creditsMarketplace = res;
		});
	}
	
	/**
	* Navigates to the recruiter-account page to 
	* allow the use to buy a subscription
	*/
	public navToSubscriptions():void{
		this.candidateNavService.reset();
		this.closeModal();
		this.validationExBox.nativeElement.close();
		this.noCreditsBox.nativeElement.close();
		this.creditsService.buySubscription();
		this.router.navigate(['recruiter-account']);
	}
	
	/**
	* Returns the name of the current T&C acceptance cookie
	*/
	private getTandCsCookieName():string{
		
		const termsAndConditionsCookieName:string = "arenella-tandc-v" + this.termsAndConditionsCookieVersion;
		
		return termsAndConditionsCookieName;
	}
	
	/**
	* Creates a cookie showing the site visitor has 
	* agreed to the terms and consitions of the site
	*/
	public acceptTandCs():void{
		
		let date:Date = new Date();
		date.setFullYear(date.getFullYear() + 1);
		
		this.cookieService.set(this.getTandCsCookieName(),"Accepted",{expires: date});
		
		this.termsAndConditionsAccepted = true;
		this.tandcBox.nativeElement.close();
	}
	
	/**
	*  Closes the confirm popup
	*/
  	public closeModal(): void {
    	this.modalService.dismissAll();
  	}

  	/**
  	* Whether or not the user has authenticated with the System 
  	*/
  	public isAuthenticated():boolean {
    	return sessionStorage.getItem('loggedIn') === 'true';
  	}

  	/**
  	* Whether or not the user has authenticated as an Admin user 
  	*/
  	public isAuthenticatedAsAdmin():boolean {
    	return sessionStorage.getItem('isAdmin') === 'true';
  	}

	/**
	* Whether or not User is a Recruiter
	*/
	public isRecruiter():boolean{
		return sessionStorage.getItem('isRecruiter') === 'true';
	}
	
	/**
	* Whether or not the Use is a Candidate
	*/
	public isCandidate():boolean{
		return sessionStorage.getItem('isCandidate') === 'true';
	}
	
	/**
	* Whether or not the recruiter that has no open Subscriptiion
	*/
	public isRecruiterNoSubscription():boolean{
		return sessionStorage.getItem('isRecruiterNoSubscription') === 'true';
	}
	
	/**
	* Whether or not the recruiter that has no open Subscriptiion
	*/
	public hasUnpaidSubscription():boolean{
		return sessionStorage.getItem('hasUnpaidSubscription') === 'true';
	}
	
	/**
	* Returns whether to show the quick action option button
	*/
	public isShowQuickActionButton():boolean{
		if (this.router.url != '/suggestions') {
			return false;
		} 
		
		if (this.isRecruiter()) {
			return true;
		}
		
		return false;
	}
	
	public isShowGoPro():boolean{
		if (!this.currentUserAuth.hasPaidSubscription()) {
			return true;
		}
		return false;
	}
	
	/**
	* Opens the quick actions dialog
	*/
	public openQuickActions():void{
		this.popupsService.openModal(this.quickActionsBox);
		this.doListingCreditCheck();
		this.doMarketplaceCreditCheck();
	}
	
	public quickActionGoPro():void{
		this.quickActionsBox.nativeElement.close();
		this.creditsService.buySubscription();
		this.router.navigate(['recruiter-account']);
		
	}
	
	public quickActionJobboardPost():void{
		this.quickActionsBox.nativeElement.close();
		localStorage.setItem("quick-action-activated","on")
		this.router.navigate(['recruiter-listings']);		
	}
		
	public quickActionMarketplacePost():void{
		this.quickActionsBox.nativeElement.close();
		localStorage.setItem("quick-action-activated","on")
		this.router.navigate(['recruiter-marketplace']);
	}	
	
	public passedListingCreditCheck:boolean = false;
	
	public doListingCreditCheck():void{
		this.listingService.doCreditCheck().subscribe(passed => {
			this.passedListingCreditCheck = passed;
		});
	}
	
	public showNoCredits():void{
		this.creditsService.tokensExhaused();
	}
	
	public passedMarketplaceCreditCheck:boolean = false;


	public doMarketplaceCreditCheck():void{
		this.mpService.doCreditCheck().subscribe(passed => {
			this.passedMarketplaceCreditCheck = passed;
		});
	}
	
	
}