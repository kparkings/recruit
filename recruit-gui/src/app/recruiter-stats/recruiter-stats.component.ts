import { Component } 									from '@angular/core';
import { RecruiterListingStatistics, ListingStat } 		from '../recruiter-listing-statistics';
import { StatisticsService } 							from '../statistics.service';
import { ChartData}										from './chart-data';
import { Label, SingleLineLabel } 										from 'ng2-charts';

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
	public	selectedListing:string								= "all";
	public	currentTab:string									= "general-stats";
	public totalNumberActiveCandidates:number 					= 0;

	/**
	* Constructor
	* @param statisticsService - Services relating to statistics
	*/
	public constructor(public statisticsService:StatisticsService){
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
    	});
    	this.statisticsService.getAvailableCandidatesByFunctionStatistics().forEach(data => {
		
			let stats:any[] = data;
			
			let functionStatCount:Array<number> 		= new Array<number>(); 
			let functionStatName:Array<string> 			= new Array<string>(); 
				
			stats.forEach(functonStat => {
				
				functionStatName.push(functonStat.function);
				functionStatCount.push(functonStat.availableCandidates);
				
			});
			
			this.availabilityChart.chartData	= [{ data: functionStatCount, label: 'Function' },];
			this.availabilityChart.chartLabels 	= functionStatName;
			
    	});
	}
	
	/**
	* Select the listing to display statistics for 
	*/
	public switchListing(listingId:string):void{
		this.selectedListing = listingId;
	
		let listingStat = this.listingStatistics.listingStats.filter(l => l.listingId == listingId)[0]
		let stats 		= listingStat.stats;
	
		this.listingViewsChart.chartData 	= [{ data: stats.map(s => s.count), label: listingStat.title },];
		this.listingViewsChart.chartLabels 	= stats.map(s => s.bucket.toString());
		
	}
	
	/**
	* Switches to the seelcted Tab
	* @param tabId - Id of Tab to display 
	*/
	public switchTab(tabId:string):void{
		this.currentTab = tabId;
	}
	
}