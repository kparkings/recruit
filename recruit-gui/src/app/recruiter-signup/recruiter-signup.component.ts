import { Component, OnInit } 							from '@angular/core';
import { NgbModal, NgbModalOptions}						from '@ng-bootstrap/ng-bootstrap';
import { UntypedFormGroup, UntypedFormControl }						from '@angular/forms';
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
	
	public feedbackBoxClass:string            = '';
  	public feedbackBoxTitle                   = '';
  	public feedbackBoxText:string             = '';

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
	public signupForm:UntypedFormGroup = new UntypedFormGroup({
		firstName: 			new UntypedFormControl(''),
		surname: 			new UntypedFormControl(''),
		email: 				new UntypedFormControl(''),
		company: 			new UntypedFormControl(''),
		preferredLanguage: 	new UntypedFormControl('ENGLISH')
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
			
			this.signupForm = new UntypedFormGroup({
				firstName: 			new UntypedFormControl(''),
				surname: 			new UntypedFormControl(''),
				email: 				new UntypedFormControl(''),
				company: 			new UntypedFormControl(''),
				preferredLanguage: 	new UntypedFormControl('ENGLISH')
			});
	
			this.open('feedbackBox', "Success",  true);
		}, err => {
			this.open('feedbackBox', "Failure",  false);
		});
		
	}
	
	/**
	* Opens the specified Dialog box
	*/
	public open(content:any, msg:string, success:boolean):void {
    
	    if (success) {
	      this.feedbackBoxTitle = 'Success';
	      this.feedbackBoxText = 'Your request has been submitted. Once accepted an email will be sent with your login details.';
	      this.feedbackBoxClass = 'feedback-success';
	    } else {
	      this.feedbackBoxTitle = 'Failure';
	      this.feedbackBoxText = 'Unfortunately there was a problem. Please email kparkings@gmail.com directly for your account';
	      this.feedbackBoxClass = 'feedback-failure';
	    }
	
	     let options: NgbModalOptions = {
	    	 centered: true
	   };
	
	  this.modalService.open(this.content, options);

  }

  /**
  *  Closes the confirm popup
  */
  public closeModal(): void {
    this.modalService.dismissAll();
  }


}