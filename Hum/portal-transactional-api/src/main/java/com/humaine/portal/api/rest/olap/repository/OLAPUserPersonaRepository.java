package com.humaine.portal.api.rest.olap.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.humaine.portal.api.olap.model.OLAPUserGroup;

@Repository
public interface OLAPUserPersonaRepository extends CrudRepository<OLAPUserGroup, String> {

	
	@Query(value = 
			"with latestDate as(select DATE(max(ug.created_date)) as ldate from humai_olap.user_group ug)select	distinct ugm.user_id from humai_olap.user_group ug inner join humai_olap.user_group_mapping ugm on	(ug.user_group_id = ugm.user_group_id)where DATE(ug.created_date) = (select ldate from latestDate)and  DATE(ugm.created_date) = DATE(ug.created_date)	and	ug.user_group_id = 'UG134'	and ug.account_id = '177'", nativeQuery = true)
	public List<String> findUserIdsForPersona(@Param("accountId") Long accountId, @Param("userGroupId") String userGroupId );

}
