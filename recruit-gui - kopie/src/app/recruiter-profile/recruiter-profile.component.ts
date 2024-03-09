import { Component, ViewChild } 								from '@angular/core';
import { PhotoAPIOutbound, RecruiterProfile } 		from './recruiter-profile';
import { RecruiterProfileService} 					from '../recruiter-profile.service';
import { AddRecruiterProfileRequest }				from './add-recruiter-profile'
import { UntypedFormGroup, UntypedFormControl }		from '@angular/forms';
import { TupleStrValueByPos }						from './tuple-string-pos-pipe';
import { EmailService, EmailRequest }				from '../email.service';
import { NgbModal, NgbModalOptions}					from '@ng-bootstrap/ng-bootstrap';
import { InfoItemBlock, InfoItemConfig, InfoItemRowKeyValue } from '../candidate-info-box/info-item';
import { EnumToHumanReadableValue } from './enum-to-hr-pipe';
import { TranslateService } from '@ngx-translate/core';
/**
* Component for working with Recruiter Profiles
*/
@Component({
  selector: 'app-recruiter-profile',
  templateUrl: './recruiter-profile.component.html',
  styleUrls: ['./recruiter-profile.component.css']
})
export class RecruiterProfileComponent {

	@ViewChild('contactBox', { static: false }) private contactBox:any;
	
	public recruiterProfile?:RecruiterProfile;
	public recruiterProfiles:Array<RecruiterProfile> 				= new Array<RecruiterProfile>();
	public selectedRecruiterProfile:RecruiterProfile 				= new RecruiterProfile();
	public currentView = 'view-main'; 
	private imageFile!:File| any;
	public infoItemConfig:InfoItemConfig 							= new InfoItemConfig();
	
	public recruitmentTypeOptions:Array<string> = new Array<string>();
	public countryOptions:Array<[string,string]> = new Array<[string,string]>();
	public languageOptions:Array<[string,string]> = new Array<[string,string]>();
	public sectorOptions:Array<[string,string]> = new Array<[string,string]>();
	public techOptions:Array<[string,string]> = new Array<[string,string]>();
	public contractTypeOptions:Array<[string,string]> = new Array<[string,string]>();
	
	/**
	* Constructor
	*/
	constructor(private recruierProfileService:RecruiterProfileService, private emailService:EmailService, private modalService:NgbModal, private translate:TranslateService){
		this.recruierProfileService.fetchOwnRecruiterProfile().subscribe( response => {
			this.recruiterProfile = response;
			
			this.recruitmentTypeOptions 	= this.getRecruitmentTypeOptions();
			this.countryOptions 			= this.getCountryOptions();
			this.languageOptions 			= this.getLanguageOptions();
			this.sectorOptions 				= this.getSectorOptions();
			this.techOptions 				= this.getTechOptions();
			this.contractTypeOptions 		= this.getContractTypeOptions();
			
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
	
	public showViewDetails(recruiterProfile:RecruiterProfile):void{
		
		this.currentView = 'view-details';
		
		this.selectedRecruiterProfile = recruiterProfile;
		
		let enumPipe:EnumToHumanReadableValue = new EnumToHumanReadableValue();
		
		this.infoItemConfig = new InfoItemConfig();
		this.infoItemConfig.setProfilePhoto(this.selectedRecruiterProfile?.profilePhoto?.imageBytes);
		this.infoItemConfig.setShowContactButton(true);
		
		let recruiterBlock:InfoItemBlock = new InfoItemBlock();
		
		recruiterBlock.addRow(new InfoItemRowKeyValue(this.translate.instant('rec-prof-info-item-company'),enumPipe.transform(this.selectedRecruiterProfile.companyName)));
		recruiterBlock.addRow(new InfoItemRowKeyValue(this.translate.instant('rec-prof-info-item-type'),enumPipe.transform(this.selectedRecruiterProfile.recruiterType)));
		recruiterBlock.addRow(new InfoItemRowKeyValue(this.translate.instant('rec-prof-info-item-years-exp'),""+this.selectedRecruiterProfile.yearsExperience));
		this.infoItemConfig.addItem(recruiterBlock);
		
		
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
		options.push(['BELGIUM', 		this.translate.instant('rec-prof-country-belgium')]);
		options.push(['EUROPE', 		this.translate.instant('rec-prof-country-europe')]);
		options.push(['IRELAND', 		this.translate.instant('rec-prof-country-ireland')]);
		options.push(['NETHERLANDS', 	this.translate.instant('rec-prof-country-netherlands')]);
		options.push(['UK', 			this.translate.instant('rec-prof-country-uk')]);
		options.push(['WORLD', 			this.translate.instant('rec-prof-country-world')]);
		
		return options;
		
	}
	
	/**
	* Spoken Langiage options
	*/
	public getLanguageOptions():Array<[string,string]>{
		
		let options:Array<[string,string]> = new Array<[string,string]>();
		
		options.push(['',		 		'']);
		options.push(['DUTCH', 			this.translate.instant('rec-prof-lang-dutch')]);
		options.push(['ENGLISH', 		this.translate.instant('rec-prof-lang-english')]);
		options.push(['FRENCH', 		this.translate.instant('rec-prof-lang-french')]);
		
		return options;
		
	}
	
	/**
	* Human readable language value
	*/
	public getLang(langCode:string):string{
		return this.getLanguageOptions().filter(opt => opt[0] == langCode)[0][1];
	}
	
	/**
	* Sector options
	*/
	public getSectorOptions():Array<[string,string]>{
		
		let options:Array<[string,string]> = new Array<[string,string]>();
		
		options.push(['',		 			'']);
		options.push(['AUTOMOBILES', 		this.translate.instant('rec-prof-sector-automitive')]);
		options.push(['CYBER_INTEL', 		this.translate.instant('rec-prof-sector-cyber-intelligence')]);
		options.push(['EU_INSTITUTIONS', 	this.translate.instant('rec-prof-sector-eu-institutions')]);
		options.push(['FINTECH', 			this.translate.instant('rec-prof-sector-fintech')]);
		options.push(['GAMING', 			this.translate.instant('rec-prof-sector-gaming')]);
		options.push(['GOVERNMENT', 		this.translate.instant('rec-prof-sector-government')]);
		options.push(['LOGISTICS', 			this.translate.instant('rec-prof-sector-logistice')]);
		options.push(['POLICE_AND_DEFENSE', this.translate.instant('rec-prof-sector-police-and-defence')]);
		
		return options;
		
	}
	
	/**
	* Human readable sector value
	*/
	public getSector(sectorCode:string):string{
		return this.getSectorOptions().filter(opt => opt[0] == sectorCode)[0][1];
	}
	
	/**
	* Tech type options
	*/
	public getTechOptions():Array<[string,string]>{
		
		let options:Array<[string,string]> = new Array<[string,string]>();
		
		options.push(['',		 			'']);
		options.push(['ARCHITECT', 			this.translate.instant('rec-prof-func-architect')]);
		options.push(['BI', 				this.translate.instant('rec-prof-func-business-intelligence')]);
		options.push(['BUSINESS_ANALYSTS', 	this.translate.instant('rec-prof-func-busines-analyst')]);
		options.push(['CLOUD', 				this.translate.instant('rec-prof-func-cloud')]);
		options.push(['DEV_OPS', 			this.translate.instant('rec-prof-func-devops')]);
		options.push(['DOT_NET', 			this.translate.instant('rec-prof-func-dotnet')]);
		options.push(['IT_SUPPORT', 		this.translate.instant('rec-prof-func-support-helpdesk')]);
		options.push(['JAVA', 				this.translate.instant('rec-prof-func-java')]);
		options.push(['NETWORKS', 			this.translate.instant('rec-prof-func-networks')]);
		options.push(['PROJECT_NANAGMENT', 	this.translate.instant('rec-prof-func-project-management')]);
		options.push(['REC2REC', 			this.translate.instant('rec-prof-func-rec2rec')]);
		options.push(['SECURITY', 			this.translate.instant('rec-prof-func-cyber-security')]);
		options.push(['TESTING', 			this.translate.instant('rec-prof-func-testing')]);
		options.push(['UI_UX', 				this.translate.instant('rec-prof-func-ui-ux')]);
		options.push(['WEB', 				this.translate.instant('rec-prof-func-web')]);
		
		return options;
		
	}
	
	/**
	* Human readable tech value
	*/
	public getTech(techCode:string):string{
		return this.getTechOptions().filter(opt => opt[0] == techCode)[0][1];
	}
	
	/**
	* Contract Type options
	*/
	public getContractTypeOptions():Array<[string,string]>{
		
		let options:Array<[string,string]> = new Array<[string,string]>();
		
		options.push(['',		 	'']);
		options.push(['FREELANCE', 	this.translate.instant('rec-prof-type-freelance-contract')]);
		options.push(['PERM', 		this.translate.instant('rec-prof-type-permanent-payroll')]);
		
		return options;
		
	}
	
	
	/**
	* Human readable contractType value
	*/
	public getContractType(conrractTypeCode:string):string{
		return this.getContractTypeOptions().filter(opt => opt[0] == conrractTypeCode)[0][1];
	}
	
	//TODO: Need to get Values from options for HTML
	
	
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
	
	/**
	* Saves / Updates own Profile
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
	
	public contactRecruiterView:string = 'message';
	
	/**
	*  Closes the confirm popup
	*/
	public closeModal(): void {
		this.modalService.dismissAll();
	}
	
	/**
	* Opend dialog to contact recuiter posting	
	*/
	public contactRecruiter():void{
		
		this.contactRecruiterView = 'message';
	
		this.contactBox.nativeElement.showModal();
	
	}
	
	/**
	* Forms group for sending a message to a Recruiter relating to a
	* specific post on the jobboard 
	*/
	public sendMessageGroup:UntypedFormGroup = new UntypedFormGroup({
		message:				new UntypedFormControl(''),
		title:				new UntypedFormControl('')
	});
	
	/**
	* Opend dialog to contact recuiter posting	
	*/
	public sendMessageToRecruiter():void{
		
		let emailRequest:EmailRequest = new EmailRequest();
		
		emailRequest.title = this.sendMessageGroup.get('title')?.value;;
		emailRequest.message = this.sendMessageGroup.get('message')?.value;;
		
		this.emailService.sendRecruiterContactEmail(emailRequest, this.selectedRecruiterProfile.recruiterId).subscribe(body => {
			this.contactRecruiterView = this.translate.instant('success');
			this.sendMessageGroup = new UntypedFormGroup({
				message: new UntypedFormControl(''),
				title: new UntypedFormControl(''),
			});
		}, err => {
			this.contactRecruiterView = this.translate.instant('failure');
		});
		
		
	}
	
}
