package com.humaine.portal.api.rest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.humaine.portal.api.model.UserGroup;
import com.humaine.portal.api.response.dto.ShortGroupDetails;

@Repository
public interface UserGroupRepository extends CrudRepository<UserGroup, Long> {

	@Query(value = "SELECT u FROM UserGroup u WHERE u.account.id=:accountID AND u.name=TRIM(:name)")
	UserGroup findGroupByAccountIdAndName(@Param("accountID") Long account, @Param("name") String name);

	@Query(value = "SELECT u FROM UserGroup u WHERE u.account.id=:accountID AND u.id !=:groupId AND u.name=TRIM(:name)")
	UserGroup findGroupByAccountIdAndName(@Param("accountID") Long account, @Param("groupId") Long group,
			@Param("name") String name);

	@Query(value = "SELECT u FROM UserGroup u WHERE u.account.id=:accountID AND u.id=:group")
	UserGroup findGroupByAccountIdAndGroupId(@Param("accountID") Long account, @Param("group") Long group);

	@Query(value = "SELECT u FROM UserGroup u WHERE u.account.id=:accountID AND u.id IN :groupIds")
	List<UserGroup> findGroupByAccountIdAndGroupIds(@Param("accountID") Long account,
			@Param("groupIds") List<Long> groupIds);

	@Query(value = "SELECT u FROM UserGroup u WHERE u.account.id=:accountID")
	List<UserGroup> groupListByAccount(@Param("accountID") Long account);

	@Query(value = "SELECT u.id FROM UserGroup u WHERE u.account.id=:accountID")
	List<Long> groupIdListByAccount(@Param("accountID") Long account);

	@Query(value = "SELECT new com.humaine.portal.api.response.dto.ShortGroupDetails(u.id,u.name) FROM UserGroup u WHERE u.account.id=:accountID")
	List<ShortGroupDetails> groupShortDetailsListByAccount(@Param("accountID") Long account);

}
