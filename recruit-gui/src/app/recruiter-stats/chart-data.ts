import { ChartDataSets, ChartOptions, ChartType } 			from 'chart.js';
import { Color, Label } 									from 'ng2-charts';

/**
* Settings for a Chart 
*/
export class ChartData{

	public chartData:ChartDataSets[] 		= [{ data: new Array<number>(), label: 'na' },];
	public chartLabels:Label[] 				= [];
	public chartLegend 						= true;
  	public chartPlugins 					= [];
  	public chartType:ChartType 				= 'bar';
  	public chartTitle:string				= "";
	
	public chartOptions = {
    	responsive: true
  	};

  	public chartColors: Color[] = [
    	{
      		borderColor: 'black',
      		backgroundColor: 'rgba(0,0,0,0.28)',
    	},
  	];
  	
  	constructor(chartTitle:string){
		this.chartTitle = chartTitle;
	}
  	
  	public setDataAndLabels(values:number[], title:string, labels:string[]):void{
		this.chartData = [{ data: values, label: title },];
		this.chartLabels = labels;
	}
	
	public switchStats(period:string):void{
		
	}
  	
}