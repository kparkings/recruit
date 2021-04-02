import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse, HttpHeaders }  from '@angular/common/http';
import { Observable, throwError }                 from 'rxjs';
import { catchError, retry }                      from 'rxjs/operators';

/**
* Service providing Authentication
* @author K Parkings
*/
@Injectable({
  providedIn: 'root'
})
export class AuthService {

/**
* Constructor
* @param httpClient - Http Client Util 
*/
constructor(private httpClient: HttpClient) { }

  httpOptions = {
    
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }), withCredentials: true
    };

  headers = { 'content-type': 'application/json'};

  /**
  * Authenticates a User with the System 
  * @param username - name user is attempting to log in with
  * @param password - password user is attempting to log in with
  */
  public authenticate(username: string, password: string): Observable<any>{



     const authDetails: any = {'username':username, 'password': password};

     return this.httpClient.post<any>('http://127.0.0.1:8080/authenticate', authDetails, this.httpOptions);

  }

}