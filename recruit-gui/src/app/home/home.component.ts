import { Component, OnInit } 					from '@angular/core';
import { StatisticsService } 					from '../statistics.service';
import { CandidateServiceService } 				from '../candidate-service.service';
import { CandidateTotals } 						from '../candidate-totals';
import { Chart } 								from 'chart.js';

@Component({
    selector: 'app-home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.css'],
    standalone: false
})
export class HomeComponent implements OnInit {

	public candidateTotals:CandidateTotals					 	= new CandidateTotals(0,0,0);
	public leftChart: any;
	private availabilityChartData 								= [{label: "",data: [''],backgroundColor: ''}];
	public availabilityChartLabels 						= [''];
	public chartColor:string = 'grey';
	public statChartDivMobile:string 							= '';
		
	/**	
	* Constructor
	* @param statisticsService - provides statistics about the system
	*/
  	constructor(public candidateService:		CandidateServiceService,
				public statisticsService:StatisticsService
	) {
	
			this.candidateService.fetchCandidateTotals().subscribe(totals => this.candidateTotals = totals);
	
  	}

  ngOnInit(): void {
	this.refreshStats();
  }

  createLeftChart(leftChartLabels:string[], leftChartData:any){
  		
    		if (this.leftChart) {
    			this.leftChart.destroy();
    		}
    		
  	    this.leftChart = new Chart("leftChart", {
  	      type: 'bar',
  	
  	      data: {
  	        labels: leftChartLabels, 
  		       datasets: leftChartData
  	      },
  	      options: {
  	        aspectRatio:1.75
  	      }
  	      
  	    });
  	   
    }
	
	private refreshStats() {
			
		this.statisticsService.getAvailableCandidatesByFunctionStatistics().forEach(data => {
				
					let stats:any[] = data;
					
					let functionStatCount:Array<string> 			= new Array<string>(); 
					let unavailableFunctionStatCount:Array<string> 	= new Array<string>(); 
					let functionStatName:Array<string> 				= new Array<string>(); 
						
					stats.forEach(functonStat => {
						
						functionStatName.push(functonStat.function);
						functionStatCount.push(functonStat.availableCandidates);
						unavailableFunctionStatCount.push(functonStat.unavailableCandidates);
						
					});
					
					this.availabilityChartData = [
			           	{label: "Candidates available by function", data: functionStatCount, backgroundColor: this.chartColor},
						{label: "Candidates unavailable by function", data: unavailableFunctionStatCount, backgroundColor:"purple"}
			        	];
					this.availabilityChartLabels = functionStatName; 
					
					this.createLeftChart(this.availabilityChartLabels, this.availabilityChartData);
					
		    	});
					
		}
		
}
