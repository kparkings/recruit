/**
* Settings for a Chart 
*/
export class ChartData{

	public chartLegend 						= true;
  	public chartPlugins 					= [];
  	public chartTitle:string				= "";
	
	public chartOptions = {
    	responsive: true
  	};

  	constructor(chartTitle:string){
		this.chartTitle = chartTitle;
	}
  	
  	public setDataAndLabels(values:number[], title:string, labels:string[]):void{
	}
	
	public switchStats(period:string):void{
		
	}
  	
}