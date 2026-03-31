package com.arenella.recruit.campaigns.controllers;

import com.arenella.recruit.campaigns.beans.Document.DocumentType;

/**
* Command to create new Docuemnt. This contains the metadata. The bytes are 
* uploaded separately 
*/
public record AddDocumentAPIInbound(String title, DocumentType type) {

}
