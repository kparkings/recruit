import { Component, OnInit } 					from '@angular/core';
import { Router}								from '@angular/router';
import { RecruiterService }						from '../recruiter.service';
import { Recruiter }							from './recruiter';
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
	showAccountTab:boolean				=true;
	recruiter:Recruiter					= new Recruiter();
		
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
	
	
	
}
