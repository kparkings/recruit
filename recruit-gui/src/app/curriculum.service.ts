import { Injectable }                                                           from '@angular/core';
import { Observable }                 	                                    from 'rxjs';
import { HttpClient, HttpHeaders }  	                                from '@angular/common/http';
import { environment } 								                        from './../environments/environment';

/**
* Service for interacting with Curriculums 
*/
@Injectable({
  providedIn: 'root'
})
export class CurriculumService {
    
    

	/**
    * Constructor
    * @param httpClient - For communicating with backend
    */
  	constructor(private httpClient: HttpClient) { }
  
   	/**
   	* Sets options for request going to the backend. 
   	* Used to ensure auth cookie is sent
   	*/
	httpOptions = {
      headers: new HttpHeaders({ }), withCredentials: true
	};

  	/**
  	* Uploads a Curriculum and returns details of the uploaded Curriculum 
  	*/
  	public uploadCurriculum(curriculum:File): Observable<any>{
  
  		var fd = new FormData();
  		fd.append('file', curriculum);
  
  		const backendUrl:string = environment.backendUrl + 'curriculum';
  	
    	return this.httpClient.post<any>(backendUrl, fd, this.httpOptions);
  
  	}

  	/**
  	* Uploads a Pending Curriculum and returns details of the uploaded Curriculum 
  	*/
  	public uploadPendingCurriculum(curriculum:File): Observable<any>{
  
  		var fd = new FormData();
  		fd.append('file', curriculum);
  
  		const backendUrl:string = environment.backendUrl + 'pending-curriculum';
  	
    	return this.httpClient.post<any>(backendUrl, fd, this.httpOptions);
  
  	}

	/**
	* Sends request to make a Pending Curriculum active
	*/
	public makePendingCurriculumActive(pendingCurriculumId:string):Observable<any>{
	
		const backendUrl:string = environment.backendUrl + 'pending-curriculum/' +pendingCurriculumId;
  	
		return this.httpClient.put<any>(backendUrl, null, this.httpOptions);
	}
  
	/**
	* Retuns url to get bytes for inline PDF version of Curriculum
	*/
	public getCurriculumUrlForInlinePdf(candidateId:string):string{
		
		return environment.backendUrl + 'curriculum-test/' + candidateId + '.pdf';
	}
		
}