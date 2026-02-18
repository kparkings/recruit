import { Injectable }                                            from '@angular/core';
import { HttpClient, HttpHeaders }                               from '@angular/common/http';
import { Observable }                                            from 'rxjs';
import { environment }                                           from './../environments/environment';

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
constructor(private readonly httpClient: HttpClient) { }

    httpOptions = {
            headers: new HttpHeaders({ 'Content-Type': 'application/json' }), withCredentials: true
    };

    /**
    * Authenticates a User with the System 
    * @param username - name user is attempting to log in with
    * @param password - password user is attempting to log in with
    */
    public authenticate(username: string, password: string): Observable<any>{

        const authDetails: any       = {'username':username, 'password': password};
        const backendUrl:string     = environment.backendUrl + 'authenticate';
  
        return this.httpClient.post<any>(backendUrl, authDetails, this.httpOptions);

    }
    
}