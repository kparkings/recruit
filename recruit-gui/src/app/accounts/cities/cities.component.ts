import { Component } 							from '@angular/core';
import { UntypedFormControl, UntypedFormGroup } from '@angular/forms';
import { CandidateServiceService } 				from 'src/app/candidate-service.service';
import { City } 								from './../../city';

/**
* Component to adminster City's  
*/
@Component({
  selector: 'app-cities',
  standalone: false,
  templateUrl: './cities.component.html',
  styleUrl: './cities.component.css'
})
export class CitiesComponent {

	public citiesAwaitingActivation:Array<City> = new Array<City>();
	
	/**
	* Construtor 
	*/
	constructor(public candidateService:CandidateServiceService){
		this.loadCitiesAwaitingActivation();
	}
	
	public cityForm:UntypedFormGroup 	= new UntypedFormGroup({
			lat:			new UntypedFormControl(''),
			lon:			new UntypedFormControl(''),
		});
	
	/**
	* fetches cities awaiting activation  
	*/
	private loadCitiesAwaitingActivation():void{
		this.citiesAwaitingActivation = new Array<City>();
		this.candidateService.getCities().subscribe(c => {
			this.citiesAwaitingActivation = c;
			this.cityForm 	= new UntypedFormGroup({
				lat:		new UntypedFormControl(''),
				lon:		new UntypedFormControl(''),
			});
		});
	}
	
	/**
	* Updates a City  
	*/
	public updateCity(city:City):void{
		
		let lat:number = this.cityForm.get('lat')?.value;
		let lon:number = this.cityForm.get('lon')?.value;
				
		this.candidateService.updateCity(city, lat, lon).subscribe(c => {
			this.loadCitiesAwaitingActivation();
		});
	}
	
	/**
	* Deletes a City  
	*/
	public deleteCity(city:City):void{
		
		this.candidateService.deleteCity(city).subscribe(c => {
			this.loadCitiesAwaitingActivation();
		});
	}	
	
}
