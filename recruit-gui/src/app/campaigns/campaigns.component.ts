import { Component, ViewChild, ElementRef }									from '@angular/core';
import { SelectionboxComponent} 								from '../campaigns/selectionbox/selectionbox.component'
import { CampaingsService, Campaign, Role}						from 'src/app/campaings.service';

@Component({
  selector: 'app-campaigns',
  templateUrl: './campaigns.component.html',
  styleUrl: './campaigns.component.css',
  standalone:false
})
export class CampaignsComponent {
	
	@ViewChild(SelectionboxComponent) 				public selectionBox!:SelectionboxComponent;
	@ViewChild('confirmDelete', {static:true})		public confirmDeleteBox!: ElementRef<HTMLDialogElement>;
	@ViewChild('addParticipant', { static: true }) 	public participantDialogBox!: ElementRef<HTMLDialogElement>;
		
	public campaign:Campaign | undefined;
	public role:Role | undefined;
	
	public constructor(private readonly campaignService:CampaingsService) {}
	
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
	
	/**
	* Shows the confirm Delete box 
	*/
	public showConfirmDeleteModal():void {
		this.confirmDeleteBox.nativeElement.showModal();
	}
	
	/**
	* Deletes the currently selected Campaign 
	*/
	private deleteCampaign():void{
		this.campaignService.deleteCampaign(''+this.campaign?.id).subscribe(result => {
			this.selectionBox.listCampaigns();
			this.confirmDeleteBox.nativeElement.close();
		});
	}

	/**
	* Deletes the currently selected Role 
	*/
	private deleteRole():void{
		this.campaignService.deleteRole(''+this.campaign?.id, ''+this.role?.id).subscribe(result => {
			this.selectionBox.listCampaigns();
			this.confirmDeleteBox.nativeElement.close();
		});		
	}
	
	/**
	* Closes the confirm delete box 
	*/
	public handleCancelDelete():void{
		this.confirmDeleteBox.nativeElement.close();
	}
	
	/**
	* Deletes the currently selected Campaign or Role 
	*/
	public handleConfirmDelete():void{
		
		if (this.role == undefined) {
			this.deleteCampaign();
		} else {
			this.deleteRole();
		}
		
	}
	
	/**
	* Opens dialog box to add a Participant to a Campaign 
	* or Role 
	*/
	public showAddParticipantBox():void{
		this.participantDialogBox.nativeElement.showModal();
	}
	
	/**
	* Closes the Add Participant dialog box 
	*/
	public handleCancelAddParticipant():void{
		this.participantDialogBox.nativeElement.close();
	}

}