package com.base.api.service;

import java.util.List;
import java.util.UUID;

import com.base.api.request.dto.FAQDTO;
import com.base.api.response.dto.FAQTopicDTO;

/**
 * Copyright 2022 Brainvire - All rights reserved.
 */
public interface FAQService {

	List<FAQDTO> getFAQList();

	String createFAQ(FAQDTO entity);

	List<FAQTopicDTO> getFAQTopicList();

	String changeStatus(UUID id, String status);

	FAQDTO getFAQ(UUID faqId);

	String update(FAQDTO faqdto, UUID id);

	String deleteFAQ(UUID faqId);
}
