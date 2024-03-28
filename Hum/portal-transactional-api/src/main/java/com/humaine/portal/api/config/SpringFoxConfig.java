package com.humaine.portal.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Lists;
import com.humaine.portal.api.security.config.AWSConfig;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SpringFoxConfig {

	@Autowired
	private AWSConfig awsConfig;

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.humaine.portal.api.rest.controller"))
				.paths(PathSelectors.any()).build().apiInfo(apiInfo())
				.securitySchemes(Lists.newArrayList(authTokenScheme()));
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Humaine Transactional API")
				.description("This set of APIs is used for user tranaactions onHumane Portal").version("1.0").build();
	}

	private ApiKey authTokenScheme() {
		return new ApiKey(awsConfig.getHttpHeader(), awsConfig.getHttpHeader(), "header");
	}

}
