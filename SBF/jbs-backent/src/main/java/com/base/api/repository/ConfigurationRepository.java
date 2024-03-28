package com.base.api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.base.api.entities.Configuration;

public interface ConfigurationRepository extends JpaRepository<Configuration, UUID> {
	Configuration findByOptionName(String optionName);
}
