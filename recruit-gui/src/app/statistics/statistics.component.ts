import { Component, OnInit } 								from '@angular/core';
import { StatisticsService } 								from '../statistics.service';
import { NewCandidateSummaryItem } 							from '../new-candidate';
import { NewCandidateStatItem } 							from '../new-candidate-stat';
import { Router}											from '@angular/router';
import { LoginStats } 										from '../login-event-stats';
import { RecruiterSearchStatistics } 						from '../recruiter-search-stats';
import { CandidateFunction } 								from '../candidate-function';
import { CandidateServiceService }							from '../candidate-service.service';

import { Chart } from 'chart.js';
import { ListingStatistics, ViewItem } from '../listing-statistics';
import { SupportedCountry } from '../supported-candidate';

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
	public showNewCandidateStatsDiv:boolean 			= false;
	public showNewCandidatesDiv:boolean 				= false;
	public loginCountRecruiter:number 					= 0;
	public loginCountCandidate:number 					= 0;
	public loginCountRecruiterToday:number 				= 0;
	public loginCountCandidateToday:number 				= 0;
	public loginCountRecruiterWeek:number 				= 0;
	public loginCountCandidateWeek:number 				= 0;
	public recruiterStatPeriod:string 					= "DAY";
	public requestedCandidateViewsTotal:number			= 0;
	public requestedCandidateViewsTotalDaily:number		= 0;	
	public requestedCandidateViewsTotalWeekly:number	= 0;	
	public loginChartViewUser:string 					= '';
	public candidateFunctions:Array<CandidateFunction> 	= new Array<CandidateFunction>();
	public leftChart: any;
	public rightChart: any;
	public marketplaceChartLabels 						= [''];
	public listingsChartLabels 							= [''];
	public availabilityChartLabels 						= [''];
	public loginChartLabels 							= [''];
	public recruiterLoginsChartLabels 					= [''];
	public downloadsChartLabels 						= [''];
	public recruiterDownloadsChartLabels 				= [''];
	
	private listingViewsWeekKeys:string[] 						= new Array<string>();
	private listingViewWeekValues:string[] 						= new Array<string>();
	private marketplaceViewsKeys:string[] 						= new Array<string>();
	private marketpalceViewsValues:string[] 					= new Array<string>();
	private marketplaceViewsWeekKeys:string[] 					= new Array<string>();
	private marketpalceViewsWeekValues:string[] 				= new Array<string>();
	private marketplaceViewsDayKeys:string[] 					= new Array<string>();
	private marketpalceViewsDayValues:string[] 					= new Array<string>();
	private yearKeys:string[] 									= new Array<string>();
	private yearValues:string[] 								= new Array<string>();
	private downloads:string[] 									= new Array<string>();
	private downloadKeys:string[]								= new Array<string>();
	private threeMonthKeys:string[] 							= new Array<string>();
	private threeMonthValues:string[] 							= new Array<string>();
	private weekKeys:string[] 									= new Array<string>();
	private weekValues:string[] 								= new Array<string>();	
	private loginsUserIdsToday:string[] 						= new Array<string>();
	private loginsToday:string[] 								= new Array<string>();
	private loginsUserIdsWeek:string[] 							= new Array<string>();
	private loginsWeek:string[] 								= new Array<string>();
	public supportedCountries:Array<SupportedCountry>			= new Array<SupportedCountry>();
	private marketplaceChartData 								= [{label: "",data: [''],backgroundColor: ''}];
	private listingsChartData 									= [{label: "",data: [''],backgroundColor: ''}];
	private availabilityChartData 								= [{label: "",data: [''],backgroundColor: ''}];
	private loginChartData 										= [{label: "",data: [''],backgroundColor: ''}];
	private recruiterLoginsChartData 							= [{label: "",data: [''],backgroundColor: ''}];
	private downloadsChartData 									= [{label: "",data: [''], backgroundColor: ''}];
	private recruiterDownloadsChartData 						= [{label: "", data: [''], backgroundColor: ''}];

	public statChartDivMobile:string = '';
	public isMobile:boolean 					= false;
	
	public getFunctionLabel(type:string){
		
		if(type == null){
			return "";
		}
		
		return this.candidateFunctions.filter(f => f.id == type)[0].desc;
	}
	
  	public marketPlaceEventsChartOptions = {
    	responsive: true,
  	};

  	/**
	* Toggles between the chart 
	* and table views
	* @param view - chart | table
	*/
  	public switchUserStatsView(view:string, selectedUserId:any = ""):void{
		  this.loginChartViewUser 	= selectedUserId.toString();
		  this.fetchReruiterSearchStats();
	}

  	public marketPlaceEventsChartLegend 					= true;
  	public marketPlaceEventsChartPlugins 					= [];
	
		
	/**
  	* Constructor
	*/
	constructor(	public statisticsService:StatisticsService, 
					public candidateService:CandidateServiceService,
					private router:Router) {

		this.fetchStatus();
		this.fetchMarketplaceStats();
		
		this.showNewCandidateStatsDiv 				= false;
		this.showNewCandidatesDiv 					= false;
		this.candidateFunctions 					= this.candidateService.loadFunctionTypes();
		
	}

	ngOnInit(): void {
		this.supportedCountries = this.candidateService.getSupportedCountries();
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
				this.marketplaceChartLabels = this.marketplaceViewsDayKeys;
				this.marketpalceViewsValues = this.marketpalceViewsDayValues;
				this.requestedCandidateViewsTotal 			= this.requestedCandidateViewsTotalDaily;
				this.createLeftChart(this.marketplaceChartLabels, this.marketplaceChartData);
				return;
			}
			case "week":{
				this.marketplaceChartLabels = this.marketplaceViewsWeekKeys;
				this.marketpalceViewsValues = this.marketpalceViewsWeekValues;
				this.requestedCandidateViewsTotal 			= this.requestedCandidateViewsTotalWeekly;
				this.createLeftChart(this.marketplaceChartLabels, this.marketplaceChartData);
				return;
			}
				
		}
	}
	
	public fetchMarketplaceStats():void{
		
		this.requestedCandidateViewsTotal						= 0;
		this.requestedCandidateViewsTotalDaily					= 0;	
		this.requestedCandidateViewsTotalWeekly					= 0;	
	
		this.statisticsService.getMarketPlaceRequestedCandidateViewStats().subscribe(mpStats => {
					
			let viewsByRecruiterDaily:string[] 		= new Array<string>();
			let viewsByRecruiterWeekly:string[] 	= new Array<string>();
			let recruiterIdsDaily:string[] 			= new Array<string>();			
			let recruiterIdsWeekly:string[]			= new Array<string>();
			
			mpStats.stats.forEach(recruiterStat => {
	
				if (recruiterStat.viewsToday && recruiterStat.viewsToday > 0){
					viewsByRecruiterDaily.push(''+recruiterStat.viewsToday);
					recruiterIdsDaily.push(recruiterStat.recruiterId);
				}
				
				if (recruiterStat.viewsThisWeek && recruiterStat.viewsThisWeek > 0){
					viewsByRecruiterWeekly.push(''+recruiterStat.viewsThisWeek);
					recruiterIdsWeekly.push(recruiterStat.recruiterId);
				}
				
				this.requestedCandidateViewsTotalDaily  = this.requestedCandidateViewsTotalDaily + recruiterStat.viewsToday;
				this.requestedCandidateViewsTotalWeekly = this.requestedCandidateViewsTotalWeekly + recruiterStat.viewsThisWeek;
				
			});
			
			this.marketplaceViewsWeekKeys 		= recruiterIdsWeekly;
			this.marketpalceViewsWeekValues 	= viewsByRecruiterWeekly;
			this.marketplaceViewsDayKeys 		= recruiterIdsDaily;
			this.marketpalceViewsDayValues 		= viewsByRecruiterDaily;
			this.marketplaceViewsKeys			= this.marketplaceViewsWeekKeys
			this.marketpalceViewsValues 		= this.marketpalceViewsWeekValues;
				
			this.marketplaceChartData = [{label: "Marketplace Views", data: this.marketpalceViewsValues, backgroundColor: this.chartColor}];
			this.marketplaceChartLabels = this.marketplaceViewsKeys; 
			this.requestedCandidateViewsTotal 	= this.requestedCandidateViewsTotalWeekly;
				
		}, 
		err => {
			console.log("Error retrieving Marketplace stats" + JSON.stringify(err));			
		});
		
	}
	
	public refreshData():void{
		this.fetchStatus();
		this.fetchMarketplaceStats();
		this.switchTab('logins');
		this.switchLoginStats('year');
	}
			
	public switchLoginStats(period:string):void{
		
		if(period === 'year') {
			this.loginChartData = [{ data: this.yearValues, label: 'Logins', backgroundColor: this.chartColor },];
			this.loginChartLabels = this.yearKeys;
			this.createLeftChartAsLine(this.loginChartLabels, this.loginChartData);
		}
		
		if(period === '3months') {
			this.loginChartData = [{ data: this.threeMonthValues, label: 'Logins', backgroundColor: this.chartColor },];
			this.loginChartLabels = this.threeMonthKeys;
			this.createLeftChartAsLine(this.loginChartLabels, this.loginChartData);	
		}
		
		if(period === 'week') {
			this.loginChartData = [{ data: this.weekValues, label: 'Logins', backgroundColor: this.chartColor },];
			this.loginChartLabels = this.weekKeys;
			this.createLeftChartAsLine(this.loginChartLabels, this.loginChartData);
		}
		
	}

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

					this.listingViewsWeekKeys 		= listingData.viewsPerWeek.map(aa => aa.bucketName);
					this.listingViewWeekValues 		= listingData.viewsPerWeek.map(aa => aa.count);
					
					this.listingsChartData = [{label: "Job Board Views", data: this.listingViewWeekValues, backgroundColor: this.chartColor}];
					this.listingsChartLabels = this.listingViewsWeekKeys; 
					
				}, err => {
					console.log("ERR " + JSON.stringify(err));
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
			
			this.loginsUserIdsToday 		= Array.from(statsObj.getEventsTodayKeys());
			this.loginsToday 				= statsObj.getEventsTodayValues();
			
			this.loginsUserIdsWeek 			= Array.from(statsObj.getEventsWeekKeys());
			this.loginsWeek 				= statsObj.getEventsWeekValues();
			
			this.yearKeys 					= Array.from(statsObj.getYearStatsKeysAsStrings());
			this.yearValues 				= statsObj.getYearStatsValues();
			
			this.threeMonthKeys 			= Array.from(statsObj.getThreeMonthStatsKeysAsStrings());
			this.threeMonthValues 			= statsObj.getThreeMonthStatsValues();
			
			this.weekKeys 					= Array.from(statsObj.getWeekStatsKeysAsStrings());
			this.weekValues 				= statsObj.getWeekStatsValues();
			
			this.loginChartData = [{label: "Logins", data: this.yearValues, backgroundColor: this.chartColor}];
			this.loginChartLabels = this.yearKeys;
			this.createLeftChartAsLine(this.loginChartLabels, this.loginChartData);
			
			this.recruiterLoginsChartData = [{label: "Todays Logins", data: this.loginsToday, backgroundColor: this.chartColor}];
			this.recruiterLoginsChartLabels = this.loginsUserIdsToday; 
			this.createRightChart(this.recruiterLoginsChartLabels, this.recruiterLoginsChartData);
			
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
			
			this.recruiterDownloadsDaily 			= Object.values(stat.recruiterDownloadsDaily);
			this.recruiterDownloadsWeekly 			= Object.values(stat.recruiterDownloadsWeekly);
	
			this.recruiterDownloadsDailyCols 		= Object.keys(stat.recruiterDownloadsDaily);
			this.recruiterDownloadsWeeklyCols 		= Object.keys(stat.recruiterDownloadsWeekly);
			
			this.downloadsChartData = [{label: "Downloads", data: this.downloads, backgroundColor: this.chartColor}];
			this.downloadsChartLabels = this.downloadKeys;
			
			this.recruiterDownloadsChartData = [{label: "Todays Downloads", data: this.recruiterDownloadsDaily, backgroundColor: this.chartColor}];
			this.recruiterDownloadsChartLabels = this.recruiterDownloadsDailyCols; 
			
			this.chartDownloadsTotal 				= this.recruiterDownloadsDaily.length;

    	});

		this.statisticsService.getTotalNumberOfActiceCandidatesStatistics().forEach(count => {

			this.totalNumberActiveCandidates = count;

    	});

		this.statisticsService.getAvailableCandidatesByFunctionStatistics().forEach(data => {
		
			let stats:any[] = data;
			
			let functionStatCount:Array<string> 		= new Array<string>(); 
			let functionStatName:Array<string> 			= new Array<string>(); 
				
			stats.forEach(functonStat => {
				
				functionStatName.push(functonStat.function);
				functionStatCount.push(functonStat.availableCandidates);
				
			});
			
			this.availabilityChartData = [{label: "Todays Logins", data: functionStatCount, backgroundColor: this.chartColor}];
			this.availabilityChartLabels = functionStatName; 
			
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
				this.showNewCandidates=false;
				this.showMarketplaceStats=false;
				
				this.loginChartData = [{ data: this.yearValues, label: 'Logins', backgroundColor: this.chartColor },];
				this.loginChartLabels = this.yearKeys;
				this.createLeftChartAsLine(this.loginChartLabels, this.loginChartData);
				this.createRightChart(this.recruiterLoginsChartLabels, this.recruiterLoginsChartData);
				break;
			}
			case "downloads":{
				this.showStatsLogins=false;
				this.showStatsDownloads=true;
				this.showStatsAvailability=false;
				this.showStatsListings=false;
				this.showNewCandidates=false;
				this.showMarketplaceStats=false;
				 
				this.createLeftChartAsLine(this.downloadsChartLabels, this.downloadsChartData);
				this.createRightChart(this.recruiterDownloadsChartLabels, this.recruiterDownloadsChartData);
			
				break;
			}
			case "listings":{
				this.showStatsLogins=false;
				this.showStatsDownloads=false;
				this.showStatsAvailability=false;
				this.showStatsListings=true;
				this.showNewCandidates=false;
				this.showMarketplaceStats=false;
				
				this.createLeftChartAsLine(this.listingsChartLabels, this.listingsChartData);
				
				if(this.rightChart){
  					this.rightChart.destroy();
  				}
				break;
			}
			case "availability":{
				this.showStatsLogins=false;
				this.showStatsDownloads=false;
				this.showStatsListings=false;
				this.showStatsAvailability=true;
				this.showNewCandidates=false;
				this.showMarketplaceStats=false;
				this.createLeftChart(this.availabilityChartLabels, this.availabilityChartData);
				
				if(this.rightChart){
  					this.rightChart.destroy();
  				}
				break;
			}
			case "email":{
				this.showStatsLogins=false;
				this.showStatsDownloads=false;
				this.showStatsListings=false;
				this.showStatsAvailability=false;
				this.showNewCandidates=false;
				this.showMarketplaceStats=false;
				break;
			}
			case "newCandidateStats":{
				this.showStatsLogins=false;
				this.showStatsDownloads=false;
				this.showStatsListings=false;
				this.showStatsAvailability=false;
				this.showNewCandidates=true;
				this.showMarketplaceStats=false;
				if(this.leftChart){
  					this.leftChart.destroy();
  				}
  				if(this.rightChart){
  					this.rightChart.destroy();
  				}
				break;
			}
			case "marketplace":{
				this.showStatsLogins=false;
				this.showStatsDownloads=false;
				this.showStatsListings=false;
				this.showStatsAvailability=false;
				this.showNewCandidates=false;
				this.showMarketplaceStats=true;
				
				this.createLeftChart(this.marketplaceChartLabels, this.marketplaceChartData);
				
				if(this.rightChart){
  					this.rightChart.destroy();
  				}
				break;
			}
		}
		
	}
  	
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

	/**
	* Gets country code for the country 
	*/
	public countryToCode(country:string):string{
		return this.candidateService.getCountryCode(country);
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
	        aspectRatio:2.5
	      }
	      
	    });
	   
  	}
  	
  	/**
	* I know but ive had enough of charts. I will refactor it later. 
	*/
  	createLeftChartAsLine(leftChartLabels:string[], leftChartData:any){
		
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
	        aspectRatio:1.75
	      }
	      
	    });
	   
  	}

	createRightChart(chartLabels:string[], chartData:any){
		
  		if(this.rightChart){
  			this.rightChart.destroy();
  		}
  		
	    this.rightChart = new Chart("rightChart", {
	      type: 'bar', 
	
	      data: {
	        labels: chartLabels, 
		       datasets: chartData
	      },
	      options: {
	        aspectRatio:1.75
	      }
	      
	    });
	    
  	}
  	
  	public getIso2Code(countryCode:string):string{
		  
		let country:SupportedCountry = this.supportedCountries.filter(c => c.name == countryCode)[0];
		
		if (!country) {
			return 'xx';
		}
		
		return country.iso2Code;
	}

}