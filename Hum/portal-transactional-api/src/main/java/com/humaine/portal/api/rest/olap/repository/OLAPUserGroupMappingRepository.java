package com.humaine.portal.api.rest.olap.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.humaine.portal.api.olap.model.OLAPUserGroupMapping;

@Repository
public interface OLAPUserGroupMappingRepository extends CrudRepository<OLAPUserGroupMapping, Long> {

	@Query(value = "SELECT COUNT(DISTINCT u.userId) FROM OLAPUserGroupMapping u WHERE u.account=:accountID AND u.userGroupId=:groupId AND u.flag =:flag")
	Long getUserCount(@Param("accountID") Long account, @Param("groupId") String group, @Param("flag") Integer flag);

	@Query(value = "SELECT COUNT(DISTINCT ugm.userId) FROM OLAPUserGroupMapping ugm JOIN OLAPUserGroup u ON ugm.userGroupId = u.groupId WHERE ugm.flag=:flag AND ugm.account=:accountID AND u.account=:accountID AND TRIM(LOWER(u.bigFive))=TRIM(LOWER(:bigFive))")
	Long getUserCountByBigFive(@Param("accountID") Long account, @Param("bigFive") String bigFive, @Param("flag") Integer flag);
}
