import { Component } 								from '@angular/core';
import { UntypedFormGroup, UntypedFormControl }		from '@angular/forms';
import { CampaingsService}							from 'src/app/campaings.service';
import { CurriculumService } 						from 'src/app/curriculum.service';

@Component({
  selector: 'app-selectionbox',
  standalone: false,
  templateUrl: './selectionbox.component.html',
  styleUrl: './selectionbox.component.css'
})
export class SelectionboxComponent {

	/**
	* Constructor
	* @param campaignService - Services for interacting with Campaigns
	*/
	constructor(private readonly campaignService:CampaingsService){
			
	}
	
	/**
	* Controls relating to visibility 
	*/
	public showCampaignSelectionList:boolean 		= true;
	public showAddCampaignForm:boolean 				= false;
	
	/**
	* Forms 
	*/
	public addCampaignForm:UntypedFormGroup = new UntypedFormGroup({
		name: 			new UntypedFormControl(),
		description: 	new UntypedFormControl(),
		file: 			new UntypedFormControl(),
	});
	
	/**
	* Shows list of exisign Campaigns and allows the 
	* User to select a Campaign to add the Candidate to 
 	*/
	public addCampaign():void{
		this.showCampaignSelectionList = false;
		this.showAddCampaignForm = true;
	}
	
	/**
	* Shows a form to allow the User to create a new Campaign which
	* the Candidate will be added to 
	 */
	public listCampaigns():void{
		this.showCampaignSelectionList = true;
		this.showAddCampaignForm = false;
	}
	
	
}
