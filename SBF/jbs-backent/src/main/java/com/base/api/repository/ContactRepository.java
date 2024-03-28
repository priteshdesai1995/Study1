package com.base.api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.api.entities.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, UUID> {
	
}
