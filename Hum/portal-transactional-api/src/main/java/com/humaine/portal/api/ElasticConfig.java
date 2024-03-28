package com.humaine.portal.api;

import java.time.Duration;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ElasticConfig {

	// timeout in seconds
	private static final Long timeout = 10L;

	@Value("${elasticsearch.host}")
	private String EsHost;

	@Value("${elasticsearch.port}")
	private String EsPort;

	@Value("${elasticsearch.rest.username}")
	private String username;

	@Value("${elasticsearch.rest.password}")
	private String password;

	@Bean
	public RestHighLevelClient client() {
		ClientConfiguration clientConfiguration = ClientConfiguration.builder().connectedTo(EsHost + ":" + EsPort)
				.usingSsl()
				.withConnectTimeout(timeout * 1000)
				.withSocketTimeout(timeout * 1000)
				.build();
		return RestClients.create(clientConfiguration).rest();
	}

	@Bean
	public ElasticsearchOperations elasticsearchTemplate() {
		return new ElasticsearchRestTemplate(client());
	}
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
	 
	    return builder
	            .setConnectTimeout(Duration.ofMillis(1000 * timeout))
	            .setReadTimeout(Duration.ofMillis(1000 * timeout))
	            .build();
	}
}
