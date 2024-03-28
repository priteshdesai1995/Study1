package com.base.api.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.base.api.entities.Country;
import com.base.api.entities.CountryTranslation;
import com.base.api.enums.UserStatus;
import com.base.api.filter.CountryFilter;
import com.base.api.filter.FilterBase;
import com.base.api.repository.CountryRepository;
import com.base.api.repository.CountryTranslationRepository;
import com.base.api.repository.LocationTranslationRepository;
import com.base.api.request.dto.CountryDTO;
import com.base.api.request.dto.CountryTranslationDTO;
import com.base.api.service.CountryService;
import com.base.api.utils.Util;

import lombok.extern.slf4j.Slf4j;

/**
 * @author kavitha_deshagani
 *
 */
@Slf4j
@Service
public class CountryServiceImpl implements CountryService {

    @Autowired
    LocationTranslationRepository locationTranslationRepository;

    @Autowired
    CountryRepository countryRepository;

    @Autowired
    EntityManager entityManager;

    @Autowired
    CountryTranslationRepository countryTranslationRepository;

    /**
     * This implementation is used to create the new country.
     *
     * @param countryDTO the country DTO
     * @return the string
     */
    @Override
    public String createCountry(@Valid CountryDTO countryDTO) {
        try {
            log.info("CountryServiceImpl : createCountry");
            log.info("country constructor");
            Country country = new Country();
            country.setCountryCode(countryDTO.getCountryCode());
            country.setCreatedDate(LocalDateTime.now());
            country.setStatus(countryDTO.getStatus());
            country.setUpdatedDate(LocalDateTime.now());
            String countryUUID = Util.generateUUID();
            country.setUuid(countryUUID);
            List<CountryTranslation> locTrans = new ArrayList<CountryTranslation>();
            for (CountryTranslationDTO dto : countryDTO.getLocationtranslableDtos()) {
                CountryTranslation locationTranslation = new CountryTranslation();
                locationTranslation.setLocale(dto.getLocale());
                locationTranslation.setName(dto.getName());
                locationTranslation.setUuid(countryUUID);
                locTrans.add(locationTranslation);
            }
            List<CountryTranslation> locationTrans = new ArrayList<CountryTranslation>();
            locationTrans = locationTranslationRepository.saveAll(locTrans);
            country.setCountries(locationTrans);
            countryRepository.save(country);
            return HttpStatus.OK.name();
        } catch (DataIntegrityViolationException e) {
            return HttpStatus.INTERNAL_SERVER_ERROR.name();
        }
    }

    /**
     * This implementation is used to get all the countries.
     *
     * @return the all countries
     */
    @Override
    public List<CountryTranslationDTO> getAllCountries() {
        log.info("get all the countries");

        List<CountryTranslation> countryTranslations = countryRepository.findByCountriesLocaleAndStatus("en",
                UserStatus.ACTIVE.getStatus());
        List<CountryTranslationDTO> dtos = new ArrayList<CountryTranslationDTO>();

        for (CountryTranslation ct : countryTranslations) {
            CountryTranslationDTO countryTranslationDTO = new CountryTranslationDTO();
            countryTranslationDTO.setLocale(ct.getLocale());
            countryTranslationDTO.setName(ct.getName());

            CountryTranslation countryTranslation = countryTranslationRepository.getById(ct.getId());
            String uuid = countryTranslation.getUuid();
            Optional<Country> country = countryRepository.findByUuid(uuid);
            countryTranslationDTO.setTransId(ct.getId());
            countryTranslationDTO.setCountryId(country.get().getId());

            // countryTranslationDTO.setTransId(ct.getTransId().toString());
            dtos.add(countryTranslationDTO);
        }
        return dtos;
    }

    /**
     * This implementation is used for change the status.
     *
     * @param countryId the country id
     * @param status    the status
     * @return the string
     */
    @Override
    public String changeStatus(UUID countryId, String status) {
        Country country = countryRepository.getById(countryId);
        if (country != null) {
            country.setStatus(status);
            countryRepository.save(country);
            return HttpStatus.OK.name();
        } else {
            return "Record not found";
        }
    }

    /**
     * Gets the total count.
     *
     * @param countryFilter the country filter
     * @return the total count
     */
    @Override
    public int getTotalCount(CountryFilter countryFilter) {
        StringBuilder query = new StringBuilder();
        query = createCommonQuery();
        String queryParam = Util.getFilterQuery(countryFilter, query.toString());
        List<Country> entities = new ArrayList<Country>();
        entities = entityManager.createQuery(queryParam).getResultList();
        return entities.size();
    }

    /**
     * This implementation is used to get the country based on id.
     *
     * @param countryId the country id
     * @return the country by id
     */
    @Override
    public Optional<CountryDTO> getCountryById(UUID countryId) {
        Optional<Country> country = Optional.empty();
        country = countryRepository.findById(countryId);
        CountryDTO countryObj = new CountryDTO();
        Optional<CountryDTO> countryDTO = Optional.of(countryObj);
        if (country.isPresent()) {
            countryDTO.get().setCountryCode(country.get().getCountryCode());
            countryDTO.get().setStatus(country.get().getStatus());
            List<CountryTranslationDTO> dtos = new ArrayList<CountryTranslationDTO>();
            for (CountryTranslation ct : country.get().getCountries()) {
                CountryTranslationDTO countryTranslationDTO = new CountryTranslationDTO();
                countryTranslationDTO.setLocale(ct.getLocale());
                countryTranslationDTO.setName(ct.getName());
                countryTranslationDTO.setTransId(ct.getId());
                dtos.add(countryTranslationDTO);
            }
            countryDTO.get().setLocationtranslableDtos(dtos);
        }
        return countryDTO;
    }

    /**
     * This implementation is used to update the country based on id.
     *
     * @param countryDTO the country DTO
     * @param countryId  the country id
     * @return the string
     */
    @Override
    public String updateCountry(CountryDTO countryDTO, UUID countryId) {

        Country country = countryRepository.getById(countryId);

        if (country == null) {
            return HttpStatus.NOT_FOUND.name();
        } else {
            country.setCountryCode(countryDTO.getCountryCode());
            country.setStatus(countryDTO.getStatus());
            List<CountryTranslation> countryTranslations = new ArrayList<CountryTranslation>();
            for (CountryTranslationDTO countryTranslationDTO : countryDTO.getLocationtranslableDtos()) {
                UUID transUuid = countryTranslationDTO.getTransId();
                Optional<CountryTranslation> countryTranslationOption = countryTranslationRepository
                        .findById(transUuid);
                if (countryTranslationOption.isPresent()) {
                    CountryTranslation countryTranslation = countryTranslationOption.get();

                    countryTranslation.setLocale(countryTranslationDTO.getLocale());
                    countryTranslation.setName(countryTranslationDTO.getName());
                    countryTranslations.add(countryTranslation);
                }
            }
            List<CountryTranslation> savedTranslations = new ArrayList<CountryTranslation>();
            savedTranslations = countryTranslationRepository.saveAll(countryTranslations);
            country.setCountries(savedTranslations);
            country.setUpdatedDate(LocalDateTime.now());
            try {
                countryRepository.save(country);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            return HttpStatus.OK.name();
        }
    }

    /**
     * Search country.
     *
     * @param filter the filter
     * @return the list
     */
    @Override
    public List<Country> searchCountry(CountryFilter filter) {
        log.info("create common query");
        StringBuilder query = new StringBuilder();
        log.info("call the common query");
        query = createCommonQuery();
        log.info("check the status ");
        if (filter.getStatus() != null && !filter.getStatus().isEmpty()) {
            query.append(" and u.status LIKE '" + filter.getStatus() + "' ");
            log.info("check the status successfully");
        }
        log.info("check the name");
        if (filter.getName() != null && !filter.getName().isEmpty()) {
            query.append(" and UPPER(co.name) LIKE UPPER('%" + filter.getName() + "%')");
            log.info("check the name succeesfuly");
        }
        log.info("check the country");
        if (filter.getCountryCode() != null && !filter.getCountryCode().isEmpty()) {
            query.append(" and UPPER(u.countryCode) LIKE UPPER('%" + filter.getCountryCode() + "%')");
            log.info("check the country success");
        }
        log.info("create object for filter base");

        if (filter.getColumnName().equals("createdDate") || filter.getColumnName().equals("updatedDate")) {
            filter.setColumnName("co." + filter.getColumnName());
        }

        FilterBase filterBase = new FilterBase();
        filterBase.setColumnName(filter.getColumnName());
        filterBase.setStartRec(filter.getStartRec());
        filterBase.setEndRec(filter.getEndRec());
        filterBase.setSortingOrder(filter.getSortingOrder());
        filterBase.setOrder(filter.getOrder());
        log.info("Filterbase crete the constructor");
        String queryParam = Util.getFilterQuery(filterBase, query.toString());
        log.info("List of countries in arraylist" + queryParam.toString());
        List<Country> results = new ArrayList<Country>();
        log.info("results");
        results = entityManager.createQuery(queryParam).setFirstResult(Integer.valueOf(filter.getStartRec()))
                .setMaxResults(Integer.valueOf(filter.getEndRec())).getResultList();

        log.info("return resullts");
        return results;
    }

    /**
     * Creates the common query.
     *
     * @return the string builder
     */
    private StringBuilder createCommonQuery() {
        StringBuilder query = new StringBuilder();
        log.info("call:cretae common query");
        query.append("select u from Country u join u.countries co");
        log.info("select query");
        query.append(" where co.locale LIKE 'en'");
        log.info(" return query");
        return query;
    }

    /**
     * Delete country.
     *
     * @param countryId the country id
     * @return the string
     */
    @Override
    public String deleteCountry(UUID countryId) {
        try {
            countryRepository.deleteById(countryId);
        } catch (Exception e) {
            return HttpStatus.BAD_REQUEST.name();
        }
        return HttpStatus.OK.name();
    }

}
