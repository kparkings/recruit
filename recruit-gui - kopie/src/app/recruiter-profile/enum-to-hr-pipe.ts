import { Pipe, PipeTransform } from '@angular/core';

/**
* Pipe for taking a enum values and converting it to 
* a human readable version
**/
@Pipe({name: 'enumToHumanReadableValue'})
export class EnumToHumanReadableValue implements PipeTransform{
	
	transform(value: string): string {

		let newVal:string = '';
		
		var parts = value.split('_');
		
		parts.forEach(part => {
			
			let newPart = part.substring(0,1).toUpperCase();
			newPart = newPart + part.substring(1).toLowerCase();
			
			if(newPart === 'Eu'){
				newPart = 'EU';
			}
					
			newVal = newVal + newPart + ' ';
				
		});

		return newVal;		

	}
	
}