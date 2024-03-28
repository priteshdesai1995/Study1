package com.humaine.portal.api.rest.olap.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.humaine.portal.api.olap.model.MyUserGroupMasterBackUp;
import com.humaine.portal.api.response.dto.MyJourneyDetails;

@Repository
public interface MyUserGroupMasterBackUpRepository extends CrudRepository<MyUserGroupMasterBackUp, String> {

	@Query(value = "Select u.bigFive as bigFive,u.id as groupName from MyUserGroupMasterBackUp u where u.id in (:ids)")
	List<MyJourneyDetails> getBigFiveAndGroupName(@Param("ids") Set<String> groupids);

	@Query(value = "Select u.bigFive as bigFive,u.id as groupName from MyUserGroupMasterBackUp u where u.id =:groupID")
	MyJourneyDetails getBigFiveAndGroupName(@Param("groupID") String groupId);

}
