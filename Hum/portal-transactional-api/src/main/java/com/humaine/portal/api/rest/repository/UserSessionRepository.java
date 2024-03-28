package com.humaine.portal.api.rest.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.humaine.portal.api.model.UserSession;

@Repository
public interface UserSessionRepository extends CrudRepository<UserSession, String> {

	@Query(value = "SELECT cast(AVG(rowsPerDay) as DECIMAL(10,0)) AS avgUserSessionsPerDay from ( select COUNT(*) AS rowsPerDay from usersession u where u.accountid =:account AND DATE(u.starttime) < current_date GROUP BY DATE(u.starttime)) as a", nativeQuery = true)
	Long getAvgUserSessionsPerDay(@Param("account") Long account);
}
