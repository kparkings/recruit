import { Injectable }                             		from '@angular/core';
import { HttpClient, HttpHeaders }  					from '@angular/common/http';
import { Observable }        		         			from 'rxjs';
import { NewPendingCandidate, Rate }                    from './create-candidate/new-pending-candidate';
import { CandidateFunction }                      		from './candidate-function';
import { environment }									from './../environments/environment';
import { SearchAlert }		 	                    	from './recruiter-alerts/search-alert';
import { CandidateSearchAlert }                     	from './suggestions/candidate-search-alert';
import { ExtractedFilters }                     		from './suggestions/extracted-filters';
import { SavedCandidate }		 	                	from './suggestions/saved-candidate';
import { NewCandidateRequest } 							from './new-candidate/new-candidate-request';
import { CandidateProfile } 							from './candidate-profile';
import { UpdateCandidateRequest } 						from './new-candidate/update-candidate-request';
import { CandidateSkill } from './accounts/candidate-skill';
import { TranslateService } from '@ngx-translate/core';

/**
* Services for new Candidates
* @author: K Parkings  
*/
@Injectable({
  providedIn: 'root'
})
export class CandidateServiceService {
    
	constructor(private httpClient: HttpClient, private translate:TranslateService) { }
	
	httpOptions = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }), withCredentials: true
    };

	httpOptionsFileUpload = {
      headers: new HttpHeaders({ }), withCredentials: true
    };

  	/**
  	* Returns a list of available Candidates 
  	*/
  	public getCandidates(filterParams:string): Observable<any>{
      
		const backendUrl:string = environment.backendUrl +'candidate?'+filterParams;
  
    	return this.httpClient.get<any>(backendUrl, this.httpOptions);
  	}
  	
	/**
  	* Returns a Candidate by its Id 
  	*/
  	public getCandidateById(candidateId:string): Observable<any>{
      
		const backendUrl:string = environment.backendUrl +'candidate/?orderAttribute=candidateId&order=desc&candidateId='+candidateId
  
    	return this.httpClient.get<any>(backendUrl, this.httpOptions);
  	}
  	
	/**
  	* Returns a Candidate by its Id 
  	*/
  	public getCandidateProfileById(candidateId:string): Observable<CandidateProfile>{
      
		const backendUrl:string = environment.backendUrl +'candidate/'+candidateId;
  
    	return this.httpClient.get<any>(backendUrl, this.httpOptions);
  	}
	
	/**
	* Enables a Candidate  
	*/
	enableCandidate(candidateId: string) {
	
		const backendUrl:string = environment.backendUrl +'candidate/'+candidateId+'/?action=enable';
			
		this.httpClient.put<any>(backendUrl, '{}',  this.httpOptions).subscribe(data => {
			
		});
	}
	 
	/**
	* Update a Candidate profile 
	*/
	public updateCandidate(candidateId:string, candidate: UpdateCandidateRequest, profileImage:File):Observable<any> {
	
		const backendUrl:string = environment.backendUrl +'candidate/'+candidateId + '/profile';
		
		var fd = new FormData();
		fd.append('file', profileImage);
  		fd.append("profile", new Blob([JSON.stringify(candidate)], { type: 'application/json' }));
	
		return this.httpClient.put<any>(backendUrl, fd, {headers: new HttpHeaders({ }), withCredentials: true});
		
	}
	
	/**
	* Update a Candidate CV 
	*/
	public updateCandidateCV(candidateId:string, cvFile:File):Observable<any> {
	
		const backendUrl:string = environment.backendUrl +'curriculum/'+candidateId;
		
		var fd = new FormData();
		fd.append('file', cvFile);
  	
		return this.httpClient.put<any>(backendUrl, fd, {headers: new HttpHeaders({ }), withCredentials: true});
		
	}

	/**
	* Disables a Candidate  
	*/
	//disableCandidate(candidateId: string): Observable<any> {
	
	//	const backendUrl:string = environment.backendUrl +'candidate/'+candidateId+'/?action=disable';
			
	//	return this.httpClient.put<any>(backendUrl,  '{}', this.httpOptions);
	
	//}


	
	/**
	* Sends a request to add a new PendingCandidate
	* @param pendingCandidate 	- Contains details of the new Pending Candidate
	* @param profileImage		- Optional profile image
	*/
	public addPendingCandidate(pendingCandidate: NewPendingCandidate, profileImage:File| any): Observable<any>{
		
		const backendUrl:string = environment.backendUrl +'pending-candidate';
		
		var fd = new FormData();
		fd.append('file', profileImage);
  		fd.append("candidate", new Blob([JSON.stringify(pendingCandidate)], { type: 'application/json' }));
	
		return this.httpClient.post<any>(backendUrl, fd, {headers: new HttpHeaders({ }), withCredentials: true});
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
	public addCandidate(newCandidateRequest:NewCandidateRequest, profileImage:File| any): Observable<any>{

	    const backendUrl:string = environment.backendUrl +'candidate';
	    
	    var fd = new FormData();
		fd.append('profileImage', profileImage);
  		fd.append("candidate", new Blob([JSON.stringify(newCandidateRequest)], { type: 'application/json' }));
		
		return this.httpClient.post<any>(backendUrl, fd, {headers: new HttpHeaders({ }), withCredentials: true});
	    
	    // return this.httpClient.post<any>(backendUrl, JSON.stringify(newCandidateRequest), this.httpOptions);
	
	}
	
	/**
	* Returns detials of avialable Canidate Functions
	*/
	public loadFunctionTypes(): Array<CandidateFunction>{
	
		const functionTypes: Array<CandidateFunction> = new Array<CandidateFunction>();
	
	    functionTypes.push(new CandidateFunction('SUPPORT',					this.translate.instant('func-support')));//'Support analyst'));
	    functionTypes.push(new CandidateFunction('BA',						this.translate.instant('func-ba')));//'Business Analyst'));
		functionTypes.push(new CandidateFunction('CSHARP_DEV',				this.translate.instant('func-csharp-dev')));//'C# Developer'));
		functionTypes.push(new CandidateFunction('DATA_SCIENTIST',			this.translate.instant('func-data-scientist')));//'Data Scientist'));
		functionTypes.push(new CandidateFunction('JAVA_DEV',				this.translate.instant('func-java-dev')));//'Java Developer'));
		functionTypes.push(new CandidateFunction('IT_RECRUITER',			this.translate.instant('func-it-recruiter')));//'IT Recruiter'));
		functionTypes.push(new CandidateFunction('IT_SECURITY',				this.translate.instant('func-it-security')));//'IT Security'));
		functionTypes.push(new CandidateFunction('NETWORK_ADMINISTRATOR',	this.translate.instant('func-network-admin')));//'Network Administrator'));
	    functionTypes.push(new CandidateFunction('PROJECT_MANAGER',			this.translate.instant('func-pmo')));//'Project Manager'));
	    functionTypes.push(new CandidateFunction('SCRUM_MASTER',			this.translate.instant('func-scrum-master')));//'Scrum Master'));
		functionTypes.push(new CandidateFunction('ARCHITECT',				this.translate.instant('func-software-architect')));//'Software Architect'));
	    functionTypes.push(new CandidateFunction('SOFTWARE_DEVELOPER',		this.translate.instant('func-software-dev')));//'Software Developer'));
		functionTypes.push(new CandidateFunction('SOFTWARE_DEV_IN_TEST',	this.translate.instant('func-support-sdet')));//'Software Dev In Test'));
		functionTypes.push(new CandidateFunction('TESTER',					this.translate.instant('func-tester')));//'Test Analyst'));
		functionTypes.push(new CandidateFunction('UI_UX',					this.translate.instant('func-uiux')));//'UI \ UX'));
	    functionTypes.push(new CandidateFunction('WEB_DEV',					this.translate.instant('func-web-dev')));//'Web Developer'));
	    
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
		
		const backendUrl:string = environment.backendUrl +'candidate?daysSinceLastAvailabilityCheck=14&orderAttribute=candidateId&order=desc&page=0&size=1000&available=true';
  
    	return this.httpClient.get<any>(backendUrl, this.httpOptions);

	}
	
	/**
	* Returns Candidates that have been marked as unavailable but are due to have their availability checked
	*/
	public fetchUnavailableCandidatesDueForAvailabilityCheck(): Observable<any>{
		
		const backendUrl:string = environment.backendUrl +'candidate?daysSinceLastAvailabilityCheck=30&orderAttribute=candidateId&order=desc&page=0&size=1000&available=false';
  
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
	
	/**
	* Sends a request to mark the Candidate as having been checked and found to still be 
	* available
	*/
	public createCandidateSearchAlert(alert:CandidateSearchAlert): Observable<any> {
		
		const backendUrl:string = environment.backendUrl +'candidate/alert';
	
		return this.httpClient.post<any>(backendUrl, JSON.stringify(alert), this.httpOptions);
		
	}
	
	/**
	* Sends a request to mark the Candidate as having been checked and found to still be 
	* available
	*/
	public deleteCandidateSearchAlert(alert:SearchAlert): Observable<any> {
		
		const backendUrl:string = environment.backendUrl +'candidate/alert/' + alert.alertId;
	
		return this.httpClient.delete<any>(backendUrl, this.httpOptions);
		
	}

	/**
	* Returns Alerts for authenticated Recruiter
	*/
	public fetchCandidateSearchAlerts(): Observable<Array<SearchAlert>>{
		
		const backendUrl:string = environment.backendUrl +'candidate/alert';
  
    	return this.httpClient.get<any>(backendUrl, this.httpOptions);

	}
	
	/**
  	* Uploads a Job description so that its search filters can be extracted 
  	*/
  	public extractFiltersFromDocument(jobSpecification:File): Observable<ExtractedFilters>{
  
  		var fd = new FormData();
  		fd.append('file', jobSpecification);
  
  		const backendUrl:string = environment.backendUrl + 'extract-filters';
  	
    	return this.httpClient.post<any>(backendUrl, fd, this.httpOptionsFileUpload);
  
  	}

	/**
  	* Uploads a Job description so that its search filters can be extracted 
  	*/
  	public extractFiltersFromText(jobSpecification:string): Observable<ExtractedFilters>{
  
  		const backendUrl:string = environment.backendUrl + 'extract-filters-text';
  	
  		return this.httpClient.post<any>(backendUrl, jobSpecification, this.httpOptions);
    	
  	}

	/**
	* Adds a Saved Candidate for a User
	*/
	public addSavedCandidate(savedCandidate:SavedCandidate): Observable<void>{
		
		const backendUrl:string = environment.backendUrl +'saved-candidate';
	
		return this.httpClient.post<any>(backendUrl, JSON.stringify(savedCandidate), this.httpOptions);

	}

	/**
	* Updates an existing  Saved Candidate for a User
	*/
	public updateSavedCandidate(savedCandidate:SavedCandidate): Observable<void>{
		
		const backendUrl:string = environment.backendUrl +'saved-candidate';
	
		return this.httpClient.put<any>(backendUrl, JSON.stringify(savedCandidate), this.httpOptions);

	}

	/**
	* Updates an existing  Saved Candidate for a User
	*/
	public fetchSavedCandidates(): Observable<Array<SavedCandidate>>{
		
		const backendUrl:string = environment.backendUrl +'saved-candidate';
	
		return this.httpClient.get<any>(backendUrl, this.httpOptions);

	}

	/**
	* Deletes an existing  Saved Candidate for a User
	*/
	public deleteSavedCandidate(candidateId:number): Observable<void>{
		
		const backendUrl:string = environment.backendUrl +'saved-candidate/' + candidateId;
	
		return this.httpClient.delete<any>(backendUrl, this.httpOptions);

	}
	
	/**
	* Deletes an existing Candidate for a User
	*/
	public deleteCandidate(candidateId:string): Observable<void>{
		
		const backendUrl:string = environment.backendUrl +'candidate/' + candidateId;
	
		return this.httpClient.delete<any>(backendUrl, this.httpOptions);

	}
	
	/**
	* Fetches skills to be validated
	*/
	fetchSkillsToValidate(): Observable<Array<CandidateSkill>> {
       
       const backendUrl:string = environment.backendUrl +'candidate/skills/pending';
  
    	return this.httpClient.get<any>(backendUrl, this.httpOptions);
    
    }
    
    /**
	* Updates Skills that have been validated
	*/
    updateValidatedSkills(validatedSkills: Array<CandidateSkill>): Observable<void> {
       
        const backendUrl:string = environment.backendUrl +'candidate/skills';
	
		return this.httpClient.put<any>(backendUrl, JSON.stringify(validatedSkills), this.httpOptions);
    }
    
    /**
	* Updates availability status
	* @param action: enable | disable 
	*/
    setCandidateAvailability(candidateId: string, action:string): Observable<any> {
	
		const backendUrl:string = environment.backendUrl +'candidate/'+candidateId+'/?action=' + action;
			
		return this.httpClient.put<any>(backendUrl,  '{}', this.httpOptions);
	
	}
    
			
}