import { Component, OnInit, ViewChild } 				from '@angular/core';
import { UntypedFormGroup, UntypedFormControl }			from '@angular/forms';
import { CurriculumService }							from '../curriculum.service';
import { CandidateServiceService }						from '../candidate-service.service';
import { NgbModal}										from '@ng-bootstrap/ng-bootstrap';
import { PopupsService }								from '../popups.service';
import { NewPendingCandidate, Rate } 					from './new-pending-candidate';
import { TranslateService } from '@ngx-translate/core';

/**
* Component for Candidates to present their own Curriculum and Profile.
*/
@Component({
    selector: 'app-create-candidate',
    templateUrl: './create-candidate.component.html',
    styleUrls: ['./create-candidate.component.css','./create-candidate.component-mob.css'],
    standalone: false
})
export class CreateCandidateComponent implements OnInit {

	@ViewChild('feedbackBox', { static: false }) private feedbackDialog:any;

  	constructor(private curriculumService: 	CurriculumService, 
				private candidateService: 	CandidateServiceService,
				private popupsService: 		PopupsService, 
				private modalService: 		NgbModal,
				private translate: 			TranslateService) {
					
				 }

 	ngOnInit(): void {		
  		this.currencies.push("EUR");
		this.currencies.push("GBP");
		this.currencies.push("BGN");
		this.currencies.push("CAD");
		this.currencies.push("CHF");
		this.currencies.push("CZK");
		this.currencies.push("DKK");
		this.currencies.push("HUF");
		this.currencies.push("INR");
		this.currencies.push("PLN");
		this.currencies.push("PKR");
		this.currencies.push("RON");	
		this.currencies.push("RUB");	
		this.currencies.push("SEK");	
		this.currencies.push("TRY");	
		this.currencies.push("UAH");	
		this.currencies.push("USD");	
	}
	
	private imageFile!:File| any;
	
	public feedbackBoxClass:string            = '';
  	public feedbackBoxTitle                   = '';
  	public feedbackBoxText:string             = '';
	public currencies:Array<string> 		  = new Array<string>();

	public formBean:UntypedFormGroup = new UntypedFormGroup({
     
		surname:			new UntypedFormControl(''),
		firstname:			new UntypedFormControl(''),
		email:				new UntypedFormControl(''),
		contract:			new UntypedFormControl(''),
		perm:				new UntypedFormControl(''),
		rateCurrency:		new UntypedFormControl(''),
		ratePeriod:			new UntypedFormControl(''),
		rateValue:			new UntypedFormControl(''),
		introduction:		new UntypedFormControl(''),
		permCurrency:		new UntypedFormControl(''),
		permTimeUnit:		new UntypedFormControl(''),
		permFrom:			new UntypedFormControl(''),
		permTo:				new UntypedFormControl(''),
		contractCurrency:	new UntypedFormControl(''),
		contractTimeUnit:	new UntypedFormControl(''),
		contractFrom:		new UntypedFormControl(''),
		contractTo:			new UntypedFormControl(''),

	});
	
  	private curriculumFile!:File;
	
	/**
	* Uploads the file for the Curriculum and stored 
	* it ready to be sent to the backend
	*/
  	public uploadCurriculumFile(event:any):void{
  
  		if (event.target.files.length <= 0) {
  			return;
  		}
  	
  		this.curriculumFile = event.target.files[0];
  		
	}
	
	/**
	* If all required fields present enabled upload button
	*/
	public uploadButtonDisabled():boolean{
		
		if (this.curriculumFile == null) {
			return true;
		}
		
		if(this.formBean.get('surname')?.value == '') {
			return true;
		}
		
		if(this.formBean.get('firstname')?.value == '') {
			return true;
		}
		
		if(this.formBean.get('email')?.value == '') {
			return true;
		}
		
		return false;
		
	}
	
	/**
	* Resets Perm salary fields 
	*/
	private resetPermRateFields():void{
		this.formBean.get('permCurrency')?.setValue('');
		this.formBean.get('permTimeUnit')?.setValue('');
		this.formBean.get('permFrom')?.setValue('');
		this.formBean.get('permTo')?.setValue('');
		this.permRoleClass = "hide-row";
	}

	/**
	* Resets Perm salary fields 
	*/
	private resetContractRateFields():void{
		this.formBean.get('contractCurrency')?.setValue('');
		this.formBean.get('contractTimeUnit')?.setValue('');
		this.formBean.get('contractFrom')?.setValue('');
		this.formBean.get('contractTo')?.setValue('');
		this.contractRoleClass = "hide-row";
	}
		
	/**
	* Resets the form bean fields 
	*/
	private resetFormBean():void{
		this.formBean.get('surname')?.setValue('');
		this.formBean.get('firstname')?.setValue('');
		this.formBean.get('email')?.setValue('');
		this.formBean.get('contract')?.setValue('');
		this.formBean.get('perm')?.setValue('');
		this.formBean.get('rateCurrency')?.setValue('');
		this.formBean.get('ratePeriod')?.setValue('');
		this.formBean.get('rateValue')?.setValue('');
		this.formBean.get('introduction')?.setValue('');
		
		this.resetContractRateFields();
		this.resetPermRateFields();
	}
	
	/**
	* Sends the Curriculum to the backend. If the curriculum is added successfully
	* the candidate details are then sent along with the Id returned from the Curriculun
	* so the Curriculum and Candidate can be associated with one another
	*/
	public addCandidate():void{
		
		if (this.curriculumFile == null) {
			return;
		}
		
		let surname:string 			= this.formBean.get('surname')?.value;
		let firstname:string 		= this.formBean.get('firstname')?.value;
		let email:string 			= this.formBean.get('email')?.value;
		let contract:boolean 		= this.formBean.get('contract')?.value;
		let perm:boolean 			= this.formBean.get('perm')?.value;
		let introduction:string		= this.formBean.get('introduction')?.value;
		let permCurrency:string 	= this.formBean.get('permCurrency')?.value;
		let permTimeUnit:string 	= this.formBean.get('permTimeUnit')?.value;
		let permFrom:string 		= this.formBean.get('permFrom')?.value;
		let permTo:string 			= this.formBean.get('permTo')?.value;
		let contractCurrency:string = this.formBean.get('contractCurrency')?.value;
		let contractTimeUnit:string = this.formBean.get('contractTimeUnit')?.value;
		let contractFrom:string 	= this.formBean.get('contractFrom')?.value;
		let contractTo:string 		= this.formBean.get('contractTo')?.value;
			
		this.curriculumService.uploadPendingCurriculum(this.curriculumFile).subscribe(pendingCandidateId =>{
			
			const pc:NewPendingCandidate = new NewPendingCandidate();
			
			pc.pendingCandidateId 	= pendingCandidateId;
			pc.firstname			= firstname;
			pc.surname 				= surname;
			pc.email 				= email;
			pc.introduction 		= introduction;
			pc.freelance			= contract;
			pc.perm					= perm;
			
			if (permCurrency.length > 0 && permTimeUnit.length > 0){
				let permRate:Rate = new Rate();	
				permRate.currency = permCurrency;
				permRate.period = permTimeUnit;
				if (typeof permFrom === 'number' ){
					permRate.valueMin = permFrom;
				}
				if (typeof permTo === 'number' ){
					permRate.valueMax = permTo;
				}
				pc.ratePerm = permRate;
			}
			if (contractCurrency.length > 0 && contractTimeUnit.length > 0){
				let contractRate:Rate = new Rate();	
				contractRate.currency = contractCurrency;
				contractRate.period = contractTimeUnit;
				if (typeof contractFrom === 'number' ){
					contractRate.valueMin = contractFrom;
				}
				if (typeof contractTo === 'number' ){
					contractRate.valueMax = contractTo;
				}
				pc.rateContract = contractRate;
			}			
			this.candidateService.addPendingCandidate(pc, this.imageFile).subscribe(data => {
				this.resetFormBean();
				this.open("Success",  true);				
			}, 
		err => {
			
			if (err.status == 406) {
				let exceptions:Array<string> = new Array<string>();
				exceptions.push(err.error);
				this.popupsService.setValidationErrors(exceptions)
			} else {
				let exceptions:Array<string> = new Array<string>();
				exceptions.push(this.translate.instant('create-candidate--profile-image'));
				this.popupsService.setValidationErrors(exceptions);	
			}
			
		});
			
    	}, 
		err => {
			let exceptions:Array<string> = new Array<string>();
			exceptions.push(this.translate.instant('create-candidate-cv-format'));
			this.popupsService.setValidationErrors(exceptions)
		});
		
	}
	
	/**
	* Opens the specified Dialog box
	*/
	public open(msg:string, success:boolean):void {
    
	    if (success) {
	      this.feedbackBoxTitle 	= 'Success';
	      this.feedbackBoxText 		= this.translate.instant('create-candidate-success');
	      this.feedbackBoxClass 	= 'feedback-success';
	    } else {
	      this.feedbackBoxTitle 	= 'Failure';
	      this.feedbackBoxText 		= this.translate.instant('create-candidate-failure');
	      this.feedbackBoxClass 	= 'feedback-failure';
	    }
	
		this.feedbackDialog.nativeElement.showModal();
	
  }

  /**
  *  Closes the confirm popup
  */
  public closeModal(): void {
    this.modalService.dismissAll();
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
		
	}

	public permRoleClass = "hide-row";
  	public permOnChange(newValue:any):void{
		if (newValue == true) {
			this.permRoleClass = "show-row";
		} else {
			this.resetPermRateFields();
		}
	}
	
	public contractRoleClass = "hide-row";
  	public contractOnChange(newValue:any):void{
		if (newValue == true) {
			this.contractRoleClass = "show-row";
		} else {
			this.resetContractRateFields();
		}
	}
	
	public updateContract():void{
		this.contractOnChange(!this.formBean.get('contract')?.value);
	}
	
	public updatePerm():void{
		this.permOnChange(!this.formBean.get('perm')?.value);
	}
	
	/**
	* Whether or not the user has authenticated with the System 
	*/
	public isAuthenticated():boolean {
		return sessionStorage.getItem('loggedIn') === 'true';
	}
	
}
