import { Component, ViewChild }				from '@angular/core';
import { Router}							from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { NgbModal, NgbModalOptions}			from '@ng-bootstrap/ng-bootstrap'
import { RecruiterMarketplaceService }		from './recruiter-marketplace.service';
import { EmailService }						from './email.service';
import { PopupsService }					from './popups.service';
import { CandidateNavService } 				from './candidate-nav.service';
import { CreditsService } 					from './credits.service';
import { NewsfeedService } 					from './newsfeed.service';

import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
}) 
export class AppComponent {

	@ViewChild('validationExBox', { static: false }) private validationExBox:any;
	@ViewChild('noCreditsBox', { static: false }) 	 private noCreditsBox:any;
	
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
				private translate: 				TranslateService){
		
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
	}
	
	private lastAlertRefresh!:Date;
	
	public refreschUnreadAlerts():void{
		
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
		
			this.mpService.updateUnseenMpPosts();
			this.mpService.fetchUnseenOpenPositionCount().subscribe(val => {
			//this.mpService.fetchUnseenMPPosts().subscribe(val => {
				this.unseenMpPosts = val;
			});
			this.emailService.updateUnseenEmails();
			this.emailService.fetchUnseenEmailsCount().subscribe(val => {
				this.unseenEmails = val;
			});
		
		}
		
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
	
}