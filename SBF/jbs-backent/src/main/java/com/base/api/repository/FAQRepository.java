package com.base.api.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.api.entities.FAQEntity;
import com.base.api.entities.FAQTopicEntity;

/**
 * Copyright 2022 Brainvire - All rights reserved.
 */
@Repository
public interface FAQRepository extends JpaRepository<FAQEntity, UUID> {
	List<FAQEntity> findAllByStatus(String status);

	Optional<FAQTopicEntity> findByFaqTopicEntity_Id(UUID faq_topic_id);
}
