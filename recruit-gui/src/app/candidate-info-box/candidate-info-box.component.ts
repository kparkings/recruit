import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DeviceDetectorService } from 'ngx-device-detector';
import { InfoItem, InfoItemConfig } from './info-item';

@Component({
  selector: 'app-candidate-info-box',
  standalone: false,
  templateUrl: './candidate-info-box.component.html',
  styleUrl: './candidate-info-box.component.css'
})
export class CandidateInfoBoxComponent {

	@Input() config:InfoItemConfig = new InfoItemConfig();
	
	@Output() openContactBoxEvent = new EventEmitter<string>();

	public isMobile:boolean = false;
	
	/**
	* Mobile CSS variables 
	*/
	public mobileContainerCSS:string = '';
	
	/**
	* Constructor
 	*/	
	public constructor(private deviceDetector:DeviceDetectorService){
		this.isMobile = deviceDetector.isMobile();
		
		if (this.isMobile) {
			this.mobileContainerCSS = "mobile-left-pane-container";
		} 	
	
	}
	
	public openContatBox() {
    	this.openContactBoxEvent.emit('openContactDialogBox');
  	}
	

}