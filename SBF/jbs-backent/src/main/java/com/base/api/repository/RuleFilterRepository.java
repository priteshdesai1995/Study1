package com.base.api.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.api.entities.RuleEntity;
import com.base.api.entities.RuleFilterEntity;

@Repository
public interface RuleFilterRepository extends JpaRepository<RuleFilterEntity, UUID> {

	List<RuleFilterEntity> findByRule(RuleEntity rule);
}
