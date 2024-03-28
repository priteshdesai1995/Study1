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
import com.base.api.filter.FilterBase;
import com.base.api.filter.StateFilter;
import com.base.api.repository.CityRepository;
import com.base.api.repository.CityTranslableRepository;
import com.base.api.repository.CountryRepository;
import com.base.api.repository.CountryTranslationRepository;
import com.base.api.repository.StateRepository;
import com.base.api.repository.StateTranslationRepository;
import com.base.api.request.dto.StateDTO;
import com.base.api.response.dto.StateTranslableDto;
import com.base.api.service.StateService;
import com.base.api.utils.Util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StateServiceImpl implements StateService {

	@Autowired
	CountryRepository countryRepository;

	@Autowired
	CountryTranslationRepository countryTranslationRepository;

	@Autowired
	StateRepository stateRepository;

	@Autowired
	StateTranslationRepository stateTranslationRepository;

	@Autowired
	CityRepository cityRepository;

	@Autowired
	CityTranslableRepository cityTranslableRepository;

	@Autowired
	EntityManager entityManager;

	/**
	 * Creates the state.
	 *
	 * @param stateDTO the state DTO
	 * @return the string
	 */
	@Override
	public String createState(@Valid StateDTO stateDTO) {
		try {

			State entity = new State();
			String uuid = Util.generateUUID();
			entity.setStateCode(stateDTO.getStateCode());
			entity.setUuid(uuid);

			List<StateTranslation> stateTranslations = new ArrayList<StateTranslation>();
			for (StateTranslableDto dto : stateDTO.getStateDtos()) {
				StateTranslation stateTranslation = new StateTranslation();
				stateTranslation.setLocale(dto.getLocale());
				stateTranslation.setName(dto.getName());
				stateTranslation.setUuid(uuid);
				stateTranslations.add(stateTranslation);
			}
			entity.setStatus(stateDTO.getStatus());
			Country countryEntity = new Country();
			if (stateDTO.getCountryId() != null) {
				log.info(stateDTO.getCountryId().toString());
				Optional<CountryTranslation> countryTransalation = countryTranslationRepository
						.findById(stateDTO.getCountryId());
				String countryUuid = countryTransalation.get().getUuid();
				Optional<Country> countryModel = countryRepository.findByUuid(countryUuid);

				countryEntity = getCountryById(countryModel.get().getId()); // requires country uuid
			} else {
				return HttpStatus.NOT_FOUND.name();
			}
			List<StateTranslation> translations = new ArrayList<StateTranslation>();
			translations = stateTranslationRepository.saveAll(stateTranslations);
			entity.setStates(translations);
			entity.setCountry(countryEntity);
			stateRepository.save(entity);
			return HttpStatus.OK.name();
		} catch (DataIntegrityViolationException e) {
			return HttpStatus.INTERNAL_SERVER_ERROR.name();
		}
	}

	/**
	 * Gets the country by id.
	 *
	 * @param uuid the uuid
	 * @return the country by id
	 */
	@Override
	public Country getCountryById(UUID uuid) {
		// Column uuid is of type CountryTranslation
		Optional<Country> countryEntity = countryRepository.findById(uuid);
		if (countryEntity != null) {
			return countryEntity.get();
		} else {
			return null;
		}
	}

	/**
	 * Gets the state by id.
	 *
	 * @param id the id
	 * @return the state by id
	 */
	@Override
	public StateDTO getStateById(UUID id) {
		State entity = new State();
		entity = getState(id);
		if (entity != null) {
			StateDTO dto = new StateDTO();
			dto = mapEntityToDto(entity);
			return dto;
		}
		return null;
	}

	/**
	 * Map entity to dto.
	 *
	 * @param entity the entity
	 * @return the state DTO
	 */
	private StateDTO mapEntityToDto(State entity) {
		StateDTO dto = new StateDTO();
		dto.setCreateDate(entity.getCreatedDate());
		dto.setStateCode(entity.getStateCode());
		dto.setStatus(entity.getStatus());
		dto.setUuid(entity.getUuid());
		Optional<CountryTranslation> countryTrans = Optional.empty();
		countryTrans = entity.getCountry().getCountries().stream().filter(c -> (c.getLocale().equals("en")))
				.findFirst();
		dto.setCountryId(countryTrans.get().getId());
		dto.setCountryName(countryTrans.get().getName());
		dto.setUpdatedAt(entity.getUpdatedDate());
		List<StateTranslableDto> dtos = new ArrayList<StateTranslableDto>();
		entity.getStates().forEach((element) -> {
			StateTranslableDto transdto = new StateTranslableDto();
			transdto.setLocale(element.getLocale());
			transdto.setName(element.getName());
			transdto.setUuid(element.getUuid());
			dtos.add(transdto);
		});
		dto.setStateDtos(dtos);
		return dto;
	}

	/**
	 * Gets the state.
	 *
	 * @param uuid the uuid
	 * @return the state
	 */
	@Override
	public State getState(UUID uuid) {
		Optional<State> entity = stateRepository.findById(uuid);
		if (entity != null) {
			return entity.get();
		}
		return null;
	}

	/**
	 * Gets the total count.
	 *
	 * @param stateFilter the state filter
	 * @return the total count
	 */
	@Override
	public int getTotalCount(StateFilter stateFilter) {
		StringBuilder query = new StringBuilder();
		query = createCommonQuery();
		List<Tuple> rows = new ArrayList<Tuple>();
		Query natQuery = entityManager.createNativeQuery(query.toString(), Tuple.class);
		rows = natQuery.getResultList();
		return rows.size();
	}

	/**
	 * Search state.
	 *
	 * @param filter the filter
	 * @return the list
	 */
	@Override
	public List<Map<String, Object>> searchState(StateFilter filter) {
		StringBuilder query = new StringBuilder();
		query = createCommonQuery();
		if (filter.getStatus() != null && !filter.getStatus().isEmpty()) {
			query.append(" and u.status LIKE '" + filter.getStatus() + "' ");
		}
		if (filter.getStateName() != null && !filter.getStateName().isEmpty()) {
			query.append(" and UPPER(st.name) LIKE UPPER('%" + filter.getStateName() + "%')");
		}
		if (filter.getStateCode() != null && !filter.getStateCode().isEmpty()) {
			query.append(" and UPPER(u.state_code) LIKE UPPER('%" + filter.getStateCode() + "%')");
		}
		if (filter.getCountryName() != null && !filter.getCountryName().isEmpty()) {
			query.append(" and UPPER(ct.name) LIKE UPPER('%" + filter.getCountryName() + "%')");
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
	 * Creates the common query.
	 *
	 * @return the string builder
	 */
	private StringBuilder createCommonQuery() {
		StringBuilder query = new StringBuilder();
		query.append(
				"select u.state_code, cast(u.state_id  as varchar) as stateId, cast(u.country_id  as varchar), u.created_date, u.updated_date , u.status as status, st.name as state_name, ct.name as country_name from states u inner join state_translable st	on st.state_id = u.state_id \n"
						+ "inner join country_translable ct  on u.country_id =ct.country_id");
		query.append(" where st.locale LIKE 'en' and ct.locale LIKE 'en'");

		log.info("Query is : " + query);
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
	 * Update state.
	 *
	 * @param stateDTO the state DTO
	 * @param uuid the uuid
	 * @return the string
	 */
	@Override
	public String updateState(StateDTO stateDTO, String uuid) {

		try {
			State entity = stateRepository.findByUuid(uuid);

			if (entity != null) {
				entity.setStatus(stateDTO.getStatus());
				entity.setStateCode(stateDTO.getStateCode());
				Optional<Country> countryEntity = Optional.empty();

				countryEntity = countryRepository.findById(stateDTO.getCountryId());
				if (countryEntity.isPresent()) {
					entity.setCountry(countryEntity.get());
				}
				List<StateTranslation> stateTranslations = new ArrayList<StateTranslation>();
				stateDTO.getStateDtos().forEach((element) -> {
					StateTranslation stateTranslation = new StateTranslation();

					StateTranslation translation = stateTranslationRepository.findByUuidAndLocale(element.getUuid(), element.getLocale());
					stateTranslation = stateTranslationRepository.findById(translation.getId()).get();
					stateTranslation.setLocale(element.getLocale());
					stateTranslation.setName(element.getName());
					stateTranslations.add(stateTranslation);
				});
				List<StateTranslation> translations = new ArrayList<StateTranslation>();
				translations = stateTranslationRepository.saveAll(stateTranslations);
				entity.setStates(translations);
				stateRepository.save(entity);
				return HttpStatus.OK.name();
			}

			return HttpStatus.NOT_FOUND.name();
		} catch (DataIntegrityViolationException e) {
			return HttpStatus.INTERNAL_SERVER_ERROR.name();
		}
	}

	/**
	 * Change status.
	 *
	 * @param uuid the uuid
	 * @param status the status
	 * @return the string
	 */
	@Override
	public String changeStatus(UUID uuid, String status) {
		State state = stateRepository.findById(uuid).get();
		if (state != null) {
			state.setStatus(status);
			stateRepository.save(state);
			return HttpStatus.OK.name();
		}
		return HttpStatus.NOT_FOUND.name();
	}

	/**
	 * Gets the all states.
	 *
	 * @param countryUuid the country uuid
	 * @return the all states
	 */
	@Override
	public List<StateTranslableDto> getAllStates(UUID countryUuid) {
		Optional<CountryTranslation> countryTranslation = countryTranslationRepository.findById(countryUuid);
		String translateUuid = countryTranslation.get().getUuid();
		Optional<Country> country = countryRepository.findByUuid(translateUuid);
		List<StateTranslation> stateTranslation = new ArrayList<StateTranslation>();
		Country countryEntity = new Country();
		countryEntity = this.getCountryById(country.get().getId());
		stateTranslation = stateRepository.findByCountryId(countryEntity.getId());
		List<StateTranslableDto> translations = new ArrayList<StateTranslableDto>();
		stateTranslation.forEach((element) -> {
			StateTranslableDto translation = new StateTranslableDto();
			translation.setLocale(element.getLocale());
			translation.setName(element.getName());
			translation.setUuid(element.getUuid());
			State state = stateRepository.findByUuid(element.getUuid());
			if(state!=null)
				translation.setStateId(state.getId());				
			translations.add(translation);
		});
		return translations;
	}

	/**
	 * Gets the state by trans uuid.
	 *
	 * @param uuid the uuid
	 * @return the state by trans uuid
	 */
	@Override
	public State getStateByTransUuid(UUID uuid) {
		// Column uuid is of type StateTranslation
		Optional<State> stateEntity = stateRepository.findById(uuid);
		if (stateEntity != null) {
			return stateEntity.get();
		} else {
			return null;
		}
	}

	/**
	 * Delete state.
	 *
	 * @param uuid the uuid
	 * @return the string
	 */
	@Override
	public String deleteState(UUID uuid) {

		try {
			Optional<State> stateEntity = stateRepository.findById(uuid);
			if (stateEntity != null) {
				List<City> cityEntities = new ArrayList<City>();
				cityEntities = cityRepository.findByState_Id(stateEntity.get().getId());
				cityEntities.forEach((ele) -> {
					List<CityTranslation> cityTranslations = new ArrayList<CityTranslation>();
					cityTranslations = ele.getCities();
					cityTranslableRepository.deleteAll(cityTranslations);
					cityRepository.deleteById(ele.getId());
				});
				List<StateTranslation> stateTranslations = new ArrayList<StateTranslation>();
				stateTranslations = stateEntity.get().getStates();
				stateTranslationRepository.deleteAll(stateTranslations);
				stateRepository.deleteById(stateEntity.get().getId());
				return HttpStatus.OK.name();
			}
			return HttpStatus.NOT_FOUND.name();
		} catch (DataIntegrityViolationException e) {
			return HttpStatus.INTERNAL_SERVER_ERROR.name();
		}
	}
}
