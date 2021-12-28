import { Component, OnInit } 							from '@angular/core';
import { NgbModal, NgbModalOptions}						from '@ng-bootstrap/ng-bootstrap';
import { FormGroup, FormControl }						from '@angular/forms';
import { RecruiterService }								from '../recruiter.service';
import { ViewChild }									from '@angular/core';

/**
* Component for a new Recruiter to sign up for a free trial
*/
@Component({
  selector: 'app-recruiter-signup',
  templateUrl: './recruiter-signup.component.html',
  styleUrls: ['./recruiter-signup.component.css']
})
export class RecruiterSignupComponent implements OnInit {

	@ViewChild('feedbackBox', { static: false }) private content:any;
	
	/**
	* Constructor
	*/
  	constructor(private modalService: NgbModal, private recruiterService: RecruiterService) { }

	/**
	* Initializes component
	*/
  	ngOnInit(): void {
  	}

	/**
	* Backing bean for the recruiter signip form
	*/
	public signupForm:FormGroup = new FormGroup({
		firstName: 			new FormControl(''),
		surname: 			new FormControl(''),
		email: 				new FormControl(''),
		company: 			new FormControl(''),
		preferredLanguage: 	new FormControl('ENGLISH')
	});
	
	/**
	* Whether or not the start free trial button is active
	*/
	public isFreeTrialButtonEnabled():boolean {
		
		if (this.signupForm.get('firstnane')?.value == '') {
			return true;
		}
		
		if (this.signupForm.get('surname')?.value == '') {
			return true;
		}
		
		if (this.signupForm.get('email')?.value == '') {
			return true;
		}
		
		if (this.signupForm.get('company')?.value == '') {
			return true;
		}
	
		return false;
	}
	
	/**
	* Sends request to start free trial
	*/
	public startFreeTrial():void{
		
		let firstName:string 	= this.signupForm.get('firstName')?.value;
		let surname:string 		= this.signupForm.get('surname')?.value;
		let email:string 		= this.signupForm.get('email')?.value;
		let companyName:string 	= this.signupForm.get('company')?.value;
		let language:string  	= this.signupForm.get('preferredLanguage')?.value;
		
		this.recruiterService.registerForFreeTrial(firstName, surname, email, companyName, language).subscribe(data => {
			console.log('RESPONSE -> ' + JSON.stringify(data));
		});
		
	}

}