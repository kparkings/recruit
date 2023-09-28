import { Component, OnInit } 					from '@angular/core';
import { SearchAlert} 							from './search-alert';
import { CandidateServiceService }				from '../candidate-service.service';
import { Router}								from '@angular/router';

@Component({
  selector: 'app-recruiter-alerts',
  templateUrl: './recruiter-alerts.component.html',
  styleUrls: ['./recruiter-alerts.component.css']
})
export class RecruiterAlertsComponent implements OnInit {

	public alerts:Array<SearchAlert> 	= new Array<SearchAlert>();
	public alertToDeleteId:SearchAlert 	= new SearchAlert('','',new Array<string>(),new Array<string>(), 0, 0,'','','',new Array<string>(),'','');

  	constructor(public candidateService:CandidateServiceService, private router:Router) {
		this.fetchAlerts();	
 	}

  	ngOnInit(): void {
  	
}

	public fetchAlerts():void{
		
		this.alerts = new Array<SearchAlert>();
		
		this.candidateService.fetchCandidateSearchAlerts().subscribe(data => {
			
			this.alerts = data;		
		}, err => {
			
			if (err.status === 401 || err.status === 0) {
				sessionStorage.removeItem('isAdmin');
				sessionStorage.removeItem('isRecruter');
				sessionStorage.removeItem('isCandidate');
				sessionStorage.removeItem('loggedIn');
				sessionStorage.setItem('beforeAuthPage', 'view-candidates');
				this.router.navigate(['login-user']);
			}
    	});
		
	}

	/**
	* Returns whether or not Recruiter has alerts
	*/
	public hasAlerts():boolean{
		return this.alerts.length > 0;
	}
	
	public deleteAlert(alert:SearchAlert):void{
		this.alertToDeleteId = alert;
	}
	
	/**
	* Sends delete request to the Server
	*/
	public confirmDeleteAlert():void{
		this.candidateService.deleteCandidateSearchAlert(this.alertToDeleteId).subscribe(data => {
			this.resetAlertToDelete();
			this.fetchAlerts();	
		});
		
	}

	/**
	* Cancels deletion of Alert before delte 
	* request sent to the backend server
	*/
	public cancelDeleteAlert():void{
		this.resetAlertToDelete();
	}

	/**
	* Resets the alertToDelet 
	*/
	private resetAlertToDelete():void{
		this.alertToDeleteId = new SearchAlert('',
						'',
						new Array<string>(),
						new Array<string>(),
						0,
						0,
						'',
						'',
						'',
						new Array<string>(),
						false,
						false);
	}

}
