import { Component, OnInit }                              from '@angular/core';
import { CandidateServiceService }                        from '../candidate-service.service';
import { Candidate }                       from './candidate';
@Component({
  selector: 'app-view-candidates',
  templateUrl: './view-candidates.component.html',
  styleUrls: ['./view-candidates.component.css']
})
export class ViewCandidatesComponent implements OnInit {

  candidates:Array<Candidate> = new Array<Candidate>();

  constructor(private candidateService:CandidateServiceService) { }

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

}
