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

	totalNumberActiveCandidates:number 			= 0;
	candidatesByFunction:Map<string,number> 	= new Map<string,number>();
	currentTab:string 							= "downloads";
	showStatsDownloads:boolean					=true;
	showStatsAvailability:boolean				=false;

	recruiterDownloads:number[] 				= [];
	recruiterDownloadsDaily:number[] 			= [];
	recruiterDownloadsWeekly:number[] 			= [];
	
	recruiterDownloadsCols:string[] 			= [];
	recruiterDownloadsDailyCols:string[] 		= [];
	recruiterDownloadsWeeklyCols:string[] 		= [];
			
	/**
  	* Constructor
	*/
	constructor(public statisticsService:StatisticsService) {

		this.fetchStatus();

	}

	ngOnInit(): void {
	}
	
	public fetchStatus():void{
		
		this.statisticsService.getCurriculumDownloadStatistics().forEach(stat => {

			let downloads:number[] 		= Object.values(stat.weeklyDownloads);
			let downloadDates:string[] 	= Object.keys(stat.weeklyDownloads);
			
			this.lineChartData = [{ data: downloads, label: 'Downloads' },];
			this.lineChartLabels = downloadDates;
			
			this.recruiterDownloadsDaily 			= Object.values(stat.recruiterDownloadsDaily);
			this.recruiterDownloadsWeekly 			= Object.values(stat.recruiterDownloadsWeekly);
	
			this.recruiterDownloadsDailyCols 		= Object.keys(stat.recruiterDownloadsDaily);
			this.recruiterDownloadsWeeklyCols 		= Object.keys(stat.recruiterDownloadsWeekly);
			
			this.recruiterDownloadsChartData = [{ data: this.recruiterDownloadsDaily, label: 'Todays downloads' }];
												
			this.recruiterDownloadsChartLabels = this.recruiterDownloadsDailyCols;

    	});

		this.statisticsService.getTotalNumberOfActiceCandidatesStatistics().forEach(count => {

			this.totalNumberActiveCandidates = count;

    	});

		this.statisticsService.getAvailableCandidatesByFunctionStatistics().forEach(data => {
		
			let stats:any[] = data;
			
			let functionStatCount:Array<number> 		= new Array<number>(); 
			let functionStatName:Array<string> 			= new Array<string>(); 
				
			stats.forEach(functonStat => {
				
				functionStatName.push(functonStat.function);
				functionStatCount.push(functonStat.availableCandidates);
				
			});
			
			this.functionChartData = [{ data: functionStatCount, label: 'Function' },];
			this.functionChartLabels = functionStatName;
			
    	});

	}
	
	public switchChartData(type:string):void{
		
		switch (type) {
			case "day":{
				this.recruiterDownloadsChartData = [{ data: this.recruiterDownloadsDaily, label: 'Todays downloads' }];
				this.recruiterDownloadsChartLabels = this.recruiterDownloadsDailyCols;
				return;
			}
			case "week":{
				this.recruiterDownloadsChartData = [{ data: this.recruiterDownloadsWeekly, label: 'This Weeks downloads' }];
				this.recruiterDownloadsChartLabels = this.recruiterDownloadsWeeklyCols;
				return;
			}
		}
	}

	/**
	* Switches between tabs in the gui
	* param tab - id of the tab to show
	*/	
	public switchTab(tab:string){
		this.currentTab = tab;
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
