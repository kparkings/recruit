package com.arenella.recruit.candidates.extractors;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import com.arenella.recruit.candidates.beans.CandidateExtractedFilters.CandidateExtractedFiltersBuilder;

/**
* Extractor to determine countries to search in
* @author K Parkings
*/
@Component
public class CityExtractor implements JobSpecifcationFilterExtractor{
	
	public static final Set<String> CITIES_UK 					= Set.of("norfolk","nottingham","newcastle","oxford", " reading."," reading,", " reading ","london","birmingham","glasgow","manchester","sheffield","leeds","edinburgh","liverpool","bristol","cardiff","leicester","bradford");
	public static final Set<String> CITIES_NETHERLANDS 			= Set.of("zeist","maastricht","almelo"," leiden ", "alkmaar", "alphen", "amsterdam","rotterdam","the hague","den haag","Utrecht","Groningen","eindhoven","tilburg","almere","breda","nijmegen","enschede","haarlem", "amstelveen", "woerden", "amersfoort", "soest","arnhem","nieuwegein","zwolle","apeldoorn", "utrecht");
	public static final Set<String> CITIES_BELGIUM 				= Set.of("brussels","bruxelles","antwerp","antwerpen","ghent","gand"," gent","charleroi","courtrai","liège","liege","anderlecht","schaarbeek","bruges","bruge","brugge","namur","leuven","molenbeek"," mons "," mons","kortrijk","mechelen","meeuwen"); 
	public static final Set<String> CITIES_LUXEMBOURG 			= Set.of("dudelange","belvaux", "schifflange","walferdange","rodange","bettembourg","diekirch","pétange","soleuvre","ettelbruck","esch-sur-alzette");
	public static final Set<String> CITIES_REPUBLIC_OF_IRELAND 	= Set.of("navan","dublin","cork","limerick","galway","tallaght","Waterford","swords","drogheda","dundalk","bray","douglas","laoghaire","letterkenny","kildare");
	public static final Set<String> CITIES_NORTHERN_IRELAND 	= Set.of("belfast","armagh","ballymena","bangor","derry","carrickfergus","lisburn","newry","antrim","newtownabbey","londonderry","craigavon");
	public static final Set<String> CITIES_GERMANY 				= Set.of("berlin","hamburg","munich","cologne","frankfurt","stuttgart","düsseldorf","dusseldorf"," essen ","dortmund","dresden","bremen","nuremberg","münchen","munchen","köln","koln","leipzig","hannover","hanover","nürnberg","nurenberg","münster","munster"," bonn ","koblenz","erlangen","duisburg","bochum","wuppertal","bielefeld","mannheim","karlsruhe","augsburg","wiesbaden","mönchengladbach","gelsenkirchen","aachen","braunschweig"," kiel ","chemnitz"," halle ","saale","magdeburg","freiburg im breisgau","krefeld","mainz","lübeck","erfurt","oberhausen","rostock","kassel","hagen","potsdam","saarbrücken","hamm ","ludwigshafen am rhein","mülheim an der ruhr","oldenburg","osnabrück","leverkusen","darmstadt","heidelberg","solingen","herne ","regensburg","neuss ","paderborn","offenbach am main","fürth"," ulm ","würzburg","heilbronn","pforzheim","wolfsburg","bottrop","göttingen","reutlingen "," erlangen ","bremerhaven","remscheid","bergisch gladbach ","recklinghausen"," trier "," jena "," moers ","salzgitter"," siegen ","gütersloh","hildesheim");
	public static final Set<String> CITIES_POLAND 				= Set.of("warsaw","lodz","łódź","krakow","wroclaw","wrocław","poznan","poznań","gdansk","gdańsk","szczecin","bydgoszcz","lublin","katowice","kraków","bialystok","białystok","gdynia");
	public static final Set<String> CITIES_FRANCE 				= Set.of("paris","marseille","lyon","toulouse","nantes","marne","strasbourg","bordeaux","montpellier","rouen","lille");
	public static final Set<String> CITIES_SPAIN 				= Set.of("madrid", "barcelona","valencia","seville","Zaragoza","málaga","malaga","murcia","palma","las Palmas","alicante","bilbao", "córdoba","cordoba");
	public static final Set<String> CITIES_ITALY 				= Set.of("prato","brescia","bari","genova","genoa","napoli","naples","parma","trieste","venezia","venice","firenze","florence","palermo","milano"," roma","milan","modena","taranto","catania","bologna"," turin"," rome","verona","messina","cosenza");
	public static final Set<String> CITIES_PORTUGAL 			= Set.of("lisbon","porto","Amadora","braga","setúbal","setubal","coimbra","queluz","Funchal","cacém","cacem","vila nova","algueirão","algueirao","loures");
	public static final Set<String> CITIES_AUSTRIA 				= Set.of("vienna","wien","graz","linz","innsbruck","klagenfurt","villach","dornbirn","neustadt", "steyr","feldkirch","Bregenz","salzburg","wels","polten","pölten");
	public static final Set<String> CITIES_ROMANIA 				= Set.of("bucharest","iași","iaso","constanța", "cluj","napoca","brasov","timișoara","timisoara","craiova","galati","târgu","targu","oradea","ploieşti","ploiesti","arad");
	public static final Set<String> CITIES_GREECE 				= Set.of("athens","thessaloniki","pátrai","patrai","Piraeus","larissa","peristeri","heraklion","kallithe","acharnes","kalamaria","nikaia","glyfada");
	public static final Set<String> CITIES_UKRAINE 				= Set.of("kyiv","Kharkiv","odesa","dnipro","donetsk","lviv","Zaporizhzhya","kryvyi Rih","sebastopol","mykolaiv","luhansk","vinnytsia");
	public static final Set<String> CITIES_SWITZERLAND 			= Set.of("fribourg","st. gallen","zurich","geneva","basel","lausanne"," bern "," bern.","Winterthur","lucerne","saint gallen","lugano"," biel "," bienne ","thun ","bellinzona");
	public static final Set<String> CITIES_CZECH_REPUBLIC 		= Set.of("prague","brno","ostrava","pilsen","liberec","Olomouc","české budějovice","ceské budejovice","hradec králové","hradec kralove","ústí nad Labem", "usti nad Labem", "pardubice","havířov","havirov","zlín","zlin");
	public static final Set<String> CITIES_SWEDEN 				= Set.of("stockholm","gothenburg","malmo","Uppsala","linköping","linkoping","örebro","orebro","sollentuna","umeå","umea ","västerås","vasteras","jönköping","jonkoping","Helsingborg","norrköping","norrkoping");
	public static final Set<String> CITIES_HUNGARY 				= Set.of("Budapest","Debrecen","Szeged","miskolc","pécs","pecs","győr","gyor","Nyíregyháza","nyiregyhaza","kecskemét","kecskemet","székesfehérvár","szekesfehervar","Szombathely","Szolnok","tatabánya","tatabanya");
	public static final Set<String> CITIES_BULGARIA 			= Set.of("sofia","plovdiv","varna","burgas","stara zagora","rousse","pleven","sliven","pernik","dobrich","shumen","blagoevgrad");
	public static final Set<String> CITIES_DENMARK 				= Set.of("copenhagen","aarhus","odense","aalborg","frederiksberg","esbjerg","randers","kolding","horsens","vejle","hvidovre","klinteby","frihed");
	public static final Set<String> CITIES_FINLAND 				= Set.of("helsinki","espoo","tampere","oulu","turku","vantaa","jyväskylä","jyvaskyla","kuopio","lahti","pori ","kouvola","joensuu"	);
	public static final Set<String> CITIES_SLOVAKIA	 			= Set.of("bratislava","košice","Kosice","petržalka","petrzalka","nitra","prešov","presov","žilina","zilina","banská","banska","bystrica","trnava","trenčín","trencin","poprad","prievidza");
	public static final Set<String> CITIES_CROATIA 				= Set.of("zagreb","rijeka","osijek","zadar","sesvete","pula ","slavonski","Karlovac","varaždin","varazdin","šibenik","sibenik","velika","gorica ");
	public static final Set<String> CITIES_LITHUANIA 			= Set.of("vilnius","kaunas","klaipėda","kzaipeda","šiauliai","siauliai","panevezys","alytus","marijampolė","marijampole","mažeikiai","mazeikiai","jonava","utena ","kėdainiai","kedainiai","telšiai","Telsiai");
	public static final Set<String> CITIES_SLOVENIA 			= Set.of("ljubljana","Maribor","kranj","celje","koper","velenje","novo mesto","ptuj","trbovlje","kamnik","jesenice","domžale","domzale");
	public static final Set<String> CITIES_LATVIA 				= Set.of(" riga ","daugavpils","liepāja","liepaja","Jelgava","jūrmala","jurmala","ventspils","jēkabpils","jekabpils","rēzekne","rezekne"," ogre ","valmiera","salaspils","mārupe","marupe");
	public static final Set<String> CITIES_ESTONIA 				= Set.of("tallinn"," tartu ","narva","parnu","kohtla-Järve","kohtla-Jarve","viljandi","maardu","rakvere","kuressaare","sillamäe","sillamae","võru"," voru ");
	public static final Set<String> CITIES_CYPRUS 				= Set.of("nicosia","limassol","larnaca","stróvolos","strovolos","famagusta","káto lakatámeia","kato lakatameia","paphos","kyrenia","mórfou","morfou","kato polemidia","aradhippou","aglantzia");
	public static final Set<String> CITIES_MALTA 				= Set.of("san pawl il-baħar","san pawl il-banar","birkirkara"," mosta ","sliema","oormi","žabbar","zabbar","naxxar","san ġwann","san gwann","swieqi","saint julian\'s","saint julians","msida","birżebbuġa","birzebbuga");
	public static final Set<String> CITIES_NORWAY 				= Set.of("oslo","Trondheim","Stavanger","kristiansand","drammen","lillestrøm","lillestrom","sandnes","fredrikstad","sandefjord","asker","sarpsborg");
	public static final Set<String> CITIES_LIECHTENSTEIN 		= Set.of("schaan","triesen","vaduz","balzers","eschen","mauren","triesenberg","ruggell","gamprin","nendeln","schellenberg","planken");
	public static final Set<String> CITIES_TURKEY 				= Set.of("istanbul","ankara","bursa","imir","gaziantep","adana","diyarbakır","kayseri","konya","atalya","malatya","çankaya","cankaya");
	public static final Set<String> CITIES_INDIA 				= Set.of();
	public static final Set<String> CITIES_PAKISTAN 			= Set.of();
	public static final Set<String> CITIES_US 					= Set.of();
	public static final Set<String> CITIES_CANADA 				= Set.of();
	
	/**
	* Constructor 
	*/
	public CityExtractor() {
	
		
	}
	
	/**
	* Refer to JobSpecifcationFilterExtractor interface for details
	*/
	public void extractFilters(String documentText, CandidateExtractedFiltersBuilder filterBuilder) {
			
		//if ignorecase match return value with no padding and first char of each word in Uppercase
		
		Set<String> extractedCities = new HashSet<>();
		
		CITIES_UK.stream().filter(documentText::contains).forEach(extractedCities::add);
		CITIES_NETHERLANDS.stream().filter(documentText::contains).forEach(extractedCities::add);
		CITIES_BELGIUM.stream().filter(documentText::contains).forEach(extractedCities::add);
		CITIES_LUXEMBOURG.stream().filter(documentText::contains).forEach(extractedCities::add);
		CITIES_REPUBLIC_OF_IRELAND.stream().filter(documentText::contains).forEach(extractedCities::add);
		CITIES_NORTHERN_IRELAND.stream().filter(documentText::contains).forEach(extractedCities::add);
		CITIES_GERMANY.stream().filter(documentText::contains).forEach(extractedCities::add);
		CITIES_POLAND.stream().filter(documentText::contains).forEach(extractedCities::add);
		CITIES_FRANCE.stream().filter(documentText::contains).forEach(extractedCities::add);
		CITIES_SPAIN.stream().filter(documentText::contains).forEach(extractedCities::add);
		CITIES_ITALY.stream().filter(documentText::contains).forEach(extractedCities::add);
		CITIES_PORTUGAL.stream().filter(documentText::contains).forEach(extractedCities::add);
		CITIES_AUSTRIA.stream().filter(documentText::contains).forEach(extractedCities::add);
		CITIES_ROMANIA.stream().filter(documentText::contains).forEach(extractedCities::add);
		CITIES_GREECE.stream().filter(documentText::contains).forEach(extractedCities::add);
		CITIES_UKRAINE.stream().filter(documentText::contains).forEach(extractedCities::add);
		CITIES_SWITZERLAND.stream().filter(documentText::contains).forEach(extractedCities::add);
		CITIES_CZECH_REPUBLIC.stream().filter(documentText::contains).forEach(extractedCities::add);
		CITIES_SWEDEN.stream().filter(documentText::contains).forEach(extractedCities::add);
		CITIES_HUNGARY.stream().filter(documentText::contains).forEach(extractedCities::add);
		CITIES_BULGARIA.stream().filter(documentText::contains).forEach(extractedCities::add);
		CITIES_DENMARK.stream().filter(documentText::contains).forEach(extractedCities::add);
		CITIES_FINLAND.stream().filter(documentText::contains).forEach(extractedCities::add);
		CITIES_SLOVAKIA.stream().filter(documentText::contains).forEach(extractedCities::add);
		CITIES_CROATIA.stream().filter(documentText::contains).forEach(extractedCities::add);
		CITIES_LITHUANIA.stream().filter(documentText::contains).forEach(extractedCities::add);
		CITIES_SLOVENIA.stream().filter(documentText::contains).forEach(extractedCities::add);
		CITIES_LATVIA.stream().filter(documentText::contains).forEach(extractedCities::add);
		CITIES_ESTONIA.stream().filter(documentText::contains).forEach(extractedCities::add);
		CITIES_CYPRUS.stream().filter(documentText::contains).forEach(extractedCities::add);
		CITIES_MALTA.stream().filter(documentText::contains).forEach(extractedCities::add);
		CITIES_NORWAY.stream().filter(documentText::contains).forEach(extractedCities::add);
		CITIES_LIECHTENSTEIN.stream().filter(documentText::contains).forEach(extractedCities::add);
		CITIES_TURKEY.stream().filter(documentText::contains).forEach(extractedCities::add);
		CITIES_INDIA.stream().filter(documentText::contains).forEach(extractedCities::add);
		CITIES_PAKISTAN.stream().filter(documentText::contains).forEach(extractedCities::add);
		CITIES_US.stream().filter(documentText::contains).forEach(extractedCities::add);
		CITIES_CANADA.stream().filter(documentText::contains).forEach(extractedCities::add);
		
		/**
		* We can only set a location if there is a single result. Otherwise we have no way to 
		* determine which is the correct location
		*/
		if (extractedCities.isEmpty() || extractedCities.size() != 1) {
			return;
		}
		
		extractedCities.add("one two three four");
		
		String city = (String)extractedCities.toArray()[0];
		city = city.trim();
		
		Stream<String> words = Stream.of(city.split(" ")).map(this::applyFormat);
		
		city = String.join(" ", words.toList());	
		city = city.replaceAll("\\.", "");
		city = city.replaceAll(",", "");
		city = city.replaceAll(";", "");
		
		filterBuilder.city(city);
			
	}
	
	/**
	* Apply's formatting to city name in case name consists of multiple words
	* @param word - to format
	* @return formatted
	*/
	private String applyFormat(String word) {
		
		if(word.length() == 1) {
			return word.toUpperCase()+" ";
		}
		
		word =  word.substring(0,1).toUpperCase() + word.substring(1).toLowerCase()+ " ";
		
		if (word.substring(word.length()-1).equals(" ")) {
			word = word.substring(0, word.length()-1);
		}
		
		return word;
		
	}
	
}