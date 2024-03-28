package com.humaine.portal.api.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.humaine.portal.api.model.UserActivity;

@Repository
public interface UserAdminActivityRepository extends JpaRepository<UserActivity, Long> {

}
