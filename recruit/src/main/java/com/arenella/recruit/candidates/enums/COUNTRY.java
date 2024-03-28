package com.arenella.recruit.candidates.enums;

/**
* Country where the Candidate lives
* @author K Parkings
*/
public enum COUNTRY {
	NETHERLANDS("nl"), 
	BELGIUM("be"), 
	LUXEMBOURG("lu"),
	UK("gb"),
	REPUBLIC_OF_IRELAND("ie"),
	NORTHERN_IRELAND("gb"),
	GERMANY("de"),
	POLAND("pl"),
	FRANCE("fr"),
	SPAIN("es"),
	ITALY("it"),
	PORTUGAL("pt"),
	AUSTRIA("at"),
	ROMANIA("ro"),
	GREECE("gr"),
	UKRAINE("ua"),
	SWITZERLAND("ch"),
	CZECH_REPUBLIC("cz"),
	SWEDEN("se"),
	HUNGARY("hu"),
	BULGARIA("bg"),
	DENMARK("dk"),
	FINLAND("fl"),
	SLOVAKIA("sk"),
	CROATIA("hr"),
	LITHUANIA("lt"),
	SLOVENIA("si"),
	LATVIA("lv"),
	ESTONIA("ee"),
	CYPRUS("cy"),
	MALTA("mt"),
	NORWAY("no"),
	LIECHTENSTEIN("li"),
	TURKEY("tr"),
	INDIA("in"),
	PAKISTAN("pk"),
	US("us"),
	CANADA("ca");
	
	private final String isoCode;
	
	/**
	* Constructor 
	*/
	COUNTRY(String isoCode) {
		this.isoCode = isoCode;
	}
	
	/**
	* Returns isoCode for Country
	* @return iso 2 code
	*/
	public String getIsoCode() {
		return this.isoCode;
	}
	
}
