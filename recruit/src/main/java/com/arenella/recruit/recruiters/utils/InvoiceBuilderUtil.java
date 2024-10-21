package com.arenella.recruit.recruiters.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import com.arenella.recruit.recruiters.beans.Recruiter;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription;

/**
* Utility to build an Invoice for a Recruiter Subscription
* @author K Parkings
*/
@Component
@RequestScope
public class InvoiceBuilderUtil {

	private static final PDFont FONT_HDR 				= PDType1Font.HELVETICA_BOLD;
	private static final PDFont FONT_TXT 				= PDType1Font.HELVETICA;
	private static final float 	SECTION_MARGIN_TOP 		= 40;
	private static final float 	LINE_HEIGHT 			= 20;
	private static final float 	LEFT_MARGIN 			= 100;
	private static final float 	TOP_POS 				= 740;
	private static final float 	FONT_SIZE_TXT 			= 12;
	private static final float 	FONT_SIZE_HDR 			= 24;
	
	private float 					currentPos = TOP_POS;
	private Recruiter 				recruiter;
	private RecruiterSubscription 	subscription;
	private Optional<String>		unitDescription;
	private Optional<Boolean>		btwApplies;
	
	/**
	* Generates a PDF invoice for the Subscription
	* @param subscriptionId		- Id of the Subscription to implement the Subscription for
	* @param invoiceNumber		- Id to give to the invoice
	* @param invoiceDate		- Date of invoice
	* @param unitDescription	- Unit of work being invoiced description
	* @return Invoice 
	*/
	public ByteArrayResource generateInvoice(Recruiter recruiter, RecruiterSubscription subscription, String invoiceNumber, Optional<Boolean> btwApplies, Optional<LocalDate> invoiceDate, Optional<String> unitDescription) {
		
		ByteArrayResource byteArrayResource = null;
		this.recruiter 			= recruiter;
		this.subscription 		= subscription;
		this.unitDescription	= unitDescription;
		this.btwApplies 		= btwApplies;
		
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
	
	/**
	* Invoice Header Sections
	* @param contentStream - Stream to add Sections to
	*/
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
	
	/**
	* Invoice Reference Section
	* @param contentStream - Stream to add Sections to
	* @param invoiceNumber
	* @param invoiceDate
	*/
	private void invoiceRefInfo(PDPageContentStream contentStream, String invoiceNumber, Optional<LocalDate> invoiceDate) {
		addLineWithLabel(contentStream, "Reference", invoiceNumber);
		addLineWithLabel(contentStream, "Invoice Date", invoiceDate.isEmpty() ? LocalDate.now().toString() : invoiceDate.get().toString());
	}
	
	/**
	* Company Info Section ( Arenella BV )
	* @param contentStream - Stream to add Sections to
	*/
	private void companyInfo(PDPageContentStream contentStream) {
		addSectionHeader(contentStream, "Arenella B.V");
		addLineWithLabel(contentStream, "Address", "Julianastraat 16, Noordwijk, 2202KD, Nederland");
		addLineWithLabel(contentStream, "KVK Nummer", "61982156");
		addLineWithLabel(contentStream, "BTW Nummer", "NL854578493");
	}
	
	/**
	*  Company Info Section ( Customer )
	* @param contentStream - Stream to add Sections to
	*/
	private void clientInfo(PDPageContentStream contentStream) {
		addSectionHeader(contentStream, this.recruiter.getCompanyName());
		addLineWithLabel(contentStream, "Address", 		this.recruiter.getCompanyAddress());
		addLineWithLabel(contentStream, "KVK Nummer", 	this.recruiter.getCompanyRegistrationNumber());
		addLineWithLabel(contentStream, "BTW Nummer", 	this.recruiter.getCompanyVatNumber());
	}
	
	/**
	* Unit details section
	* @param contentStream - Stream to add Sections to
	*/
	private void unitDetails(PDPageContentStream contentStream) {
		
		boolean btwApplies = this.btwApplies.isEmpty() ? false : this.btwApplies.get();
		SubscriptionSummary summary = new SubscriptionSummary(this.subscription, btwApplies,this.unitDescription);
		
		addSectionHeader(contentStream, "Description");
		addLineWithLabel(contentStream, summary.getDescription(), "");
		addLineWithLabel(contentStream, "Price ( Euros )",		summary.getPrice());
		addLineWithLabel(contentStream, summary.getBtwLabel(), 		summary.getBtwPrice());
		addLineWithLabel(contentStream, "Total to pay ( Euros )", summary.getTotal());
	}
	
	/**
	* Class constructs the text to use for the 
	* part of the Invoice that describes the 
	* service and costs
	* @author K Parkings
	*/
	private class SubscriptionSummary{
		
		private float 	price;
		private String 	description;
		private String 	btwLabelNL		= "BTW ( Euros ) 21%";
		private String 	btwLabelNonNL	= "BTW NOT APPLICABLE";
		private float 	btwPrice	 	= 0f;
		private boolean btwApplies;
		
		/**
		* Returns text for description
		* @return description of service
		*/
		public String getDescription() {
			return this.description;
		}
		
		/**
		* Returns the price to pay
		* @return price of the service
		*/
		public String getPrice() {
			return ""+this.price;
		}
		
		/**
		* Reutns text for lable of price to pay
		* @return label for price
		*/
		public String getBtwLabel() {
			return btwApplies ? this.btwLabelNL : this.btwLabelNonNL;
		}
		
		/**
		* Returns the amount of BTW tax to be paid
		* @return BTW tax
		*/
		public String getBtwPrice() {
			return btwApplies ? String.format("%.2f",btwPrice) : "";
		}
		
		/**
		* Returns the total price to be paid
		* @return total price for service being invoiced
		*/
		public String getTotal() {
			return String.format("%.2f", this.price + this.btwPrice);
		}
	
		/**
		* Construcor 
		* @param subscription		- Subscription being invoiced
		* @param btwApplies			- Whether or not BTW needs to be added
		* @param unitDescription	- Description of the service being invoiced
		 */
		public SubscriptionSummary(RecruiterSubscription subscription, boolean btwApplies, Optional<String> unitDescription) {
			
			this.btwApplies = btwApplies;
			
			switch(subscription.getType()) {
				case ONE_MONTH_SUBSCRIPTION:{
					this.price =  10f;
					this.description 	= "Arenella-ICT 1 month subscription";
					break;
				}
				case THREE_MONTHS_SUBSCRIPTION:{
					this.price =  30f;
					this.description = "Arenella-ICT 3 month subscription";
					break;
				}
				case SIX_MONTHS_SUBSCRIPTION:{
					this.price =  60f;
					this.description = "Arenella-ICT 6 month subscription";
					break;
				}
				case YEAR_SUBSCRIPTION:{
					this.price =  120f;
					this.description = "Arenella-ICT 1 year subscription";
					break;
				}
				default:{
					throw new IllegalArgumentException("Cannot invoice for this subscription type " + subscription.getType());
				}
			
			}
			
			if (unitDescription.isPresent()) {
				this.description = unitDescription.get();
			}
			
			this.btwPrice = (btwApplies ? ((this.price / 100) * 21) :  0f);
		}
	}
	
	

	/**
	* Banking Info Section
	* @param contentStream - Stream to add Sections to
	*/
	private void bankInfo(PDPageContentStream contentStream) {
		addSectionHeader(contentStream, "Banking Information");
		addLineWithLabel(contentStream, "Bank name", "ABN Amro");
		addLineWithLabel(contentStream, "Payable to", "Arenella B.V");
		addLineWithLabel(contentStream, "IBAN", "NL75 ABNA 0501 0319 52");
	}
	
	/**
	* Builder to add ContentSections together to form the 
	* complete invoice
	* @author Hp
	*/
	public class CSBuilder {
		
		final PDPageContentStream contentStream;
		
		/**
		* Constructor
		* @param contentStream - Stream to add Sections to
		*/
		public CSBuilder(PDPageContentStream contentStream){
			this.contentStream 	= contentStream;
		}
		
		/**
		* Adds Section to Invoice
		* @param contentSection - Section to add to the Invoice
		* @return Builder
		*/
		public CSBuilder addSection(ContentSection contentSection) {
			contentSection.apply(contentStream);
			return this;
		}
		
	}
	
	/**
	* Functional Interface allows methods accepting a contentStream argument 
	* to be passed to the builder
	* @author K Parkings
	*/
	@FunctionalInterface
	interface ContentSection{
		void apply(PDPageContentStream contentStream);
	}
	
	
	/**
	* Sets the next top position in the PDF taking into account
	* the previous current top position and the height of the 
	* section/line just added
	* @param height - Height to increase the current position by
	* @return Where to write the next line
	*/
	protected float setPos(float height) {
		this.currentPos = this.currentPos - height;
		return this.currentPos;
	}
	
	/**
	* Utility method to allow a Section header to be added without having 
	* to add the boilerplate code each time
	* @param contentStream - Stream to add text to
	* @param text - Text to add to the invloice
	*/
	private void addSectionHeader(PDPageContentStream contentStream, String text) {
		try {
			contentStream.beginText();
			contentStream.newLineAtOffset(LEFT_MARGIN, setPos(SECTION_MARGIN_TOP)); 
			contentStream.setFont(FONT_HDR, FONT_SIZE_HDR);
			contentStream.showText(text);
		contentStream.endText();
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	* Utility method to allow a line of text with a lable to be added without having 
	* to add the boilerplate code each time
	* @param contentStream - Stream to add text to
	* @param label - Label to add to the invloice
	* @param text - Text to add to the invloice
	*/
	private void addLineWithLabel(PDPageContentStream contentStream, String label, String text) {
		try {
			contentStream.beginText();
			contentStream.newLineAtOffset(LEFT_MARGIN, setPos(LINE_HEIGHT));
			contentStream.setFont(FONT_HDR, FONT_SIZE_TXT);
			contentStream.showText(label + ": ");
			contentStream.setFont(FONT_TXT, FONT_SIZE_TXT);
			contentStream.showText(text.replace("\n", " ").replace("\r", " "));
			contentStream.endText();
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}