import { Injectable } from '@angular/core';
import { Observable, throwError }                 from 'rxjs';
import { catchError, retry }                      from 'rxjs/operators';

import { HttpClient, HttpResponse, HttpHeaders }  from '@angular/common/http';


@Injectable({
  providedIn: 'root'
})
export class CurriculumService {

  constructor(private httpClient: HttpClient) { }
  
  httpOptions = {
      headers: new HttpHeaders({ }), withCredentials: true
    };

 // headers = { 'content-type': 'application/pdf'};

  /**
  * Returns a list of available Candidates 
  */
  public uploadCurriculum(curriculum:File): Observable<any>{
  
  	var fd = new FormData();
  	fd.append('file', curriculum);
  
    return this.httpClient.post<any>('http://127.0.0.1:8080/curriculum', fd, this.httpOptions);
  }
  
  
}
