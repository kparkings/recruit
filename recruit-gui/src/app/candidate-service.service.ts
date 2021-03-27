// tslint:disable: no-trailing-whitespace
// tslint:disable: jsdoc-format
// tslint:disable: import-spacing
import { Injectable }                             from '@angular/core';
import { FormGroup }                              from '@angular/forms';
import { HttpClient, HttpResponse, HttpHeaders }  from '@angular/common/http';
import { Observable, throwError }                 from 'rxjs';
import { catchError, retry }                      from 'rxjs/operators';
import { NewCandidate }                           from './new-candidate/new-candidate';
import { Language}                                from './new-candidate/language';
import { CandidateFunction }                      from './candidate-function';

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
  public getCandidates(): Observable<any>{
    return this.httpClient.get<any>('http://127.0.0.1:8080/candidate', this.httpOptions);
  }

  /**
  * Authenticates a User with the System 
  */
  public authenticate(): Observable<any>{

     const authDetails: any = {'username':'javainuse', 'password': 'password'};

     return this.httpClient.post<any>('http://127.0.0.1:8080/authenticate', authDetails, this.httpOptions);

  }

  /**
  * Adds a new Candidate 
  */
  public addCandidate(formBean:FormGroup): Observable<any>{

    const newCandidate:NewCandidate = new NewCandidate();

    newCandidate.candidateId      = formBean.get('candidateId')?.value;
    newCandidate.firstname        = formBean.get('firstname')?.value;
    newCandidate.surname          = formBean.get('surname')?.value;
    newCandidate.email            = formBean.get('email')?.value;
    newCandidate.country          = formBean.get('country')?.value;
    newCandidate.city             = formBean.get('city')?.value;
    newCandidate.permanent        = formBean.get('permanent')?.value;
    newCandidate.freelance        = formBean.get('freelance')?.value;
    newCandidate.yearsExperience  = formBean.get('yearsExperience')?.value;
    newCandidate.function         = formBean.get('function')?.value;

    const langDutch: string       = formBean.get('dutch')?.value;
    const langFrench: string      = formBean.get('french')?.value;
    const langEnglish: string     = formBean.get('english')?.value;

    if (langDutch === 'Yes') {
      newCandidate.languages.push(new Language('DUTCH', 'PROFICIENT'));
    }
    
    if (langDutch === 'Basic') {
      newCandidate.languages.push(new Language('DUTCH', 'BASIC'));
    }
    
    if (langFrench === 'Yes') {
      newCandidate.languages.push(new Language('FRENCH', 'PROFICIENT'));
    }
    
    if (langFrench === 'Basic') {
      newCandidate.languages.push(new Language('FRENCH', 'BASIC'));
    }
        
    if (langEnglish === 'Yes') {
      newCandidate.languages.push(new Language('ENGLISH', 'PROFICIENT'));
    }
    
    if (langEnglish === 'Basic') {
      newCandidate.languages.push(new Language('ENGLISH', 'BASIC'));
    }
    
    const skills                       = formBean.get('skills')?.value ? formBean.get('skills')?.value : '';
    const skillTokens: Array<string>   = skills.split(',');
    
    newCandidate.skills = new Array<string>();

    skillTokens.forEach(skillToken => {
      newCandidate.skills.push(skillToken);
    });

    return this.httpClient.post<any>('http://127.0.0.1:8080/candidate', JSON.stringify(newCandidate), this.httpOptions);

  }

  /**
  * Returns detials of avialable Canidate Functions
  */
  public loadFunctionTypes(): Array<CandidateFunction>{

    const functionTypes: Array<CandidateFunction> = new Array<CandidateFunction>();

    functionTypes.push(new CandidateFunction('JAVA_DEV','Java Developer'));
    functionTypes.push(new CandidateFunction('CSHARP_DEV','C# Developer'));
    functionTypes.push(new CandidateFunction('SUPPORT','Support analyst'));
    functionTypes.push(new CandidateFunction('BA','Business Analyst'));
    functionTypes.push(new CandidateFunction('UI_UX','UI \ UX'));
    functionTypes.push(new CandidateFunction('PROJECT_MANAGER','Project Manager'));
    functionTypes.push(new CandidateFunction('SOFTWARE_ARCHITECT','Software Architect'));
    functionTypes.push(new CandidateFunction('SOLUTIONS_ARCHITECT','Solutions Architect'));
    functionTypes.push(new CandidateFunction('ENTERPRISE_ARCHITECT','Enterprise Architect'));
    functionTypes.push(new CandidateFunction('TESTER','Test Analyset'));
    functionTypes.push(new CandidateFunction('WEB_DEV','Web Developer'));

    return functionTypes;

  }

}