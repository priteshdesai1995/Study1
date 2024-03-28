package com.base.api.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import com.base.api.entities.Country;
import com.base.api.filter.CountryFilter;
import com.base.api.request.dto.CountryDTO;
import com.base.api.request.dto.CountryTranslationDTO;

public interface CountryService {

    public String createCountry(@Valid CountryDTO countryDTO);

    public List<Country> searchCountry(CountryFilter filter);

    int getTotalCount(CountryFilter countryFilter);

    public Optional<CountryDTO> getCountryById(UUID countryId);

    public String changeStatus(UUID countryId, String status);

    public String updateCountry(CountryDTO countryDTO, UUID countryId);

     public String deleteCountry(UUID countryId);

    public List<CountryTranslationDTO> getAllCountries();

}
