import { Component, OnInit }                                                                   from '@angular/core';
import { ReactiveFormsModule, FormGroup, FormControl }                   from '@angular/forms';
import { CandidateServiceService }                                                          from '../candidate-service.service';
import { CurriculumService }                                                                     from '../curriculum.service';
import { CandidateFunction }                                                                    from '../candidate-function';
import {NgbModal, NgbModalOptions, ModalDismissReasons}            from '@ng-bootstrap/ng-bootstrap';
import {TemplateRef, ViewChild,ElementRef, AfterViewInit  }                 from '@angular/core';
import { Router}                                                                                         from '@angular/router';

@Component({
  selector: 'app-new-candidate',
  templateUrl: './new-candidate.component.html',
  styleUrls: ['./new-candidate.component.css']
})
export class NewCandidateComponent implements OnInit {

@ViewChild('feedbackBox', { static: false }) private content:any;

   public functionTypes: Array<CandidateFunction> = new Array<CandidateFunction>();
   
   public formBean:FormGroup = new FormGroup({
     
       candidateId:                    new FormControl(''),
       firstname:                        new FormControl(),
       surname:                         new FormControl(),
       email:                              new FormControl(),
       roleSought:                     new FormControl(),
       country:                           new FormControl(),
       city:                                 new FormControl(),
       perm:                              new FormControl(),
       freelance:                       new FormControl(),
       dutch:                             new FormControl(),
       english:                           new FormControl(),
       french:                            new FormControl(),
       function:                         new FormControl(),
       yearsExperience:            new FormControl(),
       skills:                               new FormControl()

  });

  /**
  * Constructor
  */
  constructor(private curriculumService: CurriculumService , private candidateService: CandidateServiceService , private modalService: NgbModal, private router: Router) {
    
    this.candidateService.loadFunctionTypes().forEach(funcType => {
      this.functionTypes.push(funcType);
    });

  }

  /**
  *  Init 
  */
  ngOnInit(): void {

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
  public feedbackBoxTitle                       = '';
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
  
  private curriculumFile!:File;
  
  public uploadCurriculumFile(event:any):void{
  
  		if (event.target.files.length <= 0) {
  			return;
  		}
  	
  		this.curriculumFile = event.target.files[0];
  
  		console.log("file = " + this.curriculumFile);
  		
  		this.curriculumService.uploadCurriculum(this.curriculumFile).subscribe(data=>{
      		this.formBean.get('candidateId')?.setValue(data.id);
    	});
  		
  }
  
  public selectOptionLangDutch:string = '';
  public selectOptionLangEnglish:string = '';
  public selectOptionLangFrench:string = '';
 
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
}
