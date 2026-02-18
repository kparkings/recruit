import { Injectable } 												from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree } 	from '@angular/router';
import { Observable } 												from 'rxjs';

/**
* Guard to restrict access to pages only available to Admin Users
*/
@Injectable({
  providedIn: 'root'
})
export class AdminGuardGuard  {
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
   
      if (sessionStorage.getItem('isAdmin')) {

        const isAdmin = sessionStorage.getItem('isAdmin');

        if (isAdmin === 'true') {
          return true;
        }

      }

      return false;
  }
  
}
