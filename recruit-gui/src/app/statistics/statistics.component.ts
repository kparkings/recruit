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

			let downloads:number[] 		= Object.values(stat.dailyDownloads);
			let downloadDates:string[] 	= Object.keys(stat.dailyDownloads);
			
			this.lineChartData = [{ data: downloads, label: 'Downloads' },];
			this.lineChartLabels = downloadDates;
			
			let recruiterDownloads:number[] 		= Object.values(stat.recruiterDownloads);
			let recruiterDownloadNames:string[] 	= Object.keys(stat.recruiterDownloads);
			
			this.recruiterDownloadsChartData = [{ data: recruiterDownloads, label: 'Downloads' },];
			this.recruiterDownloadsChartLabels = recruiterDownloadNames;

    	});
//getCurriculumDownloadStatistics

	}

	ngOnInit(): void {
	}

	/**
	* recruiter Downloads
	*/
	recruiterDownloadsChartData: 		ChartDataSets[] 	= [];
	recruiterDownloadsChartLabels: 		Label[] 			= [];

  	recruiterDownloadsChartOptions = {
    	responsive: true,
  	};

  	recruiterDownloadsChartColors: Color[] = [
    	{
      		borderColor: 'black',
      		backgroundColor: 'rgba(0,0,0,0.28)',
    	},
  	];

  	recruiterDownloadsChartLegend = true;
  	recruiterDownloadsChartPlugins = [];
  	recruiterDownloadsChartType:ChartType = 'bar';
 

	/**
	* Daily Downloads
	*/
	lineChartData: 		ChartDataSets[] 	= [];
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
