package com.base.api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.api.entities.TemplateTranslable;

@Repository
public interface TemplateTranslableRepository extends JpaRepository<TemplateTranslable, UUID> {

}