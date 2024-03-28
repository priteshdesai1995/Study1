package com.base.api.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.api.entities.City;

@Repository
public interface CityRepository extends JpaRepository<City, UUID> {

	List<City> findByCountry_Id(UUID countryId);

	List<City> findByState_Id(UUID stateId);

	City findByUuid(String id);

}
