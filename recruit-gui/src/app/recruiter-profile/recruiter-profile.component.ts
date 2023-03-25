import { Component } 								from '@angular/core';
import { PhotoAPIOutbound, RecruiterProfile } 						from './recruiter-profile';
import { RecruiterProfileService} 					from '../recruiter-profile.service';
import { AddRecruiterProfileRequest }				from './add-recruiter-profile'
import { UntypedFormGroup, UntypedFormControl }		from '@angular/forms';
import { TupleStrValueByPos }						from './tuple-string-pos-pipe';

/**
* Component for working with Recruiter Profiles
*/
@Component({
  selector: 'app-recruiter-profile',
  templateUrl: './recruiter-profile.component.html',
  styleUrls: ['./recruiter-profile.component.css']
})
export class RecruiterProfileComponent {

	public recruiterProfile?:RecruiterProfile;
	public recruiterProfiles:Array<RecruiterProfile> = new Array<RecruiterProfile>();
	public currentView = 'view-main'; 
	private imageFile!:File| any;
	
	/**
	* Constructor
	*/
	constructor(private recruierProfileService:RecruiterProfileService){
		this.recruierProfileService.fetchOwnRecruiterProfile().subscribe( response => {
			this.recruiterProfile = response;
		})
		
		this.loadRecuiterProfiles();
	}
	
	/**
	* Fetched recruiter profiles from backend
	*/
	private loadRecuiterProfiles():void{
		this.recruierProfileService.fetchRecruiterProfiles('RECRUITERS').subscribe( profiles => {
			this.recruiterProfiles = profiles;
		});
	}
	
	/**
	* Form to add new Profile
	*/
	public newProfileFormBean:UntypedFormGroup = new UntypedFormGroup({
		recruitsIn: 				new UntypedFormControl(),
		languagesSpoken: 			new UntypedFormControl(),
		visibleToRecruiters: 		new UntypedFormControl(false),
		visibleToCandidates: 		new UntypedFormControl(false),
		visibleToPublic: 			new UntypedFormControl(false),
		jobTitle: 					new UntypedFormControl(),
		yearsExperience: 			new UntypedFormControl(0),
		introduction: 				new UntypedFormControl(),
		sectorItem: 				new UntypedFormControl(),
		coreTechItem:	 			new UntypedFormControl(),
		contractTypeItem: 			new UntypedFormControl(),
		recruiterType: 				new UntypedFormControl('INDEPENDENT'),     	
	});
	
	public recruitsIn:Array<[string,string]> 				= new Array<[string,string]>();
	public languagesSpoken:Array<[string,string]> 			= new Array<[string,string]>();
	public sectors:Array<[string,string]> 					= new Array<[string,string]>();
	public coreTech:Array<[string,string]>		 			= new Array<[string,string]>();
	public contractTypes:Array<[string,string]> 			= new Array<[string,string]>();


	/**
	* Adds item to contractType
	*/
	public addContractTypeItem():void{
		
		let item = this.newProfileFormBean.get('contractTypeItem')?.value;
		
		if (this.contractTypes.indexOf(item) === -1) {
			this.contractTypes.push(item);
		}
	}
	
	/**
	* Removes Item From ContractTypes
	*/
	public removeContractTypeItem(item:[string,string]):void{
		this.contractTypes = this.contractTypes.filter(i => i !== item);
	}	
	
	/**
	* Adds item to coreTech
	*/
	public addCoreTechItem():void{
		
		let item = this.newProfileFormBean.get('coreTechItem')?.value;
		
		if (this.coreTech.indexOf(item) === -1) {
			this.coreTech.push(item);
		}
	}
	
	/**
	* Removes Item From CoreTech
	*/
	public removeCoreTechItem(item:[string,string]):void{
		this.coreTech = this.coreTech.filter(i => i !== item);
	}	
	
	/**
	* Adds item to Sectors
	*/
	public addSectorItem():void{
		
		let item = this.newProfileFormBean.get('sectorItem')?.value;
		
		if (this.sectors.indexOf(item) === -1) {
			this.sectors.push(item);
		}
	}
	
	/**
	* RemovesItemFromRecruitsIn
	*/
	public removeSectorItem(item:[string,string]):void{
		this.sectors = this.sectors.filter(i => i !== item);
	}	
	
	/**
	* Adds item to RecruitsIn
	*/
	public addRecruitsInItem():void{
		
		let item = this.newProfileFormBean.get('recruitsIn')?.value
		
		if (this.recruitsIn.indexOf(item) === -1) {
			this.recruitsIn.push(item);
		}
	}
	
	/**
	* RemovesItemFromRecruitsIn
	*/
	public removeRecruitsInItem(item:[string,string]):void{
		this.recruitsIn = this.recruitsIn.filter(i => i !== item);
	}
	
	/**
	* Adds item to RecruitsIn
	*/
	public addLanguageSpokenItem():void{
		
		let item = this.newProfileFormBean.get('languagesSpoken')?.value;
		
		if (this.languagesSpoken.indexOf(item) === -1) {
			this.languagesSpoken.push(item);
		}
	}
	
	/**
	* RemovesItemFromRecruitsIn
	*/
	public removeLanguageSpokenItem(item:[string,string]):void{
		this.languagesSpoken = this.languagesSpoken.filter(i => i !== item);
	}
	
	/**
	* Whether a recruiter has already added their own profile
	*/
	public isOwnAccountExists():boolean{
		return this.recruiterProfile != null;
	}
	
	/**
	* Switches to view to add own profile
	*/
	public showViewCreate():void{
		this.currentView = 'view-create';
		
		if (this.recruiterProfile){
			
			this.newProfileFormBean.get('recruitsIn')?.setValue(this.recruiterProfile.recruitsIn);
			
			this.newProfileFormBean.get('visibleToRecruiters')?.setValue(this.recruiterProfile.visibleToRecruiters);
			this.newProfileFormBean.get('visibleToCandidates')?.setValue(this.recruiterProfile.visibleToCandidates);
			this.newProfileFormBean.get('visibleToPublic')?.setValue(this.recruiterProfile.visibleToPublic);
			this.newProfileFormBean.get('jobTitle')?.setValue(this.recruiterProfile.jobTitle);
			this.newProfileFormBean.get('yearsExperience')?.setValue(this.recruiterProfile.yearsExperience);
			this.newProfileFormBean.get('introduction')?.setValue(this.recruiterProfile.introduction);
			this.newProfileFormBean.get('recruiterType')?.setValue(this.recruiterProfile.recruiterType);     	
		
			this.languagesSpoken 	= this.getLanguageOptions().filter(i => this.recruiterProfile?.languagesSpoken.includes(i[0]));
			this.recruitsIn 		= this.getCountryOptions().filter(i => this.recruiterProfile?.recruitsIn.includes(i[0]));
			this.sectors 			= this.getSectorOptions().filter(i => this.recruiterProfile?.sectors.includes(i[0]));
			this.coreTech 			= this.getTechOptions().filter(i => this.recruiterProfile?.coreTech.includes(i[0]));
			this.contractTypes 		= this.getContractTypeOptions().filter(i => this.recruiterProfile?.recruitsContractTypes.includes(i[0]));
				
		}
		
	}
	
	/**
	* Switches to view to view all visible Profiles
	*/
	public showViewMain():void{
		this.currentView = 'view-main';
	}
	
	/**
	* Recruiter type options
	*/
	public getRecruitmentTypeOptions():Array<string>{
		
		let options:Array<string> = new Array<string>();
		
		options.push('INDEPENDENT');
		options.push('COMMERCIAL');
		
		return options;
		
	}
	
	public getUserReadableVal(key:string):string{
		
		let firstChar:string = key.substring(0,1);
		let remainder:string = key.substring(1);
		
		remainder = remainder.toLocaleLowerCase();
		  
		
		return (firstChar + remainder);
	}

	/**
	* Switches to view to view all visible Profiles
	*/
	public getCountryOptions():Array<[string,string]>{
		
		let options:Array<[string,string]> = new Array<[string,string]>();
		
		options.push(['',		 		'']);
		options.push(['BELGIUM', 		'Belgium']);
		options.push(['EUROPE', 		'Europe']);
		options.push(['IRELAND', 		'Ireland']);
		options.push(['NETHERLANDS', 	'Netherlands']);
		options.push(['UK', 			'UK']);
		options.push(['WORLD', 			'World']);
		
		return options;
		
	}
	
	/**
	* Spoken Langiage options
	*/
	public getLanguageOptions():Array<[string,string]>{
		
		let options:Array<[string,string]> = new Array<[string,string]>();
		
		options.push(['',		 		'']);
		options.push(['DUTCH', 			'Dutch']);
		options.push(['ENGLISH', 		'English']);
		options.push(['FRENCH', 		'French']);
		
		return options;
		
	}
	
	/**
	* Sector options
	*/
	public getSectorOptions():Array<[string,string]>{
		
		let options:Array<[string,string]> = new Array<[string,string]>();
		
		options.push(['',		 			'']);
		options.push(['AUTOMOBILES', 		'Automotive']);
		options.push(['CYBER_INTEL', 		'Cyber Intelligence']);
		options.push(['EU_INSTITUTIONS', 	'EU Institutions']);
		options.push(['FINTECH', 			'Fintech']);
		options.push(['GAMING', 			'Gaming']);
		options.push(['GOVERNMENT', 		'Government']);
		options.push(['LOGISTICS', 			'Logistics']);
		options.push(['POLICE_AND_DEFENSE', 'Police and Defence']);
		
		return options;
		
	}
	
	/**
	* Tech type options
	*/
	public getTechOptions():Array<[string,string]>{
		
		let options:Array<[string,string]> = new Array<[string,string]>();
		
		options.push(['',		 			'']);
		options.push(['ARCHITECT', 			'Architect']);
		options.push(['BI', 				'Business Intelligence']);
		options.push(['BUSINESS_ANALYSTS', 	'Business Analyst\'s']);
		options.push(['CLOUD', 				'Cloud']);
		options.push(['DEV_OPS', 			'DevOps']);
		options.push(['DOT_NET', 			'DotNet']);
		options.push(['IT_SUPPORT', 		'Support/Helpdesk']);
		options.push(['JAVA', 				'Java']);
		options.push(['NETWORKS', 			'Networks']);
		options.push(['PROJECT_NANAGMENT', 	'Project Managment']);
		options.push(['REC2REC', 			'Rec2Rec']);
		options.push(['SECURITY', 			'Cyber Security']);
		options.push(['TESTING', 			'Testing']);
		options.push(['UI_UX', 				'UI/UX']);
		options.push(['WEB', 				'Web']);
		
		return options;
		
	}
	
	/**
	* Contract Type options
	*/
	public getContractTypeOptions():Array<[string,string]>{
		
		let options:Array<[string,string]> = new Array<[string,string]>();
		
		options.push(['',		 	'']);
		options.push(['FREELANCE', 	'Freelance / Contract']);
		options.push(['PERM', 		'Permanent / Payroll']);
		
		return options;
		
	}
	
		/**
	* Returns the code identifying the country
	* @param country - Country to get the country code for
	*/
	public getCountryFlagClass(country:string):string{
		switch(country){
			case "NETHERLANDS":{
				return "nl";
			}
			case "BELGIUM":{
				return "be";
			}
			case "UK":{
				return "gb";
			}
			case "IRELAND":{
				return "ie";
			}
			case "EUROPE":{
				return "eu";
			}
			case "WORLD":{
				return "we";
			}
			default:{
				return 'NA';
			}
		}

  	}
	
	/**
	* Uploads the file for the Profile Image and stored 
	* it ready to be sent to the backend
	*/
  	public uploadProfileImage(event:any):void{
  
	 	if (event.target.files.length <= 0) {
  			return;
  		}
  	
		this.imageFile = event.target.files[0];
		
		if (this.recruiterProfile && this.recruiterProfile!.profilePhoto){
			this.recruiterProfile.profilePhoto = new PhotoAPIOutbound();	
  		}
  		
	}
	
	public validationErrors:Array<[string,string]> = new Array<[string,string]>();
	
	/**
	* 
	*/
	public saveProfile():void{
		
		let recruiterProfile:AddRecruiterProfileRequest = new AddRecruiterProfileRequest();
		let tupplePipe:TupleStrValueByPos 				= new TupleStrValueByPos();
		
		recruiterProfile.recruitsIn 			= this.recruitsIn.map(i => tupplePipe.transform(i,true));
		recruiterProfile.languagesSpoken 		= this.languagesSpoken.map(i => tupplePipe.transform(i,true));
		recruiterProfile.visibleToRecruiters 	= this.newProfileFormBean.get('visibleToRecruiters')?.value;
		recruiterProfile.visibleToCandidates 	= this.newProfileFormBean.get('visibleToCandidates')?.value;
		recruiterProfile.visibleToPublic 		= this.newProfileFormBean.get('visibleToPublic')?.value;
		recruiterProfile.jobTitle 				= this.newProfileFormBean.get('jobTitle')?.value;
		recruiterProfile.yearsExperience 		= this.newProfileFormBean.get('yearsExperience')?.value;
		recruiterProfile.introduction 			= this.newProfileFormBean.get('introduction')?.value;
		recruiterProfile.sectors 				= this.sectors.map(i => tupplePipe.transform(i,true));
		recruiterProfile.coreTech 				= this.coreTech.map(i => tupplePipe.transform(i,true));
		recruiterProfile.recruitsContractTypes 	= this.contractTypes.map(i => tupplePipe.transform(i,true));
		recruiterProfile.recruiterType 			= this.newProfileFormBean.get('recruiterType')?.value;
		
		if (this.recruiterProfile){
			this.updateProfile(recruiterProfile);
		} else {
			this.createNewProfile(recruiterProfile);
		}
	
		
	}
	
	
	public updateProfile(recruiterProfile:AddRecruiterProfileRequest):void{
		
		this.recruierProfileService.updateProfile(recruiterProfile, this.imageFile).subscribe(response => {
			this.recruierProfileService.fetchOwnRecruiterProfile().subscribe( response => {
				this.recruiterProfile = response;
				this.loadRecuiterProfiles();
				this.showViewMain();
			});
		});
	}
	
	public createNewProfile(recruiterProfile:AddRecruiterProfileRequest):void{
		
		this.recruierProfileService.addProfile(recruiterProfile, this.imageFile).subscribe(response => {
			this.recruierProfileService.fetchOwnRecruiterProfile().subscribe( response => {
				this.recruiterProfile = response;
				this.loadRecuiterProfiles();
				this.showViewMain();
			});
		});
	}
	
}
