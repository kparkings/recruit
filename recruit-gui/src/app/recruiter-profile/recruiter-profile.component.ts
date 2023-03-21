import { Component } 							from '@angular/core';
import { RecruiterProfile } 					from './recruiter-profile';
import { RecruiterProfileService} 				from '../recruiter-profile.service';

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
	public currentView = 'view-main'; 

	/**
	* Constructor
	*/
	constructor(private recruierProfileService:RecruiterProfileService){
		this.recruierProfileService.fetchOwnRecruiterProfile().subscribe( response => {
			JSON.stringify(response);
		})
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
	public getRecruitmentTypeOptions():Array<[string,string]>{
		
		let options:Array<[string,string]> = new Array<[string,string]>();
		
		options.push(['INDEPENDENT', 	'Independent']);
		options.push(['COMMERCIAL', 	'Commercial']);
		
		return options;
		
	}

	/**
	* Switches to view to view all visible Profiles
	*/
	public getCountryOptions():Array<[string,string]>{
		
		let options:Array<[string,string]> = new Array<[string,string]>();
		
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
		
		options.push(['FREELANCE', 	'Freelance / Contract']);
		options.push(['PERM', 		'Permanent / Payroll']);
		
		return options;
		
	}
	
}
