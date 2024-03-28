package com.humaine.portal.api.rest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.humaine.portal.api.model.Account;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {
	@Query(value = "SELECT a FROM Account a WHERE a.username=:username")
	Account findAccountByUsername(@Param("username") String username);

	@Query(value = "SELECT a FROM Account a WHERE a.email=:email")
	Account findAccountByEmail(@Param("email") String email);

	@Query(value = "SELECT a FROM Account a WHERE a.username=:username OR a.email=:email")
	Account findAccountByUsernameOrEmail(@Param("username") String username, @Param("email") String email);
	
	@Query(value = "SELECT accountid FROM humainedev.accountmaster", nativeQuery = true)
	List<Long> getIdsOfAllAccounts();
}
