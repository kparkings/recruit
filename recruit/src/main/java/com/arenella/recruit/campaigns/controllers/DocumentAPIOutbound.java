package com.arenella.recruit.campaigns.controllers;

import java.time.LocalDateTime;

import com.arenella.recruit.campaigns.beans.Document.DocumentType;

/**
* API Outbound representation of a Document. Doesn't contain the bytes to prevent 
* slow fetches until the file actually needs to be opened
*/
public record DocumentAPIOutbound(String title, DocumentType type, LocalDateTime created) {

}
