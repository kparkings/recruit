import { Component, OnInit } 									from '@angular/core';
import { RecruiterListingStatistics, ListingStat } 		from '../recruiter-listing-statistics';
import { StatisticsService } 							from '../statistics.service';
import { ChartData}										from './chart-data';
import { NgChartsModule } 								from 'ng2-charts';
import { Router}										from '@angular/router';
import { Chart, ChartType } from 'chart.js';

@Component({
  selector: 'app-recruiter-stats',
  templateUrl: './recruiter-stats.component.html',
  styleUrls: ['./recruiter-stats.component.css']
})
export class RecruiterStatsComponent implements OnInit{
	
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

	listingViewsWeekKeys:string[] 			= new Array<string>();
	listingViewWeekValues:string[] 			= new Array<string>();
	

	/** Charts */
	public chartColor:string = 'grey';
	
	
	/**
	* Constructor
	* @param statisticsService - Services relating to statistics
	*/
	public constructor(public statisticsService:StatisticsService, private router:Router){
		//this.refreshData();
	}

	ngOnInit(): void {
		this.refreshDataGeneralStats();
		this.refreshDataRecruiterListingStats();
		
	}

	refreshDataGeneralStats():void{
		
		this.statisticsService.getTotalNumberOfActiceCandidatesStatistics().forEach(count => {
			this.totalNumberActiveCandidates = count;
    	}).catch(err => {this.handleSessionEnded(err)});
    	
    	this.statisticsService.getAvailableCandidatesByFunctionStatistics().forEach(data => {
		
			let stats:any[] = data;
			
			let functionStatCount:Array<string> 		= new Array<string>(); 
			let functionStatName:Array<string> 			= new Array<string>(); 
				
			stats.forEach(functonStat => {
				
				functionStatName.push(functonStat.function);
				functionStatCount.push(functonStat.availableCandidates);
				
			});
			
			//START
			this.availabilityChartData = [
	           	{
	            label: "Todays Logins",
	       	    data: functionStatCount,
	            backgroundColor: this.chartColor
	          },
	        	];
			this.availabilityChartLabels = functionStatName; 
			//this.switchTab(this.currentTab);
			//END
			
    	});
    	
    	this.statisticsService.getListingStats().subscribe(listingData => {
					
					this.listingViewsToday 		= listingData.viewsToday;
					this.listingViewsThisWeek 	= listingData.viewsThisWeek;
					
					this.listingViewsWeekKeys 		= listingData.viewsPerWeek.map(aa => aa.bucketName);
					this.listingViewWeekValues 		= listingData.viewsPerWeek.map(aa => aa.count);
					
					this.listingsChartData = [
			           	{
			            label: "Job Board Views",
			       	    data: this.listingViewWeekValues,
			            backgroundColor: this.chartColor
			          },
			        	];
					this.listingsChartLabels = this.listingViewsWeekKeys; 
				
					this.switchTab(this.currentTab);
					
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
    	
	}
	
	refreshData():void{
		if(this.currentTab == 'general-stats'){
			this.refreshDataGeneralStats();
		}else {
			this.refreshDataRecruiterListingStats();
		}
	}
	
	
	refreshDataRecruiterListingStats():void{
		
		this.recruiterId = sessionStorage.getItem("userId") ? sessionStorage.getItem("userId")+"" : "";
		this.statisticsService.fetchRecruiterListingStats(this.recruiterId).subscribe(stats =>  {
			this.listingStatistics = Object.assign(new RecruiterListingStatistics(), stats);
			this.switchListing(this.selectedListing);
			//this.switchTab(this.currentTab);
		});
		
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
	
		let labels:string[] = stats.map(s => ''+s.bucket);
		
		this.selectedListingsChartLabels = labels;
		
		let values = this.selectedListingsChartData = [
			           	{
			            label: "Listing View",
			       	    data: stats.map(s => ''+s.count),
			            backgroundColor: this.chartColor
			          },
			        	];
		
		this.createTopChart(labels, values);
		
	}
	
	/**
	* Switches to the seelcted Tab
	* @param tabId - Id of Tab to display 
	*/
	public switchTab(tabId:string):void{
		
		this.currentTab = tabId;
		
		if(this.topChart){
			this.topChart.destroy();
		}
		
		if(this.bottomChart){
			this.bottomChart.destroy();
		}
		
		if(this.currentTab == 'general-stats'){
			this.createTopChart(this.availabilityChartLabels, this.availabilityChartData);	
			this.createBottomChart(this.listingsChartLabels, this.listingsChartData);
			
		}else {
			this.selectedListing = '';
			this.createTopChart(this.availabilityChartLabels, this.availabilityChartData);	
			if(this.topChart){
				this.topChart.destroy();
			}
		}
		
	}
	
	availabilityChartData = [
           	{
            label: "Sales",
            data: ['467','576', '572', '79', '92',
								 '574', '573', '576'],
            backgroundColor: 'blue'
          },
        	];
	availabilityChartLabels = ['2022-05-10', '2022-05-11', '2022-05-12','2022-05-13','2022-05-14', '2022-05-15', '2022-05-16','2022-05-17', ];
	
	listingsChartData = [
	           	{
	            label: "Sales",
	            data: ['467','576', '572', '79', '92',
									 '574', '573', '576'],
	            backgroundColor: 'blue'
	          },
	        	];
	listingsChartLabels = ['2022-05-10', '2022-05-11', '2022-05-12','2022-05-13','2022-05-14', '2022-05-15', '2022-05-16','2022-05-17', ];
	
	selectedListingsChartData = [
	           	{
	            label: "Sales",
	            data: ['467','576', '572', '79', '92',
									 '574', '573', '576'],
	            backgroundColor: 'blue'
	          },
	        	];
	selectedListingsChartLabels = ['2022-05-10', '2022-05-11', '2022-05-12','2022-05-13','2022-05-14', '2022-05-15', '2022-05-16','2022-05-17', ];
	
	
	createTopChart(leftChartLabels:string[], topChartData:any){
		
  		if(this.topChart){
  			this.topChart.destroy();
  		}
  		
	    this.topChart = new Chart("topChart", {
	      type: 'bar', //this denotes tha type of chart
	
	      data: {// values on X-Axis
	        labels: leftChartLabels, 
		       datasets: topChartData
	      },
	      options: {
	        aspectRatio:2.5
	      }
	      
	    });
	   
  	}
	
	public topChart: any;
	public bottomChart: any;
	
	createBottomChart(chartLabels:string[], chartData:any){
		
  		if(this.bottomChart){
  			this.bottomChart.destroy();
  		}
  		
	    this.bottomChart = new Chart("bottomChart", {
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