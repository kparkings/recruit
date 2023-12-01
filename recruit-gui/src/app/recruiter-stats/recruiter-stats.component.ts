import { Component } 									from '@angular/core';
import { RecruiterListingStatistics, ListingStat } 		from '../recruiter-listing-statistics';
import { StatisticsService } 							from '../statistics.service';
import { ChartData}										from './chart-data';
import { NgChartsModule } 								from 'ng2-charts';
import { Router}										from '@angular/router';
import { ChartType } from 'chart.js';

@Component({
  selector: 'app-recruiter-stats',
  templateUrl: './recruiter-stats.component.html',
  styleUrls: ['./recruiter-stats.component.css']
})
export class RecruiterStatsComponent {
	
	private recruiterId:string 									= "";
	public  listingStatistics:RecruiterListingStatistics 		= new RecruiterListingStatistics();
	public  listingViewsChart:ChartData 						= new ChartData("Post views");
	public  availabilityChart:ChartData 						= new ChartData("Candidate Availability");
	public  jobboardChart:ChartData 							= new ChartData("Jobboard Views");
	public	selectedListing:string								= "all";
	public	currentTab:string									= "general-stats";
	public 	totalNumberActiveCandidates:number 					= 0;
	public 	listingViewsToday:number 							= 0;
	public 	listingViewsThisWeek:number 						= 0;
	public  jobBoardType:ChartType = 'line';

	/**
	* Constructor
	* @param statisticsService - Services relating to statistics
	*/
	public constructor(public statisticsService:StatisticsService, private router:Router){
		this.refreshData();
	}

	/**
	* Fetches data from backend 
	*/	
	public refreshData():void{
		
		this.recruiterId = sessionStorage.getItem("userId") ? sessionStorage.getItem("userId")+"" : "";
		this.statisticsService.fetchRecruiterListingStats(this.recruiterId).subscribe(stats =>  {
			this.listingStatistics = Object.assign(new RecruiterListingStatistics(), stats);
			this.switchListing(this.selectedListing);
		});	
		
		this.statisticsService.getTotalNumberOfActiceCandidatesStatistics().forEach(count => {
			this.totalNumberActiveCandidates = count;
    	}).catch(err => {this.handleSessionEnded(err)});
    	
    	this.statisticsService.getAvailableCandidatesByFunctionStatistics().forEach(data => {
		
			let stats:any[] = data;
			
			let functionStatCount:Array<number> 		= new Array<number>(); 
			let functionStatName:Array<string> 			= new Array<string>(); 
				
			stats.forEach(functonStat => {
				
				functionStatName.push(functonStat.function);
				functionStatCount.push(functonStat.availableCandidates);
				
			});
			
			//this.availabilityChart.chartData	= [{ data: functionStatCount, label: 'Function' },];
			//this.availabilityChart.chartLabels 	= functionStatName;
			
    	}).catch(err => {this.handleSessionEnded(err)});;
    	
    	this.statisticsService.getListingStats().subscribe(listingData => {
					
					this.listingViewsToday 		= listingData.viewsToday;
					this.listingViewsThisWeek 	= listingData.viewsThisWeek;
					
					let listingChartViews:number[] 		= Object.values(listingData.viewsPerWeek);
					let listingChartViewsKeys:string[] 	= Object.keys(listingData.viewsPerWeek);
		
			//		this.jobboardChart.chartData = [{ data: listingChartViews, label: 'Downloads' },];
			//		this.jobboardChart.chartLabels = listingChartViewsKeys;
					
				}, err => { this.handleSessionEnded(err)});
	}
		
	/**
	* Select the listing to display statistics for 
	*/
	public handleSessionEnded(err:any):void{
		if (err.status === 401 || err.status === 0) {
			sessionStorage.removeItem('isAdmin');
			sessionStorage.removeItem('isRecruter');
			sessionStorage.removeItem('isCandidate');
			sessionStorage.removeItem('loggedIn');
			sessionStorage.setItem('beforeAuthPage', 'view-candidates');
			this.router.navigate(['login-user']);
		}
	}
	/**
	* Select the listing to display statistics for 
	*/
	public switchListing(listingId:string):void{
		this.selectedListing = listingId;
	
		let listingStat = this.listingStatistics.listingStats.filter(l => l.listingId == listingId)[0]
		let stats 		= listingStat.stats;
	
		//this.listingViewsChart.chartData 	= [{ data: stats.map(s => s.count), label: listingStat.title },];
		//this.listingViewsChart.chartLabels 	= stats.map(s => s.bucket.toString());
		
	}
	
	/**
	* Switches to the seelcted Tab
	* @param tabId - Id of Tab to display 
	*/
	public switchTab(tabId:string):void{
		this.currentTab = tabId;
	}
	
}