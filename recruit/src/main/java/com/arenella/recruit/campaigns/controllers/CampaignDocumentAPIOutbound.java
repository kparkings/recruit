package com.arenella.recruit.campaigns.controllers;

import java.time.LocalDateTime;

import com.arenella.recruit.campaigns.beans.Document.DocumentType;

/**
* Represents a Document associated with either a Campaign or Role
* @param title 			- Human readable name for the document
* @param documentType 	- Type of the Document
* @param bytes 			- File bytes
* @param created		- Date/Time the document was added
*/
public record CampaignDocumentAPIOutbound(String title, DocumentType type, byte[] bytes, LocalDateTime created) {

}
