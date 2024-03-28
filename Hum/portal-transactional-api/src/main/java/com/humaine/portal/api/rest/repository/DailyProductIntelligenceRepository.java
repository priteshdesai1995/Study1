package com.humaine.portal.api.rest.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.humaine.portal.api.model.DailyProductIntelligence;
import com.humaine.portal.api.projection.model.ProductIntelligenceQueryProjection;

@Repository
public interface DailyProductIntelligenceRepository extends CrudRepository<DailyProductIntelligence, Long>{

	@Query(value = "select daily_product_intelligence_id as dailyProductIntelligenceId, \n"
			+ "	product_metadata as productMetadata , \n"
			+ "	\"name\" as productName,\n"
			+ "	product_id as productId, \n"
			+ "	product_image as productImage \n"
			+ "	from humainedev.daily_product_intelligence dpi\n"
			+ "where accountid =:accountId", nativeQuery = true)
	public List<ProductIntelligenceQueryProjection> getDailyProductIntelligenceByAccountId(@Param("accountId") Long accountId, Pageable pageable);

	
	@Query(value = "select count(*) from humainedev.daily_product_intelligence dpi where accountid =:accountId", nativeQuery = true)
	public Long getDailyProductIntelligenceCountForAccountId(@Param("accountId") Long accountId);
	
	@Modifying
    @Transactional
	@Query(value = "delete FROM humainedev.daily_product_intelligence\n"
			+ "where accountid =:accountId", nativeQuery = true)
	public void deleteDailyProductIntelligenceByAccountId(@Param("accountId") Long accountId);

}
