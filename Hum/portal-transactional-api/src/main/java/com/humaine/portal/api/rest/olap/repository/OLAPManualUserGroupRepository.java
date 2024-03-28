package com.humaine.portal.api.rest.olap.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.humaine.portal.api.model.ManualUserGroupRank;
import com.humaine.portal.api.olap.model.OLAPManualUserGroup;

@Repository
public interface OLAPManualUserGroupRepository extends CrudRepository<OLAPManualUserGroup, Long> {

	@Query(value = "SELECT u FROM OLAPManualUserGroup u WHERE u.account=:accountID AND u.groupId IN (:ids)")
	List<OLAPManualUserGroup> getGroupListByAccountAndIds(@Param("accountID") Long account,
			@Param("ids") List<Long> ids);

	@Query(value = "select grp.manual_user_group_id as groupId, CASE WHEN grp.success_rate > 0 THEN grp.rank ELSE null END AS rank,grp.success_rate as successMatch, grp.number_of_user as noOfUsers from (select mug.*, ROW_NUMBER () OVER (ORDER BY success_rate desc) as rank from manual_user_group mug where mug.account_id =:accountID AND manual_user_group_id IN (:ids) and date(createdon) = (select max(date(mugs.createdon)) from manual_user_group mugs where mugs.account_id =:accountID and mugs.manual_user_group_id in (:ids))) as grp", nativeQuery = true)
	List<ManualUserGroupRank> getGroupsRankByIds(@Param("accountID") Long account, @Param("ids") List<Long> ids);

	@Query(value = "select grp.manual_user_group_id as groupId, CASE WHEN grp.success_rate > 0 THEN grp.rank ELSE null END AS rank, grp.success_rate as successMatch, grp.number_of_user as noOfUsers from (select mug.*, ROW_NUMBER () OVER (ORDER BY success_rate desc) as rank from manual_user_group mug where mug.account_id =:accountID AND manual_user_group_id IN (:ids) and date(createdon) = (select max(date(mugs.createdon)) from manual_user_group mugs where mugs.account_id =:accountID and mugs.manual_user_group_id in (:ids))) as grp WHERE grp.manual_user_group_id=:group", nativeQuery = true)
	ManualUserGroupRank getGroupRankByIds(@Param("accountID") Long account, @Param("ids") List<Long> ids,
			@Param("group") Long group);
}
