import { Injectable } 			from '@angular/core';
import { Country } 				from './shared-domain-object/country';
import { FunctionType } 		from './shared-domain-object/function-type';
import { LanguageType } 		from './shared-domain-object/language-type';
import { ContractType } 		from './suggestions/contract-type';
import { TranslateService } 	from '@ngx-translate/core';

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
	constructor(private translate:TranslateService) { }

	/**
	* Returns the types of roles recognised by the system
	*/
	public fetchFunctionTypes():Array<FunctionType>{
		
		let functionTypes:Array<FunctionType>  = new Array<FunctionType>();
		
		functionTypes.push(new FunctionType("JAVA",this.translate.instant('static-func-java')));
		functionTypes.push(new FunctionType("DOT_NET",this.translate.instant('static-func-dotnet')));
		functionTypes.push(new FunctionType("DEV_OPS",this.translate.instant('static-func-devops')));
		functionTypes.push(new FunctionType("NETWORKS",this.translate.instant('static-func-networkd')));
		functionTypes.push(new FunctionType("CLOUD",this.translate.instant('static-func-cloud')));
		functionTypes.push(new FunctionType("WEB",this.translate.instant('static-func-web')));
		functionTypes.push(new FunctionType("UI_UX",this.translate.instant('static-func-uiux')));
		functionTypes.push(new FunctionType("PROJECT_NANAGMENT",this.translate.instant('static-func-pmo')));
		functionTypes.push(new FunctionType("TESTING",this.translate.instant('static-func-testing')));
		functionTypes.push(new FunctionType("BUSINESS_ANALYSTS",this.translate.instant('static-func-ba')));
		functionTypes.push(new FunctionType("SECURITY",this.translate.instant('static-func-security')));
		functionTypes.push(new FunctionType("IT_SUPPORT",this.translate.instant('static-func-supporrty')));
		functionTypes.push(new FunctionType("ARCHITECT",this.translate.instant('static-func-architecture')));
		functionTypes.push(new FunctionType("BI",this.translate.instant('static-func-bi')));
		functionTypes.push(new FunctionType("REC2REC",this.translate.instant('static-func-recruitment')));
		
		return functionTypes;
	}
	
	/**
	* Returns Countries a listing can be for
	*/
	public fetchCountries():Array<Country>{
		
		let countries:Array<Country> = new Array<Country>();
		
		countries.push(new Country("NETHERLANDS", this.translate.instant('static-country-nl')));
		countries.push(new Country("BELGIUM", this.translate.instant('static-country-be')));
		countries.push(new Country("UK", this.translate.instant('static-country-uk')));
		countries.push(new Country("IRELAND", this.translate.instant('static-country-ir')));
		countries.push(new Country("EU_REMOTE", this.translate.instant('static-country-eu')));
		countries.push(new Country("WORLD_REMOTE", this.translate.instant('static-country-world')));
		
		return countries;
		
	}
	
	/**
	* Returns ContractTypes a listing can be for
	*/
	public fetchContractTypes():Array<ContractType>{
		
		let contractTypes:Array<ContractType> = new Array<ContractType>();
		
		contractTypes.push(new ContractType("CONTRACT_ROLE", this.translate.instant('static-contract-type-contract')));
		contractTypes.push(new ContractType("PERM_ROLE", this.translate.instant('static-contract-type-perm')));
		contractTypes.push(new ContractType("BOTH", this.translate.instant('static-contract-type-both'))); 
		
		return contractTypes;
		
	}
	
		/**
	* Returns ContractTypes a listing can be for
	*/
	public fetchLanguageTypes():Array<LanguageType>{
		
		let languageTypes:Array<LanguageType> = new Array<LanguageType>();
		
		languageTypes.push(new LanguageType("DUTCH", this.translate.instant('static-lang-dutch')));
		languageTypes.push(new LanguageType("FRENCH", this.translate.instant('static-lang-french')));
		languageTypes.push(new LanguageType("ENGLISH", this.translate.instant('static-lang-english')));
		
		return languageTypes;
		
	}
}