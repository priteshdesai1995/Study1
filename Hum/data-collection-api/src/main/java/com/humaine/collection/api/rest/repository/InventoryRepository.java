package com.humaine.collection.api.rest.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.humaine.collection.api.model.InventoryMaster;

@Repository
public interface InventoryRepository extends CrudRepository<InventoryMaster, Long> {

	@Query(value = "SELECT im FROM InventoryMaster im WHERE im.product=:product")
	InventoryMaster findByProductId(@Param("product") String product);
}
