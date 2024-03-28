package com.base.api.database.seeders;

import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import com.base.api.annotations.DatabaseSeeder;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class SeederConfig {

	@Autowired
	private ApplicationContext context;

	@Value("${database.seeder.enable:false}")
	private Boolean enable;

	@PostConstruct
	public void init() {

		if (!enable)
			return;

		log.info("========================= {} =========================", "Database Seeder Start");
		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);

		scanner.addIncludeFilter(new AnnotationTypeFilter(DatabaseSeeder.class));

		Set<BeanDefinition> definationSet = scanner.findCandidateComponents("com.base.api.database.seeders");

		for (BeanDefinition bean : definationSet) {
			final String[] namePath = bean.getBeanClassName().split("\\.");
			if (namePath.length > 0) {
				String beanName = namePath[namePath.length - 1];
				log.info("========================= Seed: {} : Start =========================", beanName);
				try {
					BaseSeeder seeder = (BaseSeeder) this.context.getBean(beanName);
					seeder.seed();
				} catch (Exception e) {
					log.error("========================= Seed: {} : Falied => Reason: {} =========================",
							beanName, e.getMessage());
				}
				log.info("========================= Seed: {} : End =========================", beanName);
			}
		}

		log.info("========================= {} =========================", "Database Seeder End");
	}
}
