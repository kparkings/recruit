import { Component } 							from '@angular/core';
import { TranslateService } 					from '@ngx-translate/core';
import { NewsfeedService }						from '../newsfeed.service';
import { NewsFeedItem, NewsFeedItemLine } 		from './news-feed-item';

@Component({
  selector: 'app-newsfeed',
  standalone: false,
  templateUrl: './newsfeed.component.html',
  styleUrl: './newsfeed.component.css'
})
export class NewsfeedComponent {

	public		candidateId:string 					= "";
	private 	itemDivId:string 					= "";
	public 		lastNewsfeedView:Date				= new Date();
	public 		newsFeedItems:Array<NewsFeedItem> 	= new Array<NewsFeedItem>();
	
	public handleswitchViewEvent():void{
		this.candidateId 	= "";	
	}
	

	
	ngAfterViewChecked(){
		
		let id:string = ""+sessionStorage.getItem("news-item-div-id");

		if (id != null && id != "") {
			if(document.getElementById(id)){
				this.doScrollHere(id);
			}
		}
	}
	
	public isUnread(item:NewsFeedItem):boolean{
		return (item.created > this.lastNewsfeedView);
	}
	
	
	/**
	* Constructor
	*/
	public constructor(private newsFeedService:NewsfeedService, private translate:TranslateService){
		sessionStorage.removeItem("news-item-div-id");
		this.newsFeedService.getNewsFeedItems().subscribe(newsFeedItems => {
			this.newsFeedItems = newsFeedItems;
		});
		
		this.newsFeedService.getLastViewRecord().subscribe(record => {
			this.lastNewsfeedView = record.lastViewed;
			this.newsFeedService.updateLastViewedRecord().subscribe(respone => {
			});
		});
	}
	
	/**
	* Sets candidateId to show candidate profile
	*/
	public viewCandidateProfile(candidateId:string, itemDivId:string):void{
		this.candidateId 	= candidateId.replace('/','');
		sessionStorage.setItem("news-item-div-id", itemDivId);
	}
	
	/**
	* Returns human readable version of Enum values
	*/
	public getHumanReadableType(item:NewsFeedItem):string{
		
		switch(item.itemType){
			case "CANDIDATE_CV_UPDATED":{			return this.translate.instant('newsfeed-cv-updated');}//"Candidate - CV Updated";}
			case "CANDIDATE_PROFILE_UPDATED":{		return this.translate.instant('newsfeed-candidate-updated');}//"Candidate - Updated";}
			case "TODAYS_CANDIDATES":{				return this.translate.instant('newsfeed-todays-candidates');}//"Todays Candidates";}
			case "CANDIDATE_ADDED":{				return this.translate.instant('newsfeed-candidate-added');}//Candidate - Added";}
			case "CANDIDATE_DELETED":{				return this.translate.instant('newsfeed-candidate-deleted');}//"Candidate - Deleted";} 
			case "CANDIDATE_BECAME_UNAVAILABLE":{	return this.translate.instant('newsfeed-candidate-unavailable');}//"Candidate - Unavailable";}
			case "CANDIDATE_BECAME_AVAILABLE":{		return this.translate.instant('newsfeed-candidate-available');}//"Candidate - Available";}
			case "USER_POST":{						return this.translate.instant('newsfeed-na');}//"Na");}
			default:{
				return item.itemType;
			} 
									
		}
		
	}
	
	public doScrollHere(id:string):void{
		(<HTMLInputElement>document.getElementById(id)).scrollIntoView({
			block: "start",
			inline: "nearest"
			});
	}
	
}
