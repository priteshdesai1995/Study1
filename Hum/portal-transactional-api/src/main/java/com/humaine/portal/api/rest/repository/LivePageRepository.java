package com.humaine.portal.api.rest.repository;


import java.sql.Timestamp;
import java.time.OffsetDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.humaine.portal.api.model.LivePageRefreshHistory;

@Repository
public interface LivePageRepository extends JpaRepository<LivePageRefreshHistory, Long>{

	
	@Query(value = "select\n"
			+ "	\"timestamp\" AT TIME ZONE 'UTC' as \"lastRefreshDateTime\"\n"
			+ "from\n"
			+ "	humainedev.live_page_refresh_history\n"
			+ "where\n"
			+ "	\"timestamp\" = (\n"
			+ "	select\n"
			+ "		max(\"timestamp\")\n"
			+ "	from\n"
			+ "		humainedev.live_page_refresh_history)\n"
			+ "	and accountid =:account", nativeQuery = true)
	Timestamp getLastRefreshedDateAndTime(@Param("account") Long account);
	
	/**
	 * This method will have a query to update the entry 
	 */
	@Query(value = "update humainedev.live_page_refresh_history  set \"timestamp\" =:currentRefreshTime\n"
			+ "where accountid =:accountID", nativeQuery = true)
	LivePageRefreshHistory updateRefreshDateAndTimeForAccountId(@Param("accountID") Long account, 
			@Param("currentRefreshTime") OffsetDateTime currentTime);
	
	
	
}
