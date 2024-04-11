package com.arenella.recruit.candidates.entities;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "candidates")
public class LanguageDocument {

	@Field(type = FieldType.Keyword, name="language")
	private String		language;

	@Field(type = FieldType.Keyword, name="level")
	private String		level;
}
