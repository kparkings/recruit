package com.arenella.recruit.candidates.services;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Font; 
import org.springframework.stereotype.Service;

import com.arenella.recruit.candidates.controllers.CandidateAPIOutbound;
import com.arenella.recruit.candudates.beans.Candidate;
import com.arenella.recruit.candudates.beans.Language;
import com.arenella.recruit.candudates.beans.Language.LANGUAGE;
import com.arenella.recruit.candudates.beans.Language.LEVEL;

/**
* Services for dowloading Candidate details
* @author K Parkings
*/
@Service
public class CandidateDownloadServiceImpl implements CandidateDownloadService{

	/**
	* Refer to the CandidateDownloadService for details 
	*/
	@Override
	public XSSFWorkbook createXLSCandidateDownload(Set<Candidate> candidates) {
		
		XSSFWorkbook	workbook	= new XSSFWorkbook();
		XSSFSheet		sheet		= workbook.createSheet("Candidates");
		
		Set<CandidateAPIOutbound> candidatesForOutput = candidates.stream().map(candidate -> CandidateAPIOutbound.convertFromCandidate(candidate)).collect(Collectors.toCollection(LinkedHashSet::new));
		
		this.createHeader(workbook, sheet);
		
		AtomicInteger counter = new AtomicInteger(1);
		
		candidatesForOutput.stream().forEach(candidate -> {
			
			Row candidateRow = sheet.createRow(counter.getAndIncrement());
			
			Cell candidateCell 			= candidateRow.createCell(0, CellType.STRING);
			Cell countryCell 			= candidateRow.createCell(1, CellType.STRING);
			Cell cityCell 				= candidateRow.createCell(2, CellType.STRING);
			Cell freelanceCell 			= candidateRow.createCell(3, CellType.STRING);
			Cell permCell 				= candidateRow.createCell(4, CellType.STRING);
			Cell dutchCell 				= candidateRow.createCell(5, CellType.STRING);
			Cell englishCell 			= candidateRow.createCell(6, CellType.STRING);
			Cell frenchCell 			= candidateRow.createCell(7, CellType.STRING);
			Cell yearsExperienceCell 	= candidateRow.createCell(8, CellType.STRING);
			Cell roleCell 				= candidateRow.createCell(9, CellType.STRING);
			Cell skillsCell 			= candidateRow.createCell(10, CellType.STRING); 
			
			Optional<Language> dutch 	= candidate.getLanguages().stream().filter(lang -> lang.getLanguage() == LANGUAGE.DUTCH).findAny();
			Optional<Language> english 	= candidate.getLanguages().stream().filter(lang -> lang.getLanguage() == LANGUAGE.ENGLISH).findAny();
			Optional<Language> french 	= candidate.getLanguages().stream().filter(lang -> lang.getLanguage() == LANGUAGE.FRENCH).findAny();
			
			candidateCell.setCellValue(candidate.getCandidateId());
			countryCell.setCellValue(candidate.getCountry().toString());				
			cityCell.setCellValue(candidate.getCity());
			freelanceCell.setCellValue(candidate.isFreelance() 		? "-" : "X");	
			permCell.setCellValue(candidate.isPermanent() 			? "-" : "X");
			dutchCell.setCellValue(getLanguageValue(dutch) );	
			englishCell.setCellValue(getLanguageValue(english));	
			frenchCell.setCellValue(getLanguageValue(french));
			yearsExperienceCell.setCellValue(candidate.getYearsExperience());
			roleCell.setCellValue(candidate.getFunction().toString());
			skillsCell.setCellValue(String.join(",", candidate.getSkills()));	 
			
			CellStyle  cellStyle = workbook.createCellStyle();
			cellStyle.setAlignment(HorizontalAlignment.CENTER);
			cellStyle.setBorderTop(BorderStyle.THIN);
			cellStyle.setBorderBottom(BorderStyle.THIN);
			cellStyle.setBorderLeft(BorderStyle.THIN);
			cellStyle.setBorderRight(BorderStyle.THIN);
			
			candidateCell.setCellStyle(cellStyle);
			countryCell.setCellStyle(cellStyle);				
			cityCell.setCellStyle(cellStyle);
			freelanceCell.setCellStyle(cellStyle);	
			permCell.setCellStyle(cellStyle);
			dutchCell.setCellStyle(cellStyle);	
			englishCell.setCellStyle(cellStyle);	
			frenchCell.setCellStyle(cellStyle);
			yearsExperienceCell.setCellStyle(cellStyle);
			roleCell.setCellStyle(cellStyle);
			
			CellStyle  cellStyleLeft = workbook.createCellStyle();
			cellStyleLeft.setAlignment(HorizontalAlignment.LEFT);
			cellStyleLeft.setBorderTop(BorderStyle.THIN);
			cellStyleLeft.setBorderBottom(BorderStyle.THIN);
			cellStyleLeft.setBorderLeft(BorderStyle.THIN);
			cellStyleLeft.setBorderRight(BorderStyle.THIN);
			
			skillsCell.setCellStyle(cellStyleLeft);	
			
		});
		
		sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(1);
		sheet.autoSizeColumn(2);
		sheet.autoSizeColumn(3);
		sheet.autoSizeColumn(4);
		sheet.autoSizeColumn(5);
		sheet.autoSizeColumn(6);
		sheet.autoSizeColumn(7);
		sheet.autoSizeColumn(8);
		sheet.autoSizeColumn(9);
		sheet.autoSizeColumn(10);
		
		return workbook;
	
	}
	
	/**
	* Adds a Header to the export 
	* @param sheet
	*/
	private void createHeader(XSSFWorkbook	workbook, XSSFSheet sheet) {
		
		Row row = sheet.createRow(0);
	
		Cell candidateCell 			= row.createCell(0, CellType.STRING);
		Cell countryCell 			= row.createCell(1, CellType.STRING);
		Cell cityCell 				= row.createCell(2, CellType.STRING);
		Cell freelanceCell 			= row.createCell(3, CellType.STRING);
		Cell permCell 				= row.createCell(4, CellType.STRING);
		Cell dutchCell 				= row.createCell(5, CellType.STRING);
		Cell englishCell 			= row.createCell(6, CellType.STRING);
		Cell frenchCell 			= row.createCell(7, CellType.STRING);
		Cell yearsExperienceCell 	= row.createCell(8, CellType.STRING);
		Cell roleCell 				= row.createCell(9, CellType.STRING);
		Cell skillsCell 			= row.createCell(10, CellType.STRING);
		
		candidateCell.setCellValue("Candidate");
		countryCell.setCellValue("Country");
		cityCell.setCellValue("City");
		freelanceCell.setCellValue("Freelance");
		permCell.setCellValue("Permanent");
		dutchCell.setCellValue("Dutch");
		englishCell.setCellValue("English");
		frenchCell.setCellValue("French");
		yearsExperienceCell.setCellValue("Years Experience");
		roleCell.setCellValue("Function");
		skillsCell.setCellValue("Skills");
		
		Font font = workbook.createFont();  
		font.setBold(true);
		
		CellStyle  cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		cellStyle.setFillBackgroundColor(IndexedColors.LIGHT_BLUE.getIndex());
		cellStyle.setFont(font);
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		
		candidateCell.setCellStyle(cellStyle);
		countryCell.setCellStyle(cellStyle);
		cityCell.setCellStyle(cellStyle);
		freelanceCell.setCellStyle(cellStyle);
		permCell.setCellStyle(cellStyle);
		dutchCell.setCellStyle(cellStyle);
		englishCell.setCellStyle(cellStyle);
		frenchCell.setCellStyle(cellStyle);
		yearsExperienceCell.setCellStyle(cellStyle);
		roleCell.setCellStyle(cellStyle);
		skillsCell.setCellStyle(cellStyle);
		
	}
	
	private String getLanguageValue(Optional<Language> language) {
		
		if (language.isEmpty()) {
			return "-";
		}
		
		return language.get().getLevel() == LEVEL.PROFICIENT ? "X" : "X (basic)";
		
	}

}
