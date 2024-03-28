package com.humaine.portal.api.rest.repository;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.humaine.portal.api.model.JourneySuccessSessionCount;
import com.humaine.portal.api.model.UserEvent;
import com.humaine.portal.api.projection.model.DailyEventDistributionCount;
import com.humaine.portal.api.projection.model.WeeklyPopularProduct;

@Repository
public interface UserEventRepository extends CrudRepository<UserEvent, Long> {

	@Query(value = "SELECT eventid as eventName,\n"
			+ "CAST(((count(*)/NULLIF((select count(*) from userevent u2 where u2.\"timestamp\" > now() - interval '24 hour' and u2.accountid = 177)\\:\\:float, 0))*100) as DECIMAL(5, 2)) as percentage \n"
			+ "FROM userevent \n" + "where \"timestamp\" > now() - interval '24 hour' and accountid = :account\n"
			+ "group by eventid order by percentage desc\n", nativeQuery = true)
	List<DailyEventDistributionCount> getDailyEventDistributionData(@Param("account") Long account);

	@Query(value = "select distinct u2.menu_name from userevent u2 where u2.eventid = 'MENU' and u2.menu_name is not null and u2.accountid =:accountID", nativeQuery = true)
	List<String> findMenuNames(@Param("accountID") Long accountId);

	@Query(value = "with todaytotalsession as(select count( distinct u.sessionid) as totalSessionCount from	humainedev.userevent u where u.accountid = :accountID and DATE(u.\"timestamp\") = DATE(:currentDate)), salessession as(select count(distinct s.sessionid) as buyCount from humainedev.saledata s where s.accountid = :accountID and DATE(s.saleon) = DATE(:currentDate)) select totalSessionCount, buyCount from todaytotalsession, salesSession", nativeQuery = true)
	JourneySuccessSessionCount getSessionCountForToday(@Param("accountID") Long accountId,
			@Param("currentDate") OffsetDateTime currentDate);
	
	
	/**
	 * @param accountId
	 * @param currentDate
	 * @param stateName
	 * @return
	 * 
	 * This is the method that gives the statistics to find the success ratio of a state for today's
	 * date only 
	 */
	@Query(value = "with todaytotalsession as(\n"
			+ "select\n"
			+ "	count( distinct u.sessionid) as totalSessionCount\n"
			+ "from\n"
			+ "	humainedev.userevent u\n"
			+ "where\n"
			+ "	u.accountid =:accountID\n"
			+ "	and DATE(u.\"timestamp\") = DATE(:currentDate)),\n"
			+ " salessession as(\n"
			+ "select\n"
			+ "	count(distinct s.sessionid) as buyCount\n"
			+ "from\n"
			+ "	humainedev.saledata s\n"
			+ "left join humainedev.usersession us on\n"
			+ "	s.sessionid = us.sessionid\n"
			+ "where\n"
			+ "	us.state =:stateName\n"
			+ "	and s.accountid =:accountID\n"
			+ "	and DATE(s.saleon) = DATE(:currentDate))\n"
			+ "	\n"
			+ "select\n"
			+ "	totalSessionCount,\n"
			+ "	buyCount\n"
			+ "from\n"
			+ "	todaytotalsession,\n"
			+ "	salesSession\n", nativeQuery = true)
	JourneySuccessSessionCount getSessionCountForTodayWithStateFilter(@Param("accountID") Long accountId,
			@Param("currentDate") OffsetDateTime currentDate, @Param("stateName") String stateName);
	
	/**
	 * @param accountId
	 * @param eventId
	 * @param minDate
	 * @param maxDate
	 * @param stateName
	 * @return
	 * 
	 * This is the method that gives the statistics to find the success ratio of a state for current month
	 * only 
	 */
	
	@Query(value = "with todaytotalsession as(\n"
			+ "select\n"
			+ "		count( distinct u.sessionid) as totalSessionCount\n"
			+ "from\n"
			+ "		humainedev.userevent u\n"
			+ "left join humainedev.usersession us on \n"
			+ "			u.sessionid = us.sessionid\n"
			+ "where\n"
			+ "	u.accountid =:accountID\n"
			+ "	and DATE(u.\"timestamp\") >= DATE(:minDate)\n"
			+ "	and DATE(u.\"timestamp\") <= DATE(:maxDate)\n"
			+ "	and us.state =:stateName),\n"
			+ "	 salessession as(\n"
			+ "select\n"
			+ "	count( distinct s.sessionid ) as buyCount\n"
			+ "from\n"
			+ "	humainedev.saledata s\n"
			+ "left join humainedev.usersession us on\n"
			+ "	s.sessionid = us.sessionid\n"
			+ "where\n"
			+ "	us.state =:stateName\n"
			+ "	and s.accountid =:accountID\n"
			+ "	and DATE(s.saleon) >= DATE(:minDate)\n"
			+ "		and DATE(s.saleon) <= DATE(:maxDate)\n"
			+ ")\n"
			+ "select\n"
			+ "	totalSessionCount,\n"
			+ "	buyCount\n"
			+ "from\n"
			+ "	todaytotalsession,\n"
			+ "	salesSession",  nativeQuery = true)
	JourneySuccessSessionCount getMonthlySessionCountForCurrentMonthWithStateFilter(@Param("accountID") Long accountId,
			@Param("minDate") OffsetDateTime minDate,
			@Param("maxDate") OffsetDateTime maxDate, @Param("stateName") String stateName);
	
	
	
	
	@Query(value = "SELECT COUNT(sessionid) as totalSessionCount, (SELECT COUNT(sessionid) FROM userevent u2 WHERE u2.accountid =:accountID AND u2.eventid =:eventID AND DATE(u2.timestamp) >=DATE(:minDate) AND DATE(u2.timestamp) <=DATE(:maxDate)) as buyCount FROM userevent u WHERE u.accountid =:accountID AND DATE(u.timestamp) >= DATE(:minDate) AND DATE(u.timestamp) <= DATE(:maxDate)", nativeQuery = true)
	JourneySuccessSessionCount getMontlySessionCount(@Param("accountID") Long accountId,
			@Param("eventID") String eventId, @Param("minDate") OffsetDateTime minDate,
			@Param("maxDate") OffsetDateTime maxDate);

	
	// This repository method is to get productIds for userevent table by passing UserIds, BUY event and accountIds 
	// It gives the product purched in last 7 days  
//	
//	@Query(value="select u.productid "
//			+ "from  humainedev.userevent u "
//			+ "where u.accountid =:accountId "
//			+ "and u.eventid = 'BUY' "
//			+ "and u.userid in (:userIds) "
//			+ "and u.\"timestamp\" "
//			+ "between DATE(current_date)-interval '7 days' "
//			+ "and current_date"
//			,nativeQuery = true)
	
	@Query(value="select u.productid as productId, mp.p_name as productName, mp.p_image as productImage, count(u.productid) as totalSoldQuantities from  humainedev.userevent u inner join humainedev.master_product mp on (mp.p_id = u.productid)where u.accountid =:accountId and u.eventid = 'BUY' and u.userid in (:userIds) and u.\"timestamp\" between DATE(current_date)-interval '7 days' and current_date group by u.productid,mp.p_image ,mp.p_name order by totalSoldQuantities desc limit 5"
			,nativeQuery= true)
	List<WeeklyPopularProduct> getProductIdByAccountIdAndUserIdsForBuyEvent(@Param("accountId") Long accountId,@Param("userIds") List<String> userIds);
	
	
}
