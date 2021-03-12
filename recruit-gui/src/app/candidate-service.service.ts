import { Injectable }                         from '@angular/core';
import { FormGroup }                          from '@angular/forms';
import { HttpClient, HttpResponse }           from '@angular/common/http';
import { Observable, throwError }             from 'rxjs';
import { catchError, retry }                  from 'rxjs/operators';
import { NewCandidate }                       from './new-candidate/new-candidate';
import { Language}                            from './new-candidate/language';

/**
* Services for new Candidates
* @author: K Parkings  
*/
@Injectable({
  providedIn: 'root'
})
export class CandidateServiceService {

  constructor(private httpClient: HttpClient) { }

  public getCandidates():Observable<any>{

    const headers = { 'content-type': 'application/json'};

    return this.httpClient.get<any>('http://localhost:8080/candidate', { headers});
  
  }

  public addCandidate(formBean:FormGroup): Observable<any>{

    const headers = { 'content-type': 'application/json'};

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
    
    const langDutch:string = formBean.get('dutch')?.value;
    const langFrench:string = formBean.get('french')?.value;
    const langEnglish:string = formBean.get('english')?.value;

    console.log("EN -> " + langEnglish);
    if (langDutch === 'Yes') {
      newCandidate.languages.push(new Language("DUTCH","PROFICIENT"));
    }
    
    if (langDutch === 'Basic') {
      newCandidate.languages.push(new Language("DUTCH","BASIC"));
    }
    
    
    if (langFrench === 'Yes') {
      newCandidate.languages.push(new Language("FRENCH","PROFICIENT"));
    }
    
    if (langFrench === 'Basic') {
      newCandidate.languages.push(new Language("FRENCH","BASIC"));
    }
    

    
    if (langEnglish === 'Yes') {
      newCandidate.languages.push(new Language("ENGLISH","PROFICIENT"));
    }
    
    if (langEnglish === 'Basic') {
      newCandidate.languages.push(new Language("ENGLISH","BASIC"));
    }
    
    const skills                      = formBean.get('skills')?.value ? formBean.get('skills')?.value : "";
    const skillTokens:Array<string>   = skills.split(",");
    
    newCandidate.skills = new Array<string>();

    skillTokens.forEach(skillToken => {
      newCandidate.skills.push(skillToken);
    });

    return this.httpClient.post<any>('http://localhost:8080/candidate', JSON.stringify(newCandidate), { headers});
    
  }

}
