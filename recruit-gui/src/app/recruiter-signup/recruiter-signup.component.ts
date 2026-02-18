import { Component, OnInit, ViewChild } 				from '@angular/core';
import { NgbModal, NgbModalOptions}						from '@ng-bootstrap/ng-bootstrap';
import { UntypedFormGroup, UntypedFormControl }			from '@angular/forms';
import { RecruiterService }								from '../recruiter.service';
import { TranslateService } 							from '@ngx-translate/core';

/**
* Component for a new Recruiter to sign up for a free trial
*/
@Component({
    selector: 'app-recruiter-signup',
    templateUrl: './recruiter-signup.component.html',
    styleUrls: ['./recruiter-signup.component.css','./recruiter-signup.component-mob.css'],
    standalone: false
})
export class RecruiterSignupComponent implements OnInit {

	@ViewChild('feedbackBox', { static: false }) private feedbackBox:any;
	
	public feedbackBoxClass:string            = '';
  	public feedbackBoxTitle                   = '';
  	public feedbackBoxText:string             = '';

	/**
	* Constructor
	*/
  	constructor(private readonly modalService: NgbModal, private readonly recruiterService: RecruiterService,private readonly translate: TranslateService) { }

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
	      this.feedbackBoxTitle = this.translate.instant('register-recruiter-success-title');
	      	      this.feedbackBoxText = this.translate.instant('register-recruiter-success');
	      this.feedbackBoxClass = 'feedback-success';
	    } else {
	      this.feedbackBoxTitle = this.translate.instant('register-recruiter-failure-title');
	      this.feedbackBoxText = this.translate.instant('register-recruiter-failure');
	      this.feedbackBoxClass = 'feedback-failure';
	    }
	
	     let options: NgbModalOptions = {
	    	 centered: true
	   };
	
		this.feedbackBox.nativeElement.showModal();
  }

  /**
  *  Closes the confirm popup
  */
  public closeModal(): void {
    this.modalService.dismissAll();
  }
  
  /**
  * Whether or not the user has authenticated with the System 
  */
  public isAuthenticated():boolean {
  	return sessionStorage.getItem('loggedIn') === 'true';
  }

}