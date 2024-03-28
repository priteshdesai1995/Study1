package com.humaine.collection.api.rest.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.humaine.collection.api.model.Sale;

@Repository
public interface SaleRepository extends CrudRepository<Sale, Long> {

}
