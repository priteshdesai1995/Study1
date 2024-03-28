package com.humaine.portal.api.rest.service;

import java.util.List;

import com.humaine.portal.api.model.Ethnicity;

public interface EthnicityService {

	List<Ethnicity> getEthnicityList();
	
	Ethnicity getEmptyEthnicityValue();
}
