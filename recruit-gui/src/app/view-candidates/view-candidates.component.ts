import { Component, OnInit }                              from '@angular/core';
import { CandidateServiceService }                        from '../candidate-service.service';
import { Candidate }                                      from './candidate';
import { CandidateFunction }                            from '../candidate-function';

@Component({
  selector: 'app-view-candidates',
  templateUrl: './view-candidates.component.html',
  styleUrls: ['./view-candidates.component.css']
})
export class ViewCandidatesComponent implements OnInit {

  public functionTypes: Array<CandidateFunction> = new Array<CandidateFunction>();

  candidates: Array<Candidate> = new Array<Candidate>();

  constructor(private candidateService:CandidateServiceService) {

    this.candidateService.loadFunctionTypes().forEach(funcType => {
      this.functionTypes.push(funcType);
    });

   }

  ngOnInit(): void {

    this.candidateService.getCandidates().subscribe( data => {
    
      data.forEach((c:Candidate) => {
        
        const candidate:Candidate = new Candidate();

      candidate.candidateId       = c.candidateId;
      candidate.city              = c.city;
      candidate.country           = this.getCountryCode(c.country);
      candidate.freelance         = c.freelance;
      candidate.function          = c.function;
      candidate.permanent         = c.permanent;
      candidate.yearsExperience   = c.yearsExperience;
      candidate.languages = c.languages;
      candidate.skills = c.skills;
      

      this.candidates.push(candidate);

      
      console.log("ADDED CANDIDATE " + JSON.stringify(c));

      });
        
    })
    
  }

  getCountryCode(country:string):string{

    switch(country){
      case "NETHERLANDS":{
        return "NL";
      }
      case "BELGIUM":{
        return "BE";
      }
      case "UK":{
        return "UK";
      }
    }

     return 'NA';

  }

  /**
  * Returns the humand readable decription of a function 
  * based upon its id
  * @param id - id of the function to return the desc for 
  */
  public pretifyFunction(id: string): string {

    return this.functionTypes.filter(ft => ft.id === id)[0].desc;
    
  }

}
