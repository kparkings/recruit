import { Component, ViewChild, ElementRef, Input }										from '@angular/core';
import { DomSanitizer, SafeResourceUrl } 												from '@angular/platform-browser';
import { SelectionboxComponent} 														from '../campaigns/selectionbox/selectionbox.component'
import { CampaingsService, Campaign, Role, Participation, Candidate, Note, Document}	from 'src/app/campaings.service';
import { UntypedFormControl, UntypedFormGroup } 										from '@angular/forms';
import { AppComponent } 																from 'src/app/app.component';
import { environment } 								      							  	from './../../environments/environment';
import { CandidateServiceService }														from '../candidate-service.service';
import { SupportedCountry } 															from '../supported-candidate';

@Component({
  selector: 'app-campaigns',
  templateUrl: './campaigns.component.html',
  styleUrl: './campaigns.component.css',
  standalone:false
})
export class CampaignsComponent {
	
	@ViewChild(SelectionboxComponent) 							public selectionBox!:SelectionboxComponent;
	@ViewChild('confirmDelete', 			{static:true})		public confirmDeleteBox!: ElementRef<HTMLDialogElement>;
	@ViewChild('addNote', 					{static:true})		public addNoteBox!: ElementRef<HTMLDialogElement>;
	@ViewChild('addDocument', 				{static:true})		public addDocumentBox!: ElementRef<HTMLDialogElement>;
	@ViewChild('addParticipant',			{static:true }) 	public participantDialogBox!: ElementRef<HTMLDialogElement>;
	@ViewChild('massMessageBox',			{static:true}) 		public massMessageBox!: ElementRef<HTMLDialogElement>;
	@ViewChild('addExternalCandidateBox', 	{static:true}) 		public addExternalCandidateBox!: ElementRef<HTMLDialogElement>;
		
	@Input() 	trustedResourceUrl: 	SafeResourceUrl;
	
	public campaign:Campaign | undefined;
	public role:Role | undefined;
	public note:Note | undefined;
	public document:Document | undefined;
	public errMshActive:boolean 						= false
	public participation:Participation | undefined;
	//public candidate:Candidate | undefined;
	public showParticipants:boolean 					= false;
	public showNotes:boolean 							= false;
	public showDocuments:boolean 						= false;
	public showCandidates:boolean 						= false;
	public editNote:boolean								=false;
	public uploadedDocument:File | undefined;
	public showInlineCVView:boolean 					= false;
	public wrappedCandidates:Array<SelectableCandidate> = new Array<SelectableCandidate>();
	public countries:Array<SupportedCountry> 			= new Array<SupportedCountry>();
		
	
	/**
	* Constructor
	* @oaram campaignService - Services and Domain objects for Campaigns 
	* @param appComponent	 - Ref to main app component
	*/
	public constructor(private readonly campaignService:CampaingsService,private readonly appComponent:AppComponent, readonly sanitizer:DomSanitizer, private readonly candidateService: 			CandidateServiceService,) {
		this.trustedResourceUrl = this.sanitizer.bypassSecurityTrustResourceUrl('');
		this.countries 			= this.candidateService.getSupportedCountries();
	}
	
	public newParticipantForm:UntypedFormGroup = new UntypedFormGroup({
		userId: 		new UntypedFormControl(),
		role: 			new UntypedFormControl(),
	});
	
	public addNoteForm:UntypedFormGroup = new UntypedFormGroup({
		title: new UntypedFormControl(),
		text: new UntypedFormControl(),
	});
	
	public addDocumentForm:UntypedFormGroup = new UntypedFormGroup({
		title: new UntypedFormControl(),
	});
		
	public addMassMessageForm:UntypedFormGroup = new UntypedFormGroup({
		message: new UntypedFormControl(),
	})
	
	public newExternalCandidateForm:UntypedFormGroup = new UntypedFormGroup({
		firstName: new UntypedFormControl(),
		surname: new UntypedFormControl(),
		country: new UntypedFormControl(),
		JobTitle: new UntypedFormControl(),
		email: new UntypedFormControl(),
	});
	
	/**
	* When a new Campaign is selected an event is emmited. This is the 
	* handler for that emitted event 
 	*/
	public handleCampaignSelectedEmitterEvent(campaign:Campaign):void{
		this.campaign = campaign;
		this.showParticipants = false;
		this.getWrappedCandidates(campaign.candidates);
	}
	
	/**
	* When a new Role is selected an event is emmited. This is the 
	* handler for that emitted event 
	*/
	public handleRoleSelectedEmitterEvent(role:Role):void{
		this.role = role;
		this.showParticipants = false;
		this.getWrappedCandidates(role.candidates);
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
		
		this.addNoteForm = new UntypedFormGroup({
			title: new UntypedFormControl(),
			text: new UntypedFormControl(),
		});
		this.note = undefined;
		this.addNoteBox.nativeElement.showModal();
	}
	
	/**
	* Shows the addDocument  box 
	*/
	public showAddDocumentModal():void {
		this.addDocumentForm = new UntypedFormGroup({
			title: new UntypedFormControl(),
		});
		this.document = undefined;
		this.addDocumentBox.nativeElement.showModal();
	}
	
	/**
	* Shows the addNote  box 
	*/
	public showNote(note:Note):void {
		this.note = note;
		this.editNote = false;
		this.addNoteBox.nativeElement.showModal();
	}
	
	/**
	* Shows the addNote  box 
	*/
	public showDocument(document:Document):void {
		this.document = document;
		

	//	http://127.0.0.1:8080/curriculum-test/74.pdf
		let url = environment.backendUrl + 'campaign/document/'+document.id; 
		this.trustedResourceUrl = this.sanitizer.bypassSecurityTrustResourceUrl(url);
		this.showInlineCVView = true;
		
	}
	
	
	/**
	* Closes the addNote  box 
	*/
	public closeAddNotModal():void {
		this.addNoteBox.nativeElement.close();
	}

	/**
	* Closes the addDocument  box 
	*/
	public closeAddDocumentModal():void {
		this.addDocumentBox.nativeElement.close();
	}
	
	/**
	* Closes the Inline document view 
	*/		
	public closeDocument():void{
		this.showInlineCVView = false;
	}
	
	/**
	* Deletes the current document 
	*/
	public deleteDocument():void{
		
		if (!this.document){
			return;
		}
		
		this.campaignService.deleteDocument(this.document.id).subscribe(res => {
			this.closeDocument();
			this.selectionBox.refreshCampaign();
		});
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
	* Open the Mass message box
	*/
	public showMassMessageBox():void{
		this.errMshActive = false;
		this.massMessageBox.nativeElement.showModal();
	}
	
	/**
	* Opens dialogue box to add new external candidate.
	*/
	public showAddExternalCandidateBox():void{
		this.errMshActive = false;
		this.newExternalCandidateForm = new UntypedFormGroup({
				firstName: new UntypedFormControl(),
				surname: new UntypedFormControl(),
				country: new UntypedFormControl(),
				JobTitle: new UntypedFormControl(),
				email: new UntypedFormControl(),
			});
		this.addExternalCandidateBox.nativeElement.showModal();
	}
	
	/**
	* Closes the Add Participant dialog box 
	*/
	public handleCancelAddParticipant():void{
		this.participantDialogBox.nativeElement.close();
	}
	
	/**
	* Closes the massMessageBox box 
	*/
	public handleCancelMassMessageBox():void{
		this.massMessageBox.nativeElement.close();
	}
	
	/**
	* Closes the addExternalCandidateBox  
	*/
	public handleCancelAddExternalCandidateBox():void{
		this.addExternalCandidateBox.nativeElement.close();
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
	//public selectCandidate(candidate:Candidate):void{
	//	if (this.candidate == candidate) {
	//		this.candidate = undefined;
	//	} else {
	//		this.candidate = candidate;
	//	}
	//}
	
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
	//public getSelectedCandidateCSSClass(candidate:Candidate):string{
	//	if (this.candidate == undefined) {
	//		return "";
	//	}
	//	
	//	if(this.candidate === candidate) {
	//		return "candidate-selected";
	//	}
	//	
	//	return"";
	//	
	//	
	//}
	
	/**
	* Handles the request to create a new Document
	*/
	public handleAddDocument():void{
		
		let role:string 				= this.role ? ''+this.role?.id : '';
		let title:string 				= this.addDocumentForm.get("title")?.value;
		let type:string 				= 'PDF';
		
		if (!this.uploadedDocument){
		 return;
		}
		
		this.campaignService.addDocument( ''+this.campaign?.id, role,
			title,
			type,
			this.uploadedDocument).subscribe(res => {
				this.closeAddDocumentModal();
				this.selectionBox.refreshCampaign();
			}, err => {
				this.errMshActive = true;
			});
		
	}
	
	/**
	* Uploads the file for the Document and stores 
	* it ready to be sent to the backend
	*/
	public uploadDocumentFile(event:any):void{

		if (event.target.files.length <= 0) {
			return;
		}
	
		this.uploadedDocument = event.target.files[0];
		
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
	public deleteCandidates():void{
		
		this.wrappedCandidates.forEach(c => console.log(c.candidate.id + " " + c.selected));
		
		if (this.campaign == undefined) {
			return;
		}
		
		if (this.role == undefined) {
			this.wrappedCandidates.filter(c => c.selected).forEach(c => {
				if (this.campaign){
					this.campaignService.deleteCandidateFromCampaign(this.campaign.id, c.candidate.id).subscribe(res => {
						this.selectionBox.refreshCampaign();
					});	
				}
			});
		} else {
			this.wrappedCandidates.filter(c => c.selected).forEach(c => {
				if (this.campaign && this.role){
					this.campaignService.deleteCandidateFromRole(this.campaign.id, this.role.id, c.candidate.id).subscribe(res => {
						this.selectionBox.refreshCampaign();
					});
				}
			});
		}
	
	}
	
	/**
	* Sends message to multiple recipeints
	*/
	public messageCandidates():void{

		let message:string = ''+this.addMassMessageForm.get("message")?.value;
		
		this.campaignService.sendMultiRecipientMessage(this.wrappedCandidates.filter(c => c.selected).map(wc => wc.candidate.id), message).subscribe( res => {
			this.selectionBox.refreshCampaign();
		});
	}
	
	/**
	* Sends request to delete Candidate
	*/
	public deleteNote():void{
		
		if (this.note == undefined) {
			return;
		}
		
		this.campaignService.deleteNote(this.note.id).subscribe(res => {
			this.closeAddNotModal();	
			this.selectionBox.refreshCampaign();
		});
		
	}
	
	/**
	* Toggles between view and edit mode
	*/
	public toggleEditNote():void{	

		if (this.note == undefined) {
			return;
		}
		
		this.addNoteForm.get("title")?.setValue(this.note.title);
		this.addNoteForm.get("text")?.setValue(this.note.text);
				
		this.editNote = !this.editNote;
	}
	
	/**
	* Sends request to delete Candidate
	*/
	public updateNote():void{
		
		if (this.note == undefined) {
			return;
		}
		
		let title:string 				= this.addNoteForm.get("title")?.value;
		let text:string 				= this.addNoteForm.get("text")?.value;
						
		this.campaignService.updateNote(this.note.id, title, text).subscribe(res => {
			this.closeAddNotModal();	
			this.selectionBox.refreshCampaign();
		});
		
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
	
	/**
	* Toggles the display of the Documents section on the view
	*/
	public toggleShowDocuments():void{
		this.showDocuments = !this.showDocuments;
	}
	
	/**
	* Extracts the Candidates and applies a wrapper for the UI to allow candidates to 
	* be selected/de-selected
	* @param candidates - Candidates to be wrapped
	*/
	public getWrappedCandidates(candidates:Array<Candidate>):void{
		this.wrappedCandidates = candidates.map(c => new SelectableCandidate(c));
	}
	
	/**
	* Selects all Candidates
	*/
	public selectAllCandidates():void{
		this.wrappedCandidates.forEach(c => c.selected = true);
	}
	
	/**
	* De-selects all Candidates
	*/
	public deselectAllCandidates():void{
		this.wrappedCandidates.forEach(c => c.selected = false);
	}
	
	/**
	* Tgooles Select option for selected Candidate
	* @param candidate - Candidate to toggle select for
	*/
	public toggleCandidateSelect(candidate:SelectableCandidate):void{
		candidate.selected = !candidate.selected;
	}
	
}

/**
* UI specific wrapper to allow candidates to be selected / de-selected 
*/
export class SelectableCandidate{
	
	public candidate:Candidate;
	public selected:boolean = false;
	
	constructor(candidate:Candidate){
		this.candidate = candidate;
	}		
	
}