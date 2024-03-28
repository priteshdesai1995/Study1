package com.humaine.portal.api.rest.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.humaine.portal.api.model.Gender;
import com.humaine.portal.api.rest.repository.GenderRepository;
import com.humaine.portal.api.rest.service.GenderService;

@Service
public class GenderServiceImpl implements GenderService {

	@Value("${attributes.empty.value}")
	String emptyValue;

	@Autowired
	private GenderRepository genderRepository;

	@Override
	public List<Gender> getGenderList() {
		return genderRepository.getGenderList(emptyValue);
	}

	@Override
	public Gender getEmptyGenderValue() {
		return genderRepository.getGenderEmptyValue(emptyValue);
	}

}
