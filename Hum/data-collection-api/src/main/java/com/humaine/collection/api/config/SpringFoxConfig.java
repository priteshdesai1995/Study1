package com.humaine.collection.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Lists;
import com.humaine.collection.api.security.filter.CustomAuthenticationFilter;

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

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.humaine.collection.api.rest.controller"))
				.paths(PathSelectors.any()).build().apiInfo(apiInfo())
				.securitySchemes(Lists.newArrayList(authTokenScheme()));
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Humaine Data Collection API")
				.description("This set of APIs is used for user evnt data collection across E-comm websites")
				.version("1.0").build();
	}

	private ApiKey authTokenScheme() {
		return new ApiKey(CustomAuthenticationFilter.HEADER, CustomAuthenticationFilter.HEADER, "header");
	}
}
