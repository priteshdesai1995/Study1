package com.humaine.transactional.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.humaine.transactional.model.InventoryMaster;

@Repository
public interface InventoryRepository extends CrudRepository<InventoryMaster, String> {

}
