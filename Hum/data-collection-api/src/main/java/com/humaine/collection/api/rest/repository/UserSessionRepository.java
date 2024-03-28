package com.humaine.collection.api.rest.repository;

import java.time.OffsetDateTime;
import java.util.List;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.humaine.collection.api.es.projection.model.EnddedSession;
import com.humaine.collection.api.model.UserSession;

@Repository
public interface UserSessionRepository extends CrudRepository<UserSession, String> {
	@Query("SELECT u from UserSession u WHERE u.id=:id AND u.account=:account")
	UserSession getByIdAndAccountId(@Param("id") String id, @Param("account") Long accountId);

	@Query(value = "with lastevent as(select sessionid ,coalesce (max(\"timestamp\"), null) as lasteventtime from userevent nue where eventid != 'START' group by sessionid), latestpageload as(select pd.sessionid ,coalesce(max(\"timestamp\"), null) as lastpageloadtime from pageload_data pd group by pd.sessionid ) select us.accountid as accountID,us.sessionid as sessionID, us.userid as userID, to_char(us.starttime, 'yyyy-MM-dd HH24:MI:SS') as startTime,to_char(le.lasteventtime, 'yyyy-MM-dd HH24:MI:SS') as lastEventTime, to_char(lpd.lastpageloadtime, 'yyyy-MM-dd HH24:MI:SS') as lastPageLoadTime from usersession us inner join accountmaster a on (a.accountid = us.accountid) left join lastevent le on (le.sessionid = us.sessionid) left join latestpageload lpd on (lpd.sessionid = us.sessionid) where us.endtime is null and EXTRACT(EPOCH FROM (now() at time zone 'utc' - starttime )) >= a.sessiontimeout + 60 limit 10000", nativeQuery = true)
	List<EnddedSession> getEndedUserSessions();

	@Transactional(value = TxType.REQUIRES_NEW)
	@Modifying(flushAutomatically = true)
	@Query(value = "UPDATE UserSession u SET u.endTime=:date WHERE id=:id")
	void deactivateUsersNotLoggedInSince(@Param("id") String id, @Param("date") OffsetDateTime date);
}
