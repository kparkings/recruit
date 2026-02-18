export class LoginStats{
	
	public eventsToday:Array<Event> 		= new Array<Event>();
	public eventsWeek:Array<Event> 			= new Array<Event>();
	public weekStats:Array<Stat> 			= new Array<Stat>();
	public threeMonthStats:Array<Stat> 		= new Array<Stat>();
	public yearStats:Array<Stat> 			= new Array<Stat>();
	
	public getEventsTodayKeys():string[]{
		return Array.from(new Set(this.eventsToday.map(e => e.userId)));
	}
	
	public getEventsTodayValues():string[]{
	
		let values:Array<string> = new Array<string>();
		
		this.getEventsTodayKeys().forEach(k => {
			values.push(''+this.eventsToday.filter(e => e.userId === k).length);
		})
		
		return values;
		
	}
	
	public getEventsWeekKeys():string[]{
		return Array.from(new Set(this.eventsWeek.map(e => e.userId)));
	}
	
	public getEventsWeekValues():string[]{
	
		let values:Array<string> = new Array<string>();
		
		this.getEventsWeekKeys().forEach(k => {
			values.push(''+this.eventsWeek.filter(e => e.userId === k).length);
		})
		
		return values;
		
	}
	
	public getThreeMonthStatsKeysAsStrings():Array<string>{
		return Array.from(new Set(this.threeMonthStats.map(e => e.date.toString())));
	}
	
	public getThreeMonthStatsKeys():Array<Date>{
		return Array.from(new Set(this.threeMonthStats.map(e => e.date)));
	}

	public getThreeMonthStatsValues():string[]{
	
		let values:Array<string> = new Array<string>();
		
		this.getThreeMonthStatsKeys().forEach(k => {
			
			let stat:Stat = this.threeMonthStats.filter(e => e.date === k)[0]; 
			
			values.push(''+stat.logins.length);
		})
		
		return values;
		
	}
	
	public getWeekStatsKeysAsStrings():Array<string>{
		return Array.from(new Set(this.weekStats.map(e => e.date.toString())));
	}
	
	public getWeekStatsKeys():Array<Date>{
		return Array.from(new Set(this.weekStats.map(e => e.date)));
	}

	public getWeekStatsValues():string[]{
	
		let values:Array<string> = new Array<string>();
		
		this.getWeekStatsKeys().forEach(k => {
			
			let stat:Stat = this.weekStats.filter(e => e.date === k)[0]; 
			
			values.push(''+stat.logins.length);
		})
		
		return values;
		
	}
	
	public getYearStatsKeysAsStrings():Array<string>{
		return Array.from(new Set(this.yearStats.map(e => e.date.toString())));
	}
	
	public getYearStatsKeys():Array<Date>{
		return Array.from(new Set(this.yearStats.map(e => e.date)));
	}

	public getYearStatsValues():string[]{
	
		let values:Array<string> = new Array<string>();
		
		this.getYearStatsKeys().forEach(k => {
			
			let stat:Stat = this.yearStats.filter(e => e.date === k)[0]; 
			
			values.push(''+stat.logins.length);
		})
		
		return values;
		
	}
	
}

export class Stat{
	public date:Date = new Date();
	public logins: Array<string> = new Array<string>(); 
}

export class Event{
	userId:string = '';
	recruiter:boolean = false;
	candidate:boolean = false;
	loggedInAt:Date = new Date();
}