package com.arenella.recruit.candidates.extractors;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import com.arenella.recruit.candidates.beans.CandidateExtractedFilters.CandidateExtractedFiltersBuilder;
import com.arenella.recruit.candidates.enums.COUNTRY;

/**
* Extractor to determine countries to search in
* @author K Parkings
*/
@Component
public class CountryExtractor implements JobSpecifcationFilterExtractor{
	
	protected Set<String> uk 					= DocumentFilterExtractionUtil.UK;
	protected Set<String> netherlands 			= Set.of("zeist","olanda","pays-bas","países bajos","niederlande","brabant","zuid-holland","schiphol","netherlands","nederland ","nederland.","randstad","overijssel", "gelderland","limburg", "flevoland","zsm","vaardigheden","vereiste","softwareontwikkeling");
	protected Set<String> belgium 				= Set.of("vlaanderen","belgië","belgio","belgique","bélgica","belgien","belgium", "flemish", "flams", "wallon"); 
	protected Set<String> luxembourg 			= Set.of("lussemburgo","luxemburgo","luxembourg","luxemburg","luxembourgish");
	protected Set<String> republicOfIreland 	= Set.of("irlande","irlanda","irland","ireland" );
	protected Set<String> northernIreland 		= Set.of("county down","northern ireland");
	protected Set<String> germany 				= Set.of("duitsland","germania","allemagne","alemania","deutschland","germany");
	protected Set<String> poland 				= Set.of("Praca", "pln", "pologne","polonia","polen","poland");
	protected Set<String> france 				= Set.of("frankrijk","francia","frankreich","france");
	protected Set<String> spain 				= Set.of("spanje","spagna","espagne","españa","spanien","spain");
	protected Set<String> italy 				= Set.of("italië","italie","italia","italien","italy");
	protected Set<String> portugal 				= Set.of("portogallo","le portugal","portugal", "lisbon");
	protected Set<String> austria 				= Set.of("oostenrijk","l'autriche","österreich","austria");
	protected Set<String> romania 				= Set.of("roemenië","roumanie","rumania","rumänien","r𝗼𝗺𝗮𝗻𝗶𝗮","romania","bucharest");
	protected Set<String> greece 				= Set.of("griekenland","grèce","grecia","griechenland","greece");
	protected Set<String> ukraine 				= Set.of("oekraïne","ucrania","ukraine","ucraine","ucraina");
	protected Set<String> switzerland 			= Set.of("switzerland","switzerland ","zwitserland","svizzera","suisse","suiza","Schweiz","zwitzerland");
	protected Set<String> czechRepublic 		= Set.of("tsjechische republiek","repubblica ceca","république tchèque","república checa","tschechische republik","czech republic");
	protected Set<String> sweden 				= Set.of("stockholm","zweden","svezia","suède","suecia","schweden","sweden");
	protected Set<String> hungary 				= Set.of("esztergom","siófok","győr","miskolc","eger","szeged","pécs","debrecen","budapest","hongarije","ungheria","hongrie","hungría","ungarn","hungary");
	protected Set<String> bulgaria 				= Set.of("bulgarije","bulgarie","bulgarien","bulgaria");
	protected Set<String> denmark 				= Set.of("denemarken","danimarca","danemark","dinamarca","dänemark","denmark");
	protected Set<String> finland 				= Set.of("finlande","finlandia","finnland","finland");
	protected Set<String> slovakia	 			= Set.of("slowakije","slovaquie","eslovaquia","slowakei","slovakia");
	protected Set<String> croatia 				= Set.of("croazia","kroatie","croatie","croacia","kroatien","croatia");
	protected Set<String> lithuania 			= Set.of("litouwen","lituanie","lituania","litauen","lithuania");
	protected Set<String> slovenia 				= Set.of("slovenië","slovénie","eslovenia","slowenien","slovenia");
	protected Set<String> latvia 				= Set.of("letland","lettonia","lettonie","letonia","lettland","latvia");
	protected Set<String> estonia 				= Set.of("estonie","estland","estonia");
	protected Set<String> cyprus 				= Set.of("cipro","chypre","chipre","zypern","cyprus");
	protected Set<String> malta 				= Set.of("malte","malta");
	protected Set<String> norway 				= Set.of("noorwegen","norvegia","norvège","noruega","norwegen","norway");
	protected Set<String> liechtenstein 		= Set.of("liechtenstein");
	protected Set<String> turkey 				= Set.of("turkije","turchia","turquie","turquía","türkei","turkey");
	protected Set<String> india 				= Set.of("indië"," india ");
	protected Set<String> pakistan 				= Set.of("pakistán","pakistan");
	protected Set<String> us 					= Set.of("stati uniti","états-unis","vereinigte staaten","united states", " usa ");
	protected Set<String> canada 				= Set.of("canadá","kanada","canada");
	
	
	public static final Set<String> CITIES_UK 					= Set.of("swindon"," bath ","lincoln","northampton ","warwick","sunderland","nottingham"," reading."," reading,", " reading ","london","birmingham","glasgow","manchester","sheffield","leeds","edinburgh","liverpool","bristol","cardiff","leicester","bradford");
	public static final Set<String> CITIES_NETHERLANDS 			= Set.of("hilversum","hengelo","maastricht","almelo","alkmaar", "alphen", "amsterdam","rotterdam","the hague","den haag","groningen","eindhoven","tilburg","almere","breda","nijmegen","enschede","haarlem", "amstelveen", "woerden", "amersfoort", "soest","arnhem","nieuwegein","zwolle","apeldoorn", "utrecht");
	public static final Set<String> CITIES_BELGIUM 				= Set.of("brussels","bruxelles","antwerp","antwerpen","ghent","gand"," gent","charleroi","courtrai","liège","liege","anderlecht","schaarbeek","bruges","bruge","brugge","namur","leuven","molenbeek"," mons "," mons","kortrijk","mechelen","meeuwen"); 
	public static final Set<String> CITIES_LUXEMBOURG 			= Set.of("dudelange","belvaux", "schifflange","walferdange","rodange","bettembourg","diekirch","pétange","soleuvre","ettelbruck","esch-sur-alzette");
	public static final Set<String> CITIES_REPUBLIC_OF_IRELAND 	= Set.of("navan","dublin","cork","limerick","galway","tallaght","Waterford","swords","drogheda","dundalk","bray","douglas","laoghaire","letterkenny","kildare");
	public static final Set<String> CITIES_NORTHERN_IRELAND 	= Set.of("belfast","armagh","ballymena","bangor","derry","carrickfergus","lisburn","newry","antrim","newtownabbey","londonderry","craigavon");
	public static final Set<String> CITIES_GERMANY 				= Set.of("berlin","hamburg","munich","cologne","frankfurt","stuttgart","düsseldorf","dusseldorf"," essen ","dortmund","dresden","bremen","nuremberg","münchen","munchen","köln","koln","leipzig","hannover","hanover","nürnberg","nurenberg","münster","munster"," bonn ","koblenz","erlangen","duisburg","bochum","wuppertal","bielefeld","mannheim","karlsruhe","augsburg","wiesbaden","mönchengladbach","gelsenkirchen","aachen","braunschweig"," kiel ","chemnitz"," halle ","saale","magdeburg","freiburg im breisgau","krefeld","mainz","lübeck","erfurt","oberhausen","rostock","kassel"," hagen","potsdam","saarbrücken","hamm ","ludwigshafen am rhein","mülheim an der ruhr","oldenburg","osnabrück","leverkusen","darmstadt","heidelberg","solingen","herne ","regensburg","neuss ","paderborn","offenbach am main","fürth"," ulm ","würzburg","heilbronn","pforzheim","wolfsburg","bottrop","göttingen","reutlingen "," erlangen ","bremerhaven","remscheid","bergisch gladbach ","recklinghausen"," trier "," jena "," moers ","salzgitter"," siegen ","gütersloh","hildesheim");
	public static final Set<String> CITIES_POLAND 				= Set.of("warsaw","lodz","łódź","krakow","wroclaw","wrocław","poznan","poznań","gdansk","gdańsk","szczecin","bydgoszcz","lublin","katowice","kraków","bialystok","białystok","gdynia");
	public static final Set<String> CITIES_FRANCE 				= Set.of("île-de-france","paris","marseille","lyon","toulouse","nantes ","nantes.","marne","strasbourg","bordeaux","montpellier","rouen","lille");
	public static final Set<String> CITIES_SPAIN 				= Set.of("madrid", "barcelona","valencia","seville","Zaragoza","málaga","malaga","murcia","palma","las Palmas","alicante","bilbao", "córdoba","cordoba");
	public static final Set<String> CITIES_ITALY 				= Set.of("prato","brescia","bari","genova","genoa","napoli","naples","parma","trieste","venezia","venice","firenze","florence","palermo","milano"," roma."," roma,"," roma ","milan","modena","taranto","catania","bologna"," turin"," rome","verona","messina","cosenza");
	public static final Set<String> CITIES_PORTUGAL 			= Set.of("lisbon","porto","Amadora","braga","setúbal","setubal","coimbra","queluz","Funchal","cacém","cacem","vila nova","algueirão","algueirao","loures");
	public static final Set<String> CITIES_AUSTRIA 				= Set.of("vienna","wien","graz","linz","innsbruck","klagenfurt","villach","dornbirn","neustadt", "steyr","feldkirch","Bregenz","salzburg","wels","polten","pölten");
	public static final Set<String> CITIES_ROMANIA 				= Set.of("bucharest","iași","iaso","constanța", "cluj","cluj-napoca","brasov","timișoara","timisoara","craiova","galati","târgu","targu","oradea","ploieşti","ploiesti","arad");
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
	public CountryExtractor() {
		uk 					= Stream.of(uk, 				CITIES_UK).flatMap(Collection::stream).collect(Collectors.toSet());
		netherlands			= Stream.of(netherlands, 		CITIES_NETHERLANDS).flatMap(Collection::stream).collect(Collectors.toSet());
		belgium				= Stream.of(belgium, 			CITIES_BELGIUM).flatMap(Collection::stream).collect(Collectors.toSet()); 
		luxembourg			= Stream.of(luxembourg, 		CITIES_LUXEMBOURG).flatMap(Collection::stream).collect(Collectors.toSet());
		republicOfIreland	= Stream.of(republicOfIreland, 	CITIES_REPUBLIC_OF_IRELAND).flatMap(Collection::stream).collect(Collectors.toSet());
		northernIreland		= Stream.of(northernIreland, 	CITIES_NORTHERN_IRELAND).flatMap(Collection::stream).collect(Collectors.toSet());
		germany				= Stream.of(germany, 			CITIES_GERMANY).flatMap(Collection::stream).collect(Collectors.toSet());
		poland				= Stream.of(poland, 			CITIES_POLAND).flatMap(Collection::stream).collect(Collectors.toSet());
		france				= Stream.of(france, 			CITIES_FRANCE).flatMap(Collection::stream).collect(Collectors.toSet());
		spain				= Stream.of(spain, 				CITIES_SPAIN).flatMap(Collection::stream).collect(Collectors.toSet());
		italy				= Stream.of(italy, 				CITIES_ITALY).flatMap(Collection::stream).collect(Collectors.toSet());
		portugal			= Stream.of(portugal, 			CITIES_PORTUGAL).flatMap(Collection::stream).collect(Collectors.toSet());
		austria				= Stream.of(austria, 			CITIES_AUSTRIA).flatMap(Collection::stream).collect(Collectors.toSet());
		romania				= Stream.of(romania, 			CITIES_ROMANIA).flatMap(Collection::stream).collect(Collectors.toSet());
		greece				= Stream.of(greece, 			CITIES_GREECE).flatMap(Collection::stream).collect(Collectors.toSet());
		ukraine				= Stream.of(ukraine,	 		CITIES_UKRAINE).flatMap(Collection::stream).collect(Collectors.toSet());
		switzerland			= Stream.of(switzerland, 		CITIES_SWITZERLAND).flatMap(Collection::stream).collect(Collectors.toSet());
		czechRepublic		= Stream.of(czechRepublic, 		CITIES_CZECH_REPUBLIC).flatMap(Collection::stream).collect(Collectors.toSet());
		sweden				= Stream.of(sweden, 			CITIES_SWEDEN).flatMap(Collection::stream).collect(Collectors.toSet());
		hungary				= Stream.of(hungary, 			CITIES_HUNGARY).flatMap(Collection::stream).collect(Collectors.toSet());
		bulgaria			= Stream.of(bulgaria, 			CITIES_BULGARIA).flatMap(Collection::stream).collect(Collectors.toSet());
		denmark				= Stream.of(denmark, 			CITIES_DENMARK).flatMap(Collection::stream).collect(Collectors.toSet());
		finland				= Stream.of(finland, 			CITIES_FINLAND).flatMap(Collection::stream).collect(Collectors.toSet());
		slovakia			= Stream.of(slovakia, 			CITIES_SLOVAKIA).flatMap(Collection::stream).collect(Collectors.toSet());
		croatia				= Stream.of(croatia, 			CITIES_CROATIA).flatMap(Collection::stream).collect(Collectors.toSet());
		lithuania			= Stream.of(lithuania, 			CITIES_LITHUANIA).flatMap(Collection::stream).collect(Collectors.toSet());
		slovenia			= Stream.of(slovenia, 			CITIES_SLOVENIA).flatMap(Collection::stream).collect(Collectors.toSet());
		latvia				= Stream.of(latvia, 			CITIES_LATVIA).flatMap(Collection::stream).collect(Collectors.toSet());
		estonia				= Stream.of(estonia, 			CITIES_ESTONIA).flatMap(Collection::stream).collect(Collectors.toSet());
		cyprus				= Stream.of(cyprus, 			CITIES_CYPRUS).flatMap(Collection::stream).collect(Collectors.toSet());
		malta				= Stream.of(malta, 				CITIES_MALTA).flatMap(Collection::stream).collect(Collectors.toSet());
		norway				= Stream.of(norway, 			CITIES_NORWAY).flatMap(Collection::stream).collect(Collectors.toSet());
		liechtenstein		= Stream.of(liechtenstein, 		CITIES_LIECHTENSTEIN).flatMap(Collection::stream).collect(Collectors.toSet());
		pakistan			= Stream.of(pakistan, 			CITIES_PAKISTAN).flatMap(Collection::stream).collect(Collectors.toSet());
		us					= Stream.of(us, 				CITIES_US).flatMap(Collection::stream).collect(Collectors.toSet());
		canada				= Stream.of(canada, 			CITIES_CANADA).flatMap(Collection::stream).collect(Collectors.toSet());
		
	}
	
	/**
	* Refer to JobSpecifcationFilterExtractor interface for details
	*/
	public void extractFilters(String documentText, CandidateExtractedFiltersBuilder filterBuilder) {
			
		Set<COUNTRY> extractedCountries = new HashSet<>();
		
		uk.stream().filter(documentText::contains).findAny().ifPresent(l 					-> extractedCountries.add(COUNTRY.UK));
		netherlands.stream().filter(documentText::contains).findAny().ifPresent(l 			-> extractedCountries.add(COUNTRY.NETHERLANDS));
		belgium.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.BELGIUM)); 
		luxembourg.stream().filter(documentText::contains).findAny().ifPresent(l 			-> extractedCountries.add(COUNTRY.LUXEMBOURG));
		republicOfIreland.stream().filter(documentText::contains).findAny().ifPresent(l 	-> extractedCountries.add(COUNTRY.REPUBLIC_OF_IRELAND));
		northernIreland.stream().filter(documentText::contains).findAny().ifPresent(l 		-> extractedCountries.add(COUNTRY.NORTHERN_IRELAND));
		germany.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.GERMANY));
		poland.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.POLAND));
		france.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.FRANCE));
		spain.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.SPAIN));
		italy.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.ITALY));
		portugal.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.PORTUGAL));
		austria.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.AUSTRIA));
		romania.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.ROMANIA));
		greece.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.GREECE));
		ukraine.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.UKRAINE));
		switzerland.stream().filter(documentText::contains).findAny().ifPresent(l 			-> extractedCountries.add(COUNTRY.SWITZERLAND));
		czechRepublic.stream().filter(documentText::contains).findAny().ifPresent(l 		-> extractedCountries.add(COUNTRY.CZECH_REPUBLIC));
		sweden.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.SWEDEN));
		hungary.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.HUNGARY));
		bulgaria.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.BULGARIA));
		denmark.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.DENMARK));
		finland.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.FINLAND));
		slovakia.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.SLOVAKIA));
		croatia.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.CROATIA));
		lithuania.stream().filter(documentText::contains).findAny().ifPresent(l 			-> extractedCountries.add(COUNTRY.LITHUANIA));
		slovenia.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.SLOVENIA));
		latvia.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.LATVIA));
		estonia.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.ESTONIA));
		cyprus.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.CYPRUS));
		malta.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.MALTA));
		norway.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.NORWAY));
		liechtenstein.stream().filter(documentText::contains).findAny().ifPresent(l 		-> extractedCountries.add(COUNTRY.LIECHTENSTEIN));
		turkey.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.TURKEY));
		india.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.INDIA));
		pakistan.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.PAKISTAN));
		us.stream().filter(documentText::contains).findAny().ifPresent(l 					-> extractedCountries.add(COUNTRY.US));
		canada.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.CANADA));
		
		filterBuilder.countries(extractedCountries);
			
	}
}