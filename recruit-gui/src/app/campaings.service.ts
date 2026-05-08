import { HttpClient, HttpHeaders } 					from '@angular/common/http';
import { Injectable }	 							from '@angular/core';
import { Observable } 								from 'rxjs';
import { environment } 								from './../environments/environment';

@Injectable({
  providedIn: 'root'
})
/**
* Services and Objects for interacting with Campaigns. For example a 
* recruitent campaing for a particular Client with various roles and 
* the candidates and information related to the Role. 
*/
export class CampaingsService {
  
	constructor(private httpClient: HttpClient){}
	
	httpOptions = {
		headers: new HttpHeaders({ 'Content-Type': 'application/json' }), withCredentials: true
	};
	
	/**
	* Returns an overview of all the current Users Campaigins
	*/
	public fetchCampaignsForUser(): Observable<Array<CampaignOverview>>{
		
		const backendUrl:string = environment.backendUrl +'campaign';

	  	return this.httpClient.get<any>(backendUrl, this.httpOptions);

	}
	
	/**
	* Returns a specific Campaigin
	* @param campaiginId - Unique Id of the Campaigin to return
	*/
	public fetchCampaign(campaignId:string): Observable<Array<Campaign>>{
		
		const backendUrl:string = environment.backendUrl +'campaign/'+campaignId;

	  	return this.httpClient.get<any>(backendUrl, this.httpOptions);

	}
	
	/**
	* Creates a new Campaign
	* @param name 			- Name of the Campaign
	* @param description  	- Description of the Campaigin
	* @param logo 			- Optional logo to identify the Campaign 
	*/
	public addNewCampaign(name:string, description:string, logo:CampaignLogo):Observable<void>{
		
		let command:CommandAddCampaign = new CommandAddCampaign(name, description, logo);
		
		const backendUrl:string = environment.backendUrl +'campaign';

		return this.httpClient.post<any>(backendUrl, command, this.httpOptions);

		
	}
	
	/**
	* Creates a new Participation for a Campaigin
	* @param contacId 	- Id of Contact that will be participating in the Campaigin
	* @param campaignId - Id of the Campaign the Contact will be participating in
	* @param roleId 	- If role Level the Id of the Role the Participation relates to
	* @param type 		- Type of participation the Contact will have in the Campaign
	*/
	public addParticipation(contactId:string, campaignId:string, roleId:string, type:string):Observable<void>{
		
		let command:CommandAddParticipation = new CommandAddParticipation(contactId, campaignId, roleId, type);
		
		const backendUrl:string = environment.backendUrl +'campaign/participant';

		return this.httpClient.post<any>(backendUrl, command, this.httpOptions);
	
	}
	
	/**
	* Deletes an existing Participation
	* @param participationId - Id of the Participation to Delete 
	*/
	public deleteParticipation(participationId:string):Observable<void>{
		
		const backendUrl:string = environment.backendUrl +'campaign/participant/'+participationId;

		return this.httpClient.delete<void>(backendUrl, this.httpOptions);
		
	}
	
	/**
	* Adds a Note to a Campaign
	* @param campaignId - Campaigin to add the Note to
	* @param roleId		- If Note is for a specific Role the Id of the Role
	* @param title		- Title of the Note
	* @param text		- Note content 
	*/
	public addNote(campaignId:string, roleId:string, title:string, text:string):Observable<void>{
			
			let command:CommandAddNote = new CommandAddNote(campaignId, roleId, title, text);
					
			const backendUrl:string = environment.backendUrl +'campaign/note';

			return this.httpClient.post<any>(backendUrl, command, this.httpOptions);
		
	}
	
	/**
	* Update an existing Note in a Campaign
	* @param noteId		- Id of the Note to be updates
	* @param title		- Title of the Note
	* @param text		- Note content 
	*/
	public updateNote(noteId:string, title:string, text:string):Observable<void>{
			
			let command:CommandUpdateNote = new CommandUpdateNote(title, text);
			
			const backendUrl:string = environment.backendUrl +'campaign/note/'+noteId;

			return this.httpClient.put<any>(backendUrl, command, this.httpOptions);
		
	}
	
	/**
	* Delete an existing Note from a Campaign
	* @param noteId		- Id of the Note to be updates
	*/
	public deleteNote(noteId:string):Observable<void>{
			
		const backendUrl:string = environment.backendUrl +'campaign/note/'+noteId;

		return this.httpClient.delete<any>(backendUrl, this.httpOptions);
		
	}
	
	/**
	* Adds an Appointment to a Campaign
	* @param campaignId 	- Campaigin to add the Note to
	* @param roleId			- If Note is for a specific Role the Id of the Role
	* @param name			- Name of the Appointment
	* @param description	- Description of the Apppointment
	* @param videoLink		- If Video appointment, the link
	* @param phoneNumber 	- If Phone appointment, the number
	* @param when			- When the Appointment will take place
	*/
	public addAppointment(campaignId:string,
					roleId:string,
					name:string,
					description:string,
					videoLink:string,
					phoneNumber:string,
					when:Date):Observable<void>{
			
			let command:CommandAddAppointment = new CommandAddAppointment(campaignId, roleId, name,  description, videoLink, phoneNumber, when);
			
			const backendUrl:string = environment.backendUrl +'campaign/appointment';

			return this.httpClient.post<void>(backendUrl, command, this.httpOptions);
		
	}
	
	/**
	* Updates an Appointment in a Campaign
	* @param appointmentId 	- Id of the Appointment to be updated
	* @param campaignId 	- Campaigin to add the Note to
	* @param roleId			- If Note is for a specific Role the Id of the Role
	* @param name			- Name of the Appointment
	* @param description	- Description of the Apppointment
	* @param videoLink		- If Video appointment, the link
	* @param phoneNumber 	- If Phone appointment, the number
	* @param when			- When the Appointment will take place
	*/
	public updateAppointment(
					appointmentId:string,
					name:string,
					description:string,
					videoLink:string,
					phoneNumber:string,
					when:Date):Observable<void>{
			
			let command:CommandUpdateAppointment = new CommandUpdateAppointment(name, description, videoLink, phoneNumber, when);
					
			const backendUrl:string = environment.backendUrl +'campaign/appointment/'+appointmentId;

			return this.httpClient.put<void>(backendUrl, command, this.httpOptions);
		
	}
	
	/**
	* Deletes an existing Appointment from a Campaign
	* @param appointmentId - Id of the Appointment to delete
	*/
	public deleteAppointment(appointmentId:string):Observable<void>{
		
		const backendUrl:string = environment.backendUrl +'campaign/appointment/'+appointmentId;

		return this.httpClient.delete<void>(backendUrl, this.httpOptions);
		
	}
	
	/**
	* Adds a Document to an existing Campaign
	* @param campaignId 	- Id of the Campaigin to add the Document to
	* @param roleId 		- If Role specific the Role to add the Document to
	* @param title			- The title of the Document
	* @param type			- Document type ( i.e jped / pdf )
	* @param documentFile 	- Actual document File 
	*/
	public addDocument( campaignId:string,
						roleId:string,
						title:string,
						type:string,
						documentFile:File): Observable<any>{

		let command:CommandAddDocument = new CommandAddDocument(campaignId, roleId, title, type);
		
		const backendUrl:string = environment.backendUrl + 'campaign/document';
		
		var fd = new FormData();
		fd.append('documentFile', documentFile);
		fd.append("document", new Blob([JSON.stringify(command)], { type: 'application/json' }));
			
		return this.httpClient.post<any>(backendUrl, fd, {headers: new HttpHeaders({ }), withCredentials: true});

	}
	
	/**
	* Deletes a Document from an existing Campaign
	* @param documentId 	- Id of the Document to delete
	*/
	public deleteDocument( documentId:string): Observable<any>{

		const backendUrl:string = environment.backendUrl +'campaign/document/'+documentId;

		return this.httpClient.delete<any>(backendUrl, this.httpOptions);

	}
	
}

/**
* Command to create a new Campaign
*/
export class CommandAddCampaign{
	
	/**
	* Constructor 
	*/
	constructor(public name:string,
				public description:string,
				public logo:CampaignLogo){}
}

/**
* Command to add an Appointment to a Campaign
*/
export class CommandAddAppointment{
	
	/**
	* Constructor 
	*/
	constructor(public campaignId:string,
				public roleId:string,
				public name:string,
				public description:string,
				public videoLink:string,
				public phoneNumber:string,
				public when:Date) {}
	
}

/**
* Command to add a Document to a Campaign
*/
export class CommandAddDocument{
	
	/**
	* Constructor 
	*/
	constructor(public campaignId:string,
				public roleId:string,
				public title:string,
				public type:string){}
	
}

/**
* Command to add a Note to a Campaign
*/
export class CommandAddNote{
	
	/**
	* Constructor 
	*/
	constructor(public campaignId:string,
				public roleId:string,
				public title:string,
				public text:string){}
	
}

/**
* Command to add a Participation to a Campaign
*/
export class CommandAddParticipation{
	
	/**
	* Constructor 
	*/
	constructor(public contactId:string,
				public campaignId:string, 
				public roleId:string,
				public type:string){}
	
}

/**
* Command to update an existing Appointment in a Campaign
*/
export class CommandUpdateAppointment{
	
	/**
	* Constructor 
	*/
	constructor(public name:string,
				public description:string,
				public videoLink:string,
				public phoneNumber:string,
				public when:Date){}
	
}

/**
* Command to update and existing Note in a Campaign
*/
export class CommandUpdateNote{
	
	/**
	* Constructor 
	*/
	constructor(public title:string, 
				public text:string){}
	
}

/**
* Class represents a Logo that can be added to visually identify a 
* Campaign. For example if the Campaign is for a Bank, the Bank's 
* Logo. 
*/
export class CampaignLogo{
	
	/**
	* Constructor 
	*/
	constructor(public imageBytes:any, public format:string){}
}


/**
* Appointment 
*/
export class Appointment{
	
	/**
	* Constructor 
	*/
	constructor(public name:string,
				public description:string,
				public videoLink:string,
				public phoneNumber:string,
				public when:Date){}
	
}

/*
* Campaign overview. Light version of Campaigin without
* Child objects
*/
export class CampaignOverview{
	
	constructor(public id:string,
				public name:string,
				public description:string,
				public logo:CampaignLogo){}
}

/**
* Campaign 
*/
export class Campaign{
	
	/**
	* Constructor 
	*/
	constructor(public id:string,
				public name:string,
				public description:string,
				public logo:CampaignLogo,
				public candidates:Array<Candidate>,
				public participations:Array<Participation>,
				public notes:Array<Note>,
				public appointments:Array<Appointment>,
				public documents:Array<Document>){}
	
}

/**
* Candidate 
*/
export class Candidate{
	
	/**
	* Constructor 
	*/
	constructor(public id:string,
				public type:string,
				public firstName:string,
				public surname:string,
				public countryCode:string,
				public jobTitle:string,
				public email:string,
				public deletedFromSystem:boolean){}
	
}

/**
* Participation 
*/
export class Participation{
	
	/**
	* Constructor 
	*/
	constructor(public participationId:string,
				public contact:Contact,
				public type:string){}
	
}
	
/**
* Note 
*/
export class Note{
	
	/**
	* Constructor 
	*/
	constructor(public id:string,
				public created:Date,
				public title:string,
				public text:string){}
	
}	

/**
* Consact 
*/
export class Contact{
	
	/**
	* Constructor
	*/
	constructor(public firstName:string, public surname:string){}
}		