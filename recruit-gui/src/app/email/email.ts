import {Sender} 			from './email-sender';
import {EmailAttachment} 	from './email-attachment';

/**
* Represents an internal Email in the System
*/
export class Email{
	
	public id:string							= '';
	public title:string							= '';
	public created:Date							= new Date();
	public body:string							= '';
	public viewed:boolean						= false;
	public sender:Sender						= new Sender();
	public attachments:Array<EmailAttachment> 	= new Array<EmailAttachment>();
}
