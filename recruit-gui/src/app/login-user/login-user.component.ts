import { Component, OnInit } from '@angular/core';
import { ReactiveFormsModule, FormGroup, FormControl }    from '@angular/forms';
import { AuthService }                        from '../auth.service';
import { Router} from '@angular/router';

@Component({
  selector: 'app-login-user',
  templateUrl: './login-user.component.html',
  styleUrls: ['./login-user.component.css']
})
/**
* Component for logging Users into the System 
*/
export class LoginUserComponent implements OnInit {

   public formBean:FormGroup = new FormGroup({
     
    username: new FormControl(),
    password: new FormControl()

   });

   /**
   * Constructor 
   * @param authService - Services for authenticating users
   * @param router      - Angular router
   */
  constructor(private authService: AuthService, private router: Router) { }

  /**
  * Performs initialization
  */
  ngOnInit(): void {
  }

  /**
  * Attempts to Log the user into the system
  */
  public login(): void{
    
    this.authService.authenticate(this.formBean.get('username')?.value, this.formBean.get('password')?.value).subscribe( data => {
     
      const roles:Array<string> = data;


      if (roles.includes('ROLE_ADMIN')) {
        sessionStorage.setItem('isAdmin',      'true');
      } else {
        sessionStorage.setItem('isAdmin',      'false');
      }

      if (roles.includes('ROLE_RECRUITER')) {
        sessionStorage.setItem('isRecruiter',      'true');
      } else {
        sessionStorage.setItem('isRecruiter',      'false');
      }

      console.log(JSON.stringify(data));

      sessionStorage.setItem('loggedIn',      'true');
      
      const beforeAuthPage: any = sessionStorage.getItem('beforeAuthPage');
    
        sessionStorage.setItem('beforeAuthPage', beforeAuthPage);
    
        this.router.navigate([beforeAuthPage]);
    
    });

  }

}