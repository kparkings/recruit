import { Component } 									from '@angular/core';
import { Router } 										from '@angular/router';
import { RecruiterService } 							from 'src/app/recruiter.service';
import { Recruiter } 									from '../recruiter';
import { LoginSummary } from 'src/app/login-summary';

@Component({
  selector: 'app-recruiters',
  standalone: false,
  templateUrl: './recruiters.component.html',
  styleUrl: './recruiters.component.css'
})
export class RecruitersComponent {


	showRecruitersTabSummary:boolean								= true;
	showRecruitersTabDetails:boolean								= false;

	activePaidSubscription:Map<Recruiter, LoginSummary>				= new Map<Recruiter,LoginSummary>();
	creditBasedSubscription:Map<Recruiter, LoginSummary>			= new Map<Recruiter,LoginSummary>();
	
	showActivePaidSubscription:boolean								= true;
	showCreditBasedSubscription:boolean								= true;
	
	recruiterCount:number											= 0;
	
	activeCount:number												= 0;
		
	showDetailsForRecruiter:Recruiter								= new Recruiter();
	//private recruiterStats:Map<Recruiter, LoginSummary> = new Map<Recruiter, LoginSummary>();
	
	public recruiterToDelete:string = '';
	
	constructor(private recruiterService:RecruiterService, private router: Router){
		this.fetchRecruiters();
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
	
	public getTotalActiveRecruiters():number{
		return this.activePaidSubscription.keys.length + this.creditBasedSubscription.keys.length;
	}
	
	/**
	* Retrieves recruiters from the backend
	*/
	public fetchRecruiters(): void{
		
		this.recruiterCount 					= 0;
		//this.recruiters 						= new Array<Recruiter>();
		this.activePaidSubscription 			= new Map<Recruiter,LoginSummary>();
		this.creditBasedSubscription			= new Map<Recruiter,LoginSummary>();
		
    	this.recruiterService.getRecruiters().subscribe( data => {
  			data.forEach((r:Recruiter) => {
				//this.recruiters.push(r);
				
				//this.recruiterCount = this.recruiterCount +1;
				this.recruiterService.getLoginSummary(r.userId).subscribe(loginSummary => {
						this.addRecruiterToSubscriptionBucker(r, loginSummary);
						//this.recruiterStats.set(r, loginSummary);			
				});
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
	* Returns a score indicating how actively a Recruiter has 
	* been logging into the system 
	*/
	private getActivityScore(loginSummary:LoginSummary):number{
		
		console.log(loginSummary.loginsThisWeeek);
		console.log(loginSummary.loginsLast30Days);
		console.log(loginSummary.loginsLast60Days);
		console.log(loginSummary.loginsLast90Days);
		
		return loginSummary.loginsThisWeeek 
			+ loginSummary.loginsLast30Days
			+ loginSummary.loginsLast60Days
			+ loginSummary.loginsLast90Days;
	}
	
	/**
	* Examins the Recruiters current subscription and assigns them to 
	* a corresponding bucket
	* recruiter - Recruiter to be added to the appropriate Subscrition bucket
	*/
	private addRecruiterToSubscriptionBucker(recruiter:Recruiter, loginSummary:LoginSummary):void{
		
		let activeSubscription = recruiter.subscriptions.filter(s => s.currentSubscription == true)[0];
		
		if (!activeSubscription) {
			return;
		}

		this.recruiterCount = this.recruiterCount+1;
		
		console.log("ActivityCount == " + this.getActivityScore(loginSummary));
		
		if(this.getActivityScore(loginSummary) > 0){
			this.activeCount = this.activeCount +1;
		}
		
		if (activeSubscription.type == 'CREDIT_BASED_SUBSCRIPTION') {
			console.log("ADDING 1");
			this.creditBasedSubscription.set(recruiter, loginSummary);
			this.creditBasedSubscription = new Map([...this.creditBasedSubscription.entries()]
						.sort((a, b) => this.getActivityScore(b[1]) - this.getActivityScore(a[1])));
			return;
		}
		
		if (	activeSubscription.status == 'AWAITING_ACTIVATION'
			|| 	activeSubscription.status == 'ACTIVE_PENDING_PAYMENT'
			|| 	activeSubscription.status == 'ACTIVE'
			||  activeSubscription.status == 'DISABLED_PENDING_PAYMENT'){
				console.log("ADDING 2");
				this.activePaidSubscription.set(recruiter, loginSummary);
			this.activePaidSubscription = new Map([...this.activePaidSubscription.entries()]
						.sort((a, b) => this.getActivityScore(b[1]) - this.getActivityScore(a[1])));
			return;
		}
		 
	}
	
	/**
	* Set recruiter to delete ready for confirmation
	*/
	public deleteRecruiter(recruiterId:string):void{
		this.recruiterToDelete = recruiterId;
	}
	
	/**
	* Send definitive request to delete recruiter
	*/
	public confirmDeleteRecruiter():void{
		this.recruiterService.deleteRecruiter(this.recruiterToDelete).subscribe(() => {
			this.fetchRecruiters();
			this.recruiterToDelete = '';
		})
	}
	
	/**
	* Deselect recruiter to be deleted
	*/
	public cancelDeleteRecruiter():void{
		this.recruiterToDelete = '';
	}
	
	/**
	* Toggles the display section of the selected Recruiter row 
	*/
	public toggleRecruiterDetails(recruiter:Recruiter):void{
		
		if (this.showDetailsForRecruiter == recruiter) {
			this.showDetailsForRecruiter = new Recruiter();	
		} else {
			this.showDetailsForRecruiter = recruiter;
		}
		
	}
	
	public getPercentActiveInactive():string{
		return " ( " + this.activeCount + "/ "+this.recruiterCount+" )";
	}
	
	/**
	* Provides an indication of how active the Recruiter is. 
	* Returns the postfic for a css class to show the activity.
	* If a recruiter has logged in at least once in each timeframe
	* it means they have been active. If they have only been active 
	* in one or none then they have not been active. This is to 
	* provided an at a glance look at Recruiters activity to see 
	* who is consistently using the site
	*/
	public getRecruiterActivity(recruiter:Recruiter):string{
	
		//let allRecruiters:Map<Recruiter, LoginSummary> = new Map<Recruiter, LoginSummary>();
		
		let allRecruiters:Map<Recruiter, LoginSummary> = new Map([...this.activePaidSubscription, ...this.creditBasedSubscription]);
			
		if (!allRecruiters.has(recruiter)) {
			return 'No record for recruiter';
		}
		
		let loginSummary = allRecruiters?.get(recruiter);
		let count:number = 0;
		
		if (loginSummary!.loginsThisWeeek > 0) {
			count = count+1;
		}
		
		if (loginSummary!.loginsLast30Days > 0) {
			count = count+1;
		}
	
		if (loginSummary!.loginsLast60Days > 0) {
			count = count+1;
		}
		
		if (loginSummary!.loginsLast90Days > 0) {
			count = count+1;
		}
		
		if (count == 4) {
			return '-High';
		}
		
		if (count > 2) {
			return '-Medium';
		}
		
		if (count >= 1) {
			return '-Low';
		}
		
		return '-None';	

	}
	

}
