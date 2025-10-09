import { Component, OnInit } 								from '@angular/core';
import { StatisticsService } 								from '../statistics.service';
import { NewCandidateSummaryItem } 							from '../new-candidate';
import { NewCandidateStatItem } 							from '../new-candidate-stat';
import { Router}											from '@angular/router';
import { LoginStats } 										from '../login-event-stats';
import { RecruiterSearchStatistics } 						from '../recruiter-search-stats';
import { CandidateFunction } 								from '../candidate-function';
import { CandidateServiceService }							from '../candidate-service.service';
import { GeoZone } 											from '../geo-zone';
import { Chart } 											from 'chart.js';
import { SupportedCountry } 								from '../supported-candidate';

@Component({
    selector: 'app-statistics',
    templateUrl: './statistics.component.html',
    styleUrls: ['./statistics.component.css','./statistics.component-mob.css'],
    standalone: false
})
export class StatisticsComponent implements OnInit {

	public chartColor:string = 'grey';
	public unavailableChartColor:string = 'purple';
	public totalNumberActiveCandidates:number 			= 0;
	public candidatesByFunction:Map<string,number> 		= new Map<string,number>();
	public currentTab:string 							= "downloads";
	public showStatsLogins:boolean						= true;
	public showStatsDownloads:boolean					= false;
	public showStatsViews:boolean						= false;
	public showStatsAvailability:boolean				= false;
	public showStatsAvailabilityByFunction				=false;
	public showStatsListings:boolean					= false;
	public showNewCandidates:boolean					= false;
	public showMarketplaceStats:boolean					= false;
	public showSearches:boolean							= false;
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
	public availabilityByFunctionChartLabels				= [''];
	public loginChartLabels 							= [''];
	public recruiterLoginsChartLabels 					= [''];
	public downloadsChartLabels 						= [''];
	public recruiterDownloadsChartLabels 				= [''];
	public profileViewChartLabels 						= [''];
	
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
	private profileViews:string[] 								= new Array<string>();
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
	private availabilityByFunctionChartData 					= [{label: "",data: [''],backgroundColor: ''}];
	private recruiterLoginsChartData 							= [{label: "",data: [''],backgroundColor: ''}];
	private downloadsChartData 									= [{label: "",data: [''], backgroundColor: ''}];
	private viewsChartData 										= [{label: "",data: [''], backgroundColor: ''}];
	private recruiterDownloadsChartData 						= [{label: "", data: [''], backgroundColor: ''}];

	public statChartDivMobile:string 							= '';
	public isMobile:boolean 									= false;
	public searchsSelectedUser:string							= "";
	public getFunctionLabel(type:string){
		
		if(type == null){
			return "";
		}
		
		return this.candidateFunctions.filter(f => f.id == type)[0].desc;
	}
	
  	public marketPlaceEventsChartOptions = {
    	responsive: true,
		maintainAspectRatio:false
  	};

  	/**
	* Toggles between the chart 
	* and table views
	* @param view - chart | table
	*/
  	public switchUserStatsView(view:string, selectedUserId:any = ""):void{
		  this.loginChartViewUser 	= selectedUserId.toString();
		  this.searchsSelectedUser 	= selectedUserId;
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
		this.initGeoZones();
		this.initSupportedCountries();
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
			this.marketplaceViewsKeys			= this.marketplaceViewsDayKeys
			this.marketpalceViewsValues 		= this.marketpalceViewsDayValues;
				
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
	}

	public switchUserStats(period:string):void{
		
		if(period === 'today') {
			this.recruiterLoginsChartData = [{ data: this.loginsToday, label: 'Todays logins', backgroundColor: this.chartColor  }]; 
	        this.recruiterLoginsChartLabels  = this.loginsUserIdsToday; 
	        this.loginCountRecruiter = this.loginCountRecruiterToday;
			this.loginCountCandidate = this.loginCountCandidateToday;
			this.recruiterStatPeriod = "DAY";
			this.createLeftChart(this.recruiterLoginsChartLabels, this.recruiterLoginsChartData);	
		}
		
		if(period === 'week') {
			this.recruiterLoginsChartData = [{ data: this.loginsWeek, label: 'Weeks logins' ,backgroundColor: this.chartColor  }]; 
	        this.recruiterLoginsChartLabels  = this.loginsUserIdsWeek; 	
	        this.loginCountRecruiter = this.loginCountRecruiterWeek;
			this.loginCountCandidate = this.loginCountCandidateWeek;
			this.recruiterStatPeriod = "WEEK";
			this.createLeftChart(this.recruiterLoginsChartLabels, this.recruiterLoginsChartData);
		}
		
	}
	
	public switchSearchStats(period:string):void{
			
		this.searchsSelectedUser = "";
	
		if (period === 'today') {
			this.recruiterLoginsChartData = [{ data: this.loginsToday, label: 'Todays logins', backgroundColor: this.chartColor  }]; 
	        this.recruiterLoginsChartLabels  = this.loginsUserIdsToday; 
	        this.loginCountRecruiter = this.loginCountRecruiterToday;
			this.loginCountCandidate = this.loginCountCandidateToday;
			this.recruiterStatPeriod = "DAY";
		}
		
		if (period === 'week') {
			this.recruiterLoginsChartData = [{ data: this.loginsWeek, label: 'Weeks logins' ,backgroundColor: this.chartColor  }]; 
	        this.recruiterLoginsChartLabels  = this.loginsUserIdsWeek; 	
	        this.loginCountRecruiter = this.loginCountRecruiterWeek;
			this.loginCountCandidate = this.loginCountCandidateWeek;
			this.recruiterStatPeriod = "WEEK";
		}
		
	}
	
	public fetchStatus():void{
		
		this.getCandidatesByFunction();
		this.getCandidatesFunctionAvailabilityByCountry();
		
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
			
			this.threeMonthKeys 			= Array.from(statsObj.getThreeMonthStatsKeysAsStrings());
			this.threeMonthValues 			= statsObj.getThreeMonthStatsValues();
			
			this.weekKeys 					= Array.from(statsObj.getWeekStatsKeysAsStrings());
			this.weekValues 				= statsObj.getWeekStatsValues();
			
			this.recruiterLoginsChartData = [{label: "Todays Logins", data: this.loginsToday, backgroundColor: this.chartColor}];
			this.recruiterLoginsChartLabels = this.loginsUserIdsToday; 
			
			this.createLeftChart(this.recruiterLoginsChartLabels, this.recruiterLoginsChartData);
			if (this.rightChart){
				this.rightChart.destroy();
			}
			
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
		
		
		//START
		this.statisticsService.getProfileViewsStatistics().subscribe(stats => {
			
			let labels:Array<string> 			= new Array<string>();
			let data:Array<string> 				= new Array<string>();
			stats.forEach(stat => {
				labels.push(stat.bucketId);
				data.push(""+stat.count);
			});
		
			this.profileViewChartLabels 		= labels;
			this.profileViews 			= data;
			this.viewsChartData = [{label: "Candidate Profile Views", data: this.profileViews, backgroundColor: this.chartColor}];
		});
		//END

		
	}
	
	private getCandidatesByFunction() {
		
		this.statisticsService.getAvailableCandidatesByFunctionStatisticsWithFilters(this.selectedFilterGeoZone,this.selectedFilterCountry).forEach(data => {
		
					let stats:any[] = data;
					
					let functionStatCount:Array<string> 		= new Array<string>(); 
					let unavailablefunctionStatCount:Array<string> 		= new Array<string>(); 
					let functionStatName:Array<string> 			= new Array<string>(); 
						
					stats.forEach(functonStat => {
						
						functionStatName.push(functonStat.function);
						functionStatCount.push(functonStat.availableCandidates);
						unavailablefunctionStatCount.push(functonStat.unavailableCandidates);
					});
					
					this.availabilityChartData = [{label: "Candidates available by function", data: functionStatCount, backgroundColor: this.chartColor}, {label: "Candidates unavailable by function", data: unavailablefunctionStatCount, backgroundColor: this.unavailableChartColor}];
					this.availabilityChartLabels = functionStatName; 
					
					if (this.currentTab == "availability") {
						if (this.leftChart) {
							this.leftChart.destroy();
						}
						
						this.createLeftChart(this.availabilityChartLabels, this.availabilityChartData);
					}
		    	});
				
	}
	
	private getCandidatesFunctionAvailabilityByCountry() {
			
		let functionFilter:string = "JAVA_DEV";
		
		if(this.selectedFunction) {
			functionFilter = this.selectedFunction.id;
		}
		
		this.statisticsService.getCandidateAvailabilityByCountryForFunction(functionFilter).forEach(data => {
		
					let stats:any[] = data;
					
					let countryStatCount:Array<string> 				= new Array<string>(); 
					let unavailableCountryStatCount:Array<string> 	= new Array<string>(); 
					let countryStatName:Array<string> 				= new Array<string>(); 
						
					stats.forEach(countryStat => {
						countryStatName.push(countryStat.id);
						countryStatCount.push(countryStat.available);
						unavailableCountryStatCount.push(countryStat.unavailable);
					});
					
					this.availabilityByFunctionChartData = [{label: "Candidates available by country", data: countryStatCount, backgroundColor: this.chartColor}, {label: "Candidates unavailable by country", data: unavailableCountryStatCount, backgroundColor: this.unavailableChartColor}];
					this.availabilityByFunctionChartLabels = countryStatName; 
				
						if (this.currentTab == "availability-by-function") {
						if (this.leftChart) {
							this.leftChart.destroy();
						}
						this.createLeftChartHorizontal(this.availabilityByFunctionChartLabels, this.availabilityByFunctionChartData);
					}
					
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
				this.createLeftChart(this.recruiterDownloadsChartLabels, this.recruiterDownloadsChartData);
				return;
			}
			case "week":{
				this.recruiterDownloadsChartData = [{ data: this.recruiterDownloadsWeekly, label: 'This Weeks downloads' ,backgroundColor: this.chartColor   }];
				this.recruiterDownloadsChartLabels = this.recruiterDownloadsWeeklyCols;
				this.chartDownloadsTotal = this.recruiterDownloadsWeekly.length;
				this.createLeftChart(this.recruiterDownloadsChartLabels, this.recruiterDownloadsChartData);
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
				this.showStatsViews=false;
				this.showStatsAvailability=false;
				this.showStatsAvailabilityByFunction=false;
				this.showStatsListings=false;
				this.showNewCandidates=false;
				this.showMarketplaceStats=false;
				this.showSearches=false;
				this.createLeftChart(this.recruiterLoginsChartLabels, this.recruiterLoginsChartData);
				if (this.rightChart){
					this.rightChart.destroy();
				}
				break;
			}
			case "downloads":{
				this.showStatsLogins=false;
				this.showStatsDownloads=true;
				this.showStatsViews=false;
				this.showStatsAvailability=false;
				this.showStatsAvailabilityByFunction=false;
				this.showStatsListings=false;
				this.showNewCandidates=false;
				this.showMarketplaceStats=false;
				this.showSearches=false;
				this.createLeftChart(this.recruiterDownloadsChartLabels, this.recruiterDownloadsChartData);
				this.createRightChartAsLine(this.downloadsChartLabels, this.downloadsChartData);
				
				break;
			}
			case "views":{
							this.showStatsLogins=false;
							this.showStatsDownloads=false;
							this.showStatsViews=true;
							this.showStatsAvailability=false;
							this.showStatsAvailabilityByFunction=false;
							this.showStatsListings=false;
							this.showNewCandidates=false;
							this.showMarketplaceStats=false;
							this.showSearches=false;
							this.createLeftChartAsLine(this.profileViewChartLabels, this.viewsChartData);
							if (this.rightChart){
								this.rightChart.destroy();
							}

							break;
						}
			case "listings":{
				this.showStatsLogins=false;
				this.showStatsDownloads=false;
				this.showStatsViews=false;
				this.showStatsAvailability=false;
				this.showStatsAvailabilityByFunction=false;
				this.showStatsListings=true;
				this.showNewCandidates=false;
				this.showMarketplaceStats=false;
				this.showSearches=false;
				this.createLeftChartAsLine(this.listingsChartLabels, this.listingsChartData);
				
				if(this.rightChart){
  					this.rightChart.destroy();
  				}
				break;
			}
			case "availability":{
				this.showStatsLogins=false;
				this.showStatsDownloads=false;
				this.showStatsViews=false;
				this.showStatsListings=false;
				this.showStatsAvailability=true;
				this.showStatsAvailabilityByFunction=false;
				this.showNewCandidates=false;
				this.showMarketplaceStats=false;
				this.createLeftChart(this.availabilityChartLabels, this.availabilityChartData);
				
				if(this.rightChart){
  					this.rightChart.destroy();
  				}
				break;
			}
			case "availability-by-function": {
				this.showStatsLogins=false;
				this.showStatsDownloads=false;
				this.showStatsViews=false;
				this.showStatsListings=false;
				this.showStatsAvailability=false;
				this.showStatsAvailabilityByFunction=true;
				this.showNewCandidates=false;
				this.showMarketplaceStats=false;
				this.createLeftChartHorizontal(this.availabilityByFunctionChartLabels, this.availabilityByFunctionChartData);
				
				if(this.rightChart){
  					this.rightChart.destroy();
  				}
				break;
			}
			case "email":{
				this.showStatsLogins=false;
				this.showStatsDownloads=false;
				this.showStatsViews=false;
				this.showStatsListings=false;
				this.showStatsAvailability=false;
				this.showStatsAvailabilityByFunction=false;
				this.showNewCandidates=false;
				this.showMarketplaceStats=false;
				this.showSearches=false;
				break;
			}
			case "newCandidateStats":{
				this.showStatsLogins=false;
				this.showStatsDownloads=false;
				this.showStatsViews=false;
				this.showStatsListings=false;
				this.showStatsAvailability=false;
				this.showStatsAvailabilityByFunction=false;
				this.showNewCandidates=true;
				this.showMarketplaceStats=false;
				this.showSearches=false;
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
				this.showStatsViews=false;
				this.showStatsListings=false;
				this.showStatsAvailability=false;
				this.showStatsAvailabilityByFunction=false;
				this.showNewCandidates=false;
				this.showMarketplaceStats=true;
				this.showSearches=false;
				this.createLeftChart(this.marketplaceChartLabels, this.marketplaceChartData);
				
				if(this.rightChart){
  					this.rightChart.destroy();
  				}
				break;
			}
			case "searches":{
				this.showStatsLogins=false;
				this.showStatsDownloads=false;
				this.showStatsViews=false;
				this.showStatsListings=false;
				this.showStatsAvailability=false;
				this.showStatsAvailabilityByFunction=false;
				this.showNewCandidates=false;
				this.showMarketplaceStats=false;
				this.showSearches=true;
				if(this.leftChart){
  					this.leftChart.destroy();
  				}
  				if(this.rightChart){
  					this.rightChart.destroy();
  				}
				this.searchsSelectedUser 	= "";
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
	        aspectRatio:1.75,
			responsive:true,
			maintainAspectRatio:false
	      }
	      
	    });
	   
  	}
	
	createLeftChartHorizontal(leftChartLabels:string[], leftChartData:any){
			
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
		        aspectRatio:1.75,
				indexAxis: 'y',
				responsive:true,
				maintainAspectRatio:false
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
	        aspectRatio:1.75,
			responsive:true,
			maintainAspectRatio:false
	      }
	      
	    });
	   
  	}
	
	/**
	* I know but ive had enough of charts. I will refactor it later. 
	*/
	createRightChartAsLine(rightChartLabels:string[], rightChartData:any){
		
		if (this.rightChart) {
			this.rightChart.destroy();
		}
		
	    this.rightChart = new Chart("rightChart", {
	      type: 'line',

	      data: {
	        labels: rightChartLabels, 
		       datasets: rightChartData
	      },
	      options: {
	        aspectRatio:1.75,
			responsive:true,
			maintainAspectRatio:false
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
	
	//START AVAILABILITY CHART TO PORT TO NEW COMPONENT
	public showGeoZoneFilters:string							= "";
	public filterView:string									= 'collapsed';
	public geoZones:Array<GeoZone>								= new Array<GeoZone>();
	public showCountryFilters:string							= "";
	public showLocationFilters:string							= "";
	
	
	/**
	* Swithches between open and closed filter view for GeoZones 
	*/
	public switchGeoZoneFilterView(view:string):void{
		this.showGeoZoneFilters = view;
	}
	
	/**
	* Returns whether Candidates from the selected GeoZone are currently
	* included in the Suggestion results
	*/
	public includeResultsForGeoZone(geoZone:GeoZone):boolean{
		return this.selectedFilterGeoZone == geoZone.geoZoneId;
	}
	
	/**
	* Toggles GeoCode
	*/
	public toggleGeoZoneSelection(geoZone:GeoZone):void{
		
		if (this.selectedFilterGeoZone == geoZone.geoZoneId) {
			this.selectedFilterGeoZone = "";
			this.getCandidatesByFunction();
			return;
		}
		
		this.selectedFilterGeoZone = geoZone.geoZoneId;
		this.selectedFilterCountry = "";
		this.getCandidatesByFunction();
		
	}	
	
	/**
	*  
	*/
	private initGeoZones():void{
		
		this.geoZones = this.candidateService.getGeoZones();
	
		this.showGeoZoneFilters 	= "";
		this.showLocationFilters 	= "";
		
	}
	
	public selectedFilterCountry:string = "";
	public selectedFilterGeoZone:string = "";
	
	/**
	* Toggles whether Candidates from selected country 
	* should be included in the Suggestions 
	* @param country - Country to toggle
	*/
	public toggleCountrySelection(country:string):void{
		
		if (this.selectedFilterCountry == country) {
			this.selectedFilterCountry = "";
			this.getCandidatesByFunction();
			return;
		}
		
		this.selectedFilterCountry = country;
		this.selectedFilterGeoZone = "";
		this.getCandidatesByFunction();
				
	}
	
	/**
	* Swithches between open and closed filter view for GeoZones 
	*/
	public switchCountriesFilterView(view:string):void{
		this.showCountryFilters = view;
	}
	
	/**
	* Returns whether Candidates from the selected country are currently
	* included in the Suggestion results
	*/
	public includeResultsForCountry(country:string):boolean{
		return this.selectedFilterCountry == country;
	}
	
	private initSupportedCountries():void{
		this.supportedCountries = this.candidateService.getSupportedCountries();
	}
	
	/**
	* Toggles between displaying and hiding of the filters
	*/
	public toggleFilters():void{
		
		if (this.filterView == 'collapsed'){
			this.filterView = 'expanded';
		} else {
			this.filterView = 'collapsed';
		}
		
	}
	//END

	public selectedFunction:CandidateFunction|null = new CandidateFunction("JAVA_DEV", "na");
	
	public setFunctionType(functionType:CandidateFunction):void{
		this.selectedFunction = functionType;
		this.getCandidatesFunctionAvailabilityByCountry();
	}

	public isSelectedFunction(funcType:CandidateFunction):boolean {
		
		if (this.selectedFunction && this.selectedFunction == funcType) {
			return true;
		} else  {
			return false;	
		}
		
	}
	
}