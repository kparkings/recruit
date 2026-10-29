package com.arenella.recruit.campaigns.controllers;

import java.time.LocalDateTime;
import java.util.UUID;

import com.arenella.recruit.campaigns.beans.Document.DocumentType;

/**
* API Outbound representation of a Document. Doesn't contain the bytes to prevent 
* slow fetches until the file actually needs to be opened
*/
public record DocumentAPIOutbound(UUID id, String title, DocumentType type, LocalDateTime created) {

}
