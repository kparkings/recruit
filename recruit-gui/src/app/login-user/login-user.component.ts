import { Component, OnInit }									from '@angular/core';
import { ReactiveFormsModule, UntypedFormGroup, UntypedFormControl }			from '@angular/forms';
import { AuthService }                                          from '../auth.service';
import { RecruiterService }                                     from '../recruiter.service';
import { Router}                                                from '@angular/router';
import {TemplateRef, ViewChild,ElementRef, AfterViewInit  }		from '@angular/core';
import {NgbModal, NgbModalOptions, ModalDismissReasons}			from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-login-user',
  templateUrl: './login-user.component.html',
  styleUrls: ['./login-user.component.css']
})
/**
* Component for logging Users into the System 
*/
export class LoginUserComponent implements OnInit {
	
	@ViewChild('feedbackBox', { static: false }) private content:any;
	@ViewChild('resetPasswordDialog', { static: false }) private resetDialog:any;
	
	public showForgottenPassword:boolean = false;
	
	public formBeanForgottenPassword:UntypedFormGroup = new UntypedFormGroup({
		email: new UntypedFormControl(),
	});
	
   	public formBean:UntypedFormGroup = new UntypedFormGroup({
		username: new UntypedFormControl(),
		password: new UntypedFormControl()
	});

   /**
   * Constructor 
   * @param authService - Services for authenticating users
   * @param router      - Angular router
   */
  constructor(private recruiterService:RecruiterService, private authService: AuthService, private modalService: NgbModal, private router: Router) { }

  /**
  * Performs initialization
  */
  ngOnInit(): void {
  }

	/**
	* Attempts to Log the user into the system
	*/
	public login(): void{
    
		this.authService.authenticate(this.formBean.get('username')?.value, this.formBean.get('password')?.value).subscribe( data => {
     
			const roles:Array<string> = data;
			
			if (roles.includes('ROLE_RECRUITERNOSUBSCRITION')) {
				sessionStorage.setItem('isRecruiterNoSubscription',		'true');
				sessionStorage.setItem('beforeAuthPage', '/recruiter-account');
			} else {
				sessionStorage.setItem('isRecruiterNoSubscription',		'false');
			}
	
			if (roles.includes('ROLE_ADMIN')) {
				sessionStorage.setItem('isAdmin',      					'true');
			} else {
				sessionStorage.setItem('isAdmin',      					'false');
			}
	
			if (roles.includes('ROLE_RECRUITER')) {
				sessionStorage.setItem('isRecruiter',     	 			'true');
			} else {
				sessionStorage.setItem('isRecruiter',      				'false');
			}
			
			if (roles.includes('ROLE_CANDIDATE')) {
				sessionStorage.setItem('isCandidate',     	 			'true');
			} else {
				sessionStorage.setItem('isCandidate',      				'false');
			}
	      
			sessionStorage.setItem('loggedIn',      					'true');
	      
			sessionStorage.setItem("userId", 							this.formBean.get('username')?.value);


			const beforeAuthPage: any = sessionStorage.getItem('beforeAuthPage');

			if (roles.includes('ROLE_CANDIDATE') && beforeAuthPage == 'suggestions') {
				sessionStorage.setItem('beforeAuthPage', 'email');
	    		this.router.navigate([beforeAuthPage]);
	    	} else {
	 
				sessionStorage.setItem('beforeAuthPage', beforeAuthPage);
	    
				this.router.navigate([beforeAuthPage]);
	    	}

		}, err => {
			if (err.status === 401) {
				this.open(this.content);
		}
	});

  }

  public open(content:any):void {

	let options: NgbModalOptions = {
     centered: true
   	};

  	this.modalService.open(content, options);
  }

  /**
  *  Closes the popup
  */
  public closeModal(): void {
    this.modalService.dismissAll();
  }

	/**
	* Shows/Hides forgotten password screen
	* @param show - Whether or not to show the forgotten passworkd screen
	*/
  	public toggleForgottenPassword(show:boolean):void{
		this.showForgottenPassword = show;
	}
	
	/**
	* Sends request to email user new login details
	*/
	public resetPassword():void{
	
		let email = this.formBeanForgottenPassword.get("email")?.value;
		
		this.recruiterService.resetPassword(email).subscribe(data => {
			
			this.formBeanForgottenPassword = new UntypedFormGroup({
				email: new UntypedFormControl()
			});
		
			this.showForgottenPassword = false;
		
			this.open(this.resetDialog);
			
		},
		err => {
			console.log('Error resetting password');
			this.showForgottenPassword = false;
		});
		
	}

	/**
	* Cleans out the reset password details and returns to the 
	* login screen
	*/
	public cancelResetPassword():void{
		
		this.formBeanForgottenPassword = new UntypedFormGroup({
			email: new UntypedFormControl()
		});
		
		this.showForgottenPassword = false;
	
	}
	
}