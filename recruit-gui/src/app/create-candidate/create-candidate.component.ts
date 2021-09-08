import { Component, OnInit } 							from '@angular/core';
import { FormGroup, FormControl }						from '@angular/forms';
import { CurriculumService }							from '../curriculum.service';
import { CandidateServiceService }						from '../candidate-service.service';
import { NgbModal, NgbModalOptions}						from '@ng-bootstrap/ng-bootstrap';
import { ViewChild }									from '@angular/core';

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

  	constructor(private curriculumService: CurriculumService, private candidateService: CandidateServiceService, private modalService: NgbModal) { }

 	ngOnInit(): void {
  	}
	
	public feedbackBoxClass:string            = '';
  	public feedbackBoxTitle                   = '';
  	public feedbackBoxText:string             = '';

	public formBean:FormGroup = new FormGroup({
     
		surname:		new FormControl(''),
		firstname:		new FormControl(''),
		email:			new FormControl(''),
		contract:		new FormControl(''),
		perm:			new FormControl(''),
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
			
		this.curriculumService.uploadPendingCurriculum(this.curriculumFile).subscribe(pendingCandidateId =>{
      		
			this.candidateService.addPendingCandidate(pendingCandidateId, firstname, surname, email, contract, perm).subscribe(data => {
				
				this.formBean.get('surname')?.setValue('');
				this.formBean.get('firstname')?.setValue('');
				this.formBean.get('email')?.setValue('');
				this.formBean.get('contract')?.setValue('');
				this.formBean.get('perm')?.setValue('');
			
				this.open('feedbackBox', "Success",  true);
				
			});
			
    	});
		
	}
	
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

}
