package com.humaine.portal.api.rest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.humaine.portal.api.model.AIUserGroup;
import com.humaine.portal.api.response.dto.UserGroupRank;

@Repository
public interface AIUserGroupRepository extends CrudRepository<AIUserGroup, Long> {

	@Query(value = "SELECT u.userGroupId FROM AIUserGroup u WHERE u.account=:accountID")
	List<String> getGroupIdsByAccount(@Param("accountID") Long account);

	@Query(value = "SELECT u FROM AIUserGroup u WHERE u.account=:accountID")
	List<AIUserGroup> getGroupListByAccount(@Param("accountID") Long account);

	@Query(value = "SELECT u FROM AIUserGroup u WHERE u.account=:accountID AND u.id=:group")
	AIUserGroup findGroupByAccountIdAndGroupId(@Param("accountID") Long account, @Param("group") Long group);

	@Query(value = "SELECT u FROM AIUserGroup u WHERE u.account=:accountID AND u.userGroupId IN (:ids)")
	List<AIUserGroup> findGroupByAccountIdAndGroupId(@Param("accountID") Long account, @Param("ids") List<Long> ids);

	@Query(value = "SELECT u.userGroupId FROM AIUserGroup u WHERE u.account=:accountID AND u.userGroupId IN (:ids)")
	List<String> findGroupsByAccountAndIds(@Param("accountID") Long account, @Param("ids") List<String> ids);

	@Query(value = "select grp.user_group_id as groupId, grp.rank as rank from(select user_group_id,ROW_NUMBER () OVER (ORDER BY success_rate desc) as rank from ai_user_group ug where account_id =:accountID AND success_rate IS NOT NULL AND success_rate > 0) as grp WHERE grp.user_group_id=:group ORDER BY rank ASC LIMIT 1", nativeQuery = true)
	UserGroupRank getGroupTopRankGroupByAccount(@Param("accountID") Long account, @Param("group") String group);
}
