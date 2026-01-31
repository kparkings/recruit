import { Component, EventEmitter, Input, Output } from '@angular/core';
import { DomSanitizer, SafeResourceUrl } 											from '@angular/platform-browser';
import { Candidate } from '../candidate';

/**
* Component to display an inline CV 
*/
@Component({
  selector: 'app-inline-cv',
  standalone: false,
  templateUrl: './inline-cv.component.html',
  styleUrl: './inline-cv.component.css'
})
export class InlineCvComponent {

	@Input() 	trustedResourceUrl: 	SafeResourceUrl;
	@Input() 	cssClass: 				string						= "";
	@Input() 	candidate:				Candidate 					= new Candidate();
	@Output() 	switchViewEvent:		EventEmitter<string> 		= new EventEmitter<string>();
	
	/**
	* Constructor
	*/
	constructor(private sanitizer: 				DomSanitizer){
		this.trustedResourceUrl = this.sanitizer.bypassSecurityTrustResourceUrl('');
	}
	
	/**
	* Switches back to the previous view 
	*/
	public handleswitchViewEvent():void{
		this.switchViewEvent.emit();
	}
	
	public getCssClass():string{
		return this.cssClass;
	}

}
