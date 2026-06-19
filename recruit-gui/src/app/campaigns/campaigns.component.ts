import { Component, ViewChild, ElementRef }									from '@angular/core';
import { SelectionboxComponent} 								from '../campaigns/selectionbox/selectionbox.component'
import { CampaingsService, Campaign, Role, Participation}						from 'src/app/campaings.service';
import { UntypedFormControl, UntypedFormGroup } from '@angular/forms';

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
	public errMshActive:boolean = false
	public participation:Participation | undefined;
	
	/**
	* Constructor
	* @oaram campaignService - Services and Domain objects for Campaigns 
	*/
	public constructor(private readonly campaignService:CampaingsService) {}
	
	public newParticipantForm:UntypedFormGroup = new UntypedFormGroup({
		userId: 		new UntypedFormControl(),
		role: 			new UntypedFormControl(),
	});
	
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
		this.errMshActive = false;
		this.participantDialogBox.nativeElement.showModal();
	}
	
	/**
	* Closes the Add Participant dialog box 
	*/
	public handleCancelAddParticipant():void{
		this.participantDialogBox.nativeElement.close();
	}
	
	/**
	* Returns Participations for the current Campaign. Exludes Role level 
	* Participations
	*/
	public getCampaignLevelParticipation():Array<Participation>{
		
		
		if (this.campaign) {
			return this.campaign?.participations.filter(p => p.roleId == undefined);	
		}
		
		return new Array<Participation>();
		
	}
	
	/**
	* Sets the current Participation that has been selected or de-selects it in the case 
	* the Participation was already selected
	*/
	public selectParticipation(participation:Participation):void{
		if (this.participation == participation) {
			this.participation = undefined;
		} else {
			this.participation = participation;
		}
	}
	
	/**
	* Returns the correct CSS class to highlight the Participation in the view if it 
	* has been selected
	*/
	public getSelectedParticipantCSSClass(participation:Participation):string{
		if (this.participation == undefined) {
			return "";
		}
		
		if(this.participation === participation) {
			return "participant-selected";
		}
		
		return"";
		
		
	}
	
	/**
	* Handles the request to create a new Participant
	*/
	public handleAddParticipant():void{
		
		let userId:string 				= this.newParticipantForm.get("userId")?.value;
		let participationType:string 	= this.newParticipantForm.get("role")?.value;
		let role:string 				= this.role ? ''+this.role?.id : '';
		this.errMshActive 				= false;
			
		this.campaignService.addParticipation(userId, ''+this.campaign?.id, role, participationType)
			.subscribe(res => {
				this.newParticipantForm = new UntypedFormGroup({
						userId: 		new UntypedFormControl(),
						role: 			new UntypedFormControl(),
					});
				this.handleCancelAddParticipant();
				this.selectionBox.refreshCampaign();
		
			
			}, err => {
				this.errMshActive = true;
			});
		
	}
	
	/**
	* Sends request to delete Participation
	*/
	public deleteParticipation():void{
		if (this.participation !== undefined) {
			this.campaignService.deleteParticipation(this.participation.participationId).subscribe(res => {
				this.selectionBox.refreshCampaign();

			});
		}
	}
	
	public currentUserAdminForSelectedObject():boolean {
		
		let currentUser = sessionStorage.getItem("userId");
		
		if (this.role) {
			
			if (this.role.participations.filter(p => p.type == "ADMIN" && p.contact.contactId == currentUser).length > 0){
				return true;
			} 
			
		} else if (this.campaign && this.campaign.participations.filter(p => p.type == "ADMIN" && p.contact.contactId == currentUser).length > 0){
			return true;	
		}	
		
		return false;
	}
	
}