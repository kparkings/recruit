import { Component, OnInit, ViewChild }							from '@angular/core';
import { UntypedFormGroup, UntypedFormControl }					from '@angular/forms';
import { AuthService }                                          from '../auth.service';
import { RecruiterService }                                     from '../recruiter.service';
import { Router}                                                from '@angular/router';
import { NgbModal, NgbModalOptions}								from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } 									from '@ngx-translate/core';
import { CreditsService } 										from '../credits.service';
import { CandidateServiceService } 								from '../candidate-service.service';
import { AppComponent} 											from '../app.component';

@Component({
    selector: 'app-login-user',
    templateUrl: './login-user.component.html',
    styleUrls: ['./login-user.component.css'],
    standalone: false
})
/**
* Component for logging Users into the System 
*/
export class LoginUserComponent implements OnInit {
	
	@ViewChild('feedbackBox', { static: false }) private readonly feedbackBox:any;
	@ViewChild('resetPasswordDialog', { static: false }) private readonly resetDialog:any;
	
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
  constructor(	private readonly recruiterService:RecruiterService, 
  				private readonly candidateService:CandidateServiceService,
  				private readonly authService: AuthService, 
  				private readonly modalService: NgbModal, 
  				private readonly router: Router,
  				private readonly translate: TranslateService,
  				private readonly creditService:CreditsService,
				private readonly appComponent:AppComponent) { }

  /**
  * Performs initialization
  */
  ngOnInit(): void {
	  if(sessionStorage.getItem("new-subscription") == 'true') {
		  this.showSubscriptionMsg = true;
		  sessionStorage.setItem('beforeAuthPage', '/suggestions');
	  }
	  this.appComponent.hideCandidateProfile();
	  this.appComponent.hideInliceCV();
	  	  
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
			
			//this.appComponent.refreshUnreadNewsFeedNotifications();	
			//this.appComponent.hasUnreadNewsFeedItems();
			
			//this.scheduleOpenChatRefresh = window.setInterval(()=> {
			//	this.appComponent.refreshUnreadNewsFeedNotifications();		
			//	this.appComponent.hasUnreadNewsFeedItems();
			//},30000);
			
			
			const beforeAuthPage: any = sessionStorage.getItem('beforeAuthPage');

			if (roles.includes('ROLE_CANDIDATE') && beforeAuthPage == 'suggestions') {
				sessionStorage.setItem('beforeAuthPage', 'newsfeed');
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
	
		//This will work if the user doesnt have both a Recruiter and candidate account with the same email and 
		//needs to reset the candidates password. Very few cases and this will work almost always. To be improved later
		//but need this working to get the availability email functionality working so I can cut down on the manual admin time
		//of checking user availability. Improvement on no candidate reset and can be made perfect in the future
	
		let email = this.formBeanForgottenPassword.get("email")?.value;
		
		this.recruiterService.resetPassword(email).subscribe(data => {
			
			this.formBeanForgottenPassword = new UntypedFormGroup({
				email: new UntypedFormControl()
			});
		
			this.showForgottenPassword = false;
			this.resetDialog.nativeElement.showModal();
		},
		err => {
			
			this.candidateService.resetPassword(email).subscribe(data => {
			
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
	
	/**
	* Whether or not the user has authenticated with the System 
	*/
	public isAuthenticated():boolean {
		return sessionStorage.getItem('loggedIn') === 'true';
	}
	
	
}