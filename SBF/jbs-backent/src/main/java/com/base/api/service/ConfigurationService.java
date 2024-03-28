package com.base.api.service;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.base.api.entities.Configuration;
import com.base.api.entities.Logo;

public interface ConfigurationService {
	List<Configuration> getConfigurations();

	Logo getLogo();
	
	List<Logo> getSiteLogo(String siteName);

	String updateConfiguration(Map<String, String> request);

}
