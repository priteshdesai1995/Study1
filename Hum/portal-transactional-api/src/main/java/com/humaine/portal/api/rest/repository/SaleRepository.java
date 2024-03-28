package com.humaine.portal.api.rest.repository;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.humaine.portal.api.model.Sale;
import com.humaine.portal.api.projection.model.DailyProductAvg;
import com.humaine.portal.api.projection.model.MonthlyProductData;
import com.humaine.portal.api.projection.model.Product;
import com.humaine.portal.api.projection.model.ProductIntelligenceObject;
import com.humaine.portal.api.projection.model.StateWiseSoldData;

@Repository
public interface SaleRepository extends CrudRepository<Sale, Long> {

	/**
	 * @param account
	 * @param limit
	 * @param minDate
	 * @param maxDate
	 * @return
	 * 
	 * This method gives top 5 most popular products
	 * 
	 */
	@Query(value = "SELECT \n" + "s.productid as productId,\n" + "m.p_name as name, \n"
			+ "m.p_image as productImage, \n" + "SUM(s.saleamount) as totalSoldAmount, \n"
			+ "SUM(s.productquantity) as totalSoldQuantities \n"
			+ "FROM saledata s INNER JOIN master_product m on s.productid = m.p_id WHERE s.accountid=:accountID AND DATE(s.saleon) >= DATE(:minDate) AND DATE(s.saleon) <=  DATE(:maxDate) GROUP BY s.productid, name,productImage ORDER BY totalSoldQuantities desc limit :limitVal\n", nativeQuery = true)
	List<Product> getPopularProductsList(@Param("accountID") Long account, @Param("limitVal") Integer limit,
			@Param("minDate") OffsetDateTime minDate, @Param("maxDate") OffsetDateTime maxDate);
	
	/**
	 * @param account
	 * @param limit
	 * @param minDate
	 * @param maxDate
	 * @return
	 * 
	 * This method gives  5 least popular products
	 * 
	 */
	@Query(value = "SELECT \n" + "s.productid as productId,\n" + "m.p_name as name, \n"
			+ "m.p_image as productImage, \n" + "SUM(s.saleamount) as totalSoldAmount, \n"
			+ "SUM(s.productquantity) as totalSoldQuantities \n"
			+ "FROM saledata s INNER JOIN master_product m on s.productid = m.p_id WHERE s.productquantity > 0 AND s.accountid=:accountID AND DATE(s.saleon) >= DATE(:minDate) AND DATE(s.saleon) <=  DATE(:maxDate) GROUP BY s.productid, name,productImage ORDER BY totalSoldQuantities asc limit :limitVal\n", nativeQuery = true)	
	List<Product> getLeastPopularProductsList(@Param("accountID") Long account, @Param("limitVal") Integer limit,
			@Param("minDate") OffsetDateTime minDate, @Param("maxDate") OffsetDateTime maxDate);	
	
	/**
	 * 
	 * @param account
	 * @param limit
	 * @return
	 * 
	 * This method will give the top 5 states which are actively making purchase 
	 */
	@Query(value = "SELECT SUM(s.saleamount) as totalSoldAmount, "
			+ "us.state as state "
			+ "FROM saledata s "
			+ "LEFT JOIN usersession us on s.sessionid = us.sessionid "
			+ "where s.accountid = :accountID "
			+ "AND us.state IS NOT NULL and us.country = 'United States of America' "
			+ "and DATE(s.saleon) >= DATE(:minDate)\n"
			+ "and DATE(s.saleon) <= DATE(:maxDate) "
			+ "GROUP BY state ORDER BY totalSoldAmount desc limit :limitVal", nativeQuery = true)
	List<StateWiseSoldData> getStateWiseSoldAmount(@Param("accountID") Long account, @Param("limitVal") Integer limit,@Param("minDate") OffsetDateTime minDate, @Param("maxDate") OffsetDateTime maxDate);

	/**
	 * @param account
	 * @param limit
	 * @return
	 * 
	 * This method gives the top 5 states which are not actively making purchase 
	 * 
	 */
		@Query(value = "SELECT SUM(s.saleamount) as totalSoldAmount, "
				+ "us.state as state "
				+ "FROM saledata s "
				+ "LEFT JOIN usersession us on "
				+ "s.sessionid = us.sessionid "
				+ "where s.accountid = :accountID "
				+ "AND us.state IS NOT NULL "
				+ "and us.country = 'United States of America' "
				+ "and DATE(s.saleon) >= DATE(:minDate)\n"
				+ "and DATE(s.saleon) <= DATE(:maxDate)"
				+ "GROUP BY state "
				+ "ORDER BY totalSoldAmount asc "
				+ "limit :limitVal", nativeQuery = true)
	List<StateWiseSoldData> getLeasePerformingStateWiseSoldAmount(@Param("accountID") Long account, @Param("limitVal") Integer limit, @Param("minDate") OffsetDateTime minDate, @Param("maxDate") OffsetDateTime maxDate);
	
	
	/**
	 * @param account
	 * @param limit
	 * @param minDate
	 * @param maxDate
	 * @param stateName
	 * @return
	 * 
	 * 
	 * This method is used to get the popular products but with state wise 
	 * 
	 */
	@Query(value = "select\n"
			+ "	s.productid as productId,\n"
			+ "	m.p_name as name, \n"
			+ "		 m.p_image as productImage,\n"
			+ "	SUM(s.saleamount) as totalSoldAmount, \n"
			+ "		SUM(s.productquantity) as totalSoldQuantities\n"
			+ "from\n"
			+ "	humainedev.saledata s\n"
			+ "inner join humainedev.master_product m on\n"
			+ "	s.productid = m.p_id\n"
			+ "left join humainedev.usersession us on\n"
			+ "	s.sessionid = us.sessionid\n"
			+ "where\n"
			+ "	s.productquantity > 0\n"
			+ "	and s.accountid =:accountID\n"
			+ "	and DATE(s.saleon) >= DATE(:minDate)\n"
			+ "	and DATE(s.saleon) <= DATE(:maxDate)\n"
			+ "	and us.state =:stateName\n"
			+ "group by\n"
			+ "	s.productid,\n"
			+ "	name,\n"
			+ "	productImage,\n"
			+ "	us.state \n"
			+ "order by\n"
			+ "	totalSoldQuantities desc \n"
			+ "limit :limitVal", nativeQuery = true)
	List<Product> getPopularProductsWithStateFilter(@Param("accountID") Long account, @Param("limitVal") Integer limit,
			@Param("minDate") OffsetDateTime minDate, @Param("maxDate") OffsetDateTime maxDate, @Param("stateName") String stateName );
	
	
	

	/**
	 * @param account
	 * @param limit
	 * @param minDate
	 * @param maxDate
	 * @param stateName
	 * @return
	 * 
	 * 
	 * This method is used to get the least popular products but with state wise 
	 * 
	 */
	@Query(value = "select\n"
			+ "	s.productid as productId,\n"
			+ "	m.p_name as name, \n"
			+ "		 m.p_image as productImage,\n"
			+ "	SUM(s.saleamount) as totalSoldAmount, \n"
			+ "		SUM(s.productquantity) as totalSoldQuantities\n"
			+ "from\n"
			+ "	humainedev.saledata s\n"
			+ "inner join humainedev.master_product m on\n"
			+ "	s.productid = m.p_id\n"
			+ "left join humainedev.usersession us on\n"
			+ "	s.sessionid = us.sessionid\n"
			+ "where\n"
			+ "	s.productquantity > 0\n"
			+ "	and s.accountid =:accountID\n"
			+ "	and DATE(s.saleon) >= DATE(:minDate)\n"
			+ "	and DATE(s.saleon) <= DATE(:maxDate)\n"
			+ "	and us.state =:stateName\n"
			+ "group by\n"
			+ "	s.productid,\n"
			+ "	name,\n"
			+ "	productImage,\n"
			+ "	us.state \n"
			+ "order by\n"
			+ "	totalSoldQuantities asc \n"
			+ "limit :limitVal", nativeQuery = true)
	List<Product> getLeastPopularProductsWithStateFilter(@Param("accountID") Long account, @Param("limitVal") Integer limit,
			@Param("minDate") OffsetDateTime minDate, @Param("maxDate") OffsetDateTime maxDate, @Param("stateName") String stateName );
	
	
	
	@Query(value = "select 	COUNT(distinct grp.sessionid) as totalCustomers , SUM(grp.saleamount) as totalPurchases, SUM(grp.productquantity) as totalSoldQuantities, cast(SUM(grp.productquantity) * AVG(grp.saleamount) as DECIMAL(20, 2)) as saleRevenue from saledata grp where	grp.accountid =:accountID and DATE(grp.saleon) >= DATE(:minDate) and DATE(grp.saleon) <= DATE(:maxDate)", nativeQuery = true)
	MonthlyProductData getMonthlyProductData(@Param("accountID") Long account, @Param("minDate") OffsetDateTime minDate,
			@Param("maxDate") OffsetDateTime maxDate);
	
	
	
	
	
	
	/**
	 * @param account
	 * @param minDate
	 * @param maxDate
	 * @return
	 * 
	 * This will give us the monthly product data but state wise 
	 * 
	 */
	@Query(value = "select 	COUNT(distinct grp.sessionid) as totalCustomers , SUM(grp.saleamount) as totalPurchases, SUM(grp.productquantity) as totalSoldQuantities, cast(SUM(grp.productquantity) * AVG(grp.saleamount) as DECIMAL(20, 2)) as saleRevenue from saledata grp left join humainedev.usersession us on\n"
			+ "	grp.sessionid = us.sessionid where	grp.accountid =:accountID and DATE(grp.saleon) >= DATE(:minDate) and DATE(grp.saleon) <= DATE(:maxDate) and us.state =:stateName group by\n"
			+ "	us.state", nativeQuery = true)
	MonthlyProductData getMonthlyProductDataWithStateFilter(@Param("accountID") Long account, @Param("minDate") OffsetDateTime minDate,
			@Param("maxDate") OffsetDateTime maxDate, @Param("stateName") String stateName);

	@Query(value = "SELECT cast(AVG(totalPurchasedProducts) as DECIMAL(10,0)) as avgProducts, cast(AVG(totalPurchases) as DECIMAL(20,2)) as avgSale from (select count(*) as totalPurchasedProducts ,SUM(s.saleamount) as totalPurchases from saledata s where s.accountid =:account AND DATE(s.saleon) < current_date group by DATE(s.saleon)) as a", nativeQuery = true)
	DailyProductAvg getDailyPrductAvg(@Param("account") Long account);

	@Query(value = "select\n" + 
			"	distinct s.productid,\n" + 
			"	mp.p_name as \"name\",\n" + 
			"	mp.p_image as productImage,\n" + 
			"	count(*) over() as totalCount,\n" + 
			"	array_agg(distinct s.userid) as userIds,\n" + 
			"	count(*) as saleAmount,\n" + 
			"	sum(s.productquantity) as totalQty,\n" + 
			"	(\n" + 
			"	select\n" + 
			"		count(distinct s2.userid)\n" + 
			"	from\n" + 
			"		saledata s2\n" + 
			"	where\n" + 
			"		accountid =:account) as totalUserCount\n" + 
			"from\n" + 
			"	saledata s\n" + 
			"left join master_product mp on\n" + 
			"	s.productid = mp.p_id \n" + 
			"where\n" + 
			"	s.accountid =:account\n and \n" + 
			"	mp.p_name != :defaultTitleName " + 
			"group by\n" + 
			"	s.productid,\n" + 
			"	mp.p_name,\n" + 
			"	mp.p_image", nativeQuery = true)
	List<ProductIntelligenceObject> getProductIntelligence(@Param("account") Long account, @Param("defaultTitleName") String defaultTitleName, Pageable pageable);
}
