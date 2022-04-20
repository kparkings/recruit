import { Injectable } 									from '@angular/core';
import { CandidateServiceService }						from './candidate-service.service';
import { Candidate }									from './candidate';
import { Observable, throwError }                 		from 'rxjs';

/**
* Service relating to filtering on and retrieving 
* Candidate Suggestions
*/
@Injectable({
  providedIn: 'root'
})
export class SuggestionsService {

	private keywordsCSharpDev:Array<string> 			= new Array<string>("c#",".net","dotnet","wpf","asp","vb.net","csharp","asp.net");
	private keywordsSupport:Array<string> 				= new Array<string>("support","helpdesk","service desk");
	private keywordsBusinessAnalyst:Array<string> 		= new Array<string>("buisiness analyst","ba");
	private keywordsUiUx:Array<string> 					= new Array<string>("ui\\ux","designer","ui","ux");
	private keywordsProjectManager:Array<string> 		= new Array<string>("manager","product owner");
	private keywordsArchitect:Array<string> 			= new Array<string>("architect","solutions","enterprise");
	private keywordsTester:Array<string> 				= new Array<string>("tester","test", "qa","automation","manual","quality","assurance", "selenium", "cucumber","testing","robot");
	private keywordsWebDeveloper:Array<string> 			= new Array<string>("web developer","front end","front-end","js","vue","vuejs","vue.js","react","node","node.js","php","wordpress");
	private keywordsScrumMaster:Array<string> 			= new Array<string>("scrum","master");
	private keywordsDataScientist:Array<string> 		= new Array<string>("data", "data analyst","data scientist","bi","business intelligence","python");
	private keywordsNetworkAdmin:Array<string> 			= new Array<string>("devops","network","admin","administrator","ops","operations", "cisco", "cloud", "windows", "ansible", "kubernetes", "salesforce","docker", "citrix", "servicenow", "tibco", "warehouse", "terraform", "dns", "o365", "ServiceNow", "VMWARE", "scripting", "firewall", "wireshark", "azure");
	private keywordsSoftwareDeveloper:Array<string> 	= new Array<string>("php","python","wordpress","software engineer","software developer","golang", "c", "c++","vb", "go", "cobol","pl-sql","t-sql", "r", "groovy", "sql", "swift", "python", "ios", "scala", "microservices", "oracle", "react", "redux", "android", "sql", "node", "plsql", "go", "c++", "golang", "vue", "bdd", "laravel", "dba", "kotlin", "node.js", "ios", "ruby", "embedded", "oauth", "liferay");
	private keywordsItSecurity:Array<string> 			= new Array<string>("security","cyber","malware","owasp", "pen");
	private keywordsItRecruiter:Array<string> 			= new Array<string>("recruiter","account manager");
	private keywordsSdet:Array<string> 					= new Array<string>("tester","test", "sdet","developer in test","qa","automation tester", "selenium", "cucumber","testing");
	private keywordsJava:Array<string> 					= new Array<string>("java","j2ee","java8","spring");

	private allKeywords:Array<string>					= new Array<string>();

	/**
	* Constructor 
	*/
	constructor(private candidateService:CandidateServiceService) {
		
		this.keywordsCSharpDev.forEach(itm => this.allKeywords.push(itm));
		this.keywordsSupport.forEach(itm => this.allKeywords.push(itm));
		this.keywordsBusinessAnalyst.forEach(itm => this.allKeywords.push(itm));
		this.keywordsUiUx.forEach(itm => this.allKeywords.push(itm));
		this.keywordsProjectManager.forEach(itm => this.allKeywords.push(itm));
		this.keywordsArchitect.forEach(itm => this.allKeywords.push(itm));
		this.keywordsTester.forEach(itm => this.allKeywords.push(itm));
		this.keywordsWebDeveloper.forEach(itm => this.allKeywords.push(itm));
		this.keywordsScrumMaster.forEach(itm => this.allKeywords.push(itm));
		this.keywordsDataScientist.forEach(itm => this.allKeywords.push(itm));
		this.keywordsNetworkAdmin.forEach(itm => this.allKeywords.push(itm));
		this.keywordsSoftwareDeveloper.forEach(itm => this.allKeywords.push(itm));
		this.keywordsItSecurity.forEach(itm => this.allKeywords.push(itm));
		this.keywordsItRecruiter.forEach(itm => this.allKeywords.push(itm));
		this.keywordsSdet.forEach(itm => this.allKeywords.push(itm));
		this.keywordsJava.forEach(itm => this.allKeywords.push(itm));
		
	}
	
	/**
	* Returns the Suggestons based upon the filter criteria 
	*/
	public getSuggestons(	maxNumberOfSuggestions:number, 
							title:string, 
							countries:Array<string>, 
							contract:boolean, 
							perm:boolean, 
							experienceMin:string, 
							experienceMax:string, 
							languages:Array<string>, 
							skills:Array<string>): Observable<any>{
								
		let augmentedSkillsList:Array<string> = new Array<string>();
		
		skills.forEach(skill => augmentedSkillsList.push(skill));
		
		title.split(" ").forEach(skill => {
			
			let skillFormatted:string = skill.toLocaleLowerCase();
		
			skillFormatted = skillFormatted.trim();
			
			if (this.allKeywords.indexOf(skillFormatted) >-1 && augmentedSkillsList.indexOf(skillFormatted) == -1) {
				augmentedSkillsList.push(skillFormatted);
			}
		});
		
		return this.candidateService.getCandidates(this.getCandidateFilterParamString(	maxNumberOfSuggestions, 
																						title, 
																						countries, 
																						contract, 
																						perm, 
																						experienceMin, 
																						experienceMax, 
																						languages, 
																						augmentedSkillsList));
		
	}
	
	/**
	* Builds a query parameter string with the selected filter options
	*/
	private getCandidateFilterParamString(	maxNumberOfSuggestions:number, 
											title:string, 
											countries:Array<string>, 
											contract:boolean, 
											perm:boolean, 
											experienceMin:string, 
											experienceMax:string,
											languages:Array<string>, 
											skills:Array<string> ):string{
    	
		

		const filterParams:string = 'orderAttribute=candidateId&order=desc'
                                                         + '&page=0'
                                                         + '&size=' + maxNumberOfSuggestions
														 + '&useSuggestions=true'
														 + this.getFunctionTypeFromTitle(title)
                                                         + this.getCountryFilterParamString(countries) 			
                                                         + this.getContractTypeParamString(contract, perm)				
                                                         + this.getYearsExperienceFilterParamAsString(experienceMin, experienceMax)
														 + this.getSkillsParamString(skills)
														 + this.getLanguagesParamString(languages);
					                                   
		return filterParams;
	
	}
	
		/**
	* Adds filter string if country specifed in Listing
	*/
	public getCountryFilterParamString(countries:Array<string>):string{
		
		if (countries.length === 0){
			return '';
		}
		
		return '&countries=' + countries;
	}
	
	/**
	* Adds filter for perm positions if specified in the listing
	*/
	public getContractTypeParamString(contract:boolean, perm:boolean):string{
		
		let paramString:string = '';
		
		if (contract) {
			paramString = '&freelance=true';
		}
		
		if (perm) {
			paramString = paramString + '&perm=true';
		}
	
      	return paramString;
	}
	
	private getSkillsParamString(skills:Array<string>):string{
      
		if (skills.length > 0) {

			let rawSkills:string 		= skills.toString();
			let encodedSkills:string 	= '&skills='+encodeURIComponent(rawSkills);
			
			return encodedSkills; 
      	}

      return '';
 
	}
	
	/**
  	* Creates a query param string with the filter options to apply to the dutch languge filter
  	*/
	private getLanguagesParamString(languages:Array<string>):string{
  
		let paramString:string = '';
		
		if (languages.indexOf('DUTCH')  >= 0 ) {
			paramString = paramString +  '&dutch=' + 'PROFICIENT';
		}
		
		if (languages.indexOf('FRENCH')  >= 0 ) {
			paramString = paramString +  '&french=' + 'PROFICIENT';
		}
		
		if (languages.indexOf('ENGLISH')  >= 0 ) {
			paramString = paramString +  '&english=' + 'PROFICIENT';
		}  
		
		return paramString;
  
	}
		
	/**
 	* Adds filter for years expeprience  if specified in the listing
	*/
	public getYearsExperienceFilterParamAsString(min:string, max:string):string{
		
		let paramStr:string = '';
		
		if (min !== ''){
			paramStr = paramStr + '&yearsExperienceGtEq=' + min;
		}
		
		if (max !== ''){
			paramStr = paramStr + '&yearsExperienceLtEq=' + max;
		}
		
		return paramStr;
	}
	
	private getFunctionTypeFromTitle(title:string):string{
		
		let functionTypes:Array<string> = new Array<string>();
		
		let titleFormatted:string = title.toLowerCase();
		titleFormatted = titleFormatted.trim();
		
		functionTypes.push(this.addFunctionTypeIfJavaDev(title));
		functionTypes.push(this.addFunctionTypeIfCSHARPDev(title));
		functionTypes.push(this.addFunctionTypeIfSupport(title));
		functionTypes.push(this.addFunctionTypeIfBusinessAnalayst(title));
		functionTypes.push(this.addFunctionTypeIfUiUx(title));
		functionTypes.push(this.addFunctionTypeIfProjectManager(title)); 
		functionTypes.push(this.addFunctionTypeIfArchitect(title)); 
		functionTypes.push(this.addFunctionTypeIfTester(title)); 
		functionTypes.push(this.addFunctionTypeIfWebDeveloper(title));
		functionTypes.push(this.addFunctionTypeIfScrumMaster(title));
		functionTypes.push(this.addFunctionTypeIfDataScientist(title));
		functionTypes.push(this.addFunctionTypeIfNetworkAdmin(title));
		functionTypes.push(this.addFunctionTypeIfSoftwareDeveloper(title));
		functionTypes.push(this.addFunctionTypeIfSecurity(title));
		functionTypes.push(this.addFunctionTypeIfRecruiter(title));
		functionTypes.push(this.addFunctionTypeIfSDET(title));
		
		functionTypes = functionTypes.filter(f => f !== '');
		
		if (functionTypes.length > 0) {
			return '&functions=' + encodeURIComponent(functionTypes.toString());
		} 
		
		return '';
	}

	public addFunctionTypeIfJavaDev(title:string):string{
		
		let match:boolean = false;
		
		this.keywordsJava.forEach(kw => {
			if (title.toLowerCase().indexOf(kw) > -1) {
				match = true;
			}
		});
		
		if (match) {
			
			return 'JAVA_DEV';
		} else {
			return '';	
		}	
	}
	

	public addFunctionTypeIfCSHARPDev(title:string):string{
		
		let match:boolean = false;
		
		this.keywordsCSharpDev.forEach(kw => {
			if (title.toLowerCase().indexOf(kw) > -1) {
				match = true;
			}
		});
		
		if (match) {
			return 'CSHARP_DEV';
		} else {
			return '';	
		}	
	}
	
	public addFunctionTypeIfSupport(title:string):string{
		
		let match:boolean = false;
		
		this.keywordsSupport.forEach(kw => {
			if (title.toLowerCase().indexOf(kw) > -1) {
				match = true;
			}
		});
		
		if (match) {
			return 'SUPPORT';
		} else {
			return '';	
		}	
	}
	
	
	private addFunctionTypeIfBusinessAnalayst(title:string):string{
		
		let match:boolean = false;
		
		this.keywordsBusinessAnalyst.forEach(kw => {
			if (title.toLowerCase().indexOf(kw) > -1) {
				match = true;
			}
		});
		
		if (match) {
			return 'BA';
		} else {
			return '';	
		}
				
	}
	
	private addFunctionTypeIfUiUx(title:string):string{
		
		let match:boolean = false;
		
		this.keywordsUiUx.forEach(kw => {
			if (title.toLowerCase().indexOf(kw) > -1) {
				match = true;
			}
		});
		
		if (match) {
			return 'UI_UX';
		} else {
			return '';	
		}
				
	} 
	
	private addFunctionTypeIfProjectManager(title:string):string{
		
		let match:boolean = false;
		
		this.keywordsProjectManager.forEach(kw => {
			if (title.toLowerCase().indexOf(kw) > -1) {
				match = true;
			}
		});
		
		if (match) {
			return 'PROJECT_MANAGER';
		} else {
			return '';	
		}
				
	} 
	
	private addFunctionTypeIfArchitect(title:string):string{ 
		
		let match:boolean = false;
		
		this.keywordsArchitect.forEach(kw => {
			if (title.toLowerCase().indexOf(kw) > -1) {
				match = true;
			}
		});
		
		if (match) {
			return 'ARCHITECT';
		} else {
			return '';	
		}
				
	} 
	
	
	
	private addFunctionTypeIfTester(title:string):string{
		
		let match:boolean = false;
		
		this.keywordsTester.forEach(kw => {
			if (title.toLowerCase().indexOf(kw) > -1) {
				match = true;
			}
		});
		
		if (match) {
			return 'TESTER';
		} else {
			return '';	
		}
				
	}  
	
	private addFunctionTypeIfWebDeveloper(title:string):string{
		
		let match:boolean = false;
		
		this.keywordsWebDeveloper.forEach(kw => {
			if (title.toLowerCase().indexOf(kw) > -1) {
				match = true;
			}
		});
		
		if (match) {
			return 'WEB_DEV';
		} else {
			return '';	
		}
				
	} 
	
	private addFunctionTypeIfScrumMaster(title:string):string{
		
		let match:boolean = false;
		
		this.keywordsScrumMaster.forEach(kw => {
			if (title.toLowerCase().indexOf(kw) > -1) {
				match = true;
			}
		});
		
		if (match) {
			return 'SCRUM_MASTER';
		} else {
			return '';	
		}
				
	} 
	
	private addFunctionTypeIfDataScientist(title:string):string{
		
		let match:boolean = false;
		
		this.keywordsDataScientist.forEach(kw => {
			if (title.toLowerCase().indexOf(kw) > -1) {
				match = true;
			}
		});
		
		if (match) {
			return 'DATA_SCIENTIST';
		} else {
			return '';	
		}
				
	} 
	
	private addFunctionTypeIfNetworkAdmin(title:string):string{
		
		let match:boolean = false;
		
		this.keywordsNetworkAdmin.forEach(kw => {
			if (title.toLowerCase().indexOf(kw) > -1) {
				match = true;
			}
		});
		
		if (match) {
			return 'NETWORK_ADMINISTRATOR';
		} else {
			return '';	
		}
				
	} 
	
	private addFunctionTypeIfSoftwareDeveloper(title:string):string{
		
		let match:boolean = false;
		
		this.keywordsSoftwareDeveloper.forEach(kw => {
			if (title.toLowerCase().indexOf(kw) > -1) {
				match = true;
			}
		});
		
		if (match) {
			return 'SOFTWARE_DEVELOPER';
		} else {
			return '';	
		}
				
	} 
	
	private addFunctionTypeIfSecurity(title:string):string{
		
		let match:boolean = false;
		
		this.keywordsItSecurity.forEach(kw => {
			if (title.toLowerCase().indexOf(kw) > -1) {
				match = true;
			}
		});
		
		if (match) {
			return 'IT_SECURITY';
		} else {
			return '';	
		}
				
	} 
	
	private addFunctionTypeIfRecruiter(title:string):string{
		
		let match:boolean = false;
		
		this.keywordsItRecruiter.forEach(kw => {
			if (title.toLowerCase().indexOf(kw) > -1) {
				match = true;
			}
		});
		
		if (match) {
			return 'IT_RECRUITER';
		} else {
			return '';	
		}
				
	} 
	
	private addFunctionTypeIfSDET(title:string):string{
		
		let match:boolean = false;
		
		this.keywordsSdet.forEach(kw => {
			if (title.toLowerCase().indexOf(kw) > -1) {
				match = true;
			}
		});
		
		if (match) {
			return 'SOFTWARE_DEV_IN_TEST';
		} else {
			return '';	
		}
				
	} 
}

