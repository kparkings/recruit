import { Injectable }                             	from '@angular/core';
import { FormGroup }                              	from '@angular/forms';
import { HttpClient, HttpResponse, HttpHeaders }  	from '@angular/common/http';
import { Observable, throwError }                 	from 'rxjs';
import { NewCandidate }                           	from './new-candidate/new-candidate';
import { Candidate }                           		from './candidate';
import { NewPendingCandidate }                      from './create-candidate/new-pending-candidate';
import { Language}                                	from './new-candidate/language';
import { CandidateFunction }                      	from './candidate-function';
import { environment }								from './../environments/environment';

/**
* Services for new Candidates
* @author: K Parkings  
*/
@Injectable({
  providedIn: 'root'
})
export class CandidateServiceService {
	
	constructor(private httpClient: HttpClient) { }
	
	httpOptions = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }), withCredentials: true
    };

  	headers = { 'content-type': 'application/json'};

  	/**
  	* Returns a list of available Candidates 
  	*/
  	public getCandidates(filterParams:string): Observable<any>{
      
		const backendUrl:string = environment.backendUrl +'candidate?'+filterParams;
  
    	return this.httpClient.get<any>(backendUrl, this.httpOptions);
  	}
	
	/**
	* Enables a Candidate  
	*/
	enableCandidate(candidateId: string) {
	
		const backendUrl:string = environment.backendUrl +'candidate/'+candidateId+'/?action=enable';
			
		this.httpClient.put<any>(backendUrl, '{}',  this.httpOptions).subscribe(data => console.log('XXXXXXXXXXXXXXXY'));
	}
	  
	/**
	* Disables a Candidate  
	*/
	disableCandidate(candidateId: string): Observable<any> {
	
		const backendUrl:string = environment.backendUrl +'candidate/'+candidateId+'/?action=disable';
			
		return this.httpClient.put<any>(backendUrl,  '{}', this.httpOptions);
	
	}
	
	/**
	* Sends a request to add a new PendingCandidate
	* @param pendingCandidateId - Unique Id of the Candidate. Must be the same as the Curriculum
	* @param firstname          - Candidates firstname
	* @param surname			- Candudares surname
	* @param email				- Candidates email address
	* @param contract			- Whether or not the Candidate is interested in Contract positions
	* @param perm				- Whether or not the Candidate is interested in Perm positions
	*/
	public addPendingCandidate(pendingCandidateId:string, firstname:string, surname:string, email:string, contract:boolean, perm:boolean): Observable<any>{
		
		const newPendingCandidate:NewPendingCandidate = new NewPendingCandidate();
		
		newPendingCandidate.pendingCandidateId 	= pendingCandidateId;
		newPendingCandidate.firstname 			= firstname;
		newPendingCandidate.surname 			= surname;
		newPendingCandidate.email 				= email;
		newPendingCandidate.freelance 			= contract;
		newPendingCandidate.perm 				= perm;
		
		const backendUrl:string = environment.backendUrl +'pending-candidate';
		
		return this.httpClient.post<any>(backendUrl, JSON.stringify(newPendingCandidate), this.httpOptions);
	}

	/**
	* Logs an event of a Recruiter requesting the email address of a Candidate
	*/	
	public logRecruiterRequestedCandidateEmailEvent(candidateId:string): Observable<any>{
		
		const backendUrl:string = environment.backendUrl +'candidate/stat/email-request';
		console.log('BOOP');
		return this.httpClient.post<any>(backendUrl, candidateId, this.httpOptions);
		
	}
	
	/**
	* Retrieves Pending candidates
	*/
	public fetchPendingCandidates():Observable<any>{
		
		const backendUrl:string = environment.backendUrl +'pending-candidate';
		
		return this.httpClient.get<any>(backendUrl, this.httpOptions);
		
	}
	
	/**
	* Adds a new Candidate 
	*/
	public addCandidate(formBean:FormGroup): Observable<any>{
	
		const newCandidate:NewCandidate = new NewCandidate();
	
	    newCandidate.candidateId           		= formBean.get('candidateId')?.value;
	    newCandidate.firstname               	= formBean.get('firstname')?.value;
	    newCandidate.surname                	= formBean.get('surname')?.value;
	    newCandidate.email                     	= formBean.get('email')?.value;
	    newCandidate.country                  	= formBean.get('country')?.value;
	    newCandidate.city                       = formBean.get('city')?.value;
	    newCandidate.perm                     	= formBean.get('perm')?.value;
	    newCandidate.freelance              	= formBean.get('freelance')?.value;
	    newCandidate.yearsExperience   			= formBean.get('yearsExperience')?.value;
	    newCandidate.function                	= formBean.get('function')?.value;
	    newCandidate.roleSought           		= formBean.get('roleSought')?.value;
	    const langDutch: string                 = formBean.get('dutch')?.value;
	    const langFrench: string                = formBean.get('french')?.value;
	    const langEnglish: string               = formBean.get('english')?.value;
	
	    if (langDutch === 'YES') {
	      newCandidate.languages.push(new Language('DUTCH', 'PROFICIENT'));
	    }
	    
	    if (langDutch === 'BASIC') {
	      newCandidate.languages.push(new Language('DUTCH', 'BASIC'));
	    }
	    
	    if (langFrench === 'YES') {
	      newCandidate.languages.push(new Language('FRENCH', 'PROFICIENT'));
	    }
	    
	    if (langFrench === 'BASIC') {
	      newCandidate.languages.push(new Language('FRENCH', 'BASIC'));
	    }
	        
	    if (langEnglish === 'YES') {
	      newCandidate.languages.push(new Language('ENGLISH', 'PROFICIENT'));
	    }
	    
	    if (langEnglish === 'BASIC') {
	      newCandidate.languages.push(new Language('ENGLISH', 'BASIC'));
	    }
	    
	    const skills                                        = formBean.get('skills')?.value ? formBean.get('skills')?.value : '';
	    const skillTokens: Array<string>      = skills.split(',');
	    
	    newCandidate.skills = new Array<string>();
	
	    skillTokens.forEach(skillToken => {
	      newCandidate.skills.push(skillToken);
	    });
	
	    const backendUrl:string = environment.backendUrl +'candidate';
	    
	    return this.httpClient.post<any>(backendUrl, JSON.stringify(newCandidate), this.httpOptions);
	
	}
	
	/**
	* Returns detials of avialable Canidate Functions
	*/
	public loadFunctionTypes(): Array<CandidateFunction>{
	
		const functionTypes: Array<CandidateFunction> = new Array<CandidateFunction>();
	
	    functionTypes.push(new CandidateFunction('SUPPORT',					'Support analyst'));
	    functionTypes.push(new CandidateFunction('BA',						'Business Analyst'));
		functionTypes.push(new CandidateFunction('CSHARP_DEV',				'C# Developer'));
		functionTypes.push(new CandidateFunction('DATA_SCIENTIST',			'Data Scientist'));
		functionTypes.push(new CandidateFunction('JAVA_DEV',				'Java Developer'));
		functionTypes.push(new CandidateFunction('IT_RECRUITER',			'IT Recruiter'));
		functionTypes.push(new CandidateFunction('IT_SECURITY',				'IT Security'));
		functionTypes.push(new CandidateFunction('NETWORK_ADMINISTRATOR',	'Network Administrator'));
	    functionTypes.push(new CandidateFunction('PROJECT_MANAGER',			'Project Manager'));
	    functionTypes.push(new CandidateFunction('SCRUM_MASTER',			'Scrum Master'));
		functionTypes.push(new CandidateFunction('ARCHITECT',				'Software Architect'));
	    functionTypes.push(new CandidateFunction('SOFTWARE_DEVELOPER',		'Software Developer'));
		functionTypes.push(new CandidateFunction('SOFTWARE_DEV_IN_TEST',	'Software Dev In Test'));
		functionTypes.push(new CandidateFunction('TESTER',					'Test Analyst'));
		functionTypes.push(new CandidateFunction('UI_UX',					'UI \ UX'));
	    functionTypes.push(new CandidateFunction('WEB_DEV',					'Web Developer'));
	    
	    return functionTypes;
	
	}

	/**
	* Sends a request to mark the canidate as being potentially unavilable. This doesnt make the candiadte 
	* unavialble but rather is a flag indicating that the Candidate is potentially unavilable
	*/
	public markCandidateAsUnavailable(candidateId:string): Observable<any>{
		
		const backendUrl:string = environment.backendUrl +'candidate/'+candidateId+'/flaggedAsUnavailable/true/';
	
		return this.httpClient.put<any>(backendUrl, "{}", this.httpOptions);
	
	}

	/**
	* removed the markAsUnavailable flag for a Candidate
	*/
	public removeMarkCandidateAsUnavailableFlag(candidateId:string): Observable<any>{
		
		const backendUrl:string = environment.backendUrl +'candidate/'+candidateId+'/flaggedAsUnavailable/false/';
	
		return this.httpClient.put<any>(backendUrl, "{}", this.httpOptions);
	
	}
	
	/**
	* Returns Candidates that are due to have their availability checked
	*/
	public fetchCandidatesDueForAvailabilityCheck(): Observable<any>{
		
		const backendUrl:string = environment.backendUrl +'candidate?daysSinceLastAvailabilityCheck=14&orderAttribute=candidateId&order=desc&page=0&size=1000';
  
    	return this.httpClient.get<any>(backendUrl, this.httpOptions);

	}
	
	
	/**
	* Sends a request to mark the Candidate as having been checked and found to still be 
	* available
	*/
	public markCandidateAsAvailable(candidateId:string): Observable<any> {
		
		const backendUrl:string = environment.backendUrl +'candidate/'+candidateId+'/updateCandidatesLastAvailabilityCheck';
	
		return this.httpClient.put<any>(backendUrl, "{}", this.httpOptions);
		
	}
	
}