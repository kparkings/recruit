
import { Injectable }   from '@angular/core';
import { FormGroup }    from '@angular/forms';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class CandidateServiceService {

  constructor(private httpClient: HttpClient) { }

  public addCandidate(formBean:FormGroup): Observable<any>{

    Object.keys(formBean.controls).forEach((key:string) => {

      const control = formBean.get(key);
   
      let keyVal = key;
      let valVal =  control == null || control?.value == null ? "" : control.value;
   
      console.log(keyVal + " : " + valVal);
    
    
    });

    const headers = { 'content-type': 'application/json'};
    const body=JSON.stringify("{'key':'value'}");

    return this.httpClient.post<any>('http://localhost:8080/candidate', body, { headers});
    
  }

}
