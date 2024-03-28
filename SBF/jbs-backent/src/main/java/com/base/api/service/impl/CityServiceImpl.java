package com.base.api.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TupleElement;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.base.api.entities.City;
import com.base.api.entities.CityTranslation;
import com.base.api.entities.Country;
import com.base.api.entities.CountryTranslation;
import com.base.api.entities.State;
import com.base.api.entities.StateTranslation;
import com.base.api.filter.CityFilter;
import com.base.api.filter.FilterBase;
import com.base.api.repository.CityRepository;
import com.base.api.repository.CityTranslableRepository;
import com.base.api.repository.CountryRepository;
import com.base.api.repository.CountryTranslationRepository;
import com.base.api.repository.StateRepository;
import com.base.api.repository.StateTranslationRepository;
import com.base.api.request.dto.CityDTO;
import com.base.api.request.dto.CityTranslationDTO;
import com.base.api.service.CityService;
import com.base.api.service.StateService;
import com.base.api.utils.Util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CityServiceImpl implements CityService {
	@Autowired
	StateService stateService;

	@Autowired
	StateRepository stateRepository;

	@Autowired
	StateTranslationRepository stateTranslationRepository;

	@Autowired
	CityTranslableRepository cityTranslableRepository;

	@Autowired
	CityRepository cityRepository;

	@Autowired
	EntityManager entityManager;

	@Autowired
	CountryRepository countryRepository;

	@Autowired
	CountryTranslationRepository countryTranslationRepository;

	/**
	 * Creates the city.
	 *
	 * @param cityDto the city dto
	 * @return the string
	 */
	@Override
	public String createCity(@Valid CityDTO cityDto) {
		try {
			City cityEntity = new City();
			String uuid = Util.generateUUID();
			cityEntity = mapDTOToEntity(cityDto, cityEntity);
			List<CityTranslation> cityTranslations = new ArrayList<CityTranslation>();
			cityTranslations = mapCityTranslation(cityDto.getCityTranslableDtos(), "CREATE", uuid);
			List<CityTranslation> translations = new ArrayList<CityTranslation>();
			translations = cityTranslableRepository.saveAll(cityTranslations);
			cityEntity.setCities(translations);
			cityEntity.setCreateDate(Util.getCurrentUtcTime());
			cityEntity.setUpdatedAt(Util.getCurrentUtcTime());
			cityEntity.setUuid(uuid);
			cityRepository.save(cityEntity);
			return HttpStatus.OK.name();
		} catch (DataIntegrityViolationException e) {
			return HttpStatus.INTERNAL_SERVER_ERROR.name();
		}
	}

	/**
	 * Map city translation.
	 *
	 * @param cityTranslableDtos the city translable dtos
	 * @param operationType the operation type
	 * @param uuid the uuid
	 * @return the list
	 */
	private List<CityTranslation> mapCityTranslation(List<CityTranslationDTO> cityTranslableDtos, String operationType,
			String uuid) {
		List<CityTranslation> cityTranslations = new ArrayList<CityTranslation>();
		cityTranslableDtos.forEach((dto) -> {
			CityTranslation cityTranslation = new CityTranslation();
			cityTranslation.setLocale(dto.getLocale());
			cityTranslation.setName(dto.getName());
			if (operationType.equalsIgnoreCase("CREATE")) {
				cityTranslation.setUuid(uuid);
			}
			cityTranslations.add(cityTranslation);
		});
		return cityTranslations;
	}

	/**
	 * Map DTO to entity.
	 *
	 * @param cityDto the city dto
	 * @param cityEntity the city entity
	 * @return the city
	 */
	private City mapDTOToEntity(@Valid CityDTO cityDto, City cityEntity) {
		State stateEntity = new State();
		Country countryEntity = new Country();
		if (cityDto.getCountryUuid() != null) {
			CountryTranslation countryTranslation = countryTranslationRepository
					.getById(UUID.fromString(cityDto.getCountryUuid()));
			String uuid = countryTranslation.getUuid();
			Optional<Country> country = countryRepository.findByUuid(uuid);
			countryEntity = stateService.getCountryById(country.get().getId());
			if (countryEntity != null) {
				cityEntity.setCountry(countryEntity);
			}
		}

		if (cityDto.getStateUuid() != null) {
			State state = stateRepository.getById(UUID.fromString(cityDto.getStateUuid()));
			stateEntity = stateService.getStateByTransUuid(state.getId());
			if (stateEntity != null) {
				cityEntity.setState(stateEntity);
			}
		}
		cityEntity.setStatus(cityDto.getStatus());
		return cityEntity;
	}

	/**
	 * Search city.
	 *
	 * @param filter the filter
	 * @return the list
	 */
	@Override
	public List<Map<String, Object>> searchCity(CityFilter filter) {
		StringBuilder query = new StringBuilder();
		query = createCommonQuery();
		if (filter.getStatus() != null && !filter.getStatus().isEmpty()) {
			query.append(" and u.status LIKE '" + filter.getStatus() + "' ");
		}
		if (filter.getStateName() != null && !filter.getStateName().isEmpty()) {
			query.append(" and UPPER(st.name) LIKE UPPER('%" + filter.getStateName() + "%')");
		}
		if (filter.getCityName() != null && !filter.getCityName().isEmpty()) {
			query.append(" and UPPER(ct.name) LIKE UPPER('%" + filter.getCityName() + "%')");
		}
		FilterBase filterBase = new FilterBase();
		filterBase.setColumnName(filter.getColumnName());
		filterBase.setStartRec(filter.getStartRec());
		filterBase.setEndRec(filter.getEndRec());
		filterBase.setSortingOrder(filter.getSortingOrder());
		filterBase.setOrder(filter.getOrder());
		String queryParam = Util.getFilterNativeQuery(filterBase, query.toString());
		List<Tuple> rows = new ArrayList<Tuple>();
		Query natQuery = entityManager.createNativeQuery(queryParam, Tuple.class);
		rows = natQuery.getResultList();
		List<Map<String, Object>> result = convertTuplesToMap(rows);
		return result;
	}

	/**
	 * Gets the total count.
	 *
	 * @param stateFilter the state filter
	 * @return the total count
	 */
	@Override
	public int getTotalCount(CityFilter stateFilter) {
		StringBuilder query = new StringBuilder();
		query = createCommonQuery();
		List<Tuple> rows = new ArrayList<Tuple>();
		Query natQuery = entityManager.createNativeQuery(query.toString(), Tuple.class);
		rows = natQuery.getResultList();
		return rows.size();
	}

	/**
	 * Creates the common query.
	 *
	 * @return the string builder
	 */
	private StringBuilder createCommonQuery() {
		StringBuilder query = new StringBuilder();
		query.append(
				"select ct.name as city_name ,st.name as state_name,u.created_date, u.updated_date ,u.status, Cast(u.id as varchar), u.uuid from cities u  inner join city_translable ct on Cast(u.id as varchar) = Cast(ct.city_id as varchar) inner join state_translable  st on Cast(st.state_id as varchar) = Cast(u.state_id as varchar)");
		query.append(" where st.locale LIKE 'en' and ct.locale LIKE 'en'");
		return query;
	}

	/**
	 * Convert tuples to map.
	 *
	 * @param tuples the tuples
	 * @return the list
	 */
	public static List<Map<String, Object>> convertTuplesToMap(List<Tuple> tuples) {
		List<Map<String, Object>> result = new ArrayList<>();
		for (Tuple single : tuples) {
			Map<String, Object> tempMap = new HashMap<>();
			for (TupleElement<?> key : single.getElements()) {
				tempMap.put(key.getAlias(), single.get(key));
			}
			result.add(tempMap);
		}
		return result;
	}

	/**
	 * Update city.
	 *
	 * @param cityDto the city dto
	 * @param id the id
	 * @return the string
	 */
	@Override
	public String updateCity(@Valid CityDTO cityDto, String id) {

		try {
			City city = cityRepository.findByUuid(id);

			if (city == null)
				return HttpStatus.NOT_FOUND.name();

			city = mapDTOToEntity(cityDto, city);
			List<CityTranslation> cityTranslations = new ArrayList<CityTranslation>();
			cityDto.getCityTranslableDtos().forEach((cityTranslationdto) -> {
				CityTranslation translation = new CityTranslation();
				translation = cityTranslableRepository.findByUuidAndLocale(cityTranslationdto.getUuid(), cityTranslationdto.getLocale());
				if (translation != null && translation.getUuid() != null) {
					translation.setName(cityTranslationdto.getName());
					cityTranslations.add(translation);
				}
			});
			List<CityTranslation> savedTranslations = new ArrayList<CityTranslation>();
			savedTranslations = cityTranslableRepository.saveAll(cityTranslations);
			city.setCities(savedTranslations);
			city.setUpdatedAt(Util.getCurrentUtcTime());
			cityRepository.save(city);
			return HttpStatus.OK.name();
		} catch (DataIntegrityViolationException e) {
			return HttpStatus.INTERNAL_SERVER_ERROR.name();
		}
	}

	/**
	 * Gets the city by uuid.
	 *
	 * @param uuid the uuid
	 * @return the city by uuid
	 */
	@Override
	public CityDTO getCityByUuid(String uuid) {
		CityDTO cityDTO = new CityDTO();
		City cityEntity = new City();
		cityEntity = cityRepository.findByUuid(uuid);
		if (cityEntity != null) {
			Optional<CountryTranslation> counTrans = Optional.empty();
			counTrans = cityEntity.getCountry().getCountries().stream()
					.filter(element -> element.getLocale().equals("en")).findFirst();
			cityDTO.setCountryUuid(counTrans.get().getUuid());
			
			cityDTO.setCountryId(counTrans.get().getId());
			Optional<StateTranslation> stateTrans = Optional.empty();
			stateTrans = cityEntity.getState().getStates().stream().filter(element -> element.getLocale().equals("en"))
					.findFirst();
			cityDTO.setStateUuid(stateTrans.get().getUuid());
			cityDTO.setStatus(cityEntity.getStatus());
			List<CityTranslationDTO> cityTranslationDTOs = new ArrayList<CityTranslationDTO>();
			cityEntity.getCities().forEach((element) -> {
				CityTranslationDTO cityTranslationDTO = new CityTranslationDTO();
				cityTranslationDTO.setLocale(element.getLocale());
				cityTranslationDTO.setUuid(element.getUuid());
				cityTranslationDTO.setName(element.getName());
				cityTranslationDTOs.add(cityTranslationDTO);
			});
			cityDTO.setCityTranslableDtos(cityTranslationDTOs);
			return cityDTO;
		}
		return null;
	}

	/**
	 * Change status.
	 *
	 * @param uuid the uuid
	 * @param status the status
	 * @return the string
	 */
	@Override
	public String changeStatus(String uuid, String status) {

		try {
			City city = cityRepository.findByUuid(uuid);
			if (city != null) {
				city.setStatus(status);
				cityRepository.save(city);
				return HttpStatus.OK.name();
			} else {
				return HttpStatus.NOT_FOUND.name();
			}
		} catch (DataIntegrityViolationException e) {
			return HttpStatus.INTERNAL_SERVER_ERROR.name();
		}
	}

	/**
	 * Delete city.
	 *
	 * @param uuid the uuid
	 * @return the string
	 */
	@Override
	public String deleteCity(String uuid) {
		try {
			City city = cityRepository.findByUuid(uuid);

			if (city != null) {
				List<CityTranslation> cityTranslations = new ArrayList<CityTranslation>();
				cityTranslations = city.getCities();
				cityTranslableRepository.deleteAll(cityTranslations);
				cityRepository.deleteById(city.getId());
				return HttpStatus.OK.name();
			}

			return HttpStatus.NOT_FOUND.name();
		} catch (DataIntegrityViolationException e) {
			return HttpStatus.INTERNAL_SERVER_ERROR.name();
		}
	}
}
