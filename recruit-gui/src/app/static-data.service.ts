import { Injectable } from '@angular/core';
import { Country } from './shared-domain-object/country';
import { FunctionType } from './shared-domain-object/function-type';
import { LanguageType } from './shared-domain-object/language-type';
import { ContractType } from './suggestions/contract-type';

/**
* Service contains static data. Typically data that maps to the back end APIs
*/
@Injectable({
  providedIn: 'root'
})
export class StaticDataService {

	/**
	* Constructor 
	*/
	constructor() { }

	/**
	* Returns the types of roles recognised by the system
	*/
	public fetchFunctionTypes():Array<FunctionType>{
		
		let functionTypes:Array<FunctionType>  = new Array<FunctionType>();
		
		functionTypes.push(new FunctionType("JAVA","Java"));
		functionTypes.push(new FunctionType("DOT_NET","Dot Net"));
		functionTypes.push(new FunctionType("DEV_OPS","Devops"));
		functionTypes.push(new FunctionType("NETWORKS","Networks"));
		functionTypes.push(new FunctionType("CLOUD","Cloud"));
		functionTypes.push(new FunctionType("WEB","Web"));
		functionTypes.push(new FunctionType("UI_UX","UI/UX"));
		functionTypes.push(new FunctionType("PROJECT_NANAGMENT","Project Management"));
		functionTypes.push(new FunctionType("TESTING","Testing"));
		functionTypes.push(new FunctionType("BUSINESS_ANALYSTS","BA"));
		functionTypes.push(new FunctionType("SECURITY","IT Security"));
		functionTypes.push(new FunctionType("IT_SUPPORT","IT Support"));
		functionTypes.push(new FunctionType("ARCHITECT","Architecture"));
		functionTypes.push(new FunctionType("BI","BI"));
		functionTypes.push(new FunctionType("REC2REC","Recruitment"));
		
		return functionTypes;
	}
	
	/**
	* Returns Countries a listing can be for
	*/
	public fetchCountries():Array<Country>{
		
		let countries:Array<Country> = new Array<Country>();
		
		countries.push(new Country("NETHERLANDS", "The Netherlnads"));
		countries.push(new Country("BELGIUM", "Belgium"));
		countries.push(new Country("UK", "UK"));
		countries.push(new Country("IRELAND", "Ireland"));
		countries.push(new Country("EU_REMOTE", "Remote (EU)"));
		countries.push(new Country("WORLD_REMOTE", "Remote (Worldwide)"));
		
		return countries;
		
	}
	
	/**
	* Returns ContractTypes a listing can be for
	*/
	public fetchContractTypes():Array<ContractType>{
		
		let contractTypes:Array<ContractType> = new Array<ContractType>();
		
		contractTypes.push(new ContractType("CONTRACT_ROLE", "Contract"));
		contractTypes.push(new ContractType("PERM_ROLE", "Perm"));
		contractTypes.push(new ContractType("BOTH", "Contract/Perm"));
		
		return contractTypes;
		
	}
	
		/**
	* Returns ContractTypes a listing can be for
	*/
	public fetchLanguageTypes():Array<LanguageType>{
		
		let languageTypes:Array<LanguageType> = new Array<LanguageType>();
		
		languageTypes.push(new LanguageType("DUTCH", "Dutch"));
		languageTypes.push(new LanguageType("FRENCH", "French"));
		languageTypes.push(new LanguageType("ENGLISH", "English"));
		
		return languageTypes;
		
	}
}