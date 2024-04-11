package com.arenella.recruit.configs;

import java.time.Duration;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.arenella.recruit.candidates.repos")
@ComponentScan(basePackages = { "com.arenella.recruit.candidates.repos" })
public class ElasticSearchConfig extends AbstractElasticsearchConfiguration{

	@Bean
	@Override
	public RestHighLevelClient elasticsearchClient() {
		final ClientConfiguration clientConfiguration = 
			    ClientConfiguration
			      .builder()
			     
			      //.connectedTo("https://localhost:9200")
			      .connectedTo("localhost:9200")
			      //.withBasicAuth("elastic", "3bIw8LqDZfwMm7W4NtXG")
			      .withConnectTimeout(Duration.ofSeconds(5))
	                .withSocketTimeout(Duration.ofSeconds(3))
			      .build();

			  return RestClients.create(clientConfiguration).rest();
	}

}
