/**
 * Copyright 2022 Brainvire - All rights reserved.
 */
package com.base.api.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.api.entities.Announcement;
import com.base.api.entities.AnnouncementUser;

/**
 * This interface is use jpa repository to perform database related operation on
 * the AnnouncementUser.
 * 
 * @author minesh_prajapati
 *
 */
@Repository
public interface AnnouncementUserRepository
		extends JpaRepository<AnnouncementUser, UUID>, AnnouncementUserCustomRepository {

	@Query(value = "select * from announcement_users a where a.announcement_announcement_id = :announcementId", nativeQuery = true)
	List<Announcement> findByAnnouncementId(String announcementId);
}
