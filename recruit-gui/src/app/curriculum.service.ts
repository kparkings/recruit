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

}