package com.base.api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.api.entities.UserProfile;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, UUID>{

}
