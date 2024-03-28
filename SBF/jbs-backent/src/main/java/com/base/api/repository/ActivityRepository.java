package com.base.api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.base.api.entities.Activity;

public interface ActivityRepository extends JpaRepository<Activity, UUID> {

}
