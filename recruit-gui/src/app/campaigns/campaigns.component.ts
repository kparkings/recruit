import { Component, ViewChild, ElementRef }										from '@angular/core';
import { SelectionboxComponent} 												from '../campaigns/selectionbox/selectionbox.component'
import { CampaingsService, Campaign, Role, Participation, Candidate, Note}		from 'src/app/campaings.service';
import { UntypedFormControl, UntypedFormGroup } 								from '@angular/forms';
import { AppComponent } 														from 'src/app/app.component';
import { PublicChat}															from '../newsfeed/public-chat';


@Component({
  selector: 'app-campaigns',
  templateUrl: './campaigns.component.html',
  styleUrl: './campaigns.component.css',
  standalone:false
})
export class CampaignsComponent {
	
	@ViewChild(SelectionboxComponent) 					public selectionBox!:SelectionboxComponent;
	@ViewChild('confirmDelete', 	{static:true})		public confirmDeleteBox!: ElementRef<HTMLDialogElement>;
	@ViewChild('addNote', 			{static:true})		public addNoteBox!: ElementRef<HTMLDialogElement>;
	@ViewChild('addParticipant',	{ static: true }) 	public participantDialogBox!: ElementRef<HTMLDialogElement>;
		
	public campaign:Campaign | undefined;
	public role:Role | undefined;
	public note:Note | undefined;
	public errMshActive:boolean = false
	public participation:Participation | undefined;
	public candidate:Candidate | undefined;
	public showParticipants:boolean = false;
	public showNotes:boolean = false;
	public showCandidates:boolean = false;
	
	/**
	* Constructor
	* @oaram campaignService - Services and Domain objects for Campaigns 
	* @param appComponent	 - Ref to main app component
	*/
	public constructor(private readonly campaignService:CampaingsService,private readonly appComponent:AppComponent) {}
	
	public newParticipantForm:UntypedFormGroup = new UntypedFormGroup({
		userId: 		new UntypedFormControl(),
		role: 			new UntypedFormControl(),
	});
	
	public addNoteForm:UntypedFormGroup = new UntypedFormGroup({
		title: new UntypedFormControl(),
		text: new UntypedFormControl(),
	});
	
	/**
	* When a new Campaign is selected an event is emmited. This is the 
	* handler for that emitted event 
 	*/
	public handleCampaignSelectedEmitterEvent(campaign:Campaign):void{
		this.campaign = campaign;
		this.showParticipants = false;
	}
	
	/**
	* When a new Role is selected an event is emmited. This is the 
	* handler for that emitted event 
	*/
	public handleRoleSelectedEmitterEvent(role:Role):void{
		this.role = role;
		this.showParticipants = false;
	}
	
	/**
	* Shows the confirm Delete box 
	*/
	public showConfirmDeleteModal():void {
		this.confirmDeleteBox.nativeElement.showModal();
	}
	
	/**
	* Shows the addNote  box 
	*/
	public showAddNotModal():void {
		this.note = undefined;
		this.addNoteBox.nativeElement.showModal();
	}
	
	/**
	* Shows the addNote  box 
	*/
	public showNote(note:Note):void {
		this.note = note;
		this.addNoteBox.nativeElement.showModal();
	}
	
	/**
	* Closes the addNote  box 
	*/
	public closeAddNotModal():void {
		this.addNoteBox.nativeElement.close();
	}
	
	/**
	* Adds a new Note
	*/
	public handleAddNote():void{
		
		
		let title:string 				= this.addNoteForm.get("title")?.value;
		let text:string 				= this.addNoteForm.get("text")?.value;
		let roleId:string | undefined	= this.role !== undefined ? this.role.id : undefined;
		
		
		this.campaignService.addNote(''+this.campaign?.id, roleId, title, text).subscribe(res => {
			this.closeAddNotModal();	
			this.selectionBox.refreshCampaign();
		});
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
	* Returns Notes for the current Campaign. Exludes Role level 
	* Participations
	*/
	public getCampaignLevelNotes():Array<Note>{
		
		
		if (this.campaign) {
			return this.campaign?.notes.filter(n => n.roleId == undefined);	
		}
		
		return new Array<Note>();
		
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
	* Sets the current Candidate that has been selected or de-selects it in the case 
	* the Candiate was already selected
	*/
	public selectCandidate(candidate:Candidate):void{
		if (this.candidate == candidate) {
			this.candidate = undefined;
		} else {
			this.candidate = candidate;
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
	* Returns the correct CSS class to highlight the Candidate in the view if it 
	* has been selected
	*/
	public getSelectedCandidateCSSClass(candidate:Candidate):string{
		if (this.candidate == undefined) {
			return "";
		}
		
		if(this.candidate === candidate) {
			return "candidate-selected";
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

	/**
	* Sends request to delete Candidate
	*/
	public deleteCandidate():void{
		
		if (this.campaign == undefined) {
			return;
		}
		
		if (this.candidate !== undefined) {
			if (this.role == undefined) {
				this.campaignService.deleteCandidateFromCampaign(this.campaign.id, this.candidate.id).subscribe(res => {
					this.selectionBox.refreshCampaign();
				});
			} else {
				this.campaignService.deleteCandidateFromRole(this.campaign.id, this.role.id, this.candidate.id).subscribe(res => {
					this.selectionBox.refreshCampaign();
				});
			}
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
	
	/**
	* Opens Chat session with the owner of the Chat 
	*/
	public openChat(candidate:Candidate):void{
		this.appComponent.privateChat.showContactsItemView();
		this.appComponent.privateChat.openChat(candidate.id);	
	}
	
	/**
	* Toggles the display of the Participants section on the view
	*/
	public toggleShowParticipants():void{
		this.showParticipants = !this.showParticipants;
	}
	
	/**
	* Toggles the display of the Candidates section on the view
	*/
	public toggleShowCandidates():void{
		this.showCandidates = !this.showCandidates;
	}
	
	/**
	* Toggles the display of the Notes section on the view
	*/
	public toggleShowNotes():void{
		this.showNotes = !this.showNotes;
	}
	
}