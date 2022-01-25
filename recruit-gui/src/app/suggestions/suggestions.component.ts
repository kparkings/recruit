import { Component, OnInit } 					from '@angular/core';
import { FormGroup, FormControl }				from '@angular/forms';
import { CandidateServiceService }				from '../candidate-service.service';
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

	public suggestionFilterForm:FormGroup 	= new FormGroup({
		searchPhrase:			new FormControl(''),
		nlResults: 				new FormControl(true),
		beResults: 				new FormControl(true),
		ukResults: 				new FormControl(true),
		contractType: 			new FormControl('Both'),
		dutchLanguage: 			new FormControl(true),
		englishLanguage: 		new FormControl(true),
		frenchLanguage:			new FormControl(true),
		minYearsExperience: 	new FormControl(''),
		maxYearsExperience: 	new FormControl(''),
		skill: 					new FormControl(''),
	});
	
	public skillFilters:Array<string>		= new Array<string>();
	
	public minMaxOptions:Array<string> 		= new Array<string>('','1','2','4','8','16','32');
	/**
	* Constructor
	* @param candidateService - Services relating to Candidates
	*/
	constructor(public candidateService:CandidateServiceService) { }

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

}