import { Component, OnInit } 								from '@angular/core';
import { StatisticsService } 								from '../statistics.service';
//import { Chart, ChartDataSets, ChartType }				 			from 'chart.js';
//import { Chart, ChartType }				 			from 'chart.js';
//import { Color, Label } 									from 'ng2-charts';
import { NewCandidateSummaryItem } 							from '../new-candidate';
import { NewCandidateStatItem } 							from '../new-candidate-stat';
import { Router}											from '@angular/router';
import { LoginStats } 										from '../login-event-stats';
import { RecruiterSearchStatistics } 						from '../recruiter-search-stats';
import { CandidateFunction } 								from '../candidate-function';
import { CandidateServiceService }							from '../candidate-service.service';

import { Chart } from 'chart.js';

@Component({
  selector: 'app-statistics',
  templateUrl: './statistics.component.html',
  styleUrls: ['./statistics.component.css']
})
export class StatisticsComponent implements OnInit {

	public chartColor:string = 'grey';
	public totalNumberActiveCandidates:number 			= 0;
	public candidatesByFunction:Map<string,number> 		= new Map<string,number>();
	public currentTab:string 							= "downloads";
	public showStatsLogins:boolean						= true;
	public showStatsDownloads:boolean					= false;
	public showStatsAvailability:boolean				= false;
	public showStatsListings:boolean					= false;
	//public showStatsEmailRequests:boolean				= false;
	public showNewCandidates:boolean					= false;
	public showMarketplaceStats:boolean					= false;

	public recruiterDownloads:string[] 					= [];
	public recruiterDownloadsDaily:string[] 			= [];
	public recruiterDownloadsWeekly:string[] 			= [];
	
	public recruiterDownloadsCols:string[] 				= [];
	public recruiterDownloadsDailyCols:string[] 		= [];
	public recruiterDownloadsWeeklyCols:string[] 		= [];
			
	public chartDownloadsTotal							= 0;		
	
	public listingViewsToday 							= 0;
	public listingViewsThisWeek 						= 0;
	
	public showNewCandidateStatsDiv:boolean 				= false;
	public showNewCandidatesDiv:boolean 					= false;
	
	/**
	* Marketplace Stats
	*/
	
	public requestedCandidateViewsTotal:number							= 0;
	public requestedCandidateViewsTotalDaily:number						= 0;	
	public requestedCandidateViewsTotalWeekly:number					= 0;	
	
	//public marketPlaceEventsChartDataRequested:ChartDataSets[] 			= [];
	//public marketPlaceEventsChartLabelsRequested:Label[] 				= [];
	
	//public marketPlaceEventsChartDataRequestedDaily:ChartDataSets[] 	= [];
	//public marketPlaceEventsChartLabelsRequestedDaily:Label[] 			= [];

	//public marketPlaceEventsChartDataRequestedWeekly:ChartDataSets[] 	= [];
	//public marketPlaceEventsChartLabelsRequestedWeekly:Label[] 			= [];
	
	//public loginChartView:string = 'chart';
	public loginChartViewUser:string = '';

	public candidateFunctions:Array<CandidateFunction> = new Array<CandidateFunction>();
	public getFunctionLabel(type:string){
		
		if(type == null){
			return "";
		}
		
		return this.candidateFunctions.filter(f => f.id == type)[0].desc;
	}
	
  	public marketPlaceEventsChartOptions = {
    	responsive: true,
  	};

  	//public marketPlaceEventsChartColors: Color[] 			= [
   // 	{
    //  		borderColor: 'black',
    //  		backgroundColor: 'rgba(0,0,0,0.28)',
    //	},
  //	];
  	
  	/**
	* Toggles between the chart 
	* and table views
	* @param view - chart | table
	*/
  	public switchUserStatsView(view:string, selectedUserId:any = ""):void{
		  //this.loginChartView 		= view;
		  this.loginChartViewUser 	= selectedUserId.toString();
		  this.fetchReruiterSearchStats();
	}

  	public marketPlaceEventsChartLegend 					= true;
  	public marketPlaceEventsChartPlugins 					= [];
  	//public marketPlaceEventsChartType:ChartType 			= 'bar';	
	
		
	/**
  	* Constructor
	*/
	constructor(public statisticsService:StatisticsService, public candidateService:CandidateServiceService, private router:Router) {

		this.fetchStatus();
		//this.getEmailRequestStats();
		this.fetchMarketplaceStats();
		
		this.showNewCandidateStatsDiv 				= false;
		this.showNewCandidatesDiv 					= false;
		this.candidateFunctions 					= this.candidateService.loadFunctionTypes();
		
	}

	ngOnInit(): void {
		this.createLeftChart(this.loginChartLabels, this.loginChartData);
		this.createRightChart(this.recruiterLoginsChartLabels, this.recruiterLoginsChartData);
	}
	
	public recruiterStats:RecruiterSearchStatistics = new RecruiterSearchStatistics();
	
	/**
	* Fetches and displays searches carries out by the selected Recruiter
	*/
	public fetchReruiterSearchStats():void{
		
		this.statisticsService.fetchRecruiterSearchStats(this.loginChartViewUser, this.recruiterStatPeriod).subscribe(stats => {
			this.recruiterStats = stats;
		});
	}
	
	/**
	* Switches between the Daily and Weeklt for Requested Candidates
	* @oaram: type - which data set to show 
	*/
	public switchMarketplaceRequestedCandidatesChartData(type:string):void{
		
		switch (type) {
			case "day":{
			//	this.marketPlaceEventsChartDataRequested 	= this.marketPlaceEventsChartDataRequestedDaily;
			//	this.marketPlaceEventsChartLabelsRequested 	= this.marketPlaceEventsChartLabelsRequestedDaily;
			//	this.requestedCandidateViewsTotal 			= this.requestedCandidateViewsTotalDaily;
				return;
			}
			case "week":{
			//	this.marketPlaceEventsChartDataRequested 	= this.marketPlaceEventsChartDataRequestedWeekly;
			//	this.marketPlaceEventsChartLabelsRequested 	= this.marketPlaceEventsChartLabelsRequestedWeekly;
			//	this.requestedCandidateViewsTotal 			= this.requestedCandidateViewsTotalWeekly;
				return;
			}
		}
	}
	
	public fetchMarketplaceStats():void{
		
		this.requestedCandidateViewsTotal						= 0;
		this.requestedCandidateViewsTotalDaily					= 0;	
		this.requestedCandidateViewsTotalWeekly					= 0;	
	
		//this.marketPlaceEventsChartDataRequested 				= [];
		//this.marketPlaceEventsChartDataRequestedDaily 			= [];
		//this.marketPlaceEventsChartLabelsRequestedDaily 		= [];
		//this.marketPlaceEventsChartLabelsRequested 				= [];
		//this.marketPlaceEventsChartDataRequestedWeekly 			= [];
		//this.marketPlaceEventsChartLabelsRequestedWeekly 		= [];
		
		this.statisticsService.getMarketPlaceRequestedCandidateViewStats().subscribe(mpStats => {
					
			let viewsByRecruiterDaily:number[] 		= new Array<number>();
			let viewsByRecruiterWeekly:number[] 	= new Array<number>();
			let recruiterIds:string[] 				= new Array<string>();			

			mpStats.stats.forEach(recruiterStat => {
				
				viewsByRecruiterDaily.push(recruiterStat.viewsToday);
				viewsByRecruiterWeekly.push(recruiterStat.viewsThisWeek);
				recruiterIds.push(recruiterStat.recruiterId);
				
				this.requestedCandidateViewsTotalDaily  = this.requestedCandidateViewsTotalDaily + recruiterStat.viewsToday;
				this.requestedCandidateViewsTotalWeekly = this.requestedCandidateViewsTotalWeekly + recruiterStat.viewsThisWeek;
				
			});
		
			//this.marketPlaceEventsChartDataRequestedDaily = [{ data: viewsByRecruiterDaily, label: 'Views By Recruiter' },];
			//this.marketPlaceEventsChartLabelsRequestedDaily = recruiterIds;
			
			//this.marketPlaceEventsChartDataRequestedWeekly = [{ data: viewsByRecruiterWeekly, label: 'Views By Recruiter' },];
			//this.marketPlaceEventsChartLabelsRequestedWeekly = recruiterIds;	
			
			this.switchMarketplaceRequestedCandidatesChartData('day');
					
		}, 
		err => {
			console.log("Error retrieving listings stats" + JSON.stringify(err));			
		});
		
	}
	
	public refreshData():void{
		this.fetchStatus();
		this.fetchMarketplaceStats();
	}
	
	yearKeys:string[] 			= new Array<string>();
	yearValues:string[] 		= new Array<string>();

	downloads:string[] 			= new Array<string>();
	downloadKeys:string[]		= new Array<string>();
			
	threeMonthKeys:string[] 	= new Array<string>();
	threeMonthValues:string[] 	= new Array<string>();
			
	weekKeys:string[] 			= new Array<string>();
	weekValues:string[] 		= new Array<string>();
	
	
	loginsUserIdsToday:string[] = new Array<string>();
	loginsToday:string[] 		= new Array<string>();
			
			
	loginsUserIdsWeek:string[] 	= new Array<string>();
	loginsWeek:string[] 		= new Array<string>();
			
	
	public switchLoginStats(period:string):void{
		
		if(period === 'year') {
			this.loginChartData = [{ data: this.yearValues, label: 'Downloads', backgroundColor: this.chartColor },];
			this.loginChartLabels = this.yearKeys;
			this.createLeftChart(this.loginChartLabels, this.loginChartData);
		}
		
		if(period === '3months') {
			this.loginChartData = [{ data: this.threeMonthValues, label: 'Downloads', backgroundColor: this.chartColor },];
			this.loginChartLabels = this.threeMonthKeys;
			this.createLeftChart(this.loginChartLabels, this.loginChartData);	
		}
		
		if(period === 'week') {
			this.loginChartData = [{ data: this.weekValues, label: 'Downloads', backgroundColor: this.chartColor },];
			this.loginChartLabels = this.weekKeys;
			this.createLeftChart(this.loginChartLabels, this.loginChartData);
		}
		
	}
	
	public loginCountRecruiter:number = 0;
	public loginCountCandidate:number = 0;
	
	public loginCountRecruiterToday:number = 0;
	public loginCountCandidateToday:number = 0;
	public loginCountRecruiterWeek:number = 0;
	public loginCountCandidateWeek:number = 0;
	
	public recruiterStatPeriod:string = "DAY";
	
	public switchUserStats(period:string):void{
		
		if(period === 'today') {
			this.recruiterLoginsChartData = [{ data: this.loginsToday, label: 'Todays logins', backgroundColor: this.chartColor  }]; 
	        this.recruiterLoginsChartLabels  = this.loginsUserIdsToday; 
	        this.loginCountRecruiter = this.loginCountRecruiterToday;
			this.loginCountCandidate = this.loginCountCandidateToday;
			this.recruiterStatPeriod = "DAY";
			this.createRightChart(this.recruiterLoginsChartLabels, this.recruiterLoginsChartData);	
		}
		
		if(period === 'week') {
			this.recruiterLoginsChartData = [{ data: this.loginsWeek, label: 'Weeks logins' ,backgroundColor: this.chartColor  }]; 
	        this.recruiterLoginsChartLabels  = this.loginsUserIdsWeek; 	
	        this.loginCountRecruiter = this.loginCountRecruiterWeek;
			this.loginCountCandidate = this.loginCountCandidateWeek;
			this.recruiterStatPeriod = "WEEK";
			this.createRightChart(this.recruiterLoginsChartLabels, this.recruiterLoginsChartData);
		}
		
	}
	
	public fetchStatus():void{
		
		this.statisticsService.getListingStats().subscribe(listingData => {
					
					this.listingViewsToday 		= listingData.viewsToday;
					this.listingViewsThisWeek 	= listingData.viewsThisWeek;
					
					let listingChartViews:number[] 		= Object.values(listingData.viewsPerWeek);
					let listingChartViewsKeys:string[] 	= Object.keys(listingData.viewsPerWeek);
			
					//this.listingChartData = [{ data: listingChartViews, label: 'Downloads' },];
					//this.listingChartLabels = listingChartViewsKeys;
			
					
				}, err => {
			if (err.status === 401 || err.status === 0) {
				sessionStorage.removeItem('isAdmin');
				sessionStorage.removeItem('isRecruter');
				sessionStorage.removeItem('isCandidate');
				sessionStorage.removeItem('loggedIn');
				sessionStorage.setItem('beforeAuthPage', 'view-candidates');
				this.router.navigate(['login-user']);
			}
    	});
    	
    	this.statisticsService.fetchLoginStats().subscribe(stats => {
			
			let statsObj:LoginStats 		= Object.assign(new LoginStats(), stats)
			
			this.loginsUserIdsToday 				= Array.from(statsObj.getEventsTodayKeys());
			this.loginsToday 				= statsObj.getEventsTodayValues();
			
			this.loginsUserIdsWeek 				= Array.from(statsObj.getEventsWeekKeys());
			this.loginsWeek 				= statsObj.getEventsWeekValues();
			
			this.yearKeys 					= Array.from(statsObj.getYearStatsKeysAsStrings());
			this.yearValues 				= statsObj.getYearStatsValues();
			
			this.threeMonthKeys 			= Array.from(statsObj.getThreeMonthStatsKeysAsStrings());
			this.threeMonthValues 			= statsObj.getThreeMonthStatsValues();
			
			this.weekKeys 					= Array.from(statsObj.getWeekStatsKeysAsStrings());
			this.weekValues 				= statsObj.getWeekStatsValues();
			
			//this.loginChartData = [{ data: this.yearValues, label: 'Downloads',backgroundColor: this.chartColor},];
			//this.loginChartLabels = this.yearKeys;
			
			//START
			this.loginChartData = [
	           	{
	            label: "Logins",
	       	    data: this.yearValues,
	            backgroundColor: this.chartColor
	          },
	        	];
			this.loginChartLabels = this.yearKeys;
			this.createLeftChart(this.loginChartLabels, this.loginChartData);
			//END
			
			
			//START
			this.recruiterLoginsChartData = [
	           	{
	            label: "Todays Logins",
	       	    data: this.loginsToday,
	            backgroundColor: this.chartColor
	          },
	        	];
			this.recruiterLoginsChartLabels = this.loginsUserIdsToday; 
			this.createRightChart(this.recruiterLoginsChartLabels, this.recruiterLoginsChartData);
			//END
			
			
			//this.recruiterLoginsChartData = [{ data: this.loginsToday, label: 'Todays logins' }]; 
	        //this.recruiterLoginsChartLabels  = this.userIdsToday; 
	        
	        this.loginCountRecruiterToday = new Set(stats.eventsToday.filter(e => e.recruiter).map(e => e.userId)).size;
			this.loginCountCandidateToday = new Set(stats.eventsToday.filter(e => e.candidate).map(e => e.userId)).size;
			this.loginCountRecruiterWeek  = new Set(stats.eventsWeek.filter(e => e.recruiter).map(e => e.userId)).size;
			this.loginCountCandidateWeek  = new Set(stats.eventsWeek.filter(e => e.candidate).map(e => e.userId)).size;
			
			this.loginCountRecruiter = this.loginCountRecruiterToday;
			this.loginCountCandidate = this.loginCountCandidateToday;
			
			
    	});	
		
		this.statisticsService.getCurriculumDownloadStatistics().forEach(stat => {

			this.downloads 		= Object.values(stat.weeklyDownloads);
			this.downloadKeys 	= Object.keys(stat.weeklyDownloads);
			
			//this.lineChartData = [{ data: downloads, label: 'Downloads' },];
			//this.lineChartLabels = downloadDates;
			
			this.recruiterDownloadsDaily 			= Object.values(stat.recruiterDownloadsDaily);
			this.recruiterDownloadsWeekly 			= Object.values(stat.recruiterDownloadsWeekly);
	
			this.recruiterDownloadsDailyCols 		= Object.keys(stat.recruiterDownloadsDaily);
			this.recruiterDownloadsWeeklyCols 		= Object.keys(stat.recruiterDownloadsWeekly);
			
			//this.recruiterDownloadsChartData = [{ data: this.recruiterDownloadsDaily, label: 'Todays downloads' }];
												
			//this.recruiterDownloadsChartLabels 		= this.recruiterDownloadsDailyCols;
			
			//START
			this.downloadsChartData = [
	           	{
	            label: "Logins",
	       	    data: this.downloads,
	            backgroundColor: this.chartColor
	          },
	        	];
			this.loginChartLabels = this.yearKeys;
			this.createLeftChart(this.downloadsChartLabels, this.downloadsChartData);
			//END
			
			
			//START
			this.recruiterDownloadsChartData = [
	           	{
	            label: "Todays Logins",
	       	    data: this.recruiterDownloadsDaily,
	            backgroundColor: this.chartColor
	          },
	        	];
			this.recruiterLoginsChartLabels = this.recruiterDownloadsDailyCols; 
			this.createRightChart(this.recruiterLoginsChartLabels, this.recruiterDownloadsChartData);
			//END
			
			
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
			
			//this.functionChartData = [{ data: functionStatCount, label: 'Function' },];
			//this.functionChartLabels = functionStatName;
			
    	});

	}
	
	/**
	* Switches between the various datasets available for the chart
	* @oaram: type - which data set to show 
	*/
	public switchChartData(type:string):void{
		
		switch (type) {
			case "day":{
				this.recruiterDownloadsChartData = [{ data: this.recruiterDownloadsDaily, label: 'Todays downloads' ,backgroundColor: this.chartColor   }];
				this.recruiterDownloadsChartLabels = this.recruiterDownloadsDailyCols;
				this.chartDownloadsTotal = this.recruiterDownloadsDaily.length;
				this.createRightChart(this.recruiterDownloadsChartLabels, this.recruiterDownloadsChartData);
				return;
			}
			case "week":{
				this.recruiterDownloadsChartData = [{ data: this.recruiterDownloadsWeekly, label: 'This Weeks downloads' ,backgroundColor: this.chartColor   }];
				this.recruiterDownloadsChartLabels = this.recruiterDownloadsWeeklyCols;
				this.chartDownloadsTotal = this.recruiterDownloadsWeekly.length;
				this.createRightChart(this.recruiterDownloadsChartLabels, this.recruiterDownloadsChartData);
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
			case "logins":{
				this.showStatsLogins=true;
				this.showStatsDownloads=false;
				this.showStatsAvailability=false;
				this.showStatsListings=false;
				//this.showStatsEmailRequests=false;
				this.showNewCandidates=false;
				this.showMarketplaceStats=false;
				
				this.loginChartData = [{ data: this.yearValues, label: 'Downloads', backgroundColor: this.chartColor },];
				this.loginChartLabels = this.yearKeys;
				this.createLeftChart(this.loginChartLabels, this.loginChartData);
				this.createRightChart(this.recruiterLoginsChartLabels, this.recruiterLoginsChartData);
				break;
			}
			case "downloads":{
				this.showStatsLogins=false;
				this.showStatsDownloads=true;
				this.showStatsAvailability=false;
				this.showStatsListings=false;
				//this.showStatsEmailRequests=false;
				this.showNewCandidates=false;
				this.showMarketplaceStats=false;
				//this.loginChartData = [{ data: this.yearValues, label: 'Downloads', backgroundColor: this.chartColor },];
				//this.loginChartLabels = this.yearKeys;
				
				
				this.createLeftChart(this.downloadsChartLabels, this.downloadsChartData);
				this.createRightChart(this.recruiterLoginsChartLabels, this.recruiterDownloadsChartData);
			
				break;
			}
			case "listings":{
				this.showStatsLogins=false;
				this.showStatsDownloads=false;
				this.showStatsAvailability=false;
				this.showStatsListings=true;
				//this.showStatsEmailRequests=false;
				this.showNewCandidates=false;
				this.showMarketplaceStats=false;
				break;
			}
			case "availability":{
				this.showStatsLogins=false;
				this.showStatsDownloads=false;
				this.showStatsListings=false;
				this.showStatsAvailability=true;
				//this.showStatsEmailRequests=false;
				this.showNewCandidates=false;
				this.showMarketplaceStats=false;
				break;
			}
			case "email":{
				this.showStatsLogins=false;
				this.showStatsDownloads=false;
				this.showStatsListings=false;
				this.showStatsAvailability=false;
				//this.showStatsEmailRequests=true;
				this.showNewCandidates=false;
				this.showMarketplaceStats=false;
				break;
			}
			case "newCandidateStats":{
				this.showStatsLogins=false;
				this.showStatsDownloads=false;
				this.showStatsListings=false;
				this.showStatsAvailability=false;
				//this.showStatsEmailRequests=false;
				this.showNewCandidates=true;
				this.showMarketplaceStats=false;
				break;
			}
			case "marketplace":{
				this.showStatsLogins=false;
				this.showStatsDownloads=false;
				this.showStatsListings=false;
				this.showStatsAvailability=false;
				//this.showStatsEmailRequests=false;
				this.showNewCandidates=false;
				this.showMarketplaceStats=true;
				break;
			}
		}
		
	}






	/**
	* Logins
	*/
	//recruiterLoginsChartData: 		ChartDataSets[] 	= [];
	//recruiterLoginsChartLabels: 		Label[] 			= [];

  	recruiterLoginsChartOptions = {
    	responsive: true, 	
  	};

  	//recruiterLoginsChartColors: Color[] = [
    //	{
     // 		borderColor: 'black',
    //  		backgroundColor: 'rgba(0,0,0,0.28)',
    //	},
  	//];

  	recruiterLoginsChartLegend = true;
  	recruiterLoginsChartPlugins = [];
  	//recruiterLoginsChartType:ChartType = 'bar';
 
	//loginChartData: 	ChartDataSets[] 	= [];
	//loginChartLabels: 	Label[] 			= [];
	

  	loginChartOptions = {
    	responsive: true
  	};

  	//loginChartColors: Color[] = [
    //	{
     // 		borderColor: 'black',
    //  		backgroundColor: 'rgba(0,0,0,0.28)',
    //	},
  	//];

  	loginChartLegend = true;
  	loginChartPlugins = [];
  	//loginChartType:ChartType = 'line';

	


































	/**
	* recruiter Downloads
	*/
	//recruiterDownloadsChartData: 		ChartDataSets[] 	= [];
	//recruiterDownloadsChartLabels: 		Label[] 			= [];

  	recruiterDownloadsChartOptions = {
    	responsive: true,
  	};

  	//recruiterDownloadsChartColors: Color[] = [
    //	{
     // 		borderColor: 'black',
    //  		backgroundColor: 'rgba(0,0,0,0.28)',
    //	},
  	//];

  	recruiterDownloadsChartLegend = true;
  	recruiterDownloadsChartPlugins = [];
  	//recruiterDownloadsChartType:ChartType = 'bar';
 

	/**
	* Daily Downloads
	*/
	//lineChartData: 		ChartDataSets[] 	= [];
	//lineChartLabels: 	Label[] 			= [];

  	lineChartOptions = {
    	responsive: true,
  	};

  	//lineChartColors: Color[] = [
    //	{
     // 		borderColor: 'black',
     // 		backgroundColor: 'rgba(0,0,0,0.28)',
    //	},
  	//];

  	lineChartLegend = true;
  	lineChartPlugins = [];
  	//lineChartType:ChartType = 'line';
 
	/**
	* Availability by Function
	*/
	//functionChartData: 		ChartDataSets[] 	= [];
	//functionChartLabels: 	Label[] 			= [];

  	functionChartOptions = {
    	responsive: true,
  	};

  	//functionChartColors: Color[] = [
    //	{
     // 		borderColor: 'black',
     // 		backgroundColor: 'rgba(0,0,0,0.28)',
    //	},
  	//];

  	functionChartLegend = true;
  	functionChartPlugins = [];
  	//functionChartType:ChartType = 'bar';

	/**
	* Listing Downloads
	*/
	//listingChartData: 		ChartDataSets[] 	= [];
	//listingChartLabels: 	Label[] 			= [];

  	listingChartOptions = {
    	responsive: true,
  	};

  	//listingChartColors: Color[] = [
    //	{
    //  		borderColor: 'black',
    //  		backgroundColor: 'rgba(0,0,0,0.28)',
    //	},
  	//];

  	listingChartLegend = true;
  	listingChartPlugins = [];
  	//listingChartType:ChartType = 'line';


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
				return 'UK';
			}
			case 'REPUBLIC_OF_IRELAND':{
				return 'IE';
			}
			default:{
				return country;
			}
			
		}
		
	}
	
	
	loginChartData = [
	           	{
	            label: "Sales",
	            data: ['467','576', '572', '79', '92',
									 '574', '573', '576'],
	            backgroundColor: 'blue'
	          },
	        	];
	loginChartLabels = ['2022-05-10', '2022-05-11', '2022-05-12','2022-05-13','2022-05-14', '2022-05-15', '2022-05-16','2022-05-17', ];
	
	recruiterLoginsChartData = [
	           	{
	            label: "Sales",
	            data: ['467','576', '572', '79', '92',
									 '574', '573', '576'],
	            backgroundColor: 'blue'
	          },
	        	];
	recruiterLoginsChartLabels = ['2022-05-10', '2022-05-11', '2022-05-12','2022-05-13','2022-05-14', '2022-05-15', '2022-05-16','2022-05-17', ];
	
	
	downloadsChartLabels = ['2022-05-10', '2022-05-11', '2022-05-12','2022-05-13','2022-05-14', '2022-05-15', '2022-05-16','2022-05-17', ];
	
	downloadsChartData = [
	           	{
	            label: "Sales",
	            data: ['467','576', '572', '79', '92',
									 '574', '573', '576'],
	            backgroundColor: 'blue'
	          },
	        	];
	        	
	recruiterDownloadsChartLabels = ['2022-05-10', '2022-05-11', '2022-05-12','2022-05-13','2022-05-14', '2022-05-15', '2022-05-16','2022-05-17', ];
	
	recruiterDownloadsChartData = [
	           	{
	            label: "Sales",
	            data: ['467','576', '572', '79', '92',
									 '574', '573', '576'],
	            backgroundColor: 'blue'
	          },
	        	];
	
	createLeftChart(leftChartLabels:string[], leftChartData:any){
		
		
  		if(this.leftChart){
  			this.leftChart.destroy();
  		}
  		//this.rightChart.destroy();
  		
	    this.leftChart = new Chart("leftChart", {
	      type: 'bar', //this denotes tha type of chart
	
	      data: {// values on X-Axis
	        labels: leftChartLabels, 
		       datasets: leftChartData
	      },
	      options: {
	        aspectRatio:2.5
	      }
	      
	    });
	   
  	}
	
	
	
	public leftChart: any;
	public rightChart: any;
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	createRightChart(chartLabels:string[], chartData:any){
		
  		if(this.rightChart){
  			this.rightChart.destroy();
  		}
  		//this.rightChart.destroy();
  		
	    this.rightChart = new Chart("rightChart", {
	      type: 'bar', //this denotes tha type of chart
	
	      data: {// values on X-Axis
	        labels: chartLabels, 
		       datasets: chartData
	      },
	      options: {
	        aspectRatio:2.5
	      }
	      
	    });
	    
  	}
	
	
	
	
	
	

}