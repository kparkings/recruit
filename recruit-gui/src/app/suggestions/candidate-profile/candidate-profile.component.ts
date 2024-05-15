import { Component, ElementRef, EventEmitter, Input, Output, ViewChild } 	from '@angular/core';
import { UntypedFormControl, UntypedFormGroup } 							from '@angular/forms';
import { InfoItemConfig } 													from 'src/app/candidate-info-box/info-item';
import { CandidateProfile } 												from 'src/app/candidate-profile';
import { EmailRequest, EmailService } 										from 'src/app/email.service';
import { Candidate } 														from '../candidate';
import { NgbModalOptions }													from '@ng-bootstrap/ng-bootstrap';
import { CurriculumService } 												from 'src/app/curriculum.service';
import { CreditsService } 													from 'src/app/credits.service';
import { environment }														from '../../../environments/environment';
import { SavedCandidate } 													from '../saved-candidate';
import { DomSanitizer, SafeResourceUrl } 									from '@angular/platform-browser';
import { CandidateNavService } 												from 'src/app/candidate-nav.service';
import { CandidateServiceService } 											from 'src/app/candidate-service.service';
import { Router } 															from '@angular/router';
import { AppComponent } 													from 'src/app/app.component';

/**
* Candidate profile showing canidates details and options to interact with 
* the Candidates prfofile 
*/
@Component({
  selector: 'app-candidate-profile',
  standalone: false,
  templateUrl: './candidate-profile.component.html',
  styleUrl: './candidate-profile.component.css'
})
export class CandidateProfileComponent {

	@Input()  infoItemConfig:InfoItemConfig 			= new InfoItemConfig();
	@Input()  suggestedCandidate:Candidate 				= new Candidate();
	@Input()  skillFilters:Array<string>				= new Array<string>();
	@Input()  candidateProfile:CandidateProfile 		= new CandidateProfile();
	@Input()  externalProvileViewCandidateId:string 	= "";
	@Input()  isViewOnly:boolean 						= false;
	@Input()  parentComponent:string 					= "";
	@Output() backEvent 								= new EventEmitter<string>();
	
	@ViewChild('contactBox', {static:true})				contactDialogBox!: ElementRef<HTMLDialogElement>;
 	@ViewChild('confirmDeleteModal', {static:true})		confirmDeleteDialogBox!: ElementRef<HTMLDialogElement>;
 	@ViewChild('notesBox', { static: true }) 			notesDialogBox!: ElementRef<HTMLDialogElement>;
	
	public candidateIsRemoved:boolean					= false;
	public contactCandidateView:string 					= 'message';
	public currentView:string 							= 'suggestion-results';
	public dangerousUrl 								= 'http://127.0.0.1:8080/curriculum-test/1623.pdf';
	public savedCandidates:Array<SavedCandidate> 		= new Array<SavedCandidate>();
	public lastView:string 								= '';
	public passedCreditCheck:boolean 					= this.isAdmin();
	public trustedResourceUrl:SafeResourceUrl;
	public updatedSavedCandidate:boolean 				= false; 
	public currentSavedCandidate:SavedCandidate 		= new SavedCandidate();
	public savedCandidateNotesForm:UntypedFormGroup 	= new UntypedFormGroup({
		notes:			new UntypedFormControl(''),
	});
	/**
	* Constructor 
	*/
	constructor(private emailService:EmailService, 
				private curriculumService:CurriculumService,
				private creditsService:CreditsService,
				private sanitizer: DomSanitizer,
				private candidateNavService:CandidateNavService,
				public candidateService:CandidateServiceService,
				private router:Router,
				private appComponent:AppComponent,){
					
		this.trustedResourceUrl = this.sanitizer.bypassSecurityTrustResourceUrl('');
		
	}

	/**
	* Lifecycle - Init 
	*/	
	ngOnInit():void{
		this.candidateService.fetchSavedCandidates().subscribe(response => {
			this.savedCandidates = response;
		})
		
		if (this.parentComponent == 'savedCandidates') {
			this.currentView = 'saved-candidates';
		}
		
		this.doCreditCheck();
		
	}
	
	/**
	* Marks Candidate as no longer being being remembered
	*/
	public forgetCandidate():void{
		this.candidateService.deleteSavedCandidate(Number(this.suggestedCandidate.candidateId)).subscribe(response => {
			this.back();
		});
	}
	
	public isNoLongerAvailable():boolean{
		
		if (this.currentSavedCandidate.userId === '') {
			return false;
		}
		
		return this.currentSavedCandidate.candidate.candidateId === ' Removed';;
	}
	
	/**
	* Displays dialog to edit notes for a SavedCandidate
	*/
	public showNotesDialog(content:any, candidate:Candidate):void{
		
		this.updatedSavedCandidate = false;
		
		if (!this.isNoLongerAvailable()){
			this.currentSavedCandidate = new SavedCandidate()
		}
		
		this.savedCandidates.forEach(sc => {
			if(""+sc.candidateId === ""+candidate.candidateId) {
				this.currentSavedCandidate = sc;
			}
		});
		
		this.savedCandidateNotesForm = new UntypedFormGroup({
			notes:			new UntypedFormControl(this.currentSavedCandidate.notes)
		});
	
		this.notesDialogBox.nativeElement.showModal();

	}
	
	/**
	* Updates the current Saved Candidate
	*/
	public updateSavedCandidate():void{
		this.updatedSavedCandidate = false;
		this.currentSavedCandidate.notes = this.savedCandidateNotesForm.get('notes')?.value;
		this.candidateService.updateSavedCandidate(this.currentSavedCandidate).subscribe(response =>{
			this.candidateService.fetchSavedCandidates().subscribe(response => {
				this.savedCandidates = response;
				this.updatedSavedCandidate = true;
			});
		});
	} 
	
	public back():void{
		this.backEvent.emit();	 
	}
	
	public hasRequiredSkill(skill:string):string {
		
		if (this.hasSkill(skill)) {
			return 'skill-match';
		}
		
		return 'skill-no-match';
	}
	
	public hasSkill(skill:string):boolean {
		
		let formattedSkill:string = skill.trim().toLowerCase();
		
		let match:boolean = false;
		this.suggestedCandidate.skills.forEach(skill => {
			if (skill === formattedSkill) {
				 match = true;
			}
		})
		
		if (match) {
			return true;
		}
		
		return false;
	}
	
	/**
	* Forms group for sending a message to a Recruiter relating to a
	* specific post on the jobboard 
	*/
	public sendMessageGroup:UntypedFormGroup = new UntypedFormGroup({
		message:			new UntypedFormControl(''),
		title:				new UntypedFormControl('')
	});
	
	/**
	* Opend dialog to contact recuiter posting	
	*/
	public sendMessageToCandidate():void{
		
		let emailRequest:EmailRequest = new EmailRequest();
		
		emailRequest.title = this.sendMessageGroup.get('title')?.value;;
		emailRequest.message = this.sendMessageGroup.get('message')?.value;;
		
		this.emailService.sendCandidateContactEmail(emailRequest,this.candidateProfile.candidateId).subscribe(body => {
			this.contactCandidateView = 'success';
			this.sendMessageGroup = new UntypedFormGroup({
				message: new UntypedFormControl(''),
				title: new UntypedFormControl(''),
			});
		}, err => {
			this.contactCandidateView = 'failure';
		});
		
	}
	
	/**
	* Returns skills that the Canidate has that match the 
	* required skills
	*/
	public skillsFiltersMatch():Array<string>{
		return this.skillFilters.filter(s => this.hasSkill(s) && s != '').sort();
	}
	
	/**
	* Returns skills that the Canidate does not has that are 
	* required skills
	*/
	public skillsFiltersNoMatch():Array<string>{
		return this.skillFilters.filter(s => !this.hasSkill(s) && s != '').sort();
	}
	
	/**
	* Whether or not the User is a Candidate
	*/
	public isCandidate():boolean{
		return sessionStorage.getItem('isCandidate') === 'true';
	}
	
	/**
	* Opend dialog to contact recuiter posting	
	*/
	public contactCandidate(contactBox:any):void{
		
		this.contactCandidateView = 'message';
		let options: NgbModalOptions = {
	    	 centered: true
	   };

		this.contactDialogBox.nativeElement.showModal();
	
	}
	
	public doCreditCheckByCount():void{
		
		if(this.isAdmin()){
			this.passedCreditCheck = true;
		} else {
			this.curriculumService.getCreditCount().subscribe(count => {
				this.passedCreditCheck = (count > 1 || count == -1);
			});	
		}
		
	}
	
	public doCreditCheck():void{
		
		if(this.isAdmin()){
			this.passedCreditCheck = true;
		} else {
			this.curriculumService.getCreditCount().subscribe(count => {
				this.passedCreditCheck = (count > 0 || count == -1);
			});	
		}
	}
	
	/**
	* Whether or not the User is a Admin
	*/
	public isAdmin():boolean{
		return sessionStorage.getItem('isAdmin') === 'true';
	}
	
	public handleNoCredit():void{
		this.creditsService.tokensExhaused();
	}
	
	/**
	*  Returns the url to perform the download of the candidates CV
	*/
	public getCurriculumDownloadUrl(curriculumId:string){
		return  environment.backendUrl + 'curriculum/'+ curriculumId;
	}
	
	/**
	* Shows the inline CV view
	*/
	public showInlineCVView():void{
		
		if(!this.isAdmin()) {
			if (this.passedCreditCheck) {
				this.currentView = 'inline-cv';
				this.showCVInline(this.suggestedCandidate.candidateId);
			} else {
				this.handleNoCredit();		
			}
		
			this.doCreditCheck();
		} else {
			this.currentView = 'inline-cv';
			this.showCVInline(this.suggestedCandidate.candidateId);
		}
	}

	public backToProfile():void{
		this.currentView = 'suggestion-results';
	}
	
	/**
	* Marks Candidate as being remembered
	*/
	public rememberCandidate(suggestedCandidate:Candidate):void{
		
		const savedCandidate:SavedCandidate = new SavedCandidate();
		
		savedCandidate.candidateId = Number(suggestedCandidate.candidateId);
		
		this.candidateService.addSavedCandidate(savedCandidate).subscribe(response => {
			this.candidateService.fetchSavedCandidates().subscribe(response => {
				this.savedCandidates = response;
			})
		});
	}
	
	public showCVInline(candidateId:string):void{
		
		let url = this.curriculumService.getCurriculumUrlForInlinePdf(candidateId); 'http://127.0.0.1:8080/curriculum-test/';
		
		
		this.trustedResourceUrl = this.sanitizer.bypassSecurityTrustResourceUrl(url);
		
	}
	
	/*
	* Navigates to the edit page
	*/
	public editAccount():void{
		this.candidateNavService.doNextMove("edit", this.candidateProfile.candidateId);
	}

	/*
	* Opens confirm delete popup
	*/	
	public deleteAccount(){
		this.confirmDeleteDialogBox.nativeElement.showModal();
	}
	
	public confirmDeleteProfile():void{
		
		this.candidateService.deleteCandidate(this.candidateProfile.candidateId).subscribe(data => {
			if (this.isCandidate()) {
				sessionStorage.removeItem('isAdmin');
				sessionStorage.removeItem('isRecruter');
				sessionStorage.removeItem('isCandidate');
				sessionStorage.removeItem('loggedIn');
				sessionStorage.setItem('beforeAuthPage', 'view-candidates');
				this.candidateNavService.reset();
				this.router.navigate(['login-user']);
				this.appComponent.refreschUnreadAlerts();
			}else {
				this.back();
			}
			this.confirmDeleteDialogBox.nativeElement.close();
		});
	}
	
	/**
	* Whether the candidate is a Saved Candidate
	*/
	public isSavedCandidate(candidate:Candidate):boolean{
		
		let chk:boolean = false;
		
		this.savedCandidates.forEach(sc => {
			if (""+sc.candidateId === ""+candidate.candidateId) {
				chk = true;
			}
		});
		
		return chk;
	}
	
	/**
	* If the Recruiter is the owner of the selected Candidate
	*/
	public isOwner():boolean{
		
		if (!this.candidateNavService.isRouteActive()) {
			return false;
		}
		
		return this.candidateProfile.ownerId == this.getLoggedInUserId();
	}
	
	/**
	* Whether or not the Use is a Candidate
	*/
	public getLoggedInUserId():string{
		return ""+sessionStorage.getItem("userId");
	}
	
	/**
	* [KP] Ugly and hacky. Need to do somethin gon the BE to ass status to candidate and
	* use that insted
	*/
	public isNoLongerAvailableSC(suggestedCandidate:Candidate):boolean{
		return this.suggestedCandidate.removed;
	}
	
	
	public showBtnNotes():boolean{
		
		if (this.suggestedCandidate.removed) {
			return false;
		}
		
		if (this.parentComponent == 'newsfeed'){
			return false;
		}
		
		return this.parentComponent == 'savedCandidates';
	}
	
	public showBtnUnsaveCandidate():boolean{
		
		if (this.parentComponent == 'newsfeed'){
			return false;
		}
		
		return this.currentView === 'saved-candidates'
	}
	
	public showBtnSaveCandidate():boolean{
		
		if (this.parentComponent == 'newsfeed'){
			return false;
		}
		
		return this.currentView != 'saved-candidates' && !this.isSavedCandidate(this.suggestedCandidate) && !this.isCandidate();
	}
	
	public showBtnUnsavedCandidateDisabled():boolean{
		
		if (this.suggestedCandidate.removed) {
			return false;
		}
		
		if (this.parentComponent == 'newsfeed'){
			return false;
		}
		
		return this.currentView != 'saved-candidates' && this.isSavedCandidate(this.suggestedCandidate) && !this.isCandidate()
	}
	
	public showBtnDelete():boolean{
		
		if (this.parentComponent == 'newsfeed'){
			return false;
		}
		
		if (this.suggestedCandidate.removed) {
			return false;
		}
		
		return this.isCandidate() || this.isAdmin() || this.isOwner();
	}
	
	public showBtnEdit():boolean{
		if (this.parentComponent == 'savedCandidates') {
			return false;
		}
		
		if (this.parentComponent == 'newsfeed'){
			return false;
		}
		
		return this.isCandidate() || this.isAdmin() || this.isOwner();
	}
	
	public showBtnShowCV():boolean{
		
		if (this.suggestedCandidate.removed) {
			return false;
		}
		
		return !this.isCandidate();
	}
	
	public showBtnDownloadCV():boolean{
		
		if (this.suggestedCandidate.removed) {
			return false;
		}
		
		return !this.passedCreditCheck && (!this.isCandidate());
	}
	
	public showBtnDownloadCVPassedCreditCheck():boolean{
		
		if (this.suggestedCandidate.removed) {
			return false;
		}
		
		return this.passedCreditCheck && (!this.isCandidate());
	}
	
}