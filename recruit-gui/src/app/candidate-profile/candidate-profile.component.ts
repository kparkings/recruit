import { Component } 											from '@angular/core';
import { PhotoAPIOutbound, CandidateProfile, Language, Rate } 	from './candidate-profile';
import { CandidateProfileService} 								from '../candidate-profile-service.service';
import { CandidateServiceService} 								from '../candidate-service.service';
import { AddCandidateProfileRequest }							from './add-candidate-profile'
import { UntypedFormGroup, UntypedFormControl }					from '@angular/forms';
import { TupleStrValueByPos }									from './tuple-string-pos-pipe';
import { EmailService, EmailRequest }							from '../email.service';
import { NgbModal, NgbModalOptions}								from '@ng-bootstrap/ng-bootstrap';
import { CandidateFunction }									from '../candidate-function';
import { UpdateCandidateProfileRequest}							from './update-candidate-profile-req';
import { Router}												from '@angular/router';

@Component({
  selector: 'app-candidate-profile',
  templateUrl: './candidate-profile.component.html',
  styleUrls: ['./candidate-profile.component.css']
})
export class CandidateProfileComponent {
	public candidateProfile:CandidateProfile = new CandidateProfile();
	public candiadteProfiles:Array<CandidateProfile> = new Array<CandidateProfile>();
	public selectedCandidateProfile:CandidateProfile = new CandidateProfile();
	public currentView = 'view-main'; 
	private imageFile!:File| any;
	public functionTypes:	 	Array<CandidateFunction> 	= new Array<CandidateFunction>();
	
	/**
	* Constructor
	*/
	constructor(private candidateService:CandidateServiceService, private emailService:EmailService, private modalService:NgbModal, private router:Router){
		
		this.candidateService.loadFunctionTypes().forEach(funcType => {
      		this.functionTypes.push(funcType);
    	});

		if(this.isCandidate()){
			this.fetchCandidate();
		}
	}
	
	/**
	*Extract the level of proficiency in a language of the Candidate
	*/
	public getLanguageLevel(language:string):string{
		if (this.candidateProfile.languages.filter(l => l.language == language)[0]){
			return this.candidateProfile.languages.filter(l => l.language == language)[0].level;	
		} else {
			return '';
		}
		
	}
	
	/**
	* Fetches Candidate
	*/
	public fetchCandidate():void{
		this.candidateService.getCandidateById(this.getOwnUserId()).subscribe( candidate => {
				this.candidateProfile = candidate;
				
				console.log(JSON.stringify(this.candidateProfile));
				
				let lvlEnglish 	= 'UNKNOWN';
				let lvlDutch 	= 'UNKNOWN';
				let lvlFrench 	= 'UNKNOWN';
				
				if(candidate.languages.filter(l => l.language === 'ENGLISH').length === 1){
					lvlEnglish 	= candidate.languages.filter(l => l.language === 'ENGLISH')[0].level;
				}
				
				if(candidate.languages.filter(l => l.language === 'DUTCH').length === 1){
					lvlDutch 	= candidate.languages.filter(l => l.language === 'DUTCH')[0].level;
				}
				if(candidate.languages.filter(l => l.language === 'FRENCH').length === 1){
					lvlFrench 	= candidate.languages.filter(l => l.language === 'FRENCH')[0].level;
				}
			
				let rateCurrency:string 	= '';
				let ratePeriod:string 		= '';
				let rateValue:String 		= '0';
				
				if (this.candidateProfile.rate) {
					rateCurrency	= this.candidateProfile.rate.currency;
					ratePeriod 		= this.candidateProfile.rate.period;
					rateValue		= this.candidateProfile.rate.value;
				}
				
				this.candidateProfileFormBean = new UntypedFormGroup({
					introduction: 				new UntypedFormControl(this.candidateProfile.introduction),
					candidateId: 				new UntypedFormControl(this.candidateProfile.candidateId),
					firstname: 					new UntypedFormControl(this.candidateProfile.firstname),
					surname: 					new UntypedFormControl(this.candidateProfile.surname),
					email: 						new UntypedFormControl(this.candidateProfile.email),
					function: 					new UntypedFormControl(this.candidateProfile.function),
					roleSought: 				new UntypedFormControl(this.candidateProfile.roleSought),
					country: 					new UntypedFormControl(this.candidateProfile.country),
					city: 						new UntypedFormControl(this.candidateProfile.city),
					perm: 						new UntypedFormControl(this.candidateProfile.perm),
					freelance: 					new UntypedFormControl(this.candidateProfile.freelance),
					yearsExperience: 			new UntypedFormControl(this.candidateProfile.yearsExperience),
					available: 					new UntypedFormControl(this.candidateProfile.available),
					english: 					new UntypedFormControl(lvlEnglish),
					dutch: 						new UntypedFormControl(lvlDutch),
					french: 					new UntypedFormControl(lvlFrench),
					rateCurrency:				new UntypedFormControl(rateCurrency),
					ratePeriod:					new UntypedFormControl(ratePeriod),
					rateValue:					new UntypedFormControl(rateValue),
				});
				
				this.currentView = 'view-main';
			}, err => {
				if (err.status === 401 || err.status === 0) {
					sessionStorage.removeItem('isAdmin');
					sessionStorage.removeItem('isRecruter');
					sessionStorage.removeItem('loggedIn');
					sessionStorage.setItem('beforeAuthPage', 'view-candidates');
					this.router.navigate(['login-user']);
			}
			});
	}
	
	
	/**
	* Form to add new Profile
	*/
	public candidateProfileFormBean:UntypedFormGroup = new UntypedFormGroup({
		introduction: 				new UntypedFormControl(''),
		candidateId: 				new UntypedFormControl(0),
		firstname: 					new UntypedFormControl(''),
		surname: 					new UntypedFormControl(''),
		email: 						new UntypedFormControl(),
		function: 					new UntypedFormControl(),
		roleSought: 				new UntypedFormControl(''),
		country: 					new UntypedFormControl(''),
		city: 						new UntypedFormControl(''),
		perm: 						new UntypedFormControl(''),
		freelance: 					new UntypedFormControl('2'),
		yearsExperience: 			new UntypedFormControl(0),
		available: 					new UntypedFormControl(true),
		lastAvailabilityCheck: 		new UntypedFormControl(new Date()),
		english: 					new UntypedFormControl(),
		dutch: 						new UntypedFormControl(),
		french: 					new UntypedFormControl(),
		rateCurrency:				new UntypedFormControl(),
		ratePeriod:					new UntypedFormControl(),
		rateValue:					new UntypedFormControl(),
	});
	
	public introduction:string 			= '';
	public photo:PhotoAPIOutbound 		= new PhotoAPIOutbound();
	public rate:Rate 					= new Rate();
	
	public recruitsIn:Array<[string,string]> 				= new Array<[string,string]>();
	public languagesSpoken:Array<[string,string]> 			= new Array<[string,string]>();
	public sectors:Array<[string,string]> 					= new Array<[string,string]>();
	public coreTech:Array<[string,string]>		 			= new Array<[string,string]>();
	public contractTypes:Array<[string,string]> 			= new Array<[string,string]>();

	/**
	* Sends request to update the Candidate
	*/
	public updateCandidate():void{
		
		let english:Language = new Language();
		english.language = "ENGLISH";
		english.level = this.candidateProfileFormBean.get('english')?.value;
		
		let dutch:Language = new Language();
		dutch.language = "DUTCH";
		dutch.level = this.candidateProfileFormBean.get('dutch')?.value;
		
		let french:Language = new Language();
		french.language = "FRENCH";
		french.level = this.candidateProfileFormBean.get('french')?.value;
		
		let languages:Array<Language> = new Array<Language>();
		languages.push(english);
		languages.push(dutch);
		languages.push(french);
		
		let rateCurrency:string 	= this.candidateProfileFormBean.get('rateCurrency')?.value;
		let ratePeriod:string 		= this.candidateProfileFormBean.get('ratePeriod')?.value;
		let rateValue:string 		= this.candidateProfileFormBean.get('rateValue')?.value;
		
		let rate:Rate = new Rate();
		rate.currency = rateCurrency;
		rate.period = ratePeriod;
		rate.value =  rateValue;
		
		if (rate.currency == '' || rate.period == '' || rate.value == '') {
			rate.currency = 'EUR';
			rate.period = 'HOUR';
			rate.value =  '0';
		}
		
		let updateRequest:UpdateCandidateProfileRequest = new UpdateCandidateProfileRequest();
		
		updateRequest.firstname 		= this.candidateProfileFormBean.get('firstname')?.value;;
		updateRequest.surname 			= this.candidateProfileFormBean.get('surname')?.value;;
		updateRequest.email 			= this.candidateProfileFormBean.get('email')?.value;;
		updateRequest.roleSought 		= this.candidateProfileFormBean.get('roleSought')?.value;;
		updateRequest.function 			= this.candidateProfileFormBean.get('function')?.value;;
		updateRequest.country 			= this.candidateProfileFormBean.get('country')?.value;;
		updateRequest.city 				= this.candidateProfileFormBean.get('city')?.value;;
		updateRequest.perm 				= this.candidateProfileFormBean.get('perm')?.value;;
		updateRequest.freelance 		= this.candidateProfileFormBean.get('freelance')?.value;;
		updateRequest.yearsExperience 	= this.candidateProfileFormBean.get('yearsExperience')?.value;;
		updateRequest.available 		= this.candidateProfileFormBean.get('available')?.value;
		updateRequest.languages 		= languages;
		updateRequest.rate				= rate;
		updateRequest.introduction		= this.candidateProfileFormBean.get('introduction')?.value;
		
		this.candidateService.updateCandidate(this.getOwnUserId(), updateRequest, this.imageFile).subscribe(res => {
			this.fetchCandidate();
			this.showViewMain();
		}, err => {
			console.log("Failed to update Candidate");
		});
		
	}
	
	/**
	* Whether a recruiter has already added their own profile
	*/
	public isOwnAccountExists():boolean{
		return this.candidateProfile != null;
	}
	
	/**
	* Switches to view to add own profile
	*/
	public showViewEdit():void{
		this.currentView = 'view-edit';
	}
	
	public showViewMain():void{
		this.currentView = 'view-main';
	}
	
	public getUserReadableVal(key:string):string{
		
		let firstChar:string = key.substring(0,1);
		let remainder:string = key.substring(1);
		
		remainder = remainder.toLocaleLowerCase();
		  
		
		return (firstChar + remainder);
	}

	
	/**
	* Uploads the file for the Profile Image and stored 
	* it ready to be sent to the backend
	*/
  	public uploadProfileImage(event:any):void{
  
	 	if (event.target.files.length <= 0) {
  			return;
  		}
  	
		this.imageFile = event.target.files[0];
		
		if (this.candidateProfile && this.candidateProfile!.photo){
			this.candidateProfile.photo = new PhotoAPIOutbound();	
  		}
  		
	}
	
	public contactCandidateView:string = 'message';
	
	/**
	*  Closes the confirm popup
	*/
	public closeModal(): void {
		this.profileDeletionView = 'home';
		this.modalService.dismissAll();
	}
	
	public profileDeletionView:string = 'home';
	
	/**
	* Deletes Candidates Profile
	*/
	confirmDeleteProfile():void{
		this.candidateService.deleteCandidate(this.candidateProfile.candidateId).subscribe(res => {
			
			sessionStorage.setItem('isRecruiterNoSubscription',		'');
			sessionStorage.setItem('beforeAuthPage', 				'');
			sessionStorage.setItem('isCandidate',     	 			'');
			sessionStorage.setItem('loggedIn',      				'');
	    	sessionStorage.setItem("userId", 						'');
			this.closeModal();
			this.router.navigate(['/']);
		}, err=> {
			this.profileDeletionView = 'failure';
		});
	}
	
	/**
	* Opend dialog to contact recuiter posting	
	*/
	public contactCandidate(contactBox:any):void{
		
		this.contactCandidateView = 'message';
		let options: NgbModalOptions = {
	    	 centered: true
	   };

		this.modalService.open(contactBox, options);
	
	}
	
	/**
	* Forms group for sending a message to a Recruiter relating to a
	* specific post on the jobboard 
	*/
	public sendMessageGroup:UntypedFormGroup = new UntypedFormGroup({
		message:				new UntypedFormControl(''),
		title:				new UntypedFormControl('')
	});
	
	/**
	* Opend dialog to contact recuiter posting	
	*/
	public sendMessageToCandidate():void{
		
		let emailRequest:EmailRequest = new EmailRequest();
		
		emailRequest.title = this.sendMessageGroup.get('title')?.value;;
		emailRequest.message = this.sendMessageGroup.get('message')?.value;;
		
		this.emailService.sendRecruiterContactEmail(emailRequest, this.selectedCandidateProfile.candidateId).subscribe(body => {
			this.contactCandidateView = 'success';
			this.sendMessageGroup = new UntypedFormGroup({
				message: new UntypedFormControl(''),
				title: new UntypedFormControl(''),
			});
		}, err => {
			this.contactCandidateView = 'failure';
		});
		
		
	}
	
	/**
	* Whether or not the User is a Candidate
	*/
	public isCandidate():boolean{
		return sessionStorage.getItem('isCandidate') === 'true';
	}

	/**
	* Returns the curren users id
	*/
	public getOwnUserId():string{
		return sessionStorage.getItem("userId")+'';
	}

}
