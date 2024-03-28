package com.humaine.transactional.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.humaine.transactional.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

	@Query(value = "SELECT u FROM User u WHERE u.id=:id AND u.account=:account")
	User findByUserAndAccountId(@Param("id") String id, @Param("account") String account);
}
