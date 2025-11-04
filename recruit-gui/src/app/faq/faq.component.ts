import { Component } from '@angular/core';

@Component({
    selector: 'app-faq',
    templateUrl: './faq.component.html',
    styleUrls: ['./faq.component.css','./faq.component-mob.css'],
    standalone: false
})
export class FaqComponent {

	/**
	* Whether or not the user has authenticated with the System 
	*/
	public isAuthenticated():boolean {
		return sessionStorage.getItem('loggedIn') === 'true';
	}
	
}
