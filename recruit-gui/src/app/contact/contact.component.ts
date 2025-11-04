import { Component } from '@angular/core';

@Component({
    selector: 'app-contact',
    templateUrl: './contact.component.html',
    styleUrls: ['./contact.component.css','./contact.component-mob.css'],
    standalone: false
})
export class ContactComponent {

	/**
	* Whether or not the user has authenticated with the System 
	*/
	public isAuthenticated():boolean {
		return sessionStorage.getItem('loggedIn') === 'true';
	}
	
}
