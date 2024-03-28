package com.humaine.portal.api.config;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.humaine.portal.api.util.JPAPropertiesUtility;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(transactionManagerRef = "olapTransactionManager", entityManagerFactoryRef = "olapEntityManagerFactory", basePackages = "com.humaine.portal.api.rest.olap.repository")
public class OLAPDBConfig {

	@Bean
	@ConfigurationProperties("spring.olap.datasource")
	public HikariDataSource olapDataSource() {
		return DataSourceBuilder.create().type(HikariDataSource.class).build();
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean olapEntityManagerFactory(EntityManagerFactoryBuilder builder) {
		return builder.dataSource(olapDataSource()).packages("com.humaine.portal.api.olap.model").persistenceUnit("olap").build();
	}

	@Bean
	public PlatformTransactionManager olapTransactionManager(
			@Qualifier("olapEntityManagerFactory") EntityManagerFactory olapEntityManagerFactory) {
		return new JpaTransactionManager(olapEntityManagerFactory);
	}
}