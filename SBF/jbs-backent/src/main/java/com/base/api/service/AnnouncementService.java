/**
 * Copyright 2022 Brainvire - All rights reserved.
 */
package com.base.api.service;

import java.text.ParseException;
import java.util.List;

import com.base.api.dto.filter.AnnouncementFilter;
import com.base.api.entities.Announcement;

/**
 * @author minesh_prajapati
 *
 */
public interface AnnouncementService {

	String createAnnouncement(AnnouncementFilter announcementFilter) throws ParseException;

	List<Announcement> getAnnouncements(AnnouncementFilter announcementFilter);

	Announcement getAnnouncement(String announcement_id);

	
	/**
	 * This service is use to create an announcement.
	 * 
	 * @param attachmentFile  multipart file object.
	 * @param announcementDTO request dto.
	 * @return object of announcement.
	 */
//	Announcement createAnnouncement(MultipartFile attachmentFile, AnnouncementDTO announcementDTO);

	/**
	 * This service assign user to the announcement.
	 * 
	 * @param announcementId unique id of announcement
	 * @param usersId        set of unique id of users.
	 * @return object of announcement.
	 */
//	Announcement assignUserToAnnouncement(UUID announcementId, Set<UUID> usersId);

	/**
	 * This service find the announcement by id.
	 * 
	 * @param announcementId unique id of announcement.
	 * @return object of announcement.
	 */
//	Announcement getAnnouncementById(UUID announcementId);

	/**
	 * This service search the announcement by given criteria.
	 * 
	 * @param announcementDTO request obj.
	 * @return pageable list of announcement.
	 */
//	Page<Announcement> getAllAnnouncements(AnnouncementDTO announcementDTO);

	/**
	 * This service search the announcement's user by given criteria.
	 * 
	 * @param announcementId        unique id of the announcement.
	 * @param orderAndPaginationDTO filter object for criteria.
	 * @return pageable list of user.
	 */
//	Page<UserResponseDTO> getAnnouncementUsersById(UUID announcementId,
//			OrderAndPaginationDTO orderAndPaginationDTO);

	/**
	 * use to get the file by given name and path.
	 * 
	 * @param fileName file name.
	 * @param dirId    directory of file.
	 * @return object of resource.
	 */
//	public Resource getAnnouncemenAttachmentFile(String dirId, String fileName);

}
