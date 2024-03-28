package com.humaine.portal.api.rest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.humaine.portal.api.model.Heatmap;

@Repository
public interface HeatmapRepository extends CrudRepository<Heatmap, Long> {

	@Query(value = "SELECT h FROM Heatmap h WHERE account=:accountID AND h.awsUploadFailed = false")
	List<Heatmap> getHeatMapImages(@Param("accountID") Long account);
	
	@Query(value = "SELECT h FROM Heatmap h WHERE account=:accountID AND h.awsUploadFailed = false AND TRIM(LOWER(category))=TRIM(LOWER(:category))")
	List<Heatmap> getHeatMapImagesByCategory(@Param("accountID") Long account, @Param("category") String category);
}
