package com.arenella.recruit.candidates.imports;

import java.io.File;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.enums.FREELANCE;
import com.arenella.recruit.candidates.enums.FUNCTION;
import com.arenella.recruit.candidates.enums.PERM;
import com.arenella.recruit.candidates.services.CandidateService;
import com.arenella.recruit.candudates.beans.Candidate;
import com.arenella.recruit.candudates.beans.Language;
import com.arenella.recruit.candudates.beans.Language.LANGUAGE;
import com.arenella.recruit.candudates.beans.Language.LEVEL;

/**
* Bean checks if import.csv is present. If so it replaces the Candidates
* in the 
* @author K Parkings
*/
@Component
public class CandidateCSVImport {

	@Autowired
	private CandidateService candidateService;
	
	@Value("${csv.import.active}")
	private boolean performCandidateImport;
	
	@PostConstruct
	public void init() throws Exception{
		
		if (!performCandidateImport) {
			return;
		}
			
		performImport();
	}
	
	//Issue this is not loaded before the constructor and therefore is null
	//@Value("csv.import.active")
	//private Boolean performCandidateImport;
	
	/**
	* Constructor.
	*/
	public CandidateCSVImport() {
		
		//try{
			
		//	//if (!performCandidateImport) {
		//	//	return;
		//	//}
			
		//	this.performImport();
		
		//}catch(Exception e) {
		//	e.printStackTrace();
		//}
	}
	
	/**
	* Deletes Candidates and Adds the new Candidates from the CSV file
	* @throws Exception
	*/
	private void performImport() throws Exception{
		
		File 			candidateCSVFile 	= 	ResourceUtils.getFile("classpath:Candidates-wordformat.xlsx");		
		XSSFWorkbook	workbook			=	new  XSSFWorkbook(candidateCSVFile);
		XSSFSheet 		sheet 				= 	workbook.getSheetAt(0);
		
		int totalCandidateRows = (sheet.getPhysicalNumberOfRows());
		
		AtomicInteger currentRow = new AtomicInteger(5);
		
		while (currentRow.get() < totalCandidateRows) {
			
			XSSFRow row = sheet.getRow(currentRow.getAndIncrement());
	
			XSSFCell candidateId 		= row.getCell(0);
			XSSFCell country 			= row.getCell(1);
			XSSFCell city 				= row.getCell(2);
			XSSFCell freelance 			= row.getCell(3);
			XSSFCell perm 				= row.getCell(4);
			XSSFCell dutch 				= row.getCell(5);
			XSSFCell english 			= row.getCell(6);
			XSSFCell french 			= row.getCell(7);
			XSSFCell yearsOfExperience	= row.getCell(8);
			XSSFCell role 				= row.getCell(9);
			XSSFCell skills 			= row.getCell(10);
			
			try {
			
			Candidate candidate = Candidate
					.builder()
						.available(true)
						.candidateId(candidateId.getStringCellValue().replace("C", ""))
						.city(city.getStringCellValue())
						.country(getCountry(country))
						.email("unknown")
						.firstname("unknown")
						.freelance(getFreelance(freelance))
						.function(getFunction(role))
						.roleSought(role.getStringCellValue())
						.languages(getLanguages(dutch, english, french))
						.lastAvailabilityCheck(LocalDate.now())
						.perm(getPerm(perm))
						.registerd(LocalDate.now())
						.skills(getSkills(skills, currentRow))
						.surname("unknown")
						.yearsExperience(getYearsExperience(yearsOfExperience, currentRow))
						
					.build();
			
					candidateService.persistCandidate(candidate);
					
			}catch(Exception e) {
				e.printStackTrace();
				System.out.println("Failed for Row: " + currentRow.get());
			}
		}
		
		workbook.close();
	}
	
	public FUNCTION getFunction(XSSFCell function) {
		
		switch(function.getStringCellValue()) {
			case "Senior Java developer":
			case "Java / Javascript":
			case "Senior Java Developer":
			case "Senior Java, Scala, C#, C++, C Developer":
			case "Junior Java Developer":
			case "Full stack Java Developer":
			case "Senior Full stack Java Developer":
			case "Full stack Java DevOps Developer":
			case "Full stack Java / Dev ops":
			case "Full stack Java (junior)":
			case "Java / php Developer":
			case "Graduate Java Developer":
			case "Java developer full stack":
			case "Ful stack Java developer":
			case "Webmethods \\ Java developer":
			case "Java Developer": {
				return FUNCTION.JAVA_DEV;
			}
			case "C# .Net Developer":
			case "Embedded C, C++, C#  Senior Developer":
			case "Senior .Net / Angular developer":
			case ".Net Developer Full stack":
			case "C# Developer / Python Developer":
			case "Senior Full stack C# .Net Developer":
			case "C# .Net / Xamarin Mobile App devloper":
			case "C# / Java Developer":
			case "Jjunior C# .Net developer":
			case "Senior Freelance .Net Developer":
			case "Senior C# full stack developer":
			case "Junior/Intern .Net Developer":
			case "Lead / Senior .NET developer":
			case ".Net Developer":
			case "C# Developer": {
				return FUNCTION.CSHARP_DEV;
			}
			case "Web designer":
			case "Full stack web developer \\ Tester":
			case "Front end web design":
			case "Intern Web developer":
			case "Web developer":
			case "Junior cloud web developer":
			case "Front end Developer / Tester":
			case "Junior front end developer":
			case "Front end Architect / engineer":
			case "Front end Developer":
			case "Graduate FE developer":
			case "Junior web developper":
			case "Graduate Web developer":
			case "Student web developer":
			case "Full stack web developer / DB Developer":
			case "Junior full stack web developer":
			case "Web Developer": {
				return FUNCTION.WEB_DEV;
			}
			case "Developer":
			case "Senior Developer":
			case "Data Analyst / Python programmer":
			case "Perl Developer / Scrum Master / Product owner":
			case "Python Developer / Tester":
			case "Analyst Developer":
			case "Senior IT Consultant":
			case "Senior Application Development Analyst":
			case "Junior developer":
			case "Software Developer / Systems manager":
			case "Sofwtware developer":
			case "Remote Senior software develoer ( fully remote only)":
			case "Python Developer":
			case "Ful stack software engineer":
			case "Tech Analyst Developer":
			case "Senior Full stack Developer":
			case "Software Engineer":
			case "Full stack developer":
			case "Analyst Developer + Project Manager":
			case "Software Developer":
			case "Sofware engineer/ hardware engineer/ IT Manager":
			case "Senior fullstack developer":
			case "Senior Software Developer":
			case "Full stack Software Engineer":
			case "Senior Software Developer / scrum master":
			case "Junior full stack developer":
			case "Developer / Maintenance / Tester":
			case "IOS developer":
			case "Mobile / backend software engineer":
			case "Mobile App devloper":
			case "Software developer": {
				//Add FUNCTION SOFTWARE_DEVELOPER
				
				return FUNCTION.SOFTWARE_DEVELOPER;
			}
			case "QA Tester":
			case "Senior software test automation engineer":
			case "Test analyst \\ Data software manager":
			case "QA Analyst":
			case "Test analyst":
			case "Junior functional tester":
			case "Senior Tester":
			case "Technoloty Analyst ( Tester / Developer )":
			case "Manual test analyst":
			case "Test Engineer":
			case "Test Engineer / QA Analyst":
			case "Automation Test engineer":
			case "Test automation engineerS":
			case "Senior Test Manger / Analyst":
			case "Senior Test Analyst":
			case "Software Tester / QA Tester":
			case "QA Automation Engineer":
			case "Test Lead":
			case "QA Analyst / Tester":
			case "Test automation engineer":
			case "Senior QA Engineer":
			case "Tester":
			case "Software Tester":
			case "QA Engineer":{
				return FUNCTION.TESTER;
			}
			case "Business Analyst / Product Owner":
			case "BA \\ Project Manager":
			case "Business Analyst / Project Manager":
			case "Business Analyst / Data Engineer":
			case "Senior Business Analyst / Product owner":
			case "Business Analyst":{
				
				return FUNCTION.BA;
			}
			case "Cloud Support":
			case "Senior Field Service Engineer":
			case "Senior Support Engineer":
			case "Support Analyst":
			case "Senior IT Support":
			case "Support Engineer":
			case "Junior support / helpdesk":
			case "Application support / Maintenance":
			case "IT Support / Administration":
			case "Service Desk":
			case "Tech support / Java developer":
			case "IT Support / Junior full stack developer":
			case "IT Support":{
				return FUNCTION.SUPPORT;
			}
			case "IT Infastructure consultant":
			case "DevOps":
			case "Cloud Devops":
			case "Devops Engineer":
			case "System / Network engineer":
			case "System Administrator / Desktop support":
			case "IT Security / Network consultant":
			case "IT System administrator":
			case "Linux administrator –cyber security":
			case "Senior Windows administrator":
			case "Certified Cloud Engineer":
			case "Network Engineer":
			case "System administrator":
			case "Azure administrator / Operations Engineer":{
				//Need Network admin function
				return FUNCTION.NETWORK_ADMINISTRATOR;
				
			}
			case "Senior UX Designer":
			case "UI/UX Designer":
			case "UI/UX Web Developer":
			case "Senior UI/UX Designer":
			case "Junior UI/UX Designer":
			case "UX\\UI":{
				return FUNCTION.UI_UX;
			}
			case "IT Project Manager":
			case "Team lead, service desk manager, network management":
			case "Senior Project manager":
			case "SAP project manager":
			case "Project Manager / Scrum master":
			case "IT Project Manager / Scrum master":
			case "IT project manager":
			case "IT Manager":
			case "IT Manager ":
			case "Service delivery manager":
			case "IT project consultant":
			case "IT Manager Helpdesk / Onsite support":
			case "Project Manager":
			case "IT Manager / Analyst":{
				return FUNCTION.PROJECT_MANAGER;
			}
			case "Software architect / Analyst developer":
			case "AWS solutions architect":
			case "Service Managment Architect":
			case "Senior Solutions Architect / Enterprise architect":
			case "Solutions architect / IT project manager":{
				return FUNCTION.ARCHITECT;
			}
			case "Big Data Engineer / BI":
			case "Data Analyst":
			case "Data Scientist":
			case "Data Scientish":{
				//Need data scientist FUNCTION
				return FUNCTION.DATA_SCIENTIST;
			}
			case "Scrum master / Agile Coach":
			case "Scrum Master":{
				//Need Scrum Master FUNCTION
				return FUNCTION.DATA_SCIENTIST;
			}
			case "IT Security Analyst":{
				return FUNCTION.IT_SECURITY;
			}
			case "Software Developer in Test":
			case "Software Engineer in Test":{
				return FUNCTION.SOFTWARE_DEV_IN_TEST;
			}
			
			default:{
				System.out.println("FAILED FOR FUNCTION: " + function.getStringCellValue());
				return null;
			}
		}
		
	}
	
	private Set<String> getSkills(XSSFCell skills, AtomicInteger currentRow) {

		try {
			String rawString = skills.getStringCellValue();
			return Stream.of(rawString.split(",")).collect(Collectors.toSet());
		}catch(Exception e) {
			System.out.println("No Skills for row: " + currentRow.get());
		}
		return new HashSet<>();
		
		
	}
	
	private int getYearsExperience(XSSFCell yearsOfExperience, AtomicInteger currentRow) {
		
		try {
		
		double val = yearsOfExperience.getNumericCellValue();
		
		return Double.valueOf(val).intValue();
		
		}catch(IllegalStateException e) {
			System.out.println("No years of exp for row: " + currentRow.get());
			return 0;
		}
		
	};
	
	private Set<Language> getLanguages(XSSFCell dutch, XSSFCell english, XSSFCell french) {
		
		Set<Language> languages = new HashSet<>();
		
		Optional<Language> 	dutchLang 		= getLanguage(dutch, 	LANGUAGE.DUTCH);
		Optional<Language> 	englishLang 	= getLanguage(english, 	LANGUAGE.ENGLISH);
		Optional<Language> 	frenchLang 		= getLanguage(french, 	LANGUAGE.FRENCH);
		
		if (dutchLang.isPresent()) {
			languages.add(dutchLang.get());
		}
		
		if (englishLang.isPresent()) {
			languages.add(englishLang.get());
		}
		
		if (frenchLang.isPresent()) {
			languages.add(frenchLang.get());
		}
		
		return languages;
	}
	
	public Optional<Language> getLanguage(XSSFCell lang, LANGUAGE language) {
		
		switch(lang.getStringCellValue()) {
			case "X":{
				return Optional.of(Language.builder().language(language).level(LEVEL.PROFICIENT).build());
			}
			case "- ":
			case "":
			case "-":{
				return Optional.empty();
			} 
			case "?": {
				return Optional.of(Language.builder().language(language).level(LEVEL.UNKNOWN).build());
			}
			case "X (medior)":
			case "X (basic)" :{
				return Optional.of(Language.builder().language(language).level(LEVEL.BASIC).build());
			}
			default:{
				System.out.println("Failed: lang:" + lang.getRichStringCellValue());
				return Optional.empty();
			}

		}
		
	}
	
	private PERM getPerm(XSSFCell perm) {
		
		switch(perm.getStringCellValue()) {
			case "x":
			case "X": {
				return PERM.TRUE;
			}
			case "-": {
				return PERM.FALSE;
			}
			case "":
			case "?": {
				return PERM.UNKNOWN;
			}
			default:{
				System.out.println("Failed: Perm:" + perm.getRichStringCellValue());
				return PERM.UNKNOWN;
			}
		}
		
	}
	
	private FREELANCE getFreelance(XSSFCell freelance) {
		
		switch(freelance.getStringCellValue()) {
			case "X": {
				return FREELANCE.TRUE;
			}
			case "": {
				return FREELANCE.FALSE;
			}
			case "? (2+ jaar)":
			case "(2+ jaar)":
			case "-":
			case "?": {
				return FREELANCE.UNKNOWN;
			}
			default:{
				System.out.println("Failed: Freelance:" + freelance.getRichStringCellValue());
				return FREELANCE.UNKNOWN;
			}
		}
		
	}
	
	private COUNTRY getCountry(XSSFCell countryCell) {
		
		switch (countryCell.getStringCellValue()) {
			case "Netherlands":{
				return COUNTRY.NETHERLANDS;
			}
			case "England":
			case "Scotland":
			case "UK":{
				return COUNTRY.UK;
			}
			case "Belgium":{
				return COUNTRY.BELGIUM;
			}
			case "Belgium / Europe":
			case "Europe": {
				return COUNTRY.EUROPE;
			}
			default:{
				System.out.println("Unknown Country: " + (countryCell.getStringCellValue()));
				return COUNTRY.NETHERLANDS;
			}
		}
		
	}
	
	//TODO: freelance needs to have unknown option
	//TODO: perm needs to have unknown option
	
}
