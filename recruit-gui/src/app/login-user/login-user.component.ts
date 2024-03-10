import { Component, OnInit }									from '@angular/core';
import { ReactiveFormsModule, UntypedFormGroup, UntypedFormControl }			from '@angular/forms';
import { AuthService }                                          from '../auth.service';
import { RecruiterService }                                     from '../recruiter.service';
import { Router}                                                from '@angular/router';
import { TemplateRef, ViewChild,ElementRef, AfterViewInit  }		from '@angular/core';
import { NgbModal, NgbModalOptions, ModalDismissReasons}			from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { CreditsService } from '../credits.service';

@Component({
  selector: 'app-login-user',
  templateUrl: './login-user.component.html',
  styleUrls: ['./login-user.component.css']
})
/**
* Component for logging Users into the System 
*/
export class LoginUserComponent implements OnInit {
	
	@ViewChild('feedbackBox', { static: false }) private feedbackBox:any;
	@ViewChild('resetPasswordDialog', { static: false }) private resetDialog:any;
	
	public showForgottenPassword:boolean 	= false;
	public showSubscriptionMsg:boolean 		= false;
	public failureMessage:string			= "";
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
  constructor(	private recruiterService:RecruiterService, 
  				private authService: AuthService, 
  				private modalService: NgbModal, 
  				private router: Router,
  				private translate: TranslateService,
  				private creditService:CreditsService) { }

  /**
  * Performs initialization
  */
  ngOnInit(): void {
	  if(sessionStorage.getItem("new-subscription") == 'true') {
		  this.showSubscriptionMsg = true;
		  sessionStorage.setItem('beforeAuthPage', '/suggestions');
	  }
  }

	/**
	* Attempts to Log the user into the system
	*/
	public login(): void{
    
    	sessionStorage.removeItem("new-subscription");
    	this.showSubscriptionMsg = false;
		
		sessionStorage.setItem('hasPaidSubscription', 'false');
		
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
				
				this.creditService.hasPaidSubscription().subscribe(res => {
					
					console.log("PAID RESULT ====> " + JSON.stringify(res));
					sessionStorage.setItem('hasPaidSubscription', ''+res)
				});
				
			} else {
				sessionStorage.setItem('isRecruiter',      				'false');
			}
			
			if (roles.includes('ROLE_CANDIDATE')) {
				sessionStorage.setItem('isCandidate',     	 			'true');
			} else {
				sessionStorage.setItem('isCandidate',      				'false');
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
				this.failureMessage = this.translate.instant('login-user-invalid-login');
				this.feedbackBox.nativeElement.showModal();
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
			this.resetDialog.nativeElement.showModal();
		},
		err => {
			this.failureMessage = this.translate.instant('login-user-unable-to-send-email');
			this.feedbackBox.nativeElement.showModal();
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