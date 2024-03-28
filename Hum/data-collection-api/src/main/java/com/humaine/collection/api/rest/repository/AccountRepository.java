package com.humaine.collection.api.rest.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.humaine.collection.api.model.Account;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

	@Query(value = "SELECT a FROM Account a WHERE a.apiKey=:apiKey")
	Account findAccountByAPIKey(@Param("apiKey") String apiKey);
}
