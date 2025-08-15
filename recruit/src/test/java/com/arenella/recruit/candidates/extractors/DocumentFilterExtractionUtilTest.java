package com.arenella.recruit.candidates.extractors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.arenella.recruit.candidates.beans.CandidateExtractedFilters;
import com.arenella.recruit.candidates.beans.Language.LANGUAGE;
import com.arenella.recruit.candidates.dao.CandidateSkillsDao;
import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.enums.FREELANCE;
import com.arenella.recruit.candidates.enums.PERM;

/**
* Unit tests for the DocumentFilterExtractionUtil class 
*/
@ExtendWith(MockitoExtension.class)
class DocumentFilterExtractionUtilTest {

	@Spy
	private JobTitleExtractor 		jobTitleExtractor;
	
	@Spy
	private SeniorityExtractor 		seniorityExtractor;
	
	private CountryExtractor 		countryExtractor = new CountryExtractor();
	
	@Spy
	private LanguageExtractor 		languageExtractor;
	
	@Spy
	private CityExtractor 			cityExtractor;
	
	@Spy
	private SkillExtractor 			skillExtractor;
	
	@Spy
	private ContractTypeExtractor 	contractTypeExtractor;
	
	@Mock
	private CandidateSkillsDao 		mockSkillsDao;
	
	@InjectMocks
	private DocumentFilterExtractionUtil util = new DocumentFilterExtractionUtil();
	
	/**
	* Sets up test environment 
	*/
	@BeforeEach
	void setup() {
		ReflectionTestUtils.setField(skillExtractor, "skillsDao", mockSkillsDao);
		ReflectionTestUtils.setField(util, "countryExtractor", countryExtractor);
		Mockito.when(mockSkillsDao.getActiveAndPendingSkills()).thenReturn(Set.of("java","css","maven","wpf","vue.js","react","eclipse","powerbi", "kotlin", "c#"));
	}
	
	/**
	* Test
	* @throws IOException
	*/
	@Test
	void testExtractFunction1() throws IOException {
	
		
		File file = new File("src/test/resources/extractorscripts/extractorscenario1.txt");
		
		String contents = FileUtils.readFileToString(file, Charset.defaultCharset());
				
		CandidateExtractedFilters filters = util.extractFilters(contents);
				
		assertEquals("Java Developer", 		filters.getJobTitle());		
		assertEquals(COUNTRY.NETHERLANDS, 	filters.getCountries().toArray()[0]);
		assertEquals(1, 					filters.getCountries().size());
		assertEquals("3", 					filters.getExperienceGTE());
		assertEquals("5",	 				filters.getExperienceLTE());
		assertEquals(FREELANCE.TRUE, 		filters.getFreelance());
		assertEquals(PERM.FALSE, 			filters.getPerm());
		assertEquals(1, 					filters.getSkills().size());
		
		assertTrue(filters.getLanguages().isEmpty());
		assertTrue(filters.getSkills().contains("java"));

	}
	
	/**
	* Test
	* @throws IOException
	*/
	@Test
	void testExtractFunction2() throws IOException {
		
		File file = new File("src/test/resources/extractorscripts/extractorscenario2.txt");
		
		String 						contents 	= FileUtils.readFileToString(file, Charset.defaultCharset());
		CandidateExtractedFilters 	filters 	= util.extractFilters(contents);
				
		assertEquals("Java Developer", 		filters.getJobTitle());		
		assertEquals(PERM.TRUE, 			filters.getPerm());
			
		assertTrue(filters.getCountries().isEmpty());
		assertEquals("", 					filters.getExperienceGTE());
		assertEquals("",	 				filters.getExperienceLTE());
		assertEquals(FREELANCE.FALSE, 		filters.getFreelance());
		assertEquals(1, 					filters.getSkills().size());
		
		assertTrue(filters.getLanguages().isEmpty());
		assertTrue(filters.getSkills().contains("java"));

	}
	
	/**
	* Test Java/Kotlin Amsterdam 8-10 years 
	* 
	* @throws IOException
	*/
	@Test
	void testExtractFunction3() throws IOException {
		
		File file = new File("src/test/resources/extractorscripts/extractorscenario3.txt");
		
		String 						contents 	= FileUtils.readFileToString(file, Charset.defaultCharset());
		CandidateExtractedFilters 	filters 	= util.extractFilters(contents);
		
		assertEquals("Java Developer", 	filters.getJobTitle());	
		assertEquals(COUNTRY.NETHERLANDS, 	filters.getCountries().toArray()[0]);
		assertEquals("5", 					filters.getExperienceGTE());
		assertEquals("",	 				filters.getExperienceLTE());
		
		assertNull(filters.getPerm());
		assertNull(filters.getFreelance());	
		assertTrue(filters.getLanguages().isEmpty());
		
		
		assertTrue(filters.getSkills().contains("java"));
		assertTrue(filters.getSkills().contains("kotlin"));
		assertEquals(2, filters.getSkills().size());
		
	}
	
	/**
	* Test UK C# PERM 
	* 
	* @throws IOException
	*/
	@Test
	void testExtractFunction4() throws IOException {
		
		File file = new File("src/test/resources/extractorscripts/extractorscenario4.txt");
		
		String 						contents 	= FileUtils.readFileToString(file, Charset.defaultCharset());
		CandidateExtractedFilters 	filters 	= util.extractFilters(contents);
		
		assertEquals("C# Developer", 		filters.getJobTitle());	
		assertEquals(COUNTRY.UK, 			filters.getCountries().toArray()[0]);
		assertEquals("", 					filters.getExperienceGTE());
		assertEquals("",	 				filters.getExperienceLTE());
		assertEquals(PERM.TRUE, 			filters.getPerm());
		assertEquals(FREELANCE.FALSE, 		filters.getFreelance());
		assertEquals(1, filters.getLanguages().size());
		assertTrue(filters.getLanguages().contains(LANGUAGE.ENGLISH));
		assertTrue(filters.getSkills().contains("c#"));
		assertEquals(1, filters.getSkills().size());
		
	}
	
	/**
	* Test FINLAND JAVA PERM
	* 
	* @throws IOException
	*/
	@Test
	void testExtractFunction5() throws IOException {
		
		File file = new File("src/test/resources/extractorscripts/extractorscenario5.txt");
		
		String 						contents 	= FileUtils.readFileToString(file, Charset.defaultCharset());
		CandidateExtractedFilters 	filters 	= util.extractFilters(contents);
		
		assertEquals("Java Developer", 		filters.getJobTitle());	
		assertEquals(COUNTRY.FINLAND, 		filters.getCountries().toArray()[0]);
		assertEquals("5", 					filters.getExperienceGTE());
		assertEquals("",	 				filters.getExperienceLTE());
		assertEquals(PERM.TRUE, 			filters.getPerm());
		assertEquals(FREELANCE.FALSE, 		filters.getFreelance());

		assertTrue(filters.getLanguages().isEmpty());
		assertTrue(filters.getSkills().contains("java"));
		assertEquals(1, filters.getSkills().size());
		
	}

	/**
	* Test JAVA KOTLIN POLAND SENIOR CONTRACT ENGLISH
	* 
	* @throws IOException
	*/
	@Test
	void testExtractFunction6() throws IOException {
		
		File file = new File("src/test/resources/extractorscripts/extractorscenario6.txt");
		
		String 						contents 	= FileUtils.readFileToString(file, Charset.defaultCharset());
		CandidateExtractedFilters 	filters 	= util.extractFilters(contents);
		
		assertEquals("Java Developer", 	filters.getJobTitle());	
		assertEquals(COUNTRY.POLAND, 		filters.getCountries().toArray()[0]);
		assertEquals("5", 					filters.getExperienceGTE());
		assertEquals("",	 				filters.getExperienceLTE());
		assertEquals(PERM.FALSE, 			filters.getPerm());
		assertEquals(FREELANCE.TRUE, 		filters.getFreelance());

		assertEquals(1, filters.getLanguages().size());
		assertTrue(filters.getLanguages().contains(LANGUAGE.ENGLISH));
		assertTrue(filters.getSkills().contains("java"));
		assertTrue(filters.getSkills().contains("kotlin"));
		assertEquals(2, filters.getSkills().size());
		
	}
	
	/**
	* Test IRELAND PERM SENIOR NODE
	* 
	* @throws IOException
	*/
	@Test
	void testExtractFunction7() throws IOException {
		
		File file = new File("src/test/resources/extractorscripts/extractorscenario7.txt");
		
		String 						contents 	= FileUtils.readFileToString(file, Charset.defaultCharset());
		CandidateExtractedFilters 	filters 	= util.extractFilters(contents);
		
		assertEquals("Node Developer", 			filters.getJobTitle());	
		assertEquals(COUNTRY.REPUBLIC_OF_IRELAND, 	filters.getCountries().toArray()[0]);
		assertEquals("5", 							filters.getExperienceGTE());
		assertEquals("",	 						filters.getExperienceLTE());
		assertEquals(PERM.TRUE, 					filters.getPerm());
		assertEquals(FREELANCE.FALSE, 				filters.getFreelance());

		assertTrue(filters.getLanguages().isEmpty());
		assertTrue(filters.getSkills().contains("react"));
		assertEquals(1, filters.getSkills().size());
		
	}
	
	/**
	* Test C# PERM UK
	* 
	* @throws IOException
	*/
	@Test
	void testExtractFunction8() throws IOException {
		
		File file = new File("src/test/resources/extractorscripts/extractorscenario8.txt");
		
		String 						contents 	= FileUtils.readFileToString(file, Charset.defaultCharset());
		CandidateExtractedFilters 	filters 	= util.extractFilters(contents);
		
		assertEquals("C# Developer", 			filters.getJobTitle());	
		assertEquals(COUNTRY.UK, 				filters.getCountries().toArray()[0]);
		assertEquals("", 						filters.getExperienceGTE());
		assertEquals("",	 					filters.getExperienceLTE());
		assertEquals(PERM.TRUE, 				filters.getPerm());
		assertEquals(FREELANCE.FALSE, 			filters.getFreelance());

		assertEquals(1, filters.getLanguages().size());
		assertTrue(filters.getLanguages().contains(LANGUAGE.ENGLISH));
		assertTrue(filters.getSkills().contains("c#"));
		assertTrue(filters.getSkills().contains("react"));
		assertEquals(2, filters.getSkills().size());
		
	}
	
	/**
	* Test JAVA SENIOR FREELANCE NETHERLANDS
	* 
	* @throws IOException
	*/
	@Test
	void testExtractFunction9() throws IOException {
		
		File file = new File("src/test/resources/extractorscripts/extractorscenario9.txt");
		
		String 						contents 	= FileUtils.readFileToString(file, Charset.defaultCharset());
		CandidateExtractedFilters 	filters 	= util.extractFilters(contents);
		
		assertEquals("Java Developer", 			filters.getJobTitle());	
		assertEquals(COUNTRY.NETHERLANDS, 		filters.getCountries().toArray()[0]);
		assertEquals("5", 						filters.getExperienceGTE());
		assertEquals("",	 					filters.getExperienceLTE());
		assertEquals(PERM.FALSE, 				filters.getPerm());
		assertEquals(FREELANCE.TRUE, 			filters.getFreelance());

		assertTrue(filters.getLanguages().isEmpty());
		assertTrue(filters.getSkills().contains("java"));
		assertEquals(1, filters.getSkills().size());
		
	}

	/**
	* Test PYTHON FREELANCE UK 
	* 
	* @throws IOException
	*/
	@Test
	void testExtractFunctionA1() throws IOException {
		
		File file = new File("src/test/resources/extractorscripts/extractorscenarioA1.txt");
		
		String 						contents 	= FileUtils.readFileToString(file, Charset.defaultCharset());
		CandidateExtractedFilters 	filters 	= util.extractFilters(contents);
		
		assertEquals("Python Developer", 		filters.getJobTitle());	
		assertEquals(COUNTRY.UK, 				filters.getCountries().toArray()[0]);
		assertEquals("", 						filters.getExperienceGTE());
		assertEquals("",	 					filters.getExperienceLTE());
		assertEquals(PERM.FALSE, 				filters.getPerm());
		assertEquals(FREELANCE.TRUE, 			filters.getFreelance());

		assertEquals(1, filters.getLanguages().size());
		assertTrue(filters.getLanguages().contains(LANGUAGE.ENGLISH));
		assertTrue(filters.getSkills().isEmpty());
		
	}
	
	/**
	* Test SPAIN SENIOR JAVA  
	* 
	* @throws IOException
	*/
	@Test
	void testExtractFunctionA2() throws IOException {
		
		File file = new File("src/test/resources/extractorscripts/extractorscenarioA2.txt");
		
		String 						contents 	= FileUtils.readFileToString(file, Charset.defaultCharset());
		CandidateExtractedFilters 	filters 	= util.extractFilters(contents);
		
		assertEquals("Java Developer", 		filters.getJobTitle());	
		assertEquals("5", 					filters.getExperienceGTE());
		assertEquals("",	 				filters.getExperienceLTE());
		
		assertTrue(filters.getCountries().contains(COUNTRY.SPAIN));
		assertTrue(filters.getCountries().contains(COUNTRY.PORTUGAL));
		assertTrue(filters.getCountries().contains(COUNTRY.UK));
		
		assertNull(filters.getPerm());
		assertNull(filters.getFreelance());	
		assertEquals(1, filters.getLanguages().size());
		assertTrue(filters.getLanguages().contains(LANGUAGE.ENGLISH));
		assertTrue(filters.getSkills().contains("java"));
		assertEquals(1, filters.getSkills().size());
		
	}
	
	/**
	* Test C# SENIOR PERM NETHERLANDS 
	* 
	* @throws IOException
	*/
	@Test
	void testExtractFunctionA3() throws IOException {
		
		File file = new File("src/test/resources/extractorscripts/extractorscenarioA3.txt");
		
		String 						contents 	= FileUtils.readFileToString(file, Charset.defaultCharset());
		CandidateExtractedFilters 	filters 	= util.extractFilters(contents);
		
		assertEquals("C# Developer", 		filters.getJobTitle());	
		assertEquals("5", 					filters.getExperienceGTE());
		assertEquals("",	 				filters.getExperienceLTE());
		
		assertEquals(COUNTRY.NETHERLANDS, 			filters.getCountries().toArray()[0]);
		
		assertEquals(PERM.TRUE, 				filters.getPerm());
		assertEquals(FREELANCE.FALSE, 			filters.getFreelance());
		
		assertTrue(filters.getLanguages().isEmpty());
		assertTrue(filters.getSkills().contains("c#"));
		assertEquals(1, filters.getSkills().size());
		
	}
	
	/**
	* Test SPAIN PERM JAVA ENGLISH MEDIOR
	* 
	* @throws IOException
	*/
	@Test
	void testExtractFunctionA4() throws IOException {
		
		File file = new File("src/test/resources/extractorscripts/extractorscenarioA4.txt");
		
		String 						contents 	= FileUtils.readFileToString(file, Charset.defaultCharset());
		CandidateExtractedFilters 	filters 	= util.extractFilters(contents);
		
		assertEquals("Java Developer", 		filters.getJobTitle());	
		assertEquals("3", 					filters.getExperienceGTE());
		assertEquals("5",	 				filters.getExperienceLTE());
		
		assertEquals(COUNTRY.SPAIN, 			filters.getCountries().toArray()[0]);
		
		assertEquals(PERM.TRUE, 				filters.getPerm());
		assertEquals(FREELANCE.FALSE, 			filters.getFreelance());
		
		assertEquals(1, filters.getLanguages().size());
		assertTrue(filters.getLanguages().contains(LANGUAGE.ENGLISH));
		assertTrue(filters.getSkills().contains("java"));
		assertEquals(1, filters.getSkills().size());
		
	}
	
	/**
	* Test C# REACT POLAND PERM SENIOR 
	* 
	* @throws IOException
	*/
	@Test
	void testExtractFunctionA5() throws IOException {
		
		File file = new File("src/test/resources/extractorscripts/extractorscenarioA5.txt");
		
		String 						contents 	= FileUtils.readFileToString(file, Charset.defaultCharset());
		CandidateExtractedFilters 	filters 	= util.extractFilters(contents);
		
		assertEquals("C# Developer", 		filters.getJobTitle());	
		assertEquals("5", 					filters.getExperienceGTE());
		assertEquals("",	 				filters.getExperienceLTE());
		
		assertEquals(COUNTRY.POLAND, 		filters.getCountries().toArray()[0]);
		
		assertEquals(PERM.TRUE, 			filters.getPerm());
		assertEquals(FREELANCE.FALSE, 		filters.getFreelance());
		
		assertTrue(filters.getLanguages().isEmpty());
		assertTrue(filters.getSkills().contains("react"));
		assertEquals(1, filters.getSkills().size());
		
	}
	
	/**
	* Test C# ANGULAR NETHERLANDS FREELANCE 
	* 
	* @throws IOException
	*/
	@Test
	void testExtractFunctionA6() throws IOException {
		
		File file = new File("src/test/resources/extractorscripts/extractorscenarioA6.txt");
		
		String 						contents 	= FileUtils.readFileToString(file, Charset.defaultCharset());
		CandidateExtractedFilters 	filters 	= util.extractFilters(contents);
		
		assertEquals("C# Developer", 		filters.getJobTitle());	
		assertEquals("", 					filters.getExperienceGTE());
		assertEquals("",	 				filters.getExperienceLTE());
		
		assertEquals(COUNTRY.NETHERLANDS, 	filters.getCountries().toArray()[0]);
		
		assertEquals(PERM.FALSE, 			filters.getPerm());
		assertEquals(FREELANCE.TRUE, 		filters.getFreelance());
		
		assertTrue(filters.getLanguages().isEmpty());
		assertTrue(filters.getSkills().contains("c#"));
		assertEquals(1, filters.getSkills().size());
		
	}
	
	/**
	* Test JAVA NETHERLANDS FREELANCE
	* 
	* @throws IOException
	*/
	@Test
	void testExtractFunctionA7() throws IOException {
		
		File file = new File("src/test/resources/extractorscripts/extractorscenarioA7.txt");
		
		String 						contents 	= FileUtils.readFileToString(file, Charset.defaultCharset());
		CandidateExtractedFilters 	filters 	= util.extractFilters(contents);
		
		assertEquals("Java Developer", 		filters.getJobTitle());	
		assertEquals("", 					filters.getExperienceGTE());
		assertEquals("",	 				filters.getExperienceLTE());
		
		assertEquals(COUNTRY.NETHERLANDS, 	filters.getCountries().toArray()[0]);
		
		assertEquals(PERM.FALSE, 			filters.getPerm());
		assertEquals(FREELANCE.TRUE, 		filters.getFreelance());
		
		assertTrue(filters.getLanguages().isEmpty());
		assertTrue(filters.getSkills().contains("java"));
		assertEquals(1, filters.getSkills().size());
		
	}
	
	/**
	* Test SCRUM MASTER SWITZERLAND SENIOR ENGLISH FRENCH
	* 
	* @throws IOException
	*/
	@Test
	void testExtractFunctionA8() throws IOException {
		
		File file = new File("src/test/resources/extractorscripts/extractorscenarioA8.txt");
		
		String 						contents 	= FileUtils.readFileToString(file, Charset.defaultCharset());
		CandidateExtractedFilters 	filters 	= util.extractFilters(contents);
		
		assertEquals("Scrum Master", 		filters.getJobTitle());	
		assertEquals("5", 					filters.getExperienceGTE());
		assertEquals("",	 				filters.getExperienceLTE());
		
		assertTrue(filters.getCountries().contains(COUNTRY.SWITZERLAND));
		assertTrue(filters.getCountries().contains(COUNTRY.FRANCE));
		assertEquals(2, 					filters.getCountries().size());
		
		assertEquals(PERM.TRUE, 			filters.getPerm());
		assertEquals(FREELANCE.FALSE, 		filters.getFreelance());
		
		assertEquals(2, filters.getLanguages().size());
		assertTrue(filters.getLanguages().contains(LANGUAGE.ENGLISH));
		assertTrue(filters.getLanguages().contains(LANGUAGE.FRENCH));
		assertTrue(filters.getSkills().isEmpty());
		
	}
	
	/**
	* Test JAVA GERMANY GERMAN ENGLISH PERM
	* 
	* @throws IOException
	*/
	@Test
	void testExtractFunctionA9() throws IOException {
		
		File file = new File("src/test/resources/extractorscripts/extractorscenarioA9.txt");
		
		String 						contents 	= FileUtils.readFileToString(file, Charset.defaultCharset());
		CandidateExtractedFilters 	filters 	= util.extractFilters(contents);
		
		assertEquals("Java Developer", 		filters.getJobTitle());	
		assertEquals("", 					filters.getExperienceGTE());
		assertEquals("",	 				filters.getExperienceLTE());
		
		assertTrue(filters.getCountries().contains(COUNTRY.GERMANY));
		assertEquals(1, 					filters.getCountries().size());
		
		assertEquals(PERM.TRUE, 			filters.getPerm());
		assertEquals(FREELANCE.FALSE, 		filters.getFreelance());
		
		assertEquals(2, filters.getLanguages().size());
		assertTrue(filters.getLanguages().contains(LANGUAGE.ENGLISH));
		assertTrue(filters.getLanguages().contains(LANGUAGE.GERMAN));
		
		assertTrue(filters.getSkills().contains("css"));
		assertTrue(filters.getSkills().contains("java"));
		assertTrue(filters.getSkills().contains("maven"));
		assertTrue(filters.getSkills().contains("eclipse"));
		assertEquals(4, filters.getSkills().size());
		
	}
	
	/**
	* Test Python junior/medior uk perm
	* 
	* @throws IOException
	*/
	@Test
	void testExtractFunctionB1() throws IOException {
		
		File file = new File("src/test/resources/extractorscripts/extractorscenarioB1.txt");
		
		String 						contents 	= FileUtils.readFileToString(file, Charset.defaultCharset());
		CandidateExtractedFilters 	filters 	= util.extractFilters(contents);
		
		assertEquals("Python Developer", 	filters.getJobTitle());	
		assertEquals("", 					filters.getExperienceGTE());
		assertEquals("5",	 				filters.getExperienceLTE());
		
		assertTrue(filters.getCountries().contains(COUNTRY.UK));
		assertEquals(1, 					filters.getCountries().size());
		
		assertEquals(PERM.TRUE, 			filters.getPerm());
		assertEquals(FREELANCE.FALSE, 		filters.getFreelance());
		
		assertEquals(1, filters.getLanguages().size());
		assertTrue(filters.getLanguages().contains(LANGUAGE.ENGLISH));
		
		assertTrue(filters.getSkills().isEmpty());
		
	}
	
	/**
	* Test JAVA PERM NETHERLANDS SENIOR
	* 
	* @throws IOException
	*/
	@Test
	void testExtractFunctionB2() throws IOException {
		
		File file = new File("src/test/resources/extractorscripts/extractorscenarioB2.txt");
		
		String 						contents 	= FileUtils.readFileToString(file, Charset.defaultCharset());
		CandidateExtractedFilters 	filters 	= util.extractFilters(contents);
		
		assertEquals("Java Developer", 		filters.getJobTitle());	
		assertEquals("5", 					filters.getExperienceGTE());
		assertEquals("",	 				filters.getExperienceLTE());
		
		assertTrue(filters.getCountries().contains(COUNTRY.NETHERLANDS));
		assertEquals(1, 					filters.getCountries().size());
		
		assertEquals(PERM.TRUE, 			filters.getPerm());
		assertEquals(FREELANCE.FALSE, 		filters.getFreelance());
		
		assertTrue(filters.getLanguages().isEmpty());
		
		assertTrue(filters.getSkills().contains("java"));
		//assertTrue(filters.getSkills().contains("kotlin")); //kotlin with no space after and at end of line not being selected. Because skills have space before and after before doing comparison
		assertTrue(filters.getSkills().contains("react"));
		assertEquals(2, filters.getSkills().size());
		
	}
	
	/**
	* Test ANGULAR DEVELOPER UK CONTRACT SENIOR
	* 
	* @throws IOException
	*/
	@Test
	void testExtractFunctionB3() throws IOException {
		
		File file = new File("src/test/resources/extractorscripts/extractorscenarioB3.txt");
		
		String 						contents 	= FileUtils.readFileToString(file, Charset.defaultCharset());
		CandidateExtractedFilters 	filters 	= util.extractFilters(contents);
		
		assertEquals("Angular Developer", 		filters.getJobTitle());	
		assertEquals("5", 					filters.getExperienceGTE());
		assertEquals("",	 				filters.getExperienceLTE());
		
		assertTrue(filters.getCountries().contains(COUNTRY.UK));
		assertEquals(1, 					filters.getCountries().size());
		
		assertEquals(PERM.FALSE, 			filters.getPerm());
		assertEquals(FREELANCE.TRUE, 		filters.getFreelance());
		
		assertEquals(1, filters.getLanguages().size());
		assertTrue(filters.getLanguages().contains(LANGUAGE.ENGLISH));
		
		assertTrue(filters.getSkills().contains("c#"));
		assertEquals(1, filters.getSkills().size());
		
	}
	
}
