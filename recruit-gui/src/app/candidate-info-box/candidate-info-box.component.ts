import { Component, EventEmitter, Input, Output } 	from '@angular/core';
import { CommonModule } 							from '@angular/common';
import { InfoItem, InfoItemConfig } 				from './info-item';

@Component({
  selector: 'app-candidate-info-box',
  standalone: false,
  templateUrl: './candidate-info-box.component.html',
  styleUrl: './candidate-info-box.component.css'
})
export class CandidateInfoBoxComponent {

	@Input()  config:InfoItemConfig = new InfoItemConfig();
	@Output() openContactBoxEvent 	= new EventEmitter<string>();

	/**
	* Constructor
 	*/	
	public constructor(){
	
	}
	
	public openContatBox() {
    	this.openContactBoxEvent.emit('openContactDialogBox');
  	}
	
}