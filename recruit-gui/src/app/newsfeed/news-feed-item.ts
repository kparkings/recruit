
/**
* An item to be displayed in a news feed
*/									
export class NewsFeedItem{
	
	/**
	* Constructor
	*/
	constructor(public id:string, public created:Date, public itemType:string, public lines:Array<NewsFeedItemLine>){}
	
}

/**
* Line making up part of the Item
*/
export class NewsFeedItemLine{
	
	/**
	* Constructor
	*/
	public constructor(public type:string, public text:string, public url:string, public image:Array<any>){}
	
}
