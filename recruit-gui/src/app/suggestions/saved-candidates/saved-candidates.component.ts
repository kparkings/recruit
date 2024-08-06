import { Component, EventEmitter, Input, Output } 								from '@angular/core';
import { UntypedFormControl, UntypedFormGroup } 								from '@angular/forms';
import { Router } 																from '@angular/router';
import { InfoItemConfig } 														from 'src/app/candidate-info-box/info-item';
import { CandidateProfile } 													from 'src/app/candidate-profile';
import { CandidateServiceService } 												from 'src/app/candidate-service.service';
import { Candidate } 															from '../candidate';
import { SavedCandidate } 														from '../saved-candidate';
import { TranslateService } 													from '@ngx-translate/core';
import { SuggestionsComponent } from '../suggestions.component';

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
	
 	@Input() infoItemConfig:InfoItemConfig = new InfoItemConfig();
 	@Input() skillFilters:Array<string>		= new Array<string>();
	
	/**
	* Constructor 
	*/
	constructor(private candidateService:CandidateServiceService,
				private router:Router,
				private translate:TranslateService,
				private parentX:SuggestionsComponent){
					
	}
	
	public currentView:string 					= 'saved-candidates';
	public candidateProfile:CandidateProfile 	= new CandidateProfile();
	public suggestedCandidate:Candidate			= new Candidate();
	
	public savedCandidateNotesForm:UntypedFormGroup = new UntypedFormGroup({
		notes:			new UntypedFormControl(''),
	});
	
	/**
	* Switches back to the previous view 
	*/
	public handleswitchViewEvent():void{
		this.switchViewEvent.emit();
	}
	
	public isNoLongerAvailableSC(savedCandidate:SavedCandidate):boolean{
		return savedCandidate.candidate.firstname === 'Candidate No Longer Available';
	}
	
	public showSuggestedCandidateOverviewSavedCandidate(savedCandidate:SavedCandidate){
		//TODO: [KP] Left pane not set. Needs to be updated when candidate is selected. Object available but we don't have the data/methods available to constuct it here
		//Methods we can extract to service
		//Data is more complicated - Pass it into tag as input and then pass that to service to construct the InfoItem
		
		this.parentX.setLeftInfoPane(savedCandidate.candidateId+"", savedCandidate.candidate);
		this.currentView 			= 'suggested-canidate-overview';
		this.suggestedCandidate 	= savedCandidate.candidate;
		this.suggestedCandidate.removed = this.isNoLongerAvailableSC(savedCandidate);
		this.fetchCandidateProfile(savedCandidate.candidate.candidateId);
	}
	
	public showSavedCandidates():void{
		this.currentView 	= 'saved-candidates';
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