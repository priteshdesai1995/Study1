/**
 * Copyright 2022 Brainvire - All rights reserved.
 */
package com.base.api.service.impl;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.base.api.dto.filter.AnnouncementFilter;
import com.base.api.dto.filter.FilterBase;
import com.base.api.entities.Announcement;
import com.base.api.entities.AnnouncementUser;
import com.base.api.entities.User;
import com.base.api.enums.UserStatus;
import com.base.api.gateway.util.Util;
import com.base.api.repository.AnnouncementRepository;
import com.base.api.repository.AnnouncementUserRepository;
import com.base.api.repository.UserRepository;
import com.base.api.service.AnnouncementService;
import com.base.api.service.FilesStorageService;

import lombok.extern.slf4j.Slf4j;

/**
 * This class implements AnnouncementService.
 * 
 * @author minesh_prajapati
 *
 */
@Slf4j
@Service
public class AnnouncementServiceImpl implements AnnouncementService {

	@Autowired
	private AnnouncementRepository announcementRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	AnnouncementUserRepository announcementUserRepository;

	@Autowired
	FilesStorageService filesStorageService;

	@PersistenceContext
	EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Override
	public String createAnnouncement(AnnouncementFilter announcementFilter) throws ParseException {
		
		Announcement announcement = new Announcement();
		
		StringBuilder title = new StringBuilder (announcementFilter.getTitle());	
		StringBuilder discription = new StringBuilder  (announcementFilter.getDescription()) ;
		
		 
		 title = title.deleteCharAt(title.length() - 1);
		 title = title.deleteCharAt(0);
		 String titleStr = title.toString().replace("<p>", "");
		 titleStr = titleStr.replace("</p>", "");
				 
		 discription = discription.deleteCharAt(discription.length()-1);
		 discription = discription.deleteCharAt(0);
		  String dis = discription.toString().replace("<p>", "");
		  dis = dis.replace("</p>", "");
		 
		 String[] titleKeyValuePairs = titleStr.split(",");
		 String[] discriptionKeyValuePairs = dis.split(",");
		 
		 Map<String,String> disMap = new HashMap<>();
		 Map<String,String> titleMap = new HashMap<>();
		 
		 for (String titlePair : titleKeyValuePairs) {
				String[] titleEntry = titlePair.split(":");				
				String titleKey = titleEntry[0].trim();
				titleKey = titleKey.substring(1,titleKey.length()-1);
				String titleVal = titleEntry[1].trim();
				titleVal = titleVal.substring(1, titleVal.length() - 3);
				titleMap.put(titleKey, titleVal);
			}

		 for (String disPair : discriptionKeyValuePairs) {
			String[] disEntry = disPair.split(":");
			String disKey = disEntry[0].trim();
			disKey = disKey.substring(1,disKey.length()-1);
			
			String disVal = disEntry[1].trim();
			disVal = disVal.substring(1, disVal.length() - 3);	
			disMap.put(disKey, disVal);
		}
		 
		 log.info("title map =>" + Arrays.asList(titleMap));
		 log.info("disMap => "+ Arrays.asList(disMap));
		
//		Map<String, Object> title = new GsonJsonParser().parseMap(announcementFilter.getTitle());
//		Map<String, Object> description = new GsonJsonParser().parseMap(announcementFilter.getDescription());

		announcement.setTitleEN(titleMap.get("en"));
		announcement.setTitleAR(titleMap.get("ar"));
		announcement.setDescriptionEN((String) disMap.get("en"));
		announcement.setDescriptionAR((String) disMap.get("ar"));
		announcement.setStatus(announcementFilter.getStatus() == null ? "Pending" : "In-Progress");
		announcement.setType(announcementFilter.getType());
		announcement.setTargetPlatform(announcementFilter.getUser_type());
		announcement.setAdvancedFilters(announcementFilter.getInclusion());
		
		announcement.setCreatedDate(LocalDateTime.now());
		log.info("satrt date => "+ announcementFilter.getRegistration_start_date());
		log.info("end date => "+ announcementFilter.getRegistration_end_date());
		announcement.setStart_date(announcementFilter.getRegistration_start_date());
		announcement.setEnd_date(announcementFilter.getRegistration_end_date());
	
		announcementRepository.save(announcement);
		
		log.info("announcement saved");
		
		if (announcementFilter.getUsers().size() != 0) {
			
			List<Object> userList = announcementFilter.getUsers();
			log.info("list size "+ userList.size());
			
			User testUser = new User("test user", "test username", "testuser@gamil.com", "male", LocalDate.now(),
					"123445", UserStatus.ACTIVE);
			log.info("line 1");
			
			for (Object obj : announcementFilter.getUsers()) {
				log.info("line 2");
				String userId = ((HashMap<String, Object>) obj).get("id").toString();
				Optional<User> user = userRepository.findById(UUID.fromString(userId));
				log.info("line 3");
				AnnouncementUser announcementUser = new AnnouncementUser();
				announcementUser.setAnnouncement(announcement);
				log.info("line 4");
				if(user.isPresent() && user != null ) {
					announcementUser.setUsers(user.get());
					log.info("line 5");
				}else {
					log.info("line 6");
					log.error("User is not available");
					announcementUser.setUsers(testUser);
				}
				announcementUserRepository.save(announcementUser);
				log.info("announcement user saved ");
			}
		}
		
		return HttpStatus.OK.name();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Announcement> getAnnouncements(AnnouncementFilter filter) {
		
		StringBuilder query = new StringBuilder();
		query = createCommonQuery();

		if (filter.getStatus() != null && !filter.getStatus().isEmpty()) {
			query.append(" u.status LIKE '" + filter.getStatus() + "' ");
		}
		if (filter.getTitle() != null && !filter.getTitle().isEmpty()) {
			if (query.indexOf("where") != (query.length() - 5))
				query.append(" and ");
			query.append(" u.titleEN LIKE '%" + filter.getTitle() + "%'");
			System.out.println(filter.getTitle());
		}
		if (filter.getUser_type() != null && !filter.getUser_type().isEmpty() && !filter.getUser_type().equals("all")) {
			if (query.indexOf("where") != (query.length() - 5))
				query.append(" and ");
			query.append(" u.targetPlatform LIKE '%" + filter.getUser_type() + "%'");
		}
		if (filter.getType() != null && !filter.getType().isEmpty()) {
			if (query.indexOf("where") != (query.length() - 5))
				query.append(" and ");
			query.append(" u.type LIKE '%" + filter.getType() + "%'");
		}
		if (filter.getRegistration_start_date() != null && !filter.getRegistration_start_date().isEmpty()
				&& filter.getRegistration_end_date() != null && !filter.getRegistration_end_date().isEmpty()) {
			if (query.indexOf("where") != (query.length() - 5))
				query.append(" and ");
			query.append(" u.start_date ='" + filter.getRegistration_start_date() + "' and u.end_date ='"
					+ filter.getRegistration_end_date() + "'");
		}

		if (query.indexOf("where") == (query.length() - 5))
			query = new StringBuilder(query.substring(0, query.indexOf("where")));

		FilterBase filterBase = new FilterBase();
		filterBase.setColumnName(filter.getSort_param());
		filterBase.setStartRec(filter.getStartRec());
		filterBase.setEndRec(filter.getEndRec());
		filterBase.setSortingOrder(filter.getSort_type());
		filterBase.setOrder(filter.getOrder());
		String queryParam = Util.getFilterQuery(filterBase, query.toString());
		List<Announcement> results = new ArrayList<Announcement>();
		results = entityManager.createQuery(queryParam).setFirstResult(Integer.valueOf(filter.getStartRec()))
				.setMaxResults(Integer.valueOf(filter.getEndRec())).getResultList();
		return results;
	}

	@Override
	public Announcement getAnnouncement(String announcement_id) {
		Optional<Announcement> announcementOption = announcementRepository.findById(UUID.fromString(announcement_id));
		if(announcementOption.isPresent()) {
			return announcementOption.get();
		}else {
			return null;
		}
	}
	
	private StringBuilder createCommonQuery() {
		StringBuilder query = new StringBuilder();
		query.append("select u from Announcement u where");
		return query;
	}

//	@Override
//	@Transactional
//	public Announcement createAnnouncement(MultipartFile attachmentFile,
//			AnnouncementDTO announcementDTO) {
//		log.info("AnnouncementServiceImpl: Start createAnnouncement");
//		String fileNamePath = null;
//		if (announcementDTO.getType().equals(TemplateType.EMAIL) && !Util.isEmpty(attachmentFile)) {
//			String id = String.valueOf(System.currentTimeMillis());
//			String attachmentDirPath = ANNOUNCEMENT_FILE_DIR + FORWARD_SLASH + id;
//			filesStorageService.init(attachmentDirPath);
//			filesStorageService.save(attachmentFile, attachmentDirPath,
//					attachmentFile.getOriginalFilename());
//			fileNamePath = StringUtil
//					.removeWhiteSpace(id + FORWARD_SLASH + attachmentFile.getOriginalFilename());
//		}
//		Announcement announcement = new Announcement(announcementDTO.getTitle(),
//				announcementDTO.getType(), announcementDTO.getDescription(), Status.PENDING,
//				announcementDTO.getPlatform(), fileNamePath, announcementDTO.getInclusion(),
//				announcementDTO.getStartDate(), announcementDTO.getEndDate());
//		log.info("AnnouncementServiceImpl: End createAnnouncement");
//		announcement = announcementRepository.save(announcement);
//		if (!Util.isEmpty(announcementDTO.getUsersId())) {
//			assignUserToAnnouncement(announcement.getId(), announcementDTO.getUsersId());
//		}
//
//		log.info("AnnouncementServiceImpl: End createAnnouncement");
//		return announcement;
//	}
//
//	@Override
//	@Transactional
//	public Announcement assignUserToAnnouncement(UUID announcementId, Set<UUID> usersId) {
//		log.info("AnnouncementServiceImpl: Start assignUserToAnnouncement {}", announcementId);
//		Announcement announcement = getAnnouncementById(announcementId);
//		List<User> users = userRepository.findAllById(usersId);// change it from user services
//		for (User user : users) {
//			AnnouncementUser announcementUser = new AnnouncementUser(announcement, user);
//			announcementUserRepository.save(announcementUser);
//		}
//		log.info("AnnouncementServiceImpl: End assignUserToAnnouncement {}", announcementId);
//		return announcement;
//	}
//
//	@Override
//	public Announcement getAnnouncementById(UUID announcementId) {
//		log.info("AnnouncementServiceImpl: Start getAnnouncementById {}", announcementId);
//		Announcement announcement = announcementRepository.findById(announcementId)
//				.orElseThrow(() -> {
//					log.error("AnnouncementServiceImpl: no found {}", announcementId);
//					throw new APIException("announcement.not.found", HttpStatus.NOT_FOUND);
//				});
//		log.info("AnnouncementServiceImpl: End getAnnouncementById {}", announcementId);
//		return announcement;
//	}
//
//	@Override
//	public Resource getAnnouncemenAttachmentFile(String dirId, String fileName) {
//		log.info("AnnouncementServiceImpl: Start getAnnouncemenAttachmentFile {} {}", dirId,
//				fileName);
//		Resource file = filesStorageService.load(ANNOUNCEMENT_FILE_DIR + FORWARD_SLASH + dirId,
//				fileName);
//		log.info("AnnouncementServiceImpl: End getAnnouncemenAttachmentFile {} {}", dirId,
//				fileName);
//		return file;
//	}
//
//	@Override
//	public Page<Announcement> getAllAnnouncements(AnnouncementDTO announcementDTO) {
//		log.info("AnnouncementServiceImpl: Start getAllAnnouncements");
//		Page<Announcement> announcements = announcementUserRepository
//				.getAllAnnouncementsWithFilters(announcementDTO);
//		if (Util.isEmpty(announcements.getContent())) {
//			log.error("AnnouncementServiceImpl: getAllAnnouncements no found");
//			throw new APIException("announcement.not.found", HttpStatus.NOT_FOUND);
//		}
//		log.info("AnnouncementServiceImpl: End getAllAnnouncements");
//		return announcements;
//
//	}
//
//	@Override
//	public Page<UserResponseDTO> getAnnouncementUsersById(UUID announcementId,
//			OrderAndPaginationDTO orderAndPaginationDTO) {
//		log.info("AnnouncementServiceImpl: Start getAnnouncementUsersById {}", announcementId);
//		Page<UserResponseDTO> announcementUsers = announcementUserRepository
//				.getAnnouncementsUsersWithFiltersById(announcementId, orderAndPaginationDTO);
//		if (Util.isEmpty(announcementUsers.getContent())) {
//			log.error("AnnouncementServiceImpl: getAnnouncementUsersById not found {}",
//					announcementId);
//			throw new APIException("announcement.users.not.found", HttpStatus.NOT_FOUND);
//		}
//		log.info("AnnouncementServiceImpl: End getAnnouncementUsersById {}", announcementId);
//		return announcementUsers;
//	}

}
