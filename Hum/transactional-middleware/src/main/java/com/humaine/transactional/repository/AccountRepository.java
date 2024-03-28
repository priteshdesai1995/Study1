package com.humaine.transactional.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.humaine.transactional.model.Account;

@Repository
public interface AccountRepository extends CrudRepository<Account, String> {
}
