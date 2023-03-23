import { Pipe, PipeTransform } from '@angular/core';

@Pipe({name: 'tupleStrValueByPos'})
export class TupleStrValueByPos implements PipeTransform{
	
	transform(value: [string,string], key:boolean): string {
		
		if (key) {
			return value.toString().substring(0, value.toString().indexOf(','));	
		}		
	   
  		return value.toString().substring(value.toString().indexOf(',')+1);

	}
	
}