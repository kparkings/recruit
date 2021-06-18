import { Component, OnInit } 					from '@angular/core';
import { FormGroup, FormControl }				from '@angular/forms';
import { AccountsService }						from '../accounts.service';
import { CandidateServiceService }				from '../candidate-service.service';
import { Candidate }							from './candidate';
import { environment }							from '../../environments/environment';
import { Router}								from '@angular/router';


@Component({
  selector: 'app-accounts',
  templateUrl: './accounts.component.html',
  styleUrls: ['./accounts.component.css']
})
export class AccountsComponent implements OnInit {
	
	currentTab:string 							= "recruiters";
	showCandidatesTab:boolean					= false;
	showRecruitersTab:boolean					= true;
	createAccountDetailsAvailable:boolean 		= false;
	recruiterUsername:string					= '';
	recruiterPassword:string					= '';
	candidates:Array<Candidate>					= new Array<Candidate>();
	
	public candidateFormGroup:FormGroup = new FormGroup({
		firstname:	new FormControl(''),
		surname: 	new FormControl(''),
		email:		new FormControl('')
	});
	
	constructor(private accountsService: AccountsService, public candidateService:CandidateServiceService, private router: Router) { }

  	ngOnInit(): void {}

	public createRecruiterAccountForm:FormGroup = new FormGroup({
    	proposedUserName:new FormControl(''),
    });

	public switchTab(tab:string){
		
		this.currentTab = tab;
		
		switch(tab){
			case "candidates":{
				this.showCandidatesTab=true;
				this.showRecruitersTab=false;
				break;
			}
			case "recruiters":{
				this.showCandidatesTab=false;
				this.showRecruitersTab=true;
				break;
			}
		}
		
	}
	
	/**
	* Creates a new Recruiter account. The preferedUser name will be used 
	* if available. If not a similar usename will be returned
	*/
	public createRecruiterAccount():void{
		
		this.recruiterUsername					= '';
		this.recruiterPassword					= '';      		
		this.createAccountDetailsAvailable 		= false;
			
		let proposedUsername:string = this.createRecruiterAccountForm.get('proposedUserName')!.value;
		 
		if (proposedUsername.length <= 4){
			return;
		}
		
		this.accountsService.addCandidate(proposedUsername).forEach(data => {

			this.recruiterUsername					= data.username;
			this.recruiterPassword					= data.password;      		
			this.createAccountDetailsAvailable 		= true;
			
		});
	}
	
	/**
	* Builds a query parameter string with the selected filter options
	*/
	private getCandidateFilterParamString():string{
    	
		const filterParams:string = 
								'orderAttribute=candidateId'
                                 + "&order=desc" 
                                 + '&page=0'
                                 + '&size=50'
                                 + this.getFirstnameParamString() 
                                 + this.getSurnameParamString()
                                 + this.getEmailParamString();
		return filterParams;
	
	}
	
	/**
	* Filter param for firstname
	*/
	private getFirstnameParamString():string{
		let value:string = this.candidateFormGroup.get("firstname")?.value;
		return value  == '' ? '' : '&firstname='+value;
	}
	
	/**
	* Filter param for surname 
	*/
	private getSurnameParamString():string{
		let value:string = this.candidateFormGroup.get("surname")?.value;
		return value  == '' ? '' : '&surname='+value;
	}
	
	/**
	* Filter param for email
	*/
	private getEmailParamString():string{
		let value:string = this.candidateFormGroup.get("email")?.value;
		return value  == '' ? '' : '&email='+value;
	}
	
	/**
	* Retrieves candidates from the backend
	*/
	public fetchCandidates(): void{
    	
    	this.candidates = new Array<Candidate>();

    	this.candidateService.getCandidates(this.getCandidateFilterParamString()).subscribe( data => {
  
      		data.content.forEach((c:Candidate) => {
        
        		const candidate:Candidate = new Candidate();

      			candidate.candidateId		= c.candidateId;
  				candidate.firstname			= c.firstname;
				candidate.surname			= c.surname;
				candidate.email				= c.email;
				candidate.available			= c.available;
				
      			this.candidates.push(candidate);

			});
        
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
	* Disables the Candidate
	*/
	disableCandidate(candidateId:string):void{
		this.candidateService.disableCandidate(candidateId);
	}
	
	/**
	* Enables the Candidate
	*/
	enableCandidate(candidateId:string):void{
		this.candidateService.enableCandidate(candidateId);
	}

}