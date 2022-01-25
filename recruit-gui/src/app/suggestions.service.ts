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

	/**
	* Constructor 
	*/
	constructor(private candidateService:CandidateServiceService) {
		
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
								
		return this.candidateService.getCandidates(this.getCandidateFilterParamString(	maxNumberOfSuggestions, 
																						title, 
																						countries, 
																						contract, 
																						perm, 
																						experienceMin, 
																						experienceMax, 
																						languages, 
																						skills));
		
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
		
		if (contract) {
			return '&freelance=true';
		}
		
		if (perm) {
			return '&perm=true';
		}
	
      	return '';
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
	
	//START - Move to separate service or class
	
	public addFunctionTypeIfJavaDev(title:string):string{
		
		let keywords:Array<string> = new Array<string>();
		
		keywords.push("java");
		keywords.push("j2ee");
		
		let match:boolean = false;
		
		keywords.forEach(kw => {
			if (title.indexOf(kw) > -1) {
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
		
		let keywords:Array<string> = new Array<string>();
		
		keywords.push("c#");
		keywords.push(".net");
		keywords.push("dotnet");
		keywords.push("wpf");
		keywords.push("asp");
		keywords.push("vb.net");
		keywords.push("csharp");
		keywords.push("asp.net");
		
		let match:boolean = false;
		
		keywords.forEach(kw => {
			if (title.indexOf(kw) > -1) {
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
		
		let keywords:Array<string> = new Array<string>();
		
		keywords.push("support");
		keywords.push("helpdesk");
		keywords.push("service desk");
		
		let match:boolean = false;
		
		keywords.forEach(kw => {
			if (title.indexOf(kw) > -1) {
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
		
		let keywords:Array<string> = new Array<string>();
		
		keywords.push("buisiness analyst");
		keywords.push("ba");
		
		let match:boolean = false;
		
		keywords.forEach(kw => {
			if (title.indexOf(kw) > -1) {
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
		
		let keywords:Array<string> = new Array<string>();
		
		keywords.push("ui\\ux");
		keywords.push("designer");
		keywords.push("ui");
		keywords.push("ux");
		
		let match:boolean = false;
		
		keywords.forEach(kw => {
			if (title.indexOf(kw) > -1) {
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
		
		let keywords:Array<string> = new Array<string>();
		
		keywords.push("manager");
		keywords.push("product owner");
		
		let match:boolean = false;
		
		keywords.forEach(kw => {
			if (title.indexOf(kw) > -1) {
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
		
		let keywords:Array<string> = new Array<string>();
		
		keywords.push("architect");
		keywords.push("solutions");
		keywords.push("enterprise");
		
		let match:boolean = false;
		
		keywords.forEach(kw => {
			if (title.indexOf(kw) > -1) {
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
		
		let keywords:Array<string> = new Array<string>();
		
		keywords.push("tester");
		keywords.push("qa");
		keywords.push("automation");
		keywords.push("manual");
		keywords.push("quality");
		keywords.push("assurance");
		
		let match:boolean = false;
		
		keywords.forEach(kw => {
			if (title.indexOf(kw) > -1) {
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
		
		let keywords:Array<string> = new Array<string>();
		
		keywords.push("web developer");
		keywords.push("front end");
		keywords.push("front-end");
		keywords.push("javascript");
		keywords.push("js");
		keywords.push("vue");
		keywords.push("vuejs");
		keywords.push("vue.js");
		keywords.push("react");
		keywords.push("node");
		keywords.push("node.js");
		keywords.push("php");
		keywords.push("wordpress");
		
		let match:boolean = false;
		
		keywords.forEach(kw => {
			if (title.indexOf(kw) > -1) {
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
		
		let keywords:Array<string> = new Array<string>();
		
		keywords.push("scrum");
		keywords.push("master");
		
		let match:boolean = false;
		
		keywords.forEach(kw => {
			if (title.indexOf(kw) > -1) {
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
		
		let keywords:Array<string> = new Array<string>();
		
		keywords.push("data");
		keywords.push("data analyst");
		keywords.push("data scientist");
		keywords.push("bi");
		keywords.push("business intelligence");
		keywords.push("python");
		
		let match:boolean = false;
		
		keywords.forEach(kw => {
			if (title.indexOf(kw) > -1) {
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
		
		let keywords:Array<string> = new Array<string>();
		
		keywords.push("devops");
		keywords.push("network");
		keywords.push("admin");
		keywords.push("administrator");
		keywords.push("ops");
		keywords.push("operations");
		
		let match:boolean = false;
		
		keywords.forEach(kw => {
			if (title.indexOf(kw) > -1) {
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
		
		let keywords:Array<string> = new Array<string>();
		
		keywords.push("php");
		keywords.push("python");
		keywords.push("wordpress");
		keywords.push("software engineer");
		keywords.push("software developer");
		
		let match:boolean = false;
		
		keywords.forEach(kw => {
			if (title.indexOf(kw) > -1) {
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
		
		let keywords:Array<string> = new Array<string>();
		
		keywords.push("security");
		keywords.push("cyber");
		keywords.push("owasp");
		keywords.push("owasp");
		
		let match:boolean = false;
		
		keywords.forEach(kw => {
			if (title.indexOf(kw) > -1) {
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
		
		let keywords:Array<string> = new Array<string>();
		
		keywords.push("recruiter");
		keywords.push("account manager");
		
		let match:boolean = false;
		
		keywords.forEach(kw => {
			if (title.indexOf(kw) > -1) {
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
		
		let keywords:Array<string> = new Array<string>();
		
		keywords.push("sdet");
		keywords.push("developer in test");
		keywords.push("qa");
		keywords.push("automation tester");
		
		let match:boolean = false;
		
		keywords.forEach(kw => {
			if (title.indexOf(kw) > -1) {
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

