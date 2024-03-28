package com.base.api.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.base.api.entities.Logo;

public interface LogoRepository extends JpaRepository<Logo, UUID> {
	List<Logo> findBysiteName(String siteName);
}
