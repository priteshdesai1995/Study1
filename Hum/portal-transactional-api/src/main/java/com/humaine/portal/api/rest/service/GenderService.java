package com.humaine.portal.api.rest.service;

import java.util.List;

import com.humaine.portal.api.model.Gender;

public interface GenderService {

	List<Gender> getGenderList();

	Gender getEmptyGenderValue();
}
