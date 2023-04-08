import { Component } 										from '@angular/core';
import { PhotoAPIOutbound, CandidateProfile, Language } 	from './candidate-profile';
import { CandidateProfileService} 							from '../candidate-profile-service.service';
import { CandidateServiceService} 							from '../candidate-service.service';
import { AddCandidateProfileRequest }						from './add-candidate-profile'
import { UntypedFormGroup, UntypedFormControl }				from '@angular/forms';
import { TupleStrValueByPos }								from './tuple-string-pos-pipe';
import { EmailService, EmailRequest }						from '../email.service';
import { NgbModal, NgbModalOptions}							from '@ng-bootstrap/ng-bootstrap';
import { CandidateFunction }								from '../candidate-function';
import { UpdateCandidateProfileRequest, LanguageOption}		from './update-candidate-profile-req';

@Component({
  selector: 'app-candidate-profile',
  templateUrl: './candidate-profile.component.html',
  styleUrls: ['./candidate-profile.component.css']
})
export class CandidateProfileComponent {
	public candidateProfile?:CandidateProfile;
	public candiadteProfiles:Array<CandidateProfile> = new Array<CandidateProfile>();
	public selectedCandidateProfile:CandidateProfile = new CandidateProfile();
	public currentView = 'view-main'; 
	private imageFile!:File| any;
	public functionTypes:	 	Array<CandidateFunction> 	= new Array<CandidateFunction>();
	
	/**
	* Constructor
	*/
	constructor(private candidateService:CandidateServiceService, private emailService:EmailService, private modalService:NgbModal){
		
		this.candidateService.loadFunctionTypes().forEach(funcType => {
      		this.functionTypes.push(funcType);
    	});

		if(this.isCandidate()){
			this.candidateService.getCandidateById(this.getOwnUserId()).subscribe( candidate => {
				this.candidateProfile = candidate;
				
				let lvlEnglish 	= 'UNKNOWN';
				let lvlDutch 	= 'UNKNOWN';
				let lvlFrench 	= 'UNKNOWN';
				
				if(candidate.languages.filter(l => l.language === 'ENGLISH').length === 1){
					lvlEnglish 	= candidate.languages.filter(l => l.language === 'ENGLISH')[0].level;
				}
				
				if(candidate.languages.filter(l => l.language === 'DUTCH').length === 1){
					lvlDutch 	= candidate.languages.filter(l => l.language === 'DUTCH')[0].level;
				}
				if(candidate.languages.filter(l => l.language === 'FRENCH').length === 1){
					lvlFrench 	= candidate.languages.filter(l => l.language === 'FRENCH')[0].level;
				}
			
				this.candidateProfileFormBean = new UntypedFormGroup({
					introduction: 				new UntypedFormControl(this.candidateProfile.introduction),
					candidateId: 				new UntypedFormControl(this.candidateProfile.candidateId),
					firstname: 					new UntypedFormControl(this.candidateProfile.firstname),
					surname: 					new UntypedFormControl(this.candidateProfile.surname),
					email: 						new UntypedFormControl(this.candidateProfile.email),
					function: 					new UntypedFormControl(this.candidateProfile.function),
					roleSought: 				new UntypedFormControl(this.candidateProfile.roleSought),
					country: 					new UntypedFormControl(this.candidateProfile.country),
					city: 						new UntypedFormControl(this.candidateProfile.city),
					perm: 						new UntypedFormControl(this.candidateProfile.perm),
					freelance: 					new UntypedFormControl(this.candidateProfile.freelance),
					yearsExperience: 			new UntypedFormControl(this.candidateProfile.yearsExperience),
					available: 					new UntypedFormControl(this.candidateProfile.available),
					english: 					new UntypedFormControl(lvlEnglish),
					dutch: 						new UntypedFormControl(lvlDutch),
					french: 					new UntypedFormControl(lvlFrench)
				});
				
				this.currentView = 'view-edit';
			})
		}
	}
	
	/**
	* Fetched recruiter profiles from backend
	*/
	//private loadRecuiterProfiles():void{
	//	this.recruierProfileService.fetchRecruiterProfiles('RECRUITERS').subscribe( profiles => {
	//		this.recruiterProfiles = profiles;
	//	});
	//}
	
	/**
	* Form to add new Profile
	*/
	public candidateProfileFormBean:UntypedFormGroup = new UntypedFormGroup({
		introduction: 				new UntypedFormControl('Boop de boop'),
		candidateId: 				new UntypedFormControl(1234),
		firstname: 					new UntypedFormControl('Kevin'),
		surname: 					new UntypedFormControl('Parkings'),
		email: 						new UntypedFormControl(),
		function: 					new UntypedFormControl(),
		roleSought: 				new UntypedFormControl('Java Developer'),
		country: 					new UntypedFormControl('1'),
		city: 						new UntypedFormControl('2'),
		perm: 						new UntypedFormControl('2'),
		freelance: 					new UntypedFormControl('2'),
		yearsExperience: 			new UntypedFormControl(2),
		available: 					new UntypedFormControl(true),
		lastAvailabilityCheck: 		new UntypedFormControl(new Date()),
		english: 					new UntypedFormControl(),
		dutch: 						new UntypedFormControl(),
		french: 					new UntypedFormControl(),
	});
	
	public recruitsIn:Array<[string,string]> 				= new Array<[string,string]>();
	public languagesSpoken:Array<[string,string]> 			= new Array<[string,string]>();
	public sectors:Array<[string,string]> 					= new Array<[string,string]>();
	public coreTech:Array<[string,string]>		 			= new Array<[string,string]>();
	public contractTypes:Array<[string,string]> 			= new Array<[string,string]>();

	/**
	* Sends request to update the Candidate
	*/
	public updateCandidate():void{
		
		let english:LanguageOption = new LanguageOption();
		english.language = "ENGLISH";
		english.level = this.candidateProfileFormBean.get('english')?.value;
		
		let dutch:LanguageOption = new LanguageOption();
		dutch.language = "DUTCH";
		dutch.level = this.candidateProfileFormBean.get('dutch')?.value;
		
		let french:LanguageOption = new LanguageOption();
		french.language = "FRENCH";
		french.level = this.candidateProfileFormBean.get('french')?.value;
		
		let languages:Array<LanguageOption> = new Array<LanguageOption>();
		languages.push(english);
		languages.push(dutch);
		languages.push(french);
		
		let updateRequest:UpdateCandidateProfileRequest = new UpdateCandidateProfileRequest();
		
		updateRequest.firstname 		= this.candidateProfileFormBean.get('firstname')?.value;;
		updateRequest.surname 			= this.candidateProfileFormBean.get('surname')?.value;;
		updateRequest.email 			= this.candidateProfileFormBean.get('email')?.value;;
		updateRequest.roleSought 		= this.candidateProfileFormBean.get('roleSought')?.value;;
		updateRequest.function 			= this.candidateProfileFormBean.get('function')?.value;;
		updateRequest.country 			= this.candidateProfileFormBean.get('country')?.value;;
		updateRequest.city 				= this.candidateProfileFormBean.get('city')?.value;;
		updateRequest.perm 				= this.candidateProfileFormBean.get('perm')?.value;;
		updateRequest.freelance 		= this.candidateProfileFormBean.get('freelance')?.value;;
		updateRequest.yearsExperience 	= this.candidateProfileFormBean.get('yearsExperience')?.value;;
		updateRequest.available 		= this.candidateProfileFormBean.get('available')?.value;
		updateRequest.languages 		= languages;
		
		this.candidateService.updateCandidate(this.getOwnUserId(), updateRequest).subscribe(res => {
			
		}, err => {
			console.log("Failed to update Candidate");
		});
		
	}

	/**
	* Adds item to contractType
	*/
	//public addContractTypeItem():void{
		
	//	let item = this.newProfileFormBean.get('contractTypeItem')?.value;
		
	//	if (this.contractTypes.indexOf(item) === -1) {
	//		this.contractTypes.push(item);
	//	}
	//}
	
	/**
	* Removes Item From ContractTypes
	*/
	//public removeContractTypeItem(item:[string,string]):void{
	//	this.contractTypes = this.contractTypes.filter(i => i !== item);
	//}	
	
	/**
	* Adds item to coreTech
	*/
	//public addCoreTechItem():void{
		
	//	let item = this.newProfileFormBean.get('coreTechItem')?.value;
	//	
	//	if (this.coreTech.indexOf(item) === -1) {
	//		this.coreTech.push(item);
	//	}
	//}
	
	/**
	* Removes Item From CoreTech
	*/
	//public removeCoreTechItem(item:[string,string]):void{
	//	this.coreTech = this.coreTech.filter(i => i !== item);
	//}	
	
	/**
	* Adds item to Sectors
	*/
	//public addSectorItem():void{
		
	//	let item = this.newProfileFormBean.get('sectorItem')?.value;
		
	//	if (this.sectors.indexOf(item) === -1) {
	//		this.sectors.push(item);
	//	}
	//}
	
	/**
	* RemovesItemFromRecruitsIn
	*/
	//public removeSectorItem(item:[string,string]):void{
	//	this.sectors = this.sectors.filter(i => i !== item);
	//}	
	
	/**
	* Adds item to RecruitsIn
	*/
	//public addRecruitsInItem():void{
		
	//	let item = this.newProfileFormBean.get('recruitsIn')?.value
		
	//	if (this.recruitsIn.indexOf(item) === -1) {
	//		this.recruitsIn.push(item);
	//	}
	//}
	
	/**
	* RemovesItemFromRecruitsIn
	*/
	//public removeRecruitsInItem(item:[string,string]):void{
	//	this.recruitsIn = this.recruitsIn.filter(i => i !== item);
	//}
	
	/**
	* Adds item to RecruitsIn
	*/
	//public addLanguageSpokenItem():void{
		
	//	let item = this.newProfileFormBean.get('languagesSpoken')?.value;
		
	//	if (this.languagesSpoken.indexOf(item) === -1) {
	//		this.languagesSpoken.push(item);
	//	}
	//}
	
	/**
	* RemovesItemFromRecruitsIn
	*/
	//public removeLanguageSpokenItem(item:[string,string]):void{
	//	this.languagesSpoken = this.languagesSpoken.filter(i => i !== item);
	//}
	
	/**
	* Whether a recruiter has already added their own profile
	*/
	public isOwnAccountExists():boolean{
		return this.candidateProfile != null;
	}
	
	/**
	* Switches to view to add own profile
	*/
	public showViewEdit():void{
		
		this.currentView = 'view-edit';
		
		if (this.candidateProfile){
			
			//this.newProfileFormBean.get('recruitsIn')?.setValue(this.recruiterProfile.recruitsIn);
			
			//this.newProfileFormBean.get('visibleToRecruiters')?.setValue(this.recruiterProfile.visibleToRecruiters);
			//this.newProfileFormBean.get('visibleToCandidates')?.setValue(this.recruiterProfile.visibleToCandidates);
			//this.newProfileFormBean.get('visibleToPublic')?.setValue(this.recruiterProfile.visibleToPublic);
			//this.newProfileFormBean.get('jobTitle')?.setValue(this.recruiterProfile.jobTitle);
			//this.newProfileFormBean.get('yearsExperience')?.setValue(this.recruiterProfile.yearsExperience);
			//this.newProfileFormBean.get('introduction')?.setValue(this.recruiterProfile.introduction);
			//this.newProfileFormBean.get('recruiterType')?.setValue(this.recruiterProfile.recruiterType);     	
		
			//this.languagesSpoken 	= this.getLanguageOptions().filter(i => this.recruiterProfile?.languagesSpoken.includes(i[0]));
			//this.recruitsIn 		= this.getCountryOptions().filter(i => this.recruiterProfile?.recruitsIn.includes(i[0]));
			//this.sectors 			= this.getSectorOptions().filter(i => this.recruiterProfile?.sectors.includes(i[0]));
			//this.coreTech 			= this.getTechOptions().filter(i => this.recruiterProfile?.coreTech.includes(i[0]));
			//this.contractTypes 		= this.getContractTypeOptions().filter(i => this.recruiterProfile?.recruitsContractTypes.includes(i[0]));
				
		}
		
	}
	
	public showViewDetails(candidateProfile:CandidateProfile):void{
		
		this.currentView = 'view-details';
		
		this.selectedCandidateProfile = candidateProfile;
	}
	
	/**
	* Switches to view to view all visible Profiles
	*/
	//public showViewMain():void{
	//	this.currentView = 'view-main';
	//}
	
	/**
	* Recruiter type options
	*/
	//public getRecruitmentTypeOptions():Array<string>{
		
	//	let options:Array<string> = new Array<string>();
		
	//	options.push('INDEPENDENT');
	//	options.push('COMMERCIAL');
		
	//	return options;
		
	//}
	
	public getUserReadableVal(key:string):string{
		
		let firstChar:string = key.substring(0,1);
		let remainder:string = key.substring(1);
		
		remainder = remainder.toLocaleLowerCase();
		  
		
		return (firstChar + remainder);
	}

	/**
	* Switches to view to view all visible Profiles
	*/
	//public getCountryOptions():Array<[string,string]>{
		
	//	let options:Array<[string,string]> = new Array<[string,string]>();
		
	//	options.push(['',		 		'']);
	//	options.push(['BELGIUM', 		'Belgium']);
	//	options.push(['EUROPE', 		'Europe']);
	//	options.push(['IRELAND', 		'Ireland']);
	//	options.push(['NETHERLANDS', 	'Netherlands']);
	//	options.push(['UK', 			'UK']);
	//	options.push(['WORLD', 			'World']);
		
	//	return options;
		
	//}
	
	/**
	* Spoken Langiage options
	*/
	//public getLanguageOptions():Array<[string,string]>{
		
	//	let options:Array<[string,string]> = new Array<[string,string]>();
		
	//	options.push(['',		 		'']);
	//	options.push(['DUTCH', 			'Dutch']);
	//	options.push(['ENGLISH', 		'English']);
	//	options.push(['FRENCH', 		'French']);
		
	//	return options;
		
	//}
	
	/**
	* Sector options
	*/
	//public getSectorOptions():Array<[string,string]>{
		
	//	let options:Array<[string,string]> = new Array<[string,string]>();
		
	//	options.push(['',		 			'']);
	//	options.push(['AUTOMOBILES', 		'Automotive']);
	//	options.push(['CYBER_INTEL', 		'Cyber Intelligence']);
	//	options.push(['EU_INSTITUTIONS', 	'EU Institutions']);
	//	options.push(['FINTECH', 			'Fintech']);
	//	options.push(['GAMING', 			'Gaming']);
	//	options.push(['GOVERNMENT', 		'Government']);
	//	options.push(['LOGISTICS', 			'Logistics']);
	//	options.push(['POLICE_AND_DEFENSE', 'Police and Defence']);
		
	//	return options;
		
	//}
	
	/**
	* Tech type options
	*/
	//public getTechOptions():Array<[string,string]>{
	//	
	//	let options:Array<[string,string]> = new Array<[string,string]>();
	//	
	//	options.push(['',		 			'']);
	//	options.push(['ARCHITECT', 			'Architect']);
	//	options.push(['BI', 				'Business Intelligence']);
	//	options.push(['BUSINESS_ANALYSTS', 	'Business Analyst\'s']);
	//	options.push(['CLOUD', 				'Cloud']);
	//	options.push(['DEV_OPS', 			'DevOps']);
	//	options.push(['DOT_NET', 			'DotNet']);
	//	options.push(['IT_SUPPORT', 		'Support/Helpdesk']);
	//	options.push(['JAVA', 				'Java']);
	//	options.push(['NETWORKS', 			'Networks']);
	//	options.push(['PROJECT_NANAGMENT', 	'Project Managment']);
	//	options.push(['REC2REC', 			'Rec2Rec']);
	//	options.push(['SECURITY', 			'Cyber Security']);
	//	options.push(['TESTING', 			'Testing']);
	//	options.push(['UI_UX', 				'UI/UX']);
	//	options.push(['WEB', 				'Web']);
		
	//	return options;
		
	//}
	
	/**
	* Contract Type options
	*/
	//public getContractTypeOptions():Array<[string,string]>{
		
	//	let options:Array<[string,string]> = new Array<[string,string]>();
		
	//	options.push(['',		 	'']);
	//	options.push(['FREELANCE', 	'Freelance / Contract']);
	//	options.push(['PERM', 		'Permanent / Payroll']);
		
	//	return options;
		
	//}
	
		/**
	* Returns the code identifying the country
	* @param country - Country to get the country code for
	*/
	//public getCountryFlagClass(country:string):string{
	//	switch(country){
	//		case "NETHERLANDS":{
	//			return "nl";
	//		}
	//		case "BELGIUM":{
	//			return "be";
	//		}
	//		case "UK":{
	//			return "gb";
	//		}
	//		case "IRELAND":{
	//			return "ie";
	//		}
	//		case "EUROPE":{
	//			return "eu";
	//		}
	//		case "WORLD":{
	//			return "we";
	//		}
	//		default:{
	//			return 'NA';
	//		}
	//	}//

//  	}
	
	/**
	* Uploads the file for the Profile Image and stored 
	* it ready to be sent to the backend
	*/
  	public uploadProfileImage(event:any):void{
  
	 	if (event.target.files.length <= 0) {
  			return;
  		}
  	
		this.imageFile = event.target.files[0];
		
		if (this.candidateProfile && this.candidateProfile!.profilePhoto){
			this.candidateProfile.profilePhoto = new PhotoAPIOutbound();	
  		}
  		
	}
	
	/**
	* Saves / Updates own Profile
	*/
	public saveProfile():void{
		
		let candidateProfile:AddCandidateProfileRequest = new AddCandidateProfileRequest();
		let tupplePipe:TupleStrValueByPos 				= new TupleStrValueByPos();
		
		//recruiterProfile.recruitsIn 			= this.recruitsIn.map(i => tupplePipe.transform(i,true));
		//recruiterProfile.languagesSpoken 		= this.languagesSpoken.map(i => tupplePipe.transform(i,true));
		//recruiterProfile.visibleToRecruiters 	= this.newProfileFormBean.get('visibleToRecruiters')?.value;
		//recruiterProfile.visibleToCandidates 	= this.newProfileFormBean.get('visibleToCandidates')?.value;
		//recruiterProfile.visibleToPublic 		= this.newProfileFormBean.get('visibleToPublic')?.value;
		//recruiterProfile.jobTitle 				= this.newProfileFormBean.get('jobTitle')?.value;
		//recruiterProfile.yearsExperience 		= this.newProfileFormBean.get('yearsExperience')?.value;
		//recruiterProfile.introduction 			= this.newProfileFormBean.get('introduction')?.value;
		//recruiterProfile.sectors 				= this.sectors.map(i => tupplePipe.transform(i,true));
		//recruiterProfile.coreTech 				= this.coreTech.map(i => tupplePipe.transform(i,true));
		//recruiterProfile.recruitsContractTypes 	= this.contractTypes.map(i => tupplePipe.transform(i,true));
		//recruiterProfile.recruiterType 			= this.newProfileFormBean.get('recruiterType')?.value;
		
		if (this.candidateProfile){
			this.updateProfile(candidateProfile);
		} else {
			this.createNewProfile(candidateProfile);
		}
	
	}
	
	public updateProfile(candidateProfile:AddCandidateProfileRequest):void{
		
		//this.candidateProfileService.updateProfile(candidateProfile, this.imageFile).subscribe(response => {
		//	this.candidateProfileService.fetchOwnCanddateProfile().subscribe( response => {
		//		this.candidateProfile = response;
		//		this.showViewMain();
		//	});
		//});
	}
	
	public createNewProfile(candidateProfile:AddCandidateProfileRequest):void{
		
		//this.candidateProfileService.addProfile(candidateProfile, this.imageFile).subscribe(response => {
		//	this.candidateProfileService.fetchOwnCandidateProfile().subscribe( response => {
		//		this.candidateProfile = response;
		//		this.showViewMain();
		//	});
		//});
	}
	
	public contactCandidateView:string = 'message';
	
	/**
	*  Closes the confirm popup
	*/
	public closeModal(): void {
		this.modalService.dismissAll();
	}
	
	/**
	* Opend dialog to contact recuiter posting	
	*/
	public contactCandidate(contactBox:any):void{
		
		this.contactCandidateView = 'message';
		let options: NgbModalOptions = {
	    	 centered: true
	   };

		this.modalService.open(contactBox, options);
	
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
	public sendMessageToCandidate():void{
		
		let emailRequest:EmailRequest = new EmailRequest();
		
		emailRequest.title = this.sendMessageGroup.get('title')?.value;;
		emailRequest.message = this.sendMessageGroup.get('message')?.value;;
		
		this.emailService.sendRecruiterContactEmail(emailRequest, this.selectedCandidateProfile.candidateId).subscribe(body => {
			this.contactCandidateView = 'success';
			this.sendMessageGroup = new UntypedFormGroup({
				message: new UntypedFormControl(''),
				title: new UntypedFormControl(''),
			});
		}, err => {
			this.contactCandidateView = 'failure';
		});
		
		
	}
	
	/**
	* Whether or not the User is a Candidate
	*/
	public isCandidate():boolean{
		return sessionStorage.getItem('isCandidate') === 'true';
	}

	/**
	* Returns the curren users id
	*/
	public getOwnUserId():string{
		return sessionStorage.getItem("userId")+'';
	}

}
