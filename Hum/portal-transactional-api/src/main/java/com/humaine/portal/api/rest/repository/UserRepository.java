package com.humaine.portal.api.rest.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.humaine.portal.api.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

	@Query(value = "SELECT u FROM User u WHERE u.id=:id AND u.account=:account")
	User findByUserAndAccountId(@Param("id") String id, @Param("account") Long account);

	@Query(value = "select count(*) from \"user\" u where u.accountid =:account", nativeQuery = true)
	Long getTotalUsersByAccount(@Param("account") Long account);
}
