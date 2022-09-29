import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-recruiter-marketplace',
  templateUrl: './recruiter-marketplace.component.html',
  styleUrls: ['./recruiter-marketplace.component.css']
})
export class RecruiterMarketplaceComponent implements OnInit {

  	constructor() { }

	ngOnInit(): void {
 	}

	currentTab:string 					= "downloads";
	showSupply:boolean			= true;
	showDemand:boolean			= false;
	
	/**
	* Switches to the selected tab
	*/
	public switchTab(tab:string){
		
		this.currentTab = tab;
		
		switch(tab){
			case "showSupply":{
				this.showSupply=true;
				this.showDemand=false;
				break;
			}
			case "showDemand":{
				this.showSupply=false;
				this.showDemand=true;
				break;
			}
		}
		
	}

}
