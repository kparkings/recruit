import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { RouterModule} from '@angular/router';

/**
* Provides services relating to Angular routeing Guards
* @author K Parkings
*/ 
@Injectable({
  providedIn: 'root'
})
export class AuthGuardService implements CanActivate{

  /**
  * Constructor for the class
  * @param router - Angualr routeing 
  */
  constructor(private router: Router) {

  }

  /**
  * Refer to the CanActivate Interface for details
  */
  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean{

    if (!sessionStorage.getItem("loggedIn")) {
      
      const url: string = route.url.toString();

      sessionStorage.setItem("beforeAuthPage", url);

      this.router.navigate(['/login-user']);

      return false;

    }

    const beforeAuthPage: any = sessionStorage.getItem('beforeAuthPage');
    
    if (beforeAuthPage) {

      sessionStorage.removeItem("beforeAuthPage");

      this.router.navigate([beforeAuthPage]);

      return false;
      
    }

    return true;
    
  }

}