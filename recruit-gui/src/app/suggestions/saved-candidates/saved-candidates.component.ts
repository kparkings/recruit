import { Component, ElementRef, EventEmitter, Input, Output, ViewChild } from '@angular/core';
import { UntypedFormControl, UntypedFormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { CandidateProfile } from 'src/app/candidate-profile';
import { CandidateServiceService } from 'src/app/candidate-service.service';
import { Candidate } from '../candidate';
import { SavedCandidate } from '../saved-candidate';

/**
* Component for Candidates saved by a User to allow them 
* to easily find them at a later date 
*/
@Component({
  selector: 'app-saved-candidates',
  standalone: false,
  templateUrl: './saved-candidates.component.html',
  styleUrl: './saved-candidates.component.css'
})
export class SavedCandidatesComponent {

	@Input() 	savedCandidates: 			Array<SavedCandidate> 		= new Array<SavedCandidate>();
	@Output() 	switchViewEvent:			EventEmitter<string> 		= new EventEmitter<string>();
	//@Output() 	showCandidateEvent:			EventEmitter<Candidate> 	= new EventEmitter<Candidate>();
	
	@ViewChild('notesBox', { static: true }) 			notesDialogBox!: ElementRef<HTMLDialogElement>;
	
	/**
	* Constructor 
	*/
	constructor(private candidateService:CandidateServiceService,
				private router:Router){
					
				}
	
	public currentView:string 				= 'saved-candidates';
	public currentSavedCandidate:SavedCandidate = new SavedCandidate();
	public candidateProfile:CandidateProfile = new CandidateProfile();
	
	public suggestedCandidate:Candidate		= new Candidate();
	
	public savedCandidateNotesForm:UntypedFormGroup = new UntypedFormGroup({
		notes:			new UntypedFormControl(''),
	});
	
	/**
	* Switches back to the previous view 
	*/
	public handleswitchViewEvent():void{
		this.switchViewEvent.emit();
	}
	
	/**
	* Switches back to the previous view 
	*/
	//public handleShowCandidateEvent(candidate:Candidate):void{
	//	this.showCandidateEvent.emit(candidate);
	//}
	
	public isNoLongerAvailable():boolean{
		
		if (this.currentSavedCandidate.userId === '') {
			return false;
		}
		
		return this.currentSavedCandidate.candidate.candidateId === ' Removed';;
	}
	
	public isNoLongerAvailableSC(savedCandidate:SavedCandidate):boolean{
		return savedCandidate.candidate.candidateId === ' Removed';
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
		//this.open(content, '', true);
	}
	
	public updatedSavedCandidate:boolean = false; 
	
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
	
	public showSuggestedCandidateOverviewSavedCandidate(savedCandidate:SavedCandidate){
		this.currentView 			= 'suggested-canidate-overview';
		this.suggestedCandidate 	= savedCandidate.candidate;
		this.currentSavedCandidate	= savedCandidate;
		this.fetchCandidateProfile(savedCandidate.candidate.candidateId);
	}
	
	/**
	* Marks Candidate as no longer being being remembered
	*/
	public forgetCandidate():void{
		this.candidateService.deleteSavedCandidate(Number(this.currentSavedCandidate.candidateId)).subscribe(response => {
			this.showSavedCandidates();
		});
	}
	
	public showSavedCandidates():void{
		//this.currentView 	= 'saved-candidates';
		//this.lastView 		= 'saved-candidates';
		this.candidateService.fetchSavedCandidates().subscribe(response => {
			this.savedCandidates = response;
		});
	}
	
		
	
	/**
	* Fetches Candidate
	*/
	public fetchCandidateProfile(candidateId:string):void{
		this.candidateProfile = new CandidateProfile();
		this.candidateService.getCandidateProfileById(candidateId).subscribe( candidate => {
				this.candidateProfile = candidate;
				//this.currentView = 'suggested-canidate-overview';
			}, err => {
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
	
}
