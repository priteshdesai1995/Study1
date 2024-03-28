/**
 * Copyright 2022 Brainvire - All rights reserved.
 */
package com.base.api.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.base.api.entities.Announcement;
import com.base.api.request.dto.AnnouncementDTO;
import com.base.api.request.dto.OrderAndPaginationDTO;
import com.base.api.response.dto.UserResponseDTO;

/**
 * This interface is use criteria query to perform database related operation on
 * the AnnouncementUser.
 * 
 * @author minesh_prajapati
 *
 */
public interface AnnouncementUserCustomRepository{

	/**
	 * This interface repository search the announcement by given criteria.
	 * 
	 * @param announcementDTO request object of announcement.
	 * @return pageable list of announcement.
	 */
	Page<Announcement> getAllAnnouncementsWithFilters(AnnouncementDTO announcementDTO);

	/**
	 * This interface repository search the announcement's user by given criteria.
	 * 
	 * @param announcementId        uuid of announcement.
	 * @param OrderAndPaginationDTO request object for ordering and pagination.
	 * @return pageable list of announcement's user.
	 */
	Page<UserResponseDTO> getAnnouncementsUsersWithFiltersById(UUID announcementId,
			OrderAndPaginationDTO orderAndPaginationDTO);

}
