import { Component } 										from '@angular/core';
import { StatisticsService } 								from '../statistics.service';
import { Chart } 											from 'chart.js';

@Component({
  selector: 'app-candidate-staistics',
  standalone: false,
  templateUrl: './candidate-statistics.component.html',
  styleUrl: './candidate-statistics.component.css'
})
export class CandidateStatisticsComponent {

	public chartColor:string 						= 'grey';
	public leftChart: any;
	public profileViewChartLabels 					= [''];
	private profileViews:string[] 					= new Array<string>();
	private viewsChartData 							= [{label: "",data: [''], backgroundColor: ''}];
	
	/**
	* Constructor
	* @param statisticsService - for retrieving stats from backend 
	*/
	constructor(public statisticsService:StatisticsService) {
		this.refreshStats();
	}
	
	/**
	* Retrieves data to show statistics 
	*/
	public refreshStats():void {
		
		let candidateId:string = ""+sessionStorage.getItem("userId");
		
		this.statisticsService.getProfileViewsStatisticsForCandidate(candidateId).subscribe(stats => {
			let labels:Array<string> 			= new Array<string>();
			let data:Array<string> 				= new Array<string>();
			stats.forEach(stat => {
				labels.push(stat.bucketId);
				data.push(""+stat.count);
			});
			this.profileViewChartLabels 		= labels;
			this.profileViews 					= data;
			this.viewsChartData = [{label: "Candidate Profile Views", data: this.profileViews, backgroundColor: this.chartColor}];
			
			this.createLeftChart(this.profileViewChartLabels, this.viewsChartData);
		});
		
	}
	
	
	/**
	* Creates a chart to show statistics
	*/
	createLeftChart(leftChartLabels:string[], leftChartData:any){
			
	  		if (this.leftChart) {
	  			this.leftChart.destroy();
	  		}
	  		
		    this.leftChart = new Chart("leftChart", {
		      type: 'line',
		
		      data: {
		        labels: leftChartLabels, 
			       datasets: leftChartData
		      },
		      options: {
		        aspectRatio:2.5
		      }
		      
		    });
		   
	  	}
}
