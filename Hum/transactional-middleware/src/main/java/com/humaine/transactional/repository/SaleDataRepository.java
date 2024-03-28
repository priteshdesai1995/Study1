package com.humaine.transactional.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.humaine.transactional.model.Sale;

@Repository
public interface SaleDataRepository extends CrudRepository<Sale, String> {

}
