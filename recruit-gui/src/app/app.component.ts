import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
}) 
export class AppComponent {

  title = 'recruit-ui';

  /**
  * Whether or not the user has authenticated with the System 
  */
  public isAuthenticated():boolean {
    return sessionStorage.getItem('loggedIn') === 'true';
  }

  /**
  * Whether or not the user has authenticated as an Admin user 
  */
  public isAuthenticatedAsAdmin():boolean {
    return sessionStorage.getItem('isAdmin') === 'true';
  }

}
