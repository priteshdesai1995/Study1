package com.humaine.portal.api.config;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.humaine.portal.api.util.JPAPropertiesUtility;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(transactionManagerRef = "portalTransactionManager", entityManagerFactoryRef = "portalEntityManagerFactory", basePackages = "com.humaine.portal.api.rest.repository")
public class PortalDBConfig {

	@Primary
	@Bean
	@ConfigurationProperties("spring.datasource")
	public HikariDataSource portalDataSource() {
		return DataSourceBuilder.create().type(HikariDataSource.class).build();
	}

	@Primary
	@Bean
	public LocalContainerEntityManagerFactoryBean portalEntityManagerFactory(EntityManagerFactoryBuilder builder) {
		return builder.dataSource(portalDataSource()).packages("com.humaine.portal.api.model")
				.properties(JPAPropertiesUtility.getProperties(true, true)).persistenceUnit("portal").build();
	}

	@Primary
	@Bean
	public PlatformTransactionManager portalTransactionManager(
			@Qualifier("portalEntityManagerFactory") EntityManagerFactory portalEntityManagerFactory) {
		return new JpaTransactionManager(portalEntityManagerFactory);
	}
}