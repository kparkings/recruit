import { Component } 						from '@angular/core';
import { Router}							from '@angular/router';
import { CookieService } 					from 'ngx-cookie';
import { NgbModal, NgbModalOptions}			from '@ng-bootstrap/ng-bootstrap'
import { DeviceDetectorService } 			from 'ngx-device-detector';
import { RecruiterMarketplaceService }		from './recruiter-marketplace.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
}) 
export class AppComponent {

	private readonly termsAndConditionsCookieVersion:string = '2';

  	title = 'Arenella-ICT - IT Candidates for Recruiters';

	public termsAndConditionsAccepted:boolean = false;

	public isMobile:boolean = false;
	
	public unseenMpPosts:number = 0;
	
	/**
	* Constructor
	*/
	constructor(private router: 			Router, 
				private cookieService: 		CookieService, 
				private modalService: 		NgbModal, 
				private deviceDetector: 	DeviceDetectorService,
				private mpService:			RecruiterMarketplaceService){
		
		this.isMobile = deviceDetector.isMobile();
		
		if (this.isRecruiterNoSubscription() || this.hasUnpaidSubscription()) {
			this.router.navigate(['recruiter-account']);
		}
		
		if (cookieService.get(this.getTandCsCookieName())) {
			this.termsAndConditionsAccepted = true;
		}
		
		this.mpService.fetchUnseenMPPosts().subscribe(val => {
			this.unseenMpPosts = val;
		});
		
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
		
		this.cookieService.put(this.getTandCsCookieName(),"Accepted",{expires: date});
		
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
