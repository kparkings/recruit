import { Component, OnInit, ViewChild } 				from '@angular/core';
import { UntypedFormGroup, UntypedFormControl }			from '@angular/forms';
import { CurriculumService }							from '../curriculum.service';
import { CandidateServiceService }						from '../candidate-service.service';
import { NgbModal, NgbModalOptions}						from '@ng-bootstrap/ng-bootstrap';
import { PopupsService }								from '../popups.service';

/**
* Component for Candidates to present their own Curriculum and Profile.
*/
@Component({
  selector: 'app-create-candidate',
  templateUrl: './create-candidate.component.html',
  styleUrls: ['./create-candidate.component.css']
})
export class CreateCandidateComponent implements OnInit {

	@ViewChild('feedbackBox', { static: false }) private content:any;

  	constructor(private curriculumService: CurriculumService, 
				private candidateService: CandidateServiceService,
				private popupsService: PopupsService, 
				private modalService: NgbModal) {
					
				 }

 	ngOnInit(): void {
  	}
	
	private imageFile!:File| any;
	
	public feedbackBoxClass:string            = '';
  	public feedbackBoxTitle                   = '';
  	public feedbackBoxText:string             = '';

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
	* Sends the Curriculum to the backend. If the curriculum is added successfully
	* the candidate details are then sent along with the Id returned from the Curriculun
	* so the Curriculum and Candidate can be associated with one another
	*/
	public addCandidate():void{
		
		if (this.curriculumFile == null) {
			return;
		}
		
		let surname:string 		= this.formBean.get('surname')?.value;
		let firstname:string 	= this.formBean.get('firstname')?.value;
		let email:string 		= this.formBean.get('email')?.value;
		let contract:boolean 	= this.formBean.get('contract')?.value;
		let perm:boolean 		= this.formBean.get('perm')?.value;
		let rateCurrency:string	= this.formBean.get('rateCurrency')?.value;
		let ratePeriod:string 	= this.formBean.get('ratePeriod')?.value;
		let rateValue:string	= this.formBean.get('rateValue')?.value;
		let introduction:string	= this.formBean.get('introduction')?.value;
			
		this.curriculumService.uploadPendingCurriculum(this.curriculumFile).subscribe(pendingCandidateId =>{
      		
			this.candidateService.addPendingCandidate(pendingCandidateId, firstname, surname, email, contract, perm, rateCurrency, ratePeriod, rateValue, introduction, this.imageFile).subscribe(data => {
				
				this.formBean.get('surname')?.setValue('');
				this.formBean.get('firstname')?.setValue('');
				this.formBean.get('email')?.setValue('');
				this.formBean.get('contract')?.setValue('');
				this.formBean.get('perm')?.setValue('');
			
				this.formBean.get('rateCurrency')?.setValue('');
				this.formBean.get('ratePeriod')?.setValue('');
				this.formBean.get('rateValue')?.setValue('');
				this.formBean.get('introduction')?.setValue('');
			
				this.open('feedbackBox', "Success",  true);
				
			}, 
		err => {
			
			if (err.status == 406) {
				let exceptions:Array<string> = new Array<string>();
				exceptions.push(err.error);
				this.popupsService.setValidationErrors(exceptions)
			} else {
				let exceptions:Array<string> = new Array<string>();
				exceptions.push("There was an issue with the profile image");
				this.popupsService.setValidationErrors(exceptions);	
			}
			
			
			
		});
			
    	}, 
		err => {
			let exceptions:Array<string> = new Array<string>();
			exceptions.push("CV must be Word or PDF and less than 1MB");
			this.popupsService.setValidationErrors(exceptions)
		});
		
	}
	
	/**
	* Opens the specified Dialog box
	*/
	public open(content:any, msg:string, success:boolean):void {
    
	    if (success) {
	      this.feedbackBoxTitle = 'Success';
	      this.feedbackBoxText = 'Your details have been successfully uploaded. They will now be reviewed and added to the system';
	      this.feedbackBoxClass = 'feedback-success';
	    } else {
	      this.feedbackBoxTitle = 'Failure';
	      this.feedbackBoxText = 'Unable to add Candidate';
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

}
