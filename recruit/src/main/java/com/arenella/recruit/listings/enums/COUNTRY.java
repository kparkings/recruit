package com.arenella.recruit.listings.enums;

/**
* Country where the Candidate lives
* @author K Parkings
*/
public enum COUNTRY {
	NETHERLANDS("nl", "Netherlands"), 
	BELGIUM("be", "Belgium"), 
	LUXEMBOURG("lu", "Luxembourg"),
	UK("gb", "United Kingdom"),
	REPUBLIC_OF_IRELAND("ie", "Ireland"),
	NORTHERN_IRELAND("xx", "Ireland"),
	GERMANY("de","Germany"),
	POLAND("pl","Poland"),
	FRANCE("fr","France"),
	SPAIN("es","Spain"),
	ITALY("it","Italy"),
	PORTUGAL("pt","Portugal"),
	AUSTRIA("at","Austria"),
	ROMANIA("ro","Romania"),
	GREECE("gr","Greece"),
	UKRAINE("ua","Ukraine"),
	SWITZERLAND("ch","Zwitzerland"),
	CZECH_REPUBLIC("cz","Czech Republic"),
	SWEDEN("se","Sweden"),
	HUNGARY("hu","Hungary"),
	BULGARIA("bg","Bulgaria"),
	DENMARK("dk","Denmark"),
	FINLAND("fi","Finland"),
	SLOVAKIA("sk","Slovakia"),
	CROATIA("hr","Croatia"),
	LITHUANIA("lt","Lithuania"),
	SLOVENIA("si","Slovenia"),
	LATVIA("lv","Latvia"),
	ESTONIA("ee","Estonia"),
	CYPRUS("cy","Cyprus"),
	MALTA("mt","Malta"),
	NORWAY("no","Norway"),
	LIECHTENSTEIN("li","Liechtenstein"),
	TURKEY("tr","Turkey"),
	INDIA("in","India"),
	PAKISTAN("pk","Pakistan"),
	US("us","United States"),
	CANADA("ca","Canada");
	
	private final String isoCode;
	private final String name;
	
	/**
	* Constructor 
	*/
	COUNTRY(String isoCode, String name) {
		this.isoCode = isoCode;
		this.name = name;
	}
	
	/**
	* Returns isoCode for Country
	* @return iso 2 code
	*/
	public String getIsoCode() {
		return this.isoCode;
	}
	
	/**
	* Returns the name of the Country
	* @return Name of the Country
	*/
	public String getHumanReadableName() {
		return this.name;
	}
	
}
