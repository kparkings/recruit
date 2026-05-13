import { Component, ViewChild }		from '@angular/core';
import { Campaign  } 				from '../campaings.service';
import { SelectionboxComponent} 	from '../campaigns/selectionbox/selectionbox.component'

@Component({
  selector: 'app-campaigns',
  templateUrl: './campaigns.component.html',
  styleUrl: './campaigns.component.css',
  standalone:false
})
export class CampaignsComponent {
	
	@ViewChild(SelectionboxComponent) 			public selectionBox!:SelectionboxComponent;
	
	public campaign:Campaign | undefined;
	

}
