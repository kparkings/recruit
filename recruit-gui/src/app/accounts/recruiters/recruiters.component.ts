import { Component } 									from '@angular/core';
import { Router } 										from '@angular/router';
import { RecruiterService } 							from 'src/app/recruiter.service';
import { Recruiter } 									from '../recruiter';

@Component({
  selector: 'app-recruiters',
  standalone: false,
  templateUrl: './recruiters.component.html',
  styleUrl: './recruiters.component.css'
})
export class RecruitersComponent {


	showRecruitersTabSummary:boolean								= true;
	showRecruitersTabDetails:boolean								= false;
	
	recruiters:Array<Recruiter>										= new Array<Recruiter>();

	activePaidSubscription:Array<Recruiter>							= Array<Recruiter>();
	creditBasedSubscription:Array<Recruiter>						= Array<Recruiter>();
	
	showActivePaidSubscription:boolean								= true;
	showCreditBasedSubscription:boolean								= true;
	
	recruiterCount:number											= 0;
	
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
		return this.activePaidSubscription.length + this.creditBasedSubscription.length;
	}
	
	/**
	* Retrieves recruiters from the backend
	*/
	public fetchRecruiters(): void{
		
		this.recruiterCount 					= 0;
		this.recruiters 						= new Array<Recruiter>();
		this.activePaidSubscription 			= new Array<Recruiter>();
		this.creditBasedSubscription			= new Array<Recruiter>();
		
    	this.recruiterService.getRecruiters().subscribe( data => {
  			data.forEach((r:Recruiter) => {
				this.recruiters.push(r);
				this.addRecruiterToSubscriptionBucker(r);
				this.recruiterCount = this.recruiterCount +1;
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
	* Examins the Recruiters current subscription and assigns them to 
	* a corresponding bucket
	* recruiter - Recruiter to be added to the appropriate Subscrition bucket
	*/
	private addRecruiterToSubscriptionBucker(recruiter:Recruiter):void{
		
		let activeSubscription = recruiter.subscriptions.filter(s => s.currentSubscription == true)[0];
		
		if (!activeSubscription) {
			return;
		}
		
		if (activeSubscription.type == 'CREDIT_BASED_SUBSCRIPTION') {
			this.creditBasedSubscription.push(recruiter);
			return;
		}
		
		if (	activeSubscription.status == 'AWAITING_ACTIVATION'
			|| 	activeSubscription.status == 'ACTIVE_PENDING_PAYMENT'
			|| 	activeSubscription.status == 'ACTIVE'
			||  activeSubscription.status == 'DISABLED_PENDING_PAYMENT'){
			this.activePaidSubscription.push(recruiter);
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

}