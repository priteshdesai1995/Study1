package com.humaine.collection.api.rest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.humaine.collection.api.model.Product;

public interface ProductRepository extends CrudRepository<Product, Long> {

	@Query(value = "SELECT p FROM Product p WHERE p.account.id=:accountId")
	List<Product> getProductsByAccount(@Param("accountId") Long accountId);
	
	@Query(value = "SELECT p FROM Product p WHERE p.account.id=:accountId AND productId=:productId")
	Product getProductsByAccountAndId(@Param("accountId") Long accountId, @Param("productId") String productId);

}
