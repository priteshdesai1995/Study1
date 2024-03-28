package com.humaine.portal.api.rest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.humaine.portal.api.model.AccountTracker;

@Repository
public interface AccountTrackerRepository extends CrudRepository<AccountTracker, Long> {

	@Query(value = "SELECT a FROM AccountTracker a WHERE a.isEmailSend=false")
	List<AccountTracker> getPendingAccountTracker();
}
