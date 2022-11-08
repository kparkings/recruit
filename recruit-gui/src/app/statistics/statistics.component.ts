import { Component, OnInit } 								from '@angular/core';
import { StatisticsService } 								from '../statistics.service';
import { ChartDataSets, ChartOptions, ChartType } 			from 'chart.js';
import { Color, Label } 									from 'ng2-charts';
import { NewCandidate, 	   NewCandidateSummaryItem } 		from '../new-candidate';
import { NewCandidateStat, NewCandidateStatItem } 			from '../new-candidate-stat';

@Component({
  selector: 'app-statistics',
  templateUrl: './statistics.component.html',
  styleUrls: ['./statistics.component.css']
})
export class StatisticsComponent implements OnInit {

	public totalNumberActiveCandidates:number 			= 0;
	public candidatesByFunction:Map<string,number> 		= new Map<string,number>();
	public currentTab:string 							= "downloads";
	public showStatsDownloads:boolean					= true;
	public showStatsAvailability:boolean				= false;
	public showStatsListings:boolean					= false;
	public showStatsEmailRequests:boolean				= false;
	public showNewCandidates:boolean					= false;

	public recruiterDownloads:number[] 					= [];
	public recruiterDownloadsDaily:number[] 			= [];
	public recruiterDownloadsWeekly:number[] 			= [];
	
	public recruiterDownloadsCols:string[] 				= [];
	public recruiterDownloadsDailyCols:string[] 		= [];
	public recruiterDownloadsWeeklyCols:string[] 		= [];
			
	public chartDownloadsTotal							= 0;		
	
	public listingViewsToday 							= 0;
	public listingViewsThisWeek 						= 0;
	
	public emailRequestViewsToday 						= 0;
	public emailRequestViewsThisWeek 					= 0;
		
		
	public recruiterEmailRequests:number[] 					= [];
	public recruiterEmailRequestsDaily:number[] 			= [];
	public recruiterEmailRequestsWeekly:number[] 			= [];
	
	public recruiterEmailRequestsCols:string[] 				= [];
	public recruiterEmailRequestsDailyCols:string[] 		= [];
	public recruiterEmailRequestsWeeklyCols:string[] 		= [];	
		
	public chartEmailRequestsTotal							= 0;
	
	public showNewCandidateStatsDiv:boolean 				= false;
	public showNewCandidatesDiv:boolean 					= false;
		
	/**
  	* Constructor
	*/
	constructor(public statisticsService:StatisticsService) {

		this.fetchStatus();
		this.getEmailRequestStats();
		
		this.showNewCandidateStatsDiv 				= false;
		this.showNewCandidatesDiv 					= false;

	}

	ngOnInit(): void {
	}
	
	
	public fetchStatus():void{
		
		this.statisticsService.getListingStats().subscribe(listingData => {
					
					this.listingViewsToday 		= listingData.viewsToday;
					this.listingViewsThisWeek 	= listingData.viewsThisWeek;
					
					let listingChartViews:number[] 		= Object.values(listingData.viewsPerWeek);
					let listingChartViewsKeys:string[] 	= Object.keys(listingData.viewsPerWeek);
			
					this.listingChartData = [{ data: listingChartViews, label: 'Downloads' },];
					this.listingChartLabels = listingChartViewsKeys;
			
					
				}, 
				err => {
					console.log("Error retrieving listings stats" + JSON.stringify(err));			
				});
		
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
												
			this.recruiterDownloadsChartLabels 		= this.recruiterDownloadsDailyCols;
			this.chartDownloadsTotal 				= this.recruiterDownloadsDaily.length;

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
	
	public getEmailRequestStats():void{
		
		this.statisticsService.getEmailRequestStats().subscribe(emailRequestData => {
			
			let downloads:number[] 		= Object.values(emailRequestData.weeklyRequests);
			let downloadDates:string[] 	= Object.keys(emailRequestData.weeklyRequests);
			
			this.emailRequestChartData = [{ data: downloads, label: 'Downloads' },];
			this.emailRequestChartLabels = downloadDates;
			
			this.recruiterEmailRequestsDaily 		= Object.values(emailRequestData.recruiterRequestsDaily);
			this.recruiterEmailRequestsWeekly 		= Object.values(emailRequestData.recruiterRequestsWeekly);
	
			this.recruiterEmailRequestsDailyCols 		= Object.keys(emailRequestData.recruiterRequestsDaily);
			this.recruiterEmailRequestsWeeklyCols 		= Object.keys(emailRequestData.recruiterRequestsWeekly);
			
			this.emailRequestRecruiterChartData = [{ data: this.recruiterEmailRequestsDaily, label: 'Todays downloads' }];
												
			this.emailRequestRecruiterChartLabels 	= this.recruiterEmailRequestsDailyCols;
			this.chartDownloadsTotal 				= this.recruiterEmailRequestsDailyCols.length;
		
			
		});
		
	}
	
	/**
	* Switches between the various datasets available for the chart
	* @oaram: type - which data set to show 
	*/
	public switchChartData(type:string):void{
		
		switch (type) {
			case "day":{
				this.recruiterDownloadsChartData = [{ data: this.recruiterDownloadsDaily, label: 'Todays downloads' }];
				this.recruiterDownloadsChartLabels = this.recruiterDownloadsDailyCols;
				this.chartDownloadsTotal = this.recruiterDownloadsDaily.length;
				return;
			}
			case "week":{
				this.recruiterDownloadsChartData = [{ data: this.recruiterDownloadsWeekly, label: 'This Weeks downloads' }];
				this.recruiterDownloadsChartLabels = this.recruiterDownloadsWeeklyCols;
				this.chartDownloadsTotal = this.recruiterDownloadsWeekly.length;
				return;
			}
		}
	}
	
		/**
	* Switches between the various datasets available for the chart
	* @oaram: type - which data set to show 
	*/
	public switchChartDataEmailRequests(type:string):void{
		
		switch (type) {
			case "day":{
				this.emailRequestRecruiterChartData = [{ data: this.recruiterEmailRequestsDaily, label: 'Todays downloads' }];
				this.emailRequestRecruiterChartLabels = this.recruiterEmailRequestsCols;
				this.chartEmailRequestsTotal = this.recruiterEmailRequestsDaily.length;
				return;
			}
			case "week":{
				this.emailRequestRecruiterChartData = [{ data: this.recruiterEmailRequestsWeekly, label: 'This Weeks downloads' }];
				this.emailRequestRecruiterChartLabels = this.recruiterEmailRequestsWeeklyCols;
				this.chartEmailRequestsTotal = this.recruiterEmailRequestsWeekly.length;
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
				this.showStatsListings=false;
				this.showStatsEmailRequests=false;
				this.showNewCandidates=false;
				break;
			}
			case "listings":{
				this.showStatsDownloads=false;
				this.showStatsAvailability=false;
				this.showStatsListings=true;
				this.showStatsEmailRequests=false;
				this.showNewCandidates=false;
				break;
			}
			case "availability":{
				this.showStatsDownloads=false;
				this.showStatsListings=false;
				this.showStatsAvailability=true;
				this.showStatsEmailRequests=false;
				this.showNewCandidates=false;
				break;
			}
			case "email":{
				this.showStatsDownloads=false;
				this.showStatsListings=false;
				this.showStatsAvailability=false;
				this.showStatsEmailRequests=true;
				this.showNewCandidates=false;
				break;
			}
			case "newCandidateStats":{
				this.showStatsDownloads=false;
				this.showStatsListings=false;
				this.showStatsAvailability=false;
				this.showStatsEmailRequests=false;
				this.showNewCandidates=true;
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

	/**
	* Listing Downloads
	*/
	listingChartData: 		ChartDataSets[] 	= [];
	listingChartLabels: 	Label[] 			= [];

  	listingChartOptions = {
    	responsive: true,
  	};

  	listingChartColors: Color[] = [
    	{
      		borderColor: 'black',
      		backgroundColor: 'rgba(0,0,0,0.28)',
    	},
  	];

  	listingChartLegend = true;
  	listingChartPlugins = [];
  	listingChartType:ChartType = 'line';


	/**
	* recruiter Email Requests
	*/
	emailRequestChartData: 		ChartDataSets[] 	= [];
	emailRequestChartLabels: 	Label[] 			= [];

  	emailRequestChartOptions = {
    	responsive: true,
  	};

  	emailRequestChartColors: Color[] = [
    	{
      		borderColor: 'black',
      		backgroundColor: 'rgba(0,0,0,0.28)',
    	},
  	];

  	emailRequestChartLegend = true;
  	emailRequestChartPlugins = [];
  	emailRequestChartType:ChartType = 'line';
 

	/**
	* Daily Email Requests
	*/
	emailRequestRecruiterChartData: 	ChartDataSets[] 	= [];
	emailRequestRecruiterChartLabels: 	Label[] 			= [];

  	emailRequestRecruiterChartOptions = {
    	responsive: true,
  	};

  	emailRequestRecruiterChartColors: Color[] = [
    	{
      		borderColor: 'black',
      		backgroundColor: 'rgba(0,0,0,0.28)',
    	},
  	];

  	emailRequestRecruiterChartLegend = true;
  	emailRequestRecruiterChartPlugins = [];
  	emailRequestRecruiterChartType:ChartType = 'bar';


	public newCandidates:Array<NewCandidateSummaryItem>  	= new Array<NewCandidateSummaryItem>();
	public newCandidateStats:Array<NewCandidateStatItem>  	= new Array<NewCandidateStatItem>();
	
	/**
	* Loads list of new Candidates
	*/
	public loadNewCandidates():void{
		this.statisticsService.getNewCandidatesList().subscribe(c => {
			this.newCandidates 				= c.candidateSummary;
			this.showNewCandidatesDiv 		= true;
			this.showNewCandidateStatsDiv	= false;
		});
	}

	/**
	* Loads statistics for new Candidates
	*/
	public loadNewCandidateStats():void{
		this.statisticsService.getNewCandidatesSummary().subscribe(s => {
			this.newCandidateStats 			= s.candidatesSummary;
			this.showNewCandidatesDiv 		= false;
			this.showNewCandidateStatsDiv 	= true;
		});
	}

	public countryToCode(country:string):string{
		
		switch(country){
			case 'NETHERLANDS':{
				return 'NL';
			}
			case 'BELGIUM':{
				return 'NL';
			}
			case 'UK':{
				return 'UK/IRL';
			}
			default:{
				return country;
			}
			
		}
		
	}

}