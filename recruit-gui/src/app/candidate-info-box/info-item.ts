/**
* Defines an Confit  that can be displayed. This will be a 
* group of blocks of information such as Salary information 
* that can be displayed as a unique block on the page 
*/
export interface InfoItem{
	
	/**
	* Returns the title for the Item 
	*/
	getTitle():string;
	
	/**
	* Returns lines of info. This could be key value 
	* pairs for example with a label and value 
	*/
	getRows():Array<InfoItemRow>;
	
}

/**
* A row to be displayed in the InfoItem box
*/
export interface InfoItemRow{
	
	/**
	* If the Row has a lable
	*/
	hasLabel():boolean;
	
	/**
	* If the Row contains an Array of values
	*/
	hasMultipleValues():boolean;
	
	/**
	* Returns the Label
	*/
	getLabel():any;
	
	/**
	* Returns the value/s
	*/
	getValue():any;
	
	/**
	* CSS class name to apply to vaue  
	*/
	getValueItemCSS():string;
	
}

/**
* Repsresentsa block of information 
*/
export class InfoItemBlock implements InfoItem {
	
	private title:string 			= '';
	private rows:Array<InfoItemRow> = new Array<InfoItemRow>();
	
	public setTitle(title:string){
		this.title = title;
	}
	
	/**
	* Adds a row of info to the item
	*/
	public addRow(row:InfoItemRow){
		this.rows.push(row);
	}
	
	/**
	* Returns the title for the Item 
	*/
	public getTitle():string{
		return this.title;
	}
	
	/**
	* Returns lines of info. This could be key value 
	* pairs for example with a label and value 
	*/
	public getRows():Array<InfoItemRow>{
		return this.rows;
	}
	
}

/**
* A row of information to display where 
* the structure is key/value 
*/
export class InfoItemRowKeyValue implements InfoItemRow{
	
	private label:string 	= '';
	private value:string 	= '';
	
	/**
	* Constructor
	*/
	public constructor(label:string, value:string){
		this.label 	= label;
		this.value 	= value;
	}
	
	/**
	* Refer to the InfoItemRow interface
	*/
	public hasLabel(): boolean {
		return true;
	}
	
	public hasMultipleValues():boolean{
		return false;
	}
	
	/**
	* Refer to the InfoItemRow interface
	*/
	public getLabel():any{
		return this.label;		
	}
	
	/**
	* Refer to the InfoItemRow interface
	*/
	public getValue():any{
		return this.value;	
	}
    
    /**
	* Refer to the InfoItemRow interface
	*/
    getValueItemCSS(){
		return "";
	}
}

/**
* A row of information to display where 
* the structure is a single value to display
* under the title 
*/
export class InfoItemRowSingleValue implements InfoItemRow{
	
	private value:any 	= '';
	
	/**
	* Constructor
	*/
	public constructor(value:any){
		this.value 	= value;
	}
	
	/**
	* Refer to the InfoItemRow interface
	*/
	public hasLabel(): boolean {
		return false;
	}
	
	public hasMultipleValues():boolean{
		return false;
	}
	
	/**
	* Refer to the InfoItemRow interface
	*/
	public getLabel():any{
		return "";		
	}
	
	/**
	* Refer to the InfoItemRow interface
	*/
	public getValue():any{
		return this.value;	
	}
	
	/**
	* Refer to the InfoItemRow interface
	*/
    getValueItemCSS(){
		return "";
	}
    
}

/**
* A row of information to display where 
* the structure is an array of values 
*/
export class InfoItemRowMultiValues implements InfoItemRow{
	
	private value:Array<any> 		= new Array<any>();
	private cssClass:string = '';
	
	/**
	* Constructor
	*/
	public constructor(value:Array<any>, cssClass:string){
		this.value 		= value;
		this.cssClass 	= cssClass;
	}
	
	/**
	* Refer to the InfoItemRow interface
	*/
	public hasLabel(): boolean {
		return false;
	}
	
	public hasMultipleValues():boolean{
		return true;
	}
	
	/**
	* Refer to the InfoItemRow interface
	*/
	public getLabel():any{
		return "";		
	}
	
	/**
	* Refer to the InfoItemRow interface
	*/
	public getValue():Array<any>{
		return this.value;	
	}
	
	/**
	* Refer to the InfoItemRow interface
	*/
    getValueItemCSS():string{
		return this.cssClass;
	}
    
}


/**
* Configuration of blcks on information to be
* displayed 
*/
export class InfoItemConfig{
	
	private title:string 						= '';
	private items:Array<InfoItem> 				= new Array<InfoItem>();
	private profilePhoto:any;
	
	/**
	* Sets the bytes of Poto to be displayed
	*/
	public setProfilePhoto(profilePhoto:any):void{
		this.profilePhoto = profilePhoto;
	}
	
	/**
	* Sets the main title
	*/
	public setTitle(title:string):void{
		this.title = title;
	}
	
	/**
	* Returns the main title
	*/
	public getTitle():string{
		return this.title;
	}
	
	/**
	* Adds an Item to be displayed
	*/
	public addItem(row:InfoItem){
		this.items.push(row);
	}
	
	/**
	* * Refer to the InfoItem interface 
	*/
	public getItems():Array<InfoItem>{
		return this.items;
	}
	
	/**
	* Whether a photo is available to display
	*/
	public hasProfilePhoto():boolean{
		return this.profilePhoto;
	}
	
	/**
	* Returns bytes of the Photo
	*/
	public getProfilePhotoBytes():any{
		return this.profilePhoto;
	}
	
}