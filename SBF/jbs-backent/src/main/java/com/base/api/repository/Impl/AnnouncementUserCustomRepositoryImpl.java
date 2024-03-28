/**
 * Copyright 2022 Brainvire - All rights reserved.
 */
package com.base.api.repository.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.base.api.entities.Announcement;
import com.base.api.entities.AnnouncementUser;
import com.base.api.repository.AnnouncementUserCustomRepository;
import com.base.api.request.dto.AnnouncementDTO;
import com.base.api.request.dto.OrderAndPaginationDTO;
import com.base.api.response.dto.UserResponseDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * This class implements AnnouncementUserCustomRepository.
 * 
 * @author minesh_prajapati
 *
 */
@Slf4j
@Repository
public class AnnouncementUserCustomRepositoryImpl implements AnnouncementUserCustomRepository {

	@PersistenceContext
	private EntityManager entityManager;

	private CriteriaBuilder criteriaBuilder;

	public AnnouncementUserCustomRepositoryImpl(EntityManager entityManager) {
		this.criteriaBuilder = entityManager.getCriteriaBuilder();
	}

	@Override
	public Page<Announcement> getAllAnnouncementsWithFilters(AnnouncementDTO announcementDTO) {
		log.info("AnnouncementUserCustomRepositoryImpl: Start getAllAnnouncementsWithFilters");
		Page<Announcement> announcements = findAllAnnouncementWithFilters(null, announcementDTO);
		log.info("AnnouncementUserCustomRepositoryImpl: End getCustomAllAnnouncements");
		return announcements;
	}

	@Override
	public Page<UserResponseDTO> getAnnouncementsUsersWithFiltersById(UUID announcementId,
			OrderAndPaginationDTO orderAndPaginationDTO) {
		log.info(
				"AnnouncementUserCustomRepositoryImpl: Start getCustomAnnouncementUsersByAnnouncementId {}",
				announcementId);
		Page<UserResponseDTO> result = findAllAnnouncementUsersWithFilters(announcementId,
				orderAndPaginationDTO);
		log.info(
				"AnnouncementUserCustomRepositoryImpl: End getCustomAnnouncementUsersByAnnouncementId {}",
				announcementId);
		return result;
	}

	private Page<Announcement> findAllAnnouncementWithFilters(Announcement announcement,
			AnnouncementDTO announcementDTO) {
		CriteriaQuery<Announcement> criteriaQuery = criteriaBuilder.createQuery(Announcement.class);
		Root<Announcement> announcementRoot = criteriaQuery.from(Announcement.class);

		Predicate predicate = getPredicateAnnouncement(announcementDTO, announcementRoot);
		criteriaQuery.where(predicate);

		setOrderAnnouncement(announcementDTO, criteriaQuery, announcementRoot);

		TypedQuery<Announcement> typedQuery = entityManager.createQuery(criteriaQuery);
		typedQuery.setFirstResult(announcementDTO.getPageNumber() * announcementDTO.getPageSize());
		typedQuery.setMaxResults(announcementDTO.getPageSize());

		Pageable pageable = getPageableAnnouncement(announcementDTO);

		long count = getCount(predicate);

		return new PageImpl<>(typedQuery.getResultList(), pageable, count);
	}

	private Predicate getPredicateAnnouncement(AnnouncementDTO announcementDTO,
			Root<Announcement> announcementRoot) {
		List<Predicate> predicates = new ArrayList<>();
		if (Objects.nonNull(announcementDTO.getTitle())) {
			predicates
					.add(criteriaBuilder.like(criteriaBuilder.lower(announcementRoot.get("title")),
							"%" + announcementDTO.getTitle().toLowerCase() + "%"));
		}
		if (Objects.nonNull(announcementDTO.getStatus())) {
			predicates.add(criteriaBuilder.equal(announcementRoot.get("status"),
					announcementDTO.getStatus()));
		}
		if (Objects.nonNull(announcementDTO.getStartDate())) {
			predicates.add(criteriaBuilder.greaterThanOrEqualTo(announcementRoot.get("startDate"),
					announcementDTO.getStartDate()));
		}
		if (Objects.nonNull(announcementDTO.getEndDate())) {
			predicates.add(criteriaBuilder.lessThanOrEqualTo(announcementRoot.get("endDate"),
					announcementDTO.getEndDate()));
		}
		if (Objects.nonNull(announcementDTO.getType())) {
			predicates.add(
					criteriaBuilder.equal(announcementRoot.get("type"), announcementDTO.getType()));
		}
//		if (Objects.nonNull(announcementDTO.getPlatform())) {
//			predicates.add(criteriaBuilder.equal(announcementRoot.get("targetPlatform"),
//					announcementDTO.getPlatform()));
//		}

		return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
	}

	private void setOrderAnnouncement(AnnouncementDTO announcementPage,
			CriteriaQuery<Announcement> criteriaQuery, Root<Announcement> announcementRoot) {
		if (announcementPage.getOrderType().equals(Sort.Direction.ASC)) {
			criteriaQuery.orderBy(
					criteriaBuilder.asc(announcementRoot.get(announcementPage.getOrderBy())));
		} else {
			criteriaQuery.orderBy(
					criteriaBuilder.desc(announcementRoot.get(announcementPage.getOrderBy())));
		}
	}

	private Pageable getPageableAnnouncement(AnnouncementDTO announcementDTO) {
		Sort sort = Sort.by(announcementDTO.getOrderType(), announcementDTO.getOrderBy());
		return PageRequest.of(announcementDTO.getPageNumber(), announcementDTO.getPageSize(), sort);
	}

	private long getCount(Predicate predicate) {
		CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
		Root<Announcement> countRoot = countQuery.from(Announcement.class);
		countQuery.select(criteriaBuilder.count(countRoot)).where(predicate);
		return entityManager.createQuery(countQuery).getSingleResult();
	}

	private Page<UserResponseDTO> findAllAnnouncementUsersWithFilters(UUID announcementId,
			OrderAndPaginationDTO orderAndPaginationDTO) {

		CriteriaQuery<UserResponseDTO> criteriaQuery = criteriaBuilder
				.createQuery(UserResponseDTO.class);
		Root<AnnouncementUser> announcementUserRoot = criteriaQuery.from(AnnouncementUser.class);
		criteriaQuery.multiselect(announcementUserRoot.get("users").get("userName"),
				announcementUserRoot.get("status"),
				announcementUserRoot.get("users").get("userProfile").get("email"),
				announcementUserRoot.get("users").get("userRole").get("roleName"));

		List<Predicate> predicates = new ArrayList<>();
		if (Objects.nonNull(announcementId)) {
			predicates.add(criteriaBuilder.equal(announcementUserRoot.get("announcement").get("id"),
					announcementId));
		}
		if (Objects.nonNull(orderAndPaginationDTO.getCommonSearch())) {
			predicates.add(criteriaBuilder.or(
					criteriaBuilder.like(announcementUserRoot.get("users").get("userName"),
							"%" + orderAndPaginationDTO.getCommonSearch() + "%"),
					criteriaBuilder.like(
							announcementUserRoot.get("users").get("userProfile").get("email"),
							"%" + orderAndPaginationDTO.getCommonSearch() + "%")));
			// predicates.add(criteriaBuilder.like(announcementUserRoot.get("users").get("status"),
			/// "%" + orderAndPaginationDTO.getCommonSearch() + "%"));

			// predicates.add(criteriaBuilder.like(
			// announcementUserRoot.get("users").get("userRole").get("roleName"),
			// "%" + orderAndPaginationDTO.getCommonSearch() + "%"));
		}
		Predicate predicate = criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		criteriaQuery.where(predicate);

		if (orderAndPaginationDTO.getOrderType().equals(Sort.Direction.ASC)) {
			criteriaQuery.orderBy(criteriaBuilder
					.asc(announcementUserRoot.get(orderAndPaginationDTO.getOrderBy())));
		} else {
			criteriaQuery.orderBy(criteriaBuilder
					.desc(announcementUserRoot.get(orderAndPaginationDTO.getOrderBy())));
		}

		TypedQuery<UserResponseDTO> userResponseTypedQuery = entityManager
				.createQuery(criteriaQuery);
		userResponseTypedQuery.setFirstResult(
				orderAndPaginationDTO.getPageNumber() * orderAndPaginationDTO.getPageSize());
		userResponseTypedQuery.setMaxResults(orderAndPaginationDTO.getPageSize());

		Sort sort = Sort.by(orderAndPaginationDTO.getOrderType(),
				orderAndPaginationDTO.getOrderBy());

		Pageable pageable = PageRequest.of(orderAndPaginationDTO.getPageNumber(),
				orderAndPaginationDTO.getPageSize());

		CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
		Root<AnnouncementUser> countRoot = countQuery.from(AnnouncementUser.class);
		countQuery.select(criteriaBuilder.count(announcementUserRoot)).where(predicate);
		long count = entityManager.createQuery(countQuery).getSingleResult();

		return new PageImpl<>(userResponseTypedQuery.getResultList(), pageable, count);
	}

}
