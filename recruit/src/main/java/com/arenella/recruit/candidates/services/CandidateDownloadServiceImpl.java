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
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Font; 
import org.springframework.stereotype.Service;

import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.Language;
import com.arenella.recruit.candidates.beans.Language.LANGUAGE;
import com.arenella.recruit.candidates.beans.Language.LEVEL;
import com.arenella.recruit.candidates.controllers.CandidateStandardAPIOutbound;
import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.enums.FREELANCE;
import com.arenella.recruit.candidates.enums.PERM;

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
		
		sheet.createFreezePane(0, 2);
		sheet.addMergedRegion(new CellRangeAddress(0,0,0,10));
		
		Set<CandidateStandardAPIOutbound> candidatesForOutput = candidates.stream().map(candidate -> CandidateStandardAPIOutbound.convertFromCandidate(candidate)).collect(Collectors.toCollection(LinkedHashSet::new));
		
		this.createHeader(workbook, sheet);
		
		AtomicInteger counter = new AtomicInteger(2);
		
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
			
			candidateCell.setCellValue("C"+candidate.getCandidateId());
			countryCell.setCellValue(getHumanReadableCountry(candidate.getCountry()));				
			cityCell.setCellValue(candidate.getCity());
			freelanceCell.setCellValue(getFreelanceValue(candidate.getFreelance()));	
			permCell.setCellValue(getPermValue(candidate.getPerm()));
			dutchCell.setCellValue(getLanguageValue(dutch) );	
			englishCell.setCellValue(getLanguageValue(english));	
			frenchCell.setCellValue(getLanguageValue(french));
			yearsExperienceCell.setCellValue(candidate.getYearsExperience());
			//roleCell.setCellValue(this.getHumanReadableCandidateFunction((candidate.getFunction())));
			roleCell.setCellValue(candidate.getRoleSought());
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
		
		Row rowBanner = sheet.createRow(0);
		rowBanner.setHeight(Short.valueOf("700"));
		
		Font bannerFont = workbook.createFont();  
		bannerFont.setBold(true);
		bannerFont.setFontHeightInPoints(Short.valueOf("28"));
		bannerFont.setColor(IndexedColors.LAVENDER.index);
		
		CellStyle  bannerCellStyle = workbook.createCellStyle();
		bannerCellStyle.setAlignment(HorizontalAlignment.LEFT);
		bannerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		bannerCellStyle.setFillBackgroundColor(IndexedColors.BLACK.getIndex());
		bannerCellStyle.setFont(bannerFont);
		
		Cell bannerCandidateCell0 		= rowBanner.createCell(0, CellType.STRING);
		Cell bannerCandidateCell1 		=rowBanner.createCell(1, CellType.STRING);
		Cell bannerCandidateCell2 		=rowBanner.createCell(2, CellType.STRING);
		Cell bannerCandidateCell3 		=rowBanner.createCell(3, CellType.STRING);
		Cell bannerCandidateCell4 		=rowBanner.createCell(4, CellType.STRING);
		Cell bannerCandidateCell5 		=rowBanner.createCell(5, CellType.STRING);
		Cell bannerCandidateCell6 		=rowBanner.createCell(6, CellType.STRING);
		Cell bannerCandidateCell7 		=rowBanner.createCell(7, CellType.STRING);
		Cell bannerCandidateCell8 		=rowBanner.createCell(8, CellType.STRING);
		Cell bannerCandidateCell9 		=rowBanner.createCell(9, CellType.STRING);
		Cell bannerCandidateCell10 		=rowBanner.createCell(10, CellType.STRING);
		
		bannerCandidateCell0.setCellValue("  Arenella ICT Candidate Recruitment");
		
		bannerCandidateCell0.setCellStyle(bannerCellStyle);
		bannerCandidateCell1.setCellStyle(bannerCellStyle);
		bannerCandidateCell2.setCellStyle(bannerCellStyle);
		bannerCandidateCell3.setCellStyle(bannerCellStyle);
		bannerCandidateCell4.setCellStyle(bannerCellStyle);
		bannerCandidateCell5.setCellStyle(bannerCellStyle);
		bannerCandidateCell6.setCellStyle(bannerCellStyle);
		bannerCandidateCell7.setCellStyle(bannerCellStyle);
		bannerCandidateCell8.setCellStyle(bannerCellStyle);
		bannerCandidateCell9.setCellStyle(bannerCellStyle);
		bannerCandidateCell10.setCellStyle(bannerCellStyle);
		
		Row row = sheet.createRow(1);
	
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
		font.setColor(IndexedColors.WHITE.index);
		
		CellStyle  cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		cellStyle.setFillForegroundColor(IndexedColors.GREY_80_PERCENT.getIndex());
		cellStyle.setFillBackgroundColor(IndexedColors.GREY_80_PERCENT.getIndex());
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

	/**
	* Returns human readable value to represent a language spoken 
	* by the Candidate
	* @param language - Language spoken by the Candidate
	* @return human readable version of the Language
	*/
	private String getLanguageValue(Optional<Language> language) {
		
		if (language.isEmpty()) {
			return "-";
		}
		
		if (language.get().getLevel() == LEVEL.UNKNOWN) {
			return "?";
		}
		
		return language.get().getLevel() == LEVEL.PROFICIENT ? "X" : "X (basic)";
		
	}
	
	/**
	* Returns human readable version of the COUNTRY
	* @param country - COUNTRY value
	* @return human readable country value
	*/
	private String getHumanReadableCountry(COUNTRY country) {
		
		switch (country) {
			case NETHERLANDS:	{ return "NL";}
			case UK:		 	{ return "UK";}
			case BELGIUM:		{ return "BE";}
			case EUROPE:		{ return "EU";}
			default: 			{return "NA";}
		}
		
	}
	
	/**
	* Converts the FREELANCE value to a human readable form
	* @param freelance - Whether the Candidate is interested in Freelance roles
	* @return human readable FREELANCE option
	*/
	public String getFreelanceValue(FREELANCE freelance) {
		
		switch (freelance) {
			case TRUE: {return "X";}
			case FALSE: {return "-";}
			case UNKNOWN: {return "?";}
		}
		return "?";
	}

	/**
	* Converts the PERM value to a human readable form
	* @param perm - Whether the Candidate is interested in Perm roles
	* @return human readable PERM option
	*/
	public String getPermValue(PERM perm) {
		
		switch (perm) {
			case TRUE: {return "X";}
			case FALSE: {return "-";}
			case UNKNOWN: {return "?";}
		}
		
		return "?";
	}

}
