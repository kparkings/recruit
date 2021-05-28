import { Component, OnInit } 					from '@angular/core';
import { CandidateServiceService }				from '../candidate-service.service';
import { StatisticsService } 					from '../statistics.service';
import { ChartDataSets, ChartOptions, ChartType } 			from 'chart.js';
import { Color, Label } 						from 'ng2-charts';

@Component({
  selector: 'app-statistics',
  templateUrl: './statistics.component.html',
  styleUrls: ['./statistics.component.css']
})
export class StatisticsComponent implements OnInit {

	/**
  	* Constructor
	*/
	constructor(public statisticsService:StatisticsService) {

		this.statisticsService.getCurriculumDownloadStatistics().forEach(stat => {

			console.log(stat.dailyDownloads);
			console.log(Object.keys(stat.dailyDownloads));
			console.log(Object.values(stat.dailyDownloads));
			
			let downloads:number[] 		= Object.values(stat.dailyDownloads);
			let downloadDates:string[] 	= Object.keys(stat.dailyDownloads);
			
			this.lineChartData = [{ data: downloads, label: 'Downloads' },];
			this.lineChartLabels = downloadDates;

    	});

	}

	ngOnInit(): void {
	}


	lineChartData: 		ChartDataSets[] 	= [];// 	= [{ data: [85, 72, 78, 75, 77, 75], label: 'Downloads' },];
	lineChartLabels: 	Label[] 			= [];

  	lineChartOptions = {
    	responsive: true,
  	};

  	lineChartColors: Color[] = [
    	{
      		borderColor: 'black',
      		backgroundColor: 'rgba(0,0,0,0.28)',
    	},
  	];

  	lineChartLegend = true;
  	lineChartPlugins = [];
  	lineChartType:ChartType = 'line';
 
  
}
