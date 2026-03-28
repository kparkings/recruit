package com.arenella.recruit.campaigns.beans;

import java.time.LocalDateTime;

/**
* Represents a Document associated with either a Campaign or Role
* @param title 			- Human readable name for the document
* @param documentType 	- Type of the Document
* @param bytes 			- File bytes
* @param created		- Date/Time the document was added
*/
public record Document(String title, DocumentType type, byte[] bytes, LocalDateTime created) {
	
	public enum DocumentType {DOC,PDF}
	
}
