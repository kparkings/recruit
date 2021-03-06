import { Injectable }   from '@angular/core';
import { FormGroup }    from '@angular/forms';

@Injectable({
  providedIn: 'root'
})
export class CandidateServiceService {

  constructor() { }

  public addCandidate(formBean:FormGroup):void{

    Object.keys(formBean.controls).forEach((key:string) => {

      const control = formBean.get(key);
   
      let keyVal = key;
      let valVal =  control == null || control?.value == null ? "" : control.value;
   
      console.log(keyVal + " : " + valVal);
     });

  }

}
