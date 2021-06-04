import { Component, OnInit } 					from '@angular/core';
//import { CandidateServiceService }				from '../candidate-service.service';
import { StatisticsService } 					from '../statistics.service';
import { ChartDataSets, ChartOptions, ChartType } 			from 'chart.js';
import { Color, Label } 						from 'ng2-charts';

@Component({
  selector: 'app-statistics',
  templateUrl: './statistics.component.html',
  styleUrls: ['./statistics.component.css']
})
export class StatisticsComponent implements OnInit {

	totalNumberActiveCandidates:number = 0;
	candidatesByFunction:Map<string,number> = new Map<string,number>();
	currentTab:string = "downloads";
	showStatsDownloads:boolean=true;
	showStatsAvailability:boolean=false;

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

		this.statisticsService.getTotalNumberOfActiceCandidatesStatistics().forEach(count => {

			this.totalNumberActiveCandidates = count;

    	});

		this.statisticsService.getAvailableCandidatesByFunctionStatistics().forEach(data => {
		
			let stats:any[] = data;
			
			let functionStatCount:Array<number> 		= new Array<number>(); //Object.values(stat.recruiterDownloads);
			let functionStatName:Array<string> 			= new Array<string>(); //'Object.keys(stat.recruiterDownloads);
				
			stats.forEach(functonStat => {
				//this.candidatesByFunction.set(functonStat.function, functonStat.availableCandidates);
				
				functionStatName.push(functonStat.function);
				functionStatCount.push(functonStat.availableCandidates);
				
			});
			
			this.functionChartData = [{ data: functionStatCount, label: 'Function' },];
			this.functionChartLabels = functionStatName;
			
			
    	});

	}

	ngOnInit(): void {
	}
	
	public switchTab(tab:string){
		this.currentTab = tab;
		console.log("XXX" + tab)
		switch(tab){
			case "downloads":{
				this.showStatsDownloads=true;
				this.showStatsAvailability=false;
				break;
			}
			case "availability":{
				this.showStatsDownloads=false;
				this.showStatsAvailability=true;
				break;
			}
		}
		
		
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
 
	/**
	* Availability by Function
	*/
	functionChartData: 		ChartDataSets[] 	= [];
	functionChartLabels: 	Label[] 			= [];

  	functionChartOptions = {
    	responsive: true,
  	};

  	functionChartColors: Color[] = [
    	{
      		borderColor: 'black',
      		backgroundColor: 'rgba(0,0,0,0.28)',
    	},
  	];

  	functionChartLegend = true;
  	functionChartPlugins = [];
  	functionChartType:ChartType = 'bar';
}
