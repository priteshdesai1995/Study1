/**
 * Copyright 2022 Brainvire - All rights reserved.
 */
package com.base.api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.api.entities.Announcement;

/**
 * This interface is use jpa repository to perform database related operation on
 * Announcement.
 * 
 * @author minesh_prajapati
 *
 */
@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, UUID> {

}
