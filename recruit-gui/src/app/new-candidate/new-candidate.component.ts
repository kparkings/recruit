import { Component, OnInit }                              	from '@angular/core';
import { ReactiveFormsModule, FormGroup, FormControl }    	from '@angular/forms';
import { CandidateServiceService }                        	from '../candidate-service.service';
import { CandidateFunction }                              	from '../candidate-function';
import {NgbModal, NgbModalOptions, ModalDismissReasons}   	from '@ng-bootstrap/ng-bootstrap';
import {TemplateRef, ViewChild,ElementRef, AfterViewInit  } from '@angular/core';
import { Router} 											from '@angular/router';

@Component({
  selector: 'app-new-candidate',
  templateUrl: './new-candidate.component.html',
  styleUrls: ['./new-candidate.component.css']
})
export class NewCandidateComponent implements OnInit {

@ViewChild('feedbackBox', { static: false }) private content:any;

   public functionTypes: Array<CandidateFunction> = new Array<CandidateFunction>();
   
   public formBean:FormGroup = new FormGroup({
     
    candidateId:      new FormControl(''),
    firstname:        new FormControl(),
    surname:          new FormControl(),
    email:            new FormControl(),
    roleSought:       new FormControl(),
    country:          new FormControl(),
    city:             new FormControl(),
    perm:             new FormControl(),
    freelance:        new FormControl(),
    dutch:            new FormControl(),
    english:          new FormControl(),
    french:           new FormControl(),
    function:         new FormControl(),
    yearsExperience:  new FormControl(),
    skills:           new FormControl()

  });

  /**
  * Constructor
  */
  constructor(private candidateService: CandidateServiceService , private modalService: NgbModal, private router: Router) {
    
    this.candidateService.loadFunctionTypes().forEach(funcType => {
      this.functionTypes.push(funcType);
    });

  }

  /**
  *  Init 
  */
  ngOnInit(): void {

  //satrt
    this.candidateService.authenticate().subscribe( data => {
      console.log("Done");
    });
    //end

  }

  /**
  * Registers a new Candidate with the backend
  */
  public addCandidate(): void {
    //TODO: Convert Formbean to object before passing to Service
    this.candidateService.addCandidate(this.formBean).subscribe(d=>{
      this.open('feedbackBox', "Success",  true)
    });
  };

  public feedbackBoxClass:string = '';
  public feedbackBoxTitle = '';
  public feedbackBoxText:string = '';


//this.open('feedbackBox', "Success",  true);

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

}
