import { Component, OnInit } 							from '@angular/core';
import { FormGroup, FormControl }						from '@angular/forms';
import { CurriculumService }							from '../curriculum.service';

/**
* Component for Candidates to present their own Curriculum and Profile.
*/
@Component({
  selector: 'app-create-candidate',
  templateUrl: './create-candidate.component.html',
  styleUrls: ['./create-candidate.component.css']
})
export class CreateCandidateComponent implements OnInit {

  	constructor(private curriculumService: CurriculumService) { }

 	ngOnInit(): void {
  	}
	
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
		
		this.curriculumService.uploadPendingCurriculum(this.curriculumFile).subscribe(data=>{
      		this.formBean.get('surname')?.setValue('');
			this.formBean.get('firstname')?.setValue('');
			this.formBean.get('email')?.setValue('');
			this.formBean.get('contract')?.setValue('');
			this.formBean.get('perm')?.setValue('');
			
			console.log(JSON.stringify(data));
			
		//	//upload candidate now with Id
			
    	});
		
	}

}
