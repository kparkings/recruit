import { Component, OnInit } 					from '@angular/core';
import { StatisticsService } 					from '../statistics.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

	totalNumberActiveCandidates:number = 0;

	/**	
	* Constructor
	* @param statisticsService - provides statistics about the system
	*/
  	constructor(public statisticsService:StatisticsService) {
	
		this.statisticsService.getTotalNumberOfActiceCandidatesStatistics().forEach(count => {

			this.totalNumberActiveCandidates = count;

    	});
	
  	}

  ngOnInit(): void {
  }

}
