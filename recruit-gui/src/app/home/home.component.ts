import { Component, OnInit } 					from '@angular/core';
import { CandidateServiceService } from '../candidate-service.service';
import { CandidateTotals } from '../candidate-totals';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

	public candidateTotals:CandidateTotals					 	= new CandidateTotals(0,0,0);

	/**	
	* Constructor
	* @param statisticsService - provides statistics about the system
	*/
  	constructor(public candidateService:		CandidateServiceService) {
	
			this.candidateService.fetchCandidateTotals().subscribe(totals => this.candidateTotals = totals);
	
  	}

  ngOnInit(): void {
  }

}
