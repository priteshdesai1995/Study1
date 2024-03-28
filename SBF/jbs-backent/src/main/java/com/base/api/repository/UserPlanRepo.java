package com.base.api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.api.entities.UserPlans;

@Repository
public interface UserPlanRepo extends JpaRepository<UserPlans,UUID>{


}
