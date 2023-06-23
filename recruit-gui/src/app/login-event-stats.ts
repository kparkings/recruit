export class LoginStats{
	
	public eventsToday:Array<Event> 		= new Array<Event>();
	public eventsWeek:Array<Event> 		= new Array<Event>();
	public weekStats:Array<Stat> 			= new Array<Stat>();
	public threeMonthStats:Array<Stat> 		= new Array<Stat>();
	public yearStats:Array<Stat> 			= new Array<Stat>();
	
	
	public getEventsTodayKeys():string[]{
		return Array.from(new Set(this.eventsToday.map(e => e.userId)));
	}
	
	public getEventsTodayValues():number[]{
	
		let values:Array<number> = new Array<number>();
		
		this.getEventsTodayKeys().forEach(k => {
			values.push(this.eventsToday.filter(e => e.userId === k).length);
		})
		
		return values;
		
	}
	
	
	
	
	public getEventsWeekKeys():string[]{
		return Array.from(new Set(this.eventsWeek.map(e => e.userId)));
	}
	
	public getEventsWeekValues():number[]{
	
		let values:Array<number> = new Array<number>();
		
		this.getEventsWeekKeys().forEach(k => {
			values.push(this.eventsWeek.filter(e => e.userId === k).length);
		})
		
		return values;
		
	}
	
	
	
	
	public getThreeMonthStatsKeysAsStrings():Array<string>{
		return Array.from(new Set(this.threeMonthStats.map(e => e.date.toString())));
	}
	
	public getThreeMonthStatsKeys():Array<Date>{
		return Array.from(new Set(this.threeMonthStats.map(e => e.date)));
	}

	public getThreeMonthStatsValues():number[]{
	
		let values:Array<number> = new Array<number>();
		
		this.getThreeMonthStatsKeys().forEach(k => {
			
			let stat:Stat = this.threeMonthStats.filter(e => e.date === k)[0]; 
			
			values.push(stat.logins.length);
		})
		
		return values;
		
	}
	
	
	
	public getWeekStatsKeysAsStrings():Array<string>{
		return Array.from(new Set(this.weekStats.map(e => e.date.toString())));
	}
	
	public getWeekStatsKeys():Array<Date>{
		return Array.from(new Set(this.weekStats.map(e => e.date)));
	}

	public getWeekStatsValues():number[]{
	
		let values:Array<number> = new Array<number>();
		
		this.getWeekStatsKeys().forEach(k => {
			
			let stat:Stat = this.weekStats.filter(e => e.date === k)[0]; 
			
			values.push(stat.logins.length);
		})
		
		return values;
		
	}
	
	
	public getYearStatsKeysAsStrings():Array<string>{
		return Array.from(new Set(this.yearStats.map(e => e.date.toString())));
	}
	
	public getYearStatsKeys():Array<Date>{
		return Array.from(new Set(this.yearStats.map(e => e.date)));
	}

	public getYearStatsValues():number[]{
	
		let values:Array<number> = new Array<number>();
		
		this.getYearStatsKeys().forEach(k => {
			
			let stat:Stat = this.yearStats.filter(e => e.date === k)[0]; 
			
			values.push(stat.logins.length);
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