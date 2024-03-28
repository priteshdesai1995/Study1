package com.base.api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.api.entities.FAQTopicEntity;

/**
 * Copyright 2022 Brainvire - All rights reserved.
 */
@Repository
public interface FAQTopicRepository extends JpaRepository<FAQTopicEntity, UUID> {

}
