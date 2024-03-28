package com.humaine.collection.api.rest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.humaine.collection.api.model.ProductCategory;

public interface ProductCategoryRepository extends CrudRepository<ProductCategory, Long> {

	@Query(value = "SELECT pc FROM ProductCategory pc WHERE pc.account.id=:accountId")
	List<ProductCategory> findProductCategoryByAccount(@Param("accountId") Long accountId);
}
