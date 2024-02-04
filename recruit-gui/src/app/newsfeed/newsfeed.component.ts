import { Component } 					from '@angular/core';
import { NewsfeedService }				from '../newsfeed.service';
import { NewsFeedItem, NewsFeedItemLine } from './news-feed-item';

@Component({
  selector: 'app-newsfeed',
  standalone: false,
  templateUrl: './newsfeed.component.html',
  styleUrl: './newsfeed.component.css'
})
export class NewsfeedComponent {

	public candidateId:string = "";
	private itemDivId:string = "";
	
	public handleswitchViewEvent():void{
		this.candidateId 	= "";
	}
	
	ngAfterViewChecked(){
		
		let id:string = ""+sessionStorage.getItem("news-item-div");
		
		if (id != "") {
			this.doScrollHere(id);
			//sessionStorage.setItem("news-item-div", "");
		}
	}
	
	public newsFeedItems:Array<NewsFeedItem> = new Array<NewsFeedItem>();
	
	/**
	* Constructor
	*/
	public constructor(private newsFeedService:NewsfeedService){
		sessionStorage.setItem("news-item-div", "");
		this.newsFeedService.getNewsFeedItems().subscribe(newsFeedItems => {
			this.newsFeedItems = newsFeedItems;
		});
	}
	
	/**
	* Sets candidateId to show candidate profile
	*/
	public viewCandidateProfile(candidateId:string, itemDivId:string):void{
		this.candidateId 	= candidateId.replace('/','');
		sessionStorage.setItem("news-item-div", itemDivId);
	}
	
	/**
	* Returns human readable version of Enum values
	*/
	public getHumanReadableType(item:NewsFeedItem):string{
		
		switch(item.itemType){
			case "CANDIDATE_CV_UPDATED":{			return "Candidate - CV Updated";}
			case "CANDIDATE_PROFILE_UPDATED":{		return "Candidate - Updated";}
			case "TODAYS_CANDIDATES":{				return "Todays Candidates";}
			case "CANDIDATE_ADDED":{				return "Candidate - Added";}
			case "CANDIDATE_DELETED":{				return "Candidate - Deleted";} 
			case "CANDIDATE_BECAME_UNAVAILABLE":{	return "Candidate - Unavailable";}
			case "CANDIDATE_BECAME_AVAILABLE":{		return "Candidate - Available";}
			case "USER_POST":{						return "Na";}
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
