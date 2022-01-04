import { Component, OnInit } 					from '@angular/core';
import { Router}								from '@angular/router';
import { RecruiterService }						from '../recruiter.service';
import { Recruiter }							from './recruiter';
import { Subscription }							from './subscription';
import { NgbModal}								from '@ng-bootstrap/ng-bootstrap';

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
	
}
