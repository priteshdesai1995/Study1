package com.humaine.admin.api.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.humaine.admin.api.dto.CustomerListing;
import com.humaine.admin.api.model.Account;
import com.humaine.admin.api.model.CustomerIndustry;

@Repository
public interface CustomerRepository extends JpaRepository<Account, Long> {

	@Query(value = "select\n" + "	ad.full_name as fullName,\n" + "	am.email as email,\n" + "	ad.url as url,\n"
			+ "	ad.address as address,\n" + "	ad.city as city,\n" + "	ad.state as state,\n"
			+ "	Date(am.createdon) as registeredOn,\n" + "	am.accountid as accountId,\n"
			+ "	array_to_string(array_agg(distinct ai.industry),', ' ) as industry, status\n" + "from accountmaster am\n"
			+ "left join accountdetails ad on\n" + "	ad.accountid = am.accountid \n"
			+ "left join account_industries ai on \n" + "	am.accountid = ai.accountid\n"
			+ " where am.is_deleted = false \n"
			+ "group by ad.full_name, am.email, ad.address, ad.city, ad.state, am.createdon, ad.url, am.accountid, status", countQuery = "select count(*) from accountmaster am where am.is_deleted = false", nativeQuery = true)
	Page<CustomerListing> findAllCustomers(Pageable pageable);

	@Query(value = "select ai.accountid, array_to_string(array_agg(distinct ai.industry),', ' ) as industries from account_industries ai where ai.accountid IN (:accountIds) group by ai.accountid", nativeQuery = true)
	List<CustomerIndustry> mapCustomerIndustry(@Param("accountIds") List<Long> accountIds);
}
