package com.humaine.transactional.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.humaine.transactional.model.UserDemographics;

@Repository
public interface UserDemographicsRepository extends CrudRepository<UserDemographics, String> {

}
