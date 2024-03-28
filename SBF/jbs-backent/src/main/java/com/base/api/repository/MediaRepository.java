package com.base.api.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.base.api.entities.Media;

@Repository
public interface MediaRepository extends JpaRepository<Media,UUID> {
	
	Media findById(String id);
	
	@Query("SELECT m FROM Media m WHERE m.parent_id =:id")
	List<Media> findByParentId(@Param("id") UUID id);
}
