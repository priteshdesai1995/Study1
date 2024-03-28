package com.humaine.admin.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.humaine.admin.api.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

	@Query(value = "select am.accountid from accountmaster am where am.is_deleted = false and am.accountid IN (:accountIds)", nativeQuery = true)
	List<Long> getAccountByIds(@Param("accountIds") List<Long> accountIds);

	@Modifying
	@Transactional
	@Query(value = "update accountmaster am set is_deleted = true where am.accountid IN (:accountIds)", nativeQuery = true)
	void deleteAccountByIds(@Param("accountIds") List<Long> accountIds);

	@Query(value = "select count(*) from accountmaster am where am.status LIKE (:status)", nativeQuery = true)
	Long getCountOfRegisteredCustomers(@Param("status") String status);
}
