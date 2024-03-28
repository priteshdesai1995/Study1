package com.base.api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.api.entities.Banner;

@Repository
public interface BannerRepository extends JpaRepository<Banner, UUID> {

}
