package com.humaine.transactional.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.humaine.transactional.model.UserSession;

@Repository
public interface UserSessionRepository extends CrudRepository<UserSession, String> {

}
