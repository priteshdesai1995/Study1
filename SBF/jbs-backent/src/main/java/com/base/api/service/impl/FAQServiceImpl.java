package com.base.api.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.base.api.entities.FAQEntity;
import com.base.api.entities.FAQTopicEntity;
import com.base.api.enums.UserStatus;
import com.base.api.repository.FAQRepository;
import com.base.api.repository.FAQTopicRepository;
import com.base.api.request.dto.FAQDTO;
import com.base.api.response.dto.FAQTopicDTO;
import com.base.api.service.FAQService;
import com.base.api.utils.Util;

import lombok.extern.slf4j.Slf4j;

/**
 * Copyright 2022 Brainvire - All rights reserved.
 */
@Service
@Slf4j
public class FAQServiceImpl implements FAQService {

	@Autowired
	FAQRepository faqRepository;

	@Autowired
	FAQTopicRepository faqTopicRepository;

	/**
	 * Gets the FAQ list.
	 *
	 * @return the FAQ list
	 */
	@Override
	public List<FAQDTO> getFAQList() {
		List<FAQEntity> entities = faqRepository.findAllByStatus(UserStatus.ACTIVE.getStatus());
		List<FAQEntity> Inentities = faqRepository.findAllByStatus(UserStatus.INACTIVE.getStatus());
		List<FAQDTO> dtos = new ArrayList<FAQDTO>();
		entities.forEach((entity) -> {
			FAQDTO dto = new FAQDTO();
			dto = mapEntityToDTO(entity);
			dtos.add(dto);
		});
		Inentities.forEach((entity) -> {
			FAQDTO dto = new FAQDTO();
			dto = mapEntityToDTO(entity);
			dtos.add(dto);
		});
		return dtos;
	}

	/**
	 * Map entity to DTO.
	 *
	 * @param entity the entity
	 * @return the faqdto
	 */
	private FAQDTO mapEntityToDTO(FAQEntity entity) {
		FAQDTO dto = new FAQDTO();
		dto.setAnswer(entity.getAnswer());
		dto.setId(entity.getId());
		dto.setLocale(entity.getLocale());
		dto.setQuestion(entity.getQuestion());
		dto.setStatus(entity.getStatus());
		dto.setFaq_topic_id(entity.getFaqTopicEntity().getId());
		dto.setTopic_name(entity.getFaqTopicEntity().getTopicName());
		dto.setCreated_at(entity.getCreatedAt());
		return dto;
	}

	/**
	 * Creates the FAQ.
	 *
	 * @param faqdto the faqdto
	 * @return the string
	 */
	@Override
	public String createFAQ(FAQDTO faqdto) {
		try {
			FAQEntity entity = new FAQEntity();
			entity.setAnswer(faqdto.getAnswer());
			entity.setCreatedAt(Util.getCurrentUtcTime());
			Optional<FAQTopicEntity> faqTopicEntity = Optional.empty();

			log.info("faqdto is : " + faqdto.toString());

			faqTopicEntity = faqTopicRepository.findById(faqdto.getFaq_topic_id());
			faqTopicRepository.save(faqTopicEntity.get());
			entity.setFaqTopicEntity(faqTopicEntity.get());
			entity.setLocale("en");
			entity.setQuestion(faqdto.getQuestion());
			entity.setStatus(faqdto.getStatus());
			faqRepository.save(entity);
			return HttpStatus.OK.name();
		} catch (DataIntegrityViolationException e) {
			return HttpStatus.INTERNAL_SERVER_ERROR.name();
		}
	}

	/**
	 * Gets the FAQ topic list.
	 *
	 * @return the FAQ topic list
	 */
	@Override
	public List<FAQTopicDTO> getFAQTopicList() {
		List<FAQTopicEntity> entities = new ArrayList<FAQTopicEntity>();
		entities = faqTopicRepository.findAll();
		List<FAQTopicDTO> faqdtos = new ArrayList<FAQTopicDTO>();
		for (FAQTopicEntity faqTopicEntity : entities) {
			FAQTopicDTO faqdto = new FAQTopicDTO();
			faqdto.setId(faqTopicEntity.getId());
			faqdto.setTopic_name(faqTopicEntity.getTopicName());
			faqdtos.add(faqdto);
		}
		return faqdtos;
	}

	/**
	 * Change status.
	 *
	 * @param id the id
	 * @param status the status
	 * @return the string
	 */
	@Override
	public String changeStatus(UUID id, String status) {
		Optional<FAQEntity> parent = faqRepository.findById(id);

		if (parent.get() != null) {
			parent.get().setStatus(status);
			faqRepository.save(parent.get());
			return HttpStatus.OK.name();
		}

		return HttpStatus.NOT_FOUND.name();
	}

	/**
	 * Gets the faq.
	 *
	 * @param faqId the faq id
	 * @return the faq
	 */
	@Override
	public FAQDTO getFAQ(UUID faqId) {

		log.info("get FAQ called" + faqId);

		Optional<FAQEntity> parent = faqRepository.findById(faqId);

		log.info("parent is === > " + parent.toString());

		if (parent.get() != null) {
			FAQDTO faqdto = new FAQDTO();
			faqdto = mapEntityToDTO(parent.get());
			return faqdto;
		}
		return null;
	}

	/**
	 * Update.
	 *
	 * @param faqdto the faqdto
	 * @param faqId the faq id
	 * @return the string
	 */
	@Override
	public String update(FAQDTO faqdto, UUID faqId) {
		try {
			Optional<FAQEntity> parent = faqRepository.findById(faqId);
			if (parent.get() != null) {
				parent.get().setAnswer(faqdto.getAnswer());
				parent.get().setQuestion(faqdto.getQuestion());
				parent.get().setStatus(faqdto.getStatus());
				Optional<FAQTopicEntity> faqTopicEntity = Optional.empty();
				faqTopicEntity = faqTopicRepository.findById(faqdto.getFaq_topic_id());
				parent.get().setFaqTopicEntity(faqTopicEntity.get());
				faqRepository.save(parent.get());
				return HttpStatus.OK.name();
			}
			return HttpStatus.NOT_FOUND.name();
		} catch (DataIntegrityViolationException e) {
			return HttpStatus.INTERNAL_SERVER_ERROR.name();
		}
	}

	/**
	 * Delete FAQ.
	 *
	 * @param faqId the faq id
	 * @return the string
	 */
	@Override
	public String deleteFAQ(UUID faqId) {
		try {

			Optional<FAQEntity> parent = faqRepository.findById(faqId);

			if (parent.get() != null) {
				faqRepository.deleteById(faqId);
				return HttpStatus.OK.name();
			}
			return HttpStatus.NOT_FOUND.name();
		} catch (DataIntegrityViolationException e) {
			return HttpStatus.INTERNAL_SERVER_ERROR.name();
		}
	}

}
