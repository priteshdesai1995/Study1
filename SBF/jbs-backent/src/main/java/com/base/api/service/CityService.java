package com.base.api.service;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.base.api.filter.CityFilter;
import com.base.api.request.dto.CityDTO;

public interface CityService {
	String createCity(@Valid CityDTO cityDto);

	List<Map<String, Object>> searchCity(CityFilter cityFilter);

	int getTotalCount(CityFilter stateFilter);

	String updateCity(@Valid CityDTO cityDto, String id);

	CityDTO getCityByUuid(String cityUuid);

	String changeStatus(String uuid, String status);

	String deleteCity(String cityId);
}
