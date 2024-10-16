package com.arenella.recruit.recruiters.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class InvoiceBuilderUtil {

	private static final PDFont FONT_HDR 				= PDType1Font.HELVETICA_BOLD;
	private static final PDFont FONT_TXT 				= PDType1Font.HELVETICA;
	private static final float 	SECTION_MARGIN_TOP 		= 40;
	private static final float 	LINE_HEIGHT 			= 20;
	private static final float 	LEFT_MARGIN 			= 100;
	private static final float 	TOP_POS 				= 740;
	
	private float currentPos = TOP_POS;
	
	public ByteArrayResource generateInvoice(UUID subscriptionId, String invoiceNumber, Optional<LocalDate> invoiceDate, Optional<String> unitDescription) {
		
		ByteArrayResource byteArrayResource = null;
		
		try {
			
			PDDocument 			doc 			= new PDDocument();
			PDPage 				page 			= new PDPage();
			
			doc.addPage(page);
			
			PDPageContentStream contentStream =  new PDPageContentStream(doc, page);
			
			CSBuilder builder = new CSBuilder(contentStream);
			
			builder.addSection(contentSection -> this.title(contentStream));
			builder.addSection(contentSection -> this.invoiceRefInfo(contentStream, invoiceNumber, invoiceDate));
			builder.addSection(contentSection -> this.companyInfo(contentStream));
			builder.addSection(contentSection -> this.clientInfo(contentStream));
			builder.addSection(contentSection -> this.unitDetails(contentStream));
			builder.addSection(contentSection -> this.bankInfo(contentStream));
			
			contentStream.close();
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			doc.save(baos);
			doc.close();
			
			byteArrayResource = new ByteArrayResource(baos.toByteArray());
			
			
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
		return byteArrayResource;
		
	}
	
	private void title(PDPageContentStream contentStream) {
		
		try {
			contentStream.beginText();
			contentStream.newLineAtOffset(200, setPos(0));
			contentStream.setFont(FONT_HDR, 36);
			contentStream.showText("Arenella B.V");
			contentStream.endText();
			setPos(40);
		
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	private void invoiceRefInfo(PDPageContentStream contentStream, String invoiceNumber, Optional<LocalDate> invoiceDate) {
		addLineWithLabel(contentStream, "Reference", invoiceNumber);
		addLineWithLabel(contentStream, "Invoice Date", invoiceDate.isEmpty() ? LocalDate.now().toString() : invoiceDate.get().toString());
	}
	
	private void companyInfo(PDPageContentStream contentStream) {
		addSectionHeader(contentStream, "Arenella B.V");
		addLineWithLabel(contentStream, "Address", "Julianastraat 16, Noordwijk, 2202KD, Nederland");
		addLineWithLabel(contentStream, "KVK Nummer", "61982156");
		addLineWithLabel(contentStream, "BTW Nummer", "NL854578493");
	}
	
	private void clientInfo(PDPageContentStream contentStream) {
		addSectionHeader(contentStream, "Fake Customer Name");
		addLineWithLabel(contentStream, "Address", "12 boop street, mookie 12345KD, BoopLand");
		addLineWithLabel(contentStream, "KVK Nummer", "2433434-588");
		addLineWithLabel(contentStream, "BTW Nummer", "BE-23234-AA");
	}
	
	private void unitDetails(PDPageContentStream contentStream) {
		addSectionHeader(contentStream, "Description");
		addLineWithLabel(contentStream, "Arenella-ICT 1 Month subscription", "");
		addLineWithLabel(contentStream, "Price ( Euros ) ", "10");
		addLineWithLabel(contentStream, "BTW ( Euros ) 21%", "2.1");
		addLineWithLabel(contentStream, "Total to pay ( Euros )", "12.1");
	}

	private void bankInfo(PDPageContentStream contentStream) {
		addSectionHeader(contentStream, "Banking Information");
		addLineWithLabel(contentStream, "Bank name", "ABN Amro");
		addLineWithLabel(contentStream, "Payable to", "Arenella B.V");
		addLineWithLabel(contentStream, "IBAN", "NL75 ABNA 0501 0319 52");
	}
	
	public class CSBuilder {
		
		final PDPageContentStream contentStream;
		
		public CSBuilder(PDPageContentStream contentStream){
			this.contentStream 	= contentStream;
		}
		
		public CSBuilder addSection(ContentSection contentSection) {
			contentSection.apply(contentStream);
			return this;
		}
		
	}
	
	interface ContentSection{
		void apply(PDPageContentStream contentStream);
	}
	
	
	protected float setPos(float height) {
		return this.currentPos = this.currentPos - height;
	}
	
	public void addSectionHeader(PDPageContentStream contentStream, String text) {
		try {
			contentStream.beginText();
			contentStream.newLineAtOffset(LEFT_MARGIN, setPos(SECTION_MARGIN_TOP)); 
			contentStream.setFont(FONT_HDR, 24);
			contentStream.showText(text);
		contentStream.endText();
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void addLineWithLabel(PDPageContentStream contentStream, String label, String text) {
		try {
			contentStream.beginText();
			contentStream.newLineAtOffset(LEFT_MARGIN, setPos(LINE_HEIGHT));
			contentStream.setFont(FONT_HDR, 12);
			contentStream.showText(label + ": ");
			contentStream.setFont(FONT_TXT, 12);
			contentStream.showText(text);
			contentStream.endText();
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	
}