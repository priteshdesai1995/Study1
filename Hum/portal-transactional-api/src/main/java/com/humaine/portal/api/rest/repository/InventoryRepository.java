package com.humaine.portal.api.rest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.humaine.portal.api.model.InventoryMaster;
import com.humaine.portal.api.projection.model.DailyMostPurchasedProductCount;
import com.humaine.portal.api.projection.model.DailyMostViewedCategoryCount;
import com.humaine.portal.api.projection.model.DailyMostViewedProductCount;

@Repository
public interface InventoryRepository extends CrudRepository<InventoryMaster, Long> {

	String dailyMostViewedProductCountQuery = "";

	@Query(value = "SELECT im FROM InventoryMaster im WHERE im.product=:product")
	InventoryMaster findByProductId(@Param("product") String product);

	@Query(value = "select\n" + 
			"grp.viewCount,\n" + 
			"grp.productName,grp.image,\n" + 
			"	( select sum(s2.productquantity) as productPurchaseCount from saledata s2 where s2.accountid = :account and s2.productid = grp.productid and s2.saleon > now() at time zone 'utc' - interval '24 hour') as productPurchaseCount\n" + 
			"from\n" + 
			"	( select count(*) as viewCount,u.productid, mp.p_name as productName,mp.p_image as image from userevent u left join master_product mp on u.productid = mp.p_id where eventid = :eventId and u.accountid = :account and \"timestamp\" > now() at time zone 'utc' - interval '24 hour' group by u.productid, mp.p_name ,mp.p_image order by viewCount desc limit :limitVal ) as grp", nativeQuery = true)
	List<DailyMostViewedProductCount> getDailyMostViewedProductCount(@Param("eventId") String eventId,
			@Param("account") Long account, @Param("limitVal") Integer limit);

	@Query(value = "select grp.productName,grp.image,\n" + 
			"	(select count(*) from userevent u where eventid = :eventId and u.accountid = :account and u.productid = grp.productid and u.\"timestamp\" > now() at time zone 'utc' - interval '24 hour') as viewCount,\n" + 
			"	grp.purchasedCount\n" + 
			"    from ( select \n" + 
			"    s.productid, \n" + 
			"    sum(s.productquantity) as purchasedCount, \n" + 
			"    mp.p_name as productName, \n" + 
			"    mp.p_image as image from saledata s \n" + 
			"    left join master_product mp on s.productid = mp.p_id \n" + 
			"    where s.accountid = :account and s.saleon > now() at time zone 'utc' - interval '24 hour' group by \n" + 
			"    s.productid, mp.p_name , mp.p_image order by purchasedCount desc limit :limitVal) as grp", nativeQuery = true)
	List<DailyMostPurchasedProductCount> getDailyMostPurchasedProductCount(@Param("eventId") String eventId,
			@Param("account") Long account, @Param("limitVal") Integer limit);

	@Query(value = "select grp.category,grp.catVisitCount,\n" + 
			"(select count(*) from userevent u2 \n" + 
			" left join master_product mp2 on u2.productid = mp2.p_id \n" + 
			" right join master_category mc2 on mp2.p_cat_id = mc2.id\n" + 
			" where eventid = :eventId and u2.accountid = :account and \n" + 
			" mc2.cat_id = grp.cat_id and u2.\"timestamp\" > now() at time zone 'utc' - interval '24 hour'\n" + 
			") as viewCount\n" + 
			"from \n" + 
			"( select mc.cat_name as category, count(*) as catVisitCount, mc.cat_id\n" + 
			"  from userevent u \n" + 
			"  left join master_product mp on u.productid  = mp.p_id \n" + 
			"  right join master_category mc on mp.p_cat_id = mc.id \n" + 
			"  where u.accountid = :account and eventid = :eventId group by mc.cat_name, mc.cat_id order by catVisitCount desc limit :limitVal\n" + 
			") as grp order by viewcount desc", nativeQuery = true)
	List<DailyMostViewedCategoryCount> getDailyMostViewedCategoryCount(@Param("eventId") String eventId,
			@Param("account") Long account, @Param("limitVal") Integer limit);
}
