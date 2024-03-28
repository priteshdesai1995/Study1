package com.humaine.portal.api.rest.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.humaine.portal.api.model.AIUserGroupDeleted;

@Repository
public interface AIUserGroupDeletedRepository extends CrudRepository<AIUserGroupDeleted, Long> {

}
