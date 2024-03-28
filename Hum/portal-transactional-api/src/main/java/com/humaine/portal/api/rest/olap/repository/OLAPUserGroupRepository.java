package com.humaine.portal.api.rest.olap.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.humaine.portal.api.olap.model.OLAPUserGroup;
import com.humaine.portal.api.projection.model.BigFiveWiseUserIds;
import com.humaine.portal.api.projection.model.OLAPUserGroupRank;
import com.humaine.portal.api.projection.model.RankWithUserCount;
import com.humaine.portal.api.response.dto.UserGroupMinMaxData;
import com.humaine.portal.api.response.dto.UserGroupRank;

@Repository
public interface OLAPUserGroupRepository extends CrudRepository<OLAPUserGroup, String> {

	@Query(value = "SELECT u FROM OLAPUserGroup u WHERE u.account=:accountID AND u.groupId=:group AND u.flag=:flag AND cast(u.createdDate as java.time.LocalDate)=(SELECT MAX(cast(ugs.createdDate as java.time.LocalDate)) FROM OLAPUserGroup ugs WHERE ugs.account=:accountID AND ugs.flag=:flag)")
	OLAPUserGroup findGroupByAccountIdAndGroupId(@Param("accountID") Long account, @Param("group") String group,
			@Param("flag") Integer flag);

	@Query(value = "select grp.rank, grp.userCount from (select user_group_id,ROW_NUMBER () OVER (ORDER BY success_rate desc) as rank, number_of_user as userCount from user_group ug where account_id =:accountID AND group_flag_id=:flag AND success_rate IS NOT NULL AND success_rate > 0) as grp WHERE grp.user_group_id=:group", nativeQuery = true)
	RankWithUserCount findGroupRankByAccountIdAndGroupId(@Param("accountID") Long account, @Param("group") String group,
			@Param("flag") Integer flag);

	@Query(value = "SELECT u FROM OLAPUserGroup u WHERE u.account=:accountID AND u.flag=:flag AND cast(u.createdDate as java.time.LocalDate)=(SELECT MAX(cast(ugs.createdDate as java.time.LocalDate)) FROM OLAPUserGroup ugs WHERE ugs.account=:accountID AND ugs.flag=:flag)")
	List<OLAPUserGroup> groupListByAccount(@Param("accountID") Long account, @Param("flag") Integer flag);

	@Query(value = "SELECT u FROM OLAPUserGroup u WHERE u.account=:accountID AND u.groupId NOT IN (:ids) AND u.flag=:flag AND cast(u.createdDate as java.time.LocalDate)=(SELECT MAX(cast(ugs.createdDate as java.time.LocalDate)) FROM OLAPUserGroup ugs WHERE ugs.account=:accountID AND ugs.flag=:flag)")
	List<OLAPUserGroup> getGroupListByAccount(@Param("accountID") Long account, @Param("ids") List<String> ids,
			@Param("flag") Integer flag);

	@Query(value = "SELECT u FROM OLAPUserGroup u WHERE u.account=:accountID AND u.groupId IN (:ids) AND u.flag=:flag AND CAST(u.createdDate as java.time.LocalDate)=(SELECT MAX(CAST(ugs.createdDate as java.time.LocalDate)) FROM OLAPUserGroup ugs WHERE ugs.account=:accountID AND ugs.flag=:flag)")
	List<OLAPUserGroup> getGroupListByIDsAndAccount(@Param("accountID") Long account, @Param("ids") List<String> ids,
			@Param("flag") Integer flag);

	@Query(value = "SELECT u FROM OLAPUserGroup u WHERE u.account=:accountID AND TRIM(LOWER(u.bigFive))=TRIM(LOWER(:bigFive)) AND u.flag=:flag AND cast(u.createdDate as java.time.LocalDate)=(SELECT MAX(cast(ugs.createdDate as java.time.LocalDate)) FROM OLAPUserGroup ugs WHERE ugs.account=:accountID AND ugs.flag=:flag)")
	List<OLAPUserGroup> getGroupListByAccountAndBigFive(@Param("accountID") Long account,
			@Param("bigFive") String bigFive, @Param("flag") Integer flag);

	@Query(value = "select grp.user_group_id as groupId, grp.rank as rank from (select user_group_id,ROW_NUMBER () OVER (ORDER BY success_rate desc) as rank, big_five as bigFive from user_group ug where account_id =:accountID AND group_flag_id=:flag AND success_rate IS NOT NULL AND success_rate > 0) as grp WHERE TRIM(LOWER(grp.bigFive))=TRIM(LOWER(:bigFive))", nativeQuery = true)
	List<UserGroupRank> getGroupsRankListByAccountAndBigFive(@Param("accountID") Long account,
			@Param("bigFive") String bigFive, @Param("flag") Integer flag);

	@Query(value = "select grp.user_group_id as groupId,(select sum(ug2.number_of_user) from user_group ug2 where account_id =:accountID  AND ug2.group_flag_id=:flag AND TRIM(LOWER(ug2.big_five))=TRIM(LOWER(:bigFive)) AND date(created_date)=(SELECT max(date(ugs3.created_date)) FROM  user_group ugs3 WHERE  ugs3.account_id =:accountID AND ugs3.group_flag_id =:flag AND trim(lower(ugs3.big_five))= trim(lower(:bigFive)))) as totalUsers, grp.rank as rank from (select user_group_id,ROW_NUMBER () OVER (ORDER BY success_rate desc) as rank, big_five as bigFive from user_group ug where account_id =:accountID  AND group_flag_id=:flag AND success_rate IS NOT NULL AND success_rate > 0 AND TRIM(LOWER(big_five))=TRIM(LOWER(:bigFive)) and date(created_date)=(select max(date(created_date)) from user_group ugs where ugs.account_id =:accountID and ugs.group_flag_id =:flag and ugs.success_rate is not null and ugs.success_rate > 0 and TRIM(LOWER(ugs.big_five))= TRIM(LOWER(:bigFive)))) as grp ORDER BY rank ASC LIMIT 1", nativeQuery = true)
	OLAPUserGroupRank getGroupsTopRankGroupByAccountAndBigFive(@Param("accountID") Long account,
			@Param("bigFive") String bigFive, @Param("flag") Integer flag);

	@Query(value = "select grp.user_group_id as groupId, grp.rank as rank from (select user_group_id,ROW_NUMBER () OVER (ORDER BY success_rate desc) as rank, big_five as bigFive from user_group ug where account_id =:accountID  AND group_flag_id=:flag AND success_rate IS NOT NULL AND success_rate > 0 AND TRIM(LOWER(big_five))=TRIM(LOWER(:bigFive)) AND date(created_date)=(select max(date(created_date)) from user_group ugs where ugs.account_id =:accountID and ugs.group_flag_id =:flag and ugs.success_rate is not null and ugs.success_rate > 0 and TRIM(LOWER(ugs.big_five))= TRIM(LOWER(:bigFive)))) as grp WHERE user_group_id=:group ORDER BY rank ASC LIMIT 1", nativeQuery = true)
	UserGroupRank getGroupsTopRankGroupByAccountAndBigFive(@Param("accountID") Long account,
			@Param("bigFive") String bigFive, @Param("flag") Integer flag, @Param("group") String group);

	@Query(value = "SELECT new com.humaine.portal.api.response.dto.UserGroupMinMaxData(MIN(u.minFamilySize),MAX(u.maxFamilySize), MIN(u.minEducation), MAX(u.maxEducation), MIN(u.minAge), MAX(u.maxAge),MAX(u.malePercent), MAX(u.femalePercent), MAX(u.otherPercent),MAX(u.noOfUser)) FROM OLAPUserGroup u WHERE u.account=:accountID AND u.groupId IN (:ids) AND u.flag=:flag AND cast(u.createdDate as java.time.LocalDate)=(SELECT MAX(cast(ugs.createdDate as java.time.LocalDate)) FROM OLAPUserGroup ugs WHERE ugs.account=:accountID AND ugs.flag=:flag)")
	UserGroupMinMaxData getMinMaxDataByIds(@Param("accountID") Long account, @Param("flag") Integer flag,
			List<String> ids);

	@Query(value = "SELECT distinct um.userid as userId, array_agg(distinct ug.big_five) as bigFives\n"
			+ "FROM humai_olap.user_group ug left join user_group_mapping ugm on ug.user_group_id = ugm.user_group_id \n"
			+ "left join user_map um on ugm.user_id = cast(um.user_map as varchar) where ug.group_flag_id = :flag and ug.account_id = :accountID and date(ug.created_date)=(select max(date(ug2.created_date)) from user_group ug2 where ug2.group_flag_id = :flag and ug.account_id = :accountID) group by um.userid", nativeQuery = true)
	List<BigFiveWiseUserIds> getBigFiveWiseUsers(@Param("accountID") Long account, @Param("flag") Integer flag);
	
	
}
