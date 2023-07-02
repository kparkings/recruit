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
	public	selectedListing:string								= "all";

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
		//TODO:
	}
	
}