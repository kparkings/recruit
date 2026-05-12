import { Component } 											from '@angular/core';
import { UntypedFormGroup, UntypedFormControl }					from '@angular/forms';
import { CampaingsService, CampaignLogo, CampaignOverview}		from 'src/app/campaings.service';
import { CurriculumService } 									from 'src/app/curriculum.service';

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
		this. fetchExistingCampaigns();
	}
	
	/**
	* Controls relating to visibility 
	*/
	public showCampaignSelectionList:boolean 		= true;
	public showAddCampaignForm:boolean 				= false;
	
	/**
	* Image log  
	*/
	private logoImageFile!:File;
	
	/**
	* Campaigins 
	*/
	public campaigns:Array<CampaignOverview> = new Array<CampaignOverview>();
	
	/**
	* Forms 
	*/
	public addCampaignForm:UntypedFormGroup = new UntypedFormGroup({
		name: 			new UntypedFormControl(),
		description: 	new UntypedFormControl(),
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
	
	/**
	* Adds a new Campaign 
	*/
	public addNewCampaign():void{
		
		let name:string 		= this.addCampaignForm.get("name")?.value;
		let description:string 	= this.addCampaignForm.get("description")?.value;
		
		let logo:CampaignLogo = new CampaignLogo(new Array<any>(), 'jpeg');
		
		this.campaignService.addNewCampaign(name, description, logo).subscribe(res => {
			this. fetchExistingCampaigns();
			this.listCampaigns();
		});
		
	}
	
	/**
	* Updates the logo image file for a new Campaign 
	*/
	public updateCampaignImageFile(event:any):void{
	
		if (event.target.files.length <= 0) {
			return;
		}
		
		this.logoImageFile = event.target.files[0];
			
	}
	
	/**
	* Fetch Campaigins the User already has access to 
	*/
	public fetchExistingCampaigns():void{
		this.campaignService.fetchCampaignsForUser().subscribe(campaigns => {
			this.campaigns = campaigns;
		})
	}
	
	
}
