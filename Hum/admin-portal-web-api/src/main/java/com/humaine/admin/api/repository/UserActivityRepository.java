package com.humaine.admin.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.humaine.admin.api.model.UserActivity;

@Repository
public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {

}
