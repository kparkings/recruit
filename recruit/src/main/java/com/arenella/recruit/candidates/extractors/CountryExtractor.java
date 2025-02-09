package com.arenella.recruit.candidates.extractors;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.arenella.recruit.candidates.beans.CandidateExtractedFilters.CandidateExtractedFiltersBuilder;
import com.arenella.recruit.candidates.enums.COUNTRY;

/**
* Extractor to determine countries to search in
* @author K Parkings
*/
@Component
public class CountryExtractor implements JobSpecifcationFilterExtractor{
	
	public static final Set<String> UK 					= DocumentFilterExtractionUtil.UK;
	public static final Set<String> NETHERLANDS 		= Set.of("olanda","pays-bas","países bajos","niederlande","'s-gravenhage","groningen","brabant","zuid-holland","arnhem","nieuwegein","schiphol", "zwolle","netherlands","nederland", "amsterdam", "apeldoorn", "utrecht", "rotterdam", "randstad", "amstelveen", "woerden", "amersfoort", "soest", "den haag", "the hague", "overijssel", "gelderland", "almere", "eindhoven", "enschede", "limburg", "flevoland", "alkmaar");
	public static final Set<String> BELGIUM 			= Set.of("bruge","antwerp","liège","belgië","belgio","belgique","bélgica","belgien","belgium", "brussels", "leuven","mechelen", "bruxelles", "gand", "courtrai", "antwerp", "antwerpen", "flemish", "flams", "wallon", "mechelen", " gent", "charleoi", "meeuwen","Kortrijk", "namur"); 
	public static final Set<String> LUXEMBOURG 			= Set.of("lussemburgo","luxembourg","luxemburgo","luxemburg","luxembourg","luxemburg","luxembourgish");
	public static final Set<String> REPUBLIC_OF_IRELAND = Set.of("navan","drogheda","swords","waterford","dundalk","cork","irlande","irlanda","irland","ireland", "dublin", "galway", "letterkenny", "limerick", "kildare");
	public static final Set<String> NORTHERN_IRELAND 	= Set.of("country armagh","country antrim","country down","newry","lisburn","bangor","londonderry","newtownabbey","craigavon","derry","belfast","irlande","irlanda","irland","ireland");
	public static final Set<String> GERMANY 			= Set.of("duitsland","germania","allemagne","alemania","deutschland","germany","berlin","hamburg","munich","münchen","cologne","köln","frankfurt","stuttgart","düsseldorf","leipzig","dortmund","essen","bremen","hanover","hannover","nuremberg","duisburg","bochum","wuppertal","bielefeld","bonn","münster","mannheim","karlsruhe","augsburg","wiesbaden","mönchengladbach","gelsenkirchen","aachen","braunschweig","kiel","chemnitz","halle","saale","magdeburg","freiburg im breisgau","krefeld","mainz","lübeck","erfurt","oberhausen","rostock","kassel","hagen","potsdam","saarbrücken","hamm","ludwigshafen am rhein","mülheim an der ruhr","oldenburg","osnabrück","leverkusen","darmstadt","heidelberg","solingen","herne","regensburg","neuss","paderborn","offenbach am main","fürth","ulm","würzburg","heilbronn","pforzheim","wolfsburg","bottrop","göttingen","reutlingen","koblenz","erlangen","bremerhaven","remscheid","bergisch gladbach","recklinghausen","trier","jena","moers","salzgitter","siegen","gütersloh","hildesheim");
	public static final Set<String> POLAND 				= Set.of("pologne","polonia","polen","poland","warsaw","wrocław","kraków","łódź","poznań","gdańsk","szczecin","bydgoszcz","lublin","białystok");
	public static final Set<String> FRANCE 				= Set.of("lille","bordeaux ","Strasbourg","montpellier","nantes","toulouse","lyon","marseille","paris","frankrijk","francia","frankreich","france");
	public static final Set<String> SPAIN 				= Set.of("spanje","spagna","espagne","españa","spanien","spain");
	public static final Set<String> ITALY 				= Set.of("prato","brescia","verona","bari","genova","genoa","napoli","naples","parma","trieste","venezia","venice","firenze","florence","palermo","milano","roma","milan","modena","taranto","messina","catania","bologna","turin","rome","italië","italie","italia","italien","italy");
	public static final Set<String> PORTUGAL 			= Set.of("portogallo","le portugal","portugal");
	public static final Set<String> AUSTRIA 			= Set.of("oostenrijk","l'autriche","österreich","austria");
	public static final Set<String> ROMANIA 			= Set.of("roemenië","roumanie","rumania","rumänien","romania");
	public static final Set<String> GREECE 				= Set.of("griekenland","grèce","grecia","griechenland","greece");
	public static final Set<String> UKRAINE 			= Set.of("oekraïne","ucrania","ukraine","ucraine","ucraina");
	public static final Set<String> SWITZERLAND 		= Set.of("zwitserland","svizzera","suisse","suiza","Schweiz","zwitzerland");
	public static final Set<String> CZECH_REPUBLIC 		= Set.of("tsjechische republiek","repubblica ceca","république tchèque","república checa","tschechische republik","czech republic");
	public static final Set<String> SWEDEN 				= Set.of("zweden","svezia","suède","suecia","schweden","sweden");
	public static final Set<String> HUNGARY 			= Set.of("esztergom","siófok","győr","miskolc","eger","szeged","pécs","debrecen","budapest","hongarije","ungheria","hongrie","hungría","ungarn","hungary");
	public static final Set<String> BULGARIA 			= Set.of("bulgarije","bulgarie","bulgarien","bulgaria");
	public static final Set<String> DENMARK 			= Set.of("denemarken","danimarca","danemark","dinamarca","dänemark","denmark");
	public static final Set<String> FINLAND 			= Set.of("finlandia","finlande","finlandia","finnland","finland");
	public static final Set<String> SLOVAKIA	 		= Set.of("slowakije","slovaquie","eslovaquia","slowakei","slovakia");
	public static final Set<String> CROATIA 			= Set.of("croazia","kroatie","croatie","croacia","kroatien","croatia");
	public static final Set<String> LITHUANIA 			= Set.of("litouwen","lituania","lituanie","lituania","litauen","lithuania");
	public static final Set<String> SLOVENIA 			= Set.of("slovenië","slovénie","eslovenia","slowenien","slovenia");
	public static final Set<String> LATVIA 				= Set.of("letland","lettonia","lettonie","letonia","lettland","latvia");
	public static final Set<String> ESTONIA 			= Set.of("estonie","estonia","estland","estonia");
	public static final Set<String> CYPRUS 				= Set.of("cipro","chypre","chipre","zypern","cyprus");
	public static final Set<String> MALTA 				= Set.of("malte","malta");
	public static final Set<String> NORWAY 				= Set.of("noorwegen","norvegia","norvège","noruega","norwegen","norway");
	public static final Set<String> LIECHTENSTEIN 		= Set.of("liechtenstein");
	public static final Set<String> TURKEY 				= Set.of("turkije","turchia","turquie","turquía","türkei","turkey");
	public static final Set<String> INDIA 				= Set.of("indië","inde","indien","india");
	public static final Set<String> PAKISTAN 			= Set.of("pakistán","pakistan");
	public static final Set<String> US 					= Set.of("verenigde staten","stati uniti","états-unis","vereinigte staaten","vereinigte staaten","united states", "usa");
	public static final Set<String> CANADA 				= Set.of("canadá","kanada","canada");
	
	/**
	* Refer to JobSpecifcationFilterExtractor interface for details
	*/
	public void extractFilters(String documentText, CandidateExtractedFiltersBuilder filterBuilder) {
			
		Set<COUNTRY> extractedCountries = new HashSet<>();
		
		UK.stream().filter(documentText::contains).findAny().ifPresent(l 					-> extractedCountries.add(COUNTRY.UK));
		NETHERLANDS.stream().filter(documentText::contains).findAny().ifPresent(l 			-> extractedCountries.add(COUNTRY.NETHERLANDS));
		BELGIUM.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.BELGIUM)); 
		LUXEMBOURG.stream().filter(documentText::contains).findAny().ifPresent(l 			-> extractedCountries.add(COUNTRY.LUXEMBOURG));
		REPUBLIC_OF_IRELAND.stream().filter(documentText::contains).findAny().ifPresent(l 	-> extractedCountries.add(COUNTRY.REPUBLIC_OF_IRELAND));
		NORTHERN_IRELAND.stream().filter(documentText::contains).findAny().ifPresent(l 		-> extractedCountries.add(COUNTRY.NORTHERN_IRELAND));
		GERMANY.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.GERMANY));
		POLAND.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.POLAND));
		FRANCE.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.FRANCE));
		SPAIN.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.SPAIN));
		ITALY.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.ITALY));
		PORTUGAL.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.PORTUGAL));
		AUSTRIA.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.AUSTRIA));
		ROMANIA.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.ROMANIA));
		GREECE.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.GREECE));
		UKRAINE.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.UKRAINE));
		SWITZERLAND.stream().filter(documentText::contains).findAny().ifPresent(l 			-> extractedCountries.add(COUNTRY.SWITZERLAND));
		CZECH_REPUBLIC.stream().filter(documentText::contains).findAny().ifPresent(l 		-> extractedCountries.add(COUNTRY.CZECH_REPUBLIC));
		SWEDEN.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.SWEDEN));
		HUNGARY.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.HUNGARY));
		BULGARIA.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.BULGARIA));
		DENMARK.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.DENMARK));
		FINLAND.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.FINLAND));
		SLOVAKIA.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.SLOVAKIA));
		CROATIA.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.CROATIA));
		LITHUANIA.stream().filter(documentText::contains).findAny().ifPresent(l 			-> extractedCountries.add(COUNTRY.LITHUANIA));
		SLOVENIA.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.SLOVENIA));
		LATVIA.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.LATVIA));
		ESTONIA.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.ESTONIA));
		CYPRUS.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.CYPRUS));
		MALTA.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.MALTA));
		NORWAY.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.NORWAY));
		LIECHTENSTEIN.stream().filter(documentText::contains).findAny().ifPresent(l 		-> extractedCountries.add(COUNTRY.LIECHTENSTEIN));
		TURKEY.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.TURKEY));
		INDIA.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.INDIA));
		PAKISTAN.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.PAKISTAN));
		US.stream().filter(documentText::contains).findAny().ifPresent(l 					-> extractedCountries.add(COUNTRY.US));
		CANADA.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.CANADA));
		
		filterBuilder.countries(extractedCountries);
			
	}
}