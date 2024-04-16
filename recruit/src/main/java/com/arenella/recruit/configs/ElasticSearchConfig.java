package com.arenella.recruit.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

//import java.time.Duration;

//import org.apache.http.HttpHost;
//import org.elasticsearch.client.RestClient;
//import org.elasticsearch.client.RestHighLevelClient;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.elasticsearch.client.ClientConfiguration;
//import org.springframework.data.elasticsearch.client.RestClients;
//import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
///import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

//import co.elastic.clients.json.jackson.JacksonJsonpMapper;
//import co.elastic.clients.transport.ElasticsearchTransport;
//import co.elastic.clients.elasticsearch.ElasticsearchClient;
//import co.elastic.clients.transport.rest_client.RestClientTransport;
//import org.apache.http.message.BasicHeader;
//import org.apache.http.Header;


@Configuration
@EnableElasticsearchRepositories(basePackages = "github.io.truongbn.elasticsearch.repository")
public class ElasticSearchConfig extends ElasticsearchConfiguration {
    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo("localhost:9200").build();
    }
}

/**
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
	
	
	
	

}*/

//@Configuration 
//public class ElasticSearchConfig{
	
	
	// URL and API key
	//String serverUrl = "https://localhost:9200";
	//String apiKey = "3bIw8LqDZfwMm7W4NtXG";

	// Create the low-level client
	//RestClient restClient = RestClient
	 //   .builder(HttpHost.create(serverUrl))
	 //   .setDefaultHeaders(new Header[]{
	 //       new BasicHeader("Authorization", "ApiKey " + apiKey)
	 //   })
	 //   .build();

	// Create the transport with a Jackson mapper
	//ElasticsearchTransport transport = new RestClientTransport(
	//    restClient, new JacksonJsonpMapper());

	// And create the API client
	//ElasticsearchClient esClient = new ElasticsearchClient(transport);
//}
