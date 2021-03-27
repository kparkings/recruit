import { Component, OnInit }                              from '@angular/core';
import { ReactiveFormsModule, FormGroup, FormControl }    from '@angular/forms';
import { CandidateServiceService }                        from '../candidate-service.service';
import { CandidateFunction }                              from '../candidate-function';

@Component({
  selector: 'app-new-candidate',
  templateUrl: './new-candidate.component.html',
  styleUrls: ['./new-candidate.component.css']
})
export class NewCandidateComponent implements OnInit {

   public functionTypes: Array<CandidateFunction> = new Array<CandidateFunction>();
   
   public formBean:FormGroup = new FormGroup({
     
    candidateId:      new FormControl("C1001"),
    firstname:        new FormControl(),
    surname:          new FormControl(),
    email:            new FormControl(),
    country:          new FormControl(),
    city:             new FormControl(),
    permanent:        new FormControl(),
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
  constructor(private candidateService: CandidateServiceService) {
    
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
      console.log("Sent");
    });
  };

}
