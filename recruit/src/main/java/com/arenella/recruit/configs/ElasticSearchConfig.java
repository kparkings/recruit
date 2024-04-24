package com.arenella.recruit.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;


@Configuration
@EnableElasticsearchRepositories(basePackages = "github.io.truongbn.elasticsearch.repository")
public class ElasticSearchConfig extends ElasticsearchConfiguration {

	@Value("${elasticsearch-url}")
	private String url;
	
	@Value("${elasticsearch-use-ssl}")
	private boolean useSSL;
	
	/**
	* Configure connection to Elasticsearch 
	*/
	@Override
    public ClientConfiguration clientConfiguration() {
        
		if (!useSSL) {
			return ClientConfiguration
						.builder()
							.connectedTo(url)
						.build();
		} else {
			return ClientConfiguration
					.builder()
						.connectedTo(url)
						//.withBasicAuth(url, url)
						.usingSsl()
						
					.build();
		}
	
	}
}
