import { Component, OnInit } 					from '@angular/core';
import { FormGroup, FormControl }				from '@angular/forms';
import { AccountsService }						from '../accounts.service';

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
  
	constructor(private accountsService: AccountsService) { }

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

}