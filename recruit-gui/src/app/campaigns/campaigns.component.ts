import { Component, ViewChild }		from '@angular/core';
import { Campaign, Role  } 			from '../campaings.service';
import { SelectionboxComponent} 	from '../campaigns/selectionbox/selectionbox.component'

@Component({
  selector: 'app-campaigns',
  templateUrl: './campaigns.component.html',
  styleUrl: './campaigns.component.css',
  standalone:false
})
export class CampaignsComponent {
	
	@ViewChild(SelectionboxComponent) 			public selectionBox!:SelectionboxComponent;
	
	public campaign:Campaign | undefined;
	public role:Role | undefined;
	
	/**
	* When a new Campaign is selected an event is emmited. This is the 
	* handler for that emitted event 
 	*/
	public handleCampaignSelectedEmitterEvent(campaign:Campaign):void{
		this.campaign = campaign;
	}
	
	/**
	* When a new Role is selected an event is emmited. This is the 
	* handler for that emitted event 
	*/
	public handleRoleSelectedEmitterEvent(role:Role):void{
		this.role = role;
	}
	

}
