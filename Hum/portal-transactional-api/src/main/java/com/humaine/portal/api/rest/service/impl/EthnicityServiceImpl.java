package com.humaine.portal.api.rest.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.humaine.portal.api.model.Ethnicity;
import com.humaine.portal.api.rest.repository.EthnicityRepository;
import com.humaine.portal.api.rest.service.EthnicityService;

@Service
public class EthnicityServiceImpl implements EthnicityService {

	@Value("${attributes.empty.value}")
	String emptyValue;

	@Autowired
	private EthnicityRepository ethnicityRepository;

	@Override
	public List<Ethnicity> getEthnicityList() {
		return ethnicityRepository.getEthnicityList(emptyValue);
	}

	@Override
	public Ethnicity getEmptyEthnicityValue() {
		return ethnicityRepository.getEthnicityEmptyValue(emptyValue);
	}

}
