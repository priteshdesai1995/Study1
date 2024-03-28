package com.base.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.base.api.constants.Constants;
import com.google.common.collect.Lists;

import springfox.documentation.builders.ApiInfoBuilder;
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
				.apis(RequestHandlerSelectors.basePackage("com.base.api")).build().apiInfo(apiInfo())
				.securitySchemes(Lists.newArrayList(authTokenScheme()));
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Java Base API")
				.description("This set of APIs is used for Java Base Skaleton").version("1.0").build();
	}

	private ApiKey authTokenScheme() {
		return new ApiKey(Constants.AUTHORIZATION_HEADER_KEY, Constants.AUTHORIZATION_HEADER_KEY, "header");
	}
}
