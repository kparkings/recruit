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
	showActivePaidSubscription:boolean								= true;
	showCreditBasedSubscription:boolean								= true;
	showActivityBreakdown:boolean									= false;
	recruiterCount:number											= 0;
	activeCount:number												= 0;
	showDetailsForRecruiter:Recruiter								= new Recruiter();
	
	public recruiters:Array<RecruiterDetails> = new Array<RecruiterDetails>();
	public recruiterToDelete:string = '';
	
	/**
	* Constructor 
	*/
	constructor(private recruiterService:RecruiterService, private router: Router){
		this.fetchRecruiters();
	}
	
	/**
	* Returns Rectuiter with Credit based subscription orderd by most active 
	*/
	public  getCreditBasedRecruiters():Array<RecruiterDetails>{
		return  this.recruiters.filter(rec => rec.isPaidSubscription == false).sort((a, b) => 
			this.getActivityScore(b.loginSummary)
				.localeCompare(this.getActivityScore(a.loginSummary)));
	}
	
	/**
		* Returns Rectuiter with a paid subscription orderd by most active 
		*/
	public  getSubscriptionRecruiters():Array<RecruiterDetails>{
		return  this.recruiters.filter(rec => rec.isPaidSubscription == true && rec.subscriptionStatus != 'DISABLED_PENDING_PAYMENT').sort((a, b) => 
					this.getActivityScore(b.loginSummary)
						.localeCompare(this.getActivityScore(a.loginSummary)));
	}
	
	/**
	* Returns the number of recriters with a certain activity level
	*/
	public getActivityLevel(level:string){
		return this.recruiters.filter(r => r.loginSummary.activityLevel == level).length;
	}
	
	
	/**
	* Toggles whether recruiters with active paid subscriptions are shown
	*/
	toggleShowActivePaidSubscription():void{
		this.showActivePaidSubscription = !this.showActivePaidSubscription;
	}
	
	/**
	* Toggles whether recruiters with credit based subscriptions are shown
	*/
	toggleShowCreditBasedSubscription():void{
		this.showCreditBasedSubscription = !this.showCreditBasedSubscription;
	}
	
	/**
	* Reutrns the total number of active recruiters
	*/
	public getTotalActiveRecruiters():number{
		return this.recruiters.filter(rec => rec.subscriptionStatus != 'DISABLED_PENDING_PAYMENT').length;
	}
	
	/**
	* Retrieves recruiters from the backend
	*/
	public fetchRecruiters(): void{
		this.recruiterService.getRecruiters().subscribe( data => {
  			this.recruiterService.getLoginSummary().subscribe(loginStats => {
				data.forEach((r:Recruiter) => {
					let loginSummary:LoginSummary = loginStats.loginSummaries.filter(u => u.userId === r.userId)[0];
					if (!loginSummary) {
						loginSummary = new LoginSummary();
					}
					this.addRecruiterToSubscriptionBucker(r, loginSummary);
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
	private getActivityScore(loginSummary:LoginSummary):string{
		
		if (!loginSummary) {
			return "0";
		}
		
		if (loginSummary.activityLevel == 'HIGH') {
			return "4";
		}
		if (loginSummary.activityLevel == 'MEDIUM') {
			return "3";
		}
		if (loginSummary.activityLevel == 'LOW') {
			return "2";
		}
		
		return "1";
								
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
		
		if (loginSummary) {
			if (loginSummary.activityLevel != 'NONE') {
				this.activeCount = this.activeCount +1;
			}
			
		}	
		
		if (activeSubscription.type == 'CREDIT_BASED_SUBSCRIPTION') {
			
			let rec:RecruiterDetails = new RecruiterDetails();
			rec.recruiter = recruiter;
			rec.loginSummary = loginSummary;
			rec.isPaidSubscription = false;
			rec.subscriptionStatus = ''
			
			this.recruiters.push(rec);
			return;
		}
		
		if (	activeSubscription.status == 'AWAITING_ACTIVATION'
			|| 	activeSubscription.status == 'ACTIVE_PENDING_PAYMENT'
			|| 	activeSubscription.status == 'ACTIVE'
			|| 	activeSubscription.status == 'ACTIVE_INVOICE_SENT'
			||  activeSubscription.status == 'DISABLED_PENDING_PAYMENT'){
			this.recruiters.push()
				let rec:RecruiterDetails = new RecruiterDetails();
							rec.recruiter = recruiter;
							rec.loginSummary = loginSummary;
							rec.isPaidSubscription = true;
							rec.subscriptionStatus = activeSubscription.status;
							this.recruiters.push(rec);
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
	
	/**
	* Returns number of active/inactive Recruiters
	*/
	public getPercentActiveInactive():string{
		return " ( " + this.activeCount + "/ "+this.recruiterCount+" )";
	}
	
	/**
	* Toggles the display of Recruiter activity 
	* breakdown information 
	*/
	public toggleActivityBreakdown():void{
		this.showActivityBreakdown = !this.showActivityBreakdown;
	}

}

/**
* Class packages recruiter and Status together 
*/
export class RecruiterDetails{
	public recruiter:Recruiter 			= new Recruiter();
	public loginSummary:LoginSummary 	= new LoginSummary();
	public isPaidSubscription:boolean 	= false;
	public subscriptionStatus:string	= '';
}
