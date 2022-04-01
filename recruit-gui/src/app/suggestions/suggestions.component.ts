import { Component, OnInit } 					from '@angular/core';
import { FormGroup, FormControl }				from '@angular/forms';
import { CandidateServiceService }				from '../candidate-service.service';
import { SuggestionsService }					from '../suggestions.service';
import { Candidate}								from './candidate';
import { environment }							from '../../environments/environment';
import { Clipboard } from '@angular/cdk/clipboard';

/**
* Component to suggest suitable Candidates based upon a 
* Recruiters search
*/
@Component({
  selector: 'app-suggestions',
  templateUrl: './suggestions.component.html',
  styleUrls: ['./suggestions.component.css']
})
export class SuggestionsComponent implements OnInit {

	public suggestions:Array<Candidate>  = new Array<Candidate>();

	public suggestionFilterForm:FormGroup 	= new FormGroup({
		searchPhrase:			new FormControl(''),
		nlResults: 				new FormControl(true),
		beResults: 				new FormControl(true),
		ukResults: 				new FormControl(true),
		contractType: 			new FormControl('Both'),
		dutchLanguage: 			new FormControl(false),
		englishLanguage: 		new FormControl(false),
		frenchLanguage:			new FormControl(false),
		minYearsExperience: 	new FormControl(''),
		maxYearsExperience: 	new FormControl(''),
		skill: 					new FormControl(''),
	});
	
	public currentView:string 				= 'suggestion-results';
	public skillFilters:Array<string>		= new Array<string>();
	public minMaxOptions:Array<string> 		= new Array<string>('','1','2','4','8','16','32');
	public suggestedCandidate:Candidate		= new Candidate();
	
	/**
	* Constructor
	* @param candidateService - Services relating to Candidates
	*/
	constructor(public candidateService:CandidateServiceService, public suggestionsService:SuggestionsService, private clipboard: Clipboard) { 
		this.getSuggestions();	
	}

	/**
	* Initializes Component`
	*/
	ngOnInit(): void {
		
		this.suggestionFilterForm.valueChanges.subscribe(value => {
			this.getSuggestions();	
		});
		
	}
	
	/**
	* Sends request for Suggestions to the backend API
	*/
	private getSuggestions():void{
		
		const maxSuggestions:number 		= 12;
		
		let countries:Array<string> 		= new Array<string>();
		let skills:Array<string> 			= this.skillFilters;
		let languages:Array<string> 		= new Array<string>();
		
		let title:string 					= this.suggestionFilterForm.get('searchPhrase')?.value;
		let contract:boolean 				= true;
		let perm:boolean 					= true;
		let minExperience:string 			= this.suggestionFilterForm.get('minYearsExperience')?.value;
		let maxExperience:string 			= this.suggestionFilterForm.get('maxYearsExperience')?.value;
		
		/**
		* Add any country filters 	
		*/
		if (this.suggestionFilterForm.get('nlResults')?.value) {
			countries.push("NETHERLANDS");
		}
		
		if (this.suggestionFilterForm.get('beResults')?.value) {
			countries.push("BELGIUM");
		}
		
		if (this.suggestionFilterForm.get('ukResults')?.value) {
			countries.push("UK");
		}

		/**
		* Add any language filters 	
		*/
		if (this.suggestionFilterForm.get('dutchLanguage')?.value) {
			languages.push("DUTCH");
		}
		
		if (this.suggestionFilterForm.get('frenchLanguage')?.value) {
			languages.push("FRENCH");
		}
		
		if (this.suggestionFilterForm.get('englishLanguage')?.value) {
			languages.push("ENGLISH");
		}
				
		/**
		* Ccontract type filters
		*/
		if (this.suggestionFilterForm.get('contractType')?.value === 'BOTH'){
			//perm 		= false;
			//contract 	= false;
		}
		
		if (this.suggestionFilterForm.get('contractType')?.value === 'CONTRACT'){
			perm 		= true;
			contract 	= false;
		}
		
		if (this.suggestionFilterForm.get('contractType')?.value === 'PERM'){
			perm 		= false;
			contract 	= true;
		}
		
		this.suggestionsService.getSuggestons(	maxSuggestions,
												title,
												countries,
												contract,
												perm,
												minExperience,
												maxExperience,
												languages,
												skills,
											).subscribe(data => {
												
												this.suggestions =  new Array<Candidate>();
												
												data.content.forEach((s:Candidate) => {
													this.suggestions.push(s);	
												});
												 
											});
	}
	
	/**
	* Toggles whether Candidates from selected country 
	* should be included in the Suggestions 
	* @param country - Country to toggle
	*/
	public toggleCountrySelection(country:string):void{
		
		let included:boolean = this.suggestionFilterForm.get((country+'Results'))?.value;
		
		this.suggestionFilterForm.get((country+'Results'))?.setValue(!included);
				
	}
	
	/**
	* Returns whether Candidates from the selected country are currently
	* included in the Suggestion results
	*/
	public includeResultsForCountry(country:string):boolean{
		return this.suggestionFilterForm.get((country+'Results'))?.value;
	}
	
	/**
	* Adds a skill to the list of Skills to filter on
	*/
	public addSkill():void{
		
		let skillFormatted:string 	= this.suggestionFilterForm.get('skill')?.value.trim();
		skillFormatted 				= skillFormatted.toLocaleLowerCase();
		
		if (skillFormatted.length > 0 && this.skillFilters.indexOf(skillFormatted) == -1) {
			this.skillFilters.push(skillFormatted);	
		}
		
		this.suggestionFilterForm.get('skill')?.setValue('');
		
	}
	
	/**
	* Removes a skill and calls for new suggestions
	*/
	public removeSkill(skill:string):void{
		
		skill = skill.trim();
		skill = skill.toLocaleLowerCase();
		
		this.skillFilters = this.skillFilters.filter(s => s  !== skill);
		
		this.getSuggestions();	
	
	}

	/**
	* Shows the Suggesion result view
	*/
	public showSuggestionsResults():void{
		this.currentView = 'suggestion-results';
		this.suggestedCandidate = new Candidate();
	}

	/**
	* Shows the Suggesion result view
	*/
	public showSuggestedCandidateOverview(candidateSuggestion:Candidate):void{
		this.currentView = 'suggested-canidate-overview';
		this.suggestedCandidate = candidateSuggestion;
	}
	
	/**
	* Flags a Candidate as being potentially unavailable
	*/
	public markCandidateAsUnavailable():void {
		this.candidateService.markCandidateAsUnavailable(this.suggestedCandidate.candidateId).subscribe(data => {});
		this.suggestedCandidate.flaggedAsUnavailable = true;
	}
	
	/**
	*  Returns the url to perform the download of the candidates CV
	*/
	public getCurriculumDownloadUrl(curriculumId:string){
		return  environment.backendUrl + 'curriculum/'+ curriculumId;
	}
	
	public contractType():string{
		
		if (this.suggestedCandidate.freelance === 'TRUE' && this.suggestedCandidate.perm === 'TRUE') {
			return'Contract / Pern ';
		}
		
		if (this.suggestedCandidate.perm === 'TRUE') {
			return'Pern ';
		}
		
		if (this.suggestedCandidate.freelance === 'TRUE') {
			return'Contract ';
		}
		
		return '';
		
		
	}
	
	/**
	* Returns the Humand readable version of the Language
	* @param country - Language to get the readable version for
	*/
	public getLanguage(lang:string):string{

		switch(lang){
			case "DUTCH":{
				return "Dutch";
			}
			case "FRENCH":{
				return "French";
			}
			case "ENGLISH":{
				return "English";
			}
			default:{
				return 'NA';
			}
		}

  	}	

	/**
	* Copies the email address of the suggested candidate to 
	* the clipboard
	*/
	public copyEmailToClipboard():void {
		
		this.clipboard.copy(this.suggestedCandidate.email);
		this.candidateService.logRecruiterRequestedCandidateEmailEvent(this.suggestedCandidate.candidateId).subscribe();
		
	}
	
	public hasRequiredSkill(skill:string):string {
		
		let formattedSkill:string = skill.trim().toLowerCase();
		
		let match:boolean = false;
		this.suggestedCandidate.skills.forEach(skill => {
			if(skill === formattedSkill) {
				 match = true;
			}	else {
				console.log('not match on ' + skill);
			}
		})
		
		if (match) {
			return 'skill-match';
		}
		
		return 'skill-no-match';
	}

}