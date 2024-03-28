package com.humaine.portal.api.rest.repository;

import java.time.OffsetDateTime;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.humaine.admin.api.projection.model.AccountEventsDetails;
import com.humaine.portal.api.model.UserEvent;

@Repository
public interface UserAdminEventRepository extends CrudRepository<UserEvent, Long> {

	@Query(value = "select (select count(*) from userevent u where u.accountid = :accountID and DATE(u.\"timestamp\") >= DATE(:minDate) and DATE(u.\"timestamp\") <= DATE(:maxDate) and eventid =:buyEventId) as buyEventsCount,\n"
			+ "(select count(distinct productid) from userevent u where u.accountid = :accountID and DATE(u.\"timestamp\") >= DATE(:minDate) and DATE(u.\"timestamp\") <= DATE(:maxDate) and eventid =:buyEventId or eventid =:prodViewEventId) as productSyncCount,\n"
			+ "(select count(distinct u.sessionid) from usersession u where u.accountid = :accountID and DATE(u.starttime) >= DATE(:minDate) and DATE(u.starttime) <= DATE(:maxDate)) as totalSessions,\n"
			+ "floor(avg(dailyEventCount.eventCount)) as dailyEventCount from (select count(*) as eventCount from userevent u where u.accountid = :accountID and DATE(u.\"timestamp\") >= DATE(:minDate) and DATE(u.\"timestamp\") <= DATE(:maxDate) group by DATE(u.\"timestamp\")) as dailyEventCount", nativeQuery = true)
	AccountEventsDetails AccoutWiseEventsDetails(@Param("accountID") Long account,
			@Param("minDate") OffsetDateTime minDate, @Param("maxDate") OffsetDateTime maxDate,
			@Param("buyEventId") String buyEvent, @Param("prodViewEventId") String prodViewEvent);

}