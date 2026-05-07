import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

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
	
	//public fetchCampaignsForUser() : CampaignOverview
	//pubilc fetchCampaign(campaignId:string) : Campaign
	//public addNewCampaign(@RequestBody NewCampaignAPIInbound campaign) :Void
	//public addParticipation(@RequestBody AddParticipationAPIInbound participation) :Void
	//public deleteParticipation(@PathVariable("participationId") UUID participationId) : Void
	//public addNote(@RequestBody AddNoteAPIInbound note) : Void
	//public updateNote(@PathVariable("noteId")UUID noteId, @RequestBody UpdateNoteAPIInbound note): Void
	//public deleteNote(@PathVariable("noteId") UUID noteId) :  Void
	//public addAppointment(@RequestBody AddAppointmentAPIInbound appointment) : Void
	//public updateAppointment(@PathVariable("appointmentId")UUID appointmentId, @RequestBody UpdateAppointmentAPIInbound appointment) : Void
	//public deleteAppointment(@PathVariable("appointmentId") UUID appointmentId) : Void
	//public addDocument(@RequestPart("document") AddDocumentAPIInbound document, @RequestPart("documentBytes")MultipartFile documentBytes) : Void
	//public deleteDocument(@PathVariable("documentId") UUID documentId) : Void
	
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