import { Component, OnInit }							from '@angular/core';
import { UntypedFormGroup, UntypedFormControl }						from '@angular/forms';
import { CandidateServiceService }						from '../candidate-service.service';
import { CurriculumService }							from '../curriculum.service';
import { CandidateFunction }							from '../candidate-function';
import { NgbModal, NgbModalOptions}						from '@ng-bootstrap/ng-bootstrap'
import { ViewChild }									from '@angular/core';
import { Router}										from '@angular/router';
import { PendingCandidate }								from './pending-candidate';
import { environment }									from '../../environments/environment';

@Component({
  selector: 'app-new-candidate',
  templateUrl: './new-candidate.component.html',
  styleUrls: ['./new-candidate.component.css']
})
export class NewCandidateComponent implements OnInit {

@ViewChild('feedbackBox', { static: false }) private content:any;
@ViewChild('pendingCandidateBox', { static: false }) private pendingCandidateBoxChild:any;

    public functionTypes:	 	Array<CandidateFunction> 	= new Array<CandidateFunction>();
	public pendingCandidates: 	Array<PendingCandidate> 	= new Array<PendingCandidate>();
   
  	public selectOptionLangDutch:string = '';
  	public selectOptionLangEnglish:string = '';
  	public selectOptionLangFrench:string = '';

	public formBean:UntypedFormGroup = new UntypedFormGroup({
     
		candidateId:		new UntypedFormControl(''),
       	firstname:			new UntypedFormControl(),
       	surname:			new UntypedFormControl(),
       	email:				new UntypedFormControl(),
       	roleSought:			new UntypedFormControl(),
       	country:			new UntypedFormControl(),
       	city:				new UntypedFormControl(),
       	perm:				new UntypedFormControl(),
       	freelance:			new UntypedFormControl(),
       	dutch:				new UntypedFormControl(),
       	english:			new UntypedFormControl(),
       	french:				new UntypedFormControl(),
       	function:			new UntypedFormControl(),
       	yearsExperience:	new UntypedFormControl(),
       	skills:				new UntypedFormControl()

  	 });

	/**
  	* Constructor
  	*/
  	constructor(private curriculumService: CurriculumService , private candidateService: CandidateServiceService , private modalService: NgbModal, private router: Router) {
    
    	this.candidateService.loadFunctionTypes().forEach(funcType => {
      		this.functionTypes.push(funcType);
    	});

		this.candidateService.fetchPendingCandidates().forEach(data => {
			
			let pendingCandidates: Array<PendingCandidate> = data;
			
			pendingCandidates.forEach(pc => {
                    this.pendingCandidates.push(pc);
            });
			
			if (this.isPendingCandidates()) {
				this.openPendingCandidatesBox();
			}
		});

  	}

  	/**
  	*  Init 
  	*/
  	ngOnInit(): void {

  	}

	/**
	* Returns whether or not there are pending candidayes 
	* waiting to be processed 
	*/
	public isPendingCandidates():boolean{
		return this.pendingCandidates.length > 0;
	}
	
	public openPendingCandidatesBox():void {
    
      	let options: NgbModalOptions = {
     		centered: true
   		};

  		this.modalService.open(this.pendingCandidateBoxChild, options);
  	}
	

	/**
  	* Registers a new Candidate with the backend
  	*/
  	public addCandidate(): void {
    	this.candidateService.addCandidate(this.formBean).subscribe(d=>{
    	  this.open('feedbackBox', "Success",  true)
    	});
  	};

  	public feedbackBoxClass:string            = '';
  	public feedbackBoxTitle                   = '';
  	public feedbackBoxText:string             = '';

	public open(content:any, msg:string, success:boolean):void {
    
    	if (success) {
    	  this.feedbackBoxTitle = 'Success';
    	  this.feedbackBoxText = 'Candidate Added';
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
    this.router.navigate(['view-candidates']);
  }

  /**
  *  Closes the confirm popup
  */
  public closePendingCandidateModal(): void {
    this.modalService.dismissAll();
  }
  
  private curriculumFile!:File;
  
  public uploadCurriculumFile(event:any):void{
  
  		if (event.target.files.length <= 0) {
  			return;
  		}
  	
  		this.curriculumFile = event.target.files[0];
  
  		this.curriculumService.uploadCurriculum(this.curriculumFile).subscribe(data=>{
      		this.formBean.get('candidateId')?.setValue(data.id);
			this.formBean.get('email')?.setValue(data.emailAddress);
			this.formBean.get('skills')?.setValue(data.skills.join(", "));
    	});
  		
  }
   
  public isSelected(language:string):void{
      
      switch(language) {
          case 'dutch':{
           //   this.selectOptionLangDutch = this.formBean.get('dutch')!.value;
             return;
          } 
          case 'english':{
              return;
          } 
      }
      
  }
  
  /**
  * Updates Language options with best guesses based upon the country 
  * selected for the Candidate 
  */
  public updateCountry():void {
      
      let country:string = this.formBean.get('country') === null ? "" : this.formBean.get('country')!.value;
      
      switch(country) {
          case 'UK':{
              this.formBean.get('dutch')!.setValue('NO');
              this.formBean.get('english')!.setValue('YES');
              this.formBean.get('french')!.setValue('NO');
              break;
          }
		case 'REPUBLIC_OF_IRELAND':{
              this.formBean.get('dutch')!.setValue('NO');
              this.formBean.get('english')!.setValue('YES');
              this.formBean.get('french')!.setValue('NO');
              break;
          }  
          case 'NETHERLANDS':{
              this.formBean.get('dutch')!.setValue('NO');
              this.formBean.get('english')!.setValue('YES');
              this.formBean.get('french')!.setValue('NO');
              break;
          } 
          case 'BELGIUM':{
              this.formBean.get('dutch')!.setValue('NO');
              this.formBean.get('english')!.setValue('NO');
              this.formBean.get('french')!.setValue('NO');
              break;
          } 
      }
      
  }
	
	/**
	* Loads a PendingCandidates details intothe New candidate screen
	*/
	public loadPendingCandidate(pendingCandidate:PendingCandidate):void{
		
		this.curriculumService.makePendingCurriculumActive(pendingCandidate.pendingCandidateId).subscribe(candidate=>{
      		this.formBean.get('candidateId')?.setValue(candidate.id);
			this.formBean.get('firstname')?.setValue(pendingCandidate.firstname);
			this.formBean.get('surname')?.setValue(pendingCandidate.surname);
			this.formBean.get('email')?.setValue(pendingCandidate.email);
			this.formBean.get('skills')?.setValue(candidate.skills.join(", "));
			
			if(pendingCandidate.perm) {
				this.formBean.get('perm')?.setValue("TRUE");
			} else {
				this.formBean.get('perm')?.setValue("FALSE");
			}
			
			if(pendingCandidate.freelance) {
				this.formBean.get('freelance')?.setValue("TRUE");
			} else {
				this.formBean.get('freelance')?.setValue("FALSE");
			}
			this.closePendingCandidateModal();
    	});
		
	}
	
	/**
	* Returns URL to download pening curriculum
	*/
	public getPendingCurriculumDownloadUrl(pendingCandidateId:string):string{
		return  environment.backendUrl + 'pending-curriculum/'+ pendingCandidateId;
	}

}
