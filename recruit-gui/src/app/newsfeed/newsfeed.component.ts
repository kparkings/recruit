import { Component } 								from '@angular/core';
import { NewsFeedItem, NewsFeedItemLine } 			from './news-feed-item';
import { PublicMessagingService}					from '../public-messaging.service';
import { UntypedFormGroup, UntypedFormControl }		from '@angular/forms';
import { PublicChat}								from './public-chat';
import { AppComponent } 							from 'src/app/app.component';

@Component({
  selector: 'app-newsfeed',
  standalone: false,
  templateUrl: './newsfeed.component.html',
  styleUrl: './newsfeed.component.css'
})
export class NewsfeedComponent {

	public topLevelPosts:Array<PublicChat> = new Array<PublicChat>();
	
	/**
	* Constructor
	* @oaramparam service  - Services for PublicMessages 
	*/
	public constructor(public service:PublicMessagingService, private appComponent:AppComponent){
		this.refreshPosts();
	}
	
	private refreshPosts():void{
		this.service.fetchPageOfTopLevelChats(0,10000).subscribe(chats => {
			this.topLevelPosts = chats;
			this.topLevelPosts.sort((one:PublicChat, two:PublicChat) => this.isGreater(one,two));
		});	
	}
	
	public newMessageForm:UntypedFormGroup = new UntypedFormGroup({
		message: 			new UntypedFormControl(),
	});
	
	/**
	* Creates a new message
	*/
	public createNewMessage():void{
		this.service.createChat(undefined, this.newMessageForm.get("message")?.value).subscribe(res => {
			this.service.fetchPageOfTopLevelChats(0,10000).subscribe(chats => {
				this.topLevelPosts = chats;
				this.topLevelPosts.sort((one:PublicChat, two:PublicChat) => this.isGreater(one,two));
				this.newMessageForm = new UntypedFormGroup({
					message: new UntypedFormControl(),
				});
			});
		});
	}
	
	public deleteChat(chat:PublicChat):void{
		this.service.deleteChat(chat.id).subscribe(response => {
			this.refreshPosts();
		});
	}
	
	/**
	* Comparator to sort Chats in descending creation time order 
	*/
	private isGreater(one:PublicChat, two:PublicChat):number{
		console.log(one.created < two.created);
		
		if(one.created > two.created){
			return -1;
		}
		
		if(one.created < two.created){
			return 1;
		}
		
		return 0;
	}
	
	/**
	* Returns whether the Chat contains a profile
	* image for the owner 
	*/
	public hasImage(chat:PublicChat):boolean{
		if (chat.owner.photo ) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	* Returns the bytes of the profile image of the owner 
	*/
	public getChatParticipantPhoto(chat:PublicChat):any{
		return chat.owner.photo.imageBytes;
	}
	
	/**
	* Returns whether a given message was created by the authenticated
	* user or another user
	* @param msg: Message to be checked
	*/
	public isOwnMessage(chat:PublicChat):boolean{
		return chat.owner.id == sessionStorage.getItem("userId") ? true : false;
	}
	
	/**
	* Opens Chat session with the owner of the Chat 
	*/
	public openChat(chat:PublicChat):void{
		this.appComponent.privateChat.openChat(chat.owner.id);	
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	//public		candidateId:string 					= "";
	//private 	itemDivId:string 					= "";
	//public 		lastNewsfeedView:Date				= new Date();
	//public 		newsFeedItems:Array<NewsFeedItem> 	= new Array<NewsFeedItem>();
	
	//public handleswitchViewEvent():void{
	//	this.candidateId 	= "";	
	//}
	

	
	//ngAfterViewChecked(){
		
	//	let id:string = ""+sessionStorage.getItem("news-item-div-id");

	//	if (id != null && id != "") {
	//		if(document.getElementById(id)){
	//			this.doScrollHere(id);
	//		}
	//	}
	//}
	
	//public isUnread(item:NewsFeedItem):boolean{
	//	return (item.created > this.lastNewsfeedView);
	//}
	
	
	/**
	* Constructor
	*/
	//public constructor(private newsFeedService:NewsfeedService, private translate:TranslateService){
	//	sessionStorage.removeItem("news-item-div-id");
	//	this.newsFeedService.getNewsFeedItems().subscribe(newsFeedItems => {
	//		this.newsFeedItems = newsFeedItems;
	//	});
		
	//	this.newsFeedService.getLastViewRecord().subscribe(record => {
	//		this.lastNewsfeedView = record.lastViewed;
	//		this.newsFeedService.updateLastViewedRecord().subscribe(respone => {
	//		});
	//	});
	//}
	
	/**
	* Sets candidateId to show candidate profile
	*/
	//public viewCandidateProfile(candidateId:string, itemDivId:string):void{
	//	this.candidateId 	= candidateId.replace('/','');
	//	sessionStorage.setItem("news-item-div-id", itemDivId);
	//}
	
	/**
	* Returns human readable version of Enum values
	*/
	//public getHumanReadableType(item:NewsFeedItem):string{
	//	
	//	switch(item.itemType){
	//		case "CANDIDATE_CV_UPDATED":{			return this.translate.instant('newsfeed-cv-updated');}//"Candidate - CV Updated";}
	//		case "CANDIDATE_PROFILE_UPDATED":{		return this.translate.instant('newsfeed-candidate-updated');}//"Candidate - Updated";}
	//		case "TODAYS_CANDIDATES":{				return this.translate.instant('newsfeed-todays-candidates');}//"Todays Candidates";}
	//		case "CANDIDATE_ADDED":{				return this.translate.instant('newsfeed-candidate-added');}//Candidate - Added";}
	//		case "CANDIDATE_DELETED":{				return this.translate.instant('newsfeed-candidate-deleted');}//"Candidate - Deleted";} 
	//		case "CANDIDATE_BECAME_UNAVAILABLE":{	return this.translate.instant('newsfeed-candidate-unavailable');}//"Candidate - Unavailable";}
	//		case "CANDIDATE_BECAME_AVAILABLE":{		return this.translate.instant('newsfeed-candidate-available');}//"Candidate - Available";}
	//		case "USER_POST":{						return this.translate.instant('newsfeed-na');}//"Na");}
	//		default:{
	//			return item.itemType;
	//		} 
	//								
	//	}
		
	//}
	
	//public doScrollHere(id:string):void{
	//	(<HTMLInputElement>document.getElementById(id)).scrollIntoView({
	//		block: "start",
	//		inline: "nearest"
	//		});
	//}
	
}
