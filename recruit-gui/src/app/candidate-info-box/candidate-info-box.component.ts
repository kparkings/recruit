import { Component, EventEmitter, Input, Output } 	from '@angular/core';
import { CommonModule } 							from '@angular/common';
import { InfoItem, InfoItemConfig } 				from './info-item';
import { AppComponent } 							from 'src/app/app.component';
@Component({
  selector: 'app-candidate-info-box',
  standalone: false,
  templateUrl: './candidate-info-box.component.html',
  styleUrls: ['./candidate-info-box.component.css','./candidate-info-box.component-mob.css']
})
export class CandidateInfoBoxComponent {

	@Input()  config:InfoItemConfig = new InfoItemConfig();
	@Output() openContactBoxEvent 	= new EventEmitter<string>();

	/**
	* Constructor
 	*/	
	public constructor(private appComponent:AppComponent){
	
	}
	
	public openContatBox() {
    	this.openContactBoxEvent.emit('openContactDialogBox');
  	}
	
	public openChat():void{
		this.appComponent.currentChatWindowState = "maximized";
	}
	
}