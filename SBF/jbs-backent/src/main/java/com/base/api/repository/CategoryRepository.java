package com.base.api.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.api.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {

	@Query(value = "select * from categories p where p.status = 'Active'", nativeQuery = true)
	List<Category> findAllActiveCategories(String status);
	
	@Query(value = "select * from categories c where c.category_table_category_id = :categoryId", nativeQuery = true)
	List<Category> getChildofParent(UUID categoryId);

}
