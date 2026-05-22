import { Component, EventEmitter, Output } 									from '@angular/core';
import { UntypedFormGroup, UntypedFormControl }								from '@angular/forms';
import { CampaingsService, Campaign, Role, CampaignOverview}				from 'src/app/campaings.service';
import { CurriculumService } 												from 'src/app/curriculum.service';

@Component({
  selector: 'app-selectionbox',
  standalone: false,
  templateUrl: './selectionbox.component.html',
  styleUrl: './selectionbox.component.css'
})
export class SelectionboxComponent {

	@Output() selectedCampaignEmitter 							= new EventEmitter<Campaign>();
	@Output() selectedRoleEmitter 								= new EventEmitter<Role>();
	
	public selectedCampaign:Campaign | undefined;
	public selectedRole:Role | undefined;
	
	/**
	* Constructor
	* @param campaignService - Services for interacting with Campaigns
	*/
	constructor(private readonly campaignService:CampaingsService){
		this.fetchExistingCampaigns();
	}
	
	/**
	* Controls relating to visibility 
	*/
	public showCampaignSelectionList:boolean 		= true;
	public showAddCampaignForm:boolean 				= false;
	public showRoleSelectionList:boolean			= false;
	public showAddRoleForm:boolean					= false;
	
	/**
	* Image log  
	*/
	private logoImageFile!:File | undefined;
	
	/**
	* Campaigins 
	*/
	public campaigns:Array<CampaignOverview> = new Array<CampaignOverview>();
	
	/**
	* Forms 
	*/
	public addCampaignForm:UntypedFormGroup = new UntypedFormGroup({
		name: 			new UntypedFormControl(),
		description: 	new UntypedFormControl()
	});
	
	public addRoleForm:UntypedFormGroup = new UntypedFormGroup({
			name: 			new UntypedFormControl(),
			description: 	new UntypedFormControl(),
	});
	
	/**
	* Shows list of exisign Campaigns and allows the 
	* User to select a Campaign to add the Candidate to 
 	*/
	public addCampaign():void{
		this.showAddCampaign();
	}
	
	/**
	* Shows a form to allow the User to create a new Campaign which
	* the Candidate will be added to 
	*/
	public listCampaigns():void{
		this.fetchExistingCampaigns();
		this.showCampaignSelectionList = true;
		this.showAddCampaignForm = false;
		this.resetAddCampaignForm();
		this.selectedCampaign 	= undefined;
		this.selectedRole 		= undefined;
		this.showCampaignList();
	}
	
	/**
	* Adds a new Campaign 
	*/
	public addNewCampaign():void{
		
		let name:string 		= this.addCampaignForm.get("name")?.value;
		let description:string 	= this.addCampaignForm.get("description")?.value;
		
		this.campaignService.addNewCampaign(name, description, this.logoImageFile).subscribe(res => {
			this.fetchExistingCampaigns();
			this.listCampaigns();
			this.resetAddCampaignForm();
		});
		
	}
	
	/**
	* Adds a new Role to the currently selected Campaign 
	*/
	public addNewRole():void{
		
		let name:string 		= this.addRoleForm.get("name")?.value;
		let description:string 	= this.addRoleForm.get("description")?.value;
		
		this.campaignService.addNewRole(''+this.selectedCampaign?.id, name, description).subscribe(res => {
			//this.fetchExistingCampaigns();
			this.selectCampaignById(""+this.selectedCampaign?.id);
			this.showRoleList()
			this.resetAddCampaignForm();
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
			this.campaigns.sort((a,b) => a.name > b.name ? 0 : -1);
		})
	}
	
	/**
	* Resets the values in the addCampaiginForm and associated logo if present
	*/
	private resetAddCampaignForm():void{
		this.addCampaignForm = new UntypedFormGroup({
			name: 			new UntypedFormControl(),
			description: 	new UntypedFormControl()
		});
			
		this.logoImageFile = undefined;
	}
	
	/**
	* Switches view to the Campaign list view
	*/
	public showCampaignList():void{
		this.showCampaignSelectionList 		= true;
		this.showAddCampaignForm 			= false;
		this.showRoleSelectionList			= false;
		this.showAddRoleForm				= false;
		this.selectedCampaign 				= undefined;
		this.selectedCampaignEmitter.emit(undefined);
		this.selectedRoleEmitter.emit(undefined);
	}
	
	/**
	* Switches view to the Role list view
	*/
	public showRoleList():void{
		this.showCampaignSelectionList 		= false;
		this.showAddCampaignForm 			= false;
		this.showRoleSelectionList			= true;
		this.showAddRoleForm				= false;		
	}

	/**
	* Switches view to the Add Campaign view
	*/
	public showAddCampaign():void{
		this.showCampaignSelectionList 		= false;
		this.showAddCampaignForm 			= true;
		this.showRoleSelectionList			= false;
		this.showAddRoleForm				= false;
	}
	
	/**
	* Switches view to the Add Role view
	*/
	public showAddRole():void{
		this.showCampaignSelectionList 		= false;
		this.showAddCampaignForm 			= false;
		this.showRoleSelectionList			= false;
		this.showAddRoleForm				= true;
	}
		
	/**
	* Handles event in which a Campaign is selected. Fetches the 
	* Full Campaign from the backend and updates the parent component
	* @param campaignOverview - Selected Campaign 
	*/
	public selectCampaign(campaignOverview:CampaignOverview):void{
		this.selectCampaignById(""+campaignOverview?.id);
	}
	
	public selectCampaignById(id:string):void{
			
		this.campaignService.fetchCampaign(id).subscribe(campaign => {
			this.selectedCampaign = campaign;
			this.selectedCampaignEmitter.emit(campaign);
			this.selectedRoleEmitter.emit(undefined);
			this.selectedCampaign.roles = this.selectedCampaign.roles.sort((a,b) => a.name > b.name ? 0 : -1);
			this.showRoleList();
		});
		
	}
	
	/**
	* Handles event in which a Role is selected. 
	* @param role - Selected Role 
	*/
	public selectRole(role:Role):void{
		this.selectedRole = role;
		this.selectedRoleEmitter.emit(role);
		this.showRoleList();
		
	}
	
}