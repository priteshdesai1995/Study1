package com.humaine.portal.api.rest.repository.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

@Repository
public class AttributeRepository {

	@PersistenceContext
	private EntityManager entityManager;

}
