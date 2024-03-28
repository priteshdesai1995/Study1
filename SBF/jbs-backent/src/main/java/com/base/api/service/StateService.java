package com.base.api.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import com.base.api.entities.Country;
import com.base.api.entities.State;
import com.base.api.filter.StateFilter;
import com.base.api.request.dto.StateDTO;
import com.base.api.response.dto.StateTranslableDto;

public interface StateService {
	String createState(@Valid StateDTO stateDTO);

	StateDTO getStateById(UUID id);

	List<Map<String, Object>> searchState(StateFilter stateFilter);

	int getTotalCount(StateFilter stateFilter);

	String updateState(StateDTO stateDTO, String uuid);

	String changeStatus(UUID uuid, String status);

	List<StateTranslableDto> getAllStates(UUID countryUuid);

	Country getCountryById(UUID uuid);

	State getStateByTransUuid(UUID uuid);

	String deleteState(UUID stateId);

	State getState(UUID uuid);
}
