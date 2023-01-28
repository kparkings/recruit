package com.arenella.recruit.candidates.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;

import com.arenella.recruit.candidates.beans.CandidateExtractedFilters;
import com.arenella.recruit.candidates.beans.CandidateExtractedFilters.CandidateExtractedFiltersBuilder;
import com.arenella.recruit.curriculum.enums.FileType;

/**
* Factory for extracting filter information out of Files
* @author K Parkings
*/
public class DocumentFilterExtractionFactory {

	/**
	* Returns an implementation of the Extractor suitable for
	* the FileType of the Curriculum
	* @param fileType - FileType of the Curriculum
	* @return Extractor
	*/
	public static DocumentFilterExtractor getInstance(FileType fileType) {
		
		switch(fileType) {
			case pdf:{
				return new PDFExtractor();
			}
			case doc:
			case docx:{
				return new WordExtractor(); 
			}
		}
		
		return null;
	}
	
	/**
	* Defines the behavior of a CurriculumDetailsExtractor. That is 
	* an extractor that can extract details from a Curriculum file
	* @author K Parkings
	*/
	public static interface DocumentFilterExtractor {
		
		public CandidateExtractedFilters extract(byte[] fileBytes) throws Exception; 
		
	}

	/**
	* PDF specific implementation of a DocumentFilterExtractionExtractor
	* @author K Parkings
	*/
	public static class PDFExtractor implements DocumentFilterExtractor{

		/**
		* Refer to the DocumentFilterExtractionExtractor interface for details
		*/
		@Override
		public CandidateExtractedFilters extract(byte[] fileBytes) throws Exception{
			
			PDDocument 			doc 			= PDDocument.load(new ByteArrayInputStream(fileBytes));
			PDFTextStripper 	pdfStripper 	= new PDFTextStripper();
			String 				text 			= pdfStripper.getText(doc);
			
			text = text.toLowerCase();
			doc.close();
			
			CandidateExtractedFiltersBuilder filterBuilder = CandidateExtractedFilters.builder();
			
			extractFilters(text, filterBuilder);
			
			return filterBuilder.build();
			
		}
		
	}
	
	/**
	* Word (doc/docx) specific implementation of a DocumentFilterExtractionExtractor
	* @author K Parkings
	*/
	public static class WordExtractor implements DocumentFilterExtractor{

		/**
		* Refer to the DocumentFilterExtractionExtractor interface for details
		*/
		@Override
		public CandidateExtractedFilters extract(byte[] fileBytes) throws Exception {
			
			InputStream 		is 				= new ByteArrayInputStream(fileBytes);
			OPCPackage 			docPackage 		= OPCPackage.open(is);
			XWPFWordExtractor 	extractor 		= new XWPFWordExtractor(docPackage);
			String 				text 			= extractor.getText();
			
			extractor.close();
			
			CandidateExtractedFiltersBuilder filterBuilder = CandidateExtractedFilters.builder();
			
			extractFilters(text, filterBuilder);
			
			return filterBuilder.build();
		}
		
	}
	
	/**
	* Sets filters based upon the contents of the document
	* @param documentText 		- Text from document
	* @param filtersBuilder	  	- filters to be set
	*/
	private static void extractFilters(String documentText, CandidateExtractedFiltersBuilder filterBuilder) {
	
		documentText = documentText.toLowerCase();
		
		JobTitleExtractor.extractFilters(documentText, filterBuilder);
		SeniorityExtractor.extractFilters(documentText, filterBuilder);
	}
	
	/**
	* Class to extract the required seniority for the role
	* @author K Parkings
	*/
	private static class SeniorityExtractor{
		
		public enum Seniority {Senior, medior, junior}

		public static void extractFilters(String documentText, CandidateExtractedFiltersBuilder filterBuilder) {
			
			boolean senior = documentText.contains("senior");
			boolean medior = documentText.contains("medior");
			boolean junior = documentText.contains("junior");
			
			if (!senior && !medior && !junior) {
				return;
			}
			
			if (senior && medior && junior) {
				return;
			}
			
			if (medior && junior) {
				filterBuilder.experienceGTE("0");
				filterBuilder.experienceLTE("4");
				return;
			}
			
			if (medior && senior) {
				filterBuilder.experienceGTE("4");
				filterBuilder.experienceLTE("8");
				return;
			}
			
			if (senior && !medior && !junior) {
				filterBuilder.experienceGTE("8");
				filterBuilder.experienceLTE("");
				return;
			}
			
			if (medior && !senior && !junior) {
				filterBuilder.experienceGTE("4");
				filterBuilder.experienceLTE("8");
				return;
			}
			
			if (junior && !senior && !medior) {
				filterBuilder.experienceGTE("");
				filterBuilder.experienceLTE("2");
				return;
			}
			
		}
		
	}
	
	/**
	* Class to extract Job Title from Document
	* @author K Parkings
	*/
	private static class JobTitleExtractor{
		
		final static JobType java 				= new JobType(JobType.Type.java, 				Set.of("java developer", "java software engineer", "java engineer", "java software ontwikkelaar", "java ontwikkelaar", "fullstack java", "java backend developer"));
		final static JobType csharp 			= new JobType(JobType.Type.csharp, 				Set.of("c# developer", "c# software engineer", "c# engineer", "c# software ontwikkelaar", "c# ontwikkelaar", "fullstack c#", "c# backend developer"));
		final static JobType ba 				= new JobType(JobType.Type.ba, 					Set.of("business analyst","business analist"));
		final static JobType qa 				= new JobType(JobType.Type.qa, 					Set.of("qa engineer","test engineer", "test automation engineer", "test specialist", "test analyst", "performance tester", "automation tester", "qa tester", "software tester", "penetration tester", "software testers", "test lead"));
		final static JobType itSupport			= new JobType(JobType.Type.itSupport, 			Set.of("support engineer", "support developer", "support analyst", "tech support", "service agent", "support manager", "1st line support", "2nd line support", "3rd line support", "support specialist", "support technician"));
		final static JobType uiux				= new JobType(JobType.Type.uiux, 				Set.of("ui/ux designer", "ui designer", "ui engineer", "product designer"));
		final static JobType projectManager		= new JobType(JobType.Type.projectManager, 		Set.of("project manager", "program manager", "it manager", "procurement manager", "control manager", "operations manager", "ops manager", "head of it", "infrastructure manager", "infra manager", "development manager", "engineering manager", "security manager", "services manager", "delivery manager", "service manager", "asset manager"));
		final static JobType architect			= new JobType(JobType.Type.architect, 			Set.of("solution architect", "solutions architect", "enterprise architect", "application architect", "infrastructure architect", "security architect", "domain architect", "service now architect", "system architect", "systems architect"));
		final static JobType webDeveloper		= new JobType(JobType.Type.webDeveloper, 		Set.of("web developer","front end developer", "frontend developer"));
		final static JobType scrumMaster		= new JobType(JobType.Type.scrumMaster, 		Set.of("scrum master","scrummaster"));
		final static JobType dataScientist		= new JobType(JobType.Type.dataScientist, 		Set.of("data scientist", "data analyst", "data science"));
		final static JobType networkAdmin		= new JobType(JobType.Type.networkAdmin, 		Set.of("aws devops", "cloud devops", "azure devops", "platform engineer", "cloud engineer", "devops engineer","dev-ops engineer", "network admin", "network administrator", "network engineer", "network specialist", "system admin", "system administrator"));
		final static JobType softwareDeveloper	= new JobType(JobType.Type.softwareDeveloper, 	Set.of("software developer", "software engineer", "software engineers", "application engineer", "application developer"));
		final static JobType itSecurity			= new JobType(JobType.Type.itSecurity, 			Set.of("security engineer", "ethical hacker", "security officer", "security consultant", "security specialist", "security engineering", "security lead", "cyber consultant", "security advisor", "security manager", "security operations"));
		final static JobType itRecruiter		= new JobType(JobType.Type.itRecruiter, 		Set.of("tech recruiter", "it recruiter", "recruitment consultant", "technical recruiter"));
		final static JobType sdet 				= new JobType(JobType.Type.sdet, 				Set.of("engineer in test", "developer in test", "sdet"));
		
		public static void extractFilters(String documentText, CandidateExtractedFiltersBuilder filterBuilder) {
		
			Map<JobType.Type, AtomicInteger> scored = new LinkedHashMap<>();
		
			scored.put(JobType.Type.itRecruiter, 		new AtomicInteger(0));
			scored.put(JobType.Type.java, 				new AtomicInteger(0));
			scored.put(JobType.Type.csharp, 			new AtomicInteger(0));
			scored.put(JobType.Type.ba, 				new AtomicInteger(0));
			scored.put(JobType.Type.qa, 				new AtomicInteger(0));
			scored.put(JobType.Type.itSupport, 			new AtomicInteger(0));
			scored.put(JobType.Type.uiux, 				new AtomicInteger(0));
			scored.put(JobType.Type.projectManager, 	new AtomicInteger(0));
			scored.put(JobType.Type.architect, 			new AtomicInteger(0));
			scored.put(JobType.Type.webDeveloper, 		new AtomicInteger(0));
			scored.put(JobType.Type.scrumMaster, 		new AtomicInteger(0));
			scored.put(JobType.Type.dataScientist, 		new AtomicInteger(0));
			scored.put(JobType.Type.networkAdmin, 		new AtomicInteger(0));
			scored.put(JobType.Type.softwareDeveloper, 	new AtomicInteger(0));
			scored.put(JobType.Type.itSecurity, 		new AtomicInteger(0));
			scored.put(JobType.Type.sdet, 				new AtomicInteger(0));
			
			Set<JobType> jobs = new HashSet<>();
			
			jobs.add(java);
			jobs.add(csharp);
			jobs.add(ba);
			jobs.add(qa);
			jobs.add(itSupport);
			jobs.add(uiux);
			jobs.add(projectManager);
			jobs.add(architect);
			jobs.add(webDeveloper);
			jobs.add(scrumMaster);
			jobs.add(dataScientist);
			jobs.add(networkAdmin);
			jobs.add(softwareDeveloper);
			jobs.add(itSecurity);
			jobs.add(itRecruiter);
			jobs.add(sdet);
			
			jobs.stream().forEach(jobType -> {
				jobType.getTitles().stream().forEach(title -> {
					
					if (documentText.contains(title)) {
						scored.get(jobType.getType()).incrementAndGet();
					}
				});
			});
			
			Comparator<AtomicInteger> comparator = Comparator.comparing(o1 -> o1.get());
			
			JobType.Type type = Collections.max(scored.entrySet(), Map.Entry.comparingByValue(comparator)).getKey();
			
			/**
			* If no matched don't provide job title filter 
			*/
			if (scored.values().stream().filter(v -> v.get() > 0).findAny().isEmpty()) {
				return;
			}
			
			filterBuilder.jobTitle(type.role);
			
		}
		
	}
	
	
	private static class JobType{
		
		enum Type {
			java("Java Developer"), 
			csharp("C# Developer"), 
			ba("Business Analyst"),
			qa("Test Analyst"),
			itSupport("IT Support Analyst"),
			uiux("UI/UX Designer"),
			projectManager("Project manager"),
			architect("Architect"),
			webDeveloper("Web Developer"),
			scrumMaster("Scrum Master"),
			dataScientist("Data Scientist"),
			networkAdmin("Network Administrator"),
			softwareDeveloper("Software Developer"),
			itSecurity("IT Security"),
			itRecruiter("IT Recruiter"),
			sdet("SDET");
		
			public final String role;
			
			Type(String role) {
				this.role = role;
			}
			
		}
		
		private Type type;
		private Set<String> titles;
		
		public JobType(Type type, Set<String> titles) {
			this.type 	= type;
			this.titles = titles;
		}
		
		public Type getType() {
			return type;
		}
		
		public Set<String> getTitles(){
			return titles;
		}
		
	}
	
	/**
	* Extracts the skills of interest where they are present
	* in the Curriculum file
	* @param text - text content from the Curriculum file
	* @return Collection of skills found in the Curriculum
	*/
	//private static Set<String> extractSkills(Set<String> soughtSkills, String text) {
		
	//	Set<String> skills = new HashSet<>();
		
	//	final String baseText = text.toLowerCase();
		
	//	soughtSkills.stream().forEach(soughtSkill -> {
			
	//		String skillPatternSpace 		= soughtSkill.trim() + " ";
	//		String skillPatternPeriod 		= soughtSkill.trim() + ".";
	//		String skillPatternNewLine 		= soughtSkill.trim() + "\n";
	//		String skillPatternComma 		= soughtSkill.trim() + ",";
	//		String skillPatternSemiColom 	= soughtSkill.trim() + ";";
			
	//		if (baseText.contains(skillPatternSpace) 
	//				|| baseText.contains(skillPatternPeriod) 
	//				|| baseText.contains(skillPatternNewLine)
	//				|| baseText.contains(skillPatternComma)
	//				|| baseText.contains(skillPatternSemiColom)){
	//			skills.add(soughtSkill);
	//		}
			
	//	});
		
	//	return skills;
		
	//}
	
}